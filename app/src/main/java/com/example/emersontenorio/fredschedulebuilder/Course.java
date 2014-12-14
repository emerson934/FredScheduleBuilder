package com.example.emersontenorio.fredschedulebuilder;

import android.text.format.Time;

/**
 * Created by Marcos Souza on 12/14/2014.
 */
public class Course {
    //time in military code
    //0900, 1200, etc.
    int startTime;
    int endTime;
    String []days;

    public Course(int start, int end, String[] day){
        startTime = start;
        endTime = end;
        days = day;
    }


}
