package com.mygubbi.pipeline;

import java.util.List;

/**
 * Created by test on 11-08-2017.
 */
public interface PipelineResponseHandler
{
    public abstract void handleResponse(List<MessageDataHolder> messageDataHolders);
}
