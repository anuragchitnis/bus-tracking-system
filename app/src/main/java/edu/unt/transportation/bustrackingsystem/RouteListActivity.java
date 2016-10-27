package edu.unt.transportation.bustrackingsystem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import edu.unt.transportation.bustrackingsystem.model.BusRoute;
import edu.unt.transportation.bustrackingsystem.model.BusStop;
import edu.unt.transportation.bustrackingsystem.model.Vehicle;

/**
 * Created by gdawg on 10/15/2016.
 */
public class RouteListActivity extends AppCompatActivity implements AdapterView
        .OnItemSelectedListener, ValueEventListener, AdapterView.OnItemClickListener
{
    private static final String ROOT_ROUTE = "routes";
    private ListView routeList;
    private RouteAdapter routeAdapter;
    private FirebaseController firebaseController;
    private HashMap<Integer, BusRoute> busRoute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(FirebaseController.KEY_FIREBASE_CONTROLLER))
        {
            firebaseController = (FirebaseController) extras.getSerializable(FirebaseController.KEY_FIREBASE_CONTROLLER);
        }
        else
        {
            Log.e("NoFirebaseKey", "Firebase Controller Reference Was No Passed to Activity Bundle");
        }
        firebaseController.getFirebaseInstance().addValueEventListener(this);
        setContentView(R.layout.activity_routes);
        routeList = (ListView) findViewById(R.id.list_routes);
        routeAdapter = new RouteAdapter(RouteListActivity.this, R.layout.row_template_routes);
        routeList.setAdapter(routeAdapter);
        routeList.setOnItemSelectedListener(this);
        routeList.setOnItemClickListener(this);

    }

    @Override
    protected void onDestroy()
    {
        firebaseController.getFirebaseInstance().removeEventListener(this);
        super.onDestroy();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot)
    {
        try
        {
            for (DataSnapshot snapshot : dataSnapshot.child(ROOT_ROUTE).getChildren())
            {
                BusRoute b = snapshot.getValue(BusRoute.class);
                getBusRoutes().put(b.getId(), b);
            }
            routeAdapter.clear();
            routeAdapter.addAll(getBusRoutes().values());
            routeAdapter.notifyDataSetChanged();
        } catch (Exception e)
        {
            Log.e("onDataChange", "Error loading routes", e);
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError)
    {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        Bundle b = new Bundle();
        BusRoute route = routeAdapter.getItem(position);
        ArrayList<LatLng> stopLocations = new ArrayList<>();
        ArrayList<LatLng> vehicleLocations = new ArrayList<>();
        if (route.getStops() != null && route.getStops().size() > 0)
        {
            for (BusStop busStop : route.getStops())
            {
                stopLocations.add(new LatLng(busStop.getmLatitude(), busStop.getmLongitude()));
            }
            b.putSerializable(GoogleMapWithMarker.ARG_LOCATIONS, stopLocations);
        }
        if (route.getVehicles() != null && route.getVehicles().size() > 0)
        {
            for(Vehicle v : route.getVehicles()){
                vehicleLocations.add(new LatLng(v.getmLatitude(),v.getmLongitude()));
            }
            b.putSerializable(GoogleMapWithMarker.ARG_VEHICLES,vehicleLocations);
        }
        ActivityUtil.showScreen(RouteListActivity.this, GoogleMapWithMarker.class, b);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    private HashMap<Integer, BusRoute> getBusRoutes()
    {
        if (busRoute == null)
        {
            busRoute = new HashMap<>();
        }
        return busRoute;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        onItemSelected(parent,view,position,id);
    }
}
