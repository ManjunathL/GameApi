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
 * Created by nitinpuri on 09-11-2015.
 */
public class RelatedProductHandler extends AbstractRouteHandler
{
    private final static Logger LOG = LogManager.getLogger(RelatedProductHandler.class);

    public RelatedProductHandler(Vertx vertx)
    {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/").handler(this::getRelatedProducts); //Category, Sub-Category
    }

    private void getRelatedProducts(RoutingContext context)
    {
        String category = context.request().getParam("category");
        String styleId = context.request().getParam("styleId");
        String productId = context.request().getParam("productId");

        JsonObject params = new JsonObject().put("category", category).put("styleId", styleId).put("productId", productId);
        this.fetchProductsAndSend(context, "product.select.related", params);
    }

    private void fetchProductsAndSend(RoutingContext context, String queryId, JsonObject paramsData)
    {
        Integer id = LocalCache.getInstance().store(new QueryData(queryId, paramsData));
        LOG.info("Executing query:" + queryId + " | " + paramsData);
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null)
                    {
                        sendError(context, "Did not find related products for " + paramsData.toString() + ". Error:" + selectData.errorMessage);
                    }
                    else
                    {
                        sendJsonResponse(context, selectData.getJsonDataRows("productShortJson").toString());
                    }
                });

    }
}
