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
 * Created by mehaboob on 4/28/2016.
 */
public class CatalogProductHandler extends AbstractRouteHandler {
    private final static Logger LOG = LogManager.getLogger(CatalogProductHandler.class);

    public CatalogProductHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/").handler(this::getAll);
    }

    private void getAll(RoutingContext context) {

        String productId = context.request().getParam("");

        Integer id = LocalCache.getInstance().store(new QueryData("product.select.all", new JsonObject()));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    LOG.info("Executing query:" + "product.select.all" );
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        sendError(context, "Did not find product for "  + ". Error:" + selectData.errorMessage);
                    }
                    else
                    {

                        sendJsonResponse(context, selectData.rows.toString());


                        sendJsonResponse(context, selectData.getJsonDataRows("productJson").toString());


                    }
                });

    }
}

