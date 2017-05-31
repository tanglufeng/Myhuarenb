package com.abcs.huaqiaobang.ytbt.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.ytbt.util.CircleImageView;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

public class QR_ADD_ACtivity extends BaseActivity {
	private CircleImageView avatar;
	private TextView name,uid;
	private String result;
	private String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_add_friend);
		result = getIntent().getExtras().getString("result");
		if(result==null){
			Tool.showInfo(this, "无扫描结果");
			finish();
		}
		initView();
		
	}
	private void initView() {
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		avatar = (CircleImageView) findViewById(R.id.avatar);
		name =(TextView) findViewById(R.id.name);
		uid =(TextView) findViewById(R.id.uid);
		JSONObject obj = null;
		try {
			obj = new JSONObject(result);
			id = obj.getString("uid");
			MyApplication.bitmapUtils.display(avatar, obj.getString("avatar"));
			name.setText(obj.getString("nickname"));
			uid.setText(obj.getString("uid"));
		} catch (Exception e) {
			Tool.showInfo(this, "无扫描结果");
			finish();
			e.printStackTrace();
		}
		findViewById(R.id.btn_add).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					HttpUtils httpUtils = new HttpUtils(30000);
					httpUtils.send(HttpMethod.GET, TLUrl.URL_GET_VOIP
							+ "User/addMongoDBfrienduser?"+"uid="
							+ MyApplication.getInstance().getUid() + "&frienduid="
							+ id, new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									Tool.showInfo(QR_ADD_ACtivity.this, "加好友失败");
								}

								@Override
								public void onSuccess(ResponseInfo<String> arg0) {
									try {
										JSONObject jsonObject = new JSONObject(arg0.result);
										if (jsonObject.getInt("status") == 1) {
											Tool.showInfo(QR_ADD_ACtivity.this, "好友请求发送成功!");
										} else {
											Tool.showInfo(QR_ADD_ACtivity.this, "加好友请求失败!");
										}

									} catch (JSONException e) {
										// TODO Auto-generated catch block
										Log.i("xbb2", e.toString());
										e.printStackTrace();
									}	
								}
							});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
