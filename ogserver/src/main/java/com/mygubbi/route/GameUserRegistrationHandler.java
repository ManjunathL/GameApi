package com.mygubbi.route;

import com.mygubbi.common.LocalCache;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameUserRegistrationHandler extends AbstractRouteHandler {
    private final static Logger LOG = LogManager.getLogger(GameUserRegistrationHandler.class);

    public GameUserRegistrationHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/").handler(this::checkAndCreateNewUser);
    }

    private void checkAndCreateNewUser(RoutingContext routingContext) {
        JsonObject paramsObject = routingContext.getBodyAsJson();
        UserHandlerUtil.secureCredentials(paramsObject);
        Integer id = LocalCache.getInstance().store(new QueryData("game_user.select", paramsObject));
        HttpServerResponse response = routingContext.response();
        vertx.eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.info("Check query (ms):" + selectData.responseTimeInMillis);
                    if (selectData.rows.isEmpty()) {
                        this.createNewUser(paramsObject, response);
                    } else {
                        response.putHeader("content-type", "application/json")
                                .end(new JsonObject()
                                        .put("status", "error")
                                        .put("error", "User already registered.")
                                        .encode());
                    }
                });
    }

    private JsonObject createNewUser(JsonObject data, HttpServerResponse response) {
        Integer id = LocalCache.getInstance().store(new QueryData("game_user.insert", data));
        JsonObject result = new JsonObject();
        vertx.eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> res) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(res.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0) {
                        result.put("status", "error").put("error", "Error in creating user profile.");
                        response.putHeader("content-type", "application/json").end(result.encode());
                    } else {
                        LOG.info("Insert query (ms):" + resultData.responseTimeInMillis);
                        this.sendNewUserProfile(data, response);
                    }
                });
        return result;
    }

    private JsonObject sendNewUserProfile(JsonObject data, HttpServerResponse response) {
        Integer id = LocalCache.getInstance().store(new QueryData("game_user.select", data));
        JsonObject result = new JsonObject();
        vertx.eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> res2) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(res2.result().body());
                    JsonObject userProfile = selectData.rows.get(0);
                    LOG.info("Select query (ms):" + selectData.responseTimeInMillis);
                    result.put("status", "success").put("data", userProfile);
                    response.putHeader("content-type", "application/json").end(result.encode());
                    //vertx.eventBus().send("send.mail", new JsonObject().put("template", "user.register").put("data", userProfile));
                });
        return result;
    }

}
