package com.example.homeworktracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import android.view.View;
import android.widget.Spinner;
import android.widget.EditText;
import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;


public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void onAddHomework(View view) {
        EditText description = findViewById(R.id.description);
        String homeworkDescription = description.getText().toString();
        Spinner type = (Spinner) findViewById(R.id.type);
        String homeworkType = String.valueOf(type.getSelectedItem());
        Spinner priority = (Spinner) findViewById(R.id.priority);
        String homeworkPriority = String.valueOf(priority.getSelectedItem());
        ContentValues homeworkValues = new ContentValues();
        homeworkValues.put("DESCRIPTION", homeworkDescription);
        homeworkValues.put("TYPE", homeworkType);
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