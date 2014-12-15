package com.example.emersontenorio.fredschedulebuilder;

import android.util.Log;

/**
 * Created by Marcos Souza on 12/14/2014.
 */
public class Builder {
    private static Course[] courses = new Course[10];
    private static int nCourses = 0;

    public static Course[] getCourses(){
        return courses;
    }
    public static int getNumberCourses(){return nCourses;}

    public static void setCourse(Course course){
        courses[nCourses] = course;
        nCourses++;
    }

    public static String addClass(Course course){
        boolean conflict = false;
        int[] conflictWith = new int[courses.length];
        int pos =0;
        if (nCourses > 0 && nCourses < 10) {
            for (int i = 0; i < 0; i++) {
                System.out.println("Position: "+ i);
                if ((timeConflict(courses[i].startTime, courses[i].endTime, courses[i].days, course.startTime, course.endTime, course.days))) {
                    conflict = true;
                    conflictWith[pos] = i;
                    pos++;
                }
            }
        }

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
//    Test BD, Test Method
    public static Course[] filterCourses(int start, int end, String[] days, String subject){
        String[] daysA = new String[]{"M", "W", "F"};
        String[] daysB = new String[]{"M", "W", "F"};
        String[] daysC = new String[]{"M", "W", "F"};
        String[] daysD = new String[]{"T", "TH"};
        String[] daysE = new String[]{"T", "TH"};
        String[] daysF = new String[]{"T", "TH"};

        final Course courseA = new Course(830, 1030, daysA, "CSIT", "Course A");
        final Course courseB = new Course(1040, 1150, daysB, "CSIT", "Course B");
        final Course courseC = new Course(1140, 1230, daysC, "CSIT", "Course C");
        final Course courseD = new Course(830, 1030, daysD, "CSIT", "Course D");
        final Course courseE = new Course(1040, 1150, daysE, "CSIT", "Course E");
        final Course courseF = new Course(1140, 1230, daysF, "SPAN", "Course F");

        Course[] classes = {courseA, courseB, courseC, courseD, courseE, courseF};
        int size = classes.length;
        //number of filter passed through parameters
        int nFilters = 4; //start = 0, end = 1, days = 2, subject = 3
        boolean[][] checkingParams = new boolean[size][nFilters];
        //filling boolean matriz
        for (int i = 0; i < size; i++) {
            if(classes[i].startTime > start){
                checkingParams[i][0] = true;
            } else{
                checkingParams[i][0] = false;
            }

            if(classes[i].endTime < end){
                checkingParams[i][1] = true;
            } else{
                checkingParams[i][1] = false;
            }
            //test
            checkingParams[i][2] = true;

//            for (int j = 0; j < days.length; j++) {
//                //To think about it
//            }

            if (classes[i].subject.equals(subject)){
                checkingParams[i][3] = true;
            } else{
                checkingParams[i][3] = false;
            }
        }

        int[] classesIndexes = new int[size];
        int pos = 0;

        for (int i = 0; i < size; i++) {
            boolean match = true;
            for (int j = 0; j < nFilters; j++) {
                if(!(checkingParams[i][j])){
                    match = false;
                }
            }
            if(match){
                classesIndexes[pos] = i;
                pos++;
            }
        }

        Course[] classesFound = new Course[pos];
        for (int i = 0; i < pos; i++) {
            classesFound[i] = classes[classesIndexes[i]];
        }

        return classesFound;
    }

    public static void generateRandomSchedules(int start, int end, String[] days, String subject){
        //filtrar as disciplina com os requerimentos
        Course[] coursesFound = filterCourses(start, end, days, subject);
        if(coursesFound != null){
            int resp = setAllNoTimeConflict(coursesFound);
            int times=0;
            while((resp < 6) && (times < 3)){
                resp = removeConflict(coursesFound);
                times++;
            }
        }
    }


    public static int removeConflict(Course[] course){
        int indMax = setConflictList(course);
        course[indMax] = null;
        return setAllNoTimeConflict(course);
    }

    private static int[] conflictList = new int[10];
    public static int setConflictList(Course []course){
        int maxSize = 0;
        int indMax = 0;
        for (int i = 0; i < nCourses; i++) {
            int size = 0;
            for(int j=0; j < course.length; j++){
                if(timeConflict(courses[i].startTime,courses[i].endTime, courses[i].days, course[i].startTime,course[i].endTime, course[i].days)){
                    size++;
                }
            }
            conflictList[i] = size;
            if(size > maxSize){
                maxSize = size;
                indMax = i;
            }
        }
        return indMax;
    }

//    Takes all classes with no time conflicts
    public static int setAllNoTimeConflict(Course []course){
        int size = nCourses;
        for(int i=0; i < course.length; i++){
            String response = addClass(course[i]);
            if (response.equals("Class Added Successfully!")){
                size++;
                if(size >9){
                    break;
                }
            } else{

            }
        }
        return size;
    }
}
