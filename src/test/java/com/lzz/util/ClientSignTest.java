package com.lzz.util;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by lzz on 17/4/10.
 */
public class ClientSignTest {
    @Test
    public void clientIpTest(){
        String ip = ClientSign.clientIp();
        System.out.println(ip);
    }

    @Test
    public void test1(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        System.out.println(day);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println(hour);
        int minute = calendar.get(Calendar.MINUTE);
        System.out.println(minute);
    }

    @Test
    public void test2(){
        long time = CommonUtil.getTime();
        System.out.println(time);
    }
}
