package com.mygubbi.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Created by nitinpuri on 10-12-2015.
 */
public class PreSearchHandler extends AbstractRouteHandler {

    public PreSearchHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/").handler(this::getAll);
    }

    private void getAll(RoutingContext context) {
        sendJsonResponseFromFile(context, "testdata/pre_search.json");
    }

}
