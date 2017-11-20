package com.mygubbi.game.proposal.price;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Chirag on 08-01-2016.
 */

public class ProposalVersionPriceUpdateServiceCopy extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(ProposalVersionPriceUpdateServiceCopy.class);
    public static final String UPDATE_VERSION_PRICE_COPY = "update.proposal.version.copy";




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
        eb.localConsumer(UPDATE_VERSION_PRICE_COPY, (Message<Integer> message) ->
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
                        double versionAmount = 0;
                            ProposalHeader proposalHeader = new ProposalHeader(selectData.rows.get(0));
                            calculatePriceForModules(message, proposalVersion,proposalHeader, versionAmount);
                    }
                 });
    }


    private void calculatePriceForModules(Message<Integer> message, ProposalVersion proposalVersion, ProposalHeader proposalHeader, double versionAmount)
    {

        QueryData products = new QueryData("proposal.version.products.select", proposalVersion);
        QueryData addons = new QueryData("proposal.version.addons.select", proposalVersion);
        List<QueryData> select_QueryData = new ArrayList<>();
        select_QueryData.add(products);
        select_QueryData.add(addons);
        List<QueryData> queryDataList = new ArrayList<>();
        Date priceDate = new Date(System.currentTimeMillis());
        Integer id = LocalCache.getInstance().store(select_QueryData);
        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id,
                (AsyncResult<Message<Integer>> dataResult) ->
                {
                    proposalVersion.setAmount(0);
                    proposalVersion.setFinalAmount(0);
                    List<QueryData> selectData = (List<QueryData>) LocalCache.getInstance().remove(dataResult.result().body());
                    if ((selectData.get(0) == null || selectData.get(0).rows == null || selectData.get(0).rows.isEmpty()) &&
                            (selectData.get(1) == null || selectData.get(1).rows == null || selectData.get(1).rows.isEmpty()))
                    {
                        LOG.info("No products and addons to be updated.");
                    }
                    else
                    {

                        double oldProductCost = 0;
                        double totalProposalVersionProductCost = 0;

                        if (proposalHeader.getPriceDate() != null) {
                            proposalHeader.setPriceDate(priceDate);

                        }
                        for (JsonObject record : selectData.get(0).rows)
                        {
                            double totalProductCost = 0;
                            ProductLineItem productLineItem = new ProductLineItem(record);
                            OldToNewFinishMapping oldToNewFinishMapping = ModuleDataService.getInstance().getOldToNewMapping(productLineItem.getFinishCode(),proposalHeader.getPriceDate());
                            String newCode = oldToNewFinishMapping.getNewCode();
//                            LOG.debug("New code : " + newCode);
                            productLineItem.setFinishCode(newCode);
                            ShutterFinish shutterFinish=ModuleDataService.getInstance().getFinish(newCode);
                            Collection<ColorMaster> colorMaster=ModuleDataService.getInstance().getColours(shutterFinish.getColorGroupCode());
                            String colourValue=productLineItem.getColorgroupCode();
                            String newColorValue="";
                            String Value=" ";
                            for(ColorMaster colorMaster1:colorMaster)
                            {
                                if(colourValue ==  null || colourValue.equals(colorMaster1.getCode()))
                                {
                                    Value="P";
                                }
                            }
                           if(!Value.equals("P"))
                            {
                                productLineItem.setColorGroupCode("");
                            }

                            oldProductCost += productLineItem.getAmount();


                            for (ProductModule productModule : productLineItem.getModules()) {
                                productModule.setFinishCode(newCode);
                                productModule.setColorCode(productLineItem.getColorgroupCode());
                                productModule.setFinish("default("+ oldToNewFinishMapping.getTitle() +")");
                                ModulePriceHolder priceHolder = new ModulePriceHolder(productModule,
                                        proposalHeader.getProjectCity(), proposalHeader.getPriceDate(),productLineItem,"C");
                                priceHolder.prepare();
                                priceHolder.calculateTotalCost();
                                double totalCost = priceHolder.getTotalCost();
                                productModule.setAmount(totalCost);
                                productModule.setCostWoAccessories(priceHolder.getCostWoAccessories());
                                totalProductCost += totalCost;
                            }

                            PriceMaster lConnectorRate = RateCardService.getInstance().getHardwareRate("H074", priceDate, proposalHeader.getProjectCity());
                            double rateForLconnectorPrice = lConnectorRate.getPrice();

                            if (productLineItem.getHandletypeSelection() != null) {
                                if (productLineItem.getHandletypeSelection().equals("Gola Profile") && productLineItem.getNoOfLengths() != 0) {
                                    totalProductCost += (productLineItem.getNoOfLengths() * rateForLconnectorPrice);
                                }
                            }

                            productLineItem.setAmount(totalProductCost);

//                            updateProductPrice(productLineItem);
                            String query = "proposal.product.update";
                            queryDataList.add(new QueryData(query, productLineItem));
                            totalProposalVersionProductCost += totalProductCost;
//                            LOG.debug("Product COst : " + totalProductCost);
                        }

//                        LOG.debug("Total Product COst : " + totalProposalVersionProductCost);

                        proposalVersion.setAmount(totalProposalVersionProductCost);

                        double discountAmountNew = (int)proposalVersion.getAmount() * (proposalVersion.getDiscountPercentage()/100);
                        double finalAmount = proposalVersion.getAmount() - discountAmountNew;
                        proposalVersion.setDiscountAmount(discountAmountNew);
                        finalAmount = finalAmount - finalAmount%10;
                        proposalVersion.setFinalAmount(finalAmount);

                        List<JsonObject> addon_jsons = selectData.get(1).rows;
                        calculatePriceForAddons(message, proposalVersion,proposalHeader,queryDataList, addon_jsons);


//                        LOG.info("Updated...");
                    }
                });


    }

    private void calculatePriceForAddons(Message<Integer> message, ProposalVersion proposalVersion, ProposalHeader proposalHeader, List<QueryData> queryDataList, List<JsonObject> addon_jsons) {
//        LOG.debug("FOr addons : " + proposalVersion);
        Date priceDate = new Date(System.currentTimeMillis());
        if (proposalHeader.getPriceDate() == null) {
            proposalHeader.setPriceDate(priceDate);

        }

                    double newTotalVersionAddonCost = 0;
                    double totalVersionAddonCost = 0;


                        for (JsonObject record : addon_jsons)
                        {
                            ProductAddon addonLineItem = new ProductAddon(record);
                        }


                        for (JsonObject record : addon_jsons) {
                            ProductAddon addonLineItem = new ProductAddon(record);
                            if (addonLineItem.isCustomAddon())
                            {
                                newTotalVersionAddonCost += addonLineItem.getAmount();
                                continue;
                            }
                            PriceMaster addonRate = RateCardService.getInstance().getAddonRate(addonLineItem.getCode(), proposalHeader.getPriceDate(), proposalHeader.getProjectCity());
//                            newTotalVersionAddonCost += addonRate.getPrice();
                            addonLineItem.setRate(addonRate.getPrice());
                            addonLineItem.setAmount(addonRate.getPrice() * addonLineItem.getQuantity());
                            addonLineItem.setFromVersion(proposalVersion.getVersion());
                            if (addonLineItem.getRate() == 0)
                            {
                                deleteAddonFromNewQuotation(addonLineItem);
                            }
                            else {
                                totalVersionAddonCost += addonLineItem.getAmount();
//                                LOG.debug("Addon cost : " + addonLineItem.getAmount());
//                                updateAddonPrice(addonLineItem);
                                queryDataList.add(new QueryData("proposal.version.addon.update",addonLineItem));
                            }
                        }
//                        LOG.debug("Total Addon cost : " + newTotalVersionAddonCost);
                        double totalVersionAmount = proposalVersion.getAmount() + newTotalVersionAddonCost + totalVersionAddonCost;
//                        LOG.debug("Total Version cost : " + totalVersionAmount);
                        proposalVersion.setAmount(totalVersionAmount);
                        double finalAmount = proposalVersion.getAmount() - proposalVersion.getDiscountAmount();
                        finalAmount = finalAmount - finalAmount%10;
                        proposalVersion.setFinalAmount(finalAmount);

                        updateVersionPrice(message,proposalVersion,false,queryDataList);


    }


    private void deleteAddonFromNewQuotation(ProductAddon addonLineItem)
    {
        String query = "proposal.addon.remove";
        Integer id = LocalCache.getInstance().store(new QueryData(query, addonLineItem));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag)
                    {
                        LOG.error("Error in deleting addon line item in the proposal. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        LOG.info("Deleted Proposal Addon " + addonLineItem.getProposalId());
                    }
                });
    }

    private void updateVersionPrice(Message<Integer> message,ProposalVersion proposalVersion,boolean updateAddon,List<QueryData> queryDataList)
    {
//        LOG.debug("FOr version : " + proposalVersion);
        queryDataList.add(new QueryData("proposal.version.price.update",proposalVersion));
        Integer id = LocalCache.getInstance().store(queryDataList);
        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    List<QueryData> resultData = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.get(0).errorFlag || resultData.get(0).updateResult.getUpdated() == 0)
                    {
                        LOG.error("Error in updating product line item in the proposal. " + resultData.get(0).errorMessage, resultData.get(0).error);
                    }
                    else
                    {
//                        message.reply(LocalCache.getInstance().store(proposalVersion));
                    }

                });
    }


}
