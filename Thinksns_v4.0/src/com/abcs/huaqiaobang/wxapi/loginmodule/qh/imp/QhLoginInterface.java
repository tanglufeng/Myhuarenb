package com.abcs.huaqiaobang.wxapi.loginmodule.qh.imp;

import android.util.Log;

import com.abcs.huaqiaobang.wxapi.loginmodule.imp.HttpCallbackListener;
import com.abcs.huaqiaobang.wxapi.loginmodule.util.HttpUtils;
import com.loopj.android.http.RequestParams;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;


public class QhLoginInterface {
	private static final String TAG = "QhLoginInterface";

	// 发送 qq 登录的相关信息给启航服务器：token，openId，用户信息（昵称，头像）。。。。
	public static final String urlTencentLoginTokenForQihang = "http://user.cavacn.com:3000/api/login/qq?iou=m";
	public static final String urlSinaLoginTokenForQihang = TLUrl.getInstance().URL_user_base+"/api/login/sina?iou=m";

	public static void requestLoginCheck(String url, final HttpCallbackListener callBack, RequestParams requestParams) {
		HttpCallbackListener httpCallbackListener = new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					// 如果合法
					if ("1".equals(jsonObject.getString("code"))) {
						Log.e(TAG, jsonObject.toString());

						QhOauthInterface.requestOauthCheck(callBack, jsonObject);// 授权
					} else {
						Log.e(TAG, response);
						// 不合法
						callBack.onError(response);
					}
				} catch (JSONException e) {
					Log.e(TAG, e.getMessage());
					callBack.onError(e.getMessage());
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String exception) {
				super.onError(exception);
				callBack.onError(exception);
			}
		};
		// 发送 http 请求，检测 qq 登录的 token 是否合法
		HttpUtils.sendHttpRequestDoPost(url, requestParams, httpCallbackListener);
	}
}
