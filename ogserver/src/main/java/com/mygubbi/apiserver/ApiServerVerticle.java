package com.mygubbi.apiserver;

import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.config.StaticConfigHandler;
import com.mygubbi.prerender.PrerenderingHandler;
import com.mygubbi.route.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.utils.URIUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;


public class ApiServerVerticle extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(ApiServerVerticle.class);

    private static String[] URLS = new String[]{
            "http://mygubbi.com", "http://mygubbi.com/", "http://mygubbi.com/bangalore-lp1",
            "http://www.mygubbi.com", "http://www.mygubbi.com/", "http://www.mygubbi.com/bangalore-lp",
            "https://mygubbi.com", "https://mygubbi.com/", "https://mygubbi.com/bangalore-lp1",
            "https://www.mygubbi.com", "https://www.mygubbi.com/", "https://www.mygubbi.com/bangalore-lp"
    };

    private static final String STANDARD_RESPONSE = new JsonObject().put("status", "error")
            .put("error", "Request did not have a response").toString();

    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(ApiServerVerticle.class.getCanonicalName(), new DeploymentOptions().setInstances(4));
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.setupHttpSslServer();
        this.setupHttpRedirectServer();
        startFuture.complete();
    }

    private void setupHttpRedirectServer()
    {

        String httpsRedirectUrl = ConfigHolder.getInstance().getStringValue("urlwithssl", "https://www.mygubbi.com");
        HttpServer server = VertxInstance.get().createHttpServer();
        Router router = Router.router(VertxInstance.get());
        router.route().handler(routingContext -> {
            String url = routingContext.request().absoluteURI();
            try
            {
                URI baseUri = new URI(url);
                HttpHost httpHost = URIUtils.extractHost(baseUri);
                String hostName = httpHost.getHostName();
                boolean hostNameIsNaked = StringUtils.countMatches(hostName, ".") == 1;
                boolean httpScheme = ("http").equals(httpHost.getSchemeName());
                //boolean httpsScheme = ("https").equals(httpHost.getSchemeName());
                if (hostNameIsNaked)
                {
                    if (hostNameIsNaked) hostName = "www." + hostName;
                    URI newUri = URIUtils.rewriteURI(baseUri, new HttpHost(hostName, httpHost.getPort(), "https"));
                    url = newUri.toString();
                    LOG.info("Rewriting if doesn't starts with www", url);
                    LOG.info("URL " + url + " rewritten as :" + newUri.toString());
                }
                else
                {
                    LOG.info("if starts with www ", url);
                    url = httpsRedirectUrl;
                }
            }
            catch (URISyntaxException e)
            {
                LOG.info("Error with url:" + url + " || " + e.getMessage());
                url = httpsRedirectUrl;
            }
            RouteUtil.getInstance().redirect(routingContext, url, "Redirecting to secure mygubbi.com site");
        });
        this.setupRedirectHandlerForOldUrls(router);
        int httpPort = ConfigHolder.getInstance().getInteger("http_port", 80);
        server.requestHandler(router::accept).listen(httpPort);
    }

    private void setupHttpSslServer()
    {
        Router router = Router.router(VertxInstance.get());

        this.setupApiHandler(router);
        this.setupNakedDomainRouter(router);
        this.setupRedirectHandlerForShopifyUrls(router);
        this.setupRedirectHandlerForOldUrls(router);
        this.setupPrerenderHandler(router);
        this.setupStaticConfigHandler(router);
        this.setupStaticHandler(router);

        String ssl_keystore = ConfigHolder.getInstance().getStringValue("ssl_keystore", "ssl/keystore.jks");
        String ssl_password = ConfigHolder.getInstance().getStringValue("ssl_password", "m!gubb!");
        HttpServerOptions options = new HttpServerOptions()
                .setKeyStoreOptions(new JksOptions().
                        setPath(ssl_keystore).
                        setPassword(ssl_password))
                .setTrustStoreOptions(new JksOptions().
                        setPath(ssl_keystore).
                        setPassword(ssl_password))
                .setSsl(true)
                .setCompressionSupported(true)
                .setTcpKeepAlive(true);

        int httpsPort = ConfigHolder.getInstance().getInteger("https_port", 443);
        VertxInstance.get().createHttpServer(options).requestHandler(router::accept).listen(httpsPort);
    }

    private void setupStaticConfigHandler(Router router) {
        router.route(HttpMethod.GET, "/*").handler(new StaticConfigHandler());
    }

    private void setupEventBusHandler(Router router)
    {
        SockJSHandler sockJSHandler = SockJSHandler.create(VertxInstance.get());
        PermittedOptions inbound = new PermittedOptions().setAddress("*");
        PermittedOptions outbound = new PermittedOptions().setAddress("*");
        BridgeOptions options = new BridgeOptions().addInboundPermitted(inbound).addOutboundPermitted(outbound);
        sockJSHandler.bridge(options, be -> {

        });
        router.route("/apibus/*").handler(sockJSHandler);
    }

    private void setupPrerenderHandler(Router router)
    {
        router.route(HttpMethod.GET, "/*").handler(new PrerenderingHandler());
    }

    private void setupStaticHandler(Router router)
    {
        router.route(HttpMethod.GET, "/*").handler(StaticHandler.create().setMaxAgeSeconds(ConfigHolder.getInstance().getMaxAge()));
        router.route(HttpMethod.GET, "/*").failureHandler(new RouteFailureHandler());
    }

    private void setupRedirectHandlerForShopifyUrls(Router router)
    {
        boolean shopifyRedirectOn = ConfigHolder.getInstance().getBoolean("shopifyredirect", false);
        if (shopifyRedirectOn)
        {
            router.route(HttpMethod.GET, "/*").handler(new ShopifyRedirectHandler());
            LOG.info("Registered Shopify url handler");
        }
    }
    private void setupRedirectHandlerForOldUrls(Router router)
    {
        boolean oldUrlRedirectOn = ConfigHolder.getInstance().getBoolean("oldurlredirect", false);
        if (oldUrlRedirectOn)
        {
            router.route(HttpMethod.GET, "/*").handler(new OldUrlRedirectHandler());
            LOG.info("Registered Old url handler");
        }
    }

    private void setupNakedDomainRouter(Router router)
    {
        router.route(HttpMethod.GET, "/*").handler(new NakedDomainHandler());
    }

    private void setupApiHandler(Router router)
    {

        boolean cacheOn = ConfigHolder.getInstance().getBoolean("apicache", false);
        if (cacheOn)
        {
            router.get("/api/*").handler(CacheHandler.getInstance());
            LOG.info("Registered cache handler");
        }
        router.mountSubRouter("/api/categories", new CategoryHandler(VertxInstance.get()));
        router.mountSubRouter("/api/filter.master", new FilterMasterHandler(VertxInstance.get()));
        router.mountSubRouter("/api/products", new ProductHandler(VertxInstance.get()));
        router.mountSubRouter("/api/relatedproducts", new RelatedProductHandler(VertxInstance.get()));
        router.mountSubRouter("/api/appliances", new ApplianceHandler(VertxInstance.get()));
        router.mountSubRouter("/api/stories", new StoryHandler(VertxInstance.get()));
        router.mountSubRouter("/api/blogs", new BlogHandler(VertxInstance.get()));
        router.mountSubRouter("/api/es", new ProductSearchHandler(VertxInstance.get()));
        router.mountSubRouter("/api/pre.search", new PreSearchHandler(VertxInstance.get()));
        router.mountSubRouter("/api/auto.search", new AutoSearchHandler(VertxInstance.get()));
        router.mountSubRouter("/api/seo", new SeoHandler(VertxInstance.get()));
        router.mountSubRouter("/api/diy", new DIYHandler(VertxInstance.get()));

        //router.mountSubRouter("/api/consult", new ConsultHandler(vertx)); //todo: this is just for testing as of now, remove this handler once the real Kapture URL is put in kapture.js
    }

    private void logHeadersHandler(Router router)
    {
        router.route(HttpMethod.POST, "/api/:address/").handler(routingContext -> {
            LOG.info(routingContext.request().headers().get("content-type") + ":" + routingContext.request().headers().get("content-length"));
            LOG.info("Data In:" + routingContext.getBodyAsJson());
            routingContext.next();
        });
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }


}