package com.mygubbi.si.crm;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.StringUtils;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.route.AbstractRouteHandler;
import com.mygubbi.si.data.EventData;
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
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;


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
     // this.get("/").handler(this::test);
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
                        createProposal(routingContext, requestJson);
                    }
                    else
                    {
                    sendError(routingContext, "customer already exists");
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

        JsonObject proposalData = new JsonObject().put("title", "Proposal for " + requestJson.getString("email")).put("cname", requestJson.getString("email")).put("designerName", requestJson.getString("designerName")).put("salesExecName", requestJson.getString("salesName"));
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
        JSONArray jsonArray2 = new JSONArray();
        jsonArray2.put(requestJson);
        System.out.println("====jsonArray2");
        System.out.println(jsonArray2);
        JsonObject crmData = new JsonObject().put("crmId", requestJson.getString("opportunityId"))
                .put("fbid", "")
                .put("email", requestJson.getString("email"))
                .put("profile", jsonArray2.toString());
        JsonObject crmDataToBeInserted = new JsonObject().put("crmId", crmData.getString("opportunityId"))
                .put("fbid", "")
                .put("email", crmData.getString("email"))
                .put("profile", crmData.getString("profile"));

        LOG.info("PROPOSAL DATA: " +crmData);
        Integer id = LocalCache.getInstance().store(new QueryData("user_profile.insert", crmDataToBeInserted));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());

                        LOG.info("Create Proposal in Else");
                        LOG.info("Done Proposal");
                        LOG.info("Staring creating profile on website");
                        createUserOnWebsite(requestJson);
                        LOG.info("created profile on website");
                        sendJsonResponse(routingContext, new JsonObject().put("status", "success").toString());

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
                        createUserOnWebsite(requestJson);
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

    private void createCustomer(RoutingContext routingContext, JsonObject requestJson)
    {
        LOG.debug("Create customer request.");
        if (!isRequestAuthenticated(routingContext)) return;
        JsonObject userJson = routingContext.getBodyAsJson();
        LOG.info("Create customer request : " + requestJson.encodePrettily());

        String email = requestJson.getString("email");
        if (StringUtils.isEmpty(email))
        {
            sendError(routingContext.response(), "Email not found in json request.");
            return;
        }

        Integer id = LocalCache.getInstance().store(new QueryData("user_profile.select.email", requestJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData.rows == null || selectData.rows.isEmpty() )
                    {
                        try
                        {

                            LOG.info("Create Customer inside " +requestJson.encodePrettily());
                          createUserOnWebsite(requestJson);
                            createProposal(routingContext, requestJson);
                            sendJsonResponse(routingContext, new JsonObject().put("status", "Success").toString());
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
            String name;
            //String name = userJson.getString("email");
            //String name = userJson.getString("first_name");
            String fullName = userJson.getString("first_name");
            String customerJson = userJson.encodePrettily();
            LOG.info(customerJson);
            // Get the index of the first space.
            if(fullName.indexOf(" ") != -1) {
                int firstSpaceIndex = fullName.indexOf(" ");
                name = fullName.substring(0, firstSpaceIndex);
            }
            else {
                name = fullName;
            }
            LOG.info("Name of Customer : " +name);
           String phone =  userJson.getString("mobile");
           // String phone =  userJson.getString("userId");
           // String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8.name());
            String encodeEmail = URLEncoder.encode(email, StandardCharsets.UTF_8.name());
            String decodedEmail = URLDecoder.decode(encodeEmail, "UTF-8");
            LOG.info("encodeEmail"   +encodeEmail);
            LOG.info("decodedEmail"   +decodedEmail);
            String newEmail = decodedEmail.replace('.', '|');
            LOG.info(newEmail);
            URI uri = new URIBuilder()
                    .setScheme("https")
                    .setHost(host)
                    //.setPath("/registeruser")
                    //.setParameter("_escaped_fragment_",fragment)
                    .setParameter("name", name)
                    .setParameter("email", newEmail)
                    .setParameter("phone", phone)
                    .setParameter("password", password)
                    .setParameter("photoUrl","null")
                    .setParameter("crmId",userJson.getString("opportunityId"))
                    .build();
            String urlString = URLDecoder.decode(customerJson, "UTF-8");
            LOG.debug("URL :" + urlString);

//            URI uri = new URIBuilder()
//                    .setScheme("https")
//                    .setHost(host)
//                    .setPath("/registeruser")
//                    .setParameter("_escaped_fragment_",fragment)
//                    .setParameter("json", userJson.encodePrettily())
//                    .build();
            LOG.debug("URL :" + uri.toString());
           /* String urlString = ;
            URI uriDecode = new URI(urlString);
           *///LOG.debug("URL DE-CODE :" + uriDecode.toString());

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
                // Getting the response body.
                String responseBody = EntityUtils.toString(response.getEntity());
                LOG.info("responseBody: " +responseBody);
                LOG.info("STATUS CODE: " +statusCode);
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    LOG.error("Error in calling website for creating user." + responseBody);
                    throw new RuntimeException("Error in creating user for : " + email);
                }
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
//            throw new RuntimeException("Error in creating user for : " + email, e);
            e.printStackTrace();

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
    private Map<String, String> parameters(Map<String, List<String>> maplist) {
        Map<String, String> map = new HashMap<String, String>();

        Set mapSet = (Set) maplist.entrySet();
        Iterator mapIterator = mapSet.iterator();
        while (mapIterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
            String keyValue = (String) mapEntry.getKey();
            List<String> value = (List<String>) mapEntry.getValue();
            map.put(keyValue, value.get(0));
        }
        return map;
    }

    private void test(RoutingContext routingContext){
        String str = "{\n" +
                "  \"opportunityId\" : \"SAL-1705-005514\",\n" +
                "  \"userId\" : \"admin\",\n" +
                "  \"first_name\" : \"Test\",\n" +
                "  \"last_name\" : \"May03\",\n" +
                "  \"email\" : \"tes090@gmail.com\",\n" +
                "  \"mobile\" : \"+91-7026705125\",\n" +
                "  \"city\" : \"Bangalore\",\n" +
                "  \"propertycity\" : \"Kalkere\",\n" +
                "  \"designerUserId\" : \"ashwini.s@mygubbi.com\",\n" +
                "  \"designerName\" : \"Ashwini S\",\n" +
                "  \"designerMobile\" : \"8884442465\",\n" +
                "  \"salesExecUserId\" : \"admin\",\n" +
                "  \"salesExecName\" : \"mygubbi Administrator\",\n" +
                "  \"salesExecMobile\" : \"9900344988\",\n" +
                "  \"floorPlanURL\" : \"14498426-8264-d237-46ab-5912ecf1d0b6\",\n" +
                "  \"kDMaxDesignURL\" : \"\",\n" +
                "  \"profile \" : \"{\\\"2WheelersNo\\\":\\\"\\\",\\\"2WheelersOwned\\\":\\\"0\\\",\\\"address\\\":\\\"\\\",\\\"anniversary\\\":\\\"\\\",\\\"annualIncome\\\":\\\"NULL\\\",\\\"bedroom\\\":\\\"0\\\",\\\"bHK\\\":\\\"3\\\",\\\"birthday\\\":\\\"\\\",\\\"boy\\\":\\\"0\\\",\\\"boyAge\\\":\\\"NULL\\\",\\\"builderName\\\":\\\"mmm\\\",\\\"businessType\\\":\\\"B2C\\\",\\\"callBackCount\\\":\\\"NULL\\\",\\\"carsNo\\\":\\\"\\\",\\\"carsOwn\\\":\\\"0\\\",\\\"currency\\\":\\\"-99\\\",\\\"education\\\":\\\"\\\",\\\"enquiryID\\\":null,\\\"facebookHandle\\\":\\\"http:\\\\/\\\\/\\\",\\\"futureFollowupDate\\\":null,\\\"geocode Status\\\":\\\"\\\",\\\"girl\\\":\\\"0\\\",\\\"girlAge\\\":\\\"NULL\\\",\\\"googleHandle\\\":\\\"http:\\\\/\\\\/\\\",\\\"intialNotification\\\":null,\\\"kitchen\\\":\\\"1\\\",\\\"latitude\\\":\\\"0.00000000\\\",\\\"linkedInHandle\\\":\\\"http:\\\\/\\\\/\\\",\\\"livingDining\\\":\\\"0\\\",\\\"married\\\":\\\"0\\\",\\\"opportunityAmount\\\":null,\\\"parentsInLawsLiveIn\\\":\\\"0\\\",\\\"positionDate\\\":\\\"\\\",\\\"possession\\\":\\\"2017-05-05\\\",\\\"productServiceInterested\\\":\\\"walkkkk\\\",\\\"profession\\\":\\\"\\\",\\\"projectName\\\":\\\"my property\\\",\\\"projectStatus\\\":\\\"Registration_Pending\\\",\\\"propertyAddressCity\\\":null,\\\"propertyType\\\":\\\"Apartment\\\",\\\"siblingLiveIn\\\":\\\"0\\\",\\\"sourceItem\\\":null,\\\"spouseAnnualIncome\\\":\\\"NULL\\\",\\\"spouseBirthday\\\":\\\"\\\",\\\"spouseEmployer\\\":\\\"\\\",\\\"spouseName\\\":\\\"\\\",\\\"spouseProfession\\\":\\\"\\\",\\\"twitter Handle\\\":\\\"http:\\\\/\\\\/\\\",\\\"block\\\":null,\\\"flat\\\":null}\"\n" +
                "}";
        JsonObject requestJson = new JsonObject(str);
        JSONObject one = new JSONObject().put("full Json", requestJson.encodePrettily());
        JSONArray arr = new JSONArray().put(requestJson.encodePrettily());
        LOG.info(arr);
        JSONArray one1 = new JSONArray().put(arr);

        LOG.info("arr put");

        JSONObject obj= new JSONObject(str.toString());

        JSONArray jsonArray2 = new JSONArray();
        jsonArray2.put(obj);
        System.out.println("====jsonArray2");
        System.out.println(jsonArray2);

        JsonObject crmData = new JsonObject().put("opportunityId", requestJson.getString("opportunityId"))
                .put("userId", requestJson.getString("userId"))
                .put("email", requestJson.getString("email"))
                .put("profile", jsonArray2.toString());
        LOG.info("===crmData");
        LOG.info(crmData);
        JsonObject email = new JsonObject().put("email", requestJson.getString("email"));


        Integer id = LocalCache.getInstance().store(new QueryData("user_profile.select.email", email));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.info("Check query (ms):" + selectData.responseTimeInMillis);
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        createNewUser(crmData, routingContext);
                    }
                    else
                    {
                        LOG.info("Create Proposal in Else");
                        updateUser(requestJson, routingContext);
                        LOG.info("Done Proposal");
                    }
                });
    }
    private static JSONArray convertToArray(JSONObject songsObject) {
        Iterator objectProperty = songsObject.keys();
        final JSONArray jsonArray = new JSONArray();
        while (objectProperty.hasNext()) {
            String key = (String) objectProperty.next();
            final JSONObject songObject = new JSONObject();
            songObject.put(key, songsObject.getString(key));
            jsonArray.put(songObject);
        }
        return jsonArray;
    }
    private void createNewUser(JsonObject jsonData,RoutingContext routingContext )
    {
        LOG.info("Creating input data" +jsonData.encodePrettily());
        JsonObject userJson = new JsonObject().put("crmId", jsonData.getString("opportunityId")).put("fbid", "").put("email", jsonData.getString("email")).put("profile", jsonData.getString("profile"));

        LOG.info("Create USER" +userJson.encodePrettily());
        // this.sendWelcomeEmail(eventData);

        Integer id = LocalCache.getInstance().store(new QueryData("user_profile.insert", userJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> res) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(res.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                    LOG.info("error");
                    }
                    else
                    {
                        LOG.info("done");
                        sendJsonResponse(routingContext,new JsonObject().put("status", "success").toString() );
                    }
                });
    }
    private void updateUser(JsonObject jsonData, RoutingContext routingContext)
    {
/*
        JsonObject jsonData = routingContext.getBodyAsJson();
*/
        LOG.info("Mehbub" +jsonData.encodePrettily());
        JsonObject userJson = new JsonObject().put("fbid", "textFbId")
                .put("email", jsonData.getString("email"));

        LOG.info("Update USER" +userJson.encodePrettily());
        // this.sendWelcomeEmail(eventData);

        Integer id = LocalCache.getInstance().store(new QueryData("user_profile.crm.update", userJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> res) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(res.result().body());
                    LOG.info(resultData);
                    LOG.info(resultData.toString());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        LOG.info("error");
                    }
                    else
                    {
                        LOG.info("done");
            sendJsonResponse(routingContext,new JsonObject().put("status", "success").toString() );
                    }
                });
    }
}
