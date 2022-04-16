package com.example.plantastic;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    //Variables defined
    private final ArrayList<LocalDate> days;
    public final View parentView;
    public final TextView dayOfMonth, event1, event2, event3;
    private final CalendarAdapter.onItemListener onItemListener;

    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.onItemListener onItemListener, ArrayList<LocalDate> days) {
        super(itemView);
        parentView = itemView.findViewById(R.id.parentView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        event1 = itemView.findViewById(R.id.eventcell1);
        event2 = itemView.findViewById(R.id.eventcell2);
        event3 = itemView.findViewById(R.id.eventcell3);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
        this.days = days;
    }

    /**
     * When clicked on the button, it receives an onClick event
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(getAdapterPosition(), days.get(getAdapterPosition()));
    }
}
