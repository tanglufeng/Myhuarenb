package com.abcs.huaqiaobang.ytbt.biz;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.abcs.huaqiaobang.MyApplication;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LogcatScannerService extends Service implements LogcatObserver {
	private String iMEI, simNo, operatorName, simSerialNumber, iMSI, iSO,
			oSInfo, module, manufacturer, resolution, apps, packageName,
			versionName, versionCode;
	private DisplayMetrics dm;
	private String SourceIdentity = "";// 市场标识
	ApplicationInfo appInfo;
	JSONObject params = new JSONObject();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		new AndroidLogcatScannerThread(this).start();
		getUserInfo();
	}

	@Override
	public void handleLog(String info) {
		// 如果包含了卸载日志信息时就执行此操作
		if (info.contains("android.intent.action.DELETE")
				&& info.contains(getPackageName())) {
//			long t1 = System.currentTimeMillis();
			// getUserInfo();
//			long t2 = System.currentTimeMillis();
			doRequest();
//			long t3 = System.currentTimeMillis();
		}
	}

	/**
	 * 
	 * @author 实现输出日志信息的监控
	 * 
	 */
	private class AndroidLogcatScannerThread extends Thread {

		private LogcatObserver mObserver;

		public AndroidLogcatScannerThread(LogcatObserver observer) {
			mObserver = observer;
		}

		@Override
		public void run() {
			String[] cmds = { "logcat", "-c" };
			String shellCmd = "logcat";
			Process process = null;
			InputStream is = null;
			DataInputStream dis = null;
			String line = "";
			Runtime runtime = Runtime.getRuntime();
			try {
				mObserver.handleLog(line);
				int waitValue;
				waitValue = runtime.exec(cmds).waitFor();
				mObserver.handleLog("waitValue=" + waitValue
						+ "\n Has do Clear logcat cache.");
				process = runtime.exec(shellCmd);
				is = process.getInputStream();
				dis = new DataInputStream(is);
				while ((line = dis.readLine()) != null) {
					if (mObserver != null)
						mObserver.handleLog(line);

				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException ie) {
			} finally {
				try {
					if (dis != null) {
						dis.close();
					}
					if (is != null) {
						is.close();
					}
					if (process != null) {
						process.destroy();
					}
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 获取用户信息
	 * 
	 * 
	 */
	private void getUserInfo() {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		iMEI = tm.getDeviceId();
		simNo = tm.getLine1Number();
		operatorName = tm.getNetworkOperatorName();
		simSerialNumber = tm.getSimSerialNumber();
		iMSI = tm.getSubscriberId();
		iSO = tm.getNetworkCountryIso();
		oSInfo = android.os.Build.VERSION.RELEASE;
		module = android.os.Build.MODEL;
		manufacturer = android.os.Build.MANUFACTURER;
		try {
			appInfo = this.getPackageManager().getApplicationInfo(
					getPackageName(), PackageManager.GET_META_DATA);
			SourceIdentity = appInfo.metaData.get("BaiduMobAd_CHANNEL")
					.toString();
			PackageInfo info = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0);
			packageName = info.packageName;
			versionName = info.versionName;
//			versionCode = getResources().getString(R.string.version);
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			params.put("SourceIdentity", SourceIdentity);
			params.put("IMEI", iMEI);
			params.put("SimNo", simNo);
			params.put("OperatorName", operatorName);
			params.put("SimSerialNumber", simSerialNumber);
			params.put("IMSI", iMSI);
			params.put("OSInfo", oSInfo);
			params.put("ISO", iSO);
			params.put("Module", module);
			params.put("Manufacturer", manufacturer);
			params.put("Resolution", resolution);
			params.put("apps", apps);
			params.put("PackageVersionName", versionName);
//			params.put("PackageVersionCode", versionCode);
			params.put("PackageName", packageName);
//			params.put("WifiMac", DeviceInfo.getMacAddress(this));
//			params.put("BluetoothMac", DeviceInfo.getBluetoothAddress(this));
//			params.put("GuidData", DeviceInfo.filesDirGuid(this));
//			params.put("GuidSD", DeviceInfo.externalStorageGuid(this));
			System.out.println("params=" + params.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 在用户点击卸载时 
	 * @return
	 */
	public void doRequest() {
		try {
			Log.i("info", "dropDb");
			MyApplication.dbUtils.dropDb();
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

}
