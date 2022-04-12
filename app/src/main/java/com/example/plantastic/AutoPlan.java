package com.example.plantastic;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Duration;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;

public class AutoPlan {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<EventObject> AutoPlan(LocalDateTime deadline, String name, String taskId) {

        LocalDateTime now = LocalDateTime.now();

        System.out.println("test1 "+now.toString());
        ArrayList<EventObject> events = new ArrayList<>();
        for (EventObject event : EventObject.eventsList) {
            LocalDateTime start = event.getStartTime().atTime(event.getStartDate());
            LocalDateTime end = event.getEndDate().atTime(event.getEndTime());
            if (Duration.between(now, end).toMinutes() > 0 && Duration.between(start, deadline).toMinutes() > 0) {
                events.add(event);
            }
        }
        System.out.println("test2 "+events.size());

        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                if (Duration.between(e1.getDate().atTime(e1.getTime()), e2.getDate().atTime(e2.getTime())).toMinutes() != 0) {
                    return (int)Duration.between(e1.getDate().atTime(e1.getTime()), e2.getDateEnd().atTime(e2.getTime())).toMinutes();
                } else {
                    return (int)Duration.between(e1.getDateEnd().atTime(e1.getTimeEnd()), e2.getDateEnd().atTime(e2.getTimeEnd())).toMinutes();
                }
            }
        });

        ArrayList<Interval> intervals = new ArrayList<>();

        if (events.size() > 0 && Duration.between(now, events.get(0).getDate().atTime(events.get(0).getTime())).toMinutes() > 0) {
            int days = (int)Duration.between(now.toLocalDate().atStartOfDay(), events.get(0).getDate().atStartOfDay()).toDays();
            if (days > 0) {
                intervals.add(new Interval(now.toLocalTime(), LocalTime.MAX, now.toLocalDate()));
                for (int i = 1; i < days; i++) {
                    intervals.add(new Interval(LocalTime.MIN, LocalTime.MAX, now.toLocalDate().plusDays(i)));
                }
                intervals.add(new Interval(LocalTime.MIN, events.get(0).getTime(), events.get(0).getDate()));
            } else {
                intervals.add(new Interval(now.toLocalTime(), events.get(0).getTime(), now.toLocalDate()));
            }
        }

        for (int i = 1; i < events.size(); i++) {
            Event e1 = events.get(i-1);
            Event e2 = events.get(i);
            if (Duration.between(e1.getDateEnd().atTime(e1.getTimeEnd()), e2.getDate().atTime(e2.getTime())).toMinutes() > 0) {
                int days = (int)Duration.between(e1.getDateEnd().atStartOfDay(), e2.getDate().atStartOfDay()).toDays();
                if (days > 0) {
                    intervals.add(new Interval(e1.getTimeEnd(), LocalTime.MAX, e1.getDateEnd()));
                    for (int j = 1; j < days; j++) {
                        intervals.add(new Interval(LocalTime.MIN, LocalTime.MAX, e1.getDateEnd().plusDays(j)));
                    }
                    intervals.add(new Interval(LocalTime.MIN, e2.getTime(), e2.getDate()));
                } else {
                    intervals.add(new Interval(e1.getTimeEnd(), e2.getTime(), e1.getDateEnd()));
                }
            }
        }

        if (events.size() > 0 && Duration.between(events.get(0).getDate().atTime(events.get(0).getTime()), deadline).toMinutes() > 0) {
            int days = (int)Duration.between(events.get(0).getDate().atStartOfDay(), deadline.toLocalDate().atStartOfDay()).toDays();
            if (days > 0) {
                intervals.add(new Interval(events.get(events.size()-1).getTimeEnd(), LocalTime.MAX, events.get(events.size()-1).getDateEnd()));
                for (int i = 1; i < days; i++) {
                    intervals.add(new Interval(LocalTime.MIN, LocalTime.MAX, events.get(events.size()-1).getDateEnd().plusDays(i)));
                }
                intervals.add(new Interval(LocalTime.MIN, deadline.toLocalTime(), deadline.toLocalDate()));
            } else {
                intervals.add(new Interval(events.get(events.size()-1).getTime(), deadline.toLocalTime(), deadline.toLocalDate()));
            }
        }

        if(events.size() == 0) {
            int days = (int)Duration.between(now.toLocalDate().atStartOfDay(), deadline.toLocalDate().atStartOfDay()).toDays();
            if (days > 0) {
                intervals.add(new Interval(now.toLocalTime(), LocalTime.MAX, now.toLocalDate()));
                for (int i = 1; i < days; i++) {
                    intervals.add(new Interval(LocalTime.MIN, LocalTime.MAX, now.toLocalDate().plusDays(i)));
                }
                intervals.add(new Interval(LocalTime.MIN, deadline.toLocalTime(), deadline.toLocalDate()));
            } else {
                intervals.add(new Interval(now.toLocalTime(), deadline.toLocalTime(), now.toLocalDate()));
            }
        }

        System.out.println("test3 "+intervals.size());

        int minLength = 10; // minimal of 10 minutes as interval
        for (int i = intervals.size()-1; i >= 0; i--) {
            if (intervals.get(i).getLength() < minLength) {
                intervals.remove(i);
            }
        }

        System.out.println("test4 "+intervals.size()+" "+((int)Duration.between(now.toLocalDate().atStartOfDay(), deadline.toLocalDate().atStartOfDay()).toDays() + 1));

        int[] totalTimes = new int[(int)Duration.between(now.toLocalDate().atStartOfDay(), deadline.toLocalDate().atStartOfDay()).toDays() + 1];
        int total = 0;
        for (Interval interval : intervals) {
            totalTimes[(int)Duration.between(now.toLocalDate().atStartOfDay(), interval.getDate().atStartOfDay()).toDays()] += interval.getLength();
            total += interval.getLength();
        }

        System.out.println("test5 "+totalTimes.length+" "+total);

        int workloadEstimate = 120; //2 hours if there is no estimate yet
        Average avg = Average.getAverage(name);
        if (avg != null) {
            workloadEstimate = (int)(avg.getSum()/avg.getCount());
        }

        System.out.println("test6 "+workloadEstimate);

        if (workloadEstimate > total) { //Too little time to plan everything
            return new ArrayList<Event>();
        }

        int alpha = 1; //Parameter to prioritize in the middle of the day
        int beta = 1; //Parameter to prioritize closer to now then to the deadline
        int gamma = 1; //Parameter to prioritize days with more free time

        Collections.sort(intervals, new Comparator<Interval>() {
            @Override
            public int compare(Interval i1, Interval i2) {
                int value1 = alpha*((int)Math.abs(Duration.between(i1.getStart(), LocalTime.NOON).toMinutes()) + (int)Math.abs(Duration.between(i1.getEnd(), LocalTime.NOON).toMinutes()));
                value1 += beta*((int)Duration.between(now.toLocalDate().atStartOfDay(), i1.getDate().atStartOfDay()).toDays());
                value1 += gamma*totalTimes[(int)Duration.between(now.toLocalDate().atStartOfDay(), i1.getDate().atStartOfDay()).toDays()];

                int value2 = alpha*((int)Math.abs(Duration.between(i2.getStart(), LocalTime.NOON).toMinutes()) + (int)Math.abs(Duration.between(i2.getEnd(), LocalTime.NOON).toMinutes()));
                value2 += beta*((int)Duration.between(now.toLocalDate().atStartOfDay(), i2.getDate().atStartOfDay()).toDays());
                value2 += gamma*totalTimes[(int)Duration.between(now.toLocalDate().atStartOfDay(), i2.getDate().atStartOfDay()).toDays()];

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

        ArrayList<Event> out = new ArrayList<>();
        for (Interval interval : planned) {
            out.add(new Event(name, taskId, interval.getDate(), interval.getStart(), interval.getDate(), interval.getEnd()));
        }

        return out;
    }
}
