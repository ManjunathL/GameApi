package com.mygubbi.apiserver;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.route.AbstractRouteHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Sunil on 09-11-2015.
 */
public class ResponseFromDBHandler extends AbstractRouteHandler
{
    private final static Logger LOG = LogManager.getLogger(ResponseFromDBHandler.class);

    private String query;
    private String[] params = null;
    private String[] resultFields = null;
    private String defaultContent;

    public ResponseFromDBHandler(String query, JsonArray params, JsonArray resultFields, String defaultContent)
    {
        super(VertxInstance.get());
        this.query = query;
        this.defaultContent = defaultContent;
        this.params = this.getStringArray(params);
        this.resultFields = this.getStringArray(resultFields);
        this.get("/").handler(this::serveResponse);
    }

    private String[] getStringArray(JsonArray jsonArray)
    {
        if (jsonArray.isEmpty()) return null;
        String[] values = new String[jsonArray.size()];
        for (int i=0; i < jsonArray.size(); i++)
        {
            values[i] = jsonArray.getString(i);
        }
        return values;
    }

    private void serveResponse(RoutingContext context)
    {
        JsonObject paramsJson = this.getRequestParams(context);
        this.fetchDataAndSend(context, paramsJson);
    }

    private JsonObject getRequestParams(RoutingContext context)
    {
        JsonObject paramsJson = new JsonObject();
        if (this.params != null)
        {
            for (int i=0; i < this.params.length; i++)
            {
                paramsJson.put(this.params[i], context.request().getParam(this.params[i]));
            }
        }
        return paramsJson;
    }

    private void fetchDataAndSend(RoutingContext context, JsonObject paramsData)
    {
        Integer id = LocalCache
                .getInstance().store(new QueryData(this.query, paramsData));
        LOG.info("Executing query:" + this.query + " | " + paramsData);
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null)
                    {
                        sendError(context, "Did not find data for " + paramsData.toString() + ". Error:" + selectData.errorMessage);
                    }
                    else
                    {
                        if (this.resultFields == null)
                        {
                            sendJsonResponse(context, selectData.rows.toString());
                        }
                        else
                        {
                            sendJsonResponse(context, selectData.getJsonDataRows(this.resultFields[0]).toString());
                        }
                    }
                });

    }
}
