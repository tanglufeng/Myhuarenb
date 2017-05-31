package com.abcs.huaqiaobang.dialog;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;

/**
 * @author xbw
 * @version 创建时间：2015-6-2 上午11:04:17
 */
public class EnterSecurityDialog extends Dialog {
	private LinearLayout grp_confirm;
	private TextView lastTime;
	private Button btn;
	private EditText pwd;
	private int time = 60;
	private Handler handler = new Handler();
	private TextView[] views = new TextView[6];

	public EnterSecurityDialog(final Context context, String phone,
			final String orderId, final HttpRevMsg revMsg) {
		super(context, R.style.dialog);
		setContentView(R.layout.occft_dialog_entersecurity);
		setCanceledOnTouchOutside(false);
		Window dialogWindow = getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		dialogWindow
		.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		p.width = (int) (Util.WIDTH * 0.9); // 宽度设置为屏幕的0.65
		dialogWindow.setAttributes(p);
		findViewById(R.id.dialog_exit).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						EnterSecurityDialog.this.dismiss();
					}
				});

		((TextView) findViewById(R.id.dialog_entersecurity_title))
				.setText("验证码已发往绑定手机" + phone + "，请注意查收");
		lastTime = (TextView) findViewById(R.id.dialog_entersecurity_tip);
		btn = (Button) findViewById(R.id.dialog_entersecurity_btn);
		grp_confirm = (LinearLayout) findViewById(R.id.tljr_grp_confirm);
		for (int i = 0; i < views.length; i++) {
			views[i] = new TextView(context);
			views[i].setTextColor(context.getResources().getColor(
					R.color.tljr_text_default));
			views[i].setTextSize(30);
			views[i].setGravity(Gravity.CENTER);
			views[i].setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1));
			grp_confirm.addView(views[i]);
		}
		pwd = (EditText) findViewById(R.id.tljr_et_confirm);
		pwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int start, int before,
					int count) {
				final String s = arg0.toString().trim();
				for (int i = 0; i < views.length; i++) {
					if (i < s.length()) {
						views[i].setText(s.charAt(i) + "");
					} else {
						views[i].setText("");
					}
				}
				if (arg0.length() == 6) {
					pwd.postDelayed(new Runnable() {

						@Override
						public void run() {
							EnterSecurityDialog.this.dismiss();
							revMsg.revMsg(s);
						}
					}, 200);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				LogUtil.e("sendValidateMsg",
						"method=sendValidateMsg&orderid=" + orderId + "&uid="
								+ MyApplication.getInstance().self.getId()
								+ "&token=" + Util.token);
				HttpRequest.sendPost(TLUrl.getInstance().URL_productServlet,
						"method=sendValidateMsg&orderid=" + orderId + "&uid="
								+ MyApplication.getInstance().self.getId()
								+ "&token=" + Util.token, new HttpRevMsg() {

							@Override
							public void revMsg(final String msg) {
								LogUtil.e("sendValidateMsg", msg);
								handler.post(new Runnable() {

									@Override
									public void run() {
										try {
											JSONObject jsonObject = new JSONObject(
													msg);
											if (jsonObject.getInt("status") == 1) {
												String to = jsonObject
														.getString("msg");
												Toast.makeText(context, to,
														Toast.LENGTH_SHORT)
														.show();
												time = 60;
												handler.post(runnable);
											} else {
												Toast.makeText(context,
														"获取失败，请重试",
														Toast.LENGTH_SHORT)
														.show();
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
							}
						});
			}
		});
		handler.post(runnable);
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (time > 0) {
				handler.postDelayed(runnable, 1000);
				time--;
				btn.setVisibility(View.GONE);
				lastTime.setVisibility(View.VISIBLE);
				lastTime.setText("重新获取(" + time + ")");
			} else {
				lastTime.setVisibility(View.GONE);
				btn.setVisibility(View.VISIBLE);
			}
		}
	};
}
