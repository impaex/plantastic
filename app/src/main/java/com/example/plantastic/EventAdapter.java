package com.example.plantastic;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.List;

public class EventAdapter extends ArrayAdapter<EventObject>
{
    public EventAdapter(@NonNull Context context, List<EventObject> eventObjects)
    {
        super(context, 0, eventObjects);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        EventObject eventObject = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);
        }
        TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);

        String eventTitle = eventObject.getName() +" "+ CalendarUtils.formattedTime(eventObject.getStartTime());
        eventCellTV.setText(eventTitle);
        return convertView;
    }
}