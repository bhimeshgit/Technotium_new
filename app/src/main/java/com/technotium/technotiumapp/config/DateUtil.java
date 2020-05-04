package com.technotium.technotiumapp.config;

import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    public static final String DATE_FORMAT_yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static Date convertStringToDateTime(String strDate, String strFormat, String timeZone, Locale locale) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(strFormat, locale);
        if (timeZone!=null) {
            sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        }
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
