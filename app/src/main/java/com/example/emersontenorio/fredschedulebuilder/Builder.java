package com.example.emersontenorio.fredschedulebuilder;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Marcos Souza on 12/14/2014.
 */
public class Builder {
    static Context context;// = getBaseContext();

    public Builder(Context context){
        this.context = context;
    }

    private static ArrayList<Course> courses = new ArrayList<>();
//    private static Course[] courses = new Course[10];
    private static int nCourses = 0;

    public static ArrayList<Course> getCourses(){return courses;}
//    public static Course[] getCourses(){return courses;}
    public static int getNumberCourses(){return nCourses;}

    public static void setCourse(Course course){
        courses.add(course);
//        courses[nCourses] = course;
        nCourses++;
    }

    public static String addClass(Course course){
        boolean conflict = false;
        int[] conflictWith = new int[courses.size()];
//        int[] conflictWith = new int[courses.length];
        int pos =0;
        if (nCourses > 0 ) {
            for (int i = 0; i < nCourses; i++) {
                System.out.println("Position: "+ i);
                if ((timeConflict(courses.get(i).startTime, courses.get(i).endTime, courses.get(i).days, course.startTime, course.endTime, course.days))) {
//                if ((timeConflict(courses[i].startTime, courses[i].endTime, courses[i].days, course.startTime, course.endTime, course.days))) {
                    conflict = true;
                    conflictWith[pos] = i;
                    pos++;
                    System.out.println("Conflict in: "+ i);
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
        for (String aDays1 : days1) {
            for (String aDays2 : days2) {
                if (aDays1.equals(aDays2)) {

                    if (startTime1 < startTime2) {//(startTime1.before(startTime2)){
                        if (!(endTime1 < startTime2)) {//(!endTime1.before(startTime2)){
                            //No Time Conflict
//                            return false;
//                        } else{
                            //Time conflict, where 2nd class starts before 1st class ends
                            return true;
                        }
                    } else if (startTime2 < startTime1) {//(startTime2.before(startTime1)){
                        if (!(endTime2 < startTime1)) {//(!endTime2.before(startTime1)){
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
    public static ArrayList<Course> filterCourses(int start, int end, String[] days, String subject){

        DBAdapter myDataBase = new DBAdapter(context);

        ArrayList<Course> classes = new ArrayList<Course>();

        myDataBase.open();

        Cursor cursor = myDataBase.getAllRecords();
//final Course courseA = new Course(830, 1030, daysA, "CSIT", "Course A");
        if(cursor != null) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    int startTime = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_START_TIME)));
                    int endTime = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_END_TIME)));

                    int monday1 =    Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SUNDAY)));
                    int tuesday1 =   Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_MONDAY)));
                    int wednesday1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_TUESDAY)));
                    int thursday1 =  Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_WEDNESDAY)));
                    int friday1 =    Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_THURSDAY)));
                    int saturday1 =  Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_FRIDAY)));
                    int sunday1 =    Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SATURDAY)));

                    System.out.println("Monday1  " + monday1);
                    System.out.println("tuesday1  " + tuesday1);
                    System.out.println("wednesday1  " + wednesday1);
                    System.out.println("thursday1  " + thursday1);
                    System.out.println("friday1  " + friday1);
                    System.out.println("saturday1  " + saturday1);
                    System.out.println("sunday1  " + sunday1);

                    String[] day;
                    String week = "";
                    int counter = 0;

                    if (monday1 == 1){
                        System.out.println("Monday True ");
                        week += "M ";
                        counter++;
                    }
                    if (tuesday1 ==1){
                        System.out.println("Tuedasy True ");
                        week += " T ";
                        counter++;
                    }
                    if (wednesday1 ==1){
                        System.out.println("Wednesday True ");
                        week += " W ";
                        counter++;
                    }
                    if (thursday1 == 1){
                        System.out.println("Thurdays True ");
                        week += " R ";
                        counter++;
                    }
                    if (friday1 == 1){
                        System.out.println("Friday True ");
                        week += " F ";
                        counter++;
                    }
                    if (saturday1 == 1){
                        System.out.println("Saturday True ");
                        week += " S ";
                        counter++;
                    }
                    if (sunday1 == 1){
                        System.out.println("Sunday True ");
                        week += " U ";
                        counter++;
                    }

                    System.out.println("Week =  " + week);
                    day = TextUtils.split(week, " ");

                    for (int i = 0; i < day.length; i++) {
                        System.out.println("day["+ i +"] =  " + day[i]);
                    }

                    String subj = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SUBJECT));
                    String title = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_TITLE));

                    Course courseA = new Course(startTime, endTime, day, subj, title);

                    //Marcos
                    classes.add(courseA);//Marcos
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        myDataBase.close();


//        String[] daysA = new String[]{"M", "W", "F"};
//        String[] daysB = new String[]{"M", "W", "F"};
//        String[] daysC = new String[]{"M", "W", "F"};
//        String[] daysD = new String[]{"T", "TH"};
//        String[] daysE = new String[]{"T", "TH"};
//        String[] daysF = new String[]{"T", "TH"};
//
//        final Course courseA = new Course(830, 1030, daysA, "CSIT", "Course A");
//        final Course courseB = new Course(1040, 1150, daysB, "CSIT", "Course B");
//        final Course courseC = new Course(1140, 1230, daysC, "CSIT", "Course C");
//        final Course courseD = new Course(830, 1030, daysD, "CSIT", "Course D");
//        final Course courseE = new Course(1040, 1150, daysE, "CSIT", "Course E");
//        final Course courseF = new Course(1140, 1230, daysF, "SPAN", "Course F");
//
//        final Course courseG = new Course(1240, 1250, daysC, "CSIT", "Course G");
//        final Course courseH = new Course(1300, 1330, daysD, "CSIT", "Course H");
//        final Course courseI = new Course(1400, 1450, daysE, "CSIT", "Course I");

        //ArrayList<Course> classes = new ArrayList<>(Arrays.asList(courseA, courseB, courseC, courseD, courseE, courseF, courseG, courseH, courseI));
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
//            classesFound[i] = classes[classesIndexes[i]];
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
//        course[indMax] = null;
        course.remove(indMax);
        return setAllNoTimeConflict(course);
    }

//    private static int[] conflictList = new int[10];
    public static int setConflictList(ArrayList<Course> course){
        int maxSize = 0;
        int indMax = 0;
        for (int i = 0; i < nCourses; i++) {
            int size = 0;
            for(int j=0; j < course.size(); j++){
                if(timeConflict(courses.get(i).startTime,courses.get(i).endTime, courses.get(i).days, course.get(i).startTime,course.get(i).endTime, course.get(i).days)){
//                if(timeConflict(courses[i].startTime,courses[i].endTime, courses[i].days, course[i].startTime,course[i].endTime, course[i].days)){
                    size++;
                }
            }
//            conflictList[i] = size;
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
            } //else{

//            }
        }
        return size;
    }
}
