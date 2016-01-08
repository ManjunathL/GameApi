package com.mygubbi.si.rest;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

/**
 * Created by test on 08-01-2016.
 */
public class RestInvoker
{
    public void request(String baseUrl, String uri, String authToken, JsonObject payload, RestResponseHandler handler)
    {
        HttpClient client = Vertx.vertx().createHttpClient();
        HttpClientRequest clientRequest = client.request(HttpMethod.POST, 80, "http://www.knowtify.io/api/v1", "/contacts/upsert",
                response -> {
                    response.handler(buffer -> {

                    });
                });
        clientRequest.putHeader("Content-Type", "application/json").putHeader("Authorization", authToken)
                .putHeader("content-length", Integer.toString("sayhi".length())).write("sayhi").end();
    }

}
