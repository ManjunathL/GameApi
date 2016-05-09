package com.mygubbi.game.proposal;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by Sunil on 08-01-2016.
 */

public class ModulePricingService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(ModulePricingService.class);

    public static final String CALCULATE_PRICE = "calculate.module.price";

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
            LOG.info("Module price calculator service started." + res.succeeded());
        });
    }

    private void calculatePrice(ProductModule productModule, Message message)
    {
        Integer id = LocalCache.getInstance().store(new QueryData("kdmax.mg.map", productModule));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        this.findMgModulesForDefaultModule(productModule, message);
                    }
                    else
                    {
                        this.addMgModules(productModule, message, selectData.rows, ProductLineItem.MAPPED_AT_MODULE);
                    }
                });
    }

    private void findMgModulesForDefaultModule(ProductModule productModule, Message message)
    {
        Integer id = LocalCache.getInstance().store(new QueryData("kdmax.default", productModule));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        this.sendResponseAsNotMapped(productModule, message);
                    }
                    else
                    {
                        this.mapMgModulesForDefaultModule(productModule, message, selectData.rows.get(0).getString("kdmdefcode"));
                    }
                });
    }

    private void sendResponseAsNotMapped(ProductModule productModule, Message message)
    {
        productModule.setMappedFlag(ProductLineItem.NOT_MAPPED);
        message.reply(LocalCache.getInstance().store(productModule));
    }

    private void mapMgModulesForDefaultModule(ProductModule productModule, Message message, String defaultModule)
    {
        productModule.setDefaultModule(defaultModule);
        Integer id = LocalCache.getInstance().store(new QueryData("kdmax.mg.def.map", productModule));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        this.sendResponseAsNotMapped(productModule, message);
                    }
                    else
                    {
                        this.addMgModules(productModule, message, selectData.rows, ProductLineItem.MAPPED_AT_DEFAULT);
                    }
                });
    }

    private void addMgModules(ProductModule productModule, Message message, List<JsonObject> mgModules, String mappedFlag)
    {
        for (JsonObject mgModule : mgModules)
        {
            productModule.addMappedModule(mgModule);
        }
        productModule.setMappedFlag(mappedFlag);
        message.reply(LocalCache.getInstance().store(productModule));
    }
}
