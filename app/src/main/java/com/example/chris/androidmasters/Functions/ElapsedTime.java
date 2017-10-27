package com.example.chris.androidmasters.Functions;

import java.util.Date;

/**
 * Created by chris on 15/10/2017.
 */

public class ElapsedTime {

    private long day, hour, minute, second;
    private Date start, end;

    // Calculate Date Difference
    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400

    public ElapsedTime(Date startDate, Date endDate) {
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        this.day = different / daysInMilli;
        different = different % daysInMilli;

        this.hour = different / hoursInMilli;
        different = different % hoursInMilli;

        this.minute = different / minutesInMilli;
        different = different % minutesInMilli;

        this.second = different / secondsInMilli;

//        Log.d("DATE_ELASPSED",elapsedDays + "|" +elapsedHours + "|" +elapsedMinutes+ "|" + elapsedSeconds);

    }

    public long getDay() {
        return day;
    }

    public long getHour() {
        return hour;
    }

    public long getMinute() {
        return minute;
    }

    public long getSecond() {
        return second;
    }
}
