package com.example.homeworktracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentUris;
import com.example.homeworktracker.model.Homework;

public class HomeworkActivity extends AppCompatActivity {

    public static final String EXTRA_HOMEWORKID = "hwId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Get the homework from the intent
        int hwId = (Integer)getIntent().getExtras().get(EXTRA_HOMEWORKID);
        //Create a cursor
        SQLiteOpenHelper HomeworkTrackerDatabaseHelper = new HomeworkTrackerDatabaseHelper(this);
        try {
            SQLiteDatabase db = HomeworkTrackerDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query ("HOMEWORK",
                    new String[] {"DESCRIPTION", "CLASS_NAME", "TYPE", "DUE_DATE", "DUE_TIME", "PRIORITY", "REMINDER"},
                    "_id = ?",
                    new String[] {Integer.toString(hwId)},
                    null, null, null);
            //Move to the first record in the Cursor
            if (cursor.moveToFirst()) {
                //Get the homework details from the cursor
                String descriptionText = cursor.getString(0);
                String class_nameText = cursor.getString(1);
                String typeText = cursor.getString(2);
                String due_dateText = cursor.getString(3);
                String due_timeText = cursor.getString(4);
                String priorityText = cursor.getString(5);
                String reminderText = cursor.getString(6);

                //Populate the hw name
                TextView description = (TextView) findViewById(R.id.hw_description);
                description.setText(descriptionText);

                //Populate the related class name
                TextView related_class = (TextView)findViewById(R.id.related_class_name);
                related_class.setText(class_nameText);

                //Populate the hw type
                TextView type = (TextView)findViewById(R.id.hw_type);
                type.setText(typeText);

                //Populate the hw due date
                TextView due_date = (TextView)findViewById(R.id.hw_due_date);
                due_date.setText(due_dateText);

                //Populate the hw due time
                TextView due_time = (TextView)findViewById(R.id.hw_due_time);
                due_time.setText(due_timeText);

                //Populate the hw priority
                TextView priority = (TextView)findViewById(R.id.hw_priority);
                priority.setText(priorityText);

                //Populate the hw reminder
                TextView reminder = (TextView)findViewById(R.id.hw_reminder);
                reminder.setText(reminderText);
            }
            cursor.close();
            db.close();
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onDeleteHomework(View view) {
        Homework sampleHomework = new Homework();
        TextView description = findViewById(R.id.hw_description);
        TextView related_class = (TextView) findViewById(R.id.related_class_name);
        String descriptionFull = description.getText().toString() + ", " + related_class.getText().toString();
        sampleHomework.description = description.getText().toString();
        try {
            Uri CALENDAR_URI = Uri.parse("content://com.android.calendar/events");
            Cursor cursors = getContentResolver().query(CALENDAR_URI, null, null, null, null);
            if (cursors!= null && cursors.moveToFirst()) {
                while (cursors.moveToNext()) {
                    String desc = cursors.getString(cursors.getColumnIndex("title"));
                    // event id
                    String id = cursors.getString(cursors.getColumnIndex("_id"));
                    if (desc == null) {
                    } else if (desc.equals(descriptionFull)) {
                        Uri uri = ContentUris.withAppendedId(CALENDAR_URI, Integer.parseInt(id));
                        getContentResolver().delete(uri, null, null);
                    }
                }
            }
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Error deleting!", Toast.LENGTH_SHORT);
            toast.show();
        }

        HomeworkTrackerDatabaseHelper databaseHelper = HomeworkTrackerDatabaseHelper.getInstance(this);
        if (databaseHelper.deleteHomework(sampleHomework) == 0) {
            Toast.makeText(this, "Delete Failure", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Delete Success", Toast.LENGTH_SHORT).show();
        }
    }
}