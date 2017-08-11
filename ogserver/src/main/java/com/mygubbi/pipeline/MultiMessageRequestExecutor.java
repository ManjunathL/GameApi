package com.mygubbi.pipeline;

import co.paralleluniverse.fibers.Suspendable;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.sync.SyncVerticle;

import java.util.List;

import static io.vertx.ext.sync.Sync.awaitEvent;

/**
 * Created by Sunil on 11-08-2017.
 */
public class MultiMessageRequestExecutor extends SyncVerticle
{
    @Suspendable
    public List<MessageDataHolder> execute(List<MessageDataHolder> messageDataHolders)
    {
        messageDataHolders.stream().forEach(messageDataHolder -> {
            Integer id = LocalCache.getInstance().store(messageDataHolder.getRequestData());
            AsyncResult<Message<Integer>> selectResult = awaitEvent(handler -> VertxInstance.get().eventBus().send(messageDataHolder.getMessageId(), id));
            messageDataHolder.setResponseData(LocalCache.getInstance().remove(selectResult.result().body()));
        });
        return messageDataHolders;
    }

}
