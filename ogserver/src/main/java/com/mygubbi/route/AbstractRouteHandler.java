package com.mygubbi.route;

import com.mygubbi.common.VertxInstance;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.RouterImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by nitinpuri on 09-11-2015.
 */
public abstract class AbstractRouteHandler extends RouterImpl
{
    private final static Logger LOG = LogManager.getLogger(AbstractRouteHandler.class);

    protected Vertx vertx;

    public AbstractRouteHandler(Vertx vertx)
    {
        super(vertx);
        this.vertx = vertx;
    }

    protected void sendError(HttpServerResponse response, String message)
    {
        response.putHeader("content-type", "application/json")
                .end(new JsonObject().put("status", "error").put("error", message).encode());
    }

    protected void sendError(RoutingContext context, String message)
    {
        this.sendError(context.response(), message);
    }

    protected void sendJsonResponseFromFile(RoutingContext context, String filePath)
    {
        VertxInstance.get().fileSystem().readFile(filePath, result -> {
            if (result.succeeded())
            {
                this.sendJsonResponse(context, result.result().toString());
            }
            else
            {
                sendError(context, result.cause().getMessage());
                LOG.error("Could not read file at " + filePath, result.cause());
            }
        });

    }

    protected void sendJsonResponse(RoutingContext context, String json)
    {
        HttpServerResponse response = context.response();
        response.putHeader("Access-Control-Allow-Origin", "*")
                .putHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
                .putHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.putHeader("content-type", "application/json").end(json);
    }

    private void fileRead(HttpServerResponse response, String filePath)
    {
        InputStream in = getClass().getResourceAsStream(filePath);
        if (in != null)
        {
            try (BufferedReader r = new BufferedReader(new InputStreamReader(in)))
            {
                String l;
                String val = "";
                while ((l = r.readLine()) != null)
                {
                    val = val + l;
                }
                response.putHeader("content-type", "application/json").end(val);

            }
            catch (IOException e)
            {
                sendError(response, e.getMessage());
            }
        }
        else
        {
            sendError(response, "File Not Found - " + filePath);
        }

    }
}
