package com.example.homeworktracker;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;

import com.example.homeworktracker.model.Class;
import com.example.homeworktracker.model.Homework;

class HomeworkTrackerDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "homeworkTracker";
    private static final int DB_VERSION = 2;
    private static HomeworkTrackerDatabaseHelper sInstance;
    private static final String TAG = "MyActivity";
    private static  final List<Class> classes = new ArrayList<>();

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
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    //updating databases to version 2
    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
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

            db.execSQL("CREATE TABLE REMINDERS ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "HOMEWORK_ID INTEGER , "
                    + "TYPE TEXT);");
        }
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE COMPLETED_HOMEWORK;");
            db.execSQL("CREATE TABLE HOMEWORK_COMPLETED ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "DESCRIPTION TEXT, "
                    + "CLASS_NAME TEXT, "
                    + "TYPE TEXT, "
                    + "DUE_DATE TEXT, "
                    + "DUE_TIME TEXT);");
        }
    }

    // Insert a homework into the database
    public long addHomework(Homework homework1) {
        SQLiteDatabase db = getWritableDatabase();
        long hwId = -1;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("DESCRIPTION", homework1.description );
            values.put("CLASS_NAME", homework1.class_name);
            values.put("TYPE", homework1.type);
            values.put("DUE_DATE", homework1.due_date);
            values.put("DUE_TIME", homework1.due_time);
            values.put("PRIORITY", homework1.priority);
            values.put("REMINDER", homework1.reminder_date_time);
            hwId = db.insertOrThrow("HOMEWORK", null, values);
            db.setTransactionSuccessful();
        }catch (Exception e) {
            Log.d(TAG, "Error while trying to add the homework");
        }finally {
            db.endTransaction();
        }
        return hwId;
    }

    //insert a class into the database
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

    //insert the completed homework into the database
    public long addCompletedHomework(Homework homework2) {
        SQLiteDatabase db = getWritableDatabase();
        long hwId = -1;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("DESCRIPTION", homework2.description );
            values.put("CLASS_NAME", homework2.class_name);
            values.put("TYPE", homework2.type);
            values.put("DUE_DATE", homework2.due_date);
            values.put("DUE_TIME", homework2.due_time);
            hwId = db.insertOrThrow("HOMEWORK_COMPLETED", null, values);
            db.setTransactionSuccessful();
        }catch (Exception e) {
            Log.d(TAG, "Error while trying to add the completed homework");
        }finally {
            db.endTransaction();
        }
        return hwId;
    }

    //for testing the database
    public void deleteAllClassesAndHomework() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("HOMEWORK", null, null);
            db.delete("CLASSES", null, null);
            db.delete("HOMEWORK_COMPLETED", null, null);
            db.setTransactionSuccessful();
        }catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all homework and classes");
        }finally {
            db.endTransaction();
        }
    }

    //displaying all the existing classes in the database
    public List<Class> getAllClasses() {
        //List<Class> classes = new ArrayList<>();
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

    //displaying all the existing homework in the database
    public List<Homework> getAllHomework() {
        List<Homework> homework_list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query ("HOMEWORK",
                new String[] {"DESCRIPTION", "CLASS_NAME", "TYPE", "DUE_DATE", "DUE_TIME", "PRIORITY", "REMINDER"},
                null,
                null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Homework newHomework = new Homework();
                    newHomework.description = cursor.getString(0);
                    newHomework.class_name = cursor.getString(1);
                    newHomework.type = cursor.getString(2);
                    newHomework.due_date = cursor.getString(3);
                    newHomework.due_time = cursor.getString(4);
                    newHomework.priority = cursor.getString(5);
                    newHomework.reminder_date_time = cursor.getString(6);
                    homework_list.add(newHomework);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get list of homework from database");
        }finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return homework_list;
    }

    //displaying all the existing homework in the database
    public List<Homework> getAllCompletedHomework() {
        List<Homework> completed_homework_list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query ("HOMEWORK_COMPLETED",
                new String[] {"DESCRIPTION", "CLASS_NAME", "TYPE", "DUE_DATE", "DUE_TIME"},
                null,
                null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Homework newHomework = new Homework();
                    newHomework.description = cursor.getString(0);
                    newHomework.class_name = cursor.getString(1);
                    newHomework.type = cursor.getString(2);
                    newHomework.due_date = cursor.getString(3);
                    newHomework.due_time = cursor.getString(4);
                    completed_homework_list.add(newHomework);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get list of homework from database");
        }finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return completed_homework_list;
    }

    // deleting a class from the database
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


    //deleting the homework from the database
    public long deleteHomework(Homework homework3) {
        SQLiteDatabase db = getWritableDatabase();
        long hwId = 0;
        db.beginTransaction();
        try {
            hwId = db.delete("HOMEWORK", "DESCRIPTION = ?", new String[] {homework3.description});
            db.setTransactionSuccessful();
        }catch (Exception e) {
            Log.d(TAG, "Error while trying to delete the homework");
        }finally {
            db.endTransaction();
        }
        return hwId;
    }

    //deleting the completed homework from the database
    public long deleteCompletedHomework(Homework homework4) {
        SQLiteDatabase db = getWritableDatabase();
        long cHwId = 0;
        db.beginTransaction();
        try {
            cHwId = db.delete("HOMEWORK_COMPLETED", "DESCRIPTION = ?", new String[] {homework4.description});
            db.setTransactionSuccessful();
        }catch (Exception e) {
            Log.d(TAG, "Error while trying to delete the completed homework");
        }finally {
            db.endTransaction();
        }
        return cHwId;
    }
}
