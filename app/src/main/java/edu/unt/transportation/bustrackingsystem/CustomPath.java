package edu.unt.transportation.bustrackingsystem;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gdawg on 09/27/2016.
 */

public class CustomPath implements Serializable
{

    private transient List<LatLng> locations;
    private List<Double> latitudes;
    private List<Double> longitudes;
    private PolylineOptions options;

    public List<LatLng> getLocations()
    {
        if(locations==null && latitudes!=null && longitudes!=null){
            locations = new ArrayList<>();
            for(int i=0;i<latitudes.size();i++){
                LatLng l = new LatLng(latitudes.get(i),longitudes.get(i));
                locations.add(l);
            }
        }
        return locations;
    }

    public void setLocations(List<LatLng> locations)
    {
        this.locations = locations;
        latitudes = new ArrayList<>();
        longitudes = new ArrayList<>();
        for(LatLng location : locations){
            latitudes.add(location.latitude);
            longitudes.add(location.longitude);
        }
    }

    public PolylineOptions getOptions()
    {
        if (options == null)
        {
            setOptions(new PolylineOptions().addAll(getLocations()).width(5).color(Color.CYAN));
        }
        return options;
    }

    public void setOptions(PolylineOptions options)
    {
        this.options = options;
    }

    public void setColor(int color)
    {
        getOptions().color(color);
    }

    public void setWidth(float width)
    {
        getOptions().width(width);
    }

}
