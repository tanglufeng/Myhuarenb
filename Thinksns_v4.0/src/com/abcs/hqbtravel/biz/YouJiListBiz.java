package com.abcs.hqbtravel.biz;

import android.content.Context;
import android.util.Log;

import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.entity.YouJi;
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
public class YouJiListBiz {


    /**
     * TAG.
     */
    public static final String TAG = "YouJiListBiz";

    private static volatile YouJiListBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private YouJiListBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static YouJiListBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (YouJiListBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new YouJiListBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    /***
     *
     * @param cityId
     * @param pageSize 可选
     * @param pageNo
     * @param responseListener
     * @param errorListener
     * @param tag
     */
    public void getYouJiList(String cityId,int pageSize,int pageNo,String keyWord,Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Contonst.HOST+"/note/get";//远程的地址
//        final String url = Contonst.LOCAL_HOST+"/note/get";  //本地的地址

//        HashMap<String, Object> params = new HashMap<>();
//        params.put("token", "123");
//        params.put("timestamp", System.currentTimeMillis() + "");
//        params.put("version", "1");
//        params.put("transactionid", "transactionxxxx");
        HashMap<String, Object> params = Contonst.getBisicParams();

        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("cityId", cityId);
        bodyParams.put("pageSize", pageSize+"");
        bodyParams.put("pageNo", pageNo+"");
        bodyParams.put("keyWord", keyWord);
        params.put("body", bodyParams);

        Gson gson = new Gson();
        final GsonRequest<YouJi> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                YouJi.class, responseListener, errorListener);
        Log.i("游记列表参数： ",gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
}
