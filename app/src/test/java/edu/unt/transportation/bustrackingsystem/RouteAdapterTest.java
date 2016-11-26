package edu.unt.transportation.bustrackingsystem;

import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by gdawg on 11/25/2016.
 */
public class RouteAdapterTest
{
    @Test
    public void testDateParse()
    {
        Date date1;
        try
        {
            String string = "5:25PM";
            string = string.replace(" ", "").replace("PM", " PM").replace("AM", " AM");
            date1 = RouteAdapter.dateFormat.parse(string);
            string = "5:25 PM";
            string = string.replace(" ", "").replace("PM", " PM").replace("AM", " AM");
            date1 = RouteAdapter.dateFormat.parse(string);
            string = "5:25 AM";
            string = string.replace(" ", "").replace("PM", " PM").replace("AM", " AM");
            date1 = RouteAdapter.dateFormat.parse(string);
            string = "5:25AM";
            string = string.replace(" ", "").replace("PM", " PM").replace("AM", " AM");
            date1 = RouteAdapter.dateFormat.parse(string);

        } catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
}