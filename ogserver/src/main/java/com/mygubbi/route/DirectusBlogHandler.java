package com.mygubbi.route;

import com.google.gson.JsonArray;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.StringUtils;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;



/**
 * Created by Mehbub on 19-08-2016.
 */
public class DirectusBlogHandler extends AbstractRouteHandler
{
    private final static Logger LOG = LogManager.getLogger(DirectusBlogHandler.class);

    public DirectusBlogHandler(Vertx vertx)
    {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/insertBlog").handler(this::addBlogItem); //Category, Sub-Category
        this.post("/deleteBlog").handler(this::deleteBlogItem); //Category, Sub-Category
        this.post("/insertDiy").handler(this::addDiyItem); //Category, Sub-Category
        this.post("/deleteDiy").handler(this::deleteDiyItem); //Category, Sub-Category
    }


    private void addBlogItem(RoutingContext context) {

    LOG.info("i am in insert :)");
        LOG.info(context);
        JsonObject blogJson = context.getBodyAsJson();
        LOG.debug("JSON :" + blogJson.encodePrettily());
        JsonObject blog = new JsonObject().put("blogId", blogJson.getString("blogId"))
                .put("blog_heading", blogJson.getString("blog_heading"))
                .put("FullJson",blogJson)
                .put("blog_main_image", blogJson.getString("blog_main_image"))
                .put("blog_content", blogJson.getString("blog_content"))
                .put("tags", blogJson.getString("tags"))
                .put("author", blogJson.getString("author"))
                .put("date_of_publish", blogJson.getString("date"));

        String keyVal = blogJson.getString("tags");
        LOG.info(keyVal);
        String[] arr = keyVal.split(",");
        LOG.info(arr);
        ArrayList<String> list = new ArrayList<String>();
        for (int j =0; j< arr.length; j++) {

                list.add(arr[j]);

                LOG.info(arr[j]);

        }

        LOG.info(list);
        ArrayList<String> newList = new ArrayList<String>();

        for(int i = 0; i < list .size(); i++)
        {
            if(list.get(i).contains("living"))
            {
                newList.add(list.get(i).replace("living", "living room"));
                //someList.set(i, someList.get(i).replace(someString, otherString));
            }
            else if(list.get(i).contains("trends"))
            {
                newList.add(list.get(i).replace("trends", "trends & style"));
                //someList.set(i, someList.get(i).replace(someString, otherString));
            }
            else if(list.get(i).contains("outdoor"))
            {
                newList.add(list.get(i).replace("outdoor", "outdoors"));
                //someList.set(i, someList.get(i).replace(someString, otherString));
            }
            else if(list.get(i).contains("expert-tips"))
            {
                newList.add(list.get(i).replace("expert-tips", "expert tips"));
                //someList.set(i, someList.get(i).replace(someString, otherString));
            }
            else if(list.get(i).contains("storage-solutions"))
            {
                newList.add(list.get(i).replace("storage-solutions", "storage solutions"));
                //someList.set(i, someList.get(i).replace(someString, otherString));
            }
            else {

                newList.add(list.get(i));
            }

        }
        LOG.info("====newList====");
        LOG.info(newList);

        blog.put("blog_categories", newList);
        blog.put("blogJson",blog.encodePrettily());

        Integer id = LocalCache.getInstance().store(new QueryData("blog.select.blogid", blog));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        LOG.info("no blogs in results");
                        insertBlogs(context,blog);
                         }
                    else
                    {
                        updateBlogs(context,blog);
                    }
                });


    }
    private void insertBlogs(RoutingContext context, JsonObject blogJson) {

        Integer id = LocalCache.getInstance().store(new QueryData("blog.insert", blogJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.info("Check query (ms):" + selectData.responseTimeInMillis);

                        LOG.info("blogs inserted successfully");
                        sendJsonResponse(context,blogJson.encodePrettily());

                });
    }

    private void updateBlogs(RoutingContext context, JsonObject blogJson) {
        JsonObject blog = new JsonObject().put("blogId", blogJson.getString("blogId"))
                .put("tags", blogJson.getString("tags"))
                .put("blogJson", blogJson.toString());
        LOG.info("blogJson Mehabub");
        LOG.info(blogJson.encodePrettily());
        LOG.info("blogJson Mehabub updates");
        LOG.info(blogJson.encodePrettily());

        Integer id = LocalCache.getInstance().store(new QueryData("blog.update", blogJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.info("Check query (ms):" + selectData.responseTimeInMillis);

                    LOG.info("blogs updated successfully");
                    sendJsonResponse(context,blogJson.encodePrettily());

                });
    }

    private void deleteBlogItem(RoutingContext context) {

        LOG.info("i am in delete :)");
        LOG.info(context);
        JsonObject blogJson = context.getBodyAsJson();
        LOG.debug("JSON :" + blogJson.encodePrettily());
        JsonObject blog = new JsonObject().put("blogId", blogJson.getString("blogId"));


        blog.put("blogJson",blog.encodePrettily());

        Integer id = LocalCache.getInstance().store(new QueryData("blog.select.blogid", blog));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        sendJsonResponse(context,"no blogs found" + blogJson.encodePrettily() );
                    }
                    else
                    {
                        LOG.info("blog to be deleted");
                        deleteBlog(context,blog);
                    }
                });


    }
    private void deleteBlog(RoutingContext context, JsonObject blogJson) {

        Integer id = LocalCache.getInstance().store(new QueryData("blog.delete", blogJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.info("Check query (ms):" + selectData.responseTimeInMillis);

                    LOG.info("blog deleted successfully");
                    sendJsonResponse(context,"blogs deleted successfully" + blogJson.encodePrettily() );

                });
    }

//===================  DIY   =======================  //

    private void addDiyItem(RoutingContext context) {

        LOG.info("i am in diy insert :)");
        LOG.info(context);
        JsonObject diyJson = context.getBodyAsJson();
        LOG.debug("JSON :" + diyJson.encodePrettily());
        JsonObject diy = new JsonObject().put("diyId", diyJson.getString("diyId"))
                .put("diy_heading", diyJson.getString("diy_heading"))
                .put("FullJson",diyJson)
                .put("diy_main_image", diyJson.getString("diy_main_image"))
                .put("diy_content", diyJson.getString("diy_content"))
                .put("tags", diyJson.getString("tags"))
                .put("author", diyJson.getString("author"))
                .put("date_of_publish", diyJson.getString("date"));

        String keyVal = diyJson.getString("tags");
        LOG.info(keyVal);
        String[] arr = keyVal.split(",");
        LOG.info(arr);
        ArrayList<String> list = new ArrayList<String>();
        for (int j =0; j< arr.length; j++) {

            list.add(arr[j]);

            LOG.info(arr[j]);

        }

        LOG.info(list);


        diy.put("diy_categories", list);
        diy.put("diyJson",diy.encodePrettily());

        Integer id = LocalCache.getInstance().store(new QueryData("diy.select.diyid", diy));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        LOG.info("no diys in results");
                        insertDiys(context,diy);
                    }
                    else
                    {
                        updateDiys(context,diy);
                    }
                });


    }
    private void insertDiys(RoutingContext context, JsonObject diyJson) {

        Integer id = LocalCache.getInstance().store(new QueryData("diy.insert", diyJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.info("Check query (ms):" + selectData.responseTimeInMillis);

                    LOG.info("diys inserted successfully");
                    sendJsonResponse(context,diyJson.encodePrettily());

                });
    }

    private void updateDiys(RoutingContext context, JsonObject diyJson) {

        LOG.info("diyJson Mehabub");
        LOG.info(diyJson.encodePrettily());
        LOG.info("diyJson Mehabub updates");
        LOG.info(diyJson.encodePrettily());

        Integer id = LocalCache.getInstance().store(new QueryData("diy.update", diyJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.info("Check query (ms):" + selectData.responseTimeInMillis);

                    LOG.info("diys updated successfully");
                    sendJsonResponse(context,diyJson.encodePrettily());

                });
    }

    private void deleteDiyItem(RoutingContext context) {

        LOG.info("i am in diy delete :)");
        LOG.info(context);
        JsonObject diyJson = context.getBodyAsJson();
        LOG.debug("JSON :" + diyJson.encodePrettily());
        JsonObject diy = new JsonObject().put("diyId", diyJson.getString("diyId"));


        diy.put("diyJson",diy.encodePrettily());

        Integer id = LocalCache.getInstance().store(new QueryData("diy.select.diyid", diy));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        sendJsonResponse(context,"no diys found" + diyJson.encodePrettily() );
                    }
                    else
                    {
                        LOG.info("diy to be deleted");
                        deleteDiy(context,diy);
                    }
                });


    }
    private void deleteDiy(RoutingContext context, JsonObject diyJson) {

        Integer id = LocalCache.getInstance().store(new QueryData("diy.delete", diyJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.info("Check query (ms):" + selectData.responseTimeInMillis);

                    LOG.info("blog deleted successfully");
                    sendJsonResponse(context,"blogs deleted successfully" + diyJson.encodePrettily() );

                });
    }

}
