package com.mygubbi.apiserver;

import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.game.proposal.ProposalHandler;
import com.mygubbi.game.proposal.ProposalModuleHandler;
import com.mygubbi.game.proposal.ProposalProductHandler;
import com.mygubbi.route.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
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
        this.setupHttpSslServer();
        startFuture.complete();
    }

    private void setupHttpSslServer()
    {
        Router router = Router.router(VertxInstance.get());

        this.setupApiHandler(router);
        this.setupStaticHandler(router);

        int httpsPort = ConfigHolder.getInstance().getInteger("game_https_port", 1443);
        HttpServerOptions options = this.getHttpsServerOptions();
        VertxInstance.get().createHttpServer(options).requestHandler(router::accept).listen(httpsPort);
        LOG.info("Starting http server on port : " + httpsPort);
    }

    private HttpServerOptions getHttpsServerOptions()
    {
        String ssl_keystore = ConfigHolder.getInstance().getStringValue("ssl_keystore", "ssl/keystore.jks");
        String ssl_password = ConfigHolder.getInstance().getStringValue("ssl_password", "m!gubb!");
        return new HttpServerOptions()
/*
                .setKeyStoreOptions(new JksOptions().
                        setPath(ssl_keystore).
                        setPassword(ssl_password))
                .setTrustStoreOptions(new JksOptions().
                        setPath(ssl_keystore).
                        setPassword(ssl_password))
                .setSsl(true)
*/
                .setCompressionSupported(true)
                .setTcpKeepAlive(true);
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

        new ConfiguredRestApiHandler().setup(router);

        router.mountSubRouter("/gapi/user.auth", new GameUserLoginHandler(VertxInstance.get()));
        router.mountSubRouter("/gapi/user.reg", new GameUserRegistrationHandler(VertxInstance.get()));
        router.mountSubRouter("/gapi/user.change_pwd", new GameUserChangePwdHandler(VertxInstance.get()));
        router.mountSubRouter("/gapi/categories", new CategoryHandler(VertxInstance.get()));
        router.mountSubRouter("/gapi/proposal", new ProposalHandler(VertxInstance.get()));
        router.mountSubRouter("/gapi/proposal/product", new ProposalProductHandler(VertxInstance.get()));
        router.mountSubRouter("/gapi/proposal/module", new ProposalModuleHandler(VertxInstance.get()));


        router.mountSubRouter("/gapi/products", new CatalogProductHandler(VertxInstance.get()));



        router.mountSubRouter("/gapi/products", new CatalogProductHandler(VertxInstance.get()));



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
