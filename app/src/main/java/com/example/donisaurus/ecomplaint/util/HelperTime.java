package com.example.donisaurus.ecomplaint.util;

/**
 * Created by Donisaurus on 12/24/2016.
 */

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ArikAchmad on 12/4/2015.
 */
public class HelperTime {

    public static String ConvertMilis(String timemilis) {
        long timeMilis = Long.parseLong(timemilis);

        Calendar timenow = Calendar.getInstance();
        Calendar time_upload = Calendar.getInstance();
        time_upload.setTimeInMillis(timeMilis);

        long diff = timenow.getTimeInMillis() - time_upload.getTimeInMillis();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long week = days / 7;

        if (week > 1) {
            return week + "w";
        } else if (days > 0) {
            return days + "d";
        } else if (hours > 0) {
            return hours + "w";
        } else if (minutes > 0) {
            return minutes + "m";
        } else {
            return minutes + "s";
        }
    }
}
