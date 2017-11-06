package com.mygubbi.support;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.game.proposal.sow.SowValidatorService;
import com.mygubbi.si.email.EmailData;
import com.mygubbi.si.email.EmailService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by shilpa on 5/10/17.
 */
public class EmailSenderService extends AbstractVerticle {
    public static final String SEND_EMAIL = "send.email";
    private final static Logger LOG = LogManager.getLogger(EmailSenderService.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        this.setupEmailSender();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    private void setupEmailSender()
    {

        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(SEND_EMAIL, (Message<Integer> message) -> {

            JsonObject params = (JsonObject) LocalCache.getInstance().remove(message.body());
            String emailId = params.getString("emailId");
            String fromEmail = params.getString("fromEmail");
            String toEmail = params.getString("toEmail");
            String subject = params.getString("subject");
            JsonObject paramsObj = params.getJsonObject("paramsObj");
            String bodyTemplate = params.getString("bodyTemplate");
            String subjectTemplate = params.getString("subjectTemplate");

//
//            if(emailId.isEmpty()) {
//                EmailData emailData = new EmailData().setFromEmail(fromEmail).setToEmail(toEmail)
//                        .setHtmlBody(true).setParams(paramsObj.getMap()).setSubject(subject)
//                        .setBodyTemplate(bodyTemplate).setSubjectTemplate(subjectTemplate);
//                Integer id = LocalCache.getInstance().store(emailData);
//                VertxInstance.get().eventBus().send(EmailService.SEND_EMAIL, id,
//                        (AsyncResult<Message<Integer>> result) -> {
//
//                            if (result.succeeded()) {
//                                LOG.info("emailData"  + emailData);
//                                LOG.info("sent Mail");
//                                this.acknowledger.done(eventData);
//                            } else {
//
//                                LOG.info("USER RESGISTRATION PROCESS ERROR");
//                                this.acknowledger.failed(eventData, "Error in sending welcome email to user.");
//                            }
//                        });
//            }
//            else{
//                EmailData emailData = new EmailData().setFromEmail("noreply@mygubbi.com").setToEmail(email)
//                        .setHtmlBody(true).setParams(jsonData.getMap()).setSubject("Welcome to mygubbi!")
//                        .setBodyTemplate("email/welcome.user.vm").setSubjectTemplate("email/welcome.user.subject.vm");
//                Integer id = LocalCache.getInstance().store(emailData);
//                VertxInstance.get().eventBus().send(EmailService.SEND_EMAIL, id,
//                        (AsyncResult<Message<Integer>> result) -> {
//
//                            if (result.succeeded()) {
//                                LOG.info("emailData"  + emailData);
//
//                                LOG.info("sent Mail");
//                                this.acknowledger.done(eventData);
//                            } else {
//
//                                LOG.info("USER RESGISTRATION PROCESS ERROR");
//                                this.acknowledger.failed(eventData, "Error in sending welcome email to user.");
//                            }
//                        });
//            }

        }).completionHandler(res -> {
            LOG.info("Email Sender Service started" + res.succeeded());
        });
    }

    private void sendError(Message message,String comments) {
        JsonObject res = new JsonObject();
        res.put("status", "Failure");
        res.put("comments", comments);
//        message.reply(res);
        message.reply(LocalCache.getInstance().store(res));
    }
    private void sendSuccess(Message message,String comments) {
        JsonObject res = new JsonObject();
        res.put("status", "Success");
        res.put("comments", comments);
//        message.reply(res);
        message.reply(LocalCache.getInstance().store(res));
    }

}
