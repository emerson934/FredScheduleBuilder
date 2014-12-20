package com.example.emersontenorio.fredschedulebuilder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class CourseActivity extends ActionBarActivity {
    private DBAdapter myDataBase;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Intent intent = getIntent();
        myDataBase = new DBAdapter(this);
        SQLiteDatabase db;

        myDataBase.open();
        this.id = intent.getExtras().getInt("id");
        Cursor cursor = myDataBase.getRecord(this.id);

        if(cursor != null) {
            String description = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_DESCRIPTION));
            String title = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_TITLE));
            String instructor = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_INSTRUCTOR));
            String course_credits = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_CREDIT));
            String time =
                    Util.convertMinuteToHourMinute(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_START_TIME))))
                    + " - " +
                    Util.convertMinuteToHourMinute(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_END_TIME))));

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

            cursor.close();

        }
        myDataBase.close();

        Button btnSchedule = (Button) findViewById(R.id.btnAddSchedule);

        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDataBase.open();
                myDataBase.updateSchedule(id,true);
                myDataBase.close();
                Toast.makeText(getBaseContext(), "Course in set in the schedule!", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnDone = (Button) findViewById(R.id.btnDone);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDataBase.open();
                myDataBase.updateRecord(id, true);
                myDataBase.close();
                Toast.makeText(getBaseContext(), "Course in set as done!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.schedule) {
            Intent intent = new Intent(this, ScheduleActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
