package com.mygubbi.prerender;

import com.mygubbi.common.VertxInstance;
import com.mygubbi.route.RouteUtil;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by nitinpuri on 16-02-2016.
 */
public class PrerenderingHandler implements Handler<RoutingContext> {

    private static final String BOTS = "googlebot|baiduspider|twitterbot|facebookexternalhit|rogerbot|linkedinbot|embedly|quora link preview|showyoubot|outbrain|pinterest|slackbot|vkShare|W3C_Validator";
    private static final String ESCAPED_FRAGMENT = "_escaped_fragment_";
    private static final String FILE_EXTS = ".*\\.(otf|eot|svg|ttf|woff|woff2|json|html|js|css|xml|less|png|jpg|jpeg|gif|pdf|doc|txt|ico|rss|zip|rar|docx|ppt|xls|xlsx|pptx)$";

    private final static Logger LOG = LogManager.getLogger(PrerenderingHandler.class);

    @Override
    public void handle(RoutingContext context) {
        HttpServerRequest request = context.request();
        String uri = request.absoluteURI();
        String query = request.query() + "";

        String userAgent = request.getHeader("User-Agent");

        if ((userAgent.toLowerCase().matches(BOTS) || query.contains(ESCAPED_FRAGMENT)) && !uri.matches(FILE_EXTS)) {

            final HttpClient client = VertxInstance.get().createHttpClient();
            client.getNow(3000, "localhost", String.format("%s?%s", uri, query), response -> {
                response.bodyHandler(buffer -> {
                    String responseText = buffer.getString(0, buffer.length());
                    RouteUtil.getInstance().sendResponse(context, responseText, "text/html");
                });
            });

        } else {
            context.next();
        }

    }

}
