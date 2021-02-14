package com.example.homeworktracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homeworktracker.model.Homework;

public class CompletedActivity extends AppCompatActivity {

    public static final String EXTRA_COMPLETED_HOMEWORKID = "cHwId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Get the completed homework from the intent
        int cHwId = (Integer)getIntent().getExtras().get(EXTRA_COMPLETED_HOMEWORKID);
        //Create a cursor to access and display completed hw in the database
        SQLiteOpenHelper HomeworkTrackerDatabaseHelper = new HomeworkTrackerDatabaseHelper(this);
        try {
            SQLiteDatabase db = HomeworkTrackerDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query ("HOMEWORK_COMPLETED",
                    new String[] {"DESCRIPTION", "CLASS_NAME", "TYPE", "DUE_DATE", "DUE_TIME"},
                    "_id = ?",
                    new String[] {Integer.toString(cHwId)},
                    null, null, null);
            //Move to the first record in the Cursor
            if (cursor.moveToFirst()) {
                //Get the completed homework details from the cursor
                String descriptionText = cursor.getString(0);
                String class_nameText = cursor.getString(1);
                String typeText = cursor.getString(2);
                String due_dateText = cursor.getString(3);
                String due_timeText = cursor.getString(4);

                //Populate the completed hw name
                TextView description = (TextView) findViewById(R.id.chw_description);
                description.setText(descriptionText);

                //Populate the related class name
                TextView related_class = (TextView)findViewById(R.id.c_related_class_name);
                related_class.setText(class_nameText);

                //Populate the completed hw type
                TextView type = (TextView)findViewById(R.id.chw_type);
                type.setText(typeText);

                //Populate the completed hw due date
                TextView due_date = (TextView)findViewById(R.id.chw_due_date);
                due_date.setText(due_dateText);

                //Populate the completed hw due time
                TextView due_time = (TextView)findViewById(R.id.chw_due_time);
                due_time.setText(due_timeText);
            }
            cursor.close();
            db.close();
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //delete method for completed hw
    public void onDeleteCompletedHomework(View view) {
        Homework sampleHomework = new Homework();
        TextView description = findViewById(R.id.chw_description);
        sampleHomework.description = description.getText().toString();

        //deleting the completed hw from the database
        HomeworkTrackerDatabaseHelper databaseHelper = HomeworkTrackerDatabaseHelper.getInstance(this);
        if(databaseHelper.deleteCompletedHomework(sampleHomework) == 0)
        {
            Toast.makeText(this, "Delete Failure", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Delete Success", Toast.LENGTH_SHORT).show();
        }
    }
}