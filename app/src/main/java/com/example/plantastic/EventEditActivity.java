package com.example.plantastic;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


public class EventEditActivity  extends AppCompatActivity {

    private EditText eventNameET;
    private Button eventDateBTN, eventTimeBTN;
    DatePickerDialog datePickerDialog;

    String date1;
    String monthString;
    String dayString;

    int hour, minute, seconds;
    String hourString, minuteString, secondsString;

    private LocalTime time;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        initDatePicker();
        time = LocalTime.now();
        eventDateBTN.setText("Date: " + getTodaysDate());
        hour = -1;
        formatTime(hour, minute);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void formatTime(int hour, int minute) {
        if(time.getHour() < 10){
            hourString =  "0" + time.getHour();
        }
        else{
            hourString =  "" + time.getHour();
        }
        if(time.getMinute() < 10){
            minuteString =  "0" + time.getMinute();
        }
        else{
            minuteString =  "" + time.getMinute();
        }
        eventTimeBTN.setText("Time: " + hourString + ":" + minuteString);
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        month++;

        if(month < 10){
            monthString = "0" + month;
        }
        else{
            monthString = "" + month;
        }
        if(day < 10){
            dayString = "0" + day;
        }
        else{
            dayString = "" + day;
        }

        date1 = year + "-" + monthString + "-" + dayString;
        return makeDateString(day, month, year);
    }

    private void initWidgets() {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateBTN = findViewById(R.id.eventDateBTN);
        eventTimeBTN = findViewById(R.id.eventTimeBTN1);
    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                String date = makeDateString(day, month, year);

                if(month < 10){
                    monthString = "0" + month;
                }
                else{
                    monthString = "" + month;
                }
                if(day < 10){
                    dayString = "0" + day;
                }
                else{
                    dayString = "" + day;
                }

                date1 = year + "-" + monthString + "-" + dayString;
                System.out.println(date1);
                eventDateBTN.setText("Date: " + date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

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
        LocalDate localDate = LocalDate.parse(date1);
        LocalTime localTime = LocalTime.parse(hourString+":"+minuteString+ ":00");
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
                hour = selectedHour;
                minute = selectedMinute;
                if(hour < 10){
                    hourString =  "0" + hour;
                }
                else{
                    hourString =  "" + hour;
                }
                if(minute < 10){
                    minuteString =  "0" + minute;
                }
                else{
                    minuteString =  "" + minute;
                }
                eventTimeBTN.setText("Time: " + hourString + ":" + minuteString);
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }
}
