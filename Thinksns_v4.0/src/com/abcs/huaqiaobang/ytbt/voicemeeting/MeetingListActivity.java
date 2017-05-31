package com.abcs.huaqiaobang.ytbt.voicemeeting;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.call.CallFailReason;
import com.abcs.huaqiaobang.ytbt.common.dialog.ECListDialog;
import com.abcs.huaqiaobang.ytbt.common.utils.LogUtil;
import com.abcs.huaqiaobang.ytbt.common.utils.ToastUtil;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMeetingManager;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.meeting.ECMeeting;

import java.util.ArrayList;
import java.util.List;

public class MeetingListActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private ListView lv;
	private static final String TAG = "ECSDK_Demo.MeetingListActivity";
	/** 选择联系人 */
	public static final int SELECT_USER_FOR_CHATROOM = 0x002;
	/** 创建群聊房间 */
	public static final int REQUEST_CODE_CREATE = 0x003;
	/** 会议列表适配器 */
	private MeetingAdapter meetingAdapter;
	/** 创建的会议是否自动加入 */
	private boolean mMeetingAutoJoin = false;
	/** 创建会议参数 */
	private ECMeetingManager.ECCreateMeetingParams mMeetingParams;
	private List<ECMeeting> meetings = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_meeting_list);
		initView();
	}

	/**
	 * 初始化界面控件
	 */
	private void initView() {
		findViewById(R.id.bt_creatMeeting).setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
		lv = (ListView) findViewById(R.id.list_meeting);
		lv.setOnItemClickListener(this);
		lv.setEmptyView(findViewById(R.id.empty_tip_recommend_bind_tv));
		findViewById(R.id.bt_creatMeeting).setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i("info", "MeetingListFragment:onResume");
		// boolean handle = MeetingHelper
		// .queryMeetings(ECMeetingManager.ECMeetingType.MEETING_MULTI_VOICE);
		// if (handle)
		Tool.showProgressDialog(this, "请稍候...",true);
		// if (meetings.size() != 0) {
		// Tool.removeProgressDialog();
		// setMeetingListAdapter();
		// return;
		// }
		ECMeetingManager meetingManager = ECDevice.getECMeetingManager();
		if (meetingManager == null) {
			return;
		}
		meetingManager.listAllMultiMeetingsByType("",
				ECMeetingManager.ECMeetingType.MEETING_MULTI_VOICE,
				new ECMeetingManager.OnListAllMeetingsListener<ECMeeting>() {
					@Override
					public void onListAllMeetings(ECError reason,
							List<ECMeeting> list) {
						if (reason.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
							onMeetings(list);
							return;
						}
						onError(-1, reason);
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			LogUtil.d(TAG, "onActivityResult: requestCode=" + requestCode
					+ ", resultCode=" + resultCode + ", data=" + data);
			// If there's no data (because the user didn't select a picture
			// and
			// just hit BACK, for example), there's nothing to do.
			if (requestCode == REQUEST_CODE_CREATE) {
				if (data == null) {
					return;
				}
			} else if (resultCode != 1) {
				LogUtil.d("onActivityResult: bail due to resultCode="
						+ resultCode);
				return;
			}
			if (REQUEST_CODE_CREATE == requestCode) {
				if (!data.hasExtra(CreateMeetingActivity.EXTRA_MEETING_PARAMS)) {
					LogUtil.e(TAG, "create meeting error params null");
					return;
				}
				
				mMeetingParams = data
						.getParcelableExtra(CreateMeetingActivity.EXTRA_MEETING_PARAMS);
				if (mMeetingParams != null) {
					showCustomProcessDialog("正在创建...");
					mMeetingAutoJoin = mMeetingParams.isAutoJoin();
					// 处理创建群聊房间
					ECMeetingManager meetingManager = ECDevice
							.getECMeetingManager();
					if (meetingManager == null) {
						return;
					}
					meetingManager
							.createMultiMeetingByType(
									mMeetingParams,
									ECMeetingManager.ECMeetingType.MEETING_MULTI_VOICE,
									new ECMeetingManager.OnCreateOrJoinMeetingListener() {
										@Override
										public void onCreateOrJoinMeeting(
												ECError reason, String meetingNo) {
											dismissPostingDialog();
											if (reason.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
												onMeetingStart(meetingNo);
												return;
											}
											onError(0, reason);
										}
									});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onMeetingStart(String meetingNo) {
		Log.i("info", "list:onMeetingStart");
		dismissPostingDialog();
		MeetingHelper
				.queryMeetings(ECMeetingManager.ECMeetingType.MEETING_MULTI_VOICE);
		if (mMeetingParams != null && mMeetingParams.isAutoJoin()) {
			// 自动加入会议的
			ECMeeting meeting = new ECMeeting();
			meeting.setMeetingNo(meetingNo);
			meeting.setMeetingName(mMeetingParams.getMeetingName());
			meeting.setCreator(MyApplication.getInstance().getName());
			meeting.setJoined(1);
			try {
				MyApplication.dbUtils.createTableIfNotExist(MeetingEntity.class);
				if (MyApplication.dbUtils
						.findFirst(Selector.from(MeetingEntity.class).where(
								"meetingNo", "=", meetingNo)) == null) {
					MeetingEntity entity = new MeetingEntity();
					entity.setMeetingNo(meetingNo);
					entity.setUsers("");
					MyApplication.dbUtils.saveOrUpdate(entity);
				}
			} catch (DbException e) {
				e.printStackTrace();
			}
			doStartMeetingActivity(meeting, null, false);
		}
	}

	public void onMeetingDismissed(String meetingNo) {
		dismissPostingDialog();
		if (meetingAdapter != null) {
			for (int i = 0; i < meetingAdapter.getCount(); i++) {
				ECMeeting meeting = meetingAdapter.getItem(i);
				if (meeting != null && meeting.getMeetingNo() != null
						&& meeting.getMeetingNo().equals(meetingNo)) {
					meetingAdapter.remove(meeting);
					return;
				}
			}
		}
	}

	public void onError(int type, ECError e) {
		dismissPostingDialog();
		String msg = getResources().getString(
				CallFailReason.getCallFailReason(e.errorCode));
		if (msg == null || TextUtils.isEmpty(msg)) {
			Tool.showInfo(this, "请求错误["
					+ (e.errorCode == 111705 ? "非房间创建者无权解散房间 " : e.errorCode)
					+ "]");
			return;
		}
		Tool.showInfo(this, msg);
	}

	public void onMeetings(List<ECMeeting> list) {
		dismissPostingDialog();
		Log.i("info", "onMeetings");
		if (list == null) {
			lv.setAdapter(null);
			return;
		}
		meetings.clear();
		for (ECMeeting ecMeeting : list) {
			if (ecMeeting.getCreator().equals(
					MyApplication.getInstance().getName())) {
				meetings.add(ecMeeting);
			} else {
				try {
					if (MyApplication.dbUtils.findFirst(Selector
							.from(User.class).where("voipAccout", "=",
									ecMeeting.getCreator())) != null) {
						meetings.add(ecMeeting);
					}
				} catch (DbException e) {
					e.printStackTrace();
				}
			}
		}
		setMeetingListAdapter();
	}

	private void setMeetingListAdapter() {
		if (meetingAdapter == null) {
			meetingAdapter = new MeetingAdapter(this, meetings);
			lv.setAdapter(meetingAdapter);
		} else {
			meetingAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_creatMeeting:
			startActivityForResult(
					new Intent(this, CreateMeetingActivity.class),
					REQUEST_CODE_CREATE);
			break;
		case R.id.back:
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (meetingAdapter != null) {
			final ECMeeting meeting = meetingAdapter.getItem(position);
			if (meeting == null) {
				ToastUtil.showMessage(R.string.meeting_voice_room_error);
				return;
			}
			// 如果自己是会议的创建者
			if (MyApplication.getInstance().getName()
					.equals(meeting.getCreator())) {
				ECListDialog dialog = new ECListDialog(this,
						R.array.meeting_control);
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
				ECMeetingManager meetingManager = ECDevice
						.getECMeetingManager();
				if (meetingManager == null) {
					return;
				}
				meetingManager.deleteMultiMeetingByType(
						ECMeetingManager.ECMeetingType.MEETING_MULTI_VOICE,
						meeting.getMeetingNo(),
						new ECMeetingManager.OnDeleteMeetingListener() {
							@Override
							public void onMeetingDismiss(ECError reason,
									String meetingNo) {
								if (reason.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
									onMeetingDismissed(meetingNo);
									return;
								}
								onError(0, reason);
							}
						});
			}
			break;
		case 2:
			// // 管理会议
			// // 管理会议成员操作
			// Intent intent = new Intent(MeetingListActivity.this,
			// VoiceMeetingMemberManager.class);
			// intent.putExtra(VoiceMeetingActivity.EXTRA_MEETING, meeting);
			// startActivity(intent);
			// break;
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
								Tool.showInfo(MeetingListActivity.this,
										"密码不能为空");
							}
						}
					});
			dialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			dialog.show();
			return;
		}
		doStartMeetingActivity(meeting, null);
	}

	public class MeetingAdapter extends ArrayAdapter<ECMeeting> {

		public MeetingAdapter(Context context, List<ECMeeting> objects) {
			super(context, 0, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view;
			MeetingHolder holder;
			if (convertView == null || convertView.getTag() == null) {
				view = View.inflate(MeetingListActivity.this,
						R.layout.voice_meeting_item, null);
				holder = new MeetingHolder();
				view.setTag(holder);

				holder.name = (TextView) view.findViewById(R.id.chatroom_name);
				holder.tips = (TextView) view.findViewById(R.id.chatroom_tips);
				holder.lock = (ImageView) view.findViewById(R.id.lock);
			} else {
				view = convertView;
				holder = (MeetingHolder) convertView.getTag();
			}
			ECMeeting meeting = getItem(position);
			if (meeting != null) {
				holder.name.setText(meeting.getMeetingName());
				boolean meetingFill = (meeting.getJoined() == meeting
						.getSquare());
				// 当前会议是否满人
				int resId = meetingFill ? R.string.str_chatroom_list_join_full
						: R.string.str_chatroom_list_join_unfull;
				if (meeting.getCreator().equals(
						MyApplication.getInstance().getName())) {
					holder.tips.setText(getString(resId, meeting.getJoined(),
							MyApplication.getInstance().getOwnernickname()));
				} else {
					try {
						User u = MyApplication.dbUtils.findFirst(Selector.from(
								User.class).where("voipAccout", "=",
								meeting.getCreator()));
						if (u != null) {
							holder.tips.setText(getString(
									resId,
									meeting.getJoined(),
									u.getRemark().trim().equals("") ? u
											.getNickname() : u.getRemark()));
						} else {
							holder.tips.setText(getString(resId,
									meeting.getJoined(), meeting.getCreator()));
						}
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				// 会议是否需要验证加入
				holder.lock.setVisibility(meeting.isValidate() ? View.VISIBLE
						: View.GONE);
			}
			return view;

		}

		class MeetingHolder {
			TextView name;
			TextView tips;
			ImageView lock;
		}

	}

	protected void showProcessDialog() {
		Tool.showProgressDialog(this, "请稍候...",true);
	}

	protected void showCustomProcessDialog(String content) {
		Tool.showProgressDialog(this, content,true);
	}

	/**
	 * 关闭对话框
	 */
	protected void dismissPostingDialog() {
		Tool.removeProgressDialog();
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
		Intent intent = new Intent(this, VoiceMeetingActivity.class);
		intent.putExtra(VoiceMeetingActivity.EXTRA_MEETING, meeting);
		if (!TextUtils.isEmpty(pass)) {
			intent.putExtra(VoiceMeetingActivity.EXTRA_MEETING_PASS, pass);
		}
		intent.putExtra(VoiceMeetingActivity.EXTRA_CALL_IN, callin);
		startActivity(intent);
	}

}
