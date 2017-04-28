package com.mygubbi.si.firebase;

import com.google.firebase.database.DatabaseReference;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.si.data.EventAcknowledger;
import com.mygubbi.si.data.EventData;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by test on 11-01-2016.
 */
public class FirebaseAcknowledger implements EventAcknowledger
{
    private final static Logger LOG = LogManager.getLogger(FirebaseAcknowledger.class);

    private DatabaseReference fbRef;

    public FirebaseAcknowledger(DatabaseReference fbRef)
    {
        this.fbRef = fbRef;
    }

    @Override
    public void done(EventData eventData)
    {
        this.recordEventStatus("event_log.insert", eventData.getEventJson(), eventData.getId());
    }

    @Override
    public void failed(EventData eventData, String reason)
    {
        this.recordEventStatus("event_error.insert", eventData.getEventJson().put("reason", reason), eventData.getId());
    }

    @Override
    public void failed(String id, String rawData, String reason)
    {
        JsonObject data = new JsonObject().put("reason", reason).put("event_type", "unknown").put("event_data", rawData).put("fbid", id);
        this.recordEventStatus("event_error.insert", data, id);
    }

    private void recordEventStatus(String queryId, JsonObject jsonData, String eventId)
    {
        LOG.info("Executing query : " + queryId + ". Data:" + jsonData);
        Integer id = LocalCache.getInstance().store(new QueryData(queryId, jsonData));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> res) -> {
                    LOG.info("Result:" + res);
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(res.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        LOG.error("Event is not logged into database. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        this.fbRef.child(eventId).removeValue();
                    }
                });
    }

}
