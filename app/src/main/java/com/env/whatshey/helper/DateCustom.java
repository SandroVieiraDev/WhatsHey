package com.env.whatshey.helper;

import android.annotation.SuppressLint;
import android.icu.util.Calendar;
import android.os.Build;
import android.text.format.DateUtils;


import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateCustom {
    private static final String DATE = "dd 'de' MMMM 'de' yyyy";
    private static final String TIME = "HH:mm";
    private static final String DATE_COMPARE = "dd/MM/yyyy";

    public static long getCurrentTimeMillis(){
        return System.currentTimeMillis();
    }

    public static String getCurrentDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_COMPARE );
        String dataString = simpleDateFormat.format(getCurrentTimeMillis());
        return dataString;
    }
    public static String getDateCompare(long l) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_COMPARE );
        String dataString = simpleDateFormat.format(l);
        return dataString;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getYesterday() {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat(DATE_COMPARE);
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    public static String getDate(long l) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE);
        String dataString = simpleDateFormat.format(l);
        return dataString;
    }

    public static String getTime(long l) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME );
        String dataString = simpleDateFormat.format(l);
        return dataString;
    }




}
