package com.example.emersontenorio.fredschedulebuilder;

import android.text.format.Time;

/**
 * Created by Marcos Souza on 12/14/2014.
 */
public class Builder {
    private static Course[] courses = new Course[10];
    private static int nCourses = 0;

    public static Course[] getCourses(){
        return courses;
    }

    public static void setCourse(Course course){
        courses[nCourses] = course;
        nCourses++;
    }

    public static String addClass(Course course){
        boolean conflict = false;
        int[] conflictWith = new int[courses.length];
        int pos =0;
        for (int i = 0; i < courses.length; i++) {
            if ((timeConflict(courses[i].startTime, courses[i].endTime, courses[i].days, course.startTime, course.endTime, course.days))){
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
    public static boolean timeConflict(int startTime1, int endTime1, String[] days1, int startTime2, int endTime2, String[] days2){
        for (int i = 0; i < days1.length; i++) {
            for (int j = 0; j < days2.length; j++) {
                if (days1[i].equals(days2[j])){

                    if (startTime1 < startTime2){//(startTime1.before(startTime2)){
                        if (!(endTime1 < startTime2)){//(!endTime1.before(startTime2)){
                            //No Time Conflict
//                            return false;
//                        } else{
                            //Time conflict, where 2nd class starts before 1st class ends
                            return true;
                        }
                    } else if (startTime2 < startTime1){//(startTime2.before(startTime1)){
                        if (!(endTime2 < startTime1)){//(!endTime2.before(startTime1)){
                            //No Time Conflict
//                            return false;
//                        } else{
                            //Time conflict, where 1st class starts before 2nd class ends
                            return true;
                        }
                    } else{
                        //Time conflict, where 1st class and 2nd class start at the same time
                        return true;
                    }

                }
            }
        }
        return false;
//        if (startTime1.before(startTime2)){
//            return !endTime1.before(startTime2);
//        } else if(startTime2.before(startTime1)){
//            return !endTime2.before(startTime1);
//        } else{
//            //Time conflict, where 1st class and 2nd class start at the same time
//            return true;
//        }
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
