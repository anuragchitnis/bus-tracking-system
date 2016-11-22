package edu.unt.transportation.bustrackingsystem.firebase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.unt.transportation.bustrackingsystem.model.Vehicle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Anurag Chitnis on 11/21/2016.
 */

public class VehicleMapChangeReceiverTest {

    private VehicleMapChangeReceiver vehicleMapChangeReceiver;
    private List<Vehicle> testVehicleList;
    private VehicleChangeListener vehicleChangeListener;

    @Before
    public void setUp() {
        vehicleMapChangeReceiver = new VehicleMapChangeReceiver();
        testVehicleList = new ArrayList<>();
    }

    @Test
    public void functionalityTestCase() {
        vehicleChangeListener = new VehicleChangeListener() {
            @Override
            public void onVehicleChanged(Vehicle vehicle) {
                testVehicleList.add(vehicle);
            }
        };

        vehicleMapChangeReceiver.registerListener(vehicleChangeListener, "Discovery Park");

        for(int i=0; i<10;i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!testVehicleList.isEmpty())
                break;
        }

        assertFalse("Vehicle List is empty", testVehicleList.isEmpty());
        assertEquals("Size of the vehicle list does not match",1, testVehicleList.size());
    }

    @After
    public void tearDown() {
        vehicleMapChangeReceiver.removeListener(vehicleChangeListener);
    }
}
