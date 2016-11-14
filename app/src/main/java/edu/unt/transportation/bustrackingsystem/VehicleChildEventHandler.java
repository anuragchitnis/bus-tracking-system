package edu.unt.transportation.bustrackingsystem;

import android.widget.ArrayAdapter;

import com.firebase.client.FirebaseError;

import edu.unt.transportation.bustrackingsystem.model.Vehicle;

/**
 * Created by Satyanarayana on 11/8/2016.
 */
public class VehicleChildEventHandler implements com.firebase.client.ChildEventListener
{

    private ArrayAdapter adapter;

    public VehicleChildEventHandler(ArrayAdapter adapterParm)
    {
        adapter = adapterParm;
    }

    @Override
    public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s)
    {
        SignInActivity.vehicles.put(dataSnapshot.getKey(), dataSnapshot.getValue(Vehicle.class));
        adapter.add(dataSnapshot.getKey());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s)
    {

    }

    @Override
    public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot)
    {

    }

    @Override
    public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s)
    {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError)
    {

    }
}
