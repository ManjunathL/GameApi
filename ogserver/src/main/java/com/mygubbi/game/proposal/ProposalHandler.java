package com.mygubbi.game.proposal;

import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfPage;
import com.mygubbi.common.DateUtil;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.StringUtils;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.erp.BOQWriteToDatabase;
import com.mygubbi.game.proposal.model.PriceMaster;
import com.mygubbi.game.proposal.model.SOWMaster;
import com.mygubbi.game.proposal.output.ProposalOutputCreator;
import com.mygubbi.game.proposal.output.ProposalOutputService;
import com.mygubbi.game.proposal.price.ProposalPricingUpdateService;
import com.mygubbi.game.proposal.price.RateCardService;
import com.mygubbi.game.proposal.quote.MergePdfsRequest;
import com.mygubbi.game.proposal.quote.PdfMerger;
import com.mygubbi.game.proposal.sow.SOWCreatorService;
import com.mygubbi.game.proposal.sow.SOWWriteToDatabase;
import com.mygubbi.game.proposal.quote.QuoteRequest;
import com.mygubbi.game.proposal.quote.SowPdfRequest;
import com.mygubbi.route.AbstractRouteHandler;
import com.mygubbi.game.proposal.output.SOWPdfOutputService;
import com.mygubbi.si.gdrive.DriveServiceProvider;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.File;
import java.sql.Date;
import java.util.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by sunil on 25-04-2016.
 */
public class ProposalHandler extends AbstractRouteHandler
{
    private final static Logger LOG = LogManager.getLogger(ProposalHandler.class);

    private String proposalDocsFolder = null;

    public DriveServiceProvider serviceProvider;
    public SOWWriteToDatabase sowWriteToDatabase;
    public BOQWriteToDatabase boqWriteToDatabase;

    public ProposalHandler(Vertx vertx)
    {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/list").handler(this::getProposals);
        this.post("/create").handler(this::createProposal);
        this.post("/version/publishoverride").handler(this::publishTheProposal);
        this.post("/version/publish").handler(this::publishVersionAfterValidation);
        this.post("/version/createdraft").handler(this::createInitialDraftProposal);
        this.post("/version/createPostSalesInitial").handler(this::createPostSalesInitial);
        this.post("/version/createversion").handler(this::createProposalVersion);
        this.post("/version/copyversion").handler(this::copyVersion);
        this.post("/update").handler(this::updateProposal);
        this.post("/updateonconfirm").handler(this::updateProposalOnConfirm);
        this.post("/downloadquote").handler(this::downloadQuote);
        this.post("/downloadquoteansow").handler(this::downloadQuoteAndSow);
        this.post("/downloadjobcard").handler(this::downloadJobCard);
        this.post("/downloadsalesorder").handler(this::downloadSalesOrder);
        this.post("/createsowsheet").handler(this::createSowSheet);
        //this.post("/createboqsheet").handler(this::createBoqSheet);
        this.post("/savesowfile").handler(this::saveSowFile);
        this.post("/discardsowfile").handler(this::discardSowFile);
        this.post("/copysowlineitems").handler(this::copySowLineItems);
        this.post("/createboqlineitems").handler(this::createBoqLineItems);
        this.post("/saveboqfile").handler(this::saveBoqFile);
        this.post("/discardboqfile").handler(this::discardBoqFile);
        //this.post("/downloadprodspecfile").handler(this::downloadProdSpec);
        this.post("/downloadquotePdf").handler(this::downloadQuotePdf);
        this.get("/hardwareratedetails").handler(this::getHardwareRate);
        this.get("/accratedetails").handler(this::getAccessoryRate);
        this.get("/handleknobdetails").handler(this::getHandleKnobDetails);
        this.get("/hingedetails").handler(this::getHingeDetails);
        this.post("/updatepricefordraftproposals").handler(this::updatePriceForDraftProposals);
        this.get("/ratefactordetailsfromhandler").handler(this::getRateFactor);
        this.proposalDocsFolder = ConfigHolder.getInstance().getStringValue("proposal_docs_folder", "/tmp/");
        LOG.info("this.proposalDocsFolder:" + this.proposalDocsFolder);
    }

    private void updatePriceForDraftProposals(RoutingContext context) {

        JsonObject updatePriceJsonData = new JsonObject();
        updatePriceJsonData.put("status", false);
        int id = LocalCache.getInstance().store(updatePriceJsonData);
        VertxInstance.get().eventBus().send(ProposalPricingUpdateService.RETRIEVE_DRAFT_PROPOSALS, id,
                (AsyncResult<Message<Integer>> selectResult) ->
                {
                    JsonObject resultData = (JsonObject) LocalCache.getInstance().remove(selectResult.result().body());
                    if (!resultData.getBoolean("status"))
                    {
                        sendError(context, "Error in updating version.");
                        LOG.error("Error in updating version.");
                    }
                    else
                    {
                        sendJsonResponse(context,"Successfully updated the Quotations");
                        LOG.info("Successfully updated the Quotations");
                    }
                });
    }

    private void createInitialDraftProposal(RoutingContext routingContext) {
        JsonObject versionData = routingContext.getBodyAsJson();
        LOG.debug("routing context :" + versionData.encodePrettily());
        Integer id = LocalCache.getInstance().store(new QueryData("version.createdraft", versionData));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in creating version.");
                    }
                    else
                    {
                        sendJsonResponse(routingContext, versionData.toString());
                    }
                });
    }

    private void publishVersionAfterValidation(RoutingContext context){
        JsonObject contextJson = context.getBodyAsJson();
        LOG.info("publishVersionAfterValidation Routing context:: "+contextJson);

        String verFromProposal = String.valueOf(contextJson.getDouble("version"));
        String sowVersion = null ;
        if(verFromProposal.contains("0.")){
            sowVersion = "1.0";
        }else if(verFromProposal.contains("1.")){
            sowVersion = "2.0";
        }else{
            LOG.info("INVALID VERSION and VERSION IS::"+verFromProposal);
        }

        JsonObject queryParams =  new JsonObject();
        queryParams.put("proposalId", contextJson.getInteger("proposalId"));
        queryParams.put("sowversion", sowVersion);
        queryParams.put("version",verFromProposal);

        LOG.info("Query Params :: "+queryParams);
        List spaceService = new ArrayList();
        Set proposalSpaceRoomset = new HashSet();
        List proposalSpaceRoomList =  new ArrayList();
        JsonObject response = new JsonObject();
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.sow.select.proposalversion", queryParams));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag )
                    {
                        sendError(context, "Error in getting proposals.");
                        LOG.error("Error in getting proposal. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        if(resultData.rows.size() == 0) {
                            LOG.info("SOW ROWS NOT THERE");
                            response.put("status","Failure");
                            response.put("comments", "No associated SOW");
                            LOG.info("Response is :: "+response);
                            sendJsonResponse(context, response.toString());
                        }else{
                            Boolean serMadeNo = false;
                            for(int i=0; i < resultData.rows.size();i++) {
                                JsonObject row = resultData.rows.get(i);
                                LOG.info("row :: " + row);
                                LOG.info("row :: " + row.getString("spaceType") + "," + row.getString("roomcode"));
                                spaceService.add(row);
                                LOG.info("L1S01 = " + row.getString("L1S01"));
                                String strL1Code = row.getString("L1S01");
                                if (strL1Code.equalsIgnoreCase("Yes")) {
                                    proposalSpaceRoomset.add(row.getString("spaceType") + "_" + row.getString("roomcode"));
                                } else if (strL1Code.equalsIgnoreCase("No")) {
                                    serMadeNo = true;
                                }
                            }

                            if(proposalSpaceRoomset.size() ==  0 && serMadeNo){
                                proposalSpaceRoomList.addAll(proposalSpaceRoomset);
                                checkifAddonsAreThere(context,spaceService,proposalSpaceRoomList,queryParams);

                            }else {
                                spaceService.forEach(item -> LOG.info(item));
                                proposalSpaceRoomList.addAll(proposalSpaceRoomset);
                                getListOfDistintSpaces(context, spaceService, proposalSpaceRoomList, queryParams);
                            }
                        }
                    }
                });
    }

    private void checkifAddonsAreThere(RoutingContext routingContext,List spaceService,List proposalSpaceRoomList,JsonObject params) {
        JsonObject queryParams =  new JsonObject();
        Integer proposalId = routingContext.getBodyAsJson().getInteger("proposalId");
        Double version = routingContext.getBodyAsJson().getDouble("version");
        queryParams.put("proposalId", proposalId);
        queryParams.put("fromVersion", version);

        Integer id = LocalCache.getInstance().store(new QueryData("select.proposalAddOns.sow", queryParams));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,(AsyncResult<Message<Integer>> selectResult) -> {
            QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
            if (resultData.errorFlag) {
                sendError(routingContext, "Error in getting spaces.");
                LOG.error("Error in getting spaces. " + resultData.errorMessage, resultData.error);
            } else {
                if (resultData.rows.size() == 0) {
                    publishTheProposal(routingContext);
                }else{
                    getListOfDistintSpaces(routingContext, spaceService, proposalSpaceRoomList, queryParams);
                }
            }

        });
    }

    private void getListOfDistintSpaces(RoutingContext routingContext,List spaceService,List proposalSpaceRoomList,JsonObject params) {
        JsonObject queryParams =  new JsonObject();
        Integer proposalId = routingContext.getBodyAsJson().getInteger("proposalId");
        Double version = routingContext.getBodyAsJson().getDouble("version");
        queryParams.put("proposalIdForPro", proposalId);
        queryParams.put("fromVerForProd", version);
        queryParams.put("proposalIdForAddOn", proposalId);
        queryParams.put("fromVerForAddOn", version);

        JsonObject response = new JsonObject();
        List distinctSpacesList = new ArrayList();

        Integer id = LocalCache.getInstance().store(new QueryData("spaces.select.fromProductAndAddon", queryParams));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,(AsyncResult<Message<Integer>> selectResult) -> {
            QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
            if (resultData.errorFlag )
            {
                sendError(routingContext, "Error in getting spaces.");
                LOG.error("Error in getting spaces. " + resultData.errorMessage, resultData.error);
            }
            else
            {
                if(resultData.rows.size() == 0) {
                    LOG.info("NO SPACES ADDED FOR PRODUCT/ADDON");
                    response.put("status","Failure");
                    response.put("comments", "No associated Spaces");
                    LOG.info("Response is :: "+response);
                    sendJsonResponse(routingContext, response.toString());
                }else{
                    resultData.rows.forEach(row ->{
                                distinctSpacesList.add(row.getString("spaceType")+"_"+row.getString("roomCode"));
                        });

                    LOG.info("proposalSpaceRoomList = ");
                    proposalSpaceRoomList.forEach(item->LOG.info(item));
                    LOG.info("distinctSpacesList = ");
                    distinctSpacesList.forEach(item->LOG.info(item));
                    List ls1 = compareLists(new ArrayList<>(proposalSpaceRoomList),new ArrayList<>(distinctSpacesList));
                    List ls2 = compareLists(new ArrayList<>(distinctSpacesList),new ArrayList<>(proposalSpaceRoomList));

                    if(ls1.size() > 0 ){
                        StringBuilder val = new StringBuilder();
                        ls1.forEach(item->val.append(item+","));
                        response.put("status","Failure");
                        response.put("comments","There are entries in SOW, but no addons for following SpaceType_Room : "+val.deleteCharAt(val.lastIndexOf(",")));
                        response.put("params",ls1);
                        LOG.info("Response is :: "+response);
                        sendJsonResponse(routingContext, response.toString());
                    }else if(ls2.size() > 0 ){
                        StringBuilder val = new StringBuilder();
                        ls2.forEach(item->val.append(item+","));
                        response.put("status","Failure");
                        response.put("comments","Please add the SOWs for the following SpaceType_Room : "+val.deleteCharAt(val.lastIndexOf(",")));
                        response.put("params",ls2);
                        LOG.info("Response is :: "+response);
                        sendJsonResponse(routingContext, response.toString());
                    }else{
                       getListOfAddonCodesFromSowServiceMap(routingContext,spaceService,params);
                    }
                }
            }
        });

    }
    private void getListOfAddonCodesFromSowServiceMap(RoutingContext routingContext,List
            <JsonObject>spaceServiceFromSow,JsonObject params) {
        JsonObject response = new JsonObject();
        List addOnsFromsowServiceMp = new ArrayList();
        Integer id = LocalCache.getInstance().store(new QueryData("select.sowservicemap", params));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,(AsyncResult<Message<Integer>> selectResult) -> {
            QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
            if (resultData.errorFlag) {
                sendError(routingContext, "Error in getting addon codes.");
                LOG.error("Error in getting addon codes. " + resultData.errorMessage, resultData.error);
            } else {
                if (resultData.rows.size() == 0) {
                    LOG.info("NO ADDONS ARE SELECTED IN SOW SHEET");
                    response.put("status","Failure");
                    response.put("comments", "No addons are there for SOW");
                    sendJsonResponse(routingContext, response.toString());
                } else {
//                    resultData.rows.forEach(row->addOnsFromsowServiceMp.add(row.getString("addonCode")));
                    resultData.rows.forEach(row->addOnsFromsowServiceMp.add(row));
                    //get the service code also and add to diff list
                    getListOfAddOnCodesFromProductAddons(routingContext,spaceServiceFromSow,addOnsFromsowServiceMp);

                }
            }
            });

    }

    private void getListOfAddOnCodesFromProductAddons(RoutingContext routingContext,List
            <JsonObject>spaceServiceFromSow,List addOnsFromsowServiceMp) {
        JsonObject queryParams =  new JsonObject();
        queryParams.put("proposalId", routingContext.getBodyAsJson().getInteger("proposalId"));
        queryParams.put("fromVersion", routingContext.getBodyAsJson().getDouble("version"));

        JsonObject response = new JsonObject();
        List addOnsFromProductAddons = new ArrayList();
        Integer id = LocalCache.getInstance().store(new QueryData("select.proposalAddOns", queryParams));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,(AsyncResult<Message<Integer>> selectResult) -> {
            QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
            if (resultData.errorFlag) {
                sendError(routingContext, "Error in getting addon codes.");
                LOG.error("Error in getting addon codes. " + resultData.errorMessage, resultData.error);
            } else {
                if (resultData.rows.size() == 0) {
                    LOG.info("NO ADDONS ARE THERE FOR SERVICE IN PRODUCTS" );
                    // get services which don't have
                    response.put("status","Failure");
                    response.put("comments", "No addons are there for service " );
                    sendJsonResponse(routingContext, response.toString());
                } else {
//                    resultData.rows.forEach(row->addOnsFromProductAddons.add(row.getString("code")));

                    resultData.rows.forEach(row->addOnsFromProductAddons.add(row));
                    LOG.info("addOnsFromsowServiceMp ::");
                    addOnsFromsowServiceMp.forEach(item->LOG.info(item));

                    LOG.info("addOnsFromProductAddons ::");
                    addOnsFromProductAddons.forEach(item->LOG.info(item));

                    Set serviceSetFromSowNotInProdAddOn  = new HashSet();
                    boolean found = false;

                    for(Object object1 : addOnsFromsowServiceMp){
                        String key = ((JsonObject)object1).getString("addonCode");
                        for(Object object2: addOnsFromProductAddons){
                            LOG.info("Obj1 = "+object1+", Obj2 = "+object2);
                            String addOnFromSow = ((JsonObject)object2).getString("code");

                            if(addOnFromSow.equalsIgnoreCase(key)){
                                found = true;
                                break;
                            }else{
                                serviceSetFromSowNotInProdAddOn.add(object1);
                            }
                        }
                    }


                    Set prodSetFromProdAddOnNotInSow  = new HashSet();
                    boolean prodFound = false;

                    for(Object object1 : addOnsFromProductAddons){
                        String key = ((JsonObject)object1).getString("code");
                        for(Object object2: addOnsFromsowServiceMp){
                            LOG.info("Obj1 = "+object1+", Obj2 = "+object2);
                            String addOnFromProd = ((JsonObject)object2).getString("addonCode");

                            if(addOnFromProd.equalsIgnoreCase(key)){
                                prodFound = true;
//                                break;
                            }else{
                                prodSetFromProdAddOnNotInSow.add(object1);
                            }
                        }
                    }

                    if(!found){
                        LOG.info("No Addons are there for the added SOW" );
                        LOG.info(getL1SO1Value(new ArrayList<JsonObject>(serviceSetFromSowNotInProdAddOn)));
                        response.put("status","Failure");
                        response.put("comments", "Please add Addons for following SOW - "
                                +getL1SO1Value(new ArrayList<JsonObject>(serviceSetFromSowNotInProdAddOn)));
                        LOG.info("Response is:: "+response );
                        sendJsonResponse(routingContext, response.toString());
                    }else if(!prodFound){
                        LOG.info("No SOWs are there for the Addons added" );
                        LOG.info(getL1SO1Value(new ArrayList<JsonObject>(serviceSetFromSowNotInProdAddOn)));
                        response.put("status","Failure");
                            response.put("comments", "Please add SOWs for following addons - "
                                +getL1SO1Value(new ArrayList<JsonObject>(prodSetFromProdAddOnNotInSow)));
                        sendJsonResponse(routingContext, response.toString());
                    }
                    else{
                        publishTheProposal(routingContext);
                    }

                }
            }
        });
    }
    private String getL1SO1Value(List<JsonObject> paramObj){
        StringBuilder sbServiceName = new StringBuilder();
        paramObj.forEach(item->{
            String spaceType = item.getString("spaceType");
            Collection<SOWMaster> sowMasterList = ModuleDataService.getInstance().getSOWMaster(spaceType);
            Object[] masterSOWs = (Object[])sowMasterList.toArray();

            String strL1S01Code = item.getString("L1S01Code");
            int noOfRows = masterSOWs.length;
            for(int i =0 ;i< noOfRows;i++){
                SOWMaster sow = (SOWMaster) masterSOWs[i];

                if(sow.getL1S01Code().equalsIgnoreCase(strL1S01Code)){
                    sbServiceName.append(sow.getL1S01());
                    if((i+1) > noOfRows){
                        sbServiceName.append(", ");
                    }
                }
            }
        });
      return sbServiceName.toString();
    }
    private void publishTheProposal(RoutingContext routingContext) {
        JsonObject queryParams = new JsonObject();
        queryParams.put("id",routingContext.getBodyAsJson().getInteger("proposalId"));
        queryParams.put("version",routingContext.getBodyAsJson().getDouble("version"));
        queryParams.put("proposalId",routingContext.getBodyAsJson().getInteger("proposalId"));

        List<QueryData> queryDatas = new ArrayList<>();
        queryDatas.add(new QueryData("version.publish",queryParams));
        queryDatas.add(new QueryData("proposal.publish",queryParams));

        Integer id = LocalCache.getInstance().store(queryDatas);
        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id, (AsyncResult<Message<Integer>> selectResult) -> {
            List<QueryData> resultDatas = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());
            if (resultDatas.get(0).errorFlag || resultDatas.get(0).updateResult.getUpdated() == 0) {
                sendError(routingContext, "Error in publishing version");
                LOG.error("Error in publishing version "+resultDatas.get(0).errorMessage, resultDatas.get(0).error );
            }else if(resultDatas.get(1).errorFlag || resultDatas.get(1).updateResult.getUpdated() == 0){
                sendError(routingContext, "Error in publishing proposal");
                LOG.error("Error in publishing proposal "+resultDatas.get(1).errorMessage, resultDatas.get(1).error );
            }

            else {
                LOG.info("Response is :: SUCCESS");
                JsonObject response = new JsonObject();
                response.put("status","success");
                response.put("comments","Successfully Published Version/Proposal");
                sendJsonResponse(routingContext,response.toString());
            }
        });
    }


    public  static List compareLists(List<String> sourceList, List<String> destinationList){

        sourceList.removeAll( destinationList );
        return sourceList;
    }
    private void createPostSalesInitial(RoutingContext routingContext) {
        JsonObject versionData = routingContext.getBodyAsJson();
        LOG.debug("routing context :" + versionData.encodePrettily());
        Integer id = LocalCache.getInstance().store(new QueryData("version.createPostSalesInitial", versionData));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in creating version.");
                    }
                    else
                    {
                        sendJsonResponse(routingContext, versionData.toString());
                    }
                });
    }

    private void createProposalVersion(RoutingContext routingContext) {
        JsonObject versionData = routingContext.getBodyAsJson();
        LOG.debug("routing context :" + versionData.encodePrettily());
        Integer id = LocalCache.getInstance().store(new QueryData("version.createversion", versionData));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in creating version.");
                    }
                    else
                    {
                        sendJsonResponse(routingContext, versionData.toString());
                    }
                });
    }

    private void copyVersion(RoutingContext routingContext) {
        JsonObject versionData = routingContext.getBodyAsJson();
        LOG.debug("routing context :" + versionData.encodePrettily());
        Integer id = LocalCache.getInstance().store(new QueryData("version.copyversion", versionData));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in creating version.");
                    }
                    else
                    {
                        sendJsonResponse(routingContext, versionData.toString());
                    }
                });
    }

    //todo: code doc.remove


    private void createProposal(RoutingContext routingContext)
    {
        JsonObject proposalData = routingContext.getBodyAsJson();
        LOG.debug("proposalData :" + proposalData.encodePrettily());
        if ( StringUtils.isEmpty(proposalData.getString("createdBy")))
        {
            sendError(routingContext, "Error in creating proposal as title or createdBy are not set.");
            return;
        }
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.create", proposalData));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in creating proposal.");
                        LOG.error("Error in creating proposal. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        String docsFolder = this.proposalDocsFolder + "/" + proposalData.getLong("id");
                        try
                        {
                            VertxInstance.get().fileSystem().mkdirBlocking(docsFolder);
                        }
                        catch (Exception e)
                        {
                            sendError(routingContext, "Error in creating folder for proposal at path:" + docsFolder);
                            LOG.error("Error in creating folder for proposal at path:" + docsFolder + ". Error:" + resultData.errorMessage, resultData.error);
                            return;
                        }
                        proposalData.put("folderPath", docsFolder);
                        this.updateProposal(routingContext, proposalData, "proposal.folder.update");
                    }
                });
    }

    private void updateProposal(RoutingContext routingContext)
    {
        JsonObject proposalData = routingContext.getBodyAsJson();
        LOG.info("Proposal:" + proposalData.encodePrettily());
        this.updateProposal(routingContext, proposalData, "proposal.update");
    }

    private void updateProposalOnConfirm(RoutingContext routingContext)
    {
        JsonObject proposalData = routingContext.getBodyAsJson();
        LOG.info("Proposal:" + proposalData.encodePrettily());
        this.updateProposal(routingContext, proposalData, "proposal.update.onconfirm");
    }

    private void updateProposal(RoutingContext routingContext, JsonObject proposalData, String queryId)
    {
        Integer id = LocalCache.getInstance().store(new QueryData(queryId, proposalData));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in updating proposal.");
                        LOG.error("Error in updating proposal. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        sendJsonResponse(routingContext, proposalData.toString());
                    }
                });
    }

    private void downloadQuote(RoutingContext routingContext)
    {
        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.QUOTATION);
    }

    private void downloadQuoteAndSow(RoutingContext routingContext){
        LOG.info("Routing Context :: "+routingContext);
        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.QUOTATION);
    }

//    private void downloadSow(RoutingContext routingContext)
//    {
////        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.QUOTATION);
//        this.createSOWPdfOutput(routingContext);
//    }

    private void downloadQuotePdf(RoutingContext routingContext)
    {
        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.QUOTEPDF);
    }

    private void createSowSheet(RoutingContext routingContext)
    {
        this.createSOWOutput(routingContext);
    }

    /*private void createBoqSheet(RoutingContext routingContext)
    {
        this.createBoqSheet(routingContext, ProposalOutputCreator.OutputType.BOQ);
    }*/

    private void downloadJobCard(RoutingContext routingContext)
    {
        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.JOBCARD);
    }


    private void downloadSalesOrder(RoutingContext routingContext)
    {
        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.SALESORDER);
    }

    /*private void downloadProdSpec(RoutingContext routingContext)
    {
        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.PRODSPEC);
    }*/

    private void createProposalOutput(RoutingContext routingContext, ProposalOutputCreator.OutputType type)
    {
        LOG.debug("");
        LOG.debug("Create proposal output :" + routingContext.getBodyAsJson().toString());
        JsonObject quoteRequestJson = routingContext.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QuoteRequest(quoteRequestJson, type));
        VertxInstance.get().eventBus().send(ProposalOutputService.CREATE_PROPOSAL_OUTPUT, id,
                (AsyncResult<Message<Integer>> result) -> {
                    JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
                    String fileToUpload = ConfigHolder.getInstance().getStringValue("sow_downloaded_xls_format","sow.xlsx");
                    String proposalFolder = ConfigHolder.getInstance().getStringValue("proposal_docs_folder","/mnt/game/proposal/");
                    if(fileSowFileExists(proposalFolder+"/"+quoteRequestJson.getInteger("proposalId")+"/"+fileToUpload)){
                        this.getProposalHeader(routingContext,response);
                    }else{
                        sendJsonResponse(routingContext, response.toString());
                    }


                });
    }

    private boolean fileSowFileExists(String fileName){

        File f = new File(fileName);
        if(f.exists() && !f.isDirectory()) {
            LOG.info("FileName :: "+fileName +" Exists");
           return true;
        }
        LOG.info("FileName :: "+fileName +" Not     Exists");
        return false;
    }
    private void createSOWPdfOutput(RoutingContext routingContext,JsonObject quotePDfResponse,JsonObject inputJson){
        LOG.debug("Create SOW pdf output :" + routingContext.getBodyAsJson().toString());

        Integer id = LocalCache.getInstance().store(new SowPdfRequest(inputJson));
        VertxInstance.get().eventBus().send(SOWPdfOutputService.CREATE_SOW_PDF_OUTPUT, id,
                (AsyncResult<Message<Integer>> result) -> {
                    JsonObject sowResponse = (JsonObject) LocalCache.getInstance().remove(result.result().body());
                    createMergedPdf(routingContext,quotePDfResponse,sowResponse);
                });
    }

    private void getProposalHeader(RoutingContext context,JsonObject quotePDfResponse){

        Integer proposalId = context.getBodyAsJson().getInteger("proposalId");
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.header", new JsonObject().put("id",proposalId)));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag)
                    {
                        sendError(context, "Error in getting  proposal header.");
                        LOG.error("Error in getting  proposal header. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        JsonObject proposalHeader = resultData.rows.get(0);

                        String fileToUpload = ConfigHolder.getInstance().getStringValue("sow_downloaded_xls_format","sow.xlsx");
                        String proposalFolder = ConfigHolder.getInstance().getStringValue("proposal_docs_folder","/mnt/game/proposal/");

                        JsonObject inputJson = new JsonObject();
                        inputJson.put("xlsFileLocation",proposalFolder+ "/" + proposalId+"/");
                        inputJson.put("fileNameToUpload",fileToUpload);
                        inputJson.put("xlsFileNameInDrive",proposalHeader.getString("quoteNoNew")+"_"+fileToUpload);
                        inputJson.put("userId",context.getBodyAsJson().getString("userId"));
                        createSOWPdfOutput(context,quotePDfResponse,inputJson);
                    }
                });


    }
    private void createMergedPdf(RoutingContext routingContext,JsonObject quotePDfResponse,JsonObject sowResponse){
        LOG.debug("createMergedPdf :" + routingContext.getBodyAsJson().toString());
        Map<String,PdfNumber> inputPdfMap = new LinkedHashMap<>();
        inputPdfMap.put(quotePDfResponse.getString("quoteFile"), PdfPage.PORTRAIT);
        inputPdfMap.put(sowResponse.getString("sowPdfFile"),PdfPage.PORTRAIT);

        LOG.info("quotePDfResponse.getString(\"quoteFile\") :: "+quotePDfResponse.getString("quoteFile"));
        LOG.info("inputPdfMap.put(sowResponse.getString(\"sowPdfFile\")::"+sowResponse.getString("sowPdfFile"));
        LOG.info("MAP :: "+inputPdfMap);
        String location_folder =ConfigHolder.getInstance().getStringValue("proposal_docs_folder","/mnt/game/proposal/" )+"/"+routingContext.getBodyAsJson().getInteger("proposalId");
        String merged_pdf = ConfigHolder.getInstance().getStringValue("merged_pdf","merged.pdf" );

        String outputFileName = location_folder+"/"+merged_pdf;
        Integer id = LocalCache.getInstance().store(new MergePdfsRequest(inputPdfMap, outputFileName));
        VertxInstance.get().eventBus().send(SOWPdfOutputService.CREATE_MERGED_PDF_OUTPUT, id,
                (AsyncResult<Message<Integer>> result) -> {
                    JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
                    sendJsonResponse(routingContext, response.toString());
                });

    }


    private void createSOWOutput(RoutingContext routingContext)
    {
        JsonObject quoteRequestJson = routingContext.getBodyAsJson();
        LOG.debug("RequestJson :" + routingContext.getBodyAsJson());
        Integer id = LocalCache.getInstance().store(quoteRequestJson);
        LOG.info("Integer id = "+id);
        VertxInstance.get().eventBus().send(SOWCreatorService.CREATE_SOW_OUTPUT, id,
                (AsyncResult<Message<Integer>> result) -> {
                    JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
                    LOG.debug("Json object response sending to client :" + response.toString());
                    sendJsonResponse(routingContext, response.toString());
                });
    }

    private void createBoqSheet(RoutingContext routingContext, ProposalOutputCreator.OutputType type)
    {
        JsonObject quoteRequestJson = routingContext.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QuoteRequest(quoteRequestJson, type));
        VertxInstance.get().eventBus().send(ProposalOutputService.CREATE_PROPOSAL_OUTPUT, id,
                (AsyncResult<Message<Integer>> result) -> {
                    JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
                    sendJsonResponse(routingContext, response.toString());
                });
    }

    private void saveSowFile(RoutingContext routingContext)
    {
       JsonObject jsonObject = routingContext.getBodyAsJson();
        LOG.debug("Json object :0 " + jsonObject);
        String file_id = jsonObject.getString("id");
        int proposalId = jsonObject.getInteger("proposalId");
        String version = jsonObject.getString("version");
        String path = ConfigHolder.getInstance().getStringValue("proposal_docs_folder","/mnt/game/proposal")+
                "/"+proposalId+"/"+ConfigHolder.getInstance().getStringValue("sow_downloaded_xls_format","sow.xlsx");//"D:/Mygubbi GAME/sow_downloaded.xlsx";
        this.serviceProvider = new DriveServiceProvider();
        this.serviceProvider.downloadFile(file_id, path, DriveServiceProvider.TYPE_XLS);
        this.sowWriteToDatabase = new SOWWriteToDatabase();
        this.sowWriteToDatabase.writeToDB(path,proposalId,version);
        sendJsonResponse(routingContext,jsonObject.toString());
    }

    private void discardSowFile(RoutingContext routingContext)
    {
       JsonObject jsonObject = routingContext.getBodyAsJson();
        String file_id = jsonObject.getString("id");
        this.serviceProvider.deleteFile(file_id);
    }

    private void createBoqLineItems(RoutingContext routingContext)
    {
        JsonObject jsonObject = routingContext.getBodyAsJson();
    }

    private void saveBoqFile(RoutingContext routingContext)
    {
       JsonObject jsonObject = routingContext.getBodyAsJson();
        String file_id = jsonObject.getString("id");
        int proposalId = jsonObject.getInteger("proposalId");
        String path = "D:/Mygubbi GAME/boq_downloaded.xlsx";
        this.serviceProvider = new DriveServiceProvider();
        this.serviceProvider.downloadFile(file_id, path, DriveServiceProvider.TYPE_XLS);
        this.boqWriteToDatabase = new BOQWriteToDatabase();
        //this.boqWriteToDatabase.writeToDB(path,proposalId);
        sendJsonResponse(routingContext,jsonObject.toString());
    }

    private void discardBoqFile(RoutingContext routingContext)
    {
       JsonObject jsonObject = routingContext.getBodyAsJson();
        String file_id = jsonObject.getString("id");
        this.serviceProvider.deleteFile(file_id);
    }

    private void getProposals(RoutingContext context)
    {
        String userId = context.request().getParam("userid");
        vertx.eventBus().send(DatabaseService.DB_QUERY, LocalCache.getInstance().store(new QueryData("game_user.select", new JsonObject().put("email", userId))),
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData.rows == null || selectData.rows.isEmpty())
                    {
                        sendError(context, "User not valid : " + userId);
                    }
                    else
                    {
                        String role  = selectData.rows.get(0).getString("role");
                        String query = ("sales".equals(role) || "designer".equals(role)) ? "proposal.list." + role : "proposal.list.all";
                        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, LocalCache.getInstance().store(new QueryData(query, new JsonObject().put("userId", userId))),
                                (AsyncResult<Message<Integer>> proposalResult) -> {
                                    QueryData proposalData = (QueryData) LocalCache.getInstance().remove(proposalResult.result().body());
                                    if (proposalData == null || proposalData.rows == null || proposalData.rows.isEmpty())
                                    {
                                        sendJsonResponse(context, "[]");
                                    }
                                    else
                                    {
                                        sendJsonResponse(context, proposalData.rows.toString());
                                    }
                                });
                    }
                });
    }

    private void getAccessoryRate(RoutingContext context) {
        String code = context.request().getParam("rateId");
        String priceDate = context.request().getParam("priceDate");
        String city = context.request().getParam("city");
        getAccessoryRate(context, code, DateUtil.convertDate(priceDate), city);

    }

    private void getAccessoryRate(RoutingContext routingContext, String code, Date priceDate, String city) {
        PriceMaster addonRate = RateCardService.getInstance().getAccessoryRate(code, priceDate, city);
        if (addonRate == null ) {
            LOG.error("Error in retrieving addon price");
            sendError(routingContext, "Error in retrieving addon price.");
        } else {
            sendJsonResponse(routingContext, addonRate.toJson().toString());
        }
    }

    private void getHardwareRate(RoutingContext context) {
        String code = context.request().getParam("rateId");
        String priceDate = context.request().getParam("priceDate");
        String city = context.request().getParam("city");
        getHardwareRate(context, code, DateUtil.convertDate(priceDate), city);

    }

    private void getHardwareRate(RoutingContext routingContext, String code, Date priceDate, String city) {
        PriceMaster addonRate = RateCardService.getInstance().getHardwareRate(code, priceDate, city);
        if (addonRate == null ) {
            LOG.error("Error in retrieving addon price");
            sendError(routingContext, "Error in retrieving addon price.");
        } else {
            sendJsonResponse(routingContext, addonRate.toJson().toString());
        }
    }

    private void getRateFactor(RoutingContext context) {
        String code = context.request().getParam("rateId");
        String priceDate = context.request().getParam("priceDate");
        String city = context.request().getParam("city");
        getRateFactor(context, code, DateUtil.convertDate(priceDate), city);

    }

    private void getRateFactor(RoutingContext routingContext, String code, Date priceDate, String city) {
        PriceMaster factorRate = RateCardService.getInstance().getFactorRate(code, priceDate, city);
        if (factorRate == null ) {
            LOG.error("Error in retrieving factor rate");
            sendError(routingContext, "Error in retrieving factor rate.");
        } else {
            sendJsonResponse(routingContext, factorRate.toJson().toString());
        }
    }

    private void getHandleKnobDetails(RoutingContext context) {
        String code = context.request().getParam("rateId");
        String priceDate = context.request().getParam("priceDate");
        String city = context.request().getParam("city");
        getHandleKnobDetails(context, code, DateUtil.convertDate(priceDate), city);

    }

    private void getHingeDetails(RoutingContext context) {
        String code = context.request().getParam("rateId");
        String priceDate = context.request().getParam("priceDate");
        String city = context.request().getParam("city");
        getHingeDetails(context, code, DateUtil.convertDate(priceDate), city);

    }

    private void getHandleKnobDetails(RoutingContext routingContext, String code, Date priceDate, String city) {
        PriceMaster addonRate = RateCardService.getInstance().getHandleOrKnobRate(code, priceDate, city);
        if (addonRate == null ) {
            LOG.error("Error in retrieving handle price");
            sendError(routingContext, "Error in retrieving handle price.");
        } else {
            sendJsonResponse(routingContext, addonRate.toJson().toString());
        }
    }

    private void getHingeDetails(RoutingContext routingContext, String code, Date priceDate, String city) {
        PriceMaster addonRate = RateCardService.getInstance().getHingeRate(code, priceDate, city);
        if (addonRate == null ) {
            LOG.error("Error in retrieving hinge price");
            sendError(routingContext, "Error in retrieving hinge price.");
        } else {
            sendJsonResponse(routingContext, addonRate.toJson().toString());
        }
    }

    private void copySowLineItems(RoutingContext routingContext)
    {
        JsonObject jsonObject = routingContext.getBodyAsJson();

        String queryId = "proposal.sow.version.copy";

        Integer id = LocalCache.getInstance().store(new QueryData(queryId, jsonObject));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in updating proposal.");
                        LOG.error("Error in updating proposal. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        sendJsonResponse(routingContext, jsonObject.toString());
                    }
                });

    }

}



