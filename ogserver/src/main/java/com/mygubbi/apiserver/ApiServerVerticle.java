package com.mygubbi.apiserver;

import com.mygubbi.route.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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
        Router router = Router.router(vertx);

        this.setupApiHandler(router);
        this.setupEventBusHandler(router);
        this.setupStaticHandler(router);

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
        startFuture.complete();
    }

    private void setupEventBusHandler(Router router) {
        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        PermittedOptions inbound = new PermittedOptions().setAddress("*");
        PermittedOptions outbound = new PermittedOptions().setAddress("*");
        BridgeOptions options = new BridgeOptions().addInboundPermitted(inbound).addOutboundPermitted(outbound);
        sockJSHandler.bridge(options, be -> {

        });
        router.route("/apibus/*").handler(sockJSHandler);
    }

	private void setupStaticHandler(Router router)
	{
		router.route(HttpMethod.GET, "/*").handler(StaticHandler.create());
    }

    private void setupApiHandler(Router router) {
        router.mountSubRouter("/api/categories", new CategoryHandler(vertx));
        router.mountSubRouter("/api/products", new ProductHandler(vertx));
        router.mountSubRouter("/api/user.short", new UserProfileShortHandler(vertx));
        router.mountSubRouter("/api/shortlist.short", new ShortlistShortHandler(vertx));
        router.mountSubRouter("/api/user.register", new UserRegistrationHandler(vertx));
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
