package com.example.plantastic;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


public class EventEditActivity  extends AppCompatActivity {

    private EditText eventNameET;
    private Button eventStartDateBTN, eventStartTimeBTN;
    private Button eventEndDateBTN, eventEndTimeBTN;
    DatePickerDialog datePickerDialog;
    DatePickerDialog datePickerDialog1;

    // Strings for saving the data to the database.
    String startDate;
    String startMonthString;
    String startDayString;

    String endDate;
    String endMonthString;
    String endDayString;

    int startHour, startMinute, endHour, endMinute;

    String hourString, minuteString, targetHourString, targetMinuteString;

    private LocalTime timeNow;

    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String onlineUserID;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        initDatePicker();
        initDatePicker1();
        timeNow = LocalTime.now();

        // Sets the date buttons to the current day.
        eventStartDateBTN.setText("Date: " + getTodaysDate());
        eventEndDateBTN.setText("Date: " + getTodaysDate());

        startHour = -1;

        // This formats the current time and adds it to the buttons.
        formatTime(startHour, startMinute);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        onlineUserID = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("tasks").child(onlineUserID);

    }


    // Time formatter function.
    // This also initializes the time buttons with a time.
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void formatTime(int hour, int minute) {
        int currentHour = timeNow.getHour();
        int targetHour = currentHour + 1;

        // Convert the current hour in a nice string.
        if(currentHour < 10) {
            hourString =  "0" + timeNow.getHour();
        }
        else {
            hourString =  "" + timeNow.getHour();
        }

        // Convert the target hour in a nice string.
        if(targetHour < 10) {
            targetHourString =  "0" + timeNow.getHour();
        }
        else {
            targetHourString =  "" + timeNow.getHour();
        }

        // Convert the current minute in a nice string.
        if(timeNow.getMinute() < 10) {
            minuteString =  "0" + timeNow.getMinute();
            targetMinuteString =  "0" + timeNow.getMinute();
        }
        else {
            minuteString =  "" + timeNow.getMinute();
            targetMinuteString =  "" + timeNow.getMinute();
        }

        // Set time buttons to the current time, and current time + 1 hour.
        eventStartTimeBTN.setText("Time: " + hourString + ":" + minuteString);
        eventEndTimeBTN.setText("Time: " + targetHourString +  ":" + targetMinuteString);
    }

    // Function to prettify todays date and return it.
    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        month++;

        if(month < 10){
            startMonthString = "0" + month;
        }
        else{
            startMonthString = "" + month;
        }
        if(day < 10){
            startDayString = "0" + day;
        }
        else{
            startDayString = "" + day;
        }

        startDate = year + "-" + startMonthString + "-" + startDayString;
        return makeDateString(day, month, year);
    }

    // Initializes the buttons in the view.
    private void initWidgets() {
        eventNameET = findViewById(R.id.eventNameET);
        eventStartDateBTN = findViewById(R.id.eventDateStartBTN);
        eventEndDateBTN = findViewById(R.id.eventDateEndBTN);
        eventStartTimeBTN = findViewById(R.id.eventTimeStartBTN);
        eventEndTimeBTN = findViewById(R.id.eventTimeEndBTN);
    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                String date = makeDateString(day, month, year);

                if(month < 10){
                    startMonthString = "0" + month;
                }
                else{
                    startMonthString = "" + month;
                }

                if(day < 10){
                    startDayString = "0" + day;
                }
                else{
                    startDayString = "" + day;
                }

                startDate = year + "-" + startMonthString + "-" + startDayString;
                System.out.println(startDate);
                eventStartDateBTN.setText("Date: " + date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

    }


    private void initDatePicker1() {
        DatePickerDialog.OnDateSetListener dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                String date = makeDateString(day, month, year);

                if(month < 10){
                    endMonthString = "0" + month;
                }
                else{
                    endMonthString = "" + month;
                }

                if(day < 10){
                    endDayString = "0" + day;
                }
                else{
                    endDayString = "" + day;
                }

                endDate = year + "-" + endMonthString + "-" + endDayString;
                eventEndDateBTN.setText("Date: " + date);


            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog1 = new DatePickerDialog(this, style, dateSetListener1, year, month, day);


    }

    // Returns a prettified string of a date.
    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    // Prettifies the month in a date.
    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //Should never happen
        return "JAN";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveEventAction(View view){
        String eventName = eventNameET.getText().toString();
        LocalDate localDate = LocalDate.parse(startDate);
        LocalTime localTime = LocalTime.parse(hourString+":"+minuteString+ ":00");

        String id = reference.push().getKey();
        String startTime = hourString + ":" + minuteString;
        String endTime = targetHourString + ":" + targetMinuteString;

        eventHelper eventHelper = new eventHelper(id, eventName, startDate, endDate, startTime, endTime);
        reference.child(id).setValue(eventHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(EventEditActivity.this, "Task has been added", Toast.LENGTH_LONG).show();
                }
                else{Toast.makeText(EventEditActivity.this, "Task has not been added, try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        Event newEvent = new Event(eventName, localDate, localTime);
        Event.eventsList.add(newEvent);
        finish();

    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    public void openTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                startHour = selectedHour;
                startMinute = selectedMinute;
                if(startHour < 10){
                    hourString =  "0" + startHour;
                }
                else{
                    hourString =  "" + startHour;
                }
                if(startMinute < 10){
                    minuteString =  "0" + startMinute;
                }
                else{
                    minuteString =  "" + startMinute;
                }
                eventStartTimeBTN.setText("Time: " + hourString + ":" + minuteString);
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, startHour, startMinute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }


    public void openTimePicker1(View view) {


    }

    public void openDatePicker1(View view) {datePickerDialog1.show(); }

    public void openTimePicker1(View view) {
    }
}
