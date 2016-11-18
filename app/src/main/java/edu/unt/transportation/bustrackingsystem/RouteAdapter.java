package edu.unt.transportation.bustrackingsystem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.unt.transportation.bustrackingsystem.model.BusRoute;
import edu.unt.transportation.bustrackingsystem.model.BusStop;
import edu.unt.transportation.bustrackingsystem.model.StopSchedule;

import static edu.unt.transportation.bustrackingsystem.util.GeneralUtil.getDayStringForToday;

/**
 * Created by Gil Wasserman on 10/15/2016.
 * <p>
 * RouteAdapter to be used with any ListView or Spinner displaying BusRoutes.  This uses the
 * row_template_routes layout to display some basic route information to the user.
 * Extends ArrayAdapter class, so each item can be retrieved using a list index, however all the
 * data must be ready to be parsed when this adapter's getView method is triggered.
 */
public class RouteAdapter extends ArrayAdapter<BusRoute>
{
    //The number of times to show in each row per BusStop
    private static final int MAX_TIMES_TO_SHOW = 3;
    //DateFormat constant to parse times in Firebase Database
    private final DateFormat dateFormat = new SimpleDateFormat("h:m a", Locale.US);

    //Default Constructor for ArrayAdapter classes
    public RouteAdapter(Context context, int resource)
    {
        super(context, resource);
    }

    //Main method of ArrayAdapter class
    //sets up the display of the view a user will see in each row of the parent view
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        BusRoute busRoute = getItem(position);  //get the current BusRoute object at the
        // requested position
        if (convertView == null)    //This is the first time the view is called as convertView is
        // null
        {
            //Inflate the view into convertView to get the XML layout
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_template_routes,
                    parent, false);
        }
        //Get the UI components in the row_template_routes template and set the initial text values
        TextView routeName = (TextView) convertView.findViewById(R.id.text_view_route_name);
        TextView routeStops = (TextView) convertView.findViewById(R.id.text_view_route_stop);
        routeName.setText(busRoute.getRouteName());
        //Initialize default values for time parsing
        String stopString = "", timeString = "";
        //The following is for a simulated user time, for testing only
        //calendar.set(Calendar.HOUR_OF_DAY, 14);

        if (busRoute.getBusStopObjectMap() != null)
        {
            //If BusStops exist for route, loop through each one, parsing a time string for each
            // stop
            for (BusStop busStop : busRoute.getBusStopObjectMap().values())
            {
                timeString += parseTimeStringFromStop(busRoute, busStop);
            }
        }
        if (timeString.trim().length() == 0)
        {
            timeString = "No More Stops Today";
        }
        stopString += timeString.trim();
        routeStops.setText(stopString);

        return convertView;
    }

    //Accepts a BusRoute and BusStop parameter
    //Returns a String that is in the format <STOP_NAME>: 1:00 PM, 2:00 PM, 3:00 PM
    //Where the above times are replaced with the next 3 available bus stop times
    private String parseTimeStringFromStop(BusRoute busRoute, BusStop busStop)
    {
        String timeString = "";
        Calendar calendar = Calendar.getInstance();
        int counter = 0;
        //If the bus stop has a route schedule, as non major bus stops will not have a set
        // schedule and may be skipped
        if (busStop.getRouteSchedule() != null && busStop.getRouteSchedule().size() > 0)
        {
            Map<String, List<StopSchedule>> theList = busStop.getRouteSchedule();   //Get the
            // Route Schedule Map to a more easily readable list variable
            if (theList.containsKey(busRoute.getRouteId())) //If theList has the current bus
            // route id as a key
            {
                List<StopSchedule> scheduleTimes = theList.get(busRoute.getRouteId());  //Get the
                // schedule times in a more easily readable list variable
                for (StopSchedule stopSchedule : scheduleTimes) //Loop through the StopSchedules
                {
                    if (!stopSchedule.getDayOfWeek().equalsIgnoreCase(getDayStringForToday()))
                        continue; //Not current day, ignore
                    for (String time : stopSchedule.getTimingsList())   //Loop through each time
                    // value in the day's schedule
                    {
                        try
                        {
                            //Converts the current time and the time looped through into
                            // milliseconds
                            int timeBusStop = (int) (dateFormat.parse(time).getTime() % (24 * 60
                                    * 60 *
                                    1000L));
                            int timeCurrent = (int) (calendar.getTime().getTime() % (24 * 60 * 60 *
                                    1000L));
                            //If the current time is smaller than the bus stop's time we're
                            // looking at
                            //add the time to the timeString
                            if (timeCurrent < timeBusStop)
                            {
                                //Check to see if a time has been added or not.
                                //If no time is in the string, start the string with the bus
                                // stop's name
                                timeString += (counter > 0 ? ", " : busStop.getStopName() + ":")
                                        + time;
                                counter++;
                                if (counter == MAX_TIMES_TO_SHOW)
                                {
                                    //Reached the maximum number of times to show per stop
                                    //Append a newline character and return the value
                                    timeString += "\n";
                                    return timeString;
                                }
                            }

                        } catch (ParseException e)
                        {
                            Log.e("RouteAdapter", "Error parsing time " + time, e);
                        }
                    }
                }
            }
        }
        return "";
    }
}
