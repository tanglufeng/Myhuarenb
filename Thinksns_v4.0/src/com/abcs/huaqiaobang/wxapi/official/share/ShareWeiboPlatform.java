package com.abcs.huaqiaobang.wxapi.official.share;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Toast;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.wxapi.official.share.util.AccessTokenKeeper;
import com.abcs.huaqiaobang.wxapi.official.share.util.ShareContent;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.VoiceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;

public class ShareWeiboPlatform {

	private static final String AppKey = "3059335908";

	/**
	 * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
	 * 
	 * <p>
	 * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响， 但是没有定义将无法使用 SDK 认证登录。
	 * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
	 * </p>
	 * http://120.24.214.108:3000/api/login/sina
	 */
	public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html"; // http://www.sina.com

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
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write";

	/** 微博分享的接口实例 */
	private IWeiboShareAPI mWeiboShareAPI;

	private Activity act;

	private static final ShareWeiboPlatform shareWeibo = new ShareWeiboPlatform();

	private ShareWeiboPlatform() {
	}

	public static ShareWeiboPlatform getInstanse() {
		return shareWeibo;
	}

	public void regiesetShare(Activity act, Bundle savedInstanceState,
			IWeiboHandler.Response response) {
		// 创建微博 SDK 接口实例
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(act, AppKey);

		// 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
		// 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
		// NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
		mWeiboShareAPI.registerApp();

		// 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
		// 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
		// 使用handle 处理返回的 数据(分享情况：ok、error、canel) 。
		// handle 通知后，调用 IWeiboHandler.Response 类中的 onResponse 方法
		this.act = act;
		if (savedInstanceState != null) {
			mWeiboShareAPI.handleWeiboResponse(this.act.getIntent(), response);
		}
	}

	/**
	 * 第三方应用发送请求消息到微博，唤起微博分享界面。 注意：当
	 * {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
	 * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
	 */
	public void share(final Activity act) {
		this.act = act;
		sendMultiMessage(true, true, true, false, false, false);
	}

	public void share(final Activity act, String url, String title,
			String content) {
		this.act = act;
		// 1. 初始化微博的分享消息
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		TextObject textObject = new TextObject();
		textObject.text = title;
		weiboMessage.textObject = textObject;
		WebpageObject mediaObject = new WebpageObject();
		mediaObject.identify = Utility.generateGUID();
		mediaObject.title = title;
		mediaObject.description = content;

		// 设置 Bitmap 类型的图片到视频对象里
		mediaObject.setThumbImage(createBitmapDrawable().getBitmap());
		mediaObject.actionUrl = url;
		mediaObject.defaultText = ShareContent.defaultText;
		weiboMessage.mediaObject = mediaObject;

		// 2. 初始化从第三方到微博的消息请求
		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		// 用transaction唯一标识一个请求
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;

		// 3. 发送请求消息到微博，唤起微博分享界面
		AuthInfo authInfo = new AuthInfo(act, AppKey, REDIRECT_URL, SCOPE);
		Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(act.getApplicationContext());
		String token = "";
		if (accessToken != null) {
			token = accessToken.getToken();
		}
		mWeiboShareAPI.sendRequest(act, request, authInfo, token,
				new WeiboAuthListener() {

					@Override
					public void onWeiboException(WeiboException arg0) {
					}

					@Override
					public void onComplete(Bundle bundle) {
						Oauth2AccessToken newToken = Oauth2AccessToken
								.parseAccessToken(bundle);
						AccessTokenKeeper.writeAccessToken(act.getApplicationContext(), newToken);
						Toast.makeText(
								act.getApplicationContext(),
								"onAuthorizeComplete token = "
										+ newToken.getToken(),
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCancel() {
					}
				});

	}

	private void sendMultiMessage(boolean hasText, boolean hasImage,
			boolean hasWebpage, boolean hasMusic, boolean hasVideo,
			boolean hasVoice) {
		// 1. 初始化微博的分享消息
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		if (hasText) {
			weiboMessage.textObject = getTextObj();
		}

		if (hasImage) {
			weiboMessage.imageObject = getImageObj();
		}

		// 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
		if (hasWebpage) {
			weiboMessage.mediaObject = getWebpageObj();
		}
		// if (hasMusic) {
		// weiboMessage.mediaObject = getMusicObj();
		// }
		// if (hasVideo) {
		// weiboMessage.mediaObject = getVideoObj();
		// }
		// if (hasVoice) {
		// weiboMessage.mediaObject = getVoiceObj();
		// }

		// 2. 初始化从第三方到微博的消息请求
		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		// 用transaction唯一标识一个请求
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;

		// 3. 发送请求消息到微博，唤起微博分享界面
		AuthInfo authInfo = new AuthInfo(act, AppKey, REDIRECT_URL, SCOPE);
		Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(act);
		String token = "";
		if (accessToken != null) {
			token = accessToken.getToken();
		}
		mWeiboShareAPI.sendRequest(act, request, authInfo, token,
				new WeiboAuthListener() {

					@Override
					public void onWeiboException(WeiboException arg0) {
					}

					@Override
					public void onComplete(Bundle bundle) {
						Oauth2AccessToken newToken = Oauth2AccessToken
								.parseAccessToken(bundle);
						AccessTokenKeeper.writeAccessToken(act, newToken);
						Toast.makeText(
								act.getApplicationContext(),
								"onAuthorizeComplete token = "
										+ newToken.getToken(),
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCancel() {
					}
				});
	}

	public void callOnNewIntent(Intent intent, IWeiboHandler.Response response) {
		// 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数。
		// 使用handle 处理返回的 数据(分享情况：ok、error、canel) 。
		// handle 通知后，调用 IWeiboHandler.Response 类中的 onResponse 方法
		mWeiboShareAPI.handleWeiboResponse(intent, response);
	}

	/**
	 * 创建文本消息对象。
	 * 
	 * @return 文本消息对象。
	 */
	private TextObject getTextObj() {
		TextObject textObject = new TextObject();
		textObject.text = ShareContent.content;
		return textObject;
	}

	/**
	 * 创建图片消息对象。
	 * 
	 * @return 图片消息对象。
	 */
	private ImageObject getImageObj() {
		ImageObject imageObject = new ImageObject();
		BitmapDrawable bitmapDrawable = createBitmapDrawable();
		imageObject.setImageObject(bitmapDrawable.getBitmap());
		return imageObject;
	}

	@SuppressWarnings("deprecation")
	private BitmapDrawable createBitmapDrawable() {
		return new BitmapDrawable(act.getResources().getResourceName(R.drawable.ic_launcher));
	}

	/**
	 * 创建多媒体（网页）消息对象。
	 * 
	 * @return 多媒体（网页）消息对象。
	 */
	private WebpageObject getWebpageObj() {
		WebpageObject mediaObject = new WebpageObject();
		mediaObject.identify = Utility.generateGUID();
		mediaObject.title = ShareContent.title;
		mediaObject.description = "网页描述：" + ShareContent.content;

		// 设置 Bitmap 类型的图片到视频对象里
		mediaObject.setThumbImage(createBitmapDrawable().getBitmap());
		mediaObject.actionUrl = ShareContent.actionUrl;
		mediaObject.defaultText = ShareContent.defaultText;
		return mediaObject;
	}

	/**
	 * 创建多媒体（音乐）消息对象。
	 * 
	 * @return 多媒体（音乐）消息对象。
	 */
	private MusicObject getMusicObj() {
		// 创建媒体消息
		MusicObject musicObject = new MusicObject();
		musicObject.identify = Utility.generateGUID();
		musicObject.title = "分享音乐";
		musicObject.description = "音乐描述：图灵金融音乐，超乎你想象";

		// 设置 Bitmap 类型的图片到视频对象里
		musicObject.setThumbImage(createBitmapDrawable().getBitmap());
		musicObject.actionUrl = "http://www.ttsss.cn/";
		musicObject.dataUrl = "www.weibo.com";
		musicObject.dataHdUrl = "www.weibo.com";
		musicObject.duration = 10;
		musicObject.defaultText = ShareContent.defaultText;
		return musicObject;
	}

	/**
	 * 创建多媒体（视频）消息对象。
	 * 
	 * @return 多媒体（视频）消息对象。
	 */
	private VideoObject getVideoObj() {
		// 创建媒体消息
		VideoObject videoObject = new VideoObject();
		videoObject.identify = Utility.generateGUID();
		videoObject.title = "分享视频";
		videoObject.description = "视频描述：图灵金融视频。呦呦切克闹";

		// 设置 Bitmap 类型的图片到视频对象里
		videoObject.setThumbImage(createBitmapDrawable().getBitmap());
		videoObject.actionUrl = "http://www.ttsss.cn/";
		videoObject.dataUrl = "www.weibo.com";
		videoObject.dataHdUrl = "www.weibo.com";
		videoObject.duration = 10;
		videoObject.defaultText = ShareContent.defaultText;
		return videoObject;
	}

	/**
	 * 创建多媒体（音频）消息对象。
	 * 
	 * @return 多媒体（音乐）消息对象。
	 */
	private VoiceObject getVoiceObj() {
		// 创建媒体消息
		VoiceObject voiceObject = new VoiceObject();
		voiceObject.identify = Utility.generateGUID();
		voiceObject.title = "多媒体音频";
		voiceObject.description = "音频描述：图灵DJ";

		// 设置 Bitmap 类型的图片到视频对象里
		voiceObject.setThumbImage(createBitmapDrawable().getBitmap());
		voiceObject.actionUrl = "http://www.ttsss.cn/";
		voiceObject.dataUrl = "www.weibo.com";
		voiceObject.dataHdUrl = "www.weibo.com";
		voiceObject.duration = 10;
		voiceObject.defaultText = ShareContent.defaultText;
		return voiceObject;
	}

}
