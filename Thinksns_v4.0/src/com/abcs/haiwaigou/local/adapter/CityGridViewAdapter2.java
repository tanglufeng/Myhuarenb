package com.abcs.haiwaigou.local.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.City2;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.img.RoundImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */

public class CityGridViewAdapter2 extends BaseAdapter{
    private LayoutInflater inflater;
    private Context mContext;
    private List<City2.BodyBean.DataBean.CountrysBean.CitysBean> mCities;
    public CityGridViewAdapter2(Context context, List<City2.BodyBean.DataBean.CountrysBean.CitysBean> mCities) {
        this.mContext = context;
        this.mCities = mCities;
        this.inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mCities.size();
    }

    @Override
    public City2.BodyBean.DataBean.CountrysBean.CitysBean getItem(int position) {
        return mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityGridViewHolder cityGridViewHolder = null;
        if (convertView==null){
            convertView = inflater.inflate(R.layout.local_activity_gv_city4_item,parent,false);
            cityGridViewHolder = new CityGridViewHolder();
            cityGridViewHolder.circ_iv = (RoundImageView) convertView.findViewById(R.id.civ);
            cityGridViewHolder.city_tv = (TextView) convertView.findViewById(R.id.tv_city4);
            cityGridViewHolder.city_english = (TextView) convertView.findViewById(R.id.tv_english_city4);
            convertView.setTag(cityGridViewHolder);
        }else
        {
            cityGridViewHolder  = (CityGridViewHolder) convertView.getTag();
        }


        if(!TextUtils.isEmpty(mCities.get(position).cate_name)&&!TextUtils.isEmpty(mCities.get(position).catename_en)){
            cityGridViewHolder.city_tv.setText(mCities.get(position).cate_name);
            cityGridViewHolder.city_english.setText(mCities.get(position).catename_en);
        }
        if(!TextUtils.isEmpty(mCities.get(position).oss_url)){
           MyApplication.imageLoader.displayImage(mCities.get(position).oss_url,cityGridViewHolder.circ_iv,Options.getHDOptions());
        }

            return convertView;
    }

    public  class CityGridViewHolder{
        RoundImageView circ_iv;
        TextView city_tv;
        TextView city_english;
    }
}
