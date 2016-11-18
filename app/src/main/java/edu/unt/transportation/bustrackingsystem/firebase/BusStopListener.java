package edu.unt.transportation.bustrackingsystem.firebase;

import edu.unt.transportation.bustrackingsystem.model.BusStop;

/**
 * Created by Anurag Chitnis on 11/17/2016.
 */

public interface BusStopListener {

    void onBusStopAdded(BusStop busStop);
}
