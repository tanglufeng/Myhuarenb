package com.thinksns.tschat.map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.thinksns.tschat.R;
import com.thinksns.tschat.constant.TSConfig;
import com.thinksns.tschat.widget.SmallDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 */
public class ActivityLocation2 extends Activity implements  View.OnClickListener {

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;

    MapView mMapView;
    BaiduMap mBaiduMap;
    boolean isFirstLoc = true; // 是否首次定位
    private double latitude, longitude; //定位详细经纬度
    private String address = "";    //定位详细地址
    private int room_id;
    private boolean shareLocation = true;

    private ImageView iv_back;
    private TextView tv_send, tv_title;
    private SmallDialog dialog;
    private LinearLayout ll_location_info;
    private TextView tv_address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       setContentView(R.layout.locationsource_activity);

        mCurrentMode = LocationMode.NORMAL;
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        mBaiduMap .setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));


        initIntent();
        initMap();
        initView();


        dialog = new SmallDialog(this, "请稍后...");
        dialog.setCanceledOnTouchOutside(false);

    }

    private void initView() {
        tv_send = (TextView)findViewById(R.id.tv_send);
//        tv_send.setEnabled(false);
//        tv_send.setAlpha(0.5f);
        tv_send.setOnClickListener(this);

        iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        ll_location_info = (LinearLayout)findViewById(R.id.ll_location_info);
        tv_address = (TextView)findViewById(R.id.tv_address);

        tv_title = (TextView)findViewById(R.id.tv_title_center);
        if(!shareLocation) {
            tv_title.setText("查看位置");
            ll_location_info.setVisibility(View.VISIBLE);
            tv_address.setText(address);
            tv_send.setVisibility(View.GONE);
        }
    }

    private void initIntent() {
        room_id = getIntent().getIntExtra("room_id", -1);
        latitude = Double.parseDouble(getIntent().getStringExtra("latitude"));
        Log.i("zjz","getLatitude="+latitude);
        if (latitude == 0) {
            shareLocation = true;
        } else {
            longitude = Double.parseDouble(getIntent().getStringExtra("longitude"));
            Log.i("zjz","getLongitude="+longitude);
            address = getIntent().getStringExtra("address");
            shareLocation = false;
        }

    }

    public void initMap(){


        if (shareLocation){
            // 开启定位图层
            mBaiduMap.setMyLocationEnabled(true);
            // 定位初始化
            mLocClient = new LocationClient(this);
            mLocClient.registerLocationListener(myListener);
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true); // 打开gps
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setScanSpan(1000);
            mLocClient.setLocOption(option);
            mLocClient.start();
        }else{
            MyLocationData locData = new MyLocationData.Builder()
                 .accuracy(50)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(latitude)
                    .longitude(longitude).build();
            mBaiduMap.setMyLocationData(locData);
            mBaiduMap.setMyLocationEnabled(true);
            mBaiduMap
                    .setMyLocationConfigeration(new MyLocationConfiguration(
                            mCurrentMode, true, null));
                LatLng ll = new LatLng(latitude,
                        longitude);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        }


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

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            latitude = location.getLatitude();
            longitude = location.getLongitude();
            address = location.getAddress().toString();

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        if (mLocClient!=null){
            mLocClient.stop();
        }

        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

        @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.tv_send) {

            Log.e("zds","baidujietu===");
            //获取地图截图并发送
            mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
                @Override
                public void onSnapshotReady(Bitmap bitmap) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    if(null == bitmap){
                        dialog.setContent("获取位置截图数据失败");
                        tv_send.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                tv_send.setEnabled(false);
                            }
                        }, 1000);
                        return;
                    }

                    String error = "";
                    try {
                        String path = Environment.getExternalStorageDirectory() + "/" + TSConfig.CACHE_PATH ;
                        File file = new File(path);
                        if(!file.exists()) {
                            //创建目录
                            file.mkdirs();
                        }

                        path = path + "/location_" + sdf.format(new Date()) + ".png";
                        FileOutputStream fos = new FileOutputStream(path);
                        boolean b = bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
                        try {
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(b) {
                            //发送位置
                            if(!address.isEmpty()) {
                                Intent intent = new Intent();
                                intent.putExtra("latitude", latitude);
                                intent.putExtra("longitude", longitude);
                                intent.putExtra("location", address);
                                intent.putExtra("path", path);
                                setResult(RESULT_OK, intent);
                                finish();
                                return;
                            }else {
                                error = "获取位置信息失败,请重新定位";
                            }
                        }
                    }catch (FileNotFoundException e) {
                        e.printStackTrace();
                        error = "保存位置文件路径错误";
                    }

                    dialog.setContent(error);
                    tv_send.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            tv_send.setEnabled(true);
                        }
                    }, 1000);
                }
            });
            //刷新地图

            v.setEnabled(false);
        }else if(id == R.id.iv_back) {
            finish();
        }
    }

}
