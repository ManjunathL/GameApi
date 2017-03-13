package com.mygubbi.game.proposal.price;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.AuditMaster;
import com.mygubbi.game.proposal.model.PriceMaster;
import com.mygubbi.game.proposal.model.Proposal;
import com.mygubbi.game.proposal.model.ProposalVersion;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;

/**
 * Created by Chirag on 08-01-2016.
 */

public class ProposalVersionPriceUpdateService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(ProposalVersionPriceUpdateService.class);
    public static final String UPDATE_VERSION_PRICE = "update.proposal.version.price";

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.setupPriceUpdater();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }

    private void setupPriceUpdater()
    {
        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(UPDATE_VERSION_PRICE, (Message<Integer> message) ->
        {
            ProposalVersion proposalVersion = (ProposalVersion) LocalCache.getInstance().remove(message.body());
            this.updatePriceForProposal(message, proposalVersion);
        }).completionHandler(res -> {
            LOG.info("Proposal version price update service started." + res.succeeded());
        });
    }

    private void updatePriceForProposal(Message<Integer> message, ProposalVersion proposalVersion)
    {
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY,
                LocalCache.getInstance().store(new QueryData("proposal.select.updateprice", proposalVersion)),
                (AsyncResult<Message<Integer>> dataResult) ->
                {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(dataResult.result().body());
                    if ( selectData.rows == null || selectData.rows.isEmpty())
                        {
                        message.reply(LocalCache.getInstance().store(proposalVersion));
                    }
                    else
                    {
                        for (JsonObject record : selectData.rows)
                        {
                            Proposal proposalHeader = new Proposal(record);
                            calculatePriceForModules(message, proposalVersion,proposalHeader);
                        }
                        message.reply(LocalCache.getInstance().store(proposalVersion));
                    }
                 });
    }

    private void calculatePriceForModules(Message<Integer> message, ProposalVersion proposalVersion, Proposal proposalHeader)
    {
        QueryData value = new QueryData("proposal.version.products.select", proposalVersion);
        Integer id = LocalCache.getInstance().store(value);
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> dataResult) ->
                {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(dataResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        LOG.info("No products to be updated.");
                    }
                    else
                    {
                        AuditMaster auditMaster = new AuditMaster();

                        Date priceDate = proposalHeader.getPriceDate() != null ? proposalHeader.getPriceDate() : new Date(System.currentTimeMillis());
                        double totalProposalVersionCost = 0;
                        for (JsonObject record : selectData.rows)
                        {
                            double totalProductCost = 0;
                            ProductLineItem productLineItem = new ProductLineItem(record);
                            auditMaster.setProposalId(proposalHeader.getId());
                            auditMaster.setPriceDate(priceDate);
                            auditMaster.setOldAmount(proposalVersion.getFinalAmount());
                            for (ProductModule productModule : productLineItem.getModules()) {
                                ModulePriceHolder priceHolder = new ModulePriceHolder(productModule,
                                        proposalHeader.getPcity(), priceDate);
                                priceHolder.prepare();
                                priceHolder.calculateTotalCost();
                                double totalCost = priceHolder.getTotalCost();
                                LOG.debug("Total module Cost : " + productModule.getAmount() + "|" + totalCost );
                                productModule.setAmount(totalCost);
                                productModule.setCostWoAccessories(priceHolder.getCostWoAccessories());
                                totalProductCost += totalCost;
                            }

                            for (ProductAddon productAddon : productLineItem.getAddons()) {
                                PriceMaster addonRate = RateCardService.getInstance().getAddonRate(productAddon.getCode(), priceDate, proposalHeader.getPcity());
                                productAddon.setRate(addonRate.getPrice());
                                totalProductCost += productAddon.getAmount();
                            }

                            productLineItem.setAmount(totalProductCost);
                            updateProductPrice(productLineItem);
                            totalProposalVersionCost += totalProductCost;
                        }

                        proposalVersion.setAmount(totalProposalVersionCost);
                        double finalAmount = proposalVersion.getAmount() - proposalVersion.getDiscountAmount();
                        finalAmount = finalAmount - finalAmount%10;
                        proposalVersion.setFinalAmount(finalAmount);
                        auditMaster.setNewAmount(finalAmount);
                        updateVersionPrice(proposalVersion);
                        createAuditRecord(auditMaster);
                        LOG.info("Updated...");
                    }
                });
    }

    private void updateProductPrice(ProductLineItem productLineItem)
    {
        String query = "proposal.product.update";
        Integer id = LocalCache.getInstance().store(new QueryData(query, productLineItem));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        LOG.error("Error in updating product line item in the proposal. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        LOG.info("Updated Proposal Product " + productLineItem.getId());
                    }
                });
    }

    private void updateVersionPrice(ProposalVersion proposalVersion)
    {
        String query = "proposal.version.price.update";
        Integer id = LocalCache.getInstance().store(new QueryData(query, proposalVersion));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        LOG.error("Error in updating product line item in the proposal. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        LOG.info("Updated Proposal Product " + proposalVersion.getId());
                    }
                });
    }

    private void createAuditRecord(AuditMaster auditMaster)
    {
        String query = "proposal.audit.insert";
        Integer id = LocalCache.getInstance().store(new QueryData(query, auditMaster));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        LOG.error("Error in inserting line item in the audit table. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        LOG.info("Inserted Audit Record " + auditMaster.toString());
                    }
                });
    }
}
