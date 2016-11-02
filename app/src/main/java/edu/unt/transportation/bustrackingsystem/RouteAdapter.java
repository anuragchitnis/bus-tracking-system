package edu.unt.transportation.bustrackingsystem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import edu.unt.transportation.bustrackingsystem.model.BusRoute;
import edu.unt.transportation.bustrackingsystem.model.BusStop;

public class RouteAdapter extends ArrayAdapter<BusRoute>
{
    DateFormat dateFormat = new SimpleDateFormat("h:m  a");

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
        String stopString = "";
        for (BusStop busStop : busRoute.getBusStopObjectMap().values())
        {
            if (stopString.length() > 0)
            {
                stopString += ",";
            }
            if (busStop.getStopName() != null && busStop.getStopName().length() > 0)
            {
                stopString += busStop.getStopName();
            }
        }
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
}
