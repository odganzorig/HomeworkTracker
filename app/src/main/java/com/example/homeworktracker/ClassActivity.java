package com.example.homeworktracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.homeworktracker.model.Class;

public class ClassActivity extends AppCompatActivity {

    public static final String EXTRA_CLASSID = "classId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Get the class from the intent
        int classId = (Integer)getIntent().getExtras().get(EXTRA_CLASSID);

        //Create a cursor
        SQLiteOpenHelper HomeworkTrackerDatabaseHelper = new HomeworkTrackerDatabaseHelper(this);
        try {
            SQLiteDatabase db = HomeworkTrackerDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query ("CLASSES",
                            new String[] {"NAME", "START_DATE", "END_DATE", "INSTRUCTOR_NAME", "CLASS_DAYS", "CLASS_TIME"},
                    "_id = ?",
                            new String[] {Integer.toString(classId)},
                    null, null, null);
            //Move to the first record in the Cursor
            if (cursor.moveToFirst()) {
                //Get the class details from the cursor
                String nameText = cursor.getString(0);
                String start_dateText = cursor.getString(1);
                String end_dateText = cursor.getString(2);
                String instruction_nameText = cursor.getString(3);
                String class_daysText = cursor.getString(4);
                String class_timeText = cursor.getString(5);

                //Populate the class name
                TextView name = (TextView) findViewById(R.id.class_name);
                name.setText(nameText);

                //Populate the class start date
                TextView start_date = (TextView)findViewById(R.id.class_start_date);
                start_date.setText(start_dateText);

                //Populate the class end date
                TextView end_date = (TextView)findViewById(R.id.class_end_date);
                end_date.setText(end_dateText);

                //Populate the class instructor name
                TextView instructor_name = (TextView)findViewById(R.id.instructor_name);
                instructor_name.setText(instruction_nameText);

                //Populate the class days
                TextView class_days = (TextView)findViewById(R.id.classDays);
                class_days.setText(class_daysText);

                //Populate the class time
                TextView class_time = (TextView)findViewById(R.id.class_time);
                class_time.setText(class_timeText);
            }
                cursor.close();
                db.close();
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onDeleteClass(View view) {
        Class sampleClass = new Class();
        TextView name = findViewById(R.id.class_name);
        sampleClass.class_name = name.getText().toString();

        HomeworkTrackerDatabaseHelper databaseHelper = HomeworkTrackerDatabaseHelper.getInstance(this);
        if(databaseHelper.deleteClass(sampleClass) == 0)
        {
            Toast.makeText(this, "Delete Failure", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Delete Success", Toast.LENGTH_SHORT).show();
        }
    }
}