package com.abcs.haiwaigou.local.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.abcs.haiwaigou.local.beans.City;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.sociax.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author zaaach on 2016/1/26.
 */
public class HotCityGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<City> mCities;

    public HotCityGridAdapter(Context context) {
        this.mContext = context;
        mCities = new ArrayList<>();
        SharedPreferences sp = context.getSharedPreferences(MyString.CITY_CACHE, 0);
        String save_history = sp.getString(MyString.CITYS, "");
        String[] citys = save_history.split("##");
        if (citys.length >= 9) {
            for (int i = 9; i > -1; i--) {
                if (!citys[i].equals("")) {
                    String[] city = citys[i].split(",");
                    initData(city);
                }
            }
        } else
            for (int i = citys.length - 1; -1 < i; i--) {
                if (!citys[i].equals("")) {
                    String[] city = citys[i].split(",");
                    initData(city);
                }
            }
//        mCities.add("北京");
//        mCities.add("上海");
//        mCities.add("广州");
//        mCities.add("深圳");
//        mCities.add("杭州");
//        mCities.add("南京");
//        mCities.add("天津");
//        mCities.add("武汉");
//        mCities.add("重庆");
    }

    private void initData(String[] city) {
        if (city.length == 6) {
            City city1 = new City();
            city1.setCountryCnName(city[0]);
            city1.setCountryId(city[1]);
            city1.setCate_name(city[2]);
            city1.setCatename_en(city[3]);
            city1.setCity_id(city[4]);
            city1.setAreaId(city[5]);
            mCities.add(city1);
        }
    }


    @Override
    public int getCount() {
        return mCities == null ? 0 : mCities.size();
    }

    @Override
    public City getItem(int position) {
        return mCities == null ? null : mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        HotCityViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_local_hot_city_gridview, parent, false);
            holder = new HotCityViewHolder();
            holder.name = (TextView) view.findViewById(R.id.tv_hot_city_name);
            view.setTag(holder);
        } else {
            holder = (HotCityViewHolder) view.getTag();
        }
        holder.name.setText(mCities.get(position).getCate_name() + "\n" + mCities.get(position).getCatename_en());
        return view;
    }

    public static class HotCityViewHolder {
        TextView name;
    }
}
