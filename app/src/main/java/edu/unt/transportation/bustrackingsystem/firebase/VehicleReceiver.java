package edu.unt.transportation.bustrackingsystem.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

import edu.unt.transportation.bustrackingsystem.model.Vehicle;

/**
 * Created by Anurag Chitnis on 11/21/2016.
 */

public class VehicleReceiver implements ValueEventListener{

    private String TAG = VehicleReceiver.class.getName();
    private DatabaseReference vehicleReference;
    private DatabaseReference mDatabase;
    private List<VehicleChangeListener> vehicleChangeListenerList;

    public VehicleReceiver() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        vehicleChangeListenerList = new LinkedList<>();
    }

    /**
     * Register to listen for the BusStop with the stopId.
     * @param vehicleChangeListener implementer of this listener will receive callbacks
     * @param vehicleId vehicleId existing in the firebase
     */
    public void registerListener(VehicleChangeListener vehicleChangeListener, String vehicleId)
    {
        vehicleChangeListenerList.add(vehicleChangeListener);
        vehicleReference = mDatabase.child("vehicles");
        vehicleReference.child(vehicleId).addValueEventListener(this);
    }

    public void removeListener(VehicleChangeListener vehicleChangeListener) {
        vehicleChangeListenerList.remove(vehicleChangeListener);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot)
    {
        Vehicle vehicleSnapShot = dataSnapshot.getValue(Vehicle.class);
        performCallBacks(vehicleSnapShot);
    }

    @Override
    public void onCancelled(DatabaseError databaseError)
    {
        Log.e(TAG, "VehicleReceiver : onCancelled() " + databaseError.getMessage());
    }

    /**
     * Perform callbacks to all the listeners
     * @param vehicle
     */
    private void performCallBacks(Vehicle vehicle) {
        for(VehicleChangeListener busStopListener : vehicleChangeListenerList) {
            busStopListener.onVehicleChanged(vehicle);
        }
    }
}
