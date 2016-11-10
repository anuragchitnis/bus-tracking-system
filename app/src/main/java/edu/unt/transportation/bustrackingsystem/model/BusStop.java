package edu.unt.transportation.bustrackingsystem.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by gdawg on 09/27/2016.
 */
public class BusStop implements Serializable
{
    private String stopID;
    private String stopName;
    private double latitude;
    private double longitude;
    @Exclude
    private Map<String, List<StopSchedule>> routeSchedule;

    public BusStop() {
        //Default constructor to use with firebase
    }

    public BusStop(String stopID, String stopName, double latitude, double longitude) {
        this.stopID = stopID;
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Map<String, List<StopSchedule>> getRouteSchedule() {
        return routeSchedule;
    }

    public void setRouteSchedule(Map<String, List<StopSchedule>> routeSchedule) {
        this.routeSchedule = routeSchedule;
    }

    public String getStopID()
    {
        return stopID;
    }

    public void setStopID(String stopID)
    {
        this.stopID = stopID;
    }

    public String getStopName()
    {
        return stopName;
    }

    public void setStopName(String stopName)
    {
        this.stopName = stopName;
    }
}
