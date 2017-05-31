package com.abcs.huaqiaobang.ytbt.voicemeeting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.yuntongxun.ecsdk.ECMeetingManager;

public class CreateMeetingActivity extends BaseActivity implements
		OnClickListener {
	public static final String EXTRA_MEETING_PARAMS = "com.yuntongxun.meeting_params";
	private EditText et_room_name, et_room_psw;
	private CheckBox auto_close, auto_join, auto_del;
	private Spinner sp;
	private String[] voice_mode = { "没有提示音有背景音", "全部提示音", "无音" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creat_meeting);
		initView();
	}

	private void initView() {
		et_room_name = (EditText) findViewById(R.id.et_room_name);
		et_room_psw = (EditText) findViewById(R.id.et_room_psw);
		auto_close = (CheckBox) findViewById(R.id.auto_close);
		auto_join = (CheckBox) findViewById(R.id.auto_join);
		auto_del = (CheckBox) findViewById(R.id.auto_del);
		findViewById(R.id.bt_creat).setOnClickListener(this);
		findViewById(R.id.bt_cancel).setOnClickListener(this);
		sp = (Spinner) findViewById(R.id.spinner);
		sp.setAdapter(new ArrayAdapter<String>(this,
				R.layout.spinner_item, voice_mode));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_creat:
			try {
				String meetingName = et_room_name.getText().toString().trim();
				if (TextUtils.isEmpty(meetingName)) {
					Tool.showInfo(this, "请输入会议房间名称");
					return;
				}
				ECMeetingManager.ECCreateMeetingParams.Builder builder = new ECMeetingManager.ECCreateMeetingParams.Builder();
				// 设置语音会议房间名称
				builder.setMeetingName(et_room_name.getText().toString().trim())
				// 设置语音会议房间加入密码
						.setMeetingPwd(et_room_psw.getText().toString().trim())
						// 设置语音会议创建者退出是否自动解散会议
						.setIsAutoClose(auto_close.isChecked())
						// 设置语音会议创建成功是否自动加入
						.setIsAutoJoin(auto_join.isChecked())
						// 设置语音会议背景音模式
						.setVoiceMod(
								ECMeetingManager.ECCreateMeetingParams.ToneMode
										.values()[sp.getSelectedItemPosition()])
						// 设置语音会议所有成员退出后是否自动删除会议
						.setIsAutoDelete(auto_del.isChecked());
				Intent intent = new Intent();
				intent.putExtra(EXTRA_MEETING_PARAMS, builder.create());
				setResult(1, intent);
				finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.bt_cancel:
			finish();
			break;
		}
	}
}
