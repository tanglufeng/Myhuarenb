package com.abcs.hqbtravel.adapter;


import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.hqbtravel.entity.ZhenXuan;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;

import java.util.List;

/**
 */
public class TravelZhenXuanAdapter extends BaseAdapter {

    private Activity mcontext;
    private LayoutInflater inflater;
    private List<ZhenXuan.BodyEntry> data;

    public TravelZhenXuanAdapter(Activity mcontext, List<ZhenXuan.BodyEntry> data) {
        this.mcontext = mcontext;
        inflater = LayoutInflater.from(mcontext);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ZhenXuan.BodyEntry getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_travle_zhenxuan,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }

        ZhenXuan.BodyEntry bodyEntry=data.get(position);

        if(!TextUtils.isEmpty(bodyEntry.img)){
            MyApplication.imageLoader.displayImage(bodyEntry.img, holder.img_logo, MyApplication.getListOptions());
        }

        if(!TextUtils.isEmpty(bodyEntry.title)){
            holder.tv_name.setText(bodyEntry.title);
            holder.tv_name.setVisibility(View.VISIBLE);
        }else {
            holder.tv_name.setVisibility(View.GONE);
        }

        return convertView;
    }

    public class ViewHolder{
        ImageView img_logo;
        TextView tv_name,tv_tag;

        public ViewHolder(View view) {

            img_logo=(ImageView) view.findViewById(R.id.img_logo);
            tv_name=(TextView) view.findViewById(R.id.tv_name);
            tv_tag=(TextView) view.findViewById(R.id.tv_tag);
        }
    }

}
