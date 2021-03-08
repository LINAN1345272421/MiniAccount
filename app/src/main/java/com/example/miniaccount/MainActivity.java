package com.example.miniaccount;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements  View.OnClickListener,AdapterView.OnItemLongClickListener{
    private Button btnToday;
    private Button btnThisMonth;
    private Button btnThisYear;
    private Button btnQuery;
    private Button btnAdd;
    private Button btnReport;
    private ListView lstAcct;
    public List<Map<String,Object>> myData=null;
    private CAcctItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstAcct=(ListView)findViewById(R.id.lstAcct);
        lstAcct.setOnItemLongClickListener(this);
        btnToday=(Button)findViewById(R.id.btnToday);
        btnToday.setOnClickListener(this);
        btnThisMonth=(Button)findViewById(R.id.btnThisMonth);
        btnThisMonth.setOnClickListener(this);
        btnThisYear=(Button)findViewById(R.id.btnThisYear);
        btnThisYear.setOnClickListener(this);
        btnQuery=(Button)findViewById(R.id.btnQuery);
        btnQuery.setOnClickListener(this);
        btnAdd=(Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnReport=(Button)findViewById(R.id.btnReport);
        btnReport.setOnClickListener(this);
        loadToday();
    }
    public void onClick(View view){
        int vid=view.getId();
        if(vid==R.id.btnToday){
            loadToday();
        }else if(vid==R.id.btnThisMonth){
            loadThisMonth();
        }else if(vid==R.id.btnThisYear){
            loadThisYear();
        }else if(vid==R.id.btnQuery){
            Intent intent=new Intent(this,QueryActivity.class);
            startActivityForResult(intent,1);
        }else if(vid==R.id.btnAdd){
            Intent intent=new Intent(this,AddActivity.class);
            startActivity(intent);
        }else if(vid==R.id.btnReport){
            Intent intent=new Intent(this,ReportActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //根据查询条件显示数据
            Bundle b = data.getBundleExtra("query_condition");
            String keyword = b.getString("keyword");
            long ifrom = b.getLong("datefrom");
            long ito = b.getLong("dateto");
            long itype = b.getLong("type");
            CAccount acct = CAccount.getInstance(this, 1);
            myData = acct.query(keyword, ifrom, ito, itype);
            adapter = new CAcctItemAdapter(this, myData);
            lstAcct.setAdapter(adapter);
        }
    }

    //载入今天账目
    private void loadToday(){
        CAccount acct=CAccount.getInstance(this,1);
        myData=acct.queryByDay(System.currentTimeMillis());
        adapter=new CAcctItemAdapter(this,myData);
        lstAcct.setAdapter(adapter);
    }
    //载入本月账目
    private void loadThisMonth(){
        CAccount acct=CAccount.getInstance(this,1);
        myData=acct.queryByMonth(System.currentTimeMillis());
        adapter=new CAcctItemAdapter(this,myData);
        lstAcct.setAdapter(adapter);
    }
    //载入本年账目
    private void loadThisYear(){
        CAccount acct=CAccount.getInstance(this,1);
        myData=acct.queryByYear(System.currentTimeMillis());
        adapter=new CAcctItemAdapter(this,myData);
        lstAcct.setAdapter(adapter);
    }
    //长安删除账目
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        final int acctid=(int) l;
        if(acctid>0){
            AlertDialog.Builder dlg=new AlertDialog.Builder(MainActivity.this);
            dlg.setTitle("迷你账本").setMessage("请问确定要删除吗？").setCancelable(false);
            dlg.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CAccount acct=CAccount.getInstance(MainActivity.this,1);
                    acct.delete(acctid);
                    myData.remove(i);
                    lstAcct.setAdapter(adapter);
                }
            });
            dlg.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dlg.show();
        }
        return false;
    }
}