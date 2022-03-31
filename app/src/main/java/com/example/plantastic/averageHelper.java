package com.example.plantastic;

import java.util.ArrayList;

public class averageHelper {

    private String id, name;
    private long sum, count;

    public averageHelper(){

    }

    public averageHelper(String id, long sum, long count, String name) {
        this.id = id;
        this.sum = sum;
        this.count = count;
        this.name = name;
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
