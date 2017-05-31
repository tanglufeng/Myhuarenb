package com.abcs.haiwaigou.local.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.City2;
import com.abcs.hqbtravel.wedgt.MyGridView;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;

import java.util.List;

/**
 * Created by xuke on 2016/12/13.
 * 里层listView的adapter  item为国家和城市
 */
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = View.inflate(mcontext, R.layout.local_activity_country,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (!TextUtils.isEmpty(countrys.get(position).country_name)){
            holder.countryName.setText(countrys.get(position).country_name);
            MyApplication.clearLocaCountryName();
            MyApplication.saveLocaCountryName(countrys.get(position).country_name);
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
            }
        });

        return convertView;
    }

    public class ViewHolder{
        private TextView countryName;
        private MyGridView cityGridView;
        public ViewHolder(View view){
            countryName = (TextView) view.findViewById(R.id.item_country_name);
            cityGridView= (MyGridView) view.findViewById(R.id.item_mgv_city);
        }
    }
}


















