package com.example.plantastic;

import java.util.ArrayList;

public class Average {

    public static ArrayList<Average> averages = new ArrayList<>();

    private String id, name;
    private long sum, count;

    /**
     * Gives the average object for a given name
     *
     * @param name Name of the event/task of which the average is stored
     * @return {@code (a \in averages :: a.name == name) || (null ==> \forall a \in averages :: a.name != name)}
     */
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

    /**
     * Update the average object when a new event/task is created with {@code updateSum} as time length
     * @param updateSum The amount of time the event/task to add takes and with which the average is updated
     */
    public void update(long updateSum) {
        this.sum += updateSum;
        this.count++;
    }

    /**
     * The same as update, but then implying that the updateSum is equal to the average of the already stored average object
     */
    public void staticUpdate() {
        this.sum += sum/count;
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
