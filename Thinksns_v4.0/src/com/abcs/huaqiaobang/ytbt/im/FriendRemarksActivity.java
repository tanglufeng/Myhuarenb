package com.abcs.huaqiaobang.ytbt.im;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;

public class FriendRemarksActivity extends Activity implements OnClickListener {

	private EditText remark;
	private String frienduid, remarks;
	@SuppressLint("HandlerLeak")
	public Handler myhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == 0) {
				// Toast.makeText(FriendRemarksActivity.this,
				// msg.obj.toString(), Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra("remark", remarks);
				setResult(88, intent);
				FriendRemarksActivity.this.finish();
				
			}
		}

	};
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_remarks);
		frienduid = getIntent().getStringExtra("frienduid");
//		user = (User) getIntent().getSerializableExtra("user");
		remark = (EditText) findViewById(R.id.remark);
		Button btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		remarkName();
	}

	private void remarkName() {
		remarks = remark.getText().toString().trim();
		HttpRequest.sendPost(TLUrl.URL_GET_VOIP + "User/updateremarks", "uid="
				+ MyApplication.getInstance().getUid() + "&frienduid="
				+ frienduid + "&remarks=" + remarks, new HttpRevMsg() {
			@Override
			public void revMsg(String msg) {
				if (msg.length() <= 0) {
					return;
				}
				try {
					JSONObject jsonObject = new JSONObject(msg);
					if (jsonObject.getInt("status") == 1) {
						Message message = myhandler.obtainMessage(0, msg);
						myhandler.sendMessage(message);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

}
