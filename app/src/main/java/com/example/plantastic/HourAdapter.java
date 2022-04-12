package com.example.plantastic;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HourAdapter extends ArrayAdapter<HourEvent>
{
    public HourAdapter(@NonNull Context context, List<HourEvent> hourEvents)
    {
        super(context, 0, hourEvents);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        HourEvent event = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hour_cell, parent, false);
        }
        setHour(convertView, event.time);
        setEvents(convertView, event.eventObjects);
        
        return convertView;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setHour(View convertView, LocalTime time) {
        TextView timeView = convertView.findViewById(R.id.timeView);
        timeView.setText(CalendarUtils.formattedShortTime(time));
    }

    private void setEvents(View convertView, ArrayList<EventObject> eventObjects) {
        TextView event1 = convertView.findViewById(R.id.event1);
        TextView event2 = convertView.findViewById(R.id.event2);
        TextView event3 = convertView.findViewById(R.id.event3);

        if(eventObjects.size() == 0) {
            hideEvent(event1);
            hideEvent(event2);
            hideEvent(event3);
        }
        else if(eventObjects.size() == 1) {
            setEvent(event1, eventObjects.get(0));
            hideEvent(event2);
            hideEvent(event3);
        }
        else if(eventObjects.size() == 2) {
            setEvent(event1, eventObjects.get(0));
            setEvent(event2, eventObjects.get(1));
            hideEvent(event3);
        }
        else if(eventObjects.size() == 3) {
            setEvent(event1, eventObjects.get(0));
            setEvent(event2, eventObjects.get(1));
            setEvent(event3, eventObjects.get(2));
        }
        else {
            setEvent(event1, eventObjects.get(0));
            setEvent(event2, eventObjects.get(1));
            event3.setVisibility(View.VISIBLE);
            event3.setBackgroundColor(Color.GRAY);
            String eventsNotShown = String.valueOf(eventObjects.size() - 2);
            eventsNotShown += " more Events";

            event3.setText(eventsNotShown);

        }
    }

    private void setEvent(TextView textView, EventObject eventObject) {
        textView.setText(eventObject.getName());
        textView.setVisibility(View.VISIBLE);
    }

    private void hideEvent(TextView TextView) {
        TextView.setVisibility(View.INVISIBLE);
    }
}