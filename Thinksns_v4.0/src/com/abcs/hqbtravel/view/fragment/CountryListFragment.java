package com.abcs.hqbtravel.view.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager;
import com.abcs.hqbtravel.adapter.CityAdapter;
import com.abcs.hqbtravel.entity.City;
import com.abcs.hqbtravel.wedgt.ACache;
import com.abcs.hqbtravel.wedgt.Config;
import com.abcs.hqbtravel.wedgt.SideLetterBar;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.sociax.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by zjz on 2016/9/6.
 */
public class CountryListFragment extends BaseFragment {

    SwipeRefreshLayout swipeRefreshLayout;
    TextView tRefresh;
    TextView tvLetterOverlay;
    SideLetterBar sideLetterBar;
    ListView listviewAllCity;
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
    //    CountryAdapter countryAdpater;
    CityAdapter cityAdapter;
    private ArrayList<City> cities = new ArrayList<City>();

    public static CountryListFragment newInstance(String areaId, String objectName) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("areaId", areaId);
        bundle.putSerializable("objectName", objectName);
        CountryListFragment countryFragment = new CountryListFragment();
        countryFragment.setArguments(bundle);
        return countryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.local_fragment_city_picker2, null);
        }
        aCache = ACache.get(activity);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        tRefresh=(TextView)view.findViewById(R.id.t_refresh);
        tvLetterOverlay=(TextView)view.findViewById(R.id.tv_letter_overlay);
        sideLetterBar=(SideLetterBar)view.findViewById(R.id.side_letter_bar);
        listviewAllCity=(ListView)view.findViewById(R.id.listview_all_city);

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

        swipeRefreshLayout.setRefreshing(false);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                aCache.remove(objectName);
//                initAllDates();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }, 2000);
//            }
//        });
//        fullyLinearLayoutManager = new FullyLinearLayoutManager(activity);
//        recyclerView.setLayoutManager(fullyLinearLayoutManager);
        initAllDates();
    }

    private void initSlideBar() {
        sideLetterBar.setOverlay(tvLetterOverlay);
        sideLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = cityAdapter.getLetterPosition(letter);
                Log.i("zjz", "slide_position=" + position);
                listviewAllCity.setSelection(position);
//                recyclerView.scrollToPosition(position);
//                recyclerView.getLayoutManager().scrollToPosition(position);
            }
        });
    }

    private void initAllDates(){
        String tt = aCache.getAsString(objectName);
        try{
            if(!TextUtils.isEmpty(tt)){
                Log.i("本地国家：",tt);
                initCountryList(ACache.asJsonArray(tt));
            }else {
                ProgressDlgUtil.showProgressDlg("Loading...", getActivity());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getCountryList();
                        }catch (IOException e){
                            Log.i("getAreaList：",e.getMessage()+"");
                        }
                    }
                }).start();
            }
        }catch (JSONException e){
            Log.i("JSONException：",e.getMessage()+"");
        }
    }


    public void getCountryList( ) throws IOException{
        try {
//            https://japi.tuling.me/hrq/City/getCountryListByAreaId.json?areaId=239
            final String spec = Config.GET_COUNTRY + areaId;
            Log.i("areaIdw",areaId);

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

                Log.i("国家的结果： ",result);

                if(!TextUtils.isEmpty(result)){
                    if (aCache.getAsString(objectName) == null) {
                        aCache.put(objectName, result, 7 * 24 * 60 * 60);
                    }


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                Log.i("网络国家： ",result);
                                initCountryList(ACache.asJsonArray(result));
                            }catch (JSONException e){
                                Log.i("JSONException： ",result);
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
        cityAdapter=new CityAdapter(activity,cities);
//        countryAdpater = new CountryAdapter(activity, cities);
//        recyclerView.setAdapter(countryAdpater);
        cityAdapter.notifyDataSetChanged();
        listviewAllCity.setAdapter(cityAdapter);
//        listviewAllCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                cityAdapter.changeImageVisible(view,position);
//            }
//        });
        initSlideBar();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
