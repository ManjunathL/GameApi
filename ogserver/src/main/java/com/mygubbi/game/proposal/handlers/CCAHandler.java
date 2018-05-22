package com.mygubbi.game.proposal.handlers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mygubbi.game.proposal.model.CustomerDocument;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.provider.CrmDataProvider;
import com.mygubbi.provider.DataProviderMode;
import com.mygubbi.provider.RestDataProvider;
import com.mygubbi.route.AbstractRouteHandler;
import com.mygubbi.si.crm.CrmApiHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

import java.util.Set;

/**
 * Created by User on 09-10-2017.
 */
public class CCAHandler extends AbstractRouteHandler{

    private final static Logger LOG = LogManager.getLogger(CCAHandler.class);
    private final ObjectMapper mapper;
    private DataProviderMode dataProviderMode;
    private CrmDataProvider crmDataProvider;


    public CCAHandler(Vertx vertx) {
        super(vertx);
        mapper = new ObjectMapper();
        dataProviderMode = new RestDataProvider();
        crmDataProvider = new CrmDataProvider();

        this.route().handler(BodyHandler.create().setUploadsDirectory("c:/Users/Public/uploads"));


        this.post("/getdocuments").handler(this::getDocuments);
        this.post("/getprofile").handler(this::getOpportunity);
        this.post("/getcustomerissues").handler(this::getIssues);
        this.post("/getupdates").handler(this::getDailyUpdates);
        this.post("/createissue").handler(this::createIssue);
        this.post("/gethandoverdetails").handler(this::getHandoverDetails);
        this.get("/getfeedbackquestions").handler(this::getQuestions);
        this.get("/getfeedbackquestionoption").handler(this::getQuestionOption);
        this.post("/addfeedbackquestionanswer").handler(this::insertQuestionAnswer);
    }

    private void getDocuments(RoutingContext routingContext) {
        if (isAuthenticated(routingContext)) return;
        getDocumentDetails(routingContext);
    }

    private void getOpportunity(RoutingContext routingContext) {
        if (isAuthenticated(routingContext)) return;
        getOpportunityDetails(routingContext);
    }

    private void getIssues(RoutingContext routingContext) {
        if (isAuthenticated(routingContext)) return;
        getIssueDetails(routingContext);
    }


    private void getDailyUpdates(RoutingContext routingContext) {
        if (isAuthenticated(routingContext)) return;
        getDailyUpdatesFromCrm(routingContext);
    }

    private void createIssue(RoutingContext routingContext) {
        if (isAuthenticated(routingContext)) return;
        createCustomerIssue(routingContext);
    }

    private void getHandoverDetails(RoutingContext routingContext) {
        if (isAuthenticated(routingContext)) return;
        getHandover(routingContext);
    }


    private boolean isAuthenticated(RoutingContext routingContext) {
        CrmApiHandler crmApiHandler = new CrmApiHandler(vertx);
        boolean requestAuthenticated = crmApiHandler.isRequestAuthenticated(routingContext);
        if (!requestAuthenticated) return true;
        return false;
    }

    private void getDocumentDetails(RoutingContext routingContext) {

        String crmId = routingContext.request().getParam("opportunity_id");

        LOG.debug("SAL ID in get Documents:" + crmId);

       JSONArray documents = crmDataProvider.getDocuments(crmId);

        if (documents.length() != 0)
        {
            sendJsonResponse(routingContext, documents.toString());
        }
        else {
            sendError(routingContext, "No documents found for this opportunity");
        }

    }

    public void getOpportunityDetails(RoutingContext routingContext) {

        String crmId = routingContext.request().getParam("opportunity_id");

        LOG.debug("SAL ID in get opportunity:" + crmId);

        JSONObject opportunityDetails = crmDataProvider.getOpportunityDetails(crmId);
        if (opportunityDetails == null)
        {
            sendError(routingContext, "No details found for this opportunity");

        }
        else {


        JSONObject detailsToImaginest = new JSONObject();
        try {
            detailsToImaginest.put("customerName",opportunityDetails.getString("customer_name_c"));
            detailsToImaginest.put("customerPhone",opportunityDetails.getString("customer_phone_c"));
            detailsToImaginest.put("salesStage",opportunityDetails.getString("sales_stage"));
            detailsToImaginest.put("salesName",opportunityDetails.getString("salesuser_name"));
            detailsToImaginest.put("salesPhone",opportunityDetails.getString("salesuser_mobile"));
            detailsToImaginest.put("supervisorName",opportunityDetails.getString("supervisor"));
            detailsToImaginest.put("supervisorPhone",opportunityDetails.getString("supervisor_mobile"));
            detailsToImaginest.put("updates",opportunityDetails.getString("comment"));
            detailsToImaginest.put("salesStage",opportunityDetails.getString("sales_stage"));
            detailsToImaginest.put("projectCompletionDate",opportunityDetails.getString("project_end_date_c"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
            sendJsonResponse(routingContext, detailsToImaginest.toString());

        }
    }

    private void getIssueDetails(RoutingContext routingContext) {

        String crmId = routingContext.request().getParam("opportunity_id");

        MultiMap attributes = routingContext.request().formAttributes();
        Set<FileUpload> uploads = routingContext.fileUploads();
        LOG.debug("uploads :" + uploads.size());
        LOG.debug("uploads :" + attributes.size());



        LOG.debug("SAL ID in get Issue details:" + crmId);

        JSONArray documents = crmDataProvider.getCustomerIssues(crmId);

        if (documents.length() != 0)
        {
            sendJsonResponse(routingContext, documents.toString());
        }
        else {
            sendError(routingContext, "No issues found for this opportunity");
        }

    }


    private void createCustomerIssue(RoutingContext routingContext){

        routingContext.response().putHeader("Content-Type", "text/plain");

        routingContext.response().setChunked(true);

        Set<FileUpload> fileUploads = routingContext.fileUploads();

        for (FileUpload fileUpload : fileUploads)
        {
            LOG.debug("File upload : " + fileUpload.fileName());
        }


        String crmId = "opportunity_name";
        String issue = "issue";
        String documents = "documents";

        LOG.debug("CUSTOMER ISSUE LOG PAR+AMS : " + crmId + ":" + issue);

        JSONObject createIssue = crmDataProvider.createCustomerIssue(crmId,issue,documents);

        if (createIssue != null)
        {
            sendJsonResponse(routingContext,createIssue.toString());
        }
        else
        {
            sendError(routingContext,"Issue could not be created");
        }
    }

    private void getHandover(RoutingContext routingContext) {
        String crmId = routingContext.request().getParam("opportunity_id");

        LOG.debug("SAL ID in get handover details:" + crmId);

        JSONArray documents = crmDataProvider.getHandoverDetails(crmId);

        if (documents.length() != 0)
        {
            sendJsonResponse(routingContext, documents.toString());
        }
        else {
            sendError(routingContext, "No handover details found for this opportunity");
        }
    }


    private void getDailyUpdatesFromCrm(RoutingContext routingContext) {
        String crmId = routingContext.request().getParam("opportunity_id");

        LOG.debug("SAL ID in get updates details:" + crmId);

        JsonArray responseArray = new JsonArray();

        JSONArray documents = crmDataProvider.getUpdates(crmId);

        try {
            for(int i = 0; i < documents.length() ; i++)
            {
                JSONObject jsonObject = (JSONObject) documents.get(i);
                String room = jsonObject.keys().next();
                int value = Integer.parseInt(jsonObject.getString(room));

                JsonObject newResponse = new JsonObject();
                newResponse.put("name",room);
                newResponse.put("status",value);
                responseArray.add(newResponse);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (documents.length() != 0)
        {
            sendJsonResponse(routingContext, responseArray.toString());
        }
        else {
            sendError(routingContext, "No updates found for this opportunity");
        }
    }
    private void getQuestions(RoutingContext routingContext)
    {
        Integer id = LocalCache.getInstance().store(new QueryData("cca.questions.select"));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag ||resultData.rows.isEmpty() )
                    {
                        sendError(routingContext, "Error in fetching the data");
                        LOG.error("Error in fetching the data " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        JsonArray firstResJson = new JsonArray();
                        for (JsonObject jsonObject : resultData.rows)
                        {
                            firstResJson.add(jsonObject);
                        }
                        sendJsonResponse(routingContext, firstResJson.toString());
                    }
                });
    }

    private void getQuestionOption(RoutingContext routingContext)
    {
        Integer id = LocalCache.getInstance().store(new QueryData("cca.questionsoption.select"));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag ||resultData.rows.isEmpty() )
                    {
                        sendError(routingContext, "Error in fetching the data");
                        LOG.error("Error in fetching the data " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        JsonArray firstResJson = new JsonArray();
                        for (JsonObject jsonObject : resultData.rows)
                        {
                            firstResJson.add(jsonObject);
                        }
                        sendJsonResponse(routingContext, firstResJson.toString());
                    }
                });
    }

    private void insertQuestionAnswer(RoutingContext routingContext) {
        for(int i=0;i<routingContext.getBodyAsJsonArray().size();i++){
            JsonObject questionanswerList=routingContext.getBodyAsJsonArray().getJsonObject(i);
            Integer id = LocalCache.getInstance().store(new QueryData("cca.insert.questionanswer", questionanswerList));
            VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                    (AsyncResult<Message<Integer>> selectResult) -> {
                        QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                        if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                        {
                            sendError(routingContext, "Error in inserting question ans answer");
                            return;
                        }
                    });
        }
        sendJsonResponse(routingContext, "succesfully inserted");
    }
}
