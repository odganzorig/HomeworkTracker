package com.example.homeworktracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import java.text.SimpleDateFormat;
import android.provider.CalendarContract;
import android.util.Log;
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
import android.content.ContentUris;
import java.util.Calendar;
import android.net.Uri;
import android.content.Context;
import android.content.ContentResolver;

import com.example.homeworktracker.model.Homework;


public class OrderActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    private static final int PERMISSION_REQUEST_CODE = 123;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //populate spinner with class names from Classes table in the database
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

    //function for adding new homework to the database
    public void onAddHomework(View view) {
        Homework sampleHomework = new Homework();

        //getting the hw description that the user provided and assigning to sample homework
        EditText description = findViewById(R.id.description);
        sampleHomework.description = description.getText().toString();

        //getting the related class that the user provided and assigning to sample homework
        Spinner relatedClass = (Spinner) findViewById(R.id.related_class);
        sampleHomework.class_name = String.valueOf(relatedClass.getSelectedItem());

        //getting the type of the hw and assigning to sample homework
        Spinner type = (Spinner) findViewById(R.id.type);
        sampleHomework.type = String.valueOf(type.getSelectedItem());

        //getting the due date and assigning to sample homework
        DatePicker dueDate = (DatePicker) findViewById(R.id.dueDate);
        int startDay = dueDate.getDayOfMonth(); // get the selected day of the month
        int startMonth = dueDate.getMonth(); // get the selected month
        int startYear = dueDate.getYear(); // get the selected year
        Calendar calendar = Calendar.getInstance();
        calendar.set(startYear, startMonth, startDay);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        String dueDate1 = sdf.format(calendar.getTime());
        sampleHomework.due_date = dueDate1;

        //getting the due time and assigning to sample homework
        TimePicker dueTime = (TimePicker)findViewById(R.id.dueTime);
        int hour = dueTime.getHour();
        int minute = dueTime.getMinute();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.HOUR_OF_DAY, hour);
        calendar2.set(Calendar.MINUTE, minute);
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        String dueTime1 = sdf2.format(calendar2.getTime());
        sampleHomework.due_time = dueTime1;

        //getting the priority and assigning to sample homework
        Spinner priority = (Spinner) findViewById(R.id.priority);
        sampleHomework.priority = String.valueOf(priority.getSelectedItem());

        //getting the reminder date&time and assigning to sample homework
        DatePicker reminderDate = (DatePicker) findViewById(R.id.reminderDate);
        int reminderDay = reminderDate.getDayOfMonth(); // get the selected day of the month
        String reminderDay1 = String.valueOf(reminderDay);
        int reminderMonth = reminderDate.getMonth(); // get the selected month
        String reminderMonth1 = String.valueOf(reminderMonth);
        int reminderYear = reminderDate.getYear(); // get the selected year
        String reminderYear1 = String.valueOf(reminderYear);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(reminderYear, reminderMonth, reminderDay);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-M-d");
        String reminderDate1 = sdf1.format(calendar1.getTime());

        TimePicker reminderTime = (TimePicker)findViewById(R.id.reminderTime);
        int rHour = reminderTime.getHour();
        int rMinute = reminderTime.getMinute();
        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(Calendar.HOUR_OF_DAY, rHour);
        calendar3.set(Calendar.MINUTE, rMinute);
        SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
        String reminderTime1 = sdf3.format(calendar3.getTime());
        sampleHomework.reminder_date_time = reminderTime1;

        //creating the reminder as soon as user provides the input
        boolean result = checkPermission();
        if (result) {
            createReminder();
        }

        //adding the new hw to the database
        HomeworkTrackerDatabaseHelper databaseHelper = HomeworkTrackerDatabaseHelper.getInstance(this);
        if(databaseHelper.addHomework(sampleHomework) == -1)
        {
            Toast.makeText(this, "Add Failure!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Homework Added!", Toast.LENGTH_SHORT).show();
        }
    }

    //function for creating a reminder on the device based on user's input
    private void createReminder() {
        //accessing all the info required for creating an event reminder
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

        TimePicker reminderStartTime = (TimePicker) findViewById(R.id.reminderTime);
        int startHour, startMinute;
        startHour = reminderStartTime.getHour();
        startMinute = reminderStartTime.getMinute();

        TimePicker dueTime = (TimePicker) findViewById(R.id.dueTime);
        int dueHour, dueMinute;
        dueHour = dueTime.getHour();
        dueMinute = dueTime.getMinute();

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(startYear, startMonth, startDay, startHour, startMinute);
        Calendar endTime = Calendar.getInstance();
        endTime.set(dueYear, dueMonth, dueDay, dueHour, dueMinute);

        //adding the event reminder to the calendar on the device
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        String calendarId = getGmailCalendarId(this);
        values.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
        values.put(CalendarContract.Events.TITLE, reminderTitle);
        values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
        values.put(CalendarContract.Events.HAS_ALARM, 1);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance()
                .getTimeZone().getID());
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        long eventID = Long.parseLong(uri.getLastPathSegment());
        setReminder(cr, eventID, 0);
        Toast.makeText(getApplicationContext(), "Reminder Created", Toast.LENGTH_SHORT).show();
        //syncing the calendar
        syncCalendar(this, calendarId);
    }

    //setting the type of the reminder for the event
    public void setReminder(ContentResolver cr, long eventID, int timeBefore) {
        try {
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Reminders.MINUTES, timeBefore);
            values.put(CalendarContract.Reminders.EVENT_ID, eventID);
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            Uri uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //accessing the user's calendar id so that we can create event reminders
    public String getGmailCalendarId(Context c) {
        String calenderId = "";
        String[] projection = new String[]{"_id", "calendar_displayName"};
        Uri calendars = Uri.parse("content://com.android.calendar/calendars");
        ContentResolver contentResolver = c.getContentResolver();
        Cursor managedCursor = contentResolver.query(calendars,
                projection, null, null, null);
        if (managedCursor != null && managedCursor.moveToFirst()) {
            String calName;
            String calID;
            int nameCol = managedCursor.getColumnIndex(projection[1]);
            int idCol = managedCursor.getColumnIndex(projection[0]);
            do {
                calName = managedCursor.getString(nameCol);
                calID = managedCursor.getString(idCol);
                if (calName.contains("@gmail")) {
                    calenderId = calID;
                    break;
                }
            } while (managedCursor.moveToNext());
            managedCursor.close();
            return calenderId;
        }
        return calenderId;
    }

    //syncing the calendar so that information added can be viewed
    public static void syncCalendar(Context context, String calendarId) {
        try {
            ContentResolver cr = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
            values.put(CalendarContract.Calendars.VISIBLE, 1);

            Uri updateUri = ContentUris.withAppendedId(CalendarContract.Calendars.CONTENT_URI, Long.parseLong(calendarId));
            cr.update(updateUri, values, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //checking if the user granted us the permission to access his calendar
    public boolean checkPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(getApplicationContext(), "Permission already granted.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Permission already granted.");
        }
        else {
            requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, PERMISSION_REQUEST_CODE);}
        return true;
    }

    //requesting permission to access users calendar
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createReminder();
                }  else {
                    Toast.makeText(getApplicationContext(), " Need a Permission.", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}