package com.mygubbi.route;


import com.mygubbi.common.StringUtils;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpHost;

import org.apache.http.client.utils.URIUtils;
/**
 * Created by Mehbub on 10-01-2017.
 */
public class NakedDomainHandler implements Handler<RoutingContext> {
    private final static Logger LOG = LogManager.getLogger(NakedDomainHandler.class);

    public NakedDomainHandler() {

    }

    @Override
    public void handle(RoutingContext context) {
        String url = context.request().absoluteURI();
        String beforeFirstDot1 = url.split("\\.")[0];
        org.apache.commons.lang.StringUtils.split(beforeFirstDot1, "\\:");
        if (url.startsWith("https://www.")) {
            context.next();
            return;
        }
        if (url.equals("https://ozone.mygubbi.com/") || url.equals("https://shobha.mygubbi.com/")) {
            URI baseUri = null;

            try {
                baseUri = new URI(url);
                HttpHost httpHost = URIUtils.extractHost(baseUri);
                String hostName = httpHost.getHostName();
                String beforeFirstDot = hostName.split("\\.")[0];
                LOG.info(beforeFirstDot);
                boolean hostNameIsNaked = org.apache.commons.lang.StringUtils.countMatches(hostName, ".") == 1;

                LOG.info("ruma");
                RouteUtil.getInstance().redirectBuilder(context, url, beforeFirstDot, "Redirecting to https www.mygubbi.com site");
                return;

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
            try {
                URI baseUri = new URI(url);
                HttpHost httpHost = URIUtils.extractHost(baseUri);
                String hostName = httpHost.getHostName();
                boolean hostNameIsNaked = org.apache.commons.lang.StringUtils.countMatches(hostName, ".") == 1;
                if (hostNameIsNaked) {
                    hostName = "www." + hostName;
                    URI newUri = URIUtils.rewriteURI(baseUri, new HttpHost(hostName, httpHost.getPort(), "https"));
                    url = newUri.toString();
                    LOG.info("Rewriting if doesn't starts with www", url);
                    LOG.info("URL " + url + " rewritten as :" + newUri.toString());
                    RouteUtil.getInstance().redirect(context, url, "Redirecting to https www.mygubbi.com site");
                    return;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }


        //This should not reach but let it process normally
        context.next();
    }
}
