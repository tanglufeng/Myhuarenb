package com.abcs.haiwaigou.local.fragment.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.abcs.haiwaigou.local.beans.City;
import com.abcs.haiwaigou.local.fragment.viewholder.CountryViewHolder;
import com.abcs.huaqiaobang.adapter.CommonAdapter;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.HqbViewHolder;
import com.abcs.sociax.android.R;
import com.nineoldandroids.animation.ObjectAnimator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zjz on 2016/9/6.
 */
public class CountryAdapter extends RecyclerView.Adapter<CountryViewHolder>{
    ArrayList<City> dataList;
    Activity activity;
    private int durationRotate = 200;// 旋转动画时间
    private int durationAlpha = 200;// 透明度动画时间

    private HashMap<String, Integer> letterIndexes;
    private String[] sections;

    public CountryAdapter(Activity activity,ArrayList<City>dataList) {
        this.activity = activity;
        this.dataList=dataList;
        if (dataList == null){
            dataList = new ArrayList<>();
        }
        int size = dataList.size();
        letterIndexes = new HashMap<>();
        sections = new String[size];
        for (int index = 0; index < size; index++){
            //当前城市拼音首字母
            String currentLetter = Util.getFirstLetter(dataList.get(index).getCountryInitial());
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? Util.getFirstLetter(dataList.get(index - 1).getCountryInitial()) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)){
                letterIndexes.put(currentLetter, index);
                sections[index] = currentLetter;
            }
        }
    }
    /**
     * 获取字母索引的位置
     * @param letter
     * @return
     */
    public int getLetterPosition(String letter){
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }


    public ArrayList<City> getDataList(){
        return dataList;
    }

    public void addDatas(City city){
        dataList.add(city);
    }
    private Handler handler=new Handler();
    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_item_country_select, parent, false);
        CountryViewHolder cityPickerViewHolder = new CountryViewHolder(view);
        return cityPickerViewHolder;
    }

    @Override
    public void onBindViewHolder(final CountryViewHolder holder, int position) {
        final City foreignCity=dataList.get(position);
        final boolean[] isOpen = {false};
        final boolean[] hasLoad = {false};
        holder.t_country_name.setText(foreignCity.getCountryCnName());
        holder.t_num.setText("(" + foreignCity.getCityCount() + ")");
        String currentLetter = Util.getFirstLetter(foreignCity.getCountryInitial());
        String previousLetter = position >= 1 ? Util.getFirstLetter(dataList.get(position - 1).getCountryInitial()) : "";
        if (!TextUtils.equals(currentLetter, previousLetter)){
            holder.t_pingyin.setVisibility(View.VISIBLE);
            holder.t_pingyin.setText(currentLetter);
        }else{
            holder.t_pingyin.setVisibility(View.GONE);
        }
        holder.linear_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen[0] = !isOpen[0];
                if (isOpen[0]) {
                    ObjectAnimator.ofFloat(holder.img_arrow, "rotation", 0, 90).setDuration(durationRotate).start();
                    if (!hasLoad[0]) {
                        initCity(foreignCity.getCountryId(), holder);
                    } else {
                        holder.gridview_hot_city.setVisibility(View.VISIBLE);
                        ObjectAnimator.ofFloat(holder.gridview_hot_city, "alpha", 0, 1).setDuration(durationAlpha).start();
                    }
                } else {
                    ObjectAnimator.ofFloat(holder.img_arrow, "rotation", 90, 0).setDuration(durationRotate).start();
                    holder.gridview_hot_city.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.gridview_hot_city.setVisibility(View.GONE);
                        }
                    }, durationAlpha);
//                    holder.gridview_hot_city.setVisibility(View.GONE);
                }

            }

            private void initCity(String countryId, final CountryViewHolder holder) {
                final ArrayList<City> cities = new ArrayList<City>();
                cities.clear();
                HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_city, "countryId=" + countryId, new HttpRevMsg() {
                    @Override
                    public void revMsg(final String msg) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(msg);
                                    JSONArray cityArray = jsonObject.optJSONArray("msg");
                                    Log.i("zjz","city_msg="+msg);
                                    if (cityArray != null) {
                                        hasLoad[0]=true;
                                        holder.gridview_hot_city.setVisibility(View.VISIBLE);
                                        ObjectAnimator.ofFloat(holder.gridview_hot_city, "alpha", 0, 1).setDuration(durationAlpha).start();
                                        for (int i = 0; i < cityArray.length(); i++) {
                                            JSONObject cityObj = cityArray.getJSONObject(i);
                                            City city = new City();
                                            city.setCity_id(cityObj.optString("city_id"));
                                            city.setCate_name(cityObj.optString("cate_name"));
                                            city.setCatename_en(cityObj.optString("catename_en"));
                                            cities.add(city);
                                        }
                                        initGridView(cities, holder);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                });
            }

        });

    }



    private void initGridView(final ArrayList<City> cities, final CountryViewHolder holder) {
        holder.gridview_hot_city.setAdapter(new CommonAdapter<City>(activity, cities, R.layout.local_item_city_picker_grid) {
            @Override
            public void convert(HqbViewHolder helper, City item, int position) {
                helper.setText(R.id.t_city_name, item.getCate_name());
            }

        });
        holder.gridview_hot_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("countryName", holder.t_country_name.getText().toString());
//                intent.putExtra("countryId",foreignCity.getCountryEnName());
                intent.putExtra("cityName", cities.get(position).getCate_name());
                intent.putExtra("cityId",cities.get(position).getCity_id());
                activity.setResult(1, intent);
                activity.finish();
//                Toast.makeText(activity, holder.t_country_name.getText().toString()+cities.get(position).getCate_name(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
