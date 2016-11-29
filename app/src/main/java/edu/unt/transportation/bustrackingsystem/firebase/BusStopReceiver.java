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
 * Best use of this class can be made by registering for all the bus stops existing in busStopMap of each route.<br>
 * Created by Anurag Chitnis on 11/17/2016.
 */

public class BusStopReceiver implements ValueEventListener{

    private String TAG = BusStopReceiver.class.getName();
    private DatabaseReference busStopReference;
    private DatabaseReference mDatabase;
    private List<BusStopListener> busStopListenerList;

    public BusStopReceiver() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        busStopListenerList = new LinkedList<>();
    }

    /**
     * Register to listen for the BusStop with the stopId.
     * @param busStopListener implementer of this listener will receive callbacks
     * @param stopID stopId existing in the firebase
     */
    public void registerListener(BusStopListener busStopListener, String stopID)
    {
        busStopListenerList.add(busStopListener);
        busStopReference = mDatabase.child("stops");
        busStopReference.child(stopID).addListenerForSingleValueEvent(this);
    }

    public void removeListener(BusStopListener busStopListener) {
        busStopListenerList.remove(busStopListener);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot)
    {
        BusStop busStopSnapShot = dataSnapshot.getValue(BusStop.class);
        if(busStopSnapShot != null)
            performCallBacks(busStopSnapShot);
    }

    @Override
    public void onCancelled(DatabaseError databaseError)
    {
        Log.e(TAG, "BusStopListener : onCancelled() " + databaseError.getMessage());
    }

    /**
     * Perform callbacks to all the listeners
     * @param busStop
     */
    private void performCallBacks(BusStop busStop) {
        for(BusStopListener busStopListener : busStopListenerList) {
            busStopListener.onBusStopAdded(busStop);
        }
    }
}
