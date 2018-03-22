package org.junglestory.openfire.opengraph.plugin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	private static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 현재 날짜 구하기
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat date = new SimpleDateFormat(DATE_FORMAT);

        Calendar cal = Calendar.getInstance();
        String currentDate = date.format(cal.getTime());

        return currentDate;
    }

    /**
     * 날짜 비교
     * @param date
     * @return
     */
    public static long getDiffDate(String date)
    {
        long diffDays = 0L;

        try{
            SimpleDateFormat format = new SimpleDateFormat( DATE_FORMAT);

            Date FirstDate = format.parse(date);
            Date SecondDate = format.parse(getCurrentDate());

            long diffDate = FirstDate.getTime() - SecondDate.getTime();
            long diffDateDays = diffDate / ( 24*60*60*1000);

            diffDays = Math.abs(diffDateDays);
        } catch(ParseException e) {
            e.printStackTrace();
        }

        return diffDays;
    }
}
