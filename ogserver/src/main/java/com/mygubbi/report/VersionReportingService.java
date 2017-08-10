package com.mygubbi.report;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.ModuleForPrice;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.game.proposal.model.dw.DWProductModule;
import com.mygubbi.game.proposal.model.dw.DWProposalAddon;
import com.mygubbi.game.proposal.model.dw.DWProposalProduct;
import com.mygubbi.game.proposal.model.dw.DwProposal;
import com.mygubbi.game.proposal.price.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 04-08-2017.
 */
public class VersionReportingService extends AbstractVerticle {

    private final static Logger LOG = LogManager.getLogger(VersionReportingService.class);
    public static final String RECORD_VERSION_PRICE = "update.proposal.version.price";

    List<QueryData> queryDatasForModule = new ArrayList<>();
    List<QueryData> queryDatasForProduct = new ArrayList<>();
    List<QueryData> queryDatasForAddon = new ArrayList<>();
    QueryData queryDataForVersion = new QueryData();

    List<ProductPriceHolder> versionProductPriceHolders = new ArrayList<>();
    List<AddonPriceHolder> versionAddonPriceHolders = new ArrayList<>();

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        this.setupPriceUpdater();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    private void setupPriceUpdater() {
        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(RECORD_VERSION_PRICE, (Message<Integer> message) ->
        {
            ProposalVersion proposalVersion = (ProposalVersion) LocalCache.getInstance().remove(message.body());
            this.getProposalHeader(message, proposalVersion);
        }).completionHandler(res -> {
            LOG.info("Proposal version price update service started." + res.succeeded());
        });
    }

    private void getProposalHeader(Message message, ProposalVersion proposalVersion) {
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.header", new JsonObject().put("id", proposalVersion.getProposalId())));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty()) {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal not found for id:" + proposalVersion.getProposalId())));
                        LOG.error("Proposal not found for id:" + proposalVersion.getProposalId());
                    } else {
                        ProposalHeader proposalHeader = new ProposalHeader(resultData.rows.get(0));
                        updatePriceForProduct(message, proposalHeader, proposalVersion);
                        updatePriceForAddon(message, proposalHeader, proposalVersion);
                        updatePriceForVersion(message, versionProductPriceHolders, versionAddonPriceHolders, proposalHeader, proposalVersion);
                    }
                });

    }

    private void updatePriceForVersion(Message message,List<ProductPriceHolder> versionProductPriceHolders, List<AddonPriceHolder> versionAddonPriceHolders, ProposalHeader proposalHeader, ProposalVersion proposalVersion) {

        VersionPriceHolder versionPriceHolder = new VersionPriceHolder(proposalHeader,versionProductPriceHolders,versionAddonPriceHolders,proposalVersion);
        DwProposal dwProposal = setVersionAttributes(proposalHeader,proposalVersion,versionPriceHolder);
        insertRowToTable(dwProposal,"dw_proposal.insert",message);
    }

private void insertRowToTable(JsonObject obj,String queryId,Message message){
    Integer id = LocalCache.getInstance().store(new QueryData(queryId, obj));
    VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
            (AsyncResult<Message<Integer>> selectResult) -> {
                QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                {
                    message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Error in Executing")));
                    LOG.error("Error in Executing");
                }
                else
                {
                    message.reply(LocalCache.getInstance().store(new JsonObject().put("success", "Successfully inserted")));
                    LOG.info("Successfully inserted");
                }
            });
}
private void insertRowsToTable(List<QueryData> queryDatas,Message message){
    Integer id = LocalCache.getInstance().store(queryDatas);
    VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id,
            (AsyncResult<Message<Integer>> selectResult) -> {
                QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                {
                    message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Error in Executing")));
                    LOG.error("Error in Executing");
                }
                else
                {
                    message.reply(LocalCache.getInstance().store(new JsonObject().put("success", "Successfully inserted")));
                    LOG.info("Successfully inserted");
                }
            });
}
    private void updatePriceForProduct(Message message, ProposalHeader proposalHeader, ProposalVersion proposalVersion) {
        List<ProductLineItem> productLineItems = new ArrayList<>();
        String query = "proposal.product.versionlist";
        Integer id = LocalCache.getInstance().store(new QueryData(query, proposalVersion));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0) {
                        LOG.error("Error in retrieving product line item in the proposal. " + resultData.errorMessage, resultData.error);
                    } else {
                        for (JsonObject record : resultData.rows) {
                            productLineItems.add((ProductLineItem) record);
                        }
                        calculateProductLevelPricing(message, proposalHeader, productLineItems, proposalVersion);
                    }
                });
    }

    private void updatePriceForAddon(Message message, ProposalHeader proposalHeader, ProposalVersion proposalVersion) {
        List<ProductAddon> addonLineItems = new ArrayList<>();
        String query = "proposal.addon.list";
        Integer id = LocalCache.getInstance().store(new QueryData(query, proposalVersion));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0) {
                        LOG.error("Error in retrieving line item in the proposal. " + resultData.errorMessage, resultData.error);
                    } else {
                        for (JsonObject record : resultData.rows) {
                            addonLineItems.add((ProductAddon) record);
                        }
                        calculateAddonLevelPricing(message, proposalHeader, addonLineItems, proposalVersion);
                    }
                });
    }


    private void calculateProductLevelPricing(Message message, ProposalHeader proposalHeader, List<ProductLineItem> productLineItems, ProposalVersion proposalVersion) {

        List<ModulePriceHolder> modulePriceHolders = new ArrayList<>();


        for (ProductLineItem productLineItem : productLineItems) {


            List<ProductModule> productModules = productLineItem.getModules();
            for (ProductModule productModule: productModules)
            {
                JsonObject jsonObject = new JsonObject();
                jsonObject.put("city",proposalHeader.getProjectCity());
                jsonObject.put("module",productModule);
                jsonObject.put("product",productLineItem);
                jsonObject.put("priceDate",proposalHeader.getPriceDate());
                ModuleForPrice moduleForPrice = new ModuleForPrice(jsonObject);
                ModulePriceHolder modulePriceHolder = null;
                DWProductModule dwProductModule = null;
                try {
                    modulePriceHolder = new ModulePriceHolder(moduleForPrice);
                    modulePriceHolder.prepare();
                    if (modulePriceHolder.hasErrors()) {
                        message.reply(LocalCache.getInstance().store(modulePriceHolder));
                        return;
                    }

                    modulePriceHolder.calculateTotalCost();
                    modulePriceHolders.add(modulePriceHolder);

                    dwProductModule = setModuleAttributes(modulePriceHolder,proposalHeader,productLineItem,proposalVersion,productModule);
                    queryDatasForModule.add(new QueryData("dw_product_module.insert",dwProductModule));
                    insertRowsToTable(queryDatasForModule,message);
                }

                catch (Exception e)
                {
                    message.reply(e.getMessage());
                }

                ProductPriceHolder productPriceHolder = new ProductPriceHolder(productLineItem,modulePriceHolders,proposalHeader,proposalVersion);
                this.versionProductPriceHolders.add(productPriceHolder);

                DWProposalProduct dwProposalProduct = setProductAttributes(productPriceHolder,proposalHeader,proposalVersion,productLineItem);
                queryDatasForProduct.add(new QueryData("dw_proposal_product.insert",dwProposalProduct));

                insertRowsToTable(queryDatasForProduct,message);

            }
        }

    }

    private void calculateAddonLevelPricing(Message message, ProposalHeader proposalHeader, List<ProductAddon> productAddons, ProposalVersion proposalVersion) {

        AddonPriceHolder addonPriceHolder = null;
        for (ProductAddon productAddon : productAddons) {

            try {
                addonPriceHolder = new AddonPriceHolder(productAddon, proposalHeader);
                addonPriceHolder.prepare();
                if (addonPriceHolder.hasErrors()) {
                    message.reply(LocalCache.getInstance().store(addonPriceHolder));
                    return;
                }
            }
            catch (Exception e)
            {
                message.reply(e.getMessage());
            }

            this.versionAddonPriceHolders.add(addonPriceHolder);

            DWProposalAddon dwProposalAddon = setAddonLevelAttributes(proposalHeader,proposalVersion,productAddon,addonPriceHolder);
            queryDatasForAddon.add(new QueryData("dw_proposal_addon.insert",dwProposalAddon));
            insertRowsToTable(queryDatasForAddon,message);
        }

    }

    private DWProposalAddon setAddonLevelAttributes(ProposalHeader proposalHeader, ProposalVersion proposalVersion, ProductAddon productAddon, AddonPriceHolder addonPriceHolder)
    {
        DWProposalAddon dwProposalAddon = new DWProposalAddon();

        dwProposalAddon.setProposalID(proposalHeader.getId());
        dwProposalAddon.setVersion(Double.parseDouble(proposalVersion.getVersion()));
        dwProposalAddon.setProposalTitle(proposalHeader.getQuotationFor());
        dwProposalAddon.setPriceDate(proposalHeader.getPriceDate());
        dwProposalAddon.setBusinessDate(proposalVersion.getDate());
        dwProposalAddon.setRegion(proposalHeader.getProjectCity());
        dwProposalAddon.setSpaceType(productAddon.getSpaceType());
        dwProposalAddon.setRoom(productAddon.getRoomCode());
        dwProposalAddon.setAddonId(productAddon.getId());
        dwProposalAddon.setCode(productAddon.getCode());
        dwProposalAddon.setCategory("BP");
        dwProposalAddon.setSubCategory(productAddon.getCategoryCode());
        dwProposalAddon.setProductTypeCode(productAddon.getProductTypeCode());
        dwProposalAddon.setProductSubTypeCode(productAddon.getProductSubtypeCode());
        dwProposalAddon.setProduct(productAddon.getProduct());
        dwProposalAddon.setBrandCode(productAddon.getBrandCode());
        dwProposalAddon.setCatalogueCode(productAddon.getCatalogueCode());
        dwProposalAddon.setUom(productAddon.getUom());
        dwProposalAddon.setQuantity(productAddon.getQuantity());
        dwProposalAddon.setUpdatedBy(productAddon.getUpdatedBy());
        dwProposalAddon.setUnitPrice(addonPriceHolder.getUnitPrice());
        dwProposalAddon.setUnitSourceCost(addonPriceHolder.getUnitSourceCost());
        dwProposalAddon.setPrice(addonPriceHolder.getPrice());
        dwProposalAddon.setPriceWoTax(addonPriceHolder.getPriceWoTax());
        dwProposalAddon.setSourceCost(addonPriceHolder.getSourceCost());
        dwProposalAddon.setProfit(addonPriceHolder.getProfit());
        dwProposalAddon.setMargin(addonPriceHolder.getMargin());

        return dwProposalAddon;
    }


    private DWProductModule setModuleAttributes(ModulePriceHolder modulePriceHolder, ProposalHeader proposalHeader, ProductLineItem productLineItem, ProposalVersion proposalVersion, ProductModule productModule)
    {
        DWProductModule dwProductModule = new DWProductModule();

        dwProductModule.setProposalId(proposalHeader.getId());
        dwProductModule.setVersion(Double.parseDouble(proposalVersion.getVersion()));
        dwProductModule.setProposalTitle(proposalHeader.getQuotationFor());
        dwProductModule.setPriceDate(proposalHeader.getPriceDate());
        dwProductModule.setBusinessDate(proposalVersion.getDate());
        dwProductModule.setRegion(proposalHeader.getProjectCity());
        dwProductModule.setSpaceType(productLineItem.getSpaceType());
        dwProductModule.setRoom(productLineItem.getRoomCode());
        dwProductModule.setPrId(productLineItem.getId());
        dwProductModule.setPrTitle(productLineItem.getTitle());
        dwProductModule.setModuleSeq(productModule.getModuleSequence());
        dwProductModule.setModuleCode(productModule.getMGCode());
        dwProductModule.setDescription(productModule.getDescription());
        dwProductModule.setWidth(productModule.getWidth());
        dwProductModule.setHeight(productModule.getHeight());
        dwProductModule.setDepth(productModule.getDepth());
        dwProductModule.setModuleCategory(productModule.getModuleCategory());
        dwProductModule.setHandleSize(Double.parseDouble(productModule.getHandleThickness()));
        dwProductModule.setHandleQty(productModule.getHandleQuantity());
        dwProductModule.setCarcass(productModule.getCarcassCode());
        dwProductModule.setFinish(productModule.getFinishCode());
        dwProductModule.setFinishMaterial(productModule.getFinishType());
        dwProductModule.setColor(productModule.getColorCode());
        dwProductModule.setExposedLeft(productModule.getLeftExposed());
        dwProductModule.setExposedRight(productModule.getRightExposed());
        dwProductModule.setExposedBottom(productModule.getBottomExposed());
        dwProductModule.setExposedTop(productModule.getTopExposed());
        dwProductModule.setExposedBack(productModule.getBackExposed());
        dwProductModule.setExposedOpen(productModule.getOpenUnit());
        dwProductModule.setNoOfAccPacks(modulePriceHolder.getNoOfAccPacks());
        dwProductModule.setModuleArea(productModule.getAreaOfModuleInSft());
        dwProductModule.setCarcassPrice(modulePriceHolder.getCarcassCost());
        dwProductModule.setCarcassWoTax(modulePriceHolder.getCarcassCostWoTax());
        dwProductModule.setCarcassCost(modulePriceHolder.getCarcassSourceCost());
        dwProductModule.setCarcassProfit(modulePriceHolder.getCarcassProfit());
        dwProductModule.setCarcassMargin(modulePriceHolder.getCarcassMargin());
        dwProductModule.setShutterPrice(modulePriceHolder.getShutterCost());
        dwProductModule.setShutterWOTax(modulePriceHolder.getShutterCostWoTax());
        dwProductModule.setShutterCost(modulePriceHolder.getShutterSourceCost());
        dwProductModule.setShutterProfit(modulePriceHolder.getShutterProfit());
        dwProductModule.setShutterMargin(modulePriceHolder.getShutterMargin());
        dwProductModule.setLabourPrice(modulePriceHolder.getLabourCost());
        dwProductModule.setLabourPriceWoTax(modulePriceHolder.getLabourCostWoTax());
        dwProductModule.setLabourCost(modulePriceHolder.getLabourSourceCost());
        dwProductModule.setLabourProfit(modulePriceHolder.getLabourProfit());
        dwProductModule.setLabourMargin(modulePriceHolder.getLabourMargin());
        dwProductModule.setHandlePrice(modulePriceHolder.getHandleandKnobCost());
        dwProductModule.setHandlePriceWoTax(modulePriceHolder.getHandleandKnobCostWoTax());
        dwProductModule.setHandleCost(modulePriceHolder.getHandleandKnobSourceCost());
        dwProductModule.setHandleProfit(modulePriceHolder.getHandleandKnobProfit());
        dwProductModule.setHandleMargin(modulePriceHolder.getHandleandKnobMargin());
        dwProductModule.setHingePrice(modulePriceHolder.getHingeCost());
        dwProductModule.setHingeWoTax(modulePriceHolder.getHingeCostWoTax());
        dwProductModule.setHandleCost(modulePriceHolder.getHingeSourceCost());
        dwProductModule.setHingeProfit(modulePriceHolder.getHingeProfit());
        dwProductModule.setHingeMargin(modulePriceHolder.getHingeMargin());
        dwProductModule.setHardwarePrice(modulePriceHolder.getHardwareCost());
        dwProductModule.setHardwareWoTax(modulePriceHolder.getHardwareCostWoTax());
        dwProductModule.setHardwareCost(modulePriceHolder.getHardwareSourceCost());
        dwProductModule.setHardwareProfit(modulePriceHolder.getHardwareProfit());
        dwProductModule.setHardwareMargin(modulePriceHolder.getHardwareMargin());
        dwProductModule.setAccessoryPrice(modulePriceHolder.getAccessoryCost());
        dwProductModule.setAccessoryPriceWoTax(modulePriceHolder.getAccessoryCostWoTax());
        dwProductModule.setAccessoryCost(modulePriceHolder.getAccessorySourceCost());
        dwProductModule.setAccessoryProfit(modulePriceHolder.getAccessoryProfit());
        dwProductModule.setAccessoryMargin(modulePriceHolder.getAccessoryMargin());
        dwProductModule.setModulePrice(modulePriceHolder.getTotalCost());
        dwProductModule.setModulePriceWoTax(modulePriceHolder.getTotalCostWoTax());
        dwProductModule.setModuleCost(modulePriceHolder.getTotalSourceCost());
        dwProductModule.setModuleProfit(modulePriceHolder.getTotalProfit());
        dwProductModule.setModuleMargin(modulePriceHolder.getTotalMargin());

        return dwProductModule;
    }

    private DWProposalProduct setProductAttributes(ProductPriceHolder productPriceHolder, ProposalHeader proposalHeader, ProposalVersion proposalVersion, ProductLineItem productLineItem)
    {
        DWProposalProduct dwProposalProduct= new DWProposalProduct();

        dwProposalProduct.setProposalId(proposalHeader.getId());
        dwProposalProduct.setVersion(Double.parseDouble(proposalVersion.getVersion()));
        dwProposalProduct.setProposalTitle(proposalHeader.getQuotationFor());
        dwProposalProduct.setPriceDate(proposalHeader.getPriceDate());
        dwProposalProduct.setBusinessDate(proposalVersion.getDate());
        dwProposalProduct.setRegion(proposalHeader.getProjectCity());
        dwProposalProduct.setCategory("Modular Products");
        dwProposalProduct.setSubCategory(productLineItem.getProductCategory());
        dwProposalProduct.setProductId(productLineItem.getId());
        dwProposalProduct.setProductTitle(productLineItem.getTitle());
        dwProposalProduct.setSpaceType(productLineItem.getSpaceType());
        dwProposalProduct.setRoom(productLineItem.getRoomCode());
        dwProposalProduct.setBaseCarcass(productLineItem.getBaseCarcassCode());
        dwProposalProduct.setWallCarcass(productLineItem.getWallCarcassCode());
        dwProposalProduct.setFinish(productLineItem.getFinishCode());
        dwProposalProduct.setFinishMaterial(productLineItem.getFinishType());
        dwProposalProduct.setShutterDesign(productLineItem.getShutterDesignCode());
        dwProposalProduct.setHinge(productLineItem.getHingeType());
        dwProposalProduct.setGlass(productLineItem.getGlass());
        dwProposalProduct.setHandleSelection(productLineItem.getHandletypeSelection());
        dwProposalProduct.setNoOfLengths(productLineItem.getNoOfLengths());
        dwProposalProduct.setHandleType(productLineItem.getHandleType());
        dwProposalProduct.setHandleFinish(productLineItem.getHandleFinish());
        dwProposalProduct.setHandleSize(productLineItem.getHandleThickness());
        dwProposalProduct.setKnobType(productLineItem.getKnobType());
        dwProposalProduct.setKnobFinish(productLineItem.getKnobFinish());
        dwProposalProduct.setProductArea(productPriceHolder.getProductAreainSqft());
        dwProposalProduct.setProductCreatedBy(productLineItem.getCreatedBy());
        dwProposalProduct.setProductCreatedOn(productLineItem.getCreatedOn());
        dwProposalProduct.setProductUpdatedBy(productLineItem.getUpdatedBy());
        dwProposalProduct.setProductUpdatedOn(productLineItem.getUpdatedOn());
        dwProposalProduct.setProductPrice(productPriceHolder.getProductPrice());
        dwProposalProduct.setProductPriceAfterDiscount(productPriceHolder.getPriceAfterDiscount());
        dwProposalProduct.setProductPriceAfterTax(productPriceHolder.getProductPriceWoTax());
        dwProposalProduct.setProductSourceCost(productPriceHolder.getProductSourceCost());
        dwProposalProduct.setProductProfit(productPriceHolder.getProductProfit());
        dwProposalProduct.setProductMargin(productPriceHolder.getProductMargin());
        dwProposalProduct.setWoodWorkPrice(productPriceHolder.getWoodWorkPrice());
        dwProposalProduct.setWoodWorkPriceWoTax(productPriceHolder.getWoodWorkPriceWoTax());
        dwProposalProduct.setWoodWorkCost(productPriceHolder.getWoodWorkSourceCost());
        dwProposalProduct.setWoodWorkProfit(productPriceHolder.getWoodWorkProfit());
        dwProposalProduct.setWoodWorkMargin(productPriceHolder.getWoodWorkMargin());
        dwProposalProduct.setHwPrice(productPriceHolder.getHardwarePrice());
        dwProposalProduct.setHWPriceWoTax(productPriceHolder.getProductHardwarePriceWoTax());
        dwProposalProduct.setHwSourceCost(productPriceHolder.getProductHardwareSourceCost());
        dwProposalProduct.setHwProfit(productPriceHolder.getProductHardwareProfit());
        dwProposalProduct.setHwMargin(productPriceHolder.getProductHardwareMargin());
        dwProposalProduct.setAccPrice(productPriceHolder.getProductAccessoryPrice());
        dwProposalProduct.setAccPriceWoTax(productPriceHolder.getProductAccessoryPriceWoTax());
        dwProposalProduct.setAccSourceCost(productPriceHolder.getProductAccessorySourceCost());
        dwProposalProduct.setAccProfit(productPriceHolder.getProductAccessoryProfit());
        dwProposalProduct.setAccMargin(productPriceHolder.getProductAccessoryMargin());
        dwProposalProduct.setHkPrice(productPriceHolder.getProductHandleAndKnobPrice());
        dwProposalProduct.setHkPriceWoTax(productPriceHolder.getProductHandleAndKnobPriceWoTax());
        dwProposalProduct.setHkSourceCost(productPriceHolder.getProductHandleAndKnobSourceCost());
        dwProposalProduct.setHkProfit(productPriceHolder.getProductHandleAndKnobProfit());
        dwProposalProduct.setHkMargin(productPriceHolder.getProductHandleAndKnobMargin());
        dwProposalProduct.setHingePrice(productPriceHolder.getProductHingePrice());
        dwProposalProduct.setHingePriceWoTax(productPriceHolder.getProductHingePriceWoTax());
        dwProposalProduct.setHingeSourceCost(productPriceHolder.getProductHingeSourceCost());
        dwProposalProduct.setHingeProfit(productPriceHolder.getProductHingeProfit());
        dwProposalProduct.setHingeMargin(productPriceHolder.getProductHingeMargin());
        dwProposalProduct.setLaPrice(productPriceHolder.getProductLabourPrice());
        dwProposalProduct.setLaPriceWoTax(productPriceHolder.getProductLabourPriceWoTax());
        dwProposalProduct.setLaSourceCost(productPriceHolder.getProductLabourSourceCost());
        dwProposalProduct.setLaProfit(productPriceHolder.getProductLabourProfit());
        dwProposalProduct.setLaMargin(productPriceHolder.getProductLabourMargin());
        dwProposalProduct.setLCPrice(productPriceHolder.getLConnectorPrice());
        dwProposalProduct.setLcPriceWoTax(productPriceHolder.getLConnectorWoTax());
        dwProposalProduct.setLcSourceCost(productPriceHolder.getLConnectorSourceCost());
        dwProposalProduct.setLcProfit(productPriceHolder.getLConnectorProfit());
        dwProposalProduct.setLcMargin(productPriceHolder.getLConnectorMargin());
        dwProposalProduct.setStdModuleCount(productPriceHolder.getStdModuleCount());
        dwProposalProduct.setStdModulePrice(productPriceHolder.getStdModulePrice());
        dwProposalProduct.setNStdModuleCount(productPriceHolder.getNStdModuleCount());
        dwProposalProduct.setNStdModulePrice(productPriceHolder.getNStdModulePrice());
        dwProposalProduct.setHikeModuleCount(productPriceHolder.getHikeModuleCount());
        dwProposalProduct.setHikeModulePrice(productPriceHolder.getHikeModulePrice());
        dwProposalProduct.setInstallationCost(0);
        dwProposalProduct.setTransportationCost(0);

        return dwProposalProduct;
    }

    private DwProposal setVersionAttributes(ProposalHeader proposalHeader, ProposalVersion proposalVersion, VersionPriceHolder versionPriceHolder) {

        DwProposal dwProposal = new DwProposal();

        dwProposal.setProposalId(proposalHeader.getId());
        dwProposal.setVersion(Double.parseDouble(proposalVersion.getVersion()));
        dwProposal.setProposalTitle(proposalHeader.getQuotationFor());
        dwProposal.setRegion(proposalHeader.getProjectCity());
        dwProposal.setProjectName(proposalHeader.getProjectName());
        dwProposal.setDesignerName(proposalHeader.getDesignerName());
        dwProposal.setSalesName(proposalHeader.getSalespersonName());
        dwProposal.setDesignPartnerName(proposalHeader.getDesignPartnerName());
        dwProposal.setProposalCreateDate(proposalHeader.getCreatedOn());
        dwProposal.setProposalUpdatedBy(proposalHeader.getUpdatedBy());
        dwProposal.setProposalPriceDate(proposalHeader.getPriceDate());
        dwProposal.setBusinessDate(proposalVersion.getDate());
        dwProposal.setVersionCreatedBy(proposalVersion.getCreatedBy());
        dwProposal.setVersionCreatedOn(proposalVersion.getDate());
        dwProposal.setVersionUpdatedBy(proposalVersion.getUpdatedBy());
        dwProposal.setVersionUpdatedOn(proposalVersion.getUpdatedOn());
        dwProposal.setStdModuleCount(versionPriceHolder.getStdModuleCount());
        dwProposal.setStdModulePrice(versionPriceHolder.getStdModulePrice());
        dwProposal.setnStdModuleCount(versionPriceHolder.getNStdModuleCount());
        dwProposal.setnStdModulePrice(versionPriceHolder.getNStdModulePrice());
        dwProposal.setHikeModuleCount(versionPriceHolder.getHikeModuleCount());
        dwProposal.setHikeModulePrice(versionPriceHolder.getHikeModulePrice());
        dwProposal.setVersionPrice(versionPriceHolder.getPrice());
        dwProposal.setVersionPriceAfterDiscount(versionPriceHolder.getPriceAfterDiscount());
        dwProposal.setVersionPriceAfterTax(versionPriceHolder.getPriceWotax());
        dwProposal.setVersionCost(versionPriceHolder.getSourceCost());
        dwProposal.setVersionProfit(versionPriceHolder.getProfit());
        dwProposal.setVersionMargin(versionPriceHolder.getMargin());
        dwProposal.setProductPrice(versionPriceHolder.getProductPrice());
        dwProposal.setProductPriceAfterDiscount(versionPriceHolder.getProductPriceAfterDiscount());
        dwProposal.setProductPriceAfterTax(versionPriceHolder.getProductPriceWotax());
        dwProposal.setProductCost(versionPriceHolder.getProductSourceCost());
        dwProposal.setProductProfit(versionPriceHolder.getProductProfit());
        dwProposal.setProductMargin(versionPriceHolder.getProductMargin());
        dwProposal.setWoodWorkPrice(versionPriceHolder.getWoodworkPrice());
        dwProposal.setWoodWorkPriceAfterDiscount(versionPriceHolder.getWoodworkPriceAfterDiscount());
        dwProposal.setWoodWorkPriceAfterTax(versionPriceHolder.getWoodworkPriceWotax());
        dwProposal.setWoodWorkCost(versionPriceHolder.getWoodworkSourceCost());
        dwProposal.setWoodWorkProfit(versionPriceHolder.getWoodworkProfit());
        dwProposal.setWoodWorkMargin(versionPriceHolder.getWoodworkMargin());
        dwProposal.setHardwarePrice(versionPriceHolder.getHardwarePrice());
        dwProposal.setHardwarePriceAfterDiscount(versionPriceHolder.getHardwarePriceAfterDiscount());
        dwProposal.setHardwarePriceAfterTax(versionPriceHolder.getHardwarePriceWotax());
        dwProposal.setHardwareCost(versionPriceHolder.getHardwareSourceCost());
        dwProposal.setHardwareProfit(versionPriceHolder.getHardwareProfit());
        dwProposal.setHardwareMargin(versionPriceHolder.getHardwareMargin());
        dwProposal.setAccessoryPrice(versionPriceHolder.getAccessoryPrice());
        dwProposal.setAccessoryPriceAfterDiscount(versionPriceHolder.getAccessoryPriceAfterDiscount());
        dwProposal.setAccessoryPriceAfterTax(versionPriceHolder.getAccessoryPriceWotax());
        dwProposal.setAccessoryCost(versionPriceHolder.getAccessorySourceCost());
        dwProposal.setAccessoryProfit(versionPriceHolder.getAcccessoryProfit());
        dwProposal.setAccessoryMargin(versionPriceHolder.getAccessoryMargin());
        dwProposal.setHandleKnobPrice(versionPriceHolder.getHKPrice());
        dwProposal.setHandleKnobPriceAfterDiscount(versionPriceHolder.getHKPriceAfterDiscount());
        dwProposal.setHandleKnobPriceAfterTax(versionPriceHolder.getHKPriceWotax());
        dwProposal.setHandleKnobCost(versionPriceHolder.getHKSourceCost());
        dwProposal.setHandleKnobProfit(versionPriceHolder.getHKProfit());
        dwProposal.setHandleKnobMargin(versionPriceHolder.getHKMargin());
        dwProposal.setHingePrice(versionPriceHolder.getHingePrice());
        dwProposal.setHingePriceAfterDiscount(versionPriceHolder.getHingePriceAfterDiscount());
        dwProposal.setHingePriceAfterTax(versionPriceHolder.getHingePriceWotax());
        dwProposal.setHingeCost(versionPriceHolder.getHingeSourceCost());
        dwProposal.setHingeProfit(versionPriceHolder.getHingeProfit());
        dwProposal.setVersionMargin(versionPriceHolder.getHingeMargin());
        dwProposal.setLabourPrice(versionPriceHolder.getLabourPrice());
        dwProposal.setLabourPriceAfterDiscount(versionPriceHolder.getLabourPriceAfterDiscount());
        dwProposal.setLabourPriceAfterTax(versionPriceHolder.getLabourPriceWotax());
        dwProposal.setLabourCost(versionPriceHolder.getLabourSourceCost());
        dwProposal.setLabourProfit(versionPriceHolder.getLabourProfit());
        dwProposal.setLabourMargin(versionPriceHolder.getLabourMargin());
        dwProposal.setLConnectorPrice(versionPriceHolder.getLCPrice());
        dwProposal.setLConnectorPriceAfterDiscount(versionPriceHolder.getLCPriceAfterDiscount());
        dwProposal.setLConnectorPriceAfterTax(versionPriceHolder.getLCPriceWotax());
        dwProposal.setLConnectorCost(versionPriceHolder.getLCSourceCost());
        dwProposal.setLConnectorProfit(versionPriceHolder.getLCProfit());
        dwProposal.setLConnectorMargin(versionPriceHolder.getLCMargin());
        dwProposal.setBroughtoutProductPrice(versionPriceHolder.getBPPrice());
        dwProposal.setBroughtoutProductPriceAfterDiscount(versionPriceHolder.getBPPriceAfterDiscount());
        dwProposal.setBroughtoutProductPriceAfterTax(versionPriceHolder.getBPPriceWotax());
        dwProposal.setBroughtoutProductCost(versionPriceHolder.getBPSourceCost());
        dwProposal.setBroughtoutProductProfit(versionPriceHolder.getBPProfit());
        dwProposal.setBroughtoutProductMargin(versionPriceHolder.getBPMargin());
        dwProposal.setServicesPrice(versionPriceHolder.getSVPrice());
        dwProposal.setServicesPriceAfterDiscount(versionPriceHolder.getSVPriceAfterDiscount());
        dwProposal.setServicesPriceAfterTax(versionPriceHolder.getSVPriceWotax());
        dwProposal.setServicesCost(versionPriceHolder.getSVSourceCost());
        dwProposal.setServicesProfit(versionPriceHolder.getSVProfit());
        dwProposal.setServicesMargin(versionPriceHolder.getSVMargin());
        dwProposal.setKitchenCount(versionPriceHolder.getKitchenCount());
        dwProposal.setWardrobeCount(versionPriceHolder.getWardrobeCount());
        dwProposal.setNSproductCount(versionPriceHolder.getNSProductCount());
        dwProposal.setBroughtoutProductCount(versionPriceHolder.getBPCount());
        dwProposal.setServicesCount(versionPriceHolder.getServicesCount());

        return dwProposal;
    }

}
