package com.abcs.hqbtravel.biz;

import android.content.Context;
import android.util.Log;

import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.entity.BiChiDetials;
import com.abcs.hqbtravel.net.GsonRequest;
import com.abcs.hqbtravel.net.RequestManager;
import com.android.volley.Response;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static com.abcs.haiwaigou.activity.CheckOrderActivity.params;

/**
 * Created by Administrator on 2016/9/9.
 */
public class SeaByTagDetialsBiz {


    /**
     * TAG.
     */
    public static final String TAG = "SeaByTagDetialsBiz";

    private static volatile SeaByTagDetialsBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private SeaByTagDetialsBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static SeaByTagDetialsBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (SeaByTagDetialsBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new SeaByTagDetialsBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    public void getDetialsList(String detailId,Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

//        final String url = "http://192.168.0.16:7075/restaurant/get";
        final String url = Contonst.HOST+"/find/findDetailByDetailId";
//        final String url = "http://192.168.0.16:7076/turistattraction/get";

//        HashMap<String, Object> params = new HashMap<>();
//        params.put("token", "123");
//        params.put("timestamp", System.currentTimeMillis() + "");
//        params.put("version", "1");
//        params.put("transactionid", "transactionxxxx");
        HashMap<String, Object> params = Contonst.getBisicParams();

        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("detailId", detailId);
        params.put("body", bodyParams);

        Gson gson = new Gson();
        final GsonRequest<BiChiDetials> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                BiChiDetials.class, responseListener, errorListener);
        Log.i("搜索详情参数： ",gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
}
