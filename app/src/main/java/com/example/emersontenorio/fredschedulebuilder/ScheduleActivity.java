package com.example.emersontenorio.fredschedulebuilder;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.TextView;

public class ScheduleActivity extends ActionBarActivity implements HorizontalScrollViewListener {

    private GridLayout frozenGridHeader;
    private GridLayout contentGridHeader;
    private GridLayout frozenGrid;
    private GridLayout contentGrid;
    private ObservableHorizontalScrollView headerScrollView;
    private ObservableHorizontalScrollView contentScrollView;

    private int cellWidth, cellHeight, startTime, endTime;

    private enum CellType {WEEKDAY, TIME, GRID, COURSE}

    DBAdapter myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        this.contentGrid        = (GridLayout) findViewById(R.id.contentGrid);
        this.frozenGrid         = (GridLayout) findViewById(R.id.frozenGrid);
        this.frozenGridHeader   = (GridLayout) findViewById(R.id.frozenGridHeader);
        this.contentGridHeader  = (GridLayout) findViewById(R.id.contentGridHeader);
        this.headerScrollView   = (ObservableHorizontalScrollView) findViewById(R.id.contentGridHeaderHorizontalScrollView);
        this.contentScrollView  = (ObservableHorizontalScrollView) findViewById(R.id.contentGridHorizontalScrollView);

        this.headerScrollView.setScrollViewListener(this);
        this.contentScrollView.setScrollViewListener(this);
        this.contentScrollView.setHorizontalScrollBarEnabled(false); // remove scroll bars
        this.headerScrollView.setHorizontalScrollBarEnabled(false); // remove scroll bars

        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);

        this.cellWidth  = display.widthPixels/6;
        this.cellHeight = getPixelsFromDip(20);
        this.startTime  = 480;
        this.endTime    = 1380;

        int rowCount = (endTime - startTime)/10;
        contentGrid.setRowCount(rowCount);

        InitializeInitialData();
    }

    protected void InitializeInitialData() {
        populateHeaders();
        getSchedule();
    }

    public void onScrollChanged(ObservableHorizontalScrollView scrollView, int x, int y, int oldX, int oldY) {
        if (scrollView == headerScrollView) {
            contentScrollView.scrollTo(x, y);
        } else if (scrollView == contentScrollView) {
            headerScrollView.scrollTo(x, y);
        }
    }

    protected void populateHeaders() {

        // --- Add empty cell header ---
        TextView emptyCell = getCell("", CellType.WEEKDAY);
        frozenGridHeader.addView(emptyCell);

        // --- Add weekday header ---
        String[] lblWeekdays = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        for (String str : lblWeekdays) {
            TextView weekdayCell = getCell(str, CellType.WEEKDAY);
            contentGridHeader.addView(weekdayCell);
        }

        // --- Add time header ---
        for(int i = startTime; i < endTime; i+= 60) {
            TextView timeCell = getCell(Util.convertMinutesToHour(i), CellType.TIME);
            frozenGrid.addView(timeCell);
        }

        // --- Content ---
        for(int i = 0; i < contentGrid.getRowCount(); i++){
            for(int j = 0; j < contentGrid.getColumnCount(); j++){
                TextView cell = getCell("", CellType.GRID);
                contentGrid.addView(cell);
            }
        }
    }

    private TextView getCell(String text, CellType type) {
        int px_1 = getPixelsFromDip(1);

        TextView txtView = new TextView(this);
        txtView.setTextColor(Color.parseColor("#555555"));
        txtView.setBackgroundColor(Color.parseColor("#FFFFFF"));

        GridLayout.LayoutParams cellParams = new GridLayout.LayoutParams();

        switch (type) {
            case GRID:
                txtView.setMinimumWidth(cellWidth);
                txtView.setMinimumHeight(cellHeight);
                cellParams.setMargins(0, 0, px_1, px_1);
                break;
            case TIME:
                txtView.setText(text);
                txtView.setMinimumWidth(cellWidth);
                txtView.setMinimumHeight(getRowSpanSize(6));
                txtView.setTypeface(Typeface.MONOSPACE);
                txtView.setGravity(Gravity.TOP | Gravity.CENTER);
                cellParams.setMargins(0, 0, px_1, px_1);
                break;
            case WEEKDAY:
                txtView.setText(text);
                txtView.setMinimumWidth(cellWidth);
                txtView.setGravity(Gravity.CENTER);
                cellParams.setMargins(0, 0, px_1, 0);
                break;
            case COURSE:
                txtView.setText(text);
                txtView.setMinimumWidth(cellWidth);
                txtView.setMinimumHeight(cellHeight);
                txtView.setGravity(Gravity.CENTER);
                txtView.setBackgroundColor(Color.parseColor("#9085BA"));
                txtView.setTextColor(Color.parseColor("#FFFFFF"));
                cellParams.setMargins(0, 0, px_1, 0);
                break;
            default:
                break;
        }

        txtView.setLayoutParams(cellParams);
        return txtView;
    }

    private void loadCourse(String subject, String courseNumber, int sTime, int eTime, boolean ... weekdays) {
        if(weekdays == null || weekdays.length > 7) return;

        int row = (sTime - this.startTime)/10, spanning = (eTime - sTime)/10;

        for (int col = 0; col < weekdays.length; col++) {
            if(weekdays[col]) {
                TextView cell = getCell(subject + "\n" + courseNumber, CellType.COURSE);

                cell.setMinimumHeight(getRowSpanSize(spanning));

                contentGrid.addView(cell, new GridLayout.LayoutParams(
                        GridLayout.spec(row,spanning,GridLayout.CENTER),
                        GridLayout.spec(col,1,GridLayout.CENTER)
                ));
            }
        }
    }

    public void getSchedule() {
        myDB = new DBAdapter(this);
        myDB.open();

        Cursor cursor = myDB.getScheduledRecords();

        if(cursor != null) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    int scheduled = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SCHEDULED)));
                    String subject       = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SUBJECT));
                    String course_number = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_COURSE_NUMBER));
                    int startTime   = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_START_TIME)));
                    int endTime     = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_END_TIME)));
                    int monday      = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SUNDAY)));
                    int tuesday     = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_MONDAY)));
                    int wednesday   = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_TUESDAY)));
                    int thursday    = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_WEDNESDAY)));
                    int friday      = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_THURSDAY)));
                    int saturday    = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_FRIDAY)));
                    int sunday      = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SATURDAY)));

                    boolean[] weekDays = new boolean[7];

                    for(boolean day : weekDays) {
                        day = false;
                    }

                    if (monday == 1) weekDays[0] = true;
                    if (tuesday ==1) weekDays[1] = true;
                    if (wednesday ==1) weekDays[2] = true;
                    if (thursday == 1) weekDays[3] = true;
                    if (friday == 1) weekDays[4] = true;
                    if (saturday == 1) weekDays[5] = true;
                    if (sunday == 1) weekDays[6] = true;

                    loadCourse(subject, course_number, startTime, endTime, weekDays);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        myDB.close();

    }

    private int getRowSpanSize(int span) {
        int margin = getPixelsFromDip(1);
        return ((cellHeight + margin)*span) - margin;
    }

    private int getPixelsFromDip (int dipMeasure) {
        Resources resources = this.getResources();

        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dipMeasure,
                resources.getDisplayMetrics()
        );
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        myDB.close();
    }
}