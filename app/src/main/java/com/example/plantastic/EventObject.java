package com.example.plantastic;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class EventObject {

    // Class variable declaration
    private String name;
    private String id;
    private String taskId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String notes;

    // This is the eventsList where we save the events locally.
    public static ArrayList<EventObject> eventsList = new ArrayList<>();


    /**
     * Function which returns all tasks on the given date and hour.
     *
     * @param date A LocalDate object.
     * @param time A localTime object.
     * @return An ArrayList filled with events on the given date and hour.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<EventObject> eventsForDateAndTime(LocalDate date, LocalTime time) {
        ArrayList<EventObject> eventObjects = new ArrayList<>();

        for(EventObject eventObject : eventsList)
        {
            int eventHour = eventObject.startTime.getHour();
            int cellHour = time.getHour();
            if(eventObject.getStartDate().equals(date) && eventHour == cellHour)
                eventObjects.add(eventObject);
        }

        return eventObjects;
    }

    /**
     * Function which returns all events on the given date.
     *
     * @param date A LocalDate object.
     * @return An ArrayList filled with events on the given date.
     */
    public static ArrayList<EventObject> eventsForDate(LocalDate date) {
        ArrayList<EventObject> eventObjects = new ArrayList<>();

        for(EventObject eventObject : eventsList)
        {
            if(eventObject.getStartDate().equals(date))
                eventObjects.add(eventObject);
        }

        return eventObjects;
    }


    public EventObject(String name, String id, String taskId, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String location, String notes) {
        this.name = name;
        this.id = id;
        this.taskId = taskId;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.location = location;
        this.notes = notes;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
