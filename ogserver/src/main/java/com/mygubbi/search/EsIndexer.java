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
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;

import java.util.concurrent.atomic.AtomicInteger;

public class EsIndexer
{
    private final static Logger LOG = LogManager.getLogger(EsIndexer.class);
    private static final String PRODUCT_INDEX_TYPE = "product";

    private volatile int recordsToIndex = 0;
    private volatile int recordsIndexed = 0;

    public static void main(String[] args)
    {
        if (args.length == 1)
        {
            new EsIndexer().startServices(args[0]); //Config files can be passed in as comma delimited set
        }
        else
        {
            new EsIndexer().startServices("config/es.json");
        }
    }

    private void startServices(String configFiles)
    {

        VertxInstance.get().deployVerticle(new ServerVerticle(configFiles), new DeploymentOptions().setWorker(true), result ->
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
    }

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
                        JsonArray jsonDataRows = selectData.getJsonDataRows("productJson");
                        if (jsonDataRows.isEmpty())
                        {
                            LOG.info("No records to index, bringing down server.");
                            System.exit(-1);
                        }

                        recordsToIndex = jsonDataRows.size();
                        for (Object jsonData : jsonDataRows)
                        {
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

                    recordsIndexed++;
                    if (recordsToIndex == recordsIndexed)
                    {
                        LOG.info("All records processed, bringing down server.");
                        System.exit(-1);
                    }
                });

    }

}
