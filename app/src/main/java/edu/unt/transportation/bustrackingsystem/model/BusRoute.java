package edu.unt.transportation.bustrackingsystem.model;

import java.util.List;

/**
 * Created by gdawg on 09/27/2016.
 */

public class BusRoute
{
    int id;
    String name;
    List<BusStop> busStops;

    public List<Bus> getAssignedBuses()
    {
        return assignedBuses;
    }

    public void setAssignedBuses(List<Bus> assignedBuses)
    {
        this.assignedBuses = assignedBuses;
    }

    public List<BusStop> getBusStops()
    {
        return busStops;
    }

    public void setBusStops(List<BusStop> busStops)
    {
        this.busStops = busStops;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    List<Bus> assignedBuses;
}
