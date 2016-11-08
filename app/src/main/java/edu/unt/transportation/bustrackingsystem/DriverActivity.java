package edu.unt.transportation.bustrackingsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

import edu.unt.transportation.bustrackingsystem.model.BusRoute;
import edu.unt.transportation.bustrackingsystem.model.Vehicle;

public class DriverActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private static final String FIREBASE_URL = "https://untbustracking-acb72.firebaseio.com/";
    private static String FIREBASE_VEHICLE_NODE = null;
    private static String FIREBASE_ROUTE_NODE = null;

    private static final int REQUEST_PLACE_PICKER = 1;

    private GoogleMap mMap;
    private Firebase mFirebase;
    private GoogleApiClient mGoogleApiClient;
    private LatLngBounds.Builder mBounds = new LatLngBounds.Builder();

    private Vehicle vehicle;
    private BusRoute route;

    private static final String TAG = "DriverActivity";

    LatLng latLng;
    Marker currLocationMarker;

    LocationManager mlocManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        Toolbar toolbar = (Toolbar) findViewById(R.id.drivertoolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        FIREBASE_VEHICLE_NODE = bundle.getString("vehicleId");
        FIREBASE_ROUTE_NODE = bundle.getString("routeId");
        FIREBASE_VEHICLE_NODE = "1266";
        FIREBASE_ROUTE_NODE = "dp_00";

        // Set up Google Maps
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Set up the API client for Places API
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        // Set up Firebase
        Firebase.setAndroidContext(this);
        mFirebase = new Firebase(FIREBASE_URL);
        Log.d(TAG, "Vechile ID: [" + FIREBASE_VEHICLE_NODE + "]");
        Log.d(TAG, "Route ID: [" + FIREBASE_ROUTE_NODE + "]");
        mFirebase.child("/vehicles/").addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                if (key == FIREBASE_VEHICLE_NODE) {
                    vehicle = dataSnapshot.getValue(Vehicle.class);
                }
            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        mFirebase.child("/routes/").addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                if (key == FIREBASE_ROUTE_NODE) {
                    route = dataSnapshot.getValue(BusRoute.class);
                    route.getVehicleMap().put(FIREBASE_VEHICLE_NODE, true);
                    mFirebase.child("/routes/" + FIREBASE_ROUTE_NODE).setValue(route);
                }
            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Prompt the user to check out of their location. Called when the "Check Out!" button
     * is clicked.
     */
    public void checkOut(View view) {
        try {
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent, REQUEST_PLACE_PICKER);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    REQUEST_PLACE_PICKER);
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(this, "Please install Google Play Services!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                Map<String, Object> checkoutData = new HashMap<>();
                checkoutData.put("latitude", place.getLatLng().latitude);
                checkoutData.put("longitude", place.getLatLng().longitude);

                mFirebase.child(FIREBASE_VEHICLE_NODE).child(place.getId()).setValue(checkoutData);

            } else if (resultCode == PlacePicker.RESULT_ERROR) {
                Toast.makeText(this, "Places API failure! Check the API is enabled for your key",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Map setup. This is called when the GoogleMap is available to manipulate.
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                    123);
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
//        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location location) {
//                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//                addPointToViewPort(ll);
//                // we only want to grab the location once, to allow the user to pan and zoom freely.
//                mMap.setOnMyLocationChangeListener(null);
//            }
//        });

        // Pad the map controls to make room for the button - note that the button may not have
        // been laid out yet.
//        final Button button = (Button) findViewById(R.id.checkout_button);
//        button.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        mMap.setPadding(0, button.getHeight(), 0, 0);
//                    }
//                }
//        );
    }

    private void addPointToViewPort(LatLng newPoint) {
        mBounds.include(newPoint);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mBounds.build(),
                findViewById(R.id.checkout_button).getHeight()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Comment this super call to avoid calling finish()
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.driver, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_signOut) {
//            route.getVehicleMap().remove(FIREBASE_VEHICLE_NODE);
//            mFirebase.child("/routes/" + FIREBASE_ROUTE_NODE).setValue(route);
            SignInActivity.list1.clear();
            SignInActivity.list2.clear();
            SignInActivity.vehicleId = null;
            SignInActivity.routeId = null;
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
//        if (currLocationMarker != null) {
//            currLocationMarker.remove();
//        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        if(route != null){
//            route.getVehicleMap().put(FIREBASE_VEHICLE_NODE, true);
//            mFirebase.child("/routes/" + FIREBASE_ROUTE_NODE).setValue(route);
//        }
        if(vehicle != null){
            vehicle.setLatitude(location.getLatitude());
            vehicle.setLongitude(location.getLongitude());
            mFirebase.child("/vehicles/"+FIREBASE_VEHICLE_NODE).setValue(vehicle);
//            Toast.makeText(this, "Location Changed", Toast.LENGTH_SHORT).show();
        }
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//        currLocationMarker = mMap.addMarker(markerOptions);



        //zoom to current position:
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(14).build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

    }

    @Override
    public void onConnected(Bundle bundle) {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                    123);
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//            if(route != null){
//                route.getVehicleMap().put(FIREBASE_VEHICLE_NODE, true);
//                mFirebase.child("/routes/" + FIREBASE_ROUTE_NODE).setValue(route);
//            }
            if (vehicle != null){
                vehicle.setLatitude(mLastLocation.getLatitude());
                vehicle.setLongitude(mLastLocation.getLongitude());
                mFirebase.child("/vehicles/"+FIREBASE_VEHICLE_NODE).setValue(vehicle);
            }
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLng);
//            markerOptions.title("Current Position");
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//            currLocationMarker = mMap.addMarker(markerOptions);
        }

        LocationRequest mLocationRequest;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}