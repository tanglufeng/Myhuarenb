package com.abcs.huaqiaobang.ytbt.videomeeting;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.ytbt.common.utils.CommomUtil;
import com.abcs.huaqiaobang.ytbt.util.GlobalConstant;
import com.abcs.huaqiaobang.ytbt.videomeeting.CCPMulitVideoUI.OnVideoUIItemClickListener;
import com.yuntongxun.ecsdk.CameraInfo;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMeetingManager.ECCreateMeetingParams;
import com.yuntongxun.ecsdk.ECMeetingManager.ECMeetingType;
import com.yuntongxun.ecsdk.ECMeetingManager.OnCreateOrJoinMeetingListener;
import com.yuntongxun.ecsdk.ECMeetingManager.OnDeleteMeetingListener;
import com.yuntongxun.ecsdk.ECMeetingManager.OnMemberVideoFrameChangedListener;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.VideoRatio;
import com.yuntongxun.ecsdk.meeting.ECMeeting;
import com.yuntongxun.ecsdk.meeting.ECVideoMeetingMember;
import com.yuntongxun.ecsdk.meeting.video.ECVideoMeetingDeleteMsg;
import com.yuntongxun.ecsdk.meeting.video.ECVideoMeetingExitMsg;
import com.yuntongxun.ecsdk.meeting.video.ECVideoMeetingJoinMsg;
import com.yuntongxun.ecsdk.meeting.video.ECVideoMeetingMsg;
import com.yuntongxun.ecsdk.meeting.video.ECVideoMeetingRejectMsg;
import com.yuntongxun.ecsdk.meeting.video.ECVideoMeetingRemoveMemberMsg;
import com.yuntongxun.ecsdk.meeting.video.ECVideoMeetingSwitchMsg;
import com.yuntongxun.ecsdk.meeting.video.ECVideoMeetingVideoFrameActionMsg;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;
import com.yuntongxun.ecsdk.voip.video.ECCaptureView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class VideoMeetingActivity extends VideoMeetingBaseActivity implements
		OnVideoUIItemClickListener, View.OnClickListener {
	private static final int MODE_VIDEO_C_INVITATION = 0;
	private static final int MODE_VIDEO_C_INITIATED_INTERCOM = 1;
	public HashMap<String, Integer> mVideoMemberUI;
	private List<MultiVideoMember> mulitMembers;
	private TextView mVideoTips;
	private ImageButton mCameraControl;
	// private CCPMulitVideoUI mVideoConUI;
	private ImageButton mMuteControl;
	private ImageButton mVideoControl;
	private Handler handler = new Handler();
	private View instructionsView;
	private Button mExitVideoCon;
	private ECCaptureView captureView;
	private String mVideoMainScreenVoIP;
	private String mVideoConferenceId;
	private String mVideoCreate;
	private int modeType;
	private boolean isMute = false;
	private boolean isVideoConCreate = false;
	private boolean isVideoChatting = false;
	private boolean mPubish = true;
	/** 会议信息 */
	private ECMeeting mMeeting;
	private boolean isMeetingOver = false;
	/** 是否是自己创建的会议房间 */
	private boolean isSelfMeeting = false;
	public static final String EXTRA_MEETING = "com.yuntongxun.ecdemo.Meeting";
	public static final String EXTRA_MEETING_PASS = "com.yuntongxun.ecdemo.Meeting_Pass";
	public static final String EXTRA_CALL_IN = "com.yuntongxun.ecdemo.Meeting_Join";
	/** 会议房间密码 */
	private String mMeetingPassword;
	/** 是否需要呼入会议 */
	private boolean mMeetingCallin;
	private int mCameraCapbilityIndex = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_meeting1);
		Log.i("info", "VoiceMeetingActivity");
		mVideoMemberUI = new HashMap<String, Integer>();
		initView();
		initialize();
		mMeeting = getIntent().getParcelableExtra(EXTRA_MEETING);
		mVideoConferenceId = mMeeting.getMeetingNo();
		mMeetingPassword = getIntent().getStringExtra(EXTRA_MEETING_PASS);
		mMeetingCallin = getIntent().getBooleanExtra(EXTRA_CALL_IN, false);
		if (mMeeting == null) {
			Log.i("info", " meeting error , meeting null");
			finish();
			return;
		}
		isSelfMeeting = MyApplication.getInstance().getName()
				.equals(mMeeting.getCreator());
		if (mMeetingCallin) {
			VideoMeetingHelper.doJoinVideoMeeting(mMeeting.getMeetingNo(),
					mMeetingPassword);
			return;
		}
		mExitVideoCon.setEnabled(true);
		mCameraControl.setEnabled(true);
		mMuteControl.setEnabled(true);
		isMute = ECDevice.getECVoIPSetupManager().getMuteStatus();
		initMute();
		isVideoChatting = true;
		initVideoView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isVideoChatting) {
			VideoMeetingHelper.doQueryAllMembers(mMeeting.getMeetingNo());
			// mVideoConUI.onResume(mCameraCapbilityIndex);
		}
	}

	private void initialize() {
		Intent intent = getIntent();
		ECDevice.getECVoIPSetupManager().setVideoView(captureView, null);
		if (modeType == MODE_VIDEO_C_INITIATED_INTERCOM) {// 自动创建、加入
			ECCreateMeetingParams params = intent.getParcelableExtra("params");
			if (!checkSDK()) {
				return;
			}
			ECDevice.getECMeetingManager().createMultiMeetingByType(params,
					ECMeetingType.MEETING_MULTI_VIDEO,
					new OnCreateOrJoinMeetingListener() {
						@Override
						public void onCreateOrJoinMeeting(ECError reason,
								String meetingNo) {
							if (reason.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
								mVideoConferenceId = meetingNo;
								// TODO Auto-generated method stub
//								refreshUIAfterjoinSuccess(reason, meetingNo);
							} else {
								Toast.makeText(VideoMeetingActivity.this,
										"加入会议失败", Toast.LENGTH_SHORT).show();
								finish();
							}
						}
					});
		} else if (modeType == MODE_VIDEO_C_INVITATION) {// 加入会议
			checkSDK();
			ECDevice.getECMeetingManager().joinMeetingByType(
					mVideoConferenceId, "", ECMeetingType.MEETING_MULTI_VIDEO,
					new OnCreateOrJoinMeetingListener() {

						public void onCreateOrJoinMeeting(ECError arg0,
								String arg1) {
							if (arg0.errorCode == SdkErrorCode.REQUEST_SUCCESS) {

								ECDevice.getECVoIPSetupManager()
										.enableLoudSpeaker(true);
								// TODO Auto-generated method stub
								//refreshUIAfterjoinSuccess(arg0, arg1);
							} else {
								Toast.makeText(VideoMeetingActivity.this,
										"加入会议失败", Toast.LENGTH_SHORT).show();
								finish();
							}
						}
					});
		}
	}

	private void initVideoView() {
		ECVoIPSetupManager voIPSetupManager = ECDevice.getECVoIPSetupManager();
		if (voIPSetupManager == null) {
			finish();
			return;
		}
		CameraInfo[] cameraInfos = voIPSetupManager.getCameraInfos();
		if (cameraInfos != null) {
			for (int i = 0; i < cameraInfos.length; i++) {
				if (cameraInfos[i].index == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
					mCameraCapbilityIndex = CommomUtil.comportCapabilityIndex(
							cameraInfos[cameraInfos[i].index].caps, 352 * 288);
				}
			}
		}
		ECDevice.getECVoIPSetupManager().selectCamera(mCameraCapbilityIndex,
				mCameraCapbilityIndex, 15,
				ECVoIPSetupManager.Rotate.ROTATE_AUTO, true);
		voIPSetupManager.setVideoView(captureView);
		voIPSetupManager.enableLoudSpeaker(false);
	}

	private void initView() {
		mVideoTips = (TextView) findViewById(R.id.notice_tips);
		// mVideoConUI = (CCPMulitVideoUI) findViewById(R.id.video_ui);
		// mVideoConUI.setOnVideoUIItemClickListener(this);
		captureView = (ECCaptureView) findViewById(R.id.localvideo_view);
		mCameraControl = (ImageButton) findViewById(R.id.camera_control);
		mMuteControl = (ImageButton) findViewById(R.id.mute_control);
		mVideoControl = (ImageButton) findViewById(R.id.video_control);
		mVideoControl.setVisibility(View.GONE);
		mCameraControl.setOnClickListener(this);
		mMuteControl.setOnClickListener(this);
		mVideoControl.setOnClickListener(this);
		mMuteControl.setEnabled(false);
		mCameraControl.setEnabled(false);
		mExitVideoCon = (Button) findViewById(R.id.out_video_c_submit);
		mExitVideoCon.setOnClickListener(this);
		initUIKey();
		// ECDevice.getECVoIPSetupManager().setVideoView(
		// captureView, null);
	}

	ArrayList<Integer> UIKey = new ArrayList<Integer>();

	public synchronized void initUIKey() {
		UIKey.clear();
		UIKey.add(R.id.surfaceView1);
		UIKey.add(R.id.surfaceView2);
		UIKey.add(R.id.surfaceView3);
		UIKey.add(R.id.surfaceView4);
		UIKey.add(R.id.surfaceView5);
		// UIKey.add(CCPMulitVideoUI.LAYOUT_KEY_SUB_VIEW_1);
		// UIKey.add(CCPMulitVideoUI.LAYOUT_KEY_SUB_VIEW_2);
		// UIKey.add(CCPMulitVideoUI.LAYOUT_KEY_SUB_VIEW_3);
		// UIKey.add(CCPMulitVideoUI.LAYOUT_KEY_SUB_VIEW_4);

	}

	private void initMute() {
		if (isMute) {
			mMuteControl.setImageResource(R.drawable.mute_forbid_selector);
		} else {
			mMuteControl.setImageResource(R.drawable.mute_enable_selector);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isSelfMeeting = false;
		if (mVideoMemberUI != null) {
			mVideoMemberUI.clear();
			mVideoMemberUI = null;
		}

		// if (mVideoConUI != null) {
		// mVideoConUI.release();
		// mVideoConUI = null;
		// }

		instructionsView = null;

		mVideoConferenceId = null;
		mVideoCreate = null;

		// The first rear facing camera
		isMute = false;
		isVideoConCreate = false;
		isVideoChatting = false;
		ECDevice.getECVoIPSetupManager().enableLoudSpeaker(false);
	}

	@Override
	public void onQueryAllMembers(List members) {
		if (mulitMembers == null) {
			mulitMembers = new ArrayList<MultiVideoMember>();
		}
		mulitMembers.clear();

		if (members == null || members.size() <= 0) {
			return;
		}

		ArrayList<ECVideoMeetingMember> membersNew = (ArrayList<ECVideoMeetingMember>) members;
		for (ECVideoMeetingMember member : membersNew) {
			MultiVideoMember mulitMember = new MultiVideoMember(member);
			mulitMembers.add(mulitMember);
		}

		initMembersOnVideoUI(mulitMembers);
	}

	private void initMembersOnVideoUI(List<MultiVideoMember> members) {
		synchronized (mVideoMemberUI) {

			for (final MultiVideoMember member : members) {
				Integer CCPMultiVideoUIKey = null;
				if (MyApplication.getInstance().getName()
						.equals(member.getNumber())) {
					CCPMultiVideoUIKey = R.id.localvideo_view;
				} else {
					CCPMultiVideoUIKey = getCCPMultiVideoUIKey();
				}
				if (CCPMultiVideoUIKey == null) {
					break;
				}
				putVideoUIMemberCache(member.getNumber(), CCPMultiVideoUIKey);
				final int key = CCPMultiVideoUIKey.intValue();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setVideoUITextOperable(key, member);
					}
				});
				if (!MyApplication.getInstance().getName()
						.equals(member.getNumber())) {
					doHandlerMemberVideoFrameRequest(CCPMultiVideoUIKey, member);

				}
			}
		}
	}

	private void doHandlerMemberVideoFrameRequest(Integer CCPUIKey,
			final MultiVideoMember multiVideoMember) {
		if (multiVideoMember != null) {
			if (mVideoConferenceId.length() > 8) {

				if (!multiVideoMember.isRequestVideoFrame()) {
					// SurfaceView surfaceView = mVideoConUI.getSurfaceView(
					// CCPUIKey.intValue(), false);

					if (!checkSDK()) {
						return;
					}
					int result = ECDevice
							.getECMeetingManager()
							.requestMemberVideoInVideoMeeting(
									mVideoConferenceId,
									"",
									multiVideoMember.getNumber(),
									(SurfaceView) findViewById(R.id.surfaceView1),
									multiVideoMember.getIp(),
									multiVideoMember.getPort(),
									new OnMemberVideoFrameChangedListener() {

										@Override
										public void onMemberVideoFrameChanged(
												boolean isRequest,
												ECError reason,
												String meetingNo, String account) {
											obtainVideoFrameChange(
													multiVideoMember,
													isRequest, reason, account);
										}

									});
				} else {
					checkSDK();
					int ret = ECDevice.getECMeetingManager()
							.cancelRequestMemberVideoInVideoMeeting(
									mVideoConferenceId, "",
									multiVideoMember.getNumber(),
									new OnMemberVideoFrameChangedListener() {

										@Override
										public void onMemberVideoFrameChanged(
												boolean isRequest,
												ECError reason,
												String meetingNo, String account) {
											obtainVideoFrameChange(
													multiVideoMember,
													isRequest, reason, account);
										}

									});
					if (ret == 0) {
						multiVideoMember.setRequestVideoFrame(false);
					}
				}
			}

		}
	}

	private void setVideoUITextOperable(Integer key, MultiVideoMember member) {
		// mVideoConUI.setVideoMember(key, member);

	}

	public synchronized Integer getCCPMultiVideoUIKey() {
		if (UIKey.isEmpty()) {
			return null;
		}
		return UIKey.remove(0);
	}

	public void putVideoUIMemberCache(String who, Integer key) {
		synchronized (mVideoMemberUI) {
			if (key == R.id.localvideo_view) {
				mVideoMainScreenVoIP = who;
			} else {
				mVideoMemberUI.put(who, key);
			}
		}
	}

	/**
	 * 视频会议成员图像操作结果回调接口
	 * 
	 * @param multiVideoMember
	 *            成员
	 * @param isRequest
	 *            是否请求成员图像
	 * @param reason
	 *            结果错误码
	 * @param account
	 *            成员账号
	 */
	private void obtainVideoFrameChange(MultiVideoMember multiVideoMember,
			boolean isRequest, ECError reason, String account) {
		if (reason.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
			String errMsg = isRequest ? "成员[" + account + "]视频图像请求成功" : "成员["
					+ account + "]视频图像取消成功";
			Toast.makeText(VideoMeetingActivity.this, errMsg,
					Toast.LENGTH_SHORT).show();
			multiVideoMember.setRequestVideoFrame(isRequest);
			return;
		}
		// 请求失败/取消失败
		multiVideoMember.setRequestVideoFrame(!isRequest);
		String errMsg = isRequest ? "成员[" + account + "]视频图像请求失败" : "成员["
				+ account + "]视频图像取消失败";
		Toast.makeText(VideoMeetingActivity.this, errMsg, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Builder dialog = new Builder(this);
			dialog.setTitle("退出会议").setMessage("退出后将不会收到会议消息")
					.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							exitOrDismissChatroom(false);
						}
					}).setNegativeButton("取消", null).show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 处理退出会议逻辑
	 * 
	 * @param exit
	 *            是否退出或者解散
	 */
	private void exitOrDismissChatroom(boolean exit) {
		if (!exit) {
			// 处理会议退出
			VideoMeetingHelper.doExitVideoMeeting();
			ECHandlerHelper.postDelayedRunnOnUI(new Runnable() {
				@Override
				public void run() {
					finish();
				}
			}, 2000);
		} else {
			// showProcessDialog();
			// if (mMeeting != null) {
			// disMeeting(mMeeting.getMeetingNo());
			// isMeetingOver = true;
			// isMeeting = false;
			// }
		}
	}

	public Integer queryVideoUIMemberFormCache(String who) {
		synchronized (mVideoMemberUI) {
			if (!mVideoMemberUI.containsKey(who)
					&& !who.equals(mVideoMainScreenVoIP)) {
				return null;
			}
			Integer key = null;

			if (mVideoMemberUI.containsKey(who)) {
				key = mVideoMemberUI.get(who);
			} else {

				if (who.equals(mVideoMainScreenVoIP)) {
					key = R.id.localvideo_view;
					// .LAYOUT_KEY_MAIN_SURFACEVIEW;
				}
			}

			return key;
		}
	}

	public MultiVideoMember getMultiVideoMember(String who) {
		if (mulitMembers == null) {
			return null;
		}
		for (MultiVideoMember member : mulitMembers) {
			if (member != null && who.equals(member.getNumber())) {
				return member;
			}
		}
		return null;
	}

	@Override
	public void onVideoRatioChanged(VideoRatio video) {
		Log.i("info", "onVideoRatioChanged");
		String voip = video.getAccount();
		int width = video.getWidth();
		int height = video.getHeight();
		if (TextUtils.isEmpty(voip)) {
			return;
		}
		final Integer key = queryVideoUIMemberFormCache(voip);
		if (key == null) {
			return;
		}
		MultiVideoMember multiVideoMember = getMultiVideoMember(voip);
		if (multiVideoMember != null) {
			multiVideoMember.setWidth(width);
			multiVideoMember.setHeight(height);
		}
		final int _width = width;
		final int _height = height;
		handler.post(new Runnable() {
			@Override
			public void run() {
				// if (mVideoConUI != null) {
				// SurfaceView surfaceView = mVideoConUI.getSurfaceView(
				// key.intValue(), false);
				// if (surfaceView != null) {
				// // surfaceView.getHolder().setFixedSize(_width,
				// // _height);
				// // LayoutParams layoutParams = surfaceView
				// // .getLayoutParams();
				// // layoutParams.width=_width;
				// // layoutParams.height=_height;
				// // surfaceView.setLayoutParams(layoutParams);
				// }
				// }
			}
		});
	}

	@Override
	public void onReceiveVideoMeetingMsg(ECVideoMeetingMsg msg) {
		switch (msg.getMsgType()) {
		case JOIN:
			Log.i("info", "JOIN");
			// 视频会议消息类型-有人加入
			ECVideoMeetingJoinMsg joinMsg = (ECVideoMeetingJoinMsg) msg;
			String[] whos = joinMsg.getWhos();
			for (String who : whos) {
				if (joinMsg.isMobile()) {
					who = "m" + who;
				}
				if (isVideoUIMemberExist(who)) {
					continue;
				}
				if (MyApplication.getInstance().getName().equals(who)) {
					continue;
				}
				// has Somebody join
				Integer CCPMultiVideoUIKey = getCCPMultiVideoUIKey();
				if (CCPMultiVideoUIKey == null) {
					return;
				}
				MultiVideoMember member = new MultiVideoMember();
				member.setNumber(who);
				member.setIp(joinMsg.getIp());
				member.setPort(joinMsg.getPort());
				member.setIsMobile(joinMsg.isMobile());
				member.setPublish(joinMsg.isPublish());
				putVideoUIMemberCache(member, CCPMultiVideoUIKey);

				// If there is no image, then show the account
				// information also.
				setVideoUITextOperable(CCPMultiVideoUIKey, member);
				// updateVideoNoticeTipsUI(getString(
				// R.string.str_video_conference_join, who));
				doHandlerMemberVideoFrameRequest(CCPMultiVideoUIKey, member);
			}
			break;
		case EXIT:
			Log.i("info", "EXIT");
			// 视频会议消息类型-有人退出
			ECVideoMeetingExitMsg exitMsg = (ECVideoMeetingExitMsg) msg;
			String[] w = exitMsg.getWhos();
			for (String who : w) {
				if (exitMsg.isMobile()) {
					who = "m" + who;
				}
				// remove the member of Video Conference form VideoUI
				removeMemberFormVideoUI(who);
				// updateVideoNoticeTipsUI(getString(
				// R.string.str_video_conference_exit, who));
			}
			break;
		case DELETE:
			Log.i("info", "DELETE");
			// 视频会议消息类型-会议结束
			ECVideoMeetingDeleteMsg delMsg = (ECVideoMeetingDeleteMsg) msg;
			if (isVideoConCreate) {
				return;
			}
			if (delMsg.getMeetingNo().equals(mVideoConferenceId)) {
				AlertDialog dialog = new Builder(this)
						.setTitle("视频会议被解散 !")
						.setMessage("抱歉，该视频会议已被创建者解散，点击确定以退出")
						.setPositiveButton("确定", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								exitOrDismissVideoConference(true);
							}
						}).create();
				dialog.setCancelable(false);
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
			}
			break;
		case REMOVE_MEMBER:
			Log.i("info", "REMOVE_MEMBER");
			// 视频会议消息类型-成员被移除
			ECVideoMeetingRemoveMemberMsg rMsg = (ECVideoMeetingRemoveMemberMsg) msg;
			if (MyApplication.getInstance().getName().equals(rMsg.getWho())
					&& !rMsg.isMobile()) {
				// The removed member of the video conference is self.
				AlertDialog dialog = new Builder(this)
						.setTitle("您被群管理员移除出会议!")
						.setMessage("抱歉，您被群管理员移除出会议，点击确定以退出")
						.setPositiveButton("确定", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								exitOrDismissVideoConference(false);
							}
						}).create();
				dialog.setCancelable(false);
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
			} else {
				if (rMsg.isMobile()) {
					removeMemberFormVideoUI("m" + rMsg.getWho());
				} else {
					removeMemberFormVideoUI(rMsg.getWho());
				}
			}
			break;
		case SWITCH:
			Log.i("info", "SWITCH");
			// 视频会议消息类型-主屏切换
			ECVideoMeetingSwitchMsg sMsg = (ECVideoMeetingSwitchMsg) msg;
			break;
		case VIDEO_FRAME_ACTION:
			Log.i("info", "VIDEO_FRAME_ACTION");
			// 视频会议消息类型-成员图象发布或者取消发布
			ECVideoMeetingVideoFrameActionMsg actionMsg = (ECVideoMeetingVideoFrameActionMsg) msg;
			if (actionMsg != null
					&& actionMsg.getMember().equals(
							MyApplication.getInstance().getName())) {
				return;
			}

			if (actionMsg.isPublish()) {
				// showIOSAlert(getString(
				// R.string.videomeeting_member_publish,
				// actionMsg.getMember()));
				Log.i("info", "发布视频");
			} else {
				// showIOSAlert(getString(
				// R.string.videomeeting_member_unpublish,
				// actionMsg.getMember()));
				Log.i("info", "取消视频");
			}
			break;
		case REJECT:
			Log.i("info", "REJECT");
			// 视频会议消息类型-成员拒绝邀请加入会议请求
			ECVideoMeetingRejectMsg rejectMsg = (ECVideoMeetingRejectMsg) msg;
			onVideoMeetingRejectMsg(rejectMsg);
			break;
		default:
			Log.i("info", "can't handle notice msg " + msg.getMsgType());
			break;
		}
	}

	private void onVideoMeetingRejectMsg(ECVideoMeetingRejectMsg msg) {
		if (msg == null) {
			return;
		}
		// ECAlertDialog buildAlert =
		// ECAlertDialog.buildAlert(MultiVideoconference.this
		// , getString(R.string.meeting_invite_reject , msg.getWho()),
		// getString(R.string.dialog_btn_confim), new
		// DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		//
		// }
		// });
		// buildAlert.setTitle(R.string.app_tip);
		// buildAlert.setCanceledOnTouchOutside(false);
		// buildAlert.show();
		AlertDialog dialog = new Builder(this)
				.setTitle("提示!")
				.setMessage(
						getString(R.string.meeting_invite_reject, msg.getWho()))
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						exitOrDismissVideoConference(false);
					}
				}).create();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	public synchronized void putCCPMultiVideoUIKey(Integer key) {

		if (UIKey.size() > 4) {
			return;
		}

		if (key <= 2) {
			return;
		}

		UIKey.add(/* key - 3, */key);
		Collections.sort(UIKey, new Comparator<Integer>() {
			@Override
			public int compare(Integer lsdKey, Integer rsdKey) {

				// Apply sort mode
				return lsdKey.compareTo(rsdKey);
			}
		});
	}

	public void exitOrDismissVideoConference(boolean dismiss) {
		if (dismiss && isVideoConCreate) {
			if (!checkSDK()) {
				return;
			}
			showCustomProcessDialog(getString(R.string.common_progressdialog_title));
			ECDevice.getECMeetingManager().deleteMultiMeetingByType(
					ECMeetingType.MEETING_MULTI_VIDEO, mVideoConferenceId,
					new OnDeleteMeetingListener() {
						public void onMeetingDismiss(ECError reason,
								String meetingNo) {
							dismissPostingDialog();
						}
					});

		} else {
			checkSDK();
			ECDevice.getECMeetingManager().exitMeeting(
					ECMeetingType.MEETING_MULTI_VIDEO);
			if (!isVideoConCreate && dismiss) {
				Intent disIntent = new Intent(
						GlobalConstant.INTENT_VIDEO_CONFERENCE_DISMISS);
				disIntent.putExtra(GlobalConstant.CONFERENCE_ID,
						mVideoConferenceId);
				sendBroadcast(disIntent);
			}
		}
		finish();
	}

	private void removeMemberFromVideoUI(Integer CCPMultiVideoUIKey) {
		if (CCPMultiVideoUIKey != null) {
			putCCPMultiVideoUIKey(CCPMultiVideoUIKey);
			setVideoUITextOperable(CCPMultiVideoUIKey, null);
		}
	}

	private void removeMemberFormVideoUI(String who) {
		Integer key = removeVideoUIMemberFormCache(who);
		removeMemberMulitCache(who);
		removeMemberFromVideoUI(key);
		mVideoMainScreenVoIP = null;
		// mVideoConUI.setVideoMember(CCPMulitVideoUI.LAYOUT_KEY_MAIN_SURFACEVIEW,
		// null);
	}

	private void removeMemberMulitCache(String who) {

		if (mulitMembers == null) {
			return;
		}
		MultiVideoMember removeMember = null;
		for (MultiVideoMember multiVideoMember : mulitMembers) {
			if (multiVideoMember != null
					&& multiVideoMember.getNumber().equals(who)) {
				removeMember = multiVideoMember;
				break;
			}
		}

		if (removeMember != null) {
			mulitMembers.remove(removeMember);
		}
	}

	public Integer removeVideoUIMemberFormCache(String who) {
		synchronized (mVideoMemberUI) {
			Integer key = mVideoMemberUI.remove(who);
			return key;
		}
	}

	public void putVideoUIMemberCache(MultiVideoMember member, Integer key) {
		synchronized (mVideoMemberUI) {
			putVideoUIMemberCache(member.getNumber(), key);
			if (mulitMembers != null) {
				mulitMembers.add(member);
			}
		}
	}

	private boolean isVideoUIMemberExist(String who) {
		synchronized (mVideoMemberUI) {
			if (TextUtils.isEmpty(who)) {
				return false;
			}
			if (mVideoMemberUI.containsKey(who)) {
				return true;
			}
			return false;
		}
	}

	@Override
	public void onVideoUIItemClick(int key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
