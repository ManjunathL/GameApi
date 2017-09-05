package com.mygubbi.report;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
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
                        message.reply(LocalCache.getInstance().store(getResponseJson("Success",reportingObjects.proposalId,reportingObjects.version,"Successfully inserted")));

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
        dwModuleComponent = dwModuleComponent.setDwComponentAttributes(proposalHeader,proposalVersion,productLineItem,productModule,panelComponent);


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
        QueryData queryDataVersion = new QueryData("dw_proposal.insert", dwProposalVersion);


        List<QueryData> queryDatas = new ArrayList<>();
        queryDatas.add(new QueryData("dw_proposal.delete", proposalVersion));
        queryDatas.add(new QueryData("dw_proposal_product.delete", proposalVersion));
        queryDatas.add(new QueryData("dw_proposal_addon.delete", proposalVersion));
        queryDatas.add(new QueryData("dw_product_module.delete", proposalVersion));
        queryDatas.add(new QueryData("dw_module_component.delete", proposalVersion));
        if (!(reportingObjects.queryDatasForComponent.isEmpty())) queryDatas.add(new QueryData("dw_module_component.insert",reportingObjects.queryDatasForComponent));
        if (!(reportingObjects.queryDatasForModule.isEmpty())) queryDatas.add(new QueryData("dw_product_module.insert",reportingObjects.queryDatasForModule));
        if (!(reportingObjects.queryDatasForProduct.isEmpty())) queryDatas.add(new QueryData("dw_proposal_product.insert",reportingObjects.queryDatasForProduct));
        if (!(reportingObjects.queryDatasForAddon.isEmpty())) queryDatas.add(new QueryData("dw_proposal_addon.insert",reportingObjects.queryDatasForAddon));
        queryDatas.add(queryDataVersion);
        JsonObject params = new JsonObject();
        params.put("proposalId",reportingObjects.proposalId);
        params.put("version",reportingObjects.version);
        params.put("status","Yes");
        params.put("dataLoadedOn",getCurrentDate());
        queryDatas.add(new QueryData("update.version_master.dataLoadStatus",params));
        insertRowsToTable(queryDatas, message,reportingObjects);
    }
    private String getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        LocalDateTime localDate = LocalDateTime.now();
        return dtf.format(localDate);
    }

}