package com.abcs.haiwaigou.local.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.getShaiXuanFirm;
import com.abcs.sociax.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Administrator on 2017/3/8 09:59.
 */

public class GirdHDPopAdapter extends BaseAdapter {

    Context context;
    List<getShaiXuanFirm.DatasBean.ActivityListBean> list=new ArrayList<>();
    public GirdHDPopAdapter(Context context, List<getShaiXuanFirm.DatasBean.ActivityListBean> list) {
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
            view= LayoutInflater.from(context).inflate(R.layout.item_hd_text,viewGroup,false);
            vieeHolder=new VieeHolder(view);
            view.setTag(vieeHolder);
        }else {
            vieeHolder=(VieeHolder) view.getTag();
        }

        getShaiXuanFirm.DatasBean.ActivityListBean bean= (getShaiXuanFirm.DatasBean.ActivityListBean) getItem(i);

        if(bean!=null){
            if(!TextUtils.isEmpty(bean.activityName)){
                vieeHolder.tv.setText(bean.activityName);
            }
        }

        if (selectedPosition == i) {
            vieeHolder.rela_bg.setSelected(true);
            vieeHolder.rela_bg.setPressed(true);
            vieeHolder.rela_bg.setBackgroundColor(context.getResources().getColor(R.color.red01));
        } else {
            vieeHolder.rela_bg.setSelected(false);
            vieeHolder.rela_bg.setPressed(false);
            vieeHolder.rela_bg.setBackgroundColor(context.getResources().getColor(R.color.left_item_bg_no));
        }
        return view;
    }
    public class VieeHolder{
        TextView tv;
        ImageView img;
        LinearLayout rela_bg;

        public VieeHolder(View view) {
            tv=(TextView) view.findViewById(R.id.tv);
            img=(ImageView) view.findViewById(R.id.img);
            rela_bg=(LinearLayout) view.findViewById(R.id.rela_bg);
        }
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    private int selectedPosition = -1;// 选中的位置

}