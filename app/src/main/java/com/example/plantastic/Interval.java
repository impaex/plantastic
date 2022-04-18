package com.example.plantastic;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;

import android.os.Build;

import androidx.annotation.RequiresApi;

public class Interval {

    private LocalTime start;
    private LocalTime end;
    private LocalDate date;
    private long length;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Interval(LocalTime start, LocalTime end, LocalDate date){
        this.start = start;
        this.end = end;
        this.date = date;
        this.length = Duration.between(start, end).toMinutes();
    }

    //all getters and setters
    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }
}
