package com.example.emersontenorio.fredschedulebuilder;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Emerson Tenorio on 12/14/2014.
 */
public class DBAdapter {
    private static final int DATABASE_VERSION = 12;//DO NOT CHANGE THIS
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
    private /*static*/ SQLiteDatabase db;//edited by Marcos added (static)

    public DBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    private /*static*/ class DatabaseHelper extends SQLiteOpenHelper {
        //edited by Marcos
        private final Context mHelperContext;
        //End Edited by Marcos
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.mHelperContext = context;//edited Marcos

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);

//            open();
//            loadFileIntoBD();//edited Marcos
//            close();
        }



        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    //Edited By Marcos
    //Method to get input from a .txt file
    public void loadFileIntoBD() {
//        new Thread(new Runnable() {
//            public void run() {
                try {
//                        open();
                    loadCourses();
                    Toast.makeText(context, "Database Updated", Toast.LENGTH_SHORT).show();
//                        close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//            }
//        }).start();
    }

    private void loadCourses() throws IOException {
        final Resources resources = /*mHelperContext*/context.getResources();
        InputStream inputStream = resources.openRawResource(R.raw.definitions);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] strings = TextUtils.split(line, "-");
                if (strings.length < 10) continue;

                int index = 9;//change to 10 if you enter the course description
                int size = strings[/*10*/index].length();
                boolean monday = false;
                boolean tuesday = false;
                boolean wednesday = false;
                boolean thursday = false;
                boolean friday = false;
                boolean saturday = false;
                boolean sunday = false;
                for (int i = 0; i < size; i++) {
                    if (strings[index].charAt(i) == 'M'){//monday
                        monday = true;
                    } else if(strings[index].charAt(i) == 'T'){//tuesday
                        tuesday = true;
                    } else if (strings[index].charAt(i) == 'W'){//wednesday
                        wednesday = true;
                    }else if (strings[index].charAt(i) == 'R'){//thursday
                        thursday = true;
                    }else if (strings[index].charAt(i) == 'F'){//friday
                        friday = true;
                    }else if (strings[index].charAt(i) == 'S'){//saturday
                        saturday = true;
                    }else if (strings[index].charAt(i) == 'U'){//sunday
                        sunday = true;
                    }
                }

                int startTime = Integer.parseInt(strings[/*8*/7].trim());
                int endTime = Integer.parseInt(strings[/*9*/8].trim());



                long id = insertRecord(Integer.parseInt(strings[0].trim()),//crn
                        strings[1].trim(),//subject
                        Integer.parseInt(strings[2].trim()),//course number
                        Integer.parseInt(strings[3].trim()),//section
                        Integer.parseInt(strings[4].trim()),//credit
                        strings[5].trim(),//title
                        strings[6].trim(),//instructor
                        "Latim",//strings[7].trim(),//description
                        convertHourMinuteToMinute(startTime),//start time
                        convertHourMinuteToMinute(endTime),//end time

                        monday,//sunday
                        tuesday,//monday
                        wednesday,//tuesday
                        thursday,//wednesday
                        friday,//thursday
                        saturday,//fridays
                        sunday,//saturday

                        false,//schedule
                        false);//done
                if (id < 0) {
                    Log.e(TAG, "unable to add word: " + strings[0].trim());
                }
            }
        } finally {
            reader.close();
        }
    }

    public int convertHourMinuteToMinute(int time){
        int hour = time/100;
        int minute = time % 100;

        return hour*60 + minute;
    }

    public String convertMinuteToMinute(int time){
        int hour = time/60;
        int minute = time % 60;
        return hour + ":" + minute;
    }

    //End Edited By Marcos

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



    public ArrayList<String> getAllCourses() {//static added marcos
        ArrayList<String> list = new ArrayList<>();

        this.open();

        Cursor cursor = this.getAllRecords();

        if(cursor != null) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String id = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_ID));
                    String title = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_TITLE));
                    String scheduled = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SCHEDULED));
                    String subject = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SUBJECT));//Marcos
                    String time = convertMinuteToMinute(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_START_TIME))))
                           + " - " + convertMinuteToMinute(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_END_TIME))));
                    list.add(id + " / " + title + " / " + scheduled + " / " + subject + " / " + time);//Marcos
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        this.close();

        return list;
    }

    public ArrayList<String> getCoursesScheduled() {//static added marcos
        ArrayList<String> list = new ArrayList<String>();

        this.open();

        Cursor cursor = this.getAllRecords();

        if(cursor != null) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    int scheduled = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SCHEDULED)));
                    if(scheduled == 1){
                        String subject = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SUBJECT));//Marcos
                        int course_number = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_COURSE_NUMBER)));
                        int startTime = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_START_TIME)));
                        int endTime = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_END_TIME)));
                        int monday =    Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SUNDAY)));
                        int tuesday =   Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_MONDAY)));
                        int wednesday = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_TUESDAY)));
                        int thursday =  Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_WEDNESDAY)));
                        int friday =    Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_THURSDAY)));
                        int saturday =  Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_FRIDAY)));
                        int sunday =    Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SATURDAY)));
                        list.add(subject + " / " + course_number + " /  " + startTime + " / " + endTime + " / " + monday + " / " + tuesday +  " / " + wednesday + " / " + thursday +  " / " + friday + " / " + saturday + " / " + sunday );
                    }
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        this.close();

        return list;
    }

}
