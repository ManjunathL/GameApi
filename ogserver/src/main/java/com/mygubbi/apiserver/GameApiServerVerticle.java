package com.mygubbi.apiserver;

import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.config.StaticConfigHandler;
import com.mygubbi.game.proposal.*;
import com.mygubbi.prerender.PrerenderingHandler;
import com.mygubbi.route.*;
import com.mygubbi.si.crm.CrmApiHandler;
import com.mygubbi.si.crm.CrmOutboundApiHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GameApiServerVerticle extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(GameApiServerVerticle.class);

    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(GameApiServerVerticle.class.getCanonicalName(), new DeploymentOptions().setInstances(4));
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        Router router = Router.router(VertxInstance.get());
        router.route().handler(CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedHeader("Authorization")
                .allowedHeader("Content-Type"));
        this.setupApiHandler(router);
        this.setupHttpSslServer(router);
        this.setupHttpServer(router);
        startFuture.complete();
    }

    private void setupHttpServer(Router router)
    {
        HttpServerOptions options = new HttpServerOptions()
                .setCompressionSupported(true)
                .setTcpKeepAlive(true);
        int httpPort =  ConfigHolder.getInstance().getInteger("http_port",1445);
        VertxInstance.get().createHttpServer(options).requestHandler(router::accept).listen(httpPort);
    }

    private void setupHttpSslServer(Router router)
    {
        String ssl_keystore = ConfigHolder.getInstance().getStringValue("ssl_keystore", "ssl/mygubbiprod.jks");
        String ssl_password = ConfigHolder.getInstance().getStringValue("ssl_password", "0r@nge123$");
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

        int httpsPort =   ConfigHolder.getInstance().getInteger("https_port",1446);;
        VertxInstance.get().createHttpServer(options).requestHandler(router::accept).listen(httpsPort);
    }

    private void setupStaticConfigHandler(Router router) {
        router.route(HttpMethod.GET, "/*").handler(new StaticConfigHandler());
    }
    private void setupPrerenderHandler(Router router)
    {
        router.route(HttpMethod.GET, "/*").handler(new PrerenderingHandler());
    }

    private void setupStaticHandler(Router router)
    {
        router.route(HttpMethod.GET, "/*").handler(StaticHandler.create());
        router.route(HttpMethod.GET, "/*").failureHandler(new RouteFailureHandler());
    }

    private void setupApiHandler(Router router)
    {
        boolean cacheOn = ConfigHolder.getInstance().getBoolean("apicache", false);
        if (cacheOn)
        {
            router.get("/api/*").handler(CacheHandler.getInstance());
            LOG.info("Registered cache handler");
        }
        else
        {
            LOG.info("Cache handler not registered.");
        }
        router.mountSubRouter("/gapi/crm", new CrmApiHandler(VertxInstance.get()));
        router.mountSubRouter("/gapi/outboundCrm", new CrmOutboundApiHandler(VertxInstance.get()));

        new ConfiguredRestApiHandler().setup(router);

        router.mountSubRouter("/gapi/user.auth", new GameUserLoginHandler(VertxInstance.get()));
        router.mountSubRouter("/gapi/user.reg", new GameUserRegistrationHandler(VertxInstance.get()));
        router.mountSubRouter("/gapi/user.change_pwd", new GameUserChangePwdHandler(VertxInstance.get()));
        router.mountSubRouter("/gapi/proposal", new ProposalHandler(VertxInstance.get()));
        router.mountSubRouter("/gapi/product", new ProposalProductHandler(VertxInstance.get()));
        router.mountSubRouter("/gapi/addon", new ProposalAddonHandler(VertxInstance.get()));
        router.mountSubRouter("/gapi/module", new ProposalModuleHandler(VertxInstance.get()));
//        router.mountSubRouter("/gapi/catalogue", new ProductHandler(VertxInstance.get()));
        router.mountSubRouter("/gapi/categories", new CategoryHandler(VertxInstance.get()));
        router.mountSubRouter("/gapi/payment", new PaymentHandler(VertxInstance.get()));
        router.mountSubRouter("/gapi/fileupload", new FileUploadHandler(VertxInstance.get()));

        //LOG.info("Routes:" + router.getRoutes().toString());
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
