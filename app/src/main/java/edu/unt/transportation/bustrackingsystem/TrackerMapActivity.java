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

import com.firebase.client.Firebase;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.unt.transportation.bustrackingsystem.model.Vehicle;

/**
 * Created by Anurag Chitnis on 10/5/2016.
 */

public class TrackerMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
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

        // Set up the API client for Places API
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .build();
//        mGoogleApiClient.connect();

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
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        LatLng mapCenter = new LatLng(33.2528625,-97.15247265625);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, 13));
        // Flat markers will rotate when the map is rotated,
        // and change perspective when the map is tilted.
        //addPointToViewPort(mapCenter);
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus))
                .position(mapCenter)
                .flat(true));

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d(TAG,"vehicleChangeListener : onDataChange "+dataSnapshot.getKey());
        Vehicle vehicleSnapShot = dataSnapshot.getValue(Vehicle.class);
        if(vehicleList.contains(vehicleSnapShot)) {
            //TODO: Change the vehicle data on the User Interface
        }
        else
            vehicleList.add(dataSnapshot.getValue(Vehicle.class));
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

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
            //mMap.getUiSettings().setMyLocationButtonEnabled(true);
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

    @Override
    public boolean onMyLocationButtonClick() {
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }
}
