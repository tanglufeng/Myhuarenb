package com.abcs.huaqiaobang.ytbt.im;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.ytbt.adapter.GridViewAdapter;
import com.abcs.huaqiaobang.ytbt.bean.LabelBean;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.abcs.huaqiaobang.ytbt.view.MyGridView;
import com.abcs.huaqiaobang.ytbt.voicemeeting.FriendsListActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LabelDetailActivity extends BaseActivity implements
		OnClickListener, OnItemLongClickListener, OnItemClickListener {
	private LabelBean bean;
	private EditText et;
	private MyGridView gv;
	private int ADD_MEMBER_REQUEST = 100;
	private ArrayList<User> members = new ArrayList<>();
	private GridViewAdapter adapter;
	private TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_label_detail);
		bean = (LabelBean) getIntent().getSerializableExtra("label");
		if (bean == null) {
			finish();
			return;
		}
		initView();
		getAllMembers();
	}

	private void getAllMembers() {
		Tool.showProgressDialog(LabelDetailActivity.this, "正在加载...",true);
		HttpUtils httpUtils = new HttpUtils(30000);
		httpUtils.configCurrentHttpCacheExpiry(1000);
		String url = TLUrl.URL_GET_VOIP + "User/findgroupingfrienduser?uid="
				+ MyApplication.getInstance().getUid() + "&grouping="
				+ bean.getId();
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Tool.removeProgressDialog();
				Tool.showInfo(LabelDetailActivity.this, "网络异常");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Tool.removeProgressDialog();
				String resutl = arg0.result;
				try {
					JSONObject object = new JSONObject(resutl);
					if (object.getInt("status") == 1) {
						JSONArray array = object.getJSONArray("msg");
						if (array.length() == 0) {
							return;
						}
						members.clear();
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							User u = new User();
							u.setAvatar(obj.getString("avatar"));
							u.setUid(Integer.parseInt(obj
									.getString("frienduid")));
							u.setNickname(obj.getString("nickname"));
							u.setRemark(obj.getString("remarks"));
							u.setVoipAccout(obj.getString("voipAccount"));
							members.add(u);
						}
						loadData();
					} else {
						Tool.showInfo(LabelDetailActivity.this, object.optString("msg", "加载成员失败"));
					}
				} catch (Exception e) {
					e.printStackTrace();
					Tool.showInfo(LabelDetailActivity.this, "加载成员失败");
				}
			}
		});
	}

	private void loadData() {
		if (adapter == null) {
			adapter = new GridViewAdapter(LabelDetailActivity.this, members);
			gv.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
	}

	private void initView() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.add).setOnClickListener(this);
		findViewById(R.id.bt_numOK).setOnClickListener(this);
		findViewById(R.id.delete).setOnClickListener(this);
		et = (EditText) findViewById(R.id.et_name);
		gv = (MyGridView) findViewById(R.id.membergridview);
		gv.setEmptyView(findViewById(R.id.empty_tip_recommend_bind_tv));
		gv.setOnItemClickListener(this);
		gv.setOnItemLongClickListener(this);
		title = (TextView) findViewById(R.id.title);
		title.setText(bean.getLabelName());
		et.setText(bean.getLabelName());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.add:
			Intent intent = new Intent(LabelDetailActivity.this,
					FriendsListActivity.class);
			startActivityForResult(intent, ADD_MEMBER_REQUEST);
			break;
		case R.id.bt_numOK:
			updateLabelName();
			break;
		case R.id.delete:
			deleteLabel();
			break;
		}
	}

	private void deleteLabel() {
		String url = TLUrl.URL_GET_VOIP + "User/deletegrouping?uid="
				+ MyApplication.getInstance().getUid() + "&grouping="
				+ bean.getId();
		HttpUtils httpUtils = new HttpUtils(30000);
		httpUtils.configCurrentHttpCacheExpiry(1000);
		Tool.showProgressDialog(LabelDetailActivity.this, "正在删除...",true);
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Tool.removeProgressDialog();
				Tool.showInfo(LabelDetailActivity.this, "删除失败");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Tool.removeProgressDialog();
				String result = arg0.result;
				try {
					JSONObject object = new JSONObject(result);
					if (object.getInt("status") == 1) {
						Tool.showInfo(LabelDetailActivity.this, "删除成功");
						MyApplication.dbUtils.deleteById(LabelBean.class,
								bean.getLabelName());
						sendBroadcast(new Intent(
								LabelListActivity.ACTION_LABEL_CHANGED));
						finish();
					} else
						Tool.showInfo(LabelDetailActivity.this, "删除失败");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void updateLabelName() {
		final String name = et.getText().toString().trim();
		if (TextUtils.isEmpty(name) || name.equals(bean.getLabelName())) {
			Tool.showInfo(this, "请输入更改名称");
			return;
		}
		String url = TLUrl.URL_GET_VOIP + "User/updategroupingname";
		HttpUtils httpUtils = new HttpUtils(30000);
		httpUtils.configCurrentHttpCacheExpiry(1000);
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", MyApplication.getInstance().getUid()
				+ "");
		params.addBodyParameter("grouping", bean.getId());
		params.addBodyParameter("groupingname", name);
		Tool.showProgressDialog(LabelDetailActivity.this, "正在修改...",true);
		httpUtils.send(HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Tool.removeProgressDialog();
						Tool.showInfo(LabelDetailActivity.this, "修改失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						Tool.removeProgressDialog();
						String result = arg0.result;
						try {
							JSONObject object = new JSONObject(result);
							if (object.getInt("status") == 1) {
								Tool.showInfo(LabelDetailActivity.this, "修改成功");
								bean.setLabelName(name);
								title.setText(name);
								MyApplication.dbUtils.saveOrUpdate(bean);
								sendBroadcast(new Intent(
										LabelListActivity.ACTION_LABEL_CHANGED));
							} else {
								Tool.showInfo(LabelDetailActivity.this, "修改失败");
							}
						} catch (Exception e) {
							Tool.showInfo(LabelDetailActivity.this, "修改失败");
						}

					}
				});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ADD_MEMBER_REQUEST && resultCode == 2) {
			String nums = data.getStringExtra("uids");
			Log.i("xbb邀请加入群", nums);
			if (nums != null) {
				// String[] phones = nums.split(",");
				addMember(nums);
				ArrayList<User> users = (ArrayList<User>) data
						.getSerializableExtra("users");
				for (User user : users) {
					if (!members.contains(user)) {
						members.add(user);
					}
				}
				loadData();
			}
		}
	}

	private void addMember(String phones) {
		String url = TLUrl.URL_GET_VOIP + "User/joingroup";
		HttpUtils httpUtils = new HttpUtils(30000);
		httpUtils.configCurrentHttpCacheExpiry(1000);
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", MyApplication.getInstance().getUid()
				+ "");
		params.addBodyParameter("frienduid", phones);
		params.addBodyParameter("grouping", bean.getId());
		Tool.showProgressDialog(LabelDetailActivity.this, "正在提交...",true);
		httpUtils.send(HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Tool.removeProgressDialog();
						Tool.showInfo(LabelDetailActivity.this, "提交失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						Tool.removeProgressDialog();
						String result = arg0.result;
						try {
							JSONObject object = new JSONObject(result);
							if (object.getInt("status") == 1) {
								Tool.showInfo(LabelDetailActivity.this, "添加成功");

							} else {
								Tool.showInfo(LabelDetailActivity.this, "添加失败");
							}
						} catch (Exception e) {
							Tool.showInfo(LabelDetailActivity.this, "添加失败");
						}

					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		User u = members.get(position);
		Intent intent = new Intent(LabelDetailActivity.this,
				FriendDetailsActivity.class);
		intent.putExtra("friend", u);
		startActivity(intent);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		deleteMember(members.get(position));
		return true;
	}

	private void deleteMember(final User user) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("请确认")
				.setMessage(
						"您确定要移除"
								+ (user.getRemark().trim().equals("") ? user
										.getNickname() : user.getRemark())+"?")
				.setPositiveButton("取消", null);
		builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// ProgressDlgUtil.showProgressDlg("修改中", activity);
				String url = TLUrl.URL_GET_VOIP + "User/outgruoping";
				HttpUtils httpUtils = new HttpUtils(30000);
				httpUtils.configCurrentHttpCacheExpiry(1000);
				RequestParams params = new RequestParams();
				params.addBodyParameter("grouping", bean.getId());
				params.addBodyParameter("grouping", user.getUid() + "");
				Tool.showProgressDialog(LabelDetailActivity.this, "正在移除...",true);
				httpUtils.send(HttpMethod.POST, url, params,
						new RequestCallBack<String>() {

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								Tool.removeProgressDialog();
								Tool.showInfo(LabelDetailActivity.this, "移除失败");
							}

							@Override
							public void onSuccess(ResponseInfo<String> arg0) {
								Tool.removeProgressDialog();
								String result = arg0.result;
								try {
									JSONObject object = new JSONObject(result);
									if (object.getInt("status") == 1) {
										Tool.showInfo(LabelDetailActivity.this,
												"移除成功");
										members.remove(user);
										loadData();
									} else {
										Tool.showInfo(
												LabelDetailActivity.this,
												"移除失败"
														+ object.getString("msg"));
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						});
			}
		});
		builder.show();
	}
}
