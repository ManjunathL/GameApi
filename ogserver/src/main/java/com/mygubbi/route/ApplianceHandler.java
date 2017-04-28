package com.mygubbi.route;

import com.mygubbi.common.StringUtils;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by Sunil on 01-02-2016.
 */
public class ApplianceHandler extends AbstractRouteHandler
{
    private final static Logger LOG = LogManager.getLogger(ApplianceHandler.class);

    public ApplianceHandler(Vertx vertx)
    {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/").handler(this::getForCategory);
    }

    private void getForCategory(RoutingContext context)
    {
        String type = context.request().getParam("category");
        if (StringUtils.isEmpty(type))
        {
            type = context.request().getParam("subcategory");
        }
        sendJsonResponseFromFile(context, "data/appliances." + type + ".json", "[]");
    }

}
