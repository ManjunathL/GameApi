package com.mygubbi.si.catalog;

import com.mygubbi.route.AbstractRouteHandler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Created by nitinpuri on 09-11-2015.
 */
public class ShopifyDataHandler extends AbstractRouteHandler
{

    public ShopifyDataHandler(Vertx vertx)
    {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/").handler(this::loadData);
    }

    private void loadData(RoutingContext context)
    {
        String productFile = context.request().getParam("productFile");
        String componentFile = context.request().getParam("componentFile");
        new ShopifyCatalogConverter(productFile, componentFile).parse();
        sendJsonResponseFromFile(context, new JsonObject().put("status" ,"Data loaded").toString());
    }
}
