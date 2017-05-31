package com.abcs.huaqiaobang.ytbt.videomeeting;

import java.util.List;

import android.util.Log;

import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMeetingManager;
import com.yuntongxun.ecsdk.ECMeetingManager.ECMeetingType;
import com.yuntongxun.ecsdk.ECMeetingManager.OnCreateOrJoinMeetingListener;
import com.yuntongxun.ecsdk.ECMeetingManager.OnListAllMeetingsListener;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.meeting.ECMeeting;

public class VideoMeetingHelper {
	private static VideoMeetingHelper meetingHelper = new VideoMeetingHelper();
	private onVideoMeetingCallBack callBack;
	private boolean mSyncMeetings = false;

	public static VideoMeetingHelper getInstance() {
		if(meetingHelper==null){
			meetingHelper = new VideoMeetingHelper();
		}
		return meetingHelper;
	}

	private VideoMeetingHelper() {

	}

	public static void setonVideoMeetingCallBack(onVideoMeetingCallBack callBack) {
		getInstance().callBack = callBack;
	}

	/**
	 * 调用sdk获取会议列表
	 */
	public static boolean doQueryVideoMeetings() {
		// 获取一个会议管理接口对象
		ECMeetingManager meetingManager = ECDevice.getECMeetingManager();
		// 发起查询视频会议请求
		if (meetingManager == null) {
			getInstance().callBack.onError(-1);
			return false;
		}
		getInstance().mSyncMeetings = true;
		meetingManager.listAllMultiMeetingsByType("",
				ECMeetingType.MEETING_MULTI_VIDEO,
				new OnListAllMeetingsListener<ECMeeting>() {
					@Override
					public void onListAllMeetings(ECError reason,
							List<ECMeeting> list) {
						getInstance().mSyncMeetings = false;
						if (SdkErrorCode.REQUEST_SUCCESS == reason.errorCode) {
							// 查询会议成功
							if (getInstance().callBack == null) {
								return;
							}
							getInstance().callBack.onQueryVideoMeetings(list);
							return;
						}
						Log.i("ECSDK_Demo", "del meeting member error["
								+ reason.errorCode + " ]");
						getInstance().callBack.onError(reason.errorCode);
					}
				});
		return true;
	}

	/**
	 * 创建一个新的视频会议
	 */
	public static void doCreatVideoMeeting(
			ECMeetingManager.ECCreateMeetingParams params) {
		// 获取一个会议管理接口对象
		ECMeetingManager meetingManager = ECDevice.getECMeetingManager();
		// 发起查询视频会议请求
		if (meetingManager == null) {
			getInstance().callBack.onError(-1);
			return;
		}
		meetingManager.createMultiMeetingByType(params,
				ECMeetingType.MEETING_MULTI_VIDEO,
				new OnCreateOrJoinMeetingListener() {
					@Override
					public void onCreateOrJoinMeeting(ECError reason,
							String meetingNo) {
						if (reason.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
							getInstance().callBack
									.onJoinVideoMeeting(meetingNo);
							return;
						}
						getInstance().callBack.onError(reason.errorCode);
					}
				});
	}

	/**
	 * 加入一个视频会议
	 */
	public static void doJoinVideoMeeting(String meetingNo, String psw) {
		// 获取一个会议管理接口对象
		ECMeetingManager meetingManager = ECDevice.getECMeetingManager();
		// 发起查询视频会议请求
		if (meetingManager == null) {
			getInstance().callBack.onError(-1);
			return;
		}
		meetingManager.joinMeetingByType(meetingNo, psw,
				ECMeetingType.MEETING_MULTI_VIDEO,
				new OnCreateOrJoinMeetingListener() {

					@Override
					public void onCreateOrJoinMeeting(ECError arg0, String arg1) {
						if (arg0.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
							getInstance().callBack.onJoinVideoMeeting(arg1);
							return;
						}
						getInstance().callBack.onError(arg0.errorCode);
					}
				});
	}

	/**
	 * 邀请加入会议
	 */
	public static void doInviteMembers(String members[], String meetingNo,
			boolean isLandingCall) {
		// memebers[]: 封装需要请请加入会议的成员
		// isLandingCall:表示是否以落地电话形式火者VoIP来电方式
		// 获取一个会议管理接口对象
		ECMeetingManager meetingManager = ECDevice.getECMeetingManager();
		// 发起查询视频会议请求
		if (meetingManager == null) {
			getInstance().callBack.onError(-1);
			return;
		}
		// 发起邀请加入会议请求
		meetingManager.inviteMembersJoinToMeeting(meetingNo, members,
				isLandingCall,
				new ECMeetingManager.OnInviteMembersJoinToMeetingListener() {
					@Override
					public void onInviteMembersJoinToMeeting(ECError reason,
							String meetingNo) {
						if (reason.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
							// 邀请加入会议成功
							
							return;
						}
						Log.e("ECSDK_Demo", "invite member error["
								+ reason.errorCode + " ]");
						getInstance().callBack.onError(reason.errorCode);
					}
				});
	}

	/**
	 * 解散视频会议
	 */
	public static void doCloseVideoMeeting(String meetingNO) {
		ECMeetingManager meetingManager = ECDevice.getECMeetingManager();
		// 发起查询视频会议请求
		if (meetingManager == null) {
			getInstance().callBack.onError(-1);
			return;
		}
		// 发起解散会议请求
		meetingManager.deleteMultiMeetingByType(
				ECMeetingType.MEETING_MULTI_VIDEO, meetingNO,
				new ECMeetingManager.OnDeleteMeetingListener() {
					@Override
					public void onMeetingDismiss(ECError reason,
							String meetingNo) {
						if (reason.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
							// 解散会议成功
							getInstance().callBack.onCloseVideoMeeting(meetingNo);
							return;
						}
						Log.e("ECSDK_Demo", "del meeting error["
								+ reason.errorCode + " ]");
						getInstance().callBack.onError(reason.errorCode);
					}
				});
	}

	/**
	 * 退出视频会议
	 */
	public static void doExitVideoMeeting() {
		ECMeetingManager meetingManager = ECDevice.getECMeetingManager();
		// 发起查询视频会议请求
		if (meetingManager == null) {
			getInstance().callBack.onError(-1);
			return;
		}
		// 发起退出视频会议请求
		meetingManager
				.exitMeeting(ECMeetingType.MEETING_MULTI_VIDEO);
	}

	/**
	 * 视频会议踢出会议成员
	 */
	public static void doKickMember(String meetingNo, String member,
			boolean isMobile) {
		ECMeetingManager meetingManager = ECDevice.getECMeetingManager();
		// 发起查询视频会议请求
		if (meetingManager == null) {
			getInstance().callBack.onError(-1);
			return;
		}
		// 发起视频会议移除成员请求
		meetingManager.removeMemberFromMultiMeetingByType(
				ECMeetingType.MEETING_MULTI_VIDEO, meetingNo,
				member, isMobile,
				new ECMeetingManager.OnRemoveMemberFromMeetingListener() {
					@Override
					public void onRemoveMemberFromMeeting(ECError reason,
							String member) {
						if (SdkErrorCode.REQUEST_SUCCESS == reason.errorCode) {
							// 移除会议成员成功
							getInstance().callBack.onRemoveMember(member);
							return;
						}
						Log.e("ECSDK_Demo", "del meeting member error["
								+ reason.errorCode + " ]");
						getInstance().callBack.onError(reason.errorCode);
					}
				});
	}

	/**
	 * 查询当前视频会议成员
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void doQueryAllMembers(String meetingNo) {
		ECMeetingManager meetingManager = ECDevice.getECMeetingManager();
		// 发起查询视频会议请求
		if (meetingManager == null) {
			getInstance().callBack.onError(-1);
			return;
		}
		// 发起查询视频会议成员请求
		meetingManager.queryMeetingMembersByType(meetingNo,
				ECMeetingType.MEETING_MULTI_VIDEO,
				new ECMeetingManager.OnQueryMeetingMembersListener() {
					@Override
					public void onQueryMeetingMembers(ECError reason,
							List members) {
						if (SdkErrorCode.REQUEST_SUCCESS == reason.errorCode) {
							// 查询视频会议成员成功
							getInstance().callBack.onQueryAllMembers(members);
							return;
						}
						Log.e("ECSDK_Demo", "query meeting member error["
								+ reason.errorCode + " ]");
						getInstance().callBack.onError(reason.errorCode);
					}
				});

	}

	public interface onVideoMeetingCallBack {
		void onQueryVideoMeetings(List<ECMeeting> list);

		void onCreatVideoMeeting(String meetingNo);

		void onJoinVideoMeeting(String meetingNo);

		void onInviteMembers(String meetingNo);

		void onCloseVideoMeeting(String meetingNo);

		void onRemoveMember(String member);

		void onQueryAllMembers(List members);

		void onError(int reason);
	}
}
