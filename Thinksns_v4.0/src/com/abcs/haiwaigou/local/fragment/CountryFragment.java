package com.abcs.haiwaigou.local.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager;
import com.abcs.haiwaigou.local.beans.City;
import com.abcs.haiwaigou.local.fragment.adapter.CountryAdapter;
import com.abcs.haiwaigou.local.view.SideLetterBar;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zjz on 2016/9/6.
 */
public class CountryFragment extends BaseFragment {

    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.t_refresh)
    TextView tRefresh;
    @InjectView(R.id.tv_letter_overlay)
    TextView tvLetterOverlay;
    @InjectView(R.id.side_letter_bar)
    SideLetterBar sideLetterBar;
    private View view;
    private Activity activity;
    private String objectName;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;

    ACache aCache;
    private String areaId;
    private Handler handler = new Handler();
    FullyLinearLayoutManager fullyLinearLayoutManager;
    CountryAdapter countryAdpater;

    private ArrayList<City>cities=new ArrayList<City>();
    public static CountryFragment newInstance(String areaId, String objectName) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("areaId", areaId);
        bundle.putSerializable("objectName", objectName);
        CountryFragment countryFragment = new CountryFragment();
        countryFragment.setArguments(bundle);
        return countryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.local_city_picker, null);
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
            areaId = bundle.getString("areaId");
            objectName = bundle.getString("objectName");
            Log.i("zjz", "local_object=" + objectName);
        }
        isPrepared = true;
        lazyLoad();

        return view;
    }



    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }

        initRecycler();
    }

    private void initRecycler() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                aCache.remove(objectName);
                initAllDates();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        fullyLinearLayoutManager = new FullyLinearLayoutManager(activity);
        recyclerView.setLayoutManager(fullyLinearLayoutManager);
        initAllDates();


    }
    private void initSlideBar() {
        sideLetterBar.setOverlay(tvLetterOverlay);
        sideLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = countryAdpater.getLetterPosition(letter);
                Log.i("zjz","slide_position="+position);
//                recyclerView.scrollToPosition(position);
                recyclerView.getLayoutManager().scrollToPosition(position);
            }
        });
    }
    private void initAllDates() {

        JSONArray countryArray = aCache.getAsJSONArray(objectName);
        if (countryArray != null) {
            Log.i("zjz", objectName + "_local_country=" + countryArray);
            try {
                initCountryList(countryArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_country, "areaId=" + areaId, new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(msg);
                                JSONArray countryArray = jsonObject.optJSONArray("msg");
                                if (countryArray != null) {
                                    if (aCache.getAsJSONArray(objectName) == null) {
                                        aCache.put(objectName, countryArray, 7 * 24 * 60 * 60);
                                    }
                                    initCountryList(countryArray);
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

    private void initCountryList(JSONArray countryArray) throws JSONException {
        mHasLoadedOnce = true;
//        countryAdpater.getDataList().clear();
        cities.clear();
        for (int i = 0; i < countryArray.length(); i++) {
            JSONObject countryObj = countryArray.getJSONObject(i);
            City city = new City();
            city.setCountryInitial(countryObj.optString("initial"));
            city.setCountryCnName(countryObj.optString("countryCnName"));
            city.setCountryId(countryObj.optString("countryId"));
            city.setCityCount(countryObj.optString("cityCount"));
            city.setCountryEnName(countryObj.optString("countryEnName"));
//            countryAdpater.addDatas(city);
            cities.add(city);
        }
        countryAdpater = new CountryAdapter(activity,cities);
        recyclerView.setAdapter(countryAdpater);
        countryAdpater.notifyDataSetChanged();
        initSlideBar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
