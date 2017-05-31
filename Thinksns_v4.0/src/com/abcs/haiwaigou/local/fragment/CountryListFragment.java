package com.abcs.haiwaigou.local.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager;
import com.abcs.haiwaigou.local.adapter.SearchResultAdapter;
import com.abcs.haiwaigou.local.beans.City;
import com.abcs.haiwaigou.local.beans.LocateState;
import com.abcs.haiwaigou.local.fragment.adapter.CityAdapter;
import com.abcs.haiwaigou.local.view.SideLetterBar;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.tljr.news.widget.InputTools;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zjz on 2016/9/6.
 */
public class CountryListFragment extends BaseFragment implements View.OnClickListener {

    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.t_refresh)
    TextView tRefresh;
    @InjectView(R.id.tv_letter_overlay)
    TextView tvLetterOverlay;
    @InjectView(R.id.side_letter_bar)
    SideLetterBar sideLetterBar;
    @InjectView(R.id.listview_all_city)
    ListView listviewAllCity;
    @InjectView(R.id.et_search)
    EditText etSearch;
    @InjectView(R.id.iv_search_clear)
    ImageView ivSearchClear;
    @InjectView(R.id.t_search)
    TextView tSearch;
    @InjectView(R.id.listview_search_result)
    ListView listviewSearchResult;
    @InjectView(R.id.empty_view)
    LinearLayout emptyView;
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
    private String countryId;
    private boolean reset;
    private Handler handler = new Handler();
    FullyLinearLayoutManager fullyLinearLayoutManager;
    //    CountryAdapter countryAdpater;
    private CityAdapter cityAdapter;
    private SearchResultAdapter mResultAdapter;
    private ArrayList<City> cities = new ArrayList<City>();

    //    private volatile static CountryListFragment countryListFragment=null;
    public static CountryListFragment newInstance(String areaId, String objectName, String countryId, boolean reset) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("areaId", areaId);
        bundle.putSerializable("objectName", objectName);
        bundle.putSerializable("countryId", countryId);
        bundle.putSerializable("reset", reset);
//        if(countryListFragment==null){
//            synchronized (CountryListFragment.class){
//                if(countryListFragment==null){
//                    countryListFragment=new CountryListFragment();
//                    countryListFragment.setArguments(bundle);
//                }
//            }
//        }
        CountryListFragment countryFragment = new CountryListFragment();
        countryFragment.setArguments(bundle);
        return countryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.local_fragment_city_picker, null);
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
            countryId = bundle.getString("countryId");
            reset = bundle.getBoolean("reset");
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
        initEditText();
        initListener();
    }

    private void initListener() {
        ivSearchClear.setOnClickListener(this);
        tSearch.setOnClickListener(this);
    }

    private void initEditText() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    ivSearchClear.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    listviewSearchResult.setVisibility(View.GONE);
                } else {
                    ivSearchClear.setVisibility(View.VISIBLE);
                }
            }
        });
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
//        fullyLinearLayoutManager = new FullyLinearLayoutManager(activity);
//        recyclerView.setLayoutManager(fullyLinearLayoutManager);
        initAllDates();


    }

    LocationManager mLocationManager01;
    Location mLocation01;
    String strLocationProvider;
    public final LocationListener mLocationListener01 = new LocationListener() {

        public void onLocationChanged(Location location) {
// TODO Auto-generated method stub
            processLocationUpDated(location);
        }

        public void onProviderDisabled(String provider) {
// TODO Auto-generated method stub

        }

        public void onProviderEnabled(String provider) {
// TODO Auto-generated method stub

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
// TODO Auto-generated method stub

        }

    };

    private void gpsdw() {
        try {
            mLocationManager01 = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            mLocation01 = getLocationProvider(mLocationManager01);
            if (mLocation01 != null) {
                processLocationUpDated(mLocation01);
            } else {
                cityAdapter.updateLocateState(LocateState.FAILED, null);
                mLocationManager01.requestLocationUpdates(strLocationProvider, 0, 0, mLocationListener01);
            }
            //监视 时间为2秒距离为10米
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processLocationUpDated(Location location) {
        if (location != null) {
            Log.i("zjz", "latitude=" + location.getLatitude());
            Log.i("zjz", "longitude=" + location.getLongitude());
            initLocate(location.getLatitude(), location.getLongitude());
        } else {
            cityAdapter.updateLocateState(LocateState.FAILED, null);
            mLocationManager01.requestLocationUpdates(strLocationProvider, 0, 0, mLocationListener01);
        }
    }

    private Location getLocationProvider(LocationManager lm) {
        Location retLocation = null;
        try {
            Criteria mCriteria01 = new Criteria();
            mCriteria01.setAccuracy(mCriteria01.ACCURACY_COARSE);
            mCriteria01.setAltitudeRequired(false);
            mCriteria01.setBearingRequired(false);
            mCriteria01.setCostAllowed(true);
            mCriteria01.setSpeedRequired(false);
            mCriteria01.setPowerRequirement(Criteria.POWER_LOW);
            strLocationProvider = lm.getBestProvider(mCriteria01, true);
            //List<String> matchingProviders = lm.getProviders(mCriteria01,false);
            retLocation = lm.getLastKnownLocation(strLocationProvider);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retLocation;
    }


    private void initLocation() {

        /****************使用Android原生的定位********************/
//        LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
//        List<String> lp = lm.getAllProviders();// 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
//        for (String item : lp) {
//            Log.i("zjz", "---->可用位置服务:" + item);
//        }
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
//        criteria.setAltitudeRequired(false);//不要求海拔
//        criteria.setBearingRequired(false);//不要求方位
//        criteria.setCostAllowed(false);//不允许有花费
//        criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗
//        String providerName = lm.getBestProvider(criteria, true); //getBestProvider 只有允许访问调用活动的位置供应商将被返回
//        Log.i("zjz", "选择的位置服务为：" + providerName);
////        ProgressDlgUtil.showProgressDlg("正在定位...", activity);
//        if (providerName != null) {
//            Location location = lm.getLastKnownLocation(providerName);
//            if (location != null) {
//                double latitude = location.getLatitude();//获取维度信息
//                double longitude = location.getLongitude();//获取经度信息
//                Log.i("zjz", "GPS_latitude=" + latitude + "  longitude=" + longitude);
//                initLocate(latitude, longitude);
//            } else {
//                showToast(activity, "1.请检查网络连接 \n2.请打开我的位置");
//                cityAdapter.updateLocateState(LocateState.FAILED, null);
//            }
//        } else {
//            showToast(activity, "1.请检查网络连接 \n2.请打开我的位置");
//            cityAdapter.updateLocateState(LocateState.FAILED, null);
//        }
        /****************使用百度SDK定位********************/
        initLocationWithBaiDu();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLocationClient.stop();
            }
        },5000);

    }


    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    private void initLocationWithBaiDu(){
        mLocationClient = new LocationClient(getActivity());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setCoorType("wgs84"); // 设置坐标类型
        option.setIsNeedAddress(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();


    }

    private String current_lng;
    private String current_lat;


    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            if (location.getLocType() == BDLocation.TypeGpsLocation) {   // GPS定位结果
                double latitude = location.getLatitude();//获取维度信息
                double longitude = location.getLongitude();//获取经度信息

                current_lat=String .valueOf(latitude);
                current_lng=String .valueOf(longitude);
                Log.i("zds", "lat=" + current_lat + "  lng=" + current_lng);
                if (!TextUtils.isEmpty(current_lat)&&!TextUtils.isEmpty(current_lng)) {
                    initLocate(Double.valueOf(current_lat), Double.valueOf(current_lng));
                } else {
                    showToast(activity, "1.请检查网络连接 \n2.请打开我的位置");
                    cityAdapter.updateLocateState(LocateState.FAILED, null);
                }


            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                double latitude = location.getLatitude();//获取经度
                double longitude = location.getLongitude();//获取纬度
                current_lat=String .valueOf(latitude);
                current_lng=String .valueOf(longitude);
                Log.i("zds", "lat=" + current_lat + "  lng=" + current_lng);
                if (!TextUtils.isEmpty(current_lat)&&!TextUtils.isEmpty(current_lng)) {
                    initLocate(Double.valueOf(current_lat), Double.valueOf(current_lng));
                } else {
                    showToast(activity, "1.请检查网络连接 \n2.请打开我的位置");
                    cityAdapter.updateLocateState(LocateState.FAILED, null);
                }
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {  // 离线定位结果
                double latitude = location.getLatitude();//获取经度
                double longitude = location.getLongitude();//获取纬度
                current_lat=String .valueOf(latitude);
                current_lng=String .valueOf(longitude);
                Log.i("zds", "lat=" + current_lat + "  lng=" + current_lng);
                if (!TextUtils.isEmpty(current_lat)&&!TextUtils.isEmpty(current_lng)) {
                    initLocate(Double.valueOf(current_lat), Double.valueOf(current_lng));
                } else {
                    showToast(activity, "1.请检查网络连接 \n2.请打开我的位置");
                    cityAdapter.updateLocateState(LocateState.FAILED, null);
                }
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }

//            Toast.makeText(activity,sb.toString(),Toast.LENGTH_LONG).show();
            Log.i("zds", sb.toString());
            Log.i("zds", current_lat);
            Log.i("zds", current_lng);
        }
    }

    private void initLocate(double latitude, double longitude) {
        HttpRequest.sendGet(TLUrl.getInstance().URL_translate_latlng_to_address, "lat=" + latitude + "&lng=" + longitude + "&uid=" + MyApplication.getInstance().self.getId(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject mainObject = new JSONObject(msg);
                            Log.i("zjz", "getGPS_msg=" + msg);
                            if (mainObject.optString("status").equals("1")) {
                                JSONObject msgObj = mainObject.optJSONObject("msg");
                                if (msgObj != null) {
                                    StringBuilder stringBuffer = new StringBuilder();
                                    String city="";
                                    JSONObject baiduObj = msgObj.optJSONObject("baidu");
                                    if (baiduObj != null) {
                                        JSONObject baiduResult = baiduObj.optJSONObject("result");
                                        if (baiduResult != null) {
                                            stringBuffer.append("百度：\n");
                                            baiduResult.optString("address");
                                            baiduResult.optString("city");
                                            baiduResult.optString("country");
                                            baiduResult.optString("description");
                                            baiduResult.optString("district");
                                            baiduResult.optString("lat");
                                            baiduResult.optString("lng");
                                            baiduResult.optString("province");
                                            baiduResult.optString("type");
                                            stringBuffer.append("当前位置：\n")
                                                    .append("Country:" + baiduResult.optString("country") + "\n")
                                                    .append("Province:" + baiduResult.optString("province") + "\n")
                                                    .append("City:" + baiduResult.optString("city") + "\n")
                                                    .append("District:" + baiduResult.optString("district") + "\n")
                                                    .append("Address:" + baiduResult.optString("address") + "\n");
                                            city=baiduResult.optString("city");
                                        }
                                    } else {
                                        stringBuffer.append("百度没有信息\n");
                                    }

                                    JSONObject googleObj = msgObj.optJSONObject("google");
                                    if (googleObj != null) {
                                        JSONObject googleResult = googleObj.optJSONObject("result");
                                        if (googleResult != null) {
                                            stringBuffer.append("Google：\n");
                                            googleResult.optString("address");
                                            googleResult.optString("city");
                                            googleResult.optString("country");
                                            googleResult.optString("description");
                                            googleResult.optString("district");
                                            googleResult.optString("lat");
                                            googleResult.optString("lng");
                                            googleResult.optString("province");
                                            googleResult.optString("type");
                                            stringBuffer.append("当前位置：\n")
                                                    .append("Country:" + googleResult.optString("country") + "\n")
                                                    .append("Province:" + googleResult.optString("province") + "\n")
                                                    .append("City:" + googleResult.optString("city") + "\n")
                                                    .append("District:" + googleResult.optString("district") + "\n")
                                                    .append("Address:" + googleResult.optString("address") + "\n");
                                            city=googleResult.optString("city");
                                        }
                                    } else {
                                        stringBuffer.append("Google没有信息\n");
                                    }
                                    Log.i("zjz","located_city="+city);
                                    if(!TextUtils.isEmpty(city)){
                                        cityAdapter.updateLocateState(LocateState.SUCCESS, city);
                                    }
                                    else {
                                        cityAdapter.updateLocateState(LocateState.FAILED, null);
                                    }

                                    String addr = stringBuffer.toString();
//                                        Log.i("zjz", "address=" + addr);
//                                        new ShowMessageDialog(view, activity, Util.WIDTH * 4 / 5, addr, "当前定位");
                                } else {
                                    cityAdapter.updateLocateState(LocateState.FAILED, null);
                                }
                            } else {
                                cityAdapter.updateLocateState(LocateState.FAILED, null);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
//                                ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });
            }
        });
    }

    private void initSlideBar() {
        sideLetterBar.setOverlay(tvLetterOverlay);
        sideLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                if (letter.equals("#")) {
                    listviewAllCity.setSelection(0);
                } else {
                    int position = cityAdapter.getLetterPosition(letter);
                    listviewAllCity.setSelection(position);
                }
//                recyclerView.scrollToPosition(position);
//                recyclerView.getLayoutManager().scrollToPosition(position);
            }
        });
    }

    private void initAllDates() {
        mResultAdapter = new SearchResultAdapter(activity);
        JSONArray countryArray = aCache.getAsJSONArray(objectName);
        if (countryArray != null) {
            Log.i("zjz", objectName + "_local_country=" + countryArray);
            try {
                initCountryList(countryArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("zjz", "country_url=" + TLUrl.getInstance().getInstance().URL_local_get_country + "?areaId=" + areaId);
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
        City city = null;
        for (int i = 0; i < countryArray.length(); i++) {
            JSONObject countryObj = countryArray.getJSONObject(i);
            city = new City();
            city.setId(countryObj.optString("countryId"));
            city.setAreaId(areaId);
            city.setCountryInitial(countryObj.optString("initial"));
            city.setCountryCnName(countryObj.optString("countryCnName"));
            city.setCountryId(countryObj.optString("countryId"));
            city.setCityCount(countryObj.optString("cityCount"));
            city.setCountryEnName(countryObj.optString("countryEnName"));
//            countryAdpater.addDatas(city);
            cities.add(city);
        }

        SharedPreferences sp = activity.getSharedPreferences(MyString.CITY_CACHE, 0);
        String save_history = sp.getString(MyString.CITYS, "");
        Log.i("zjz", "cache_" + save_history);
        String[] citys = save_history.split("##");
        Log.i("zjz", "citys[0]=" + citys[0]);
        if (!TextUtils.isEmpty(citys[0])) {
            cityAdapter = new CityAdapter(activity, cities, countryId, reset, 3);
        } else {
            cityAdapter = new CityAdapter(activity, cities, countryId, reset, 2);
        }

        cityAdapter.setOnCityClickListener(new CityAdapter.OnCityClickListener() {
            @Override
            public void locationClick() {
                cityAdapter.updateLocateState(LocateState.LOCATING, null);
                initLocation();
//                gpsdw();
//                mLocationClient.startLocation();
            }
        });
//        countryAdpater = new CountryAdapter(activity, cities);
//        recyclerView.setAdapter(countryAdpater);
        cityAdapter.notifyDataSetChanged();
        listviewAllCity.setAdapter(cityAdapter);
        listviewAllCity.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                        InputTools.HideKeyboard(view);
                        break;
                    case SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView listview, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        if (reset) {
            setScroll();
        }
//        listviewAllCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                cityAdapter.changeImageVisible(view,position);
//            }
//        });
        initSlideBar();
        initLocation();
//        gpsdw();
        listviewSearchResult.setAdapter(mResultAdapter);
        listviewSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("countryName", mResultAdapter.getItem(position).getCountryCnName());
                intent.putExtra("countryId", mResultAdapter.getItem(position).getCountryId());
                intent.putExtra("cityName", mResultAdapter.getItem(position).getCate_name());
                intent.putExtra("cityId", mResultAdapter.getItem(position).getCity_id());
                intent.putExtra("areaId", "");
                saveCity(mResultAdapter.getItem(position).getCountryCnName(), mResultAdapter.getItem(position));
                activity.setResult(1, intent);
                activity.finish();
            }
        });
    }

    private void saveCity(String countryName, City city) {
        String text = (countryName + "," + city.getCountryId() + "," + city.getCate_name() + "," + city.getCatename_en() + "," + city.getCity_id() + "," + city.getAreaId());
        SharedPreferences sp = activity.getSharedPreferences(MyString.CITY_CACHE, 0);
        String save_Str = sp.getString(MyString.CITYS, "");
        Log.i("zjz", "local_city=" + save_Str);
        Log.i("zjz", "save_city=" + city.getCate_name());
        String temp = "";
        String[] citys = save_Str.split("##");
        for (String city1 : citys) {
            if (city1.contains(city.getCate_name())) {
                temp = save_Str.replaceAll(city1 + "##", "");
            }
        }
        StringBuilder sb;
        if (!TextUtils.isEmpty(temp)) {
            sb = new StringBuilder(temp);
        } else {
            sb = new StringBuilder(save_Str);
        }
        sb.append(text + "##");
        sp.edit().putString(MyString.CITYS, sb.toString()).commit();
    }

    List<City> searchList = new ArrayList<City>();

    private void searchCity(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            showToast(activity, "请输入城市名称");
            return;
        }
        InputTools.HideKeyboard(view);
        Log.i("zjz", "search_url=" + TLUrl.getInstance().URL_local_search_city + "?cityName=" + keyword);
        final String putURL = TLUrl.getInstance().URL_local_search_city + "?cityName=" + keyword;
        searchList.clear();
        final HttpUtils hu = new HttpUtils(20000);
//        final RequestParams params = new RequestParams();
//        params.addBodyParameter("cityName",keyword);
        handler.post(new Runnable() {
            @Override
            public void run() {
                hu.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, putURL, null,
                        new RequestCallBack<String>() {
                            @Override
                            public void onFailure(HttpException arg0, String arg1) {
                                emptyView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onSuccess(ResponseInfo<String> arg0) {
                                Log.i("zjz", "publish_msg=" + arg0.result);
                                try {
                                    JSONObject mainObj = new JSONObject(arg0.result);
                                    if (mainObj.optString("status").equals("1")) {
                                        JSONArray msgArray = mainObj.optJSONArray("msg");
                                        if (msgArray != null && msgArray.length() != 0) {
                                            for (int i = 0; i < msgArray.length(); i++) {
                                                JSONObject msgObj = msgArray.getJSONObject(i);
                                                City city = new City();
                                                city.setCity_id(msgObj.optString("city_id"));
                                                city.setCountryId(msgObj.optString("countryId"));
                                                city.setCate_name(msgObj.optString("cate_name"));
                                                city.setCatename_en(msgObj.optString("catename_en"));
                                                city.setCountryCnName(msgObj.optString("countryCnName"));
                                                searchList.add(city);
                                            }
                                        }
                                    }
                                    Log.i("zjz", "searchList=" + searchList.size());
                                    if (searchList == null || searchList.size() == 0) {
                                        emptyView.setVisibility(View.VISIBLE);
                                    } else {
                                        listviewSearchResult.setVisibility(View.VISIBLE);
                                        emptyView.setVisibility(View.GONE);
                                        mResultAdapter.changeData(searchList);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });

    }

    private void setScroll() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initPosition();
            }
        }, 500);
    }

    private void initPosition() {
        if (cities.size() != 0) {
            for (int i = 0; i < cities.size(); i++) {
                if (countryId.equals(cities.get(i).getCountryId())) {
                    listviewAllCity.setSelection(i);
                    Log.i("zjz", "scroll_position=" + i);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search_clear:
                etSearch.setText("");
                ivSearchClear.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                listviewSearchResult.setVisibility(View.GONE);
                break;
            case R.id.t_search:
                searchCity(etSearch.getText().toString());
                break;

        }
    }
}
