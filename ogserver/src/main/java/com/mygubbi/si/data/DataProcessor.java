package com.mygubbi.si.data;

/**
 * Created by Sunil on 07-01-2016.
 */
public interface DataProcessor<T>
{
    public abstract void process(T dataObject);

}
