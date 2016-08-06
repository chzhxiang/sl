package com.sl.util;

import java.util.Calendar;

/**
 * Created by jiangqiang on 2016/6/2.
 * use 获取时间戳
 */
public class TimesUtil {
    //获得当天0点时间
    public static long getTimesmorning(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long timeInMillis = cal.getTimeInMillis();
        return timeInMillis;
    }
    //获得当天24点时间
    public static long getTimesnight(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    //获得本周一0点时间
    public static long getTimesWeekmorning(){
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTimeInMillis();
    }

    //获得本周日24点时间
    public static long getTimesWeeknight(){
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTimeInMillis()+(7 * 24 * 60 * 60 * 1000)-1000;
    }
}
