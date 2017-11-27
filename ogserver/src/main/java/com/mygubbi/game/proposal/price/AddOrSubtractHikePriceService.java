package com.mygubbi.game.proposal.price;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.ModuleDataService;
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
import org.apache.poi.ss.formula.functions.Code;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class AddOrSubtractHikePriceService extends AbstractVerticle
{
    private final static String HIKE_MODULE_CODE = "MG-NS-H-003";
    private final static Logger LOG = LogManager.getLogger(AddOrSubtractHikePriceService.class);
    public static final String UPDATE_DISCOUNT_OR_HIKE_FOR_PROPOSALS = "update.proposal.version.discount.orAddHike";
    public static final String UPDATE_DISCOUNT_OR_HIKE_FOR_ALL_PROPOSALS = "update.all.proposal.version.discount.orAddHike";


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
        eb.localConsumer(UPDATE_DISCOUNT_OR_HIKE_FOR_PROPOSALS, (Message<Integer> message) ->
        {
            NewPriceMaster newPriceMasterObj = (NewPriceMaster) LocalCache.getInstance().remove(message.body());
            this.pickDifferenceAmount(message, newPriceMasterObj);
        }).completionHandler(res -> {
            LOG.info("Proposal version price update service started." + res.succeeded());
        });
        eb.localConsumer(UPDATE_DISCOUNT_OR_HIKE_FOR_ALL_PROPOSALS, (Message<Integer> message) ->
        {
            QueryData requestData = new QueryData("proposal.select.all.newPriceMaster", new JsonObject());
            MessageDataHolder dataHolder = new MessageDataHolder(DatabaseService.DB_QUERY, requestData);
            new PipelineExecutor().execute(dataHolder, new AddOrSubtractHikePriceService.ProposalVersionsRetriever(message));
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

    private void pickDifferenceAmount(Message<Integer> message, NewPriceMaster newPriceMasterObj)
    {
        QueryData header = new QueryData("proposal.header", new JsonObject().put("id",newPriceMasterObj.getProposalId()));
        QueryData version_json = new QueryData("proposal.version.selectversion", new JsonObject().put("proposalId",newPriceMasterObj.getProposalId()).put("version",newPriceMasterObj.getVersion()));
        List<QueryData> queryDataList = new ArrayList<>();
        queryDataList.add(header);
        queryDataList.add(version_json);
        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY,
                LocalCache.getInstance().store(queryDataList),
                (AsyncResult<Message<Integer>> dataResult) ->
                {
                    List<QueryData> selectData = (List<QueryData>) LocalCache.getInstance().remove(dataResult.result().body());
                    if ( (selectData.get(0).rows == null || selectData.get(0).rows.isEmpty()) || (selectData.get(1).rows == null || selectData.get(1).rows.isEmpty()))
                        {
                            message.reply(LocalCache.getInstance().store(getResponseJson("Failure",newPriceMasterObj.getProposalId(),newPriceMasterObj.getVersion(),"Empty rows")));

                        }
                    else
                    {
                        ProposalHeader proposalHeader = new ProposalHeader(selectData.get(0).rows.get(0));

                        ProposalVersion proposalVersion = new ProposalVersion(selectData.get(1).rows.get(0));

                        Double differenceAmt = newPriceMasterObj.getDifferenceAmount();
                        if(differenceAmt == 0.0){
                            LOG.info("No updates Needed");
                            message.reply(LocalCache.getInstance().store(getResponseJson("Success",newPriceMasterObj.getProposalId(),newPriceMasterObj.getVersion(),"Discount updation is not needed")));
                        }
                        else
                        {
                            retrieveFirstProduct(message,proposalHeader,newPriceMasterObj,proposalVersion);
                        }
                    }
                 });
    }

    private void retrieveFirstProduct(Message<Integer> message, ProposalHeader proposalHeader, NewPriceMaster newPriceMaster, ProposalVersion proposalVersion) {
//        LOG.debug("Retrieve first product :");
//        LOG.debug("Proposal version : " + proposalVersion);
        QueryData toBeChangedPrice = new QueryData("proposal.selectfirst.product", proposalVersion);
        QueryData products = new QueryData("proposal.product.selectversion", proposalVersion);

        List<QueryData> queryDataList = new ArrayList<>();
        queryDataList.add(toBeChangedPrice);
        queryDataList.add(products);

        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY,
                LocalCache.getInstance().store(queryDataList),
                (AsyncResult<Message<Integer>> dataResult) ->
                {
                    List<QueryData> selectData = (List<QueryData>) LocalCache.getInstance().remove(dataResult.result().body());
//                    LOG.debug("Select data :" + selectData.rows.size());
                    if (selectData.get(0).rows == null || selectData.get(0).rows.isEmpty()) {
                        message.reply(LocalCache.getInstance().store(getResponseJson("Failure", proposalVersion.getProposalId(), proposalVersion.getVersion(), "NO products found")));

                    } else {
                     ProductLineItem productLineItem = new ProductLineItem(selectData.get(0).rows.get(0));
                        List<ProductLineItem> productLineItemList = new ArrayList<ProductLineItem>();
                        for (JsonObject jsonObject: selectData.get(1).rows)
                        {
                            ProductLineItem productLineItems = new ProductLineItem(jsonObject);
                            productLineItemList.add(productLineItems);
                        }

                        addHikeModule(message,proposalHeader,newPriceMaster,productLineItem,proposalVersion,productLineItemList);
                    }
                });

    }

    private void addHikeModule(Message<Integer> message, ProposalHeader proposalHeader, NewPriceMaster newPriceMaster, ProductLineItem productLineItem, ProposalVersion proposalVersion, List<ProductLineItem> productLineItemList) {
//        LOG.debug("Add hike module");

        Module module = ModuleDataService.getInstance().getModule(HIKE_MODULE_CODE);

        ShutterFinish shutterFinish = ModuleDataService.getInstance().getFinish(productLineItem.getFinishCode());

        double versionCostWoAccessory = 0.0;

        for (ProductLineItem productLineItem1 : productLineItemList)
        {
            versionCostWoAccessory += productLineItem1.getCostWoAcc();
        }

        JsonObject jsonObject = new JsonObject()
                .put(  "seq" , 0).put(  "moduleSequence" , 100)
                .put(  "unitType" , module.getUnitType())
                .put(  "extCode" , "null").put(  "extText" , "null")
                .put(  "mgCode" ,module.getCode())
                .put(  "carcass" , productLineItem.getBaseCarcassCode())
                .put(  "wallcasscode" , productLineItem.getWallCarcassCode())
                .put(  "carcassCode" , productLineItem.getBaseCarcassCode())
                .put(  "fixedCarcassCode" , "null")
                .put(  "finishType" , "default("+productLineItem.getFinishType()+")")
                .put(  "finishTypeCode" , productLineItem.getFinishType())
                .put(  "finish" , "default (" +shutterFinish.getTitle()+")")
                .put(  "finishCode" , productLineItem.getFinishCode())
                .put(  "colorCode" , productLineItem.getColorgroupCode())
                .put(  "colorName" , "null")
                .put(  "colorImagePath" , "null")
                .put(  "amount" , 0.0)
                .put(  "remarks" , "")
                .put(  "importStatus" , "m")
                .put(  "description" , module.getDescription())
                .put(  "dimension" , module.getDimension())
                .put(  "imagePath" , "image.jpg")
                .put(  "exposedRight" , false)
                .put(  "exposedLeft" , false)
                .put(  "exposedTop" , false)
                .put(  "exposedBottom" , false)
                .put(  "exposedBack" , false)
                .put(  "exposedOpen" , false)
                .put(  "area" , 0.0)
                .put(  "amountWOAccessories" , 0.0)
                .put(  "width" , newPriceMaster.getDifferenceAmount())
                .put(  "depth" , module.getDepth())
                .put(  "height" , module.getHeight())
                .put(  "moduleCategory" , module.getModuleCategory())
                .put(  "moduleType" , module.getModuleType())
                .put(  "productCategory" , productLineItem.getProductCategory())
                .put(  "moduleSource" , "button")
                .put(  "expSides" , "null")
                .put(  "expBottom" , "null")
                .put(  "accessoryPackDefault" , "No")
                .put(  "woodworkCost" , 0.0)
                .put(  "hardwareCost" , 0.0)
                .put(  "shutterCost" , 0.0)
                .put(  "carcassCost" , 0.0)
                .put(  "accessoryCost" , 0.0)
                .put(  "labourCost" , 0.0)
                .put(  "accessoryflag" , "N")
                .put(  "shutterDesign" , productLineItem.getShutterDesignCode())
                .put(  "handleType" , productLineItem.getHandleType())
                .put(  "handleFinish" , productLineItem.getHandleFinish())
                .put(  "handleThickness" , productLineItem.getHandleThickness())
                .put(  "knobType" , productLineItem.getKnobType())
                .put(  "knobFinish" , productLineItem.getKnobFinish())
                .put(  "knobThickness" , "null")
                .put(  "handlePresent" , module.getHandleMandatory())
                .put(  "knobPresent" , module.getKnobMandatory())
                .put(  "handleCode" , productLineItem.getHandleCode())
                .put(  "knobCode" , productLineItem.getKnobCode())
                .put(  "customText" , "null")
                .put(  "customCheck" , "null")
                .put(  "handleQuantity" , 0)
                .put(  "knobQuantity" , 0)
                .put(  "newModuleFlag" , "null")
                .put(  "glassType" , productLineItem.getGlass())
                .put(  "hingeType" , productLineItem.getHingeType())
                .put(  "hingePresent" , "null")
                .put(  "hingeCode" , "null")
                .put(  "hingeQuantity" , 0)
                .put(  "handleTypeSelection" , "null")
                .put(  "handleChangedFlag" , false)
                .put(  "knobChangedFlag" , false)
                .put(  "golaProfileFlag" , "null")
                .put(  "handleOverrideFlag" , "null")
                .put(  "finishSetId" , "null");

        ProductModule productModule = new ProductModule(jsonObject);

        String adminOverrideFlag = "Yes";

        ModulePriceHolder modulePriceHolder = new ModulePriceHolder(productModule,proposalHeader.getProjectCity(),proposalHeader.getPriceDate(),productLineItem,adminOverrideFlag);
        modulePriceHolder.prepare();
        modulePriceHolder.calculateTotalCost();

        ProductModule updatedProductModule = new ProductModule(modulePriceHolder.getProductModule());
        updatedProductModule.setAmount(modulePriceHolder.getTotalCost());

//        LOG.debug("update product module : " + updatedProductModule);

        List<ProductModule> productModules = productLineItem.getModules();

        productModules.add(updatedProductModule);

        productLineItem.setModules(productModules);

        double totalProductPrice = productLineItem.getAmount() + modulePriceHolder.getTotalCost();
        productLineItem.setAmount(totalProductPrice);
        productLineItem.setCostWoAccessories(productLineItem.getCostWoAcc() + modulePriceHolder.getTotalCost());
        versionCostWoAccessory += modulePriceHolder.getTotalCost();

        double totalVersionPrice = proposalVersion.getAmount() + modulePriceHolder.getTotalCost();
        proposalVersion.setAmount(totalVersionPrice);

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
            double discountAmountNew = (int)versionCostWoAccessory * (proposalVersion.getDiscountPercentage()/100);
            double finalAmount = proposalVersion.getAmount() - discountAmountNew;
            proposalVersion.setDiscountAmount(discountAmountNew);
            finalAmount = finalAmount - finalAmount%10;
            proposalVersion.setFinalAmount(finalAmount);

        }



        updateProduct(message,productLineItem,proposalHeader,proposalVersion);

    }

    private void updateProduct(Message<Integer> message, ProductLineItem productLineItem, ProposalHeader proposalHeader, ProposalVersion proposalVersion) {

//        LOG.debug("Update product");

        QueryData toBeUpdatedProduct = new QueryData("proposal.product.update", productLineItem);
        QueryData toBeUpdatedVersion = new QueryData("proposal.version.price.update", proposalVersion);
        List<QueryData> queryDataList = new ArrayList<>();
        queryDataList.add(toBeUpdatedProduct);
        queryDataList.add(toBeUpdatedVersion);

        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY,
                LocalCache.getInstance().store(queryDataList),
                (AsyncResult<Message<Integer>> dataResult) ->
                {
                    List<QueryData> resultData = (List<QueryData>) LocalCache.getInstance().remove(dataResult.result().body());
                    if (resultData.get(0).updateResult.getUpdated() == 0 || resultData.get(1).updateResult.getUpdated() == 0) {
                        message.reply(LocalCache.getInstance().store(getResponseJson("Failure", proposalHeader.getId(), proposalVersion.getVersion(), "Could not update")));

                    } else {
                       message.reply(LocalCache.getInstance().store(getResponseJson("Success", proposalHeader.getId(), proposalVersion.getVersion(), "Updated")));
                    }
                });
    }

    private class ProposalVersionsRetriever implements PipelineResponseHandler {
        private Message message;
        private Integer proposalId;
        private String version;

        public ProposalVersionsRetriever(Message message) {
            LOG.info("Message :: " + message);
            this.message = message;

        }

        public ProposalVersionsRetriever(Message message, Integer proposalId, String version) {
            this.message = message;
            this.proposalId = proposalId;
            this.version = version;
        }

        @Override
        public void handleResponse(List<MessageDataHolder> messageDataHolders) {
            QueryData resultData = (QueryData) messageDataHolders.get(0).getResponseData();
            if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty()) {
//                message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal " + proposalId + " doesn't have any versions")));
                LOG.error("Proposal " + proposalId + " doesn't have any versions");
            } else {
                List<MessageDataHolder> steps = resultData.rows.stream()
                        .map(row -> new MessageDataHolder(AddOrSubtractHikePriceService.UPDATE_DISCOUNT_OR_HIKE_FOR_PROPOSALS,new NewPriceMaster(row)))
                        .collect(Collectors.toList());
                new PipelineExecutor().execute(steps, new AddOrSubtractHikePriceService.ReportingServiceResponseHandler(message));
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



}
