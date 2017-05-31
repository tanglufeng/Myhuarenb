package com.abcs.hqbtravel.biz;

import android.content.Context;
import android.util.Log;

import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.entity.FindByTag;
import com.abcs.hqbtravel.net.GsonRequest;
import com.abcs.hqbtravel.net.RequestManager;
import com.android.volley.Response;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static com.abcs.haiwaigou.activity.CheckOrderActivity.params;

/**
 */
public class FindByTagBiz {


    /**
     * TAG.
     */
    public static final String TAG = "FindByTagBiz";

    private static volatile FindByTagBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private FindByTagBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static FindByTagBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (FindByTagBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new FindByTagBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

//    "pageSize":"10","cityId":"50","pageNo":"1","keyWord":""


    public void getList(String cityId,int pageNo,String pageSize,String keyWord,Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

//        final String url = "http://192.168.0.16:7075/restaurant/get";
        final String url = Contonst.HOST+"/find/findListByKey";
//        final String url = "http://192.168.0.16:7076/turistattraction/get";

//        HashMap<String, Object> params = new HashMap<>();
//        params.put("token", "123");
//        params.put("timestamp", System.currentTimeMillis() + "");
//        params.put("version", "1");
//        params.put("transactionid", "transactionxxxx");
        HashMap<String, Object> params = Contonst.getBisicParams();

        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("cityId", cityId);
        bodyParams.put("pageNo", pageNo+"");
        bodyParams.put("pageSize", pageSize);
        bodyParams.put("keyWord", keyWord);
        params.put("body", bodyParams);

        Gson gson = new Gson();
        final GsonRequest<FindByTag> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                FindByTag.class, responseListener, errorListener);
        Log.i("搜索byTag参数： ",gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
}
