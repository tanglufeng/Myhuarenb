package com.abcs.huaqiaobang.util;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

public class ServerUtils {
	public static String Tag;
	public static boolean isConnect(Context context) {
		if (Tag == null) {
			Tag = ServerUtils.class.getSimpleName();
		}
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static String msg="";
	private static Random random;
	public static int[] bugTypeCounts; 
	
	public static String  getBugTypeCounts(int index) throws JSONException{
		
		if(TextUtils.isEmpty(msg)){
			return "1";
		}
		
		
		String num;
		try {
			JSONObject jsonObject = new JSONObject(msg);
			JSONObject bugType = jsonObject.getJSONObject("AICountList");
 
			num = bugType.getString(index+"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "1";
		}
		Log.i("WWW",  "SSSnum:"+num);
		
		return num;
		
	}
	
}
