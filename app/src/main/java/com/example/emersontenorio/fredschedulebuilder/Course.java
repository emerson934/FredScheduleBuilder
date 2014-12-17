package com.example.emersontenorio.fredschedulebuilder;

/**
 * Created by Marcos Souza on 12/14/2014.
 */
public class Course {

    String name;
    int startTime;
    int endTime;
    String []days;
    String subject;

    //new fields
    int id;
    int crn;
    int course_number;
    int section;
    int credit;
    String instructor;
    String description;
    boolean scheduled;
    boolean done;

    public Course(int start, int end, String[] day, String subj, String nam,
    /*new fields*/int id, int crn, int course_number, int section, int credit, String instructor, String description, boolean scheduled, boolean done){
        startTime = start;
        endTime = end;
        days = day;
        subject = subj;
        name = nam;
        /*new fields*/
        this.id = id;
        this.crn = crn;
        this.course_number = course_number;
        this.section = section;
        this.credit = credit;
        this.instructor = instructor;
        this.description = description;
        this.scheduled = scheduled;
        this.done = done;
    }
}
