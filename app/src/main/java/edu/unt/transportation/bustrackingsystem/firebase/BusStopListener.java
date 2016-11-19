package edu.unt.transportation.bustrackingsystem.firebase;

import edu.unt.transportation.bustrackingsystem.model.BusStop;

/**
 * This interface can be implemented by the entities interested in receiving callbacks whenever a new bus stop is added.
 * Created by Anurag Chitnis on 11/17/2016.
 */

public interface BusStopListener {

    /**
     * Callback to receive the busStop which has added to a specific route.
     * @param busStop BusStop object which has been added from firebase
     */
    void onBusStopAdded(BusStop busStop);
}
