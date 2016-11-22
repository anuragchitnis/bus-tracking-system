package edu.unt.transportation.bustrackingsystem.firebase;

import org.junit.Before;
import org.junit.Test;

import edu.unt.transportation.bustrackingsystem.model.BusStop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by anura on 11/20/2016.
 */

public class BusStopReceiverTest {

    private BusStopReceiver busStopReceiver;
    private BusStop testBusStop;

    @Before
    public void setUp() {
        busStopReceiver = new BusStopReceiver();
    }

    @Test
    public void functionalityTestCase() {
        busStopReceiver.registerListener(new BusStopListener() {
            @Override
            public void onBusStopAdded(BusStop busStop) {
                testBusStop = busStop;
            }
        }, "gab");

        for(int i=0; i<10;i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(testBusStop!=null)
                break;
        }

        assertNotEquals("Bus Stop is null", null, testBusStop);
        assertEquals("Mismatch in the ID","gab",testBusStop.getStopID());
    }
}
