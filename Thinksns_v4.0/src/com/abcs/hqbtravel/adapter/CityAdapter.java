package com.abcs.hqbtravel.adapter;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.hqbtravel.biz.GetCityBiz;
import com.abcs.hqbtravel.entity.City;
import com.abcs.hqbtravel.entity.CityEntry;
import com.abcs.hqbtravel.holder.HqbViewHolder;
import com.abcs.hqbtravel.wedgt.LocateState;
import com.abcs.hqbtravel.wedgt.MyGridView;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.sociax.android.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

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

    private View mLastView;
    private int mLastPosition;
    private int mLastVisibility;

    private Handler handler = new Handler();

    public CityAdapter(Activity activity, List<City> mCities) {
        this.activity = activity;
        this.mCities = mCities;
        this.inflater = LayoutInflater.from(activity);
        mLastPosition = -1;
        if (mCities == null) {
            mCities = new ArrayList<>();
        }
        int size = mCities.size();
        letterIndexes = new HashMap<>();
        sections = new String[size];
        for (int index = 0; index < size; index++) {
            //当前城市拼音首字母
            String currentLetter = getFirstLetter(mCities.get(index).getCountryInitial());
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? getFirstLetter(mCities.get(index - 1).getCountryInitial()) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                letterIndexes.put(currentLetter, index);
                sections[index] = currentLetter;
            }
        }
    }

    public static String getFirstLetter(final String pinyin) {
        if (TextUtils.isEmpty(pinyin)) return "定位";
        String c = pinyin.substring(0, 1);
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c).matches()) {
            return c.toUpperCase();
        } else if ("0".equals(c)) {
            return "定位";
        } else if ("1".equals(c)) {
            return "热门";
        }
        return "定位";
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
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
        final boolean[] hasLoad = {false};
        final City foreignCity = mCities.get(position);
        if (view == null) {
            view = inflater.inflate(R.layout.local_item_country_select2, parent, false);
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
//        holder.t_num.setText("(" + foreignCity.getCityCount() + ")");
        String currentLetter = getFirstLetter(foreignCity.getCountryInitial());
        String previousLetter = position >= 1 ? getFirstLetter(mCities.get(position - 1).getCountryInitial()) : "";
        if (!TextUtils.equals(currentLetter, previousLetter)) {
            holder.t_pingyin.setVisibility(View.VISIBLE);
            holder.t_pingyin.setText(currentLetter);
        } else {
            holder.t_pingyin.setVisibility(View.GONE);
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
                final ArrayList<CityEntry.BodyEntity.ItemsEntity> cities = new ArrayList<CityEntry.BodyEntity.ItemsEntity>();
                cities.clear();
                ProgressDlgUtil.showProgressDlg("Loading...", activity);
                /*****************用自己的方法请求****************/
                GetCityBiz.getInstance(activity).getCityList(Integer.valueOf(countryId), new Response.Listener<CityEntry>() {
                    @Override
                    public void onResponse(CityEntry response) {

                        if (response != null) {
                            ProgressDlgUtil.stopProgressDlg();
                            hasLoad[0] = true;
                            holder.gridview_hot_city.setVisibility(View.VISIBLE);
                            ObjectAnimator.ofFloat(holder.gridview_hot_city, "alpha", 0, 1).setDuration(durationAlpha).start();
                            for (int i = 0; i < response.body.items.size(); i++) {
                                CityEntry.BodyEntity.ItemsEntity city = new CityEntry.BodyEntity.ItemsEntity();
                                city.setId(response.body.items.get(i).id);
                                city.setName(response.body.items.get(i).name);
//                                    city.setCity_id(response.body.items.get(i).id);
//                                    city.setCate_name(response.body.items.get(i).name);
                                cities.add(city);
                            }
                            initGridView(cities, holder);
//
                            ///////////////////////////保存数据///////////////////////////

//
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG).show();
                        ProgressDlgUtil.stopProgressDlg();
                    }
                }, null);
//
//                            }catch (Exception e){
//                                Log.i("getAreaList：",e.getMessage()+"");
//                            }
//                        }
//                    }).start();
//                }catch (Exception e){
//                    Log.i("JSONException：",e.getMessage()+"");
//                }
            }
        });
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

    private void initGridView(final ArrayList<CityEntry.BodyEntity.ItemsEntity> cities, final CityViewHolder holder) {

        holder.gridview_hot_city.setAdapter(new CommonAdapter<CityEntry.BodyEntity.ItemsEntity>(activity, cities, R.layout.local_item_city_picker_grid) {
            @Override
            public void convert(HqbViewHolder helper, CityEntry.BodyEntity.ItemsEntity item, int position) {
//                helper.setText(R.id.t_city_name, Util.StringFilter(item.getCate_name())+"\n"+item.getCatename_en());
                helper.setText(R.id.t_city_name, item.getName());
            }
        });

        holder.gridview_hot_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("countryName", holder.t_country_name.getText().toString());
//                intent.putExtra("countryId",foreignCity.getCountryEnName());
                intent.putExtra("cityName", cities.get(position).name);
//                intent.putExtra("cityName", cities.get(position).getCate_name());
                intent.putExtra("cityId", cities.get(position).id);
                activity.setResult(1, intent);
                activity.finish();
//                Toast.makeText(activity, holder.t_country_name.getText().toString()+cities.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static class CityViewHolder {
        public LinearLayout linear_country;
        public TextView t_country_name;
        public TextView t_pingyin;
        public TextView t_num;
        public MyGridView gridview_hot_city;
        public ImageView img_arrow;
    }

}
