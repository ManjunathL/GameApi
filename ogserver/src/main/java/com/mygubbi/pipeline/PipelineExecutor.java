package com.mygubbi.pipeline;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.QueryData;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
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
        LOG.info("In List execute size = "+steps.size());
        handleStepInPipeline(steps, 0, responseHandler);
    }

    public void execute(MessageDataHolder step, PipelineResponseHandler responseHandler)
    {
        LOG.info("In step");
        List<MessageDataHolder> steps = new ArrayList<>();
        steps.add(step);
        handleStepInPipeline(steps, 0, responseHandler);
    }

    private void handleStepInPipeline(List<MessageDataHolder> steps, int index, PipelineResponseHandler responseHandler)
    {
        LOG.info("index = "+index +", "+steps.size());
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
//                    LOG.info("selectResult.result().body() = "+selectResult.result().body());
                    if(selectResult.result().body() != null) {
                        LOG.info("selectResult.result().body() = " + selectResult.result().body());
                        messageDataHolder.setResponseData(LocalCache.getInstance().remove(selectResult.result().body()));
                        //Call step response handler


                        int nextIndex = index + 1;
                        LOG.info("Calling next index :: " + nextIndex);
                        handleStepInPipeline(steps, nextIndex, responseHandler);
                    }else{
                        LOG.info("selectResult.result().body() IS NULL");
                    }
                });


    }

}
