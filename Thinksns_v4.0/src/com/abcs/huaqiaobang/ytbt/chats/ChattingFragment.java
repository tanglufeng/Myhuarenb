package com.abcs.huaqiaobang.ytbt.chats;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.ytbt.adapter.ChatsDetailsAdapter;
import com.abcs.huaqiaobang.ytbt.bean.ConversationBean;
import com.abcs.huaqiaobang.ytbt.bean.MsgBean;
import com.abcs.huaqiaobang.ytbt.bean.TopConversationBean;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.call.CallActivity;
import com.abcs.huaqiaobang.ytbt.common.utils.FileAccessor;
import com.abcs.huaqiaobang.ytbt.emotion.DisplayUtils;
import com.abcs.huaqiaobang.ytbt.emotion.EmotionGvAdapter;
import com.abcs.huaqiaobang.ytbt.emotion.EmotionPagerAdapter;
import com.abcs.huaqiaobang.ytbt.emotion.EmotionUtils;
import com.abcs.huaqiaobang.ytbt.emotion.StringUtils;
import com.abcs.huaqiaobang.ytbt.im.sdkhelper.SDKCoreHelper;
import com.abcs.huaqiaobang.ytbt.util.GlobalConstant;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECDeskManager;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECDevice.OnGetUserStateListener;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.ECUserState;
import com.yuntongxun.ecsdk.im.ECImageMessageBody;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;
import com.yuntongxun.ecsdk.im.ECVoiceMessageBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/26.
 */
public class ChattingFragment extends Fragment implements OnClickListener,
        OnTouchListener, OnItemClickListener, OnPageChangeListener {
    public static DbUtils dbUtils;
    String smgtype;
    Boolean isspeak = false;
    ListView chatDetailListview;
    EditText inputTxt;
    ArrayList<MsgBean> list = new ArrayList<>();
    Button send;
    ImageView more, back, mic;
    TextView chattingname, recording_hint;
    private String sendAccout;
    private ChatsDetailsAdapter adapter;
    private Boolean isgroup;
    private User friend;
    private String msgtype;
    //	private ArrayList<MsgBean> msgBeans = new ArrayList<>();
    private RelativeLayout voice, text, recordingContainer;
    private Button btn_set_mode_voice, startspeak, btn_more, btn_set_mode_text,
            btn_send, btn_emoji;
    private ConversationBean conversationBean;
    private LinearLayout more_controler, indicator;
    private ViewPager viewPager;
    private long recordTime;

    //    public ChattingFragment(User friend, Boolean isgroup) {
//        dbUtils = MyApplication.dbUtils;
//        try {
//            dbUtils.createTableIfNotExist(MsgBean.class);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//        this.msgtype = (MyApplication.getInstance().getUserBean()) == null ? "" : MyApplication.getInstance().getUserBean().getVoipAccount()
//                + "sendto" + friend.getVoipAccout();
//        this.friend = friend;
//        this.sendAccout = friend.getVoipAccout();
//        this.isgroup = isgroup;
//    }
    public static ChattingFragment getInstance(User friend, Boolean isgroup) {

        ChattingFragment fragment = new ChattingFragment();



        Bundle bundle = new Bundle();

        bundle.putSerializable("friend", friend);
        bundle.putString("sendAccout", friend.getVoipAccout());
        bundle.putBoolean("isgroup", isgroup);
        fragment.setArguments(bundle);


        return fragment;
    }


    private File file;
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                InitData();
            }
        }
    };

    //	private Uri uri;
//	private Button speak;
//	private String imgurl;
    private BroadcastReceiver receiver;
    private View view;
    private String msg;
    private EmotionPagerAdapter emotionPagerGvAdapter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(
                R.layout.activity_chat_detail, null);

        dbUtils = MyApplication.dbUtils;
        try {
            dbUtils.createTableIfNotExist(MsgBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        friend= (User) getArguments().getSerializable("friend");
        msgtype = (MyApplication.getInstance().getUserBean()) == null ? "" : MyApplication.getInstance().getUserBean().getVoipAccount()
                + "sendto" + friend.getVoipAccout();
        isgroup=getArguments().getBoolean("isgroup");
        sendAccout=getArguments().getString("sendAccout");
        updateUnread();
        initView();
        InitData();
        initEmotion();
        return view;
    }

    private void initView() {
        btn_send = (Button) view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        btn_emoji = (Button) view.findViewById(R.id.btn_emoji);
        btn_emoji.setOnClickListener(this);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(this);
        voice = (RelativeLayout) view.findViewById(R.id.speak);
        voice.setVisibility(View.GONE);
        voice.setOnTouchListener(this);
        recordingContainer = (RelativeLayout) view
                .findViewById(R.id.recording_container);
        text = (RelativeLayout) view.findViewById(R.id.text);
        text.setVisibility(View.VISIBLE);
        view.findViewById(R.id.image).setOnClickListener(this);
        view.findViewById(R.id.camera).setOnClickListener(this);
        view.findViewById(R.id.voice_call).setOnClickListener(this);
        view.findViewById(R.id.video_call).setOnClickListener(this);
        more_controler = (LinearLayout) view.findViewById(R.id.more_controler);
        more_controler.setVisibility(View.GONE);
        indicator = (LinearLayout) view.findViewById(R.id.indicator);
        // startspeak = (RelativeLayout) view.findViewById(R.id.startspeak);
        // startspeak.setOnTouchListener(this);
        btn_more = (Button) view.findViewById(R.id.btn_more);
        btn_more.setOnClickListener(this);
        btn_set_mode_voice = (Button) view
                .findViewById(R.id.btn_set_mode_voice);
        btn_set_mode_text = (Button) view.findViewById(R.id.btn_set_mode_text);
        btn_set_mode_voice.setOnClickListener(this);
        btn_set_mode_voice.setVisibility(View.VISIBLE);
        btn_set_mode_text.setVisibility(View.GONE);
        wakeLock = ((PowerManager) getActivity().getSystemService(
                Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
        btn_set_mode_text.setOnClickListener(this);
        chatDetailListview = (ListView) view
                .findViewById(R.id.chat_detail_listview);
        chatDetailListview.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                more_controler.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                indicator.setVisibility(View.GONE);
                hideSoftInput();
                return false;
            }
        }); // chatDetailListview.setItemsCanFocus(false);

        inputTxt = (EditText) view.findViewById(R.id.input_txt);
        inputTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                btn_more.setVisibility(s.toString().equals("") ? View.VISIBLE
                        : View.GONE);
                btn_send.setVisibility(s.toString().equals("") ? View.GONE
                        : View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        setEditText();
        more = (ImageView) view.findViewById(R.id.more);
        mic = (ImageView) view.findViewById(R.id.mic_image);
        recording_hint = (TextView) view.findViewById(R.id.recording_hint);
    }

    private void setEditText() {
        inputTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                more_controler.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                indicator.setVisibility(View.GONE);
            }
        });
        // inputTxt.setOnEditorActionListener(new OnEditorActionListener() {
        // @Override
        // public boolean onEditorAction(TextView v, int actionId,
        // KeyEvent event) {
        // if (actionId == EditorInfo.IME_ACTION_SEND) {
        // sendtxt();
        // inputTxt.setText("");
        // return true;
        // }
        // return false;
        // }
        // });
        // inputTxt.setOnKeyListener(new OnKeyListener() {
        //
        // @Override
        // public boolean onKey(View v, int keyCode, KeyEvent event) {
        // if (KeyEvent.KEYCODE_ENTER == keyCode
        // && event.getAction() == KeyEvent.ACTION_DOWN) {
        // sendtxt();
        // inputTxt.setText("");
        // return true;
        // }
        // return false;
        // }
        // });
        more = (ImageView) view.findViewById(R.id.more);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnread();
                try {
                    List<MsgBean> list = MyApplication.dbUtils.findAll(Selector
                            .from(MsgBean.class).orderBy("msgtime", false));
                    adapter.setList(list);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        };
        IntentFilter filter = new IntentFilter();// 构造过滤器对象
        filter.addAction(SDKCoreHelper.action);
        // 给过滤器设置动作字符串 用来匹配intent的动作决定启动接收器
        filter.setPriority(100);
        getActivity().registerReceiver(receiver, filter);

    }

    private void updateUnread() {
        try {
            TopConversationBean t = MyApplication.dbUtils.findById(
                    TopConversationBean.class, MyApplication.getInstance()
                            .getUserBean().getVoipAccount()
                            + friend.getVoipAccout());
            ConversationBean c = MyApplication.dbUtils.findById(
                    ConversationBean.class, MyApplication.getInstance()
                            .getUserBean().getVoipAccount()
                            + friend.getVoipAccout());
            if (t != null) {
                t.setUnread(0);
                MyApplication.dbUtils.saveOrUpdate(t);
            }
            if (c != null) {
                c.setUnread(0);
                MyApplication.dbUtils.saveOrUpdate(c);
            }
        } catch (DbException e1) {
            e1.printStackTrace();
        }
        getActivity().sendBroadcast(new Intent("action_con_unread"));
    }


    private void InitData() {



        adapter = new ChatsDetailsAdapter(getActivity(), MyApplication
                .getInstance().getMsgBeans(), friend, isgroup);
        try {
            chatDetailListview.setAdapter(adapter);
            chatDetailListview.setSelection(adapter.getList().size());
            adapter.setList(dbUtils.findAll(MsgBean.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化表情面板内容
     */
    private void initEmotion() {
        // 获取屏幕宽度
        int gvWidth = DisplayUtils.getScreenWidthPixels(getActivity());
        // 表情边距
        int spacing = DisplayUtils.dp2px(getActivity(), 8);
        // GridView中item的宽度
        int itemWidth = (gvWidth - spacing * 8) / 7;
        int gvHeight = itemWidth * 3 + spacing * 4;

        List<GridView> gvs = new ArrayList<GridView>();
        List<String> emotionNames = new ArrayList<String>();
        // 遍历所有的表情名字
        for (String emojiName : EmotionUtils.emojiMap.keySet()) {
            emotionNames.add(emojiName);
            // 每20个表情作为一组,同时添加到ViewPager对应的view集合中
            if (emotionNames.size() == 20) {
                GridView gv = createEmotionGridView(emotionNames, gvWidth,
                        spacing, itemWidth, gvHeight);
                gvs.add(gv);
                // 添加完一组表情,重新创建一个表情名字集合
                emotionNames = new ArrayList<String>();
            }
        }

        // 检查最后是否有不足20个表情的剩余情况
        if (emotionNames.size() > 0) {
            GridView gv = createEmotionGridView(emotionNames, gvWidth, spacing,
                    itemWidth, gvHeight);
            gvs.add(gv);
        }

        // 将多个GridView添加显示到ViewPager中
        emotionPagerGvAdapter = new EmotionPagerAdapter(gvs);
        viewPager.setAdapter(emotionPagerGvAdapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gvWidth, gvHeight);
        viewPager.setLayoutParams(params);
        indicator.getChildAt(0).setBackgroundResource(
                R.drawable.indicator_selected);
    }

    /**
     * 创建显示表情的GridView
     */
    private GridView createEmotionGridView(List<String> emotionNames,
                                           int gvWidth, int padding, int itemWidth, int gvHeight) {
        // 创建GridView
        GridView gv = new GridView(getActivity());
        // gv.setBackgroundColor(Color.rgb(233, 233, 233));
        gv.setSelector(android.R.color.transparent);
        gv.setNumColumns(7);
        gv.setPadding(padding, padding, padding, padding);
        gv.setHorizontalSpacing(padding);
        gv.setVerticalSpacing(padding);
        LayoutParams params = new LayoutParams(gvWidth, gvHeight);
        gv.setLayoutParams(params);
        // 给GridView设置表情图片
        EmotionGvAdapter adapter = new EmotionGvAdapter(getActivity(),
                emotionNames, itemWidth);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(this);
        return gv;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set_mode_voice:
                btn_set_mode_voice.setVisibility(View.GONE);
                btn_set_mode_text.setVisibility(View.VISIBLE);
                voice.setVisibility(View.VISIBLE);
                text.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputTxt.getWindowToken(), 0);
                break;
            case R.id.btn_set_mode_text:
                btn_set_mode_voice.setVisibility(View.VISIBLE);
                btn_set_mode_text.setVisibility(View.GONE);
                voice.setVisibility(View.GONE);
                text.setVisibility(View.VISIBLE);
                inputTxt.requestFocus();
                break;
            case R.id.btn_more:
                viewPager.setVisibility(View.GONE);
                indicator.setVisibility(View.GONE);
                if (more_controler.getVisibility() == View.GONE) {
                    more_controler.setVisibility(View.VISIBLE);
                    hideSoftInput();
                } else {
                    more_controler.setVisibility(View.GONE);
                }
                break;
            case R.id.image:
                if (!MyApplication.users.contains(friend) && !isgroup) {
                    Tool.showInfo(getActivity(), "请先加为好友!");
                    return;
                }
                InitPicture();
                more_controler.setVisibility(View.GONE);
                break;
            case R.id.camera:
                if (!MyApplication.users.contains(friend) && !isgroup) {
                    Tool.showInfo(getActivity(), "请先加为好友!");
                    return;
                }
                InitPhotos();
                more_controler.setVisibility(View.GONE);
                break;
            case R.id.voice_call:
                if (isgroup) {
                    Tool.showInfo(getActivity(), "群组不能语音聊天");
                    return;
                } else if (!MyApplication.users.contains(friend) && !isgroup) {
                    Tool.showInfo(getActivity(), "请先加为好友!");
                    return;
                }
                callVoice();
                more_controler.setVisibility(View.GONE);
                break;
            case R.id.video_call:
                if (isgroup) {
                    Tool.showInfo(getActivity(), "群组不能视频聊天");
                    return;
                } else if (!MyApplication.users.contains(friend) && !isgroup) {
                    Tool.showInfo(getActivity(), "请先加为好友!");
                    return;
                }
                callVideo();
                more_controler.setVisibility(View.GONE);
                break;
            case R.id.btn_send:
                if (!MyApplication.users.contains(friend) && !isgroup) {
                    Tool.showInfo(getActivity(), "请先加为好友!");
                    inputTxt.setText("");
                    return;
                }
                msg = inputTxt.getText().toString().trim();
                if (msg.isEmpty()) {
                    Toast.makeText(getActivity(), "消息为空", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                sendtxt(packagMsg(0));
                break;
            case R.id.btn_emoji:
                hideSoftInput();
                viewPager
                        .setVisibility(viewPager.getVisibility() == View.VISIBLE ? View.GONE
                                : View.VISIBLE);
                indicator
                        .setVisibility(viewPager.getVisibility() == View.VISIBLE ? View.VISIBLE
                                : View.GONE);
                more_controler.setVisibility(View.GONE);
                break;
        }

    }

    private void hideSoftInput() {
        InputMethodManager im = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                        .getApplicationWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

//	private void showSoftInput() {
//		InputMethodManager imm = (InputMethodManager) getActivity()
//				.getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.showSoftInput(inputTxt, InputMethodManager.SHOW_FORCED);
//	}

    private void callVoice() {
        ECDevice.getUserState("对方的账号userid", new OnGetUserStateListener() {

            @Override
            public void onGetUserState(ECError arg0, ECUserState arg1) {

            }
        });
        Intent intent = new Intent(getActivity(), CallActivity.class);
        intent.putExtra("con.yuntongxun.ecdemo.VoIP_OUTGOING_CALL", true);
        intent.putExtra("callType", GlobalConstant.Call_TYPE_VOICE);
        intent.putExtra("num", sendAccout);
        intent.putExtra("name", friend.getNickname());
        intent.putExtra("avatar", friend.getAvatar());
        startActivity(intent);
    }

    private void callVideo() {
        Intent intent = new Intent(getActivity(), CallActivity.class);
        intent.putExtra("con.yuntongxun.ecdemo.VoIP_OUTGOING_CALL", true);
        intent.putExtra("callType", GlobalConstant.Call_TYPE_VIDEO);
        intent.putExtra("num", sendAccout);
        intent.putExtra("name", friend.getNickname());
        intent.putExtra("avatar", friend.getAvatar());
        startActivity(intent);
    }

    private MediaRecorder mRecorder;
    //	private TopConversationBean topConversationBean;
    private int oldPagerPosition = 0;

    private void startspeak() {
        file = new File(FileAccessor.IMESSAGE_VOICE, Tool.getVioceFileName());
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mRecorder.setOutputFile(file.getPath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
            mRecorder.start();
            updateMicStatus();
            // Toast.makeText(getActivity(), "开始录音！",
            // Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getActivity(), "录音失败", Toast.LENGTH_SHORT).show();
            Log.i("xbb", e.toString());
            mRecorder = null;
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(getActivity(), "录音失败", Toast.LENGTH_SHORT).show();
            Log.i("xbb", e.toString());
            e.printStackTrace();
            mRecorder = null;
        }

    }

    private void stopspeak() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mRecorder == null) {
                    return;
                }
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
                // Toast.makeText(getActivity(), "停止录音，正在发送。。。。",
                // Toast.LENGTH_SHORT).show();
                // changeUI(1);

                // try {
                // messageBody.setDuration((int) (Tool.getAmrDuration(file)
                // / 1000));
                // } catch (Exception e) {
                // e.printStackTrace();
                // }
                // final ECChatManager manager = ECDevice.getECChatManager();
                // manager.startVoiceRecording(msg, new
                // OnRecordTimeoutListener() {
                // public void onRecordingTimeOut(long arg0) {
                // sendVoice(msg);
                // }
                //
                // public void onRecordingAmplitude(double arg0) {
                //
                // }
                // });
                // manager.stopVoiceRecording(new OnStopVoiceRecordingListener()
                // {
                //
                // @Override
                // public void onRecordingComplete() {
                // sendVoice(msg);
                // }
                // });
                sendVoice(packagMsg(1));
            }
        });

    }

    private int BASE = 600;
    private int SPACE = 200;// 间隔取样时间
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };

    private void updateMicStatus() {
        if (mRecorder != null && view != null) {
            // int vuSize = 10 * mMediaRecorder.getMaxAmplitude() / 32768;
            int ratio = mRecorder.getMaxAmplitude() / BASE;
            int db = 0;// 分贝
            if (ratio > 1)
                db = (int) (20 * Math.log10(ratio));
            // System.out.println("分贝值：" + db + "     " + Math.log10(ratio));
            switch (db / 4) {
                case 0:
                    mic.setImageResource(R.drawable.record_animate_03);
                    break;
                case 1:
                    mic.setImageResource(R.drawable.record_animate_05);
                    break;
                case 2:
                    mic.setImageResource(R.drawable.record_animate_07);
                    break;
                case 3:
                    mic.setImageResource(R.drawable.record_animate_10);
                    break;
                case 4:
                    mic.setImageResource(R.drawable.record_animate_12);
                    break;
                case 5:
                    mic.setImageResource(R.drawable.record_animate_14);
                    break;
                default:
                    mic.setImageResource(R.drawable.record_animate_01);
                    break;
            }
            handler.postDelayed(mUpdateMicStatusTimer, SPACE);
            /*
             * if (db > 1) { vuSize = (int) (20 * Math.log10(db)); Log.i("mic_",
			 * "麦克风的音量的大小：" + vuSize); } else Log.i("mic_", "麦克风的音量的大小：" + 0);
			 */
        }
    }

//	private void changeUI(int i) {
//		if (i == 1) {
//			startspeak.setVisibility(View.GONE);
//			inputTxt.setVisibility(View.VISIBLE);
//		} else {
//			startspeak.setVisibility(View.VISIBLE);
//			inputTxt.setVisibility(View.INVISIBLE);
//			Toast.makeText(getActivity(), "按下开始录音！", Toast.LENGTH_SHORT).show();
//		}
//	}

    private void InitPicture() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*");
        }
        startActivityForResult(intent, 333);
    }

    private void InitPhotos() {
        if (!Tool.isExitsSdcard()) {
            Tool.showInfo(getActivity(), "SD卡不存在，不能拍照");
            return;
        }
        file = new File(FileAccessor.IMESSAGE_IMAGE, Tool.getPhotoFileName());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, 444);
    }

    private PowerManager.WakeLock wakeLock;

    @SuppressLint("Wakelock")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!MyApplication.users.contains(friend) && !isgroup) {
            Tool.showInfo(getActivity(), "请先加为好友!");
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!Tool.isExitsSdcard()) {
                    Tool.showInfo(getActivity(), "发送语音需要sdcard支持！");
                    return false;
                }
                try {
                    v.setPressed(true);
                    wakeLock.acquire();
                    if (adapter.mPlayer != null && adapter.mPlayer.isPlaying()) {
                        adapter.mPlayer.stop();
                        adapter.resetAnim();
                    }
                    recordingContainer.setVisibility(View.VISIBLE);
                    recording_hint.setText("手指上滑，取消发送");
                    recording_hint.setBackgroundColor(Color.TRANSPARENT);
                    startspeak();
                } catch (Exception e) {
                    e.printStackTrace();
                    v.setPressed(false);
                    if (wakeLock.isHeld())
                        wakeLock.release();
                    if (mRecorder != null)
                        mRecorder.stop();
                    recordingContainer.setVisibility(View.INVISIBLE);
                    Tool.showInfo(getActivity(), "录音失败，请重试！");
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE: {
                if (event.getY() < 0) {
                    recording_hint.setText("松开手指，取消发送");
                    recording_hint
                            .setBackgroundResource(R.drawable.recording_hint_bg);
                } else {
                    recording_hint.setText("手指上滑，取消发送");
                    recording_hint.setBackgroundColor(Color.TRANSPARENT);
                }
                return true;
            }
            case MotionEvent.ACTION_UP:
                v.setPressed(false);
                recordingContainer.setVisibility(View.INVISIBLE);
                if (wakeLock.isHeld())
                    wakeLock.release();
                if (event.getY() < 0) {
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                    file.delete();
                    return true;
                }
                stopspeak();
                break;
        }
        return true;
    }

    public void sendtxt(final MsgBean m) {
        if (!Util.preference.getBoolean("islogin", false)) {
            Tool.showInfo(getActivity(), "请先登录!");
            getActivity().finish();
            return;
        }
        if (!MyApplication.users.contains(friend) && !isgroup) {
            Tool.showInfo(getActivity(), "请先加为好友!");
            return;
        }
        try {
            // final MsgBean m = packagMsg(0);
            final ECMessage message = ECMessage
                    .createECMessage(ECMessage.Type.TXT);
            if (isgroup) {
                message.setForm(MyApplication.getInstance().getUserBean()
                        .getVoipAccount());
                message.setMsgTime(System.currentTimeMillis());
                message.setSessionId(sendAccout);
                message.setDirection(ECMessage.Direction.SEND);
                message.setUserData(friend.toString());
            } else message.setUserData(MyApplication.getInstance().getUser().toString());

            message.setTo(sendAccout);
            ECTextMessageBody messageBody = new ECTextMessageBody(m.getMsg());
            message.setBody(messageBody);
            ECChatManager manager = ECDevice.getECChatManager();
            manager.sendMessage(message,
                    new ECDeskManager.OnSendDeskMessageListener() {
                        @Override
                        public void onSendMessageComplete(ECError ecError,
                                                          ECMessage ecMessage) {
                            if (ecError.errorCode == 200) {
                                m.setFlag(1);
                            } else {
                                m.setFlag(2);
                            }
                            setMsgBean(m);
                        }

                        @Override
                        public void onProgress(String s, int i, int i1) {

                        }
                    });
        } catch (Exception e) {
            Log.i("xbb群组消息发送成功a", e.toString());
        }
    }

    public void sendVoice(final MsgBean m) {
        if (!Util.preference.getBoolean("islogin", false)) {
            Tool.showInfo(getActivity(), "请先登录!");
            getActivity().finish();
            return;
        }
        if (!MyApplication.users.contains(friend) && !isgroup) {
            Tool.showInfo(getActivity(), "请先加为好友!");
            return;
        }
        // final MsgBean m = packagMsg(1);
        if (m == null) {
            return;
        }
        final ECMessage msg = ECMessage.createECMessage(ECMessage.Type.VOICE);
        msg.setTo(sendAccout);
        if (isgroup) {
            msg.setForm(MyApplication.getInstance().getUserBean()
                    .getVoipAccount());
            msg.setMsgTime(System.currentTimeMillis());
            msg.setSessionId(sendAccout);
            msg.setNickName(friend.getNickname());
            msg.setUserData(friend.toString());
        } else msg.setUserData(MyApplication.getInstance().getUser().toString());

        ECVoiceMessageBody messageBody = new ECVoiceMessageBody(new File(
                file.getPath()), 0);
        msg.setBody(messageBody);
        ECChatManager manager = ECDevice.getECChatManager();
        manager.sendMessage(msg, new ECDeskManager.OnSendDeskMessageListener() {
            @Override
            public void onSendMessageComplete(ECError ecError,
                                              ECMessage ecMessage) {
                if (ecError.errorCode == 200) {
                    m.setFlag(1);
                } else {
                    m.setFlag(2);
                }
                setMsgBean(m);
            }

            @Override
            public void onProgress(String msgId, int total, int progress) {
                Log.i("xbb", "[IMChattingHelper - onProgress] msgId：" + msgId
                        + " ,total：" + total + " ,progress:" + progress);
            }
        });

        adapter.notifyDataSetChanged();
    }

    public void sendimg(final MsgBean m) {
        if (!Util.preference.getBoolean("islogin", false)) {
            Tool.showInfo(getActivity(), "请先登录!");
            getActivity().finish();
            return;
        }
        if (!MyApplication.users.contains(friend) && !isgroup) {
            Tool.showInfo(getActivity(), "请先加为好友!");
            return;
        }
        // final MsgBean m = packagMsg(2);
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                final ECMessage msg = ECMessage
                        .createECMessage(ECMessage.Type.IMAGE);
                if (isgroup) {
                    msg.setForm(MyApplication.getInstance().getUserBean()
                            .getVoipAccount());
                    msg.setMsgTime(System.currentTimeMillis());
                    msg.setSessionId(sendAccout);
                    msg.setDirection(ECMessage.Direction.SEND);
                    msg.setUserData(friend.toString());
                } else msg.setUserData(MyApplication.getInstance().getUser().toString());

                msg.setTo(sendAccout);
                ECImageMessageBody msgBody = new ECImageMessageBody();
                // 设置附件名
                // msgBody.setFileName("Tony_2015.jpg");
                // 设置附件扩展名
                // msgBody.setFileExt("jpg");
                // 设置附件本地路径
                msgBody.setLocalUrl(file.getPath());
                msgBody.setIsCompress(true);
                msg.setBody(msgBody);
                ECChatManager manager = ECDevice.getECChatManager();
                manager.sendMessage(msg,
                        new ECChatManager.OnSendMessageListener() {
                            @Override
                            public void onSendMessageComplete(ECError error,
                                                              ECMessage message) {
                                // 处理消息发送结果
                                if (error.errorCode == 200) {
                                    m.setFlag(1);
                                } else {
                                    m.setFlag(2);
                                }
                                setMsgBean(m);
                            }

                            @Override
                            public void onProgress(String msgId, int totalByte,
                                                   int progressByte) {
                                // 处理文件发送上传进度（尽上传文件、图片时候SDK回调该方法）
                            }
                        });

            }
        });

    }

    protected MsgBean packagMsg(int i) {
        conversationBean = new ConversationBean();
        if (isgroup) {
            conversationBean.setIsgroup(true);
        } else {
            conversationBean.setIsgroup(false);
        }
//		conversationBean.setMsgfrom(MyApplication.getInstance().getUserBean()
//				.getVoipAccount());
        conversationBean.setMsgto(sendAccout);
        conversationBean.setMsgfrom(friend.toString());
        conversationBean.setConversationpeople(MyApplication.getInstance()
                .getUserBean().getVoipAccount()
                + sendAccout);

        conversationBean.setMsglasttime(System.currentTimeMillis());
        MsgBean msgBean = new MsgBean();
        msgBean.setType(msgtype);
        msgBean.setMsgfrom(MyApplication.getInstance().getUserBean()
                .getVoipAccount());
        msgBean.setMgsTo(sendAccout);
        msgBean.setMsgtime(System.currentTimeMillis());
        if (i == 0) {
            inputTxt.setText("");
            msgBean.setMsg(msg);
            msgBean.setImg("");
            msgBean.setVoicepath("");
            conversationBean.setMsg(msg);
        }
        if (i == 1) {
            msgBean.setMsg("");
            msgBean.setImg("");
            try {
                recordTime = Tool.getAmrDuration(file);
                if (recordTime < 1000) {
                    Tool.showInfo(getActivity(), "录音时间太短");
                    return null;
                }
                // Log.i("info",recordTime +"");
                msgBean.setVoicepath(file.getPath() + "~" + recordTime / 1000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            conversationBean.setMsg("[语音]");
        }
        if (i == 2) {
            msgBean.setMsg("");
            msgBean.setImg(file.getPath());
            msgBean.setVoicepath("");
            conversationBean.setMsg("[图片]");
        }
        setMsgBean(msgBean);
        Intent intent = new Intent();
        intent.setAction("com.robin.mybc.action4");
        intent.putExtra("conversation", conversationBean);
        getActivity().sendBroadcast(intent);// 发送一个异步广播
        return msgBean;
    }

    private void setMsgBean(MsgBean msgBean) {
        try {
            dbUtils.saveOrUpdate(msgBean);
            List<MsgBean> list = MyApplication.dbUtils.findAll(Selector.from(
                    MsgBean.class).orderBy("msgtime", false));
            adapter.setList(list);
            // adapter.setList(dbUtils.findAll(MsgBean.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Object itemAdapter = parent.getAdapter();
        if (itemAdapter instanceof EmotionGvAdapter) {
            // 点击的是表情
            EmotionGvAdapter emotionGvAdapter = (EmotionGvAdapter) itemAdapter;
            if (position == emotionGvAdapter.getCount() - 1) {
                // 如果点击了最后一个回退按钮,则调用删除键事件
                inputTxt.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_DEL));
            } else {
                // 如果点击了表情,则添加到输入框中
                String emotionName = emotionGvAdapter.getItem(position);
                // 获取当前光标位置,在指定位置上添加表情图片文本
                int curPosition = inputTxt.getSelectionStart();
                StringBuilder sb = new StringBuilder(inputTxt.getText()
                        .toString());
                sb.insert(curPosition, emotionName);
                // 特殊文字处理,将表情等转换一下
                inputTxt.setText(StringUtils.getEmotionContent(getActivity(),
                        inputTxt, sb.toString()));
                // 将光标设置到新增完表情的右侧
                inputTxt.setSelection(curPosition + emotionName.length());
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 444:// 拍照
                try {
                    byte[] bytes = Tool.getimage(file.getPath());
                    FileOutputStream f = new FileOutputStream(file);
                    f.write(bytes);
                    f.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                sendimg(packagMsg(2));
                break;
            case 333:// 图库
                if (data == null) {
                    return;
                }
                Uri uri = data.getData();
                String path = "";
                Cursor cursor = getActivity().getContentResolver().query(uri, null,
                        null, null, null);
                if (cursor != null) {
                    cursor.moveToNext();
                    path = cursor.getString(cursor
                            .getColumnIndex(Images.Media.DATA));
                    cursor.close();
                    cursor = null;
                } else {
                    path = uri.getPath();
                }
                try {
                    byte[] bytes = Tool.getimage(path);
                    file = new File(FileAccessor.IMESSAGE_IMAGE,
                            Tool.getPhotoFileName());
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(bytes);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sendimg(packagMsg(2));
                break;
            case 3:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
        indicator.getChildAt(oldPagerPosition).setBackgroundResource(
                R.drawable.indicator_normal);
        indicator.getChildAt(arg0).setBackgroundResource(
                R.drawable.indicator_selected);
        oldPagerPosition = arg0;
    }

}
