package com.abcs.hqbtravel.biz;

import android.content.Context;

import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.entity.TravelResponse;
import com.abcs.hqbtravel.net.GsonRequest;
import com.abcs.hqbtravel.net.RequestManager;
import com.android.volley.Response;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/9.
 */
public class TravelBiz {


    /**
     * TAG.
     */
    public static final String TAG = "TravelBiz";

    private static volatile TravelBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private TravelBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static TravelBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (TravelBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new TravelBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    public void getTravel(String cityId,Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Contonst.HOST+"/index/get";
//        final String url = "http://192.168.0.16:7076/index/get";

//        HashMap<String, Object> params = new HashMap<>();
//        params.put("token", "123");
//        params.put("timestamp", System.currentTimeMillis() + "");
//        params.put("version", "1");
//        params.put("transactionid", "transactionxxxx");
        HashMap<String, Object> params = Contonst.getBisicParams();

        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("cityId", cityId);
//        bodyParams.put("cityId", "7823");
        bodyParams.put("categoryId", "151");
        params.put("body", bodyParams);

        Gson gson = new Gson();
        final GsonRequest<TravelResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                TravelResponse.class, responseListener, errorListener);
        sRequestManager.add(gsonRequest, tag);
    }
}
