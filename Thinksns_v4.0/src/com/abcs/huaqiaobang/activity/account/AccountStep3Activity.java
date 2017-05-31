package com.abcs.huaqiaobang.activity.account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.activity.WebActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
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
public class AccountStep3Activity extends BaseActivity {
	public static String s_phone = "";
	private Handler handler = new Handler();
	private EditText cardno, phone, security;
	private TextView name, namep;
	private ImageView icon, cardnoimg;
	private CheckBox cb;
	private Button securitycode, btn;
	private boolean completeCode, completePhone, completeProtocol = true;
	private String requestid = "";
	private int binkCheck = 0;
	private int time = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.occft_activity_account_step3);
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
		findViewById(R.id.acount_grp).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Util.bankList == null || Util.bankList.length() == 0) {
//					MyApplication.getInstance().getMainActivity().my
//							.getBankList(new Complete() {
//
//								@Override
//								public void complete() {
//									handler.post(new Runnable() {
//
//										@Override
//										public void run() {
//											checkBink();
//										}
//									});
//								}
//							});
				} else {
					checkBink();
				}
			}
		});
		findViewById(R.id.acount_step1_txt_protocol).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(AccountStep3Activity.this,
								WebActivity.class);
						intent.putExtra("url", TLUrl.getInstance().URL_bindbank);
						intent.putExtra("name", "委托扣款协议");
						startActivity(intent);
					}
				});
		name = (TextView) findViewById(R.id.acount_step3_edit_name);
		namep = (TextView) findViewById(R.id.acount_step3_name);
		icon = (ImageView) findViewById(R.id.acount_step3_icon);
		cardno = (EditText) findViewById(R.id.acount_step3_edit_cardno);
		cardnoimg = (ImageView) findViewById(R.id.acount_step3_img_cardno);
		phone = (EditText) findViewById(R.id.acount_step3_edit_phone);
		phone.setText(s_phone);
		if (!s_phone.equals("")) {
			completePhone = true;
		}
		security = (EditText) findViewById(R.id.acount_step3_edit_securitycode);
		cb = (CheckBox) findViewById(R.id.acount_step3_cb);
		btn = (Button) findViewById(R.id.acount_step3_btn);
		securitycode = (Button) findViewById(R.id.acount_step3_btn_securitycode);
		cardnoimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cardno.setText("");
				cardnoimg.setVisibility(View.GONE);
			}
		});
		cardno.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					cardnoimg.setVisibility(View.VISIBLE);
				} else {
					cardnoimg.setVisibility(View.GONE);
				}
				completeCode = s.length() > 15;
				checkComplete();
				if (s.length() == 16 || s.length() == 19) {
					getCardName();
				} else if (s.length() == 0) {
					hideCard();
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
		security.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (requestid.length() > 0 && s.length() == 6) {
					btn.setBackgroundResource(R.drawable.img_yuananniu);
					btn.setClickable(true);
				} else {
					btn.setBackgroundResource(R.drawable.img_yuananniu1);
					btn.setClickable(false);
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
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				completeProtocol = cb.isChecked();
				checkComplete();
			}
		});
		securitycode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(cardno==null){
					showToast("卡号不能为空");
					return;
				}
				if (time == 0)
					getSecurityCode();
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
		if (completeCode && completePhone && completeProtocol) {
			securitycode.setBackgroundResource(R.drawable.img_huoqixuanze2);
			securitycode.setClickable(true);
			securitycode.setTextColor(Color.WHITE);
		} else {
			securitycode.setBackgroundResource(R.drawable.img_huoqixuanze1);
			securitycode.setClickable(false);
			securitycode.setTextColor(Color.parseColor("#969696"));
		}
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (time > 0) {
				handler.postDelayed(runnable, 1000);
				time--;
				securitycode.setText("重新获取(" + time + ")");
			} else {
				securitycode.setText(getResources().getString(
						R.string.account_securitycode));
				securitycode.setBackgroundResource(R.drawable.img_huoqixuanze2);
				securitycode.setTextColor(Color.WHITE);
			}
		}
	};

	private void getSecurityCode() {
		ProgressDlgUtil.showProgressDlg("", this);
		LogUtil.e("bindCard",
				"identityid=" + MyApplication.getInstance().self.getId()
						+ "&method=bindCard&bankName="
						+ name.getText().toString() + "&cardno="
						+ cardno.getText().toString() + "&phone="
						+ phone.getText().toString() + "&token=" + Util.token);
		HttpRequest.sendPost(TLUrl.getInstance().URL_bindBank,
				"identityid=" + MyApplication.getInstance().self.getId()
						+ "&method=bindCard&bankName="
						+ name.getText().toString() + "&cardno="
						+ cardno.getText().toString() + "&phone="
						+ phone.getText().toString() + "&token=" + Util.token,
				new HttpRevMsg() {

					@Override
					public void revMsg(final String msg) {
						ProgressDlgUtil.stopProgressDlg();
						LogUtil.e("toNextStep 1", msg);// {"msg":{"requestid":"TULINGBANK1445500940702"},"status":1}
						handler.post(new Runnable() {

							@Override
							public void run() {
								try {
									JSONObject jsonObject = new JSONObject(msg);
									if (jsonObject.getInt("status") == 1) {
										requestid = jsonObject.getJSONObject(
												"msg").getString("requestid");
										securitycode
												.setBackgroundResource(R.drawable.img_huoqixuanze1);
										securitycode.setTextColor(Color
												.parseColor("#969696"));
										time = 60;
										handler.post(runnable);
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

	private void toNextStep() {
		LogUtil.e("comfigBindCard", "identityid="
				+ MyApplication.getInstance().self.getId()
				+ "&method=comfigBindCard&validatecode="
				+ security.getText().toString() + "&requestid=" + requestid
				+ "&token=" + Util.token);
		HttpRequest.sendPost(TLUrl.getInstance().URL_bindBank,
				"identityid=" + MyApplication.getInstance().self.getId()
						+ "&method=comfigBindCard&validatecode="
						+ security.getText().toString() + "&requestid="
						+ requestid + "&token=" + Util.token, new HttpRevMsg() {

					@Override
					public void revMsg(final String msg) {
						LogUtil.e("toNextStep 1", msg);// {"msg":"","status":1}
						handler.post(new Runnable() {

							@Override
							public void run() {
								try {
									JSONObject jsonObject = new JSONObject(msg);
									if (jsonObject.getInt("status") == 1) {
										showToast("开户成功");
										finish();
										MyApplication.getInstance()
												.getMainActivity().mHandler
												.sendEmptyMessage(901);
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

	private void checkBink() {
		final String[] arrayItems = new String[Util.bankList.length()];
		for (int i = 0; i < Util.bankList.length(); i++) {
			try {
				arrayItems[i] = Util.bankList.getJSONObject(i).getString(
						"bankname");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 初始化并显示Dialog
		AlertDialog alertDialog = new AlertDialog.Builder(this)
				.setTitle(
						getResources().getString(
								R.string.account_step3_name_hint))
				.setSingleChoiceItems(arrayItems, binkCheck,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								binkCheck = which;
							}
						})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						name.setText(arrayItems[binkCheck]);
						showCard(arrayItems[binkCheck]);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).create();
		alertDialog.show();
	}

	private String getUrl(String name) {
		String url = "";
		if (Util.bankList == null || Util.bankList.length() == 0)
			return url;
		try {
			for (int i = 0; i < Util.bankList.length(); i++) {
				JSONObject object = Util.bankList.getJSONObject(i);
				if (object.getString("bankname").substring(0, 2)
						.equals(name.substring(0, 2))) {
					return object.getString("iconurl");
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}

	private void getCardName() {
		LogUtil.e("getCardName",
				"method=bankchek&identityid="
						+ MyApplication.getInstance().self.getId() + "&token="
						+ Util.token + "&cardno=" + cardno.getText().toString());
		HttpRequest.sendPost(TLUrl.getInstance().URL_bindBank, "method=bankchek&identityid="
				+ MyApplication.getInstance().self.getId() + "&token="
				+ Util.token + "&cardno=" + cardno.getText().toString(),
				new HttpRevMsg() {

					@Override
					public void revMsg(final String msg) {
						LogUtil.e("getCardName", msg);
						// {"msg":{"bankcode":"CMBCHINA","bankname":"招商银行","cardtype":"1","isvalid":"1"},"status":1}
						handler.post(new Runnable() {

							@Override
							public void run() {
								try {
									JSONObject jsonObject = new JSONObject(msg);
									JSONObject object = jsonObject
											.getJSONObject("msg");
									if (jsonObject.getInt("status") == 1
											&& object.getInt("cardtype") == 1) {
										showCard(object.getString("bankname"));
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

	private void showCard(String cardname) {
		name.setText(cardname);
		String url = getUrl(cardname);
		icon.setVisibility(View.VISIBLE);
		namep.setVisibility(View.INVISIBLE);
		if (!url.equals(""))
			MyApplication.imageLoader.displayImage(url, icon,
					MyApplication.options);
	}

	private void hideCard() {
		name.setText("");
		icon.setVisibility(View.GONE);
		namep.setVisibility(View.VISIBLE);
	}
}
