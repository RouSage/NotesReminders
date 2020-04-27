package com.notesreminders.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {

    public static String getCurrentDateTimeUTC() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }

    public static String convertToUTC(Date datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(datetime);
    }

    public static String parseDateTimeToLocal(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.US);
        Date date = null;
        try {
            // Parse in UTC so it returns local date (idk how that works)
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = sdf.parse(datetime);
        } catch(ParseException e) {
            e.printStackTrace();
        }

        // Get local time zone because sdf.format will convert to UTC again
        String localTimeZone = Calendar.getInstance().getTimeZone().getID();
        sdf.setTimeZone(TimeZone.getTimeZone(localTimeZone));

        return sdf.format(date);
    }
}
