package com.abcs.hqbtravel.biz;

import android.content.Context;
import android.util.Log;

import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.entity.YouJiDetials;
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
public class YouJiDetialsBiz {


    /**
     * TAG.
     */
    public static final String TAG = "YouJiDetialsBiz";

    private static volatile YouJiDetialsBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private YouJiDetialsBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static YouJiDetialsBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (YouJiDetialsBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new YouJiDetialsBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    /***
     *
     * @param youjiId
     * @param responseListener
     * @param errorListener
     * @param tag
     */
    public void getYouJiDetials(String youjiId,Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Contonst.HOST+"/travelnotedetail/get";

//        HashMap<String, Object> params = new HashMap<>();
//        params.put("token", "123");
//        params.put("timestamp", System.currentTimeMillis() + "");
//        params.put("version", "1");
//        params.put("transactionid", "transactionxxxx");
        HashMap<String, Object> params = Contonst.getBisicParams();

        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("id", youjiId);
        params.put("body", bodyParams);

        Gson gson = new Gson();
        final GsonRequest<YouJiDetials> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                YouJiDetials.class, responseListener, errorListener);
        Log.i("游记详情参数： ",gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
}
