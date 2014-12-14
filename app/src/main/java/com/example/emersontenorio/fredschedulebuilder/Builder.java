package com.example.emersontenorio.fredschedulebuilder;

import android.text.format.Time;

/**
 * Created by Marcos Souza on 12/14/2014.
 */
public class Builder {
    private Course[] courses = new Course[10];
    private int nCourses = 0;

    public Course[] getCourses(){
        return courses;
    }

    public void setCourse(Course course){
        courses[nCourses] = course;
        nCourses++;
    }

    public String addClass(Course course){
        boolean conflict = false;
        int[] conflictWith = new int[courses.length];
        int pos =0;
        for (int i = 0; i < courses.length; i++) {
            if ((timeConflict(courses[i].startTime, courses[i].endTime, course.startTime, course.endTime))){
                conflict = true;
                conflictWith[pos] = i;
                pos++;
            }
        }
//9347
        if (conflict){
            String returned = "Time Conflict With:";
            for (int i = 0; i < pos; i++) {
                returned += " " + conflictWith[i];
            }
            return returned;
        } else{
            setCourse(course);
            return "Class Added Successfully!";
        }
    }

    //Tests if there is a time conflict
    public boolean timeConflict(Time startTime1, Time endTime1, Time startTime2, Time endTime2){
        if (startTime1.before(startTime2)){
            return !endTime1.before(startTime2);
        } else if(startTime2.before(startTime1)){
            return !endTime2.before(startTime1);
        } else{
            //Time conflict, where 1st class and 2nd class start at the same time
            return true;
        }
    }

    //Takes all classes with no time conflicts
//    public Course[] setAllNoTimeConflict(Course []courses){
//        Course[] noConflictCourses;
//        int size = 0;
//        int[] noConflictIndexes = new int[courses.length];
//        for(int i=0; i < courses.length - 1; i++){
//            boolean conflict = false;
//            for (int j = i; j < courses.length; j++) {
//                if (timeConflict(courses[i].startTime, courses[i].endTime, courses[j].startTime, courses[i].endTime)){
//                    conflict = true;
//                    break;
//                }
//            }
//            if (!conflict){
//                noConflictIndexes[size] = i;
//                size++;
//            }
//        }
//        if(size > 0){
//            noConflictCourses = new Course[size];
//            for (int i = 0; i < size; i++) {
//                noConflictCourses[i] = courses[noConflictIndexes[i]];
//            }
//            return noConflictCourses;
//        } else{
//            return null;
//        }
//    }




}
