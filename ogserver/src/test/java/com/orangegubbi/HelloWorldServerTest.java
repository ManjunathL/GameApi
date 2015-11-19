package com.orangegubbi;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;

import org.junit.Test;

public class HelloWorldServerTest
{
	@Test
	public void testSayHello() throws Exception
	{
		HttpClient client = Vertx.vertx().createHttpClient();

		System.out.println("Sending request ...");
		long startTime = System.currentTimeMillis();
		HttpClientRequest clientRequest = client.request(HttpMethod.POST, 8000, "localhost", "/", 
				response -> {
			  System.out.println("Received response with status code " + response.statusCode() + " in (ms):" 
				+ (System.currentTimeMillis() - startTime) + 
				". Server response (ms): " + response.getHeader("x-response-time"));
			  response.handler(buffer -> {
				  System.out.println("Response: " + buffer); 
			  });
			});
		
        clientRequest.putHeader("content-type", "application/json")
        	.putHeader("content-length", Integer.toString("sayhi".length())).write("sayhi").end();
		
		Thread.sleep(2000);
	}
}
