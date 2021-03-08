package com.example.miniaccount;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.cdma.CdmaCellLocation;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class QueryActivity extends AppCompatActivity implements View.OnLongClickListener,
        View.OnClickListener{
    private EditText txtTitle;
    private EditText txtFrom;
    private EditText txtTo;
    private RadioButton rdoAll;
    private RadioButton rdoExpenditure;
    private RadioButton rdoIncome;
    private Button btnSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_layout);
        setTitle("查询");
        CDateTime dt=new CDateTime();
        txtTitle=(EditText)findViewById(R.id.txtQueryTitle);
        txtFrom=(EditText)findViewById(R.id.txtQueryFrom);
        txtFrom.setOnLongClickListener(this);
        txtFrom.setText(dt.toDateString());
        txtTo=(EditText)findViewById(R.id.txtQueryTo);
        txtTo.setOnLongClickListener(this);
        txtTo.setText(dt.toDateString());
        rdoAll=(RadioButton)findViewById(R.id.rdoQueryAll);
        rdoExpenditure=(RadioButton)findViewById(R.id.rdoQueryExpenditure);
        rdoIncome=(RadioButton)findViewById(R.id.rdoQueryIncome);
        btnSearch=(Button)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
    }
//长按时显示时间选择对话框
    @Override
    public boolean onLongClick(View v) {
        int vid=v.getId();
        if(vid==R.id.txtQueryFrom){
            CDateTime dt=new CDateTime();
            DatePickerDialog dlg=new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener(){
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    txtFrom.setText(String.format("%d-%d-%d",year,month+1,dayOfMonth));
                }
            },dt.year(),dt.month()-1,dt.day());
            dlg.show();
        }else if(vid==R.id.txtQueryTo){
            CDateTime dt=new CDateTime();
            DatePickerDialog dlg=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    txtTo.setText(String.format("%d-%d-%d",year,month+1,dayOfMonth));
                }
            },dt.year(),dt.month()-1,dt.day());
            dlg.show();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnSearch){
            //整理查询条件
            String sFrom=txtFrom.getText().toString().trim();//trim删除空白字符
            String sTo=txtTo.getText().toString().trim();
            long ifrom=0,ito=0;
            if(CDateTime.checkDateString(sFrom,"-")&&
                    CDateTime.checkDateString(sTo,"-")){
                ifrom=new CDateTime(sFrom,"-").startOfDay();
                ito=new CDateTime(sTo,"-").endOfDay();
            }
            long itype=0;
            if(rdoExpenditure.isChecked()) itype=1;
            else if(rdoIncome.isChecked()) itype=2;
            //传递查询参数
            Intent intent=new Intent();
            Bundle b=new Bundle();
            b.putString("keyword",txtTitle.getText().toString());
            b.putLong("datefrom",ifrom);
            b.putLong("dateto",ito);
            b.putLong("type",itype);
            intent.putExtra("query_condition",b);
            setResult(RESULT_OK,intent);//返回到MainActivity执行查询
            finish();
        }
    }
    //通过返回键返回主界面

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        setResult(RESULT_CANCELED,intent);
        finish();
    }
}