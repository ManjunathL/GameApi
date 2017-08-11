package com.mygubbi.pipeline;

import co.paralleluniverse.fibers.Suspendable;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.sync.SyncVerticle;

import static io.vertx.ext.sync.Sync.awaitEvent;

/**
 * Created by Sunil on 11-08-2017.
 */
public class SingleMessageRequestExecutor extends SyncVerticle
{
    @Suspendable
    public Object execute(String mesageId, Object requestData)
    {
        Integer id = LocalCache.getInstance().store(requestData);
        AsyncResult<Message<Integer>> selectResult = awaitEvent(handler -> VertxInstance.get().eventBus().send(mesageId, id));
        return LocalCache.getInstance().remove(selectResult.result().body());
    }

}
