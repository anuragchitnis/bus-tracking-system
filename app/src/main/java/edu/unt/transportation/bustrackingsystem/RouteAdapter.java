package edu.unt.transportation.bustrackingsystem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import edu.unt.transportation.bustrackingsystem.model.BusRoute;

/**
 * Created by gdawg on 10/15/2016.
 */
public class RouteAdapter extends ArrayAdapter<BusRoute>
{
    public RouteAdapter(Context context, int resource)
    {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        BusRoute busRoute = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_template_routes,parent,false);
        }
        TextView routeName = (TextView)convertView.findViewById(R.id.text_view_route_name);
        routeName.setText(busRoute.getName());

        return convertView;
    }
}
