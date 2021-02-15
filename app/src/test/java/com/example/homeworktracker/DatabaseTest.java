package com.example.homeworktracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import com.example.homeworktracker.model.Class;
import com.example.homeworktracker.model.Homework;

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
    private Homework testHomework1 = new Homework();
    private Homework testHomework2 = new Homework();
    private Homework testHomework3 = new Homework();
    private Homework testHomework4 = new Homework();
    private Homework testHomework5 = new Homework();

    @Before
    public void setUpDatabase() {
        context = ApplicationProvider.getApplicationContext();
        db = HomeworkTrackerDatabaseHelper.getInstance(context);
        //adding classes
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
        //adding homework
        testHomework1.description = "hw1";
        testHomework1.class_name = "macro";
        testHomework1.due_date = "feb 25th, 2021";
        testHomework1.due_time = "3pm";
        testHomework1.type = "reading assignment";
        testHomework1.priority = "high";
        testHomework1.reminder_date_time = "feb 24th, 12pm";

        testHomework2.description = "hw2";
        testHomework2.class_name = "system fundamentals";
        testHomework2.due_date = "feb 28th, 2021";
        testHomework2.due_time = "6pm";
        testHomework2.type = "lab";
        testHomework2.priority = "low";
        testHomework2.reminder_date_time = "feb 26th, 12pm";

        testHomework3.description = "hw3";
        testHomework3.class_name = "history of native americans";
        testHomework3.due_date = "feb 29th, 2021";
        testHomework3.due_time = "5pm";
        testHomework3.type = "paper";
        testHomework3.priority = "very high";
        testHomework3.reminder_date_time = "feb 26th, 12pm";
        db.addHomework(testHomework1);
        db.addHomework(testHomework2);
    }

    @Test
    public void checkClasses() throws Exception {
        //HomeworkTrackerDatabaseHelper db = new HomeworkTrackerDatabaseHelper(null);
        //SQLiteDatabase database = db.getReadableDatabase();
        List <Class> allClasses = db.getAllClasses();
        assertEquals(2, allClasses.size());
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
//        db.deleteClass(testClass1);
        List <Class> allClasses = db.getAllClasses();
//        assertEquals("Class2", allClasses.get(0).class_name);
    }

    @Test
    public void AddAndCheckClasses() throws Exception {
//        db.deleteClass(testClass1);
        //db.addClass(testClass1);
        List <Class> allClasses = db.getAllClasses();
//        assertEquals("Class2", allClasses.get(0).class_name);
//        assertEquals("Class1", allClasses.get(1).class_name);
    }

    @Test
    public void checkHomework() throws Exception {
//        assertEquals("hw1", db.getAllHomework().get(0).description);
        assertEquals(2, db.getAllHomework().size());
//        assertEquals("hw1", allHomework.get(0).description);
//        db.deleteHomework(testHomework1);
//        assertEquals("hw2",allHomework.get(0).description);
    }

    @Test
    public void AddAndCheckHomework() throws Exception {
//        db.addHomework(testHomework1);
//        List <Homework> allHomework = db.getAllHomework();
//        assertEquals("hw1", allHomework.get(0).description);
//        assertEquals("hw3", allHomework.get(1).description);
//        assertEquals("hw2", allHomework.get(2).description);
    }

    @After
    public void createAndCloseDatabase(){
        db.close();
        context.deleteDatabase("homeworkTracker");
    }

}
