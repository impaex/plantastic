package com.example.plantastic;

import static com.example.plantastic.CalendarUtils.daysInMonthArray;
import static com.example.plantastic.CalendarUtils.monthYearFromDate;
import static com.example.plantastic.CalendarUtils.selectedDate;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantastic.login.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class MonthlyView extends AppCompatActivity implements CalendarAdapter.onItemListener, NavigationView.OnNavigationItemSelectedListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String onlineUserID;


    // Function to initialize the view once it's opened.
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_monthly_view);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_monthly);

        //Retrieving Events from database
        if (Event.eventsList.size() == 0) {
            retrieveEvents();
        }

        if (Average.averages.size() == 0) {
            retrieveAverage();
        }

        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();
    }

    // Since the monthly view is the first view, the event loading logic is placed here.
    // This only runs if the eventlist is empty.
    // This pulls the events from the firebase realtime database and adds them to the local eventlist.
    private void retrieveEvents() {

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        onlineUserID = mUser.getUid();

        FirebaseDatabase.getInstance().getReference().child("tasks").child(onlineUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    eventHelper event = snapshot.getValue(eventHelper.class);

                    String name = event.getName();
                    String taskId = event.getTaskId();
                    LocalDate localDate = LocalDate.parse(event.getStartDate());
                    LocalTime localTime = LocalTime.parse(event.getStartTime());
                    LocalDate endDate = LocalDate.parse(event.getEndDate());
                    LocalTime endTime = LocalTime.parse(event.getEndTime());


                    Event newEvent = new Event(name, taskId, localDate, localTime, endDate, endTime);
                    Event.eventsList.add(newEvent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//
//        reference = FirebaseDatabase.getInstance().getReference("tasks");
//        reference.child(onlineUserID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//
//                if (task.isSuccessful()){
//
//                    if (task.getResult().exists()){
//                        DataSnapshot dataSnapshot = task.getResult();
//                        String eventName = String.valueOf(dataSnapshot.child("name"));
//                        String eventDate = String.
//
//                    }
//
//                }
//                else{
//                    Toast.makeText(getApplicationContext(), "Couldn't sync tasks, check internet connection", Toast.LENGTH_LONG).show();
//                }
//
//            }
//        });

    }

    private void retrieveAverage() {
        FirebaseDatabase.getInstance().getReference().child("average").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    averageHelper avg = snapshot.getValue(averageHelper.class);
                    Average avg2 = new Average(avg.getId(), avg.getSum(), avg.getCount(), avg.getName());
                    Average.averages.add(avg2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // This function is responsible for loading in the monthly view as a whole.
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray();

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    // This function initializes the widgets.
    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    // Button function to go one month back.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    // Button function to go one month forward.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    // The function for when you click on a date.
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            CalendarUtils.selectedDate = date;
            startActivity(new Intent(this, DailyViewActivity.class));
        }
    }

    public void weeklyAction(View view) {
        startActivity(new Intent(this, WeeklyViewActivity.class));
    }

    // Whenever the back button is pressed within the app, only toggles the drawer.
    // This is here to prevent you to go back to the login screen and logging yourself out.
    // TODO: When coming from any other view, make the back button behave normally.
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

    }

    // This is the drawer click logic.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_daily:
                Intent intent = new Intent(getApplicationContext(), DailyViewActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_monthly:
                break;
            case R.id.nav_weekly:
                Intent intent1 = new Intent(getApplicationContext(), WeeklyViewActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_logout:
                Intent intent2 = new Intent(getApplicationContext(), Login.class);
                startActivity(intent2);
                break;

        }

        return true;
    }
}