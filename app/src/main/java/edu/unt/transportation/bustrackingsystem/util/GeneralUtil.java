package edu.unt.transportation.bustrackingsystem.util;

import java.util.Calendar;
import java.util.Locale;

/**
 * Utility class consisting of all static methods. It can be used by any class just by calling the required static method.
 * Created by Anurag Chitnis on 11/18/2016.
 */

public class GeneralUtil {

    /**
     * This method returns the day for TODAY. In firebase realtime database we have just one entry 'Monday' , which
     * whose schedule is same for other days like Tuesday, Wednesday, Thursday. So, we are returning the string as 'Monday' even if
     * the day is any of the above mentioned days.
     * @return 'Monday' for Mon, Tue, Wed, Thu or 'Friday', 'Saturday' , 'Sunday' as applicable
     */
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
