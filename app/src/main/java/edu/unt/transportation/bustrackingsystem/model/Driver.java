package edu.unt.transportation.bustrackingsystem.model;

/**
 * Created by gdawg on 09/27/2016.
 */

public class Driver
{
    private int mDriverID;
    private String name;
    private String email;
    private String password;

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public int getmDriverID()
    {
        return mDriverID;
    }

    public void setmDriverID(int mDriverID)
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