package com.mygubbi.game.proposal.handlers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mygubbi.game.proposal.model.CustomerDocument;
import com.mygubbi.provider.CrmDataProvider;
import com.mygubbi.provider.DataProviderMode;
import com.mygubbi.provider.RestDataProvider;
import com.mygubbi.route.AbstractRouteHandler;
import com.mygubbi.si.crm.CrmApiHandler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

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

        this.post("/getdocuments").handler(this::getDocuments);
        this.post("/getprofile").handler(this::getOpportunity);
        this.post("/getcustomerissues").handler(this::getIssues);
        this.post("/getupdates").handler(this::getDailyUpdates);
        this.post("/createissue").handler(this::createIssue);
        this.post("/gethandoverdetails").handler(this::getHandoverDetails);
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
        JsonObject proposalData = routingContext.getBodyAsJson();
        LOG.debug("proposalData : " +  proposalData.encodePrettily());
        LOG.debug("context : " +  routingContext.getBodyAsJson());
        LOG.debug("name : " + routingContext.getBodyAsJson());
        LOG.debug("issue : " + routingContext.request().getParam("issue"));


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

        JSONArray documents = crmDataProvider.getUpdates(crmId);

        if (documents.length() != 0)
        {
            sendJsonResponse(routingContext, documents.toString());
        }
        else {
            sendError(routingContext, "No updates found for this opportunity");
        }
    }




}
