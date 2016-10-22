package edu.unt.transportation.bustrackingsystem.model;

import java.util.Map;

/**
 * Created by gdawg on 09/27/2016.
 */

public class BusRoute
{
    private String routeId;
    private String routeName;
    private Map<String, Boolean> vehicleMap;
    private Map<String, Boolean> busStopMap;
    private String lastUpdated;


    public BusRoute() {
        //Default constructor to use with firebase
    }

    public BusRoute(String routeId, String routeName, Map<String, Boolean> vehicleMap, Map<String, Boolean> busStopMap, String lastUpdated) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.vehicleMap = vehicleMap;
        this.busStopMap = busStopMap;
        this.lastUpdated = lastUpdated;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public Map<String, Boolean> getVehicleMap() {
        return vehicleMap;
    }

    public void setVehicleMap(Map<String, Boolean> vehicleMap) {
        this.vehicleMap = vehicleMap;
    }

    public Map<String, Boolean> getBusStopMap() {
        return busStopMap;
    }

    public void setBusStopMap(Map<String, Boolean> busStopMap) {
        this.busStopMap = busStopMap;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
