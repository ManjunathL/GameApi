package com.mygubbi.catalog;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Mehbub on 13-10-2016.
 */
public class SeoManagementService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(SeoManagementService.class);

    public static final String CREATE_SEO = "create.seo";

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.setupSeoProcessor();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }

    private void setupSeoProcessor()
    {
        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(CREATE_SEO, (Message<Integer> message) -> {
            SeoJson seoJson = (SeoJson) LocalCache.getInstance().remove(message.body());

            this.addOrUpdateSeo(seoJson, message);
            //this.updateProductInFirebase(productJson);

        }).completionHandler(res -> {
            LOG.info("SeoManagementService started." + res.succeeded());
        });
    }

    /*  private void updateBlogInFirebase(BlogJson blog)
      {
          Integer id = LocalCache.getInstance().store(blog);
          VertxInstance.get().eventBus().send(FirebaseProductUpdateService.UPDATE_PRODUCT_IN_FB, id,
                  result -> {
                      if (result.failed())
                      {
                          LOG.error("Blog is not updated in firebase. " + blog);
                      }
                  });
      }
  */
    private void addOrUpdateSeo(SeoJson seoJson, Message message)
    {
        JsonObject seo = new JsonObject().put("seoId", seoJson.getSeoId())
                .put("id", seoJson.getSeoId())
                .put("title", seoJson.getTitle())
                .put("description", seoJson.getDescription())
                .put("metaKeywords", seoJson.getMetaKeywords().toString())
                .put("location", seoJson.getLocation())
                .put("category", seoJson.getCategory())
                .put("subCategory", seoJson.getSubCategory())
                .put("seoJson", seoJson.toString())
                .put("content", seoJson.getContent());
        Integer id = LocalCache.getInstance().store(new QueryData("seo.select.seoid", seo));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        this.upsertSeo(seo, message, true);
                    }
                    else
                    {
                        this.upsertSeo(seo, message, false);
                    }
                });
    }

    private void upsertSeo(JsonObject seo, Message message, boolean insert)
    {
        String query = insert ? "seo.insert" : "seo.update";
        Integer id = LocalCache.getInstance().store(new QueryData(query, seo));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> res) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(res.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        message.fail(0, "seo not updated to database.");
                    }
                    else
                    {
                        message.reply("seo updated to database");
                    }
                });
    }}
