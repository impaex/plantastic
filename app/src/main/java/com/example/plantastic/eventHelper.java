package com.example.plantastic;

public class eventHelper {

    private String name, date, time, id;

    public eventHelper(){

    }

    public eventHelper(String id, String name, String date, String time) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.id = id;
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

    public void setId(String name) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
