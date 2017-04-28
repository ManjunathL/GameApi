package com.mygubbi.si.firebase;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by test on 22-01-2016.
 */
public class FirebaseObjectMapper
{
    private final static Logger LOG = LogManager.getLogger(FirebaseObjectMapper.class);

    public void fromJsonToFirebase(Firebase ref, JsonObject json)
    {
        Map<String, Object> objectMap = json.getMap();
        for (String key : objectMap.keySet())
        {
            Object value = objectMap.get(key);
            if (value instanceof JsonObject)
            {
                this.fromJsonToFirebase(ref, (JsonObject) value);
            }
            else if (value instanceof JsonArray)
            {
                this.fromJsonToFirebase(ref, key, (JsonArray) value);
            }
            else
            {
                ref.child(key).setValue(value);
            }
        }
    }

    public void fromJsonToFirebase(DatabaseReference ref, JsonObject json)
    {
        Map<String, Object> objectMap = json.getMap();
        for (String key : objectMap.keySet())
        {
            Object value = objectMap.get(key);
            if (value instanceof JsonObject)
            {
                this.fromJsonToFirebase(ref, (JsonObject) value);
            }
            else if (value instanceof JsonArray)
            {
                this.fromJsonToFirebase(ref, key, (JsonArray) value);
            }
            else
            {
                ref.child(key).setValue(value);
                System.out.println("Setting value at :" + ref + " : " + key + " : " + value);
            }
        }
    }

    public void fromJsonToFirebase(Firebase ref, String key, JsonArray array)
    {
        int index = 0;
        for (Object value : array)
        {
            Firebase nodeRef = ref.child(key).child(String.valueOf(index));
            if (value instanceof JsonObject)
            {
                this.fromJsonToFirebase(nodeRef, (JsonObject) value);
            }
            else if (value instanceof JsonArray)
            {
                this.fromJsonToFirebase(nodeRef, "0", (JsonArray) value);
            }
            else
            {
                nodeRef.setValue(value);
            }
            index++;
        }
    }

    public void fromJsonToFirebase(DatabaseReference ref, String key, JsonArray array)
    {
        int index = 0;
        for (Object value : array)
        {
            DatabaseReference nodeRef = ref.child(key).child(String.valueOf(index));
            if (value instanceof JsonObject)
            {
                this.fromJsonToFirebase(nodeRef, (JsonObject) value);
            }
            else if (value instanceof JsonArray)
            {
                this.fromJsonToFirebase(nodeRef, "0", (JsonArray) value);
            }
            else
            {
                nodeRef.setValue(value);
            }
            index++;
        }
    }
}
