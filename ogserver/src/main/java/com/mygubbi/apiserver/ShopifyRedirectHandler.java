package com.mygubbi.apiserver;

import com.mygubbi.common.StringUtils;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.route.RouteUtil;
import io.vertx.core.Handler;
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
 * Created by Sunil on 15-02-2016.
 */
public class ShopifyRedirectHandler implements Handler<RoutingContext>
{
    private final static Logger LOG = LogManager.getLogger(ShopifyRedirectHandler.class);
    private static final String COLLECTIONS_URI = "/collections/";
    public static final int COLLECTIONS_URI_LENGTH = COLLECTIONS_URI.length();

    private final ConcurrentHashMap<RequestKey, ApiResponse> dataCache = new ConcurrentHashMap(256);
    private Map<String, String> shopifyUrlsMap = Collections.emptyMap();

    public ShopifyRedirectHandler()
    {
        this.prepareRedirectUrls();
    }

    private void prepareRedirectUrls()
    {
        JsonArray shopifyUrls = (JsonArray) ConfigHolder.getInstance().getConfigValue("shopifyurls");
        if (shopifyUrls == null || shopifyUrls.isEmpty())
        {
            LOG.info("Shopify Urls not configured.");
            return;
        }

        this.shopifyUrlsMap = new HashMap<>(256);
        for (Object urlObject : shopifyUrls)
        {
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
            RouteUtil.getInstance().redirect(context, newUrl);
            return;
        }

        if (oldUrl.startsWith(COLLECTIONS_URI))
        {
            LOG.info("Current url:" + oldUrl);
            List<String> urlParts = StringUtils.fastSplit(oldUrl.substring(COLLECTIONS_URI_LENGTH), '/');
            if (urlParts != null && urlParts.size() == 3 && urlParts.get(1).equals("products"))
            {
                newUrl = "/product/" + urlParts.get(2);
                RouteUtil.getInstance().redirect(context, newUrl);
                this.shopifyUrlsMap.put(oldUrl, newUrl);
                return;
            }
        }

        context.next();
    }
}
