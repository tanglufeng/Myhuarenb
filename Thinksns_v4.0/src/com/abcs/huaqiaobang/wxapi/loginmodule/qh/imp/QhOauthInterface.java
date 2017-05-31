package com.abcs.huaqiaobang.wxapi.loginmodule.qh.imp;

import android.content.SharedPreferences;
import android.util.Log;

import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.loginmodule.imp.HttpCallbackListener;
import com.abcs.huaqiaobang.wxapi.loginmodule.util.HttpUtils;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class QhOauthInterface {
    private static final String TAG = "QhOauthInterface";
    // qq 用户向启航请求登录授权
    public static final String urlAutoForQihang = "http://user.cavacn.com:3000/api/oauth?iou=gege";

    // 向启航服务器请求进行 qq 用户授权
    public static void requestOauthCheck(final HttpCallbackListener callBack, JSONObject jsonObject) {
        HttpCallbackListener callbackListener = new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.e(TAG + "   server check oauth response msg:", response);
                if (response != null) {
                    try {
                        JSONObject autoObject = new JSONObject(response);
                        if ("1".equals(autoObject.getString("code"))) {
                            callBack.onFinish(autoObject.getString("result"));
                        } else {
                            Log.e(TAG, response);
                            callBack.onError(response);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Exception exception) {
                callBack.onError(exception.getMessage());
            }
        };
        try {
            RequestParams params = new RequestParams();
            params.put("token", jsonObject.getString("result"));

            Util.token = jsonObject.getString("result");
            Log.e("QQtoken", Util.token);
            SharedPreferences.Editor edit = Util.preference.edit();
            edit.putString("logintype", "QQ");
            edit.putString("token", Util.token);
            edit.commit();
            HttpUtils.sendHttpRequestDoPost(QhOauthInterface.urlAutoForQihang, params, callbackListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
