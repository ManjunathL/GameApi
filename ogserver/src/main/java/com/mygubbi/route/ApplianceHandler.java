package com.mygubbi.route;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.StringUtils;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        sendJsonResponseFromFile(context, "data/appliances." + type + ".json");
    }

}
