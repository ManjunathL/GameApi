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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Sunil on 07-01-2016.
 */
public class AddToShortlistProcessor implements DataProcessor
{
    private final static Logger LOG = LogManager.getLogger(AddToShortlistProcessor.class);

    private EventAcknowledger acknowledger;

    public AddToShortlistProcessor(EventAcknowledger acknowledger)
    {
        this.acknowledger = acknowledger;
    }

    @Override
    public String getName()
    {
        return DataProcessor.SHORTLIST_ADD_EVENT;
    }

    @Override
    public void process(EventData eventData)
    {
        this.addToShortlist(eventData);
    }

    private void addToShortlist(EventData eventData)
    {
        JsonObject jsonData = eventData.getJsonData();
        JsonObject productJson = jsonData.getJsonObject("product");
        JsonObject shortlistJson = new JsonObject().put("uid", eventData.getUid()).put("email", jsonData.getString("email"))
                .put("productId", productJson.getString("productId")).put("product", productJson);
        Integer id = LocalCache.getInstance().store(new QueryData("shortlist.add", shortlistJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> res) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(res.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        this.acknowledger.failed(eventData, "Shortlist could not be recorded in database.");
                    }
                    else
                    {
                        this.acknowledger.done(eventData);
                    }
                });
    }

}
