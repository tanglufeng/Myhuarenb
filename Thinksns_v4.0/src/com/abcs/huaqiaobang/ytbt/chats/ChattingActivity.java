package com.abcs.huaqiaobang.ytbt.chats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.ytbt.bean.GroupBean;
import com.abcs.huaqiaobang.ytbt.bean.GroupMemberBean;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.chats.group.GroupDetailsActivity;
import com.abcs.huaqiaobang.ytbt.util.JsonUtil;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChattingActivity extends BaseFragmentActivity implements OnClickListener {
    private ChattingFragment chattingFragment;

    public ChattingFragment getChattingFragment() {
        return chattingFragment;
    }

    public static final int REQUEST_CHAT = 100;
    private GroupBean groupBean;
    private Boolean isgroup;
    private ImageView more;
    private User friend;//group
    //	protected GroupMemberBean members;
    GroupMemberBean member;
    ArrayList<User> userlist = new ArrayList<>();
    public Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                try {

                    if (member != null) {
                        member.setMembers(JsonUtil.toString(userlist));
                    } else {
                        member = new GroupMemberBean();
                        member.setGroupid(friend.getVoipAccout());
                        member.setMembers(JsonUtil.toString(userlist));

                    }
                    MyApplication.dbUtils.saveOrUpdate(member);
                    // GroupMemberBean
                    // member=MyApplication.dbUtils.findById(GroupMemberBean.class,
                    // friend.getVoipAccout());
                    Log.i("xbb群组加入消息", member.getMembers() + "加入群组");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 1) {
                chatting.setText(groupBean.getGroupName());
            }

        }

    };
    private BroadcastReceiver receiver;
    private TextView chatting;

    @Override
    protected void onResume() {
        super.onResume();
        hideSoftInput();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chaatting);
        groupBC();
        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initView() {
        chatting = (TextView) findViewById(R.id.chattingname);
        try {
            MyApplication.dbUtils.createTableIfNotExist(GroupMemberBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        more = (ImageView) findViewById(R.id.more);
        isgroup = getIntent().getBooleanExtra("isgroup", false);
        friend = (User) getIntent().getSerializableExtra("friend");
        if (friend == null) {
            finish();
            return;
        }
        if (isgroup) {
            groupBean = (GroupBean) getIntent().getSerializableExtra("group");

            try {
                member = MyApplication.dbUtils.findById(
                        GroupMemberBean.class, friend.getVoipAccout());
            } catch (DbException e) {
                e.printStackTrace();
            }
            if (groupBean == null) {
                initGroup();
            }
            chatting.setText(friend.getNickname());
//			try {
//				member = MyApplication.dbUtils.findById(GroupMemberBean.class,
//						friend.getVoipAccout());
//				if (member != null) {
//					userlist.clear();
//					userlist.addAll(JsonUtil.parseString(member.getMembers()));
//				}else{
//					member = new GroupMemberBean();
//					member.setGroupid(friend.getVoipAccout());
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
            more.setVisibility(View.VISIBLE);
            more.setOnClickListener(this);
            queryGroupMember();
        } else if (friend.getRemark().trim().equals("")) {
            chatting.setText(friend.getNickname());
        } else {
            chatting.setText(friend.getRemark());
        }

        chattingFragment = ChattingFragment.getInstance(friend, isgroup);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.linearlayout, chattingFragment);
        transaction.commit();
        findViewById(R.id.back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initGroup() {
        Tool.showProgressDialog(this, "正在加载成员...", false);
        HttpRequest.sendGet(TLUrl.URL_GET_VOIP + "group/QueryGroupDetail",
                "uid=" + MyApplication.getInstance().getUid() + "&groupId="
                        + friend.getVoipAccout(), new HttpRevMsg() {
                    @Override
                    public void revMsg(String msg) {
                        Log.i("xbb群组详情", msg);
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.getInt("status") == 1) {
                                JSONObject object = jsonObject
                                        .getJSONObject("msg");
                                groupBean = new GroupBean();
                                groupBean.setDateCreate(object
                                        .getString("date"));
                                groupBean.setGroupDeclared(object
                                        .getString("declared"));
                                groupBean.setGroupName(object.getString("name"));
                                groupBean.setGroupOwner(object.getString("uid"));
                                groupBean.setGroupPermission(object
                                        .getString("permission"));
                                groupBean.setMemberInGroup(object
                                        .getString("count"));
                                groupBean.setGroupAvatar(object
                                        .getString("avatar"));
                                groupBean.setGroupId(object
                                        .getString("groupid"));
                                MyApplication.dbUtils.saveOrUpdate(groupBean);
                                myhandler.sendEmptyMessage(1);
                            }
                        } catch (Exception e) {
                            Log.i("xbb群组详情", e.toString());
                            e.printStackTrace();
                        } finally {
                            Tool.removeProgressDialog();
                        }
                    }
                });
    }

    private void queryGroupMember() {
        HttpUtils httpUtils = new HttpUtils(60000);
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET,
                TLUrl.URL_GET_VOIP + "member/QueryMember?uid="
                        + MyApplication.getInstance().getUid() + "&groupId="
                        + friend.getVoipAccout() + "&page=1&size=1000",
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        try {
                            JSONObject jsonObject = new JSONObject(arg0.result);
                            if (jsonObject.getInt("status") == 1) {
                                JSONArray jsonArray = jsonObject
                                        .getJSONArray("msg");
                                userlist.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray
                                            .getJSONObject(i);
                                    User user = new User();
                                    user.setAvatar(object.getString("avatar"));
                                    user.setNickname(object
                                            .getString("nickname"));
                                    user.setUid(object.getInt("uid"));
                                    user.setVoipAccout(object
                                            .getString("voipAccount"));
                                    userlist.add(user);
                                }
                                myhandler.sendEmptyMessage(0);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("xbb群组成员", e.toString());
                            e.printStackTrace();
                        } finally {

                        }
                    }
                });

    }

    // protected void savaMember(final String voip) {
    // // groupid=joinGroupMsg.getGroupId();
    // HttpRequest.sendGet(TLUrl.getInstance().URL_GET_VOIP + "User/findvoipuser",
    // "voipAccount=" + voip, new HttpRevMsg() {
    // @Override
    // public void revMsg(String msg) {
    // Log.i("xbb群组成员保存", msg);
    // if (msg.length() <= 0) {
    // return;
    // }
    // try {
    // JSONObject jsonObject = new JSONObject(msg);
    // if (jsonObject.getInt("status") == 1) {
    // JSONObject object = jsonObject
    // .getJSONObject("msg");
    // User user = new User();
    // user.setAvatar(object.getString("avatar"));
    // user.setNickname(object.getString("nickname"));
    // user.setUid(object.getInt("uid"));
    // user.setVoipAccout(voip);
    // Message message = myhandler.obtainMessage(0,
    // user);
    // myhandler.sendMessage(message);
    // }
    //
    // } catch (JSONException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    // });
    // }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setResult(REQUEST_CHAT);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, GroupDetailsActivity.class);
        intent.putExtra("groupid", friend.getVoipAccout());
        intent.putExtra("name", groupBean == null ? friend.getNickname() : groupBean.getGroupName());
        // intent.putExtra("users", userlist);
        intent.putExtra("group", groupBean);
        intent.putExtra("member", member);
        startActivity(intent);
    }

    private void hideSoftInput() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getCurrentFocus()
                        .getApplicationWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void groupBC() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        IntentFilter filter = new IntentFilter();// 构造过滤器对象
        filter.addAction("com.abcs.mybc.action.group");
        registerReceiver(receiver, filter);
    }
}
