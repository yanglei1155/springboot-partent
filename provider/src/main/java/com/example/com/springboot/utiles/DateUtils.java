package com.example.com.springboot.utiles;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String getMillTimeToT(String date){
        SimpleDateFormat tFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ParsePosition pos = new ParsePosition(0);
        Date Dt=format.parse(date,pos);
        return  tFormat.format(Dt.getTime());
    }
    public static String getNowTime(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ParsePosition pos=new ParsePosition(0);
        Date date=new Date();
        return format.format(date.getTime());
    }
    public static String getMillTime(String date){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ParsePosition pos=new ParsePosition(0);
        Date dt = format.parse(date, pos);
        return String.valueOf(dt.getTime());
    }
    public static String getMillTimeDelT(String date){
        SimpleDateFormat tFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ParsePosition pos = new ParsePosition(0);
        Date Dt=tFormat.parse(date,pos);
        return format.format(Dt.getTime());
    }
    public static void main(String[] args) {
        System.out.println(getMillTime("2020-02-20 09:30"));
    }
}