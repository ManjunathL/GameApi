package com.mygubbi.user;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.si.data.DataProcessor;
import com.mygubbi.si.data.EventAcknowledger;
import com.mygubbi.si.data.EventData;
import com.mygubbi.si.knowtify.KnowtifyService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        return "new.user";
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
        JsonObject userJson = new JsonObject().put("fbid", eventData.getUid()).put("email", jsonData.getString("email")).put("profile", jsonData);
        Integer id = LocalCache.getInstance().store(new QueryData("user_profile.insert", userJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> res) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(res.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        this.acknowledger.failed(eventData, "User already registered");
                    }
                    else
                    {
                        this.registerWithKnowtify(eventData);
                    }
                });
    }

    private void registerWithKnowtify(EventData eventData)
    {
        JsonObject jsonData = eventData.getJsonData();
        JsonObject userJson = new JsonObject().put("name", jsonData.getString("name")).put("email", jsonData.getString("email"));
        Integer id = LocalCache.getInstance().store(userJson);
        VertxInstance.get().eventBus().send(KnowtifyService.ADD_CONTACT, id,
                (AsyncResult<Message<Integer>> result) -> {
                    if (result.succeeded())
                    {
                        this.acknowledger.done(eventData);
                    }
                    else
                    {
                        this.acknowledger.failed(eventData, "Error in sending through Knowtify");
                    }
                });
    }
}
