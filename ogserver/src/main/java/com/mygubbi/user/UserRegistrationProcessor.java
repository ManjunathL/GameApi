package com.mygubbi.user;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.si.data.DataProcessor;
import com.mygubbi.si.data.EventAcknowledger;
import com.mygubbi.si.data.EventData;
import com.mygubbi.si.email.EmailData;
import com.mygubbi.si.email.EmailService;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * Created by Sunil on 07-01-2016.
 */
public class UserRegistrationProcessor implements DataProcessor
{
    private final static Logger LOG = LogManager.getLogger(UserRegistrationProcessor.class);

    private EventAcknowledger acknowledger;

    public UserRegistrationProcessor(EventAcknowledger acknowledger)
    {
        this.acknowledger = acknowledger;
    }

    @Override
    public String getName()
    {
        return DataProcessor.USER_ADD_EVENT;
    }

    @Override
    public void process(EventData eventData)
    {
        Integer id = LocalCache.getInstance().store(new QueryData("user_profile.select.email", eventData.getJsonData()));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.info("Check query (ms):" + selectData.responseTimeInMillis);
                    if (selectData.rows.isEmpty())
                    {
                        this.createNewUser(eventData);
                    }
                    else
                    {
                        this.acknowledger.failed(eventData, "User already registered");
                    }
                });
    }

    private void createNewUser(EventData eventData)
    {
        JsonObject jsonData = eventData.getJsonData();
        LOG.info("Mehbub" +jsonData.encodePrettily());
        JsonObject userJson = new JsonObject().put("crmId", jsonData.getString("crmId")).put("fbid", eventData.getUid()).put("email", jsonData.getString("email")).put("profile", jsonData);
        LOG.info("Mehbub USER" +userJson.encodePrettily());
        this.sendWelcomeEmail(eventData);

        Integer id = LocalCache.getInstance().store(new QueryData("user_profile.insert", userJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> res) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(res.result().body());
                   LOG.info(resultData);
                   LOG.info(resultData.toString());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        this.acknowledger.failed(eventData, "User could not be recorded in database.");
                    }
                    else
                    {

                        this.sendWelcomeEmail(eventData);

                    }
                });
    }

    private void sendWelcomeEmail(EventData eventData)
    {
        JsonObject jsonData = eventData.getJsonData();

        String fName = jsonData.getString("displayName");
        String email = jsonData.getString("email");
        String testemail = "";
        LOG.info(fName);
        String crmId = jsonData.getString("crmId");
        if(crmId.isEmpty()) {
            sendToLeadSquared(jsonData);
            EmailData emailData = new EmailData().setFromEmail("team@mygubbi.com").setToEmail("mehaboob.basha@mygubbi.com")
                    .setHtmlBody(true).setParams(jsonData.getMap()).setSubject("Welcome to mygubbi!")
                    .setBodyTemplate("email/welcome.websiteuser.vm").setSubjectTemplate("email/welcome.user.subject.vm");
            Integer id = LocalCache.getInstance().store(emailData);
            VertxInstance.get().eventBus().send(EmailService.SEND_EMAIL, id,
                    (AsyncResult<Message<Integer>> result) -> {

                        if (result.succeeded()) {
                            this.acknowledger.done(eventData);
                        } else {

                            LOG.info("USER RESGISTRATION PROCESS ERROR");
                            this.acknowledger.failed(eventData, "Error in sending welcome email to user.");
                        }
                    });
        }
        else{
            sendToLeadSquared(jsonData);
            EmailData emailData = new EmailData().setFromEmail("team@mygubbi.com").setToEmail("mehaboob.basha@mygubbi.com")
                    .setHtmlBody(true).setParams(jsonData.getMap()).setSubject("Welcome to mygubbi!")
                    .setBodyTemplate("email/welcome.user.vm").setSubjectTemplate("email/welcome.user.subject.vm");
            Integer id = LocalCache.getInstance().store(emailData);
            VertxInstance.get().eventBus().send(EmailService.SEND_EMAIL, id,
                    (AsyncResult<Message<Integer>> result) -> {

                        if (result.succeeded()) {
                            this.acknowledger.done(eventData);
                        } else {

                            LOG.info("USER RESGISTRATION PROCESS ERROR");
                            this.acknowledger.failed(eventData, "Error in sending welcome email to user.");
                        }
                    });
        }
    }
    private void sendToLeadSquared(JsonObject requestJson){

        JsonObject obj = new JsonObject().put("Attribute", "FirstName")
                .put("Value", requestJson.getValue("displayName"));
        /*JsonObject obj3 = new JsonObject().put("Attribute", "LastName")
                .put("Value", "Basha");*/
        JsonObject obj1 = new JsonObject().put("Attribute","Phone").put("Value",requestJson.getValue("phone"));
        JsonObject obj2 = new JsonObject().put("Attribute","EmailAddress").put("Value",requestJson.getValue("email"));
        String accessKey = "u$r1c221f1db494ffe457d25c16814685ce";
        String secretKey = "894f1e8cbdc36c967504b9dc912905f1fc77c012";
        String api_url_base = "https://api.leadsquared.com/v2/LeadManagement.svc/Lead.Create?accessKey=" + accessKey + "&secretKey=" + secretKey ;
        System.out.println(api_url_base);
        JsonArray data = new JsonArray().add(obj);
        data.add(obj1);
        data.add(obj2);
        //data.add(obj3);
        LOG.info(data);
        LOG.info("API to send data into leadsquared");
        try {
            URL objUrl = new URL(api_url_base);
            HttpsURLConnection conn = (HttpsURLConnection) objUrl.openConnection();
            conn.setDoOutput( true );
            conn.setInstanceFollowRedirects( false );
            conn.setRequestMethod( "POST" );
            conn.setRequestProperty( "Content-Type", "application/json");
            conn.setRequestProperty( "charset", "utf-8");
            //conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            conn.setUseCaches( false );
            try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
                LOG.info("Sene data");
                LOG.info(data.encodePrettily());
                wr.writeBytes(data.encodePrettily());
                wr.flush();
                wr.close();
                int responseCode = conn.getResponseCode();
                LOG.info("Response Code : " + responseCode);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                //print result
                LOG.info(response.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        };

        return;

    }
}
