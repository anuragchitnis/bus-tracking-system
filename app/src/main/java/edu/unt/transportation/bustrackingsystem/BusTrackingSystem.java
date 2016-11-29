package edu.unt.transportation.bustrackingsystem;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * This class is loaded when the application starts. We are using it to initialize the offline caching
 * Created by Anurag Chitnis on 10/4/2016.
 */

public class BusTrackingSystem extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        // Make the data persist for offline usage
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
