package com.abcs.huaqiaobang.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;

/**
 * @author xbw
 * @version 创建时间：2015-6-2 上午11:04:17
 */
public class EnterPwdDialog extends Dialog {

	private TextView[] views = new TextView[6];
	private LinearLayout grp_confirm;
	private EditText pwd;

	public EnterPwdDialog(Context context, String title, String tip,
			String money, final HttpRevMsg revMsg) {
		super(context, R.style.dialog);
		setContentView(R.layout.occft_dialog_enterpwd);
		setCanceledOnTouchOutside(false);
		Window dialogWindow = getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		dialogWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		p.width = (int) (Util.WIDTH * 0.9); // 宽度设置为屏幕的0.65
		dialogWindow.setAttributes(p);

		((TextView) findViewById(R.id.dialog_enterpwd_title)).setText(title);
		((TextView) findViewById(R.id.dialog_enterpwd_tip)).setText(tip);
		if (money.equals("")) {
			((TextView) findViewById(R.id.dialog_enterpwd_money))
					.setVisibility(View.GONE);
		} else {
			((TextView) findViewById(R.id.dialog_enterpwd_money))
					.setText(money);
		}
		findViewById(R.id.dialog_exit).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						EnterPwdDialog.this.dismiss();
					}
				});
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
							EnterPwdDialog.this.dismiss();
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
		pwd.setFocusable(true);
	}

}
