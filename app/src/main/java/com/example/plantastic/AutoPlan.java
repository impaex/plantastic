package com.example.plantastic;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Duration;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;

public class AutoPlan {

    private static DatabaseReference reference;

    /**
     * For a given task it automatically plans events to work on the task based on
     * an estimate of time given by the average that other people spend on it.
     *
     * @param onlineUserID The id of the user that plans the task
     * @param deadline The deadline of the task
     * @param name The name of the task
     * @param taskId The id of the task
     * @param location The location of the task
     * @param notes The notes of the task
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void AutoPlan(String onlineUserID, LocalDateTime deadline, String name, String taskId, String location, String notes) {

        reference = FirebaseDatabase.getInstance().getReference().child("events").child(onlineUserID);

        LocalDateTime now = LocalDateTime.now();

        // Get a list of all events between now and the deadline
        ArrayList<EventObject> events = new ArrayList<>();
        for (EventObject event : EventObject.eventsList) {
            LocalDateTime start = event.getStartDate().atTime(event.getStartTime());
            LocalDateTime end = event.getEndDate().atTime(event.getEndTime());
            if (Duration.between(now, end).toMinutes() > 0 && Duration.between(start, deadline).toMinutes() > 0) {
                events.add(event);
            }
        }

        // Sort this list in chronological order
        Collections.sort(events, new Comparator<EventObject>() {
            @Override
            public int compare(EventObject e1, EventObject e2) {
                if (Duration.between(e1.getStartDate().atTime(e1.getStartTime()), e2.getStartDate().atTime(e2.getStartTime())).toMinutes() != 0) {
                    return (int)Duration.between(e1.getStartDate().atTime(e1.getStartTime()), e2.getStartDate().atTime(e2.getStartTime())).toMinutes();
                } else {
                    return (int)Duration.between(e1.getEndDate().atTime(e1.getEndTime()), e2.getEndDate().atTime(e2.getEndTime())).toMinutes();
                }
            }
        });

        ArrayList<Interval> intervals = new ArrayList<>(); // A list of intervals meant to store free timeslots between events

        // If there is time between now and the first event make intervals from it
        if (events.size() > 0 && Duration.between(now, events.get(0).getStartDate().atTime(events.get(0).getStartTime())).toMinutes() > 0) {
            int days = (int)Duration.between(now.toLocalDate().atStartOfDay(), events.get(0).getStartDate().atStartOfDay()).toDays();
            if (days > 0) {
                intervals.add(new Interval(now.toLocalTime(), LocalTime.MAX, now.toLocalDate()));
                for (int i = 1; i < days; i++) {
                    intervals.add(new Interval(LocalTime.MIN, LocalTime.MAX, now.toLocalDate().plusDays(i)));
                }
                intervals.add(new Interval(LocalTime.MIN, events.get(0).getStartTime(), events.get(0).getStartDate()));
            } else {
                intervals.add(new Interval(now.toLocalTime(), events.get(0).getStartTime(), now.toLocalDate()));
            }
        }

        // If there is time between the events make intervals from it
        for (int i = 1; i < events.size(); i++) {
            EventObject e1 = events.get(i-1);
            EventObject e2 = events.get(i);
            if (Duration.between(e1.getEndDate().atTime(e1.getEndTime()), e2.getStartDate().atTime(e2.getStartTime())).toMinutes() > 0) {
                int days = (int)Duration.between(e1.getEndDate().atStartOfDay(), e2.getStartDate().atStartOfDay()).toDays();
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

        // If there is time between the last event and the deadline make intervals from it
        if (events.size() > 0 && Duration.between(events.get(0).getStartDate().atTime(events.get(0).getStartTime()), deadline).toMinutes() > 0) {
            int days = (int)Duration.between(events.get(0).getStartDate().atStartOfDay(), deadline.toLocalDate().atStartOfDay()).toDays();
            if (days > 0) {
                intervals.add(new Interval(events.get(events.size()-1).getEndTime(), LocalTime.MAX, events.get(events.size()-1).getEndDate()));
                for (int i = 1; i < days; i++) {
                    intervals.add(new Interval(LocalTime.MIN, LocalTime.MAX, events.get(events.size()-1).getEndDate().plusDays(i)));
                }
                intervals.add(new Interval(LocalTime.MIN, deadline.toLocalTime(), deadline.toLocalDate()));
            } else {
                intervals.add(new Interval(events.get(events.size()-1).getStartTime(), deadline.toLocalTime(), deadline.toLocalDate()));
            }
        }

        // If there were no events make intervals from now until the deadline
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

        // Take out the really small deadlines because these are impractical
        int minLength = 10; // minimal of 10 minutes as interval
        for (int i = intervals.size()-1; i >= 0; i--) {
            if (intervals.get(i).getLength() < minLength) {
                intervals.remove(i);
            }
        }

        // Store the total sum of free time for each day from now until the deadline and the total time of all free time
        int[] totalTimes = new int[(int)Duration.between(now.toLocalDate().atStartOfDay(), deadline.toLocalDate().atStartOfDay()).toDays() + 1];
        int total = 0;
        for (Interval interval : intervals) {
            totalTimes[(int)Duration.between(now.toLocalDate().atStartOfDay(), interval.getDate().atStartOfDay()).toDays()] += interval.getLength();
            total += interval.getLength();
        }

        // Estimate the workload based on the average of other users with the same task
        // If there is no person in the database with this task take 2 hours (120 minutes) as estimate
        int workloadEstimate = 120;
        Average avg = Average.getAverage(name);
        if (avg != null) {
            workloadEstimate = (int)(avg.getSum()/avg.getCount());
        }

        // If there is too little time to plan everything, don't plan it
        if (workloadEstimate > total) {
            return;
        }

        int alpha = 1; //Parameter to prioritize in the middle of the day
        int beta = 1; //Parameter to prioritize closer to now then to the deadline
        int gamma = 1; //Parameter to prioritize days with more free time

        // Sort all events based on how more likely they are to be in a nice timeslot for the user to work on it
        // For this sorting 3 different aspects get taken into account:
        // - The closer to the middle of the day the interval is, the better
        // - The closer to now it is in comparison to the deadline, the better
        // - The more free time (bigger and more intervals) you have on a day the better it is for those intervals
        // With the parameters above one can tweak how much priority each aspect has and also bring the different
        // orders of size of the numbers closer together to get a better timeslot
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

        // Pick the first so many intervals until you have the total amount of workload covered in those intervals
        // The last may get broken up a bit where it prioritizes to get as close to noon as possible
        ArrayList<Interval> planned = new ArrayList<>();
        int left = workloadEstimate;
        for (Interval interval : intervals) {
            if (left - interval.getLength() > 0) {
                planned.add(interval);
                left -= interval.getLength();
            } else if (Duration.between(interval.getStart(), LocalTime.NOON).toMinutes() > 0 && Duration.between(LocalTime.NOON, interval.getEnd()).toMinutes() > 0) {
                if (Duration.between(interval.getStart(), LocalTime.NOON).toMinutes() < left/2) {
                    planned.add(new Interval(interval.getStart(), interval.getStart().plusMinutes(left), interval.getDate()));
                    break;
                } else if (Duration.between(LocalTime.NOON, interval.getEnd()).toMinutes() < left/2) {
                    planned.add(new Interval(interval.getEnd().minusMinutes(left), interval.getEnd(), interval.getDate()));
                    break;
                } else {
                    planned.add(new Interval(LocalTime.NOON.minusMinutes(left/2), LocalTime.NOON.plusMinutes(left/2), interval.getDate()));
                    break;
                }
            } else if (Duration.between(LocalTime.NOON, interval.getEnd()).toMinutes() > 0) {
                planned.add(new Interval(interval.getStart(), interval.getStart().plusMinutes(left), interval.getDate()));
                break;
            } else {
                planned.add(new Interval(interval.getEnd().minusMinutes(left), interval.getEnd(), interval.getDate()));
                break;
            }
        }

        // Change the intervals in events and store those in the database and event list
        for (Interval interval : planned) {
            String id = reference.push().getKey();
            EventDatabaseObject event = new EventDatabaseObject(name, id, taskId, interval.getDate().toString(), interval.getStart().toString(), interval.getDate().toString(), interval.getEnd().toString(), location, notes);
            reference.child(id).setValue(event);
            EventObject.eventsList.add(EventDatabaseObject.convertToEventObject(event));
        }
    }
}
