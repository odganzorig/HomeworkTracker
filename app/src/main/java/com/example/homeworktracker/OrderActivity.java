package com.example.homeworktracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.EditText;
import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.database.Cursor;


public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //populate spinner with class names from Classes table
        Spinner s = (Spinner) findViewById(R.id.related_class);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        SQLiteOpenHelper HomeworkTrackerDatabaseHelper = new HomeworkTrackerDatabaseHelper(this);
        try {
            SQLiteDatabase db = HomeworkTrackerDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("CLASSES",
                    new String[]{"_id", "NAME"},
                    null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    adapter.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onAddHomework(View view) {
        EditText description = findViewById(R.id.description);
        String homeworkDescription = description.getText().toString();

        Spinner relatedClass = (Spinner) findViewById(R.id.related_class);
        String homeworkRelatedClass = String.valueOf(relatedClass.getSelectedItem());

        Spinner type = (Spinner) findViewById(R.id.type);
        String homeworkType = String.valueOf(type.getSelectedItem());

        DatePicker dueDate = (DatePicker) findViewById(R.id.dueDate);
        int startDay = dueDate.getDayOfMonth(); // get the selected day of the month
        String startDay1 = String.valueOf(startDay);
        int startMonth = dueDate.getMonth(); // get the selected month
        String startMonth1 = String.valueOf(startMonth);
        int startYear = dueDate.getYear(); // get the selected year
        String startYear1 = String.valueOf(startYear);

        TimePicker dueTime = (TimePicker)findViewById(R.id.dueTime);
        String hour1, minute1;
        if (Build.VERSION.SDK_INT >= 23 ){
            int hour = dueTime.getHour();
            int minute = dueTime.getMinute();
            hour1 = String.valueOf(hour);
            minute1 = String.valueOf(minute);
        }
        else{
            int hour = dueTime.getCurrentHour();
            int minute = dueTime.getCurrentMinute();
            hour1 = String.valueOf(hour);
            minute1 = String.valueOf(minute);
        }

        Spinner priority = (Spinner) findViewById(R.id.priority);
        String homeworkPriority = String.valueOf(priority.getSelectedItem());

        ContentValues homeworkValues = new ContentValues();
        homeworkValues.put("DESCRIPTION", homeworkDescription);
        homeworkValues.put("CLASS_NAME", homeworkRelatedClass);
        homeworkValues.put("TYPE", homeworkType);
        homeworkValues.put("DUE_DATE", startMonth1 + "/" + startDay1 + "/" + startYear1);
        homeworkValues.put("DUE_TIME", hour1 + ":" + minute1);
        homeworkValues.put("PRIORITY", homeworkPriority);

        SQLiteOpenHelper HomeworkTrackerDatabaseHelper = new HomeworkTrackerDatabaseHelper(this);
        try {
            SQLiteDatabase db = HomeworkTrackerDatabaseHelper.getWritableDatabase();
            db.insert("HOMEWORK", null, homeworkValues);
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}