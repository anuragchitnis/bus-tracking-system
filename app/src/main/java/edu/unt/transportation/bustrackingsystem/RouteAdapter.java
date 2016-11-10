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

public class RouteAdapter extends ArrayAdapter<BusRoute>
{
    private static final int MAX_TIMES_TO_SHOW = 3;
    DateFormat dateFormat = new SimpleDateFormat("h:m a");

    public RouteAdapter(Context context, int resource)
    {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        BusRoute busRoute = getItem(position);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_template_routes,
                    parent, false);
        }
        TextView routeName = (TextView) convertView.findViewById(R.id.text_view_route_name);
        TextView routeStops = (TextView) convertView.findViewById(R.id.text_view_route_stop);
        routeName.setText(busRoute.getRouteName());
        String stopString = "", timeString = "";
        Calendar calendar = Calendar.getInstance();
        //The following is for a simulated user time
        calendar.set(Calendar.HOUR_OF_DAY, 14);

        if (busRoute.getBusStopObjectMap() != null)
        {
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
        /*
        Calendar cal = Calendar.getInstance();
        String timeString = "";
        for(BusStop busStop : busRoute.getBusStopObjectMap().values()){
            for(String time: busStop.getRouteSchedule().get(busRoute.getRouteId()).getTimingsList
            ()){
                try
                {
                    if(dateFormat.parse(time).after(cal.getTime())){
                        if(timeString.length()>0){
                            timeString+=",";
                        }
                        timeString += time;
                    }
                } catch (ParseException e)
                {
                    e.printStackTrace();
                }

            }
        }
        */


        return convertView;
    }

    private String parseTimeStringFromStop(BusRoute busRoute, BusStop busStop)
    {
        String timeString = "";
        Calendar calendar = Calendar.getInstance();
        String today = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
        int counter = 0;
        if (busStop.getRouteSchedule() != null && busStop.getRouteSchedule().size() > 0)
        {
            Map<String, List<StopSchedule>> theList = busStop.getRouteSchedule();
            if (theList.containsKey(busRoute.getRouteId()))
            {
                List<StopSchedule> scheduleTimes = theList.get(busRoute.getRouteId());
                for (StopSchedule stopSchedule : scheduleTimes)
                {
                    if (!stopSchedule.getDayOfWeek().equalsIgnoreCase(today)) continue;
                    for (String time : stopSchedule.getTimingsList())
                    {
                        try
                        {
                            int time1 = (int) (dateFormat.parse(time).getTime() % (24 * 60 * 60 *
                                    1000L));
                            int time2 = (int) (calendar.getTime().getTime() % (24 * 60 * 60 *
                                    1000L));
                            if (time2 < time1)
                            {
                                timeString += (counter > 0 ? ", " : busStop.getStopName() + ":")
                                        + time;
                                counter++;
                                if (counter == MAX_TIMES_TO_SHOW)
                                {
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
