package com.example.emersontenorio.fredschedulebuilder;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ScheduleActivity extends Activity implements HorizontalScrollViewListener {

    private GridLayout frozenGridHeader;
    private GridLayout contentGridHeader;
    private GridLayout frozenGrid;
    private GridLayout contentGrid;
    private ObservableHorizontalScrollView headerScrollView;
    private ObservableHorizontalScrollView contentScrollView;

    private int cellWidth, cellHeight, startTime, endTime;

    private enum CellType {WEEKDAY, TIME, GRID, COURSE};

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

        // --- test ---
        loadCourse("CSIT", "171", 780, 830, true, false, true, false, true, false, false);
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
            TextView timeCell = getCell(minutesToTime(i), CellType.TIME);
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

        int startHour = sTime/60,
            startMinutes = sTime%60,
            endHour = eTime/60,
            endMinutes = eTime%60;

        int row = (sTime - this.startTime)/10, spanning = (eTime - sTime)/10;

        for (int col = 0; col < weekdays.length; col++) {
            if(weekdays[col]) {

                TextView cell = getCell(subject + "\n" + getRowSpanSize(spanning), CellType.COURSE);

                cell.setMinimumHeight(getRowSpanSize(spanning));

                contentGrid.addView(cell, new GridLayout.LayoutParams(
                        GridLayout.spec(row,spanning,GridLayout.CENTER),
                        GridLayout.spec(col,1,GridLayout.CENTER)
                ));
            }
        }
    }

    private int getRowSpanSize(int span) {
        int margin = getPixelsFromDip(1);
        return ((cellHeight + margin)*span) - margin;
    }

    private int getPixelsFromDip (int dipMeasure) {
        Resources resources = this.getResources();

        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dipMeasure,
                resources.getDisplayMetrics()
        );

        return px;
    }

    private String minutesToTime(int totalMinutes){
        int hours = totalMinutes / 60;
        // --- Returns nothing
        if(totalMinutes%60 != 0) return "";
        // --- Set PM/AM ---
        switch (hours) {
            case 8:
                return "0"+ hours + "\nAM";
            case 12:
                return hours + "\nPM";
        }
        // --- Return the hour
        hours = (hours > 12)? hours - 12 : hours;
        return (hours < 10)? "0" + hours : hours + "";
    }
}