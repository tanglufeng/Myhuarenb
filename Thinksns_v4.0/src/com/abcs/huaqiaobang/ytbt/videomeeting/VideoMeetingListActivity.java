package com.abcs.huaqiaobang.ytbt.videomeeting;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.ytbt.common.dialog.ECListDialog;
import com.abcs.huaqiaobang.ytbt.common.utils.ToastUtil;
import com.abcs.huaqiaobang.ytbt.util.GlobalConstant;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.abcs.huaqiaobang.ytbt.voicemeeting.CreateMeetingActivity;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMeetingManager;
import com.yuntongxun.ecsdk.ECMeetingManager.ECMeetingType;
import com.yuntongxun.ecsdk.ECMeetingManager.OnListAllMeetingsListener;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.meeting.ECMeeting;
import com.yuntongxun.ecsdk.meeting.video.ECVideoMeetingDeleteMsg;
import com.yuntongxun.ecsdk.meeting.video.ECVideoMeetingExitMsg;
import com.yuntongxun.ecsdk.meeting.video.ECVideoMeetingJoinMsg;
import com.yuntongxun.ecsdk.meeting.video.ECVideoMeetingMsg;

import java.util.ArrayList;
import java.util.List;

public class VideoMeetingListActivity extends VideoMeetingBaseActivity
		implements OnClickListener, OnItemClickListener {
	public static final String CONFERENCE_CREATOR = "com.voice.demo.ccp.VIDEO_CREATE";
	public static final String CONFERENCE_TYPE = "COM.VOICE.DEMO.CCP.VIDEO_TYPE";
	public static final int TYPE_SINGLE = 0;
	public static final int TYPE_MULIT = 1;
	private RelativeLayout mVideoCEmpty;
	private ListView mVideoCListView;
	private ArrayList<ECMeeting> mVideoCList;
	private VideoConferenceConvAdapter mVideoCAdapter;
	private int mVideoConfType = TYPE_SINGLE;
	private int REQUEST_CODE_CREATE = 123;
	/** 创建会议参数 */
	private ECMeetingManager.ECCreateMeetingParams mMeetingParams;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videomeeting_list);
		initView();
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalConstant.INTENT_VIDEO_CONFERENCE_DISMISS);
		registerReceiver(new VideoMsgBroadCastReceiver(), filter);
	}

	private void initView() {
		mVideoCEmpty = (RelativeLayout) findViewById(R.id.video_conferenc_empty);
		mVideoCListView = (ListView) findViewById(R.id.video_conferenc_list);
		findViewById(R.id.begin_create_video_conference).setOnClickListener(
				this);
		findViewById(R.id.title_btn1).setOnClickListener(this);
		mVideoCListView.setOnItemClickListener(this);
		mVideoCListView.setEmptyView(mVideoCEmpty);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("info", "onResume");
		// boolean b = VideoMeetingHelper.doQueryVideoMeetings();
		boolean b = doQueryVideoMeetings();
		if (b) {
			showCustomProcessDialog(getString(R.string.common_progressdialog_title));
		}
	}

	private boolean doQueryVideoMeetings() {
		// 获取一个会议管理接口对象
		ECMeetingManager meetingManager = ECDevice.getECMeetingManager();
		// 发起查询视频会议请求
		if (meetingManager == null) {
			return false;
		}
		meetingManager.listAllMultiMeetingsByType("",
				ECMeetingType.MEETING_MULTI_VIDEO,
				new OnListAllMeetingsListener<ECMeeting>() {
					@Override
					public void onListAllMeetings(ECError reason,
							List<ECMeeting> list) {
						if (SdkErrorCode.REQUEST_SUCCESS == reason.errorCode) {
							// 查询会议成功
							onQueryVideoMeetings(list);
							return;
						}
						Log.i("ECSDK_Demo", "del meeting member error["
								+ reason.errorCode + " ]");
					}
				});
		return true;
	}

	@Override
	public void onQueryVideoMeetings(List<ECMeeting> list) {
		Log.i("info", "onQueryVideoMeetings");
		dismissPostingDialog();
		if (list == null) {
			return;
		}
		mVideoCList = new ArrayList<ECMeeting>();
		for (ECMeeting conference : list) {
			if (conference != null) {
				mVideoCList.add(conference);
			}
		}
		if (mVideoCList == null || mVideoCList.isEmpty()) {
			if (mVideoCAdapter != null) {
				mVideoCAdapter.clear();
			}
			return;
		}
		initVideoConferenceAdapter();
	}


	@Override
	public void onCloseVideoMeeting(String meetingNo) {
		dismissPostingDialog();
		if (mVideoCAdapter != null) {
			for (int i = 0; i < mVideoCAdapter.getCount(); i++) {
				ECMeeting meeting = mVideoCAdapter.getItem(i);
				if (meeting != null && meeting.getMeetingNo() != null
						&& meeting.getMeetingNo().equals(meetingNo)) {
					mVideoCAdapter.remove(meeting);
					return;
				}
			}
		}
	}

	private void initVideoConferenceAdapter() {
		if (mVideoCAdapter == null) {
			mVideoCAdapter = new VideoConferenceConvAdapter(
					VideoMeetingListActivity.this);
			mVideoCListView.setAdapter(mVideoCAdapter);
		}
		mVideoCAdapter.setData(mVideoCList);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_btn1:
		case R.id.begin_create_video_conference:
			Intent intent = new Intent(VideoMeetingListActivity.this,
					CreateMeetingActivity.class);
			// intent.putExtra(VideoMeetingListActivity.CONFERENCE_TYPE,
			// mVideoConfType);
			startActivityForResult(intent, REQUEST_CODE_CREATE);
			overridePendingTransition(R.anim.video_push_up_in,
					R.anim.push_empty_out);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mVideoCAdapter != null) {
			final ECMeeting meeting = mVideoCAdapter.getItem(position);
			if (meeting == null) {
				ToastUtil.showMessage(R.string.meeting_voice_room_error);
				return;
			}
			// 如果自己是会议的创建者
			if (MyApplication.getInstance().getName()
					.equals(meeting.getCreator())) {
				ECListDialog dialog = new ECListDialog(
						VideoMeetingListActivity.this, R.array.meeting_control);
				dialog.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
					@Override
					public void onDialogItemClick(Dialog d, int position) {
						handleMeetingClick(meeting, position);
					}
				});
				dialog.setTitle(meeting.getMeetingName());
				dialog.show();
				return;
			}
			// 验证是否有加入权限
			startMeeting(meeting);
		}
	}

	/**
	 * 处理点击会议加入操作
	 * 
	 * @param meeting
	 *            会议
	 * @param position
	 */
	private void handleMeetingClick(ECMeeting meeting, int position) {
		switch (position) {
		case 0:
			// 验证是否有加入权限
			startMeeting(meeting);
			break;
		case 1:
			if (meeting != null) {
				// 解散会议
				VideoMeetingHelper.doCloseVideoMeeting(meeting.getMeetingNo());
			}
			break;
		}
	}

	/**
	 * 根据权限加入会议
	 * 
	 * @param meeting
	 */
	private void startMeeting(final ECMeeting meeting) {
		if (meeting.isValidate()) {
			// 会议加入需要验证
			Builder dialog = new Builder(this);
			LayoutInflater inflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout layout = (LinearLayout) inflater.inflate(
					R.layout.dialogview, null);
			dialog.setView(layout);
			final EditText et_search = (EditText) layout
					.findViewById(R.id.searchC);
			et_search.setTransformationMethod(PasswordTransformationMethod
					.getInstance());
			dialog.setTitle("请输入密码");
			dialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							String searchC = et_search.getText().toString();
							if (!TextUtils.isEmpty(searchC)) {
								String password = et_search.getText()
										.toString().trim();
								doStartMeetingActivity(meeting, password);
							} else {
								Tool.showInfo(VideoMeetingListActivity.this,
										"密码不能为空");
							}
						}
					});
			dialog.setNegativeButton("取消", null);
			dialog.show();
			return;
		}
		doStartMeetingActivity(meeting, null);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (requestCode == REQUEST_CODE_CREATE) {
				if (data == null) {
					finish();
					return;
				}
			} else if (resultCode != RESULT_OK) {
				finish();
				return;
			}
			if (REQUEST_CODE_CREATE == requestCode) {
				if (!data.hasExtra(CreateMeetingActivity.EXTRA_MEETING_PARAMS)) {
					Log.e("info", "create meeting error params null");
					return;
				}
				mMeetingParams = data
						.getParcelableExtra(CreateMeetingActivity.EXTRA_MEETING_PARAMS);
				if (mMeetingParams != null) {
					showProcessDialog();
					// mMeetingAutoJoin = mMeetingParams.isAutoJoin();
					// 处理创建群聊房间
					//VideoMeetingHelper.doCreatVideoMeeting(mMeetingParams);
					Intent intent = new Intent(VideoMeetingListActivity.this,VideoMeetingActivity.class);
					intent.putExtra("params", mMeetingParams);
					startActivity(intent);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onError(int reason) {
		Tool.showInfo(this, "操作失败,错误码" + reason);
	}

	class VideoConferenceConvAdapter extends ArrayAdapter<ECMeeting> {

		LayoutInflater mInflater;

		public VideoConferenceConvAdapter(Context context) {
			super(context, 0);

			mInflater = getLayoutInflater();
		}

		public void setData(List<ECMeeting> data) {
			setNotifyOnChange(false);
			clear();
			setNotifyOnChange(true);
			if (data != null) {
				for (ECMeeting appEntry : data) {
					add(appEntry);
				}
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			VideoConferenceHolder holder;
			if (convertView == null || convertView.getTag() == null) {
				convertView = mInflater.inflate(R.layout.voice_meeting_item,
						null);
				holder = new VideoConferenceHolder();
				convertView.setTag(holder);

				holder.videoConName = (TextView) convertView
						.findViewById(R.id.chatroom_name);
				holder.videoConTips = (TextView) convertView
						.findViewById(R.id.chatroom_tips);
				holder.gotoIcon = (TextView) convertView
						.findViewById(R.id.goto_icon);

				LayoutParams params = (LayoutParams) holder.gotoIcon
						.getLayoutParams();
				params.setMargins(0, 0, 10, 0);
				holder.gotoIcon.setLayoutParams(params);

				// convertView
				// .setBackgroundResource(R.drawable.video_list_item_conference);
				// // set Video Conference Item Style
				// holder.videoConName.setTextColor(Color.parseColor("#FFFFFF"));
				// holder.gotoIcon
				// .setBackgroundResource(R.drawable.video_item_goto);
			} else {
				holder = (VideoConferenceHolder) convertView.getTag();
			}

			try {
				// do ..
				ECMeeting item = getItem(position);
				if (item != null) {

					String conferenceName = "";
					String voipAccount = item.getCreator();
					if (TextUtils.isEmpty(voipAccount)) {
						voipAccount = "";
					}

					// Video Conference Name
					if (!TextUtils.isEmpty(item.getMeetingName())) {
						conferenceName = item.getMeetingName();
					} else {
						conferenceName = getString(
								R.string.video_conference_name, voipAccount);
					}

					holder.videoConName.setText(conferenceName);

					int resourceId;

					if (item.getJoined() >= 5) {
						//
						resourceId = R.string.str_chatroom_list_join_full;
					} else {

						resourceId = R.string.str_chatroom_list_join_unfull;
					}

					holder.videoConTips.setText(getString(resourceId,
							item.getJoined(), voipAccount));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return convertView;
		}

		class VideoConferenceHolder {
			TextView videoConName;
			TextView videoConTips;
			TextView gotoIcon;
		}

	}

	class VideoMsgBroadCastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (GlobalConstant.INTENT_VIDEO_CONFERENCE_DISMISS.equals(intent
					.getAction())) {
				// receive action dismiss Video Conference.
				if (intent.hasExtra(GlobalConstant.CONFERENCE_ID)) {
					String conferenceId = intent
							.getStringExtra(GlobalConstant.CONFERENCE_ID);
					ECVideoMeetingDeleteMsg VideoMsg = new ECVideoMeetingDeleteMsg();
					VideoMsg.setMeetingNo(conferenceId);
					;
					handleReceiveVideoConferenceMsg(VideoMsg);
				}

			}
		}

		protected void handleReceiveVideoConferenceMsg(
				ECVideoMeetingMsg VideoMsg) {
			synchronized (VideoMeetingListActivity.class) {
				if (VideoMsg == null) {
					return;
				}
				// remove the Video Conference Conversation form the Video
				// Adapter.
				if (VideoMsg instanceof ECVideoMeetingDeleteMsg) {
					ECVideoMeetingDeleteMsg videoConferenceDismissMsg = (ECVideoMeetingDeleteMsg) VideoMsg;

					String conferenceId = videoConferenceDismissMsg
							.getMeetingNo();
					if (mVideoCAdapter == null || mVideoCAdapter.isEmpty()) {
						return;
					}
					for (int position = 0; position < mVideoCAdapter.getCount(); position++) {
						ECMeeting item = mVideoCAdapter.getItem(position);
						if (item == null
								|| TextUtils.isEmpty(item.getMeetingNo())) {
							continue;
						}

						if (item.getMeetingNo().equals(conferenceId)) {
							mVideoCAdapter.remove(item);
							return;
						}
					}

					// if Video Conference list empty .
					if (mVideoCAdapter.isEmpty()) {
						// str_tips_no_video_c
						//mVideoTips.setText(R.string.str_tips_no_video_c);
					}
				} else if (VideoMsg instanceof ECVideoMeetingJoinMsg
						|| VideoMsg instanceof ECVideoMeetingExitMsg) {
					doQueryVideoMeetings();
				}
			}
		}
	}
}
