package com.mygubbi.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Created by nitinpuri on 09-11-2015.
 */
public class ProductHandler extends AbstractRouteHandler {

    public ProductHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/").handler(this::getAll);
        this.get("/:productId").handler(this::getProduct);
    }

    private void getAll(RoutingContext context) {
        sendJsonResponse(context, "/products.json");
    }

    private void getProduct(RoutingContext context) {
        String productId = context.request().getParam("productId");
        sendJsonResponse(context, "/product_" + productId + ".json");
    }
}
