package com.mygubbi.game.proposal.price;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.game.proposal.ProductModule;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Sunil on 08-01-2016.
 */

public class ComprehensiveModulePricingService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(ComprehensiveModulePricingService.class);
    public static final String CALCULATE_PRICE = "calculate.module.price.v2";

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.setupPriceCalculator();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }

    private void setupPriceCalculator()
    {
        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(CALCULATE_PRICE, (Message<Integer> message) -> {
            ProductModule productModule = (ProductModule) LocalCache.getInstance().remove(message.body());
            this.calculatePrice(productModule, message);
        }).completionHandler(res -> {
            LOG.info("Comprehensive Module price calculator service started." + res.succeeded());
        });
    }

    private void calculatePrice(ProductModule productModule, Message message)
    {
        ModulePriceHolder modulePriceHolder = new ModulePriceHolder(productModule);
        modulePriceHolder.prepare();
        if (modulePriceHolder.hasErrors())
        {
            this.sendResponse(message, modulePriceHolder, productModule);
            return;
        }

        modulePriceHolder.calculateTotalCost();
        this.sendResponse(message, modulePriceHolder, productModule);
    }

    private void sendResponse(Message message, ModulePriceHolder modulePriceHolder, ProductModule productModule)
    {
        JsonObject resultJson = null;
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
        message.reply(LocalCache.getInstance().store(resultJson));

    }

}
