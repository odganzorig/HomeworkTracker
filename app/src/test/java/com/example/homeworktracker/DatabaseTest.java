package com.example.homeworktracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.homeworktracker.model.Class;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(maxSdk = Build.VERSION_CODES.P, minSdk = Build.VERSION_CODES.P)
//@Config(sdk = {Build.VERSION_CODES.O_MR1})
//class SampleTest {}

public class DatabaseTest {
    private HomeworkTrackerDatabaseHelper db;
    private Context context;
    private Class testClass1 = new Class();
    private Class testClass2 = new Class();

    @Before
    public void setUpDatabase() {
        context = ApplicationProvider.getApplicationContext();
        db = HomeworkTrackerDatabaseHelper.getInstance(context);
        testClass1.class_name = "Class1";
        testClass1.start_date = "start_date1";
        testClass1.end_date = "end_date1";
        testClass1.instructor = "instructor1";
        testClass1.class_days = "class_days1";
        testClass1.class_time = "class_time1";
        testClass2.class_name = "Class2";
        testClass2.start_date = "start_date2";
        testClass2.end_date = "end_date2";
        testClass2.instructor = "instructor2";
        testClass2.class_days = "class_days2";
        testClass2.class_time = "class_time2";
        db.addClass(testClass1);
        db.addClass(testClass2);
    }


    @Test
    public void checkClasses() throws Exception {
        //HomeworkTrackerDatabaseHelper db = new HomeworkTrackerDatabaseHelper(null);
        //SQLiteDatabase database = db.getReadableDatabase();
        List <Class> allClasses = db.getAllClasses();
        assertEquals("Class1", allClasses.get(0).class_name);
        assertEquals("start_date1", allClasses.get(0).start_date);
        assertEquals("end_date1", allClasses.get(0).end_date);
        assertEquals("instructor1", allClasses.get(0).instructor);
        assertEquals("class_days1", allClasses.get(0).class_days);
        assertEquals("class_time1", allClasses.get(0).class_time);

        assertEquals("Class2", allClasses.get(1).class_name);
        assertEquals("start_date2", allClasses.get(1).start_date);
        assertEquals("end_date2", allClasses.get(1).end_date);
        assertEquals("instructor2", allClasses.get(1).instructor);
        assertEquals("class_days2", allClasses.get(1).class_days);
        assertEquals("class_time2", allClasses.get(1).class_time);
    }

    @Test
    public void deleteAndCheckClasses() throws Exception {
        db.deleteClass(testClass1);
        List <Class> allClasses = db.getAllClasses();
        assertEquals("Class2", allClasses.get(0).class_name);
    }

    @Test
    public void AddAndCheckClasses() throws Exception {
        db.deleteClass(testClass1);
        db.addClass(testClass1);
        List <Class> allClasses = db.getAllClasses();
        assertEquals("Class2", allClasses.get(0).class_name);
        assertEquals("Class1", allClasses.get(1).class_name);
    }

    @After
    public void createAndCloseDatabase(){
        db.close();
        context.deleteDatabase("homeworkTracker");
    }

}
