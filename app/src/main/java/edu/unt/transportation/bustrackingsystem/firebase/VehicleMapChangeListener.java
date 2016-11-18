package edu.unt.transportation.bustrackingsystem.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This class tracks the changes to the vehicle map of the given route
 * Created by Anurag Chitnis on 11/17/2016.
 */

public class VehicleMapChangeListener implements ChildEventListener{
    private String TAG = VehicleMapChangeListener.class.getName();
    private static final String FIREBASE_VEHICLES = "vehicles";
    DatabaseReference vehicleMapRef;
    private ValueEventListener valueEventListener;
    private DatabaseReference mDatabase;
    /**
     * Reference of the 'vehicles' node, where we have all the vehicle objects located
     */
    private DatabaseReference vehiclesRef;

    public VehicleMapChangeListener() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        vehiclesRef = mDatabase.child(FIREBASE_VEHICLES);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s)
    {
        Log.d(TAG, "onChildAdded " + dataSnapshot.getKey());
        //Register to listen to the changes made to all the vehicles on this route
        vehiclesRef.child(dataSnapshot.getKey()).addValueEventListener(valueEventListener);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s)
    {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot)
    {
        //Remove the listener for this bus as we don't want to receive updates from it anymore
        vehiclesRef.child(dataSnapshot.getKey()).removeEventListener(valueEventListener);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s)
    {

    }

    @Override
    public void onCancelled(DatabaseError databaseError)
    {
        Log.e(TAG, "VehicleMapChangeListener : onCancelled() " + databaseError.getMessage());
    }

    public void registerListener(ValueEventListener valueEventListener, String routeID)
    {
        this.valueEventListener = valueEventListener;
        vehicleMapRef = mDatabase.child("routes/" + routeID + "/vehicleMap");
        vehicleMapRef.addChildEventListener(this);
    }

    public void unregisterListener(ValueEventListener valueEventListener) {
        vehicleMapRef.removeEventListener(valueEventListener);
    }
}
