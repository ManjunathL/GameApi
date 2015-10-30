package com.orangegubbi.assetserver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;


public class AssetServerVerticle extends AbstractVerticle
{

	public static void main(String[] args)
	{
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(AssetServerVerticle.class.getCanonicalName(), new DeploymentOptions().setInstances(4));
	}

	@Override
	public void start() throws Exception
	{
		super.start();
		Router router = Router.router(vertx);
		Route route = router.route(HttpMethod.GET, "/static/*");
	    route.handler(StaticHandler.create());

	    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	    System.out.println("Asset Server started.");
	}

	@Override
	public void stop() throws Exception
	{
		super.stop();
	}

	
}
