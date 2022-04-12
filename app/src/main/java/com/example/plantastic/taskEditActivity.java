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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


public class taskEditActivity  extends AppCompatActivity {

    // The variables for the buttons and dialogs.
    private EditText taskNameET, taskLocationET, editTaskNoteET;
    private Button taskDateBTN, taskTimeBTN;
    private CheckBox autoPlan;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    LocalDate selectedDate;
    LocalTime selectedTime;

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
        taskNameET = findViewById(R.id.taskNameET);
        taskDateBTN = findViewById(R.id.taskDateBTN);
        taskTimeBTN = findViewById(R.id.taskTimeBTN);
        taskLocationET = findViewById(R.id.taskLocationET);
        editTaskNoteET = findViewById(R.id.editTaskNoteET);
        autoPlan = findViewById(R.id.autoPlanTask);


        taskDateBTN.setText(Formatters.getTodaysDate(false));
        taskTimeBTN.setText(Formatters.getCurrentTime());

        selectedDate = LocalDate.now();
        selectedTime = LocalTime.now();

        initDatePicker();
        initTimePicker();
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
        setContentView(R.layout.activity_task_edit);
        initWidgets();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        onlineUserID = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("tasks").child(onlineUserID);
        average = FirebaseDatabase.getInstance().getReference().child("average");
    }


    /**
     * This function sets initiates the date picker and specifies it's behaviour.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                LocalDate date = LocalDate.of(year, month+1, day);
                selectedDate = date;
                taskDateBTN.setText(Formatters.getTextDate(date));
            }
        };

        datePickerDialog = new DatePickerDialog(this, dateSetListener, selectedDate.getYear(), selectedDate.getMonthValue()-1, selectedDate.getDayOfMonth());

    }


    /**
     * This function sets initiates the time picker and specifies it's behaviour.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initTimePicker() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                LocalTime time = LocalTime.of(selectedHour, selectedMinute);
                selectedTime = time;
                taskTimeBTN.setText(Formatters.getTime(time));
            }
        };

        timePickerDialog = new TimePickerDialog(this, onTimeSetListener, selectedTime.getHour(), selectedTime.getMinute(), true);

    }


    /**
     * This function runs when the user presses the save task button.
     * It saves the task to the database and then adds it to the local storage.
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveTaskAction(View view){
        String taskName = taskNameET.getText().toString();
        String id = reference.push().getKey();
        Boolean isChecked = autoPlan.isChecked();
        String taskLocation = taskLocationET.getText().toString();
        String taskNotes = editTaskNoteET.getText().toString();

        String selectedDateString = Formatters.getNumericalDate(selectedDate);
        String selectedTimeString = Formatters.getTime(selectedTime);


        TaskDatabaseObject tempTask = new TaskDatabaseObject(taskName, id, selectedDateString, selectedTimeString, isChecked, taskLocation, taskNotes);
        reference.child(id).setValue(tempTask).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(taskEditActivity.this, "Task has been added", Toast.LENGTH_LONG).show();
                }
                else{Toast.makeText(taskEditActivity.this, "Task has not been added, try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        // autoplan
        if (isChecked) {
            LocalDateTime deadline = selectedDate.atTime(selectedTime);
            AutoPlan.AutoPlan(onlineUserID, deadline, taskName, id, taskLocation, taskNotes);

            // average stuff
            Average avg = Average.getAverage(taskName);
            if (avg != null) {
                avg.staticUpdate();
                average.child(avg.getId()).setValue(avg);
            } else {
                String id2 = average.push().getKey();
                Average avg2 = new Average(id2, taskName);
                avg2.update(120);
                Average.averages.add(avg2);
                average.child(id2).setValue(avg2);
            }
        }

        TaskObject.tasksList.add(TaskDatabaseObject.convertToTaskObject(tempTask));
        finish();

    }

    /**
     * Opens the date picker when the button gets clicked.
     * @param view
     */
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    /**
     * Opens the time picker when the button gets clicked.
     * @param view
     */
    public void openTimePicker(View view) {
        timePickerDialog.show();
    }
}
