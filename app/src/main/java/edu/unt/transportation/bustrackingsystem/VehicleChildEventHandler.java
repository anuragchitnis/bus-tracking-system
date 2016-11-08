package edu.unt.transportation.bustrackingsystem;

import com.firebase.client.FirebaseError;

/**
 * Created by Satyanarayana on 11/8/2016.
 */
public class VehicleChildEventHandler implements com.firebase.client.ChildEventListener {
    @Override
    public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
        SignInActivity.list1.add(dataSnapshot.getKey());
    }

    @Override
    public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
