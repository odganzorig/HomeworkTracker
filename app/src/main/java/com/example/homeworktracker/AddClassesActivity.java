package com.example.homeworktracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Button;
import android.os.Bundle;
import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

public class AddClassesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        TimePicker classTime = (TimePicker)findViewById(R.id.classTime);
        classTime.setIs24HourView(true);
    }

    public void onAddClass(View view) {
        EditText name = findViewById(R.id.name);
        String className = name.getText().toString();

        DatePicker startDate = (DatePicker) findViewById(R.id.startDate);
        int startDay = startDate.getDayOfMonth(); // get the selected day of the month
        String startDay1 = String.valueOf(startDay);
        int startMonth = startDate.getMonth(); // get the selected month
        String startMonth1 = String.valueOf(startMonth);
        int startYear = startDate.getYear(); // get the selected year
        String startYear1 = String.valueOf(startYear);

        DatePicker endDate = (DatePicker) findViewById(R.id.endDate);
        int endDay = endDate.getDayOfMonth();
        String endDay1 = String.valueOf(endDay);
        int endMonth = endDate.getMonth();
        String endMonth1 = String.valueOf(endMonth);
        int endYear = endDate.getYear();
        String endYear1 = String.valueOf(endYear);

        EditText instructor = findViewById(R.id.instructor);
        String instructorName = instructor.getText().toString();

        Spinner classDays = (Spinner) findViewById(R.id.class_days);
        String daysOfClass = String.valueOf(classDays.getSelectedItem());

        TimePicker classTime = (TimePicker)findViewById(R.id.classTime);
        String hour1, minute1;
        if (Build.VERSION.SDK_INT >= 23 ){
            int hour = classTime.getHour();
            int minute = classTime.getMinute();
            hour1 = String.valueOf(hour);
            minute1 = String.valueOf(minute);
        }
        else{
            int hour = classTime.getCurrentHour();
            int minute = classTime.getCurrentMinute();
            hour1 = String.valueOf(hour);
            minute1 = String.valueOf(minute);
        }

        ContentValues classValues = new ContentValues();
        classValues.put("NAME", className);
        classValues.put("START_DATE", startMonth1 + "/" + startDay1 + "/" + startYear1);
        classValues.put("END_DATE", endMonth1 + "/" + endDay1 + "/" + endYear1);
        classValues.put("INSTRUCTOR_NAME", instructorName);
        classValues.put("CLASS_DAYS", daysOfClass);
        classValues.put("CLASS_TIME", hour1 + ":" + minute1);

        SQLiteOpenHelper HomeworkTrackerDatabaseHelper = new HomeworkTrackerDatabaseHelper(this);
        try {
            SQLiteDatabase db = HomeworkTrackerDatabaseHelper.getWritableDatabase();
            if (db.insert("CLASSES", null, classValues) == -1)
            {
                Toast.makeText(this, "Write Failure", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Write Success", Toast.LENGTH_SHORT).show();
            }
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}