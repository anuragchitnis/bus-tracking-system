package edu.unt.transportation.bustrackingsystem;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import edu.unt.transportation.bustrackingsystem.model.BusRoute;
import edu.unt.transportation.bustrackingsystem.model.Vehicle;

import static com.google.android.gms.location.LocationServices.API;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;

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
public class LocationSharingService extends Service implements
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
    private static final String TAG = "LocationSharingService";

    /**
     * Actual location parameters are
     * stored in this instance
     */
    LatLng latLng = null;

//    /**
//     * Creates an IntentService.  Invoked by your subclass's constructor.
//     *
//     * @param name Used to name the worker thread, important only for debugging.
//     */
//    public LocationSharingService(String name) {
//        super(name);
//    }
//
//    public LocationSharingService() {
//        super("LocationSharingService");
//    }

    /**
     * When the activty is called, this
     * function gets invoked which creates and
     * render the UI
     */
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();

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
         * Set up the API client for Places API
         */
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(API)
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

        vehicle.setIsAssigned(true);
        mFirebase.child("/vehicles/" + FIREBASE_VEHICLE_NODE).setValue(vehicle);

        Log.d("LocationSharingService", "onHandleIntentEnd");

        return START_NOT_STICKY;
    }

    /**
     * on this activity is
     * closed or moved to anotehr
     * activity, onDestory() method
     * would be called
     */
    public void onDestroy() {
        Log.d("LocationSharingService", "onDestroy");
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        route.getVehicleMap().remove(FIREBASE_VEHICLE_NODE);
        mFirebase.child("/routes/" + FIREBASE_ROUTE_NODE).setValue(route);
        vehicle.setIsAssigned(false);
        mFirebase.child("/vehicles/" + FIREBASE_VEHICLE_NODE).setValue(vehicle);
        nm.cancelAll();
        stopLocationUpdates();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

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
        }

    }

    /**
     * on First time GoogleApiClient connected, this
     * callback gets called.
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        /**
         * getting the last location available location
         * onConnected
         */
        Location mLastLocation = null;
        try {
            mLastLocation = FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }
        catch (SecurityException ex) {
            Log.e(TAG, "Check if the location permission is granted "+ex.getMessage());

        }

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
        try{
            FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }catch(SecurityException e){
            Log.e(TAG, "Check if the location permission is granted "+e.getMessage());
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected void stopLocationUpdates() {
        FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }
}