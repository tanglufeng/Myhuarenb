package com.abcs.huaqiaobang.ytbt.videomeeting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.ytbt.common.dialog.ECProgressDialog;
import com.abcs.huaqiaobang.ytbt.voicemeeting.MeetingMsgReceiver;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.VideoRatio;
import com.yuntongxun.ecsdk.meeting.ECMeeting;
import com.yuntongxun.ecsdk.meeting.video.ECVideoMeetingMsg;

import java.util.List;

public class VideoMeetingBaseActivity extends Activity implements
		VideoMeetingHelper.onVideoMeetingCallBack,
		MeetingMsgReceiver.OnVideoMeetingMsgReceive {
	private ECProgressDialog mPostingdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		VideoMeetingHelper.setonVideoMeetingCallBack(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MeetingMsgReceiver.addVideoMeetingListner(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MeetingMsgReceiver.removeVideoMeetingListner();
	}
	public boolean checkSDK() {
		if (ECDevice.getECMeetingManager() == null) {
			return false;
		}
		return true;
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
		Intent intent = new Intent(VideoMeetingBaseActivity.this,
				VideoMeetingActivity.class);
		intent.putExtra(VideoMeetingActivity.EXTRA_MEETING, meeting);
		if (!TextUtils.isEmpty(pass)) {
			intent.putExtra(VideoMeetingActivity.EXTRA_MEETING_PASS, pass);
		}
		intent.putExtra(VideoMeetingActivity.EXTRA_CALL_IN, callin);
		startActivity(intent);
	}

	protected void showProcessDialog() {
		if (mPostingdialog != null && mPostingdialog.isShowing()) {
			return;
		}
		mPostingdialog = new ECProgressDialog(VideoMeetingBaseActivity.this,
				R.string.login_posting_submit);
		mPostingdialog.show();
	}

	protected void showCustomProcessDialog(String content) {

		if (mPostingdialog != null && mPostingdialog.isShowing()) {
			return;
		}

		mPostingdialog = new ECProgressDialog(VideoMeetingBaseActivity.this,
				content);
		mPostingdialog.show();
	}

	/**
	 * 关闭对话框
	 */
	protected void dismissPostingDialog() {
		if (mPostingdialog == null || !mPostingdialog.isShowing()) {
			return;
		}
		mPostingdialog.dismiss();
		mPostingdialog = null;
	}

	@Override
	public void onQueryVideoMeetings(List<ECMeeting> list) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCreatVideoMeeting(String meetingNo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onJoinVideoMeeting(String meetingNo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInviteMembers(String meetingNo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCloseVideoMeeting(String meetingNo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRemoveMember(String member) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onQueryAllMembers(List members) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(int reason) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceiveVideoMeetingMsg(ECVideoMeetingMsg msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVideoRatioChanged(VideoRatio video) {
		// TODO Auto-generated method stub
		
	}

}
