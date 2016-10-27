package edu.unt.transportation.bustrackingsystem.model;

import java.util.Date;
import java.util.List;

/**
 * Created by gdawg on 09/27/2016.
 */
public class BusStop
{
    private int mStopID;
    private String mStopName;
    private double mLatitude;
    private double mLongitude;
    private List<Date> scheduledStops;

    public BusStop(int i, double latitude, double longitude)
    {
        this.mStopID = i;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public BusStop()
    {

    }

    public BusStop(int id)
    {
        this.mStopID = id;
    }

    public List<Date> getScheduledStops()
    {
        return scheduledStops;
    }

    public void setScheduledStops(List<Date> scheduledStops)
    {
        this.scheduledStops = scheduledStops;
    }

    public double getmLatitude()
    {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude)
    {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude()
    {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude)
    {
        this.mLongitude = mLongitude;
    }

    public int getmStopID()
    {
        return mStopID;
    }

    public void setmStopID(int mStopID)
    {
        this.mStopID = mStopID;
    }

    public String getmStopName()
    {
        return mStopName;
    }

    public void setmStopName(String mStopName)
    {
        this.mStopName = mStopName;
    }
}
