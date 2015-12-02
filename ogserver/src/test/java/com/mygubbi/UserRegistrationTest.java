package com.mygubbi;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

import org.junit.Test;

public class UserRegistrationTest
{
	@Test
	public void testUserRegistration() throws Exception
	{
		JsonObject profileDetail = new JsonObject().put("name", "Kevin").put("city", "Bangalore");
		JsonObject userProfile = new JsonObject().put("user_id", "minion2@factory.com").put("profile", profileDetail);
		String json = userProfile.toString();

		Vertx vertx = Vertx.vertx();
		this.sendRequest(vertx, json, 1000);
		
		Thread.sleep(2000);
	}

	private void sendRequest(Vertx vertx, String json, int max)
	{
//		System.out.println("Sending request " + max + ":" + json);
		HttpClient client = vertx.createHttpClient();
		long startTime = System.currentTimeMillis();
		HttpClientRequest clientRequest = client.request(HttpMethod.POST, 8080, "localhost", "/api/user.register/", 
				response -> {
			  System.out.println("Response " + max + " : " + response.statusCode() + " : " 
				+ (System.currentTimeMillis() - startTime)); 
			  response.handler(buffer -> {
//				  System.out.println("Response: " + buffer); 
			  });
			  
			  client.close();
			  if (max == 1000)
			  {
				  System.out.println("Keep-Alive Header" + response.headers().get(HttpHeaders.KEEP_ALIVE)); 
				  System.out.println("Connection Header" + response.headers().get(HttpHeaders.CONNECTION)); 
			  }

			  if (max > 0)
			  {
				  vertx.setTimer(100, handler -> {
					  this.sendRequest(vertx, json, (max - 1));
				  });
			  }

			  response.exceptionHandler(handler -> {
				 handler.getCause().printStackTrace(); 
			  });
			});
		
        clientRequest.putHeader("content-type", "application/json")
	    	.putHeader(HttpHeaders.CONNECTION, "Keep-Alive")
        	.putHeader("content-length", Integer.toString(json.length()))
        	.write(json).end();
	}
}
