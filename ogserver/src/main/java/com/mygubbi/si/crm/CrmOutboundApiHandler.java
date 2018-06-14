package com.mygubbi.si.crm;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.route.AbstractRouteHandler;
import com.mygubbi.si.email.EmailData;
import com.mygubbi.si.firebase.FirebaseDataRequest;
import com.mygubbi.si.firebase.FirebaseDataService;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * Created by Chirag on 26-10-2016.
 */
public class CrmOutboundApiHandler extends AbstractRouteHandler {

    private final static Logger LOG = LogManager.getLogger(CrmOutboundApiHandler.class);



    public CrmOutboundApiHandler(Vertx vertx)
    {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/getOpportunityDetails").handler(this::getOpportunityDetails);
        this.get("/sendApprovalEmail").handler(this::sendEmailToSales);
        this.post("/getDocuments").handler(this::getDocuments);
        this.post("/getLatestDocuments").handler(this::getLatestDocuments);
        this.post("/updateTask").handler(this::updateTask);
        this.post("/updateDocument").handler(this::updateDocument);


    }

    private void sendEmailToSales(RoutingContext routingContext) {
        String emailId = routingContext.request().getParam("emailId");
        String status = routingContext.request().getParam("status");
        String customerName = routingContext.request().getParam("customerName");
        String customerPhone = routingContext.request().getParam("customerPhone");
        String scheduleTime = routingContext.request().getParam("scheduleTime");
        String designerEmail = routingContext.request().getParam("designerEmail");
        String crmId = routingContext.request().getParam("crmId");
        String  opportunities = "Opportunities";
        String taskType = routingContext.request().getParam("taskType");
        String  taskId = "";
        String  parentId = "1";
        JsonObject params = new JsonObject().put("emailId", emailId).put("status", status).put("scheduleTime",scheduleTime).put("customerName",customerName).put("customerPhone",customerPhone).put("designerEmail",designerEmail);
      //  JsonObject updateInCrm = new JsonObject().
        try {
            LOG.info(" Update Task" +opportunities + "," + crmId + "," + status + "," + taskType + "," + parentId);
           new CrmApiClient().updateTask(opportunities, crmId, status, taskId, taskType, parentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendWelcomeEmail(params);
        sendJsonResponse(routingContext, new JsonObject().put("status is", "Email Sent and task Updated").toString());

    }


    private void sendWelcomeEmail(JsonObject jsonData) {
    String emailId = jsonData.getString("emailId");
    String status = jsonData.getString("status");
    String customerName = jsonData.getString("customerName");
    String customerPhone = jsonData.getString("customerPhone");
    String scheduleTime = jsonData.getString("scheduleTime");
    String designerEmail = jsonData.getString("designerEmail");
    LOG.info(emailId);
    LOG.info(status);
    LOG.info(scheduleTime);
    LOG.info(customerName);
    LOG.info(customerPhone);
    LOG.info(designerEmail);
    if (status != "approved" & scheduleTime != null ) {
        EmailData emailData = new EmailData().setFromEmail("mehaboob.basha@orangegubbi.com").setToEmail("mehaboob.basha@mygubbi.com")
                .setHtmlBody(true).setParams(jsonData.getMap()).setSubject("Welcome to mygubbi!")
                .setBodyTemplate("email/cep.proposal.requestcall.vm")
                .setSubjectTemplate("email/requestcall.subject.vm");
        final String SENDGRID_APIKEY = "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";

        SendGrid sendgrid = new SendGrid(SENDGRID_APIKEY);
        SendGrid.Email email = new SendGrid.Email();
        email.addTo(emailData.getToEmail());
        email.setFrom(emailData.getFromEmail());
        email.setCc(new String[]{"smruti.m@mygubbi.com"});
        email.setSubject(emailData.getSubject());
        email.setHtml(emailData.getMessageBody());
//        email.setText("Trying again ...");

        try {
            SendGrid.Response response = sendgrid.send(email);
            LOG.info("Message sent status: " + response.getMessage());
        } catch (SendGridException e) {
            LOG.error("Error in sending email.", e);
        }
    }
    else{

        EmailData emailData = new EmailData().setFromEmail("mehaboob.basha@orangegubbi.com").setToEmail("mehaboob.basha@mygubbi.com")
                .setHtmlBody(true).setParams(jsonData.getMap()).setSubject("Welcome to mygubbi!")
                .setBodyTemplate("email/cep.proposal.approve.vm")
                .setSubjectTemplate("email/approval.subject.vm");


        final String SENDGRID_APIKEY = "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";

        SendGrid sendgrid = new SendGrid(SENDGRID_APIKEY);
        SendGrid.Email email = new SendGrid.Email();
        email.addTo(emailData.getToEmail());
        email.setFrom(emailData.getFromEmail());
        email.setCc(new String[]{"smruti.m@mygubbi.com"});
        email.setSubject(emailData.getSubject());
        email.setHtml(emailData.getMessageBody());
//        email.setText("Trying again ...");

        try {
            SendGrid.Response response = sendgrid.send(email);
            LOG.info("Message sent status: " + response.getMessage());
        } catch (SendGridException e) {
            LOG.error("Error in sending email.", e);
        }
    }

}

    private void updateDocument(RoutingContext context) {
        String parentId = context.request().getParam("parentId");
        String parentType = context.request().getParam("parentType");
        String category = context.request().getParam("category");
        String user_id = context.request().getParam("user_id");
        String file_contents = context.request().getParam("file_contents");
        String file_ext = context.request().getParam("file_ext");
        String file_name = context.request().getParam("file_name");

//        try {
//            String updateDocument = new CrmApiClient().updateDocument(parentId, parentType, category, user_id, file_contents, file_ext, file_name);
//            if (updateDocument == null || updateDocument.isEmpty()) {
//                sendJsonResponse(context, "[]");
//            } else {
//                sendJsonResponse(context, updateDocument);
//            }
//        } catch (Exception e) {
//            sendError(context, "No Documents Found : ");
//        }
    }
    private void updateTask(RoutingContext context) {
        String parentId = context.request().getParam("parentId");
        String parentType = context.request().getParam("parentType");
        String status = context.request().getParam("status");
        String task_id = context.request().getParam("task_id");
        String task_type = context.request().getParam("task_type");
        String user_id = context.request().getParam("user_id");

        try
        {
            String updateTask = new CrmApiClient().updateTask(parentId, parentType, status, task_id, task_type, user_id);
            if (updateTask == null || updateTask.isEmpty())
            {
                sendJsonResponse(context, "[]");
            }
            else{
                sendJsonResponse(context, updateTask );
            }
        }
        catch (Exception e) {
            sendError(context, "No Task Found : ");
        }
    }

    private void getLatestDocuments(RoutingContext context) {
        String parentId = context.request().getParam("parentId");
        String parentType = context.request().getParam("parentType");
        String category = context.request().getParam("category");

        try {
            String getLatestDocuments = new CrmApiClient().getLatestDocuments(parentType,parentId,category);



            if (getLatestDocuments == null || getLatestDocuments.isEmpty())
            {
                sendJsonResponse(context, "[]");
            }
            else {
                sendJsonResponse(context, getLatestDocuments);
            }
        } catch (Exception e) {
            sendError(context, "No Documents Found : ");
        }
    }

    private void getDocuments(RoutingContext context) {
        String type = context.request().getParam("type");
        String parentId = context.request().getParam("parentId");
        String parentType = context.request().getParam("parentType");
        String category = context.request().getParam("category");

        try {
            String getDocuments = new CrmApiClient().getDocuments(parentType,parentId,category,type);
            if (getDocuments == null || getDocuments.isEmpty())
            {
                sendJsonResponse(context, "[]");
            }
            else {
                sendJsonResponse(context, getDocuments);
            }
        } catch (Exception e) {
            sendError(context, "No Documents Found : ");
        }

    }

    private void getOpportunityDetails(RoutingContext routingContext) {
        String emailId = routingContext.request().getParam("emailId");
        Integer id = LocalCache.getInstance().store(new QueryData("user_profile.select.email", new JsonObject().put("email", emailId)));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                        if (selectData.rows == null || selectData.rows.isEmpty())
                        {
                            sendError(routingContext.response(), "User does not exist for email: " + emailId);
                        }
                        else
                        {
                            JsonObject userObject = selectData.rows.get(0);
                            String opportunityDetails = userObject.getString("crmId");
                            try {
                                opportunityDetails = new CrmApiClient().getOpportunityDetails(opportunityDetails);
                                if (opportunityDetails == null || opportunityDetails.isEmpty())
                                {
                                    sendJsonResponse(routingContext, "[]");
                                }
                                else {
                                    sendJsonResponse(routingContext, opportunityDetails);
                                    JsonObject oppObj = new JsonObject(opportunityDetails);
                                    updateDataInFirebase(oppObj,userObject);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
    }

    private void updateDataInFirebase(JsonObject requestJson, JsonObject userObject)
    {
        FirebaseDataRequest dataRequest = new FirebaseDataRequest().setDataUrl("/projects/" + userObject.getString("fbid") + "/myNest/")
              .setJsonData(requestJson);
        Integer id = LocalCache.getInstance().store(dataRequest);
        VertxInstance.get().eventBus().send(FirebaseDataService.UPDATE_DB, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    Integer id_new = selectResult.result().body();
                    FirebaseDataRequest dataResponse = (FirebaseDataRequest) LocalCache.getInstance().remove(id_new);
                    if (dataResponse == null ){
                        LOG.error("Error occurred in dataResponse");
                    }

                    else if (!dataResponse.isError())
                    {
                        LOG.info("Firebase updated with " + requestJson.encode());
                    }
                });
    }
}
