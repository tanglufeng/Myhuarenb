package com.abcs.haiwaigou.local.fragment.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.local.adapter.HotCityGridAdapter;
import com.abcs.haiwaigou.local.beans.City;
import com.abcs.haiwaigou.local.beans.LocateState;
import com.abcs.haiwaigou.local.db.CityDAO;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.view.MyGridView;
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
import java.util.List;

/**
 * author zjz on 2016/1/26.
 */
public class CityAdapter extends BaseAdapter {

    private static final int VIEW_TYPE_COUNT = 1;
    private int durationRotate = 200;// 旋转动画时间
    private int durationAlpha = 200;// 透明度动画时间
    private Activity activity;
    private LayoutInflater inflater;
    private List<City> mCities;
    private HashMap<String, Integer> letterIndexes;
    private String[] sections;
    private int locateState = LocateState.LOCATING;
    private String locatedCity;

    private int viewType;
    private View mLastView;
    private int mLastPosition;
    private int mLastVisibility;

    private Handler handler = new Handler();
    private String countryId;
    private boolean reset;
    CityDAO cityDAO;
    public CityAdapter(Activity activity, List<City> mCities,String countryId,boolean reset,int viewType) {
        this.countryId=countryId;
        this.reset=reset;
        this.viewType=viewType;
        this.activity = activity;
        this.mCities = mCities;
        this.inflater = LayoutInflater.from(activity);
        cityDAO=new CityDAO(activity);
        mLastPosition = -1;
        if (mCities == null) {
            mCities = new ArrayList<>();
        }

        mCities.add(0,new City("定位","#","0"));
        if(viewType>2)
            mCities.add(1,new City("常用","#","0"));
        int size = mCities.size();
        letterIndexes = new HashMap<>();
        sections = new String[size];
        for (int index = 0; index < size; index++) {
            //当前城市拼音首字母
            String currentLetter = Util.getFirstLetter(mCities.get(index).getCountryInitial());
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? Util.getFirstLetter(mCities.get(index - 1).getCountryInitial()) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                letterIndexes.put(currentLetter, index);
                sections[index] = currentLetter;
            }
        }
    }

    /**
     * 更新定位状态
     *
     * @param state
     */
    public void updateLocateState(int state, String city) {
        this.locateState = state;
        this.locatedCity = city;
        notifyDataSetChanged();
    }

    /**
     * 获取字母索引的位置
     *
     * @param letter
     * @return
     */
    public int getLetterPosition(String letter) {
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }

    @Override
    public int getViewTypeCount() {
        return viewType;
    }

    @Override
    public int getItemViewType(int position) {
//        if(viewType>2){
            return position < viewType - 1 ? position : viewType - 1;
//        }else {
//            return position < viewType - 1 ? position : viewType;
//        }
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
    public View getView(final int position, View view, ViewGroup parent) {
        final CityViewHolder holder;
        int type = getItemViewType(position);
        switch (type){
            case 0:     //定位
                view = inflater.inflate(R.layout.view_local_locate_city, parent, false);
                ViewGroup container = (ViewGroup) view.findViewById(R.id.layout_locate);
                TextView state = (TextView) view.findViewById(R.id.tv_located_city);
                switch (locateState){
                    case LocateState.LOCATING:
                        state.setText("正在定位");
                        break;
                    case LocateState.FAILED:
                        state.setText("定位失败");
                        break;
                    case LocateState.SUCCESS:
                        state.setText(locatedCity);
                        break;
                }
                container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (locateState == LocateState.FAILED){
                            //重新定位
                            if (onCityClickListener != null){
                                onCityClickListener.locationClick();
                            }
                        }else if (locateState == LocateState.SUCCESS){
                            //返回定位城市
                            if (onCityClickListener != null){
//                                onCityClickListener.locationClick(locatedCity);
                                Toast.makeText(activity,locatedCity,Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                break;
            default:
//                break;
//            case (viewType-1):     //热门
                if(viewType>2&&type==1){
                    view = inflater.inflate(R.layout.view_local_hot_city, parent, false);
                    MyGridView gridView = (MyGridView) view.findViewById(R.id.gridview_hot_city);
                    final HotCityGridAdapter hotCityGridAdapter = new HotCityGridAdapter(activity);
                    gridView.setAdapter(hotCityGridAdapter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent();
                            intent.putExtra("countryName", hotCityGridAdapter.getItem(position).getCountryCnName());
                            intent.putExtra("countryId",hotCityGridAdapter.getItem(position).getCountryId());
                            intent.putExtra("cityName", hotCityGridAdapter.getItem(position).getCate_name());
                            intent.putExtra("cityId", hotCityGridAdapter.getItem(position).getCity_id());
                            intent.putExtra("areaId",hotCityGridAdapter.getItem(position).getAreaId());
                            saveCity(hotCityGridAdapter.getItem(position).getCountryCnName(),hotCityGridAdapter.getItem(position));
                            activity.setResult(1, intent);
                            activity.finish();
                        }
                    });
                }else {
                    final boolean[] hasLoad = {false};
                    final City foreignCity = mCities.get(position);
                    if (view == null) {
                        view = inflater.inflate(R.layout.local_item_country_select, parent, false);
                        holder = new CityViewHolder();
                        holder.linear_country = (LinearLayout) view.findViewById(R.id.linear_country);
                        holder.t_country_name = (TextView) view.findViewById(R.id.t_country_name);
                        holder.t_pingyin = (TextView) view.findViewById(R.id.t_pingyin);
                        holder.t_num = (TextView) view.findViewById(R.id.t_num);
                        holder.gridview_hot_city = (MyGridView) view.findViewById(R.id.gridview_hot_city);
                        holder.img_arrow = (ImageView) view.findViewById(R.id.img_arrow);
                        view.setTag(holder);
                    } else {
                        holder = (CityViewHolder) view.getTag();
                    }
                    if (mLastPosition == position) {
                        holder.gridview_hot_city.setVisibility(mLastVisibility);
                    } else {
                        holder.gridview_hot_city.setVisibility(View.GONE);
                        ObjectAnimator.ofFloat(holder.img_arrow, "rotation", 0, 0).setDuration(durationRotate).start();
                    }
                    holder.t_country_name.setText(foreignCity.getCountryCnName());
                    holder.t_num.setText("(" + foreignCity.getCityCount() + ")");
                    String currentLetter = Util.getFirstLetter(foreignCity.getCountryInitial());
                    String previousLetter = position >= 1 ? Util.getFirstLetter(mCities.get(position - 1).getCountryInitial()) : "";
                    if (!TextUtils.equals(currentLetter, previousLetter)) {
                        holder.t_pingyin.setVisibility(View.VISIBLE);
                        holder.t_pingyin.setText(currentLetter);
                    } else {
                        holder.t_pingyin.setVisibility(View.GONE);
                    }
                    if(reset){
                        if(countryId.equals(mCities.get(position).getCountryId())){
                            reset=false;
                            mLastPosition = position;
                            mLastVisibility = View.VISIBLE;
                            final ArrayList<City> cities = new ArrayList<City>();
                            cities.clear();
                            ObjectAnimator.ofFloat(holder.img_arrow, "rotation", 0, 90).setDuration(durationRotate).start();
                            HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_city, "countryId=" + countryId, new HttpRevMsg() {
                                @Override
                                public void revMsg(final String msg) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                JSONObject jsonObject = new JSONObject(msg);
                                                JSONArray cityArray = jsonObject.optJSONArray("msg");
                                                Log.i("zjz", "city_msg=" + msg);
                                                if (cityArray != null) {
                                                    hasLoad[0] = true;
                                                    holder.gridview_hot_city.setVisibility(View.VISIBLE);
                                                    ObjectAnimator.ofFloat(holder.gridview_hot_city, "alpha", 0, 1).setDuration(durationAlpha).start();
                                                    for (int i = 0; i < cityArray.length(); i++) {
                                                        JSONObject cityObj = cityArray.getJSONObject(i);
                                                        City city = new City();
                                                        city.setAreaId(mCities.get(position).getAreaId());
                                                        city.setCountryId(countryId);
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
                    }
                    holder.linear_country.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mLastPosition = position;
                            switch (holder.gridview_hot_city.getVisibility()) {
                                case View.GONE:
                                    ObjectAnimator.ofFloat(holder.img_arrow, "rotation", 0, 90).setDuration(durationRotate).start();
                                    if (!hasLoad[0]) {
                                        initCity(foreignCity.getCountryId(), holder);
                                    } else {
                                        holder.gridview_hot_city.setVisibility(View.VISIBLE);
                                        ObjectAnimator.ofFloat(holder.gridview_hot_city, "alpha", 0, 1).setDuration(durationAlpha).start();
                                    }
                                    mLastVisibility = View.VISIBLE;
                                    break;
                                case View.VISIBLE:
                                    ObjectAnimator.ofFloat(holder.img_arrow, "rotation", 90, 0).setDuration(durationRotate).start();
                                    holder.gridview_hot_city.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            holder.gridview_hot_city.setVisibility(View.GONE);
                                        }
                                    }, durationAlpha);
                                    mLastVisibility = View.GONE;
                                    break;
                            }
                            notifyDataSetChanged();
                        }

                        private void initCity(final String countryId, final CityViewHolder holder) {
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
                                                Log.i("zjz", "city_msg=" + msg);
                                                if (cityArray != null) {
                                                    hasLoad[0] = true;
                                                    holder.gridview_hot_city.setVisibility(View.VISIBLE);
                                                    ObjectAnimator.ofFloat(holder.gridview_hot_city, "alpha", 0, 1).setDuration(durationAlpha).start();
                                                    for (int i = 0; i < cityArray.length(); i++) {
                                                        JSONObject cityObj = cityArray.getJSONObject(i);
                                                        City city = new City();
                                                        city.setAreaId(mCities.get(position).getAreaId());
                                                        city.setCountryId(countryId);
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
                break;
        }

        return view;
    }


//    public void changeImageVisible(View view, int position) {
//        if (mLastView != null && mLastPosition != position) {
//            CityViewHolder holder = (CityViewHolder) mLastView.getTag();
//            switch (holder.gridview_hot_city.getVisibility()) {
//                case View.VISIBLE:
//                    holder.gridview_hot_city.setVisibility(View.GONE);
//                    mLastVisibility = View.GONE;
//                    break;
//                default:
//                    break;
//            }
//        }
//        mLastPosition = position;
//        mLastView = view;
//        final CityViewHolder holder = (CityViewHolder) view.getTag();
//        switch (holder.gridview_hot_city.getVisibility()) {
//            case View.GONE:
//                ObjectAnimator.ofFloat(holder.img_arrow, "rotation", 0, 90).setDuration(durationRotate).start();
//                initCity(mCities.get(position).getCountryId(), holder);
////                holder.gridview_hot_city.setVisibility(View.VISIBLE);
//                mLastVisibility = View.VISIBLE;
//                break;
//            case View.VISIBLE:
//                ObjectAnimator.ofFloat(holder.img_arrow, "rotation", 90, 0).setDuration(durationRotate).start();
//                holder.gridview_hot_city.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        holder.gridview_hot_city.setVisibility(View.GONE);
//                    }
//                }, durationAlpha);
////                holder.gridview_hot_city.setVisibility(View.GONE);
//                mLastVisibility = View.GONE;
//                break;
//        }
//    }
//
//    private void initCity(String countryId, final CityViewHolder holder) {
//        final ArrayList<City> cities = new ArrayList<City>();
//        cities.clear();
//        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_city, "countryId=" + countryId, new HttpRevMsg() {
//            @Override
//            public void revMsg(final String msg) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            JSONObject jsonObject = new JSONObject(msg);
//                            JSONArray cityArray = jsonObject.optJSONArray("msg");
//                            Log.i("zjz", "city_msg=" + msg);
//                            if (cityArray != null) {
////                                hasLoad[0] = true;
//                                holder.gridview_hot_city.setVisibility(View.VISIBLE);
//                                ObjectAnimator.ofFloat(holder.gridview_hot_city, "alpha", 0, 1).setDuration(durationAlpha).start();
//                                for (int i = 0; i < cityArray.length(); i++) {
//                                    JSONObject cityObj = cityArray.getJSONObject(i);
//                                    City city = new City();
//                                    city.setCity_id(cityObj.optString("city_id"));
//                                    city.setCate_name(cityObj.optString("cate_name"));
//                                    city.setCatename_en(cityObj.optString("catename_en"));
//                                    cities.add(city);
//                                }
//                                initGridView(cities, holder);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//            }
//        });
//    }

    private void initGridView(final ArrayList<City> cities, final CityViewHolder holder) {
        holder.gridview_hot_city.setAdapter(new CommonAdapter<City>(activity, cities, R.layout.local_item_city_picker_grid) {
            @Override
            public void convert(HqbViewHolder helper, City item, int position) {
                helper.setText(R.id.t_city_name, Util.StringFilter(item.getCate_name())+"\n"+item.getCatename_en());
            }

        });
        holder.gridview_hot_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("countryName", holder.t_country_name.getText().toString());
                intent.putExtra("countryId",cities.get(position).getCountryId());
                intent.putExtra("cityName", cities.get(position).getCate_name());
                intent.putExtra("cityId", cities.get(position).getCity_id());
                intent.putExtra("areaId",cities.get(position).getAreaId());
                saveCity( holder.t_country_name.getText().toString(),cities.get(position));
//                saveSQL(cities.get(position));
                activity.setResult(1, intent);
                activity.finish();
//                Toast.makeText(activity, holder.t_country_name.getText().toString()+cities.get(position).getCate_name(), Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void saveSQL(City city) {
        if(cityDAO.selectByCityId(city.getCity_id())){
            cityDAO.updateOne(city);
        }else {
            cityDAO.insert(city);
        }
        Log.i("zjz","recent_citys="+cityDAO.selectByAll().size());

    }

    private void saveCity(String countryName,City city) {
        String text= (countryName + "," + city.getCountryId() + "," +city.getCate_name() + "," +city.getCatename_en()+","+ city.getCity_id() + "," + city.getAreaId());
        SharedPreferences sp = activity.getSharedPreferences(MyString.CITY_CACHE, 0);
        String save_Str = sp.getString(MyString.CITYS, "");
        Log.i("zjz","local_city="+save_Str);
        Log.i("zjz","save_city="+city.getCate_name());
        String temp="";
        String[] citys = save_Str.split("##");
        for (String city1 : citys) {
            if (city1.contains(city.getCate_name())) {
                temp=save_Str.replaceAll(city1 + "##", "");
            }
        }
        StringBuilder sb;
        if(!TextUtils.isEmpty(temp)){
            sb = new StringBuilder(temp);
        }else {
            sb = new StringBuilder(save_Str);
        }
        sb.append(text + "##");
        sp.edit().putString(MyString.CITYS, sb.toString()).commit();
    }


    public static class CityViewHolder {
        public LinearLayout linear_country;
        public TextView t_country_name;
        public TextView t_pingyin;
        public TextView t_num;
        public MyGridView gridview_hot_city;
        public ImageView img_arrow;
    }

    public void setOnCityClickListener(OnCityClickListener listener){
        this.onCityClickListener = listener;
    }
    private OnCityClickListener onCityClickListener;
    public interface OnCityClickListener{
        void locationClick();
    }

}
