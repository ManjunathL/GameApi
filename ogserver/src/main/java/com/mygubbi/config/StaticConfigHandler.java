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
    private static final String MAIN_JS_RESOURCE = "/js/main.js";
    private static final String IMPORTS_RESOURCE = "/imports.css";
    private static final String ROBOTS_RESOURCE = "/robots.txt";

    private final static Logger LOG = LogManager.getLogger(StaticConfigHandler.class);

    @Override
    public void handle(RoutingContext context) {
        HttpServerRequest request = context.request();
        String path = request.path();

        if (path.equals(IMPORTS_RESOURCE)) {
            String configFilePath = ConfigHolder.getInstance().getConfigValue("importsPath").toString();
            context.reroute("/" + configFilePath);

        }  else if (path.equals(MAIN_JS_RESOURCE)) {
            String configFilePath = ConfigHolder.getInstance().getConfigValue("alljs").toString();
            context.reroute("/" + configFilePath);

        } else if (path.equals(CONFIG_RESOURCE)) {
            String configFilePath = ConfigHolder.getInstance().getConfigValue("staticConfigPath").toString();
            context.reroute("/" + configFilePath);

        } else if (path.equals(ROBOTS_RESOURCE)){
            String robotsContent = ConfigHolder.getInstance().getConfigValue("robots.txt").toString();
            RouteUtil.getInstance().sendResponse(context, robotsContent, "text/plain");

        } else {
            context.next();
        }

    }

}