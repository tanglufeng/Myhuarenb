package com.abcs.hqbtravel.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.City1;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.img.RoundImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */

public class CityGridViewAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private Context mContext;
    private List<City1.BodyBean.DataBean.CitysBean> mCities;
    public CityGridViewAdapter(Context context, List<City1.BodyBean.DataBean.CitysBean> mCities) {
        this.mContext = context;
        this.mCities = mCities;
        this.inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mCities.size()==0?0:mCities.size();
    }

    @Override
    public City1.BodyBean.DataBean.CitysBean getItem(int position) {
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
            convertView = inflater.inflate(R.layout.local_activity_gv_city3_item,parent,false);
            cityGridViewHolder = new CityGridViewHolder();
            cityGridViewHolder.circ_iv = (RoundImageView) convertView.findViewById(R.id.civ);
            cityGridViewHolder.city_tv = (TextView) convertView.findViewById(R.id.tv_city3);
            convertView.setTag(cityGridViewHolder);
        }else
        {
            cityGridViewHolder  = (CityGridViewHolder) convertView.getTag();
        }

        City1.BodyBean.DataBean.CitysBean citysBean=mCities.get(position);

        if(!TextUtils.isEmpty(citysBean.cateName)){
            cityGridViewHolder.city_tv.setText(citysBean.cateName);
        }
        if(!TextUtils.isEmpty(citysBean.ossUrl)){
           MyApplication.imageLoader.displayImage(citysBean.ossUrl,cityGridViewHolder.circ_iv, MyApplication.options);
        }

            return convertView;
    }

    public  class CityGridViewHolder{
        RoundImageView circ_iv;
        TextView city_tv;
    }
}
