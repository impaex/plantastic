package com.example.plantastic;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>{

    private final ArrayList<LocalDate> days;
    private final onItemListener onItemListener;

    public CalendarAdapter(ArrayList<LocalDate> days, CalendarAdapter.onItemListener onItemListener) {
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if(days.size() > 15){ //monthlyView
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        }
        else{ //weeklyView
            layoutParams.height = (int) parent.getHeight();

        }

        return new CalendarViewHolder(view, onItemListener, days);
    }

    // Code responsible for setting the colors of the individual date cells in the view.
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);

        holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));

        // This code gets all the events for the current date.
        ArrayList<EventObject> eventsToday = EventObject.eventsForDate(date);

        // Set all fields invisible first, and make visible in the if statements.
        holder.event1.setVisibility(View.INVISIBLE);
        holder.event2.setVisibility(View.INVISIBLE);
        holder.event3.setVisibility(View.INVISIBLE);

        // Ugly code, but we're in a hurry since we started too late so please just deal with it.
        if (eventsToday.size() > 0) {
            if (eventsToday.size() == 1) {
                holder.event1.setText(eventsToday.get(0).getName());
                holder.event1.setVisibility(View.VISIBLE);
            }
            else if (eventsToday.size() == 2) {
                holder.event1.setText(eventsToday.get(0).getName());
                holder.event2.setText(eventsToday.get(1).getName());
                holder.event1.setVisibility(View.VISIBLE);
                holder.event2.setVisibility(View.VISIBLE);
            }
            else if (eventsToday.size() >= 3) {
                holder.event1.setText(eventsToday.get(0).getName());
                holder.event2.setText(eventsToday.get(1).getName());
                if (eventsToday.size() > 3) {
                    holder.event3.setText((eventsToday.size() - 2) + " more");
                    holder.event3.setBackgroundColor(Color.GRAY);
                }
                else {
                    holder.event3.setText(eventsToday.get(2).getName());
                }
                holder.event1.setVisibility(View.VISIBLE);
                holder.event2.setVisibility(View.VISIBLE);
                holder.event3.setVisibility(View.VISIBLE);
            }
        }


        if(date.equals(CalendarUtils.selectedDate)) {
            holder.parentView.setBackgroundColor(Color.LTGRAY);
        }
        if(date.getMonth().equals(CalendarUtils.selectedDate.getMonth())) {
            holder.dayOfMonth.setTextColor(Color.BLACK);
        } else {
            holder.dayOfMonth.setTextColor(Color.LTGRAY);

        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface onItemListener{
        void onItemClick(int position, LocalDate date);
    }
}
