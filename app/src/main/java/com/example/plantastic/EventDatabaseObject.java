package com.example.plantastic;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventDatabaseObject {

    private String name;
    private String id;
    private String taskId;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String location;
    private String notes;

    public EventDatabaseObject() {
        // Empty constructor for the database to access.
    }

    public EventDatabaseObject(String name, String id, String taskId, String startDate, String startTime, String endDate, String endTime, String location, String notes) {
        this.name = name;
        this.id = id;
        this.taskId = taskId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.notes = notes;

    }

    /**
     * This function is here to easily convert an EventDataBaseObject into a normal eventObject.
     *
     * @param Obj the EventDatabaseObject.
     * @return A new EventObject.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static EventObject convertToEventObject(EventDatabaseObject Obj) {
        LocalDate startDate = LocalDate.parse(Obj.startDate);
        LocalDate endDate = LocalDate.parse(Obj.endDate);
        LocalTime startTime = LocalTime.parse(Obj.startTime);
        LocalTime endTime = LocalTime.parse(Obj.endTime);
        return new EventObject(Obj.name, Obj.id, Obj.taskId, startDate, startTime, endDate, endTime, Obj.location, Obj.notes);
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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
