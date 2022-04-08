package com.example.plantastic;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalTime;

public class TaskDatabaseObject {

    /**
     * This class exists allow the database to pull information with.
     */

    private String name;
    private String id;
    private String deadlineDate;
    private String deadlineTime;
    private Boolean autoPlan;
    private String location;
    private String notes;

    public TaskDatabaseObject() {
        // Empty constructor for the database to access.
    }

    public TaskDatabaseObject(String name, String id, String deadlineDate, String deadlineTime, Boolean autoPlan, String location, String notes) {
        this.name = name;
        this.id = id;
        this.deadlineDate = deadlineDate;
        this.deadlineTime = deadlineTime;
        this.autoPlan = autoPlan;
        this.location = location;
        this.notes = notes;
    }

    /**
     * This function is here to easily convert a TaskDataBaseObject into a normal TaskObject.
     *
     * @param Obj the TaskDataBaseObject.
     * @return A new TaskObject.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static TaskObject convertToTaskObject(TaskDatabaseObject Obj) {
        LocalDate deadLineDate = LocalDate.parse(Obj.deadlineDate);
        LocalTime deadLineTime = LocalTime.parse(Obj.deadlineTime);
        return new TaskObject(Obj.name, Obj.id, deadLineDate, deadLineTime, Obj.autoPlan, Obj.location, Obj.notes);
    }

    // All setters and getters.
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

    public String getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public String getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(String deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public Boolean getAutoPlan() {
        return autoPlan;
    }

    public void setAutoPlan(Boolean autoPlan) {
        this.autoPlan = autoPlan;
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
