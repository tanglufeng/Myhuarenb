package com.abcs.huaqiaobang.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.EnterPwdDialog;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author xbw
 * @version 创建时间：2015年10月30日 下午12:05:43
 */
public class MyBankActivity extends BaseActivity {
	private LinearLayout layout;
	private Handler handler = new Handler();
	private PopupWindow popupWindow;
	private JSONObject nowBank;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.occft_activity_mybank);
		findViewById(R.id.mybank_btn_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		findViewById(R.id.mybank_set).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSet();
			}
		});
		layout = (LinearLayout) findViewById(R.id.mybank_list);
		if (MyApplication.getInstance().self.getBanks() == null
				|| MyApplication.getInstance().self.getBanks().length() == 0) {
			getUserBanks();
		} else {
			getMyBank();
		}
	}

	public void getUserBanks() {
		LogUtil.e("getUserBanks", "method=requestBanks&identityid="
				+ MyApplication.getInstance().self.getId() + "&token="
				+ Util.token);
		HttpRequest.sendPost(TLUrl.getInstance().URL_bindBank,
				"method=requestBanks&identityid="
						+ MyApplication.getInstance().self.getId() + "&token="
						+ Util.token, new HttpRevMsg() {

					@Override
					public void revMsg(String msg) {
						LogUtil.e("getUserBanks", msg);
						try {
							JSONObject jsonObject = new JSONObject(msg);
							if (jsonObject.getInt("status") == 1) {
								MyApplication.getInstance().self
										.setBanks(jsonObject
												.getJSONArray("msg"));
								handler.post(new Runnable() {

									@Override
									public void run() {
										getMyBank();
									}
								});
							} else {
								handler.post(new Runnable() {

									@Override
									public void run() {
										showToast("获取银行卡信息失败,请返回重试!");
									}
								});
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void getMyBank() {
		JSONArray array = MyApplication.getInstance().self.getBanks();
		try {
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				nowBank = object;
				View view = View.inflate(this, R.layout.occft_item_bank, null);
				((TextView) view.findViewById(R.id.item_bank_number))
						.setText(object.getString("card_top") + "******"
								+ object.getString("card_last"));
				((TextView) view.findViewById(R.id.item_bank_name))
						.setText(object.getString("card_name"));
				display(object.getString("card_name"), view);
				layout.addView(view);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void display(String name, View view) {
		if (Util.bankList == null || Util.bankList.length() == 0)
			return;
		try {
			for (int i = 0; i < Util.bankList.length(); i++) {
				JSONObject object = Util.bankList.getJSONObject(i);
				if (object.getString("bankname").substring(0, 2)
						.equals(name.substring(0, 2))) {
					if (!object.getString("iconurl").equals(""))
						MyApplication.imageLoader.displayImage(object
								.getString("iconurl"), (ImageView) view
								.findViewById(R.id.item_bank_icon),
								MyApplication.options);
					if (!object.getString("bgurl").equals(""))
						MyApplication.imageLoader.displayImage(object
								.getString("bgurl"), (ImageView) view
								.findViewById(R.id.item_bank_bj),
								MyApplication.options);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showSet() {
		if (popupWindow == null) {
			View view = View.inflate(this, R.layout.view_removebank, null);
			view.findViewById(R.id.view_removebank).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							popupWindow.dismiss();
							removeBank();
						}
					});
			view.findViewById(R.id.view_cancel).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							popupWindow.dismiss();
						}
					});
			popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT, true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.setOutsideTouchable(true);
			popupWindow
					.setAnimationStyle(R.style.AnimBottom);
			popupWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					setAlpha(1f);
				}
			});
		}
		popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,
				0, 0);
		setAlpha(0.8f);
	}

	private void setAlpha(float f) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = f;
		lp.dimAmount = f;
		getWindow().setAttributes(lp);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}

	private void removeBank() {
		new EnterPwdDialog(this, "解绑银行卡", nowBank.optString("card_name"), "",
				new HttpRevMsg() {

					@Override
					public void revMsg(String msg) {
						ProgressDlgUtil
								.showProgressDlg("", MyBankActivity.this);
						LogUtil.e(
								"removeBank",
								"method=unbundling&identityid="
										+ MyApplication.getInstance().self
												.getId() + "&token="
										+ Util.token + "&bindid="
										+ nowBank.optString("bindid"));
						HttpRequest.sendPost(
								TLUrl.getInstance().URL_bindBank,
								"method=unbundling&identityid="
										+ MyApplication.getInstance().self
												.getId() + "&token="
										+ Util.token + "&bindid="
										+ nowBank.optString("bindid"),
								new HttpRevMsg() {

									@Override
									public void revMsg(final String msg) {
										ProgressDlgUtil.stopProgressDlg();
										LogUtil.e("removeBank result", msg);
										handler.post(new Runnable() {

											@Override
											public void run() {
												try {
													JSONObject jsonObject = new JSONObject(
															msg);
													if (jsonObject
															.getInt("status") == 1) {
														showToast("解绑成功");
														finish();
														MyApplication
																.getInstance()
																.getMainActivity().mHandler
																.sendEmptyMessage(901);
													} else {
														showToast(jsonObject
																.getString("msg"));
													}
												} catch (JSONException e) {
													e.printStackTrace();
												}
											}
										});
									}
								});
					}
				}).show();
	}
}
