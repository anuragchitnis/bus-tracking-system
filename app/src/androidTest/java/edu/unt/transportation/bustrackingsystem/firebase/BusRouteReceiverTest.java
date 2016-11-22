package edu.unt.transportation.bustrackingsystem.firebase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.unt.transportation.bustrackingsystem.model.BusRoute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Anurag Chitnis on 11/21/2016.
 */

public class BusRouteReceiverTest {

    private BusRouteReceiver busRouteReceiver;
    private List<BusRoute> testBusRouteList;
    private BusRouteListener busRouteListener;

    @Before
    public void setUp() {
        busRouteReceiver = new BusRouteReceiver();
        testBusRouteList = new ArrayList<>();
    }

    @Test
    public void functionalityTestCase() {
        busRouteListener = new BusRouteListener() {
            @Override
            public void onBusRouteAdded(BusRoute busRoute) {
                testBusRouteList.add(busRoute);
            }
        };

        busRouteReceiver.registerListener(busRouteListener);

        for(int i=0; i<10;i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!testBusRouteList.isEmpty())
                break;
        }

        assertFalse("Bus Route List is empty", testBusRouteList.isEmpty());
        assertEquals("Size of the route list does not match",6, testBusRouteList.size());
    }

    @After
    public void tearDown() {
        busRouteReceiver.removeListener(busRouteListener);
    }
}
