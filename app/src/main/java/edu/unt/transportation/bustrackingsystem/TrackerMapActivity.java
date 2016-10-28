package edu.unt.transportation.bustrackingsystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
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

public class TrackerMapActivity extends AppCompatActivity implements OnMapReadyCallback, ValueEventListener {

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
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .build();
        mGoogleApiClient.connect();

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
}
