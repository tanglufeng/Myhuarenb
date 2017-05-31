package com.abcs.hqbtravel.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.sociax.android.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.CameraUpdateFactory;
//import com.amap.api.maps.LocationSource;
//import com.amap.api.maps.MapView;
//import com.amap.api.maps.model.BitmapDescriptorFactory;
//import com.amap.api.maps.model.LatLng;
//import com.amap.api.maps.model.MarkerOptions;
//import com.amap.api.maps.model.MyLocationStyle;

public class BaiDuMapActivity extends BaseActivity implements  View.OnClickListener{
//        ,AMap.OnMapLoadedListener, LocationSource, AMapLocationListener {

    private ImageView img_back;
    private TextView tv_title;
    private MapView mMapView;
    private BaiduMap baiduMap;
    private String lng;
    private String lat;
    private String title;

    //地图首次坐标定位
    private boolean isFirst = true;
    private double latitude, longitude; //定位详细经纬度
    private String address = "";    //定位详细地址
    private boolean isLocate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_bai_du_map);

        lng=getIntent().getStringExtra("lng");
        lat=getIntent().getStringExtra("lat");
        title=getIntent().getStringExtra("title");
        img_back=(ImageView)findViewById(R.id.img_back);
        tv_title=(TextView)findViewById(R.id.tv_title);

        if(!TextUtils.isEmpty(title)){
            tv_title.setText(title);
        }else {
            tv_title.setText("位置");
        }
        img_back.setOnClickListener(this);
        mMapView = (MapView) findViewById(R.id.mapView);
        baiduMap = mMapView.getMap();

//        mapView.onCreate(savedInstanceState);
        if(!TextUtils.isEmpty(lng)&&!TextUtils.isEmpty(lat)){
            Log.i("lng:", lng);
            Log.i("lat:", lat);
            latitude = Double.parseDouble(lat);
            longitude = Double.parseDouble(lng);
            isLocate = latitude != 0;
//            initMap();
//                    LatLng point = new LatLng(39.963175, 146.400244);
//            构建Marker图标
//
            LatLng point = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
            MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(point, 16);
            baiduMap.setMapStatus(mapStatus);

            addcurrent(MyApplication.my_current_lng, MyApplication.my_current_lat);

//
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.img_dibiao);
//            构建MarkerOption，用于在地图上添加Marker
            MarkerOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap).zIndex(0).period(16);
            option.animateType(MarkerOptions.MarkerAnimateType.grow);
//            在地图上添加Marker，并显示
            baiduMap.addOverlay(option);
        }
    }
    private void  addcurrent(String curr_lng,String curr_lat){

        if(!TextUtils.isEmpty(curr_lng)&&!TextUtils.isEmpty(curr_lat)){
            LatLng point = new LatLng(Double.valueOf(curr_lat), Double.valueOf(curr_lng));
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.my_curre);
            //            构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //            在地图上添加Marker，并显示
            baiduMap.addOverlay(option);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
//        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
//        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
//        mapView.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }

//    private void initMap() {
//        if (aMap == null) {
//            aMap = mapView.getMap();
//
//            setUpMap();
//        }
//    }
//
//    /**
//     * 设置一些amap的属性
//     */
//    private void setUpMap() {
//        if (isLocate) {
//            addMarkersToMap();
//        } else{
//            showToast("位置获取失败！定位到当前位置！");
//            setMyLocation();
//        }
//        aMap.setOnMapLoadedListener(this);
//    }

//    private void setMyLocation() {
//        // 自定义系统定位小蓝点
//        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
//                .fromResource(com.thinksns.tschat.R.drawable.redpin));// 设置小蓝点的图标
////        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
//        myLocationStyle.radiusFillColor(Color.argb(160, 0, 0, 180));// 设置圆形的填充颜色
//        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
//        myLocationStyle.strokeWidth(0f);// 设置圆形的边框粗细
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
//        aMap.setMyLocationStyle(myLocationStyle);
//        aMap.setLocationSource(this);// 设置定位监听
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
//        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//        // aMap.setMyLocationType()
//    }
//
//    private void addMarkersToMap() {
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
//        aMap.moveCamera(CameraUpdateFactory.changeLatLng(
//                new LatLng(latitude, longitude)));
////        CameraUpdateFactory.zoomTo(18);
//        //设置marker
//        MarkerOptions markerOption = new MarkerOptions();
//        markerOption.position(new LatLng(latitude, longitude));
//        markerOption.title(address).snippet(address + ": " + latitude + ", " + longitude);
//        markerOption.icon(BitmapDescriptorFactory.fromResource(com.thinksns.tschat.R.drawable.redpin));
//        markerOption.draggable(true);
//        aMap.addMarker(markerOption);
//
//    }
//
//
//    @Override
//    public void onMapLoaded() {
//        CameraUpdateFactory.zoomTo(18);
//    }
//
//    @Override
//    public void activate(OnLocationChangedListener onLocationChangedListener) {
//        mListener = onLocationChangedListener;
//        if (mlocationClient == null) {
//            mlocationClient = new AMapLocationClient(this);
//            mLocationOption = new AMapLocationClientOption();
//            //设置定位监听
//            mlocationClient.setLocationListener(this);
//            //设置为高精度定位模式
//            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//            //设置定位参数
//            mlocationClient.setLocationOption(mLocationOption);
//            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
//            // 在定位结束后，在合适的生命周期调用onDestroy()方法
//            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//            mlocationClient.startLocation();
//        }
//    }
//
//    @Override
//    public void deactivate() {
//        mListener = null;
//        if (mlocationClient != null) {
//            mlocationClient.stopLocation();
//            mlocationClient.onDestroy();
//        }
//        mlocationClient = null;
//    }
//
//    @Override
//    public void onLocationChanged(AMapLocation amapLocation) {
//        if (mListener != null && amapLocation != null) {
//            if (amapLocation != null
//                    && amapLocation.getErrorCode() == 0) {
//                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
//                if (isFirst) {
//                    isFirst = false;
//                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(
//                            new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
//                    CameraUpdateFactory.zoomTo(18);
//
//                    latitude = amapLocation.getLatitude();
//                    longitude = amapLocation.getLongitude();
//                    address = amapLocation.getAddress();
//                    Log.v("zjz", "amap_当前定位结果:" + latitude + ", " + longitude + ", " + address);
//
//                }
////                tv_send.setEnabled(true);
////                tv_send.setAlpha(1.0f);
//            } else {
//                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
//                Log.e("AmapErr", errText);
//                Toast.makeText(this, "定位失败,请检查是否打开手机定位功能", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}
