package com.mygubbi.si.rest;

import com.mygubbi.common.VertxInstance;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by test on 18-01-2016.
 */
public class RestInvoker
{
    private final static Logger LOG = LogManager.getLogger(RestInvoker.class);

    private RestResponseHandler handler;
    private String authHeader;
    private String baseUrl;

    public RestInvoker(RestResponseHandler handler, String baseUrl)
    {
        this(handler, baseUrl, null);
    }

    public RestInvoker(RestResponseHandler handler, String baseUrl, String authHeader)
    {
        this.handler = handler;
        this.baseUrl = baseUrl;
        this.authHeader = authHeader;
    }

    public void request(RequestData requestData, String uri)
    {
        HttpClient client = VertxInstance.get().createHttpClient();
            HttpClientRequest clientRequest = client.requestAbs(HttpMethod.POST, this.baseUrl + uri,
                response -> {
                    response.handler(buffer -> {
                        LOG.info("Status code:" + response.statusCode() + " for uri:" + uri + " with payload:" + requestData.getPaylod());
                        this.handler.handle(requestData, response.statusCode(), buffer.toString());
                    });
                });

        if (this.authHeader != null) clientRequest.putHeader("Authorization", this.authHeader);
        String payload = requestData.getPaylod();
        clientRequest.putHeader("Content-Type", "application/json").putHeader("content-length", Integer.toString(payload.length())).write(payload).end();
    }

}
