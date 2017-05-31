package com.abcs.hqbtravel.view.activity;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.hqbtravel.RollPager.Util;
import com.abcs.hqbtravel.adapter.CFViewPagerAdapter;
import com.abcs.hqbtravel.entity.City;
import com.abcs.hqbtravel.view.fragment.CountryListFragment;
import com.abcs.hqbtravel.wedgt.ACache;
import com.abcs.hqbtravel.wedgt.Config;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.sociax.android.R;
import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class CountryCityActivity extends FragmentActivity {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.t_title_name)
    TextView tTitleName;
    @InjectView(R.id.area_tabs)
    PagerSlidingTabStrip areaTabs;
    @InjectView(R.id.linear_tab)
    LinearLayout linearTab;
    @InjectView(R.id.seperate_line)
    View seperateLine;
    @InjectView(R.id.main_pager)
    ViewPager mainPager;
    @InjectView(R.id.linear_root)
    LinearLayout linearRoot;


    private ArrayList<City> cities = new ArrayList<City>();
    private Handler handler = new Handler();
    Fragment currentFragment;
    int position;
    int currentType;
    CFViewPagerAdapter viewPagerAdapter;
    private String[] strs;
    ACache aCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.local_activity_country_city2);
        ButterKnife.inject(this);

        tTitleName.setText("请选择城市");
        aCache = ACache.get(this);
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initArea();
    }

    public void initArea() {

        try {
            String relite = aCache.getAsString(ACache.File_AREA);
            if (!TextUtils.isEmpty(relite)) {
                initAreaList(ACache.asJsonArray(relite));
                Log.i("本地：", relite);
            } else {
                ProgressDlgUtil.showProgressDlg("Loading...", this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getAreaList();
                        } catch (IOException e) {
                            Log.i("getAreaList：", e.getMessage() + "");
                        }
                    }
                }).start();
            }

        } catch (JSONException e) {
            Log.i("JSONException：", e.getMessage() + "");
        }
    }


    public void getAreaList() throws IOException {
        try {
            final String spec = Config.GET_AREA;
            URL url = new URL(spec);
            // url.openConnection()打开网络链接
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("GET");// 设置请求的方式
            urlConnection.setReadTimeout(5000);// 设置超时的时间
            urlConnection.setConnectTimeout(5000);// 设置链接超时的时间
            urlConnection.setDoInput(true);
            urlConnection.connect();
//            // 设置请求的头
//            urlConnection
//                    .setRequestProperty("User-Agent",
//                            "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
            // 获取响应的状态码 404 200 505 302
            if (urlConnection.getResponseCode() == 200) {
                ProgressDlgUtil.stopProgressDlg();
                // 获取响应的输入流对象

                InputStream is = urlConnection.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    os.write(buffer, 0, len);
                }


//                 返回字符串
                final String result = new String(os.toByteArray());

                // 释放资源
                os.close();
                is.close();

                Log.i("网络州的结果： ", result);

                if (!TextUtils.isEmpty(result)) {

                    if (aCache.getAsString(ACache.File_AREA) == null) {
                        aCache.put(ACache.File_AREA, result, 7 * 24 * 60 * 60);
                    }

                    CountryCityActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                initAreaList(ACache.asJsonArray(result));
                            } catch (JSONException e) {
                                Log.i("JSONException： ", result);
                            }
                        }
                    });
                }
            } else {
                System.out.println("------------------链接失败-----------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAreaList(JSONArray areaArray) throws JSONException {
        Log.i("areaArray:  ", areaArray.toString());
        cities.clear();
        for (int i = 0; i < areaArray.length(); i++) {
            JSONObject areaObj = areaArray.getJSONObject(i);
            City city = new City();
            city.setAreaCnName(areaObj.optString("areaCnName"));
            city.setAreaEnName(areaObj.optString("areaEnName"));
            city.setAreaId(areaObj.optString("areaId"));
            cities.add(city);
        }
        initViewPager();
    }

    private void initViewPager() {
        strs = new String[cities.size()];
        viewPagerAdapter = new CFViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < cities.size(); i++) {
            strs[i] = ACache.File_AREA + i;
//            viewPagerAdapter.getDatas().add(CountryFragment.newInstance(cities.get(i).getAreaId(), strs[i]));
            viewPagerAdapter.getDatas().add(CountryListFragment.newInstance(cities.get(i).getAreaId(), strs[i]));
            viewPagerAdapter.getTitle().add(i, cities.get(i).getAreaCnName());
        }
        //第三方Tab
        if (mainPager != null && areaTabs != null) {
            mainPager.setAdapter(viewPagerAdapter);
            mainPager.setOffscreenPageLimit(1);
            mainPager.setCurrentItem(position);
//        pager.setPageTransformer(true, new DepthPageTransformer());
            areaTabs.setViewPager(mainPager);
            areaTabs.setIndicatorHeight(Util.dip2px(this, 4));
            areaTabs.setTextSize(Util.dip2px(this, 16));
            setSelectTextColor(position);
            setTextType();
            areaTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    // TODO Auto-generated method stub
                    setSelectTextColor(position);
                    currentFragment = viewPagerAdapter.getItem(position);
                    currentType = position + 1;
//                currentgoodsFragment.initRecycler();
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onPageScrollStateChanged(int position) {

                    System.out.println("Change Posiont:" + position);

                    // TODO Auto-generated method stub

                }
            });
            currentFragment = viewPagerAdapter.getItem(0);
            currentType = 1;
        }
    }

    private void setTextType() {
        for (int i = 0; i < cities.size(); i++) {
            View view = areaTabs.getChildAt(0);
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView != null) {
//                Util.setFZLTHFont(tabTextView);
            }
        }
    }

    private void setSelectTextColor(int position) {
        for (int i = 0; i < cities.size(); i++) {
            View view = areaTabs.getChildAt(0);
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView != null) {
                if (position == i) {
                    tabTextView.setTextColor(getResources().getColor(R.color.weiba_title_p));
                } else {
                    tabTextView.setTextColor(getResources().getColor(R.color.hwg_text2));
                }
            }
        }
    }
}
