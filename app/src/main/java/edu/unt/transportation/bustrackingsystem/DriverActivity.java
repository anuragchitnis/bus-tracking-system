package edu.unt.transportation.bustrackingsystem;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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
 * <b>Driver Activity:</b> When driver logs in, this activity updates the
 * vehicle location.
 * <b>Created By:</b> Satyanarayana
 * <b>Revised By:</b> Satyanarayana
 * <b>Descritpion:</b> An activity that shows map and<br/>
 * keep pushing the latest location to firebase as vehicle moves
 * <b>Data Structre:</b> Uses some final static variables, instance of
 * Vechicle and BusRoute classes
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
    private GoogleMap mMap = null;

    /**
     * Firebase instance variable
     */
    private Firebase mFirebase = null;

    /**
     * GogoleAPIClient, to receieve map
     * location updates
     */
    private GoogleApiClient mGoogleApiClient = null;

    /**
     * LatLong bounds of hte
     * location
     */
    private LatLngBounds.Builder mBounds = new LatLngBounds.Builder();

    /**
     * An instance of Vehicle class
     * passed from SignInActivity, the
     * vehicle that the driver checked out
     */
    private Vehicle vehicle = null;

    /**
     * An instance of BusRoute class
     * passed from SignInActivity, the
     * route that the driver checked out
     */
    private BusRoute route = null;

    /**
     * Activity name used to
     * Log the comments
     */
    private static final String TAG = "DriverActivity";

    /**
     * Actual location parameters are
     * stored in this instance
     */
    LatLng latLng = null;

    /**
     * Textviews
     */
    TextView routeTextView = null;
    TextView vehicleTextView = null;
    TextView speedTextView = null;

    /**
     * When the activty is called, this
     * function gets invoked which creates and
     * render the UI
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * setting content view/layout of
         * Driver Activity
         */
        setContentView(R.layout.activity_driver);

        /**
         * creating toolbar of the DriverActivity
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.drivertoolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();

        /**
         * Extracting paramters passed from
         * SignInActivity
         */
        FIREBASE_VEHICLE_NODE = bundle.getString("vehicleId");
        FIREBASE_ROUTE_NODE = bundle.getString("routeId");

        /**
         * Deserializing instances passed from
         * SignInActivity
         */
        vehicle = (Vehicle)bundle.getSerializable(FIREBASE_VEHICLE_NODE);
        route = (BusRoute)bundle.getSerializable((FIREBASE_ROUTE_NODE));

        /**
         * Set up Google Maps
         */
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /**
         * Set up the API client for Places API
         */
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        /**
         * Set up Firebase
         */
        Firebase.setAndroidContext(this);
        mFirebase = new Firebase(FIREBASE_URL);

        /**
         * Making an entry into route's vehicle map
         * with the vehicle id as true
         */
        route.getVehicleMap().put(FIREBASE_VEHICLE_NODE, true);
        mFirebase.child("/routes/" + FIREBASE_ROUTE_NODE).setValue(route);

        routeTextView = (TextView) findViewById(R.id.textView3);
        vehicleTextView = (TextView) findViewById(R.id.textView4);
        speedTextView = (TextView) findViewById(R.id.textView5);
        routeTextView.setText("Route: ["+FIREBASE_ROUTE_NODE+"]");
        vehicleTextView.setText("Vehicle: ["+FIREBASE_VEHICLE_NODE+"]");
        vehicleTextView.setText("Speed: ["+0.0+"]");
    }

    /**
     * on this activity is
     * closed or moved to anotehr
     * activity, onDestory() method
     * would be called
     */
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
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /**
             * If location services are not enabled,
             * the function following requests the user
             * to enable them
             */
            requestPermissions(new String[] {
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                            123);
            return;
        }
        /**
         * setting the GPS locatio checkup and
         * button enabled, that shows the button
         * on the top right corner
         */
        mMap.setMyLocationEnabled(true);

    }

    /**
     * On backbutton pressed, letting
     * the super class know about the
     * event
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Comment this super call to avoid calling finish()
    }

//    /**
//     * a callback method called onCreate()
//     * @param menu
//     * @return
//     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.driver, menu);
//        return true;
//    }
//
//    /**
//     * onSelect options menu from top right corner,
//     * this method gets called.
//     * @param item
//     * @return
//     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        /**
//         * on Click of sign out
//         * the following block of code clears
//         * route list, vehicle list and deletes
//         * an entry from vehicel map
//         */
//        if (id == R.id.action_signOut) {
//            route.getVehicleMap().remove(FIREBASE_VEHICLE_NODE);
//            mFirebase.child("/routes/" + FIREBASE_ROUTE_NODE).setValue(route);
//            startActivity(new Intent(this, RouteListActivity.class));
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * onLocation changed, when vehicle is in motion,
     * the method following would get called as callback,
     * and updates the location in Firebase
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(vehicle != null){
            vehicle.setLatitude(location.getLatitude());
            vehicle.setLongitude(location.getLongitude());
            mFirebase.child("/vehicles/"+FIREBASE_VEHICLE_NODE).setValue(vehicle);
            speedTextView.setText("Speed: ["+location.getSpeed()+"], m/s");
        }
        /**
         * zoom to current position
         */
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(14).build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

    }

    /**
     * on First time GoogleApiClient connected, this
     * callback gets called.
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /**
             * If location services are not enabled,
             * the function following requests the user
             * to enable them
             */
            requestPermissions(new String[] {
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                            123);
            return;
        }
        /**
         * getting the last location available location
         * onConnected
         */
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        /**
         * If last location is found,
         * creating the LatLng instance and
         * updating the firebase with vehicles
         * latest position
         */
        if (mLastLocation != null) {
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            if (vehicle != null){
                vehicle.setLatitude(mLastLocation.getLatitude());
                vehicle.setLongitude(mLastLocation.getLongitude());
                mFirebase.child("/vehicles/"+FIREBASE_VEHICLE_NODE).setValue(vehicle);
            }
        }

        /**
         * setting the frequency of
         * location requestor, calling
         * for every 500ms and at fastestinterval of
         * 300ms
         */
        LocationRequest mLocationRequest;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500); //5 seconds
        mLocationRequest.setFastestInterval(300); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}