package com.example.plantastic;

import static com.example.plantastic.CalendarUtils.selectedDate;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DailyViewActivity extends AppCompatActivity {

    private TextView monthDayText;
    private TextView dayOfWeekView;
    private ListView hourListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_view);
        initWidget(); //finds each of the views by its IDs
    }

    private void initWidget() {
        monthDayText = findViewById(R.id.monthDayText);
        dayOfWeekView = findViewById(R.id.dayOfWeekView);
        hourListView = findViewById(R.id.hourListView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        setDayView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDayView() {
        monthDayText.setText(CalendarUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekView.setText(dayOfWeek);
        setHourAdapter();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setHourAdapter() {
        HourAdapter hourAdapter = new HourAdapter(getApplicationContext(), hourEventList());
        hourListView.setAdapter(hourAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<HourEvent> hourEventList() {
        ArrayList<HourEvent> list = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            LocalTime time = LocalTime.of(hour, 0);
            ArrayList<Event> events = Event.eventsForDateAndTime(selectedDate, time);
            HourEvent hourEvent = new HourEvent(time, events);
            list.add(hourEvent);
        }
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousDayAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
        setDayView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextDayAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
        setDayView();
    }

    public void newEventAction(View view) {
        startActivity(new Intent(this, EventEditActivity.class));
    }
}