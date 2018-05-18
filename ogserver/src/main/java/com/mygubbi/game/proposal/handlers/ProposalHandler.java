package com.mygubbi.game.proposal.handlers;

import com.mygubbi.common.DateUtil;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.StringUtils;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.Upload.FileUploadHandler;
import com.mygubbi.game.proposal.erp.BOQHandler;
import com.mygubbi.game.proposal.erp.BoqCreatorService;
import com.mygubbi.game.proposal.model.*;
import com.mygubbi.game.proposal.output.ProposalOutputCreator;
import com.mygubbi.game.proposal.output.ProposalOutputService;
import com.mygubbi.game.proposal.price.*;
import com.mygubbi.game.proposal.quote.MergePdfsRequest;
import com.mygubbi.game.proposal.sow.SOWCreatorService;
import com.mygubbi.game.proposal.quote.QuoteRequest;
import com.mygubbi.game.proposal.sow.SowValidatorService;
import com.mygubbi.pipeline.MessageDataHolder;
import com.mygubbi.pipeline.PipelineExecutor;
import com.mygubbi.pipeline.PipelineResponseHandler;
import com.mygubbi.report.ReportTableFillerSevice;
import com.mygubbi.route.AbstractRouteHandler;
import com.mygubbi.game.proposal.output.SOWPdfOutputService;
import com.mygubbi.si.crm.CrmApiClient;
import com.mygubbi.si.email.EmailData;
import com.mygubbi.si.gdrive.DriveServiceProvider;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map;

/**
 * Created by sunil on 25-04-2016.
 */
public class ProposalHandler extends AbstractRouteHandler
{
    private final static Logger LOG = LogManager.getLogger(ProposalHandler.class);
    
    private final static String CONFIRMED = "Confirmed";
    private final static String DSO = "DSO";
    private final static String PSO = "PSO";

    private String proposalDocsFolder = null;
    boolean uploadToS3;
    String quoteFile="";

    public DriveServiceProvider serviceProvider;

    public BOQHandler boqHandler;

    private  final static String COLON_DELIMITER = ":";
    public ProposalHandler(Vertx vertx)
    {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/list").handler(this::getProposals);
        this.post("/create").handler(this::createProposal);
        this.post("/version/saveService").handler(this::saveServiceFortheProposal);
        this.post("/version/publishoverride").handler(this::publishTheProposal);
//        this.post("/version/publish").handler(this::publishVersionAfterValidation);
        this.post("/version/createdraft").handler(this::createInitialDraftProposal);
        this.post("/version/createPostSalesInitial").handler(this::createPostSalesInitial);
        this.post("/version/createversion").handler(this::createProposalVersion);
        this.post("/version/copyversion").handler(this::copyVersion);
        this.post("/createQuoteNumber").handler(this::createQuoteNumber);
//        this.post("/version/confirm").handler(this::confirmVersion);
//        this.post("/version/designsignoff").handler(this::dsoVersion);
//        this.post("/version/productionsignoff").handler(this::psoVersion);
        this.post("/update").handler(this::updateProposal);
        this.post("/updateonconfirm").handler(this::updateProposalOnConfirm);
        this.post("/downloadquote").handler(this::downloadQuote);
        this.post("/downloadquoteandSow").handler(this::downloadQuoteAndSow);
        this.post("/downloadjobcard").handler(this::downloadJobCard);
        this.post("/downloadsalesorder").handler(this::downloadSalesOrder);
        this.post("/createsowsheet").handler(this::createSowSheet);
        this.post("/runReportFiller").handler(this::runReportFiller);
        this.post("/createboqsheet").handler(this::createBoqSheet);
        this.post("/discardsowfile").handler(this::discardSowFile);
        this.post("/copysowlineitems").handler(this::copySowLineItems);
        this.post("/discardboqfile").handler(this::discardBoqFile);
        //this.post("/downloadprodspecfile").handler(this::downloadProdSpec);
        this.post("/downloadquotePdf").handler(this::downloadQuotePdf);
        this.get("/hardwareratedetails").handler(this::getHardwareRate);
        this.get("/accratedetails").handler(this::getAccessoryRate);
        this.get("/handleknobdetails").handler(this::getHandleKnobDetails);
        this.get("/hingedetails").handler(this::getHingeDetails);
        this.post("/updatepricefordraftproposals").handler(this::updatePriceForDraftProposals);
        this.get("/ratefactordetailsfromhandler").handler(this::getRateFactor);
        this.post("/version/price").handler(this::getPriceV2);
        this.get("/getbookingmonthoptions").handler(this::getBookingMonth);
        this.proposalDocsFolder = ConfigHolder.getInstance().getStringValue("proposal_docs_folder", "/tmp/");
        LOG.info("this.proposalDocsFolder:" + this.proposalDocsFolder);
    }

    private void saveServiceFortheProposal(RoutingContext routingContext){
//        String sampleJson = "[{\\\"proposalId\\\":7969,\\\"fromVersion\\\":0.1,\\\"serviceTitle\\\":\\\"Project Handling Charges\\\",\\\"quantity\\\":25690,\\\"amount\\\":1541.3999999999999,\\\"updatedBy\\\":\\\"shruthi.r@mygubbi.com\\\"},{\\\"proposalId\\\":7969,\\\"fromVersion\\\":0.1,\\\"serviceTitle\\\":\\\"Deep Clearing Charges\\\",\\\"quantity\\\":1,\\\"amount\\\":26,\\\"updatedBy\\\":\\\"shruthi.r@mygubbi.com\\\"},{\\\"proposalId\\\":7969,\\\"fromVersion\\\":0.1,\\\"serviceTitle\\\":\\\"Floor Protection Charges\\\",\\\"quantity\\\":1,\\\"amount\\\":30,\\\"updatedBy\\\":\\\"shruthi.r@mygubbi.com\\\"}]" ;
        LOG.info("routingContext.getBodyAsJson() = "+routingContext.getBodyAsJson());
        JsonObject parmsJson = routingContext.getBodyAsJson();


        JsonArray paramsArray = parmsJson.getJsonArray("services");
        LOG.info("Array.size = "+paramsArray.size());

        List<QueryData> queryDatas = new ArrayList<>();
        queryDatas.add(new QueryData("proposal_service.insert",paramsArray.getJsonObject(0)));
        queryDatas.add(new QueryData("proposal_service.insert",paramsArray.getJsonObject(1)));
        queryDatas.add(new QueryData("proposal_service.insert",paramsArray.getJsonObject(2)));

        // remove this looping after u know about batch insert/update
//        for(int i = 0;i<paramsArray.size();i++) {
            Integer id = LocalCache.getInstance().store(queryDatas);
            VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id,
                    (AsyncResult<Message<Integer>> selectResult) -> {
                        List<QueryData> resultDatas = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());
                        int i = 0;
                        for (; i < resultDatas.size(); i++) {
                            if (resultDatas.get(i).errorFlag ) {

                                sendError(routingContext, "Error in inserting into Miscellaneous services.");
                            }
                        }

                        if (i == resultDatas.size()) {
                            sendJsonResponse(routingContext, new JsonObject().put("status","success").toString());
                        }
                    });
//        }

    }
    private void getPriceV2(RoutingContext routingContext)
    {

        JsonObject versionJson = routingContext.getBodyAsJson();
        LOG.debug("Version Json : " + versionJson.encodePrettily());
        ProposalVersion proposalVersion = new ProposalVersion(versionJson);
        Integer id = LocalCache.getInstance().store(proposalVersion);
        VertxInstance.get().eventBus().send(VersionPricingService.CALCULATE_VERSION_PRICE, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    VersionPriceHolder versionPriceHolder = (VersionPriceHolder) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.debug("Version Price holder json :" + versionPriceHolder.getPriceJson());
                    sendJsonResponse(routingContext, String.valueOf(versionPriceHolder.getPriceJson()));
                });
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

    private void runReportFiller(RoutingContext context){

        JsonObject contextJson = context.getBodyAsJson();
        Integer id1 = LocalCache.getInstance().store(contextJson);
        if(contextJson.containsKey("proposalId")) {
            LOG.info("For ProposalID");
            if(contextJson.containsKey("version")){
                MessageDataHolder dataHolder = new MessageDataHolder(ReportTableFillerSevice.RUN_FOR_SINGLE_PROPOSAL_VERSION, contextJson);
                new PipelineExecutor().execute(dataHolder, new ProposalHandler.ReportTablefillerResponseHandler(context));
            }else {
                MessageDataHolder dataHolder = new MessageDataHolder(ReportTableFillerSevice.RUN_FOR_SINGLE_PROPOSAL, contextJson);
                new PipelineExecutor().execute(dataHolder, new ProposalHandler.ReportTablefillerResponseHandler(context));
            }
       }else if(contextJson.containsKey("reports")) {
            //"reports":"update"
            String val = contextJson.getString("reports");
            if(val.equalsIgnoreCase("UPDATE")) {
                if (contextJson.containsKey("startDate") && contextJson.containsKey("endDate")){
                    MessageDataHolder dataHolder = new MessageDataHolder(ReportTableFillerSevice.RUN_FROM_STARTDATE_TO_ENDDATE_PROPOSALS, contextJson);
                    new PipelineExecutor().execute(dataHolder, new ProposalHandler.ReportTablefillerResponseHandler(context));
                } else{
                    MessageDataHolder dataHolder = new MessageDataHolder(ReportTableFillerSevice.RUN_FOR_UPDATED_PROPOSALS, contextJson);
                    new PipelineExecutor().execute(dataHolder, new ProposalHandler.ReportTablefillerResponseHandler(context));
                }
            }else if(val.equalsIgnoreCase("FORCE")){
                MessageDataHolder dataHolder = new MessageDataHolder(ReportTableFillerSevice.FORCE_UPDATE_PROPOSALS, contextJson);
                new PipelineExecutor().execute(dataHolder, new ProposalHandler.ReportTablefillerResponseHandler(context));
            }else{
                sendJsonResponse(context,"Please specify one of option - update/force");
            }
        }else if(contextJson.containsKey("proposals")){
            MessageDataHolder dataHolder = new MessageDataHolder(ReportTableFillerSevice.RUN_FOR_LIST_OF_PROPOSALS, contextJson);
            new PipelineExecutor().execute(dataHolder, new ProposalHandler.ReportTablefillerResponseHandler(context));
        }else if(contextJson.containsKey("props")){
            LOG.info("props = "+contextJson);
            MessageDataHolder dataHolder = new MessageDataHolder(ReportTableFillerSevice.RUN_FOR_PROPOSALS_LIST, contextJson);
            new PipelineExecutor().execute(dataHolder, new ProposalHandler.ReportTablefillerResponseHandler(context));
        }else{

            sendJsonResponse(context,"Please specify Valid option to run Report");
        }

    }

    private void publishTheProposal(RoutingContext routingContext) {




        JsonObject queryParams = new JsonObject();
        Integer proposalId = routingContext.getBodyAsJson().getInteger("proposalId");
        queryParams.put("id", proposalId);
        Double version = routingContext.getBodyAsJson().getDouble("version");

        checkIfProductsAddedAsPerOffer(routingContext,proposalId, String.valueOf(version),queryParams);

    }

    private void publishVersion(RoutingContext routingContext, JsonObject queryParams, Integer proposalId, Double version) {
        queryParams.put("version", version);
        queryParams.put("proposalId", proposalId);
        queryParams.put("businessDate",routingContext.getBodyAsJson().getString("businessDate"));

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


                uploadToS3 = true;
                checkValidRowsInDB(routingContext,new JsonObject());
                //this.createProposalOutput(routingContext,ProposalOutputCreator.OutputType.QUOTEPDF,false,new JsonObject());
                /*LOG.info("quote file after calling method " +quoteFile);
                FileUploadHandler fileUploadHandler = new FileUploadHandler(VertxInstance.get());
                String filename = fileUploadHandler.uploadFile("C:\\Users\\Public\\game_files\\8554\\merged.pdf","BLR-2017-23");
                LOG.info("file name " +filename);
                LOG.info("Response is :: SUCCESS");*/
            }
        });
    }

    private void checkIfProductsAddedAsPerOffer(RoutingContext routingContext, int proposalId, String version,JsonObject queryParams)
    {
        List<QueryData> queryDataList = new ArrayList<>();
        List<OfferProductMapping> offerProductMappingList = new ArrayList<>();
        List<ProductAddon> productAddons = new ArrayList<>();
        double totalAfterDiscount = routingContext.getBodyAsJson().getDouble("finalAmount");
        String offerCode = routingContext.getBodyAsJson().getString("offerCode");

        queryDataList.add(new QueryData("offer.product.mapping.select", new JsonObject().put("offerCode",offerCode)));
        queryDataList.add(new QueryData("proposal.version.addons.select", new JsonObject().put("proposalId",proposalId).put("version",version)));

        Integer id = LocalCache.getInstance().store(queryDataList);

        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    List<QueryData> resultData = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.get(0).errorFlag)
                    {
                        sendError(routingContext, "Error in creating version.");
                    }
                    else
                    {
                        int noOfPresentProducts = 0;
                        int minProductReqd = 0;
                        for (JsonObject jsonObject : resultData.get(0).rows)
                        {
                            offerProductMappingList.add(new OfferProductMapping(jsonObject));
                        }
                        for (JsonObject jsonObject : resultData.get(1).rows)
                        {
                            productAddons.add(new ProductAddon(jsonObject));
                        }


        if (offerProductMappingList.size() != 0)
        {
            for (OfferProductMapping offerProductMapping : offerProductMappingList)
            {
                if (offerProductMapping.getMinOrderValue() <= totalAfterDiscount)
                {
                    minProductReqd = offerProductMapping.getMinOrderQty();
                }
            }
        }

        if (productAddons.size() != 0)
        {
            for (ProductAddon productAddon : productAddons)
            {
                if (productAddon.getCategoryCode().equals("Appliances") && productAddon.getProductTypeCode().equals("AC") && productAddon.getProductSubtypeCode().equals("AC"))
                {
                    noOfPresentProducts += productAddon.getQuantity();
                }
            }
        }

        int difference = noOfPresentProducts - minProductReqd;


        if (difference < 0)
        {
            sendJsonResponse(routingContext, String.valueOf(new JsonObject().put("status","error").put("message","Please add " + Math.abs(difference) + " AC's in order to publish")));
            return;
        }
        if (difference > 0)
        {
            sendJsonResponse(routingContext, String.valueOf(new JsonObject().put("status","error").put("message","Please remove " + difference + " AC's in order to publish")));
            return;
        }
        else {
            publishVersion(routingContext, queryParams, proposalId, Double.valueOf(version));

        }

                    }
                });
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
                         List<QueryData> queryDatas = new ArrayList<>();
                         queryDatas.add(new QueryData("proposal.folder.update",proposalData));
                        this.updateProposal(routingContext, proposalData, queryDatas);
                    }
                });
    }

    private void updateProposal(RoutingContext routingContext)
    {
        JsonObject proposalData = routingContext.getBodyAsJson();
        LOG.info("Proposal:" + proposalData.encodePrettily());
        // get the quote no based on city before saving
        LOG.info("proposal data project city " +proposalData.getString("pcity"));
        /*if(routingContext.getBodyAsJson().getString("pcity")==null || !routingContext.getBodyAsJson().getString("quoteNoNew").isEmpty() )
        {
            LOG.info("inside if of city is null " );*/
            List<QueryData> queryDatas = new ArrayList<>();
            queryDatas.add( new QueryData("proposal.update", proposalData));
            this.updateProposal(routingContext, proposalData,queryDatas);
        /*}else
        {
            getMaxQuoteNoBasedOnCityMonthYear(routingContext);
        }*/

    }

    private void createQuoteNumber(RoutingContext routingContext) {
        LOG.info("create quote number called" + routingContext.get("pcity"));
        JsonObject bodyDetails =routingContext.getBodyAsJson();
        ProposalHeader header = new ProposalHeader(bodyDetails);
        String city = header.getProjectCity();
        LOG.info("city in create quote method " +city);
        String cityCode = "";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate today = LocalDate.now();
        if(city==null  || city.isEmpty())
        {
            city="No city";
        }
        switch (city) {
            case "Bangalore":
                cityCode = "BLR";
                break;
            case "Chennai":
                cityCode = "CHN";
                break;
            case "Mangalore":
                cityCode = "MLR";
                break;
            case "Pune":
                cityCode = "PUN";
                break;
            default: break;
        }

        String tempInc = String.format("%04d", 1);

        StringBuilder newQuote = new StringBuilder();

        JsonObject paramsObj = new JsonObject();
        paramsObj.put("city", cityCode);
        paramsObj.put("curmonth", Integer.valueOf(String.format("%02d", today.getMonthValue())));
        paramsObj.put("curYear", today.getYear());
        LOG.info("city " + cityCode+ " cur month " +String.format("%02d", today.getMonthValue()) + "cur year " +today.getYear());
        Integer id = LocalCache.getInstance().store(new QueryData("city.selectMonthCount", paramsObj));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag) {
                        LOG.error("Error in getting quote val from city_master");
                    }  else {

                        if (resultData.rows == null || resultData.rows.isEmpty()) {
                            newQuote.append(paramsObj.getString("city")).append("-").append(today.getYear()).append("-")
                                    .append(String.format("%02d", today.getMonthValue())).append("-").append(tempInc);
                        } else {
                            JsonObject output = resultData.rows.get(0);
                            String lastQuoteNo = output.getString("quoteNo");
                            String[] quote_Details = lastQuoteNo.toString().split("-");
                            newQuote.append(paramsObj.getString("city")).append("-").append(today.getYear()).append("-")
                                    .append(String.format("%02d", today.getMonthValue())).append("-").
                                    append(String.format("%04d", Integer.parseInt(quote_Details[quote_Details.length - 1]) + 1));

                        }
                        List<QueryData> queryDatas = new ArrayList<>();

                        queryDatas.add( new QueryData("proposal.update", bodyDetails));
                        // city.newCityQuote
                        paramsObj.put("proposalId",bodyDetails.getInteger("id"));
                        paramsObj.put("quoteNo",newQuote);
                        LOG.info("quote number " +newQuote);
                        queryDatas.add( new QueryData("city.newCityQuote", paramsObj));

                        this.updateProposal(routingContext, bodyDetails, queryDatas);
                        //i have quote no, insert that to city master and proposal table ...

                    }
                });


    }

    private void sendEmails(ProposalVersion proposalVersion){

        LOG.info("Email contextJson = "+proposalVersion);
        JsonObject paramsForEmail = new JsonObject();
        Integer proposalId = proposalVersion.getProposalId();
        String toVersion = proposalVersion.getVersion();
        paramsForEmail.put("fromEmail",ConfigHolder.getInstance().getStringValue("FROM_EMAIL","game@mygubbi.com"));

        LOG.info("toVersion = "+toVersion);
        if(toVersion.equals("1.0")){
//            Send a mail to Sales, Design, Design Head and Region Head
            paramsForEmail.put("subject","Booking confirmation is done");
            paramsForEmail.put("subjectTemplate","email/confirmation.1_0.subject.vm");
            paramsForEmail.put("bodyTemplate", "email/confirmation.1_0.body.vm");
            getProposalHeaderAndCallSendEmail(proposalVersion,paramsForEmail,proposalId,toVersion);
//            getVersionRecord(paramsForEmail,proposalId,toVersion);
            //fill to emails and params object properly from database

        }else if(toVersion.equals("2.0")){
            paramsForEmail.put("subject","Booking confirmation is done");
            paramsForEmail.put("subjectTemplate","email/confirmation.2_0.subject.vm");
            paramsForEmail.put("bodyTemplate", "email/confirmation.2_0.body.vm");
            getProposalHeaderAndCallSendEmail(proposalVersion,paramsForEmail,proposalId,toVersion);
//            getVersionRecord(paramsForEmail,proposalId,toVersion);
//send a mail to Finance team (Jyoti, Sudarshan) and  Planning team (Suresh, Prabhu)
        }else if(toVersion.equals("3.0")){
            paramsForEmail.put("subject","Booking confirmation is done");
            paramsForEmail.put("subjectTemplate","email/confirmation.3_0.subject.vm");
            paramsForEmail.put("bodyTemplate", "email/confirmation.3_0.body.vm");
            getProposalHeaderAndCallSendEmail(proposalVersion,paramsForEmail,proposalId,toVersion);
//            getVersionRecord(paramsForEmail,proposalId,toVersion);
//send a mail to Design team(Indicating that Design is frozen and Production starting), Purchase team, Factory team
        }else{
            LOG.error("Invalid toVersion");
        }
    }

    private void getProposalHeaderAndCallSendEmail(ProposalVersion proposalVersion,JsonObject paramsForEmail,Integer proposalId,String toVersion ) {
        Vector<String> emailIds = new Vector<String>();
        JsonObject bodyDetails = new JsonObject();
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.header", new JsonObject().put("id", proposalId)));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty()) {
                        LOG.error("Proposal not found for id:" + proposalId);
                    } else {
                        ProposalHeader proposalHeader = new ProposalHeader(resultData.rows.get(0));
                        String projectCity = proposalHeader.getProjectCity();
                        if(toVersion.equalsIgnoreCase("1.0")){
                            emailIds.add(proposalHeader.getDesignerEmail()); //designer
                            emailIds.add(proposalHeader.getSalesEmail()); //sales person

                            Collection<UsersForEmail> designHeads = ModuleDataService.getInstance().getUserForEmail("designHead");
                            designHeads.forEach(dhead -> {
                                emailIds.add(dhead.getEmail()); //design head
                            });
                            Collection<UsersForEmail> regionalHeads = ModuleDataService.getInstance().getUserForEmail("regionHead");
                            regionalHeads.forEach(rhead -> {
                                if (rhead.getRegion().contains(projectCity)) {
                                    emailIds.add(rhead.getEmail());//regional head
                                }
                            });
                            Collection<UsersForEmail> financeTeam = ModuleDataService.getInstance().getUserForEmail("finance");
                            financeTeam.forEach(finace -> {
                                emailIds.add(finace.getEmail());//finance
                            });

                            Collection<UsersForEmail> crmTeam = ModuleDataService.getInstance().getUserForEmail("crm");
                            crmTeam.forEach(crm -> {
                                emailIds.add(crm.getEmail());//crm team
                            });

                            Collection<UsersForEmail> purchase = ModuleDataService.getInstance().getUserForEmail("purchaseHead");
                            purchase.forEach(purTeam -> {
                                emailIds.add(purTeam.getEmail());//purchase
                            });

                        }else if(toVersion.equalsIgnoreCase("2.0")){
                            emailIds.add(proposalHeader.getDesignerEmail()); //designer
                            emailIds.add(proposalHeader.getSalesEmail()); //sales person

                            Collection<UsersForEmail> designHeads = ModuleDataService.getInstance().getUserForEmail("designHead");
                            designHeads.forEach(dhead -> {
                                emailIds.add(dhead.getEmail()); //design head
                            });

                            Collection<UsersForEmail> regionalHeads = ModuleDataService.getInstance().getUserForEmail("regionHead");
                            regionalHeads.forEach(rhead -> {
                                if (rhead.getRegion().contains(projectCity)) {
                                    emailIds.add(rhead.getEmail());//regional head
                                }
                            });

                            Collection<UsersForEmail> financeTeam = ModuleDataService.getInstance().getUserForEmail("finance");
                            financeTeam.forEach(finace -> {
                                emailIds.add(finace.getEmail());//finance
                            });

                            Collection<UsersForEmail> planningTeam = ModuleDataService.getInstance().getUserForEmail("planning");
                            planningTeam.forEach(planning -> {
                                LOG.info("Planning team :: "+planning.getEmail());
                                emailIds.add(planning.getEmail());//planning
                            });

                            Collection<UsersForEmail> operationsHead = ModuleDataService.getInstance().getUserForEmail("operationsHead");
                            operationsHead.forEach(opHead -> {
                                emailIds.add(opHead.getEmail());//operations Head
                            });

                            Collection<UsersForEmail> crmTeam = ModuleDataService.getInstance().getUserForEmail("crm");
                            crmTeam.forEach(crm -> {
                                emailIds.add(crm.getEmail());//crm team
                            });

                            Collection<UsersForEmail> purchase = ModuleDataService.getInstance().getUserForEmail("purchaseHead");
                            purchase.forEach(purTeam -> {
                                emailIds.add(purTeam.getEmail());//purchase
                            });

                        }else if(toVersion.equalsIgnoreCase("3.0")){
                            emailIds.add(proposalHeader.getDesignerEmail()); //designer

                            Collection<UsersForEmail> designHeads = ModuleDataService.getInstance().getUserForEmail("designHead");
                            designHeads.forEach(dhead -> {
                                emailIds.add(dhead.getEmail()); //design head
                            });

                            Collection<UsersForEmail> planningTeam = ModuleDataService.getInstance().getUserForEmail("planning");
                            planningTeam.forEach(planning -> {
                                emailIds.add(planning.getEmail());//planning
                            });

                            Collection<UsersForEmail> purchase = ModuleDataService.getInstance().getUserForEmail("purchaseHead");
                            purchase.forEach(purTeam -> {
                                emailIds.add(purTeam.getEmail());//purchase
                            });

                            Collection<UsersForEmail> factoryTeam = ModuleDataService.getInstance().getUserForEmail("factoryHead");
                            factoryTeam.forEach(factory -> {
                                emailIds.add(factory.getEmail());//factory team
                            });

                            Collection<UsersForEmail> operationsHead = ModuleDataService.getInstance().getUserForEmail("operationsHead");
                            operationsHead.forEach(opHead -> {
                                emailIds.add(opHead.getEmail());//operations Head
                            });

                            Collection<UsersForEmail> crmTeam = ModuleDataService.getInstance().getUserForEmail("crm");
                            crmTeam.forEach(crm -> {
                                emailIds.add(crm.getEmail());//crm team
                            });

                        }

                        bodyDetails.put("clientName",proposalHeader.getName());
                        bodyDetails.put("opportunityId",proposalHeader.getCrmId());
                        bodyDetails.put("quoteNo",proposalHeader.getQuoteNumNew());
                        bodyDetails.put("noOfDaysForWorkCompletion",proposalHeader.getNoOfDaysforworkcompletion());
                        bodyDetails.put("expectedDeliveryDate",proposalHeader.getExpectedDeliveryDate().toString());
                        bodyDetails.put("price",proposalVersion.getAmount());
                        bodyDetails.put("priceAfterDiscount",proposalVersion.getFinalAmount());
                        bodyDetails.put("discountAmountPerc",proposalVersion.getDiscountPercentage());
                        String url = ConfigHolder.getInstance().getStringValue("App_url_for_proposal","https://game.mygubbi.com/#!New%20Quotation/");
                        bodyDetails.put("url",url+proposalId);

                        StringBuilder sb = new StringBuilder();
                        for(int i=0;i<emailIds.size();i++){
                            sb.append(emailIds.get(i));
                            if(i+1 != emailIds.size()){
                                sb.append(",");
                            }
                        }
                        paramsForEmail.put("toEmails",sb.toString());
                        paramsForEmail.put("paramsObj",bodyDetails);

//                        sendEmailToOnConfirm(paramsForEmail);
                        getAddonAndCustomAddonDetails(paramsForEmail,proposalId,proposalVersion.getVersion()+"");

                    }
                });

    }

    private void getAddonAndCustomAddonDetails(JsonObject paramsForEmail, Integer proposalId, String toVersion) {
        JsonObject params = new JsonObject();
        params.put("proposalId",proposalId);
        params.put("version",toVersion);

        Map<String,String> addonMp = new HashMap<>();
        addonMp.put("addon_acc_app","No");
        addonMp.put("service_counter_top","No");
        addonMp.put("loose_furniture","No");

        Map<String,String> customAddonMp = new HashMap<>();
        customAddonMp.put("custom_addon_acc_app","No");
        customAddonMp.put("custom_service_counter_top","No");
        customAddonMp.put("custom_loose_furniture","No");

        JsonObject paramsObjForEmail = paramsForEmail.getJsonObject("paramsObj");
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.addon.selectForEmail", params));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,(AsyncResult<Message<Integer>> res) -> {
            QueryData data = (QueryData) LocalCache.getInstance().remove(res.result().body());
            if (data == null || data.rows == null || data.rows.isEmpty()){
                        LOG.info("No regular or custom addon");
            }else {
                   List<JsonObject> rows =  data.rows;
                   rows.forEach(row -> {
                            String categoryCode = row.getString("categoryCode");
                            switch(categoryCode){
                                case "Accessories":
                                    addonMp.put("addon_acc_app","Yes");
                                    break;

                                case "Appliances":
                                    addonMp.put("addon_acc_app","Yes");
                                    break;

                                case "Services":
                                    addonMp.put("service_counter_top","Yes");
                                    break;

                                case "Counter Top":
                                    addonMp.put("service_counter_top","Yes");
                                    break;

                                case "Loose Furniture":
                                    addonMp.put("loose_furniture","Yes");
                                    break;

                                case "Custom Addon":
                                {
                                    String customAddonCategory = row.getString("customAddonCategory");
                                    switch (customAddonCategory){
                                        case "Accessories":
                                            customAddonMp.put("custom_addon_acc_app","Yes");
                                            break;
                                        case "Appliances":
                                            customAddonMp.put("custom_addon_acc_app","Yes");
                                            break;
                                        case "Chimney":
                                            customAddonMp.put("custom_addon_acc_app","Yes");
                                            break;
                                        case "Hob":
                                            customAddonMp.put("custom_addon_acc_app","Yes");
                                            break;
                                        case "Sink":
                                            customAddonMp.put("custom_addon_acc_app","Yes");
                                            break;

                                        case "Loose Furniture":
                                            customAddonMp.put("custom_loose_furniture","Yes");
                                            break;

                                        default:
                                            customAddonMp.put("custom_service_counter_top","Yes");
                                            break;
                                    }
                                }
                            }
                        });
                    }

                    paramsObjForEmail.put("addon_acc_app",addonMp.get("addon_acc_app"));
                    paramsObjForEmail.put("service_counter_top",addonMp.get("service_counter_top"));
                    paramsObjForEmail.put("loose_furniture",addonMp.get("loose_furniture"));

                    paramsObjForEmail.put("custom_addon_acc_app",customAddonMp.get("custom_addon_acc_app"));
                    paramsObjForEmail.put("custom_service_counter_top",customAddonMp.get("custom_service_counter_top"));
                    paramsObjForEmail.put("custom_loose_furniture",customAddonMp.get("custom_loose_furniture"));

                    paramsForEmail.put("paramsObj",paramsObjForEmail);
                    sendEmailToOnConfirm(paramsForEmail);

                });
    }

    private void sendEmailToOnConfirm(JsonObject emailParams){

        String fromEmail = emailParams.getString("fromEmail");
        String[] toemails = emailParams.getString("toEmails").split(",");
//        String[] temp = emailParams.getString("toEmails").split(",");
//        LOG.info("Email receivers = ");
//        for (String s : temp) {
//            LOG.info(s);
//        }

//        String[] toemails = {"shilpa.g@mygubbi.com","shruthi.r@mygubbi.com ","nagmani.bhushan@mygubbi.com"};
        JsonObject params = emailParams.getJsonObject("paramsObj");
        String subject = emailParams.getString("subject");
        String subjectTemplate = emailParams.getString("subjectTemplate");
        String bodyTemplate = emailParams.getString("bodyTemplate");

        String sendGridApiKey = ConfigHolder.getInstance().getStringValue("SENDGRID_KEY","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");
        EmailData emailData = new EmailData().setFromEmail(fromEmail).setToEmails(toemails)
                .setHtmlBody(true).setParams(params.getMap())
                .setSubject(subject).setBodyTemplate(bodyTemplate)
                .setSubjectTemplate(subjectTemplate);

        SendGrid sendgrid = new SendGrid(sendGridApiKey);
        SendGrid.Email email = new SendGrid.Email();
        email.addTo(emailData.getToEmails());
        email.setFrom(emailData.getFromEmail());
        email.setSubject(emailData.getSubject());
        email.setHtml(emailData.getMessageBody());
        email.setText("Trying again ...");

        LOG.info("Email is :: "+emailData.toString());

        try
        {
            SendGrid.Response response = sendgrid.send(email);
            LOG.info("Message sent status: " + response.getMessage());
        }
        catch (SendGridException e)
        {
            LOG.error("Error in sending email.", e);
        }
    }
    private void updateProposalOnConfirm(RoutingContext context)
    {
        JsonObject contextJson = context.getBodyAsJson();

        boolean m1Value = checkIfM1ValueisPresent(contextJson.getString("crmId"));
        if (!m1Value)
        {
            contextJson.put("responseMessageForM1Amount",true);
            contextJson.put("confirmedStatus",false);
            sendJsonResponse(context, contextJson.toString());
            return;
        }


        String verFromProposal = String.valueOf(contextJson.getString("version"));
        String sowVersion = null ;
        if(verFromProposal.contains("0.")){
            sowVersion = "1.0";
        }else if(verFromProposal.contains("1.") ){
            sowVersion = "2.0";
        }else if(verFromProposal.contains("2.") || verFromProposal.contains("3.")){
            sowVersion = "3.0";
        }else{

            LOG.info("INVALID VERSION and VERSION IS ::"+verFromProposal);
            return;
        }

        JsonObject queryParams =  new JsonObject();
        queryParams.put("proposalId", contextJson.getInteger("proposalId"));
        queryParams.put("sowversion", sowVersion);
        queryParams.put("version",contextJson.getString("version"));
        queryParams.put("businessDate",contextJson.getString("businessDate"));

        Integer id1 = LocalCache.getInstance().store(queryParams);
        VertxInstance.get().eventBus().send(SowValidatorService.VALIDATE_PROPOSAL_SOW, id1,
                (AsyncResult<Message<Integer>> result) -> {
                    JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
                    LOG.info("Message Reply :: "+response);

                    if(response.getString("status").equalsIgnoreCase("FAILURE")) {
                        contextJson.put("responseMessage",response.getString("comments"));
                        contextJson.put("confirmedStatus",false);
                        sendJsonResponse(context, contextJson.toString());
                    }
                    else{
                        confirmTheProposal(context);
                    }
                });
    }

    private boolean checkIfM1ValueisPresent(String opportunityId) {
        boolean m1ValuePresent = true;
        JsonObject opportunityDetailsForGame;
        try {
            opportunityDetailsForGame = new CrmApiClient().getOpportunityDetailsForGame(opportunityId);
            if (Objects.equals(opportunityDetailsForGame.getString("m1_amount_collected_c"),null) || opportunityDetailsForGame.getString("m1_amount_collected_c").equals("")) m1ValuePresent = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return m1ValuePresent;
    }

    private void confirmTheProposal(RoutingContext context) {

        JsonObject contextJson = context.getBodyAsJson();
        List<QueryData> queryDatas = new ArrayList<>();
        ProposalVersion proposalVersion = new ProposalVersion(contextJson);
        JsonObject queryParams = new JsonObject();

        JsonObject proposalHeaderJson = new JsonObject();
        if (proposalVersion.getVersion().startsWith("0.")) {
            proposalVersion.setFromVersion(proposalVersion.getVersion());
            proposalVersion.setVersion("1.0");
            proposalVersion.setProposalStatus(CONFIRMED);
            proposalVersion.setInternalStatus(CONFIRMED);
            proposalHeaderJson.put("status",CONFIRMED);
            java.util.Date date=new java.util.Date(); // your date
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR)%100;
            int month = cal.get(Calendar.MONTH);
            proposalHeaderJson.put("bookingOrderMonth",theMonth(month)+"-"+year);
        } else if (proposalVersion.getVersion().startsWith("1.")) {
            proposalVersion.setFromVersion(proposalVersion.getVersion());
            proposalVersion.setVersion("2.0");
            proposalVersion.setProposalStatus(DSO);
            proposalVersion.setInternalStatus(DSO);
            proposalHeaderJson.put("status",DSO);
        } else if (proposalVersion.getVersion().startsWith("2.")) {
            proposalVersion.setFromVersion(proposalVersion.getVersion());
            proposalVersion.setVersion("3.0");
            proposalVersion.setProposalStatus(PSO);
            proposalVersion.setInternalStatus(PSO);
            proposalHeaderJson.put("status",PSO);
        }

        proposalHeaderJson.put("id",proposalVersion.getProposalId()).put("version",proposalVersion.getVersion()).put("amount",proposalVersion.getFinalAmount());


        queryDatas.add(new QueryData("proposal.version.confirm",proposalVersion));
        queryDatas.add(new QueryData("proposal.update.onconfirm",proposalHeaderJson));

//        queryDatas.add(new QueryData("version.confirm",contextJson));
        queryDatas.add(new QueryData("proposal.product.updateVersionOnConfirm",proposalVersion));
        queryDatas.add(new QueryData("proposal.addon.updateVersionOnConfirm",proposalVersion));
        queryDatas.add(new QueryData("proposal.version.selectversion",proposalVersion));

        switch (proposalVersion.getVersion())
        {
            case "1.0":{
                queryDatas.add(new QueryData("version.lockallpresalesversions",proposalVersion));
                queryDatas.add(new QueryData("proposal.sow.version.copy",proposalVersion));
                queryDatas.add(new QueryData("proposal.update.bookingamount",proposalHeaderJson));
                break;
            }
            case "2.0":{
                queryDatas.add(new QueryData("version.lockallpostsalesversions",proposalVersion));
                queryDatas.add(new QueryData("proposal.sow.version.copy.from2.0",proposalVersion));
                break;
            }
            default:
                queryDatas.add(new QueryData("version.lockallversions",proposalVersion));
        }

        Integer id = LocalCache.getInstance().store(queryDatas);
        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id, (AsyncResult<Message<Integer>> selectResult) -> {
            List<QueryData> resultDatas = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());
            resultDatas.forEach(item->{
                if(item.errorFlag && item.updateResult.getUpdated() == 0){
                    contextJson.put("responseMessage", "Error in publishing version");
                    contextJson.put("confirmedStatus",false);
                    sendJsonResponse(context,contextJson.toString());
                }
            });

            JsonObject firstResJson = resultDatas.get(4).rows.get(0);
            String sendEmails =  ConfigHolder.getInstance().getStringValue("sendEmail","No");
            if(sendEmails.equalsIgnoreCase("yes")) {
                sendEmails(new ProposalVersion(firstResJson));
            }
            uploadToS3 = true;
            checkValidRowsInDB(context,firstResJson);
            /*this.createProposalOutput(context,ProposalOutputCreator.OutputType.QUOTEPDF,false,firstResJson);*/


        });
    }
    public static String theMonth(int month){
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return monthNames[month];
    }
    private void updateProposal(RoutingContext routingContext, JsonObject proposalData, List<QueryData> queryDatas)
    {
        Integer id = LocalCache.getInstance().store(queryDatas);
        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    List<QueryData> resultDatas = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());
                    int i = 0;
                    for (; i < resultDatas.size(); i++) {
                        if (resultDatas.get(i).errorFlag ) {

                            sendError(routingContext, "Error in insert Queries");
                        }
                    }

                    if (i == resultDatas.size()) {
                        sendJsonResponse(routingContext, proposalData.toString());
                    }

                });
    }

    private void downloadQuote(RoutingContext routingContext)
    {

        this.createProposalOutputForExcel(routingContext, ProposalOutputCreator.OutputType.QUOTATION,false,new JsonObject());
    }

    private void downloadQuoteAndSow(RoutingContext routingContext){
        LOG.info("Routing Context :: "+routingContext);

        //LOG.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%" +quoteRequestJson.getJsonObject("proposalId"));
        LOG.info("Quote  and SOW");
        LOG.info("****************************" +routingContext.getBodyAsJson().getString("city"));
//        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.QUOTATION);
        checkValidRowsInDB(routingContext,new JsonObject());
    }

//    private void downloadSow(RoutingContext routingContext)
//    {
////        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.QUOTATION);
//        this.createSOWPdfOutput(routingContext);
//    }

    private void downloadQuotePdf(RoutingContext routingContext)
    {
        LOG.info("Only Quote");
        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.QUOTEPDF,false,new JsonObject());
    }

    private void createSowSheet(RoutingContext routingContext)
    {
        this.createSOWOutput(routingContext);
    }


    private void createBoqSheet(RoutingContext routingContext)
    {
        this.createBoqOutput(routingContext);
    }

    private void downloadJobCard(RoutingContext routingContext)
    {
        this.createProposalOutputForExcel(routingContext, ProposalOutputCreator.OutputType.JOBCARD,false,new JsonObject());
    }


    private void downloadSalesOrder(RoutingContext routingContext)
    {
        this.createProposalOutputForExcel(routingContext, ProposalOutputCreator.OutputType.SALESORDER,false,new JsonObject());
    }

    /*private void downloadProdSpec(RoutingContext routingContext)
    {
        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.PRODSPEC);
    }*/

    private void createProposalOutputForExcel(RoutingContext routingContext, ProposalOutputCreator.OutputType type,boolean ValidSows,JsonObject jsonObject)
    {
        try
        {
            JsonObject jsonObj = routingContext.getBodyAsJson();
            String status=routingContext.getBodyAsJson().getString("status");
            LOG.info("jsonObj.containsKey(\"bookingFormFlag\") = "+jsonObj.containsKey("bookingFormFlag"));
            Boolean bookingFormFlag = false;
            if(jsonObj.containsKey("bookingFormFlag") && (jsonObj.getValue("bookingFormFlag") != null)){
                bookingFormFlag = jsonObj.getString("bookingFormFlag").equalsIgnoreCase("yes")?true:false;
            }
            Boolean IsBookingFormFlag = new Boolean(bookingFormFlag);

            LOG.info("ValidSows = "+ValidSows);
            LOG.debug("Json **** " +routingContext.getBodyAsJson());
            LOG.debug("Create proposal output :" + routingContext.getBodyAsJson().toString());
            JsonObject quoteRequestJson = routingContext.getBodyAsJson();
            quoteRequestJson.put("validSow",ValidSows);
            // LOG.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%" +quoteRequestJson.getJsonObject("proposalId"));
            Integer id = LocalCache.getInstance().store(new QuoteRequest(quoteRequestJson, type));
            VertxInstance.get().eventBus().send(ProposalOutputService.CREATE_PROPOSAL_OUTPUT, id,
                    (AsyncResult<Message<Integer>> result) -> {
                        JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
                        LOG.info("Quote Res :: "+response);
                        if(ValidSows){
                            createSowOutputInPdf(routingContext,response,IsBookingFormFlag,jsonObj);
                        }else if(IsBookingFormFlag){
                            createBookingFormInPdf(routingContext,response,new JsonObject(),jsonObject);
//                        sendJsonResponse(routingContext, response.toString());
                        }else{
                                LOG.info("outside quote PDF");
                                sendJsonResponse(routingContext, response.toString());
                        }
                    });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOG.info("Exception " +e);
        }

    }

    private void createProposalOutput(RoutingContext routingContext, ProposalOutputCreator.OutputType type,boolean ValidSows,JsonObject jsonObject)
    {
        try
        {
            JsonObject jsonObj = routingContext.getBodyAsJson();
            String status=routingContext.getBodyAsJson().getString("status");
            String quoteNumber=routingContext.getBodyAsJson().getString("quoteNumber");
            String crmId=routingContext.getBodyAsJson().getString("crmId");
            String version=routingContext.getBodyAsJson().getString("toVersion");
            LOG.info("jsonObj.containsKey(\"bookingFormFlag\") = "+jsonObj.containsKey("bookingFormFlag"));
            Boolean bookingFormFlag = false;
            if(jsonObj.containsKey("bookingFormFlag") && (jsonObj.getValue("bookingFormFlag") != null)){
                bookingFormFlag = jsonObj.getString("bookingFormFlag").equalsIgnoreCase("yes")?true:false;
            }
            Boolean IsBookingFormFlag = new Boolean(bookingFormFlag);

            LOG.info("ValidSows = "+ValidSows);
            LOG.debug("Json **** " +routingContext.getBodyAsJson());
            LOG.debug("Create proposal output :" + routingContext.getBodyAsJson().toString());
            JsonObject quoteRequestJson = routingContext.getBodyAsJson();
            quoteRequestJson.put("validSow",ValidSows);
            // LOG.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%" +quoteRequestJson.getJsonObject("proposalId"));
            Integer id = LocalCache.getInstance().store(new QuoteRequest(quoteRequestJson, type));
            VertxInstance.get().eventBus().send(ProposalOutputService.CREATE_PROPOSAL_OUTPUT, id,
                    (AsyncResult<Message<Integer>> result) -> {
                        JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
                        LOG.info("Quote Res :: "+response);
                        if(ValidSows){
                            createSowOutputInPdf(routingContext,response,IsBookingFormFlag,jsonObj);
                        }else if(IsBookingFormFlag){
                            createBookingFormInPdf(routingContext,response,new JsonObject(),jsonObject);
                        }else{
                                LOG.info("inside quote pdf");
                                List<String> inputPdfs = new ArrayList<>();
                                inputPdfs.add(response.getString("quoteFile"));
                                String location_folder =ConfigHolder.getInstance().getStringValue("proposal_docs_folder","/mnt/game/proposal/" )+"/"+routingContext.getBodyAsJson().getInteger("proposalId");
                                String merged_pdf = ConfigHolder.getInstance().getStringValue("merged_pdf","merged.pdf" );
                                String outputFileName = location_folder+"/"+"ver1_"+merged_pdf;
                                String outputFileNameAfterPageNum = location_folder+"/"+merged_pdf;
                                Integer id1 = LocalCache.getInstance().store(new MergePdfsRequest(inputPdfs, outputFileName,outputFileNameAfterPageNum,status,crmId,quoteNumber,version));
                                VertxInstance.get().eventBus().send(SOWPdfOutputService.CREATE_MERGED_PDF_OUTPUT, id1,
                                        (AsyncResult<Message<Integer>> result1) -> {
                                            JsonObject response1 = (JsonObject) LocalCache.getInstance().remove(result1.result().body());
                                            LOG.info("Routing Context in quote file = "+response);
                                            sendJsonResponse(routingContext, response1.toString());
                                        });
                                LOG.info("outside quote PDF");
                        /*}else {
                            sendJsonResponse(routingContext, response.toString());
                            }*/

                        }
                    });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOG.info("Exception " +e);
        }

    }


    private void checkValidRowsInDB(RoutingContext routingContext,JsonObject jsonObject){
        JsonObject params = new JsonObject();
        LOG.info("routingContext.getBodyAsJson() == "+routingContext.getBodyAsJson().encodePrettily());
        params.put("proposalId",routingContext.getBodyAsJson().getInteger("proposalId"));

        String verFromProposal =routingContext.getBodyAsJson().getString("toVersion");
        String sowVersion = null ;
        if(verFromProposal.contains("0.") || verFromProposal.equals("1.0")){
            sowVersion = "1.0";
        }else if(verFromProposal.contains("1.") || verFromProposal.contains("2.")){
            sowVersion = "2.0";
        }else if(verFromProposal.contains("2.") || verFromProposal.contains("3.")){
            sowVersion = "3.0";
        }else{
            LOG.info("INVALID VERSION and VERSION IS::"+verFromProposal);
        }
        params.put("sowversion",sowVersion);

        LOG.info("params == "+params);
        vertx.eventBus().send(DatabaseService.DB_QUERY, LocalCache.getInstance()
                        .store(new QueryData("proposal.sow.select.proposalversion.forPdf",
                                params)),
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData.rows == null || selectData.rows.isEmpty()){
                        LOG.info("VAlid Sows = "+false);
                        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.QUOTEPDF,false,jsonObject);
                    }
                    else {
                        LOG.info("VAlid Sows = "+true);
                        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.QUOTEPDF,true,jsonObject);
//                        createSowOutputInPdf(routingContext,res);

                    }
                });

    }
    private void createSowOutputInPdf(RoutingContext context, JsonObject quoteReponse,Boolean IsBookingFormFlag,JsonObject jsonObject){
        JsonObject quoteRequestJson = context.getBodyAsJson();

        Boolean workContractFlag = false;
        if(quoteRequestJson.containsKey("worksContractFlag") && (quoteRequestJson.getValue("worksContractFlag") != null)){
            workContractFlag = quoteRequestJson.getString("worksContractFlag").equalsIgnoreCase("yes")?true:false;
        }
        Boolean IsWorkingContract = new Boolean(workContractFlag);


        String version = quoteRequestJson.getString("toVersion");
        LOG.info("Version :: "+version);
        Integer id = LocalCache.getInstance().store(new QuoteRequest(quoteRequestJson, ProposalOutputCreator.OutputType.SOWPDF));
        VertxInstance.get().eventBus().send(SOWPdfOutputService.CREATE_SOW_PDF_OUTPUT, id, (AsyncResult<Message<Integer>> result) -> {
            JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
            if(IsBookingFormFlag) {
                createBookingFormInPdf(context, quoteReponse, response,jsonObject);
//            }else if(IsWorkingContract){
//                this.createWorksContractinPdf(context,quoteReponse,response);
//                //createMergedPdf(context,quoteReponse,response,new JsonObject());
            }else{
                createMergedPdf(context,quoteReponse,response,new JsonObject(),jsonObject);


            }
        });
    }

    private void createBookingFormInPdf(RoutingContext context,JsonObject quoteResponse,JsonObject sowresponse,JsonObject jsonObject){
        LOG.info("create booking form in  pdf " ) ;
        JsonObject quoteRequestJson = context.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QuoteRequest(quoteRequestJson, ProposalOutputCreator.OutputType.BOOKING_FORM));
        VertxInstance.get().eventBus().send(SOWPdfOutputService.CREATE_BOOKING_FORM_PDF_OUTPUT, id, (AsyncResult<Message<Integer>> result) -> {
            JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
            LOG.info("response of booking form pdf " +response);
            createMergedPdf(context,quoteResponse,sowresponse,response,jsonObject);


        });
    }
    private void createWorksContractinPdf(RoutingContext context, JsonObject quoteResponse, JsonObject sowresponse, JsonObject jsonObject)
    {
        JsonObject quoteRequestJson = context.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QuoteRequest(quoteRequestJson, ProposalOutputCreator.OutputType.WORKSCONTRACT));
        VertxInstance.get().eventBus().send(SOWPdfOutputService.CREATE_WORK_CONTRACT_PDF_OUTPUT, id, (AsyncResult<Message<Integer>> result) -> {
            JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
            LOG.info("response of workscontract" +response);
            createMergedPdf(context,quoteResponse,sowresponse,response,jsonObject);


        });

    }
    private void createMergedPdf(RoutingContext routingContext,JsonObject quotePDfResponse,JsonObject sowResponse,JsonObject bookingformresponse,JsonObject jsonObject){
        JsonObject quoteRequestJson = routingContext.getBodyAsJson();
        String city=routingContext.getBodyAsJson().getString("city");
        String version=routingContext.getBodyAsJson().getString("toVersion");
        String status=routingContext.getBodyAsJson().getString("status");
        String quoteNumber=routingContext.getBodyAsJson().getString("quoteNo") + version;
        String bookingFormFlag=routingContext.getBodyAsJson().getString("bookingFormFlag");
        String quoteNo=routingContext.getBodyAsJson().getString("quoteNumber");
        String crmId=routingContext.getBodyAsJson().getString("crmId");
        List<String> inputPdfs = new ArrayList<>();

        Boolean workContractFlag = false;
        if(quoteRequestJson.containsKey("worksContractFlag") && (quoteRequestJson.getValue("worksContractFlag") != null)){
            workContractFlag = quoteRequestJson.getString("worksContractFlag").equalsIgnoreCase("yes")?true:false;
        }
        Boolean IsWorkingContract = new Boolean(workContractFlag);

        LOG.info("workContractFlag = "+workContractFlag);

        if(workContractFlag)
        {
            String location_folder =ConfigHolder.getInstance().getStringValue("workscontract_template","/mnt/game/proposal/templates/WorkscontractTemplate.pdf");
            inputPdfs.add(location_folder);
        }

        inputPdfs.add(quotePDfResponse.getString("quoteFile"));
        if(bookingFormFlag.equals("Yes"))
        {
            if(city.equals("Bangalore"))
            {
                String location_folder =ConfigHolder.getInstance().getStringValue("termsandcondition_banglore","/mnt/game/proposal/templates/BookingFormBanglore.pdf");
                inputPdfs.add(location_folder);
            }else if(city.equals("Mangalore"))
            {
                String location_folder =ConfigHolder.getInstance().getStringValue("termsandcondition_manglore","/mnt/game/proposal/templates/BookingFormManglore.pdf");
                inputPdfs.add(location_folder);
            }else if(city.equals("Chennai"))
            {
                String location_folder =ConfigHolder.getInstance().getStringValue("termsandcondition_chennai","/mnt/game/proposal/templates/BookingFormChennai.pdf");
                inputPdfs.add(location_folder);
            }else if(city.equals("Pune"))
            {
                String location_folder =ConfigHolder.getInstance().getStringValue("termsandcondition_pune","/mnt/game/proposal/templates/BookingFormPune.pdf");
                inputPdfs.add(location_folder);
            }
        }
        if(sowResponse.size() > 0) {
            inputPdfs.add(sowResponse.getString("sowPdfFile"));
        }

        if(bookingFormFlag.equalsIgnoreCase("yes") && bookingformresponse.containsKey("bookingFormPDFfile"))
            inputPdfs.add(bookingformresponse.getString("bookingFormPDFfile"));


        String location_folder =ConfigHolder.getInstance().getStringValue("proposal_docs_folder","/mnt/game/proposal/" )+"/"+routingContext.getBodyAsJson().getInteger("proposalId");
        String merged_pdf = ConfigHolder.getInstance().getStringValue("merged_pdf","merged.pdf" );


        String outputFileName = location_folder+"/"+"ver1_"+merged_pdf;
        LOG.info("outputFileName = "+outputFileName);

        String outputFileNameAfterPageNum = location_folder+"/"+merged_pdf;

        inputPdfs.forEach(f->{LOG.info("File Name :: "+f);});



        if (uploadToS3)
        {
            uploadToS3=false;
            Integer id = LocalCache.getInstance().store(new MergePdfsRequest(inputPdfs, outputFileName,outputFileNameAfterPageNum,status,crmId,quoteNo,version));
            VertxInstance.get().eventBus().send(SOWPdfOutputService.CREATE_MERGED_PDF_OUTPUT, id,
                    (AsyncResult<Message<Integer>> result) -> {
                        JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
                        LOG.info("Routing Context = "+response);
                        quoteFile=response.getString("quoteFile");
                        LOG.info("quote file upload " +quoteFile);
                        FileUploadHandler fileUploadHandler = new FileUploadHandler(VertxInstance.get());
                        String lastFile = fileUploadHandler.uploadFile(quoteFile,quoteNumber);
                        LOG.debug("Final file to be sent to UI :" + lastFile);
                        response.put("status","success");
                        response.put("comments","Successfully published version/proposal");
                        jsonObject.put("quoteFile",lastFile).put("status","success").put("comments","Successfully published version/proposal").put("responseMessage", "Successfully Confirmed").put("confirmedStatus",true);
                        LOG.info("JSON object response " +jsonObject.toString());
                        sendJsonResponse(routingContext, jsonObject.toString());
                    });
        }
        else
        {
            Integer id = LocalCache.getInstance().store(new MergePdfsRequest(inputPdfs, outputFileName,outputFileNameAfterPageNum,status,crmId,quoteNo,version));
            VertxInstance.get().eventBus().send(SOWPdfOutputService.CREATE_MERGED_PDF_OUTPUT, id,
                    (AsyncResult<Message<Integer>> result) -> {
                        JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
                        LOG.info("Routing Context = "+response);
                        sendJsonResponse(routingContext, response.toString());
                    });
        }
    }


    private void createSOWOutput(RoutingContext routingContext)
    {
        try
        {
            LOG.debug("Routing context in sow op : " + routingContext.getBodyAsJson().encodePrettily());
            JsonObject quoteRequestJson = routingContext.getBodyAsJson();
            Integer id = LocalCache.getInstance().store(quoteRequestJson);
            VertxInstance.get().eventBus().send(SOWCreatorService.CREATE_SOW_OUTPUT, id,
                    (AsyncResult<Message<Integer>> result) -> {
                        JsonObject response = new JsonObject();
                         try{
                             response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
                            }catch(Exception e){
                                LOG.info(e);
                                JsonObject res = new JsonObject();
                                res.put("status", "Failure");
                                res.put("comments", "Error in creating scope of services");
                                sendJsonResponse(routingContext, res.toString());
                         }
                        sendJsonResponse(routingContext, response.toString());
                    });
        }
        catch(Exception e)
        {
            LOG.info(e);
            JsonObject res = new JsonObject();
            res.put("status", "Failure");
            res.put("comments", "Error in creating scope of services");
            sendJsonResponse(routingContext, res.toString());
        }

    }

    private void createBoqOutput(RoutingContext routingContext)
    {
        int count = 0;
        LOG.debug("Inside create boq output : " + ++count);
        JsonObject quoteRequestJson = routingContext.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(quoteRequestJson);
        VertxInstance.get().eventBus().send(BoqCreatorService.CREATE_BOQ_OUTPUT, id,  new DeliveryOptions().setSendTimeout(120000),
                (AsyncResult<Message<Integer>> result) -> {
                    JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
                    sendJsonResponse(routingContext, response.toString());
                });
    }



    private void discardSowFile(RoutingContext routingContext)
    {
       JsonObject jsonObject = routingContext.getBodyAsJson();
        String file_id = jsonObject.getString("id");
        this.serviceProvider.deleteFile(file_id);
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
        PriceMaster addonRate = getAccessoryPriceMaster(code, priceDate, city);
        if (addonRate == null ) {
            LOG.error("Error in retrieving addon price");
            sendError(routingContext, "Error in retrieving addon price.");
        } else {
            sendJsonResponse(routingContext, addonRate.toJson().toString());
        }
    }

    public PriceMaster getAccessoryPriceMaster(String code, Date priceDate, String city) {
        return RateCardService.getInstance().getAccessoryRate(code, priceDate, city);
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
        LOG.info("factor rate " +code + "priceDate " +priceDate+ " city " +city );
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
        LOG.info("jsonObj :: "+jsonObject);

        String queryId ;
        if(jsonObject.getDouble("version") == 1.0){
            queryId = "proposal.sow.version.copy";
        }else{
            queryId = "proposal.sow.version.copy.from2.0";
        }

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
    private class ReportTablefillerResponseHandler implements PipelineResponseHandler
    {
        private RoutingContext context;

        public ReportTablefillerResponseHandler(RoutingContext context)
        {
            this.context = context;
        }

        @Override
        public void handleResponse(List<MessageDataHolder> messageDataHolders)
        {
            LOG.debug("From proposalHandler service ");
//            message.reply(LocalCache.getInstance().store(new JsonObject().put("status","success")));
            sendJsonResponse(context,new JsonObject().put("status","success").toString());
        }
    }

    private void getBookingMonth(RoutingContext routingContext)
    {
        int proposalId = Integer.parseInt(routingContext.request().getParam("proposalId"));
        String version = (routingContext.request().getParam("version"));
        JsonObject jsonObject =  new JsonObject();
        jsonObject.put("proposalId",proposalId);
        jsonObject.put("version",version);

        Integer id = LocalCache.getInstance().store(new QueryData("proposal.version.selectversion", jsonObject));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows.isEmpty())
                    {
                        getBookingMonthOptions(routingContext,new java.util.Date(System.currentTimeMillis()),false);
                    }
                    else
                    {
                        ProposalVersion proposalVersion = new ProposalVersion(resultData.rows.get(0));
                        java.util.Date utilDate = new java.util.Date(proposalVersion.getBusinessDate().getTime());
                        getBookingMonthOptions(routingContext,utilDate,true);

                    }
                });

    }

    private void getBookingMonthOptions(RoutingContext routingContext,java.util.Date confirmedDate,boolean confirmedStatus)
    {
        List<JsonObject> months = new ArrayList<>();
        if (confirmedStatus)
        {
            LOG.info("Confirmed status true:");

            Calendar c = Calendar.getInstance();
            c.setTime(confirmedDate);
            int curr_month = c.get(Calendar.MONTH);
            int previous_month = curr_month - 1;

            String curr_monthName = concatMonthAndYear(curr_month);
            String previous_monthName = concatMonthAndYear(previous_month);

            months.add(new JsonObject().put("bookingMonth",curr_monthName));
            months.add(new JsonObject().put("bookingMonth",previous_monthName));

            sendJsonResponse(routingContext,months.toString());
        }
        else
        {
            LOG.info("Confirmed status false:");
            months.add(new JsonObject().put("bookingMonth","NA"));
            sendJsonResponse(routingContext,months.toString());
        }


    }

    public static String concatMonthAndYear(int num)
    {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }

        String month_Title = month.substring(0,3);
        DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
        String formattedDate = df.format(Calendar.getInstance().getTime());

        return month_Title + "-" + formattedDate;
    }

}



