package com.abcs.haiwaigou.local.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.abcs.haiwaigou.local.beans.ForeignCity;
import com.abcs.haiwaigou.local.viewholder.CityPickerViewHolder;
import com.abcs.huaqiaobang.adapter.CommonAdapter;
import com.abcs.huaqiaobang.view.HqbViewHolder;
import com.abcs.sociax.android.R;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/8/30.
 */
public class CityPickerAdapter extends RecyclerView.Adapter<CityPickerViewHolder> {
    ArrayList<ForeignCity>dataList;
    Activity activity;

    public CityPickerAdapter(Activity activity) {
        this.activity = activity;
        this.dataList=new ArrayList<ForeignCity>();
    }

    public ArrayList<ForeignCity> getDataList(){
        return dataList;
    }

    public void addDatas(ForeignCity foreignCity){
        dataList.add(foreignCity);
    }

    @Override
    public CityPickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_item_city_picker, parent, false);
        CityPickerViewHolder cityPickerViewHolder = new CityPickerViewHolder(view);
        return cityPickerViewHolder;
    }

    @Override
    public void onBindViewHolder(CityPickerViewHolder holder, int position) {
        final ForeignCity foreignCity=dataList.get(position);
        holder.t_country_name.setText(foreignCity.getKeyTitle());
        holder.gridview_hot_city.setAdapter(new CommonAdapter<ForeignCity.HrqCitiesBean>(activity, foreignCity.getHrqCities(), R.layout.local_item_city_picker_grid) {
            @Override
            public void convert(HqbViewHolder helper, ForeignCity.HrqCitiesBean item, int position) {
                helper.setText(R.id.t_city_name, item.getCityName());
            }

        });
        holder.gridview_hot_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("countryName", foreignCity.getKeyTitle());
                intent.putExtra("countryId",foreignCity.getCountryEnName());
                intent.putExtra("cityName", foreignCity.getHrqCities().get(position).getCityName());
                intent.putExtra("cityId",foreignCity.getHrqCities().get(position).getCityId());
                activity.setResult(1, intent);
                activity.finish();
//                Toast.makeText(activity,foreignCity.getHrqCities().get(position).getCityName(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
