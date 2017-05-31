package com.abcs.huaqiaobang.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
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
import android.widget.Toast;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author xbw
 * @version 创建时间：2015-6-2 上午11:04:17
 */
public class ModifyPwdDialog extends Dialog {
	private Handler handler = new Handler();
	private Context context;
	private TextView[] views = new TextView[6];
	private LinearLayout grp_confirm;
	private EditText pwd;
	private TextView title;
	private String firstPwd = "";

	public ModifyPwdDialog(Context context) {
		super(context, R.style.dialog);
		this.context = context;
		setContentView(R.layout.occft_dialog_enterpwd);
		setCanceledOnTouchOutside(false);
		Window dialogWindow = getWindow();
		dialogWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		p.width = (int) (Util.WIDTH * 0.9); // 宽度设置为屏幕的0.65
		dialogWindow.setAttributes(p);

		((TextView) findViewById(R.id.dialog_pwd_title)).setText("修改交易密码");
		title = ((TextView) findViewById(R.id.dialog_enterpwd_title));
		title.setText("请输入旧密码");
		((TextView) findViewById(R.id.dialog_enterpwd_money))
				.setVisibility(View.GONE);
		findViewById(R.id.dialog_exit).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						ModifyPwdDialog.this.dismiss();
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
							if (firstPwd.length() == 0) {
								firstPwd = s;
								for (int i = 0; i < views.length; i++) {
									views[i].setText("");
								}
								pwd.setText("");
								title.setText("再次输入密码");
							} else {
								if (firstPwd.equals(s)) {
									showToast("两次输入密码一致");
									for (int i = 0; i < views.length; i++) {
										views[i].setText("");
									}
									pwd.setText("");
								} else {
									ModifyPwdDialog.this.dismiss();
									modifyPwd(s);
								}
							}
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

	private void modifyPwd(String newPwd) {
		String postUrl = TLUrl.getInstance().URL_bindentity;
		String param = "method=modifypaypwd&identityid="
				+ MyApplication.getInstance().self.getId() + "&paypwd="
				+ firstPwd + "&newpaypwd=" + newPwd + "&token=" + Util.token;
		LogUtil.e("modifyPwd", param);
		ProgressDlgUtil.showProgressDlg("", (Activity) context);
		HttpRequest.sendPost(postUrl, param, new HttpRevMsg() {
			@Override
			public void revMsg(String msg) {
				LogUtil.e("modifyPwdResult", msg);
				ProgressDlgUtil.stopProgressDlg();
				try {
					JSONObject jsonObject = new JSONObject(msg);
					if(jsonObject.getInt("status")==1){
						showToast(jsonObject.getString("msg"));
					}else{
						showToast("修改失败,"+jsonObject.getString("msg"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void showToast(final String msg) {
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
