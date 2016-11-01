package edu.unt.transportation.bustrackingsystem.model;

import java.io.Serializable;

/**
 * Created by gdawg on 09/27/2016.
 */

public class Driver implements Serializable
{
    private String mDriverID;
    private String name;
    private String email;
    private String password;

    public Driver() {
        //Default constructor to use with firebase
    }
    public Driver(String id){
        this.mDriverID=id;
    }
    public Driver(String mDriverID, String name) {
        this.mDriverID = mDriverID;
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getmDriverID()
    {
        return mDriverID;
    }

    public void setmDriverID(String mDriverID)
    {
        this.mDriverID = mDriverID;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
