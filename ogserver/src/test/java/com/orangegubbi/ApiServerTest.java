package com.orangegubbi;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

import org.junit.Test;

import com.orangegubbi.apiserver.ApiServerVerticle;

public class ApiServerTest
{
	@Test
	public void testApiServer() throws Exception
	{
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(ApiServerVerticle.class.getCanonicalName(), result -> {
			if (result.succeeded()) executeTest(vertx);
			else System.out.println(result.cause());
		});
		Thread.sleep(2000);
	}
	
	private void executeTest(Vertx vertx )
	{
		HttpClient client = vertx.createHttpClient();

		System.out.println("Sending request ...");
		HttpClientRequest clientRequest = client.request(HttpMethod.POST, 8080, "localhost", "/api/user.register/", response -> {
			  System.out.println("Received response with status code " + response.statusCode());
			  response.handler(buffer -> {
				  System.out.println("Received a part of the response body: " + buffer); 
			  });
			});
		
		JsonObject profileDetail = new JsonObject().put("name", "Kevin").put("city", "Bangalore");
		JsonObject userProfile = new JsonObject().put("user_id", "minion@factory.com").put("profile", profileDetail);
		String json = userProfile.toString();

		System.out.println("Sending:" + json);
        clientRequest.putHeader("content-type", "application/json")
        	.putHeader("content-length", Integer.toString(json.length())).write(json).end();
	}
}
