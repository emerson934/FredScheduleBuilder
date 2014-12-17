package com.example.emersontenorio.fredschedulebuilder;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import java.util.ArrayList;

/**
 * Created by Marcos Souza on 12/14/2014.
 */
public class Builder {
    static Context context;// = getBaseContext();

    public Builder(Context context){
        this.context = context;
    }

    private static ArrayList<Course> courses = new ArrayList<>();

    private static int nCourses = 0;

    public static ArrayList<Course> getCourses(){return courses;}

    public static int getNumberCourses(){return nCourses;}

    public static void setCourse(Course course){
        courses.add(course);
        nCourses++;
    }

    public static boolean courseSet(int courseNumber){
        boolean result = false;

        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).course_number == courseNumber){
                result = true;
                break;
            }
        }

        return result;
    }

    public static String addClass(Course course){
        boolean conflict = false;
        int[] conflictWith = new int[courses.size()];

        int pos =0;
        if ((course.done)||(course.scheduled)) {
            //skipping course (marked as done) or (already in schedule)
//            System.out.println("Done or Already In!");
            conflict = true;
        } else if (courseSet(course.course_number)){
            //course with the same number already in the schedule
            //can have different times
            conflict = true;
        } else if (nCourses > 0 ) {
            for (int i = 0; i < nCourses; i++) {
//                System.out.println("Position: "+ i);
                if ((timeConflict(courses.get(i).startTime, courses.get(i).endTime, courses.get(i).days, course.startTime, course.endTime, course.days))) {
                    conflict = true;
                    conflictWith[pos] = i;
                    pos++;
//                    System.out.println("Conflict in: "+ i);
//                    break;
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

            //setting up a schedule
            DBAdapter myDataBase = new DBAdapter(context);
            myDataBase.open();
            myDataBase.updateSchedule(course.id, true);
            myDataBase.close();

            return "Class Added Successfully!";
        }
    }

    //Tests if there is a time conflict
    public static boolean timeConflict(int startTime1, int endTime1, String[] days1, int startTime2, int endTime2, String[] days2){
        for (String aDays1 : days1) {
            for (String aDays2 : days2) {
                if (aDays1.equals(aDays2)) {

                    if (startTime1 < startTime2) {
                        if (!(endTime1 < startTime2)) {
                            //No Time Conflict
//                            return false;
//                        } else{
                            //Time conflict, where 2nd class starts before 1st class ends
                            return true;
                        }
                    } else if (startTime2 < startTime1) {
                        if (!(endTime2 < startTime1)) {
                            //No Time Conflict
//                            return false;
//                        } else{
                            //Time conflict, where 1st class starts before 2nd class ends
                            return true;
                        }
                    } else {
                        //Time conflict, where 1st class and 2nd class start at the same time
                        return true;
                    }
                }
            }
        }
        return false;
    }

//    Test BD, Test Method
    public static ArrayList<Course> filterCourses(int start, int end, String[] days, String subject){

        DBAdapter myDataBase = new DBAdapter(context);

        ArrayList<Course> classes = new ArrayList<Course>();

        myDataBase.open();

        Cursor cursor = myDataBase.getAllRecords();

        if(cursor != null) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    int startTime = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_START_TIME)));
                    int endTime = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_END_TIME)));

                    int monday =    Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SUNDAY)));
                    int tuesday =   Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_MONDAY)));
                    int wednesday = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_TUESDAY)));
                    int thursday =  Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_WEDNESDAY)));
                    int friday =    Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_THURSDAY)));
                    int saturday =  Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_FRIDAY)));
                    int sunday =    Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SATURDAY)));

//                    System.out.println("Monday  " + monday);//debugging
//                    System.out.println("tuesday  " + tuesday);//debugging
//                    System.out.println("wednesday  " + wednesday);//debugging
//                    System.out.println("thursday  " + thursday);//debugging
//                    System.out.println("friday  " + friday);//debugging
//                    System.out.println("saturday  " + saturday);//debugging
//                    System.out.println("sunday  " + sunday);//debugging

                    String[] day;
                    String week = "";

                    if (monday == 1){
//                        System.out.println("Monday True ");//debugging
                        week += "M ";
                    }
                    if (tuesday ==1){
//                        System.out.println("Tuedasy True ");//debugging
                        week += " T ";
                    }
                    if (wednesday ==1){
//                        System.out.println("Wednesday True ");//debugging
                        week += " W ";
                    }
                    if (thursday == 1){
//                        System.out.println("Thurdays True ");//debugging
                        week += " R ";
                    }
                    if (friday == 1){
//                        System.out.println("Friday True ");//debugging
                        week += " F ";
                    }
                    if (saturday == 1){
//                        System.out.println("Saturday True ");//debugging
                        week += " S ";
                    }
                    if (sunday == 1){
//                        System.out.println("Sunday True ");//debugging
                        week += " U ";
                    }

//                    System.out.println("Week =  " + week);//debugging
                    day = TextUtils.split(week, " ");

                    //debugging
//                    for (int i = 0; i < day.length; i++) {
//                        System.out.println("day["+ i +"] =  " + day[i]);
//                    }

                    String subj = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SUBJECT));
                    String title = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_TITLE));

                    int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_ID)));
                    int crn = 12;//Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_CRN)));
                    int course_number = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_COURSE_NUMBER)));
                    int section = 1;// Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SECTION)));
                    int credit = 3;//Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_CREDIT)));
                    String instructor = "test";//cursor.getString(cursor.getColumnIndex(DBAdapter.COL_INSTRUCTOR));
                    String description = "test";// cursor.getString(cursor.getColumnIndex(DBAdapter.COL_DESCRIPTION));

                    boolean scheduled = false;
                    if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SCHEDULED))) == 1){
                        scheduled = true;
                    }
                    boolean done = false;
                    if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_DONE))) == 1){
                        done = true;
                    }

                    Course courseA = new Course(startTime, endTime, day, subj, title,
                    /*new fields*/ id, crn, course_number, section, credit, instructor, description, scheduled, done);

                    classes.add(courseA);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        myDataBase.close();

        int size = classes.size();
        //number of filter passed through parameters
        int nFilters = 4; //start = 0, end = 1, days = 2, subject = 3
        boolean[][] checkingParams = new boolean[size][nFilters];
        //filling boolean matriz
        for (int i = 0; i < size; i++) {
            checkingParams[i][0] = classes.get(i).startTime > start;

            checkingParams[i][1] = classes.get(i).endTime < end;
            //test
            checkingParams[i][2] = true;

//            for (int j = 0; j < days.length; j++) {
//                //To think about it
//            }

            checkingParams[i][3] = classes.get(i).subject.equals(subject);
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

        ArrayList<Course> classesFound = new ArrayList<>();
        for (int i = 0; i < pos; i++) {
            classesFound.add(i, classes.get(classesIndexes[i]));
        }

        return classesFound;
    }

    public static void generateRandomSchedules(int start, int end, String[] days, String subject){
        //filtrar as disciplina com os requerimentos
        ArrayList<Course> coursesFound = filterCourses(start, end, days, subject);
        if(coursesFound != null){
            int resp = setAllNoTimeConflict(coursesFound);
            int times=0;
            while((resp < 6) && (times < 3)){
                resp = removeConflict(coursesFound);
                times++;
            }
        }
    }


    public static int removeConflict(ArrayList<Course> course){
        int indMax = setConflictList(course);
        course.remove(indMax);

        //removing from schedule
        DBAdapter myDataBase = new DBAdapter(context);
        myDataBase.open();
        myDataBase.updateSchedule(course.get(indMax).id, false);
        myDataBase.close();


        return setAllNoTimeConflict(course);
    }

    public static int setConflictList(ArrayList<Course> course){
        int maxSize = 0;
        int indMax = 0;
        for (int i = 0; i < nCourses; i++) {
            int size = 0;
            for(int j=0; j < course.size(); j++){
                if(timeConflict(courses.get(i).startTime,courses.get(i).endTime, courses.get(i).days, course.get(i).startTime,course.get(i).endTime, course.get(i).days)){
                    size++;
                }
            }
            if(size > maxSize){
                maxSize = size;
                indMax = i;
            }
        }
        return indMax;
    }

//    Takes all classes with no time conflicts
    public static int setAllNoTimeConflict(ArrayList<Course> course){
        int size = nCourses;
        for(int i=0; i < course.size(); i++){
            String response = addClass(course.get(i));
            if (response.equals("Class Added Successfully!")){
                size++;
                if(size >20){
                    break;
                }
            }
        }
        return size;
    }
}
