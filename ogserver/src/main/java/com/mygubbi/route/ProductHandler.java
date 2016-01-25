package com.mygubbi.route;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.StringUtils;
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
 * Created by nitinpuri on 09-11-2015.
 */
public class ProductHandler extends AbstractRouteHandler
{
    private final static Logger LOG = LogManager.getLogger(ProductHandler.class);

    public ProductHandler(Vertx vertx)
    {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/").handler(this::getAll); //Category, Sub-Category
        this.get("/:productId").handler(this::getProduct);
    }

    private void getAll(RoutingContext context)
    {
        String subCategory = context.request().getParam("subcategory");
        if (StringUtils.isEmpty(subCategory))
        {
            JsonObject params = new JsonObject().put("category", context.request().getParam("category"));
            this.fetchProductsAndSend(context, "product.select.category", params);
        }
        else
        {
            JsonObject params = new JsonObject().put("subcategory", subCategory);
            this.fetchProductsAndSend(context, "product.select.subcategory", params);
        }
    }

    private void getProduct(RoutingContext context)
    {
        String productId = context.request().getParam("productId");

        Integer id = LocalCache.getInstance().store(new QueryData("product.select.productid", new JsonObject().put("productId", productId)));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        sendError(context, "Did not find product for " + productId + ". Error:" + selectData.errorMessage);
                    }
                    else
                    {
                        sendJsonResponse(context, selectData.rows.get(0).toString());
                    }
                });

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
                        sendError(context, "Did not find products for " + paramsData.toString() + ". Error:" + selectData.errorMessage);
                    }
                    else
                    {
                        sendJsonResponse(context, selectData.getJsonDataRows("productJson").toString());
                    }
                });

    }
}
