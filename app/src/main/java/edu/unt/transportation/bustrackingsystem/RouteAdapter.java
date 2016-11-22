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
import java.util.Collection;
import java.util.Iterator;
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

        TextView routeStop1 = (TextView) convertView.findViewById(R.id.routes_stop_1);
        TextView routeStop2 = (TextView) convertView.findViewById(R.id.routes_stop_2);
        TextView routeStop1Separator = (TextView) convertView.findViewById(R.id
                .routes_stop_1_separator);
        TextView routeStop2Separator = (TextView) convertView.findViewById(R.id
                .routes_stop_2_separator);

        TextView stop1Time1 = (TextView) convertView.findViewById(R.id.routes_stop_1_time_1);
        TextView stop1Time2 = (TextView) convertView.findViewById(R.id.routes_stop_1_time_2);
        TextView stop1Time3 = (TextView) convertView.findViewById(R.id.routes_stop_1_time_3);
        TextView stop2Time1 = (TextView) convertView.findViewById(R.id.routes_stop_2_time_1);
        TextView stop2Time2 = (TextView) convertView.findViewById(R.id.routes_stop_2_time_2);
        TextView stop2Time3 = (TextView) convertView.findViewById(R.id.routes_stop_2_time_3);


        routeName.setText(busRoute.getRouteName());
        //Initialize default values for time parsing
        String stopString = "", timeString = "";
        //The following is for a simulated user time, for testing only
        //calendar.set(Calendar.HOUR_OF_DAY, 14);

        if (busRoute.getBusStopObjectMap() != null)
        {
            Collection<BusStop> busRouteCollection = busRoute.getBusStopObjectMap().values();
            Iterator<BusStop> iterator = busRouteCollection.iterator();
            for (int i = 0; i <= 1; i++)
            {
                if (!iterator.hasNext())
                {
                    if (i == 0)
                    {
                        routeStop1.setText("No Scheduled Stops");
                        routeStop1.setWidth(500);
                        routeStop1Separator.setVisibility(View.INVISIBLE);
                        continue;
                    }
                    if (i == 1)
                    {
                        routeStop2.setText("");
                        routeStop2Separator.setVisibility(View.INVISIBLE);
                        break;
                    }
                }
                BusStop busStop = iterator.next();
                String fullTimeString = parseTimeStringFromStopNew(busRoute, busStop);
                if (i == 0)
                {
                    routeStop1.setText(busStop.getStopName());

                    String[] timeSplit = fullTimeString.split(",");
                    if (timeSplit.length >= 1)
                    {
                        stop1Time1.setText(timeSplit[0]);
                        if (timeSplit.length >= 2)
                        {
                            stop1Time2.setText(timeSplit[1]);
                            if (timeSplit.length >= 3)
                            {
                                stop1Time3.setText(timeSplit[2]);
                            }
                        }

                    }

                }
                else
                {
                    routeStop2.setText(busStop.getStopName());
                    String[] timeSplit = fullTimeString.split(",");
                    if (timeSplit.length >= 1)
                    {
                        stop2Time1.setText(timeSplit[0]);
                        if (timeSplit.length >= 2)
                        {
                            stop2Time2.setText(timeSplit[1]);
                            if (timeSplit.length >= 3)
                            {
                                stop2Time3.setText(timeSplit[2]);
                            }
                        }

                    }
                }
            }
        }
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

    private String parseTimeStringFromStopNew(BusRoute busRoute, BusStop busStop)
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
                                timeString += (counter > 0 ? ", " : "")
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
