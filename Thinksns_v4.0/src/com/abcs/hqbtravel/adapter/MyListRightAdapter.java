package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abcs.hqbtravel.entity.ShaiXuanBean;
import com.abcs.sociax.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Administrator on 2017/3/8 10:00.
 */

public class MyListRightAdapter extends BaseAdapter {

    Context context;
    List<ShaiXuanBean.BodyBean.FilterBean.ChildNameBean> list=new ArrayList<>();
    public MyListRightAdapter(Context context) {
        this.context = context;
    }

    public void addAllData(List<ShaiXuanBean.BodyBean.FilterBean.ChildNameBean> date){
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
    public ShaiXuanBean.BodyBean.FilterBean.ChildNameBean getItem(int i) {
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
            view= LayoutInflater.from(context).inflate(R.layout.item_shuaixuan_text_right,viewGroup,false);
            vieeHolder=new VieeHolder(view);
            view.setTag(vieeHolder);
        }else {
            vieeHolder=(VieeHolder) view.getTag();
        }

        final ShaiXuanBean.BodyBean.FilterBean.ChildNameBean item=getItem(i);

        if(item!=null){
            if (!TextUtils.isEmpty(item.name)) {
                vieeHolder.textView.setText(item.name);
            }
        }

        return view;
    }

    public class VieeHolder{
        TextView textView;

        public VieeHolder(View view) {
            textView = (TextView) view.findViewById(R.id.tv);
        }
    }
}
