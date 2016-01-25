package com.mygubbi.si.data;

/**
 * Created by Sunil on 07-01-2016.
 */
public interface DataProcessor
{
    public abstract String getName();

    public abstract void process(EventData data);

}
