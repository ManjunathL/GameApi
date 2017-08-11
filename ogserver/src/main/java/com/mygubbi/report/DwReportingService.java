package com.mygubbi.report;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.game.proposal.model.dw.DWProductModule;
import com.mygubbi.game.proposal.model.dw.DWProposalAddon;
import com.mygubbi.game.proposal.model.dw.DWProposalProduct;
import com.mygubbi.game.proposal.model.dw.DwProposalVersion;
import com.mygubbi.game.proposal.price.AddonPriceHolder;
import com.mygubbi.game.proposal.price.ModulePriceHolder;
import com.mygubbi.game.proposal.price.ProductPriceHolder;
import com.mygubbi.game.proposal.price.VersionPriceHolder;
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
public class DwReportingService extends AbstractVerticle {

    private final static Logger LOG = LogManager.getLogger(DwReportingService.class);
    public static final String RECORD_VERSION_PRICE = "update.proposal.dw.version.price";

    List<QueryData> queryDatasForModule = new ArrayList<>();
    List<QueryData> queryDatasForProduct = new ArrayList<>();
    List<QueryData> queryDatasForAddon = new ArrayList<>();
    QueryData queryDataVersion = null;

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
            this.getProposalData(message, proposalVersion);
        }).completionHandler(res -> {
            LOG.info("Proposal version price update service started." + res.succeeded());
        });
    }

    private void getProposalData(Message message, ProposalVersion proposalVersion) {
        List<QueryData> queryDatas = new ArrayList<>();

        queryDatas.add(new QueryData("proposal.header", new JsonObject().put("id", proposalVersion.getProposalId())));
        queryDatas.add(new QueryData("proposal.product.selectversion", new JsonObject().put("id", proposalVersion.getProposalId()).put("version",proposalVersion.getVersion())));
        queryDatas.add(new QueryData("proposal.addon.selectversion", new JsonObject().put("id", proposalVersion.getProposalId()).put("version",proposalVersion.getVersion())));

/*
        List<QueryData> resultData = (List<QueryData>)new SingleMessageRequestExecutor().execute(DatabaseService.MULTI_DB_QUERY, queryDatas);
        handleResult(message, proposalVersion, resultData);
*/

        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, LocalCache.getInstance().store(queryDatas),
                (AsyncResult<Message<Integer>> selectResult) -> {
                    List<QueryData> resultDataAsync = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());
                    handleResult(message, proposalVersion, resultDataAsync);
                });

    }

    private void handleResult(Message message, ProposalVersion proposalVersion, List<QueryData> resultData)
    {
        if (resultData.get(0).errorFlag || resultData.get(0).rows == null || resultData.get(0).rows.isEmpty()) {
            message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal not found for id:" + proposalVersion.getProposalId())));
            LOG.error("Proposal not found for id:" + proposalVersion.getProposalId());
        } else {
            ProposalHeader proposalHeader = new ProposalHeader(resultData.get(0).rows.get(0));
            List<ProductLineItem> productLineItems = new ArrayList<>();
            List<JsonObject> jsonObjects = resultData.get(1).rows;
            for (JsonObject jsonObject : jsonObjects)
            {
                LOG.debug("product jsin object :" + jsonObject) ;
                ProductLineItem productLineItem = new ProductLineItem(jsonObject);
                productLineItems.add(productLineItem);
            }
            LOG.debug("Products :" + productLineItems.size());

            List<ProductAddon> addonLineItems = new ArrayList<>();
            List<JsonObject> addonjsonObjects = resultData.get(2).rows;
            for (JsonObject jsonObject : addonjsonObjects)
            {
                ProductAddon productAddon = new ProductAddon(jsonObject);
                addonLineItems.add(productAddon);
            }

            LOG.debug("Addons :" + addonLineItems.size());


            calculateProductLevelPricing(message, proposalHeader, productLineItems, proposalVersion, addonLineItems);

        }
    }


    private void insertRowsToTable(List<QueryData> queryDatas,Message message){
    LOG.info("Inserting rowssss");
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


    private void calculateProductLevelPricing(Message message, ProposalHeader proposalHeader, List<ProductLineItem> productLineItems, ProposalVersion proposalVersion, List<ProductAddon> productAddons) {

        LOG.debug("Calculate product level pricing");

        List<ModulePriceHolder> modulePriceHolders = new ArrayList<>();


        for (ProductLineItem productLineItem : productLineItems) {
            LOG.debug("Inside product line items for loop");

            List<ProductModule> productModules = productLineItem.getModules();
            for (ProductModule productModule: productModules)
            {
                LOG.debug("Inside modules");
                ModulePriceHolder modulePriceHolder = null;
                try {
                    modulePriceHolder = new ModulePriceHolder(productModule,proposalHeader.getProjectCity(),proposalHeader.getPriceDate(),productLineItem);
                    modulePriceHolder.prepare();
                    if (modulePriceHolder.hasErrors()) {
                        LOG.debug("Has errors");
                        message.reply(LocalCache.getInstance().store(modulePriceHolder));
                        return;
                    }

                    modulePriceHolder.calculateTotalCost();
                    modulePriceHolders.add(modulePriceHolder);

                    setModuleAttributes(modulePriceHolder,proposalHeader,productLineItem,proposalVersion,productModule);
                }


                catch (Exception e)
                {
                    message.reply(e.getMessage());
                }


                ProductPriceHolder productPriceHolder = new ProductPriceHolder(productLineItem,modulePriceHolders,proposalHeader,proposalVersion);
                setProductAttributes(productPriceHolder,proposalHeader,proposalVersion,productLineItem);
                this.versionProductPriceHolders.add(productPriceHolder);

            }
        }

        calculateAddonLevelPricing(message,proposalHeader,productAddons,proposalVersion);

    }

    private void calculateAddonLevelPricing(Message message,ProposalHeader proposalHeader, List<ProductAddon> productAddons, ProposalVersion proposalVersion) {

        AddonPriceHolder addonPriceHolder = null;
        for (ProductAddon productAddon : productAddons) {

            try {
                addonPriceHolder = new AddonPriceHolder(productAddon, proposalHeader);
                addonPriceHolder.prepare();
                if (addonPriceHolder.hasErrors()) {
                    return;
                }
            }
            catch (Exception e)
            {
               LOG.debug("Exception :" + e.getMessage());
            }

            this.versionAddonPriceHolders.add(addonPriceHolder);

            DWProposalAddon dwProposalAddon = setAddonLevelAttributes(proposalHeader,proposalVersion,productAddon,addonPriceHolder);
            queryDatasForAddon.add(new QueryData("dw_proposal_addon.insert",dwProposalAddon));
        }

        VersionPriceHolder versionPriceHolder = new VersionPriceHolder(proposalHeader,versionProductPriceHolders,versionAddonPriceHolders,proposalVersion);
        setVersionAttributes(proposalHeader,proposalVersion,versionPriceHolder,message);

    }

    private DWProposalAddon setAddonLevelAttributes(ProposalHeader proposalHeader, ProposalVersion proposalVersion, ProductAddon productAddon, AddonPriceHolder addonPriceHolder)
    {
        DWProposalAddon dwProposalAddon = new DWProposalAddon();
        dwProposalAddon = dwProposalAddon.setDwAddonObjects(proposalHeader,proposalVersion,productAddon,addonPriceHolder);

        queryDatasForAddon.add(new QueryData("dw_proposal_addon.insert",dwProposalAddon));

        return dwProposalAddon;
    }


    private void setModuleAttributes(ModulePriceHolder modulePriceHolder, ProposalHeader proposalHeader, ProductLineItem productLineItem, ProposalVersion proposalVersion, ProductModule productModule)
    {
        DWProductModule dwProductModule = new DWProductModule();
        dwProductModule = dwProductModule.setDwModuleObjects(modulePriceHolder,proposalHeader,productLineItem,proposalVersion,productModule);

        queryDatasForModule.add(new QueryData("dw_product_module.insert",dwProductModule));

    }

    private void setProductAttributes(ProductPriceHolder productPriceHolder, ProposalHeader proposalHeader, ProposalVersion proposalVersion, ProductLineItem productLineItem)
    {
        DWProposalProduct dwProposalProduct= new DWProposalProduct();
        dwProposalProduct = dwProposalProduct.setDwProductObjects(productPriceHolder,proposalHeader,proposalVersion,productLineItem);

        queryDatasForProduct.add(new QueryData("dw_proposal_product.insert",dwProposalProduct));
    }

    private void setVersionAttributes(ProposalHeader proposalHeader, ProposalVersion proposalVersion, VersionPriceHolder versionPriceHolder, Message message) {

        DwProposalVersion dwProposalVersion = new DwProposalVersion();
        dwProposalVersion = dwProposalVersion.setDwVersionObjects(proposalHeader,proposalVersion,versionPriceHolder);

        queryDataVersion = new QueryData("dw.proposal.insert",dwProposalVersion);

        List<QueryData> queryDatas = new ArrayList<>();
        queryDatas.addAll(queryDatasForAddon);
        queryDatas.addAll(queryDatasForProduct);
        queryDatas.addAll(queryDatasForModule);
        queryDatas.add(queryDataVersion);

        insertRowsToTable(queryDatas,message);
    }

}
