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
 * Created by Mehbub on 01-12-2016.
 */
public class DIYManagementService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(DIYManagementService.class);

    public static final String CREATE_DIY = "create.diy";

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.setupDiyProcessor();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }

    private void setupDiyProcessor()
    {
        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(CREATE_DIY, (Message<Integer> message) -> {
            DIYJson diyJson = (DIYJson) LocalCache.getInstance().remove(message.body());

            this.addOrUpdateDiy(diyJson, message);
            //this.updateProductInFirebase(productJson);

        }).completionHandler(res -> {
            LOG.info("DIYManagementService started." + res.succeeded());
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
    private void addOrUpdateDiy(DIYJson diyJson, Message message)
    {
        JsonObject diyData = new JsonObject().put("diyId", diyJson.getDiyId())
                .put("id", diyJson.getDiyId())
                .put("tags", diyJson.getTags())
                .put("diy_categories", diyJson.getDiy_categories().toString())
                .put("diyJson", diyJson.toString());
        Integer id = LocalCache.getInstance().store(new QueryData("diy.select.diyid", diyData));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        this.upsertDiy(diyData, message, true);
                    }
                    else
                    {
                        this.upsertDiy(diyData, message, false);
                    }
                });
    }

    private void upsertDiy(JsonObject diyData, Message message, boolean insert)
    {
        String query = insert ? "diy.insert" : "diy.update";
        Integer id = LocalCache.getInstance().store(new QueryData(query, diyData));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> res) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(res.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        message.fail(0, "diy not updated to database.");
                    }
                    else
                    {
                        message.reply("diy updated to database");
                    }
                });
    }
}
