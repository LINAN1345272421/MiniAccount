package com.example.miniaccount;

import android.app.UiAutomation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CAccount extends SQLiteOpenHelper {
    public CAccount(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static CAccount getInstance(Context context, int version){
        return new CAccount(context,"miniAccount.db",null,version);
    }

    private static final String createAccountTableSql=
            "create table if not exists account("+
                    "acctid integer primary key,"+
                    "accttitle text not null,"+
                    "acctamount float,"+
                    "acctdate integer,"+
                    "acctyear integer,"+
                    "acctmonth integer,"+
                    "acctday integer,"+
                    "accttype integer);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createAccountTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insert(String title,double amount,long type,CDateTime dt){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues data=new ContentValues();
        data.put("accttitle",title);
        data.put("acctamount",amount);
        data.put("accttype",type);
        data.put("acctdate",dt.getTimeInMillis());
        data.put("acctyear",dt.year());
        data.put("acctmonth",dt.month());
        data.put("acctday",dt.day());
        long result=db.insert("account",null,data);
        db.close();
        return result;
    }
    public long delete(long id){
        SQLiteDatabase db=this.getWritableDatabase();
        long result=db.delete("account","acctid="+id,null);
        db.close();
        return result;
    }
    public static final String fields="acctid,accttitle,acctamount,acctdate,accttype,acctyear,acctmonth,acctday";
    private  static List<Map<String,Object>> toList(Cursor cursor){
        List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
        if((cursor!=null)&&cursor.moveToFirst()){
            do{
                Map<String,Object> map=new HashMap<String ,Object>();
                map.put("acctId",cursor.getLong(0));
                map.put("acctTitle",cursor.getString(1));
                map.put("acctAmount",cursor.getDouble(2));
                map.put("acctDate",cursor.getLong(3));
                map.put("acctType",cursor.getLong(4));
                map.put("acctYear",cursor.getLong(5));
                map.put("acctMonth",cursor.getLong(6));
                map.put("acctDay",cursor.getLong(7));
                result.add(map);
            }while(cursor.moveToNext());
        }
        return result;
    }
    //按天查询
    public List<Map<String,Object>> queryByDay(long msec){
        SQLiteDatabase db=this.getReadableDatabase();
        String sql=String.format("select %s from account where acctdate %s order by acctdate desc;",
                fields,CDateTime.SqlBuilder.inDay(msec));//format()函数与printf函数类似，可变字符 即%s处的字符串可变。%d表示整型，%c字符型
        Cursor cursor=db.rawQuery(sql,null);
        List<Map<String,Object>> result=toList(cursor);
        db.close();
        return result;
    }
    //按月查询
    public List<Map<String,Object>> queryByMonth(long msec){
        SQLiteDatabase db=this.getReadableDatabase();
        String sql=String.format("select %s from account where acctdate %s order by acctdate desc;",
                fields,CDateTime.SqlBuilder.inMonth(msec));
        Cursor cursor=db.rawQuery(sql,null);
        List<Map<String,Object>> result =toList(cursor);
        db.close();
        return result;
    }
    //按年查询
    public List<Map<String,Object>> queryByYear(long msec){
        SQLiteDatabase db=this.getReadableDatabase();
        String sql=String.format("select %s from account where acctdate %s order by acctdate desc;",
                fields,CDateTime.SqlBuilder.inYear(msec));
        Cursor cursor=db.rawQuery(sql,new String[]{});
        List<Map<String,Object>> result=toList(cursor);
        db.close();
        return result;
    }
    //综合查询
    public List<Map<String,Object>> query(String keyword,long startTime,long endTime,long type){
        //创建SQL
        StringBuilder sb=new StringBuilder(300);
        sb.append(String.format("select %s from account where accttitle like \'",fields));
        sb.append("%");
        sb.append(keyword);
        sb.append("%\'");
        //日期范围
        if(!(startTime==0&&endTime==0)){
            sb.append(String.format("and (acctdate %s)",CDateTime.SqlBuilder.dateRange(startTime,endTime)));
        }
        //类型
        if(type==1||type==2){
            sb.append(String.format("and (accttype=%d)",type));
        }
        sb.append(" order by acctdate desc;");
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.rawQuery(sb.toString(),new String[]{});
        List<Map<String,Object>> result=toList(cursor);
        db.close();
        return result;
    }
    //计算指定日期范围内的总收入
    public double incomeSum(long startTime,long endTime){
        String sql;
        if(startTime==0&&endTime==0){
            sql=String.format("select sum(acctamount) from account where accttype=2;");
        }else{
            sql=String.format("select sum(acctamount) from account where accttype=2 and acctdate %s",
                    CDateTime.SqlBuilder.dateRange(startTime,endTime));
        }
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.rawQuery(sql,new String[]{});
        double result=0d;
        if(cursor.moveToFirst()) result=cursor.getDouble(0);
        db.close();
        return result;
    }
    //计算指定日期范围内的总支出
    public double expenditureSum(long startTime,long endTime){
        String sql;
        if(startTime==0&&endTime==0){
            sql=String.format("select sum(acctamount) from account where accttype=1;");
        }else{
            sql=String.format("select sum(acctamount) from account where accttype=1 and acctdate %s",
                    CDateTime.SqlBuilder.dateRange(startTime, endTime));
        }
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.rawQuery(sql,new String[]{});
        double result=0d;
        if(cursor.moveToFirst()) result=cursor.getDouble(0);
        db.close();
        return result;
    }
}
