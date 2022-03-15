package com.env.whatshey.helper;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String currentDate() {
        long data = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy" );
        String dataString = simpleDateFormat.format(data);
        return dataString;
    }
}
