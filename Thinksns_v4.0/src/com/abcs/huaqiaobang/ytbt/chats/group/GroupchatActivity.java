package com.abcs.huaqiaobang.ytbt.chats.group;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.ytbt.adapter.GroupAdapter;
import com.abcs.huaqiaobang.ytbt.bean.GroupBean;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.chats.ChattingActivity;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;
import com.abcs.huaqiaobang.ytbt.util.Tool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupchatActivity extends Activity implements OnClickListener {

	private ListView groudlistview;
	static List<GroupBean> groups = new ArrayList<>();
	private GroupAdapter adapter;
	public Handler myhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				Toast.makeText(GroupchatActivity.this, "你还没有加入任何群组",
						Toast.LENGTH_SHORT).show();
			}
			if (msg.what == 1) {
//				adapter = new GroupAdapter(GroupchatActivity.this, groups);
				groudlistview.setAdapter(adapter);
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groupchat);
		ImageView more = (ImageView) findViewById(R.id.more);
		more.setOnClickListener(this);
		groudlistview = (ListView) findViewById(R.id.groudlistview);
		initGroup();

		groudlistview.setOnItemClickListener(listener);
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int posion,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(GroupchatActivity.this,
					ChattingActivity.class);
			User user = new User();
			user.setVoipAccout(groups.get(posion).getGroupId());
			intent.putExtra("isgroup", true);
			// intent.putExtra("name",groups.get(posion).getGroupId());
			Bundle bundle = new Bundle();
			bundle.putSerializable("friend", user);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};

	private void initGroup() {
		HttpRequest.sendGet(TLUrl.URL_GET_VOIP + "member/QueryGroup", "uid="
				+ MyApplication.getInstance().getUid(), new HttpRevMsg() {

			@Override
			public void revMsg(String msg) {
				Log.i("xbb我的群组", msg);
				try {
					JSONObject jsonObject = new JSONObject(msg);
					JSONObject groupObject = jsonObject.getJSONObject("msg");
					if (jsonObject.getInt("status") == 1) {
						groups.clear();
						JSONArray jsonArray = groupObject
								.getJSONArray("GetPublicGroups");
						if (jsonArray.length() == 0) {
							myhandler.sendEmptyMessage(0);
							return;
						}
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject object = jsonArray.getJSONObject(i);
							GroupBean groupBean = new GroupBean();
							groupBean.setGroupName(object.getString("name"));
							groupBean.setGroupId(object.getString("groupId"));
							groupBean.setDateCreate(object
									.getString("dateCreated"));
							groupBean.setGroupType(object.getString("type"));
							groupBean.setGroupPermission(object
									.getString("permission"));
							groups.add(groupBean);
						}
						myhandler.sendEmptyMessage(1);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.i("xbb加入群组", e.toString());
					e.printStackTrace();
				} finally {
					Tool.removeProgressDialog();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.more:
			showPopupWindow(v);
			break;
		}
	}

	private void showPopupWindow(View view) {

		View contentView = LayoutInflater.from(this).inflate(
				R.layout.pop_window, null);
		PopupWindow popupWindow = new PopupWindow(contentView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		Button searchgroup = (Button) contentView
				.findViewById(R.id.searchgroup);
		searchgroup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GroupchatActivity.this,
						SearchActivity.class);
				startActivityForResult(intent, 2);
				// showDialog("创建群组", "", false);
			}
		});
		Button creategroup = (Button) contentView
				.findViewById(R.id.creategroup);
		creategroup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GroupchatActivity.this,
						CreateGroupActivity.class);
				startActivityForResult(intent, 1);
			}
		});

		popupWindow.setTouchable(true);
		popupWindow.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.backgroud_blue));
		popupWindow.showAsDropDown(view);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && requestCode == 1) {
			initGroup();
		}
	}

	private void showDialog(String title, String msg, boolean needNegativeButton) {
		Builder dialog = new Builder(GroupchatActivity.this).setTitle(title)
				.setMessage(msg).setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() { // 设置确定按钮
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
		;

		if (needNegativeButton) {
			dialog.setNegativeButton("取消", null);
		}
		dialog.create().setCanceledOnTouchOutside(false);
		dialog.create().show();
	}

}
