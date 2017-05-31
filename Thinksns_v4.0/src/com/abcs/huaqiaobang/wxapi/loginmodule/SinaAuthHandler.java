package com.abcs.huaqiaobang.wxapi.loginmodule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.loginmodule.imp.HttpCallbackListener;
import com.abcs.huaqiaobang.wxapi.loginmodule.imp.LogoutAPI;
import com.abcs.huaqiaobang.wxapi.loginmodule.qh.imp.QhLoginInterface;
import com.abcs.huaqiaobang.wxapi.official.share.util.AccessTokenKeeper;
import com.loopj.android.http.RequestParams;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.utils.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;


public class SinaAuthHandler {
	/** 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
	public static final String APP_KEY = "3059335908";
	/**
	 * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
	 * <p>
	 * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响， 但是没有定义将无法使用 SDK 认证登录。
	 * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
	 * 
	 * https://api.weibo.com/oauth2/default.html
	 * </p>
	 */
	// public static final String REDIRECT_URL =
	// "https://api.weibo.com/oauth2/default.html";
	public static final String REDIRECT_URL = TLUrl.getInstance().URL_user_base+"/api/login/sina";

	/**
	 * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
	 * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
	 * 
	 * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。
	 * 
	 * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
	 * 
	 * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
	 * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
	 */
	public static final String SCOPE = "all";

	public static final String TAG = "SinaAuthHandler";

	private AuthInfo mAuthInfo;
	private SsoHandler mSsoHandler;
	private Oauth2AccessToken mToken;
	private HttpCallbackListener callBack;
	private Activity activity;
	/** 登出操作对应的listener */
	private LogOutRequestListener mLogoutListener = new LogOutRequestListener();

	public SinaAuthHandler(Activity activity) {
		this.activity = activity;
		LogUtil.sIsLogEnable = true;

		// 初始化授权的配置信息
		mAuthInfo = new AuthInfo(activity, APP_KEY, REDIRECT_URL, SCOPE);
		mSsoHandler = new SsoHandler(activity, mAuthInfo);
	}

	public void login(Activity activity,
			HttpCallbackListener httpCallbackListener) {
		this.callBack = httpCallbackListener;
		mSsoHandler.authorize(new AuthListener());
	}

	public void logout() {
		mToken = new Oauth2AccessToken();
		new LogoutAPI(activity, APP_KEY,
				AccessTokenKeeper.readAccessToken(activity))
				.logout(mLogoutListener);
	}

	// 应用调用Andriod_SDK接口时，如果要成功接收到回调，需要在调用接口的Activity的onActivityResult方法调用该方法
	public void callOnActivityResult(int requestCode, int resultCode,
			Intent data) {
		// SSO 授权回调
		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	/**
	 * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #} 中调用
	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
	 * SharedPreferences 中。
	 */
	class AuthListener implements WeiboAuthListener {
		@Override
		public void onComplete(Bundle values) {
			// 从 Bundle 中解析 Token
			mToken = Oauth2AccessToken.parseAccessToken(values);
			for (String str : values.keySet()) {
				Log.e(TAG + "   str:", str);
			}
			if (mToken != null && mToken.isSessionValid()) {
				RequestParams requestParams = new RequestParams();
				requestParams.put("appid", APP_KEY);
				requestParams.put("token", mToken.getToken());
				QhLoginInterface.requestLoginCheck(
						QhLoginInterface.urlSinaLoginTokenForQihang, callBack,
						requestParams);
				AccessTokenKeeper.writeAccessToken(
						activity.getApplicationContext(), mToken);
				Util.token = mToken.getToken();
			} else {
				// 以下几种情况，您会收到 Code：
				// 1. 当您未在平台上注册的应用程序的包名与签名时；
				// 2. 当您注册的应用程序包名与签名不正确时；
				// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
				JSONObject jsonObject = new JSONObject();
				for (String str : values.keySet()) {
					try {
						jsonObject.put(str, values.get(str));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				callBack.onFinish(jsonObject.toString());
			}
		}

		@Override
		public void onCancel() {
			callBack.onCancel();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			callBack.onError(e.getMessage());
		}
	}

	/**
	 * 登出按钮的监听器，接收登出处理结果。（API 请求结果的监听器）
	 */
	private class LogOutRequestListener implements RequestListener {
		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				try {
					JSONObject obj = new JSONObject(response);
					String value = obj.getString("result");

					if ("true".equalsIgnoreCase(value)) {
						AccessTokenKeeper.clear(activity);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
		}
	}
}
