package com.abcs.sociax.t4.android.map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.adapter.PoiAdapter;
import com.abcs.sociax.t4.android.ThinksnsAbscractActivity;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.thinksns.tschat.widget.SmallDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ActivityGetMyLocation extends ThinksnsAbscractActivity
        implements AdapterView.OnItemClickListener, OnGetPoiSearchResultListener {

    //    private AMap aMap;
//    private OnLocationChangedListener mListener;
//    private AMapLocationClient mlocationClient;
//    private AMapLocationClientOption mLocationOption;
    private ImageButton tv_title_left;
    private SmallDialog dialog;

    // about map data
    private String currentAddress;
    private double currentLatitude, currentLongitude;

    private ListView listPoi;
    private View headerView;
    private TextView headerStreet;

    List<PoiInfo> list = new ArrayList<PoiInfo>();


    // Poi
    //   private PoiSearch.Query query;// Poi查询条件类


    private PoiSearch mPoiSearch = null;


    private PoiResult poiResult; // poi返回的结果
    //    private List<PoiItem> poiItems;// poi数据
    private PoiAdapter adapter;


    MapView mMapView;
    BaiduMap mBaiduMap;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreateNoTitle(savedInstanceState);

        //      mapView.onCreate(savedInstanceState);// 必须要写

        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.map);
        mBaiduMap = mMapView.getMap();
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);



        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));


        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);

        mLocClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setCoorType("wgs84"); // 设置坐标类型
        option.setIsNeedAddress(true);
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();


        initView();
        initMap();
    }

    /**
     * 初始化地图相关
     */
    private void initMap() {
//        if (aMap == null) {
//            aMap = mapView.getMap();
//
//            aMap.setLocationSource(this);// 设置定位监听
//            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//            // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
//            aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//        }
    }

    @Override
    public void onGetPoiResult(PoiResult result) {

//        LogUtil.i("nowpio", "onGetPoiResult");


        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
//            Toast.makeText(ActivityGetMyLocation.this, "未找到结果", Toast.LENGTH_LONG).show();
            return;
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            poiResult = result;
            // 取得第一页的poiitem数据，页数从数字0开始
            List<PoiInfo> poiItems = poiResult.getAllPoi();
            if (poiItems != null && poiItems.size() > 0) {
                Log.e("sn_", poiItems.get(0).address);
                list.clear();

                list.addAll(poiItems);
                adapter.notifyDataSetChanged();
                mLocClient.stop();
            }

            return;
        }


    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
//        LogUtil.i("nowpio", "onGetPoiDetailResult");
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
//        LogUtil.i("nowpio", "onGetPoiIndoorResult");
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            // LogUtil.i("locationbaidu","latitude"+location.getLatitude() + "longtitude"+location.getLongitude());
          if (location.getLatitude()==4.9e-324 ){
              return;
          }else{
              mLocClient.stop();
          }

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

//            LogUtil.i("locationbaidu", " location.getAddress().address:" + location.getAddress().address);
//            LogUtil.i("locationbaidu", " location.getCountry:" + location.getCountry());
//            LogUtil.i("locationbaidu", " location.getCity:" + location.getCity());
            currentAddress = /*amapLocation.getProvince() + */location.getCity() + location.getDistrict() + location.getStreet();
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
//            LogUtil.i("locationbaidu", "getMyLocation当前定位结果:currentLongitude :" +currentLongitude   + ", currentLatitude:" + currentLatitude + ", " + currentAddress);

            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(21.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));


            initPoiAround(location);

        }

        public void onReceivePoi(BDLocation poiLocation) {
//            LogUtil.i("locationbaidu", "onReceivePoi:");

        }
    }


    /**
     * 初始化控件
     */
    private void initView() {
        tv_title_left = (ImageButton) findViewById(R.id.tv_title_left);

        listPoi = (ListView) findViewById(R.id.list_poi);
        listPoi.setOnItemClickListener(this);

        headerView = getLayoutInflater().inflate(R.layout.header_my_location, null, false);
        headerStreet = (TextView) headerView.findViewById(R.id.tv_location_street);

        initListener();
    }

    /**
     * 初始化监听事件
     */
    private void initListener() {
        tv_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityGetMyLocation.this.finish();
            }
        });

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
//               LogUtil.i("zjz","why currentLatitude:"+currentLatitude);
//               LogUtil.i("zjz","why currentLongitude:"+currentLongitude);
                returnLocation(currentAddress, currentLatitude,currentLongitude);
            }
        });
    }

    /**
     * 显示提示框
     */
    private void showDialog() {
        showDialog("请稍后...");
    }

    /**
     * 显示提示框
     *
     * @param msg 消息
     */
    private void showDialog(String msg) {
        if (dialog == null) {
            dialog = new SmallDialog(this, msg);
        } else {
            dialog.setContent(msg);
        }
        dialog.show();
    }

    /**
     * 隐藏提示框
     */
    private void hideDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public String getTitleCenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_get_my_location;
    }

    /**
     * 当定位发生变化
     *
     * @param
     */
//    @Override
//    public void onLocationChanged(AMapLocation amapLocation) {
//        if (mListener != null && amapLocation != null) {
//            if (amapLocation.getErrorCode() == 0) {
//                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
//
//                // 获取地理位置信息
//                currentAddress = /*amapLocation.getProvince() + */amapLocation.getCity() + amapLocation.getDistrict() + amapLocation.getRoad();
//                currentLatitude = amapLocation.getLatitude();
//                currentLongitude = amapLocation.getLongitude();
//                initPoiAround(amapLocation);
//            } else {
//                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
//                Log.e("AmapErr", errText);
//                Toast.makeText(this, "定位失败", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
    public void returnLocation(String address, double latitude, double longitude) {
        showDialog();
        Intent intent = getIntent();
        intent.putExtra("address", address);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);


//        LogUtil.i("zjz", "发出去的位置 latitude:" + latitude);
//        LogUtil.i("zjz", "发出去的位置 longitude:" + longitude);

        setResult(RESULT_OK, intent);
        ActivityGetMyLocation.this.finish();
    }


    /**
     * 初始化POI搜索
     *
     * @param amapLocation
     */
    private void initPoiAround(BDLocation amapLocation) {
        String location = amapLocation.getProvince() + amapLocation.getCity() + amapLocation.getDistrict() + amapLocation.getStreet();
        if (amapLocation.getAddress().country!=null){
            headerStreet.setText(amapLocation.getAddress().address);
            listPoi.addHeaderView(headerView);
        }else{
//            LogUtil.i("locationbaidu","----*** 自己接口查询" );
            showDialog("请稍后...");
            HttpRequest.sendGet(TLUrl.getInstance().URL_vip_base+"/finance/GeoServlet", "lng=" + amapLocation.getLongitude() + "&lat=" + amapLocation.getLatitude(), new HttpRevMsg() {
                @Override
                public void revMsg(String msg) {
                    try {
                        hideDialog();
//                        LogUtil.i("locationbaidu","----***msg:"+msg);
                        JSONObject ojb= new JSONObject(msg).getJSONObject("msg").getJSONObject("baidu").getJSONObject("result");

//                        LogUtil.i("locationbaidu","----***"+ojb.optString("country")+"city:"+ojb.optString("city"));
                        currentAddress = ojb.optString("country")+" "+ojb.optString("city");
                        headerStreet.setText(currentAddress);

                        listPoi.addHeaderView(headerView);

                    } catch (JSONException e) {
//                        LogUtil.i("locationbaidu","----***"+e.toString());
                        e.printStackTrace();
                    }
                }

//                @Override
//                public void failed(String msg) {
//
//                }
            },20000);

        }
        if (adapter==null){
            adapter = new com.abcs.sociax.t4.adapter.PoiAdapter(this, list);
            listPoi.setAdapter(adapter);
        }




        searchNeayBy(amapLocation);
    }


    private void searchNeayBy(BDLocation amapLocation) {
        PoiNearbySearchOption option = new PoiNearbySearchOption();

        option.keyword(amapLocation.getAddress().address==null?"":amapLocation.getAddress().address);
        option.sortType(PoiSortType.distance_from_near_to_far);
        option.location(new LatLng(currentLatitude, currentLongitude));
        option.radius(30000);

        option.pageCapacity(20);
        mPoiSearch.searchNearby(option);




    }

    /**
     * 进行poi查询
     *
     * @param location
     */
//    private void doSearchQuery(String city, LatLonPoint lp) {
//        query = new PoiSearch.Query("", "公司企业|道路附属设施|地名地址信息|公共设施", city);
//        query.setPageSize(10);
//        query.setPageNum(0);
//
//        poiSearch = new PoiSearch(this, query);
//        // 设置搜索区域为以lp点为圆心，其周围2000米范围
//        poiSearch.setBound(new PoiSearch.SearchBound(lp, 2000, true));
//        poiSearch.setOnPoiSearchListener(this);
//        poiSearch.searchPOIAsyn();  // 异步搜索
//    }

    /**
     * 激活定位
     *
     * @param listener
     */
//    @Override
//    public void activate(OnLocationChangedListener listener) {
//        mListener = listener;
//        if (mlocationClient == null) {
//            mlocationClient = new AMapLocationClient(this);
//            mLocationOption = new AMapLocationClientOption();
//            //设置定位监听
//            mlocationClient.setLocationListener(this);
//            //设置为高精度定位模式
//            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//            //设置需要地理位置信息
//            mLocationOption.isNeedAddress();
//            //设置定位参数
//            mlocationClient.setLocationOption(mLocationOption);
//            mLocationOption.setOnceLocation(true);
//            mlocationClient.startLocation();
//        }
//    }

    /**
     * 取消定位
     */
//    @Override
//    public void deactivate() {
//        mListener = null;
//        if (mlocationClient != null) {
//            mlocationClient.stopLocation();
//            mlocationClient.onDestroy();
//        }
//        mlocationClient = null;
//    }

//    @Override
//    public void onPoiSearched(PoiResult result, int rCode) {
//        if (rCode == 0) {
//            if (result != null && result.getQuery() != null) {// 搜索poi的结果
//                if (result.getQuery().equals(query)) {// 是否是同一条
//                    poiResult = result;
//                    // 取得第一页的poiitem数据，页数从数字0开始
//                    poiItems = poiResult.getPois();
//                    if (poiItems != null && poiItems.size() > 0) {
//                        Log.e("sn_", poiItems.toString());
//                        adapter = new PoiAdapter(this, poiItems);
//                        listPoi.setAdapter(adapter);
//                        listPoi.addHeaderView(headerView);
//                    }
//                }
//            }
//        }
//    }
//
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position > 0) {
            PoiInfo poiItem = adapter.getItem((int)id);
            returnLocation(poiItem.address ,poiItem.location.latitude,
                    poiItem.location.longitude);
        }
    }
}

