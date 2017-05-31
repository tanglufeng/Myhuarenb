package com.abcs.huaqiaobang.ytbt.im;

import android.content.BroadcastReceiver;
import android.os.Bundle;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.ytbt.bean.AddFriendRequestBean;
import com.abcs.huaqiaobang.ytbt.bean.GroupMemberBean;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;

public class MainActivity extends BaseFragmentActivity {

    public static final int REQUEST_CONTACTS = 999;
    public static final int REQUEST_CODE_CREATE = 0x003;
    public static final int CREATE_GROUP = 200;
    private final int GROUP_QUIT = 1;
    private final int GROUP_JOIN = 2;
    /**
     * 创建的会议是否自动加入
     */
    ConversationFragment conversationFragment;
    private BroadcastReceiver receiver;
    String groupid, members;
    GroupMemberBean member;
    ArrayList<User> userlist = new ArrayList<>();

//	private static class MyHandler extends Handler {
//		private final WeakReference<MainActivity> mActivity;

//		public MyHandler(MainActivity activity) {
//			mActivity = new WeakReference<MainActivity>(activity);
//		}
//
//		@Override
//		public void handleMessage(Message msg) {
//			MainActivity activity = mActivity.get();
//			if (activity != null) {
//				switch (msg.what) {
//				case 0:
//					Toast.makeText(activity, "加入群组", Toast.LENGTH_SHORT).show();
//					break;
//				case 1:
//					activity.userlist.clear();
//					User user = (User) msg.obj;
//					activity.saveNotice(user.getNickname()+ "加入群组");
//					try {
//						MyApplication.dbUtils.createTableIfNotExist(GroupMemberBean.class);
//						activity.member = MyApplication.dbUtils.findById(GroupMemberBean.class, activity.groupid);
//						if (activity.member != null) {
//							try {
//								activity.userlist = JsonUtil.parseString(activity.member.getMembers());
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//						}
//						activity.userlist.add(user);
//						GroupMemberBean member1 = new GroupMemberBean();
//						member1.setGroupid(activity.groupid);
//						member1.setMembers(JsonUtil.toString(activity.userlist));
//						MyApplication.dbUtils.saveOrUpdate(member1);
//					} catch (DbException e) {
//						e.printStackTrace();
//					}
//					break;
//				case 2:
//					User u = (User) msg.obj;
//					if (!u.getVoipAccout().equals(MyApplication.getInstance().getUserBean().getVoipAccount())) {
//						activity.saveNotice(((User) msg.obj).getNickname()+ "退出群组");
//						try {
//							activity.member = MyApplication.dbUtils.findById(GroupMemberBean.class, activity.groupid);
//							if (activity.member != null) {
//								try {
//									activity.userlist = JsonUtil.parseString(activity.member.getMembers());
//								} catch (JSONException e) {
//									e.printStackTrace();
//								}
//							}
//							activity.userlist.remove(u);
//							GroupMemberBean member1 = new GroupMemberBean();
//							member1.setGroupid(activity.groupid);
//							member1.setMembers(JsonUtil.toString(activity.userlist));
//							MyApplication.dbUtils.saveOrUpdate(member1);
//						} catch (DbException e) {
//							e.printStackTrace();
//						}
//					}
//					break;
//				}
//			}
//		}
//		}

//		private final MyHandler myhandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Tool.showProgressDialog(this, "正在加载...", false);
        // PushManager.startWork(this,PushConstants.LOGIN_TYPE_API_KEY,
        // "wTDSAlFqE8Q9xIWFKNpZyTGt");
//		initView();
        // setPushHandler();
        // try {
        // MyApplication.requests.addAll(MyApplication.dbUtils.findAll(AddFriendRequestBean.class));
        // } catch (DbException e) {
        // e.printStackTrace();
        // }
        try {
            MyApplication.dbUtils.createTableIfNotExist(AddFriendRequestBean.class);
            MyApplication.dbUtils.createTableIfNotExist(User.class);
            MyApplication.dbUtils.createTableIfNotExist(GroupMemberBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
//		getAllFriends();
//		addFriendsInfo();

    }
}

//	private void initView() {
//		// RelativeLayout container=(RelativeLayout)
//		// findViewById(R.id.container);
//		FragmentManager manager = getSupportFragmentManager();
//		FragmentTransaction transaction = manager.beginTransaction();
//		conversationFragment = new ConversationFragment();
//		transaction.add(R.id.container, conversationFragment);
//		transaction.commit();
//		groupNotice();
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//			exit();
//		}
//		return super.onKeyDown(keyCode, event);
//	}

//	private void exit() {
//		AlertDialog.Builder builder = new AlertDialog.Builder(this); // 先得到构造器
//		builder.setTitle("提示"); // 设置标题
//		builder.setMessage("是否退出程序"); // 设置内容
//		// builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
//		builder.setPositiveButton("退出", new DialogInterface.OnClickListener() { // 设置确定按钮
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss(); // 关闭dialog
//						MyApplication.exit();
//					}
//				});
//		builder.setNegativeButton("取消", null);
//		// 参数都设置完成了，创建并显示出来
//		builder.create().show();
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		unregisterReceiver(receiver);
//	}
//
//	private String name;
//	private void groupNotice() {
//		receiver = new BroadcastReceiver() {
//
//			@Override
//			public void onReceive(Context context, Intent intent) {
//				switch (intent.getAction()) {
//				case "com.im.group.notice":
//					ECGroupNoticeMessage notice = intent
//							.getParcelableExtra("groupnotice");
//					groupid = notice.getGroupId();
//					name = notice.getGroupName();
//					ECGroupNoticeMessage.ECGroupMessageType type = notice
//							.getType();
//					if (type == ECGroupNoticeMessage.ECGroupMessageType.PROPOSE) {
//						Log.i("info", "ECGroupMessageType.PROPOSE");
//						// 群组收到有人申请加入群组
//						ECProposerMsg proposerMsg = (ECProposerMsg) notice;
//						showDailog(proposerMsg);
//					} else if (type == ECGroupNoticeMessage.ECGroupMessageType.INVITE) {
//						Log.i("info", "ECGroupMessageType.INVITE");
//						// 群组管理员邀请用户加入群组 -
//						ECInviterMsg inviterMsg = (ECInviterMsg) notice;
//						// 处理群组管理员邀请加入群组通知
//						switch (inviterMsg.getConfirm()) {
//						case 0:
//							break;
//						case 1:
//							GroupBean groupBean = new GroupBean();
//							groupBean.setGroupName(inviterMsg.getGroupName());
//							groupBean.setGroupId(inviterMsg.getGroupId());
//							groupBean.setGroupOwner(inviterMsg.getNickName());
//							groupBean.setGroupType(inviterMsg.isDiscuss() ? "0"
//									: "1");
//							groupBean.setGroupPermission("0");
//							groupBean.setGroupAvatar("");
//							groupBean.setGroupDeclared("declared");
//							groupBean.setMemberInGroup("");
//							groupBean.setDateCreate(inviterMsg.getDateCreated()
//									+ "");
//							Intent intent2 = new Intent("action.group.update");
//							intent2.putExtra("group", groupBean);
//							sendBroadcast(intent2);
//							saveNotice("您被邀请进入" + inviterMsg.getGroupName());
//							GroupMemberBean bean = new GroupMemberBean();
//							bean.setGroupid(inviterMsg.getGroupId());
//							bean.setMembers("");
//							try {
//								MyApplication.dbUtils.saveOrUpdate(groupBean);
//								MyApplication.dbUtils.saveOrUpdate(bean);
//							} catch (DbException e) {
//								e.printStackTrace();
//							}
//							break;
//						}
//					} else if (type == ECGroupNoticeMessage.ECGroupMessageType.REMOVE_MEMBER) {
//						Log.i("info", "ECGroupMessageType.REMOVE_MEMBER");
//						// 群组管理员删除成员
//						ECRemoveMemberMsg removeMemberMsg = (ECRemoveMemberMsg) notice;
//						// 处理群组移除成员通知
//						if ("$Smith账号".equals(removeMemberMsg.getMember())) {
//							// 如果是自己则将从本地群组关联关系中移除
//							// 通知UI处理刷新
//						}
//
//					} else if (type == ECGroupNoticeMessage.ECGroupMessageType.QUIT) {
//						Log.i("info", "ECGroupMessageType.QUIT");
//						// 群组成员主动退出群组
//						ECQuitGroupMsg quitGroupMsg = (ECQuitGroupMsg) notice;
//						// 处理某人退出群组通知
//						handlerGroupMsg(GROUP_QUIT, quitGroupMsg.getGroupId(),
//								quitGroupMsg.getMember());
//					} else if (type == ECGroupNoticeMessage.ECGroupMessageType.DISMISS) {
//						Log.i("info", "ECGroupMessageType.DISMISS");
//						ECDismissGroupMsg dismissGroupMsg = (ECDismissGroupMsg) notice;
//						// 处理群组被解散通知
//						// 将群组从本地缓存中删除并通知UI刷新
//						// 删除群组（解散群组）
//						onGroupDismiss(dismissGroupMsg.getMsgId());
//					} else if (type == ECGroupNoticeMessage.ECGroupMessageType.JOIN) {
//						ECJoinGroupMsg joinGroupMsg = (ECJoinGroupMsg) notice;
//						Log.i("info", joinGroupMsg.getMember() + "加入群组");
//						handlerGroupMsg(GROUP_JOIN, joinGroupMsg.getGroupId(),
//								joinGroupMsg.getMember());
//					} else if (type == ECGroupNoticeMessage.ECGroupMessageType.REPLY_INVITE) {
//						// 用户通过或拒绝群组管理员邀请加入群组的邀请
//						Log.i("info", "ECGroupMessageType.REPLY_INVITE");
//						ECReplyInviteGroupMsg replyInviteGroupMsg = (ECReplyInviteGroupMsg) notice;
//						switch (replyInviteGroupMsg.getConfirm()) {
//						case 2:
//							handlerGroupMsg(GROUP_JOIN, groupid,
//									replyInviteGroupMsg.getMember());
//							break;
//						case 1:
//							break;
//						}
//					} else if (type == ECGroupNoticeMessage.ECGroupMessageType.REPLY_JOIN) {
//						Log.i("info", "ECGroupMessageType.REPLY_JOIN");
//					}
//					break;
//				case GlobalConstant.ACTION_ADDFRIEND_REQUEST:
//					conversationFragment.unread.setVisibility(View.VISIBLE);
//					conversationFragment.addFriendRequsrt = true;
//					break;
//				}
//			}
//		};
//		IntentFilter filter = new IntentFilter();// 构造过滤器对象
//		filter.addAction("com.im.group.notice");
//		filter.addAction(GlobalConstant.ACTION_ADDFRIEND_REQUEST);
//		registerReceiver(receiver, filter);
//	}
//	private void onGroupDismiss(String groupId){
//		saveNotice("群组已被解散");
//		GroupBean bean =null;
//		try {
//			bean = MyApplication.dbUtils.findById(GroupBean.class, groupId);
//            if(bean!=null) {
//                MyApplication.dbUtils.delete(bean);
//            }
//            MyApplication.dbUtils.deleteById(GroupMemberBean.class, groupId);
//        } catch (DbException e) {
//			e.printStackTrace();
//		} finally{
//			Intent intent = new Intent();
//			intent.setAction("com.abcs.mybc.action.group");
//			intent.putExtra("bc", groupId);
//			intent.putExtra("group", bean);
//			sendBroadcast(intent);
//		}
//
//	}
//	private void handlerGroupMsg(final int type, String groupId,
//			final String member) {
//		HttpRequest.sendGet(TLUrl.getInstance().URL_GET_VOIP + "User/findvoipuser",
//                "voipAccount=" + member, new HttpRevMsg() {
//                    private Message message;
//
//                    @Override
//                    public void revMsg(String msg) {
//                        Log.i("xbb群组成员保存", msg);
//                        if (msg.length() <= 0) {
//                            return;
//                        }
//                        try {
//                            JSONObject jsonObject = new JSONObject(msg);
//                            if (jsonObject.getInt("status") == 1) {
//                                JSONObject object = jsonObject
//                                        .getJSONObject("msg");
//                                User user = new User();
//                                user.setAvatar(object.getString("avatar"));
//                                user.setNickname(object.getString("nickname"));
//                                user.setUid(object.getInt("uid"));
//                                user.setVoipAccout(member);
//                                switch (type) {
//                                    case GROUP_JOIN:
//                                        message = myhandler.obtainMessage(1, user);
//                                        break;
//                                    case GROUP_QUIT:
//                                        message = myhandler.obtainMessage(2, user);
//                                        break;
//                                }
//                                myhandler.sendMessage(message);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//	}
//
//	protected void showDailog(final ECProposerMsg proposerMsg) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); // 先得到构造器
//		builder.setTitle("群组消息！"); // 设置标题
//		builder.setMessage("是否同意" + proposerMsg.getSender() + "加入"
//				+ proposerMsg.getGroupName() + "申请理由："
//				+ proposerMsg.getDeclared()); // 设置内容
//
//		builder.setPositiveButton("同意", new DialogInterface.OnClickListener() { // 设置确定按钮
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dealWith(proposerMsg, 0);
//						dialog.dismiss(); // 关闭dialog
//					}
//				});
//		builder.setNegativeButton("不同意", new DialogInterface.OnClickListener() { // 设置取消按钮
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dealWith(proposerMsg, 1);
//						dialog.dismiss();
//
//					}
//				});
//		// 参数都设置完成了，创建并显示出来
//		builder.create().show();
//	}
//
//	protected void dealWith(ECProposerMsg proposerMsg, int i) {
//		HttpRequest.sendGet(
//				TLUrl.getInstance().URL_GET_VOIP + "member/AskJoin",
//				"uid=" + MyApplication.getInstance().getUid() + "&groupId="
//						+ proposerMsg.getGroupId() + "&asker="
//						+ proposerMsg.getSender() + "&confirm=" + i,
//				new HttpRevMsg() {
//
//					@Override
//					public void revMsg(String msg) {
//						Log.i("xbb管理员处理申请消息", msg);
//						try {
//							JSONObject jsonObject = new JSONObject(msg);
//							if (jsonObject.getInt("status") == 1) {
//								myhandler.sendEmptyMessage(0);
//							}
//						} catch (JSONException e) {
//							Log.i("xbb管理员处理申请消息", e.toString());
//							e.printStackTrace();
//						} finally {
//
//						}
//					}
//				});
//
//	}
//
//	private void saveNotice(String notice) {
//		ConversationBean conversationBean = new ConversationBean();
//		conversationBean.setIsgroup(true);
//		// conversationBean.setMsgfrom(MyApplication.getInstance()
//		// .getUserBean().getVoipAccount());
//		conversationBean.setMsgto(groupid);
//		conversationBean.setMsgfrom(name);
//		conversationBean.setMsglasttime(System.currentTimeMillis());
//		conversationBean.setConversationpeople(MyApplication.getInstance().getUserBean().getVoipAccount()+ groupid);
//		conversationBean.setMsg(notice);
//		MsgBean msgBean = new MsgBean();
//		msgBean.setMsgfrom(groupid);
//		msgBean.setType("notice");
//		msgBean.setMsgtime(System.currentTimeMillis());
//		msgBean.setMsg(notice);
//		try {
//			MyApplication.dbUtils.save(msgBean);
//		} catch (DbException e) {
//			Log.i("xbbbb", e.toString());
//			e.printStackTrace();
//		}
//		Intent intent = new Intent();
//		intent.setAction("com.robin.mybc.action4");
//		intent.putExtra("conversation", conversationBean);
//		sendBroadcast(intent);
//	}
//
//	private void getAllFriends() {
//		HttpUtils httpUtils = new HttpUtils(60000);
//		httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, TLUrl.getInstance().URL_GET_VOIP
//				+ "User/findfriendUser?uid="
//				+ MyApplication.getInstance().getUid() + "&page=1"
//				+ "&size=1000", new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				Tool.removeProgressDialog();
//				Tool.showInfo(MainActivity.this, "网络异常,加载好友失败");
//				MyApplication.users = new ArrayList<>();
//				try {
//					MyApplication.users.addAll(MyApplication.dbUtils
//							.findAll(User.class));
//				} catch (DbException e) {
//					e.printStackTrace();
//				}
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> arg0) {
//				Tool.removeProgressDialog();
//				MyApplication.users = new ArrayList<User>();
//				MyApplication.friends = new ConcurrentHashMap<>();
//				try {
//					JSONObject jsonObject = new JSONObject(arg0.result);
//					if (jsonObject.getInt("status") == 1) {
//						JSONArray jsonArray = jsonObject.getJSONArray("msg");
//						if (jsonArray.length() == 0) {
//							return;
//						}
//						// List<String> nums = new ArrayList<String>();
//						for (int i = 0; i < jsonArray.length(); i++) {
//							JSONObject object = jsonArray.getJSONObject(i);
//							User user = new User();
//							user.setVoipAccout(object.getString("voipAccount"));
//							user.setNickname(object.getString("nickname"));
//							user.setUid(object.getInt("frienduid"));
//							user.setAvatar(object.getString("avatar"));
//							user.setRemark(object.getString("remarks"));
//							MyApplication.users.add(user);
//							if (user.getRemark().trim().equals("")) {
//								MyApplication.friends.put(user.getNickname(),
//										user);
//							} else {
//								MyApplication.friends.put(user.getRemark(),
//										user);
//							}
//						}
//						MyApplication.dbUtils
//								.saveOrUpdateAll(MyApplication.users);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					MyApplication.users = null;
//					MyApplication.friends = null;
//				}
//			}
//		});
//	}
//
//	private void addFriendsInfo() {
//		HttpUtils httpUtils = new HttpUtils(60000);
//		httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, TLUrl.getInstance().URL_GET_VOIP
//				+ "User/pollingfriendUserzz?frienduid="
//				+ MyApplication.getInstance().getUid(),
//				new RequestCallBack<String>() {
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> arg0) {
//						if (arg0.result.length() <= 0)
//							return;
//						try {
//							JSONObject jsonObject = new JSONObject(arg0.result);
//							Log.i("info", arg0.result);
//							if (jsonObject.getInt("status") == 1) {
//								JSONArray jsonArray = jsonObject
//										.getJSONArray("msg");
//								for (int i = 0; i < jsonArray.length(); i++) {
//									JSONObject obj = jsonArray.getJSONObject(i);
//									AddFriendRequestBean bean = new AddFriendRequestBean();
//									bean.setId(obj.optString("id"));
//									bean.setNickname(obj.optString("nickname"));
//									bean.setUid(obj.optString("uid"));
//									bean.setAvadar(obj.optString("avatar"));
//									bean.setVoip(obj.optString("voipAccount"));
//									bean.setTime(System.currentTimeMillis());
//									bean.setState(0);
//									Log.i("info", bean.getId());
//									if (MyApplication.requests.contains(bean)) {
//										AddFriendRequestBean a = MyApplication.requests
//												.get(MyApplication.requests
//														.indexOf(bean));
//										if (a.getState() != 0
//												|| !a.getId().equals(
//														bean.getId())) {
//											MyApplication.requests.remove(a);
//										} else {
//											continue;
//										}
//									}
//									MyApplication.requests.add(bean);
//									SDKCoreHelper.getInstance().alertMag(false);
//									conversationFragment.unread
//											.setVisibility(View.VISIBLE);
//									conversationFragment.addFriendRequsrt = true;
//								}
//								MyApplication.dbUtils
//										.saveOrUpdateAll(MyApplication.requests);
//								// Message message = myhandler.obtainMessage(3,
//								// msg);
//								// myhandler.sendMessage(message);
//								// isagete = false;
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				});
//	}


// private void setPushHandler() {
// UmengMessageHandler handler = new UmengMessageHandler() {
// private String result;
//
// @Override
// public void dealWithCustomMessage(Context context, UMessage msg) {
// super.dealWithCustomMessage(context, msg);
// Log.i("info", msg.custom);
// String responce = msg.custom;
// switch (responce.charAt(responce.length() - 1)) {
// case '0':
// result = responce.substring(0,
// responce.lastIndexOf("#division"));
// try {
// JSONArray array = new JSONArray(result);
// JSONObject obj = array.getJSONObject(0);
// AddFriendRequestBean bean = new AddFriendRequestBean();
// bean.setId(obj.optString("id"));
// bean.setNickname(obj.optString("nickname"));
// bean.setUid(obj.optString("uid"));
// bean.setAvadar(obj.optString("avatar"));
// bean.setVoip(obj.optString("voipAccount"));
// bean.setTime(System.currentTimeMillis());
// bean.setState(0);
// if (MyApplication.requests.contains(bean)) {
// int position = MyApplication.requests.indexOf(bean);
// if (MyApplication.requests.get(
// MyApplication.requests.indexOf(bean))
// .getState() != 0
// || !MyApplication.requests.get(position)
// .equals(bean.getId())) {
// MyApplication.requests.remove(position);
// } else {
// return;
// }
// }
// MyApplication.requests.add(bean);
// SDKCoreHelper.getInstance().alertMag();
// conversationFragment.unread.setVisibility(View.VISIBLE);
// conversationFragment.addFriendRequsrt = true;
// MyApplication.dbUtils
// .saveOrUpdateAll(MyApplication.requests);
// sendBroadcast(new Intent(
// GlobalConstant.ACTION_ADDFRIEND_REQUEST));
// } catch (Exception e) {
// e.printStackTrace();
// }
// break;
// case '1':
// result = responce.substring(0,
// responce.lastIndexOf("#division"));
// try {
// User user = MyApplication.dbUtils.findFirst(Selector
// .from(User.class).where("uid", "=",
// Integer.parseInt(result)));
// MyApplication.users.remove(user);
// MyApplication.dbUtils.delete(user);
// MyApplication.friends.remove(user.getRemark().trim()
// .equals("") ? user.getNickname() : user
// .getRemark());
// sendBroadcast(new Intent(
// GlobalConstant.ACTION_UPDATE_FRIENDS));
// } catch (DbException e) {
// e.printStackTrace();
// }
// break;
// case '2':
// result = responce.substring(0,
// responce.lastIndexOf("#division"));
// getFriendInfo(result);
// break;
// }
//
// }
//
// };
// MyApplication.mPushAgent.setMessageHandler(handler);
// }
//

