package com.mygubbi.si.crm;

import com.mygubbi.route.AbstractRouteHandler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Created by Chirag on 26-10-2016.
 */
public class CrmOutboundApiHandler extends AbstractRouteHandler {


    public CrmOutboundApiHandler(Vertx vertx)
    {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/getOpportunityDetails").handler(this::getOpportunityDetails);
        this.post("/getDocuments").handler(this::getDocuments);
        this.post("/getLatestDocuments").handler(this::getLatestDocuments);
        this.post("/updateTask").handler(this::updateTask);
        this.post("/updateDocument").handler(this::updateDocument);


    }

    private void updateDocument(RoutingContext context) {
        String parentId = context.request().getParam("parentId");
        String parentType = context.request().getParam("parentType");
        String category = context.request().getParam("category");
        String user_id = context.request().getParam("user_id");
        String file_contents = context.request().getParam("file_contents");
        String file_ext = context.request().getParam("file_ext");
        String file_name = context.request().getParam("file_name");

        try
        {
        String updateDocument = new CrmApiClient().updateDocument(parentId, parentType, category, user_id, file_contents, file_ext, file_name);
            if (updateDocument == null || updateDocument.isEmpty())
            {
                sendJsonResponse(context, "[]");
            }
            else{
                sendJsonResponse(context, updateDocument );
                }
            }
        catch (Exception e) {
            sendError(context, "No Documents Found : ");
        }
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

    private void getOpportunityDetails(RoutingContext context) {
        String opportunityId = context.request().getParam("opportunityId");

        try {
            String opportunityDetails = new CrmApiClient().getOpportunityDetails(opportunityId);
            if (opportunityDetails == null || opportunityDetails.isEmpty())
            {
                sendJsonResponse(context, "[]");
                System.out.print("==============" +opportunityDetails);

            }
            else {
                sendJsonResponse(context, opportunityDetails);
            }
        } catch (Exception e) {
            sendError(context, "Opportunity not valid : " + opportunityId);
        }
    }


}
