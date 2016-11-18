package edu.unt.transportation.bustrackingsystem.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

import edu.unt.transportation.bustrackingsystem.model.BusStop;
/**
 * This class receives the callback for all the BusStops which exist on the currently
 * selected route.
 * Let us  keep the list of BusStop objects, so they can be displayed on the UI whenever user
 * requests it
 * Created by Anurag Chitnis on 11/17/2016.
 */

public class BusStopReceiver implements ValueEventListener{

    private String TAG = BusStopReceiver.class.getName();
    DatabaseReference busStopReference;
    private DatabaseReference mDatabase;
    List<BusStopListener> busStopListenerList;

    public BusStopReceiver() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        busStopListenerList = new LinkedList<>();
    }

    public void registerListener(BusStopListener busStopListener, String stopID)
    {
        busStopListenerList.add(busStopListener);
        busStopReference = mDatabase.child("stops");
        busStopReference.child(stopID).addListenerForSingleValueEvent(this);
    }

    public void unregisterListener(BusStopListener busStopListener) {
        busStopListenerList.remove(busStopListener);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot)
    {
        BusStop busStopSnapShot = dataSnapshot.getValue(BusStop.class);
        performCallBacks(busStopSnapShot);
    }

    @Override
    public void onCancelled(DatabaseError databaseError)
    {
        Log.e(TAG, "BusStopListener : onCancelled() " + databaseError.getMessage());
    }

    private void performCallBacks(BusStop busStop) {
        for(BusStopListener busStopListener : busStopListenerList) {
            busStopListener.onBusStopAdded(busStop);
        }
    }
}
