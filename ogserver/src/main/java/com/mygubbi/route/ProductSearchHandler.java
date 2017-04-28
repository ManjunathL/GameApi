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
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ProductSearchHandler extends AbstractRouteHandler {

    private final static Logger LOG = LogManager.getLogger(ProductSearchHandler.class);
    private static final String PRODUCT_TYPE = "product";

    public ProductSearchHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/suggest").handler(this::suggest);
        this.get("/search").handler(this::search);
    }

    private void suggest(RoutingContext context) {
        String inputTerm = context.request().getParam("term");
        query(context, "suggestQueryJson", inputTerm);
    }

    private void search(RoutingContext context) {
        String inputTerm = context.request().getParam("term");
        query(context, "searchQueryJson", inputTerm);
    }

    private void query(RoutingContext context, String queryName, String inputTerm) {
        JsonObject jsonObject = (JsonObject) ConfigHolder.getInstance().getConfigValue(queryName);
        String searchQueryJson = jsonObject.toString().replaceAll("__TERM", inputTerm);
        LOG.info("queryJson:" + searchQueryJson);
        Integer id = LocalCache.getInstance().store(new SearchQueryData(SearchService.INDEX_NAME, new JsonObject(searchQueryJson), PRODUCT_TYPE));
        VertxInstance.get().eventBus().send(SearchService.SEARCH, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    SearchQueryData selectData = (SearchQueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    sendJsonResponse(context, selectData.getResult());
                });
    }

}
