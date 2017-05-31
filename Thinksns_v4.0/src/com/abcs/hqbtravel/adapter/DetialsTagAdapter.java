package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.abcs.sociax.android.R;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class DetialsTagAdapter extends BaseAdapter {
    private Context context;
    private List<String> data;
    private LayoutInflater inflater;

    public DetialsTagAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_textview,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
            viewHolder=(ViewHolder)convertView.getTag();
            viewHolder.tv_name_jingdian1.setText(data.get(position));
        return convertView;
    }

    private class ViewHolder{
        TextView tv_name_jingdian1;

        public ViewHolder(View view) {
            tv_name_jingdian1=(TextView)view.findViewById(R.id.tv_tab);
        }
    }
}
