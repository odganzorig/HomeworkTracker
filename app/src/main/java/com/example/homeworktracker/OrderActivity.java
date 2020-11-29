package com.example.homeworktracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;

import android.provider.AlarmClock;
import android.provider.CalendarContract;
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
import android.app.AlarmManager;
import android.app.PendingIntent;
import java.util.Calendar;

import com.example.homeworktracker.model.Class;
import com.example.homeworktracker.model.Homework;


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
        Homework sampleHomework = new Homework();
        EditText description = findViewById(R.id.description);
        sampleHomework.description = description.getText().toString();

        Spinner relatedClass = (Spinner) findViewById(R.id.related_class);
        sampleHomework.class_name = String.valueOf(relatedClass.getSelectedItem());

        Spinner type = (Spinner) findViewById(R.id.type);
        sampleHomework.type = String.valueOf(type.getSelectedItem());

        DatePicker dueDate = (DatePicker) findViewById(R.id.dueDate);
        int startDay = dueDate.getDayOfMonth(); // get the selected day of the month
        String startDay1 = String.valueOf(startDay);
        int startMonth = dueDate.getMonth(); // get the selected month
        String startMonth1 = String.valueOf(startMonth);
        int startYear = dueDate.getYear(); // get the selected year
        String startYear1 = String.valueOf(startYear);
        sampleHomework.due_date = startMonth1 + "/" + startDay1 + "/" + startYear1;

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
        sampleHomework.due_time = hour1 + ":" + minute1;

        Spinner priority = (Spinner) findViewById(R.id.priority);
        sampleHomework.priority = String.valueOf(priority.getSelectedItem());

        HomeworkTrackerDatabaseHelper databaseHelper = HomeworkTrackerDatabaseHelper.getInstance(this);
        databaseHelper.addHomework(sampleHomework);
    }

    public void createReminder (View view) {
//        Intent intent = new Intent (this, AddReminderActivity.class);
//        startActivity(intent);

//        EditText setDate = (EditText) findViewById(R.id.set_date);
//        EditText setHour = (EditText) findViewById(R.id.set_hour);
//        EditText setMinute = (EditText) findViewById(R.id.set_minute);
//        int date = Integer.parseInt(setDate.getText().toString());
//        int hour = Integer.parseInt(setHour.getText().toString());
//        int minute = Integer.parseInt(setMinute.getText().toString());

        EditText description = findViewById(R.id.description);
        Spinner relatedClass = (Spinner) findViewById(R.id.related_class);
        String reminderDescription = description.getText().toString() + ", " + relatedClass.getSelectedItem() ;

//        Intent reminderIntent = new Intent (AlarmClock.ACTION_SET_ALARM);
//        reminderIntent.putExtra(AlarmClock.EXTRA_DAYS, date);
//        reminderIntent.putExtra(AlarmClock.EXTRA_HOUR, hour);
//        reminderIntent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
//        reminderIntent.putExtra(AlarmClock.EXTRA_MESSAGE, reminderDescription);
//        if(hour <= 24 && minute <= 60){
//            startActivity(reminderIntent);
//        }
        Intent reminderIntent = new Intent (Intent.ACTION_INSERT);
        reminderIntent.setType("vnd.android.cursor.item/event");
        //reminderIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date + hour + minute);
        reminderIntent.putExtra(CalendarContract.Events.HAS_ALARM, true);
        reminderIntent.putExtra(CalendarContract.Events.TITLE, reminderDescription);
        reminderIntent.putExtra(CalendarContract.Reminders.EVENT_ID, CalendarContract.Events._ID);
        reminderIntent.putExtra(CalendarContract.Events.ALLOWED_REMINDERS, "METHOD_DEFAULT");
        reminderIntent.putExtra(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);

        startActivity(reminderIntent);
    }
}