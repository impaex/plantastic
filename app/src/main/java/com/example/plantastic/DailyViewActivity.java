package com.example.plantastic;

import static com.example.plantastic.CalendarUtils.selectedDate;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.example.plantastic.login.Login;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class DailyViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView monthDayText;
    private TextView dayOfWeekView;
    private ListView hourListView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_daily_view);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setCheckedItem(R.id.nav_daily);


        navigationView.setNavigationItemSelectedListener(this);

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
            ArrayList<EventObject> eventObjects = EventObject.eventsForDateAndTime(selectedDate, time);
            HourEvent hourEvent = new HourEvent(time, eventObjects);
            list.add(hourEvent);
        }
        return list;
    }

    // previous day button
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousDayAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
        setDayView();
    }

    // Next day button
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextDayAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
        setDayView();
    }

    // new event button
    public void newEventAction(View view) {
        startActivity(new Intent(this, EventEditActivity.class));
    }

    // New task button
    public void newTaskAction(View view) {
        startActivity(new Intent(this, taskEditActivity.class));
    }

    // Logic for the drawer.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_daily:
                break;
            case R.id.nav_monthly:
                Intent intent1 = new Intent(getApplicationContext(), MonthlyView.class);
                startActivity(intent1);
                break;
            case R.id.nav_weekly:
                Intent intent = new Intent(getApplicationContext(), WeeklyViewActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                Intent intent2 = new Intent(getApplicationContext(), Login.class);
                startActivity(intent2);
                break;

        }

        return true;
    }
}