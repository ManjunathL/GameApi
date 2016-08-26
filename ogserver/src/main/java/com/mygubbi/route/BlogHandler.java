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
 * Created by Mehbub on 19-08-2016.
 */
public class BlogHandler extends AbstractRouteHandler
{
    private final static Logger LOG = LogManager.getLogger(ProductHandler.class);

    public BlogHandler(Vertx vertx)
    {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/").handler(this::getAll); //Category, Sub-Category

    }

    private void getAll(RoutingContext context) {

        String blogTags = context.request().getParam("tags");

       if (StringUtils.isNonEmpty(blogTags))
        {

            if (blogTags.equals("all")){

                this.fetchProductsAndSendb(context, "blog.select.all", "[]");

            }
            else {

                JsonObject params = new JsonObject().put("tags", blogTags);
                this.fetchProductsAndSend(context, "product.select.blogTags", params);
            }
    }
        else{

        this.fetchProductsAndSendb(context, "blog.select.all", "[]");
       }

    }
   private void fetchProductsAndSendb(RoutingContext context, String queryId, String paramsData)
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

                       sendJsonResponse(context, selectData.getJsonDataRows("blogJson").toString());
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

                        sendJsonResponse(context, selectData.getJsonDataRows("blogJson").toString());
                    }
                });

    }
}
