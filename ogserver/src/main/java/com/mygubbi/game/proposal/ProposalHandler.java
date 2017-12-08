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
import com.mygubbi.report.DwReportingService;
import com.mygubbi.report.ReportTableFillerSevice;
import com.mygubbi.route.AbstractRouteHandler;
import com.mygubbi.game.proposal.output.SOWPdfOutputService;
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
import java.util.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by sunil on 25-04-2016.
 */
public class ProposalHandler extends AbstractRouteHandler
{
    private final static Logger LOG = LogManager.getLogger(ProposalHandler.class);

    private String proposalDocsFolder = null;

    public DriveServiceProvider serviceProvider;

    public BOQHandler boqHandler;

    private  final static String COLON_DELIMITER = ":";
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
        this.post("/version/confirm").handler(this::confirmVersion);
        this.post("/version/designsignoff").handler(this::dsoVersion);
        this.post("/version/productionsignoff").handler(this::psoVersion);
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
        this.proposalDocsFolder = ConfigHolder.getInstance().getStringValue("proposal_docs_folder", "/tmp/");
        LOG.info("this.proposalDocsFolder:" + this.proposalDocsFolder);
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
    private void publishVersionAfterValidation(RoutingContext context){
        JsonObject contextJson = context.getBodyAsJson();

        String verFromProposal = String.valueOf(contextJson.getDouble("version"));
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
        queryParams.put("version",contextJson.getDouble("version"));
        queryParams.put("businessDate",contextJson.getString("businessDate"));

        Integer id1 = LocalCache.getInstance().store(queryParams);

        VertxInstance.get().eventBus().send(SowValidatorService.VALIDATE_PROPOSAL_SOW, id1,
                (AsyncResult<Message<Integer>> result) -> {
                    JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
                    sendJsonResponse(context, response.toString());
                });


    }
    private void publishTheProposal(RoutingContext routingContext) {
        JsonObject queryParams = new JsonObject();
        queryParams.put("id",routingContext.getBodyAsJson().getInteger("proposalId"));
        queryParams.put("version",routingContext.getBodyAsJson().getDouble("version"));
        queryParams.put("proposalId",routingContext.getBodyAsJson().getInteger("proposalId"));
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
                LOG.info("Response is :: SUCCESS");
                JsonObject response = new JsonObject();
                response.put("status","success");
                response.put("comments","Successfully published version/proposal");
                sendJsonResponse(routingContext,response.toString());
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

    private void dsoVersion(RoutingContext routingContext){
        LOG.info("DSO HERE Shilpa");
        String queryId = "version.designsignoff";
        JsonObject proposalData = routingContext.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QueryData(queryId, proposalData));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in updating version.");
                        LOG.error("Error in updating version. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        sendEmails(routingContext);
                        sendJsonResponse(routingContext, proposalData.toString());
                    }
                });
    }

    private void psoVersion(RoutingContext routingContext){
        LOG.info("PSO HERE Shilpa");
        String queryId = "version.productionsignoff";
        JsonObject proposalData = routingContext.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QueryData(queryId, proposalData));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in updating version.");
                        LOG.error("Error in updating version. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        sendEmails(routingContext);
                        sendJsonResponse(routingContext, proposalData.toString());
                    }
                });
    }

    private void confirmVersion(RoutingContext routingContext)
    {
        LOG.info("Confirming HERE Shilpa");
        String queryId = "version.confirm";
        JsonObject proposalData = routingContext.getBodyAsJson();
        LOG.info("proposalData = "+proposalData);
        Integer id = LocalCache.getInstance().store(new QueryData(queryId, proposalData));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in updating version.");
                        LOG.error("Error in updating version. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        String sendEmails =  ConfigHolder.getInstance().getStringValue("sendEmail","No");
                       if(sendEmails.equalsIgnoreCase("yes")) {
                           sendEmails(routingContext);
                       }
                        sendJsonResponse(routingContext, proposalData.toString());
                    }
                });
    }

    private void sendEmails(RoutingContext context){
        JsonObject contextJson = context.getBodyAsJson();
        LOG.info("contextJson = "+contextJson);
        JsonObject paramsForEmail = new JsonObject();
        Integer proposalId = contextJson.getInteger("proposalId");
        String toVersion = contextJson.getDouble("version")+"";
        paramsForEmail.put("fromEmail",ConfigHolder.getInstance().getStringValue("FROM_EMAIL","game@mygubbi.com"));

        LOG.info("toVersion = "+toVersion);
        if(toVersion.equals("1.0")){
//            Send a mail to Sales, Design, Design Head and Region Head
            paramsForEmail.put("subject","Booking confirmation is done");
            paramsForEmail.put("subjectTemplate","email/confirmation.1_0.subject.vm");
            paramsForEmail.put("bodyTemplate", "email/confirmation.1_0.body.vm");
            getVersionRecord(paramsForEmail,proposalId,toVersion);
            //fill to emails and params object properly from database

        }else if(toVersion.equals("2.0")){
            paramsForEmail.put("subject","Booking confirmation is done");
            paramsForEmail.put("subjectTemplate","email/confirmation.2_0.subject.vm");
            paramsForEmail.put("bodyTemplate", "email/confirmation.2_0.body.vm");
            getVersionRecord(paramsForEmail,proposalId,toVersion);
//send a mail to Finance team (Jyoti, Sudarshan) and  Planning team (Suresh, Prabhu)
        }else if(toVersion.equals("3.0")){
            paramsForEmail.put("subject","Booking confirmation is done");
            paramsForEmail.put("subjectTemplate","email/confirmation.3_0.subject.vm");
            paramsForEmail.put("bodyTemplate", "email/confirmation.3_0.body.vm");
            getVersionRecord(paramsForEmail,proposalId,toVersion);
//send a mail to Design team(Indicating that Design is frozen and Production starting), Purchase team, Factory team
        }else{
            LOG.error("Invalid toVersion");
        }
    }

    private void getVersionRecord(JsonObject paramsForEmail,Integer proposalId,String toVersion){
        JsonObject params = new JsonObject();
        params.put("proposalId",proposalId);
        params.put("version",toVersion);
        Integer id = LocalCache.getInstance().store(new QueryData("version.getversiondetails",params));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty()) {
                        LOG.error("Proposal not found for id:" + proposalId);
                    } else {
                        ProposalVersion proposalVer = new ProposalVersion(resultData.rows.get(0));
                        getProposalHeaderAndCallSendEmail(proposalVer,paramsForEmail,proposalId,toVersion);
                    }
                });
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
                                emailIds.add(planning.getEmail());//planning
                            });

                            Collection<UsersForEmail> operationsHead = ModuleDataService.getInstance().getUserForEmail("operationsHead");
                            operationsHead.forEach(opHead -> {
                                emailIds.add(opHead.getEmail());//operations Head
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

                        sendEmailToOnConfirm(paramsForEmail);
                    }
                });

    }
    private void sendEmailToOnConfirm(JsonObject emailParams){

        String fromEmail = emailParams.getString("fromEmail");
        String[] toemails = emailParams.getString("toEmails").split(",");
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
    private void updateProposalOnConfirm(RoutingContext routingContext)
    {

        JsonObject proposalData = routingContext.getBodyAsJson();
        //send Emai and then publish
//        sendemailAndUpdateProposal(routingContext);
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
        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.QUOTATION,false);
    }

    private void downloadQuoteAndSow(RoutingContext routingContext){
        LOG.info("Routing Context :: "+routingContext);

        //LOG.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%" +quoteRequestJson.getJsonObject("proposalId"));
        LOG.info("Quote  and SOW");
        LOG.info("****************************" +routingContext.getBodyAsJson().getString("city"));
//        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.QUOTATION);
        checkValidRowsInDB(routingContext);
    }

//    private void downloadSow(RoutingContext routingContext)
//    {
////        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.QUOTATION);
//        this.createSOWPdfOutput(routingContext);
//    }

    private void downloadQuotePdf(RoutingContext routingContext)
    {
        LOG.info("Only Quote");
        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.QUOTEPDF,false);
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
        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.JOBCARD,false);
    }


    private void downloadSalesOrder(RoutingContext routingContext)
    {
        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.SALESORDER,false);
    }

    /*private void downloadProdSpec(RoutingContext routingContext)
    {
        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.PRODSPEC);
    }*/

    private void createProposalOutput(RoutingContext routingContext, ProposalOutputCreator.OutputType type,boolean ValidSows)
    {
        try
        {
            JsonObject jsonObj = routingContext.getBodyAsJson();
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
                            createSowOutputInPdf(routingContext,response,IsBookingFormFlag);
                        }else if(IsBookingFormFlag){
                            createBookingFormInPdf(routingContext,response,new JsonObject());
//                        sendJsonResponse(routingContext, response.toString());
                        }else{
                            sendJsonResponse(routingContext, response.toString());
                        }
                    });
        }
        catch (Exception e)
        {
            LOG.info("Exception " +e);
        }

    }


    private void checkValidRowsInDB(RoutingContext routingContext){
        JsonObject params = new JsonObject();
        LOG.info("routingContext.getBodyAsJson() == "+routingContext.getBodyAsJson().encodePrettily());
        params.put("proposalId",routingContext.getBodyAsJson().getInteger("proposalId"));

        String verFromProposal =routingContext.getBodyAsJson().getString("fromVersion");
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
                        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.QUOTEPDF,false);
                    }
                    else {
                        LOG.info("VAlid Sows = "+true);
                        this.createProposalOutput(routingContext, ProposalOutputCreator.OutputType.QUOTEPDF,true);
//                        createSowOutputInPdf(routingContext,res);

                    }
                });

    }
    private void createSowOutputInPdf(RoutingContext context, JsonObject quoteReponse,Boolean IsBookingFormFlag){
        JsonObject quoteRequestJson = context.getBodyAsJson();

        Boolean workContractFlag = false;
        if(quoteRequestJson.containsKey("worksContractFlag") && (quoteRequestJson.getValue("worksContractFlag") != null)){
            workContractFlag = quoteRequestJson.getString("worksContractFlag").equalsIgnoreCase("yes")?true:false;
        }
        Boolean IsWorkingContract = new Boolean(workContractFlag);


        String version = quoteRequestJson.getString("fromVersion");
        LOG.info("Version :: "+version);
        Integer id = LocalCache.getInstance().store(new QuoteRequest(quoteRequestJson, ProposalOutputCreator.OutputType.SOWPDF));
        VertxInstance.get().eventBus().send(SOWPdfOutputService.CREATE_SOW_PDF_OUTPUT, id, (AsyncResult<Message<Integer>> result) -> {
            JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
            if(IsBookingFormFlag) {
                createBookingFormInPdf(context, quoteReponse, response);
//            }else if(IsWorkingContract){
//                this.createWorksContractinPdf(context,quoteReponse,response);
//                //createMergedPdf(context,quoteReponse,response,new JsonObject());
            }else{
                createMergedPdf(context,quoteReponse,response,new JsonObject());
            }
        });
    }

    private void createBookingFormInPdf(RoutingContext context,JsonObject quoteResponse,JsonObject sowresponse){
        LOG.info("create booking form in  pdf " ) ;
        JsonObject quoteRequestJson = context.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QuoteRequest(quoteRequestJson, ProposalOutputCreator.OutputType.BOOKING_FORM));
        VertxInstance.get().eventBus().send(SOWPdfOutputService.CREATE_BOOKING_FORM_PDF_OUTPUT, id, (AsyncResult<Message<Integer>> result) -> {
            JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
            LOG.info("response of booking form pdf " +response);
            createMergedPdf(context,quoteResponse,sowresponse,response);
            LOG.info("after calling merge");

        });
    }
    private void createWorksContractinPdf(RoutingContext context,JsonObject quoteResponse,JsonObject sowresponse)
    {
        JsonObject quoteRequestJson = context.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QuoteRequest(quoteRequestJson, ProposalOutputCreator.OutputType.WORKSCONTRACT));
        VertxInstance.get().eventBus().send(SOWPdfOutputService.CREATE_WORK_CONTRACT_PDF_OUTPUT, id, (AsyncResult<Message<Integer>> result) -> {
            JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
            LOG.info("response of workscontract" +response);
            createMergedPdf(context,quoteResponse,sowresponse,response);
            LOG.info("after calling merge");

        });

    }
    private void createMergedPdf(RoutingContext routingContext,JsonObject quotePDfResponse,JsonObject sowResponse,JsonObject bookingformresponse){
        LOG.debug("createMergedPdf :" + routingContext.getBodyAsJson().toString());
        JsonObject quoteRequestJson = routingContext.getBodyAsJson();
        String city=routingContext.getBodyAsJson().getString("city");
        String version=routingContext.getBodyAsJson().getString("fromVersion");
        LOG.info("version value in merged pdf" +version);
        String bookingFormFlag=routingContext.getBodyAsJson().getString("bookingFormFlag");
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

        Integer id = LocalCache.getInstance().store(new MergePdfsRequest(inputPdfs, outputFileName,outputFileNameAfterPageNum));
        VertxInstance.get().eventBus().send(SOWPdfOutputService.CREATE_MERGED_PDF_OUTPUT, id,
                (AsyncResult<Message<Integer>> result) -> {
                    JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
                    LOG.info("Routing Context = "+response);
                    sendJsonResponse(routingContext, response.toString());
                });

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
                        JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
                        sendJsonResponse(routingContext, response.toString());
                    });
        }
        catch(Exception e)
        {
            LOG.info(e);
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
}



