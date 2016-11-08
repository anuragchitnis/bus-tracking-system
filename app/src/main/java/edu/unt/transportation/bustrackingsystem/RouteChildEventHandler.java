package edu.unt.transportation.bustrackingsystem;

import android.widget.ArrayAdapter;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

import edu.unt.transportation.bustrackingsystem.model.BusRoute;

/**
 * Created by Satyanarayana on 11/8/2016.
 */
public class RouteChildEventHandler implements com.firebase.client.ChildEventListener {

    private ArrayAdapter adapter;

    public RouteChildEventHandler(ArrayAdapter adapterParm){
        adapter = adapterParm;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        SignInActivity.routes.put(dataSnapshot.getKey(), dataSnapshot.getValue(BusRoute.class));
        adapter.add(dataSnapshot.getKey());
        adapter.notifyDataSetChanged();
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
    public void onCancelled(FirebaseError firebaseError) {

    }
}
