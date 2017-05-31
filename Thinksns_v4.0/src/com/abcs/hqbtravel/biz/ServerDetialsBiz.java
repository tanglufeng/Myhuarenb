package com.abcs.hqbtravel.biz;

import android.content.Context;
import android.util.Log;

import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.entity.ServerDetail;
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
public class ServerDetialsBiz {


    /**
     * TAG.
     */
    public static final String TAG = "ServerDetialsBiz";

    private static volatile ServerDetialsBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private ServerDetialsBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static ServerDetialsBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (ServerDetialsBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new ServerDetialsBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    public void getServerDetials(String serverId,Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Contonst.HOST+"/servicedetail/get";
//        final String url = "http://192.168.0.16:7076/servicedetail/get";

//        HashMap<String, Object> params = new HashMap<>();
//        params.put("token", "123");
//        params.put("timestamp", System.currentTimeMillis() + "");
//        params.put("version", "1");
//        params.put("transactionid", "transactionxxxx");
        HashMap<String, Object> params = Contonst.getBisicParams();

        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("serviceId", serverId);
//        bodyParams.put("serviceId", "0");
        params.put("body", bodyParams);

        Gson gson = new Gson();
        final GsonRequest<ServerDetail> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                ServerDetail.class, responseListener, errorListener);
        Log.i("服务详情参数： ",gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
}
