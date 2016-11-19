package edu.unt.transportation.bustrackingsystem.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This class contains the network related utility methods
 * Originally Created by Gil on 11/10/2016.
 */
public class NetworkUtil
{

    /**
     * This method checks if the internet connection is available or not
     * @param context Android application context
     * @return true - if internet is available, false otherwise
     */
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
