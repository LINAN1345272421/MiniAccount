package com.example.miniaccount;

import android.telephony.cdma.CdmaCellLocation;

import java.util.Calendar;
import java.util.concurrent.CancellationException;

public class CDateTime {
    public static final long MSEC_PER_DAY = 86400000L;
    private Calendar myCalendar=Calendar.getInstance();//取得一个Calendar的实例


    /*构造部分*/
    public CDateTime(){

    }
    public CDateTime(long msec){
        this();
        myCalendar.setTimeInMillis(msec);//根据传递的long值设置由此Calendar表示的Calendars时间。
    }
    public CDateTime(int year,int month,int day,int hour,int minute,int second){
        this();
        myCalendar.set(year,month-1,day,hour,minute,second);//月份从0开始
    }
    public CDateTime(int year,int month,int day){
        this();
        myCalendar.set(year,month-1,day,0,0,0);
        myCalendar.set(Calendar.MILLISECOND,0);//设置秒为0
    }
    public CDateTime(String sDate,String separator){//将字符日期分解为数字
        this();
        String[] date=sDate.split(separator);
        if(date.length!=3)return;
        int y=Integer.parseInt(date[0]);
        int m=Integer.parseInt(date[1]);
        int d=Integer.parseInt(date[2]);
        if(checkDate(y,m,d))
            myCalendar.set(y,m-1,d);
    }
    public static boolean checkDate(int year,int month,int day){//检查日期是否符合格式
        if(CC.inList(month,1,3,5,7,8,10,12)){
            return (day>=1&&day<=31);
        }else if(CC.inList(month,4,6,9,11)){
            return (day>=1&&day<=30);
        }else if(month==2){
            int max=(isLeapYear(year)?29:28);
            return (day>=1&&day<=max);
        }else{
            return false;
        }
    }
    public static boolean checkDateString(String sDate,String separator){
        String[]date =sDate.split(separator);
        if(date.length!=3)return false;
        return checkDate(Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2]));
    }//检查日期字符是否符合
    /*构造部分*/


    /*set与get部分*/
    public void setMes(long msec){
        myCalendar.setTimeInMillis(msec);
    }
    public void setDateTime(int year,int month,int day,int hour,int minute,int second){
        myCalendar.set(year, month-1, day, hour, minute, second);
    }
    public void setDateTime(int year,int month,int day){
        myCalendar.set(year, month-1, day,0,0,0);
        myCalendar.set(Calendar.MILLISECOND,0);
    }

    public int year(){
        return myCalendar.get(Calendar.YEAR);
    }
    public boolean isLeapYear(){//判断是否为闰年
        int y=year();
        return isLeapYear(y);
    }
    public static boolean isLeapYear(int y){
        return (y%400==0||(y%100!=0&&y%4==0));
    }
    public int month() {
        return myCalendar.get(Calendar.MONTH)+1;
    }
    public int day(){
        return myCalendar.get(Calendar.DAY_OF_MONTH);
    }
    public int weekday(){
        return myCalendar.get(Calendar.DAY_OF_WEEK)-1;
    }
    public int weekOfMonth(){
        return myCalendar.get(Calendar.WEEK_OF_MONTH);
    }
    public int quarter(){
        return myCalendar.get(Calendar.MONTH)/3+1;
    }
    public int hour(){
        return myCalendar.get(Calendar.HOUR_OF_DAY);
    }
    public int minute(){
        return myCalendar.get(Calendar.MINUTE);
    }
    public int second(){
        return myCalendar.get(Calendar.SECOND);
    }
    public int millisecond(){
        return myCalendar.get(Calendar.MILLISECOND);
    }
    public long getTimeInMillis(){
        return myCalendar.getTimeInMillis();
    }
    public static int daysInMonth(int year,int month){
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return (isLeapYear(year)?29:28);
            default:
                return -1;
        }
    }
    public String toDateString(){
        return String.format("%d-%d-%d",year(),month(),day());
    }
    /*set与get部分*/


    public static long startOfDay(long msec){
        return msec/MSEC_PER_DAY*MSEC_PER_DAY;
    }
    public static long endOfDay(long msec){return (msec/MSEC_PER_DAY+1L)*MSEC_PER_DAY-1L;}
    public long startOfDay(){
        long msec=myCalendar.getTimeInMillis();
        return startOfDay(msec);
    }
    public long endOfDay(){
        long msec=myCalendar.getTimeInMillis();
        return endOfDay(msec);
    }



    /*数据库操作*/
    public static class SqlBuilder{
        //指定两个日期范围
        public static String dateRange(long startTime,long endTime){
            long min,max;
            if(startTime<=endTime){
                min=startOfDay(startTime);
                max=endOfDay(endTime);
            }else{
                min=startOfDay(endTime);
                max=endOfDay(startTime);
            }
            return String.format("between %d and %d",min,max);
        }
        //指定某一天
        public static String inDay(long msec){
            long startTime=startOfDay(msec);
            long endTime=endOfDay(msec);
            return String.format("between %d and %d",startTime,endTime);
        }
        //指定某个月
        public static String inMonth(long msec){
            CDateTime dt=new CDateTime(msec);
            int year =dt.year();
            int month=dt.month();
            CDateTime dtStart=new CDateTime(year,month,1);
            CDateTime dtEnd=new CDateTime(year,month,daysInMonth(year,month));
            return String.format("between %d and %d",dtStart.startOfDay()+MSEC_PER_DAY,dtEnd.endOfDay()+MSEC_PER_DAY);
        }
        //指定某一年
        public static String inYear(long msec){
            CDateTime dt=new CDateTime(msec);
            int year=dt.year();
            CDateTime dtStart=new CDateTime(year,1,1);
            CDateTime dtEnd=new CDateTime(year,12,31);
            return String.format("between %d and %d",dtStart.startOfDay()+MSEC_PER_DAY,dtEnd.endOfDay()+MSEC_PER_DAY);
        }
    }
    /*数据库操作*/
    
}
