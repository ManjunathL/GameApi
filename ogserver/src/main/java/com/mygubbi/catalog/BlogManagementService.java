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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Mehbub on 18-08-2016.
 */
public class BlogManagementService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(BlogManagementService.class);

    public static final String CREATE_BLOG = "create.blog";

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.setupBlogProcessor();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }

    private void setupBlogProcessor()
    {
        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(CREATE_BLOG, (Message<Integer> message) -> {
            BlogJson blogJson = (BlogJson) LocalCache.getInstance().remove(message.body());

            this.addOrUpdateBlog(blogJson, message);
            //this.updateProductInFirebase(productJson);

        }).completionHandler(res -> {
            LOG.info("BlogManagementService started." + res.succeeded());
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
    private void addOrUpdateBlog(BlogJson blogJson, Message message)
    {
        JsonObject blog = new JsonObject().put("blogId", blogJson.getBlogId())
                .put("id", blogJson.getBlogId())
                .put("tags", blogJson.getTags())
                .put("blog_categories", blogJson.getBlog_categories().toString())
                .put("blogJson", blogJson.toString());
        Integer id = LocalCache.getInstance().store(new QueryData("blog.select.blogid", blog));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        this.upsertBlog(blog, message, true);
                    }
                    else
                    {
                        this.upsertBlog(blog, message, false);
                    }
                });
    }

    private void upsertBlog(JsonObject blog, Message message, boolean insert)
    {
        String query = insert ? "blog.insert" : "blog.update";
        Integer id = LocalCache.getInstance().store(new QueryData(query, blog));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> res) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(res.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        message.fail(0, "blog not updated to database.");
                    }
                    else
                    {
                        message.reply("blog updated to database");
                    }
                });
    }
}
