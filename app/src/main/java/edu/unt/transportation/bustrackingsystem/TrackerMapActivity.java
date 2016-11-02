package edu.unt.transportation.bustrackingsystem;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.unt.transportation.bustrackingsystem.model.Vehicle;

/**
 * Created by Anurag Chitnis on 10/5/2016.
 */

public class TrackerMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, ValueEventListener {

    private static final String TAG = TrackerMapActivity.class.getName();
    private static final String FIREBASE_URL = "https://untbustracking-acb72.firebaseio.com/";
    private static final String FIREBASE_VEHICLES = "vehicles";
    private static final String FIREBASE_ROUTES = "routes";

    private GoogleMap mMap;
    private Firebase mFirebase;
    private DatabaseReference mDatabase;
    private GoogleApiClient mGoogleApiClient;
    private LatLngBounds.Builder mBounds = new LatLngBounds.Builder();
    private List<Vehicle> vehicleList = new ArrayList<>();
    private List<String> vehicleIdList;
    private ValueEventListener vehicleChangeListener;
    private DatabaseReference vehiclesRef;
    private Map<String, Marker> markerMap;

    private String routeID = "dp_00";

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up Google Maps
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.trackerMap);
        mapFragment.getMapAsync(this);

        markerMap = new HashMap<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        vehiclesRef = mDatabase.child(FIREBASE_VEHICLES);
        DatabaseReference vehicleMapRef = mDatabase.child("routes/"+routeID+"/vehicleMap");

        vehicleMapRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG,"onChildAdded "+dataSnapshot.getKey());
                //Register to listen to the changes made to all the vehicles on this route
                vehiclesRef.child(dataSnapshot.getKey()).addValueEventListener(TrackerMapActivity.this);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //Remove the listener for this bus as we don't want to receive updates from it anymore
                vehiclesRef.child(dataSnapshot.getKey()).removeEventListener(TrackerMapActivity.this);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();
        LatLng mapCenter = new LatLng(33.2139981,-97.1483429);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, 13));
        // Flat markers will rotate when the map is rotated,
        // and change perspective when the map is tilted.
        //addPointToViewPort(mapCenter);
//        mMap.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus))
//                .position(mapCenter)
//                .flat(true));

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d(TAG,"vehicleChangeListener : onDataChange "+dataSnapshot.getKey());
        Vehicle vehicleSnapShot = dataSnapshot.getValue(Vehicle.class);
        LatLng vehiclePosition = new LatLng(vehicleSnapShot.getLatitude(),vehicleSnapShot.getLongitude());
        if(vehicleList.contains(vehicleSnapShot)) {
            // Replace the old vehicle object with the new one
            vehicleList.set(vehicleList.indexOf(vehicleSnapShot), vehicleSnapShot);
            /**
             * If the vehicle is already present on the UI and in the vehicleList,
             * most probably the position is updated, so we update the marker position
             */
            markerMap.get(vehicleSnapShot.getVehicleID()).setPosition(vehiclePosition);
        }
        else {
            /**
             * Perform following operations if new vehicle is added on the route
             */
            vehicleList.add(vehicleSnapShot);
            Marker newVehicleMarker = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus))
                    .position(vehiclePosition)
                    .flat(true));
            markerMap.put(vehicleSnapShot.getVehicleID(),newVehicleMarker);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void onStopToggled(View view) {
        Toast.makeText(TrackerMapActivity.this, "Stop Checkbox Clicked",
                Toast.LENGTH_LONG);

    }

    private void addPointToViewPort(LatLng newPoint) {
        mBounds.include(newPoint);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mBounds.build(),
                findViewById(R.id.checkout_button).getHeight()));
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }
}
