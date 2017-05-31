package com.abcs.huaqiaobang.ytbt.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.abcs.huaqiaobang.MyApplication;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Util {
	public static String netType = "";
	public static String filePath;
	public static int WIDTH;
	public static int HEIGHT;
	public static int IMAGEWIDTH = 720;
	public static int IMAGEHEIGTH = 1280;
	public static final SharedPreferences preference = PreferenceManager
			.getDefaultSharedPreferences(MyApplication.getInstance());
	public static String STARTKEY = "FirstStart";

	public static DecimalFormat df = new DecimalFormat("0.00");
	@SuppressLint("SimpleDateFormat")
	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	public static void init() {
		if (preference.getBoolean(STARTKEY, true)) {
			preference.edit().clear().commit();
			preference.edit().putBoolean(STARTKEY, false).commit();
		}
		netType = Util.getAPNType(MyApplication.getInstance());
		filePath = MyApplication.getInstance().getFilesDir().getAbsolutePath();
		WIDTH = getScreenWidth(MyApplication.getInstance());
		HEIGHT = getScreenHeight(MyApplication.getInstance());
		format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
	}

	/**
	 * 从int类型时间，转为String(如3599s>01:59:59)
	 * 
	 * @param time
	 * @return
	 */
	public static String getStringFromTime(int time) {
		int h, n, s;
		h = time / 3600;
		n = time % 3600 / 60;
		s = time % 60;
		return getStringAddZero(h) + ":" + getStringAddZero(n) + ":"
				+ getStringAddZero(s);
	}

	public static String getStringAddZero(int n) {
		return (n < 10 ? "0" : "") + n;
	}

	/**
	 * 得到屏幕宽度
	 * 
	 * @return 单位:px
	 */
	public static int getScreenWidth(Context context) {
		int screenWidth;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		return screenWidth;
	}

	/**
	 * 得到屏幕高度
	 * 
	 * @return 单位:px
	 */
	public static int getScreenHeight(Context context) {
		int screenHeight;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		screenHeight = dm.heightPixels;
		return screenHeight;
	}

	/**
	 * dp 的单位 转成为 px(像素)
	 */
	public static int dp2px(Context context, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}

	/**
	 * 得到当前时间
	 * 
	 * @return 时间的字符串
	 */
	public static String getCurrentTime() {
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = format.format(curDate);
		return str;
	}

	/**
	 * 获取Manifest里meta的值
	 * 
	 * @param key
	 * @param context
	 * @return
	 */
	public static String getMeteDate(String key, Context context) {
		String msg = "";
		try {
			ApplicationInfo appInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			msg = appInfo.metaData.getString(key);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap网络3：net网络,4:3g
	 * 
	 * @param context
	 * @return
	 */
	public static String getAPNType(Context context) {
		String netType = "未知";
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				netType = "CMNET";
			} else if (networkInfo.getExtraInfo().toLowerCase().equals("3gnet")) {
				netType = "3GNET";
			} else {
				netType = "CMWAP";
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = "WIFI";
		}
		return netType;
	}
}
