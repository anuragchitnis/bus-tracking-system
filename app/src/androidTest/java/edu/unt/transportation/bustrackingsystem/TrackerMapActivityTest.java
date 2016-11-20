package edu.unt.transportation.bustrackingsystem;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static edu.unt.transportation.bustrackingsystem.TrackerMapActivity.KEY_ROUTE_ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;


/**
 * This class is for testing the functionalities in the TrackerMapActivity
 * Created by Anurag Chitnis on 11/7/2016.
 */
@RunWith(AndroidJUnit4.class)
public class TrackerMapActivityTest {

    private TrackerMapActivity trackerMapActivity;

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<TrackerMapActivity>(
            TrackerMapActivity.class) {

        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra(KEY_ROUTE_ID, "Discovery Park");
            return intent;
        }
    };

    @Before
    public void setUp() {
        trackerMapActivity = (TrackerMapActivity) mActivityRule.getActivity();

        /**
         * Introducing the delay of 500 ms before each test case,
         * so that the data loads from firebase in that time
         */
        for(int i=0;i<10;i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!trackerMapActivity.getBusStopList().isEmpty())
                break;
        }
    }

    /**
     * Test if the vehicle list is loaded successfully or not
     */
    @Test
    public void vehicleListTest() {
        assertThat("Vehicle list is empty", !trackerMapActivity.getVehicleList().isEmpty());
    }

    /**
     * Test if the bus Stop list is loaded successfully or not
     */
    @Test
    public void busStopListTest() {
        assertThat("Bus Stop list is empty", !trackerMapActivity.getBusStopList().isEmpty());
        assertEquals("busStopListTest : Size mismatch", 13, trackerMapActivity.getBusStopList().size());
    }

    @Test
    public void busStopMarkerListTest() {
        assertThat("bus stop marker list should be empty", trackerMapActivity.getmBusStopMarkerList().isEmpty());

        onView(withId(R.id.stopCheckbox)).perform(click());


        assertThat("bus stop marker list should not be empty", !trackerMapActivity.getmBusStopMarkerList().isEmpty());
        assertEquals("busStopMarkerListTest : Size mismatch", 13, trackerMapActivity.getmBusStopMarkerList().size());

    }

    @Test
    public void vehicleMarkerMapTest() {
        assertThat("Vehicle marker map should not be empty", !trackerMapActivity.getMarkerMap().isEmpty());
    }

    @Test
    public void scheduledStopListTest() {
        assertThat("Scheduled stops list should be empty", trackerMapActivity.getScheduledStopsList().isEmpty());

        onView(withId(R.id.scheduleCheckbox)).perform(click());

        assertThat("Scheduled stops list should not be empty", !trackerMapActivity.getScheduledStopsList().isEmpty());
        assertEquals("scheduledStopListTest : Size mismatch", 2, trackerMapActivity.getScheduledStopsList().size());

    }

    @Test
    public void scheduleListMapTest() {
        assertThat("schedule list map should be empty", trackerMapActivity.getScheduleListMap().isEmpty());

        onView(withId(R.id.scheduleCheckbox)).perform(click());

        assertThat("schedule list map should not be empty", !trackerMapActivity.getScheduleListMap().isEmpty());
        assertEquals("scheduleListMapTest : Size mismatch", 2, trackerMapActivity.getScheduleListMap().size());

    }
}
