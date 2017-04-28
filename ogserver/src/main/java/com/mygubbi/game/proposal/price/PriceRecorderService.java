package com.mygubbi.game.proposal.price;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by Chirag on 19-08-2016.
 */
public class PriceRecorderService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(PriceRecorderService.class);
    public static final String RECORD_PRICE_CALCULATION = "record.price.calculation";

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.setupPriceRecorder();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }

    private void setupPriceRecorder()
    {
        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(RECORD_PRICE_CALCULATION, (Message<Integer> message) -> {
            ModulePriceHolder priceHolder = (ModulePriceHolder) LocalCache.getInstance().remove(message.body());
            try
            {
                new PricingCalculationExcelCreator(priceHolder).create();
            }
            catch (Exception e)
            {
                LOG.error("Price recorder error for module :" + priceHolder.getName(), e);
            }
        }).completionHandler(res -> {
            LOG.info("Module price recorder service started." + res.succeeded());
        });
    }

}
