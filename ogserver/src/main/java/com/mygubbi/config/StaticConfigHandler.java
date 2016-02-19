package com.mygubbi.config;

import com.mygubbi.route.RouteUtil;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by nitinpuri on 16-02-2016.
 */
public class StaticConfigHandler implements Handler<RoutingContext> {

    private static final String CONFIG_RESOURCE = "/js/config.js";

    private final static Logger LOG = LogManager.getLogger(StaticConfigHandler.class);

    @Override
    public void handle(RoutingContext context) {
        HttpServerRequest request = context.request();
        String uri = request.uri();

        if (uri.equals(CONFIG_RESOURCE)) {

            String configFilePath = ConfigHolder.getInstance().getConfigValue("staticConfigPath").toString();
            RouteUtil.getInstance().sendResponseFromFile(context, configFilePath, "application/javascript");

        } else {
            context.next();
        }

    }

}
