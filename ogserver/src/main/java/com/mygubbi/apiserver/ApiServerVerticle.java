package com.mygubbi.apiserver;

import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.route.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shaded.apache.http.HttpStatus;


public class ApiServerVerticle extends AbstractVerticle {
    private final static Logger LOG = LogManager.getLogger(ApiServerVerticle.class);

    private static final String STANDARD_RESPONSE = new JsonObject().put("status", "error")
            .put("error", "Request did not have a response").toString();

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(ApiServerVerticle.class.getCanonicalName(), new DeploymentOptions().setInstances(4));
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

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
            if (url.startsWith("http:"))
            {
                url = "https:" + url.substring(5);
            }
            else
            {
                url = httpsRedirectUrl;
            }
            HttpServerResponse response = routingContext.response();
            response.setStatusCode(HttpStatus.SC_MOVED_PERMANENTLY)
                    .setStatusMessage("Server requires https")
                    .putHeader("Location", url)
                    .end();
        });
        int httpPort = ConfigHolder.getInstance().getInteger("http_port", 80);
        server.requestHandler(router::accept).listen(httpPort);
    }

    private void setupHttpSslServer()
    {
        Router router = Router.router(VertxInstance.get());

        this.setupApiHandler(router);
        this.setupStaticHandler(router);

        HttpServerOptions options = new HttpServerOptions()
                .setKeyStoreOptions(new JksOptions().
                        setPath("keystore.jks").
                        setPassword("m!gubb!"))
                .setTrustStoreOptions(new JksOptions().
                        setPath("keystore.jks").
                        setPassword("m!gubb!"))
                .setSsl(true)
                .setCompressionSupported(true)
                .setTcpKeepAlive(true);

        int httpsPort = ConfigHolder.getInstance().getInteger("https_port", 443);
        VertxInstance.get().createHttpServer(options).requestHandler(router::accept).listen(httpsPort);
    }

    private void setupEventBusHandler(Router router) {
        SockJSHandler sockJSHandler = SockJSHandler.create(VertxInstance.get());
        PermittedOptions inbound = new PermittedOptions().setAddress("*");
        PermittedOptions outbound = new PermittedOptions().setAddress("*");
        BridgeOptions options = new BridgeOptions().addInboundPermitted(inbound).addOutboundPermitted(outbound);
        sockJSHandler.bridge(options, be -> {

        });
        router.route("/apibus/*").handler(sockJSHandler);
    }

    private void setupStaticHandler(Router router) {
        router.route(HttpMethod.GET, "/*").handler(StaticHandler.create());
        router.route(HttpMethod.GET, "/*").failureHandler(new RouteFailureHandler());
    }

    private void setupApiHandler(Router router) {

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
        router.mountSubRouter("/api/es", new ProductSearchHandler(VertxInstance.get()));
        router.mountSubRouter("/api/pre.search", new PreSearchHandler(vertx));
        router.mountSubRouter("/api/auto.search", new AutoSearchHandler(vertx));

        //router.mountSubRouter("/api/consult", new ConsultHandler(vertx)); //todo: this is just for testing as of now, remove this handler once the real Kapture URL is put in kapture.js
    }

    private void logHeadersHandler(Router router) {
        router.route(HttpMethod.POST, "/api/:address/").handler(routingContext -> {
            LOG.info(routingContext.request().headers().get("content-type") + ":" + routingContext.request().headers().get("content-length"));
            LOG.info("Data In:" + routingContext.getBodyAsJson());
            routingContext.next();
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }


}
