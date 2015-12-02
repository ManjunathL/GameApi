package com.mygubbi;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(VertxUnitRunner.class)
public class ApiServerTest {

    private Vertx vertx;
    private Integer port = 8080;

    @Before
    public void setUp(TestContext context) throws IOException {
        vertx = Vertx.vertx();
        DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("http.port", port));
        vertx.deployVerticle(ServerVerticle.class.getName(), options, context.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testUserRegistration(TestContext context) {
        Async async = context.async();
        HttpClient client = vertx.createHttpClient();
        System.out.println("Sending request ...");
        HttpClientRequest clientRequest = client.request(HttpMethod.POST, 8080, "localhost", "/api/user.register/", response -> {
            System.out.println("Received response with status code " + response.statusCode());
            response.handler(buffer -> System.out.println("Received a part of the response body: " + buffer));
            async.complete();
        });

        JsonObject profileDetail = new JsonObject().put("name", "Kevin").put("city", "Bangalore");
        JsonObject userProfile = new JsonObject().put("user_id", "minion@factory.com").put("profile", profileDetail);
        String json = userProfile.toString();

        System.out.println("Sending:" + json);
        clientRequest.putHeader("content-type", "application/json")
                .putHeader("content-length", Integer.toString(json.length())).write(json).end();
    }

    @Test
    public void testProductHandler(TestContext context) {
        final Async async = context.async();
        vertx.createHttpClient(new HttpClientOptions().setReceiveBufferSize(16384))
                .getNow(port, "localhost", "/api/products", response -> {
                    response.handler(body -> {
                        context.assertTrue(body.toString().contains("56383ff69951036a2732566f"));
                        async.complete();
                    });
                });
    }
}
