package com.mygubbi.search;

import com.mygubbi.ServerVerticle;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;

public class EsIndexer
{
    private final static Logger LOG = LogManager.getLogger(EsIndexer.class);
    private static final String PRODUCT_INDEX_TYPE = "product";

    public static void main(String[] args)
    {
        final EsIndexer esIndexer = new EsIndexer();
        esIndexer.startServices();
    }

    private void startServices()
    {

        VertxInstance.get().deployVerticle(new ServerVerticle("config/conf.es.json"), new DeploymentOptions().setWorker(true), result ->
        {
            if (result.succeeded())
            {
                LOG.info("Server started.");
                this.createIndexForProducts();
            }
            else
            {
                LOG.error("Server did not start. Message:" + result.result(), result.cause());
                System.exit(1);
            }
        });
/*        VertxInstance.get().setTimer(5000, new Handler<Long>()
        {
            @Override
            public void handle(Long event)
            {
                String searchQueryJson = ConfigHolder.getInstance().config().getJsonObject("searchQueryJson").toString().replaceAll("__TERM", "Albatross");
                Integer id = LocalCache.getInstance().store(new SearchQueryData(SearchService.INDEX_NAME, new JsonObject(searchQueryJson), PRODUCT_INDEX_TYPE));
                VertxInstance.get().eventBus().send(SearchService.SEARCH, id,
                        (AsyncResult<Message<Integer>> selectResult) -> {
                            SearchQueryData selectData = (SearchQueryData) LocalCache.getInstance().remove(selectResult.result().body());
                            System.out.println(selectData.getResult());
                        });

            }
        });
*/    }

    private void createIndexForProducts()
    {
        VertxInstance.get().eventBus().send(SearchService.PREPARE, 0,
                (AsyncResult<Message<Boolean>> result) -> {
                    if (result.result().body())
                    {
                        LOG.info("Index created");
                        this.indexProducts();
                    }
                    else
                    {
                        LOG.error("Index not created", result.cause());
                        System.exit(1);
                    }
                });

    }

    private void indexProducts()
    {
        Integer id = LocalCache.getInstance().store(new QueryData("product.select.all", new JsonObject()));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> result) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(result.result().body());
                    if (selectData == null || selectData.rows == null)
                    {
                        LOG.error("Could not fetch products from DB. Message:" + result.result(), result.cause());
                        System.exit(1);
                    }
                    else
                    {
                        for (Object jsonData : selectData.getJsonDataRows("productJson"))
                        {
                            //LOG.info("Doc :" + jsonData.toString());
                            this.indexProduct((JsonObject) jsonData);
                        }
                    }
                });

    }

    private void indexProduct(JsonObject productJson)
    {
        IndexData indexData = new IndexData(productJson.getString("id"), SearchService.INDEX_NAME, PRODUCT_INDEX_TYPE, productJson);
        Integer id = LocalCache.getInstance().store(indexData);
        VertxInstance.get().eventBus().send(SearchService.INDEX, id,
                (AsyncResult<Message<Integer>> result) -> {
                    IndexResponse response = (IndexResponse) LocalCache.getInstance().remove(result.result().body());
                    if (response == null || !response.isCreated())
                    {
                        LOG.error("Could not index product:" + indexData.getId(), result.cause());
                    }
                    else
                    {
                        LOG.info("Product indexed:" + indexData.getId());
                    }
                });

    }

}
