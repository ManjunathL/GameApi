package com.mygubbi.game.proposal;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.route.AbstractRouteHandler;
import com.mygubbi.si.excel.ExcelReaderService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sunil on 25-04-2016.
 */
public class ProposalProductHandler extends AbstractRouteHandler
{
    private final static Logger LOG = LogManager.getLogger(ProposalProductHandler.class);

    private String proposalDocsFolder = null;

    public ProposalProductHandler(Vertx vertx)
    {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/mapandupdate").handler(this::mapAndUpdate);
        this.post("/update").handler(this::updateProduct);
        this.post("/delete").handler(this::deleteProduct);
        this.proposalDocsFolder = ConfigHolder.getInstance().getStringValue("proposal_docs_folder", "/tmp/");
    }

    private void mapAndUpdate(RoutingContext routingContext)
    {
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
        AtomicInteger doneCounter = new AtomicInteger(0);
        for (int i = 0; i < size; i++)
        {
            ProductModule module = modules.get(i);
            productLineItem.addModule(module);
            Integer id = LocalCache.getInstance().store(module);
            vertx.eventBus().send(ProductModuleMapperService.MAP_TO_MG, id,
                    (AsyncResult<Message<Integer>> selectResult) -> {
                        int counter = doneCounter.incrementAndGet();
                        if (counter == size)
                        {
                            this.updateProductLineItem(routingContext, productLineItem);
                        }
                    });
        }
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
                        sendError(routingContext, "Error in updating product line item in the proposal.");
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
