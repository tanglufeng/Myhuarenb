package com.abcs.huaqiaobang.ytbt.voicemeeting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.ytbt.common.dialog.ECProgressDialog;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.meeting.ECMeeting;
import com.yuntongxun.ecsdk.meeting.ECMeetingMember;
import com.yuntongxun.ecsdk.meeting.intercom.ECInterPhoneMeetingMsg;
import com.yuntongxun.ecsdk.meeting.voice.ECVoiceMeetingMsg;

import java.util.List;

public class VoiceMeetingBaseActivity extends BaseActivity implements
		MeetingHelper.OnMeetingCallback,
		MeetingMsgReceiver.OnVoiceMeetingMsgReceive {
	private ECProgressDialog mPostingdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MeetingMsgReceiver.addVoiceMeetingListener(this);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		MeetingHelper.removeInterPhoneCallback(this);
		MeetingMsgReceiver.removeVoiceMeetingListener(this);
	}

	@Override
	public void onMeetings(List<ECMeeting> list) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMeetingMembers(List<? extends ECMeetingMember> members) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMeetingStart(String meetingNo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMeetingDismiss(String meetingNo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(int type, ECError e) {
		
	}

	@Override
	public void onReceiveVoiceMeetingMsg(ECVoiceMeetingMsg msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceiveInterPhoneMeetingMsg(ECInterPhoneMeetingMsg msg) {
		// TODO Auto-generated method stub

	}

	/**
	 * 跳转到会议聊天界面
	 * 
	 * @param meeting
	 * @param pass
	 */
	protected void doStartMeetingActivity(ECMeeting meeting, String pass) {
		doStartMeetingActivity(meeting, pass, true);
	}

	/**
	 * 跳转到会议聊天界面
	 * 
	 * @param meeting
	 * @param pass
	 */
	protected void doStartMeetingActivity(ECMeeting meeting, String pass,
			boolean callin) {
		Intent intent = new Intent(VoiceMeetingBaseActivity.this,
				VoiceMeetingActivity.class);
		intent.putExtra(VoiceMeetingActivity.EXTRA_MEETING, meeting);
		if (!TextUtils.isEmpty(pass)) {
			intent.putExtra(VoiceMeetingActivity.EXTRA_MEETING_PASS, pass);
		}
		intent.putExtra(VoiceMeetingActivity.EXTRA_CALL_IN, callin);
		startActivity(intent);
	}

	protected void showProcessDialog() {
		// if (mPostingdialog != null && mPostingdialog.isShowing()) {
		// return;
		// }
		// mPostingdialog = new ECProgressDialog(VoiceMeetingBaseActivity.this,
		// R.string.login_posting_submit);
		// mPostingdialog.show();
		Tool.showProgressDialog(this, "请稍候...",true);
	}

	protected void showCustomProcessDialog(String content) {
		//
		// if (mPostingdialog != null && mPostingdialog.isShowing()) {
		// return;
		// }
		//
		// mPostingdialog = new ECProgressDialog(VoiceMeetingBaseActivity.this,
		// content);
		// mPostingdialog.show();
		Tool.showProgressDialog(this, "请稍候...",true);
	}

	/**
	 * 关闭对话框
	 */
	protected void dismissPostingDialog() {
//		if (mPostingdialog == null || !mPostingdialog.isShowing()) {
//			return;
//		}
//		mPostingdialog.dismiss();
//		mPostingdialog = null;
		Tool.removeProgressDialog();
	}
}
