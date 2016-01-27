package com.mygubbi.si.rest;

/**
 * Created by test on 18-01-2016.
 */
public interface RestResponseHandler
{
    public abstract void handle(RequestData requestData, int statusCode, String response);

    public abstract void error(RequestData requestData, int statusCode, String error);

}
