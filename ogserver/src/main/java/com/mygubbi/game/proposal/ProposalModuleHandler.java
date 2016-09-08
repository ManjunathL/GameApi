package com.mygubbi.game.proposal;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.game.proposal.model.AccessoryPack;
import com.mygubbi.game.proposal.price.ModulePricingService;
import com.mygubbi.route.AbstractRouteHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

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
        this.get("/accpacks").handler(this::getAccessoryPacksForModule);
    }


    private void getAccessoryPacksForModule(RoutingContext routingContext)
    {
        String mgCode = routingContext.request().getParam("mgCode");
        Collection<AccessoryPack> accPacks = ModuleDataService.getInstance().getAccessoryPacksForModule(mgCode);
        if (accPacks != null && !accPacks.isEmpty())
        {
            JsonArray accPacksList = new JsonArray();
            for (AccessoryPack accPack : accPacks)
            {
                accPacksList.add(accPack);
            }
            sendJsonResponse(routingContext, accPacksList.encode());
        }
        else
        {
            sendJsonResponse(routingContext, "[]");
        }
    }


    private void getPrice(RoutingContext routingContext)
    {
        JsonObject moduleJson = routingContext.getBodyAsJson();
        LOG.debug("Module Json : " + moduleJson.encodePrettily());
        ProductModule module = new ProductModule(moduleJson);
        Integer id = LocalCache.getInstance().store(module);
        VertxInstance.get().eventBus().send(ModulePricingService.CALCULATE_PRICE, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    JsonObject result = (JsonObject) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.debug("result :" + result.encodePrettily());
                    sendJsonResponse(routingContext, result.toString());
                });
    }

    private void getPriceRecorder(RoutingContext routingContext)
    {
        JsonObject moduleJson = routingContext.getBodyAsJson();
        LOG.debug("Module Json : " + moduleJson.encodePrettily());
        ProductModule module = new ProductModule(moduleJson);
        Integer id = LocalCache.getInstance().store(module);
        VertxInstance.get().eventBus().send(ModulePricingService.CALCULATE_PRICE, id);
    }

}
