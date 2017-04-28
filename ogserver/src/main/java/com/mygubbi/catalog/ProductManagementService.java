package com.mygubbi.catalog;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.si.firebase.FirebaseProductUpdateService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by test on 08-01-2016.
 */
public class ProductManagementService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(ProductManagementService.class);

    public static final String CREATE_PRODUCT = "create.product";

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.setupProductProcessor();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }

    private void setupProductProcessor()
    {
        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(CREATE_PRODUCT, (Message<Integer> message) -> {
            ProductJson productJson = (ProductJson) LocalCache.getInstance().remove(message.body());

            this.addOrUpdateProduct(productJson, message);
            //this.updateProductInFirebase(productJson);

        }).completionHandler(res -> {
            LOG.info("ProductManagementService started." + res.succeeded());
        });
    }

    private void updateProductInFirebase(ProductJson product)
    {
        Integer id = LocalCache.getInstance().store(product);
        VertxInstance.get().eventBus().send(FirebaseProductUpdateService.UPDATE_PRODUCT_IN_FB, id,
                result -> {
                    if (result.failed())
                    {
                        LOG.error("Product is not updated in firebase. " + product);
                    }
                });
    }

    private void addOrUpdateProduct(ProductJson productJson, Message message)
    {
        JsonObject product = new JsonObject().put("productId", productJson.getProductId()).put("id", productJson.getProductId())
                .put("name", productJson.getName())
                .put("description", productJson.getDescription())
                .put("category", productJson.getCategory())
                .put("subcategory", productJson.getSubCategory())
                .put("productJson", productJson.toString())
                .put("styleId", productJson.getStyleId())
                .put("popularity", productJson.getPopularity())
                .put("styleSortSeq", productJson.getStyleSortSeq())
                .put("relevance", productJson.getRelevance())
                .put("url", productJson.getUrl())
                .put("productShortJson", productJson.getShortJson().toString());

        Integer id = LocalCache.getInstance().store(new QueryData("product.select.productid", product));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        this.upsertProduct(product, message, true);
                    }
                    else
                    {
                        this.upsertProduct(product, message, false);
                    }
                });
    }

    private void upsertProduct(JsonObject product, Message message, boolean insert)
    {
        String query = insert ? "product.insert" : "product.update";
        Integer id = LocalCache.getInstance().store(new QueryData(query, product));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> res) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(res.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        message.fail(0, "Product not updated to database.");
                    }
                    else
                    {
                        message.reply("Product updated to database");
                    }
                });
    }
}
