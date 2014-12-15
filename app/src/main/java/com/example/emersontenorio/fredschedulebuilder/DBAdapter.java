package com.example.emersontenorio.fredschedulebuilder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Emerson Tenorio on 12/14/2014.
 */
public class DBAdapter {
    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "scheduledb.sqlite";
    public static final String TABLE_NAME = "course";

    public static final String COL_ID = "_id";
    public static final String COL_CRN = "crn";
    public static final String COL_SUBJECT = "subject";
    public static final String COL_COURSE_NUMBER = "course_number";
    public static final String COL_SECTION = "section";
    public static final String COL_CREDIT = "credit";
    public static final String COL_TITLE = "title";
    public static final String COL_INSTRUCTOR = "instructor";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_START_TIME = "start_time";
    public static final String COL_END_TIME = "end_time";
    public static final String COL_SUNDAY = "sunday";
    public static final String COL_MONDAY = "monday";
    public static final String COL_TUESDAY = "tuesday";
    public static final String COL_WEDNESDAY = "wednesday";
    public static final String COL_THURSDAY = "thursday";
    public static final String COL_FRIDAY = "friday";
    public static final String COL_SATURDAY = "saturday";
    public static final String COL_SCHEDULED = "scheduled";
    public static final String COL_DONE = "done";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + "_id INTEGER PRIMARY KEY ,"
            + "crn INTEGER NOT NULL ,"
            + "subject TINYTEXT NOT NULL ,"
            + "course_number INTEGER NOT NULL ,"
            + "section INTEGER NOT NULL ,"
            + "credit INTEGER NOT NULL ,"
            + "title TINYTEXT NOT NULL ,"
            + "instructor TINYTEXT NOT NULL ,"
            + "description TINYTEXT NOT NULL ,"
            + "start_time INTEGER NOT NULL ,"
            + "end_time INTEGER NOT NULL ,"
            + "sunday BOOLEAN NOT NULL ,"
            + "monday BOOLEAN NOT NULL ,"
            + "tuesday BOOLEAN NOT NULL ,"
            + "wednesday BOOLEAN NOT NULL ,"
            + "thursday BOOLEAN NOT NULL ,"
            + "friday BOOLEAN NOT NULL ,"
            + "saturday BOOLEAN NOT NULL ,"
            + "scheduled BOOLEAN NOT NULL ,"
            + "done BOOLEAN NOT NULL )";

    private static final String TAG = "DBAdapter";
    private final Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close() {
        if(db != null) db.close();
        if(dbHelper != null) dbHelper.close();
    }

    //---insert a record into the database---
    public long insertRecord (
            int crn,
            String subject,
            int course_number,
            int section,
            int credit,
            String title,
            String instructor,
            String description,
            int start_time,
            int end_time,
            boolean sunday,
            boolean monday,
            boolean tuesday,
            boolean wednesday,
            boolean thursday,
            boolean friday,
            boolean saturday,
            boolean scheduled,
            boolean done) {

        ContentValues initialValues = new ContentValues();

        initialValues.put(COL_CRN, crn);
        initialValues.put(COL_SUBJECT, subject);
        initialValues.put(COL_COURSE_NUMBER, course_number);
        initialValues.put(COL_SECTION, section);
        initialValues.put(COL_CREDIT, credit);
        initialValues.put(COL_TITLE, title);
        initialValues.put(COL_INSTRUCTOR, instructor);
        initialValues.put(COL_DESCRIPTION, description);
        initialValues.put(COL_START_TIME, start_time);
        initialValues.put(COL_END_TIME, end_time);
        initialValues.put(COL_SUNDAY, sunday);
        initialValues.put(COL_MONDAY, monday);
        initialValues.put(COL_TUESDAY, tuesday);
        initialValues.put(COL_WEDNESDAY, wednesday);
        initialValues.put(COL_THURSDAY, thursday);
        initialValues.put(COL_FRIDAY, friday);
        initialValues.put(COL_SATURDAY, saturday);
        initialValues.put(COL_SCHEDULED, scheduled);
        initialValues.put(COL_DONE, done);


        return db.insert(TABLE_NAME, null, initialValues);
    }

    //---deletes a particular record---
    public boolean deleteContact(int id) {
        return db.delete(TABLE_NAME, COL_ID + "=" + id, null) > 0;
    }

    //---retrieves all the records---
    public Cursor getAllRecords() {
        return db.query(TABLE_NAME, new String[] {
                COL_ID,
                COL_SUBJECT,
                COL_COURSE_NUMBER,
                COL_SECTION,
                COL_CREDIT,
                COL_TITLE,
                COL_START_TIME,
                COL_END_TIME,
                COL_SUNDAY,
                COL_MONDAY,
                COL_TUESDAY,
                COL_WEDNESDAY,
                COL_THURSDAY,
                COL_FRIDAY,
                COL_SATURDAY,
                COL_SCHEDULED,
                COL_DONE
               }, null, null, null, null, null);
    }

    //---retrieves a particular record---
    public Cursor getRecord(int id) {
        Cursor cursor = db.query(TABLE_NAME, new String[] {
                COL_ID,
                COL_CRN,
                COL_SUBJECT,
                COL_COURSE_NUMBER,
                COL_SECTION,
                COL_CREDIT,
                COL_TITLE,
                COL_INSTRUCTOR,
                COL_DESCRIPTION,
                COL_START_TIME,
                COL_END_TIME,
                COL_SUNDAY,
                COL_MONDAY,
                COL_TUESDAY,
                COL_WEDNESDAY,
                COL_THURSDAY,
                COL_FRIDAY,
                COL_SATURDAY,
                COL_SCHEDULED,
                COL_DONE
        }, COL_ID + " = " + id, null, null, null, null, null);

        if(cursor != null) cursor.moveToFirst();

        return cursor;
    }

    //---updates a record---
    public boolean updateRecord(int id, boolean done) {
        ContentValues args = new ContentValues();
        args.put(COL_DONE, done);
        return db.update(TABLE_NAME, args, COL_ID + " = " + id, null) > 0;
    }

    //---updates a record---
    public boolean updateSchedule(int id, boolean scheduled) {
        ContentValues args = new ContentValues();
        args.put(COL_SCHEDULED, scheduled);
        return db.update(TABLE_NAME, args, COL_ID + " = " + id, null) > 0;
    }

//    public ArrayList<String> getAllCourses() {
//        ArrayList<String> list = new ArrayList<String>();
//
//        this.open();
//
//        Cursor cursor = this.getAllRecords();
//
//        if (cursor != null && cursor.moveToFirst()) {
//            while (!cursor.isAfterLast()) {
//                String id = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_ID));
//                String title = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_TITLE));
//                String scheduled = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SCHEDULED));
//                list.add(id + " / " + title + " / schedule " + scheduled);
//                cursor.moveToNext();
//            }
//        }
//
//        if(cursor != null) cursor.close();
//        this.close();
//
//        return list;
//    }

}