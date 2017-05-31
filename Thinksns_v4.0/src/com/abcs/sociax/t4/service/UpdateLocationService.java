package com.abcs.sociax.t4.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.util.Pair;

//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
import com.abcs.haiwaigou.local.beans.LocateState;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.t4.android.Thinksns;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.thinksns.sociax.thinksnsbase.exception.ApiException;

/**
 * 类说明：
 *
 * @author Zoey
 * @version 1.0
 * @date 2015年10月21日
 */
public class UpdateLocationService extends Service {
//        implements AMapLocationListener {

    private static final int UPDATE_TIME = 30 * 60 * 1000;    // 半个小时定位一次并上传服务器

//    private AMapLocationClient locationClient = null;
//    private AMapLocationClientOption locationOption = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLocation();
    }

    //    // 初始化位置
//    public void initLocation() {
//        locationClient = new AMapLocationClient(getApplicationContext());
//        locationOption = new AMapLocationClientOption();
//        // 设置定位模式为高精度模式
//        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        locationOption.setInterval(UPDATE_TIME);
//        // 设置定位监听
//        locationClient.setLocationListener(this);
//
//        // 设置定位参数
//        locationClient.setLocationOption(locationOption);
//    }
    public LocationClient mLocationClient = null;
    private LocationClientOption option;

    private void initLocation() {
        mLocationClient = new LocationClient(this);     //声明LocationClient类
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                StringBuffer sb = new StringBuffer(256);
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                        Pair<String, String> l = Pair.create(
                                String.valueOf(location.getLatitude()),
                                String.valueOf(location.getLongitude()));
                        sendNowLocation(l);
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    Pair<String, String> l = Pair.create(
                            String.valueOf(location.getLatitude()),
                            String.valueOf(location.getLongitude()));
                    sendNowLocation(l);
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    Pair<String, String> l = Pair.create(
                            String.valueOf(location.getLatitude()),
                            String.valueOf(location.getLongitude()));
                    sendNowLocation(l);
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
            }
        });    //注册监听函数
        option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setCoorType("wgs84"); // 设置坐标类型
        option.setIsNeedAddress(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 启动定位
//        locationClient.startLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    // 发送当前位置
    public void sendNowLocation(final Pair<String, String> location) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Thinksns app = (Thinksns) getApplicationContext();
                try {
                    app.getFindPeopleApi().updateLocation(location.first, location.second);
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        if (null != mLocationClient) {
//            mLocationClient.onDestroy();
//            mLocationClient = null;
//            locationOption = null;
        }
        super.onDestroy();
    }

//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (aMapLocation != null) {
//            if (aMapLocation.getErrorCode() == 0) {
//                Pair<String, String> location = Pair.create(
//                        String.valueOf(aMapLocation.getLatitude()),
//                        String.valueOf(aMapLocation.getLongitude()));
//                sendNowLocation(location);
//            }
//        }
//    }
}
