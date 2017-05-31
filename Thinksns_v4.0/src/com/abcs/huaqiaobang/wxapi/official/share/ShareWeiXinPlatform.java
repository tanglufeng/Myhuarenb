package com.abcs.huaqiaobang.wxapi.official.share;

//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.CompressFormat;
//import android.graphics.BitmapFactory;
//
//import com.abcs.sociax.android.R;
//import com.abcs.huaqiaobang.wxapi.official.share.util.ShareContent;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.SendMessageToWX;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.tencent.mm.sdk.openapi.WXMediaMessage;
//import com.tencent.mm.sdk.openapi.WXWebpageObject;

import java.io.ByteArrayOutputStream;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.wxapi.official.share.util.ShareContent;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class ShareWeiXinPlatform {
	private IWXAPI wxApi;
	private static final String appId = "wx9906731d4fadfdaa";
	private Activity activity;

	public ShareWeiXinPlatform(Activity activiy) {
		this.activity = activiy;
		// 实例化
		wxApi = WXAPIFactory.createWXAPI(activiy, appId);
		boolean s =wxApi.registerApp(appId);
		System.out.println("实例化ShareWeiXinPlatform :"+s);
	}

	/**
	 * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
	 * 
	 * @param flag
	 *            (0:分享到微信好友，1：分享到微信朋友圈)
	 */
	public void wechatShare(int flag) {
		System.out.println("in wechatShare");
		  WXWebpageObject webpage = new WXWebpageObject();
		  
	 
		  
		  webpage.webpageUrl = url.equals("")? ShareContent.actionUrl:url;
	           
	       WXMediaMessage msg = new WXMediaMessage(webpage);
	       msg.title = title.equals("")? ShareContent.title:title;
	       msg.description = content.equals("")?ShareContent.content:content;
	     
	 
	  	
	
	       
	       // 这里替换一张自己工程里的图片资源
 	       Bitmap thumb = BitmapFactory.decodeResource(activity.getResources(),
 	               R.drawable.ic_launcher);
	   	msg.thumbData =  bmpToByteArray(thumb, true);
 	       msg.setThumbImage(thumb);
	 
	       SendMessageToWX.Req req = new SendMessageToWX.Req();
	       req.transaction = String.valueOf(System.currentTimeMillis());
	       req.message = msg;
	       req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession
	               : SendMessageToWX.Req.WXSceneTimeline;
	       req.transaction = buildTransaction("webpage");
	       boolean fg =wxApi.sendReq(req);
	   	System.out.println(" wxApi.sendReq :"+fg);
	       // //在需要分享的地方添加代码：
	       // wechatShare(0);//分享到微信好友
	       // wechatShare(1);//分享到微信朋友圈

	}

	private String url = "";
	private String title = "";
	private String content = ""; 

	
	  

	public void setContent(String content) {
		this.content = content;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	 
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
