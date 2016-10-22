package edu.unt.transportation.bustrackingsystem;

import android.content.Context;

import com.firebase.client.Firebase;

import java.util.List;

import edu.unt.transportation.bustrackingsystem.model.BusRoute;
import edu.unt.transportation.bustrackingsystem.model.BusStop;
import edu.unt.transportation.bustrackingsystem.model.Vehicle;

/**
 * Created by Anurag Chitnis on 10/5/2016.
 */

public class FirebaseController {

    private static final String FIREBASE_URL = "https://untbustracking-acb72.firebaseio.com/";
    private static final String FIREBASE_ROOT_NODE = "checkouts";
    private static final String FIREBASE_VEHICLES = "vehicles";
    private static final String FIREBASE_ROUTES = "routes";
    private static final String FIREBASE_STOPS = "stops";

    private Firebase mFirebase;

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
