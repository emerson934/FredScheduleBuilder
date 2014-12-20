package com.example.emersontenorio.fredschedulebuilder;

/**
 * Created by Emerson Tenorio on 12/19/2014.
 */
public class Util {

    public static int convertHourMinuteToMinute(int time){
        int hour = time/100;
        int minute = time % 100;
        return hour*60 + minute;
    }

    public static String convertMinuteToHourMinute(int totalMinutes){
        int hour = totalMinutes/60;
        int minute = totalMinutes % 60;
        return hour + ":" + minute;
    }

    public static String convertMinutesToHour(int totalMinutes) {
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
