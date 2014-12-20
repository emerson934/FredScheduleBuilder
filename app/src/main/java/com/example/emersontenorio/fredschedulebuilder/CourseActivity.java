package com.example.emersontenorio.fredschedulebuilder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;


public class CourseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Intent intent = getIntent();
        DBAdapter myDataBase = new DBAdapter(this);
        SQLiteDatabase db;

        myDataBase.open();
        int id = intent.getExtras().getInt("id");
        System.out.println("IDddddddd: " + id);
//        Cursor cursor = myDataBase.getRecord(intent.getExtras().getInt("id"));
        Cursor cursor = myDataBase.getAllRecords();

        if(cursor != null) {
            if (cursor.moveToFirst()) {
                while(!cursor.isAfterLast()) {
                    String description = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_DESCRIPTION));
                    String title = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_TITLE));
                    String instructor = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_INSTRUCTOR));
                    String course_credits = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_CREDIT));
                    String time = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_START_TIME)))
                            + " - " + Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_END_TIME)));

                    System.out.println("Description: " + title);
                    TextView courseTitle = (TextView) findViewById(R.id.course_name);
                    courseTitle.setText(title);
                    TextView professor = (TextView) findViewById(R.id.professor_name);
                    professor.setText(instructor);
                    TextView timeCourse = (TextView) findViewById(R.id.course_time);
                    timeCourse.setText(time);
                    TextView credits = (TextView) findViewById(R.id.course_credits);
                    credits.setText(course_credits);
                    TextView courseDescription = (TextView) findViewById(R.id.description);
                    courseDescription.setText(description);

                }
            }
            cursor.close();

        }
        myDataBase.close();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
