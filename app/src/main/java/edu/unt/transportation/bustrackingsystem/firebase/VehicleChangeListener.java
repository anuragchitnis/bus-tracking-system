package edu.unt.transportation.bustrackingsystem.firebase;

import edu.unt.transportation.bustrackingsystem.model.Vehicle;

/**
 * Implementer of this interface will receive callbacks whenever a vehicle data changes
 * Created by Anurag Chitnis on 11/21/2016.
 */

public interface VehicleChangeListener {

    void onVehicleChanged(Vehicle vehicle);
}
