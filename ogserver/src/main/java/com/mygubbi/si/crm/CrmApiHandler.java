package com.mygubbi.si.crm;

import com.mygubbi.common.StringUtils;
import com.mygubbi.route.AbstractRouteHandler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.util.Base64;

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
        LOG.debug("create proposal");
        if (!isRequestAuthenticated(routingContext))
            return;
        String body = routingContext.getBodyAsString();
        LOG.info("Inbound request : " + body);
        sendJsonResponse(routingContext, new JsonObject().put("status", "success").toString());
    }

    private void createCustomer(RoutingContext routingContext)
    {
        LOG.debug("create customer");
        if (!isRequestAuthenticated(routingContext))
            return;
        String body = routingContext.getBodyAsString();
        LOG.info("Inbound request : " + body);
        sendJsonResponse(routingContext, new JsonObject().put("status", "success").toString());
    }

    private boolean isRequestAuthenticated(RoutingContext routingContext){
        final String authorization = routingContext.request().getHeader("Authorization");
        LOG.debug("values :" + authorization);
        if (authorization != null && authorization.startsWith("Basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));
            // credentials = username:password
            if (StringUtils.isNonEmpty(credentials)) {
                final String[] values = credentials.split(":", 2);
                LOG.debug("values :" + credentials);
                if (values[0].equals("game") && values[1].equals("Mygubbi"))
                    return true;
            }
        }
        sendError(routingContext.response(), "Credentials not valid");
        return false;
    }


}
