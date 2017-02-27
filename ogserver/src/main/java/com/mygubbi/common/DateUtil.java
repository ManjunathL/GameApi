package com.mygubbi.common;

import java.sql.Date;

/**
 * Created by Chirag on 27-02-2017.
 */
public class DateUtil {

    public static Date convertDate(String dateString)
    {
       return Date.valueOf((dateString).substring(0, 10));
    }
}
