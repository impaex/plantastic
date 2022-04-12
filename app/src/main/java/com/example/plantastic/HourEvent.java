package com.example.plantastic;

import java.time.LocalTime;
import java.util.ArrayList;

public class HourEvent {

    LocalTime time;
    ArrayList<EventObject> eventObjects;

    public HourEvent(LocalTime time, ArrayList<EventObject> eventObjects) {
        this.time = time;
        this.eventObjects = eventObjects;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public ArrayList<EventObject> getEvents() {
        return eventObjects;
    }

    public void setEvents(ArrayList<EventObject> eventObjects) {
        this.eventObjects = eventObjects;
    }
}
