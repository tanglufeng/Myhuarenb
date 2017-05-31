package com.abcs.hqbtravel.wedgt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.abcs.huaqiaobang.util.CancelComplete;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;

/**
 * @author xbw
 * @version 创建时间：2015-6-2 上午11:04:17
 */
public class PromptDialog extends Dialog implements OnClickListener {
	private Activity context;
	private Complete complete;
	private CancelComplete cancelComplete;

	public PromptDialog(Context context) {
		super(context, R.style.dialog);
		init("确定退出图灵金融 ？");
	}

	public PromptDialog(Activity context, String msg, Complete complete) {
		super(context, R.style.dialog);
		this.complete = complete;
		init(msg);
	}
	public PromptDialog(Activity context, String msg, CancelComplete cancelComplete,Complete complete) {
		super(context, R.style.dialog);
		this.complete = complete;
		this.cancelComplete = cancelComplete;
		init(msg);
	}

	private void init(String msg) {
		setContentView(R.layout.travel_dialog_exit);
		setCanceledOnTouchOutside(false);
		Window dialogWindow = getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//		p.height = (int) (Util.HEIGHT * 0.27); 
		p.width = (int) (Util.WIDTH * 0.77); 
		dialogWindow.setAttributes(p);
		findViewById(R.id.tljr_dialog_exit_cancel).setOnClickListener(this);
		findViewById(R.id.tljr_dialog_exit_ok).setOnClickListener(this);
//		setOnDismissListener(new OnDismissListener() {
//
//			@Override
//			public void onDismiss(DialogInterface dialog) {
//				if (cancelComplete != null) {
//					cancelComplete.cancel();
//				}
//			}
//		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.tljr_dialog_exit_cancel:
			this.dismiss();
			if (cancelComplete != null) {
				cancelComplete.cancel();
			}
			break;
		case R.id.tljr_dialog_exit_ok:
			this.dismiss();
			if (complete != null) {
				complete.complete();
				return;
			}
//			try {
////				Constent.dataStorage();
//			} catch (Throwable e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			if (context instanceof Activity) {
//				((Activity) context).finish();
//				MobclickAgent.onKillProcess(context);
//			}
////			if(Warn.needpublicWarn){
////				Constent.preference.edit().putBoolean("Mainislive", false).commit();
////				MyApplication.getInstance().startService(new Intent(MyApplication.getInstance(),WarnService.class));
////			}
//			MyApplication.getInstance().exit();
//			android.os.Process.killProcess(android.os.Process.myPid());
//			System.exit(0);
			break;

		default:
			break;
		}
	}
}
