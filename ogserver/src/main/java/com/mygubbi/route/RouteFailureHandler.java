package com.mygubbi.route;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by nitinpuri on 02-02-2016.
 */
public class RouteFailureHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext context) {

        int statusCode = context.statusCode();
        HttpServerResponse response = context.response();
        String uri = context.request().uri();

        if (statusCode == 404 && !uri.contains(".")) {
            RouteUtil.getInstance().sendResponseFromFile(context, "webroot/index.html", "text/html");
        } else {
            response.setStatusCode(statusCode).end("Sorry we have encountered an Error. Please refresh the page or go to <a href='#'>Home</a> to continue.");
        }

    }
}
