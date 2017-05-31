package com.abcs.hqbtravel.biz;

import android.content.Context;
import android.util.Log;

import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.entity.CityEntry;
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
public class GetCityBiz {


    /**
     * TAG.
     */
    public static final String TAG = "GetCityBiz";

    private static volatile GetCityBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private GetCityBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static GetCityBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (GetCityBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new GetCityBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    public void getCityList(int countryId,Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Contonst.HOST+"/find/city";

//        HashMap<String, Object> params = new HashMap<>();
//        params.put("token", "123");
//        params.put("timestamp", System.currentTimeMillis() + "");
//        params.put("version", "1");
//        params.put("transactionid", "transactionxxxx");
        HashMap<String, Object> params = Contonst.getBisicParams();

        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("countryId", countryId+"");
        params.put("body", bodyParams);

        Gson gson = new Gson();
        final GsonRequest<CityEntry> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                CityEntry.class, responseListener, errorListener);
        Log.i("获取城市参数： ",gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
}
