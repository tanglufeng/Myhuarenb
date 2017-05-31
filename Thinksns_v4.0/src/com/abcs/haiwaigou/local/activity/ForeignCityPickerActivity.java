package com.abcs.haiwaigou.local.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager;
import com.abcs.haiwaigou.local.adapter.CityPickerAdapter;
import com.abcs.haiwaigou.local.beans.ForeignCity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.sociax.android.R;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ForeignCityPickerActivity extends BaseActivity {

    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    FullyLinearLayoutManager fullyLinearLayoutManager;
    CityPickerAdapter cityPickerAdapter;
    ArrayList<ForeignCity> foreignCities = new ArrayList<ForeignCity>();
    Handler handler = new Handler();
    boolean isFirst;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_activity_foreign_city_picker);
        ButterKnife.inject(this);
        initRecycler();
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isFirst = true;
                initDatas();
            }
        });
    }

    private void initRecycler() {
        fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
        recyclerView.setLayoutManager(fullyLinearLayoutManager);
        cityPickerAdapter = new CityPickerAdapter(this);
        initDatas();
        recyclerView.setAdapter(cityPickerAdapter);
    }

    private void initDatas() {
        if (!isFirst) {
            ProgressDlgUtil.showProgressDlg("Loading...", this);
        }
        HttpRequest.sendGet(TLUrl.getInstance().URL_bind_base+"/hrq/City/getCountryAndCityList.json", null, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i("zjz", "cityPicker_msg=" + msg);
                            JSONArray array = new JSONArray(msg);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                ForeignCity foreignCity = new ForeignCity();
                                foreignCity.setCountryId(object.optString("countryId"));
                                foreignCity.setKeyTitle(object.optString("keyTitle"));
                                foreignCity.setCountryEnName(object.optString("countryEnName"));
                                JSONArray cityArray = object.optJSONArray("hrqCities");
                                ArrayList<ForeignCity.HrqCitiesBean> hrqCitiesBeanArrayList = new ArrayList<ForeignCity.HrqCitiesBean>();
                                if (cityArray != null) {
                                    for (int j = 0; j < cityArray.length(); j++) {
                                        ForeignCity.HrqCitiesBean hrqCitiesBean = new ForeignCity.HrqCitiesBean();
                                        JSONObject cityObj = cityArray.getJSONObject(j);
                                        hrqCitiesBean.setCityId(cityObj.optString("cityId"));
                                        hrqCitiesBean.setCityName(cityObj.optString("cityName"));
                                        hrqCitiesBeanArrayList.add(hrqCitiesBean);
                                    }
                                    foreignCity.setHrqCities(hrqCitiesBeanArrayList);
                                }
                                cityPickerAdapter.addDatas(foreignCity);
                            }
                            cityPickerAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            ProgressDlgUtil.stopProgressDlg();
                            if(swipeRefreshLayout.isRefreshing()&&swipeRefreshLayout!=null){
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    }
                });
            }
        });
    }
}
