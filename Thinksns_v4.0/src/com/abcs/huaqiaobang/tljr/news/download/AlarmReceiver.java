package com.abcs.huaqiaobang.tljr.news.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.abcs.huaqiaobang.tljr.data.Constent;

/**
 * 
 * @ClassName: AlarmReceiver
 * @Description: 闹铃时间到了会进入这个广播，这个时候可以做一些该做的业务。
 * @author HuHood
 * @date 2013-11-25 下午4:44:30
 * 
 */
public class AlarmReceiver extends BroadcastReceiver {
	private boolean isTimingDownLoad ;
	@Override
	public void onReceive(Context context, Intent intent) {
	
		Log.i("DownLoadActivity", "收到闹钟广播");
		isTimingDownLoad= Constent.preference.getBoolean("isTimingDownLoad", false);
		
		if(isTimingDownLoad){
			System.out.println("开始离线下载------AlarmReceiver--- -boastcast----");
			Intent bindIntent = new Intent(context, DownLoadService.class);
			bindIntent.putExtra("isTiming", "true");
			context.startService(bindIntent);
		}
		 

	}

 

}
