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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import edu.unt.transportation.bustrackingsystem.model.BusRoute;
import edu.unt.transportation.bustrackingsystem.model.BusStop;
import edu.unt.transportation.bustrackingsystem.model.Vehicle;

/**
 * Created by gdawg on 10/15/2016.
 */
public class RouteListActivity extends AppCompatActivity implements AdapterView
        .OnItemSelectedListener, ValueEventListener, AdapterView.OnItemClickListener, Serializable {
    private static final String ROOT_ROUTE = "routes";
    private ListView routeList;
    private RouteAdapter routeAdapter;
    private FirebaseController firebaseController;
    private HashMap<String, BusRoute> busRoute;
    private BusRoute selectedRoute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(FirebaseController.KEY_FIREBASE_CONTROLLER)) {
            firebaseController = (FirebaseController) extras.getSerializable(FirebaseController.KEY_FIREBASE_CONTROLLER);
        } else {
            firebaseController = new FirebaseController(RouteListActivity.this);
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
    protected void onDestroy() {
        firebaseController.getFirebaseInstance().removeEventListener(this);
        super.onDestroy();
    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        try {
            for (DataSnapshot snapshot : dataSnapshot.child(ROOT_ROUTE).getChildren()) {
                BusRoute b = snapshot.getValue(BusRoute.class);
                for (String id : b.getBusStopMap().keySet()) {
                    BusStop value = dataSnapshot.child(FirebaseController.FIREBASE_STOPS).child(id).getValue(BusStop.class);
                    if (value != null) {
                        b.putBusStop(id, value);
                    }
                }
                for (String id : b.getVehicleMap().keySet()) {
                    Vehicle value = dataSnapshot.child(FirebaseController.FIREBASE_VEHICLES).child(id).getValue(Vehicle
                            .class);
                    if (value != null) {
                        b.putVehicle(id, value);
                    }
                }
                getBusRoutes().put(b.getRouteId(), b);
            }
            routeAdapter.clear();
            routeAdapter.addAll(getBusRoutes().values());
            routeAdapter.notifyDataSetChanged();
            if (BusTrackingSystem.isMapActivityVisible()) {
                navigateToMap();
            }
        } catch (Exception e) {
            Log.e("onDataChange", "Error loading routes", e);
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onItemSelected(parent, view, position, id);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        BusRoute route = routeAdapter.getItem(position);
        if (route == null) return;
        setSelectedRoute(route);
        navigateToMap();
    }

    private void navigateToMap() {
        Bundle b = new Bundle();
//        ArrayList<LatLng> stopLocations = new ArrayList<>();
//        ArrayList<LatLng> vehicleLocations = new ArrayList<>();
/*
        if (getSelectedRoute().getBusStopObjectMap() != null && getSelectedRoute().getBusStopObjectMap().size() > 0) {
           /* for (BusStop busStop : getSelectedRoute().getBusStopObjectMap().values()) {
                stopLocations.add(new LatLng(busStop.getLatitude(), busStop.getLongitude()));
            }
            */
        /*
            b.putSerializable(GoogleMapWithMarker.ARG_LOCATIONS, getSelectedRoute().getBusStopObjectMap());
        }
        if (getSelectedRoute().getVehicleObjectMap() != null && getSelectedRoute().getVehicleObjectMap().size() > 0) {

            b.putSerializable(GoogleMapWithMarker.ARG_VEHICLES, getSelectedRoute().getVehicleObjectMap());
        }*/
        b.putSerializable(GoogleMapWithMarker.ARG_CURRENT_ROUTE,getSelectedRoute());
        firebaseController.getFirebaseInstance().removeEventListener(this);
        ActivityUtil.showScreen(RouteListActivity.this, GoogleMapWithMarker.class, b);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private HashMap<String, BusRoute> getBusRoutes() {
        if (busRoute == null) {
            busRoute = new HashMap<>();
        }
        return busRoute;
    }

    public ArrayList<BusStop> getBusStops() {
        return new ArrayList<>();
    }

    public ArrayList<Vehicle> getVehicles() {
        return new ArrayList<>();
    }

    public BusRoute getSelectedRoute() {
        return selectedRoute;
    }

    public void setSelectedRoute(BusRoute selectedRoute) {
        this.selectedRoute = selectedRoute;
    }
}
