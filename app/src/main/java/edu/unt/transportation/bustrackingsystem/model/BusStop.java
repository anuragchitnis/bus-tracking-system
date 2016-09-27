package edu.unt.transportation.bustrackingsystem.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

/**
 * Created by gdawg on 09/27/2016.
 */
public class BusStop
{
    int id;
    double latitude;
    double longitude;
    List<Date> scheduledStops;

    public BusStop(int i, double latitude, double longitude)
    {
        this.id=i;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public List<Date> getScheduledStops()
    {
        return scheduledStops;
    }

    public void setScheduledStops(List<Date> scheduledStops)
    {
        this.scheduledStops = scheduledStops;
    }

    public LatLng getLatLng(){
        return new LatLng(getLatitude(),getLongitude());
    }
}
