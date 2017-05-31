package com.abcs.huaqiaobang.service;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.util.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * @author xbw
 * @version 创建时间：2015-6-4 下午5:41:56
 */
public class NetChangeReceiver extends BroadcastReceiver {
	public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Util.netType = Util.getAPNType(MyApplication.getInstance());
		Log.v("网络变化", Util.netType);
		if (MyApplication.getInstance().getMainActivity() != null) {
			MyApplication.getInstance().getMainActivity().mHandler.sendEmptyMessage(1000);
		}
	}

}
