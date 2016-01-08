package com.mygubbi.si.email;

import com.mygubbi.common.LocalCache;
import com.mygubbi.db.QueryData;
import com.mygubbi.db.QueryPrepareService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

/**
 * Created by test on 07-01-2016.
 */
public class EmailSender extends AbstractVerticle
{
    public static final String SEND_EMAIL = "send.email";

    private void setupMessageHandler()
    {
        EventBus eb = vertx.eventBus();
        eb.localConsumer(SEND_EMAIL, (Message<Integer> message) -> {

            QueryData qData = (QueryData) LocalCache.getInstance().remove(message.body());
            QueryPrepareService.getInstance().prepareQueryData(qData);
            if (qData.errorFlag)
            {
                message.reply(LocalCache.getInstance().store(qData));
                return;
            }
            handleQuery(message, qData);
        }).completionHandler(res -> {
            LOG.info("Database Service handler registered." + res.succeeded());
        });
    }

}
