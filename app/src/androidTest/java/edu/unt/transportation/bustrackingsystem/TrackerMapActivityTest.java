package edu.unt.transportation.bustrackingsystem;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * This class is for testing the functionalities in the TrackerMapActivity
 * Created by Anurag Chitnis on 11/7/2016.
 */
@RunWith(AndroidJUnit4.class)
public class TrackerMapActivityTest {

    private boolean isActivityFinished;

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<TrackerMapActivity>(
            TrackerMapActivity.class) {
        @Override
        protected void afterActivityFinished() {
            super.afterActivityFinished();
            isActivityFinished = true;
        }
    };

    /**
     * Test if the vehicle list is loaded successfully or not
     */
    @Test
    public void vehicleListTest() {
        TrackerMapActivity trackerMapActivity = (TrackerMapActivity) mActivityRule.getActivity();

        if(isActivityFinished)
            assertThat("Vehicle list is empty", !trackerMapActivity.getVehicleList().isEmpty());
    }

    /**
     * Test if the bus Stop list is loaded successfully or not
     */
    @Test
    public void busStopListTest() {
        TrackerMapActivity trackerMapActivity = (TrackerMapActivity) mActivityRule.getActivity();

        if(isActivityFinished)
            assertThat("Bus Stop list is empty", !trackerMapActivity.getBusStopList().isEmpty());
    }
}
