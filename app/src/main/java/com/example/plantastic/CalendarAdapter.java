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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);

        holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));

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
