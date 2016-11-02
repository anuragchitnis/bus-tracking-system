package edu.unt.transportation.bustrackingsystem;

import android.content.Context;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.List;

import edu.unt.transportation.bustrackingsystem.model.BusRoute;
import edu.unt.transportation.bustrackingsystem.model.BusStop;
import edu.unt.transportation.bustrackingsystem.model.Vehicle;

public class FirebaseController implements Serializable
{

    private static final String FIREBASE_URL = "https://untbustracking-acb72.firebaseio.com/";
    public static final String KEY_FIREBASE_CONTROLLER = "keyFirebaseController";

    public static final String FIREBASE_ROOT_NODE = "checkouts";
    public static final String FIREBASE_VEHICLES = "vehicles";
    public static final String FIREBASE_ROUTES = "routes";
    public static final String FIREBASE_STOPS = "stops";

    public Firebase getmFirebase()
    {
        return mFirebase;
    }

    public void setmFirebase(Firebase mFirebase)
    {
        this.mFirebase = mFirebase;
    }

    private Firebase mFirebase;

    public Firebase getFirebaseInstance()
    {
        if (mFirebase == null)
        {
            mFirebase = new Firebase(FIREBASE_URL);
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithCustomToken("vaaleKOSxYr6ApZTHL2OY1EFD1u44zBCGEdidQs9");
            mFirebase.authWithCustomToken("vaaleKOSxYr6ApZTHL2OY1EFD1u44zBCGEdidQs9", new Firebase.AuthResultHandler()
            {
                @Override
                public void onAuthenticated(AuthData authData)
                {

                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError)
                {
                    Log.e("Auth", "onAuthenticationError: " + firebaseError.getMessage() + " -- " + firebaseError.getDetails());
                }
            });
        }
        return mFirebase;
    }

    public FirebaseController(Context context) {
        // Set up Firebase
        Firebase.setAndroidContext(context);
        mFirebase = new Firebase(FIREBASE_URL);
    }

    public List<BusRoute> getBusRouteList() {
        return null;
    }

    public List<String> getSchedule(int stopID, int routeID) {
        return null;
    }

    public Vehicle getVehicleFromID(int vehicleID) {
        return null;
    }

    public List<Vehicle> getVehicleListFromRoute(int routeID) {
        return null;
    }

    public BusStop getBusStop(int stopID) {
        return null;
    }

    private void writeNewBusStop(String stopID, BusStop busStop) {

    }
}
