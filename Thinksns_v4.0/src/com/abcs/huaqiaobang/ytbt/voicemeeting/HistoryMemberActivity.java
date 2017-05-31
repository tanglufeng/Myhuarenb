package com.abcs.huaqiaobang.ytbt.voicemeeting;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECDevice.OnGetUserStateListener;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMeetingManager;
import com.yuntongxun.ecsdk.ECMeetingManager.OnInviteMembersJoinToMeetingListener;
import com.yuntongxun.ecsdk.ECUserState;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.meeting.ECMeeting;
import com.yuntongxun.ecsdk.meeting.ECVoiceMeetingMember;

import java.util.ArrayList;
import java.util.List;

public class HistoryMemberActivity extends VoiceMeetingBaseActivity implements
		OnClickListener {
	private ListView mListView;
	private MeetingMemberAdapter mListAdapter;
	private ECMeeting mMeeting;
	private List<User> list = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_meeting_members);
		mMeeting = getIntent().getParcelableExtra(
				VoiceMeetingActivity.EXTRA_MEETING);
		if (mMeeting == null) {
			finish();
			return;
		}
		for (ECVoiceMeetingMember m : VoiceMeetingActivity.sMembers) {
			User u = new User();
			u.setVoipAccout(m.getNumber());
			list.add(u);
		}
		initView();
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.meeting_member_lv);
		View emptyView = findViewById(R.id.empty_tip_recommend_bind_tv);
		mListAdapter = new MeetingMemberAdapter(this);
		mListAdapter.setMembers(VoiceMeetingActivity.users);
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemLongClickListener(null);
		mListView.setEmptyView(emptyView);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.btn_all).setOnClickListener(this);
	}

	/**
	 * 处理邀请成员加入会议请求
	 */
	private void doInviteMobileMember(String phoneNumber, String mMeetingNo,
			boolean misLandingCall) {
		User u = new User();
		u.setVoipAccout(phoneNumber);
		if(list.contains(u)){
			Tool.showInfo(HistoryMemberActivity.this,
					"对方已在会议中");
			return;
		}
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
							Tool.showInfo(HistoryMemberActivity.this,
									"邀请成功,等待对方接受邀请");
							return;
						}
						Tool.showInfo(HistoryMemberActivity.this, "邀请加入会议失败["
								+ reason.errorCode + "]");
					}
				});

	}

	public class MeetingMemberAdapter extends ArrayAdapter<User> {

		public MeetingMemberAdapter(Context context) {
			super(context, 0, new ArrayList<User>());
		}

		public void setMembers(List<User> members) {
			clear();
			if (members != null) {
				for (User member : members) {
					if (member instanceof User) {
						super.add((User) member);
					}
				}
			}
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder mViewHolder;
			if (convertView == null || convertView.getTag() == null) {
				convertView = View.inflate(getContext(),
						R.layout.meeting_member_item, null);

				mViewHolder = new ViewHolder();
				mViewHolder.mNikeName = (TextView) convertView
						.findViewById(R.id.tv_name);
				// mViewHolder.mPermission = (TextView) view
				// .findViewById(R.id.meeting_contact_item_digest_tv);
				mViewHolder.mOperatekick = (Button) convertView
						.findViewById(R.id.bt_del);
				// mViewHolder.mOperateMute = (Button) view
				// .findViewById(R.id.chatroom_contact_mute_btn);
				// mViewHolder.mAvatar = (ImageView) view
				// .findViewById(R.id.content);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			final User item = getItem(position);
			if (item != null) {
				try {
					User u = MyApplication.dbUtils.findById(User.class, item.getVoipAccout());
					if(u!=null){
						mViewHolder.mNikeName.setText(u.getRemark().trim().equals("")?u.getNickname():u.getRemark());
					}else{
						mViewHolder.mNikeName.setText(item.getNickname());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// if (CCPAppManager.getUserId().equals(item.getNumber())
				// && !item.isMobile()) {
				// mViewHolder.mOperatekick.setVisibility(View.GONE);
				// mViewHolder.mOperateMute.setVisibility(View.GONE);
				// return view;
				// }
				mViewHolder.mOperatekick.setVisibility(View.VISIBLE);
				if (item.getVoipAccout().matches("\\d{16}")) {
					ECDevice.getUserState(item.getVoipAccout(),
							new OnGetUserStateListener() {
								@Override
								public void onGetUserState(ECError error,
										ECUserState userState) {
									// 处理获取对方状态的回调结果
									// 通过userState就能知道对方的终端类型、网络及在线状态等等
									if (userState != null
											&& !userState.isOnline()) {
										mViewHolder.mOperatekick
												.setEnabled(false);
									}
								}
							});
				}
				// mViewHolder.mOperateMute.setVisibility(View.GONE);

				// final ECVoiceMeetingMsg.ForbidOptions options = item
				// .getForbid();
				// if (options.inSpeak ==
				// ECVoiceMeetingMsg.ForbidOptions.OPTION_SPEAK_FREE) {
				// mViewHolder.mOperateMute
				// .setText(R.string.chatroom_permission_mute);
				// } else {
				// mViewHolder.mOperateMute
				// .setText(R.string.chatroom_permission_mute_revert);
				// }
				mViewHolder.mOperatekick
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (item.getVoipAccout().matches("\\d{16}")) {
									doInviteMobileMember(item.getVoipAccout(),
											mMeeting.getMeetingNo(), false);
								} else {
									doInviteMobileMember(item.getVoipAccout(),
											mMeeting.getMeetingNo(), true);
								}
							}
						});
			}
			return convertView;

		}

		class ViewHolder {
			TextView mNikeName;
			ImageView mAvatar;
			TextView mPermission;
			Button mOperatekick;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.btn_all:
			for (final User u : VoiceMeetingActivity.users) {
				if (u.getVoipAccout().matches("\\d{16}")) {
					ECDevice.getUserState(u.getVoipAccout(),
							new OnGetUserStateListener() {
								@Override
								public void onGetUserState(ECError error,
										ECUserState userState) {
									// 处理获取对方状态的回调结果
									// 通过userState就能知道对方的终端类型、网络及在线状态等等
									if (userState != null
											&& !userState.isOnline()) {
										Toast.makeText(
												HistoryMemberActivity.this,
												u.getNickname() + "不在线",
												Toast.LENGTH_SHORT).show();
									} else {
										doInviteMobileMember(u.getVoipAccout(),
												mMeeting.getMeetingNo(), false);
									}
								}
							});
				} else {
					doInviteMobileMember(u.getVoipAccout(),
							mMeeting.getMeetingNo(), true);
				}
			}

			break;
		}
	}
}
