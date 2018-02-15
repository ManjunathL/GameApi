package com.mygubbi.report;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.*;
import com.mygubbi.game.proposal.model.dw.*;
import com.mygubbi.game.proposal.price.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by User on 04-08-2017.
 */
public class DwReportingService extends AbstractVerticle {

    private final static Logger LOG = LogManager.getLogger(DwReportingService.class);
    public static final String RECORD_VERSION_PRICE = "update.proposal.dw.version.price";
    public static final String ODS_POOL_NAME = "ods";


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
            ReportingObjects reportingObjects = new ReportingObjects();
            reportingObjects.proposalId = proposalVersion.getProposalId();
            reportingObjects.version = proposalVersion.getVersion();
            reportingObjects.versionId = proposalVersion.getId();
            LOG.info("Handling (proposalId,version) = "+"("+reportingObjects.proposalId+","+reportingObjects.version+")");

            this.getProposalData(message, proposalVersion,reportingObjects);
        }).completionHandler(res -> {
            LOG.info("Proposal version price update service started." + res.succeeded());
        });
    }

    private void getProposalData(Message message, ProposalVersion proposalVersion, ReportingObjects reportingObjects) {
        List<QueryData> queryDatas = new ArrayList<>();

        queryDatas.add(new QueryData("proposal.header", new JsonObject().put("id", proposalVersion.getProposalId())));
        queryDatas.add(new QueryData("proposal.product.selectversion", new JsonObject().put("id", proposalVersion.getProposalId()).put("version", proposalVersion.getVersion())));
        queryDatas.add(new QueryData("proposal.addon.selectversion", new JsonObject().put("id", proposalVersion.getProposalId()).put("version", proposalVersion.getVersion())));

/*
        List<QueryData> resultData = (List<QueryData>)new SingleMessageRequestExecutor().execute(DatabaseService.MULTI_DB_QUERY, queryDatas);
        handleResult(message, proposalVersion, resultData);
*/

        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, LocalCache.getInstance().store(queryDatas),
                (AsyncResult<Message<Integer>> selectResult) -> {
                    List<QueryData> resultDataAsync = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());
                    handleResult(message, proposalVersion, resultDataAsync,reportingObjects);
                });

    }

    private void handleResult(Message message, ProposalVersion proposalVersion, List<QueryData> resultData, ReportingObjects reportingObjects) {
        if (resultData.get(0).errorFlag || resultData.get(0).rows == null || resultData.get(0).rows.isEmpty()) {
            message.reply(LocalCache.getInstance().store(getResponseJson("Failure",reportingObjects.proposalId,reportingObjects.version,resultData.get(0).errorMessage)));
            LOG.error("Proposal not found for id:" + proposalVersion.getProposalId());
        } else {
            ProposalHeader proposalHeader = new ProposalHeader(resultData.get(0).rows.get(0));
            List<ProductLineItem> productLineItems = new ArrayList<>();
            List<JsonObject> jsonObjects = resultData.get(1).rows;
            for (JsonObject jsonObject : jsonObjects) {
                ProductLineItem productLineItem = new ProductLineItem(jsonObject);
                productLineItems.add(productLineItem);
            }
            List<ProductAddon> addonLineItems = new ArrayList<>();
            List<JsonObject> addonjsonObjects = resultData.get(2).rows;
            for (JsonObject jsonObject : addonjsonObjects) {
                ProductAddon productAddon = new ProductAddon(jsonObject);
                addonLineItems.add(productAddon);
            }
            calculateProductLevelPricing(message, proposalHeader, productLineItems, proposalVersion, addonLineItems,reportingObjects);

        }
    }


    private void insertRowsToTable(List<QueryData> queryDatas, Message message, ReportingObjects reportingObjects) {
        Integer id = LocalCache.getInstance().store(queryDatas);
        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id, new DeliveryOptions().setSendTimeout(120000),
                (AsyncResult<Message<Integer>> selectResult) -> {
                    List<QueryData> resultDatas = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());
                    int i = 0;
                    for (; i < resultDatas.size(); i++) {
                        if (resultDatas.get(i).errorFlag ) {

                            message.reply(LocalCache.getInstance().store(getResponseJson("Failure",reportingObjects.proposalId,reportingObjects.version,resultDatas.get(i).errorMessage)));
                            LOG.error("Error in Executing Query : "+resultDatas.get(i).queryId +" and error is::"+resultDatas.get(i).errorMessage);
                            return;
                        }
                    }

                    if (i == resultDatas.size()) {
                        JsonObject params = new JsonObject();

                        params.put("vId1",reportingObjects.versionId);
                        params.put("isDataLoadedToReport1", "Yes");
                        params.put("dataLoadedOn1", getCurrentDate());
                        params.put("vId2",reportingObjects.versionId);
                        params.put("isDataLoadedToReport2", "Yes");
                        params.put("dataLoadedOn2", getCurrentDate());

                        Integer id2 = LocalCache.getInstance().store(new QueryData("insertOrUpdate.version_master.dataLoadStatus", params));
                        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id2,
                                (AsyncResult<Message<Integer>> res) -> {
                                    QueryData resData = (QueryData) LocalCache.getInstance().remove(res.result().body());
                                    if ((resData.errorFlag) || resData.updateResult.getUpdated() == 0) {
                                        message.reply(LocalCache.getInstance().store(getResponseJson("Failure",reportingObjects.proposalId,reportingObjects.version,resData.errorMessage)));
                                        LOG.error("Error in Executing Query : "+resData.queryId +" and error is::"+resData.errorMessage);
                                        return;
                                    } else {
                                        message.reply(LocalCache.getInstance().store(getResponseJson("Success", reportingObjects.proposalId, reportingObjects.version, "Successfully inserted")));
                                    }
                                });
                    }
                });
    }

    private JsonObject getResponseJson(String status,int proposalId,String version,String comments){
        JsonObject response = new JsonObject();
        response.put("status",status);
        response.put("proposalId",proposalId);
        response.put("version",version);
        response.put("comments",comments);
        return response;
    }

    private void calculateProductLevelPricing(Message message, ProposalHeader proposalHeader, List<ProductLineItem> productLineItems, ProposalVersion proposalVersion, List<ProductAddon> productAddons, ReportingObjects reportingObjects) {

        List<ModulePriceHolder> modulePriceHolders = new ArrayList<>();
        List<ProductPriceHolder> versionProductPriceHolders = new ArrayList<>();

        int count = 0;


        for (ProductLineItem productLineItem : productLineItems) {
            count++;
            modulePriceHolders.clear();

            List<ProductModule> productModules = productLineItem.getModules();
            for (ProductModule productModule : productModules) {
                ModulePriceHolder modulePriceHolder = null;
                try {
                    modulePriceHolder = new ModulePriceHolder(productModule, proposalHeader.getProjectCity(), proposalHeader.getPriceDate(), productLineItem);
                    modulePriceHolder.prepare();
                    if (modulePriceHolder.hasErrors()) {
                        LOG.debug("Has errors");
                        LOG.info("Error is :: "+modulePriceHolder.getErrors());
                        message.reply(LocalCache.getInstance().store(getResponseJson("Failure",reportingObjects.proposalId,reportingObjects.version,"Module Price Holder has errors")));
                        return;
                    }

                    modulePriceHolder.calculateTotalCost();
                    modulePriceHolders.add(modulePriceHolder);

                    setModuleAttributes(modulePriceHolder, proposalHeader, productLineItem, proposalVersion, productModule,reportingObjects);

                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.info("Error is :: "+modulePriceHolder.getErrors());
                    message.reply(LocalCache.getInstance().store(getResponseJson("Failure",reportingObjects.proposalId,reportingObjects.version,"Module Price Holder has errors")));
                }

                List<AccessoryComponent> accessoryComponents = modulePriceHolder.getAccessoryComponents();
                List<HardwareComponent> hardwareComponents = modulePriceHolder.getHardwareComponents();
                List<PanelComponent> panelComponents = modulePriceHolder.getPanelComponents();

                for (PanelComponent panelComponent : panelComponents )
                {
                    if (panelComponent.isCarcass() || panelComponent.isShutter())
                    {
                        setComponentAttributes(proposalHeader,proposalVersion,productLineItem,productModule,panelComponent,reportingObjects);
                    }
                }

                for (HardwareComponent hardwareComponent : hardwareComponents)
                {
                    setComponentAttributesForHardware(proposalHeader,proposalVersion,productLineItem,productModule,hardwareComponent,reportingObjects);
                }

                for (AccessoryComponent accessoryComponent: accessoryComponents)
                {
                    setComponentAttributesForAccessory(proposalHeader,proposalVersion,productLineItem,productModule,accessoryComponent,reportingObjects);
                }

                this.collectModuleHandles(proposalHeader,proposalVersion,productLineItem,productModule,reportingObjects);
                this.collectModuleKnob(proposalHeader,proposalVersion,productLineItem,productModule,reportingObjects);
                if (!(productModule.getHingePacks().size() == 0) || productModule.getHingePacks() == null) this.collectModuleHinge(proposalHeader,proposalVersion,productLineItem,productModule,reportingObjects);

            }

            PriceMaster lConnectorRate=RateCardService.getInstance().getHardwareRate("H074",proposalHeader.getPriceDate(),proposalHeader.getProjectCity());
            AccHwComponent lConnector = ModuleDataService.getInstance().getHardware(lConnectorRate.getRateId());
            if(productLineItem.getHandletypeSelection() != null) {
                if (productLineItem.getHandletypeSelection().equals("Gola Profile") && productLineItem.getNoOfLengths() != 0) {
                    setComponentAttributesForProfileHardware(proposalHeader,proposalVersion,productLineItem,productModules.get(0),lConnector,productLineItem.getNoOfLengths(),reportingObjects);

                }
            }


            ProductPriceHolder productPriceHolder = new ProductPriceHolder(productLineItem, modulePriceHolders, proposalHeader, proposalVersion);
            productPriceHolder.prepare();
            setProductAttributes(productPriceHolder, proposalHeader, proposalVersion, productLineItem,reportingObjects);
            versionProductPriceHolders.add(productPriceHolder);
        }
        calculateAddonLevelPricing(message, proposalHeader, productAddons, proposalVersion, versionProductPriceHolders,reportingObjects);
    }

    private void calculateAddonLevelPricing(Message message, ProposalHeader proposalHeader, List<ProductAddon> productAddons, ProposalVersion proposalVersion, List<ProductPriceHolder> versionProductPriceHolders, ReportingObjects reportingObjects) {

        List<AddonPriceHolder> versionAddonPriceHolders = new ArrayList<>();

        AddonPriceHolder addonPriceHolder = null;
        for (ProductAddon productAddon : productAddons) {

            try {
                addonPriceHolder = new AddonPriceHolder(productAddon, proposalHeader);
                addonPriceHolder.prepare();
                if (addonPriceHolder.hasErrors()) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOG.debug("Exception :" + e.getMessage());
            }

            versionAddonPriceHolders.add(addonPriceHolder);

            DWProposalAddon dwProposalAddon = setAddonLevelAttributes(proposalHeader, proposalVersion, productAddon, addonPriceHolder);
//            queryDatasForAddon.add(new QueryData("dw_proposal_addon.insert", dwProposalAddon));
            reportingObjects.queryDatasForAddon.add( dwProposalAddon);
        }

        VersionPriceHolder versionPriceHolder = new VersionPriceHolder(proposalHeader, versionProductPriceHolders, versionAddonPriceHolders, proposalVersion);
        versionPriceHolder.prepare();
        setVersionAttributes(proposalHeader, proposalVersion, versionPriceHolder, message,reportingObjects);

    }

    private DWProposalAddon setAddonLevelAttributes(ProposalHeader proposalHeader, ProposalVersion proposalVersion, ProductAddon productAddon, AddonPriceHolder addonPriceHolder) {
        DWProposalAddon dwProposalAddon = new DWProposalAddon();
        dwProposalAddon = dwProposalAddon.setDwAddonObjects(proposalHeader, proposalVersion, productAddon, addonPriceHolder);

//        queryDatasForAddon.add(new QueryData("dw_proposal_addon.insert",dwProposalAddon));

        return dwProposalAddon;
    }

    private void setComponentAttributes(ProposalHeader proposalHeader, ProposalVersion proposalVersion, ProductLineItem productLineItem, ProductModule productModule, PanelComponent panelComponent, ReportingObjects reportingObjects) {
        DWModuleComponent dwModuleComponent = new DWModuleComponent();
        dwModuleComponent = dwModuleComponent.setDwComponentAttributesForPanel(proposalHeader,proposalVersion,productLineItem,productModule,panelComponent);


//        queryDatasForComponent.add(new QueryData("dw_module_component.insert", dwModuleComponent));
        reportingObjects.queryDatasForComponent.add(dwModuleComponent);

    }

    private void setComponentAttributesForHardware(ProposalHeader proposalHeader, ProposalVersion proposalVersion, ProductLineItem productLineItem, ProductModule productModule, HardwareComponent hardwareComponent, ReportingObjects reportingObjects) {
        DWModuleComponent dwModuleComponent = new DWModuleComponent();
        dwModuleComponent = dwModuleComponent.setDwComponentAttributesForHardware(proposalHeader,proposalVersion,productLineItem,productModule,hardwareComponent);

        reportingObjects.queryDatasForComponent.add(dwModuleComponent);
    }

    private void setComponentAttributesForProfileHardware(ProposalHeader proposalHeader, ProposalVersion proposalVersion, ProductLineItem productLineItem, ProductModule productModule, AccHwComponent accHwComponent, double quantity, ReportingObjects reportingObjects) {
        DWModuleComponent dwModuleComponent = new DWModuleComponent();
        dwModuleComponent = dwModuleComponent.setDwComponentAttributesForGolaProfileHardware(proposalHeader,proposalVersion,productLineItem,productModule,accHwComponent,quantity);

        reportingObjects.queryDatasForComponent.add(dwModuleComponent);
    }

    private void setComponentAttributesForAccessory(ProposalHeader proposalHeader, ProposalVersion proposalVersion, ProductLineItem productLineItem, ProductModule productModule, AccessoryComponent accessoryComponent, ReportingObjects reportingObjects) {
        DWModuleComponent dwModuleComponent = new DWModuleComponent();
        dwModuleComponent = dwModuleComponent.setDwComponentAttributesForAccessories(proposalHeader,proposalVersion,productLineItem,productModule,accessoryComponent);


//        queryDatasForComponent.add(new QueryData("dw_module_component.insert", dwModuleComponent));
        reportingObjects.queryDatasForComponent.add(dwModuleComponent);

    }


    private void setModuleAttributes(ModulePriceHolder modulePriceHolder, ProposalHeader proposalHeader, ProductLineItem productLineItem, ProposalVersion proposalVersion, ProductModule productModule, ReportingObjects reportingObjects) {
        DWProductModule dwProductModule = new DWProductModule();
        dwProductModule = dwProductModule.setDwModuleObjects(modulePriceHolder, proposalHeader, productLineItem, proposalVersion, productModule);

        reportingObjects.queryDatasForModule.add( dwProductModule);

    }

    private void setProductAttributes(ProductPriceHolder productPriceHolder, ProposalHeader proposalHeader, ProposalVersion proposalVersion, ProductLineItem productLineItem, ReportingObjects reportingObjects) {
        DWProposalProduct dwProposalProduct = new DWProposalProduct();

        dwProposalProduct = dwProposalProduct.setDwProductObjects(productPriceHolder, proposalHeader, proposalVersion, productLineItem);
        reportingObjects.queryDatasForProduct.add( dwProposalProduct);
    }

    private void setVersionAttributes(ProposalHeader proposalHeader, ProposalVersion proposalVersion, VersionPriceHolder versionPriceHolder, Message message, ReportingObjects reportingObjects) {

        DwProposalVersion dwProposalVersion = new DwProposalVersion();
        dwProposalVersion = dwProposalVersion.setDwVersionObjects(proposalHeader, proposalVersion, versionPriceHolder);
        QueryData queryDataVersion = new QueryData("dw_proposal.insert", dwProposalVersion,ODS_POOL_NAME);


        List<QueryData> queryDatas = new ArrayList<>();
        queryDatas.add(new QueryData("dw_proposal.delete", proposalVersion,ODS_POOL_NAME));
        queryDatas.add(new QueryData("dw_proposal_product.delete", proposalVersion,ODS_POOL_NAME));
        queryDatas.add(new QueryData("dw_proposal_addon.delete", proposalVersion,ODS_POOL_NAME));
        queryDatas.add(new QueryData("dw_product_module.delete", proposalVersion,ODS_POOL_NAME));
        queryDatas.add(new QueryData("dw_module_component.delete", proposalVersion, ODS_POOL_NAME));

        if (!(reportingObjects.queryDatasForComponent.isEmpty())) queryDatas.add(new QueryData("dw_module_component.insert",reportingObjects.queryDatasForComponent,ODS_POOL_NAME));
        if (!(reportingObjects.queryDatasForModule.isEmpty())) queryDatas.add(new QueryData("dw_product_module.insert",reportingObjects.queryDatasForModule,ODS_POOL_NAME));
        if (!(reportingObjects.queryDatasForProduct.isEmpty())) queryDatas.add(new QueryData("dw_proposal_product.insert",reportingObjects.queryDatasForProduct,ODS_POOL_NAME));
        if (!(reportingObjects.queryDatasForAddon.isEmpty())) queryDatas.add(new QueryData("dw_proposal_addon.insert",reportingObjects.queryDatasForAddon,ODS_POOL_NAME));
        queryDatas.add(queryDataVersion);
        insertRowsToTable(queryDatas, message,reportingObjects);
    }

    private String getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        LocalDateTime localDate = LocalDateTime.now();
        return dtf.format(localDate);
    }

    private void collectModuleHandles(ProposalHeader proposalHeader, ProposalVersion proposalVersion,ProductLineItem productLineItem,ProductModule productModule,ReportingObjects reportingObjects) {

        String GOLA_PROFILE = "Gola Profile";

        java.sql.Date priceDate = proposalHeader.getPriceDate();
        String city = proposalHeader.getProjectCity();

        PriceMaster lWidthRate = RateCardService.getInstance().getHardwareRate("H073", priceDate, city);
        PriceMaster cWidthRate = RateCardService.getInstance().getHardwareRate("H071", priceDate, city);
        PriceMaster wWidthRate = RateCardService.getInstance().getHardwareRate("H076", priceDate, city);
        PriceMaster bracketRate = RateCardService.getInstance().getHardwareRate("H075", priceDate, city);
        PriceMaster lConnectorRate = RateCardService.getInstance().getHardwareRate("H074", priceDate, city);
        PriceMaster cConnectorRate = RateCardService.getInstance().getHardwareRate("H072", priceDate, city);
        PriceMaster gProfileRate = RateCardService.getInstance().getHardwareRate("H018", priceDate, city);
        PriceMaster jProfileRate = RateCardService.getInstance().getHardwareRate("H084", priceDate, city);

        if (Objects.equals(productLineItem.getHandletypeSelection(), GOLA_PROFILE)) {

            AccHwComponent wProfile = ModuleDataService.getInstance().getHardware(wWidthRate.getRateId());
            double wProfileWidth = productModule.getWidth();
            wProfileWidth = (wProfileWidth /1000);
            setComponentAttributesForProfileHardware(proposalHeader,proposalVersion,productLineItem,productModule,wProfile,wProfileWidth,reportingObjects);

            AccHwComponent lProfile = ModuleDataService.getInstance().getHardware(lWidthRate.getRateId());
            double lProfileWidth = productModule.getWidth();
            lProfileWidth = (lProfileWidth /1000);
            setComponentAttributesForProfileHardware(proposalHeader,proposalVersion,productLineItem,productModule,lProfile,lProfileWidth,reportingObjects);

            AccHwComponent cProfile = ModuleDataService.getInstance().getHardware(cWidthRate.getRateId());
            double cProfileWidth = productModule.getWidth();
            cProfileWidth = (lProfileWidth /1000);
            setComponentAttributesForProfileHardware(proposalHeader,proposalVersion,productLineItem,productModule,cProfile,cProfileWidth,reportingObjects);

            AccHwComponent bracket = ModuleDataService.getInstance().getHardware(bracketRate.getRateId());
            setComponentAttributesForProfileHardware(proposalHeader,proposalVersion,productLineItem,productModule,bracket,2,reportingObjects);

            AccHwComponent cConnector = ModuleDataService.getInstance().getHardware(cConnectorRate.getRateId());
            setComponentAttributesForProfileHardware(proposalHeader,proposalVersion,productLineItem,productModule,cConnector,1,reportingObjects);

        }
        if (Objects.equals(productLineItem.getHandletypeSelection(), "G Profile")) {

            double quantity = 0;
            double lwidth = 0;
            double moduleWidth = productModule.getWidth();
            double factor = moduleWidth /1000;


            Collection<AccessoryPackComponent> handles = ModuleDataService.getInstance().getAccessoryPackComponents(productModule.getMGCode());
            for (AccessoryPackComponent accessoryPackComponent : handles) {
                if (accessoryPackComponent.getType().equals("HL"))
                {
                    quantity = accessoryPackComponent.getQuantity();
                }
            }

            if (Objects.equals(productModule.getHandleMandatory(), "Yes"))
            {
                if (productModule.getModuleCategory().contains("Drawer"))
                {
                    lwidth = lwidth + (quantity * factor);
                }
                else {
                    lwidth = lwidth + factor;
                }
            }

            AccHwComponent hardware5 = ModuleDataService.getInstance().getHardware(gProfileRate.getRateId());
            setComponentAttributesForProfileHardware(proposalHeader,proposalVersion,productLineItem,productModule,hardware5,lwidth,reportingObjects);

        }
        if (Objects.equals(productLineItem.getHandletypeSelection(), "J Profile")) {

            double quantity = 0;
            double lWidth = 0;
            double moduleWidth = productModule.getWidth();
            double factor = moduleWidth /1000;

            Collection<AccessoryPackComponent> handles = ModuleDataService.getInstance().getAccessoryPackComponents(productModule.getMGCode());
            for (AccessoryPackComponent accessoryPackComponent : handles)
            {
                if (accessoryPackComponent.getType().equals("HL"))
                {
                    quantity = accessoryPackComponent.getQuantity();
                }
            }
            if (Objects.equals(productModule.getHandleMandatory(), "Yes"))
            {
                if (productModule.getModuleCategory().contains("Drawer"))
                {
                    lWidth = lWidth + (quantity * factor);
                }
                else {
                    lWidth = lWidth + factor;
                }
            }

            AccHwComponent hardware6 = ModuleDataService.getInstance().getHardware(jProfileRate.getRateId());
            setComponentAttributesForProfileHardware(proposalHeader,proposalVersion,productLineItem,productModule,hardware6,lWidth,reportingObjects);
        }
        if (productModule.getHandleCode() == null) {

        }

        if (Objects.equals(productLineItem.getHandletypeSelection(),"Normal" ))
        {
            if (productModule.getHandleOverrideFlag()== null)
            {
                if (!(productLineItem.getHandleCode() == null))
                {
                    if (productModule.getHandleMandatory().equalsIgnoreCase("yes"))
                    {
                        Handle handle = ModuleDataService.getInstance().getHandleKnobHingeDetails(productLineItem.getHandleCode());
                        setComponentAttributesForHandle(proposalHeader,proposalVersion,productLineItem,productModule,handle,reportingObjects,productModule.getHandleQuantity());

                    }
                }
            }
            else if (productModule.getHandleOverrideFlag().equals("Yes")) {
                {
                    Handle handle = ModuleDataService.getInstance().getHandleKnobHingeDetails(productModule.getHandleCode());
                    setComponentAttributesForHandle(proposalHeader,proposalVersion,productLineItem,productModule,handle,reportingObjects,productModule.getHandleQuantity());
                }
            }

        }


           /* if (productModule.getHandleMandatory().equalsIgnoreCase("yes"))
            {
//                LOG.debug("Collect handle : " + productModule.getHandleQuantity() + " : " + productModule.getMGCode());
                Handle handle = ModuleDataService.getInstance().getHandleKnobHingeDetails(productModule.getHandleCode());
                setComponentAttributesForHandle(proposalHeader,proposalVersion,productLineItem,productModule,handle,reportingObjects,productModule.getHandleQuantity());
            }*/


    }

    private void collectModuleKnob(ProposalHeader proposalHeader, ProposalVersion proposalVersion,ProductLineItem productLineItem,ProductModule productModule,ReportingObjects reportingObjects) {
        if (productModule.getKnobCode() == null) {
            return;
        }
        if (productModule.getKnobMandatory().equalsIgnoreCase("yes"))
        {
//            LOG.debug("Collect knob : " + productModule.getKnobQuantity() + " : " + productModule.getMGCode());

            Handle knob = ModuleDataService.getInstance().getHandleKnobHingeDetails(productModule.getKnobCode());
            setComponentAttributesForHandle(proposalHeader,proposalVersion,productLineItem,productModule,knob,reportingObjects,productModule.getKnobQuantity());
        }

    }

    private void collectModuleHinge(ProposalHeader proposalHeader, ProposalVersion proposalVersion,ProductLineItem productLineItem,ProductModule productModule,ReportingObjects reportingObjects) {


        for (HingePack hingePack : productModule.getHingePacks()) {
            double quantity = hingePack.getQUANTITY();


            if (Objects.equals(hingePack.getQtyFlag(), "C")) {
                if (Objects.equals(hingePack.getQtyFormula(), "") || hingePack.getQtyFormula().isEmpty()) {
                    String code = null;
                    if (Objects.equals(hingePack.getTYPE(), "Soft Close"))
                    {
                        if (productModule.getDepth() < 350)
                            code = "HINGE05";
                        else if (productModule.getDepth() < 400)
                            code = "HINGE04";
                        else if (productModule.getDepth() < 450)
                            code = "HINGE02";
                        else if (productModule.getDepth() < 500)
                            code = "HINGE03";
                        else if (productModule.getDepth() < 650)
                            code = "HINGE06";
                    }
                    else
                    {

                        if (productModule.getDepth() < 350)
                            code = "HINGE11";
                        else if (productModule.getDepth() < 400)
                            code = "HINGE10";
                        else if (productModule.getDepth() < 450)
                            code = "HINGE08";
                        else if (productModule.getDepth() < 500)
                            code = "HINGE09";
                        else if (productModule.getDepth() < 650)
                            code = "HINGE12";
                    }


                    Handle hinge = ModuleDataService.getInstance().getHandleKnobHingeDetails(code);

                    setComponentAttributesForHandle(proposalHeader,proposalVersion,productLineItem,productModule,hinge,reportingObjects,quantity);


                } else {
                    quantity = getHingeRateBasedOnQty(hingePack,productModule);
                    Handle hinge = ModuleDataService.getInstance().getHandleKnobHingeDetails(hingePack.getHingeCode());

                    setComponentAttributesForHandle(proposalHeader,proposalVersion,productLineItem,productModule,hinge,reportingObjects,quantity);

                }
            }
            else {
                Handle hinge = ModuleDataService.getInstance().getHandleKnobHingeDetails(hingePack.getHingeCode());

                setComponentAttributesForHandle(proposalHeader,proposalVersion,productLineItem,productModule,hinge,reportingObjects,quantity);
            }
        }
    }

    private void setComponentAttributesForHandle(ProposalHeader proposalHeader, ProposalVersion proposalVersion, ProductLineItem productLineItem, ProductModule productModule, Handle handle, ReportingObjects reportingObjects, double quantity) {
        DWModuleComponent dwModuleComponent = new DWModuleComponent();
        dwModuleComponent = dwModuleComponent.setDwComponentAttributesForHandleKnobOrHinge(proposalHeader,proposalVersion,productLineItem,productModule,handle,quantity);

        reportingObjects.queryDatasForComponent.add(dwModuleComponent);
    }

    private double getHingeRateBasedOnQty(HingePack hingePack, ProductModule productModule) {

        double quantity = hingePack.getQUANTITY();

        if (Objects.equals(hingePack.getQtyFlag(), "C")) {
            if (Objects.equals(hingePack.getQtyFormula(), "F6")) {
                int value1 = (productModule.getHeight() > 2100) ? 5 : 4;
                int value2 = (productModule.getWidth() > 600) ? 2 : 1;
                quantity = value1 * value2;
            } else if (Objects.equals(hingePack.getQtyFormula(), "F12")) {
                quantity = (productModule.getWidth() >= 601) ? 4 : 2;
            }
        }
        return quantity;
    }


}