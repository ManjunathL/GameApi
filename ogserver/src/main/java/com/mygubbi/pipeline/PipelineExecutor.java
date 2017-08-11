package com.mygubbi.pipeline;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * PipelineExecutor takes in a series of steps to execute and returns the results of the execution.
 *
 * Created by Sunil on 11-08-2017.
 */
public class PipelineExecutor
{
    private final static Logger LOG = LogManager.getLogger(PipelineExecutor.class);

    public void execute(List<MessageDataHolder> steps, PipelineResponseHandler responseHandler)
    {
        handleStepInPipeline(steps, 0, responseHandler);
    }

    public void execute(MessageDataHolder step, PipelineResponseHandler responseHandler)
    {
        List<MessageDataHolder> steps = new ArrayList<>();
        steps.add(step);
        handleStepInPipeline(steps, 0, responseHandler);
    }

    private void handleStepInPipeline(List<MessageDataHolder> steps, int index, PipelineResponseHandler responseHandler)
    {
        if (index >= steps.size())
        {
            LOG.debug("Done with all steps :" + index + " : " + steps.size());
            responseHandler.handleResponse(steps);
            return;
        }

        MessageDataHolder messageDataHolder = steps.get(index);
        Integer id = LocalCache.getInstance().store(messageDataHolder.getRequestData());
        VertxInstance.get().eventBus().send(messageDataHolder.getMessageId(), id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    messageDataHolder.setResponseData(LocalCache.getInstance().remove(selectResult.result().body()));
                    //Call step response handler
                    handleStepInPipeline(steps, index+1, responseHandler);
                });

    }

}
