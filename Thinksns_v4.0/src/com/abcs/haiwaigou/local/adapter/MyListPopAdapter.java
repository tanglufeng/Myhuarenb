package com.abcs.haiwaigou.local.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.SSXaun;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Administrator on 2017/3/8 09:59.
 */

public class MyListPopAdapter extends BaseAdapter {

    Context context;
    List<SSXaun.DatasEntry> list=new ArrayList<>();
    public MyListPopAdapter(Context context, List<SSXaun.DatasEntry> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
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
            view= LayoutInflater.from(context).inflate(R.layout.item_huohang_one,viewGroup,false);
            vieeHolder=new VieeHolder(view);
            view.setTag(vieeHolder);
        }else {
            vieeHolder=(VieeHolder) view.getTag();
        }

        SSXaun.DatasEntry bean= (SSXaun.DatasEntry) getItem(i);

        if(bean!=null){
            if(!TextUtils.isEmpty(bean.gcName)){
                vieeHolder.tv_title.setText(bean.gcName);
            }
            if(!TextUtils.isEmpty(bean.img)){
                MyApplication.imageLoader.displayImage(bean.img,vieeHolder.tv_icon,MyApplication.getListOptions());
            }

            if(!TextUtils.isEmpty(bean.cEnName)){
                vieeHolder.tv_en_name.setText(bean.cEnName);
                vieeHolder.tv_en_name.setVisibility(View.VISIBLE);
            }else {
                vieeHolder.tv_en_name.setVisibility(View.GONE);
            }
        }

        if (selectedPosition == i) {
            vieeHolder.line_bg.setVisibility(View.VISIBLE);
            vieeHolder.rela_bg.setSelected(true);
            vieeHolder.rela_bg.setPressed(true);
            vieeHolder.rela_bg.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            vieeHolder.line_bg.setVisibility(View.GONE);
            vieeHolder.rela_bg.setSelected(false);
            vieeHolder.rela_bg.setPressed(false);
            vieeHolder.rela_bg.setBackgroundColor(context.getResources().getColor(R.color.left_item_bg_no));
        }
        return view;
    }
    public class VieeHolder{
        TextView tv_title,tv_en_name,line_bg;
        ImageView tv_icon;
        RelativeLayout rela_bg;

        public VieeHolder(View view) {
            line_bg=(TextView) view.findViewById(R.id.line_bg);
            tv_title=(TextView) view.findViewById(R.id.tv_title);
            tv_en_name=(TextView) view.findViewById(R.id.tv_en_name);
            tv_icon=(ImageView) view.findViewById(R.id.tv_icon);
            rela_bg=(RelativeLayout) view.findViewById(R.id.rela_bg);
        }
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    private int selectedPosition = -1;// 选中的位置

}