package com.mygubbi.si.email;

import com.mygubbi.common.LocalCache;
import com.mygubbi.si.rest.RestInvoker;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Sunil on 08-01-2016.
 */
public class EmailService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(EmailService.class);

    public static final String SEND_EMAIL = "send.email";

    private static final String SENDGRID_APIKEY = "74f3772ab4cfa162c4695b545abe93e8";


    private RestInvoker restInvoker;
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
            email.setText(emailData.getMessageBody());
        }

        try
        {
            SendGrid.Response response = sendgrid.send(email);
            LOG.info("Message sent status: " + response.toString());
            message.reply("Ok");
        }
        catch (SendGridException e)
        {
            LOG.error("Error in sending email : " + emailData.toString(), e);
            message.fail(0, "Could not send email. " + e.getMessage());
        }
    }

}
