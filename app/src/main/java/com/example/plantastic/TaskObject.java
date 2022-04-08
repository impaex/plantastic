package com.example.plantastic;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TaskObject {

    // Class variable declaration.
    private String name;
    private String id;
    private LocalDate deadlineDate;
    private LocalTime deadlineTime;
    private Boolean autoPlan;
    private String location;
    private String notes;

    // This is the tasksList where we save the tasks locally.
    public static ArrayList<TaskObject> tasksList = new ArrayList<>();


    /**
     * Function which returns all tasks on the given date and hour.
     *
     * @param date A LocalDate object.
     * @param time A LocalTime object.
     * @return An arrayList filled with tasks on the given date and hour.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<TaskObject> tasksForDateAndTime(LocalDate date, LocalTime time) {

        ArrayList<TaskObject> tasksOnList = new ArrayList<>();
        int givenTime = time.getHour();

        for (TaskObject task : tasksList) {
            int taskHour = task.getDeadlineTime().getHour();
            if (task.getDeadlineDate().equals(date) && taskHour == givenTime) {
                tasksOnList.add(task);
            }
        }

        return tasksOnList;
    }


    /**
     * Function which returns all tasks on the given date.
     *
     * @param date A LocalDate object.
     * @return An arrayList filled with tasks on the given date.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<TaskObject> tasksForDate(LocalDate date) {

        ArrayList<TaskObject> tasksOnList = new ArrayList<>();

        for (TaskObject task : tasksList) {
            if (task.getDeadlineDate().equals(date)) {
                tasksOnList.add(task);
            }
        }

        return tasksOnList;
    }


    public TaskObject () {
        // Empty constructor for the database.
    }

    // Constructor.
    public TaskObject(String name, String id, LocalDate deadlineDate, LocalTime deadlineTime, Boolean autoPlan, String location, String notes) {

        this.name = name;
        this.id = id;
        this.deadlineDate = deadlineDate;
        this.deadlineTime = deadlineTime;
        this.autoPlan = autoPlan;
        this.location = location;
        this.notes = notes;
    }

    // This file will include getters and setters, so we can implement the `TaskHelper` class
    // within this class, for optimization.
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

    public LocalDate getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(LocalDate deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public LocalTime getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(LocalTime deadlineTime) {
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
