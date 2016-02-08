package com.mygubbi.route;


import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.search.SearchQueryData;
import com.mygubbi.search.SearchService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class ProductSearchHandler extends AbstractRouteHandler {

    private static final String PRODUCT_TYPE = "product";

    public ProductSearchHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/suggest").handler(this::suggest);
        this.post("/search").handler(this::search);
    }

    private void suggest(RoutingContext context) {
        JsonObject requestJson = context.getBodyAsJson();
        String inputTerm = requestJson.getString("term");
        query(context, "suggestQueryJson", inputTerm);
    }

    private void search(RoutingContext context) {
        JsonObject requestJson = context.getBodyAsJson();
        String inputTerm = requestJson.getString("term");
        query(context, "searchQueryJson", inputTerm);
    }

    private void query(RoutingContext context, String queryName, String inputTerm) {
        String searchQueryJson = ConfigHolder.getInstance().config().getJsonObject(queryName).toString().replaceAll("__TERM", inputTerm);
        Integer id = LocalCache.getInstance().store(new SearchQueryData(SearchService.INDEX_NAME, new JsonObject(searchQueryJson), PRODUCT_TYPE));
        VertxInstance.get().eventBus().send(SearchService.SEARCH, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    SearchQueryData selectData = (SearchQueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    sendJsonResponse(context, selectData.getResult());
                });
    }

}
