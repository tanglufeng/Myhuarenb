package com.abcs.huaqiaobang.ytbt.im;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.ytbt.adapter.ChatsListViewAdapter;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.call.CallActivity;
import com.abcs.huaqiaobang.ytbt.chats.group.GroupchatActivity;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.abcs.huaqiaobang.ytbt.voicemeeting.InviteContactActivity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/26.
 */
public class FriendFragment extends Fragment implements OnClickListener,
		OnItemLongClickListener, OnItemClickListener {
	ListView list;
	public static ArrayList<User> userlist = new ArrayList<>();
	private ChatsListViewAdapter adapter;
	Boolean isagete = true;
	public static final int REQUEST_CONTACTS = 999;
	public static final int REQUEST_CHAT = 100;
	public static DbUtils dbUtils;
	private PopupWindow popupWindow;
	public Handler myhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				try {
					dbUtils.saveOrUpdateAll(userlist);
				} catch (DbException e) {
					e.printStackTrace();
				}
				adapter = new ChatsListViewAdapter(getActivity(),
						(ArrayList<User>) msg.obj);
				list.setAdapter(adapter);
			}
			if (msg.what == 1) {
				Toast.makeText(getActivity(), "好友请求发送成功！",
						Toast.LENGTH_SHORT).show();
				initFriends();
			}
			if (msg.what == 2) {
				Toast.makeText(getActivity(), msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
			}
			if (msg.what == 3) {
				try {
					JSONObject jsonObject = new JSONObject(msg.obj.toString());
					// Log.i("xbb5", msg.obj.toString());
					if (jsonObject.getInt("status") == 1) {
						isagete = false;
						JSONArray array = jsonObject.getJSONArray("msg");
						JSONObject object = array.getJSONObject(0);
						String uid = object.getString("uid");
						String nickname = object.getString("nickname");
						String msgid = object.getString("id");
						showDailog(uid, nickname, msgid);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.i("xbb5", e.toString());
					e.printStackTrace();
				}
			}
			if (msg.what == 4) {
				Toast.makeText(getActivity(), "好友添加成功！",
						Toast.LENGTH_SHORT).show();
				getAllFriends();
				
			}
			if (msg.what == 5) {
				Toast.makeText(getActivity(), "删除好友成功！",
						Toast.LENGTH_SHORT).show();
				User u = userlist.get(msg.arg1);
				userlist.remove(msg.arg1);
				try {
					dbUtils.delete(u);
				} catch (DbException e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
			}
			if (msg.what == 6) {
				adapter.notifyDataSetChanged();
			
			}
		}

	};
	private Button bt_voice, bt_direct, bt_video, bt_addfriends,
			refreshfriends;
	private EditText et_search;
	private ImageView creategroup;
	private TextView ownerid;
	private BroadcastReceiver receiver;
	private View view;

	
	
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_chats_inbox, null);
		dbUtils = MyApplication.getInstance().dbUtils;
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				//adapter.setList(MyApplication.getInstance().getMsgBeans());
			}
		};
		IntentFilter filter = new IntentFilter();// 构造过滤器对象
		filter.addAction("com.robin.mybc.action4");
		getActivity().registerReceiver(receiver, filter);
		new Thread() {
			@Override
			public void run() {
				while (isagete) {
					try {
						addFriendsInfo();
						this.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				super.run();
			}
		}.start();
		initView();
		getAllFriends();
		list = (ListView) view.findViewById(R.id.list);
		list.setOnItemLongClickListener(this);
		list.setOnItemClickListener(this);
		return view;
	}


	protected void showDailog(String msg, String nickname, final String msgid) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // 先得到构造器
		builder.setTitle("好友请求！"); // 设置标题
		builder.setMessage("是否同意" + nickname + "添加你为好友？"); // 设置内容
		// builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
		builder.setPositiveButton("同意", new DialogInterface.OnClickListener() { // 设置确定按钮
					@Override
					public void onClick(DialogInterface dialog, int which) {
						choicefrienduser(msgid, 1);
						dialog.dismiss(); // 关闭dialog
						isagete = true;
					}
				});
		builder.setNegativeButton("不同意", new DialogInterface.OnClickListener() { // 设置取消按钮
					@Override
					public void onClick(DialogInterface dialog, int which) {
						choicefrienduser(msgid, 0);
						dialog.dismiss();
						Toast.makeText(getActivity(), "不同意" + which,
								Toast.LENGTH_SHORT).show();
						isagete = true;
					}
				});
		// 参数都设置完成了，创建并显示出来
		builder.create().show();
	}

	protected void choicefrienduser(String msgid, int i) {
		HttpRequest.sendGet(TLUrl.URL_GET_VOIP + "User/choicefrienduser", "id="
				+ msgid + "&state=" + i, new HttpRevMsg() {
			@Override
			public void revMsg(String msg) {

				if (msg.length() <= 0) {
					return;
				}
				try {
					JSONObject jsonObject = new JSONObject(msg);
					if (jsonObject.getInt("status") == 1) {
						String info = jsonObject.getString("msg");
						Message message = myhandler.obtainMessage(4, info);
						myhandler.sendMessage(message);
						return;
					}
					Message message = myhandler.obtainMessage(4, msg);
					myhandler.sendMessage(message);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void addFriendsInfo() {
		// TODO Auto-generated method stub
		HttpRequest.sendGet(TLUrl.URL_GET_VOIP + "User/pollingfriendUser",
				"frienduid=" + MyApplication.getInstance().getUid(),
				new HttpRevMsg() {
					@Override
					public void revMsg(String msg) {

						Log.i("xbb7", msg);
						if (msg.length() <= 0) {
							return;
						}
						try {
							JSONObject jsonObject = new JSONObject(msg);
							if (jsonObject.getInt("status") == 1) {
								JSONArray jsonArray = jsonObject
										.getJSONArray("msg");
								Message message = myhandler.obtainMessage(3,
										msg);
								myhandler.sendMessage(message);
								isagete = false;
								return;
							}
							Message message = myhandler.obtainMessage(3, msg);
							myhandler.sendMessage(message);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	private void initFriends() {
		Tool.showProgressDialog(getActivity(), "正在加载好友...",true);
		userlist.clear();
		try {
			dbUtils.createTableIfNotExist(User.class);
			userlist.addAll(dbUtils.findAll(User.class));
			if (userlist.size() != 0) {
				Message message = myhandler.obtainMessage(0, userlist);
				Log.i("info", "database");
				myhandler.sendMessage(message);
				Tool.removeProgressDialog();
				return;
			}
		} catch (DbException e1) {
			e1.printStackTrace();
		}
		getAllFriends();
	}

	void getAllFriends() {
		HttpRequest.sendGet(TLUrl.URL_GET_VOIP + "User/findfriendUser", "uid="
				+ MyApplication.getInstance().getUid() + "&page=1"
				+ "&size=100", new HttpRevMsg() {
			@Override
			public void revMsg(String msg) {
				Log.i("xbb2", msg);
				if (msg.length() <= 0) {
					return;
				}
				try {
					JSONObject jsonObject = new JSONObject(msg);

					if (jsonObject.getInt("status") == 1) {
						userlist.clear();
						JSONArray jsonArray = jsonObject.getJSONArray("msg");
						if (jsonArray.length() == 0) {
							Toast.makeText(getActivity(), "您目前还没有好友，请添加!",
									Toast.LENGTH_SHORT).show();
							Message message = myhandler.obtainMessage(6);
							myhandler.sendMessage(message);
							return;

						}
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject object = jsonArray.getJSONObject(i);
							User user = new User();
							user.setVoipAccout(object.getString("voipAccount"));
							user.setNickname(object.getString("nickname"));
							user.setUid(object.getInt("frienduid"));
							user.setAvatar(object.getString("avatar"));
							userlist.add(user);
						}
						Message message = myhandler.obtainMessage(0, userlist);
						myhandler.sendMessage(message);
						return;
					}
					Toast.makeText(getActivity(), msg.toString(),
							Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					Tool.removeProgressDialog();
				}
			}
		});
	}

	private void initView() {
		ownerid = (TextView)view.findViewById(R.id.ownerid);
		ownerid.setText(MyApplication.getInstance().getOwnernickname());
		view.findViewById(R.id.more).setOnClickListener(this);
		view.findViewById(R.id.back).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getActivity(), CallActivity.class);
		intent.putExtra("con.yuntongxun.ecdemo.VoIP_OUTGOING_CALL", true);
		switch (v.getId()) {

		case R.id.more:
			showPopupWindow(v);

			break;
		case R.id.back:
			exit();
			break;
		case R.id.btn_DIRECT:
			startActivityForResult(new Intent(getActivity(),
					InviteContactActivity.class), REQUEST_CONTACTS);
			popupWindow.dismiss();
			break;
		case R.id.addfriends:
			addFriendDialog();
			popupWindow.dismiss();
			break;

		case R.id.btn_Meeting:
			break;
		case R.id.creategroup:
			startActivity(new Intent(getActivity(),
					GroupchatActivity.class));
			popupWindow.dismiss();
			break;
		}
	}

//	private void onButtonClick() {
//		Builder dialog = new AlertDialog.Builder(getActivity());
//		LayoutInflater inflater = (LayoutInflater) getActivity()
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		LinearLayout layout = (LinearLayout) inflater.inflate(
//				R.layout.dialogview, null);
//		dialog.setView(layout);
//		et_search = (EditText) layout.findViewById(R.id.searchC);
//		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				String searchC = et_search.getText().toString();
//				if (!TextUtils.isEmpty(searchC)) {
//					Intent intent = new Intent(getActivity(),
//							CallActivity.class);
//					intent.putExtra("con.yuntongxun.ecdemo.VoIP_OUTGOING_CALL",
//							true);
//					intent.putExtra("callType", GlobalConstant.Call_TYPE_DIRECT);
//					intent.putExtra("num", searchC);
//					startActivity(intent);
//				}
//
//			}
//		});
//		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//
//			}
//
//		});
//		dialog.show();
//	}

	private void showPopupWindow(View view) {
		View contentView = LayoutInflater.from(getActivity()).inflate(
				R.layout.menu_more, null);
		popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		contentView.findViewById(R.id.btn_DIRECT).setOnClickListener(this);
		contentView.findViewById(R.id.addfriends).setOnClickListener(this);
		contentView.findViewById(R.id.btn_Meeting).setOnClickListener(this);
		contentView.findViewById(R.id.creategroup).setOnClickListener(this);
		contentView.findViewById(R.id.refreshfriends).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						popupWindow.dismiss();
						getAllFriends();
					}
				});
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

	protected void addFriends(final String friendId) {
		myhandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpRequest.sendGet(TLUrl.URL_GET_VOIP
						+ "User/addMongoDBfrienduser", "uid="
						+ MyApplication.getInstance().getUid() + "&frienduid="
						+ friendId, new HttpRevMsg() {
					@Override
					public void revMsg(String msg) {
						Log.i("xbb3", msg);
						if (msg.length() <= 0) {
							return;
						}
						try {
							JSONObject jsonObject = new JSONObject(msg);
							if (jsonObject.getInt("status") == 1) {
								Message message = myhandler.obtainMessage(1,
										jsonObject.getString("msg"));
								myhandler.sendMessage(message);
							} else {
								Message message = myhandler.obtainMessage(2,
										jsonObject.getString("msg"));
								myhandler.sendMessage(message);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							Log.i("xbb2", e.toString());
							e.printStackTrace();
						}
					}
				});
			}
		});
		// TODO Auto-generated method stub

	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//			exit();
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	private void exit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // 先得到构造器
		builder.setTitle("提示"); // 设置标题
		builder.setMessage("退出程序"); // 设置内容
		// builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
		builder.setPositiveButton("退出", new DialogInterface.OnClickListener() { // 设置确定按钮
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss(); // 关闭dialog
						getActivity().finish();
					}
				});
		builder.setNegativeButton("取消", null);
		// 参数都设置完成了，创建并显示出来
		builder.create().show();
	}

	private void addFriendDialog() {
		final AlertDialog dialog = new Builder(getActivity()).create();
		View view = View.inflate(getActivity(), R.layout.add_friend_dialog, null);
		final EditText et = (EditText) view.findViewById(R.id.et_friend_id);
		dialog.setView(view);
		dialog.show();
		view.findViewById(R.id.button1).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						String id = et.getText().toString().trim();
						if (TextUtils.isEmpty(id)) {
							Tool.showInfo(getActivity(), "好友ID输入错误");
						} else {
							addFriends(id);
							dialog.dismiss();
						}
					}
				});
		view.findViewById(R.id.button2).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		getActivity().unregisterReceiver(receiver);
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			int position, long arg3) {
		showDeleDailog(position);
		return true;
	}

	private void showDeleDailog(final int position) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // 先得到构造器
		builder.setTitle("删除好友"); // 设置标题
		builder.setMessage("真的要删除" + userlist.get(position).getNickname() + "?"); // 设置内容
		// builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // 设置确定按钮
					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteFriend(position);
						dialog.dismiss(); // 关闭dialog

					}
				});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // 设置取消按钮
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});
		// 参数都设置完成了，创建并显示出来
		builder.create().show();
	}

	private void deleteFriend(final int position) {
		// TODO Auto-generated method stub
		HttpRequest.sendGet(TLUrl.URL_GET_VOIP + "User/deletefrienduid", "uid="
				+ MyApplication.getInstance().getUid() + "&frienduid="
				+ userlist.get(position).getUid(), new HttpRevMsg() {
			@Override
			public void revMsg(String msg) {
				Log.i("xbb9", msg);
				if (msg.length() <= 0) {
					return;
				}
				try {
					JSONObject jsonObject = new JSONObject(msg);
					if (jsonObject.getInt("status") == 1) {
						Message message = new Message();
						message.arg1 = position;
						message.what = 5;
						myhandler.sendMessage(message);
					} else {
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.i("xbb2", e.toString());
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		Log.i("xbb9", userlist.get(position).getUid() + "");
     	Intent intent = new Intent(getActivity(), FriendDetailsActivity.class);
    	intent.putExtra("friend", userlist.get(position).getVoipAccout());
		startActivity(intent);

	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == REQUEST_CONTACTS && resultCode == 1) {
//			String num = data.getStringExtra("num");
//			if (num != null) {
//				Intent intent = new Intent(getActivity(),
//						CallActivity.class);
//				intent.putExtra("con.yuntongxun.ecdemo.VoIP_OUTGOING_CALL",
//						true);
//				intent.putExtra("callType", GlobalConstant.Call_TYPE_DIRECT);
//				intent.putExtra("num", num);
//				startActivity(intent);
//			}
//		}
//		if (requestCode == REQUEST_CHAT && resultCode == RESULT_OK) {
//			myhandler.sendEmptyMessage(6);
//		}
//	}

}


//public class FriendFragment extends Fragment{
//	@Override
//	public View onCreateView(LayoutInflater inflater,
//			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//		
//		View view=LayoutInflater.from(getActivity()).inflate(R.layout.activity_chats_inbox, null);
//		
//		return view;
//	}
//
//}
