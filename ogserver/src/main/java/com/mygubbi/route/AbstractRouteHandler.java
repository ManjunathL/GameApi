package com.mygubbi.route;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.RouterImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
        response.putHeader("Access-Control-Allow-Origin", "*")
                .putHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
                .putHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        InputStream in = getClass().getResourceAsStream(relPath);
        if (in != null) {
            try (BufferedReader r = new BufferedReader(new InputStreamReader(in))) {
                String l;
                String val = "";
                while ((l = r.readLine()) != null) {
                    val = val + l;
                }
                response.putHeader("content-type", "application/json").end(val);

            } catch (IOException e) {
                sendError(response, e.getMessage());
            }
        } else {
            sendError(response, "File Not Found - " + relPath);
        }


    }
}
