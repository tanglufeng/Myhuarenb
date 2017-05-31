package com.abcs.huaqiaobang.ytbt.im.sdkhelper;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.main.MainActivity;
import com.abcs.huaqiaobang.presenter.MyPrsenter;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.ytbt.bean.ConversationBean;
import com.abcs.huaqiaobang.ytbt.bean.GroupBean;
import com.abcs.huaqiaobang.ytbt.bean.MsgBean;
import com.abcs.huaqiaobang.ytbt.bean.TopConversationBean;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.common.utils.FileAccessor;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.abcs.huaqiaobang.ytbt.voicemeeting.MeetingMsgReceiver;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECDevice.ECConnectState;
import com.yuntongxun.ecsdk.ECDevice.InitListener;
import com.yuntongxun.ecsdk.ECDevice.OnECDeviceConnectListener;
import com.yuntongxun.ecsdk.ECDevice.OnLogoutListener;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;
import com.yuntongxun.ecsdk.OnChatReceiveListener;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.im.ECImageMessageBody;
import com.yuntongxun.ecsdk.im.ECMessageNotify;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;
import com.yuntongxun.ecsdk.im.ECVoiceMessageBody;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SDKCoreHelper implements InitListener, OnECDeviceConnectListener,
        OnLogoutListener {
    private static SDKCoreHelper coreHelper;
    public static final String action = "com.robin.mybc.action4";
    public static final String action2 = "com.abcs.huaqiaobang.shoppingxiaoxi";
    private ECInitParams.LoginMode mMode = ECInitParams.LoginMode.FORCE_LOGIN;
    private Context mContext;
    private ECDevice.ECConnectState mConnect = ECDevice.ECConnectState.CONNECT_FAILED;
    private Handler handler;
    private static String userName;
    private static String pwd;
    private ECTextMessageBody textMessageBody;
    private MsgBean msgBean;
    private ConversationBean conversationBean;
    private TopConversationBean topConversationBean;
    private NotificationManager notificationManager;
    private String msgtype;
    private Boolean isfrien = false;
    private MediaPlayer mPlayer;
    public static ArrayList<User> userlist = new ArrayList<>();
    public static ArrayList<GroupBean> grouplist = new ArrayList<>();
    public static int count;
    public static boolean rlLogin = false;
    static ProgressDialog dialog;

    private SDKCoreHelper() {
    }

    public static SDKCoreHelper getInstance() {
        if (coreHelper == null) {
            coreHelper = new SDKCoreHelper();
        }
        return coreHelper;
    }

    public synchronized void setHandler(final Handler handler) {
        this.handler = handler;
    }

    public static void init(Context ctx, ECInitParams.LoginMode mode,
                            String userName, String pwd, ProgressDialog dialog) {

        getInstance().mMode = mode;
        getInstance().mContext = ctx;
        SDKCoreHelper.userName = userName;
        SDKCoreHelper.pwd = pwd;
        SDKCoreHelper.dialog = dialog;
        // 判断SDK是否已经初始化，没有初始化则先初始化SDK
        if (!ECDevice.isInitialized()) {
            getInstance().mConnect = ECDevice.ECConnectState.CONNECTING;
            // ECSDK.setNotifyOptions(getInstance().mOptions);
            ECDevice.initial(ctx, getInstance());
            return;
        }

        getInstance().onInitialized();
    }

    @Override
    public void onError(Exception arg0) {
        // TODO Auto-generated method stub
        Log.e("SDK_error", arg0.getMessage());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onInitialized() {

        Log.e("SDK_init", "sdk_init");
        // TODO Auto-generated method stub
        final ECInitParams params = ECInitParams.createParams();

        // params.setUserid(MyApplication.getInstance().getUserBean().getName());
        params.setAppKey("aaf98f8951aa81e70151aa8eebca0025");
        params.setToken("8b0fa7fd6363919ba31ff72b45d16433");
        // 设置登陆验证模式（是否验证密码）NORMAL_AUTH-自定义方式
        // params.setAuthType(ECInitParams.LoginAuthType.NORMAL_AUTH);
        // 1代表用户名+密码登陆（可以强制上线，踢掉已经在线的设备）
        // 2代表自动重连注册（如果账号已经在其他设备登录则会提示异地登陆）
        // 3 LoginMode（强制上线：FORCE_LOGIN 默认登录：AUTO）
        params.setMode(ECInitParams.LoginMode.FORCE_LOGIN);
        // voip账号+voip密码方式：
        params.setUserid(MyApplication.getInstance().getUserBean()
                .getVoipAccount());
        // params.setPwd("k1ZFf5u3");
        params.setPwd(MyApplication.getInstance().getUserBean().getVoipPwd());
        // params.setAppKey("应用ID");
        // params.setToken("应用Token");
        // 设置登陆验证模式（是否验证密码）PASSWORD_AUTH-密码登录方式
        // params.setAuthType(ECInitParams.LoginAuthType.NORMAL_AUTH);
        params.setAuthType(ECInitParams.LoginAuthType.PASSWORD_AUTH);
        // 1代表用户名+密码登陆（可以强制上线，踢掉已经在线的设备）
        // 2代表自动重连注册（如果账号已经在其他设备登录则会提示异地登陆）
        // 3 LoginMode（强制上线：FORCE_LOGIN 默认登录：AUTO）
        // params.setMode(ECInitParams.LoginMode.FORCE_LOGIN);

        // 设置登陆状态回调
        params.setOnDeviceConnectListener(new ECDevice.OnECDeviceConnectListener() {
            public void onConnect() {
                // 兼容4.0，5.0可不必处理
            }

            @Override
            public void onDisconnect(ECError error) {
                // 兼容4.0，5.0可不必处理
                Log.i("info", "onDisconnect");
            }

            @Override
            public void onConnectState(ECDevice.ECConnectState state,
                                       ECError error) {

                if (state == ECDevice.ECConnectState.CONNECT_FAILED) {
                    if (error.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
                        // 账号异地登陆
//                        Tool.removeProgressDialog();
                        Toast.makeText(mContext, "账号异地登陆！请修改密码！",
                                Toast.LENGTH_SHORT).show();
                        mContext.getSharedPreferences("user", Context.MODE_PRIVATE).edit().putBoolean("isReload", true).commit();
                        if (isBackground(mContext)) {
//                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                            intent.setAction(Intent.ACTION_MAIN);
                            Util.preference.edit().putString("lizai_pwd", "").commit();
                            Util.preference.edit().putBoolean("ydlogin", true).commit();
                            Util.preference.edit().putBoolean("islogin", false).commit();
                            final Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mContext.startActivity(intent);

                            android.os.Process.killProcess(android.os.Process.myPid());
                        } else {
                  //          MyApplication.getInstance().getMainActivity().logout();
                        }
                    } else {
                        // 连接状态失败
                        Tool.removeProgressDialog();
                        Toast.makeText(mContext, "网络异常！", Toast.LENGTH_SHORT)
                                .show();
                        rlLogin = false;
//                        getInstance().onInitialized();
                        ProgressDlgUtil.stopProgressDlg();
                    }
                    return;
                } else if (state == ECDevice.ECConnectState.CONNECT_SUCCESS) {
                    // 登陆成功
//                    Log.i("info", "SDK 登陆成功");

                    ProgressDlgUtil.stopProgressDlg();
                    rlLogin = true;

                    if(((MainActivity)(mContext)).my!=null)
                    new MyPrsenter(((MainActivity)(mContext)).my).loginSuccess();
//                    ((MainActivity) mContext).main.initUser();
                    ((MainActivity) mContext).sendBroadcast(new Intent("com.abcs.huaqiaobang.changeuser"));
                    MyApplication.getInstance().setName(params.getUserId());
                    Util.preference.edit().putBoolean("islogin", true).commit();
//                    if (mContext.getSharedPreferences("user", Context.MODE_PRIVATE).getBoolean("isReload", true) || !Util.preference.getBoolean("islogin", true))
//                        ;

                    PushManager.startWork(mContext, PushConstants.LOGIN_TYPE_API_KEY, "cBxRk7dz68YyKQEflhSM6P8K");//appkey

                    //zjz
                    MyUpdateUI.sendUpdateCarNum(mContext);
                    // NoticeDialog.stopNoticeDlg();
//                    Toast.makeText(mContext, "登陆成功", Toast.LENGTH_SHORT).show();
                    // Intent intent = new Intent(mContext,
                    // ActivityChats.class);
//                    if (mContext.getSharedPreferences("user", Context.MODE_PRIVATE).getString("acount", "")
//                            .equals(userName)) {
//                        //new QueryFriendsTask(mContext).execute();
//                    } else {
//                        try {
//                            MyApplication.dbUtils
//                                    .dropTable(GroupMemberBean.class);
//                            MyApplication.dbUtils
//                                    .dropTable(ConversationBean.class);
//                            MyApplication.dbUtils.dropTable(User.class);
//                            MyApplication.dbUtils.dropTable(UserBean.class);
//                            MyApplication.dbUtils.dropTable(GroupBean.class);
//                            MyApplication.dbUtils.dropTable(AddFriendRequestBean.class);
//                            MyApplication.dbUtils.dropTable(LabelBean.class);
//                            MyApplication.dbUtils.dropTable(TopConversationBean.class);
//                            MyApplication.dbUtils.dropTable(MsgBean.class);
//                            MyApplication.dbUtils
//                                    .dropTable(MeetingEntity.class);
//                            MyApplication.dbUtils
//                                    .dropTable(ContactEntity.class);
////							MyApplication.dbUtils.dropDb();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }

                    Editor editor = mContext.getSharedPreferences("user", Context.MODE_PRIVATE).edit();
                    editor.putString("acount", userName);
                    editor.putString("pwd", pwd);
                    editor.commit();
//					Intent intent = new Intent(mContext, MainActivity.class);
//					mContext.startActivity(intent);
                    Intent intent2 = new Intent();
                    intent2.setAction("com.abcs.huaqiaobang.shequ.refresh");
                    mContext.sendBroadcast(intent2);
                }
            }
        });
        // ECDevice.setOnChatReceiveListener(IMChattingHelper.getInstance());
        params.setOnChatReceiveListener(new OnChatReceiveListener() {


            @Override
            public void onSoftVersion(String arg0, int arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onServicePersonVersion(int arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onReceiveOfflineMessageCompletion() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onReceiveOfflineMessage(List<ECMessage> msgs) {
                Log.i("info", "onReceiveOfflineMessage");
                for (ECMessage msg : msgs) {
//					handlerMsg(msg);
                    toDealWith(msg, true);
                }

            }

            @Override
            public void onReceiveMessageNotify(ECMessageNotify arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onReceiveDeskMessage(ECMessage arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onOfflineMessageCount(int conut) {
                count = conut;

                // TODO Auto-generated method stub

            }

            @Override
            public int onGetOfflineMessage() {
                // TODO Auto-generated method stub
                return ECDevice.SYNC_OFFLINE_MSG_ALL;
            }

            @Override
            public void OnReceiveGroupNoticeMessage(ECGroupNoticeMessage notice) {
                // TODO Auto-generated method stub
                if (notice == null) {
                    return;
                }
                Intent intent = new Intent();
                intent.setAction("com.im.group.notice");
                intent.putExtra("groupnotice", notice);
                getInstance().mContext.sendBroadcast(intent);// 发送一个异步广播

            }

            @Override
            public void OnReceivedMessage(ECMessage msg) {
                Log.i("info", "OnReceivedMessage");
//				handlerMsg(msg);
//                count++;
                toDealWith(msg, false);
            }
        });


        // 来电
        Intent intent = new Intent(mContext,
                com.abcs.huaqiaobang.ytbt.call.CallActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ECDevice.setPendingIntent(pendingIntent);
        // 设置VOIP 自定义铃声路径
        ECVoIPSetupManager setupManager = ECDevice.getECVoIPSetupManager();
        if (setupManager != null) {
            // 目前支持下面三种路径查找方式
            // 1、如果是assets目录则设置为前缀[assets://]
            setupManager.setInComingRingUrl(true, "assets://phonering.mp3");
            setupManager.setOutGoingRingUrl(true, "assets://phonering.mp3");
            setupManager.setBusyRingTone(true, "assets://playend.mp3");
            // 2、如果是raw目录则设置为前缀[raw://]
            // 3、如果是SDCard目录则设置为前缀[file://]
        }
        // 注册会议消息处理监听
        if (ECDevice.getECMeetingManager() != null) {
            ECDevice.getECMeetingManager().setOnMeetingListener(
                    MeetingMsgReceiver.getInstance());
        }

        if (params.validate()) {
            // 判断注册参数是否正确
            ECDevice.login(params);
        }
    }

    // private void handlerMsg(ECMessage msg) {
    // try {
    // grouplist = (ArrayList<GroupBean>) MyApplication.dbUtils
    // .findAll(GroupBean.class);
    // userlist = (ArrayList<User>) MyApplication.dbUtils
    // .findAll(User.class);
    // } catch (DbException e) {
    // e.printStackTrace();
    // return;
    // }
    // for (int i = 0; i < userlist.size(); i++) {
    // if (userlist != null
    // && userlist.get(i).getVoipAccout()
    // .equals(msg.getForm())) {
    // isfrien = true;
    // break;
    // }
    // }
    // if (!isfrien) {
    // for (int j = 0; j < grouplist.size(); j++) {
    // if (grouplist != null
    // && grouplist.get(j).getGroupId()
    // .equals(msg.getForm())) {
    // isfrien = true;
    // break;
    // }
    // }
    // }
    // if (isfrien) {
    // toDealWith(msg);
    // isfrien = false;
    // }
    // }

    boolean isGroup;
    String msgId;

    protected void toDealWith(final ECMessage msg, final boolean isOffMsg) {
        conversationBean = new ConversationBean();
        msgId = msg.getTo();
        msgBean = new MsgBean();
        msgBean.setMsgtime(System.currentTimeMillis());
        msgBean.setMsgfrom(msg.getForm());
        msgBean.setMgsTo(msg.getTo());
        conversationBean.setMsgfrom(msg.getUserData());
        if (msg.getTo().indexOf("g") >= 0) {
            isGroup = true;
            msgtype = MyApplication.getInstance().getUserBean()
                    .getVoipAccount()
                    + "rev" + msg.getTo();
            conversationBean.setIsgroup(true);
            conversationBean.setConversationpeople(MyApplication.getInstance()
                    .getUserBean().getVoipAccount()
                    + msg.getTo());
//			conversationBean.setMsgfrom(MyApplication.getInstance()
//					.getUserBean().getVoipAccount());
            conversationBean.setMsgto(msg.getTo());
        } else {
            isGroup = false;
            msgtype = msg.getTo() + "rev" + msg.getForm();
            conversationBean.setIsgroup(false);
            conversationBean.setConversationpeople(msg.getTo() + msg.getForm());
//			conversationBean.setMsgfrom(msg.getTo());
            conversationBean.setMsgto(msg.getForm());
        }
        msgBean.setType(msgtype);
        conversationBean.setMsglasttime(msg.getMsgTime());
        ECMessage.Type type = msg.getType();
        if (type == ECMessage.Type.TXT) {
            alertMag(isGroup);
            textMessageBody = (ECTextMessageBody) msg.getBody();
            msgBean.setImg("");
            msgBean.setVoicepath("");
            msgBean.setMsg(textMessageBody.getMessage().toString());
            conversationBean.setMsg(textMessageBody.getMessage().toString());
            saveData(msgBean, isOffMsg);
        }
        if (type == ECMessage.Type.IMAGE) {
            ECImageMessageBody imageMessageBody = (ECImageMessageBody) msg
                    .getBody();
//			String thumbnailFileUrl = imageMessageBody.getThumbnailFileUrl();
            final String remoteUrl = imageMessageBody.getRemoteUrl();
            File file = new File(FileAccessor.IMESSAGE_IMAGE,
                    Tool.getPhotoFileName());
            HttpUtils httpUtils = new HttpUtils(60000);
            httpUtils.download(remoteUrl, file.getPath(),
                    new RequestCallBack<File>() {
                        @Override
                        public void onSuccess(ResponseInfo<File> arg0) {
                            msgBean.setMsg("");
                            msgBean.setVoicepath("");
                            msgBean.setImg(arg0.result.getPath());
                            conversationBean.setMsg("[图片]");
                            saveData(msgBean, isOffMsg);
                            alertMag(isGroup);
                        }

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            msgBean.setMsg("");
                            msgBean.setVoicepath("");
                            msgBean.setImg(remoteUrl);
                            conversationBean.setMsg("[图片]");
                            saveData(msgBean, isOffMsg);
                            alertMag(isGroup);
                        }
                    });
        }
        if (type == ECMessage.Type.VOICE) {
            final ECVoiceMessageBody voiceMsgBody = (ECVoiceMessageBody) msg
                    .getBody();
            // 获得下载地址
            final String remoteUrl = voiceMsgBody.getRemoteUrl();
            File file = new File(FileAccessor.IMESSAGE_VOICE,
                    Tool.getVioceFileName());
            HttpUtils httpUtils = new HttpUtils(60000);
            httpUtils.download(remoteUrl, file.getPath(),
                    new RequestCallBack<File>() {
                        @Override
                        public void onSuccess(ResponseInfo<File> arg0) {
                            msgBean.setMsg("");
                            msgBean.setImg("");
                            try {
                                msgBean.setVoicepath(arg0.result.getPath()
                                        + "~"
                                        + Tool.getAmrDuration(arg0.result)
                                        / 1000);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            conversationBean.setMsg("[语音]");
                            saveData(msgBean, isOffMsg);
                            alertMag(isGroup);
                        }

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            msgBean.setMsg("");
                            msgBean.setImg("");
                            msgBean.setVoicepath(remoteUrl + "~"
                                    + voiceMsgBody.getLength() / 1000);
                            conversationBean.setMsg("[语音]");
                            saveData(msgBean, isOffMsg);
                            alertMag(isGroup);
                        }
                    });
        }
    }

    public void alertMag(boolean isGroup) {
        if (isGroup ? MyApplication.isShake
                && (!mContext
                .getSharedPreferences("user", Context.MODE_PRIVATE)
                .getBoolean(msgId, false)) : MyApplication.isShake) {
            Vibrator vibrator = (Vibrator) mContext
                    .getSystemService(Service.VIBRATOR_SERVICE);
            vibrator.vibrate(250);
        }
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        if (isGroup ? MyApplication.isSound
                && (!mContext
                .getSharedPreferences("user", Context.MODE_PRIVATE)
                .getBoolean(msgId, false)) : MyApplication.isSound) {
            try {
                AssetManager am = mContext.getAssets();
                if (!mPlayer.isPlaying()) {
                    mPlayer.reset();
                    mPlayer.setDataSource(am.openFd("abc.mp3")
                            .getFileDescriptor());
                    mPlayer.prepare();
                    mPlayer.start();
                }
            } catch (Exception e) {
                Log.i("xbb", e.toString());
            }
        }
    }

    private void saveData(MsgBean msgbean, boolean isOffMsg) {
        List<TopConversationBean> topconversationBeans = new ArrayList<>();
        List<ConversationBean> conversationBeans = new ArrayList<>();
        try {
            MyApplication.dbUtils.createTableIfNotExist(TopConversationBean.class);
            MyApplication.dbUtils.createTableIfNotExist(ConversationBean.class);
            MyApplication.dbUtils.createTableIfNotExist(MsgBean.class);
            topconversationBeans.addAll(MyApplication.dbUtils.findAll(TopConversationBean.class));
            conversationBeans.addAll(MyApplication.dbUtils.findAll(ConversationBean.class));
            MyApplication.dbUtils.saveOrUpdate(msgbean);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (isOffMsg) {
            try {
                boolean topflag = false;
                for (int a = 0; a < topconversationBeans.size(); a++) {
                    if (topconversationBeans.get(a).getConversationpeople().equals(conversationBean.getConversationpeople())) {
                        topflag = true;
                        conversationBean.setUnread(topconversationBeans.get(a).getUnread() + 1);
                    }
                }
                if (!topflag) {
                    boolean i = false;
                    for (ConversationBean c : conversationBeans) {
                        if (c.getConversationpeople().equals(conversationBean.getConversationpeople())) {
                            conversationBean.setUnread(c.getUnread() + 1);
                            i = true;
                            break;
                        }
                    }
                    if (!i) conversationBean.setUnread(1);
                }
                MyApplication.dbUtils.saveOrUpdate(topflag ? Tool.toTopconversation(conversationBean,
                        topConversationBean) : conversationBean);
            } catch (DbException e) {
                e.printStackTrace();
            }
        } else {
            if (isBackground(mContext)) {
//				MyApplication.unreadCount++;
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

                Notification notification = new NotificationCompat.Builder(mContext)
                        .setContentTitle("华人邦")
                        .setContentText("您有:" + count + "条新消息")
                        .setTicker("有新消息")
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .setWhen(System.currentTimeMillis())
                        .build();

//                Notification notification = NotificationUtil.buildNotification(mContext, R.drawable.ic_launcher, 0, false, "消息", null, null, null, pendingIntent);
                ((NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, notification);
            }
            Intent intent = new Intent();
            intent.setAction(action);
            intent.putExtra("conversation", conversationBean);
            getInstance().mContext.sendOrderedBroadcast(intent, null);
        }
    }

    @Override
    public void onLogout() {
        // TODO Auto-generated method stub
        ECDevice.NotifyMode notifyMode = ECDevice.NotifyMode.IN_NOTIFY;
        ECDevice.logout(notifyMode, getInstance());
    }

    @Override
    public void onConnect() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectState(ECConnectState arg0, ECError arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnect(ECError arg0) {
        // TODO Auto-generated method stub

    }

    public boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                } else {
                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

}
