package com.mygubbi.route;

import com.mygubbi.apiserver.CacheHandler;
import com.mygubbi.common.VertxInstance;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by nitinpuri on 02-02-2016.
 */
public class RouteUtil {

    private final static Logger LOG = LogManager.getLogger(RouteUtil.class);
    private static final RouteUtil instance = new RouteUtil();

    private RouteUtil() {
    }

    public static RouteUtil getInstance() {
        return instance;
    }

    public void sendError(HttpServerResponse response, String message)
    {
        response.putHeader("content-type", "application/json")
                .end(new JsonObject().put("status", "error").put("error", message).encode());
    }

    public void sendError(RoutingContext context, String message)
    {
        this.sendError(context.response(), message);
    }

    public void sendJsonResponseFromFile(RoutingContext context, String filePath)
    {
        sendResponseFromFile(context, filePath, "application/json");
    }

    public void sendJsonResponseFromFile(RoutingContext context, String filePath, String defaultContent)
    {
        sendResponseFromFile(context, filePath, "application/json", defaultContent);
    }

    public void sendResponseFromFile(RoutingContext context, String filePath, String type)
    {
        sendResponseFromFile(context, filePath, type, null);
    }

    public void sendResponseFromFile(RoutingContext context, String filePath, String type, String defaultContent)
    {
        VertxInstance.get().fileSystem().readFile(filePath, result -> {
            if (result.succeeded())
            {
                this.sendResponse(context, result.result().toString(), type);
            }
            else if (defaultContent != null)
            {
                this.sendResponse(context, defaultContent, type);
            }
            else
            {
                sendError(context, result.cause().getMessage());
                LOG.error("Could not read file at " + filePath, result.cause());
            }
        });
    }

    public void sendJsonResponse(RoutingContext context, String json)
    {
        sendResponse(context, json, "application/json");
    }

    public void sendResponse(RoutingContext context, String text, String type)
    {
        HttpServerResponse response = context.response();
        response.putHeader("Access-Control-Allow-Origin", "*")
                .putHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
                .putHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.putHeader("content-type", type).end(text);
        CacheHandler.getInstance().cache(context, type, text);
    }

}
