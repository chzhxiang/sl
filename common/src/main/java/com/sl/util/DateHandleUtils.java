package com.sl.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间处理工具
 */
public class DateHandleUtils {
    /**
     * 默认日期字符串格式
     */
    public static String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String YY_MM_DD_FORMAT = "yy年MM月dd日";


    public static String DAY_BEGIN_DATE = "yyyy-MM-dd 00:00:00";
    public static String DAY_END_DATE = "yyyy-MM-dd 23:59:59";
    public static String DAY_NOON_DATE = "yyyy-MM-dd 12:00:00";

    private static String[] weekDaysName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};


    public static Date parse(String date) {
        return parse(date, DEFAULT_FORMAT);
    }

    /**
     * 根据格式 格式化时间
     * @param date
     * @param format
     * @return
     */
    public static Date parseFormatDate(Date date,String format) {
        String dateStr = format(date, format);
        return parse(dateStr,format);
    }


    /**
     * 根据格式把字符串转化为时间
     *
     * @param date   字符串时间
     * @param format 格式
     * @return
     */
    public static Date parse(String date, String format) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        DateTimeFormatter df = DateTimeFormat.forPattern(format);
        return DateTime.parse(date, df).toDate();
    }

    /**
     * 当前时间转化成字符串
     * <p>格式yyyy-MM-dd HH:mm:ss</p>
     *
     * @return
     */
    public static String formatCurDate() {
        return format(new Date(), DEFAULT_FORMAT);
    }

    /**
     * 根据格式把时间转化成字符串
     * <p>默认格式yyyy-MM-dd HH:mm:ss</p>
     *
     * @param date 时间
     * @return
     */
    public static String format(Date date) {
        return format(date, DEFAULT_FORMAT);
    }

    /**
     * 根据格式把时间转换成字符串
     * <p>格式yy年MM月dd日</p>
     *
     * @param date
     * @return
     */
    public static String formatYYMMDD(Date date) {
        return format(date, YY_MM_DD_FORMAT);
    }

    /**
     * 根据格式把时间转化成字符串
     *
     * @param date   时间
     * @param format 格式
     * @return
     */
    public static String format(Date date, String format) {
        if (date == null) {
            return "";
        }
        return new DateTime(date.getTime()).toString(format);
    }

    /**
     * 根据格式把毫秒数转化成字符串
     *
     * @param milliseconds 毫秒
     * @param format       格式
     * @return
     */
    public static String format(Long milliseconds, String format) {
        if (milliseconds == null) return "";
        return new DateTime(milliseconds).toString(format);
    }


    /**
     * 根据时间添加年数
     *
     * @param date  时间
     * @param years 年
     * @return
     */
    public static Date plusYeas(Date date, int years) {
        return new DateTime(date.getTime()).plusYears(years).toDate();
    }

    /**
     * 根据时间添加天数
     *
     * @param date 时间
     * @param days 天数，可为负数
     * @return
     */
    public static Date plusDays(Date date, int days) {
        return new DateTime(date.getTime()).plusDays(days).toDate();
    }

    /**
     * 根据时间添加月数
     *
     * @param date   时间
     * @param months 月数，可为负数
     * @return
     */
    public static Date plusMonths(Date date, int months) {
        return new DateTime(date.getTime()).plusMonths(months).toDate();
    }

    /**
     * 根据时间添加小时
     *
     * @param date
     * @param hours
     * @return
     */
    public static Date plusHours(Date date, int hours) {
        return new DateTime(date.getTime()).plusHours(hours).toDate();
    }

    /**
     * 根据时间添加分钟
     *
     * @param date
     * @param minutes
     * @return
     */
    public static Date plusMinutes(Date date, int minutes) {
        return new DateTime(date.getTime()).plusMinutes(minutes).toDate();
    }


    /**
     * 2个时间天数差
     *
     * @param start
     * @param end
     * @return
     */
    public static int daysBetween(Date start, Date end) {
        LocalDate startLd = new LocalDate(start.getTime());
        LocalDate endLd = new LocalDate(end.getTime());
        return Days.daysBetween(startLd, endLd).getDays();
    }


    /**
     * 是否为同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        String d1 = format(date1, "yyyy-MM-dd");
        String d2 = format(date2, "yyyy-MM-dd");
        return isSameDay(d1, d2);
    }


    public static boolean isSameDay(String date1, String date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return StringUtils.equals(date1, date2);
    }

    /**
     * 得到以当前时间转化为long
     * <p>yyyyMMddHHmmss</p>
     *
     * @return
     */
    public static Long getCurTimeNum() {
        return Long.valueOf(format(new Date(), "yyyyMMddHHmmss"));
    }

    /**
     * 获取当前时间秒
     *
     * @return
     */
    public static int getCurrentSecond() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        int currentSecond = currentHour * 3600 + currentMinute * 60;
        return currentSecond;
    }


    /**
     * 获取指定时间的小时
     * @param date
     * @return
     */
    public static int getDateHours(Date date){
        Long ms = date.getTime();
        SimpleDateFormat formatter=new SimpleDateFormat("HH");
        String hours = formatter.format(ms);
        return Integer.parseInt(hours);
    }

    /**
     * dt 是否在startDt 和 endDt 范围 内
     *
     * @param dt
     * @param startDt
     * @param endDt
     * @return
     */
    public static boolean isBetween(Date dt, Date startDt, Date endDt) {
        long time = dt.getTime();
        return (startDt.getTime() <= time && time <= endDt.getTime());
    }

    public static boolean isBetween(Date startDt, Date endDt) {
        return isBetween(new Date(), startDt, endDt);
    }

    public static boolean isNotBetween(Date startDt, Date endDt) {
        return !isBetween(startDt, endDt);
    }

    public static boolean isGt(Date date1, Date date2) {
        return (date1.getTime() > date2.getTime());
    }

    public static boolean isLt(Date date1, Date date2) {
        return !isGt(date1, date2);
    }

    /**
     * 星期几，从1开始
     *
     * @param date
     * @return
     */
    public static int dayOfWeek(Date date) {
        return new DateTime(date.getTime()).getDayOfWeek();
    }

    /**
     * 根据传入的日期返回该日期为星期几
     *
     * @param date
     * @return
     */
    public static String dateOfWeek(Date date) {
        Integer week = dayOfWeek(date);
        return weekDaysName[week];
    }

    public static Date getDayBegin(Date date) {

        return parse(format(date, DateHandleUtils.DAY_BEGIN_DATE));
    }

    public static Date getDayEnd(Date date) {

        return parse(format(date, DateHandleUtils.DAY_END_DATE));
    }
    public static Date getDayNoon(Date date) {

        return parse(format(date, DateHandleUtils.DAY_NOON_DATE));
    }

    /**
     * HH:MM
     *  -1, 0,1
     *is less than, equal to, or greater than
     * @param time1
     * @param time2
     * @return
     */
    public static Integer compareDateHour(String time1,String time2){
        if(StringUtils.isBlank(time1) || StringUtils.isBlank(time2)){
            return 0;
        }

        Integer hour1 =Integer.parseInt(time1.split(":")[0]);
        Integer hour2 =Integer.parseInt(time2.split(":")[0]);
        if(hour1 < hour2){
            return -1;
        }else if(hour1 == hour2){
            return 0;
        }else {
            return 1;
        }
    }

    public static List<String> get7DayList(){
        List<String> dayList = new ArrayList<>();
        dayList.add("星期一");
        dayList.add("星期二");
        dayList.add("星期三");
        dayList.add("星期四");
        dayList.add("星期五");
        dayList.add("星期六");
        dayList.add("星期日");
        return dayList;
    }

    public static List<String> get24HourList(){
        List<String> hourList = new ArrayList<>();
        hourList.add("05:00");
        hourList.add("06:00");
        hourList.add("07:00");
        hourList.add("08:00");
        hourList.add("09:00");
        hourList.add("10:00");
        hourList.add("11:00");
        hourList.add("12:00");
        hourList.add("13:00");
        hourList.add("14:00");
        hourList.add("15:00");
        hourList.add("16:00");
        hourList.add("17:00");
        hourList.add("18:00");
        hourList.add("19:00");
        hourList.add("20:00");
        hourList.add("21:00");
        hourList.add("22:00");
        hourList.add("23:00");
        hourList.add("00:00");
        hourList.add("次日01:00");
        hourList.add("次日02:00");
        hourList.add("次日03:00");
        hourList.add("次日04:00");
        hourList.add("次日05:00");

        return hourList;
    }

    /**
     * @param beginDay 星期四
     * @param endDay 星期二
     * @return 星期四, 星期五, 星期六, 星期日, 星期一, 星期二
     */
    public static List<String> getWeekBetween(String beginDay, String endDay) {
        int weekDay = 7;
        List<String> weekList = get7DayList();
        int begin = weekList.indexOf(beginDay);
        int end = weekList.indexOf(endDay);

        List<String> betweenWeekList = new ArrayList<>();
        if(begin <= end){
            for(int i =begin;i<=end;i++){
                betweenWeekList.add(weekList.get(i));
            }
        }else {
            for(int i =begin;i<7;i++){
                betweenWeekList.add(weekList.get(i));
            }
            for(int i =0;i<=end;i++){
                betweenWeekList.add(weekList.get(i));
            }
        }

        return betweenWeekList;
    }


    public static void main(String[] args) {

    }

}
