package com.abcs.huaqiaobang.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.tljr.data.Constent;


public class CueDialog extends Dialog implements OnClickListener {
	int type  ;
	public CueDialog(Activity activity,int type) {
		super(activity, R.style.dialog);
		this.type =type ;
		if(type==1){
			setContentView(R.layout.tljr_news_cue);
		}else{
			setContentView(R.layout.tljr_news_cue2);
		}
	
		setCanceledOnTouchOutside(false);
		init();
		windowDeploy();
	}

	// 设置窗口显示
	public void windowDeploy()
	{
		Window win = getWindow(); // 得到对话框
		win.setWindowAnimations(R.style.speakdialog_bottom); // 设置窗口弹出动画
		win.getDecorView().setPadding(0, 0, 0, 0); // 宽度占满，因为style里面本身带有padding
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.FILL_PARENT;
		win.setAttributes(lp);
	}

	private void init()
	{
		// TODO Auto-generated method stub
		findViewById(R.id.tljr_dialog_news_cue).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.tljr_dialog_news_cue:
			this.dismiss();
			Constent.isNewsGuideToast=type;
			Constent.preference.edit().putInt("isNewsGuideToast", type).commit(); 
			break;

		default:
			break;
		}
	}

}