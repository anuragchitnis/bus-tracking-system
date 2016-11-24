package edu.unt.transportation.bustrackingsystem.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gdawg on 09/27/2016.
 */

public class BusRoute implements Serializable
{
    private String routeId;
    private String routeName;
    private Map<String, Boolean> vehicleMap;
    private Map<String, Boolean> busStopMap;
    private String lastUpdated;
    @Exclude
    private HashMap<String, BusStop> busStopObjectMap;
    @Exclude
    private HashMap<String, Vehicle> vehicleObjectMap;

    public BusRoute()
    {
        //Default constructor to use with firebase
    }

    public BusRoute(String routeId, String routeName, Map<String, Boolean> vehicleMap,
                    Map<String, Boolean> busStopMap, String lastUpdated)
    {
        this.routeId = routeId;
        this.routeName = routeName;
        this.vehicleMap = vehicleMap;
        this.busStopMap = busStopMap;
        this.lastUpdated = lastUpdated;
    }

    public Map<String, Boolean> getBusStopMap()
    {
        return busStopMap;
    }

    public void setBusStopMap(Map<String, Boolean> busStopMap)
    {
        this.busStopMap = busStopMap;
    }

    public HashMap<String, BusStop> getBusStopObjectMap()
    {
        if (busStopObjectMap == null) busStopObjectMap = new HashMap<>();
        return busStopObjectMap;
    }

    public void setBusStopObjectMap(HashMap<String, BusStop> busStopObjectMap)
    {
        this.busStopObjectMap = busStopObjectMap;
    }

    public String getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    public String getRouteId()
    {
        return routeId;
    }

    public void setRouteId(String routeId)
    {
        this.routeId = routeId;
    }

    public String getRouteName()
    {
        return routeName;
    }

    public void setRouteName(String routeName)
    {
        this.routeName = routeName;
    }

    public Map<String, Boolean> getVehicleMap()
    {
        return vehicleMap == null ? new HashMap<String, Boolean>() : vehicleMap;
    }

    public void setVehicleMap(Map<String, Boolean> vehicleMap)
    {
        this.vehicleMap = vehicleMap;
    }

    public HashMap<String, Vehicle> getVehicleObjectMap()
    {
        return vehicleObjectMap;
    }

    public void setVehicleObjectMap(HashMap<String, Vehicle> vehicleObjectMap)
    {
        this.vehicleObjectMap = vehicleObjectMap;
    }

    public void putBusStop(String id, BusStop stop)
    {
        if (busStopObjectMap == null) busStopObjectMap = new HashMap<>();
        busStopObjectMap.put(id, stop);
    }

    public void putVehicle(String id, Vehicle vehicle)
    {
        if (vehicleObjectMap == null) vehicleObjectMap = new HashMap<>();
        vehicleObjectMap.put(id, vehicle);
    }
}
