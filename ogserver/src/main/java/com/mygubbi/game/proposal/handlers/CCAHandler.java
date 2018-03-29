package com.mygubbi.game.proposal.handlers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygubbi.game.proposal.model.CustomerDocument;
import com.mygubbi.provider.CrmDataProvider;
import com.mygubbi.provider.DataProviderMode;
import com.mygubbi.provider.RestDataProvider;
import com.mygubbi.route.AbstractRouteHandler;
import com.mygubbi.si.crm.CrmApiHandler;
import groovy.json.JsonException;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;

import java.io.InputStream;
import java.util.*;

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
        this.post("/getcustomerdetails").handler(this::getCustomerDetails);
        this.post("/createissue").handler(this::createIssue);
    }

    private void getDocuments(RoutingContext routingContext) {
        if (isAuthenticated(routingContext)) return;
        getDocumentDetails(routingContext);
    }

    private void getCustomerDetails(RoutingContext routingContext) {
        if (isAuthenticated(routingContext)) return;
        getDocumentDetails(routingContext);
    }

    private void createIssue(RoutingContext routingContext) {
        if (isAuthenticated(routingContext)) return;
        getDocumentDetails(routingContext);
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

//        JSONArray documents = getDocuments(crmId);
        if (documents.length() != 0)
        {
            sendJsonResponse(routingContext, documents.toString());
        }
        else {
            sendError(routingContext, "No documents found for this opportunity");
        }

    }

    public List<JsonObject> getOpportunity(String opportunityId) {

        JSONArray jsonArray = dataProviderMode.getResourceArray("rest-get-opportunity-details.php", new HashMap<String, String>() {
            {
                put("opportuities_id", opportunityId + "");
            }
        });
        try {
            JsonObject[] items = this.mapper.readValue(jsonArray.toString(), JsonObject[].class);
            return new ArrayList<>(Arrays.asList(items));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public JSONArray getDocuments(String opportunityId) {

        try {
            return dataProviderMode.postResourceWithFormData("get_customer_documents.php", opportunityId);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }


}
