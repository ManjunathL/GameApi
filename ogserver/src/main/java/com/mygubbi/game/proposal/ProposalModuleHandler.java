package com.mygubbi.game.proposal;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.StringUtils;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
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
        this.get("/mgmodules").handler(this::getMGModules);
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

    private void getMGModules(RoutingContext routingContext)
    {
        String extCode = routingContext.request().getParam("extCode");
        String extDefCode = routingContext.request().getParam("extDefCode");
        LOG.info("Ext code:" + extCode + " and Def code: " + extDefCode);
        if (StringUtils.isEmpty(extCode))
        {
            sendError(routingContext, "External module code is not set for this module " + extCode);
            return;
        }

        String moduleCode = StringUtils.isNonEmpty(extDefCode) ? extDefCode : extCode;
        Integer id = LocalCache.getInstance().store(new QueryData("kdmax.mg.select", new JsonObject().put("kdmcode", moduleCode)));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        sendError(routingContext, "No modules mapped for external module " + moduleCode);
                    }
                    else
                    {
                        sendJsonResponse(routingContext, selectData.rows.toString());
                    }
                });
    }
}
