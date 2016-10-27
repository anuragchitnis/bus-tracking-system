package edu.unt.transportation.bustrackingsystem.model;

import java.util.List;

/**
 * Created by gdawg on 09/27/2016.
 */

public class BusRoute
{
    private int id;
    private String name;
    private List<Vehicle> vehicles;
    private List<BusStop> stops;
    private String lastUpdated;

    public int getId() {
        return id;
    }

    public void setId(int mID) {
        this.id = mID;
    }

    public String getName() {
        return name;
    }

    public BusRoute()
    {
    }

    public BusRoute(int mID)
    {

        this.id = mID;
    }

    public void setName(String mName) {
        this.name = mName;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<BusStop> getStops() {
        return stops;
    }

    public void setStops(List<BusStop> busStops) {
        this.stops = busStops;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
