package com.abcs.huaqiaobang.tljr.news.store;

import android.content.Context;

import com.abcs.huaqiaobang.tljr.data.Constent;

public class ServerUtils {
	public static String Tag;
	public static boolean isConnect(Context context) {
		return !Constent.netType.equals("未知");
//		if (Tag == null) {
//			Tag = ServerUtils.class.getSimpleName();
//		}
//		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
//		try {
//			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//			if (connectivity != null) {
//				// 获取网络连接管理的对象
//				NetworkInfo info = connectivity.getActiveNetworkInfo();
//				if (info != null && info.isConnected()) {
//					// 判断当前网络是否已经连接
//					if (info.getState() == NetworkInfo.State.CONNECTED) {
//						return true;
//					}
//				}
//			}
//		} catch (Exception e) {
//			LogUtil.e(Tag, e.toString());
//		}
//		return false;
	} 
	
	 
	
}
