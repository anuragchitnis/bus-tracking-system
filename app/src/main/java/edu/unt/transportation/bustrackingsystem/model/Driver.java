package edu.unt.transportation.bustrackingsystem.model;

import java.io.Serializable;

/**
 * Created by gdawg on 09/27/2016.
 */

public class Driver implements Serializable
{
    private String driverId;
    private String name;
    private String email;
    private String password;

    public Driver()
    {
        //Default constructor to use with firebase
    }

    public Driver(String driverId, String name, String email)
    {
        this.driverId = driverId;
        this.name = name;
        this.email = email;
    }

    public Driver(String driverId, String email)
    {
        this.driverId = driverId;
        this.email = email;
    }

    public Driver(String id)
    {
        this.driverId = id;
    }

    public String getDriverId()
    {
        return driverId;
    }

    public void setDriverId(String driverId)
    {
        this.driverId = driverId;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
