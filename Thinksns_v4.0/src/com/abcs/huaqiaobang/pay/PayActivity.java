package com.abcs.huaqiaobang.pay;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.TurnInActivity;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.PayWay;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.activity.StartActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * @author xbw
 * @version 创建时间：2015年12月2日 上午10:54:34
 */
public class PayActivity extends BaseActivity implements OnClickListener {
	private String id, money;

	private static ArrayList<PayWay> list_payway = new ArrayList<PayWay>();

	private ImageLoader imageLoader;

	private LinearLayout other_pay_layout;

	private LayoutInflater inflater;

	private LinearLayout yibao_pay;

	// private ForResult listener;
	// private TurnInActivity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		id = getIntent().getStringExtra("id");
		money = getIntent().getStringExtra("money");

		// activity = (TurnInActivity) getIntent().getSerializableExtra("act");
		// Log.i("tga",activity.toString());
		// listener =(ForResult) activity;

		setContentView(R.layout.occft_activity_pay);
		// ((TextView) findViewById(R.id.money)).setText("￥" + money + "元");
		findViewById(R.id.back).setOnClickListener(this);

		other_pay_layout = (LinearLayout) findViewById(R.id.other_pay);
		yibao_pay = (LinearLayout) findViewById(R.id.yibao_pay);

		inflater = getLayoutInflater();

		initPayforWay();

		AliPay.getInstance().init(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	private void initPayforWay() {
		// TODO Auto-generated method stub

		loadPayData(new Complete() {

			@Override
			public void complete() {
				// TODO Auto-generated method stub

				for (int i = 0; i < list_payway.size(); i++) {
					View view = inflater.inflate(
							R.layout.occft_activity_pay_item, null);
					
					final int index=i;
					view.findViewById(R.id.pay).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View arg0) {

									Log.i("tga", "=====click pay");
									MyApplication.getInstance().cardname=list_payway.get(index).getName();
									MyApplication.getInstance().carddesc=list_payway.get(index).getSub_title();
									TurnInActivity.type=list_payway.get(index).getType();
									finish();
								}
							});
					((TextView) view.findViewById(R.id.pay_name))
							.setText(list_payway.get(i).getName());

					((TextView) view.findViewById(R.id.pay_desc))
							.setText(list_payway.get(i).getSub_title());

					imageLoader.displayImage(list_payway.get(i).getImg_url(),
							(ImageView) view.findViewById(R.id.pay_icon),
							StartActivity.options);

					other_pay_layout.addView(view);
				}

			}
		});
		// 用户绑定的银行，显示支付

//		JSONArray banks = MyApplication.getInstance().self.getBanks();
//
//		for (int i = 0; i < banks.length(); i++) {
//			View view = inflater.inflate(R.layout.occft_activity_yibaopay_item,
//					null);
//			TextView carname = (TextView) view.findViewById(R.id.car_name);
//
//
//			final int position=i;
//			view.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					TurnInActivity.index=position;
//					TurnInActivity.type=0;
//					finish();
//					MyApplication.cardname=null;
//
//				}
//			});
//
//			try {
//				carname.setText(MyApplication.getInstance().self.getBanks()
//						.getJSONObject(i).getString("card_name")
//						+ "("
//						+ MyApplication.getInstance().self.getBanks()
//								.getJSONObject(i).getString("card_last") + ")");
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			yibao_pay.addView(view);
//		}

	}

	private void loadPayData(final Complete complete) {
		imageLoader = ImageLoader.getInstance();

		RequestQueue newRequestQueue = Volley.newRequestQueue(this);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				TLUrl.getInstance().URL_goods_base+"/finance/PayWayServlet", null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						try {
							if (response.getInt("status") == 1) {

								JSONArray array_payway = response
										.getJSONArray("msg");
								list_payway.clear();

								for (int i = 0; i < array_payway.length(); i++) {

									String name = array_payway.getJSONObject(i)
											.getString("name");
									String desc = array_payway.getJSONObject(i)
											.getString("desc");
									String img_url = array_payway
											.getJSONObject(i).getString(
													"iconurl");
									PayWay payWay = new PayWay(name, desc,
											img_url);
									payWay.setType(array_payway
											.getJSONObject(i).getInt(
													"type"));
									// linear_pay_list.get(i).setVisibility(
									// View.VISIBLE);
									//
									list_payway.add(payWay);

									// list_payway.add(payWay);
								}

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							complete.complete();
						}

					}
				}, null);

		newRequestQueue.add(jsonObjectRequest);

	}

	private void alipay() {
		HttpRequest.sendPost(TLUrl.getInstance().URL_alipay, "id=" + id, new HttpRevMsg() {

			@Override
			public void revMsg(String msg) {
				try {
					JSONObject jsonObject = new JSONObject(msg);
					if (jsonObject.getInt("status") == 1) {
						AliPay.getInstance().pay(jsonObject.getString("msg"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		// case R.id.alipay:
		// alipay();
		// break;

		default:
			break;
		}
	}

}
