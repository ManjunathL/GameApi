package com.mygubbi.si.knowtify;

import com.mygubbi.common.LocalCache;
import com.mygubbi.db.QueryPrepareService;
import com.mygubbi.user.UserProfileData;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

/**
 * Created by test on 08-01-2016.
 */
public class KnowtifyService extends AbstractVerticle
{
    public static final String ADD_CONTACT = "add.contact";
    public static final String SEND_EMAIL = "send.email";
    private static final String API_KEY = "74f3772ab4cfa162c4695b545abe93e8";
    private static final String AUTH_HEADER = "Token token=\"" + API_KEY + "\"";

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        this.setupContactProcessor();
        this.setupEmailSender();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    private void setupContactProcessor()
    {
        EventBus eb = vertx.eventBus();
        eb.localConsumer(ADD_CONTACT, (Message<Integer> message) -> {

            UserProfileData qData = (UserProfileData) LocalCache.getInstance().remove(message.body());
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
