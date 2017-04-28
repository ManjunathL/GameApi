package com.mygubbi.route;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;

public class ConsultHandler extends AbstractRouteHandler {
    private final static Logger LOG = LogManager.getLogger(ConsultHandler.class);

    public ConsultHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/").handler(this::consult);
    }

    private void consult(RoutingContext routingContext) {

        Set<FileUpload> uploads = routingContext.fileUploads();
        Map<String, Object> data = routingContext.data();
        HttpServerResponse response = routingContext.response();
        response.putHeader("Access-Control-Allow-Origin", "*")
                .putHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
                .putHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        response.putHeader("content-type", "application/json")
                .end(new JsonObject().put("status", "success").encode());

    }
}
