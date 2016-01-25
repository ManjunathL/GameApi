package com.mygubbi.crm;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.si.data.DataProcessor;
import com.mygubbi.si.data.EventAcknowledger;
import com.mygubbi.si.data.EventData;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by test on 15-01-2016.
 */
public class NewEnquiryProcessor implements DataProcessor
{
    private final static Logger LOG = LogManager.getLogger(NewEnquiryProcessor.class);

    private EventAcknowledger acknowledger;

    public NewEnquiryProcessor(EventAcknowledger acknowledger)
    {
        this.acknowledger = acknowledger;
    }

    @Override
    public String getName()
    {
        return "consult";
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
                        this.acknowledger.done(eventData);
                    }
                });
    }

}
