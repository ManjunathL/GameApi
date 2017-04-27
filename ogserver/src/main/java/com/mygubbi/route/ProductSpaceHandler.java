package com.mygubbi.route;


import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.search.LibSearchService;
import com.mygubbi.search.SearchQueryData;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductSpaceHandler extends AbstractRouteHandler {

    private final static Logger LOG = LogManager.getLogger(ProductSpaceHandler.class);
    private static final String PRODUCT_TYPE = "items";

    public ProductSpaceHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/addlibprod").handler(this::addLibProductToSpace);
        this.post("/addcatprod").handler(this::addCatProductToSpace);
        this.post("/addaddon").handler(this::addAddonToSpace);
        this.post("/deleteprod").handler(this::deleteProduct);
        this.post("/deletespace").handler(this::deleteSpace);
        this.get("/getspacedata").handler(this::getSpaceProducts);
        this.post("/search").handler(this::search);
    }

    private void addLibProductToSpace(RoutingContext context)
    {
        JsonObject inputJson = context.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QueryData("space.add.prodcutlib", inputJson));
        vertx.eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) ->
        {
            QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
            if (selectData.errorFlag || selectData.updateResult.getUpdated() == 0)
            {
                LOG.error("Error in adding product to space");
                sendError(context, "Error in adding product to space");
            }
            else
            {
                LOG.info("Successfully added product to space");
                sendJsonResponse(context, "Successfully added product to space");
            }
        });
    }

    private void addCatProductToSpace(RoutingContext context)
    {
        JsonObject inputJson = context.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QueryData("space.add.catalogue", inputJson));
        vertx.eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) ->
                {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData.errorFlag || selectData.updateResult.getUpdated() == 0)
                    {
                        LOG.error("Error in adding catalogue product to space");
                        sendError(context, "Error in adding catalogue product to space");
                    }
                    else
                    {
                        LOG.info("Successfully added catalogue product to space");
                        sendJsonResponse(context, "Successfully added catalogue product to space");
                    }
                });
    }

    private void addAddonToSpace(RoutingContext context)
    {
        JsonObject inputJson = context.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QueryData("space.add.addon", inputJson));
        vertx.eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) ->
                {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData.errorFlag || selectData.updateResult.getUpdated() == 0)
                    {
                        LOG.error("Error in adding addon to space");
                        sendError(context, "Error in adding addon to space");
                    }
                    else
                    {
                        LOG.info("Successfully added addon to space");
                        sendJsonResponse(context, "Successfully added addon to space");
                    }
                });
    }

    private void deleteProduct(RoutingContext context)
    {
        JsonObject inputJson = context.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QueryData("space.delete.product", inputJson));
        vertx.eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) ->
        {
            QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
            if (selectData.errorFlag || selectData.updateResult.getUpdated() == 0)
            {
                LOG.error("Error in deleting product to space");
                sendError(context, "Error in deleting product from space");
            }
            else
            {
                LOG.info("Successfully deleted product to space");
                sendJsonResponse(context, "Successfully deleted product from space");
            }
        });
    }

    private void deleteSpace(RoutingContext context)
    {
        JsonObject inputJson = context.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QueryData("space.delete.product", inputJson));
        vertx.eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) ->
        {
            QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
            if (selectData.errorFlag)
            {
                LOG.error("Error in deleting space");
                sendError(context, "Error in deleting space");
            }
            else
            {
                LOG.info("Successfully deleted space items from product");
                deleteSpaceAddon(context, inputJson);
            }
        });
    }

    private void deleteSpaceAddon(RoutingContext context, Jsonobject inputJson)
    {
        Integer id = LocalCache.getInstance().store(new QueryData("space.delete.addon", inputJson));
        vertx.eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) ->
                {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData.errorFlag)
                    {
                        LOG.error("Error in deleting space items from addon");
                        sendError(context, "Error in deleting space items from addon");
                    }
                    else
                    {
                        LOG.info("Successfully deleted space items from addon");
                        sendJsonResponse(context, "Successfully deleted space");
                    }
                });
    }

    private void getSpaceProducts(RoutingContext context)
    {
        String proposalIdStr = context.request().getParam("proposalId");
        int proposalId = Integer.parseInt(proposalIdStr);
        String version = context.request().getParam("fromVersion");

        JsonObject inputJson = new JsonObject();
        inputJson.put("proposalId", proposalId);
        inputJson.put("fromVersion", version);

        Integer id = LocalCache.getInstance().store(new QueryData("spaces.data", inputJson));
        vertx.eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) ->
        {
            QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
            if (selectData.errorFlag)
            {
                LOG.error("Error in getting spaces data");
                sendError(context, "Error in getting spaces data");
            }
            else
            {
                LOG.info("Successfully deleted space");
                sendJsonResponse(context, selectData.rows.toString());
            }
        });
    }

    private void search(RoutingContext context) {
        JsonObject inputJson = context.getBodyAsJson();
        query(context, "spaceSearchQueryJson", inputJson);
    }

    private void query(RoutingContext context, String queryName, JsonObject inputJson)
    {
        String inputTerm = inputJson.getString("term");
        JsonObject facetJson = inputJson.getJsonObject("facets");
        JsonObject filterJson = inputJson.getJsonObject("filter");

        JsonObject jsonObject = (JsonObject) ConfigHolder.getInstance().getConfigValue(queryName);
        jsonObject.put("facets", facetJson);
        jsonObject.put("filter", filterJson);
        String searchQueryJson = jsonObject.toString().replaceAll("__TERM", inputTerm);
        LOG.info("queryJson:" + searchQueryJson);

        Integer id = LocalCache.getInstance().store(new SearchQueryData(LibSearchService.INDEX_NAME, new JsonObject(searchQueryJson), PRODUCT_TYPE, true));
        VertxInstance.get().eventBus().send(LibSearchService.SEARCH, id,
                (AsyncResult<Message<Integer>> selectResult) ->
                {
                    SearchQueryData selectData = (SearchQueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    sendJsonResponse(context, selectData.getResult());
                });
    }
}
