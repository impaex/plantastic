package com.example.plantastic;

import static com.example.plantastic.CalendarUtils.daysInWeekArray;
import static com.example.plantastic.CalendarUtils.monthYearFromDate;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.plantastic.login.Login;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeeklyViewActivity extends AppCompatActivity implements CalendarAdapter.onItemListener, NavigationView.OnNavigationItemSelectedListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_weekly_view);

        // Navbar stuff.
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.nav_weekly);

        navigationView.setNavigationItemSelectedListener(this);

        initWidgets();
        setWeekView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setWeekView() {
        System.out.println(CalendarUtils.selectedDate);
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapater();
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
    }

    // Sets view to a week before.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    // Sets view to a week after.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    // Selects whatever day you clicked on.
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onResume(){
        super.onResume();
        setEventAdapater();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setEventAdapater() {
        ArrayList<EventObject> dailyEventObjects = EventObject.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEventObjects);
        eventListView.setAdapter(eventAdapter);

        // Add the on click event listener here.
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                EventObject event = eventAdapter.getItem(position);
                editTaskFunction(event);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void editTaskFunction(EventObject event) {
        Intent i = new Intent(this, EventEditActivity.class);
        i.putExtra("name", event.getName());
        i.putExtra("id", event.getId());
        i.putExtra("taskId", event.getTaskId());
        i.putExtra("startDate", Formatters.getNumericalDate(event.getStartDate()));
        i.putExtra("startTime", Formatters.getTime(event.getStartTime()));
        i.putExtra("endDate", Formatters.getNumericalDate(event.getEndDate()));
        i.putExtra("endTime", Formatters.getTime(event.getEndTime()));
        i.putExtra("location", event.getLocation());
        i.putExtra("notes", event.getNotes());
        startActivity(i);
    }

    // New event button
    public void newEventAction(View view) {
        startActivity(new Intent(this, EventEditActivity.class));
    }

    // New task button
    public void newTaskAction(View view) {
        startActivity(new Intent(this, taskEditActivity.class));
    }


    public void dailyAction(View view) {
        startActivity(new Intent(this, DailyViewActivity.class));
    }

    // Logic behind the drawer.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_daily:
                Intent intent = new Intent(getApplicationContext(), DailyViewActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_monthly:
                Intent intent1 = new Intent(getApplicationContext(), MonthlyView.class);
                startActivity(intent1);
                break;
            case R.id.nav_weekly:
                break;
            case R.id.nav_logout:
                Intent intent2 = new Intent(getApplicationContext(), Login.class);
                startActivity(intent2);
                break;

        }

        return true;
    }
}