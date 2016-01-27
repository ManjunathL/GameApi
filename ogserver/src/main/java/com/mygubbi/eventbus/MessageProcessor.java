package com.mygubbi.eventbus;

/**
 * Created by test on 08-01-2016.
 */
public interface MessageProcessor <Request>
{
    public Class getType();

    public Object process(Request requestObject);
}
