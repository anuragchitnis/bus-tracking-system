package edu.unt.transportation.bustrackingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.HashMap;

import edu.unt.transportation.bustrackingsystem.model.BusRoute;
import edu.unt.transportation.bustrackingsystem.model.BusStop;

import static edu.unt.transportation.bustrackingsystem.TrackerMapActivity.KEY_ROUTE_ID;

/**
 * Created by Gil Wasserman on 10/15/2016.
 * This is the main RouteList Activity.  The view will show the user a list of all Bus Routes
 * currently available
 * and allow them to select a route to view it on TrackerMapActivity.
 *
 * This is the main launcher class for the app, so it will also contain a Driver Sign-In option
 * item to allow
 * navigating to the Driver Sign-In screen where the driver will be assigned a bus and route
 */

public class RouteListActivity extends AppCompatActivity implements AdapterView
        .OnItemSelectedListener, AdapterView.OnItemClickListener, Serializable,
        ValueEventListener, ChildEventListener
{
    //constant for root node used in this activity
    private static final String ROOT_ROUTE = "routes";

    private ListView routeList; //ListView showing list of routes in UI
    private RouteAdapter routeAdapter;  //Adapter of routes to be linked with routeList
    private HashMap<String, BusRoute> busRoute; //Map of RouteIDs to BusRoute objects
    private BusRoute selectedRoute; //The bus route selected by the user
    private DatabaseReference mDatabase;  //reference to the root node of the Firebase database
    private DatabaseReference routeRoot;  //reference to the root node of the routes in the
    // Firebase database

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        // Set up toolbar to be used in activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Sets the context of the Firebase database to the current activity
        Firebase.setAndroidContext(this);
        //Initialize mDatabase value and add activity as ChildEvent Listener
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addChildEventListener(this);
        //Iniitalize routeRoot value and add activity as ValueEvent listener
        routeRoot = mDatabase.child(ROOT_ROUTE);
        routeRoot.addValueEventListener(this);
        //Initialize routeList component with routeAdapter and set current class listeners
        routeList = (ListView) findViewById(R.id.list_routes);
        routeAdapter = new RouteAdapter(RouteListActivity.this, R.layout.row_template_routes);
        routeList.setAdapter(routeAdapter);
        routeList.setOnItemSelectedListener(this);
        routeList.setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        routeRoot.removeEventListener((ChildEventListener) this);   //remove the event listener
        // from routeRoot now that we're done with the activity
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        onItemSelected(parent, view, position, id);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        BusRoute route = routeAdapter.getItem(position);    //gets current BusRoute object from list
        if (route == null) return;  //unexpected error case, do nothing
        setSelectedRoute(route);    //mark the route as the current selected route
        navigateToMap();            //Navigate to the Tracker Map activity
    }

    //empty override method, nothing to do here at the moment
    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot)
    {
        try
        {
            setUpRoutes(dataSnapshot.child(ROOT_ROUTE));    //call method to set up routes when
            // the Firebase data has changed
        } catch (Exception e)
        {
            Log.e("onDataChange", "Error loading routes", e);
        }
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s)
    {
        if (dataSnapshot.getKey().equals(ROOT_ROUTE))
        {
            setUpRoutes(dataSnapshot);  //call method to set up routes when the Firebase route
            // data has a new route added to it
        }
    }

    //empty override method, nothing to do here at the moment
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s)
    {
        if (dataSnapshot.getKey().equals(ROOT_ROUTE))
        {
            setUpRoutes(dataSnapshot); //call method to set up routes when the Firebase route
            // data is changed
        }
    }

    //empty override method, nothing to do here at the moment
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot)
    {
    }

    //empty override method, nothing to do here at the moment
    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s)
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_signIn:
                startActivity(new Intent(this, SignInActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    //getter and setter method for selectedRoute
    public BusRoute getSelectedRoute()
    {
        return selectedRoute;
    }

    public void setSelectedRoute(BusRoute selectedRoute)
    {
        this.selectedRoute = selectedRoute;
    }

    //getter method for BusRoute Map
    private HashMap<String, BusRoute> getBusRoutes()
    {
        if (busRoute == null)
        {
            busRoute = new HashMap<>();
        }
        return busRoute;
    }

    //Create an intent and start the TrackerMapActivity
    //This should pass the currently selected route ID to the TrackerMapActivity
    private void navigateToMap()
    {
        Intent intent = new Intent(RouteListActivity.this, TrackerMapActivity.class);
        intent.putExtra(KEY_ROUTE_ID, getSelectedRoute().getRouteId());
        startActivity(intent);
    }

    //Set up Routes takes a datasnapshot as a parameter.  The parameter datasnapshot should be
    // the root of the BusRoute node
    //setUpRoutes does the work of setting up the data within the routeAdapter
    private void setUpRoutes(DataSnapshot dataSnapshot)
    {
        for (DataSnapshot snapshot : dataSnapshot.getChildren())    //For each BusRoute node
        {
            BusRoute b = snapshot.getValue(BusRoute.class);         //Bind the snapshot to an
            // instance of BusRoute
            getBusRoutes().put(b.getRouteId(), b);                  //Add BusRoute object to HashMap
            if (b.getBusStopMap() != null)
            {
                for (String id : b.getBusStopMap().keySet())    //For each bus stop, register a
                // listener for that BusStop ID
                {
                    BusStopListener busStopListener = new BusStopListener(b.getRouteId());
                    busStopListener.registerListener(id);
                }
            }
        }
        //Clear the current routeAdapter values, add the current set of BusRoute values, and
        // notify the adapter that the underlying data has changed
        routeAdapter.clear();
        routeAdapter.addAll(getBusRoutes().values());
        routeAdapter.notifyDataSetChanged();
        //If we're viewing the map activity, navigate to the map with the new route data
        if (BusTrackingSystem.isMapActivityVisible())
        {
            navigateToMap();
        }
    }

    //Implementation of ValueEventLister
    private class BusStopListener implements ValueEventListener
    {

        private final String routeID;           //The Bus Stop's associated Route ID
        DatabaseReference busStopReference;     //DatabaseReference to a specific bus stop

        public BusStopListener(String routeId)
        {
            this.routeID = routeId;
        }

        //Registers a BusStop listener for any changes to a single BusStop
        //parameter stopID is the key for the bus stop registered
        public void registerListener(String stopID)
        {
            busStopReference = mDatabase.child("stops");
            busStopReference.child(stopID).addListenerForSingleValueEvent(this);
        }        //When the BusStop data has changed, rebuild the BusRoutes map and notify the
        // adapter

        // that changes were made
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            BusStop busStopSnapShot = dataSnapshot.getValue(BusStop.class); //Get an instance of
            // BusStop from the dataSnapshot
            if (getBusRoutes().containsKey(routeID))    //The bus routes listed reference a bus
            // route using this stop
            {
                //Get the BusRoute by routeID, and add this BusStop, using its StopID as a key,
                // to the BusStopObjectMap
                //The BusStopObjectMap will be used in the RouteAdapter
                getBusRoutes().get(routeID).getBusStopObjectMap().put(busStopSnapShot.getStopID()
                        , busStopSnapShot);
                routeAdapter.notifyDataSetChanged();

            }
        }

        //Empty override method, nothing to do here at the moment
        @Override
        public void onCancelled(DatabaseError databaseError)
        {

        }


    }

    @Override
    public void onCancelled(DatabaseError databaseError)
    {

    }


}
