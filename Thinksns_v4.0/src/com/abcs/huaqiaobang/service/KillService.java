package com.abcs.huaqiaobang.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.util.Util;

import java.util.List;

public class KillService extends Service {
	private ActivityManager activityManager;
	private String packageName;
	private boolean flag = true;
	private int i = 0;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (thread == null) {
			activityManager = (ActivityManager) this
					.getSystemService(Context.ACTIVITY_SERVICE);
			packageName = this.getPackageName();
			thread = new MyThread();
			new Thread(thread).start();
			IntentFilter filter = new IntentFilter();
			filter.addAction("screenOn");
			registerReceiver(new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					flag = true;
				}
			}, filter);
			IntentFilter filter1 = new IntentFilter();
			filter1.addAction("screenOFF");
			registerReceiver(new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					flag = false;
				}
			}, filter1);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	MyThread thread;

	class MyThread implements Runnable {
		public void run() {
			try {
				while (true) {
					Thread.sleep(10000);
					if (!isAppOnForeground() || !flag) {
						i++;
					} else {
						i = 0;
					}
					Log.i("tgakill",i+"");
					if (i > 60|| Util.HEIGHT==0) {

						MyApplication.getInstance().exit();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	public boolean isAppOnForeground() {
		// Returns a list of application processes that are running on the
		// device
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

}
