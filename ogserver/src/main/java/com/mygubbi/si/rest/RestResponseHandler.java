package com.mygubbi.si.rest;

/**
 * Created by test on 08-01-2016.
 */
public interface RestResponseHandler
{
    public abstract void onSuccess();

    public abstract void onError();
}
