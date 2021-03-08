package com.example.miniaccount;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CAcctItemAdapter extends BaseAdapter {
    private List<Map<String,Object>> myList;
    private LayoutInflater myInflater;//布局服务
    public CAcctItemAdapter(Context context,List<Map<String,Object>> lst){
        myList=lst;
        myInflater=LayoutInflater.from(context);
    }
    @Override
    public boolean areAllItemsEnabled(){
        return true;
    }//条目是否可点击
    @Override
    public boolean isEnabled(int i){
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver){//数据改变时监听器
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {//解除监听器
    }
    @Override
    public int getCount() {//要绑定的数目
        if(myList==null)return 0;
        else return myList.size();
    }

    @Override
    public Object getItem(int position) {//根据索引获取对象
        if(myList==null) return null;
        else return myList.get(position);
    }

    @Override
    public long getItemId(int position) {//获取条目id
        if(myList==null) return -1L;
        else return (long)myList.get(position).get("acctId");
    }

    @Override
    public boolean hasStableIds() {//起到局部刷新的优化效果
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//获取显示界面
        if(convertView==null){
            convertView=myInflater.inflate(R.layout.layout,null);//读取布局文件
        }
        TextView txtTitle=convertView.findViewById(R.id.txtTitle);
        txtTitle.setText((String)(myList.get(position).get("acctTitle")));
        TextView txtAmount=convertView.findViewById(R.id.txtAmount);
        txtAmount.setText(String.format("%.2f",myList.get(position).get("acctAmount")));
        TextView textDate=convertView.findViewById(R.id.txtDate);
        textDate.setText(String.format("%d-%d-%d"
                ,myList.get(position).get("acctYear")
                ,myList.get(position).get("acctMonth")
                ,myList.get(position).get("acctDay")));
        ImageView imgType=convertView.findViewById(R.id.imgType);
        long type=(long)(myList.get(position).get("acctType"));
        if(type==1){
            imgType.setImageResource(R.drawable.type_1);
        }else{
            imgType.setImageResource(R.drawable.type_2);
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {//获取视图类型
        return R.layout.layout;
    }

    @Override
    public int getViewTypeCount() {//返回视图创建的数量
        return 1;
    }

    @Override
    public boolean isEmpty() {//数据是否为空
        return false;
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
