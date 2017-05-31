package com.abcs.huaqiaobang.ytbt.voicemeeting;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.call.CallFailReason;
import com.abcs.huaqiaobang.ytbt.util.JsonUtil;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.abcs.huaqiaobang.ytbt.voicemeeting.MeetingHelper.OnMeetingCallback;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMeetingManager;
import com.yuntongxun.ecsdk.ECMeetingManager.OnInviteMembersJoinToMeetingListener;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.meeting.ECMeeting;
import com.yuntongxun.ecsdk.meeting.ECMeetingMember;
import com.yuntongxun.ecsdk.meeting.ECMeetingMsg.ForbidOptions;
import com.yuntongxun.ecsdk.meeting.ECVoiceMeetingMember;
import com.yuntongxun.ecsdk.meeting.voice.ECVoiceMeetingExitMsg;
import com.yuntongxun.ecsdk.meeting.voice.ECVoiceMeetingJoinMsg;
import com.yuntongxun.ecsdk.meeting.voice.ECVoiceMeetingMemberForbidOpt;
import com.yuntongxun.ecsdk.meeting.voice.ECVoiceMeetingMsg;
import com.yuntongxun.ecsdk.meeting.voice.ECVoiceMeetingRejectMsg;
import com.yuntongxun.ecsdk.meeting.voice.ECVoiceMeetingRemoveMemberMsg;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VoiceMeetingActivity extends VoiceMeetingBaseActivity implements
		View.OnClickListener {
	private static final String TAG = "info";
	public static final String EXTRA_MEETING = "com.yuntongxun.ecdemo.Meeting";
	public static final String EXTRA_MEETING_PASS = "com.yuntongxun.ecdemo.Meeting_Pass";
	public static final String EXTRA_CALL_IN = "com.yuntongxun.ecdemo.Meeting_Join";
	public static final int REQUEST_CONTACTS = 999;
	public static final int REQUEST_FRIENDS = 997;
	public static final int REQUEST_CODE_KICK_MEMBER = 0x001;
	/** 外呼电话邀请加入会议 */
	public static final int REQUEST_CODE_INVITE_BY_PHONECALL = 0x002;
	/** 会议成员显示控件 */
	private GridView mGridView;
	/** 会议成员显示控件适配器 */
	private MeetingMemberAdapter mMeetingMemberAdapter;
	/** 会议信息 */
	private ECMeeting mMeeting;
	/** 会议房间密码 */
	private String mMeetingPassword;
	/** 是否需要呼入会议 */
	private boolean mMeetingCallin;
	/** 会议成员 */
	public static List<ECVoiceMeetingMember> sMembers;
	/** 是否是自己创建的会议房间 */
	private boolean isSelfMeeting = false;
	/** 会议房间是否已经被解散 */
	private boolean isMeetingOver = false;
	private boolean isMeeting = false;
	/** 是否是扬声器模式 */
	private boolean mSpeakerOn = false;
	/*** 会议成员是否有自己 */
	private boolean hasSelf = false;
	/** 是否静音 */
	private boolean isMikeEnable;
	private TextView micState;
	private ImageView mic;
	private MeetingEntity meetingEntity;
	private RelativeLayout manager;
	public static ArrayList<User> users;
	private HttpUtils httpUtils;
	private PopupWindow popupWindow;

	/* (non-Javadoc)
	 * @see com.abcs.huaqiaobang.ytbt.voicemeeting.VoiceMeetingBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		httpUtils = new HttpUtils(30000);
		httpUtils.configCurrentHttpCacheExpiry(1000);
		setContentView(R.layout.activity_voice_meeting);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		Log.i("info", "VoiceMeetingActivity");
		mMeeting = getIntent().getParcelableExtra(EXTRA_MEETING);
		mMeetingPassword = getIntent().getStringExtra(EXTRA_MEETING_PASS);
		mMeetingCallin = getIntent().getBooleanExtra(EXTRA_CALL_IN, false);
		try {
			MyApplication.dbUtils.createTableIfNotExist(MeetingEntity.class);
			meetingEntity = MyApplication.dbUtils.findFirst(Selector.from(
					MeetingEntity.class).where("meetingNo", "=",
					mMeeting.getMeetingNo()));
			if (meetingEntity != null) {
				if (!TextUtils.isEmpty(meetingEntity.getUsers())) {
					try {
						users = JsonUtil.parseString(meetingEntity.getUsers());
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					users = new ArrayList<User>();
				}
			} else {
				meetingEntity = new MeetingEntity();
				meetingEntity.setMeetingNo(mMeeting.getMeetingNo());
				users = new ArrayList<User>();
				meetingEntity.setUsers("");
				MyApplication.dbUtils.saveOrUpdate(meetingEntity);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		initView();
		if (mMeeting == null) {
			Log.i(TAG, " meeting error , meeting null");
			finish();
			return;
		}
		// setTopBar(CCPAppManager.getClientUser().getUserId()
		// .equals(mMeeting.getCreator()));
		isSelfMeeting = MyApplication.getInstance().getName()
				.equals(mMeeting.getCreator());
		if (isSelfMeeting) {
			manager.setVisibility(View.VISIBLE);
		}
		// // 创建一个下拉菜单
		// mOverflowHelper = new OverflowHelper(this);
		// initOverflowItems();
		if (mMeetingCallin) {
			// 判断是否需要调用加入接口加入会议
			// mInterPhoneBannerView.setTips(R.string.top_tips_connecting_wait);
			joinMeeting();
			return;
		}
		isMeeting = true;
	}

	private void joinMeeting() {
		ECMeetingManager meetingManager = ECDevice.getECMeetingManager();
		if (meetingManager == null) {
			return;
		}
		meetingManager.joinMeetingByType(mMeeting.getMeetingNo(),
				mMeetingPassword,
				ECMeetingManager.ECMeetingType.MEETING_MULTI_VOICE,
				new ECMeetingManager.OnCreateOrJoinMeetingListener() {
					@Override
					public void onCreateOrJoinMeeting(ECError reason,
							String meetingNo) {
						if (reason.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
							onMeetingStart(meetingNo);
							return;
						}
						onError(0x0013, reason);
					}
				});
	}

	private void initView() {
		mGridView = (GridView) findViewById(R.id.chatroom_member_list);
		((TextView) findViewById(R.id.tips)).setText(getString(
				R.string.str_voice_room_name, mMeeting.getMeetingName()));
		mMeetingMemberAdapter = new MeetingMemberAdapter(this);
		mGridView.setAdapter(mMeetingMemberAdapter);
		micState = (TextView) findViewById(R.id.mic_state);
		mic = (ImageView) findViewById(R.id.iv_mic);
		mic.setOnClickListener(this);
		manager = (RelativeLayout) findViewById(R.id.manager);
		manager.setOnClickListener(this);
		findViewById(R.id.forbid).setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.sound_mode).setOnClickListener(this);
		((TextView) findViewById(R.id.title))
				.setText(mMeeting.getMeetingName());
		// ((TextView)findViewById(R.id.meetingNo)).setText(mMeeting.getMeetingNo());
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isMeeting) {
			queryMeetingMembers(mMeeting.getMeetingNo());
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
		isMikeEnable = true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		List<User> list = new ArrayList<>();
		for (ECVoiceMeetingMember m : sMembers) {
			User u = new User();
			u.setVoipAccout(m.getNumber());
			list.add(u);
		}
		if (requestCode == REQUEST_CONTACTS && resultCode == 1) {
			String num = data.getStringExtra("num");
			String nums = data.getStringExtra("nums");
			if (num != null) {
				doInviteMobileMember(num, mMeeting.getMeetingNo(), true);
			} else if (nums != null) {
				String[] phones = nums.split(",");
				for (String string : phones) {
					User u = new User();
					u.setVoipAccout(string);
					if (list.contains(u)) {
						Tool.showInfo(VoiceMeetingActivity.this, "对方已在会议中");
						return;
					}
					doInviteMobileMember(string, mMeeting.getMeetingNo(), true);
				}
			}
		}
		if (requestCode == REQUEST_FRIENDS && resultCode == 1) {
			String nums = data.getStringExtra("nums");
			if (nums != null) {
				// doInviteMobileMember(num, mMeeting.getMeetingNo(), false);
				String[] phones = nums.split(",");
				for (String string : phones) {
					User u = new User();
					u.setVoipAccout(string);
					if (list.contains(u)) {
						Tool.showInfo(VoiceMeetingActivity.this, "对方已在会议中");
						return;
					}
					doInviteMobileMember(string, mMeeting.getMeetingNo(), false);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onMeetingMembers(List<? extends ECMeetingMember> members) {
		super.onMeetingMembers(members);
		Log.i("info", "onMeetingMembers");
		sMembers = (List<ECVoiceMeetingMember>) members;
		if (sMembers != null) {
			for (ECVoiceMeetingMember mbr : sMembers) {
				if (mbr != null
						&& mbr.getNumber().equals(
								MyApplication.getInstance().getName())
						&& !mbr.isMobile()) {
					hasSelf = true;
				}
			}
		}
		if (!hasSelf) {
			ECVoiceMeetingMember member = new ECVoiceMeetingMember();
			member.setNumber(MyApplication.getInstance().getName());
			member.setIsMobile(false);
			if (sMembers == null) {
				sMembers = new ArrayList<ECVoiceMeetingMember>();
			}
			sMembers.add(member);
			hasSelf = true;
		}

		if (mMeetingMemberAdapter == null) {
			mMeetingMemberAdapter = new MeetingMemberAdapter(this);
			mMeetingMemberAdapter.setMembers(sMembers);
			mGridView.setAdapter(mMeetingMemberAdapter);
		}
		mMeetingMemberAdapter.setMembers(sMembers);
	}

	@Override
	public void onMeetingStart(String meetingNo) {
		super.onMeetingStart(meetingNo);
		Log.i("info", "onMeetingStart");
		isMeeting = true;
		// 加入会议成功
		hasSelf = false;
		// MeetingHelper.queryMeetingMembers(meetingNo);
		isMikeEnable = true;
		queryMeetingMembers(meetingNo);
	}

	private void queryMeetingMembers(String meetingNo) {
		ECMeetingManager meetingManager = ECDevice.getECMeetingManager();
		if (meetingManager == null) {
			return;
		}
		meetingManager
				.queryMeetingMembersByType(
						meetingNo,
						ECMeetingManager.ECMeetingType.MEETING_MULTI_VOICE,
						new ECMeetingManager.OnQueryMeetingMembersListener<ECVoiceMeetingMember>() {
							@Override
							public void onQueryMeetingMembers(ECError reason,
									List<ECVoiceMeetingMember> members) {
								if (reason.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
									// getInstance().notifyMeetingMembers(members);
									onMeetingMembers(members);
									return;
								}
								// getInstance().notifyError(OnMeetingCallback.QUERY_MEMBERS,
								// reason);
								onError(0x0012, reason);
							}
						});
	}

	@Override
	public void onMeetingDismiss(String meetingNo) {
		super.onMeetingDismiss(meetingNo);
		Log.i("info", "onMeetingDismiss");
		isMeeting = false;
		if (mMeeting != null && meetingNo != null
				&& meetingNo.equals(mMeeting.getMeetingNo())) {
			dismissPostingDialog();
			finish();
		}
	}

	@Override
	public void onReceiveVoiceMeetingMsg(ECVoiceMeetingMsg msg) {
		super.onReceiveVoiceMeetingMsg(msg);
		Log.i("info", "onReceiveVoiceMeetingMsg");
		if (msg == null
				|| !(mMeeting != null && msg.getMeetingNo().equals(
						mMeeting.getMeetingNo()))) {
			Log.i(TAG, "onReceiveVoiceMeetingMsg error msg " + msg + " , no "
					+ msg.getMeetingNo());
			return;
		}
		if (sMembers == null) {
			sMembers = new ArrayList<ECVoiceMeetingMember>();
		}
		boolean handle = convertToVoiceMeetingMember(msg);
		// 是否列表数据有改变
		if (handle && mMeetingMemberAdapter != null) {
			if (sMembers != null) {
				mMeetingMemberAdapter.setMembers(sMembers);
				mMeetingMemberAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onError(int type, ECError e) {
		Log.i("info", "onError");
		dismissPostingDialog();
		String msg = getResources().getString(
				CallFailReason.getCallFailReason(e.errorCode));
		if (msg == null || TextUtils.isEmpty(msg)) {
			Tool.showInfo(this, "请求错误["
					+ (e.errorCode == 111705 ? "非房间创建者无权解散房间 " : e.errorCode)
					+ "]");
		} else {
			Tool.showInfo(this, msg);
		}
		if (OnMeetingCallback.MEETING_JOIN == type) {
			// 加入会议失败
			MeetingHelper.exitMeeting();
			finish();
		}
	}

	/**
	 * 转换成成员消息
	 * 
	 * @param msg
	 */
	private boolean convertToVoiceMeetingMember(ECVoiceMeetingMsg msg) {
		if (msg.getMsgType() == ECVoiceMeetingMsg.ECVoiceMeetingMsgType.JOIN) {
			ECVoiceMeetingJoinMsg joinMsg = (ECVoiceMeetingJoinMsg) msg;
			// 有人加入会议消息
			for (String who : joinMsg.getWhos()) {
				Log.i(TAG, " hasSelf :" + hasSelf);
				if (who.equals(MyApplication.getInstance().getName())
						&& hasSelf && (!joinMsg.isMobile())) {
					Log.i(TAG, " hasSelf true");
					continue;
				}
				ECVoiceMeetingMember member = new ECVoiceMeetingMember();
				member.setNumber(who);
				member.setIsMobile(joinMsg.isMobile());
				if (!isMemberExist(member)) {
					sMembers.add(member);
					User u = null;
					if (joinMsg.isMobile()) {
						u = new User();
						u.setVoipAccout(who);
						u.setNickname("m" + who);
					} else {
						try {
							u = MyApplication.dbUtils.findFirst(Selector.from(
									User.class).where("voipAccout", "=", who));
						} catch (DbException e) {
							e.printStackTrace();
						}
					}
					if (u != null && !users.contains(u)) {
						users.add(u);
						Log.i("info", users.toString());
						meetingEntity.setUsers(JsonUtil.toString(users));
						try {
							MyApplication.dbUtils.saveOrUpdate(meetingEntity);
						} catch (DbException e) {
							e.printStackTrace();
						}
					}
				}
				Log.i(TAG, " hasSelf " + who);
				// updateTopMeetingBarTips(getString(R.string.str_chatroom_join,
				// member.getNumber()));
			}
			return true;
		}

		if (msg.getMsgType() == ECVoiceMeetingMsg.ECVoiceMeetingMsgType.EXIT) {
			ECVoiceMeetingExitMsg exitMsg = (ECVoiceMeetingExitMsg) msg;
			// 有人退出会议消息
			List<ECVoiceMeetingMember> exitMembers = new ArrayList<ECVoiceMeetingMember>();
			for (ECVoiceMeetingMember member : sMembers) {
				if (member != null && member.getNumber() != null) {
					for (String who : exitMsg.getWhos()) {
						if (member.getNumber().equals(who)
								&& (exitMsg.isMobile() == member.isMobile())) {
							exitMembers.add(member);
						}
					}
				}
			}
			if (exitMembers.size() > 0) {
				isMeetingOver = false;
				sMembers.removeAll(exitMembers);
				// updateTopMeetingBarTips(getString(R.string.str_chatroom_exit,
				// exitMembers.get(0).getNumber()));
			}
			return true;
		}

		if (msg.getMsgType() == ECVoiceMeetingMsg.ECVoiceMeetingMsgType.REMOVE_MEMBER) {
			ECVoiceMeetingRemoveMemberMsg removeMemberMsg = (ECVoiceMeetingRemoveMemberMsg) msg;
			// 有成员被移除出会议消息
			if ((!removeMemberMsg.isMobile())
					&& removeMemberMsg != null
					&& MyApplication.getInstance().getName()
							.equals(removeMemberMsg.getWho())) {
				// 如果被移除出会议的成员是自己
				// 提示被移除出聊天室对话框
				isMeetingOver = false;
				showRemovedFromChatroomAlert();
				return false;
			} else {
				// 如果移除的成员是其他人
				ECVoiceMeetingMember rMember = null;
				for (ECVoiceMeetingMember member : sMembers) {
					String number = member.getNumber();
					if (member != null
							&& number != null
							&& (member.isMobile() == removeMemberMsg.isMobile())
							&& number.equals(removeMemberMsg.getWho())) {
						rMember = member;
						break;
					}
				}
				if (rMember != null) {
					sMembers.remove(rMember);
					// 刷新会议通知栏
					// updateTopMeetingBarTips(getString(
					// R.string.str_chatroom_kick, rMember.getNumber()));
				}
				return true;
			}
		}

		// 处理会议房间被解散消息
		if (msg.getMsgType() == ECVoiceMeetingMsg.ECVoiceMeetingMsgType.DELETE) {
			if (isSelfMeeting && isMeetingOver) {
				// 不需要处理
				return false;
			}
			onMeetingRoomDel(msg);
			return false;
		}

		if (msg.getMsgType() == ECVoiceMeetingMsg.ECVoiceMeetingMsgType.SPEAK_OPT) {
			// 处理会议成员被禁言操作
			doChatroomMemberForbidOpt((ECVoiceMeetingMemberForbidOpt) msg);
		}

		if (msg.getMsgType() == ECVoiceMeetingMsg.ECVoiceMeetingMsgType.REJECT) {
			// 处理对方拒绝邀请加入请求
			ECVoiceMeetingRejectMsg rejectMsg = (ECVoiceMeetingRejectMsg) msg;
			onVoiceMeetingRejectMsg(rejectMsg);
		}

		return false;
	}

	private boolean isMemberExist(ECVoiceMeetingMember member) {
		for (ECVoiceMeetingMember item : sMembers) {
			if (item != null && (item.getNumber().equals(member.getNumber()))
					&& (item.isMobile() == member.isMobile())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 处理会议成员被禁言
	 * 
	 * @param forbidOpt
	 * @return
	 */
	private boolean doChatroomMemberForbidOpt(
			ECVoiceMeetingMemberForbidOpt forbidOpt) {
		if (forbidOpt == null) {
			return false;
		}
		try {
			String userId = MyApplication.getInstance().getName();
			ForbidOptions options = forbidOpt.getForbid();
			if (options.inSpeak == ForbidOptions.OPTION_SPEAK_LIMIT
					&& userId.equals(forbidOpt.getMember())) {
				showDialog("提示", "您被会议管理员设置为禁言模式，你的发言将无法被会议成员所听到", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// updateTopMeetingBarTips(getString(R.string.top_tips_chatroom_disforbid,
		// forbidOpt.getMember()));
		return false;
	}

	/**
	 * 提示被移除出聊天室对话框
	 */
	private void showRemovedFromChatroomAlert() {
		if (isFinishing()) {
			return;
		}
		AlertDialog dialog = new Builder(this).setTitle("您已被请出房间")
				.setCancelable(false)
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	/**
	 * 处理会议房间被解散消息
	 * 
	 * @param msg
	 *            会议被解散的消息
	 */
	private void onMeetingRoomDel(ECVoiceMeetingMsg msg) {

		if (isFinishing()) {
			return;
		}
		try {
			showDialog("房间被解散", "抱歉，该房间已被创建者解散，点击确定以退出", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 处理Mic状态
		// notifyMeetingMikeEnable(true);
		// updateTopMeetingBarTips(getString(R.string.dialog_title_be_dissmiss_chatroom));
	}

	private void onVoiceMeetingRejectMsg(ECVoiceMeetingRejectMsg msg) {
		if (msg == null) {
			return;
		}
		String name = "";
		try {
			User u = MyApplication.dbUtils.findById(User.class, msg.getWho());
			if (u != null) {
				name = u.getRemark().trim().equals("") ? u.getNickname() : u
						.getRemark();
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		AlertDialog dialog = new Builder(this).setTitle("提示")
				.setMessage(name.equals("") ? msg.getWho() : name + "拒绝了你的请求")
				.setCancelable(false)
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		Builder dialog = new Builder(this);
		dialog.setTitle("退出会议").setMessage("退出后将不会收到会议消息")
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						exitOrDismissChatroom(false);
					}
				}).setNegativeButton("取消", null).show();
	}

	/**
	 * 处理会议退出逻辑
	 */
	private void doExitChatroomAction() {
		// notifyMeetingMikeEnable(true);
		if (isSelfMeeting) {
			exitOrDismissChatroom(true);
		} else {
			// Here is the receipt dissolution news, not so directly off the
			// Page
			finish();
		}
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
			MeetingHelper.exitMeeting();
			ECHandlerHelper.postDelayedRunnOnUI(new Runnable() {
				@Override
				public void run() {
					finish();
				}
			}, 2000);
		} else {
			showProcessDialog();
			if (mMeeting != null) {
				disMeeting(mMeeting.getMeetingNo());
				isMeetingOver = true;
				isMeeting = false;
			}
		}
	}

	public void disMeeting(String meetingNo) {

		ECMeetingManager meetingManager = ECDevice.getECMeetingManager();
		if (meetingManager == null) {
			return;
		}
		meetingManager.deleteMultiMeetingByType(
				ECMeetingManager.ECMeetingType.MEETING_MULTI_VOICE, meetingNo,
				new ECMeetingManager.OnDeleteMeetingListener() {
					@Override
					public void onMeetingDismiss(ECError reason,
							String meetingNo) {
						if (reason.errorCode == SdkErrorCode.REQUEST_SUCCESS) {

							isMeeting = false;
							if (mMeeting != null
									&& meetingNo != null
									&& meetingNo.equals(mMeeting.getMeetingNo())) {
								dismissPostingDialog();
								finish();
							}
							return;
						}
						Tool.showInfo(VoiceMeetingActivity.this, "解散会议失败,错误码"
								+ reason.errorCode);
					}
				});

	}

	public class MeetingMemberAdapter extends
			ArrayAdapter<ECVoiceMeetingMember> {

		public MeetingMemberAdapter(Context context) {
			super(context, 0, new ArrayList<ECVoiceMeetingMember>());
		}

		public void setMembers(List<? extends ECMeetingMember> members) {
			clear();
			if (members != null) {
				for (ECMeetingMember member : members) {
					if (member instanceof ECVoiceMeetingMember) {
						if (((ECVoiceMeetingMember) member).getNumber().equals(
								MyApplication.getInstance().getName())) {
							super.insert((ECVoiceMeetingMember) member, 0);
						} else {
							super.add((ECVoiceMeetingMember) member);
						}
					}
				}
			}

		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			final MeetingHolder holder;
			if (convertView == null || convertView.getTag() == null) {
				view = getLayoutInflater().inflate(
						R.layout.list_item_meeting_member, null);
				holder = new MeetingHolder();
				view.setTag(holder);

				holder.name = (TextView) view.findViewById(R.id.member_name);
				holder.icon = (ImageView) view.findViewById(R.id.meeting_icon);
			} else {
				view = convertView;
				holder = (MeetingHolder) convertView.getTag();
			}
			ECVoiceMeetingMember member = getItem(position);
			if (member != null) {
				if (MyApplication.getInstance().getName()
						.equals(member.getNumber())) {
					holder.icon.setImageResource(R.drawable.touxiang);
				} else {
					holder.icon.setImageResource(R.drawable.img_chengyuan);
				}

				if (member.isMobile()) {
					holder.name.setText("m" + member.getNumber());
				} else if (member.getNumber().equals(
						MyApplication.getInstance().getName())) {
					holder.name.setText(MyApplication.getInstance()
							.getOwnernickname());
				} else {
					try {
						User u = MyApplication.dbUtils.findFirst(Selector.from(
								User.class).where("voipAccout", "=",
								member.getNumber()));
						if (u != null) {
							holder.name
									.setText(u.getRemark().trim().equals("") ? u
											.getNickname() : u.getRemark());
						} else {
							holder.name.setText(member.getNumber());
							httpUtils.send(HttpMethod.GET, TLUrl.URL_GET_VOIP
									+ TLUrl.URL_GET_VOIP + "User/findvoipuser"
									+ "voipAccount=" + member.getNumber(),
									new RequestCallBack<String>() {

										@Override
										public void onFailure(
												HttpException arg0, String arg1) {
										}

										@Override
										public void onSuccess(
												ResponseInfo<String> arg0) {
											try {
												holder.name
														.setText(new JSONObject(
																arg0.result)
																.getString("nickname"));
											} catch (JSONException e) {
												e.printStackTrace();
											}
										}
									});
						}
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
			}

			return view;
		}

		class MeetingHolder {
			TextView name;
			ImageView icon;
		}
	}

	@SuppressLint("InflateParams")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.manager:
			showPopupWindow(manager);
			break;
//			PopupMenu menu = new PopupMenu(this, v);
//			menu.getMenuInflater().inflate(R.menu.voice_meeting_menu,
//					menu.getMenu());
//			menu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//				@Override
//				public boolean onMenuItemClick(final MenuItem item) {
//					switch (item.getItemId()) {
					case R.id.menu_invite_tel:
						try {
							Intent i = new Intent(VoiceMeetingActivity.this,
									InviteContactActivity.class);
							i.putExtra("isNums", true);
							startActivityForResult(i, REQUEST_CONTACTS);
						} catch (Exception e) {
							e.printStackTrace();
						}
						popupWindow.dismiss();
						break;
					case R.id.menu_invite_voip:
						Log.i("xbb8", "点击了" + "");
						try {
							Intent b = new Intent(VoiceMeetingActivity.this,
									FriendsListActivity.class);
							b.putExtra("type", 1);
							startActivityForResult(b, REQUEST_FRIENDS);
						} catch (Exception e) {
							e.printStackTrace();
						}
						popupWindow.dismiss();
						// Builder dialog = new AlertDialog.Builder(
						// VoiceMeetingActivity.this);
						// LayoutInflater inflater = (LayoutInflater)
						// VoiceMeetingActivity.this
						// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						// LinearLayout layout = (LinearLayout)
						// inflater.inflate(
						// R.layout.dialogview, null);
						// dialog.setView(layout);
						// final EditText et_search = (EditText) layout
						// .findViewById(R.id.searchC);
						// dialog.setPositiveButton("确定",
						// new DialogInterface.OnClickListener() {
						// public void onClick(DialogInterface dialog,
						// int which) {
						// String num = et_search.getText()
						// .toString().trim();
						// if (!TextUtils.isEmpty(num)) {
						// if (mMeeting == null
						// && mMeeting.getMeetingNo() == null) {
						// return;
						// }
						// doInviteMobileMember(
						// num,
						// mMeeting.getMeetingNo(),
						// item.getItemId() == R.id.menu_invite_tel ? true
						// : false);
						// }
						// }
						// });
						// dialog.setNegativeButton("取消", null);
						// dialog.show();
						break;
					// case R.id.menu_sound_mode:
					// // 更改当前的扬声器模式
					// changeSpeakerOnMode();
					// break;
					case R.id.menu_manager_members:
						if (mMeeting == null && mMeeting.getMeetingNo() == null) {
							return ;
						}
						// 管理会议成员操作
						Intent intent = new Intent(VoiceMeetingActivity.this,
								HistoryMemberActivity.class);
						intent.putExtra(EXTRA_MEETING, mMeeting);
						startActivityForResult(intent, REQUEST_CODE_KICK_MEMBER);
						popupWindow.dismiss();
						break;
					// case R.id.menu_close_room:
					// try {
					// showDialog("解散房间", "您确定要解散该房间吗，解散后不可恢复", true);
					// } catch (Exception e) {
					// e.printStackTrace();
					// }
					// break;
//					}
//					return true;
//				}
//			});
//			menu.show();
//			break;
		case R.id.back:
			exit();
			break;
		case R.id.forbid:
			forbidAll();
			break;
		case R.id.sound_mode:
			changeSpeakerOnMode();
			break;
		case R.id.iv_mic:
			ECVoIPSetupManager setupManager = ECDevice.getECVoIPSetupManager();
			if (setupManager != null) {
				int state = setupManager.setMute(isMikeEnable);
				if (state == 0) {
					isMikeEnable = !isMikeEnable;
				}
				if (!isMikeEnable) {
					micState.setText("点击打开麦克风");
					mic.setImageResource(R.drawable.img_jieting2);
				} else {
					micState.setText("点击关闭麦克风");
					mic.setImageResource(R.drawable.img_bohao2);
				}
			}
			break;
		}
	}
	private void showPopupWindow(View view) {
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.voice_meeting_member_menu, null);
		popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		contentView.findViewById(R.id.menu_invite_tel).setOnClickListener(this);
		contentView.findViewById(R.id.menu_invite_voip).setOnClickListener(this);
		contentView.findViewById(R.id.menu_manager_members).setOnClickListener(this);
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

	/**
	 * 更改当前的扬声器模式
	 */
	private void changeSpeakerOnMode() {
		ECVoIPSetupManager setupManager = ECDevice.getECVoIPSetupManager();
		if (setupManager == null) {
			return;
		}
		boolean speakerOn = setupManager.getLoudSpeakerStatus();
		setupManager.enableLoudSpeaker(!speakerOn);
		mSpeakerOn = setupManager.getLoudSpeakerStatus();
		if (mSpeakerOn) {
			Tool.showInfo(this, "已切换为扬声器模式");
		} else {
			Tool.showInfo(this, "已切换为听筒模式");
		}
	}

	private void showDialog(String title, String msg, boolean needNegativeButton) {
		Builder dialog = new Builder(VoiceMeetingActivity.this).setTitle(title)
				.setMessage(msg).setCancelable(false)
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						doExitChatroomAction();
					}
				});
		if (needNegativeButton) {
			dialog.setNegativeButton("取消", null);
		}
		dialog.create().setCanceledOnTouchOutside(false);
		dialog.create().show();
	}

	/**
	 * 处理邀请成员加入会议请求
	 */
	private void doInviteMobileMember(String phoneNumber, String mMeetingNo,
			boolean misLandingCall) {
		ECMeetingManager meetingManager = ECDevice.getECMeetingManager();
		if (meetingManager == null) {
			return;
		}
		showProcessDialog();
		meetingManager.inviteMembersJoinToMeeting(mMeetingNo,
				new String[] { phoneNumber }, misLandingCall,
				new OnInviteMembersJoinToMeetingListener() {
					@Override
					public void onInviteMembersJoinToMeeting(ECError reason,
							String arg1) {

						dismissPostingDialog();
						if (SdkErrorCode.REQUEST_SUCCESS == reason.errorCode) {
							// 邀请加入会议成功
							Tool.showInfo(VoiceMeetingActivity.this,
									"邀请成功,等待对方接受邀请");
							return;
						}
						Tool.showInfo(VoiceMeetingActivity.this, "邀请加入会议失败["
								+ reason.errorCode + "]");
					}
				});

	}
	private void forbidAll(){
		for (ECVoiceMeetingMember m : sMembers) {
			m.setForbid(new ForbidOptions(ForbidOptions.OPTION_SPEAK_LIMIT, ForbidOptions.OPTION_LISTEN_FREE));
		}
	}
}
