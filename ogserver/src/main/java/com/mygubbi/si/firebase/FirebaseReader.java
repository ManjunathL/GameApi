package com.mygubbi.si.firebase;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by test on 02-01-2016.
 */
public class FirebaseReader
{
    private Firebase fbRef;

    public FirebaseReader()
    {
        this.fbRef = new Firebase("https://sweltering-fire-6356.firebaseio.com/user-profiles");
    }

    public void start()
    {
        this.fbRef.addChildEventListener();
    }

    public static void main(String[] args) throws Exception
    {
        new FirebaseReader().start();
        Thread.sleep(120000);
    }
}
