package com.example.emersontenorio.fredschedulebuilder;

import android.text.format.Time;

/**
 * Created by Marcos Souza on 12/14/2014.
 */
public class Builder {

    public Course[] scheduleBuilder(Course []courses){
        //set all classes with no time conflicts at first.


        return courses;
    }

    //Tests if there is a time conflict
    public boolean timeConflict(Time startTime1, Time endTime1, Time startTime2, Time endTime2){
        if (startTime1.before(startTime2)){
            if(endTime1.before(startTime2)){
                //No Time Conflict
                return false;
            } else{
                //Time conflict, where 2nd class starts before 1st class ends
                return true;
            }
        } else if(startTime2.before(startTime1)){
            if (endTime2.before(startTime1)){
                //No Time Conflict
                return false;
            } else{
                //Time conflict, where 1st class starts before 2nd class ends
                return true;
            }
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
