package com.example.plantastic;

public class TaskHelperClass {

    String name, description, notes;


    public TaskHelperClass() {
    }

    public TaskHelperClass(String name, String description, String notes) {
        this.name = name;
        this.description = description;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
