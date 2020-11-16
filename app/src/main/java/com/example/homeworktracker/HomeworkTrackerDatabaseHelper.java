package com.example.homeworktracker;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

class HomeworkTrackerDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "homeworktracker";
    private static final int DB_VERSION = 1;

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

    }
}
