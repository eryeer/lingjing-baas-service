package com.onchain.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    public static final String MICROSECOND = "MICROSECOND";
    public static final String SECOND = "SECOND";
    public static final String MINUTE = "MINUTE";
    public static final String HOUR = "HOUR";
    public static final String DAY = "DAY";
    public static final String WEEK = "WEEK";
    public static final String MOUTH = "MOUTH";
    public static final String YEAR = "YEAR";

    public static String getFormatTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date getFormatTime(String date, String format)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(date);
    }

    public static Date addDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }

    public static Date addMinute(Date date, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minute);
        return cal.getTime();
    }

    public static Date minusDay(Date date, int day) {
        return addDay(date, -day);
    }

    public static String getCurrentDayStr() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(new Date());
    }


}
