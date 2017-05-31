package com.abcs.hqbtravel.biz;

import android.content.Context;
import android.util.Log;

import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.entity.FujinSanBi;
import com.abcs.hqbtravel.net.GsonRequest;
import com.abcs.hqbtravel.net.RequestManager;
import com.android.volley.Response;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/9.
 */
public class getFuJinSanBiBiz {


    /**
     * TAG.
     */
    public static final String TAG = "getFuJinSanBiBiz";

    private static volatile getFuJinSanBiBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private getFuJinSanBiBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static getFuJinSanBiBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (getFuJinSanBiBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new getFuJinSanBiBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    /***
     * 获取附近三必

     * @param cityId
     * @param pageNo
     * @param lng
     * @param lat
     * @param responseListener
     * @param errorListener
     * @param tag
     */

    public void getFuJinSanBiList(String cityId,int pageNo,String lng,String lat,Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Contonst.HOST+"/index/getNearByList";

        HashMap<String, Object> params = Contonst.getBisicParams();
//        params.put("token", "123");
//        params.put("timestamp", System.currentTimeMillis() + "");
//        params.put("version", "1");
//        params.put("transactionid", "transactionxxxx");

        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("cityId", cityId);
        bodyParams.put("pageNo", pageNo+"");
        bodyParams.put("lng", lng);
        bodyParams.put("lat", lat);
        params.put("body", bodyParams);

        Gson gson = new Gson();
        final GsonRequest<FujinSanBi> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                FujinSanBi.class, responseListener, errorListener);
        Log.i("获取附近三必参数： ",gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
}
