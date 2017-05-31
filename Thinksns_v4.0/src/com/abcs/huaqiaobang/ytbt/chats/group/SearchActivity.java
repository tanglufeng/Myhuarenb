package com.abcs.huaqiaobang.ytbt.chats.group;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.ytbt.adapter.GroupAdapter;
import com.abcs.huaqiaobang.ytbt.adapter.SearchFriendsAdapter;
import com.abcs.huaqiaobang.ytbt.bean.GroupBean;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;
import com.abcs.huaqiaobang.ytbt.util.Tool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private Button search;
	private String flag;
	private EditText ed_mach;
	private ListView searchlistview;
	private LinearLayout linearlayout;
	private ArrayList<GroupBean> groups = new ArrayList<>();
	private String searchname, searchid, mach;
	private SearchFriendsAdapter friendadapter;
	private GroupAdapter groupAdapter;
	ArrayList<User> userlist = new ArrayList<>();
	public Handler myhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				if (friendadapter == null) {
					friendadapter = new SearchFriendsAdapter(
							SearchActivity.this, userlist);
					searchlistview.setAdapter(friendadapter);

				} else {
					friendadapter.notifyDataSetChanged();
				}
				searchlistview
						.setEmptyView(findViewById(R.id.empty_tip_recommend_bind_tv));
			}
			if (msg.what == 1) {
				Toast.makeText(SearchActivity.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();

			}
			if (msg.what == 2) {
				Toast.makeText(SearchActivity.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
			}
			if (msg.what == 3) {
				Toast.makeText(SearchActivity.this, "好友添加成功！",
						Toast.LENGTH_SHORT).show();
			}
			if (msg.what == 4) {
				Toast.makeText(SearchActivity.this, "好友请求发送成功！",
						Toast.LENGTH_SHORT).show();

			}
			if (msg.what == 5) {
				Toast.makeText(SearchActivity.this, "没有相关好友！",
						Toast.LENGTH_SHORT).show();
			}
			if (msg.what == 6) {
				if (groupAdapter == null) {
					groupAdapter = new GroupAdapter(SearchActivity.this, groups,true);
					searchlistview.setAdapter(groupAdapter);
				} else {
					groupAdapter.notifyDataSetChanged();
				}

				searchlistview
						.setEmptyView(findViewById(R.id.empty_tip_recommend_bind_tv));
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_group);
		flag = getIntent().getStringExtra("flag");
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		ImageView back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		search = (Button) findViewById(R.id.search);
		ed_mach = (EditText) findViewById(R.id.mach);
		if (flag.equals("searchfriend")) {
			ed_mach.setHint("好友ID或名称");
		} else {
			ed_mach.setHint("群组ID或名称");
		}
		searchlistview = (ListView) findViewById(R.id.grouplistview);
		searchlistview
				.setEmptyView(findViewById(R.id.empty_tip_recommend_bind_tv));
		searchlistview.setOnItemClickListener(this);
		if (flag.equals("searchfriend")) searchlistview.setOnItemClickListener(this);
		linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
		search.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			if (flag.equals("searchgroup")) {
				setResult(666);
			} else {
				setResult(888);
			}
			finish();
			break;
		case R.id.search:
			mach = ed_mach.getText().toString().trim();
			if (flag.equals("searchfriend")) {
				userlist.clear();
				searchFriends();
			} else {
				groups.clear();
				searchGroup();
			}
			ed_mach.setText("");
		}
	}

	private void searchFriends() {
		Tool.showProgressDialog(SearchActivity.this, "正在查找...",true);
		HttpRequest.sendPost(TLUrl.URL_GET_VOIP + "User/lookupuidnickname",
				"uidnickname=" + mach + "&page=1" + "&size=100",
				new HttpRevMsg() {
					@Override
					public void revMsg(String msg) {
						Log.i("xbb搜索好友", msg);
						try {
							JSONObject jsonObject = new JSONObject(msg);
							if (jsonObject.getInt("status") == 1) {

								JSONArray jsonArray = jsonObject
										.getJSONArray("msg");
								if (jsonArray.length() == 0) {
									myhandler.sendEmptyMessage(5);
									return;
								}

								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject object = jsonArray
											.getJSONObject(i);
									User user = new User();
									user.setNickname(object
											.getString("nickname"));
									user.setAvatar(object.getString("avatar"));
									user.setUid(object.getInt("uid"));
									userlist.add(user);
								}
								myhandler.sendEmptyMessage(0);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							Log.i("xbb搜索好友", e.toString());
							e.printStackTrace();
						} finally {
							Tool.removeProgressDialog();
						}
					}
				});
	}

	private void searchGroup() {
		Log.i("xbb群组", "点击了");
		Log.i("xbb群组", mach + "");
		searchid = "";
		searchname = mach;
		Log.i("xbb群组", searchid + "ID");
		Log.i("xbb群组", searchname + "name");
		Tool.showProgressDialog(SearchActivity.this, "正在查找...",true);
		HttpRequest.sendPost(TLUrl.URL_GET_VOIP + "group/SearchPublicGroups",
				"uid=" + MyApplication.getInstance().getUid() + "&groupId="
						+ mach + "&name=" + searchname, new HttpRevMsg() {
					@Override
					public void revMsg(String msg) {
						Log.i("xbb群组", msg);
						try {
							JSONObject jsonObject = new JSONObject(msg);
							if (jsonObject.getInt("status") == 1) {

								JSONArray jsonArray = jsonObject
										.getJSONArray("msg");
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject object = jsonArray
											.getJSONObject(i);
									GroupBean groupBean = new GroupBean();
									groupBean.setGroupName(object
											.getString("name"));
									groupBean.setGroupId(object
											.getString("groupid"));
									groupBean.setGroupType(object
											.getString("type"));
									groupBean.setGroupPermission(object
											.getString("permission"));
									groupBean.setGroupAvatar(object
											.getString("avatar"));
									groupBean.setGroupDeclared("declared");
									groupBean.setDateCreate(object
											.getString("date"));
									groups.add(groupBean);
								}
								myhandler.sendEmptyMessage(6);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							Log.i("xbb群组", e.toString());
							e.printStackTrace();
						} finally {
							Tool.removeProgressDialog();
						}
					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
			long arg3) {
		if (flag.equals("searchfriend")) {
			addFriends(position);
		} else {
			joinGroup(position);
		}

	}

	private void joinGroup(int position) {
		HttpRequest.sendGet(TLUrl.URL_GET_VOIP + "group/JoinGroup",
				"uid=" + MyApplication.getInstance().getUid() + "&groupId="
						+ groups.get(position).getGroupId() + "&declared="
						+ searchname, new HttpRevMsg() {
					@Override
					public void revMsg(String msg) {
						Log.i("xbb加入群组", msg);
						try {
							JSONObject jsonObject = new JSONObject(msg);
							String info = jsonObject.getString("msg");
							Message message = myhandler.obtainMessage(1, info);
							myhandler.sendMessage(message);
							if (jsonObject.getInt("status") == 1) {
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block

							e.printStackTrace();
						} finally {
							// Tool.removeProgressDialog();
						}
					}
				});

	}

	protected void addFriends(int position) {
		Log.i("xbb", MyApplication.getInstance().getUid() + "");
		HttpRequest.sendGet(TLUrl.URL_GET_VOIP + "User/addMongoDBfrienduser",
				"uid=" + MyApplication.getInstance().getUid() + "&frienduid="
						+ userlist.get(position).getUid(), new HttpRevMsg() {
					@Override
					public void revMsg(String msg) {
						Log.i("xbb3", msg);
						if (msg.length() <= 0) {
							return;
						}
						try {
							JSONObject jsonObject = new JSONObject(msg);
							if (jsonObject.getInt("status") == 1) {
								Message message = myhandler.obtainMessage(4,
										jsonObject.getString("msg"));
								myhandler.sendMessage(message);
							} else {
								Message message = myhandler.obtainMessage(2,
										jsonObject.getString("msg"));
								myhandler.sendMessage(message);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							Log.i("xbb2", e.toString());
							e.printStackTrace();
						}
					}
				});
	}
}
