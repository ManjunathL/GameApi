package com.mygubbi.game.proposal.price;

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
 * Created by Sunil on 08-01-2016.
 */

public class VersionPricingService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(VersionPricingService.class);
    public static final String CALCULATE_VERSION_PRICE = "calculate.version.price";

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.setupPriceCalculator();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }

    private void setupPriceCalculator()
    {
        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(CALCULATE_VERSION_PRICE, (Message<Integer> message) -> {
            ProposalVersion proposalVersion = (ProposalVersion) LocalCache.getInstance().remove(message.body());
            this.getProposalHeader(proposalVersion, message);
        }).completionHandler(res -> {
            LOG.info("Comprehensive Module price calculator service started." + res.succeeded());
        });
    }

    private void getProposalHeader(ProposalVersion proposalVersion, Message message) {
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.header", new JsonObject().put("id", proposalVersion.getProposalId())));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty()) {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal not found for id:" + proposalVersion.getProposalId())));
                        LOG.error("Proposal not found for id:" + proposalVersion.getProposalId());
                    } else {
                        ProposalHeader proposalHeader = new ProposalHeader(resultData.rows.get(0));
                        getProductAndAddons(proposalHeader,message,proposalVersion);

                    }
                });
    }

    private void getProductAndAddons(ProposalHeader proposalHeader, Message message, ProposalVersion proposalVersion)
    {

        List<QueryData> queryDatas = new ArrayList<>();
        queryDatas.add(new QueryData("proposal.version.products.select", new JsonObject().put("proposalId", proposalVersion.getProposalId()).put("version",proposalVersion.getVersion())));
        queryDatas.add(new QueryData("proposal.version.addons.select", new JsonObject().put("proposalId", proposalVersion.getProposalId()).put("version",proposalVersion.getVersion())));

        Integer id = LocalCache.getInstance().store(queryDatas);
        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    List<QueryData> resultData = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());

                    if (resultData.get(0).errorFlag || resultData.get(1).errorFlag)
                    {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("status","Error")));
                    }
                    else
                    {
                        List<ProductLineItem> products = new ArrayList<ProductLineItem>();
                        for (JsonObject json : resultData.get(0).rows) {
                            products.add(new ProductLineItem(json));
                        }
                        List<ProductAddon> addons = new ArrayList<ProductAddon>();
                        for (JsonObject json : resultData.get(1).rows) {
                            addons.add(new ProductAddon(json));
                        }

                        collectPriceHolders(proposalHeader,proposalVersion,products,addons, message);
                    }
                });
    }

    private void collectPriceHolders(ProposalHeader proposalHeader, ProposalVersion proposalVersion,List<ProductLineItem> productLineItems,List<ProductAddon> productAddons, Message message)
    {
        List<ProductPriceHolder> productPriceHolders = new ArrayList<>();
        List<AddonPriceHolder> addonPriceHolders = new ArrayList<>();

        for (ProductLineItem productLineItem : productLineItems) {
            List<ModulePriceHolder> modulePriceHolders = new ArrayList<>();
            for (ProductModule productModule : productLineItem.getModules()) {
                ModuleForPrice moduleForPrice = new ModuleForPrice(productModule, productLineItem, proposalHeader.getPriceDate(), proposalHeader.getProjectCity());

                ModulePriceHolder modulePriceHolder = null;
                try {
                    modulePriceHolder = new ModulePriceHolder(moduleForPrice);
                    modulePriceHolder.prepare();
                    if (modulePriceHolder.hasErrors()) {
                        message.reply(LocalCache.getInstance().store(modulePriceHolder));
                        return;
                    }

                    modulePriceHolder.calculateTotalCost();
                    modulePriceHolders.add(modulePriceHolder);
                } catch (Exception e) {
                    if (modulePriceHolder != null)
                        modulePriceHolder.addError("Error in calculating price:" + e.getMessage());
                    LOG.error("Error in pricing", e);
                }
            }
            ProductPriceHolder productPriceHolder;

            productPriceHolder = new ProductPriceHolder(productLineItem,modulePriceHolders,proposalHeader,proposalVersion);
            productPriceHolder.prepare();
            productPriceHolders.add(productPriceHolder);
        }

        for (ProductAddon productAddon : productAddons)
        {
            AddonPriceHolder addonPriceHolder = null;

            try {
                addonPriceHolder = new AddonPriceHolder(productAddon,proposalHeader);
                addonPriceHolder.prepare();
                if (addonPriceHolder.hasErrors()) {
                    message.reply(LocalCache.getInstance().store(addonPriceHolder));
                    return;
                }

                addonPriceHolders.add(addonPriceHolder);
            } catch (Exception e) {
                LOG.error("Error in pricing", e);
            }
        }

        setupVersionPriceHolder(proposalHeader,proposalVersion,productPriceHolders,addonPriceHolders,message);
    }

    private void setupVersionPriceHolder(ProposalHeader proposalHeader, ProposalVersion proposalVersion, List<ProductPriceHolder> productPriceHolders, List<AddonPriceHolder> addonPriceHolders, Message message) {


        VersionPriceHolder versionPriceHolder = null;
        try
        {
            versionPriceHolder = new VersionPriceHolder(proposalHeader,productPriceHolders,addonPriceHolders,proposalVersion);
            versionPriceHolder.prepare();
        }
        catch (Exception e)
        {
            if (versionPriceHolder != null) versionPriceHolder.addError("Error in calculating price:" + e.getMessage());
            LOG.error("Error in pricing", e);
        }
        if (versionPriceHolder != null) {
            message.reply(LocalCache.getInstance().store(versionPriceHolder));
        }
    }




}
