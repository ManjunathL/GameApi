package com.mygubbi.route;

/**
 * Created by Mehbub on 20-02-2017.
 */

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.si.email.EmailData;
import com.mygubbi.si.firebase.FirebaseDataRequest;
import com.mygubbi.si.firebase.FirebaseDataService;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;

public class PaymentHandler extends AbstractRouteHandler
{
    private final static Logger LOG = LogManager.getLogger(PaymentHandler.class);

    public PaymentHandler(Vertx vertx)
    {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/sucessPayment").handler(this::sucessPaymentData);
        this.post("/failurePayment").handler(this::sucessPaymentData);
    }

    private void sucessPaymentData(RoutingContext routingContext)

    {

        String contentType = routingContext.request().headers().get("Content-Type");
        LOG.info("contentType  "  +contentType);
        if ("application/x-www-form-urlencoded".equals(contentType)) {
            QueryStringDecoder qsd = new QueryStringDecoder(routingContext.getBody().toString(), false);
            LOG.info(qsd);
            LOG.info(qsd.parameters());

            Map<String, List<String>> params1 = qsd.parameters();
            Map<String, String> params = parameters(params1);

            System.out.println(params);
            System.out.println(params.get("status"));
            String str = params.get("cardnum");

            if(params.get("cardnum") != null &&  params.get("field9") != "Cancelled by user") {
                char[] EncryptCard = str.toCharArray();
                for (int i = 0; i < EncryptCard.length - 2; i++) {
                    EncryptCard[i] = 'X';
                }
                LOG.info(EncryptCard);
                str= new String(EncryptCard);
                LOG.info("CardEncryptNumber : " + str);
            }
            JsonObject paymentJson = new JsonObject().put("mihpayid", params.get("mihpayid").toString())
                    .put("mode", params.get("mode"))
                    .put("status", params.get("status"))
                    .put("unmappedstatus", params.get("unmappedstatus"))
                    .put("key", params.get("key"))
                    .put("txnid", params.get("txnid"))
                    .put("amount", params.get("amount"))
                    .put("cardCategory", params.get("cardCategory"))
                    .put("discount", params.get("discount"))
                    .put("net_amount_debit", params.get("net_amount_debit"))
                    .put("addedon", params.get("addedon"))
                    .put("productinfo", params.get("productinfo"))
                    .put("firstname", params.get("firstname"))
                    .put("lastname", params.get("lastname"))
                    .put("address1", params.get("address1"))
                    .put("address2", params.get("address2"))
                    .put("city", params.get("city"))
                    .put("state", params.get("state"))
                    .put("country", params.get("country"))
                    .put("zipcode", params.get("zipcode"))
                    .put("email", params.get("email"))
                    .put("phone", params.get("phone"))
                    .put("udf1", params.get("udf1"))
                    .put("udf2", params.get("udf2"))
                    .put("udf3", params.get("udf3"))
                    .put("udf4", params.get("udf4"))
                    .put("udf5", params.get("udf5"))
                    .put("udf6", params.get("udf6"))
                    .put("udf7", params.get("udf7"))
                    .put("udf8", params.get("udf8"))
                    .put("udf9", params.get("udf9"))
                    .put("udf10", params.get("udf10"))
                    .put("hash", params.get("hash"))
                    .put("field2", params.get("field2"))
                    .put("field3", params.get("field3"))
                    .put("field4", params.get("field4"))
                    .put("field5", params.get("field5"))
                    .put("field6", params.get("field6"))
                    .put("field7", params.get("field7"))
                    .put("field8", params.get("field8"))
                    .put("field9", params.get("field9"))
                    .put("payment_source", params.get("payment_source"))
                    .put("PG_TYPE", params.get("PG_TYPE"))
                    .put("bank_ref_num", params.get("bank_ref_num"))
                    .put("bankcode", params.get("bankcode"))
                    .put("error", params.get("error"))
                    .put("error_Message", params.get("error_Message"))
                    .put("name_on_card", params.get("name_on_card"))
                    .put("cardnum", str)
                    .put("cardhash", params.get("cardhash"))
                    .put("card_type", params.get("card_type"));

            JsonObject paymentJsonObj = new JsonObject().put("mihpayid", params.get("mihpayid"))
                    .put("status", params.get("status"))
                    .put("txnid", params.get("txnid"))
                    .put("amount", params.get("amount"))
                    .put("net_amount_debit", params.get("net_amount_debit"))
                    .put("addedon", params.get("addedon"))
                    .put("productinfo", params.get("productinfo"))
                    .put("firstname", params.get("firstname"))
                    .put("email", params.get("email"))
                    .put("phone", params.get("phone"))
                    .put("udf1", params.get("udf1"))
                    .put("udf2", params.get("udf2"))
                    .put("bank_ref_num", params.get("bank_ref_num"))
                    .put("bankcode", params.get("bankcode"))
                    .put("name_on_card", params.get("name_on_card"))
                    .put("cardnum", str)
                    .put("card_type", params.get("card_type"))
                    .put("fulldetails", paymentJson);
            LOG.info("Payment Json   :" + paymentJsonObj);
            LOG.info("Payment Json   :" + paymentJsonObj.getString("status"));

          //  transac(paymentJsonObj);
            Integer id = LocalCache.getInstance().store(new QueryData("transaction.insert", paymentJsonObj));
            LOG.info(id);
            VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                    (AsyncResult<Message<Integer>> res) -> {
                        LOG.info(res);
                        QueryData resultData = (QueryData) LocalCache.getInstance().remove(res.result().body());
                        LOG.info(resultData);
                        LOG.info(resultData.toString());
                        if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0) {
                            LOG.error("Error in inserting transaction. " + resultData.errorMessage, resultData.error);
                            sendError(routingContext, "Error in inserting a transaction.");
                        } else {
                            updateDataInFirebase(paymentJsonObj);
                        }
                    });
            String status = paymentJsonObj.getString("status");
            LOG.info("status=====>   " +status);
            String txn = paymentJsonObj.getString("txnid");
            LOG.info("txn=====>   " +txn);
            byte[] encodedBytes = Base64.getEncoder().encode(txn.getBytes());
            String encryptTxn = new String(encodedBytes);
            System.out.println("encodedBytes " + encryptTxn);
//            byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
//            System.out.println("decodedBytes " + new String(decodedBytes));
            if(Objects.equals(status, "failure")){
                LOG.info("Failure=====>   " +paymentJsonObj.getString("status"));
                RouteUtil.getInstance().redirect(routingContext, "https://www.mygubbi.com/payfailure-" + encryptTxn, "Redirecting to new Success Page");

            }
            else {
                LOG.info("==Success===>   " +paymentJsonObj.getString("status"));
                sendSuccessEmailToCustomer(paymentJsonObj);
                sendSuccessEmailTomygubbiTeam(paymentJsonObj);
                RouteUtil.getInstance().redirect(routingContext, "https://www.mygubbi.com/paysuccess-" + encryptTxn, "Redirecting to new Success Page");
            }
        }

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

    private void sendSuccessEmailTomygubbiTeam(JsonObject jsonData)
    {
        LOG.info("email Json Input   "   +jsonData);
        String firstname = jsonData.getString("firstname");
        String emailId = jsonData.getString("email");
        String phone = jsonData.getString("phone");
        String crmId = jsonData.getString("udf1");
        String amount = jsonData.getString("amount");
        String txnid = jsonData.getString("txnid");
            EmailData emailData = new EmailData().setFromEmail("noreply@mygubbi.com").setToEmail(emailId)
                                .setHtmlBody(true).setParams(jsonData.getMap()).setSubject("Payment Done!")
                                .setBodyTemplate("email/payment.success.vm")
                                .setSubjectTemplate("email/payment.success.subject.vm");
                        final String SENDGRID_APIKEY = "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";

                        SendGrid sendgrid = new SendGrid(SENDGRID_APIKEY);
                        SendGrid.Email email = new SendGrid.Email();
                        email.addTo(emailData.getToEmail());
                        email.setFrom(emailData.getFromEmail());
                        email.setCc(new String[]{"mehaboob.basha@mygubi.com"});
                        email.setSubject(emailData.getSubject());
                        email.setHtml(emailData.getMessageBody());
                        try {
                            SendGrid.Response response = sendgrid.send(email);
                            LOG.info("Message sent status: " + response.getMessage());
                        } catch (SendGridException e) {
                            LOG.error("Error in sending email.", e);
                        }
    }

    private void sendSuccessEmailToCustomer(JsonObject jsonData)
    {
        LOG.info("email Json Input   "   +jsonData);
        String firstname = jsonData.getString("firstname");
        String emailId = jsonData.getString("email");
        String phone = jsonData.getString("phone");
        String crmId = jsonData.getString("udf1");
        String amount = jsonData.getString("amount");
        String txnid = jsonData.getString("txnid");
            EmailData emailData = new EmailData().setFromEmail("noreply@mygubbi.com").setToEmail(emailId)
                                .setHtmlBody(true).setParams(jsonData.getMap()).setSubject("Payment Done!")
                                .setBodyTemplate("email/customer.payment.success.vm")
                                .setSubjectTemplate("email/payment.success.subject.vm");
                        final String SENDGRID_APIKEY = "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";

                        SendGrid sendgrid = new SendGrid(SENDGRID_APIKEY);
                        SendGrid.Email email = new SendGrid.Email();
                        email.addTo(emailData.getToEmail());
                        email.setFrom(emailData.getFromEmail());
                        //email.setCc(new String[]{"shashidhar.rajarao@mygubbi.com"});
                        //email.setCc(new String[]{"shashidhar.rajarao@mygubbi.com", "smruti.m@mygubbi.com" , "mehaboob.basha@mygubbi.com"});
                        email.setSubject(emailData.getSubject());
                        email.setHtml(emailData.getMessageBody());
                        try {
                            SendGrid.Response response = sendgrid.send(email);
                            LOG.info("Message sent status: " + response.getMessage());
                        } catch (SendGridException e) {
                            LOG.error("Error in sending email.", e);
                        }
    }
/*    private static void failed(JsonObject paymentJsonObj, String reason)
    {
        Integer id = LocalCache.getInstance().store(new QueryData("event_error.insert", paymentJsonObj));
        LOG.info(id);
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> res) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(res.result().body());

                    this.recordEventStatus("event_error.insert", eventData.getEventJson().put("reason", reason), eventData.getId());
                }
    }*/
    private static void updateDataInFirebase(JsonObject userObject)
    {
        FirebaseDataRequest dataRequest = new FirebaseDataRequest().setDataUrl("/transactions/" + userObject.getString("udf2") + "/myPayment/" + userObject.getString("txnid") )
                .setJsonData(userObject);
        LOG.info(dataRequest.toString());

        Integer id = LocalCache.getInstance().store(dataRequest);
        LOG.info(id);

        VertxInstance.get().eventBus().send(FirebaseDataService.UPDATE_DB, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    LOG.info(selectResult);

                    Integer id_new = selectResult.result().body();
                    LOG.info(id_new);

                    FirebaseDataRequest dataResponse = (FirebaseDataRequest) LocalCache.getInstance().remove(id_new);
                    LOG.info("dataResponse");
                    LOG.info(dataResponse);
                    if (dataResponse == null ){
                        LOG.error("Error occurred in dataResponse");
                    }

                    else if (!dataResponse.isError())
                    {
                        LOG.info("Firebase updated with " + userObject.encode());
                    }
                });
    }
}