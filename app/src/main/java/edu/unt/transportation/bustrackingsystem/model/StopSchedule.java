package edu.unt.transportation.bustrackingsystem.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Anurag Chitnis on 10/20/2016.
 */

public class StopSchedule implements Serializable {

    private String dayOfWeek;
    private List<String> timingsList;

    public StopSchedule() {

    }

    public StopSchedule(String dayOfWeek, List<String> timingsList) {
        this.dayOfWeek = dayOfWeek;
        this.timingsList = timingsList;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public List<String> getTimingsList() {
        return timingsList;
    }
}
