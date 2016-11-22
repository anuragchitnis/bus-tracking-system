package edu.unt.transportation.bustrackingsystem.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This class tracks the changes to the vehicle map of the given route
 * Created by Anurag Chitnis on 11/17/2016.
 */

public class VehicleMapChangeReceiver implements ChildEventListener {
    private String TAG = VehicleMapChangeReceiver.class.getName();
    private static final String FIREBASE_VEHICLES = "vehicles";
    private DatabaseReference vehicleMapRef;
    private VehicleReceiver vehicleReceiver;
    private VehicleChangeListener vehicleChangeListener;
    private DatabaseReference mDatabase;
    /**
     * Reference of the 'vehicles' node, where we have all the vehicle objects located
     */
    private DatabaseReference vehiclesRef;

    public VehicleMapChangeReceiver() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        vehiclesRef = mDatabase.child(FIREBASE_VEHICLES);
        vehicleReceiver = new VehicleReceiver();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s)
    {
        Log.d(TAG, "onChildAdded " + dataSnapshot.getKey());
        //Register to listen to the changes made to all the vehicles on this route
        vehicleReceiver.registerListener(vehicleChangeListener, dataSnapshot.getKey());
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s)
    {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot)
    {
        //Remove the listener for this bus as we don't want to receive updates from it anymore
        vehiclesRef.child(dataSnapshot.getKey()).removeEventListener(vehicleReceiver);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s)
    {
        Log.w(TAG, "VehicleMapChangeReceiver : onChildMoved() " + s);
    }

    @Override
    public void onCancelled(DatabaseError databaseError)
    {
        Log.e(TAG, "VehicleMapChangeReceiver : onCancelled() " + databaseError.getMessage());
    }

    /**
     * Register for receiving the callbacks for all the vehicles on the specified route
     * @param vehicleChangeListener This listener will receive callbacks whenever a new child is added
     * @param routeID route, whose vehicles you want to keep track of
     */
    public void registerListener(VehicleChangeListener vehicleChangeListener, String routeID)
    {
        this.vehicleChangeListener = vehicleChangeListener;
        vehicleMapRef = mDatabase.child("routes/" + routeID + "/vehicleMap");
        vehicleMapRef.addChildEventListener(this);
    }

    public void removeListener(VehicleChangeListener vehicleChangeListener) {
        vehicleMapRef.removeEventListener(vehicleReceiver);
        vehicleReceiver.removeListener(vehicleChangeListener);
    }
}
