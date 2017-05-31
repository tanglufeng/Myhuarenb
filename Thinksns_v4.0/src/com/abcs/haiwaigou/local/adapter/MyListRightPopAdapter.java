package com.abcs.haiwaigou.local.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.SSXaun;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Administrator on 2017/3/8 10:00.
 */

public class MyListRightPopAdapter extends BaseAdapter {

    Context context;
    public  List<SSXaun.DatasEntry> list=new ArrayList<>();
    public MyListRightPopAdapter(Context context) {
        this.context = context;
    }

    public List<SSXaun.DatasEntry> getList() {
        return list;
    }

    public MyListRightPopAdapter(Context context, List<SSXaun.DatasEntry> list) {
        this.context = context;
        this.list = list;
    }

    public void addAllData(List<SSXaun.DatasEntry> date){
        if(date!=null){
            if(date.size()>0){
                list.clear();
                list.addAll(date);
                notifyDataSetChanged();
            }
        }
    }
    
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SSXaun.DatasEntry getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        VieeHolder vieeHolder=null;
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_huohang_two,viewGroup,false);
            vieeHolder=new VieeHolder(view);
            view.setTag(vieeHolder);
        }else {
            vieeHolder=(VieeHolder) view.getTag();
        }

        final SSXaun.DatasEntry item=getItem(i);

        if(item!=null){
            if (!TextUtils.isEmpty(item.gcName)) {
                vieeHolder.title.setText(item.gcName);
            }

            if (!TextUtils.isEmpty(item.img)) {
                MyApplication.imageLoader.displayImage(item.img,vieeHolder.iv_logo,MyApplication.getListOptions());
            }
        }

        return view;
    }

    public class VieeHolder{
        TextView title;
        ImageView iv_logo;

        public VieeHolder(View view) {
            title = (TextView) view.findViewById(R.id.tv);
            iv_logo = (ImageView) view.findViewById(R.id.iv_logo);
        }
    }
}
