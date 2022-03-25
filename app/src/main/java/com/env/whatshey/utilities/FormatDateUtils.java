package com.env.whatshey.utilities;


import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FormatDateUtils {

    public static String getDateMessage(long neededtimemillis) {
        Calendar nowTime = Calendar.getInstance();
        Calendar neededTime = Calendar.getInstance();
        neededTime.setTimeInMillis(neededtimemillis);

        if ((neededTime.get(Calendar.YEAR) == nowTime.get(Calendar.YEAR))) {

            if ((neededTime.get(Calendar.MONTH) == nowTime.get(Calendar.MONTH))) {

                if (neededTime.get(Calendar.DATE) - nowTime.get(Calendar.DATE) == 1) {
                    return "AMANHÃ";

                } else if (nowTime.get(Calendar.DATE) == neededTime.get(Calendar.DATE)) {
                    return "HOJE";

                } else if (nowTime.get(Calendar.DATE) - neededTime.get(Calendar.DATE) == 1) {
                    return "ONTEM";

                } else {
                    return getNameMonth(neededTime.getTimeInMillis());
                }

            } else {
                return getNameMonth(neededTime.getTimeInMillis());
            }

        } else {
            return getNameMonth(neededTime.getTimeInMillis());
        }
    }

    public static String getNameMonth(long timestamp) {
        String[] meses = new String[]{
                "Janeiro",
                "Fevereiro",
                "Março",
                "Abril",
                "Maio",
                "Junho",
                "Julho",
                "Agosto",
                "Setembro",
                "Outubro",
                "Novembro",
                "Dezembro"
        };

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);
        return c.get(Calendar.DAY_OF_MONTH) + " de " + meses[c.get(Calendar.MONTH)] + " de " + c.get(Calendar.YEAR);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getHours(long millis) {
        return new SimpleDateFormat("HH:mm").format(millis);
    }
}
