package com.example.miniaccount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelStoreOwner;

import android.app.DatePickerDialog;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity implements View.OnClickListener,
        View.OnLongClickListener, DatePickerDialog.OnDateSetListener {

    private EditText txtTitle;
    private EditText txtAmount;
    private EditText txtDate;
    private RadioButton rdoExpenditure;
    private RadioButton rdoIncome;
    private Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_layout);
        setTitle("新账目");
        txtTitle =(EditText)findViewById(R.id.txtAddAcctTitle);
        txtAmount=(EditText)findViewById(R.id.txtAddAcctAmount);
        txtDate=(EditText)findViewById(R.id.txtAddAcctDate);
        txtDate.setOnLongClickListener(this);
        rdoExpenditure=(RadioButton)findViewById(R.id.rdoAddAcctExpenditure);
        rdoIncome=(RadioButton)findViewById(R.id.rdoAddAcctIncome);
        CDateTime dt=new CDateTime();
        txtDate.setText(dt.toDateString());
        btnSave=(Button)findViewById(R.id.btnAddSave);
        btnSave.setOnClickListener(this);
    }
    //长按EditText组件时会显示日期选择对话框
    @Override
    public boolean onLongClick(View v) {
        if(v.getId()==R.id.txtAddAcctDate){
            CDateTime dt=new CDateTime();
            DatePickerDialog dlg=new DatePickerDialog(this,this,dt.year(),dt.month()-1,dt.day());
            //此用于显示一个时间选择
            dlg.show();
        }
        return false;
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        txtDate.setText(String.format("%d-%d-%d",year,month+1,dayOfMonth));
    }
    //

    //单击保存时会执行数据检查以及保存操作
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnAddSave){
            String title=txtTitle.getText().toString().trim();
            if(title.equals("")){
                Toast.makeText(this,"标题为空",Toast.LENGTH_LONG).show();
                return;
            }
            double amount=CC.toDbl(txtAmount.getText().toString());
            if(amount<=0){
                Toast.makeText(this,"金额小于0",Toast.LENGTH_LONG).show();
                return;
            }
            long type=(rdoExpenditure.isChecked()?1:2);
            if(CDateTime.checkDateString(txtDate.getText().toString(),"-")==false){
                Toast.makeText(this,"日期格式不正确",Toast.LENGTH_LONG).show();
                return;
            }
            CDateTime dt=new CDateTime(txtDate.getText().toString(),"-");
            //保存
            CAccount acct=CAccount.getInstance(this,1);
            long result=acct.insert(title,amount,type,dt);
            if(result>0){
                Toast.makeText(this,"保存成功",Toast.LENGTH_LONG).show();
                txtTitle.setText("");
            }else{
                Toast.makeText(this,"保存失败",Toast.LENGTH_LONG).show();
            }
        }
    }


}