package com.mygubbi.route;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Shruthi on 1/29/2018.
 */
public class RecentProjectPageHandler extends AbstractRouteHandler
{
    private final static Logger LOG = LogManager.getLogger(RecentProjectPageHandler.class);

    public RecentProjectPageHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/").handler(this::getAll);
        this.get("/draft").handler(this::getAllDraft);
    }
    private void getAll(RoutingContext context) {
        Integer id = LocalCache.getInstance().store(new QueryData("recentProject.select.all", null));
        LOG.info("Executing query:" + "recentProject.select.all" + " | ");
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null) {
                        sendError(context, "Did not find blogs for . Error:" + selectData.errorMessage);
                    } else {
                        //  this.fetchLatestProductsAndSend(context, "product.select.blogTagsLatest", paramsData);

                        JsonArray localJson = new JsonArray();
                        selectData.rows.forEach(
                                item->{
                                    LOG.info("JSon I sm reading:: "+item);
                                }

                        );
                        sendJsonResponse(context, selectData.rows.toString());
                    }
                });
    }
    private void getAllDraft(RoutingContext context)
    {
        Integer id = LocalCache.getInstance().store(new QueryData("recentProject.select.draft", null));
        LOG.info("Executing query:" + "recentProject.select.all" + " | ");
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null) {
                        sendError(context, "Did not find blogs for . Error:" + selectData.errorMessage);
                    } else {
                        //  this.fetchLatestProductsAndSend(context, "product.select.blogTagsLatest", paramsData);

                        JsonArray localJson = new JsonArray();
                        selectData.rows.forEach(
                                item->{
                                    LOG.info("JSon I sm reading:: "+item);
                                }

                        );
                        sendJsonResponse(context, selectData.rows.toString());
                    }
                });
    }
}
