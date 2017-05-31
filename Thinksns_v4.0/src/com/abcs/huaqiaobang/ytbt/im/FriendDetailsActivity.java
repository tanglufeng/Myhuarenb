package com.abcs.huaqiaobang.ytbt.im;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.call.CallActivity;
import com.abcs.huaqiaobang.ytbt.chats.ChattingActivity;
import com.abcs.huaqiaobang.ytbt.util.CircleImageView;
import com.abcs.huaqiaobang.ytbt.util.GlobalConstant;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONException;
import org.json.JSONObject;

public class FriendDetailsActivity extends BaseActivity implements
		OnClickListener {

	private String frienduid;
	private TextView name;
	private TextView uid;
	private ImageView changeName;
	private User user;
	private Button toim, tovoip;
	private Boolean isgroup = false;
	@SuppressLint("HandlerLeak")
	public Handler myhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				initView();
			}
			if (msg.what == 1) {
				Tool.showInfo(FriendDetailsActivity.this, "修改成功");
				String remark = (String)msg.obj;
				name.setText(remark);
				if (user.getRemark().trim().equals("")) {
					MyApplication.friends.remove(user.getNickname());
				} else {
					MyApplication.friends.remove(user.getRemark());
				}
				MyApplication.users.remove(user);
				user.setRemark(remark);
				MyApplication.users.add(user);
				MyApplication.friends.put(remark, user);
				Intent intent = new Intent();
				intent.setAction(GlobalConstant.ACTION_UPDATE_FRIENDS);
				sendBroadcast(intent);// 发送一个异步广播
				try {
					MyApplication.dbUtils.saveOrUpdate(user);
				} catch (DbException e) {
					e.printStackTrace();
				}
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_details);
		user = (User) getIntent().getSerializableExtra("friend");
//		frienduid = getIntent().getStringExtra("friend");
//		try {
//			user = MyApplication.dbUtils.findById(User.class,
//					frienduid);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
		initView();
	}

	private void initView() {
		toim = (Button) findViewById(R.id.btn_im);
		tovoip = (Button) findViewById(R.id.btn_voip);
		changeName = (ImageView) findViewById(R.id.changeName);
		findViewById(R.id.btn_voip_video).setOnClickListener(this);
		findViewById(R.id.ImageView01).setOnClickListener(this);
		changeName.setOnClickListener(this);
		toim.setOnClickListener(this);
		tovoip.setOnClickListener(this);
		CircleImageView avatar = (CircleImageView) findViewById(R.id.avatar);
		name = (TextView) findViewById(R.id.name);
		uid = (TextView) findViewById(R.id.uid);
		uid.setText(user.getUid() + "");
		if (user != null) {
			MyApplication.bitmapUtils.display(avatar, user.getAvatar());
			if (user.getRemark().trim().equals("")) {
				name.setText(user.getNickname());
			} else {
				name.setText(user.getRemark());
			}

		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_im:
			finishOthers();
			Intent intent = new Intent(this, ChattingActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("friend", user);
			intent.putExtras(bundle);
			intent.putExtra("isgroup", isgroup);
			startActivity(intent);
			finish();
	
			break;

		case R.id.btn_voip:
			finishOthers();
			Intent i = new Intent(FriendDetailsActivity.this,
					CallActivity.class);
			i.putExtra("con.yuntongxun.ecdemo.VoIP_OUTGOING_CALL", true);
			i.putExtra("callType", GlobalConstant.Call_TYPE_VOICE);
			i.putExtra("num", user.getVoipAccout());
			i.putExtra("name", user.getNickname());
			i.putExtra("avatar", user.getAvatar());
			startActivity(i);
			finish();
		
			break;
		case R.id.btn_voip_video:
			finishOthers();
			Intent t = new Intent(FriendDetailsActivity.this,
					CallActivity.class);
			t.putExtra("con.yuntongxun.ecdemo.VoIP_OUTGOING_CALL", true);
			t.putExtra("callType", GlobalConstant.Call_TYPE_VIDEO);
			t.putExtra("num", user.getVoipAccout());
			t.putExtra("name", user.getNickname());
			t.putExtra("avatar", user.getAvatar());
			startActivity(t);
			finish();
	
			break;
		case R.id.ImageView01:
			finish();
			break;
		case R.id.changeName:
//			Intent intent2 = new Intent(FriendDetailsActivity.this,
//					FriendRemarksActivity.class);
//			intent2.putExtra("nickname", user.getNickname());
//			intent2.putExtra("frienduid", String.valueOf(user.getUid()));
//			startActivityForResult(intent2, 500);
			modifyNickName();
			break;
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 500 && resultCode == 88) {
			String remark = data.getStringExtra("remark");
			name.setText(remark);
			if (user.getRemark().trim().equals("")) {
				MyApplication.friends.remove(user.getNickname());
			} else {
				MyApplication.friends.remove(user.getRemark());
			}
			user.setRemark(remark);
			MyApplication.friends.put(remark, user);
			Intent intent = new Intent();
			intent.setAction(GlobalConstant.ACTION_UPDATE_FRIENDS);
			sendBroadcast(intent);// 发送一个异步广播
		}
	}

	private void modifyNickName() {
		LinearLayout layout = (LinearLayout) View.inflate(this, R.layout.nickname_edittext, null);
        final EditText inputNickName = (EditText) layout.findViewById(R.id.editText1);
        inputNickName.setHint("请输入备注");
        inputNickName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改备注").setIcon(android.R.drawable.ic_dialog_info)
                .setView(layout).setPositiveButton("取消", null);
        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String remarks = inputNickName.getText().toString().trim();
				if(remarks.equals("")){
					Tool.showInfo(FriendDetailsActivity.this, "请输入备注");
				}else{
					remarkName(remarks);
				}
			}
		});
		builder.show();
	}
	private void remarkName(final String remarks) {
		Tool.showProgressDialog(this, "正在提交...", true);
		HttpRequest.sendPost(TLUrl.URL_GET_VOIP + "User/updateremarks", "uid="
				+ MyApplication.getInstance().getUid() + "&frienduid="
				+ user.getUid() + "&remarks=" + remarks, new HttpRevMsg() {
			@Override
			public void revMsg(String msg) {
				Tool.removeProgressDialog();
				if (msg.length() <= 0) {
					Tool.showInfo(FriendDetailsActivity.this, "修改失败");
					return;
				}
				try {
					JSONObject jsonObject = new JSONObject(msg);
					if (jsonObject.getInt("status") == 1) {
						Message message = myhandler.obtainMessage(1, remarks);
						myhandler.sendMessage(message);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
