package com.example.homeworktracker;
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


@RunWith(RobolectricTestRunner.class)
@Config(maxSdk = Build.VERSION_CODES.P, minSdk = Build.VERSION_CODES.P)

public class DatabaseTest {
    private HomeworkTrackerDatabaseHelper db;
    private Context context;
    private Class testClass1 = new Class();
    private Class testClass2 = new Class();
    private Homework testHomework1 = new Homework();
    private Homework testHomework2 = new Homework();
    private Homework testHomework3 = new Homework();
    private Homework testHomework4 = new Homework();

    @Before
    public void setUpDatabase() {
        context = ApplicationProvider.getApplicationContext();
        db = HomeworkTrackerDatabaseHelper.getInstance(context);
        //classes
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
        //homework
        testHomework1.description = "hw1";
        testHomework1.class_name = "macro";
        testHomework1.due_date = "feb 25th, 2021";
        testHomework1.due_time = "3pm";
        testHomework1.type = "reading assignment";
        testHomework1.priority = "high";
        testHomework1.reminder_date_time = "feb 24th, 12pm";
        //completed hw
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

        testHomework4.description = "hw4";
        testHomework4.class_name = "micro";
        testHomework4.due_date = "march 29th, 2021";
        testHomework4.due_time = "2pm";
        testHomework4.type = "quiz";
    }

    //test for checking classes
    @Test
    public void checkClasses() throws Exception {
        db.addClass(testClass1);
        db.addClass(testClass2);
        db.deleteClass(testClass1);
        db.addClass(testClass1);
        List <Class> allClasses = db.getAllClasses();
        assertEquals(2, allClasses.size());
        assertEquals("Class2", allClasses.get(0).class_name);
        assertEquals("Class1", allClasses.get(1).class_name);
    }

    //test for checking homework
    @Test
    public void checkHomework() throws Exception {
        db.addHomework(testHomework1);
        db.addHomework(testHomework2);
        db.deleteHomework(testHomework1);
        db.addHomework(testHomework1);
        db.addHomework(testHomework1);
        List <Homework> allHomework = db.getAllHomework();
        assertEquals(3, allHomework.size());
        assertEquals("hw2", allHomework.get(0).description);
        assertEquals("hw1", allHomework.get(1).description);
        assertEquals("hw1", allHomework.get(2).description);
    }

    //test for completed hw
    @Test
    public void checkCompletedHomework() throws Exception {
        db.addCompletedHomework(testHomework3);
        db.addCompletedHomework(testHomework4);
        db.deleteCompletedHomework(testHomework4);
        db.addCompletedHomework(testHomework3);
        db.addCompletedHomework(testHomework4);
        List <Homework> allCompletedHomework = db.getAllCompletedHomework();
        assertEquals(3, allCompletedHomework.size());
        assertEquals("hw3", allCompletedHomework.get(0).description);
        assertEquals("hw3", allCompletedHomework.get(1).description);
        assertEquals("hw4", allCompletedHomework.get(2).description);
    }

    //test for checking empty database
    @Test
    public void checkEmptyDatabase() throws Exception {
        db.addClass(testClass1);
        db.addHomework(testHomework1);
        db.addCompletedHomework(testHomework3);
        db.deleteAllClassesAndHomework();
        assertEquals(0, db.getAllClasses().size());
        assertEquals(0, db.getAllHomework().size());
        assertEquals(0, db.getAllCompletedHomework().size());


    }

    @After
    public void createAndCloseDatabase(){
        db.close();
        context.deleteDatabase("homeworkTracker");
    }

}
