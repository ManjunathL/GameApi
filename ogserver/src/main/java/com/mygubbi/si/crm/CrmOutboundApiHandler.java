package com.mygubbi.si.crm;

import com.mygubbi.route.AbstractRouteHandler;
import io.vertx.core.Vertx;
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

    }

    private void updateTask(RoutingContext context) {

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
            }
            else {
                sendJsonResponse(context, opportunityDetails);
            }
        } catch (Exception e) {
            sendError(context, "Opportunity not valid : " + opportunityId);
        }
    }


}
