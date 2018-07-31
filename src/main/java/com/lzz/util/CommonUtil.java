package com.lzz.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lzz on 17/4/12.
 */
public class CommonUtil {
    public static int getDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        int day = Integer.parseInt((new SimpleDateFormat("yyyyMMdd")).format(calendar.getTime()));
        return day;
    }
    public static int getHour(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour;
    }
    public static int getMinute(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        int minute = calendar.get(Calendar.MINUTE);
        return minute;
    }

    /**
     * 获取当前时间戳
     * @return
     */
    public static long getTime(){
        return new Date().getTime();
    }

}
