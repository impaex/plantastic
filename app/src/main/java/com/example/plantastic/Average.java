package com.example.plantastic;

import java.util.ArrayList;

public class Average {

    public static ArrayList<Average> averages = new ArrayList<>();

    private String id, name;
    private long sum, count;

    public static Average getAverage(String name) {
        for (Average avg : averages) {
            if (avg.name.equals(name)) {
                return avg;
            }
        }
        return null;
    }

    public Average(String id, String name){
        this.id = id;
        this.name = name;
        this.sum = 0;
        this.count = 0;
    }

    public Average(String id, long sum, long count, String name) {
        this.id = id;
        this.sum = sum;
        this.count = count;
        this.name = name;
    }

    public void update(long updateSum) {
        this.sum += updateSum;
        this.count++;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public long getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
