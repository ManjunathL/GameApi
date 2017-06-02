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

import us.monoid.json.JSONException;
import us.monoid.web.Resty;


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
        this.get("/").handler(this::getAll); //Category, Sub-Category
        this.get("/item").handler(this::getBlogItem); //Category, Sub-Category
        this.post("/insertBlog").handler(this::addBlogItem); //Category, Sub-Category
    }

    private void getAll(RoutingContext context) {

        try {
            Resty resty = new Resty();
            resty.withHeader("Authorization", "Bearer AkROdcTIJ8gw416A");
LOG.info("https://mehbub--mygubbi.directus.io/api/1.1/tables/blog/rows?filters[active]=1");
            JsonObject obj = new JsonObject(resty.json("https://mehbub--mygubbi.directus.io/api/1.1/tables/blog/rows?filters[active]=1").object().toString());
            LOG.info("obj");
            LOG.info(obj.encodePrettily());
            JsonObject newObj = new JsonObject();
            newObj.put("data", obj.getJsonArray("data"));
           // newObj.getValue("blog_categories").toString();
            for (int i = 0; i < obj.getJsonArray("data").size(); ++i) {

                JsonObject jsn = obj.getJsonArray("data").getJsonObject(i);
                LOG.info("jsn");
                String keyVal = jsn.getString("testing");
                LOG.info(keyVal);
                String[] arr = keyVal.split(",");
                ArrayList<String> list = new ArrayList<String>();
                for (int j =0; j< arr.length; j++) {
                    list.add(arr[j]);
                    LOG.info(arr[j]);
                }
                LOG.info(list);
                jsn.put("tagArr", list);

                LOG.info(jsn.encodePrettily());
            }
            LOG.info("new Obj");

            sendJsonResponse(context, obj.encodePrettily()) ;

        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying - " + e);
        }

    }

    private void getBlogItem(RoutingContext context) {
        try {
            Resty resty = new Resty();
            String blogId = context.request().getParam("blogId");
LOG.info(blogId);
            resty.withHeader("Authorization", "Bearer AkROdcTIJ8gw416A");
            JsonObject obj = new JsonObject(resty.json("https://mehbub--mygubbi.directus.io/api/1.1/tables/blog/rows?filters[blogId]="+blogId).object().toString());
            LOG.info("obj");
            LOG.info(obj.encodePrettily());
            sendJsonResponse(context, obj.encodePrettily()) ;

        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying - " + e);
        }

    }
    private void addBlogItem(RoutingContext context) {

    LOG.info("i am in insert :)");
        LOG.info(context);
        JsonObject requestJson = context.getBodyAsJson();
        LOG.debug("JSON :" + requestJson.encodePrettily());
    }

}
