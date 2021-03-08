package com.example.miniaccount;

public class CC {
    public static int toInt(String s){
        try{
            return Integer.parseInt(s);
        }catch (Exception e){
            return 0;
        }
    }
    public static long toLong(String s){
        try{
            return Long.parseLong(s);
        }catch(Exception e){
            return 0L;
        }
    }
    public static float toSng(String s){
        try{
            return Float.parseFloat(s);
        }catch (Exception e){
            return 0;
        }
    }
    public static double toDbl(String s){
        try{
            return Double.parseDouble(s);
        }catch (Exception e){
            return 0;
        }
    }
    //判断value中的值是否在values中
    public static <T> boolean inList(T  value,T value1,T... values) {
        if(value==value1)return true;
        for(T v:values){
            if(value==v)return true;
        }
        return false;
    }
}
