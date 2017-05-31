package com.abcs.huaqiaobang.wxapi.loginmodule;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.abcs.huaqiaobang.wxapi.loginmodule.imp.HttpCallbackListener;
import com.abcs.huaqiaobang.wxapi.loginmodule.qh.imp.QhLoginInterface;
import com.loopj.android.http.RequestParams;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TencentAuthHandler {
    private final static String TAG = "TencentAuthHandler";

    private String APP_ID = "1104982229";
    // private String APP_ID = "1104890931";图灵的
    private String Scope = "all";// 应用需要获得哪些API的权限，由“，”分隔。 例如：SCOPE = “get_user_info,add_t,get_user_info”；所有权限用“all”

    private Activity activity;
    private Tencent mTencentHandler;
    private IUiListener listenerLoginTencent;// qq登录成功的回调
    private HttpCallbackListener callBack; // 客户端回调

    public TencentAuthHandler(Activity activity) {
        this.activity = activity;
        this.listenerLoginTencent = new BaseUiListener() {
            @Override
            protected void doComplete(JSONObject jaObj) {
                initOpenidAndToken(jaObj);
                getUserInfo(jaObj);
            }
        };

        createTencentHandler();
    }

    // 设置额外的配置
    private void initOpenidAndToken(JSONObject jaObj) {
        try {
            String token = jaObj.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jaObj.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jaObj.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                mTencentHandler.setAccessToken(token, expires);
                mTencentHandler.setOpenId(openId);
//				Util.token = token;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。 其中APP_ID是分配给第三方应用的appid，类型为String。
    private void createTencentHandler() {
        mTencentHandler = Tencent.createInstance(APP_ID, activity);
    }

    // 登录
    public void login(Activity activity, HttpCallbackListener callBack) {
        if (mTencentHandler == null)
            createTencentHandler();
        // 如果回话是无效的,则登录
        if (!mTencentHandler.isSessionValid()) {
            this.callBack = callBack;
            mTencentHandler.login(activity, Scope, listenerLoginTencent); // lUiListener 回调API，IUiListener实例。
            return;
        }
        // 注销
        logout(activity);
        this.login(activity, callBack);// 回调
    }

    // 注销
    public void logout(Activity activity) {
        mTencentHandler.logout(activity);
    }

    private void getUserInfo(final JSONObject jaObj) {
        if (mTencentHandler != null && mTencentHandler.isSessionValid()) {
            IUiListener listener = new BaseUiListener() {
                @Override
                protected void doComplete(JSONObject infoObject) {
                    try {
                        RequestParams requestParams = spliceRequestParams(jaObj, infoObject); // 组装 url 参数
                        QhLoginInterface.requestLoginCheck(QhLoginInterface.urlTencentLoginTokenForQihang, callBack, requestParams);// 调用启航的登录验证接口
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            // 获取用户信息
            UserInfo info = new UserInfo(activity, mTencentHandler.getQQToken());
            info.getUserInfo(listener);
        }
    }

    // 组装 url 参数:把 登录成功返回的全部字段和获取的用户信息的全部字段都拼装在 RequestParams中，用于后面向启航服务器发送请求，进行用户授权(允许qq用户)
    private RequestParams spliceRequestParams(JSONObject values, JSONObject infoObject) throws JSONException {
        RequestParams requestParams = new RequestParams();
        JSONArray names = values.names();
        for (int i = 0; i < names.length(); i++) {
            String keyString = names.getString(i);
            requestParams.put(keyString, values.get(keyString));
        }
        JSONArray names2 = infoObject.names();
        for (int i = 0; i < names2.length(); i++) {
            String keyString = names2.getString(i);
            requestParams.put(keyString, infoObject.get(keyString));
        }
        requestParams.put("appid", APP_ID);
        return requestParams;
    }

    public void callOnActivityResult(int requestCode, int resultCode, Intent data) {
        // 解决在某些低端机上调用登录后，由于内存紧张导致APP被系统回收，登录成功后无法成功回传数据。
        if (requestCode == Constants.REQUEST_LOGIN)
            Tencent.handleResultData(data, listenerLoginTencent);

        // 应用调用Andriod_SDK接口时，如果要成功接收到回调，需要在调用接口的Activity的onActivityResult方法调用该方法
        mTencentHandler.onActivityResult(requestCode, resultCode, data);
    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                callBack.onError("登录失败");
                // 登录失败
                return;
            }
            doComplete(jsonResponse);
        }

        protected void doComplete(JSONObject values) {
        }

        @Override
        public void onError(UiError e) {
            callBack.onError(e.errorMessage);
        }

        @Override
        public void onCancel() {
            callBack.onCancel();
        }
    }

}
