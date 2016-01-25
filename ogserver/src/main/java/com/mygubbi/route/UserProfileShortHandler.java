package com.mygubbi.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Created by nitinpuri on 09-11-2015.
 */
public class UserProfileShortHandler extends AbstractRouteHandler {

    public UserProfileShortHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/:username").handler(this::getShortProfile);
    }

    private void getShortProfile(RoutingContext context) {
        String username = context.request().getParam("username");
        sendJsonResponseFromFile(context, "/user_profile_short_" + username + ".json");
    }
}
