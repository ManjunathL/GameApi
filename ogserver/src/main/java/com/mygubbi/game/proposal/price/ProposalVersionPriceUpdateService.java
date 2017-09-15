package com.mygubbi.game.proposal.price;

import com.mygubbi.common.DateUtil;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.*;
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
                            Proposal proposalHeader = new Proposal(selectData.rows.get(0));
                            calculatePriceForModules(message, proposalVersion,proposalHeader);
                            calculatePriceForAddons(message, proposalVersion,proposalHeader);
                    }
                 });
    }


    private void calculatePriceForModules(Message<Integer> message, ProposalVersion proposalVersion, Proposal proposalHeader)
    {
        QueryData value = new QueryData("proposal.version.products.select", proposalVersion);
        final Date[] priceDate = {new Date(System.currentTimeMillis())};
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
                        double totalProposalVersionProductCost = 0;
                        double oldProductCost = 0;

                        if (proposalHeader.getPriceDate() != null) {
                            String priceDate1 = proposalHeader.getPriceDate();
                            priceDate[0] = DateUtil.convertDate(priceDate1);
                        }
                        for (JsonObject record : selectData.rows)
                        {
                            double totalProductCost = 0;
                            ProductLineItem productLineItem = new ProductLineItem(record);
                            OldToNewFinishMapping oldToNewFinishMapping = ModuleDataService.getInstance().getOldToNewMapping(productLineItem.getFinishCode());
                            productLineItem.setFinishCode(oldToNewFinishMapping.getNewCode());
                            auditMaster.setProposalId(proposalHeader.getId());
                            auditMaster.setPriceDate(priceDate[0]);
                            auditMaster.setVersion(proposalVersion.getVersion());
                            oldProductCost += productLineItem.getAmount();


                            for (ProductModule productModule : productLineItem.getModules()) {
                                productModule.setFinishCode(oldToNewFinishMapping.getNewCode());
                                ModulePriceHolder priceHolder = new ModulePriceHolder(productModule,
                                        proposalHeader.getPcity(), priceDate[0],productLineItem,"C");
                                priceHolder.prepare();
                                priceHolder.calculateTotalCost();
                                double totalCost = priceHolder.getTotalCost();
                                productModule.setAmount(totalCost);
                                productModule.setCostWoAccessories(priceHolder.getCostWoAccessories());
                                totalProductCost += totalCost;
                            }

                            for (ProductAddon productAddon : productLineItem.getAddons()) {
                                PriceMaster addonRate = RateCardService.getInstance().getAddonRate(productAddon.getCode(), priceDate[0], proposalHeader.getPcity());
                                productAddon.setRate(addonRate.getPrice());
                                totalProductCost += productAddon.getAmount();
                            }
                            totalProductCost = ((int) totalProductCost);

                            productLineItem.setAmount(totalProductCost);

                            updateProductPrice(productLineItem);
                            totalProposalVersionProductCost += totalProductCost;
                        }

                        proposalVersion.setAmount(totalProposalVersionProductCost);

                        double discountAmountNew = (int)proposalVersion.getAmount() * (proposalVersion.getDiscountPercentage()/100);
                        double finalAmount = proposalVersion.getAmount() - discountAmountNew;
                        proposalVersion.setDiscountAmount(discountAmountNew);
                        finalAmount = finalAmount - finalAmount%10;
                        proposalVersion.setFinalAmount(finalAmount);
                        auditMaster.setOldAmountProduct(oldProductCost);
                        auditMaster.setNewAmountProduct(totalProposalVersionProductCost);

                        updateVersionPrice(message,proposalVersion,auditMaster,true);

                        LOG.info("Updated...");
                    }
                });
    }

    private void calculatePriceForAddons(Message<Integer> message, ProposalVersion proposalVersion, Proposal proposalHeader) {
        QueryData value = new QueryData("proposal.version.addons.select", proposalVersion);
        Date priceDate = new Date(System.currentTimeMillis());
        Integer id = LocalCache.getInstance().store(value);
        AuditMaster auditMaster = new AuditMaster();
        if (proposalHeader.getPriceDate() != null) {
            String priceDate1 = proposalHeader.getPriceDate();
            priceDate = DateUtil.convertDate(priceDate1);
        }
        LOG.debug("Price Date : " + priceDate);
        LOG.debug("Price Date from proposal header : " + proposalHeader.getPriceDate());


        final Date finalPriceDate = priceDate;
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> dataResult) ->
                {
                    double newTotalVersionAddonCost = 0;
                    double totalVersionAddonCost = 0;

                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(dataResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        LOG.info("No Addons to be updated.");
                    }
                    else
                    {
                        LOG.debug("hi");

                        for (JsonObject record : selectData.rows)
                        {
                            ProductAddon addonLineItem = new ProductAddon(record);
                            totalVersionAddonCost += addonLineItem.getAmount();
                        }
                        auditMaster.setOldAmountAddon(totalVersionAddonCost);


                        for (JsonObject record : selectData.rows) {
                            ProductAddon addonLineItem = new ProductAddon(record);
                            if (addonLineItem.isCustomAddon())
                            {
                                newTotalVersionAddonCost += addonLineItem.getAmount();
                                continue;
                            }
                            PriceMaster addonRate = RateCardService.getInstance().getAddonRate(addonLineItem.getCode(), finalPriceDate, proposalHeader.getPcity());
                            newTotalVersionAddonCost += addonRate.getPrice();
                            addonLineItem.setRate(addonRate.getPrice());
                            addonLineItem.setAmount(addonRate.getPrice() * addonLineItem.getQuantity());
                            addonLineItem.setFromVersion(proposalVersion.getVersion());
                            updateAddonPrice(addonLineItem);
                        }
                        double totalVersionAmount = proposalVersion.getAmount() + newTotalVersionAddonCost;
                        proposalVersion.setAmount(totalVersionAmount);
                        double finalAmount = proposalVersion.getAmount() - proposalVersion.getDiscountAmount();
                        finalAmount = finalAmount - finalAmount%10;
                        proposalVersion.setFinalAmount(finalAmount);

                        LOG.debug("Update version price before :" + proposalVersion.toString());


                        auditMaster.setProposalId(proposalHeader.getId());
                        auditMaster.setVersion(proposalVersion.getVersion());
                        auditMaster.setPriceDate(finalPriceDate);
                        auditMaster.setNewAmountAddon(newTotalVersionAddonCost);

                        updateVersionPrice(message,proposalVersion,auditMaster,false);



                    }

                });
    }

    private void updateProductPrice(ProductLineItem productLineItem)
    {
        LOG.info("update product " +productLineItem.toString());
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

    private void updateAddonPrice(ProductAddon addonLineItem)
    {
        String query = "proposal.version.addon.update";
        Integer id = LocalCache.getInstance().store(new QueryData(query, addonLineItem));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag)
                    {
                        LOG.error("Error in updating addon line item in the proposal. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        LOG.info("Updated Proposal Addon " + addonLineItem.getProposalId());
                    }
                });
    }

    private void updateVersionPrice(Message<Integer> message,ProposalVersion proposalVersion, AuditMaster auditMaster,boolean updateAddon)
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
                        createAuditRecord(message,auditMaster,updateAddon,proposalVersion);

                        LOG.info("Updated Proposal Product " + proposalVersion.getId());
                    }

                });
    }

    private void createAuditRecord(Message<Integer> message,AuditMaster auditMaster,boolean updateAddonFlag, ProposalVersion proposalVersion)
    {

        String query = updateAddonFlag ? "proposal.audit.insert" : "proposal.audit.addon.update";
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
                    message.reply(LocalCache.getInstance().store(proposalVersion));
                });
    }

    /*private void updateAuditRecordForAddons(AuditMaster auditMaster)
    {
        String query = "proposal.audit.addon.update";
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
    }*/
}
