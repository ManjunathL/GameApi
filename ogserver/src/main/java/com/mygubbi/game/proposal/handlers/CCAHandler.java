package com.mygubbi.game.proposal.handlers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mygubbi.game.proposal.model.CustomerDocument;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.URLDecoder;

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

        String amazon_uploads_directory = ConfigHolder.getInstance().getStringValue("amazon_uploads_directory", "c:/Users/Public/uploads");
        this.route().handler(BodyHandler.create().setUploadsDirectory(amazon_uploads_directory));

        this.post("/getdocuments").handler(this::getDocuments);
        this.post("/getprofile").handler(this::getOpportunity);
        this.post("/updateprofile").handler(this::updateOpportunity);
        this.post("/getcustomerissues").handler(this::getIssues);
        this.post("/getupdates").handler(this::getDailyUpdates);
        this.post("/createissue").handler(this::createIssue);
        this.post("/gethandoverdetails").handler(this::getHandoverDetails);
        this.get("/getfeedbackquestions").handler(this::getQuestions);
        this.get("/getfeedbackquestionoption").handler(this::getQuestionOption);
        this.post("/addfeedbackquestionanswer").handler(this::insertQuestionAnswer);
        this.post("/uploaddocument").handler(this::uploadDocument);
    }

    private void getDocuments(RoutingContext routingContext) {
        if (isAuthenticated(routingContext)) return;
        getDocumentDetails(routingContext);
    }

    private void getOpportunity(RoutingContext routingContext) {
        if (isAuthenticated(routingContext)) return;
        getOpportunityDetails(routingContext);
    }

    private void updateOpportunity(RoutingContext routingContext) {
        if (isAuthenticated(routingContext)) return;
        updateOpportunityInCrm(routingContext);
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

    private void uploadDocument(RoutingContext routingContext) {
        if (isAuthenticated(routingContext)) return;
        uploadHandoverDocument(routingContext);
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
            detailsToImaginest.put("id",opportunityDetails.getString("id"));
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
            detailsToImaginest.put("feedbackSubmitted",opportunityDetails.getString("feedback_submitted_c"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
            sendJsonResponse(routingContext, detailsToImaginest.toString());

        }
    }

    private void getIssueDetails(RoutingContext routingContext) {

        String crmId = routingContext.request().getParam("opportunity_id");


        JSONArray documents = crmDataProvider.getCustomerIssues(crmId);

        if (documents.length() != 0)
        {
            sendJsonResponse(routingContext, documents.toString());
        }
        else {
            sendError(routingContext, String.valueOf(new JsonObject().put("status","error").put("message","Error in retrieving issues")));
        }

    }

    private void updateOpportunityInCrm(RoutingContext routingContext) {

        String crmId = routingContext.request().getParam("opportunity_id");
        String feedbackSubmitted = routingContext.request().getParam("feedbackSubmitted");

        JSONObject documents = crmDataProvider.updateOpportunity(crmId,feedbackSubmitted);

        if (documents != null)
        {
            sendJsonResponse(routingContext, documents.toString());
        }
        else {
            sendError(routingContext, String.valueOf(new JsonObject().put("status","error").put("message","Could not update profile")));
        }

    }


    private void createCustomerIssue(RoutingContext routingContext){

        String amazon_uploads_directory = ConfigHolder.getInstance().getStringValue("amazon_uploads_directory", "c:/Users/Public/uploads");
        this.route().handler(BodyHandler.create().setUploadsDirectory(amazon_uploads_directory));

        routingContext.response().putHeader("Content-Type", "text/plain");

        routingContext.response().setChunked(true);

        Set<FileUpload> fileUploads = routingContext.fileUploads();

        LOG.debug("FILE uploads : " + fileUploads.size());

        String newFile = null;

        for (FileUpload fileUpload : fileUploads) {
            newFile = fileUpload.uploadedFileName();
            fileUpload.fileName();
            fileUpload.contentType();
        }

        String crmId = routingContext.request().getFormAttribute("opportunity_id");
        String issue = routingContext.request().getFormAttribute("issue");
        if (newFile != null) {
            createIssueInCrmNew(routingContext,newFile,issue,crmId);
        }
        else {
            createIssueInCrmNew(routingContext,"",issue,crmId);
        }

    }

    private void uploadHandoverDocument(RoutingContext routingContext){

        String amazon_uploads_directory = ConfigHolder.getInstance().getStringValue("amazon_uploads_directory", "c:/Users/Public/uploads");
        this.route().handler(BodyHandler.create().setUploadsDirectory(amazon_uploads_directory));

        routingContext.response().putHeader("Content-Type", "text/plain");

        routingContext.response().setChunked(true);

        Set<FileUpload> fileUploads = routingContext.fileUploads();

        LOG.debug("FILE uploads : " + fileUploads.size());
        String crmId = routingContext.request().getFormAttribute("opportunity_id");


        String newFile = null;

        for (FileUpload fileUpload : fileUploads) {
            newFile = fileUpload.uploadedFileName();
            fileUpload.fileName();
            fileUpload.contentType();
        }

        CloseableHttpClient client = HttpClientBuilder.create().build();


        HttpPost post = new HttpPost("https://suite.mygubbi.com/mygubbi_crm29102017/test-api/upload_completion_certificate.php");

        MultipartEntityBuilder builder;

        File fileNew = null;
        if (newFile != null) {
            fileNew = new File(newFile);
        }
        builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("document", fileNew, ContentType.DEFAULT_BINARY, newFile);
        builder.addTextBody("opportunity_name", crmId, ContentType.TEXT_PLAIN);

        HttpEntity entity = builder.build();
        post.setEntity(entity);
        try {
            HttpResponse response = client.execute(post);
            if (response != null)
            {
                LOG.debug(response.getEntity());
                sendJsonResponse(routingContext, String.valueOf(new JsonObject().put("status","success").put("message","Document uploaded")));
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        QueryData value = new QueryData("cca.questionsoption.select");
        QueryData value1 = new QueryData("cca.question.distinct.select");
        List<QueryData> queryList = new ArrayList<>();
        queryList.add(value);
        queryList.add(value1);
        Integer id = LocalCache.getInstance().store(queryList);
        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    List<QueryData> resultData = (ArrayList<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.get(0).errorFlag ||resultData.get(0).rows.isEmpty() )
                    {
                        sendError(routingContext, "Error in fetching the data");
                        LOG.error("Error in fetching the data " + resultData.get(0).errorMessage, resultData.get(0).error);
                    }
                    else
                    {
                        List<JsonObject> questions =  resultData.get(1).rows;
                        List<JsonObject> options =  resultData.get(0).rows;
                        manipulateOptions(routingContext,questions,options );
                    }
                });
    }

    private void manipulateOptions(RoutingContext routingContext, List<JsonObject> questions, List<JsonObject> options)
    {
        JsonArray whole_array = new JsonArray();
        for (JsonObject question_object : questions)
        {
            JsonObject jsonGroup = new JsonObject();
            JsonArray options_new = new JsonArray();
            String questionCode = question_object.getString("questionCode");
            String questionGroupCode1 = question_object.getString("questionGroupCode");

            JsonObject jsonObject1 ;

            for (JsonObject optionsObject : options)
            {
                String questionCode1 = optionsObject.getString("questionCode");
                String questionGroupCode = optionsObject.getString("questionGroupCode");

                if (questionCode1.equals(questionCode) && questionGroupCode.equals(questionGroupCode1))
                {
                    jsonObject1 = new JsonObject();
                    jsonObject1.put("answer_code",optionsObject.getString("answerCode"));
                    jsonObject1.put("answer_icon",optionsObject.getString("answerIcon"));
                    options_new.add(jsonObject1);
                }
            }
            jsonGroup.put("question_code",questionCode);
            jsonGroup.put("options",options_new);
            whole_array.add(jsonGroup);
        }

        sendJsonResponse(routingContext,whole_array.toString());

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

    private void createIssueInCrmNew(RoutingContext routingContext,String imageFileName, String issue, String crmId)
    {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        String open_assigned = "Open_Assigned";
        String others = "Others";

        HttpPost post = new HttpPost("https://suite.mygubbi.com/mygubbi_crm29102017/test-api/create_customer_issue.php");

        MultipartEntityBuilder builder;

        if (Objects.equals(imageFileName, ""))
        {
            builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addTextBody("opportunity_name", crmId, ContentType.TEXT_PLAIN);
            builder.addTextBody("name", issue, ContentType.TEXT_PLAIN);
            builder.addTextBody("status", open_assigned, ContentType.TEXT_PLAIN);
            builder.addTextBody("room_c", others, ContentType.TEXT_PLAIN);
        }
        else
        {

            File fileNew = new File(imageFileName);
            builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addBinaryBody("documents", fileNew, ContentType.DEFAULT_BINARY, imageFileName);
            builder.addTextBody("opportunity_name", crmId, ContentType.TEXT_PLAIN);
            builder.addTextBody("name", issue, ContentType.TEXT_PLAIN);
            builder.addTextBody("status", open_assigned, ContentType.TEXT_PLAIN);
            builder.addTextBody("room_c", others, ContentType.TEXT_PLAIN);
        }

        HttpEntity entity = builder.build();
        post.setEntity(entity);
        try {
            HttpResponse response = client.execute(post);
            if (response != null)
            {
                LOG.debug(response.getEntity());
                sendJsonResponse(routingContext, String.valueOf(new JsonObject().put("Success","Image upload")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
