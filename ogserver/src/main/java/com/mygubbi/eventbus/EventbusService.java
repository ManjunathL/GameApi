package com.mygubbi.eventbus;

/**
 * Created by test on 08-01-2016.
 */
public class EventbusService
{
    public static EventbusService INSTANCE = new EventbusService();

    public static EventbusService getInstance()
    {
        return INSTANCE;
    }

    private EventbusService()
    {

    }


}
