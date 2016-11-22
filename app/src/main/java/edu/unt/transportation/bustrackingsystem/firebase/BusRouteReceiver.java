package edu.unt.transportation.bustrackingsystem.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;

import edu.unt.transportation.bustrackingsystem.model.BusRoute;

/**
 * This class registers for the available routes on the firebase realtime database with BusRouteListener.
 * implementers of BusRouteListener gets the call back in onBusRouteAdded() method
 * Created by Anurag Chitnis on 11/17/2016.
 */

public class BusRouteReceiver implements ChildEventListener {

    private String TAG = BusRouteReceiver.class.getName();
    private DatabaseReference busRouteReference;
    private DatabaseReference mDatabase;
    private List<BusRouteListener> busRouteListenerList;
    private static final String FIREBASE_ROUTES = "routes";

    public void registerListener(BusRouteListener busRouteListener)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        busRouteListenerList = new LinkedList<>();
        busRouteListenerList.add(busRouteListener);
        busRouteReference = mDatabase.child(FIREBASE_ROUTES);
        busRouteReference.addChildEventListener(this);
    }

    public void removeListener(BusRouteListener busRouteListener) {
        busRouteListenerList.remove(busRouteListener);
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
        Log.e(TAG, "BusStopListener : onCancelled() " + databaseError.getMessage());
    }

    private void performCallBacks(BusRoute busRoute) {
        for (BusRouteListener busRouteListener : busRouteListenerList) {
            busRouteListener.onBusRouteAdded(busRoute);
        }
    }
}
