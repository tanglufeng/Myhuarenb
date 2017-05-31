package com.abcs.huaqiaobang.wxapi.official.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.huaqiaobang.wxapi.official.share.util.ShareContent;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class ShareQQPlatform {
	private static final String Tag = "ShareQQPlatform";
	private static final String AppId = "1104982229";// 发布时，使用 1104538121
	// private static final String AppKey = "gMS6KrunziPaI8WI";

	private Tencent mTencent;
	private static final ShareQQPlatform shareQQ = new ShareQQPlatform();

	private ShareQQPlatform() {
	}

	public static ShareQQPlatform getInstance() {
		return shareQQ;
	}

	/**
	 * 在 activity 的 oncreate 方法中调用（在 初始化 view 之前，即 setContentView 之前）
	 * 
	 * @param context
	 */
	public void registerShare(Context context) {
		// Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI,其中APP_ID是分配给第三方应用的appid，类型为String。
		mTencent = Tencent.createInstance(AppId, context);

	}

	/**
	 * 那个 activity 调用该方法，就传入那个 activity
	 * 
	 * @param act
	 */
	public void share(Activity act) {
		Bundle bundle = new Bundle();

		// 这条分享消息被好友点击后的跳转URL。
		bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, ShareContent.actionUrl);
		// 分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_ SUMMARY不能全为空，最少必须有一个是有值的。
		bundle.putString(QQShare.SHARE_TO_QQ_TITLE, ShareContent.title);
		// 分享的图片URL
		bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, ShareContent.imgQQ_Url);
		// 分享的消息摘要，最长50个字
		bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, ShareContent.content);
		// 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
		bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, ShareContent.appName);

		mTencent.shareToQQ(act, bundle, uiListener);
	}

	public void share(Activity act, String url, String title, String imgUrl,
			String content, String appName) {
		Bundle bundle = new Bundle();
		System.out.println("QQPlatform-url:"+url);
		// 这条分享消息被好友点击后的跳转URL。
		bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
		// 分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_ SUMMARY不能全为空，最少必须有一个是有值的。
		bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
		// 分享的图片URL
		bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
		// 分享的消息摘要，最长50个字
		bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
		// 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
		bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName);

		mTencent.shareToQQ(act, bundle, uiListener);
	}

	/**
	 * 如果要成功接收到回调，需要在调用接口的Activity的onActivityResult方法中调用 该方法
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void callOnActivityResult(int requestCode, int resultCode,
			Intent data) {
		mTencent.onActivityResult(requestCode, resultCode, data);
	}

	private IUiListener uiListener = new IUiListener() {
		@Override
		public void onCancel() {
			LogUtil.e(Tag, "------onCancel-------- 取消qq分享");
		}

		@Override
		public void onComplete(Object arg0) {
			LogUtil.e(Tag, "------onComplete-------- 分享成功" + arg0.toString());
		}

		@Override
		public void onError(UiError e) {
			LogUtil.e(Tag, "------onComplete-------- 分享失败  code:" + e.errorCode
					+ ", msg:" + e.errorMessage + ", detail:" + e.errorDetail);
		}
	};
}
