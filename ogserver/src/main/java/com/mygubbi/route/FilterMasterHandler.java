package com.mygubbi.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Created by nitinpuri on 09-11-2015.
 */
public class FilterMasterHandler extends AbstractRouteHandler {

    public FilterMasterHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/").handler(this::getAll);
    }

    private void getAll(RoutingContext context) {
        sendJsonResponseFromFile(context, "data/filter_master.json");
    }

}
