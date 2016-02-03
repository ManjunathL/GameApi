package com.mygubbi.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Created by nitinpuri on 09-11-2015.
 */
public class ShortlistShortHandler extends AbstractRouteHandler {

    public ShortlistShortHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/:username").handler(this::getShortShortlist);
    }

    private void getShortShortlist(RoutingContext context) {
        String username = context.request().getParam("username");
        sendJsonResponseFromFile(context, "/shortlisted_items_short_" + username + ".json");
    }
}
