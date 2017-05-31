package com.abcs.huaqiaobang.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.XListView;

/**
 *
 * @author xbw
 * @version 创建时间：2015年10月29日 下午3:00:03
 */
public class CurrentGainActivity extends BaseActivity {
	private XListView mListView;
	private BaseAdapter adapter;
	private Handler handler;
	private ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	private ArrayList<JSONObject> mList = new ArrayList<JSONObject>();
	private int page = 1;
	private int size = 15;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.occft_activity_currentgain);
		findViewById(R.id.tljr_img_confirm_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		initView();
	}

	private void initView() {
		handler = new Handler();
		mListView = (XListView) findViewById(R.id.list_view);
		mListView.setPullRefreshEnable(false);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(false);
		mListView.setXListViewListener(new XListView.IXListViewListener() {

			@Override
			public void onRefresh() {
			}

			@Override
			public void onLoadMore() {
				getTen();
			}
		});
		mListView.setRefreshTime(getTime());
		adapter = new BaseAdapter() {

			@Override
			public View getView(int position, View v, ViewGroup parent) {
				JSONObject object = list.get(position);
				if (v == null) {
					v = View.inflate(CurrentGainActivity.this,
							R.layout.occft_item_tengain, null);
					v.setLayoutParams(new AbsListView.LayoutParams(
							LayoutParams.FILL_PARENT, Util.HEIGHT / 15));
				}
				try {
					((TextView) v.findViewById(R.id.item_tengain_time))
							.setText(Util.getDateOnlyDat(object.getLong("date")));
					((TextView) v.findViewById(R.id.item_tengain_gain))
							.setText(object.getDouble("thousandsOfIncome") + "");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return v;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return list.size();
			}
		};
		mListView.setAdapter(adapter);
		getTen();
	}

	private void getTen() {
		ProgressDlgUtil.showProgressDlg("", this);
		LogUtil.e("getLiveProductThousandsOfIncome",
				"method=getLiveProductThousandsOfIncome&page=" + page
						+ "&size=" + size);
		page++;
		HttpRequest.sendPost(TLUrl.getInstance().URL_productServlet,
				"method=getLiveProductThousandsOfIncome&page=" + page
						+ "&size=" + size, new HttpRevMsg() {

					@Override
					public void revMsg(String msg) {
						ProgressDlgUtil.stopProgressDlg();
						try {
							JSONObject jsonObject = new JSONObject(msg);
							if (jsonObject.getInt("status") == 1) {
								JSONArray array = jsonObject
										.getJSONArray("msg");
								for (int i = 0; i < array.length(); i++) {
									JSONObject object = array.getJSONObject(i);
									mList.add(object);
								}
								handler.post(new Runnable() {

									@Override
									public void run() {
										list.clear();
										list.addAll(mList);
										adapter.notifyDataSetChanged();
										onLoad();
									}
								});
								if (array.length() == 0) {
									mListView.setPullLoadEnable(false);
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(getTime());
	}

	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}
}
