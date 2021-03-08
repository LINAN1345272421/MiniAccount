package com.example.miniaccount;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.telephony.cdma.CdmaCellLocation;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.BatchUpdateException;
import java.util.Calendar;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener{
    private EditText txtFrom;
    private EditText txtTo;
    private TextView txtExpenditure;
    private TextView txtIncome;
    private TextView txtSurplus;
    private Button btnCalculate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_layout);
        setTitle("统计");
        CDateTime dt=new CDateTime();
        txtFrom=(EditText)findViewById(R.id.txtReportFrom);
        txtFrom.setOnLongClickListener(this);
        txtFrom.setText(dt.toDateString());
        txtTo=(EditText)findViewById(R.id.txtReportTo);
        txtTo.setOnLongClickListener(this);
        txtTo.setText(dt.toDateString());
        txtExpenditure=(TextView)findViewById(R.id.txtReportExpenditure);
        txtIncome=(TextView)findViewById(R.id.txtReportIncome);
        txtSurplus=(TextView)findViewById(R.id.txtReportSurplus);
        btnCalculate=(Button)findViewById(R.id.btnCalculate);
        btnCalculate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnCalculate){
            //整理日期
            long startTime=0L,endTime=0L;
            String sFrom=txtFrom.getText().toString().trim();
            String sTo=txtTo.getText().toString().trim();
            if(CDateTime.checkDateString(sFrom,"-")&&CDateTime.checkDateString(sTo,"-")){
                startTime=new CDateTime(sFrom,"-").startOfDay();
                endTime=new CDateTime(sTo,"-").endOfDay();
            }
            //显示计算结果
            CAccount acct=CAccount.getInstance(this,1);
            double incomeSum=acct.incomeSum(startTime,endTime);
            double expenditureSum=acct.expenditureSum(startTime,endTime);
            txtIncome.setText(String.format("%s:%.2f","总收入",incomeSum));
            txtExpenditure.setText(String.format("%s:%.2f","总支出",expenditureSum));
            txtSurplus.setText(String.format("%s:%.2f","结余",incomeSum-expenditureSum));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        int vid=v.getId();
        if(vid==R.id.txtReportFrom){
            CDateTime dt=new CDateTime();
            DatePickerDialog dlg=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    txtFrom.setText(String.format("%d-%d-%d",year,month+1,dayOfMonth));
                }
            },dt.year(),dt.month()-1,dt.day());
            dlg.show();
        }else if(vid==R.id.txtReportTo){
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
}