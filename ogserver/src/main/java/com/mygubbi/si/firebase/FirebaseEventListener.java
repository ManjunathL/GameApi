package com.mygubbi.si.firebase;

import com.google.firebase.database.*;
import com.mygubbi.si.data.DataProcessor;
import com.mygubbi.si.data.EventAcknowledger;
import com.mygubbi.si.data.EventData;
import org.apache.log4j.LogManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by test on 06-01-2016.
 */
public class FirebaseEventListener implements ChildEventListener, ValueEventListener
{
    private final static org.apache.log4j.Logger LOG = LogManager.getLogger(FirebaseEventListener.class);

    private DatabaseReference fbRef;
    private Map<String, DataProcessor> processorMap = new HashMap<>();
    private EventAcknowledger acknowledger;

    public FirebaseEventListener(DatabaseReference fbRef, EventAcknowledger acknowledger)
    {
        this.fbRef = fbRef;
        this.acknowledger = acknowledger;
        LOG.info("Listening for data changes at " + fbRef.toString());
    }

    public void register(DataProcessor processor)
    {
        this.processorMap.put(processor.getName(), processor);
    }

    public void start()
    {
        this.fbRef.addListenerForSingleValueEvent(this);
        this.fbRef.addChildEventListener(this);
    }

    public void stop()
    {
        this.fbRef.removeEventListener((ChildEventListener) this);
        this.fbRef.removeEventListener((ValueEventListener) this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s)
    {
        this.processEvent(dataSnapshot);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s)
    {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot)
    {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s)
    {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    //This event is called just once when the listener is attached to retrieve all the events existing at that time.
    @Override
    public void onDataChange(DataSnapshot snapshot)
    {
        for (DataSnapshot nodeData: snapshot.getChildren())
        {
            this.processEvent(nodeData);
        }
    }

    private void processEvent(DataSnapshot nodeData)
    {
        LOG.info("Node:" + nodeData.toString());
        EventData data = this.toEventData(nodeData);
        if (data == null)
        {
            this.acknowledger.failed(nodeData.getKey(), nodeData.toString(), "Unable to decipher event data.");
            return;
        }

        LOG.info("EventData:" + data.toString());

        if (this.processorMap.containsKey(data.getType()))
        {
            this.processorMap.get(data.getType()).process(data);
        }
        else
        {
            this.acknowledger.failed(data, "Event type not setup. " + data.getType());
        }
    }

    private EventData toEventData(DataSnapshot nodeData)
    {
        EventData eventData = null;
        try
        {
            for (DataSnapshot level1Data : nodeData.getChildren())
            {
                eventData = level1Data.getValue(EventData.class);
                eventData.setUid(level1Data.getKey());
                break;
            }
            if (eventData != null) eventData.setId(nodeData.getKey());
        }
        catch (Exception e)
        {
            LOG.error("Unable to extract EventData out of nodeData:" + nodeData.toString() + ". Error message:" + e.getMessage());
        }
        return eventData;
    }


}
