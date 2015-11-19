package com.orangegubbi;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.ResponseTimeHandler;

public class HelloWorldServer
{
	public static void main(String[] args)
	{
		new HelloWorldServer().startServer();
	}
	
	public void startServer()
	{
		Vertx vertx = Vertx.vertx();
		Router router = Router.router(vertx);

		router.route().handler(ResponseTimeHandler.create());
		
		router.route().handler(request -> {

			long startTime = System.currentTimeMillis();
		  // This handler gets called for each request that arrives on the server
		  HttpServerResponse response = request.response();
		  response.putHeader("content-type", "text/plain");

		  // Write to the response and end it
		  response.end("Hello World!");
		  System.out.println("Response time for request(ms):" + (System.currentTimeMillis() - startTime));
		  request.next();
		});

		vertx.createHttpServer().requestHandler(router::accept).listen(8080);	
	}
}
