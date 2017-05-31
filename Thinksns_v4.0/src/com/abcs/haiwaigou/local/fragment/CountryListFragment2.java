package com.abcs.haiwaigou.local.fragment;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.abcs.haiwaigou.local.adapter.CityGridViewAdapter2;
import com.abcs.haiwaigou.local.beans.City2;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.hqbtravel.wedgt.MyGridView;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by zds 各大洲
 */
public class CountryListFragment2 extends BaseFragment  {

    private View view;
    private Activity activity;
    ListView local_lv;
    ACache aCache;
    private String msg;
    private int position=0;

    public static CountryListFragment2 newInstance(String msg,int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("msg", msg);
        bundle.putSerializable("position", position);
        CountryListFragment2 countryFragment = new CountryListFragment2();
        countryFragment.setArguments(bundle);
        return countryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.local_activity_country_city4_new, null);
        }

        local_lv=(ListView) view.findViewById(R.id.local_lv);
        aCache = ACache.get(activity);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            msg = bundle.getString("msg");
            position = bundle.getInt("position");
            Log.i("zjz", "msg=" + msg);
            Log.i("zjz", "position=" + position);
        }
        lazyLoad2(msg,position);

        return view;
    }

    protected void lazyLoad2(String msg,int position) {

        City2 city = new Gson().fromJson(msg, City2.class);
        if (city.result == 1) {
            if (city.body.data != null && city.body.data.size() > 0) {
                if (city.body.data.get(position).countrys != null && city.body.data.get(position).countrys.size()>0){
                    local_lv.setAdapter(new CountryListAdapter(activity,city.body.data.get(position).countrys));
                }
            }
        }
    }

    @Override
    protected void lazyLoad() {
    }


    public class CountryListAdapter extends BaseAdapter {

        private List<City2.BodyBean.DataBean.CountrysBean> countrys;
        private Activity mcontext;

        public CountryListAdapter(Activity mcontext, List<City2.BodyBean.DataBean.CountrysBean> countrys) {
            this.mcontext = mcontext;
            this.countrys = countrys;
        }

        @Override
        public int getCount() {
            return countrys.size();
        }

        @Override
        public City2.BodyBean.DataBean.CountrysBean getItem(int position) {
            return countrys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null){
                convertView = View.inflate(mcontext, R.layout.local_activity_country2,null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (!TextUtils.isEmpty(countrys.get(position).country_name)){
                holder.countryName.setText(countrys.get(position).country_name);
            }
            if (!TextUtils.isEmpty(countrys.get(position).national_flag)){
                MyApplication.imageLoader.displayImage(countrys.get(position).national_flag,holder.imageView,MyApplication.getListOptions());
                holder.imageView.setVisibility(View.VISIBLE);
            }else {
                holder.imageView.setVisibility(View.GONE);
            }

            if (countrys.get(position).citys!=null&&countrys.get(position).citys.size()>0){
                holder.cityGridView.setAdapter(new CityGridViewAdapter2(mcontext,countrys.get(position).citys));
            }

            holder.cityGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    City2.BodyBean.DataBean.CountrysBean.CitysBean citysBean = (City2.BodyBean.DataBean.CountrysBean.CitysBean) adapterView.getItemAtPosition(i);

                    Intent intent = new Intent();
                    intent.putExtra("cityName", citysBean.cate_name + "");
                    intent.putExtra("cityId", citysBean.city_id + "");
                    mcontext.setResult(1, intent);
                    mcontext.finish();

                    Util.preference.edit().putString(MyString.LOCAL_CITY_NAME, citysBean.cate_name).commit();
                    Util.preference.edit().putString(MyString.LOCAL_CITY_ID, citysBean.city_id+"").commit();
                    Util.preference.edit().putString(MyString.LOCAL_COUNTRY_NAME, countrys.get(position).country_name).commit();

                }
            });

            return convertView;
        }

        public class ViewHolder{
            private ImageView imageView;
            private TextView countryName;
            private MyGridView cityGridView;
            public ViewHolder(View view){
                imageView = (ImageView) view.findViewById(R.id.imageView);
                countryName = (TextView) view.findViewById(R.id.item_country_name);
                cityGridView= (MyGridView) view.findViewById(R.id.item_mgv_city);
            }
        }
    }
}
