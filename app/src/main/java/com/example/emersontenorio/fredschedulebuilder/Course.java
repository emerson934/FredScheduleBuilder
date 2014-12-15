package com.example.emersontenorio.fredschedulebuilder;

import android.text.format.Time;

/**
 * Created by Marcos Souza on 12/14/2014.
 */
public class Course {
    //time in military code
    //0900, 1200, etc.
    String name;
    int startTime;
    int endTime;
    String []days;
    String subject;

    public Course(int start, int end, String[] day, String subj, String nam){
        startTime = start;
        endTime = end;
        days = day;
        subject = subj;
        name = nam;
    }


}
