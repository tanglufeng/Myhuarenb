package com.abcs.huaqiaobang.tljr.data;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.StartActivity;
import com.abcs.huaqiaobang.model.User;
import com.abcs.huaqiaobang.util.DataCleanManager;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Constent {
	public static String device_token = "";
	public static boolean isThirdLogin = false;
	public static String GUKEY = "GU";
	public static String STARTKEY = "FirstStart";
	public static String LIULIANGKEY = "liuliangkey";
	public static String LIULIANGTIMEKEY = "liuliangtimekey";
	public static String TBGPDMTIMEKEY = "tbgpdmtimekey";
	public static String TBGGXXTIMEKEY = "tbggxxtimekey";
	public static String TBXTSSTIMEKEY = "tbxtsstimekey";
	public static String FLUSHKEY = "flushrate";
	public static String NEWSFONTSIZE = "newsfontsize";
	public static int ZXFontSize = 15;
	public static int[] FontSizes = { 22, 18, 14, 12, 10 };
	public static String[] FontSizeNames = { "超大", "大", "正常", "小", "细小" };
	public static int FlushTime = 60;
	public static int[] FlushTimes = { 0, 5, 15, 30, 60 };
	public static String[] FlushTimeNames = { "不刷新", "5秒", "15秒", "30秒", "60秒" };
	public static int guVersion;
	public static final SharedPreferences preference = PreferenceManager
			.getDefaultSharedPreferences(MyApplication.getInstance());
	public static String netType = "";
	public static String appVersion = "2.0";
	public static String packageName = "";
	public static long Liuliang = 0;
	public static String lastSynTime = "";
	public static String aboutUsUrl = "http://42.120.45.171:8008/weiXin/aboutUs.html";

	public static String[] names = { "国内指数", "其他指数", "股指期货" };
	public static int readNums = 0;

	public static boolean noPictureMode =false; //无图模式
	public static int isNewsGuideToast = 0; //是否弹出过新闻-界面的引导提示 , 0-未打开  1-新闻界面引导已打开  2-新闻内页引导已打开  
	
	
	public static void init() {
	 
		// preference = PreferenceManager
		// .getDefaultSharedPreferences(MyApplication.getInstance());
		// preference = app.getSharedPreferences("TLJR",
		// Application.MODE_PRIVATE);
		netType = Util.getAPNType(MyApplication.getInstance());
		if (preference.getBoolean(STARTKEY, true)) {
			DataCleanManager.cleanCustomCache(Util.sdPath);
			preference.edit().clear().commit();
			preference.edit().putBoolean(STARTKEY, false).commit();
			DataCleanManager.cleanApplicationData(MyApplication.getInstance());
			StartActivity.imageLoader.clearMemoryCache();
		}
		Util.init();
	}

	/**
	 * 初始化分组
	 */



	public static void LLStorage() {
		long now = preference.getLong(LIULIANGKEY, Liuliang);
		preference.edit().putLong(LIULIANGKEY, (now + Liuliang)).commit();
	}

	

	public static String getStringByObject(Object object) throws Throwable {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream);
		objectOutputStream.writeObject(object);
		String s = new String(Base64.encode(
				byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
		objectOutputStream.close();
		return s;
	}

	public static void dataRead(final String key, final readComplete complete) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Object obj = null;
					String mobilesString = preference.getString(key, null);
					if (mobilesString == null) {
						complete.read(obj);
						return;
					}
					byte[] mobileBytes = Base64.decode(
							mobilesString.getBytes(), Base64.DEFAULT);
					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
							mobileBytes);
					ObjectInputStream objectInputStream = new ObjectInputStream(
							byteArrayInputStream);
					obj = objectInputStream.readObject();
					objectInputStream.close();
					complete.read(obj);
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					complete.read(null);
				}
			}
		}).start();

	}

	public static interface readComplete {
		public void read(Object object);
	}

	public static void sendToken(User user) {
		HttpRequest.sendPost(
				TLUrl.getInstance().URL_share,
				"reqType=pushInfo&id=" + user.getId() + "&userName="
						+ user.getUserName() + "&nickName="
						+ user.getNickName() + "&token=" + device_token
						+ "&device=android");
	}

}
