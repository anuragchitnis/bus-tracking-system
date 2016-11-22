package edu.unt.transportation.bustrackingsystem.util;

import org.junit.Test;

import java.util.Calendar;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Created by Anurag Chitnis on 11/21/2016.
 */

public class GeneralUtilTest {

    @Test
    public void getDayStringForTodayTest() {
        Calendar calendar = Calendar.getInstance();
        String today = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
        assertEquals("Today's day returned incorrectly",today,GeneralUtil.getDayStringForToday());
    }
}
