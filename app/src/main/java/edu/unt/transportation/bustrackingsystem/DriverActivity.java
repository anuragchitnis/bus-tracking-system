package edu.unt.transportation.bustrackingsystem;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import edu.unt.transportation.bustrackingsystem.model.BusRoute;
import edu.unt.transportation.bustrackingsystem.model.Vehicle;

/**
 * Created By: Anurag
 * Revised By: Satyanarayana
 * Descritpion: An activity that shows map and
 * keep pushing the latest location to firebase
 */
public class DriverActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    /**
     * Firebase root URL
     */
    private static final String FIREBASE_URL = "https://untbustracking-acb72.firebaseio.com/";

    /**
     * Firebase vehicle root, the vehicle that
     * was selected by driver while logging in
     */
    private static String FIREBASE_VEHICLE_NODE = null;

    /**
     * Firebase route root, the route that
     * wast selected by driver while logging in
     */
    private static String FIREBASE_ROUTE_NODE = null;

    /**
     * Google Map private variable, that was called
     * onMapReady event fired
     */
    private GoogleMap mMap;

    /**
     * Firebase instance variable
     */
    private Firebase mFirebase;
    private GoogleApiClient mGoogleApiClient;
    private LatLngBounds.Builder mBounds = new LatLngBounds.Builder();

    private Vehicle vehicle;
    private BusRoute route;

    private static final String TAG = "DriverActivity";

    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        Toolbar toolbar = (Toolbar) findViewById(R.id.drivertoolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

        FIREBASE_VEHICLE_NODE = bundle.getString("vehicleId");
        FIREBASE_ROUTE_NODE = bundle.getString("routeId");
        vehicle = (Vehicle)bundle.getSerializable(FIREBASE_VEHICLE_NODE);
        route = (BusRoute)bundle.getSerializable((FIREBASE_ROUTE_NODE));

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

        route.getVehicleMap().put(FIREBASE_VEHICLE_NODE, true);
        mFirebase.child("/routes/" + FIREBASE_ROUTE_NODE).setValue(route);

    }

    public void onDestroy() {
        super.onDestroy();
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
            route.getVehicleMap().remove(FIREBASE_VEHICLE_NODE);
            mFirebase.child("/routes/" + FIREBASE_ROUTE_NODE).setValue(route);
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
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(vehicle != null){
            vehicle.setLatitude(location.getLatitude());
            vehicle.setLongitude(location.getLongitude());
            mFirebase.child("/vehicles/"+FIREBASE_VEHICLE_NODE).setValue(vehicle);
//            Toast.makeText(this, "Location Changed", Toast.LENGTH_SHORT).show();
        }
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
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            if (vehicle != null){
                vehicle.setLatitude(mLastLocation.getLatitude());
                vehicle.setLongitude(mLastLocation.getLongitude());
                mFirebase.child("/vehicles/"+FIREBASE_VEHICLE_NODE).setValue(vehicle);
            }
        }

        LocationRequest mLocationRequest;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500); //5 seconds
        mLocationRequest.setFastestInterval(300); //3 seconds
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