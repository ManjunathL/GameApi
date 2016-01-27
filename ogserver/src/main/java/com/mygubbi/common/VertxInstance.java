package com.mygubbi.common;

import io.vertx.core.Vertx;

/**
 * Created by test on 15-01-2016.
 */
public class VertxInstance
{
    public static final Vertx vertx = Vertx.vertx();

    public static Vertx get()
    {
        return vertx;
    }
}
