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

import static com.mygubbi.route.UserHandlerUtil.USER_ID_KEY;

public class GameUserLoginHandler extends AbstractRouteHandler {
    private final static Logger LOG = LogManager.getLogger(GameUserLoginHandler.class);

    public GameUserLoginHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/").handler(this::login);
    }

    private void login(RoutingContext routingContext) {
        JsonObject paramsObject = routingContext.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QueryData("game_user.select", paramsObject));
        HttpServerResponse response = routingContext.response();
        vertx.eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.info("User login check query (ms):" + selectData.responseTimeInMillis);
                    if (selectData.rows == null || selectData.rows.isEmpty()) {
                        respond(response, "error", null, "User not setup.");
                        LOG.info("User not setup:" + paramsObject.getString("email"));
                    } else {
                        boolean valid = UserHandlerUtil.authenticate(paramsObject, selectData);
                        if (valid) {
                            respond(response, "success", formUserData(selectData.rows.get(0)), null);
                        } else {
                            respond(response, "error", null, "Incorrect user or password.");
                            LOG.info("Password not valid for user:" + paramsObject.getString("email"));
                        }
                    }
                });
    }

    private String formUserData(JsonObject data) {
        return "{\"email\": \"" + data.getString(USER_ID_KEY) + "\", \"role\": \"" + data.getString("role") + "\", \"phone\": \"" + data.getString("phone") + "\", \"name\": \"" + data.getString("name") + "\"}";
    }

    private void respond(HttpServerResponse response, String status, String userData, String errorMessage) {

        JsonObject jsonObject = new JsonObject().put("status", status);
        if (errorMessage != null) {
            jsonObject.put("error", "Incorrect user or password.");
        }
        if (userData != null) {
            jsonObject.put("user_data", userData);
        }
        response.putHeader("content-type", "application/json")
                .end(jsonObject.encode());
    }
}
