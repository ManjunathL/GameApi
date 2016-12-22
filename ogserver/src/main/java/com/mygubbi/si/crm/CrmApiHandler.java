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
import com.mygubbi.si.firebase.FirebaseService;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
      //  this.post("/createCustomer").handler(this::createCustomer);
        this.proposalDocsFolder = ConfigHolder.getInstance().getStringValue("proposal_docs_folder", "/tmp/");
    }

    private void createProposal(RoutingContext routingContext)
    {
        new DeploymentOptions().setWorker(true);

        LOG.debug("create proposal request");
        if (!isRequestAuthenticated(routingContext)) return;
        JsonObject requestJson = routingContext.getBodyAsJson();
        LOG.debug("JSON :" + requestJson.encodePrettily());
        //createCustomer(routingContext);
        String email = requestJson.getString("email");
        Integer id = LocalCache.getInstance().store(new QueryData("user_profile.select.email", new JsonObject().put("email", email)));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData.rows == null || selectData.rows.isEmpty())
                    {
                        createCustomer(routingContext);
                    }
                    else
                    {
                        createProposal(routingContext, requestJson);

                    }
                });
    }


    private void createProposal(RoutingContext routingContext, JsonObject requestJson)
    {
        JsonObject userJson = routingContext.getBodyAsJson();
        LOG.info("USER JSON:------>");
        LOG.info(userJson);
        LOG.info("request Json:------>");
        LOG.info(requestJson);
        String stringToBeInserted = requestJson.toString();

        JsonObject proposalData = new JsonObject().put("title", "Proposal for " + requestJson.getString("first_name")).put("cname", requestJson.getString("first_name")).put("designerName", requestJson.getString("designerName")).put("salesExecName", requestJson.getString("salesName"));
       // proposalData.put("fullJson", requestJson);
        proposalData.put("createdBy", requestJson.getString("designerName"));
        proposalData.put("opportunityId", requestJson.getString("opportunityId"));
        proposalData.put("userId", requestJson.getString("userId"));
        proposalData.put("email", requestJson.getString("email"));
        proposalData.put("designerUserId", requestJson.getString("designerUserId"));
        proposalData.put("designerName", requestJson.getString("designerName"));
        proposalData.put("salesExecUserId", requestJson.getString("salesExecUserId"));
        proposalData.put("salesExecName", requestJson.getString("salesExecName"));
        proposalData.put("floorPlanURL", requestJson.getString("floorPlanURL"));
        proposalData.put("kDMaxDesignURL", requestJson.getString("kDMaxDesignURL"));
        proposalData.put("salesExecUserId", requestJson.getString("salesExecUserId"));

        String Json = requestJson.getString("profile ");
        JsonObject jsonObjectProfile = new JsonObject(Json);
        proposalData.put("profile",jsonObjectProfile);
        LOG.info("PROPOSAL DATA: " +proposalData);
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
                        LOG.info("Create Proposal in Else");
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
                        LOG.info("Done Proposal");
                        this.updateProposal(routingContext, requestJson, proposalData);
                    }
                });
    }

    private void updateProposal(RoutingContext routingContext, JsonObject requestJson, JsonObject proposalData)
    {
        LOG.info("updateProposal Started");
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

                        //sendJsonResponse(routingContext, proposalData.encodePrettily());
                       // updateDataInFirebase(requestJson, proposalData);
                        LOG.info("updateProposal Success in else");
                        sendJsonResponse(routingContext, new JsonObject().put("status", "success").toString());
                    }
                });
    }

    public void updateDataInFirebase(JsonObject requestJson, JsonObject proposalData)
    {
        LOG.info("Update in Firebase");
        LOG.info(proposalData.encodePrettily());
        LOG.info(requestJson.encodePrettily());

                String email = proposalData.getString("email");
        LOG.info("=============" +email);

        Integer id1 = LocalCache.getInstance().store(new QueryData("user_profile.select.email", new JsonObject().put("email", email)));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id1,
                (AsyncResult<Message<Integer>> selectResult1) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult1.result().body());


                    if (selectData.rows == null || selectData.rows.isEmpty())
                    {
                        LOG.info("No Data into DB");
                       // sendError(routingContext.response(), "User does not exist for email: " + email);
                    }
                    else
                    {
                    String fbid = selectData.rows.get(0).getString("fbid");
                        //JsonObject jsonEmail = new JsonObject(selectData.rows.get(0).getString("fbid"));
                        LOG.info("JSON fbid:" + fbid);
                       // createProposal(routingContext, requestJson, selectData.rows.get(0));
                FirebaseDataRequest dataRequest = new FirebaseDataRequest().setDataUrl("/projects/" + fbid + "/myNest/projectDetails")
                .setJsonData(this.getProjectDetailsJson(proposalData));
        Integer id = LocalCache.getInstance().store(dataRequest);
        VertxInstance.get().eventBus().send(FirebaseDataService.UPDATE_DB, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    LOG.debug("select Result :" + selectResult.result());
                    Integer id_new = selectResult.result().body();
                    FirebaseDataRequest dataResponse = (FirebaseDataRequest) LocalCache.getInstance().remove(id_new);
                    LOG.debug("Firebase data response :" + dataResponse);
                    if (dataResponse == null ){
                        LOG.error("Error Occured in dataResponse");
                    }

                    else if (!dataResponse.isError())
                    {
                        LOG.info("Firebase updated with " + requestJson.encode());
                    }
                });
           }
        });
    }


    private JsonObject getProjectDetailsJson(JsonObject requestJson)
    {
        String propertyName = requestJson.getJsonObject("profile").getString("projectName");
        String propertyType = requestJson.getJsonObject("profile").getString("propertyType");
        String propertyAddressCity = requestJson.getJsonObject("profile").getString("propertyAddressCity");
        String blockNumber = requestJson.getJsonObject("profile").getString("blockNumber");
        String builderName = requestJson.getJsonObject("profile").getString("builderName");
        String flatNumber = requestJson.getJsonObject("profile").getString("propertyAddressCity");
        if (propertyAddressCity == null)
        {
            propertyAddressCity = "NA";
        }
        if (propertyName == null)
        {
            propertyName = "NA";
        }
        if (propertyType == null)
        {
            propertyType = "NA";
        }
        if (blockNumber == null)
        {
            blockNumber = "NA";
        }
        if (builderName == null)
        {
            builderName = "NA";
        }
        if (flatNumber == null)
        {
            flatNumber = "NA";
        }

        return new JsonObject().put("propertyName", propertyName).put("propertyType", propertyType)
                .put("propertyCity", propertyAddressCity).put("blockNumber",blockNumber).put("builderName",builderName).put("flatNumber",flatNumber);
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
                    if (selectData.rows == null || selectData.rows.isEmpty() )
                    {
                        try
                        {

                            LOG.info("Create Customer inside " +userJson.encodePrettily());
                           createUserOnWebsite(userJson);
                            createProposal(routingContext, userJson);

                        //  sendJsonResponse();
                        //sendJsonResponse(routingContext, new JsonObject().put("status", "success").toString());
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

    private int createUserOnWebsite(JsonObject userJson)
    {
        String acceptSSLCertificates = ConfigHolder.getInstance().getStringValue("acceptSSLCertificates","true");
        String email = userJson.getString("email");
        String fragment = "key1";
        String host = ConfigHolder.getInstance().getStringValue("websiteHost", null);
        if (host == null)
        {
            LOG.error("websiteHost not setup in config for creating user.");
            throw new RuntimeException("Error in creating user for : " + email);
        }
        try
        {
            HttpResponse response;
           // String password = RandomStringUtils.random(8, true, true);
            String password = "mygubbi";
            String name = userJson.getString("email");
           // String name = userJson.getString("firstName");
           // String phone =  userJson.getString("mobile");
            String phone =  userJson.getString("userId");
            String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8.name());

            LOG.info("decodedEmail"   +decodedEmail);
            URI uri = new URIBuilder()
                    .setScheme("https")
                    .setHost(host)
                    .setPath("/registerUser.html")
                    .setParameter("_escaped_fragment_",fragment)
                    .setParameter("name", name)
                    .setParameter("email", email)
                    .setParameter("phone", phone)
                    .setParameter("password", password)
                    .setParameter("photoUrl","null")
                    .setParameter("crmId",userJson.getString("opportunityId"))
                    .build();
           // LOG.debug("URL :" + uri.toString());
            String urlString = URLDecoder.decode(uri.toString(), "UTF-8");
            URI uriDecode = new URI(urlString);
            LOG.debug("URL DE-CODE :" + uriDecode.toString());

            if (acceptSSLCertificates.equals("true"))
            {
                org.apache.http.ssl.SSLContextBuilder context_b = SSLContextBuilder.create();
                context_b.loadTrustMaterial(new org.apache.http.conn.ssl.TrustSelfSignedStrategy());
                SSLContext ssl_context = context_b.build();
                org.apache.http.conn.ssl.SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(ssl_context,
                        new org.apache.http.conn.ssl.NoopHostnameVerifier());

                HttpClientBuilder builder = HttpClients.custom()
                        .setSSLSocketFactory(sslSocketFactory);
                CloseableHttpClient httpclient = builder.build();
                LOG.info("acceptSSLCertificates True");

               response = httpclient.execute(new HttpGet(uri));
                int statusCode = response.getStatusLine().getStatusCode();
                LOG.info("STATUS CODE: " +statusCode);
                return statusCode;
            }
            else
            {
                LOG.info("acceptSSLCertificates False");

                response = Request.Get(uri).execute().returnResponse();
            }
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    LOG.error("Error in calling website for creating user." + response.toString());
                    throw new RuntimeException("Error in creating user for : " + email);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error in creating user for : " + email, e);
        }
        return 0;
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
