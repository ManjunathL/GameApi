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
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import static com.mygubbi.route.UserHandlerUtil.NEW_PASSWORD_KEY;
import static com.mygubbi.route.UserHandlerUtil.OLD_PASSWORD_KEY;

/**
 * Created by nitinpuri on 25-04-2016.
 */
public class GameUserChangePwdHandler extends AbstractRouteHandler {

    private final static Logger LOG = LogManager.getLogger(GameUserChangePwdHandler.class);

    public GameUserChangePwdHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/").handler(this::changePassword);

    }

    private void changePassword(RoutingContext routingContext) {
        JsonObject paramsObject = routingContext.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QueryData("game_user.select", paramsObject));
        HttpServerResponse response = routingContext.response();
        vertx.eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.info("Check query (ms):" + selectData.responseTimeInMillis);
                    if (selectData.rows.isEmpty()) {
                        respond(response, "error", "Incorrect user or password.");
                    } else {
                        boolean valid = UserHandlerUtil.authenticate(paramsObject, selectData, OLD_PASSWORD_KEY);
                        if (valid) {
                            UserHandlerUtil.secureCredentials(paramsObject, NEW_PASSWORD_KEY);
                            updatePassword(response, paramsObject);
                        } else {
                            respond(response, "error", "Incorrect user or password.");
                        }
                    }
                });

    }

    private void updatePassword(HttpServerResponse response, JsonObject paramsObject) {
        Integer id = LocalCache.getInstance().store(new QueryData("game_user.update_password", paramsObject));
        vertx.eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.info("Check query (ms):" + resultData.responseTimeInMillis);
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        respond(response, "error", "Not able to update password.");
                    }
                    else
                    {
                        respond(response, "success", null);
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
