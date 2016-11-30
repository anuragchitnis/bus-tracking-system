package edu.unt.transportation.bustrackingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.unt.transportation.bustrackingsystem.firebase.BusRouteListener;
import edu.unt.transportation.bustrackingsystem.firebase.BusRouteReceiver;
import edu.unt.transportation.bustrackingsystem.firebase.BusStopListener;
import edu.unt.transportation.bustrackingsystem.firebase.BusStopReceiver;
import edu.unt.transportation.bustrackingsystem.model.BusRoute;
import edu.unt.transportation.bustrackingsystem.model.BusStop;
import edu.unt.transportation.bustrackingsystem.util.GeneralUtil;

import static edu.unt.transportation.bustrackingsystem.TrackerMapActivity.KEY_ROUTE_ID;
import static edu.unt.transportation.bustrackingsystem.TrackerMapActivity.KEY_ROUTE_NAME;

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
        .OnItemSelectedListener, AdapterView.OnItemClickListener, Serializable, BusStopListener, BusRouteListener
{
    //constant for root node used in this activity
    private static final String ROOT_ROUTE = "routes";

    private ListView routeList; //ListView showing list of routes in UI
    private RouteAdapter routeAdapter;  //Adapter of routes to be linked with routeList
    private Map<String, BusRoute> busRouteMap; //Map of RouteIDs to BusRoute objects
    private BusRoute selectedRoute; //The bus route selected by the user
    private DatabaseReference mDatabase;  //reference to the root node of the Firebase database
    // Firebase database

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        setTitle(R.string.title_route_list);
        // Set up toolbar to be used in activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Sets the context of the Firebase database to the current activity
        Firebase.setAndroidContext(this);
        busRouteMap = new HashMap<>();
        //Initialize mDatabase value and add activity as ChildEvent Listener
        mDatabase = FirebaseDatabase.getInstance().getReference();
        BusRouteReceiver busRouteReceiver = new BusRouteReceiver();
        busRouteReceiver.registerListener(this);
        //Initialize routeList component with routeAdapter and set current class listeners
        routeList = (ListView) findViewById(R.id.list_routes);
        routeAdapter = new RouteAdapter(RouteListActivity.this, R.layout.row_template_routes);
        routeList.setAdapter(routeAdapter);
        routeList.setOnItemSelectedListener(this);
        routeList.setOnItemClickListener(this);
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

            case R.id.action_aboutUs:
                startActivity(new Intent(this, About_Us.class));
                return true;

            case R.id.action_likeUs:
                GeneralUtil.OpenFacebookPage(this);
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

    //Create an intent and start the TrackerMapActivity
    //This should pass the currently selected route ID to the TrackerMapActivity
    private void navigateToMap()
    {
        Intent intent = new Intent(RouteListActivity.this, TrackerMapActivity.class);
        intent.putExtra(KEY_ROUTE_ID, getSelectedRoute().getRouteId());
        intent.putExtra(KEY_ROUTE_NAME, getSelectedRoute().getRouteName());
        startActivity(intent);
    }

    @Override
    public void onBusStopAdded(BusStop busStop) {
        //Check if the bus stop is scheduled or not, StopSchedule List Map will be null
        // if it is unscheduled
        if(busStop.getRouteSchedule() != null) {
            //Iterate through all the routes for that stop
            for(String busRouteKey : busStop.getRouteSchedule().keySet()) {
                //Add the bus stop to the map to keep track of it
                busRouteMap.get(busRouteKey).getBusStopObjectMap().put(busStop.getStopID()
                        , busStop);
                routeAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onBusRouteAdded(BusRoute busRoute) {
        busRouteMap.put(busRoute.getRouteId(), busRoute);                  //Add BusRoute object to HashMap
        if (busRoute.getBusStopMap() != null)
        {
            for (String busStopId : busRoute.getBusStopMap().keySet())    //For each bus stop, register a
            // listener for that BusStop ID
            {
                BusStopReceiver busStopReceiver = new BusStopReceiver();
                busStopReceiver.registerListener(this, busStopId);
            }
        }

        //Clear the current routeAdapter values, add the current set of BusRoute values, and
        // notify the adapter that the underlying data has changed
        routeAdapter.add(busRoute);
        routeAdapter.notifyDataSetChanged();
    }
}
