package com.example.homeworktracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import java.text.SimpleDateFormat;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import java.util.Date;
import java.sql.Time;
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
import android.content.ContentUris;
import android.app.AlarmManager;
import android.app.PendingIntent;
import java.text.ParseException;
import java.util.Calendar;
import android.net.Uri;
import android.content.ContentResolver;
import com.example.homeworktracker.model.Class;
import com.example.homeworktracker.model.Homework;


public class OrderActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";

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
        String dueHour, dueMinute;
        if (Build.VERSION.SDK_INT >= 23 ){
            int hour = dueTime.getHour();
            int minute = dueTime.getMinute();
            dueHour = String.valueOf(hour);
            dueMinute = String.valueOf(minute);
        }
        else{
            int hour = dueTime.getCurrentHour();
            int minute = dueTime.getCurrentMinute();
            dueHour = String.valueOf(hour);
            dueMinute = String.valueOf(minute);
        }
        sampleHomework.due_time = dueHour + ":" + dueMinute;

        Spinner priority = (Spinner) findViewById(R.id.priority);
        sampleHomework.priority = String.valueOf(priority.getSelectedItem());

        HomeworkTrackerDatabaseHelper databaseHelper = HomeworkTrackerDatabaseHelper.getInstance(this);
        databaseHelper.addHomework(sampleHomework);
    }

    public void createReminder (View view) {

        EditText description = findViewById(R.id.description);
        Spinner relatedClass = (Spinner) findViewById(R.id.related_class);
        String reminderTitle = description.getText().toString() + ", " + relatedClass.getSelectedItem();

        DatePicker startDate = (DatePicker) findViewById(R.id.reminderDate);
        int startDay = startDate.getDayOfMonth();
        int startMonth = startDate.getMonth();
        int startYear = startDate.getYear();

        DatePicker dueDate = (DatePicker) findViewById(R.id.dueDate);
        int dueDay = dueDate.getDayOfMonth();
        int dueMonth = dueDate.getMonth();
        int dueYear = dueDate.getYear();

        TimePicker reminderStartTime = (TimePicker)findViewById(R.id.reminderTime);
        int startHour, startMinute;
        if (Build.VERSION.SDK_INT >= 23 ){
            startHour = reminderStartTime.getHour();
            startMinute = reminderStartTime.getMinute();
        }
        else{
            startHour = reminderStartTime.getCurrentHour();
            startMinute = reminderStartTime.getCurrentMinute();
        }

        TimePicker dueTime = (TimePicker)findViewById(R.id.dueTime);
        int dueHour, dueMinute;
        if (Build.VERSION.SDK_INT >= 23 ){
            dueHour = dueTime.getHour();
            dueMinute = dueTime.getMinute();
        }
        else{
            dueHour = dueTime.getCurrentHour();
            dueMinute = dueTime.getCurrentMinute();
        }

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(startYear, startMonth, startDay, startHour, startMinute);
        Calendar endTime = Calendar.getInstance();
        endTime.set(dueYear, dueMonth, dueDay, dueHour, dueMinute);

        Intent reminderIntent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI);
        reminderIntent.putExtra(CalendarContract.Events.TITLE, reminderTitle);
        reminderIntent.putExtra(CalendarContract.Events.ALL_DAY, false);
        reminderIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
        reminderIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
        reminderIntent.putExtra(CalendarContract.Events.HAS_ALARM, 1);
        reminderIntent.putExtra(CalendarContract.Reminders.MINUTES, 0);
        reminderIntent.putExtra(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        startActivity(reminderIntent);

//
//        Calendar cal = Calendar.getInstance();
//        try{
//            Date reminderStartDate1 = new SimpleDateFormat("MM/dd/yyyy").parse(reminderStartDate.getText().toString());
//            Date reminderEndDate1 = new SimpleDateFormat("MM/dd/yyyy").parse(reminderEndDate.getText().toString());
//            cal.setTime(reminderStartDate1);
//            cal.set(Calendar.HOUR_OF_DAY, startHour);
//            cal.set(Calendar.MINUTE, startMinute);
//            long startCalTime = cal.getTimeInMillis();
//            cal.setTime(reminderEndDate1);
//            cal.set(Calendar.HOUR_OF_DAY, endHour);
//            cal.set(Calendar.MINUTE, endMinute);
//            long endCalTime = cal.getTimeInMillis();
//            Intent reminderIntent = new Intent(Intent.ACTION_INSERT)
//                    .setData(CalendarContract.Events.CONTENT_URI);
//            reminderIntent.putExtra(CalendarContract.Events.TITLE, reminderTitle);
//            reminderIntent.putExtra(CalendarContract.Events.HAS_ALARM, 1);
//            reminderIntent.putExtra(CalendarContract.Events.RDATE, reminderStartDate1);
//            reminderIntent.putExtra(CalendarContract.Events.LAST_DATE, reminderEndDate1);
//            reminderIntent.putExtra(CalendarContract.Events.DTSTART, reminderStartDate1);
//            reminderIntent.putExtra(CalendarContract.Events.DTEND, reminderEndDate1);
//            startActivity(reminderIntent);
//        }catch (ParseException e){
//            e.printStackTrace();
//        }

    }
}