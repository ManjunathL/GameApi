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
import com.mygubbi.pipeline.MessageDataHolder;
import com.mygubbi.pipeline.PipelineExecutor;
import com.mygubbi.pipeline.PipelineResponseHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Chirag on 08-01-2016.
 */

public class FinishChangeService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(FinishChangeService.class);
    public static final String UPDATE_FINISH_COPY = "update.proposal.finish";
    public static final String UPDATE_FINISH_COPY_FOR_PROPOSALS = "update.proposals.finish";




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
        eb.localConsumer(UPDATE_FINISH_COPY, (Message<Integer> message) ->
        {
            ProposalVersion proposalVersion = (ProposalVersion) LocalCache.getInstance().remove(message.body());
            this.updatePriceForProposal(message, proposalVersion);
        }).completionHandler(res -> {
            LOG.info("Proposal version price update service started." + res.succeeded());
        });
        eb.localConsumer(UPDATE_FINISH_COPY_FOR_PROPOSALS, (Message<Integer> message) ->
        {
            JsonObject paramsObj = (JsonObject) LocalCache.getInstance().remove(message.body());
            LOG.info("Params Obj = "+paramsObj);
            QueryData requestData = new QueryData("proposal.versions.list.forPriceUpdate.select", paramsObj);
            MessageDataHolder dataHolder = new MessageDataHolder(DatabaseService.DB_QUERY, requestData);
            new PipelineExecutor().execute(dataHolder, new FinishChangeService.ProposalVersionsRetriever(message));
        }).completionHandler(res -> {
            LOG.info("Proposal version price update service started." + res.succeeded());
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

    private void updatePriceForProposal(Message<Integer> message, ProposalVersion proposalVersion)
    {
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY,
                LocalCache.getInstance().store(new QueryData("proposal.select.updateprice", proposalVersion)),
                (AsyncResult<Message<Integer>> dataResult) ->
                {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(dataResult.result().body());
                    if ( selectData.rows == null || selectData.rows.isEmpty())
                        {
                        message.reply(LocalCache.getInstance().store(getResponseJson("Failure",proposalVersion.getProposalId(),proposalVersion.getVersion(),"Empty rows")));

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
        Date priceDate = proposalHeader.getPriceDate();
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
                        message.reply(LocalCache.getInstance().store(getResponseJson("Success",proposalVersion.getProposalId(),proposalVersion.getVersion(),"Success")));

                    }
                    else
                    {

                        double oldProductCost = 0;
                        double totalProposalVersionProductCost = 0;
                        double totalProposalVersionProductCostWoAccessories = 0;

                        if (proposalHeader.getPriceDate() != null) {
                            proposalHeader.setPriceDate(priceDate);

                        }
                        for (JsonObject record : selectData.get(0).rows)
                        {
                            double totalProductCost = 0;
                            double productCostWoAccessories = 0;
                            ProductLineItem productLineItem = new ProductLineItem(record);
                            String finishCode = productLineItem.getFinishCode();
                            OldToNewFinishMapping oldToNewFinishMapping = ModuleDataService.getInstance().getOldToNewMapping(finishCode,proposalHeader.getPriceDate());
                            String newCode = oldToNewFinishMapping.getNewCode();
//                            LOG.debug("New code : " + newCode);

                            productLineItem.setFinishCode(newCode);
                            ShutterFinish shutterFinish=ModuleDataService.getInstance().getFinish(newCode);
                            Collection<ColorMaster> colorMaster=ModuleDataService.getInstance().getColours(shutterFinish.getColorGroupCode());
                            String colourValue=productLineItem.getColorgroupCode();
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

                                String finishCodeModule = productLineItem.getFinishCode();
                                OldToNewFinishMapping oldToNewFinishMappingModule = ModuleDataService.getInstance().getOldToNewMapping(finishCodeModule,proposalHeader.getPriceDate());
                                String newCodeModule = oldToNewFinishMappingModule.getNewCode();

                                ShutterFinish shutterFinishModule = ModuleDataService.getInstance().getFinish(newCodeModule);

                                Collection<ColorMaster> colorMasterModule=ModuleDataService.getInstance().getColours(shutterFinish.getColorGroupCode());
                                String colourValueModule=productLineItem.getColorgroupCode();
                                String ValueModule=" ";


                                java.util.Date currentDate = new java.util.Date(117, 8, 18, 0, 0, 00);


                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                String strDate= formatter.format(currentDate);

                                Date currentDt2 = DateUtil.convertDate(strDate);

                                if (priceDate.before(currentDt2))
                                {
                                    LOG.info("Big IFF");
                                    productModule.setFinishType(productModule.getFinishType());
                                    productModule.setFinishTypeCode(productModule.getFinishTypeCode());
                                    productModule.setFinish(productModule.getFinish());
                                    productModule.setFinishCode(productModule.getFinishCode());

                                    productModule.setColorCode(productModule.getColorCode());
                                }
                                else {
                                    if (productModule.getFinishCode().equals(newCodeModule) || !productModule.getFinishCode().equals(productLineItem.getFinishCode())) {
                                        LOG.info("Inside IFF");
                                        productModule.setFinishType(productModule.getFinishType());
                                        productModule.setFinishTypeCode(productModule.getFinishTypeCode());
                                        productModule.setFinish(productModule.getFinish());
                                        productModule.setFinishCode(productModule.getFinishCode());

                                        productModule.setColorCode(productModule.getColorCode());


                                    } else {
                                        LOG.info("Inside Else");
                                        productModule.setFinishType(productModule.getFinishType());
                                        productModule.setFinishTypeCode(productModule.getFinishTypeCode());
                                        productModule.setFinish("default (" + shutterFinishModule.getTitle() + ")");
                                        productModule.setFinishCode(newCodeModule);

                                        for (ColorMaster colorMaster1 : colorMasterModule) {
                                            if (colourValueModule == null || colourValueModule.equals(colorMaster1.getCode())) {
                                                ValueModule = "P";
                                            }
                                        }
                                        if (!ValueModule.equals("P")) {
                                            productLineItem.setColorGroupCode("");
                                        }
                                    }

                                }
                                LOG.info("AFTER :: "+productModule.getMGCode()+", "+productModule.getFinishCode()+", "+productModule.getFinish()+", "+productModule.getFinishType()+", "+productModule.getFinishTypeCode());





                                ModulePriceHolder priceHolder = new ModulePriceHolder(productModule,
                                        proposalHeader.getProjectCity(), proposalHeader.getPriceDate(),productLineItem,"C");
                                priceHolder.prepare();
                                priceHolder.calculateTotalCost();
                                double totalCost = priceHolder.getTotalCost();
                                productModule.setAmount(totalCost);
                                productModule.setCostWoAccessories(priceHolder.getCostWoAccessories());
                                totalProductCost += totalCost;
                                productCostWoAccessories += priceHolder.getCostWoAccessories();
                            }

                            PriceMaster lConnectorRate = RateCardService.getInstance().getHardwareRate("H074", priceDate, proposalHeader.getProjectCity());
                            double rateForLconnectorPrice = lConnectorRate.getPrice();

                            if (productLineItem.getHandletypeSelection() != null) {
                                if (productLineItem.getHandletypeSelection().equals("Gola Profile") && productLineItem.getNoOfLengths() != 0) {
                                    totalProductCost += (productLineItem.getNoOfLengths() * rateForLconnectorPrice);
                                    productCostWoAccessories += (productLineItem.getNoOfLengths() * rateForLconnectorPrice);
                                }
                            }
                            totalProductCost = this.round(totalProductCost,0);

                            productLineItem.setAmount(totalProductCost);
                            productLineItem.setCostWoAccessories(productCostWoAccessories);


                            String query = "proposal.product.update";
                            queryDataList.add(new QueryData(query, productLineItem));
                            totalProposalVersionProductCost += totalProductCost;
                            totalProposalVersionProductCostWoAccessories += productCostWoAccessories;
                        }

                        totalProposalVersionProductCost = this.round(totalProposalVersionProductCost,0);
                        totalProposalVersionProductCostWoAccessories = this.round(totalProposalVersionProductCostWoAccessories,0);

                        proposalVersion.setAmount(totalProposalVersionProductCost);

                        java.util.Date date = proposalHeader.getCreatedOn();
                        java.util.Date currentDate = new java.util.Date(117, 3, 20, 0, 0, 00);
                        if (date.after(currentDate)) {
                            double discountAmountNew = (int)proposalVersion.getAmount() * (proposalVersion.getDiscountPercentage()/100);
                            double finalAmount = proposalVersion.getAmount() - discountAmountNew;
                            proposalVersion.setDiscountAmount(discountAmountNew);
                            finalAmount = finalAmount - finalAmount%10;
                            proposalVersion.setFinalAmount(finalAmount);
                        }
                        else
                        {

                            double discountAmountNew = (int)totalProposalVersionProductCostWoAccessories * (proposalVersion.getDiscountPercentage()/100);
                            double finalAmount = proposalVersion.getAmount() - discountAmountNew;
                            proposalVersion.setDiscountAmount(discountAmountNew);
                            finalAmount = finalAmount - finalAmount%10;
                            proposalVersion.setFinalAmount(finalAmount);
                        }


                        List<JsonObject> addon_jsons = selectData.get(1).rows;
                        calculatePriceForAddons(message, proposalVersion,proposalHeader,queryDataList, addon_jsons);


//                        LOG.info("Updated...");
                    }
                });


    }

    private void calculatePriceForAddons(Message<Integer> message, ProposalVersion proposalVersion, ProposalHeader proposalHeader, List<QueryData> queryDataList, List<JsonObject> addon_jsons) {
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
                        totalVersionAmount = this.round(totalVersionAmount,0);
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
                        message.reply(LocalCache.getInstance().store(getResponseJson("Success",proposalVersion.getProposalId(),proposalVersion.getVersion(),"Success")));

                    }

                });
    }

    private class ProposalVersionsRetriever implements PipelineResponseHandler
    {
        private Message message;
        private Integer proposalId;
        private String version;

        public ProposalVersionsRetriever(Message message)
        {
            LOG.info("Message :: "+message);
            this.message = message;

        }

        public ProposalVersionsRetriever(Message message, Integer proposalId,String version)
        {
            this.message = message;
            this.proposalId = proposalId;
            this.version = version;
        }

        @Override
        public void handleResponse(List<MessageDataHolder> messageDataHolders)
        {
            QueryData resultData = (QueryData) messageDataHolders.get(0).getResponseData();
            if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty())
            {
//                message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal " + proposalId + " doesn't have any versions")));
                LOG.error("Proposal " + proposalId + " doesn't have any versions");
            }
            else
            {
                List<MessageDataHolder> steps = resultData.rows.stream()
                        .map(row -> new MessageDataHolder(FinishChangeService.UPDATE_FINISH_COPY, new ProposalVersion(row)))
                        .collect(Collectors.toList());
                new PipelineExecutor().execute(steps, new FinishChangeService.ReportingServiceResponseHandler(message));
            }
        }

    }

    private class ReportingServiceResponseHandler implements PipelineResponseHandler
    {
        private Message message;

        public ReportingServiceResponseHandler(Message message)
        {
            this.message = message;
        }

        @Override
        public void handleResponse(List<MessageDataHolder> messageDataHolders)
        {
            StringBuilder comments = new StringBuilder();
            for(int i=0;i<messageDataHolders.size();i++){

                JsonObject response = (JsonObject) messageDataHolders.get(i).getResponseData();
                if(response.containsKey("status")){
                    if(response.getString("status").equalsIgnoreCase("FAILURE")){
                        int proposalId = response.getInteger("proposalId");
                        String version = response.getString("version");
                        comments.append(proposalId+"-"+version+", ");
                    }
                }
            }
            if(comments.toString().length() == 0){
                comments.append("Successfully ran for all proposals");
            }
            LOG.debug("Reporting service returned :: "+messageDataHolders.get(0).getResponseData());
            message.reply(LocalCache.getInstance().store(new JsonObject().put("status","success")));


        }
    }

    private double round(double value, int places)
    {
        if (places < 0)
        {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


}
