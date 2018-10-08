package com.dmall.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author:yuhang
 * @Date:2018/9/12
 */
public class DateUtil {
    public static final String DATE_FORMAT_PUBLIC="yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD="yyyy-MM-dd";
    public static final String DATE_FORMAT_CHINA="yyyy年MM月dd日";
    public static final String YYYYMMDD="yyyyMMdd";

    /**
     * 格式化日期
     * @param date 如果date传null 格式化当前日期
     * @param format 格式 如果为null 默认取值
     * @return
     */
    public static String formatDate(Date date,String format){
        if(date==null){
            date=new Date();
        }
        if(format==null){
            format=DATE_FORMAT_PUBLIC;
        }
        return new SimpleDateFormat(format).format(date);
    }

    public static String formatDate(String format){
       return formatDate(new Date(),format);
    }

    public static String formatDate(){
        return formatDate(new Date(),DATE_FORMAT_PUBLIC);
    }
}
