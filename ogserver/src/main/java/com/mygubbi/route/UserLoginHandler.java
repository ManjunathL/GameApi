package com.mygubbi.route;

import com.mygubbi.common.LocalCache;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.security.SecurityService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserLoginHandler extends AbstractRouteHandler {
    private final static Logger LOG = LogManager.getLogger(UserLoginHandler.class);

    public UserLoginHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/").handler(this::login);
    }

    private void login(RoutingContext routingContext) {
        JsonObject paramsObject = routingContext.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QueryData("user_profile.select.id", paramsObject));
        HttpServerResponse response = routingContext.response();
        vertx.eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.info("Check query (ms):" + selectData.responseTimeInMillis);
                    if (selectData.rows.isEmpty()) {
                        respond(response, "error", "Incorrect user or password.");
                    } else {
                        boolean valid = SecurityService.getInstance().authenticate(paramsObject, selectData.rows.get(0));

                        if (valid) {
                            respond(response, "success", null);
                        } else {
                            respond(response, "error", "Incorrect user or password.");
                        }
                    }
                });
    }

    private void respond(HttpServerResponse response, String status, String errorMessage) {

        JsonObject jsonObject = new JsonObject().put("status", status);
        if (errorMessage != null) {
            jsonObject.put("error", "Incorrect user or password.");
        }
        response.putHeader("content-type", "application/json")
                .end(jsonObject.encode());
    }
}
