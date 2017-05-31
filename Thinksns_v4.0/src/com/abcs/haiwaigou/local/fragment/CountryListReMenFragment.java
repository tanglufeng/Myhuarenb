package com.abcs.haiwaigou.local.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.City2;
import com.abcs.haiwaigou.local.beans.HotCityEntry;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.img.RoundImageView;
import com.google.gson.Gson;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * zds 热门城市
 */
public class CountryListReMenFragment extends BaseFragment{


    @InjectView(R.id.item_country_name)
    TextView item_country_name;
    @InjectView(R.id.item_mgv_city)
    GridView cityGridView;
    @InjectView(R.id.lin_gnei)
    LinearLayout lin_gnei;

    private View view;
    private Activity activity;
    ACache aCache;
    private String msg;

    public static CountryListReMenFragment newInstance(String msg) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("msg", msg);
        CountryListReMenFragment countryFragment = new CountryListReMenFragment();
        countryFragment.setArguments(bundle);
        return countryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.local_activity_country, null);
        }
        aCache = ACache.get(activity);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);
        ButterKnife.inject(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            msg = bundle.getString("msg");
            Log.i("zds", "msg=" + msg);
        }

        item_country_name.setText("热门城市");

        lazyLoad2(msg);
        lin_gnei.setVisibility(View.VISIBLE);
        lin_gnei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("isChina", true);
                activity.setResult(1, intent);
                activity.finish();
            }
        });
        return view;
    }


    @Override
    protected void lazyLoad() {
    }

    protected void lazyLoad2(String msg) {
        City2 city = new Gson().fromJson(msg, City2.class);
        if (city.result == 1&&city.body!=null) {
            if (city.body.hotCity != null && city.body.hotCity.size() > 0) {
                cityGridView.setAdapter(new CityGridViewAdapter(activity,city.body.hotCity));
            }
        }

        cityGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HotCityEntry citysBean = (HotCityEntry) adapterView.getItemAtPosition(i);

                Intent intent = new Intent();
                intent.putExtra("cityName", citysBean.cateName);
                intent.putExtra("cityId", citysBean.cityId + "");
                intent.putExtra("isChina", false);
                activity.setResult(1, intent);
                activity.finish();

                Util.preference.edit().putString(MyString.LOCAL_CITY_NAME, citysBean.cateName).commit();
                Util.preference.edit().putString(MyString.LOCAL_CITY_ID, citysBean.cityId+"").commit();
                Util.preference.edit().putString(MyString.LOCAL_COUNTRY_NAME, citysBean.country_name).commit();

                Log.i("zds", "onItemClick: "+citysBean.cateName);
            }
        });
    }
    public class CityGridViewAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private Context mContext;
        private List<HotCityEntry> mCities;
        public CityGridViewAdapter(Context context, List<HotCityEntry> mCities) {
            this.mContext = context;
            this.mCities = mCities;
            this.inflater = LayoutInflater.from(mContext);
        }
        @Override
        public int getCount() {
            return mCities.size();
        }

        @Override
        public HotCityEntry getItem(int position) {
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
                convertView = inflater.inflate(R.layout.local_activity_gv_city4_item_r,parent,false);
                cityGridViewHolder = new CityGridViewHolder();
                cityGridViewHolder.circ_iv = (RoundImageView) convertView.findViewById(R.id.civ);
                cityGridViewHolder.city_tv = (TextView) convertView.findViewById(R.id.tv_city4);
                cityGridViewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                cityGridViewHolder.city_english = (TextView) convertView.findViewById(R.id.tv_english_city4);
                convertView.setTag(cityGridViewHolder);
            }else
            {
                cityGridViewHolder  = (CityGridViewHolder) convertView.getTag();
            }


            if(!TextUtils.isEmpty(mCities.get(position).cateName)){
                cityGridViewHolder.city_tv.setText(mCities.get(position).cateName);
            }
            if(!TextUtils.isEmpty(mCities.get(position).ossUrl)){
                MyApplication.imageLoader.displayImage(mCities.get(position).ossUrl,cityGridViewHolder.circ_iv, Options.getHDOptions());
            }
            if(!TextUtils.isEmpty(mCities.get(position).nationalFlag)){
                MyApplication.imageLoader.displayImage(mCities.get(position).nationalFlag,cityGridViewHolder.imageView, Options.getHDOptions());
            }

            return convertView;
        }

        public  class CityGridViewHolder{
            RoundImageView circ_iv;
            TextView city_tv;
            TextView city_english;
            ImageView imageView;
        }
    }

}
