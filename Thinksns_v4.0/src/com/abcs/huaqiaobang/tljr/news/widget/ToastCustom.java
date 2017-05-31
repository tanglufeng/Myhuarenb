package com.abcs.huaqiaobang.tljr.news.widget;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.sociax.android.R;

@SuppressLint("HandlerLeak")
public class ToastCustom {
	private static final int MESSAGE_TIMEOUT = 2;
	private WindowManager wdm;
	private double time;
	private Toast toast;
	private WindowManager.LayoutParams params;

	@SuppressLint("ShowToast")
	private ToastCustom(Context context, String content, double time) {
		wdm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		LayoutInflater inflater = LayoutInflater.from(context);
		View layout = inflater.inflate(R.layout.tljr_item_news_toast, null);
		TextView title = (TextView) layout.findViewById(R.id.tljr_tvContent);
		title.setText("完全自定义Toast");

		toast = Toast.makeText(context.getApplicationContext(), content, Toast.LENGTH_LONG);
		toast.setView(layout);

		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.format = PixelFormat.TRANSLUCENT;
		// params.windowAnimations = toast.getView().getAnimation().INFINITE;
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		params.setTitle("Toast");
		params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

		params.windowAnimations = R.style.custom_toast_anim_view;
		this.time = time;
	}

	public static ToastCustom makeText(Context context, String text, double time) {
		ToastCustom toastCustom = new ToastCustom(context, text, time);
		return toastCustom;
	}

	public void setText(String content) {
		((TextView) toast.getView().findViewById(R.id.tljr_tvContent)).setText(content);
	}
	private boolean isAddView = false;
	public void show() {
		if (isAddView) {
			cancel();
			mHandler.removeMessages(MESSAGE_TIMEOUT);
		}
		wdm.addView(toast.getView(), params);
		mHandler.sendEmptyMessageDelayed(MESSAGE_TIMEOUT, (long) (time * 1000));
		isAddView = true;
	}

	public void cancel() {
		wdm.removeView(toast.getView());
		isAddView = false;
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MESSAGE_TIMEOUT :
					cancel();
					break;
			}
		}
	};
}