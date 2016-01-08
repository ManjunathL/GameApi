package com.mygubbi.si.firebase;

import com.firebase.client.*;
import com.mygubbi.si.data.DataProcessor;

/**
 * Created by test on 06-01-2016.
 */
public class FirebaseEventListener implements ChildEventListener, ValueEventListener
{
    private Firebase fbRef;
    private DataProcessor processor;

    public FirebaseEventListener(String url, DataProcessor processor)
    {
        this.fbRef = new Firebase(url);
        this.fbRef.addListenerForSingleValueEvent(this);
        this.fbRef.addChildEventListener(this);
        this.processor = processor;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s)
    {
        dataSnapshot
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

    //This event is called just once when the listener is attached to retrieve all the events existing at that time.
    @Override
    public void onDataChange(DataSnapshot snapshot)
    {
        for (DataSnapshot postSnapshot: snapshot.getChildren())
        {
            BlogPost post = postSnapshot.getValue(BlogPost.class);
            System.out.println(post.getAuthor() + " - " + post.getTitle());

        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError)
    {

    }
}
