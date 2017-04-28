package com.mygubbi.route;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.RouterImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by nitinpuri on 09-11-2015.
 */
public abstract class AbstractRouteHandler extends RouterImpl
{
    private final static Logger LOG = LogManager.getLogger(AbstractRouteHandler.class);

    private RouteUtil routeUtil = RouteUtil.getInstance();
    protected Vertx vertx;

    public AbstractRouteHandler(Vertx vertx)
    {
        super(vertx);
        this.vertx = vertx;
    }

    protected void sendError(HttpServerResponse response, String message)
    {
        routeUtil.sendError(response, message);
    }

    protected void sendError(RoutingContext context, String message)
    {
        routeUtil.sendError(context, message);
    }

    protected void sendJsonResponseFromFile(RoutingContext context, String filePath)
    {
        routeUtil.sendJsonResponseFromFile(context, filePath);
    }

    protected void sendJsonResponseFromFile(RoutingContext context, String filePath, String defaultContent)
    {
        routeUtil.sendJsonResponseFromFile(context, filePath, defaultContent);
    }

    protected void sendJsonResponse(RoutingContext context, String json)
    {
        routeUtil.sendJsonResponse(context, json);
    }

}
