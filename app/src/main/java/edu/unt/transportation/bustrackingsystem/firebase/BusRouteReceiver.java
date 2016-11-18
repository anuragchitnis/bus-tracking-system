package edu.unt.transportation.bustrackingsystem.firebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;

import edu.unt.transportation.bustrackingsystem.model.BusRoute;

/**
 * Created by Anurag Chitnis on 11/17/2016.
 */

public class BusRouteReceiver implements ChildEventListener {

    private String TAG = BusRouteReceiver.class.getName();
    DatabaseReference busRouteReference;
    private DatabaseReference mDatabase;
    List<BusRouteListener> busRouteListenerList;

    public void registerListener(BusRouteListener busRouteListener)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        busRouteListenerList = new LinkedList<>();
        busRouteListenerList.add(busRouteListener);
        busRouteReference = mDatabase.child("routes");
        busRouteReference.addChildEventListener(this);
    }

    public void unregisterListener(BusStopListener busStopListener) {
        busRouteListenerList.remove(busStopListener);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        BusRoute busRoute = dataSnapshot.getValue(BusRoute.class);         //Bind the snapshot to an
        performCallBacks(busRoute);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void performCallBacks(BusRoute busRoute) {
        for (BusRouteListener busRouteListener : busRouteListenerList) {
            busRouteListener.onBusRouteAdded(busRoute);
        }
    }
}
