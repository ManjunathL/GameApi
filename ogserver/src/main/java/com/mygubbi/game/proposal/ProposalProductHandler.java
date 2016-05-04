package com.mygubbi.game.proposal;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.route.AbstractRouteHandler;
import com.mygubbi.si.excel.ExcelDataContainer;
import com.mygubbi.si.excel.ExcelReaderService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
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
        this.post("/mapmodules").handler(this::mapModules);
        this.post("/create").handler(this::createProduct);
        this.post("/update").handler(this::updateProduct);
        this.post("/delete").handler(this::deleteProduct);
        this.proposalDocsFolder = ConfigHolder.getInstance().getStringValue("proposal_docs_folder", "/tmp/");
    }

    private void mapModules(RoutingContext routingContext)
    {
        JsonObject paramsObject = routingContext.getBodyAsJson();
        String file = paramsObject.getString("filename");
        List<KDMaxModule> modules = new ExcelReaderService(file).mapKDMaxToMGModules();
        if (modules == null || modules.isEmpty()) sendJsonResponse(routingContext, "[]");

        JsonArray mappedModules = new JsonArray();

        int size = modules.size();
        AtomicInteger doneCounter = new AtomicInteger(0);
        for (int i = 0; i < size; i++)
        {
            ProductModuleMap kdmModule = new ProductModuleMap().setKdmModule(modules.get(i));
            Integer id = LocalCache.getInstance().store(kdmModule);
            vertx.eventBus().send(KDMaxMapperService.MAP_TO_MG, id,
                    (AsyncResult<Message<Integer>> selectResult) -> {
                        ProductModuleMap kdmModuleResult = (ProductModuleMap) LocalCache.getInstance().remove(selectResult.result().body());
                        mappedModules.add(kdmModuleResult);
                        int counter = doneCounter.incrementAndGet();
                        if (counter == size)
                        {
                            sendJsonResponse(routingContext, mappedModules.toString());
                        }
                    });
        }
    }

    private void createProduct(RoutingContext routingContext)
    {
        JsonObject productJson = routingContext.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.product.create", productJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in creating proposal product line item.");
                        LOG.error("Error in creating proposal product line item. " + resultData.errorMessage, resultData.error);
                    }
                });
    }

    private void updateProduct(RoutingContext routingContext)
    {
        JsonObject productJson = routingContext.getBodyAsJson();

        Integer id = LocalCache.getInstance().store(new QueryData("proposal.product.update", productJson));
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
                        sendJsonResponse(routingContext, productJson.toString());
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
