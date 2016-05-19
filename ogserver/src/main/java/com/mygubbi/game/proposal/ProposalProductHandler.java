package com.mygubbi.game.proposal;

import com.mygubbi.common.LocalCache;
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

import java.util.List;

/**
 * Created by sunil on 25-04-2016.
 */
public class ProposalProductHandler extends AbstractRouteHandler
{
    private final static Logger LOG = LogManager.getLogger(ProposalProductHandler.class);

    public ProposalProductHandler(Vertx vertx)
    {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/mapandupdate").handler(this::mapAndUpdate);
        this.post("/update").handler(this::updateProduct);
        this.post("/delete").handler(this::deleteProduct);
    }

    private void mapAndUpdate(RoutingContext routingContext)
    {
        LOG.info("Reading product json");
        JsonObject productJson = routingContext.getBodyAsJson();
        ProductLineItem productLineItem = new ProductLineItem(productJson);
        this.mapModules(routingContext, productLineItem);
    }

    private void mapModules(RoutingContext routingContext, ProductLineItem productLineItem)
    {
        productLineItem.resetModules();
        List<ProductModule> modules = new ModuleFileReader(productLineItem.getKdMaxFile()).loadModules();
        if (modules == null || modules.isEmpty())
        {
            this.updateProductLineItem(routingContext, productLineItem);
            return;
        }

        int size = modules.size();
        for (int i = 0; i < size; i++)
        {
            ProductModule module = modules.get(i);
            ModuleDataService.getInstance().setMapping(module);
            productLineItem.addModule(module);
        }
        this.updateProductLineItem(routingContext, productLineItem);
    }

    private void updateProduct(RoutingContext routingContext)
    {
        JsonObject productJson = routingContext.getBodyAsJson();
        ProductLineItem productLineItem = new ProductLineItem(productJson);
        this.updateProductLineItem(routingContext, productLineItem);
    }

    private void updateProductLineItem(RoutingContext routingContext, ProductLineItem productLineItem)
    {
        String query = productLineItem.getId() == 0 ? "proposal.product.create" : "proposal.product.update";
        Integer id = LocalCache.getInstance().store(new QueryData(query, productLineItem));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in updating product line item in the proposal. " + resultData.errorMessage);
                        LOG.error("Error in updating product line item in the proposal. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        sendJsonResponse(routingContext, productLineItem.toString());
                    }
                });
    }

    private void deleteProduct(RoutingContext routingContext)
    {
        JsonObject productJson = routingContext.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.product.delete", productJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in removing product line item in the proposal.");
                        LOG.error("Error in removing product line item in the proposal. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        sendJsonResponse(routingContext, productJson.toString());
                    }
                });
    }
}
