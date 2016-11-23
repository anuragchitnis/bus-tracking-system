package edu.unt.transportation.bustrackingsystem.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Anurag Chitnis on 10/4/2016.
 */

@IgnoreExtraProperties
public class Vehicle implements Serializable{
    private String vehicleID;
    private double latitude;
    private double longitude;
    private String routeID;
    private String timestamp;
    private String vehicleType;
    private String driverID;
    private boolean assigned;


    public Vehicle() {
        //Default Constructor for use with firebase
    }

    public Vehicle(String vehicleID, double latitude, double longitude, String routeID, String timestamp, String vehicleType, String driverID) {
        this.vehicleID = vehicleID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.routeID = routeID;
        this.timestamp = timestamp;
        this.vehicleType = vehicleType;
        this.driverID = driverID;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }


    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {

        this.latitude = latitude;
    }

    public String getDriver() {
        return driverID;
    }

    public void setDriver(String driver) {
        this.driverID = driver;
    }

    public void setIsAssigned(boolean isAssigned) {
        this.assigned = isAssigned;
    }

    public boolean isAssigned() {
        return assigned;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Vehicle) {
            Vehicle vehicle = (Vehicle) o;
            if(vehicle.getVehicleID().equals(this.getVehicleID()))
                return true;
        }
        return false;
    }
}
