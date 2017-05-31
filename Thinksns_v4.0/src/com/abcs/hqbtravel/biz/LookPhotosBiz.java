package com.abcs.hqbtravel.biz;

import android.content.Context;
import android.util.Log;

import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.entity.LookPhotos;
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
public class LookPhotosBiz {


    /**
     * TAG.
     */
    public static final String TAG = "LookPhotosBiz";

    private static volatile LookPhotosBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private LookPhotosBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static LookPhotosBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (LookPhotosBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new LookPhotosBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取城市banner图片
     * @param cityId
     * @param responseListener
     * @param errorListener
     * @param tag
     */
    public void getPhotosList(String cityId,Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Contonst.HOST+"/index/getBanners/";

//        HashMap<String, Object> params = new HashMap<>();
//        params.put("token", "123");
//        params.put("timestamp", System.currentTimeMillis() + "");
//        params.put("version", "1");
//        params.put("transactionid", "transactionxxxx");
        HashMap<String, Object> params = Contonst.getBisicParams();

        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("cityId", cityId);
        params.put("body", bodyParams);

        Gson gson = new Gson();
        final GsonRequest<LookPhotos> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                LookPhotos.class, responseListener, errorListener);
        Log.i("获取图片列表参数： ",gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
}
