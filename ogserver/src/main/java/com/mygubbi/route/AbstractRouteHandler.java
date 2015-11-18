package com.mygubbi.route;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.RouterImpl;

import java.net.URL;

/**
 * Created by nitinpuri on 09-11-2015.
 */
public abstract class AbstractRouteHandler extends RouterImpl {

    protected Vertx vertx;

    public AbstractRouteHandler(Vertx vertx) {
        super(vertx);
        this.vertx = vertx;
    }

    protected void sendError(HttpServerResponse response, String message) {
        response.putHeader("content-type", "application/json")
                .end(new JsonObject().put("status", "error").put("error", message).encode());
    }

    protected void sendJsonResponse(RoutingContext context, String relPath) { //todo: this method will change once ES is integrated
        HttpServerResponse response = context.response();
        URL resource = getClass().getResource(relPath);

        if (resource != null) {
            String filePath = resource.getPath();
            vertx.fileSystem().readFile(filePath, asyncResult -> {
                if (asyncResult.succeeded()) {
                    String json = asyncResult.result().toString();

                    response.putHeader("Access-Control-Allow-Origin", "*") //todo: this is only for dev for http-server to be able to call vertx
                            .putHeader("content-type", "application/json")
                            .end(json);
                } else {
                    sendError(response, asyncResult.cause().getMessage());
                }
            });
        }
        else {
            sendError(response, relPath + " is not valid!");
        }

    }
}
