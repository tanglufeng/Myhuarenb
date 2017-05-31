package com.abcs.huaqiaobang.ytbt.call;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.common.utils.DemoUtils;
import com.abcs.huaqiaobang.ytbt.util.CircleImageView;
import com.abcs.huaqiaobang.ytbt.util.GlobalConstant;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.lidroid.xutils.db.sqlite.Selector;
import com.yuntongxun.ecsdk.CameraCapability;
import com.yuntongxun.ecsdk.CameraInfo;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECVoIPCallManager.CallType;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;
import com.yuntongxun.ecsdk.VoIPCallUserInfo;
import com.yuntongxun.ecsdk.voip.video.ECCaptureView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class CallActivity extends BaseActivity implements OnClickListener,
        VoIPCallHelper.OnCallEventNotifyListener {
    /**
     * 通话昵称
     */
    protected String mCallName;
    /**
     * 通话号码
     */
    protected String mCallNumber;
    protected String mPhoneNumber;
    /**
     * 是否来电
     */
    protected boolean mOutGoingCall = false;
    /**
     * 呼叫唯一标识号
     */
    protected String mCallId;
    /**
     * 呼入方或者呼出方
     */
    public static final String EXTRA_OUTGOING_CALL = "con.yuntongxun.ecdemo.VoIP_OUTGOING_CALL";
    /**
     * VoIP呼叫类型（音视频）
     */
    protected CallType mCallType;
    /**
     * 透传号码参数
     */
    private static final String KEY_TEL = "tel";
    /**
     * 透传名称参数
     */
    private static final String KEY_NAME = "nickname";
    // VOIP类型
    private int callType;
    private SurfaceView mVideoView;
    private ECCaptureView mCaptureView;
    private CameraInfo[] cameraInfos;
    private int numberOfCameras;
    private int defaultCameraId = -1;
    private int mCameraCapbilityIndex;
    private TextView tv_tips, tv_num, tv_state, tv_mute, tv_loader;
    private CircleImageView iv_photo;
    private RelativeLayout mute, loader;
    private Timer timer;
    private long time;
    private SimpleDateFormat format = new SimpleDateFormat("mm:ss",
            Locale.CHINA);
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    time += 1000;
                    tv_tips.setText(format.format(new Date(time)));
                    break;
            }
        }
    };
    private RelativeLayout rl;
    private RelativeLayout controler;
    private RelativeLayout bt_video_stop;
    private RelativeLayout bt_video_start;
    private RelativeLayout switch_camera;
    private RelativeLayout rl_out;
    private RelativeLayout rl_in;
    private boolean mSpeakerOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VoIPCallHelper.setOnCallEventNotifyListener(this);
        setScreemOn();
        moveToFront();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }
        // 获取是否是呼入还是呼出
        mOutGoingCall = (getIntent()
                .getBooleanExtra(EXTRA_OUTGOING_CALL, false));
        if (mOutGoingCall) {
            try {
                outGoingCall();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                inGoingCall();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 唤醒屏幕并解除屏幕锁,屏幕保持常亮
     */
    private void setScreemOn() {
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 将应用移到前台
     */
    private void moveToFront() {
        ActivityManager activityManager = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> list = activityManager
                .getRunningAppProcesses();
        for (RunningAppProcessInfo runningAppProcessInfo : list) {
            if (runningAppProcessInfo.processName.equals("com.abcs.huaqiaobang")) {
                activityManager.moveTaskToFront(runningAppProcessInfo.pid,
                        ActivityManager.MOVE_TASK_WITH_HOME);
            }
        }
    }

    /**
     * 呼出
     */
    private void outGoingCall() {
        callType = getIntent().getIntExtra("callType", 0);
        String num = getIntent().getStringExtra("num");
        String name = getIntent().getStringExtra("name");
        if (callType == GlobalConstant.Call_TYPE_VIDEO) {
            setContentView(R.layout.ec_video_call);
            initVideoView();
            tv_num = (TextView) findViewById(R.id.tv_num);
            tv_num.setText(name);
            iv_photo = (CircleImageView) findViewById(R.id.imv_photo);
            tv_num.setVisibility(View.VISIBLE);
            iv_photo.setVisibility(View.VISIBLE);
        } else {
            setContentView(R.layout.activity_out_call);
            tv_state = (TextView) findViewById(R.id.tv_state);
            tv_state.setText("取消");
            tv_mute = (TextView) findViewById(R.id.textView1);
            tv_loader = (TextView) findViewById(R.id.textView2);
            mute = (RelativeLayout) findViewById(R.id.mute);
            loader = (RelativeLayout) findViewById(R.id.loudspeaker);
            tv_tips = (TextView) findViewById(R.id.tv_tips);
            tv_num = (TextView) findViewById(R.id.tv_num);
            tv_num.setText(name);
            iv_photo = (CircleImageView) findViewById(R.id.imv_photo);
            mute.setOnClickListener(this);
            loader.setOnClickListener(this);
            mute.setVisibility(View.GONE);
            loader.setVisibility(View.GONE);
            tv_mute.setVisibility(View.GONE);
            tv_loader.setVisibility(View.GONE);
        }
        // 创建一个个人信息参数对象
        VoIPCallUserInfo mUserInfo = new VoIPCallUserInfo();
        mUserInfo.setNickName(name);
        mUserInfo.setPhoneNumber(num);
        // 调用VoIP设置接口注入VoIP呼叫透传参数
        ECVoIPSetupManager setupManager = ECDevice.getECVoIPSetupManager();
        setupManager.setVoIPCallUserInfo(mUserInfo);
        CallType type = null;
        switch (callType) {
            case GlobalConstant.Call_TYPE_VOICE:// 语音
                type = CallType.VOICE;
                try {
                    MyApplication.bitmapUtils.display(iv_photo, getIntent()
                            .getStringExtra("avatar"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case GlobalConstant.Call_TYPE_VIDEO:// 视频
                type = CallType.VIDEO;
                try {
                    MyApplication.bitmapUtils.display(iv_photo, getIntent()
                            .getStringExtra("avatar"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case GlobalConstant.Call_TYPE_DIRECT:// 网络电话
                type = CallType.DIRECT;
                break;
        }
        mCallId = VoIPCallHelper.makeCall(type, num);
        if (mCallId == null) {
            Tool.showInfo(this, "呼叫失败");
            finishCalling();
            return;
        }
        ((RelativeLayout) findViewById(R.id.releaseCall))
                .setOnClickListener(this);

    }

    /**
     * 呼入
     */
    private void inGoingCall() {
        // 获取是否是音频还是视频
        mCallType = (CallType) getIntent()
                .getSerializableExtra(ECDevice.CALLTYPE);
        getIntent().getSerializableExtra(ECDevice.CALLTYPE);
        // Log (“开始这事相应的布局”)
        // 获取当前的callid
        mCallId = getIntent().getStringExtra(ECDevice.CALLID);
        Log.i("info", mCallId);
        // 获取对方的号码
        mCallNumber = getIntent().getStringExtra(ECDevice.CALLER);
        Log.i("info", mCallNumber);
        if (!mOutGoingCall) {
            // 透传信息
            String[] infos = getIntent().getExtras().getStringArray(
                    ECDevice.REMOTE);
            if (infos != null && infos.length > 0) {
                for (String str : infos) {
                    if (str.startsWith(KEY_TEL)) {
                        mPhoneNumber = DemoUtils.getLastwords(str, "=");
                        Log.i("info", mPhoneNumber);
                    } else if (str.startsWith(KEY_NAME)) {
                        mCallName = DemoUtils.getLastwords(str, "=");
                        Log.i("info", mCallName);
                    }
                }
            }
        }
        if (mCallType == CallType.VIDEO) {
            setContentView(R.layout.ec_video_call);
            initVideoView();
        } else {
            setContentView(R.layout.activity_call);
            rl = (RelativeLayout) findViewById(R.id.rl);
            rl.setVisibility(View.VISIBLE);
            controler = (RelativeLayout) findViewById(R.id.controler);
            controler.setVisibility(View.GONE);
            tv_tips = (TextView) findViewById(R.id.tv_tips);
            mute = (RelativeLayout) findViewById(R.id.mute);
            loader = (RelativeLayout) findViewById(R.id.loudspeaker);
            mute.setOnClickListener(this);
            loader.setOnClickListener(this);
        }
        iv_photo = (CircleImageView) findViewById(R.id.imv_photo);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_num.setText(mCallNumber.equals("0000000000") ? "会议" : mCallNumber);
        iv_photo.setVisibility(View.VISIBLE);
        try {
            User u = MyApplication.dbUtils.findFirst(Selector.from(User.class)
                    .where("voipAccout", "=", mCallNumber));
            if (u != null) {
                MyApplication.bitmapUtils.display(iv_photo, u.getAvatar());
                tv_num.setText(u.getNickname());
            }
        } catch (Exception e) {
        }
        tv_num.setVisibility(View.VISIBLE);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        ((RelativeLayout) findViewById(R.id.releaseCall))
                .setOnClickListener(this);
    }

    /**
     * 设置视频界面UI
     */
    private void initVideoView() {
        mVideoView = (SurfaceView) findViewById(R.id.video_view);
        mCaptureView = (ECCaptureView) findViewById(R.id.localvideo_view);
        mVideoView.setVisibility(View.GONE);
        mCaptureView.setVisibility(View.GONE);
        mVideoView.getHolder().setFixedSize(240, 320);
        mCaptureView.setZOrderMediaOverlay(true);
        OpenSpeaker();
        setCamera();
        if (defaultCameraId != -1) {
            ECDevice.getECVoIPSetupManager().selectCamera(defaultCameraId,
                    mCameraCapbilityIndex, 15,
                    ECVoIPSetupManager.Rotate.ROTATE_AUTO, true);
        }
        ECDevice.getECVoIPSetupManager().setVideoView(mVideoView, mCaptureView);
        findViewById(R.id.rl_switch).setOnClickListener(this);
        findViewById(R.id.releaseCall).setOnClickListener(this);
        rl_out = (RelativeLayout) findViewById(R.id.rl_out);
        rl_in = (RelativeLayout) findViewById(R.id.rl_in);
        if (mOutGoingCall) {
            rl_out.setVisibility(View.VISIBLE);
            rl_in.setVisibility(View.GONE);
        } else {
            rl_out.setVisibility(View.GONE);
            rl_in.setVisibility(View.VISIBLE);
            findViewById(R.id.button1).setOnClickListener(this);
            findViewById(R.id.button2).setOnClickListener(this);
        }

    }

    private void setCamera() {
        cameraInfos = ECDevice.getECVoIPSetupManager().getCameraInfos();
        if (cameraInfos != null) {
            numberOfCameras = cameraInfos.length;
        }
        for (int i = 0; i < numberOfCameras; i++) {
            if (cameraInfos[i].index == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
                defaultCameraId = i;
                comportCapbilityIndex(cameraInfos[i].caps);
            }
        }
    }

    private void comportCapbilityIndex(CameraCapability[] caps) {

        if (caps == null) {
            return;
        }
        int pixel[] = new int[caps.length];
        int _pixel[] = new int[caps.length];
        for (CameraCapability cap : caps) {
            if (cap.index >= pixel.length) {
                continue;
            }
            pixel[cap.index] = cap.width * cap.height;
        }
        System.arraycopy(pixel, 0, _pixel, 0, caps.length);
        Arrays.sort(_pixel);
        for (int i = 0; i < caps.length; i++) {
            if (pixel[i] == /* _pixel[0] */352 * 288) {
                mCameraCapbilityIndex = i;
                return;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                // 如果视频呼叫，则在接受呼叫之前，需要先设置视频通话显示的view
                // ECDevice.getECVoIPSetupManager().setVideoView(view, localView);
                // view 显示远端视频的surfaceview
                // localView本地显示视频的view
                VoIPCallHelper.acceptCall(mCallId);
                if (rl != null) {
                    rl.setVisibility(View.GONE);
                    controler.setVisibility(View.VISIBLE);
                    setTimer();
                }
                if (rl_out != null) {
                    rl_out.setVisibility(View.VISIBLE);
                    rl_in.setVisibility(View.GONE);
                    try {
                        tv_num.setVisibility(View.GONE);
                        iv_photo.setVisibility(View.GONE);
                        mCaptureView.setVisibility(View.VISIBLE);
                        mVideoView.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.button2:
                VoIPCallHelper.rejectCall(mCallId);
                break;
            case R.id.releaseCall:
                VoIPCallHelper.releaseCall(mCallId);
                finishCalling();
                break;
            case R.id.mute:
                if (VoIPCallHelper.setMute() == 0) {
                    Tool.showInfo(this, VoIPCallHelper.getInstance().mCallSetInterface.getMuteStatus() ? "已切换静音" : "已关闭静音");
                } else Tool.showInfo(this, "切换失败");
                break;
            case R.id.loudspeaker:
                changeSpeakerOnMode();
                break;
            case R.id.rl_switch:
                if (mCaptureView != null) {
                    mCaptureView.switchCamera();
                }
                break;
        }
    }

    @Override
    public void onCallProceeding(String callId) {
        // TODO Auto-generated method stub
        Log.i("info", "正在连接服务器处理呼叫请求");
    }

    @Override
    public void onMakeCallback(ECError arg0, String arg1, String arg2) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onCallAlerting(String callId) {
        // TODO Auto-generated method stub
        Log.i("info", "呼叫到达对方客户端，对方正在振铃");
        if (tv_tips != null)
            tv_tips.setText("正在等待对方接受邀请");
    }

    @Override
    public void onCallAnswered(String callId) {
        Log.i("info", "对方接听本次呼叫");
        ((Vibrator) getSystemService(Service.VIBRATOR_SERVICE)).vibrate(100);
        if (tv_mute != null) {
            mute.setVisibility(View.VISIBLE);
            loader.setVisibility(View.VISIBLE);
            tv_mute.setVisibility(View.VISIBLE);
            tv_loader.setVisibility(View.VISIBLE);
            tv_state.setText("挂断");
            setTimer();
        }
        if (callType == GlobalConstant.Call_TYPE_VIDEO && mOutGoingCall) {
            mCaptureView.setVisibility(View.VISIBLE);
            mVideoView.setVisibility(View.VISIBLE);
            tv_num.setVisibility(View.GONE);
            iv_photo.setVisibility(View.GONE);
        }
    }

    private void setTimer() {
        tv_tips.setText("00:00");
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 1000, 1000);
    }

    @Override
    public void onMakeCallFailed(String callId, int reason) {
        try {
            Log.i("info", "本次呼叫失败，根据失败原因播放提示音" + reason);
            String msg = getResources().getString(
                    CallFailReason.getCallFailReason(reason));
            Tool.showInfo(this, msg);
            VoIPCallHelper.releaseCall(mCallId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finishCalling();
        }
    }

    @Override
    public void onCallReleased(String callId) {
        ((Vibrator) getSystemService(Service.VIBRATOR_SERVICE)).vibrate(100);
        Tool.showInfo(this, "通话结束");
        if (callId != null && callId.equals(mCallId)) {
            VoIPCallHelper.releaseMuteAndHandFree();
        }
        finishCalling();
    }

    private void finishCalling() {
        if (timer != null) {
            timer.cancel();
        }
        if (isFinishing()) {
            return;
        }
        // insertCallLog();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCallType == CallType.VIDEO) {
            if (mCaptureView != null) {
                mCaptureView.onResume();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VoIPCallHelper.mHandlerVideoCall = false;
    }

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

    @SuppressWarnings("deprecation")
    public void OpenSpeaker() {
        try {// 判断扬声器是否在打开
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setMode(AudioManager.ROUTE_SPEAKER);
            // 获取当前通话音量
            // currVolume
            // =audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
            if (!audioManager.isSpeakerphoneOn()) {
                audioManager.setSpeakerphoneOn(true);
                audioManager
                        .setStreamVolume(
                                AudioManager.STREAM_VOICE_CALL,
                                audioManager
                                        .getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                                AudioManager.STREAM_VOICE_CALL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }

}
