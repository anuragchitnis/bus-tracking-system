package edu.unt.transportation.bustrackingsystem.firebase;

import edu.unt.transportation.bustrackingsystem.model.BusRoute;

/**
 * Created by Anurag Chitnis on 11/17/2016.
 */

public interface BusRouteListener {
    void onBusRouteAdded(BusRoute busRoute);
}
