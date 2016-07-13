/**
 * 
 * 惠购 - 惠购网 - www.huiget.com - 特别会购！
 * Copyright © 2014 惠购 www.huiget.com 版权所有
 */
package com.sl.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author yaofang
 * @version $Id: DateUtil.java, v 0.1 2014年7月18日 下午1:50:22 yaofang Exp $
 */
public class DateUtil {

    public static final String LONG_DATE_GBK_FORMAT = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String LONG_DATE_FORMAT     = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT          = "yyyy-MM-dd";
    public static final String DATE_FORMAT_NUMBER   = "yyyyMMdd";

    /**
     * 将一个日期型转换为指定格式字串
     * @param date
     * @param formatStr
     * @return
     */
    public static final String toFormatDateString(Date date, String formatStr) {
        if (date == null)
            return StringUtils.EMPTY;
        return new SimpleDateFormat(formatStr).format(date);

    }

    /**
     * 将一个日期型转换为'yyyy年MM月dd日 HH时mm分ss秒'格式字串
     * @param date
     * @return
     */
    public static final String toLongDateGBKString(Date date) {
        return toFormatDateString(date, LONG_DATE_GBK_FORMAT);
    }

    public static final String toLongDateString(Date date) {
        return toFormatDateString(date, LONG_DATE_FORMAT);
    }

    public static final String toDateString(Date date) {
        return toFormatDateString(date, DATE_FORMAT);
    }

    public static final String toDateNumber(Date date) {
        return toFormatDateString(date, DATE_FORMAT_NUMBER);
    }

    public static final Date parser(String dateStr, String formatter) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatter);

        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static final long getInterval(Date begin, Date end) {
        return end.getTime() - begin.getTime();
    }
}
