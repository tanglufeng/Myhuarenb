package com.abcs.haiwaigou.local.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.City1;
import com.abcs.haiwaigou.local.beans.City2;
import com.abcs.hqbtravel.wedgt.MyGridView;
import com.abcs.hqbtravel.wedgt.MyListView;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.sociax.android.R;

import java.util.List;


public class CityListAdapter3 extends BaseAdapter{
    private Activity mcontext;
    private LayoutInflater inflater;
    private List<City2.BodyBean.DataBean> cityList;

    public CityListAdapter3(Activity mcontext, List<City2.BodyBean.DataBean> cityList) {
        this.mcontext = mcontext;
        this.cityList = cityList;
        inflater = LayoutInflater.from(mcontext);
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public City2.BodyBean.DataBean getItem(int position) {
        return cityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        ViewHolder holder=null;
//        if(convertView==null){
//            convertView=inflater.inflate(R.layout.local_activity_country_city4_item,parent,false);
//            holder=new ViewHolder(convertView);
//            convertView.setTag(holder);
//        }else{
//            holder=(ViewHolder) convertView.getTag();
//        }
//
//        if(!TextUtils.isEmpty(dataBean.area_cn_name)){
//            holder.areaName.setText(dataBean.area_cn_name);  //欧洲
//            MyApplication.clearLocaCountryName();
//            MyApplication.saveLocaCountryName(dataBean.area_cn_name);
//        }
//
//        if(dataBean.countrys !=null&& dataBean.countrys.size()>0){
//            Log.i("国家大小:",dataBean.countrys.size()+"");
//            LogUtil.i("xk","countrys.get(position).citys"+dataBean.countrys.get(position).country_name);
//            holder.countryName.setText(dataBean.countrys.get(position).country_name);  //奥地利
//
//            mGridViewAdapter=new CityGridViewAdapter2(mcontext,dataBean.countrys.get(position).citys); //维也纳集合
//            holder.cityGridView.setAdapter(mGridViewAdapter);
//
//        }
//
//        holder.cityGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                City2.BodyBean.DataBean.CountrysBean.CitysBean citysBean = (City2.BodyBean.DataBean.CountrysBean.CitysBean) adapterView.getItemAtPosition(i);
//                Intent intent = new Intent();
//                intent.putExtra("cityName", citysBean.cate_name+"");
//                intent.putExtra("cityId", citysBean.city_id+"");
//                mcontext.setResult(1, intent);
//                mcontext.finish();
//            }
//        });
        ViewHolder holder= null;
        if (convertView == null){
            convertView = View.inflate(mcontext,R.layout.local_activity_area,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if (!TextUtils.isEmpty(cityList.get(position).area_cn_name)){
            holder.areaName.setText(cityList.get(position).area_cn_name);
//            MyApplication.clearLocaCountryName();
//            MyApplication.saveLocaCountryName(cityList.get(position).area_cn_name);
        }

        if (cityList.get(position).countrys != null && cityList.get(position).countrys.size()>0){
            holder.cityListView.setAdapter(new CountryListAdapter(mcontext,cityList.get(position).countrys));
        }

        return convertView;
    }

    public class  ViewHolder{
        private TextView areaName;  //欧洲
        private MyListView cityListView;  //国家和城市
        public ViewHolder(View view) {
            areaName=(TextView) view.findViewById(R.id.item_tv_area);
            cityListView= (MyListView) view.findViewById(R.id.item_lv_area);
        }

    }
}
