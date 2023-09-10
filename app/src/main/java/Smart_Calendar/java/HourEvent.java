package Smart_Calendar.java;

import java.time.LocalTime;
import java.util.ArrayList;

public class HourEvent
{
    LocalTime time;
    ArrayList<Smart_Calendar.java.Event> events;

    public HourEvent(LocalTime time, ArrayList<Smart_Calendar.java.Event> events)
    {
        this.time = time;
        this.events = events;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public ArrayList<Smart_Calendar.java.Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Smart_Calendar.java.Event> events) {
        this.events = events;
    }
}

