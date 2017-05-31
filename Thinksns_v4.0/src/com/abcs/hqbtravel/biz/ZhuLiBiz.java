package com.abcs.hqbtravel.biz;

import android.content.Context;
import android.util.Log;

import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.entity.ZhuLi;
import com.abcs.hqbtravel.net.GsonRequest;
import com.abcs.hqbtravel.net.RequestManager;
import com.android.volley.Response;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/9.
 */
public class ZhuLiBiz {


    /**
     * TAG.
     */
    public static final String TAG = "ZhuLiBiz";

    private static volatile ZhuLiBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private ZhuLiBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static ZhuLiBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (ZhuLiBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new ZhuLiBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    public void getZhuLiList(String cityId,int pageNo,Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Contonst.HOST+"/assistant/get";
//        final String url = "http://192.168.0.16:7076/assistant/get";

//        HashMap<String, Object> params = new HashMap<>();
//        params.put("token", "123");
//        params.put("timestamp", System.currentTimeMillis() + "");
//        params.put("version", "1");
//        params.put("transactionid", "transactionxxxx");
        HashMap<String, Object> params = Contonst.getBisicParams();

        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("cityId", cityId);
        bodyParams.put("categoryId", "151");
        bodyParams.put("pageNo", pageNo+"");
        params.put("body", bodyParams);

        Gson gson = new Gson();
        final GsonRequest<ZhuLi> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                ZhuLi.class, responseListener, errorListener);
        Log.i("助理参数： ",gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
}
