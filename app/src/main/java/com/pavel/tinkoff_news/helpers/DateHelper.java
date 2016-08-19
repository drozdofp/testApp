package com.pavel.tinkoff_news.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pavel on 19.08.2016.
 */
public class DateHelper {

    private static SimpleDateFormat SDF = new SimpleDateFormat("mm:hh dd.MM.yyyy");

    public static String convertTimeStampToDateFormat(long milliseconds) {

        Date netDate = new Date(milliseconds);
        return SDF.format(netDate);
    }
}
