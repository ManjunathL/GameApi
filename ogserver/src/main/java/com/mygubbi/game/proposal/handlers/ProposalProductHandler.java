package com.mygubbi.game.proposal.handlers;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ModuleTextFileReader;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.ProductModule;
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
        this.post("/loadandupdate").handler(this::loadAndUpdate);
        this.post("/update").handler(this::updateProduct);
        this.post("/delete").handler(this::deleteProduct);
        this.post("/createnew").handler(this::copyProductsFromVersion);
        this.post("/createnewfromoldproposal").handler(this::copyProductsFromOldProposalId);
        this.post("/createnewbeforeproductionspecification").handler(this::copyProductsFromOldQuotation);
        this.post("/updatesequence").handler(this::updateProductSequence);
        this.post("/insertproductlibray").handler(this::CreateProposalProductLibrary);
        this.post("/updateproductlibray").handler(this::UpdateProposalProductLibrary);
        this.post("/addtoproposalproduct").handler(this::AddToProposalProductFromLibrary);
    }

    private void loadAndUpdate(RoutingContext routingContext)
    {
        JsonObject productJson = routingContext.getBodyAsJson();
        ProductLineItem productLineItem = new ProductLineItem(productJson);
        this.loadModules(routingContext, productLineItem);
    }

    private void loadModules(RoutingContext routingContext, ProductLineItem productLineItem)
    {
        productLineItem.resetModules();
        List<ProductModule> modules = new ModuleTextFileReader(productLineItem.getKdMaxFile()).loadModules();
        if (modules == null || modules.isEmpty())
        {
            this.updateProductLineItem(routingContext, productLineItem);
            return;
        }

        int size = modules.size();
        for (int i = 0; i < size; i++)
        {
            ProductModule module = modules.get(i);
            ModuleDataService.getInstance().setMapping(module,productLineItem);
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

    private void updateProductSequence(RoutingContext routingContext)
    {
        JsonObject productJson = routingContext.getBodyAsJson();
        LOG.debug("Product Json :" + productJson.encodePrettily());
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.product.updatesequence", productJson));
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

    private void copyProductsFromVersion(RoutingContext routingContext)
    {
        JsonObject productJson = routingContext.getBodyAsJson();
        LOG.debug("Get body as Json :" + productJson.encodePrettily());
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.product.createnew", productJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());

                    LOG.debug("resultData.updateResult.getUpdated() 1:" + resultData.updateResult.getUpdated());

                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in copying product line item in the proposal.");
                        LOG.error("Error in copying product line item in the proposal. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        sendJsonResponse(routingContext, productJson.toString());
                    }
                });
    }


    private void CreateProposalProductLibrary(RoutingContext routingContext)
    {
        //LOG.info("Product library item" +productLineItem);
        JsonObject productJson = routingContext.getBodyAsJson();
        LOG.debug("Get body as Json :" + productJson.encodePrettily());
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.product.insertproductlibray", productJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());

                    LOG.debug("resultData.updateResult.getUpdated() 1:" + resultData.updateResult.getUpdated());

                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in copying product line item in the proposal.");
                        LOG.error("Error in copying product line item in the proposal. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        sendJsonResponse(routingContext, productJson.toString());
                    }
                });

        /*String query;
        if(productLineItem.getFromVersion()!="0" && productLineItem.getProposalId()!="0")
        {
            query= "proposal.product.updateproductlibray";
        }else
        {
            query="proposal.product.insertproductlibray";
        }
        //String query = productLineItem.getId() == 0 ? "proposal.product.insertproductlibray" : "proposal.product.updateproductlibray";
        LOG.info("id " +query);
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
                });*/
    }

    private void UpdateProposalProductLibrary(RoutingContext routingContext)
    {
        JsonObject productJson = routingContext.getBodyAsJson();
        LOG.debug("Get body as Json :" + productJson.encodePrettily());
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.product.updateproductlibray", productJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());

                    LOG.debug("resultData.updateResult.getUpdated() 1:" + resultData.updateResult.getUpdated());

                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in copying product line item in the proposal.");
                        LOG.error("Error in copying product line item in the proposal. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        sendJsonResponse(routingContext, productJson.toString());
                    }
                });
    }
    private void AddToProposalProductFromLibrary(RoutingContext routingContext)
    {
        JsonObject versionJson = routingContext.getBodyAsJson();
        LOG.debug("Get body as Json :" + versionJson.encodePrettily());
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.product.addtoproposalproduct", versionJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.debug("resultData.updateResult.getUpdated() 2:" + resultData.updateResult.getUpdated());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in copying product line item in the proposal.");
                        LOG.error("Error in copying product line item in the proposal. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        sendJsonResponse(routingContext, versionJson.toString());
                    }
                });
    }



    private void copyProductsFromOldProposalId(RoutingContext routingContext)
    {
        JsonObject versionJson = routingContext.getBodyAsJson();
        LOG.debug("Get body as Json :" + versionJson.encodePrettily());
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.product.createnewfromoldproposal", versionJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.debug("resultData.updateResult.getUpdated() 2:" + resultData.updateResult.getUpdated());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in copying product line item in the proposal.");
                        LOG.error("Error in copying product line item in the proposal. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        sendJsonResponse(routingContext, versionJson.toString());
                    }
                });
    }
    private void copyProductsFromOldQuotation(RoutingContext routingContext)
    {
        JsonObject versionJson = routingContext.getBodyAsJson();
        LOG.debug("Get body as Json :" + versionJson.encodePrettily());
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.product.createnewbeforeproductionspecification", versionJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.debug("resultData.updateResult.getUpdated() 2:" + resultData.updateResult.getUpdated());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in copying product line item in the proposal.");
                        LOG.error("Error in copying product line item in the proposal. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        sendJsonResponse(routingContext, versionJson.toString());
                    }
                });
    }


}
