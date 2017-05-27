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
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ProductSpaceHandler extends AbstractRouteHandler {

    private final static Logger LOG = LogManager.getLogger(ProductSpaceHandler.class);
    private static final String PRODUCT_TYPE = "items";
    private static final String USER_TYPE = "users";

    public ProductSpaceHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/addlibprod").handler(this::addLibProductToSpace);
        this.post("/addcatprod").handler(this::addCatProductToSpace);
        this.post("/addaddon").handler(this::addAddonToSpace);
        this.post("/deleteprod").handler(this::deleteProduct);
        this.post("/deleteaddon").handler(this::deleteAddon);
        this.post("/deletespace").handler(this::deleteSpace);
        this.post("/getspacedata").handler(this::getSpaceProducts);
        this.post("/search").handler(this::search);
        this.post("/usearch").handler(this::userSearch);
        this.post("/csearch").handler(this::customSearch);
        this.post("/addspace").handler(this::addSpace);
        this.post("/renamespace").handler(this::renameSpace);
    }

    private void addSpace(RoutingContext context)
    {
        JsonObject inputJson = context.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QueryData("add.spacemaster", inputJson));
        vertx.eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) ->
        {
            QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
            if (selectData.errorFlag)
            {
                LOG.error("Error in creating space");
                sendError(context, "Error in creating space");
            }
            else
            {
                LOG.info("Successfully created a space");
                int rowId = selectData.paramsObject.getInteger("id");
                JsonObject result = new JsonObject();
                result.put("id", rowId);
                sendJsonResponse(context, result.toString());
            }
        });
    }

    private void renameSpace(RoutingContext context)
    {
        JsonObject inputJson = context.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QueryData("rename.spacemaster", inputJson));
        vertx.eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) ->
                {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData.errorFlag)
                    {
                        LOG.error("Error in updating spacename");
                        sendError(context, "Error in updating spacename");
                    }
                    else
                    {
                        LOG.info("Successfully updated spacemaster");
                        updateproductSpace(context);
                    }
                });
    }

    private void updateproductSpace(RoutingContext context)
    {
        JsonObject inputJson = context.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QueryData("updatespace.proposal.product", inputJson));
        vertx.eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) ->
                {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData.errorFlag)
                    {
                        LOG.error("Error in updating spacename");
                        sendError(context, "Error in updating spacename");
                    }
                    else
                    {
                        LOG.info("Successfully updated product space");
                        updateAddonSpace(context);
                    }
                });
    }

    private void updateAddonSpace(RoutingContext context)
    {
        JsonObject inputJson = context.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QueryData("updatespace.proposal.addon", inputJson));
        vertx.eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) ->
                {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData.errorFlag)
                    {
                        LOG.error("Error in updating spacename");
                        sendError(context, "Error in updating spacename");
                    }
                    else
                    {
                        LOG.info("Successfully updated addon space");
                        sendSuccess(context, "Successfully updated space name");
                    }
                });
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
                int rowId = selectData.paramsObject.getInteger("id");
                JsonObject result = new JsonObject();
                result.put("id", rowId);
                sendJsonResponse(context, result.toString());
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
                        int rowId = selectData.paramsObject.getInteger("id");
                        JsonObject result = new JsonObject();
                        result.put("id", rowId);
                        sendJsonResponse(context, result.toString());
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
                        int rowId = selectData.paramsObject.getInteger("id");
                        JsonObject result = new JsonObject();
                        result.put("id", rowId);
                        sendJsonResponse(context, result.toString());
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
                sendSuccess(context, "Successfully deleted product from space");
            }
        });
    }


    private void deleteSpace(RoutingContext context)
    {
        JsonObject inputJson = context.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QueryData("delete.spacemaster", inputJson));
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
                        LOG.info("Successfully deleted space");
                        deleteSpaceProduct(context);
                    }
                });
    }

    private void deleteSpaceProduct(RoutingContext context)
    {
        JsonObject inputJson = context.getBodyAsJson();
        Integer id = LocalCache.getInstance().store(new QueryData("space.delete.products", inputJson));
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
                deleteSpaceAddon(context, inputJson, "space.delete.addons");
            }
        });
    }

    private void deleteAddon(RoutingContext context)
    {
        this.deleteSpaceAddon(context, context.getBodyAsJson(), "space.delete.addon");
    }

    private void deleteSpaceAddon(RoutingContext context, JsonObject inputJson, String queryId)
    {
        Integer id = LocalCache.getInstance().store(new QueryData(queryId, inputJson));
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
                        sendSuccess(context, "Successfully deleted");
                    }
                });
    }


    private void getSpaceProducts(RoutingContext context)
    {
        JsonObject inputJson = context.getBodyAsJson();
        LOG.info(inputJson.encodePrettily());
        Integer id = LocalCache.getInstance().store(new QueryData("spaces.data", inputJson));
        vertx.eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) ->
        {
            QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
            if (selectData.errorFlag)
            {
                LOG.error("Error in getting spaces data" + selectData.errorMessage);
                sendError(context, "Error in getting spaces data");
            }
            else
            {
                LOG.info("Successfully fetched space data");
                sendJsonResponse(context, selectData.rows.toString());
            }
        });
    }

    private void search(RoutingContext context) {
        JsonObject inputJson = context.getBodyAsJson();
        query(context, "searchQueryJson", inputJson);
    }

    private void query(RoutingContext context, String queryName, JsonObject inputJson)
    {
        LOG.info(inputJson.toString());
        String inputTerm = inputJson.getString("term");
        JsonObject facetJson = inputJson.getJsonObject("facets");
        JsonObject filterJson = inputJson.getJsonObject("filter");

        LOG.info(facetJson);
        LOG.info(filterJson);

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

    private void userSearch(RoutingContext context)
    {
        JsonObject inputJson = context.getBodyAsJson();
        LOG.info(inputJson.toString());

        String inputTerm = inputJson.getString("term");
        JsonObject facetJson = inputJson.getJsonObject("facets");
        JsonObject filterJson = inputJson.getJsonObject("filter");

        LOG.info(facetJson);
        LOG.info(filterJson);

        String queryName = "searchQueryJson";
        JsonObject jsonObject = (JsonObject) ConfigHolder.getInstance().getConfigValue(queryName);
        jsonObject.put("facets", facetJson);
        jsonObject.put("filter", filterJson);

        String searchQueryJson = jsonObject.toString().replaceAll("__TERM", inputTerm);
        LOG.info("queryJson:" + searchQueryJson);

        Integer id = LocalCache.getInstance().store(new SearchQueryData(LibSearchService.INDEX_NAME_U, new JsonObject(searchQueryJson), USER_TYPE, true));
        VertxInstance.get().eventBus().send(LibSearchService.SEARCH_TEXT, id,
                (AsyncResult<Message<Integer>> selectResult) ->
                {
                    SearchQueryData selectData = (SearchQueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    sendJsonResponse(context, selectData.getResult());
                });
    }


    private void customSearch(RoutingContext context)
    {
        JsonObject inputJson = context.getBodyAsJson();
        LOG.info(inputJson.toString());

        JsonObject queryJson = inputJson.getJsonObject("query");
        JsonObject facetJson = inputJson.getJsonObject("facets");
        JsonObject filterJson = inputJson.getJsonObject("filter");

        LOG.info(queryJson);
        LOG.info(facetJson);
        LOG.info(filterJson);

        String queryName = "customQueryJson";
        JsonObject jsonObject = (JsonObject) ConfigHolder.getInstance().getConfigValue(queryName);
        jsonObject.put("query", queryJson);
        jsonObject.put("facets", facetJson);
        jsonObject.put("filter", filterJson);

        String customQueryJson = jsonObject.toString();
        LOG.info("queryJson:" + customQueryJson);

        Integer id = LocalCache.getInstance().store(new SearchQueryData(LibSearchService.INDEX_NAME, jsonObject, PRODUCT_TYPE, true));
        VertxInstance.get().eventBus().send(LibSearchService.SEARCH_TEXT, id,
                (AsyncResult<Message<Integer>> selectResult) ->
                {
                    SearchQueryData selectData = (SearchQueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    sendJsonResponse(context, selectData.getResult());
                });
    }
}
