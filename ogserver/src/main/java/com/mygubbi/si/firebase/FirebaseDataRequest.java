package com.mygubbi.si.firebase;


import io.vertx.core.json.JsonObject;

/**
 * Created by test on 04-10-2016.
 */
public class FirebaseDataRequest
{
    private String dataUrl;
    private JsonObject jsonData;
    private boolean error;
    private String errorMessage;

    public String getDataUrl()
    {
        return dataUrl;
    }

    public FirebaseDataRequest setDataUrl(String dataUrl)
    {
        this.dataUrl = dataUrl;
        return this;
    }

    public JsonObject getJsonData()
    {
        return jsonData;
    }

    public FirebaseDataRequest setJsonData(JsonObject jsonData)
    {
        this.jsonData = jsonData;
        return this;
    }

    public boolean isError()
    {
        return error;
    }

    public FirebaseDataRequest setError(boolean error)
    {
        this.error = error;
        return this;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public FirebaseDataRequest setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
        return this;
    }

    @Override
    public String toString()
    {
        return "FirebaseDataRequest{" +
                "dataUrl='" + dataUrl + '\'' +
                ", jsonData=" + jsonData +
                ", error=" + error +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
