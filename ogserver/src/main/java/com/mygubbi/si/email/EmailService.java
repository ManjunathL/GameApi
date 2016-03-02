package com.mygubbi.si.email;

import com.mygubbi.common.LocalCache;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Sunil on 08-01-2016.
 */
public class EmailService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(EmailService.class);

    public static final String SEND_EMAIL = "send.email";
    private static final String SENDGRID_APIKEY = "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";

    private SendGrid sendgrid;

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.sendgrid = new SendGrid(SENDGRID_APIKEY);
        this.setupEmailProcessor();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }

    private void setupEmailProcessor()
    {
        EventBus eb = vertx.eventBus();
        eb.localConsumer(SEND_EMAIL, (Message<Integer> message) -> {
            this.sendEmail(message);

        }).completionHandler(res -> {
            LOG.info("EmailService registered." + res.succeeded());
        });

    }

    private void sendEmail(Message<Integer> message)
    {
        EmailData emailData = (EmailData) LocalCache.getInstance().remove(message.body());

        SendGrid.Email email = new SendGrid.Email();

        email.addTo(emailData.getToEmail());
        email.setFrom(emailData.getFromEmail());
        email.setSubject(emailData.getSubject());
        if (emailData.isHtmlBody())
        {
            email.setHtml(emailData.getMessageBody());
        }
        else
        {
            email.setText(emailData.getTextMessage());
        }

        try
        {
            SendGrid.Response response = sendgrid.send(email);
            LOG.info("Message sent status: " + response.toString());
            message.reply("Ok");
        }
        catch (Exception e)
        {
            LOG.error("Error in sending email : " + emailData.toString(), e);
            message.fail(0, "Could not send email. " + e.getMessage());
        }
    }

    public static void main(String[] args)
    {
        EmailData emailData = new EmailData().setFromEmail("team@mygubbi.com").setToEmail("debashis.s@mygubbi.com")
                .setHtmlBody(true).setParams(new JsonObject().put("name", "Debashis").getMap())
                .setSubject("Welcome to mygubbi!").setBodyTemplate("email/welcome.user.vm")
                .setSubjectTemplate("email/welcome.user.subject.vm");

        SendGrid sendgrid = new SendGrid(SENDGRID_APIKEY);
        SendGrid.Email email = new SendGrid.Email();
        email.addTo(emailData.getToEmail());
        email.setFrom(emailData.getFromEmail());
        email.setSubject(emailData.getSubject());
        email.setHtml(emailData.getMessageBody());
//        email.setText("Trying again ...");

        try
        {
            SendGrid.Response response = sendgrid.send(email);
            LOG.info("Message sent status: " + response.getMessage());
        }
        catch (SendGridException e)
        {
            LOG.error("Error in sending email.", e);
        }


    }
}
