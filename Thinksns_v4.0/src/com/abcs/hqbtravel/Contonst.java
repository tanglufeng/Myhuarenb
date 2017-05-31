package com.abcs.hqbtravel;

import com.abcs.huaqiaobang.MyApplication;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/9.
 */
public class Contonst {


    
    /*****远程地址*******/
    public static final String   HOST= TLUrl.getInstance().getUrl()+"/travel";

//    public static final String   HOST="http://120.24.19.29:7075";
    /*****本地地址*******/
    public static final String   LOCAL_HOST="http://192.168.0.18:7075";
    /*****切换城市*******/
    public static final String   LOCAL_CHANGECITY=TLUrl.getInstance().getUrl()+"/travel/find/getCountry";


    public static final String KEY_CHI = "CHI";
    public static final String KEY_WAN = "WAN";
    public static final String KEY_MAI = "MAI";


    public static HashMap<String, Object> getBisicParams(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", "123");
        params.put("timestamp", System.currentTimeMillis() + "");
        params.put("version", "1");
        params.put("transactionid", "transactionxxxx");

//        http://fa.163.com/interfaces/queryPopupImgForFresher.do?apiLevel=13&channel=appstore&deviceId=09E5A8ED-4D44-4981-B09F-851CDAC58F2F&deviceType=iphone&productVersion=2.20&systemName=iPhone%20OS&systemVersion=9.3&uniqueId=09E5A8ED-4D44-4981-B09F-851CDAC58F2F


        params.put("apiLevel", MyApplication.getAPILevel()+"");
        params.put("channel", MyApplication.getChannel()+"");
        params.put("deviceId", MyApplication.getDeviceId()+"");
        params.put("deviceType", MyApplication.getDeviceType()+"");
        params.put("productVersion", MyApplication.getProductVersion()+"");
        params.put("systemVersion", MyApplication.getSystemVersion()+"");
        params.put("uniqueId", MyApplication.getDeviceId()+"");

        return params;
    }

}
