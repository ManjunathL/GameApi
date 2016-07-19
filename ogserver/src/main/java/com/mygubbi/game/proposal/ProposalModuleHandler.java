package com.mygubbi.game.proposal;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.route.AbstractRouteHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by sunil on 25-04-2016.
 */
public class ProposalModuleHandler extends AbstractRouteHandler
{
    private final static Logger LOG = LogManager.getLogger(ProposalModuleHandler.class);

    public ProposalModuleHandler(Vertx vertx)
    {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/price").handler(this::getPrice);
    }

    private void getPrice(RoutingContext routingContext)
    {
        JsonObject moduleJson = routingContext.getBodyAsJson();
        ProductModule module = new ProductModule(moduleJson);
        Integer id = LocalCache.getInstance().store(module);
        VertxInstance.get().eventBus().send(ModulePricingService.CALCULATE_PRICE, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    JsonObject result = (JsonObject) LocalCache.getInstance().remove(selectResult.result().body());
                    sendJsonResponse(routingContext, result.toString());
                });
    }

}
