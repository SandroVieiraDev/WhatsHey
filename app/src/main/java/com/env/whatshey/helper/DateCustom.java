package com.env.whatshey.helper;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

public class DateCustom {
    private static final String CURRENT_DATE = "dd 'de' MMMM 'de' yyyy";
    private static final String TIME = "HH:mm";

    public static long getCurrentTimeMillis(){
        return System.currentTimeMillis();
    }

    public static String getCurrentDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CURRENT_DATE );
        String dataString = simpleDateFormat.format(getCurrentTimeMillis());
        return dataString;
    }

    public static String getTime(long l) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME );
        String dataString = simpleDateFormat.format(l);
        return dataString;
    }
}
