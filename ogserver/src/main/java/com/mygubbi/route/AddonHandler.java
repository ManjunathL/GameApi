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
 * Created by SWADHA on 20-Mar-17.
 */
public class AddonHandler extends AbstractRouteHandler {

    private final static Logger LOG = LogManager.getLogger(BlogHandler.class);


    public AddonHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/").handler(this::getAll);
        this.get("/category").handler(this::getCategory);
    }

    private void getAll(RoutingContext context) {

        Integer id = LocalCache.getInstance().store(new QueryData("addon-master.select.all", new JsonObject()));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    LOG.info("Executing query:" + "addon-master.select.all");
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty()) {
                        sendError(context, "Did not find product for " + ". Error:" + selectData.errorMessage);
                    } else {

                        sendJsonResponse(context, selectData.rows.toString());

/*
                        sendJsonResponse(context, selectData.getJsonDataRows("productJson").toString());
*/


                    }
                });

    }

    private void getCategory(RoutingContext context) {

        String addonCategory = context.request().getParam("categoryCode");

        if (StringUtils.isNonEmpty(addonCategory)) {
            JsonObject params = new JsonObject().put("categoryCode", addonCategory);
            this.fetchCategoryAddonsAndSend(context, "addon-master.select.category", params);
        } else {

            sendError(context, " Error:" + "Did not find blogs because" + " " + "no tags selected");
        }

    }

    private void fetchCategoryAddonsAndSend(RoutingContext context, String queryId, JsonObject paramsData) {
        Integer id = LocalCache.getInstance().store(new QueryData(queryId, paramsData));
        LOG.info("Executing query:" + queryId + " | " + paramsData);
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null) {
                        sendError(context, "Did not find products for " + paramsData.toString() + ". Error:" + selectData.errorMessage);
                    } else {

                        sendJsonResponse(context, selectData.rows.toString());
                    }
                });
    }
}
