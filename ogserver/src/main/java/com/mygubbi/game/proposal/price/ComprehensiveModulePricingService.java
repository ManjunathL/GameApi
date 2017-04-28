package com.mygubbi.game.proposal.price;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.game.proposal.ModuleForPrice;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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
            ModuleForPrice moduleForPrice = (ModuleForPrice) LocalCache.getInstance().remove(message.body());
            this.calculatePrice(moduleForPrice, message);
        }).completionHandler(res -> {
            LOG.info("Comprehensive Module price calculator service started." + res.succeeded());
        });
    }

    private void calculatePrice(ModuleForPrice moduleForPrice, Message message)
    {
        ModulePriceHolder modulePriceHolder = null;
        try
        {
            modulePriceHolder = new ModulePriceHolder(moduleForPrice);
            modulePriceHolder.prepare();
            if (modulePriceHolder.hasErrors())
            {
                message.reply(LocalCache.getInstance().store(modulePriceHolder));
                return;
            }

            modulePriceHolder.calculateTotalCost();
        }
        catch (Exception e)
        {
            if (modulePriceHolder != null) modulePriceHolder.addError("Error in calculating price:" + e.getMessage());
            LOG.error("Error in pricing", e);
        }
        message.reply(LocalCache.getInstance().store(modulePriceHolder));
    }
}
