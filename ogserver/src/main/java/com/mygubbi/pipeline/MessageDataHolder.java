package com.mygubbi.pipeline;

/**
 * Created by test on 11-08-2017.
 */
public class MessageDataHolder
{
    private String messageId;
    private Object requestData;
    private Object responseData;

    public MessageDataHolder(String messageId)
    {
        this(messageId, null);
    }

    public MessageDataHolder(String messageId, Object requestData)
    {
        this.messageId = messageId;
        this.requestData = requestData;
    }

    public void setResponseData(Object responseData)
    {
        this.responseData = responseData;
    }

    public String getMessageId()
    {
        return messageId;
    }

    public Object getRequestData()
    {
        return requestData;
    }

    public Object getResponseData()
    {
        return responseData;
    }
}
