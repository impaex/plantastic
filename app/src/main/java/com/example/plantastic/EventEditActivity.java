package com.example.plantastic;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.Duration;


public class EventEditActivity  extends AppCompatActivity {

    // The variables for the buttons and dialogs.
    private EditText eventNameET, eventLocationET, eventNoteET;
    private Button eventStartDateBTN, eventStartTimeBTN, eventEndDateBTN, eventEndTimeBTN;
    private CheckBox allDay;
    DatePickerDialog startDatePickerDialog;
    DatePickerDialog endDatePickerDialog;
    TimePickerDialog startTimePickerDialog;
    TimePickerDialog endTimePickerDialog;

    LocalDate selectedStartDate;
    LocalTime selectedStartTime;
    LocalDate selectedEndDate;
    LocalTime selectedEndTime;

    // Variables regarding the Firebase database.
    private DatabaseReference reference;
    private DatabaseReference average;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String onlineUserID;


    /**
     * Function to initialize the widgets in the views. It also initiates the
     * date and time buttons with the current date and time.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initWidgets() {
        eventNameET = findViewById(R.id.eventNameET);
        eventStartDateBTN = findViewById(R.id.eventDateStartBTN);
        eventEndDateBTN = findViewById(R.id.eventDateEndBTN);
        eventStartTimeBTN = findViewById(R.id.eventTimeStartBTN);
        eventEndTimeBTN = findViewById(R.id.eventTimeEndBTN);
        allDay = findViewById(R.id.allDayCheckBox);
        eventLocationET = findViewById(R.id.eventLocationET);
        eventNoteET = findViewById(R.id.eventNoteET);

        eventStartDateBTN.setText(Formatters.getTodaysDate(false));
        eventEndDateBTN.setText(Formatters.getTodaysDate(false));

        eventStartTimeBTN.setText(Formatters.getCurrentTime());
        eventEndTimeBTN.setText(Formatters.getTime(LocalTime.now().plusHours(1)));

        selectedStartDate = LocalDate.now();
        selectedEndDate = LocalDate.now();
        selectedStartTime = LocalTime.now();
        selectedEndTime = LocalTime.now().plusHours(1);

        initStartDatePicker();
        initEndDatePicker();

        initStartTimePicker();
        initEndTimePicker();
    }

    /**
     * This function runs when the activity is opened and created.
     *
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        onlineUserID = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("events").child(onlineUserID);
        average = FirebaseDatabase.getInstance().getReference().child("average");
    }

    /**
     *  This function initiates the start date picker and specifies it's behaviour.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initStartDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                LocalDate date = LocalDate.of(year, month+1, day);
                selectedStartDate = date;
                eventStartDateBTN.setText(Formatters.getTextDate(date));
            }
        };

        startDatePickerDialog = new DatePickerDialog(this, dateSetListener, selectedStartDate.getYear(), selectedStartDate.getMonthValue()-1, selectedStartDate.getDayOfMonth());
    }

    /**
     *  This function initiates the end date picker and specifies it's behaviour.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initEndDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                LocalDate date = LocalDate.of(year, month+1, day);
                selectedEndDate = date;
                eventEndDateBTN.setText(Formatters.getTextDate(date));
            }
        };

        endDatePickerDialog = new DatePickerDialog(this, dateSetListener, selectedEndDate.getYear(), selectedEndDate.getMonthValue()-1, selectedEndDate.getDayOfMonth());
    }

    /**
     * This function sets initiates the time picker and specifies it's behaviour.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initStartTimePicker() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                LocalTime time = LocalTime.of(selectedHour, selectedMinute);
                selectedStartTime = time;
                eventStartTimeBTN.setText(Formatters.getTime(time));
            }
        };

        startTimePickerDialog = new TimePickerDialog(this, onTimeSetListener, selectedStartTime.getHour(), selectedStartTime.getMinute(), true);
    }

    /**
     * This function sets initiates the time picker and specifies it's behaviour.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initEndTimePicker() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                LocalTime time = LocalTime.of(selectedHour, selectedMinute);
                selectedEndTime = time;
                eventEndTimeBTN.setText(Formatters.getTime(time));
            }
        };

        endTimePickerDialog = new TimePickerDialog(this, onTimeSetListener, selectedEndTime.getHour(), selectedEndTime.getMinute(), true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveEventAction(View view) {
        String eventName = eventNameET.getText().toString();
        String eventLocation = eventLocationET.getText().toString();
        String eventNotes = eventNoteET.getText().toString();
        String id = reference.push().getKey();
        Boolean isChecked = allDay.isChecked();

        // If the all Day button is checked, times range from 00:00 till 00:00.
        if (isChecked) {
            selectedStartTime = LocalTime.parse("00:00");
            selectedEndTime = LocalTime.parse("00:00");
        }

        String selectedStartDateString = Formatters.getNumericalDate(selectedStartDate);
        String selectedEndDateString = Formatters.getNumericalDate(selectedEndDate);
        String selectedStartTimeString = Formatters.getTime(selectedStartTime);
        String selectedEndTimeString = Formatters.getTime(selectedEndTime);


        // TODO: taskID is now prefilled, should be connected to the selected task in interface.
        String taskID = "";

        EventDatabaseObject EventDatabaseObj = new EventDatabaseObject(eventName, taskID, id, selectedStartDateString, selectedStartTimeString, selectedEndDateString, selectedEndTimeString, eventLocation, eventNotes);
        reference.child(id).setValue(EventDatabaseObj).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(EventEditActivity.this, "Event has been added", Toast.LENGTH_LONG).show();
                }
                else{Toast.makeText(EventEditActivity.this, "Event has not been added, try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        // average stuff

        LocalDateTime start = selectedStartDate.atTime(selectedStartTime);
        LocalDateTime end = selectedEndDate.atTime(selectedEndTime);
        long updateSum = Duration.between(start, end).toMinutes();
        Average avg = Average.getAverage(eventName);
        if (avg != null) {
            avg.update(updateSum);
            average.child(avg.getId()).setValue(avg);
        } else {
            String id2 = average.push().getKey();
            Average avg2 = new Average(id2, eventName);
            avg2.update(updateSum);
            Average.averages.add(avg2);
            average.child(id2).setValue(avg2);
        }


        EventObject.eventsList.add(EventDatabaseObject.convertToEventObject(EventDatabaseObj));
        finish();

    }


    /**
     * Opens the start date picker when the button gets clicked.
     * @param view
     */
    public void openDatePicker(View view) {
        startDatePickerDialog.show();
    }

    /**
     * Opens the end date picker when the button gets clicked.
     * @param view
     */
    public void openDatePicker1(View view) {endDatePickerDialog.show(); }

    /**
     * Opens the end date picker when the button gets clicked.
     * @param view
     */
    public void openTimePicker(View view) {startTimePickerDialog.show(); }

    /**
     * Opens the end date picker when the button gets clicked.
     * @param view
     */
    public void openTimePicker1(View view) {endTimePickerDialog.show(); }

    /**
     * This function turns off the time buttons when 'all-day' is selected.
     * @param view
     */
    public void allDayClicked(View view) {
        if (allDay.isChecked()) {
            eventStartTimeBTN.setVisibility(view.INVISIBLE);
            eventEndTimeBTN.setVisibility(view.INVISIBLE);
        }
        else {
            eventStartTimeBTN.setVisibility(view.VISIBLE);
            eventEndTimeBTN.setVisibility(view.VISIBLE);
        }
    }

}
