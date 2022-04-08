package com.example.plantastic;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Formatters {

    /*
     * This class is here to create predefined functions to remove the ugly code
     * In all other activities dealing with dates.
     */


    /**
     * This function returns a date string from a LocalDate object.
     *
     * @param date The LocalDate object.
     * @return The date in YYYY-MM-DD format.
     */
    public static String getNumericalDate(LocalDate date) {
        return date.toString();
    }


    /**
     * This function returns a textual date from a LocalDate object.
     *
     * @param date The LocalDate object.
     * @return The date in MM-DD-YYYY format e.g. "APR 25 2022".
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getTextDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
    }


    /**
     * This function converts a LocalTime object to a string.
     *
     * @param time the LocalTime object.
     * @return the time string in hh:mm format.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getTime(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }


    /**
     * Function which can return todays date textually or numerically.
     * @param numerical if true, date will be returned numerically.
     * @return the date string.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static  String getTodaysDate(Boolean numerical) {
        LocalDate today = LocalDate.now();
        if (numerical) {
            return getNumericalDate(today);
        }
        return getTextDate(today);
    }


    /**
     * This function returns the current time.
     *
     * @return Current time in hh:mm format.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static  String getCurrentTime() {
        LocalTime time = LocalTime.now();
        return getTime(time);
    }
}
