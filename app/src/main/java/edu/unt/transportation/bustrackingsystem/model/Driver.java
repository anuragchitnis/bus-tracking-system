package edu.unt.transportation.bustrackingsystem.model;

/**
 * Created by gdawg on 09/27/2016.
 */

public class Driver
{
    int id;
    String name;
    String email;
    String password;
    int currentBusID;

    public int getCurrentBusID()
    {
        return currentBusID;
    }

    public void setCurrentBusID(int currentBusID)
    {
        this.currentBusID = currentBusID;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
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

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
