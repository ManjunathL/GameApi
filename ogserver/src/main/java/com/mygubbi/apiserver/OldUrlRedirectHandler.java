package com.mygubbi.apiserver;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.StringUtils;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.route.RouteUtil;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Mehbub on 12-01-2017.
 */
public class OldUrlRedirectHandler implements Handler<RoutingContext>
{
    private final static Logger LOG = LogManager.getLogger(OldUrlRedirectHandler.class);
    private static final String COLLECTIONS_URI = "/product-";
    public static final int COLLECTIONS_URI_LENGTH = COLLECTIONS_URI.length();

    private final ConcurrentHashMap<RequestKey, ApiResponse> dataCache = new ConcurrentHashMap(256);
    private Map<String, String> shopifyUrlsMap = Collections.emptyMap();

    public OldUrlRedirectHandler()
    {
        this.prepareRedirectUrls();
    }

    private void prepareRedirectUrls()
    {
        String test = "{\n" +
                "  \"oldurls\": [\n" +
                "    {\n" +
                "      \"old\": \"/bedroom-interior-design\",\n" +
                "      \"new\": \"/wardrobe-designs-online\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"old\": \"/newproduct-details-\",\n" +
                "      \"new\": \"/product-\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"old\": \"/living-and-dining-room-designs\",\n" +
                "      \"new\": \"/storage-solutions\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n" +
                "\n";
        JsonObject jsobj = new JsonObject(test);

        //JsonArray shopifyUrls = (JsonArray) ConfigHolder.getInstance().getConfigValue("oldurls");
        JsonArray shopifyUrls = (JsonArray) jsobj.getValue("oldurls");

        if (shopifyUrls == null || shopifyUrls.isEmpty())
        {
            LOG.info("OLD Urls not configured.");
            LOG.info("i am null redirect 0000");

            return;
        }

        this.shopifyUrlsMap = new HashMap<>(256);
        for (Object urlObject : shopifyUrls)
        {
            LOG.info(urlObject.toString());
            JsonObject urlJson = (JsonObject) urlObject;
            this.shopifyUrlsMap.put(urlJson.getString("old"), urlJson.getString("new"));
        }
    }

    @Override
    public void handle(RoutingContext context)
    {
        String oldUrl = context.request().uri();
        String newUrl = this.shopifyUrlsMap.get(oldUrl);

        if (StringUtils.isNonEmpty(newUrl))
        {
            LOG.info("iam in old url redirect");
            RouteUtil.getInstance().redirect(context, newUrl, "Redirecting to new mygubbi.com site");
            return;
        }

/*
        if (oldUrl.startsWith(COLLECTIONS_URI))
        {
            String oldURI = oldUrl;
            String newURI = oldURI.replace("/newproduct-details-","product-");
            LOG.info("Current url:" + newURI);
            RouteUtil.getInstance().redirect(context, newURI, "Redirecting to new mygubbi.com site");

        }*/
        if (oldUrl.startsWith(COLLECTIONS_URI))
        {
            String oldURI = oldUrl;
            // String newURI = oldURI.replace("/newproduct-details-","product-");
            LOG.info("Current url:" + oldUrl);
            String productId= oldURI.substring(oldURI.indexOf('-')+1);
            LOG.info("productId: " + productId);
            Integer id = LocalCache.getInstance().store(new QueryData("product.select.productid", new JsonObject().put("productId", productId)));
            VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                    (AsyncResult<Message<Integer>> selectResult) -> {
                        LOG.info("Executing query:" + "product.select.productid" + " | " + productId);
                        QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                        if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                        {
                            LOG.error("sorry no product id found");
                        }
                        else
                        {
                            JsonArray jsonDataRows = selectData.getJsonDataRows("productJson");
                            LOG.info(jsonDataRows.encodePrettily());
                            JsonObject obj = jsonDataRows.getJsonObject(0);

                            String newURI = "/" +obj.getString("seoId") + "/p/" + productId;
                            LOG.info(newURI);
                            RouteUtil.getInstance().redirect(context, newURI, "Redirecting to new product url");
                        }
                    });
            return;

        }

        context.next();
    }
}
