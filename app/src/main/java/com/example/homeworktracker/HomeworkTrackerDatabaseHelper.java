package com.example.homeworktracker;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;

import com.example.homeworktracker.model.Class;
import com.example.homeworktracker.model.Homework;

class HomeworkTrackerDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "homeworkTracker";
    private static final int DB_VERSION = 1;
    private static HomeworkTrackerDatabaseHelper sInstance;
    private static final String TAG = "MyActivity";

    public static synchronized HomeworkTrackerDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new HomeworkTrackerDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    HomeworkTrackerDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE HOMEWORK ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "DESCRIPTION TEXT, "
                + "CLASS_NAME TEXT, "
                + "TYPE TEXT, "
                + "DUE_DATE TEXT, "
                + "DUE_TIME TEXT, "
                + "PRIORITY TEXT, "
                + "REMINDER TEXT);");

        db.execSQL("CREATE TABLE CLASSES ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "START_DATE TEXT, "
                + "END_DATE TEXT, "
                + "INSTRUCTOR_NAME TEXT, "
                + "CLASS_DAYS TEXT, "
                + "CLASS_TIME TEXT);");

        db.execSQL("CREATE TABLE COMPLETED_HOMEWORK ("
                + "HOMEWORK_ID INTEGER , "
                + "COMPLETION_DATE INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + "HOMEWORK");
            db.execSQL("DROP TABLE IF EXISTS " + "CLASSES");
            onCreate(db);
        }
    }

    // Insert a post into the database
    public void addHomework(Homework homework) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("DESCRIPTION", homework.description );
            values.put("CLASS_NAME", homework.class_name);
            values.put("TYPE", homework.type);
            values.put("DUE_DATE", homework.due_date);
            values.put("DUE_TIME", homework.due_time);
            values.put("PRIORITY", homework.priority);
            db.insertOrThrow("HOMEWORK", null, values);
            db.setTransactionSuccessful();
        }catch (Exception e) {
            Log.d(TAG, "Error while trying to add homework to database");
        }finally {
            db.endTransaction();
        }
    }

    public long addClass(Class class1) {
        SQLiteDatabase db = getWritableDatabase();
        long classId = -1;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("NAME", class1.class_name);
            values.put("START_DATE", class1.start_date);
            values.put("END_DATE", class1.end_date);
            values.put("INSTRUCTOR_NAME", class1.instructor);
            values.put("CLASS_DAYS", class1.class_days);
            values.put("CLASS_TIME", class1.class_time);
            classId = db.insertOrThrow("CLASSES", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add a class");
        } finally {
            db.endTransaction();
        }
        return classId;
    }

    public void deleteAllClassesAndHomework() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("HOMEWORK", null, null);
            db.delete("CLASSES", null, null);
            db.setTransactionSuccessful();
        }catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all homework and classes");
        }finally {
            db.endTransaction();
        }
    }

    public List<Class> getAllClasses() {
        List<Class> classes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query ("CLASSES",
                new String[] {"NAME", "START_DATE", "END_DATE", "INSTRUCTOR_NAME", "CLASS_DAYS", "CLASS_TIME"},
                null,
                null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Class newClass = new Class();
                    newClass.class_name = cursor.getString(0);
                    newClass.start_date = cursor.getString(1);
                    newClass.end_date = cursor.getString(2);
                    newClass.instructor = cursor.getString(3);
                    newClass.class_days = cursor.getString(4);
                    newClass.class_time = cursor.getString(5);
                    classes.add(newClass);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get classes from database");
        }finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return classes;
    }

    public long deleteClass(Class class2) {
        SQLiteDatabase db = getWritableDatabase();
        long classId = 0;
        db.beginTransaction();
        try {
            classId = db.delete("CLASSES", "NAME = ?", new String[] {class2.class_name});
            db.setTransactionSuccessful();
        }catch (Exception e) {
            Log.d(TAG, "Error while trying to delete the class");
        }finally {
            db.endTransaction();
        }
        return classId;
    }
}
