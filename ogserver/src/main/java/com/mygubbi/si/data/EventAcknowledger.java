package com.mygubbi.si.data;

/**
 * Created by test on 11-01-2016.
 */
public interface EventAcknowledger
{
    public abstract void done(EventData eventData);

    public abstract void failed(EventData eventData, String reason);

    public abstract void failed(String id, String rawData, String reason);
}
