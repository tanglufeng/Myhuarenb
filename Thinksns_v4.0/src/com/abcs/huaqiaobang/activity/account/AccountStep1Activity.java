package com.abcs.huaqiaobang.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.activity.WebActivity;
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
public class AccountStep1Activity extends BaseActivity {
	private Handler handler = new Handler();
	private EditText name, cardno, phone;
	private CheckBox cb;
	private Button btn;
	private boolean completeName, completeCode, completePhone,
			completeProtocol = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.occft_activity_account_step1);
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
		findViewById(R.id.acount_step1_txt_protocol).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(AccountStep1Activity.this,
								WebActivity.class);
						intent.putExtra("url",
								"http://oss.aliyuncs.com/tuling/protocol.html");
						intent.putExtra("name", "账户服务协议");
						startActivity(intent);
					}
				});
		name = (EditText) findViewById(R.id.acount_step1_edit_name);
		cardno = (EditText) findViewById(R.id.acount_step1_edit_cardno);
		phone = (EditText) findViewById(R.id.acount_step1_edit_phone);
		cb = (CheckBox) findViewById(R.id.acount_step1_cb);
		btn = (Button) findViewById(R.id.acount_step1_btn);
		name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				completeName = s.length() > 1;
				checkComplete();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		cardno.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				completeCode = s.length() > 17;
				checkComplete();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				completePhone = s.length() > 10;
				checkComplete();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				completeProtocol = cb.isChecked();
				checkComplete();
			}
		});
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toNextStep();
			}
		});
	}

	private void checkComplete() {
		if (completeName && completeCode && completePhone && completeProtocol) {
			btn.setBackgroundResource(R.drawable.img_yuananniu);
			btn.setClickable(true);
		} else {
			btn.setBackgroundResource(R.drawable.img_yuananniu1);
			btn.setClickable(false);
		}
	}

	private void toNextStep() {
		LogUtil.e("bindidenity", "identityid="
				+ MyApplication.getInstance().self.getId()
				+ "&method=bindidenity&username=" + name.getText().toString()
				+ "&cardno=" + cardno.getText().toString() + "&phone="
				+ phone.getText().toString() + "&token=" + Util.token);
		HttpRequest.sendPost(TLUrl.getInstance().URL_bindentity, "identityid="
				+ MyApplication.getInstance().self.getId()
				+ "&method=bindidenity&username=" + name.getText().toString()
				+ "&cardno=" + cardno.getText().toString() + "&phone="
				+ phone.getText().toString() + "&token=" + Util.token,
				new HttpRevMsg() {

					@Override
					public void revMsg(final String msg) {
						LogUtil.e("toNextStep 1", msg);// {"msg":"绑定成功","status":1}
						handler.post(new Runnable() {

							@Override
							public void run() {
								try {
									JSONObject jsonObject = new JSONObject(msg);
									if (jsonObject.getInt("status") == 1
											|| jsonObject.getInt("status") == -1004) {
										AccountStep3Activity.s_phone = phone
												.getText().toString();
										startActivity(new Intent(
												AccountStep1Activity.this,
												AccountStep2Activity.class));
										finish();
									} else {
										showToast(jsonObject.getString("msg"));
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
}
