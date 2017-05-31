package com.abcs.haiwaigou.local.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.City1;
import com.abcs.hqbtravel.wedgt.MyGridView;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.sociax.android.R;

import java.util.List;


public class CityListAdapter2 extends BaseAdapter{
    private Activity mcontext;
    private LayoutInflater inflater;
    private List<City1.BodyBean.DataBean> cityList;

    public CityListAdapter2(Activity mcontext, List<City1.BodyBean.DataBean> cityList) {
        this.mcontext = mcontext;
        inflater = LayoutInflater.from(mcontext);
        this.cityList = cityList;
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public City1.BodyBean.DataBean getItem(int position) {
        return cityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    CityGridViewAdapter mGridViewAdapter;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.local_activity_country_city3_item,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        if(!TextUtils.isEmpty(cityList.get(position).areaCnName)){
            holder.tv_acer.setText(cityList.get(position).areaCnName);

            MyApplication.clearLocaCountryName();
            MyApplication.saveLocaCountryName(cityList.get(position).areaCnName);
        }

        if(cityList.get(position).citys!=null&&cityList.get(position).citys.size()>0){
            LogUtil.i("lzq","dataBean.citys"+cityList.get(position).citys.toString());

            mGridViewAdapter=new CityGridViewAdapter(mcontext,cityList.get(position).citys);
            holder.cityGridView.setAdapter(mGridViewAdapter);

        }

        holder.cityGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                City1.BodyBean.DataBean.CitysBean citysBean=(City1.BodyBean.DataBean.CitysBean)adapterView.getItemAtPosition(i);

                Intent intent = new Intent();
//                intent.putExtra("countryName", holder.t_country_name.getText().toString());
//                intent.putExtra("countryId",foreignCity.getCountryEnName());
                intent.putExtra("cityName", citysBean.cateName+"");
//                intent.putExtra("cityName", cities.get(position).getCate_name());
                intent.putExtra("cityId", citysBean.cityId+"");
                mcontext.setResult(1, intent);
                mcontext.finish();

            }
        });

        return convertView;
    }

    public class  ViewHolder{

        private TextView tv_acer;
        private MyGridView cityGridView;
        public ViewHolder(View view) {
            tv_acer=(TextView) view.findViewById(R.id.area_tv);
            cityGridView= (MyGridView) view.findViewById(R.id.local_gv);
        }

    }
}
