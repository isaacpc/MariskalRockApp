package com.isaacpc.mariskalrock.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {

    public static String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);


    /**
     * Obtiene un Date de una fecha en string de formato
     *
     * @param pubDate
     * @return
     */
    public static Calendar stringToCalendar(String pubDate) {

        final Calendar cal = Calendar.getInstance();
        try {
            pubDate = pubDate.substring(0, 16);
            pubDate = pubDate.replaceAll("T", " ");

            cal.setTime(FORMATTER.parse(pubDate));

        } catch (final Exception e) {
            e.printStackTrace();
        }
        return cal;
    }


    /**
     * Convirte un calendar a una fecha usando el formato indicado
     *
     * @param cal
     * @return
     */
    public static String CalendarToString(final Calendar cal, String format) {

        if (StringUtils.isEmpty(format)) {
            return FORMATTER.format(cal.getTime());
        } else {
            final SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);
            return formatter.format(cal.getTime());
        }
    }


    /**
     * Convirte un calendar a una fecha usando el formato indicado
     *
     * @param cal
     * @return
     */
    public static String CalendarToString(final Calendar cal) {
        return FORMATTER.format(cal.getTime());
    }


    /**
     * @param seconds
     * @return
     */
    public static String secondsToMinutes(int seconds) {

        final SimpleDateFormat sdf = new java.text.SimpleDateFormat("mm:ss", new Locale("es", "ES"));

        final Calendar calendar = Calendar.getInstance();

        calendar.clear();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        calendar.set(Calendar.SECOND, seconds);

        return sdf.format(calendar.getTime());

    }

}
