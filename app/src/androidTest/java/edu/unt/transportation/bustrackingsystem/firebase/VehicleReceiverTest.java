package edu.unt.transportation.bustrackingsystem.firebase;

import org.junit.Before;
import org.junit.Test;

import edu.unt.transportation.bustrackingsystem.model.Vehicle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by nitesh on 11/20/2016.
 */

public class VehicleReceiverTest {

    private VehicleReceiver vehicleReceiver;
    private Vehicle testVehicle;

    @Before
    public void setUp() {
        vehicleReceiver = new VehicleReceiver();
    }

    @Test
    public void functionalityTestCase() {
        vehicleReceiver.registerListener(new VehicleChangeListener() {
            @Override
            public void onVehicleChanged(Vehicle vehicle) {
                testVehicle = vehicle;
            }
        }, "1230");

        for(int i=0; i<10;i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(testVehicle!=null)
                break;
        }

        assertNotEquals("Vehicle is null", null, testVehicle);
        assertEquals("Mismatch in the Veh-ID","1230",testVehicle.getVehicleID());
    }
}
