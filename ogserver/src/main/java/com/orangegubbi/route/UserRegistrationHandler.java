package com.orangegubbi.route;

import com.orangegubbi.common.LocalCache;
import com.orangegubbi.db.QueryData;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserRegistrationHandler extends AbstractRouteHandler {
    private final static Logger LOG = LogManager.getLogger(UserRegistrationHandler.class);

    public UserRegistrationHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/").handler(this::checkAndCreateNewUser);
    }

    private void checkAndCreateNewUser(RoutingContext routingContext) {
        JsonObject paramsObject = routingContext.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QueryData("user_profile.select.id", paramsObject));
        HttpServerResponse response = routingContext.response();
        vertx.eventBus().send("db.query", id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.info("Check query (ms):" + selectData.responseTimeInMillis);
                    if (selectData.rows.isEmpty()) {
                        JsonObject data = this.createNewUser(paramsObject);
                        response.putHeader("content-type", "application/json").end(data.encode());
                    } else {
                        response.putHeader("content-type", "application/json")
                                .end(new JsonObject()
                                        .put("status", "error")
                                        .put("error", "Error in creating user profile.")
                                        .encode());
                    }
                });
    }

    private JsonObject createNewUser(JsonObject data) {
        Integer id = LocalCache.getInstance().store(new QueryData("user_profile.insert", data));
        JsonObject result = new JsonObject();
        vertx.eventBus().send("db.query", id,
                (AsyncResult<Message<Integer>> res) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(res.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0) {
                        result.put("status", "error").put("error", "Error in creating user profile.");
                    } else {
                        LOG.info("Insert query (ms):" + resultData.responseTimeInMillis);
                        result.put("status", "success").put("data", this.sendNewUserProfile(data));
                    }
                });
        return result;
    }

    private JsonObject sendNewUserProfile(JsonObject data) {
        Integer id = LocalCache.getInstance().store(new QueryData("user_profile.select.id", data));
        JsonObject result = new JsonObject();
        vertx.eventBus().send("db.query", id,
                (AsyncResult<Message<Integer>> res2) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(res2.result().body());
                    JsonObject userProfile = selectData.rows.get(0);
                    LOG.info("Select query (ms):" + selectData.responseTimeInMillis);
                    result.mergeIn(userProfile);
                    vertx.eventBus().send("send.mail", new JsonObject().put("template", "user.register").put("data", userProfile));
                });
        return result;
    }

}
