package com.mygubbi.si.data;

import io.vertx.core.json.JsonObject;

import java.util.Map;

/**
 * Created by test on 11-01-2016.
 */
public class EventData
{
    private String type;
    private String id;
    private String uid;
    private Map<String, Object> data;
    private JsonObject jsonData;

    public EventData()
    {

    }

    public EventData(String id, String uid, String type, Map<String, Object> data)
    {
        this.id = id;
        this.uid = uid;
        this.type = type;
        this.setData(data);
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public Map<String, Object> getData()
    {
        return data;
    }

    public void setData(Map<String, Object> data)
    {
        this.data = data;
        this.jsonData = new JsonObject(this.data);
    }

    public JsonObject getJsonData()
    {
        return jsonData;
    }

    public JsonObject getJsonDataWithIds()
    {
        return new JsonObject(this.data).put("fbid", this.getId()).put("uid", this.getUid());
    }

    public JsonObject getEventJson()
    {
        return new JsonObject()
                .put("fbid", this.getId())
                .put("event_type", this.getType())
                .put("event_data", this.getJsonData());
    }

    @Override
    public String toString()
    {
        return "EventData{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", jsonData=" + jsonData +
                '}';
    }
}
