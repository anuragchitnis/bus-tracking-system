package edu.unt.transportation.bustrackingsystem;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Anurag Chitnis on 10/4/2016.
 */

public class BusTrackingSystem extends Application
{

    private static final String FIREBASE_URL = "https://untbustracking-acb72.firebaseio.com/";
    private static final String FIREBASE_ROOT_NODE = "checkouts";
    private static final String FIREBASE_VEHICLES = "vehicles";
    private static final String FIREBASE_ROUTES = "routes";
    private static final String FIREBASE_STOPS = "stops";
    private static boolean mapActivityVisible;
    private Firebase mFirebase;

    public static boolean isMapActivityVisible()
    {
        return mapActivityVisible;
    }

    public static void mapActivityPaused()
    {
        mapActivityVisible = false;
    }

    public static void mapActivityResumed()
    {
        mapActivityVisible = true;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Set up Firebase
        Firebase.setAndroidContext(this);
        mFirebase = new Firebase(FIREBASE_URL);
        // Make the data persist for offline usage
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public Firebase getmFirebase()
    {
        return mFirebase;
    }
}
