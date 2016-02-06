package com.mygubbi.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Created by mygubbi on 26/1/16.
 */
public class StoryHandler extends AbstractRouteHandler {

    public StoryHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/").handler(this::getAll);
    }

    private void getAll(RoutingContext context) {
        sendJsonResponseFromFile(context, "data/stories.json", "[]");
    }

}
