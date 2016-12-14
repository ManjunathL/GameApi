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
 * Created by Mehbub on 17-10-2016.
 */
public class SeoHandler  extends AbstractRouteHandler {

    private final static Logger LOG = LogManager.getLogger(ProductHandler.class);

    public SeoHandler(Vertx vertx){
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/").handler(this::getAll); //Category, Sub-Category

    }

    private void getAll(RoutingContext context) {
        String subcategory = context.request().getParam("subCategory");
        if (StringUtils.isEmpty(subcategory))
        {
            String seoCategory = context.request().getParam("category");
            String seoCity = context.request().getParam("location");
            System.out.println(seoCategory.toString());
            System.out.println("no Sub category");
            if (StringUtils.isNonEmpty(seoCategory)) {

                if (seoCategory.equals("all")) {
                    this.fetchSeosAndSenddb(context, "seo.select.all", null);

                } else {

                    JsonObject params = new JsonObject().put("category", seoCategory).put("location", seoCity);
                    this.fetchSeosAndSend(context, "seo.select.seoCategory", params);
                }
            } else {

                this.fetchSeosAndSenddb(context, "seo.select.all", null);
            }
        }
        else{
            String seoSubCategory = context.request().getParam("subCategory");
            String seoCity = context.request().getParam("location");
            System.out.println(seoSubCategory.toString());
            if (StringUtils.isNonEmpty(seoSubCategory)) {

                if (seoSubCategory.equals("all")) {
                    this.fetchSeosAndSenddb(context, "seo.select.all", null);

                } else {

                    JsonObject params = new JsonObject().put("subCategory", seoSubCategory).put("location", seoCity);
                    this.fetchSeosAndSend(context, "seo.select.seoSubCategory", params);
                }
            } else {

                this.fetchSeosAndSenddb(context, "seo.select.all", null);
            }

        }

    }

    private void fetchSeosAndSenddb(RoutingContext context, String queryId, JsonObject paramsData)
    {
        Integer id = LocalCache.getInstance().store(new QueryData(queryId, paramsData));
        LOG.info("Executing query:" + queryId + " | " + paramsData);
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null)
                    {
                        sendError(context, "Did not find SEO for " + paramsData.toString() + ". Error:" + selectData.errorMessage);
                    }
                    else
                    {

                        sendJsonResponse(context, selectData.getJsonDataRows("seoJson").toString());
                       // sendJsonResponse(context, selectData.rows.toString().toString());
                    }
                });

    }
    private void fetchSeosAndSend(RoutingContext context, String queryId, JsonObject paramsData)
    {
        Integer id = LocalCache.getInstance().store(new QueryData(queryId, paramsData));
        LOG.info("Executing query:" + queryId + " | " + paramsData);
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null)
                    {
                        sendError(context, "Did not find SEO for " + paramsData.toString() + ". Error:" + selectData.errorMessage);
                    }
                    else
                    {

                        sendJsonResponse(context, selectData.getJsonDataRows("seoJson").toString());
                       // sendJsonResponse(context, selectData.rows.toString().toString());

                    }
                });

    }

}
