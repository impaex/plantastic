package com.example.plantastic;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Duration;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;

public class AutoPlan {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<EventObject> AutoPlan(LocalDateTime deadline, String name, String taskId) {

        LocalDateTime now = LocalDateTime.now();

        ArrayList<EventObject> eventObjects = new ArrayList<>();
        for (EventObject eventObject : EventObject.eventsList) {
            LocalDateTime start = eventObject.getStartDate().atTime(eventObject.getStartTime());
            LocalDateTime end = eventObject.getEndDate().atTime(eventObject.getEndTime());
            if (Duration.between(now, end).toMinutes() > 0 && Duration.between(start, deadline).toMinutes() > 0) {
                eventObjects.add(eventObject);
            }
        }

        Collections.sort(eventObjects, new Comparator<EventObject>() {
            @Override
            public int compare(EventObject e1, EventObject e2) {
                if (Duration.between(e1.getStartDate().atTime(e1.getStartTime()), e2.getStartDate().atTime(e2.getStartTime())).toMinutes() != 0) {
                    return (int)Duration.between(e1.getStartDate().atTime(e1.getStartTime()), e2.getEndDate().atTime(e2.getStartTime())).toMinutes();
                } else {
                    return (int)Duration.between(e1.getEndDate().atTime(e1.getEndTime()), e2.getEndDate().atTime(e2.getEndTime())).toMinutes();
                }
            }
        });

        ArrayList<Interval> intervals = new ArrayList<>();

        if (eventObjects.size() > 0 && Duration.between(now, eventObjects.get(0).getStartDate().atTime(eventObjects.get(0).getStartTime())).toMinutes() > 0) {
            int days = (int)Duration.between(now.toLocalDate(), eventObjects.get(0).getStartDate()).toDays();
            if (days > 0) {
                intervals.add(new Interval(now.toLocalTime(), LocalTime.MAX, now.toLocalDate()));
                for (int i = 1; i < days; i++) {
                    intervals.add(new Interval(LocalTime.MIN, LocalTime.MAX, now.toLocalDate().plusDays(i)));
                }
                intervals.add(new Interval(LocalTime.MIN, eventObjects.get(0).getStartTime(), eventObjects.get(0).getStartDate()));
            } else {
                intervals.add(new Interval(now.toLocalTime(), eventObjects.get(0).getStartTime(), now.toLocalDate()));
            }
        }

        for (int i = 1; i < eventObjects.size(); i++) {
            EventObject e1 = eventObjects.get(i-1);
            EventObject e2 = eventObjects.get(i);
            if (Duration.between(e1.getEndDate().atTime(e1.getEndTime()), e2.getStartDate().atTime(e2.getStartTime())).toMinutes() > 0) {
                int days = (int)Duration.between(e1.getEndDate(), e2.getStartDate()).toDays();
                if (days > 0) {
                    intervals.add(new Interval(e1.getEndTime(), LocalTime.MAX, e1.getEndDate()));
                    for (int j = 1; j < days; j++) {
                        intervals.add(new Interval(LocalTime.MIN, LocalTime.MAX, e1.getEndDate().plusDays(j)));
                    }
                    intervals.add(new Interval(LocalTime.MIN, e2.getStartTime(), e2.getStartDate()));
                } else {
                    intervals.add(new Interval(e1.getEndTime(), e2.getStartTime(), e1.getEndDate()));
                }
            }
        }

        if (eventObjects.size() > 0 && Duration.between(eventObjects.get(0).getStartDate().atTime(eventObjects.get(0).getStartTime()), deadline).toMinutes() > 0) {
            int days = (int)Duration.between(eventObjects.get(0).getStartDate(), deadline.toLocalDate()).toDays();
            if (days > 0) {
                intervals.add(new Interval(eventObjects.get(eventObjects.size()-1).getEndTime(), LocalTime.MAX, eventObjects.get(eventObjects.size()-1).getEndDate()));
                for (int i = 1; i < days; i++) {
                    intervals.add(new Interval(LocalTime.MIN, LocalTime.MAX, now.toLocalDate().plusDays(i)));
                }
                intervals.add(new Interval(LocalTime.MIN, deadline.toLocalTime(), deadline.toLocalDate()));
            } else {
                intervals.add(new Interval(eventObjects.get(eventObjects.size()-1).getStartTime(), deadline.toLocalTime(), deadline.toLocalDate()));
            }
        }

        int minLength = 10; // minimal of 10 minutes as interval
        for (int i = intervals.size()-1; i >= 0; i--) {
            if (intervals.get(i).getLength() < minLength) {
                intervals.remove(i);
            }
        }

        int[] totalTimes = new int[(int)Duration.between(now.toLocalDate(), deadline.toLocalDate()).toDays() + 1];
        int total = 0;
        for (Interval interval : intervals) {
            totalTimes[(int)Duration.between(now.toLocalDate(), interval.getDate()).toDays()] += interval.getLength();
            total += interval.getLength();
        }

        int workloadEstimate = 120; //2 hours if there is no estimate yet
        Average avg = Average.getAverage(name);
        if (avg != null) {
            workloadEstimate = (int)(avg.getSum()/avg.getCount());
        }

        if (workloadEstimate > total) { //Too little time to plan everything
            return new ArrayList<EventObject>();
        }

        int alpha = 1; //Parameter to prioritize in the middle of the day
        int beta = 1; //Parameter to prioritize closer to now then to the deadline
        int gamma = 1; //Parameter to prioritize days with more free time

        Collections.sort(intervals, new Comparator<Interval>() {
            @Override
            public int compare(Interval i1, Interval i2) {
                int value1 = alpha*((int)Math.abs(Duration.between(i1.getStart(), LocalTime.NOON).toMinutes()) + (int)Math.abs(Duration.between(i1.getEnd(), LocalTime.NOON).toMinutes()));
                value1 += beta*((int)Duration.between(now.toLocalDate(), i1.getDate()).toDays());
                value1 += gamma*totalTimes[(int)Duration.between(now.toLocalDate(), i1.getDate()).toDays()];

                int value2 = alpha*((int)Math.abs(Duration.between(i2.getStart(), LocalTime.NOON).toMinutes()) + (int)Math.abs(Duration.between(i2.getEnd(), LocalTime.NOON).toMinutes()));
                value2 += beta*((int)Duration.between(now.toLocalDate(), i2.getDate()).toDays());
                value2 += gamma*totalTimes[(int)Duration.between(now.toLocalDate(), i2.getDate()).toDays()];

                return value1 - value2;
            }
        });

        ArrayList<Interval> planned = new ArrayList<>();
        int left = workloadEstimate;
        for (Interval interval : intervals) {
            if (left - interval.getLength() > 0) {
                planned.add(interval);
                left -= interval.getLength();
            } else if (Duration.between(LocalTime.NOON, interval.getEnd()).toMinutes() > 0) {
                planned.add(new Interval(interval.getStart(), interval.getStart().plusMinutes(left), interval.getDate()));
                break;
            } else {
                planned.add(new Interval(interval.getEnd().minusMinutes(left), interval.getEnd(), interval.getDate()));
                break;
            }
        }

        ArrayList<EventObject> out = new ArrayList<>();
        for (Interval interval : planned) {
            // TODO: Uncomment and make sure it contains a location and notes. This is part of the constructor now. This may be an empty string.
            // out.add(new EventObject(name, taskId, interval.getDate(), interval.getStart(), interval.getDate(), interval.getEnd()));
        }

        return out;
    }
}
