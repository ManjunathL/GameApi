package com.mygubbi.si.firebase;

import com.firebase.client.Firebase;

/**
 * Created by test on 02-01-2016.
 */
public class FirebaseReader
{
    public FirebaseReader()
    {
        Firebase myFirebaseRef = new Firebase("https://<YOUR-FIREBASE-APP>.firebaseio.com/");
    }
}
