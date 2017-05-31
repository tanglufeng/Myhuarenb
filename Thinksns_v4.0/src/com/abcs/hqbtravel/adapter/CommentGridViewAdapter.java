package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;

import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */

public class CommentGridViewAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private Context mContext;
    private List<String> mCities;
    public CommentGridViewAdapter(Context context, List<String> mCities) {
        this.mContext = context;
        this.mCities = mCities;
        this.inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mCities.size()==0?0:mCities.size();
    }

    @Override
    public String getItem(int position) {
        return mCities.get(position)==null? null:mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityGridViewHolder cityGridViewHolder = null;
        if (convertView==null){
            convertView = inflater.inflate(R.layout.item_travel_comment,parent,false);
            cityGridViewHolder = new CityGridViewHolder();
            cityGridViewHolder.logo = (ImageView) convertView.findViewById(R.id.logo);
            cityGridViewHolder.rela_gongjiz = (RelativeLayout) convertView.findViewById(R.id.rela_gongjiz);
            cityGridViewHolder.tv_gongjizhang = (TextView) convertView.findViewById(R.id.tv_gongjizhang);
            convertView.setTag(cityGridViewHolder);
        }else
        {
            cityGridViewHolder  = (CityGridViewHolder) convertView.getTag();
        }
        if(!TextUtils.isEmpty(mCities.get(position))){
           MyApplication.imageLoader.displayImage(mCities.get(position),cityGridViewHolder.logo, MyApplication.options);
        }

        if(position==2){
            cityGridViewHolder.rela_gongjiz.setVisibility(View.VISIBLE);
            cityGridViewHolder.tv_gongjizhang.setText("共"+mCities.size()+"张");
        }
            return convertView;
    }

    public  class CityGridViewHolder{
        ImageView logo;
        RelativeLayout rela_gongjiz;
        TextView tv_gongjizhang;
    }
}
