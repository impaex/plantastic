package com.example.plantastic;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event {

    public static ArrayList<Event> eventsList = new ArrayList<>();

    public static ArrayList<Event> eventsForDate(LocalDate date)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList)
        {
            if(event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<Event> eventsForDateAndTime(LocalDate date, LocalTime time)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList)
        {
            int eventHour = event.time.getHour();
            int cellHour = time.getHour();
            if(event.getDate().equals(date) && eventHour == cellHour)
                events.add(event);
        }

        return events;
    }

    private String name;
    private String taskId;
    private LocalDate date;
    private LocalTime time;
    private LocalDate endDate;
    private LocalTime endTime;

    public Event(String name, String taskId, LocalDate date, LocalTime time, LocalDate endDate, LocalTime endTime) {

        this.name = name;
        this.taskId = taskId;
        this.date = date;
        this.time = time;
        this.endDate = endDate;
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDateEnd() {
        return endDate;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.endDate = dateEnd;
    }

    public LocalTime getTimeEnd() {
        return endTime;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.endTime = timeEnd;
    }
}
