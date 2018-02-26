package com.mygubbi.crm;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.si.data.DataProcessor;
import com.mygubbi.si.data.EventAcknowledger;
import com.mygubbi.si.data.EventData;
import com.mygubbi.si.email.EmailData;
import com.mygubbi.si.email.EmailService;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by test on 15-01-2016.
 */
public class NewEnquiryProcessor implements DataProcessor
{
    private final static Logger LOG = LogManager.getLogger(NewEnquiryProcessor.class);

    private EventAcknowledger acknowledger;
    private String emailIdToSendTo;

    public NewEnquiryProcessor(EventAcknowledger acknowledger)
    {
        this.acknowledger = acknowledger;
        this.emailIdToSendTo = ConfigHolder.getInstance().getStringValue("enquirymailto", "debashis.s@mygubbi.com");
    }

    @Override
    public String getName()
    {
        return DataProcessor.CONSULT_EVENT;
    }

    @Override
    public void process(EventData eventData)
    {
        Integer id = LocalCache.getInstance().store(new QueryData("enquiry.insert", eventData.getJsonDataWithIds()));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> res) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(res.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        this.acknowledger.failed(eventData, "Enquiry not recorded. " + resultData.errorMessage);
                    }
                    else
                    {
                        LOG.info("Enquiry recorded in database");
                        this.sendEnquiryEmail(eventData);
                        this.acknowledger.done(eventData);
                    }
                });
    }

    private void sendEnquiryEmail(EventData eventData)
    {
        JsonObject jsonData = eventData.getJsonData();
        EmailData emailData = new EmailData().setFromEmail("team@mygubbi.com").setToEmail(this.emailIdToSendTo)
                .setHtmlBody(true).setParams(jsonData.getMap()).setSubject("New enquiry")
                .setBodyTemplate("email/new.enquiry.vm").setSubjectTemplate("email/new.enquiry.subject.vm");
        Integer id = LocalCache.getInstance().store(emailData);
        VertxInstance.get().eventBus().send(EmailService.SEND_EMAIL, id,
                (AsyncResult<Message<Integer>> result) -> {
                    if (result.succeeded())
                    {
                        LOG.info("Email sent for new enquiry in database");
                    }
                    else
                    {
                        this.acknowledger.failed(eventData, "Error in sending welcome email to user.");
                    }
                });
    }

}
