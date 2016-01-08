package com.mygubbi.eventbus;

import com.mygubbi.common.LocalCache;
import com.mygubbi.db.QueryData;
import com.mygubbi.db.QueryPrepareService;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

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
