package com.mygubbi.route;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.StringUtils;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Mehbub on 06-07-2017.
 */
public class LandingPageHandler extends AbstractRouteHandler
{
    private final static Logger LOG = LogManager.getLogger(BlogHandler.class);

    public LandingPageHandler(Vertx vertx)
    {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/").handler(this::getAll); //Category, Sub-Category
        //this.get("/latestTag").handler(this::getLatest); //Category, Sub-Category
    }
    private void getAll(RoutingContext context) {

        String page_url = context.request().getParam("page_url");
        JsonObject params = new JsonObject().put("page_url", page_url);

        Integer id = LocalCache.getInstance().store(new QueryData("page.select.page_url", params));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    LOG.info("Executing query:" + "page.select.page_url" );
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        sendError(context, "Did not find pages for "  + ". Error:" + selectData.errorMessage);
                    }
                    else
                    {

                        sendJsonResponse(context, selectData.rows.toString());

/*
                        sendJsonResponse(context, selectData.getJsonDataRows("productJson").toString());
*/


                    }
                });

    }
}