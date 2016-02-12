package com.mygubbi.search;

import io.vertx.core.json.JsonObject;

public class IndexData
{
    private String id;
    private String index;
    private String type;
    private JsonObject document;

    public boolean errorFlag;
    public String errorMessage;
    public Throwable errorCause;

    public IndexData()
    {

    }

    public IndexData(String id, String index, String type, JsonObject document)
    {
        this.id = id;
        this.index = index;
        this.type = type;
        this.document = document;
    }

    public String getId()
    {
        return id;
    }

    public IndexData setId(String id)
    {
        this.id = id;
        return this;
    }

    public String getIndex()
    {
        return index;
    }

    public IndexData setIndex(String index)
    {
        this.index = index;
        return this;
    }

    public String getType()
    {
        return type;
    }

    public IndexData setType(String type)
    {
        this.type = type;
        return this;
    }

    public JsonObject getDocument()
    {
        return document;
    }

    public String getDocumentAsString()
    {
        return document.toString();
    }

    public IndexData setDocument(JsonObject document)
    {
        this.document = document;
        return this;
    }


}
