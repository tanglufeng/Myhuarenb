package com.abcs.haiwaigou.local.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.PinLeiRight;
import com.abcs.sociax.android.R;

import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */

public class PinLeiGridViewAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private Context mContext;
    private List<PinLeiRight.DatasBean.Class3Bean> mCities;
    public PinLeiGridViewAdapter(Context context, List<PinLeiRight.DatasBean.Class3Bean> mCities) {
        this.mContext = context;
        this.mCities = mCities;
        this.inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mCities.size()==0?0:mCities.size();
    }

    @Override
    public PinLeiRight.DatasBean.Class3Bean getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_text_hwg,parent,false);
            cityGridViewHolder = new CityGridViewHolder();
            cityGridViewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(cityGridViewHolder);
        }else
        {
            cityGridViewHolder  = (CityGridViewHolder) convertView.getTag();
        }

        PinLeiRight.DatasBean.Class3Bean citysBean=mCities.get(position);

        if(!TextUtils.isEmpty(citysBean.className)){
            cityGridViewHolder.tv.setText(citysBean.className);
        }

            return convertView;
    }

    public  class CityGridViewHolder{
        TextView tv;
    }
}
