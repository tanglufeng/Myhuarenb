package com.abcs.huaqiaobang.ytbt.chats.group;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.ytbt.adapter.GridViewAdapter;
import com.abcs.huaqiaobang.ytbt.bean.ConversationBean;
import com.abcs.huaqiaobang.ytbt.bean.GroupBean;
import com.abcs.huaqiaobang.ytbt.bean.GroupMemberBean;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.emotion.DisplayUtils;
import com.abcs.huaqiaobang.ytbt.im.FriendDetailsActivity;
import com.abcs.huaqiaobang.ytbt.settings.QRCodeUtil;
import com.abcs.huaqiaobang.ytbt.settings.QR_ADD_ACtivity;
import com.abcs.huaqiaobang.ytbt.util.JsonUtil;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.abcs.huaqiaobang.ytbt.view.MyGridView;
import com.abcs.huaqiaobang.ytbt.voicemeeting.FriendsListActivity;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GroupDetailsActivity extends BaseActivity implements
		OnClickListener, AdapterView.OnItemClickListener {
	private String groupId;
	private TextView groupname, count, chattingname;
	private String owner, groupName;
	private Button quit, invite;
	private GridViewAdapter adapter;
	private MyGridView membergridview;
	private ArrayList<User> members;
	private GroupBean groupBean;
	List<String> uids = new ArrayList<>();
	private Boolean isaccept = false;
	private SharedPreferences preferences;
	private GroupMemberBean memberBean;
	private boolean isSilence;
	public Handler myhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				groupName = groupBean.getGroupName();
				if (groupBean.getGroupOwner().equals(
						MyApplication.getInstance().getUid())) {
					quit.setText("解散群组");
					invite.setVisibility(View.VISIBLE);
				}
				groupname.setText(groupName);
			}
			if (msg.what == 1) {
				try {
					GroupMemberBean member = MyApplication.dbUtils.findById(
							GroupMemberBean.class, groupId);
					if (member != null) {
						member.setMembers(JsonUtil.toString(members));
					} else {
						member = new GroupMemberBean();
						member.setGroupid(groupId);
						member.setMembers(JsonUtil.toString(members));
						MyApplication.dbUtils.saveOrUpdate(member);
					}
					// GroupMemberBean
					// member=MyApplication.dbUtils.findById(GroupMemberBean.class,
					// friend.getVoipAccout());
					Log.i("xbb群组加入消息", member.getMembers() + "加入群组");
				} catch (Exception e) {
					e.printStackTrace();
				}
				loadData();
			}
			if (msg.what == 2) {
				try {
					MyApplication.dbUtils.deleteById(ConversationBean.class,
							MyApplication.getInstance().getUserBean()
									.getVoipAccount()
									+ groupId);
					MyApplication.dbUtils.deleteById(GroupBean.class, groupId);
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent intent = new Intent();
				intent.setAction("com.abcs.mybc.action.group");
				intent.putExtra("bc", groupId);
				intent.putExtra("group", groupBean);
				GroupDetailsActivity.this.sendBroadcast(intent);
				GroupDetailsActivity.this.finish();
				sendBroadcast(new Intent("action_con_unread"));
			}
			if (msg.what == 3) {
				Toast.makeText(GroupDetailsActivity.this, "成员加入成功！",
						Toast.LENGTH_SHORT).show();
				ArrayList<User> list = (ArrayList<User>) msg.obj;
				for (User user : list) {
					if (!members.contains(user)) {
						members.add(user);
					}
				}
				memberBean.setMembers(JsonUtil.toString(members));
				loadData();
			}

		}

	};
	private ImageView back;
	private ToggleButton msg;
	private TextView notice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_details);
		preferences = getSharedPreferences("user", MODE_PRIVATE);
		groupId = getIntent().getStringExtra("groupid");
		groupBean = (GroupBean) getIntent().getSerializableExtra("group");
		isSilence = preferences.getBoolean(groupId, false);
		memberBean = (GroupMemberBean) getIntent().getSerializableExtra(
				"member");
		initView();
		try {
			if (memberBean == null) {
				memberBean = MyApplication.dbUtils.findById(
						GroupMemberBean.class, groupId);
			}
			members = JsonUtil.parseString(memberBean.getMembers());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (members == null) {
			initGroupMember();
		} else if (groupBean == null) {
			initGroup();
		} else {
			loadData();
			if (groupBean.getGroupOwner().equals(
					MyApplication.getInstance().getUid() + "")) {
				quit.setText("解散群组");
				invite.setVisibility(View.VISIBLE);
			}
		}

	}

	private void initView() {
		msg = (ToggleButton) findViewById(R.id.btn_msg);
		msg.setChecked(isSilence);
		msg.setOnClickListener(this);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		findViewById(R.id.QR_CODE).setOnClickListener(this);
		chattingname = (TextView) findViewById(R.id.chattingname);
		groupname = (TextView) findViewById(R.id.groupname);
		chattingname.setText(getIntent().getStringExtra("name"));
		groupname.setText(getIntent().getStringExtra("name"));
		membergridview = (MyGridView) findViewById(R.id.membergridview);
		membergridview.setOnItemClickListener(this);
		count = (TextView) findViewById(R.id.count);
		notice = (TextView) findViewById(R.id.notice);
		quit = (Button) findViewById(R.id.quit);
		invite = (Button) findViewById(R.id.invite);
		quit.setOnClickListener(this);
		invite.setOnClickListener(this);
		notice.setText(groupBean.getGroupDeclared().equals("declared") ? ""
				: groupBean.getGroupDeclared());
	}

	private void loadData() {
		count.setText("成员个数(" + members.size() + ")");
		if (adapter == null) {
			adapter = new GridViewAdapter(GroupDetailsActivity.this, members);
			membergridview.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
	}

	private void initGroup() {
		Tool.showProgressDialog(this, "正在加载成员...", false);
		HttpRequest.sendGet(TLUrl.URL_GET_VOIP + "group/QueryGroupDetail",
				"uid=" + MyApplication.getInstance().getUid() + "&groupId="
						+ groupId, new HttpRevMsg() {
					@Override
					public void revMsg(String msg) {
						Log.i("xbb群组详情", msg);
						try {
							JSONObject jsonObject = new JSONObject(msg);
							if (jsonObject.getInt("status") == 1) {
								JSONObject object = jsonObject
										.getJSONObject("msg");
								groupBean = new GroupBean();
								groupBean.setDateCreate(object
										.getString("date"));
								groupBean.setGroupDeclared(object
										.getString("declared"));
								groupBean.setGroupName(object.getString("name"));
								groupBean.setGroupOwner(object.getString("uid"));
								groupBean.setGroupPermission(object
										.getString("permission"));
								groupBean.setMemberInGroup(object
										.getString("count"));
								groupBean.setGroupAvatar(object
										.getString("avatar"));
								groupBean.setGroupId(object
										.getString("groupid"));
								MyApplication.dbUtils.saveOrUpdate(groupBean);
								myhandler.sendEmptyMessage(0);
							}
						} catch (Exception e) {
							Log.i("xbb群组详情", e.toString());
							e.printStackTrace();
						} finally {
							Tool.removeProgressDialog();
						}
					}
				});
	}

	private void initGroupMember() {
		HttpRequest.sendGet(TLUrl.URL_GET_VOIP + "member/QueryMember", "uid="
				+ MyApplication.getInstance().getUid() + "&groupId=" + groupId
				+ "&page=1&size=1000", new HttpRevMsg() {
			@Override
			public void revMsg(String msg) {
				Log.i("xbb群组成员", msg);
				try {
					JSONObject jsonObject = new JSONObject(msg);
					JSONArray jsonArray = jsonObject.getJSONArray("msg");
					if (members == null) {
						members = new ArrayList<>();
					} else {
						members.clear();
					}
					if (jsonObject.getInt("status") == 1) {
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject object = jsonArray.getJSONObject(i);
							User user = new User();
							user.setAvatar(object.getString("avatar"));
							user.setNickname(object.getString("nickname"));
							user.setUid(object.getInt("uid"));
							members.add(user);
						}
						myhandler.sendEmptyMessage(1);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.i("xbb群组成员", e.toString());
					e.printStackTrace();
				} finally {

				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.QR_CODE:
			showQRCode();
			break;
		case R.id.quit:
			if (quit.getText().toString().equals("解散群组")) {
				showDeleDailog(1);
				// quitGroup("group/DeleteGroup");
			} else {
				showDeleDailog(2);
				// quitGroup("group/LogoutGroup");
			}
			break;
		case R.id.invite:
			Intent intent = new Intent(GroupDetailsActivity.this,
					FriendsListActivity.class);
			startActivityForResult(intent, 300);
			break;

		case R.id.back:
			finish();
			break;
		case R.id.btn_msg:
			isSilence = !isSilence;
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean(groupId, isSilence);
			editor.commit();
			msg.setChecked(isSilence);
		}
	}

	private void showDeleDailog(final int position) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(
				GroupDetailsActivity.this); // 先得到构造器
		builder.setTitle(position == 1 ? "解散群组" : "退出群组"); // 设置标题
		builder.setMessage("真的要删除" + (position == 1 ? "解散群组" : "退出群组") + "吗?"); // 设置内容
		// builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // 设置确定按钮
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (position == 1) {
							quitGroup("group/DeleteGroup");
						} else {
							quitGroup("group/LogoutGroup");
						}
						dialog.dismiss(); // 关闭dialog

					}
				});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // 设置取消按钮
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});
		// 参数都设置完成了，创建并显示出来
		builder.create().show();
	}

	private void quitGroup(String pamster) {
		Tool.showProgressDialog(this, "正在提交...",false);
		HttpRequest.sendGet(TLUrl.URL_GET_VOIP + pamster, "uid="
				+ MyApplication.getInstance().getUid() + "&groupId=" + groupId,
				new HttpRevMsg() {
					@Override
					public void revMsg(String msg) {
						Log.i("xbb我的群组", msg);
						try {
							JSONObject jsonObject = new JSONObject(msg);
							if (jsonObject.getInt("status") == 1) {
								myhandler.sendEmptyMessage(2);
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

	// private void showPopupWindow(View view) {
	// View contentView = LayoutInflater.from(this).inflate(
	// R.layout.invite_pop_window, null);
	// final EditText voip=(EditText) contentView.findViewById(R.id.voip);
	// Button invitevoip=(Button) contentView.findViewById(R.id.invitevoip);
	// invitevoip.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// // inviteVoip(voip.getText().toString());
	// }
	// });
	// final PopupWindow popupWindow = new PopupWindow(contentView,
	// LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
	// popupWindow.setTouchable(true);
	// popupWindow.setTouchInterceptor(new OnTouchListener() {
	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// return false;
	// }
	// });
	// popupWindow.setBackgroundDrawable(getResources().getDrawable(
	// R.drawable.backgroud_blue));
	// popupWindow.showAsDropDown(view);
	// }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 300 && resultCode == 2) {
			String nums = data.getStringExtra("uids");
			Log.i("xbb邀请加入群", nums);
			if (nums != null) {
				String[] phones = nums.split(",");
				for (String uid : phones) {
					// uids.add(string);
					inviteVoip(uid);
				}
				@SuppressWarnings("unchecked")
				ArrayList<User> users = (ArrayList<User>) data
						.getSerializableExtra("users");
				Message msg = myhandler.obtainMessage(3, users);
				myhandler.sendMessage(msg);
			}
		}
	}

	private void inviteVoip(String uid) {
		Log.i("xbb邀请加入群", groupId);
		// Log.i("xbb邀请加入群",uids.get(0)+uids.get(1));
		HttpRequest.sendGet(TLUrl.URL_GET_VOIP + "group/InviteJoinGroup",
				"uid="
						+ MyApplication.getInstance().getUid()
						+ "&groupId="
						+ groupId
						+ "&member="
						+ uid
						+ "&confirm=1"
						+ "&declared="
						+ (MyApplication.getInstance().getOwnernickname()
								+ "邀请你加入" + groupName), new HttpRevMsg() {
					@Override
					public void revMsg(String msg) {
						Log.i("xbb邀请加入群", msg);
						try {
							JSONObject jsonObject = new JSONObject(msg);
							if (jsonObject.getInt("status") == 1) {
								
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {

						}
					}
				});
	}

	private void showQRCode() {
		Tool.showProgressDialog(this, "正在加载", true);
		final ImageView iv = new ImageView(this);
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(iv);
		final String filePath = getFileRoot(this) + File.separator + "qr_"
				+ groupBean.getGroupId() + ".jpg";
		new Thread(new Runnable() {
			int width = DisplayUtils
					.getScreenWidthPixels(GroupDetailsActivity.this);

			@Override
			public void run() {
				boolean success = QRCodeUtil.createQRImage("{\"nickname\":\""
						+ groupBean.getGroupName() + "\",\"uid\":\""
						+ groupBean.getGroupId() + "\",\"avatar\":\"" + ""
						+ "\"}", width / 3 * 2, width / 3 * 2, null, filePath);
				if (success) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							iv.setImageBitmap(BitmapFactory
									.decodeFile(filePath));
							Tool.removeProgressDialog();
							builder.show();
						}
					});
				}
			}
		}).start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(members.get(position).getVoipAccout().equals(MyApplication.getInstance().getName())){
//			startActivity(new Intent(this, SettingActivity.class));
//			finishOthers();
			return;
		}
		User u = null;
		try {
			u = MyApplication.dbUtils.findById(User.class, members
					.get(position).getVoipAccout());
		} catch (DbException e) {
			e.printStackTrace();
		}
		if (u != null) {
			Intent intent = new Intent(GroupDetailsActivity.this,
					FriendDetailsActivity.class);
			intent.putExtra("friend", u);
			startActivity(intent);
		} else {
			u = members.get(position);
			Intent resultIntent = new Intent(this, QR_ADD_ACtivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("result", u.toString());
			resultIntent.putExtras(bundle);
			startActivity(resultIntent);
		}
	}
}
