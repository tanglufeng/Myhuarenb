package com.abcs.huaqiaobang.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author xbw
 * @version 创建时间：2015年10月22日 下午2:23:14
 */
public class AccountStep2Activity extends BaseActivity {
	private Handler handler = new Handler();
	private EditText pwd;
	private TextView[] views = new TextView[6];
	private LinearLayout grp_confirm;
	private String firstPwd = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.occft_activity_account_step2);
		init();
	}

	private void init() {
		findViewById(R.id.account_btn_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		grp_confirm = (LinearLayout) findViewById(R.id.tljr_grp_confirm);
		for (int i = 0; i < views.length; i++) {
			views[i] = new TextView(this);
			views[i].setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
			views[i].setTextColor(getResources().getColor(
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
				String s = arg0.toString().trim();
				for (int i = 0; i < views.length; i++) {
					if (i < s.length()) {
						views[i].setText(s.charAt(i) + "");
					} else {
						views[i].setText("");
					}
				}
				if (arg0.length() == 6) {
					if (firstPwd.length() == 0) {
						firstPwd = arg0.toString().trim();
						for (int i = 0; i < views.length; i++) {
							views[i].setText("");
						}
						pwd.setText("");
						((TextView) findViewById(R.id.tljr_txt_confirm))
								.setText("再次输入密码");
					} else {
						if (firstPwd.equals(arg0.toString().trim())) {
							toNextStep();
						} else {
							showToast("两次输入密码不一致");
							for (int i = 0; i < views.length; i++) {
								views[i].setText("");
							}
							pwd.setText("");
						}
					}
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
	}

	private void toNextStep() {
		LogUtil.e("paypwd",
				"identityid=" + MyApplication.getInstance().self.getId()
						+ "&method=paypwd&paypwd=" + pwd.getText().toString()
						+ "&token=" + Util.token);
		HttpRequest.sendPost(TLUrl.getInstance().URL_bindentity, "identityid="
				+ MyApplication.getInstance().self.getId()
				+ "&method=paypwd&paypwd=" + pwd.getText().toString()
				+ "&token=" + Util.token, new HttpRevMsg() {

			@Override
			public void revMsg(final String msg) {
				LogUtil.e("toNextStep 1", msg);// {"msg":"设置成功","status":1}
				handler.post(new Runnable() {

					@Override
					public void run() {
						try {
							JSONObject jsonObject = new JSONObject(msg);
							if (jsonObject.getInt("status") == 1) {
								startActivity(new Intent(
										AccountStep2Activity.this,
										AccountStep3Activity.class));
								finish();
								MyApplication.getInstance().getMainActivity().mHandler
										.sendEmptyMessage(902);
							} else {
								showToast(jsonObject.getString("msg"));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							toNextStep();
						}
					}
				});
			}
		});
	}
}
