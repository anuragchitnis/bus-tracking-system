package edu.unt.transportation.bustrackingsystem.util;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Anurag Chitnis on 11/18/2016.
 */

public class GeneralUtil {

    public static String getDayStringForToday() {
        Calendar calendar = Calendar.getInstance();
        String today = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
        switch(today) {
            case "Monday":
                return "Monday";

            case "Tuesday":
                return "Monday";

            case "Wednesday":
                return "Monday";

            case "Thursday":
                return "Monday";

            case "Friday":
                return "Friday";

            case "Saturday":
                return "Saturday";

            case "Sunday":
                return "Sunday";

            default:
                return "Error";
        }
    }
}
