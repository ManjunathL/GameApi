package com.mygubbi.si.crm;

import com.mygubbi.route.AbstractRouteHandler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by sunil on 25-04-2016.
 */
public class CrmApiHandler extends AbstractRouteHandler
{
    private final static Logger LOG = LogManager.getLogger(CrmApiHandler.class);

    public CrmApiHandler(Vertx vertx)
    {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/createProposal").handler(this::createProposal);
        this.post("/createCustomer").handler(this::createCustomer);
    }

    private void createProposal(RoutingContext routingContext)
    {
        String body = routingContext.getBodyAsString();
        LOG.info("Inbound request : " + body);
        sendJsonResponse(routingContext, new JsonObject().put("status", "success").toString());
    }

    private void createCustomer(RoutingContext routingContext)
    {
        String body = routingContext.getBodyAsString();
        LOG.info("Inbound request : " + body);
        sendJsonResponse(routingContext, new JsonObject().put("status", "success").toString());
    }


}
