package com.mygubbi.game.proposal;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.game.proposal.model.AccessoryPack;
import com.mygubbi.game.proposal.price.ComprehensiveModulePricingService;
import com.mygubbi.game.proposal.price.ModulePriceHolder;
import com.mygubbi.game.proposal.price.PriceRecorderService;
import com.mygubbi.route.AbstractRouteHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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
        this.post("/pricev2").handler(this::getPriceV2);
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

    private void getPriceV2(RoutingContext routingContext)
    {
        this.getPrice(routingContext, ComprehensiveModulePricingService.CALCULATE_PRICE);
    }

    private void getPrice(RoutingContext routingContext, String priceModule)
    {
        LOG.debug("Price module : " + priceModule);
        JsonObject moduleJson = routingContext.getBodyAsJson();
        LOG.debug("Module Json : " + moduleJson.encodePrettily());
        ModuleForPrice moduleForPrice = new ModuleForPrice(moduleJson);
        Integer id = LocalCache.getInstance().store(moduleForPrice);
        VertxInstance.get().eventBus().send(priceModule, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    ModulePriceHolder modulePriceHolder = (ModulePriceHolder) LocalCache.getInstance().remove(selectResult.result().body());
                    sendResponse(routingContext, modulePriceHolder);
                    recordPriceCalculation(modulePriceHolder);
                });
    }

    private void sendResponse(RoutingContext routingContext, ModulePriceHolder modulePriceHolder)
    {
        JsonObject resultJson = null;
        ProductModule productModule = modulePriceHolder.getProductModule();
        if (modulePriceHolder.hasErrors())
        {
            resultJson = new JsonObject().put("errors", modulePriceHolder.getErrors()).put("mgCode", productModule.getMGCode());
            LOG.info("Pricing for product module has errors: " + productModule.encodePrettily() + " ::: " + resultJson.encodePrettily());
        }
        else
        {
            resultJson = modulePriceHolder.getPriceJson();
            JsonObject pm = new JsonObject().put("mg", productModule.getMGCode()).put("carcass", productModule.getCarcassCode())
                    .put("finish", productModule.getFinishCode());
            LOG.info("Sending price calculation result :" + pm.encodePrettily() + " :: " + resultJson.encodePrettily());
        }
        sendJsonResponse(routingContext, resultJson.toString());
    }

    private void recordPriceCalculation(ModulePriceHolder modulePriceHolder)
    {
        Integer id = LocalCache.getInstance().store(modulePriceHolder);
        VertxInstance.get().eventBus().send(PriceRecorderService.RECORD_PRICE_CALCULATION, id);
    }

}
