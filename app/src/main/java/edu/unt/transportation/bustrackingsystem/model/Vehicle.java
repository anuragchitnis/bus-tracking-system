package edu.unt.transportation.bustrackingsystem.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anurag Chitnis on 10/4/2016.
 */

public class Vehicle {
    private int mID;
    private double mLatitude;
    private double mLongitude;
    private int routeID;
    private int mSpeedKmHr;
    private String mTimestamp;
    private String mVehicleType;
    private Driver mCurrentDriver;
    private Map<Integer, Object> mFirebaseDataMap;

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }


    public String getmVehicleType() {
        return mVehicleType;
    }

    public void setmVehicleType(String mVehicleType) {
        this.mVehicleType = mVehicleType;
    }

    public String getmTimestamp() {
        return mTimestamp;
    }

    public void setmTimestamp(String mTimestamp) {
        this.mTimestamp = mTimestamp;
    }

    public int getmSpeedKmHr() {
        return mSpeedKmHr;
    }

    public void setmSpeedKmHr(int mSpeedKmHr) {
        this.mSpeedKmHr = mSpeedKmHr;
    }

    public int getRouteID() {
        return routeID;
    }

    public void setRouteID(int routeID) {
        this.routeID = routeID;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {

        this.mLatitude = mLatitude;
    }

    public Driver getmCurrentDriver() {
        return mCurrentDriver;
    }

    public void setmCurrentDriver(Driver mCurrentDriver) {
        this.mCurrentDriver = mCurrentDriver;
    }

    public Map<Integer, Object> getJsonDataMap() {
        mFirebaseDataMap = new HashMap<>();
        Map<String, Object> vehichleMap = new HashMap<>();
        vehichleMap.put("latitude",this.mLatitude);
        vehichleMap.put("longitude",this.mLongitude);
        vehichleMap.put("routeID",this.routeID);
        vehichleMap.put("speedKmHr",this.mSpeedKmHr);
        vehichleMap.put("timestamp",this.mTimestamp);
        vehichleMap.put("vehicleType",this.mVehicleType);

        mFirebaseDataMap.put(this.mID,vehichleMap);
        return mFirebaseDataMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vehicle vehicle = (Vehicle) o;

        return mID == vehicle.mID;

    }

    @Override
    public int hashCode() {
        return mID;
    }
}
