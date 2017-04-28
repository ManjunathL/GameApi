package com.mygubbi.si.knowtify;

import com.mygubbi.common.LocalCache;
import com.mygubbi.si.rest.RequestData;
import com.mygubbi.si.rest.RestInvoker;
import com.mygubbi.si.rest.RestResponseHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by test on 08-01-2016.
 */
public class KnowtifyService extends AbstractVerticle implements RestResponseHandler
{
    private final static Logger LOG = LogManager.getLogger(KnowtifyService.class);

    public static final String ADD_CONTACT = "add.contact";

    private static final String API_KEY = "74f3772ab4cfa162c4695b545abe93e8";
    private static final String AUTH_HEADER = "Token token=\"" + API_KEY + "\"";
    public static final String CONTACTS_UPSERT_URI = "/contacts/upsert";
    public static final String WELCOME_USER_EVENT = "welcome.user";

    private RestInvoker restInvoker;

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.restInvoker = new RestInvoker(this, "http://www.knowtify.io/api/v1", AUTH_HEADER);
        this.setupContactProcessor();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }

    private void setupContactProcessor()
    {
        EventBus eb = vertx.eventBus();
        eb.localConsumer(ADD_CONTACT, (Message<Integer> message) -> {
            this.addContact(message);

        }).completionHandler(res -> {
            LOG.info("KnowtifyService contact handler registered." + res.succeeded());
        });

    }

    private void addContact(Message<Integer> message)
    {
        JsonObject userProfile = (JsonObject) LocalCache.getInstance().remove(message.body());
        KnowtifyRequest requestData = new KnowtifyRequest(userProfile, message).setContactsPaylod();
        this.restInvoker.request(requestData, CONTACTS_UPSERT_URI);
    }

    @Override
    public void handle(RequestData requestData, int statusCode, String response)
    {
        KnowtifyRequest data = (KnowtifyRequest)requestData;
        if (!data.eventSent)
        {
            this.restInvoker.request(data.setEventPaylod(WELCOME_USER_EVENT), CONTACTS_UPSERT_URI);
        }
        else
        {
            data.message.reply(data.userProfile);
        }
    }

    @Override
    public void error(RequestData requestData, int statusCode, String error)
    {
        ((KnowtifyRequest)requestData).message.fail(0, "Knowtify request failed for payload : " + requestData.getPaylod());
    }

    private static class KnowtifyRequest implements RequestData
    {
        public JsonObject userProfile;
        public Message message;
        private String payload;
        private boolean eventSent = false;

        public KnowtifyRequest(JsonObject userProfile, Message message)
        {
            this.userProfile = userProfile;
            this.message = message;
        }

        public KnowtifyRequest setContactsPaylod()
        {
            JsonArray contacts = new JsonArray();
            contacts.add(this.userProfile);
            this.payload = new JsonObject().put("contacts", contacts).toString();
            return this;
        }

        public KnowtifyRequest setEventPaylod(String event)
        {
            this.eventSent = true;
            JsonArray contacts = new JsonArray();
            contacts.add(userProfile);
            this.payload = new JsonObject().put("contacts", contacts).put("event", event).toString();
            return this;
        }

        @Override
        public String getPaylod()
        {
            return this.payload;
        }
    }
}
