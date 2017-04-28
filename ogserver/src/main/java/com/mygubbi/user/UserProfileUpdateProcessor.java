package com.mygubbi.user;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.si.data.DataProcessor;
import com.mygubbi.si.data.EventAcknowledger;
import com.mygubbi.si.data.EventData;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by Sunil on 07-01-2016.
 */
public class UserProfileUpdateProcessor implements DataProcessor
{
    private final static Logger LOG = LogManager.getLogger(UserProfileUpdateProcessor.class);

    private EventAcknowledger acknowledger;

    public UserProfileUpdateProcessor(EventAcknowledger acknowledger)
    {
        this.acknowledger = acknowledger;
    }

    @Override
    public String getName()
    {
        return DataProcessor.USER_UPDATE_EVENT;
    }

    @Override
    public void process(EventData eventData)
    {
        this.updateUserProfile(eventData);
    }

    private void updateUserProfile(EventData eventData)
    {
        JsonObject jsonData = eventData.getJsonData();
        JsonObject userJson = new JsonObject().put("email", jsonData.getString("email")).put("profile", jsonData);
        Integer id = LocalCache.getInstance().store(new QueryData("user_profile.update", userJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> res) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(res.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        this.acknowledger.failed(eventData, "User could not be located in database to update.");
                    }
                    else
                    {
                        this.acknowledger.done(eventData);
                    }
                });
    }
}
