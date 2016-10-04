package com.mygubbi.si.crm;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.StringUtils;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.route.AbstractRouteHandler;
import com.mygubbi.si.firebase.FirebaseDataRequest;
import com.mygubbi.si.firebase.FirebaseDataService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Created by sunil on 25-04-2016.
 */
public class CrmApiHandler extends AbstractRouteHandler
{
    private final static Logger LOG = LogManager.getLogger(CrmApiHandler.class);

    private String proposalDocsFolder;

    public CrmApiHandler(Vertx vertx)
    {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/createProposal").handler(this::createProposal);
        this.post("/createCustomer").handler(this::createCustomer);
        this.proposalDocsFolder = ConfigHolder.getInstance().getStringValue("proposal_docs_folder", "/tmp/");
    }

    private void createProposal(RoutingContext routingContext)
    {
        LOG.debug("create proposal request");
        if (!isRequestAuthenticated(routingContext)) return;

        JsonObject requestJson = routingContext.getBodyAsJson();
        String email = requestJson.getString("email");

        Integer id = LocalCache.getInstance().store(new QueryData("user_profile.select.email", new JsonObject().put("email", email)));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData.rows.isEmpty())
                    {
                        sendError(routingContext.response(), "User does not exist for email: " + email);
                    }
                    else
                    {
                        createProposal(routingContext, requestJson, selectData.rows.get(0));
                    }
                });

    }

    private void createProposal(RoutingContext routingContext, JsonObject requestJson, JsonObject userJson)
    {
        JsonObject proposalData = new JsonObject().put("title", "Proposal for " + userJson.getString("name")).put("createdBy", "crm");
        proposalData.put("designer", requestJson.getString("designer")); //todo: all other attributes from requestJson

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
                        this.updateProposal(routingContext, requestJson, proposalData, userJson);
                    }
                });

    }

    private void updateProposal(RoutingContext routingContext, JsonObject requestJson, JsonObject proposalData, JsonObject userJson)
    {
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.folder.update", proposalData));
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
                        updateDataInFirebase(requestJson, proposalData, userJson);
                    }
                });
    }

    private void updateDataInFirebase(JsonObject requestJson, JsonObject proposalData, JsonObject userJson)
    {
        FirebaseDataRequest dataRequest = new FirebaseDataRequest().setDataUrl("/projects/" + userJson.getString("fbid") + "/my_nest/project_details")
                .setJsonData(this.getProjectDetailsJson(requestJson));
        VertxInstance.get().eventBus().send(FirebaseDataService.UPDATE_DB, LocalCache.getInstance().store(dataRequest),
                (AsyncResult<Message<Integer>> selectResult) -> {
                    FirebaseDataRequest dataResponse = (FirebaseDataRequest) LocalCache.getInstance().remove(selectResult.result().body());
                    if (!dataResponse.isError())
                    {
                        LOG.info("Firebase updated with " + requestJson.encode());
                    }
                });
    }

    private JsonObject getProjectDetailsJson(JsonObject requestJson)
    {
        return new JsonObject().put("property_name", requestJson.getString("projectName")).put("property_type", requestJson.getString("propertyType"))
                .put("property_city", requestJson.getString("propertyAddressCity"));
    }

    private void createCustomer(RoutingContext routingContext)
    {
        LOG.debug("Create customer request.");
        if (!isRequestAuthenticated(routingContext)) return;
        JsonObject userJson = routingContext.getBodyAsJson();
        LOG.info("Create customer request : " + userJson.encodePrettily());

        String email = userJson.getString("email");
        if (StringUtils.isEmpty(email))
        {
            sendError(routingContext.response(), "Email not found in json request.");
            return;
        }

        Integer id = LocalCache.getInstance().store(new QueryData("user_profile.select.email", userJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData.rows.isEmpty())
                    {
                        try
                        {
                            createUserOnWebsite(userJson);
                            sendJsonResponse(routingContext, new JsonObject().put("status", "success").toString());
                        }
                        catch (Exception e)
                        {
                            LOG.error("Error in creating user for : " + email, e);
                            sendError(routingContext, e.getMessage());
                        }
                    }
                    else
                    {
                        sendError(routingContext.response(), "User already exists for email: " + email);
                    }
                });

    }

    private void createUserOnWebsite(JsonObject userJson)
    {
        String email = userJson.getString("email");
        String host = ConfigHolder.getInstance().getStringValue("websiteHost", null);
        if (host == null)
        {
            LOG.error("websiteHost not setup in config for creating user.");
            throw new RuntimeException("Error in creating user for : " + email);
        }
        try
        {
            String password = RandomStringUtils.random(8, true, true);
            URI uri = new URIBuilder()
                    .setScheme("https")
                    .setHost(host)
                    .setPath("/createuser")
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .setParameter("phone", userJson.getString("phone"))
                    .setParameter("name", userJson.getString("name"))
                    .build();
            HttpResponse response = Request.Get(uri).execute().returnResponse();
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
            {
                LOG.error("Error in calling website for creating user." + response.toString());
                throw new RuntimeException("Error in creating user for : " + email);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error in creating user for : " + email, e);
        }
    }

    private boolean isRequestAuthenticated(RoutingContext routingContext){
        final String authorization = routingContext.request().getHeader("Authorization");
        LOG.debug("values :" + authorization);
        if (authorization != null && authorization.startsWith("Basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));
            // credentials = username:password
            if (StringUtils.isNonEmpty(credentials)) {
                final String[] values = credentials.split(":", 2);
                LOG.debug("values :" + credentials);
                if (values[0].equals("game") && values[1].equals("Mygubbi"))
                    return true;
            }
        }
        sendError(routingContext.response(), "Credentials not valid");
        return false;
    }


}
