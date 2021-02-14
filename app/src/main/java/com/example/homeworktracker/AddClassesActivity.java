package com.example.homeworktracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.homeworktracker.model.Class;

public class AddClassesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //the main function for adding classes to the database
    public void onAddClass(View view) {
        Class sampleClass = new Class();
        //getting the class name that the user provided and assigning to sample class
        EditText name = findViewById(R.id.name);
        sampleClass.class_name = name.getText().toString();

        //getting the start date of the class that the user provided and assigning to sample class
        DatePicker startDate = (DatePicker) findViewById(R.id.startDate);
        int startDay = startDate.getDayOfMonth(); // get the selected day of the month
        String startDayText = String.valueOf(startDay);
        int startMonth = startDate.getMonth(); // get the selected month
        String startMonthText = String.valueOf(startMonth);
        int startYear = startDate.getYear(); // get the selected year
        String startYearText = String.valueOf(startYear);
        sampleClass.start_date = startMonthText + "/" + startDayText + "/" + startYearText;

        //getting the end date of the class and assigning to sample class
        DatePicker endDate = (DatePicker) findViewById(R.id.endDate);
        int endDay = endDate.getDayOfMonth();
        String endDayText = String.valueOf(endDay);
        int endMonth = endDate.getMonth();
        String endMonthText = String.valueOf(endMonth);
        int endYear = endDate.getYear();
        String endYearText = String.valueOf(endYear);
        sampleClass.end_date = endMonthText + "/" + endDayText + "/" + endYearText;

        //getting the instructor name and assigning to sample class
        EditText instructor = findViewById(R.id.instructor);
        sampleClass.instructor = instructor.getText().toString();

        //getting the class days and assigning to sample class
        Spinner classDays = (Spinner) findViewById(R.id.class_days);
        sampleClass.class_days = String.valueOf(classDays.getSelectedItem());

        //getting the class time and assigning to sample class
        TimePicker classTime = (TimePicker)findViewById(R.id.classTime);
        int hour = classTime.getHour();
        int minute = classTime.getMinute();
        String hourText = String.valueOf(hour);
        String minuteText = String.valueOf(minute);
        sampleClass.class_time = hourText + ":" + minuteText;

        //adding the the class to the database and assigning to sample class
        HomeworkTrackerDatabaseHelper databaseHelper = HomeworkTrackerDatabaseHelper.getInstance(this);
        if(databaseHelper.addClass(sampleClass) == -1)
        {
            Toast.makeText(this, "Add Failure!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Class Added!", Toast.LENGTH_SHORT).show();
        }
    }
}