package com.abcs.hqbtravel.biz;

import android.content.Context;
import android.util.Log;

import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.entity.BiMai;
import com.abcs.hqbtravel.net.GsonRequest;
import com.abcs.hqbtravel.net.RequestManager;
import com.android.volley.Response;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/9.
 */
public class BiMaiBiz {


    /**
     * TAG.
     */
    public static final String TAG = "BiMaiBiz";

    private static volatile BiMaiBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private BiMaiBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static BiMaiBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (BiMaiBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new BiMaiBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    public void getBiMaiList(String cityId,int pageNo,String lng,String lat,Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

//        final String url = "http://192.168.0.16:7075/shop/get";
        final String url = Contonst.HOST+"/shop/get";
//        final String url = "http://192.168.0.16:7076/turistattraction/get";

        HashMap<String, Object> params = Contonst.getBisicParams();
//        HashMap<String, Object> params = new HashMap<>();
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
        final GsonRequest<BiMai> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                BiMai.class, responseListener, errorListener);
        Log.i("必买参数： ",gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
    public void getBiMaiList(String cityId,String type,int pageNo,String lng,String lat,Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

//        final String url = "http://192.168.0.16:7075/shop/get";
        final String url = Contonst.HOST+"/shop/get";
//        final String url = "http://192.168.0.16:7076/turistattraction/get";

        HashMap<String, Object> params = Contonst.getBisicParams();
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("token", "123");
//        params.put("timestamp", System.currentTimeMillis() + "");
//        params.put("version", "1");
//        params.put("transactionid", "transactionxxxx");

        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("cityId", cityId);
        bodyParams.put("type", type);
        bodyParams.put("pageNo", pageNo+"");
        bodyParams.put("lng", lng);
        bodyParams.put("lat", lat);
        params.put("body", bodyParams);

        Gson gson = new Gson();
        final GsonRequest<BiMai> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                BiMai.class, responseListener, errorListener);
        Log.i("必买参数： ",gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
    public void getBiMaiChildList(String cityId,String type_child_id,int pageNo,String lng,String lat,Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

//        final String url = "http://192.168.0.16:7075/shop/get";
        final String url = Contonst.HOST+"/shop/get";
//        final String url = "http://192.168.0.16:7076/turistattraction/get";

        HashMap<String, Object> params = Contonst.getBisicParams();
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("token", "123");
//        params.put("timestamp", System.currentTimeMillis() + "");
//        params.put("version", "1");
//        params.put("transactionid", "transactionxxxx");

        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("cityId", cityId);
        bodyParams.put("type_child_id", type_child_id);
        bodyParams.put("pageNo", pageNo+"");
        bodyParams.put("lng", lng);
        bodyParams.put("lat", lat);
        params.put("body", bodyParams);

        Gson gson = new Gson();
        final GsonRequest<BiMai> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                BiMai.class, responseListener, errorListener);
        Log.i("必买参数： ",gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
}
