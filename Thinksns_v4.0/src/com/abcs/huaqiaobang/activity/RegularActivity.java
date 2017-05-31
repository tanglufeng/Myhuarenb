package com.abcs.huaqiaobang.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.Product;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * 活期产品信息
 * 
 * @author xbw
 * @version 创建时间：2015年10月21日 下午6:03:29
 */
public class RegularActivity extends BaseActivity {
	private Product product;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.occft_activity_regular);
		findViewById(R.id.xian2).setVisibility(View.GONE);
		findViewById(R.id.turnout).setVisibility(View.GONE);
		product = (Product) Util.dataRead(getIntent().getStringExtra("info"));

		((TextView) findViewById(R.id.title)).setText("年化收益率");
		((TextView) findViewById(R.id.Investmoney)).setText(Util.df
				.format(product.getEarnings() * 100) + "%");
		((TextView) findViewById(R.id.yitougeshu)).setText("已售出"
				+ product.getSoldMoney() / 100 + "元");
		((TextView) findViewById(R.id.yitoujinqian)).setText("已有"
				+ product.getBuyNum() + "人购买");

		((TextView) findViewById(R.id.tljr_textView2)).setText(product
				.getName());
		if(product.getName().contains("7天")){
			((TextView) findViewById(R.id.special_shuoming)).setText("本产品专门提供给新手体验，只针对第一次注册的用户。给您带来的不便敬请理解和支持！");
		}
		((TextView) findViewById(R.id.qigoujine)).setText("起购金额(元)\n"
				+ product.getBuyMoney() / 100);
		((TextView) findViewById(R.id.touziqixian)).setText("投资期限(天)\n"
				+ product.getNumberOfDays());
		findViewById(R.id.vip).setVisibility(
				product.isVip() ? View.VISIBLE : View.GONE);
		((TextView) findViewById(R.id.lilv)).setText("利率+"
				+ Util.df.format(product.getOverlayEarnings() * 100) + "%");
		changeText(((TextView) findViewById(R.id.qigoujine)));
		changeText(((TextView) findViewById(R.id.touziqixian)));
		findViewById(R.id.regular_btn_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		findViewById(R.id.turnout).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MyApplication.getInstance().self == null) {
					login();
					return;
				}
				try {
					startActivity(new Intent(RegularActivity.this,
							TurnOutActivity.class).putExtra("info",
							Util.getStringByObject(product)));
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		findViewById(R.id.turnin).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MyApplication.getInstance().self == null) {
					login();
					return;
				}
				try {
					startActivity(new Intent(RegularActivity.this,
							TurnInActivity.class).putExtra("info",
							Util.getStringByObject(product)));
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		try {
			LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
			JSONArray array = new JSONArray(product.getItems());
			View vip = findViewById(R.id.vip);
			if (array.length() > 0) {
				layout.removeView(vip);
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					Iterator<String> iterator = object.keys();
					while (iterator.hasNext()) {
						final String key = iterator.next();
						if (!key.equals("islink")) {
							View view = View.inflate(this,
									R.layout.occft_item_info, null);
							TextView name = ((TextView) view
									.findViewById(R.id.name));
							name.setText(key);
							name.setTextColor(getResources().getColor(
									R.color.tljr_text_shense));
							name.setTextSize(18);
							view.setLayoutParams(new LinearLayout.LayoutParams(
									Util.WIDTH, Util.HEIGHT / 12));
							view.setBackgroundColor(getResources().getColor(
									R.color.tljr_white));
							boolean islink = object.getBoolean("islink");
							view.findViewById(R.id.arrow).setVisibility(
									islink ? View.VISIBLE : View.GONE);
							view.findViewById(R.id.value).setVisibility(
									islink ? View.GONE : View.VISIBLE);
							layout.addView(view);
							if (islink) {
								final String value = object.getString(key);
								view.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										Intent intent = new Intent(
												RegularActivity.this,
												WebActivity.class);
										intent.putExtra("url", value);
										intent.putExtra("name", key);
										startActivity(intent);
									}
								});
							} else {
								JSONArray jsonArray = object.getJSONArray(key);
								for (int j = 0; j < jsonArray.length(); j++) {
									JSONObject obj = jsonArray.getJSONObject(j);
									Iterator<String> iterator1 = obj.keys();
									while (iterator1.hasNext()) {
										final String key1 = iterator1.next();
										if (!key1.equals("islink")) {
											final String value1 = obj
													.getString(key1);
											View view1 = View.inflate(this,
													R.layout.occft_item_info,
													null);
											view1.setLayoutParams(new LinearLayout.LayoutParams(
													Util.WIDTH,
													Util.HEIGHT / 15));
											((TextView) view1
													.findViewById(R.id.name))
													.setText(key1);
											boolean islink1 = object
													.getBoolean("islink");
											view1.findViewById(R.id.arrow)
													.setVisibility(
															islink1 ? View.VISIBLE
																	: View.GONE);
											view1.findViewById(R.id.value)
													.setVisibility(
															islink1 ? View.GONE
																	: View.VISIBLE);
											if (islink1) {
												view1.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(View v) {
														Intent intent = new Intent(
																RegularActivity.this,
																WebActivity.class);
														intent.putExtra("url",
																value1);
														intent.putExtra("name",
																key1);
														startActivity(intent);
													}
												});
											} else {
												((TextView) view1
														.findViewById(R.id.value))
														.setText(obj
																.getString(key1));
											}
											layout.addView(view1);
										}
									}
								}
							}
						}
					}

				}
				layout.addView(vip);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void changeText(TextView view) {
		String str = view.getText().toString();
		int index = str.indexOf(")");
		if (index < 0) {
			return;
		}
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		style.setSpan(new AbsoluteSizeSpan(20, true), index + 1, str.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		style.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")),
				index + 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		view.setText(style);

	}
}
