package com.abcs.huaqiaobang.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.chart.ChartActivity;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.huaqiaobang.ytbt.adapter.ConversationAdapter;
import com.abcs.huaqiaobang.ytbt.adapter.TopConversationAdapter;
import com.abcs.huaqiaobang.ytbt.bean.AddFriendRequestBean;
import com.abcs.huaqiaobang.ytbt.bean.ConversationBean;
import com.abcs.huaqiaobang.ytbt.bean.GroupBean;
import com.abcs.huaqiaobang.ytbt.bean.GroupMemberBean;
import com.abcs.huaqiaobang.ytbt.bean.MsgBean;
import com.abcs.huaqiaobang.ytbt.bean.TopConversationBean;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.call.CallActivity;
import com.abcs.huaqiaobang.ytbt.chats.ChattingActivity;
import com.abcs.huaqiaobang.ytbt.chats.group.CreateGroupActivity;
import com.abcs.huaqiaobang.ytbt.chats.group.SearchActivity;
import com.abcs.huaqiaobang.ytbt.im.FriendsActivity;
import com.abcs.huaqiaobang.ytbt.im.GroupActivity;
import com.abcs.huaqiaobang.ytbt.im.MyListView;
import com.abcs.huaqiaobang.ytbt.im.sdkhelper.SDKCoreHelper;
import com.abcs.huaqiaobang.ytbt.settings.SettingActivity;
import com.abcs.huaqiaobang.ytbt.util.GlobalConstant;
import com.abcs.huaqiaobang.ytbt.util.JsonUtil;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.abcs.huaqiaobang.ytbt.voicemeeting.CreateMeetingActivity;
import com.abcs.huaqiaobang.ytbt.voicemeeting.InviteContactActivity;
import com.abcs.huaqiaobang.ytbt.voicemeeting.MeetingListActivity;
import com.abcs.mining.app.zxing.MipcaActivityCapture;
import com.abcs.sociax.android.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yuntongxun.ecsdk.im.group.ECDismissGroupMsg;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;
import com.yuntongxun.ecsdk.im.group.ECInviterMsg;
import com.yuntongxun.ecsdk.im.group.ECJoinGroupMsg;
import com.yuntongxun.ecsdk.im.group.ECProposerMsg;
import com.yuntongxun.ecsdk.im.group.ECQuitGroupMsg;
import com.yuntongxun.ecsdk.im.group.ECRemoveMemberMsg;
import com.yuntongxun.ecsdk.im.group.ECReplyInviteGroupMsg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/3/3.
 */
public class SheQuFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {


    Button friend;
    Button group;
    ImageView search, unread;
    Button meetting;
    boolean addFriendRequsrt = false;
    private PopupWindow popupWindow;
    private BroadcastReceiver receiver, updatereceiver;
    private ConversationAdapter adapter;
    private View view;
    private MyListView listview, toplistview;
    ArrayList<MsgBean> msgbeanlist = new ArrayList<>();
    private Boolean isgroup;
    private AddFriendBroadcast broadcast = new AddFriendBroadcast();
    private List<ConversationBean> conversationBeans = new ArrayList<ConversationBean>();
    private List<TopConversationBean> topconversationBeans = new ArrayList<TopConversationBean>();
    private RelativeLayout relativefriend, relativegroup, relativemeetting;
    private TopConversationAdapter topConversationAdapter;
    private Boolean topflag = false;
    String groupid, members;
    GroupMemberBean member;
    ArrayList<User> userlist = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    private Handler myhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            MainActivity activity = mActivity.get();
            if (getActivity() != null) {
                switch (msg.what) {
                    case 0:
                        Toast.makeText(getActivity(), "加入群组", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        userlist.clear();
                        User user = (User) msg.obj;
                        saveNotice(user.getNickname() + "加入群组");
                        try {
                            MyApplication.dbUtils.createTableIfNotExist(GroupMemberBean.class);
                            member = MyApplication.dbUtils.findById(GroupMemberBean.class, groupid);
                            if (member != null) {
                                try {
                                    userlist = JsonUtil.parseString(member.getMembers());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            userlist.add(user);
                            GroupMemberBean member1 = new GroupMemberBean();
                            member1.setGroupid(groupid);
                            member1.setMembers(JsonUtil.toString(userlist));
                            MyApplication.dbUtils.saveOrUpdate(member1);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        User u = (User) msg.obj;
                        if (!u.getVoipAccout().equals(MyApplication.getInstance().getUserBean().getVoipAccount())) {
                            saveNotice(((User) msg.obj).getNickname() + "退出群组");
                            try {
                                member = MyApplication.dbUtils.findById(GroupMemberBean.class, groupid);
                                if (member != null) {
                                    try {
                                        userlist = JsonUtil.parseString(member.getMembers());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                userlist.remove(u);
                                GroupMemberBean member1 = new GroupMemberBean();
                                member1.setGroupid(groupid);
                                member1.setMembers(JsonUtil.toString(userlist));
                                MyApplication.dbUtils.saveOrUpdate(member1);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }
            }
        }
    };
    private TopConversationBean topConversationBean;
    private String name;
    public static final int REQUEST_CONTACTS = 999;
    public static final int REQUEST_CODE_CREATE = 0x003;
    public static final int CREATE_GROUP = 200;
    private final int GROUP_QUIT = 1;
    private final int GROUP_JOIN = 2;
    private BroadcastReceiver msgreceiver;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(
                R.layout.activity_chats_inbox, null);
        try {
            MyApplication.dbUtils
                    .createTableIfNotExist(TopConversationBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.abcs.huaqiaobang.shequ.refresh");
        filter.addAction(GlobalConstant.ACTION_READ_ADDFRIEND);
        filter.addAction("action_con_unread");
        getActivity().registerReceiver(broadcast, filter);
        initView();
        groupNotice();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            window = getWindow();
            view.findViewById(R.id.statusbar).setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void getAllFriends() {
        Tool.showProgressDialog(getContext(), "正在加载...", false);
        HttpUtils httpUtils = new HttpUtils(60000);

//        HttpRequest.sendGet(TLUrl.getInstance().URL_GET_VOIP
//                + "User/findfriendUser?uid="
//                + MyApplication.getInstance().self.getId() + "&page=1"
//                + "&size=1000", null, new HttpRevMsg() {
//            @Override
//            public void revMsg(String msg) {
//                Tool.removeProgressDialog();
//                MyApplication.users = new ArrayList<User>();
//                MyApplication.friends = new ConcurrentHashMap<>();
//                try {
//                    JSONObject jsonObject = new JSONObject(msg);
//                    if (jsonObject.getInt("status") == 1) {
//                        JSONArray jsonArray = jsonObject.getJSONArray("msg");
//                        if (jsonArray.length() == 0) {
//                            return;
//                        }
//                        // List<String> nums = new ArrayList<String>();
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject object = jsonArray.getJSONObject(i);
//                            User user = new User();
//                            user.setVoipAccout(object.getString("voipAccount"));
//                            user.setNickname(object.getString("nickname"));
//                            user.setUid(object.getInt("frienduid"));
//                            user.setAvatar(object.getString("avatar"));
//                            user.setRemark(object.getString("remarks"));
//                            MyApplication.users.add(user);
//                            if (user.getRemark().trim().equals("")) {
//                                MyApplication.friends.put(user.getNickname(),
//                                        user);
//                            } else {
//                                MyApplication.friends.put(user.getRemark(),
//                                        user);
//                            }
//                        }
//                        MyApplication.dbUtils
//                                .saveOrUpdateAll(MyApplication.users);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    MyApplication.users = null;
//                    MyApplication.friends = null;
//                }
//            }
//
//        });
//    }
        httpUtils.send(HttpRequest.HttpMethod.GET, TLUrl.URL_GET_VOIP
                + "User/findfriendUser?uid="
                + MyApplication.getInstance().getUid() + "&page=1"
                + "&size=1000", new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Tool.removeProgressDialog();
                Tool.showInfo(getContext(), "网络异常,加载好友失败");
                MyApplication.users = new ArrayList<>();
                try {
                    MyApplication.users.addAll(MyApplication.dbUtils
                            .findAll(User.class));
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                Tool.removeProgressDialog();
                MyApplication.users = new ArrayList<User>();
                MyApplication.friends = new ConcurrentHashMap<>();
                try {
                    JSONObject jsonObject = new JSONObject(arg0.result);
                    if (jsonObject.getInt("status") == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("msg");
                        if (jsonArray.length() == 0) {
                            return;
                        }
                        // List<String> nums = new ArrayList<String>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            User user = new User();
                            user.setVoipAccout(object.getString("voipAccount"));
                            user.setNickname(object.getString("nickname"));
                            user.setUid(object.getInt("frienduid"));
                            user.setAvatar(object.getString("avatar"));
                            user.setRemark(object.getString("remarks"));
                            MyApplication.users.add(user);
                            if (user.getRemark().trim().equals("")) {
                                MyApplication.friends.put(user.getNickname(),
                                        user);
                            } else {
                                MyApplication.friends.put(user.getRemark(),
                                        user);
                            }
                        }
                        MyApplication.dbUtils
                                .saveOrUpdateAll(MyApplication.users);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    MyApplication.users = null;
                    MyApplication.friends = null;
                }
            }
        });
    }

    private void addFriendsInfo() {
        HttpUtils httpUtils = new HttpUtils(60000);
//        HttpRequest.sendGet(TLUrl.getInstance().URL_GET_VOIP
//                + "User/pollingfriendUser?frienduid="
//                + MyApplication.getInstance().getUid(), null, new HttpRevMsg() {
//            @Override
//            public void revMsg(String msg) {
//                if (msg.length() <= 0) {
//                    return;
//                }
//                try {
//                    JSONObject jsonObject = new JSONObject(msg);
//                    Log.i("info", msg);
//                    if (jsonObject.getInt("status") == 1) {
//                        JSONArray jsonArray = jsonObject
//                                .getJSONArray("msg");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject obj = jsonArray.getJSONObject(i);
//                            AddFriendRequestBean bean = new AddFriendRequestBean();
//                            bean.setId(obj.optString("id"));
//                            bean.setNickname(obj.optString("nickname"));
//                            bean.setUid(obj.optString("uid"));
//                            bean.setAvadar(obj.optString("avatar"));
//                            bean.setVoip(obj.optString("voipAccount"));
//                            bean.setTime(System.currentTimeMillis());
//                            bean.setState(0);
//                            if (MyApplication.requests.contains(bean)) {
//                                int position = MyApplication.requests
//                                        .indexOf(bean);
//                                if (MyApplication.requests.get(
//                                        MyApplication.requests
//                                                .indexOf(bean))
//                                        .getState() != 0
//                                        || !MyApplication.requests.get(
//                                        position).equals(
//                                        bean.getId())) {
//                                    MyApplication.requests
//                                            .remove(position);
//                                } else {
//                                    continue;
//                                }
//                            }
//                            MyApplication.requests.add(bean);
//                            SDKCoreHelper.getInstance().alertMag();
////                                    conversationFragment.unread
////                                            .setVisibility(View.VISIBLE);
////                                    conversationFragment.addFriendRequsrt = true;
//                        }
//                        MyApplication.dbUtils
//                                .saveOrUpdateAll(MyApplication.requests);
//                        // Message message = myhandler.obtainMessage(3,
//                        // msg);
//                        // myhandler.sendMessage(message);
//                        // isagete = false;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        httpUtils.send(HttpRequest.HttpMethod.GET, TLUrl.URL_GET_VOIP
                        + "User/pollingfriendUser?frienduid="
                        + MyApplication.getInstance().getUid(),
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        if (arg0.result.length() <= 0) {
                            return;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(arg0.result);
                            Log.i("info", arg0.result);
                            if (jsonObject.getInt("status") == 1) {
                                JSONArray jsonArray = jsonObject
                                        .getJSONArray("msg");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    AddFriendRequestBean bean = new AddFriendRequestBean();
                                    bean.setId(obj.optString("id"));
                                    bean.setNickname(obj.optString("nickname"));
                                    bean.setUid(obj.optString("uid"));
                                    bean.setAvadar(obj.optString("avatar"));
                                    bean.setVoip(obj.optString("voipAccount"));
                                    bean.setTime(System.currentTimeMillis());
                                    bean.setState(0);
                                    if (MyApplication.requests.contains(bean)) {
                                        int position = MyApplication.requests
                                                .indexOf(bean);
                                        if (MyApplication.requests.get(
                                                MyApplication.requests
                                                        .indexOf(bean))
                                                .getState() != 0
                                                || !MyApplication.requests.get(
                                                position).equals(
                                                bean.getId())) {
                                            MyApplication.requests
                                                    .remove(position);
                                        } else {
                                            continue;
                                        }
                                    }
                                    MyApplication.requests.add(bean);
                                    SDKCoreHelper.getInstance().alertMag(false);
                                    unread
                                            .setVisibility(View.VISIBLE);
                                    addFriendRequsrt = true;
                                }
                                MyApplication.dbUtils
                                        .saveOrUpdateAll(MyApplication.requests);
                                // Message message = myhandler.obtainMessage(3,
                                // msg);
                                // myhandler.sendMessage(message);
                                // isagete = false;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initData() {


    }

    private void initView() {

        if (NetworkUtils.isNetAvailable(getContext())) {
            getAllFriends();
            addFriendsInfo();
        }
        ((ScrollView) view.findViewById(R.id.sc)).smoothScrollTo(0, 20);
        view.findViewById(R.id.huarentang).setOnClickListener(this);
        search = (ImageView) view.findViewById(R.id.search);
        unread = (ImageView) view.findViewById(R.id.unread);
        search.setOnClickListener(this);
        view.findViewById(R.id.back).setOnClickListener(this);
        view.findViewById(R.id.more).setOnClickListener(this);
//        view.findViewById(R.id.djl).setOnClickListener(this);
//        view.findViewById(R.id.kefu).setOnClickListener(this);
//		view.findViewById(R.id.test).setOnClickListener(this);//测试
        relativefriend = (RelativeLayout) view
                .findViewById(R.id.relativefriend);
        relativegroup = (RelativeLayout) view.findViewById(R.id.relativegroup);
        relativemeetting = (RelativeLayout) view
                .findViewById(R.id.relativemeetting);
        view.findViewById(R.id.relativeqz).setOnClickListener(this);
        relativefriend.setOnClickListener(this);
        relativegroup.setOnClickListener(this);
        relativemeetting.setOnClickListener(this);
        toplistview = (MyListView) view.findViewById(R.id.toplist);
        toplistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                Intent intent = new Intent(getActivity(),
                        ChattingActivity.class);
                Bundle bundle = new Bundle();
                TopConversationBean c = topconversationBeans.get(position);
                try {
                    if (c.isIsgroup()) {
                        GroupBean group = MyApplication.dbUtils.findById(
                                GroupBean.class, c.getMsgto());
                        User user = new User();
                        if (group == null) {
                            try {
                                JSONObject obj = new JSONObject(c.getMsgfrom());
                                user.setAvatar(obj.optString("avatar"));
                                user.setNickname(obj.optString("nickname"));
                                user.setVoipAccout(c.getMsgto());
                                user.setRemark("");
                                user.setUid(0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            user.setVoipAccout(group.getGroupId());
                            user.setNickname(group.getGroupName());
                            user.setAvatar(group.getGroupAvatar());
                            intent.putExtra("group", group);
                        }
                        bundle.putSerializable("friend", user);
                        isgroup = true;
                        intent.putExtras(bundle);
                        intent.putExtra("isgroup", isgroup);
                        startActivityForResult(intent, 200);
                    } else {
                        User user = MyApplication.dbUtils.findById(User.class,
                                c.getMsgto());
                        if (user == null) {
                            user = new User();
                            try {
                                JSONObject obj = new JSONObject(c.getMsgfrom());
                                user.setAvatar(obj.optString("avatar"));
                                user.setNickname(obj.optString("nickname"));
                                user.setRemark("");
                                user.setVoipAccout(c.getMsgto());
                                user.setUid(Integer.parseInt(obj
                                        .getString("uid")));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        bundle.putSerializable("friend", user);
                        intent.putExtras(bundle);
                        isgroup = false;
                        intent.putExtra("isgroup", isgroup);
                        startActivityForResult(intent, 200);
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }

            }
        });
        toplistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                CharSequence[] items = {"取消置顶", "删除该聊天"};
                showDialog(position, topconversationBeans.get(position)
                        .getMsgto(), items);
                return true;
            }
        });

        listview = (MyListView) view.findViewById(R.id.list);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                CharSequence[] items = {"置顶聊天", "删除聊天"};
                showDialog(position,
                        conversationBeans.get(position).getMsgto(), items);
                return true;
            }
        });
        listview.setOnItemClickListener(this);
        loadData();
        msgreceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    ConversationBean conversationBean = (ConversationBean) intent
                            .getSerializableExtra("conversation");
                    for (int a = 0; a < topconversationBeans.size(); a++) {
                        if (topconversationBeans
                                .get(a)
                                .getConversationpeople()
                                .equals(conversationBean
                                        .getConversationpeople())) {
                            topflag = true;
                            conversationBean.setUnread(topconversationBeans
                                    .get(a).getUnread() + 1);
                        }
                    }
                    if (topflag) {
                        MyApplication.dbUtils.saveOrUpdate(Tool
                                .toTopconversation(conversationBean,
                                        topConversationBean));
                        topflag = false;
                    } else {
                        boolean i = false;
                        for (ConversationBean c : conversationBeans) {
                            if (c.getConversationpeople().equals(
                                    conversationBean.getConversationpeople())) {
                                conversationBean.setUnread(c.getUnread() + 1);
                                i = true;
                                break;
                            }
                        }
                        if (!i)
                            conversationBean.setUnread(1);
                        MyApplication.dbUtils.saveOrUpdate(conversationBean);
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                } finally {
                    loadData();
                }
            }
        };
        IntentFilter filter = new IntentFilter();// 构造过滤器对象
        filter.addAction(SDKCoreHelper.action);
        filter.setPriority(200);
        getActivity().registerReceiver(msgreceiver, filter);
        updatereceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    topconversationBeans = MyApplication.dbUtils
                            .findAll(Selector.from(ConversationBean.class)
                                    .orderBy("msglasttime", true));
                    if (topconversationBeans != null)
                        topConversationAdapter.setList(topconversationBeans);
                    conversationBeans = MyApplication.dbUtils.findAll(Selector
                            .from(ConversationBean.class).orderBy(
                                    "msglasttime", true));
                    if (conversationBeans != null)
                        adapter.setList(conversationBeans);
                } catch (DbException e) {
                    e.printStackTrace();
                }

            }
        };
        IntentFilter filter2 = new IntentFilter();// 构造过滤器对象
        filter2.addAction("updateconversation");
        getActivity().registerReceiver(updatereceiver, filter2);
    }

    // @Override
    // public void onResume() {
    // super.onResume();
    // loadData();
    // }

    private void loadData() {
        try {
            List<TopConversationBean> l = (MyApplication.dbUtils
                    .findAll(Selector.from(TopConversationBean.class).orderBy(
                            "msglasttime", true)));
            if (l != null) {
                topconversationBeans.clear();
                topconversationBeans.addAll(l);
            }
            MyApplication.getInstance().setTopConversationBeans(
                    topconversationBeans);
            if (topConversationAdapter == null) {
                topConversationAdapter = new TopConversationAdapter(
                        getActivity(), topconversationBeans);
                toplistview.setAdapter(topConversationAdapter);
            } else {
                topConversationAdapter.notifyDataSetChanged();
            }
            List<ConversationBean> list = (MyApplication.dbUtils
                    .findAll(Selector.from(ConversationBean.class).orderBy(
                            "msglasttime", true)));
            if (list != null) {
                conversationBeans.clear();
                conversationBeans.addAll(list);
            }
            if (adapter == null) {
                adapter = new ConversationAdapter(getActivity(),
                        conversationBeans);
                listview.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(receiver);
        getActivity().unregisterReceiver(msgreceiver);
        getActivity().unregisterReceiver(updatereceiver);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        Intent intent = new Intent(getActivity(), ChattingActivity.class);
        Bundle bundle = new Bundle();
        ConversationBean c = conversationBeans.get(position);
        try {
            if ("群组已被解散".equals(c.getMsg()))
                return;
            if (c.isIsgroup()) {
                GroupBean group = MyApplication.dbUtils.findById(
                        GroupBean.class, c.getMsgto());
                User user = new User();
                if (group == null) {
                    try {
                        JSONObject obj = new JSONObject(c.getMsgfrom());
                        user.setAvatar(obj.optString("avatar"));
                        user.setNickname(obj.optString("nickname"));
                        user.setVoipAccout(c.getMsgto());
                        user.setRemark("");
                        user.setUid(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    user.setVoipAccout(group.getGroupId());
                    user.setNickname(group.getGroupName());
                    user.setAvatar(group.getGroupAvatar());
                    intent.putExtra("group", group);
                }
                bundle.putSerializable("friend", user);
                isgroup = true;
                intent.putExtras(bundle);
                intent.putExtra("isgroup", isgroup);
                startActivityForResult(intent, 200);
            } else {
                User user = MyApplication.dbUtils.findById(User.class,
                        c.getMsgto());
                if (user == null) {
                    user = new User();
                    try {
                        JSONObject obj = new JSONObject(c.getMsgfrom());
                        user.setAvatar(obj.optString("avatar"));
                        user.setNickname(obj.optString("nickname"));
                        user.setRemark("");
                        user.setVoipAccout(c.getMsgto());
                        user.setUid(Integer.parseInt(obj.getString("uid")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                bundle.putSerializable("friend", user);
                intent.putExtras(bundle);
                isgroup = false;
                intent.putExtra("isgroup", isgroup);
                startActivityForResult(intent, 200);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativefriend:

                if (Util.preference.getBoolean("islogin", false)) {
                    Intent intent = new Intent(getActivity(), FriendsActivity.class);
                    intent.putExtra("addFriendRequsrt", addFriendRequsrt);
                    startActivity(intent);
                } else {
                    Intent intent1 = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent1);
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.relativegroup:
                if (Util.preference.getBoolean("islogin", false)) {
                    Intent intent1 = new Intent(getActivity(), GroupActivity.class);
                    startActivity(intent1);
                } else {
                    Intent intent1 = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent1);
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.relativemeetting:
                Intent intent2 = new Intent(getActivity(),
                        MeetingListActivity.class);
                startActivity(intent2);
                break;
            case R.id.relativeqz:
                if (Util.preference.getBoolean("islogin", false)) {
                    Intent intent3 = new Intent(getActivity(),
                            GroupActivity.class);
                    intent3.putExtra("isQuanzi", true);
                    startActivity(intent3);
                } else {
                    Intent intent1 = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent1);
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.more:
                showPopupWindow(v);
                break;
            case R.id.back:
                // exit();
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.btn_DIRECT:
                startActivityForResult(new Intent(getActivity(),
                        InviteContactActivity.class), REQUEST_CONTACTS);
                closePopupWindow();
                break;
            case R.id.addfriends:
                // addFriendDialog();
                popupWindow.dismiss();
                break;

            case R.id.btn_Meeting:
                startActivityForResult(new Intent(getActivity(),
                        CreateMeetingActivity.class), REQUEST_CODE_CREATE);
                popupWindow.dismiss();
                break;
            case R.id.creategroup:
                startActivityForResult(new Intent(getActivity(),
                        CreateGroupActivity.class), CREATE_GROUP);
                popupWindow.dismiss();
                break;
            case R.id.huarentang:
                if (Util.preference.getBoolean("islogin", false)) {
                    Intent intent1 = new Intent(getActivity(), ChartActivity.class);
//                    intent1.putExtra("title", "问答");
                    startActivity(intent1);
                } else {
                    Intent intent1 = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent1);

                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;
//		case R.id.test://测试
//			HttpUtils httpUtils =new HttpUtils();
//			RequestParams params = new RequestParams();
//			params.addBodyParameter("uid", MyApplication.getInstance().getUid()+"");
//			params.addBodyParameter("maxmember", "100");
//			params.addBodyParameter("passwd", "");
//			params.addBodyParameter("confduration", "0");
//			params.addBodyParameter("autohangup", "false");
//			params.addBodyParameter("confendprompt", "false");
//			params.addBodyParameter("autorecord", "false");
//			params.addBodyParameter("autojoin", "false");
//			httpUtils.send(HttpMethod.POST, TLUrl.getInstance().URL_GET_VOIP+"conf/createconf",params, new RequestCallBack<String>() {
//				@Override
//				public void onFailure(HttpException arg0, String arg1) {
//					Log.i("info", arg1);
//				}
//				@Override
//				public void onSuccess(ResponseInfo<String> arg0) {
//					Log.i("info", arg0.result);
//				}
//			});
//			break;
            case R.id.search:
                startActivityForResult(new Intent(getActivity(),
                        SearchActivity.class), CREATE_GROUP);
            case R.id.QR_Code:
                startActivity(new Intent(getActivity(), MipcaActivityCapture.class));
                closePopupWindow();
                break;
        }
    }

    private void showPopupWindow(View view) {
        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.menu_more, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        WindowManager.LayoutParams params = getActivity().getWindow()
                .getAttributes();
        params.alpha = 0.8f;
        getActivity().getWindow().setAttributes(params);
        contentView.findViewById(R.id.btn_DIRECT).setOnClickListener(this);
        // contentView.findViewById(R.id.addfriends).setOnClickListener(this);
        // contentView.findViewById(R.id.btn_Meeting).setOnClickListener(this);
        // contentView.findViewById(R.id.creategroup).setOnClickListener(this);
        // contentView.findViewById(R.id.searchgroup).setOnClickListener(this);
        contentView.findViewById(R.id.QR_Code).setOnClickListener(this);
        // contentView.findViewById(R.id.refreshfriends).setOnClickListener(
        // new OnClickListener() {
        // @Override
        // public void onClick(View v) {
        // popupWindow.dismiss();
        // // listfriendsfragment.getAllFriends();
        //
        // }
        // });
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getActivity().getWindow()
                        .getAttributes();
                params.alpha = 1f;
                getActivity().getWindow().setAttributes(params);
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAsDropDown(view);
    }

    private void closePopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == MeetingListFragment.REQUEST_CODE_CREATE) {
//			// meettingFragment.onActivityResult(requestCode, resultCode, data);
//		}
        if (requestCode == CREATE_GROUP && resultCode == 200) {
            // groupFragment.refreshGroup();
        }
        if (requestCode == REQUEST_CONTACTS && resultCode == 1) {
            String num = data.getStringExtra("num");
            if (num != null) {
                Intent intent = new Intent(getActivity(), CallActivity.class);
                intent.putExtra("con.yuntongxun.ecdemo.VoIP_OUTGOING_CALL",
                        true);
                intent.putExtra("callType", GlobalConstant.Call_TYPE_DIRECT);
                intent.putExtra("num", num);
                intent.putExtra("name", data.getStringExtra("name"));
                startActivity(intent);
            }
        }
    }

    public void showDialog(final int position, String name,
                           CharSequence[] items2) {
        final CharSequence[] items = {items2[0], items2[1]};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("置顶聊天")) {
                    topConversation(position);

                } else if (items[item].equals("取消置顶")) {
                    restoreConversation(position);

                } else if (items[item].equals("删除聊天")) {
                    delConversation(position, 1);

                } else if (items[item].equals("删除该聊天")) {
                    delConversation(position, 2);
                }
            }
        });
        builder.show();
    }

    protected void restoreConversation(int position) {
        ConversationBean conversationBean = new ConversationBean();
        conversationBean.setConversationpeople(topconversationBeans.get(
                position).getConversationpeople());
        conversationBean.setIsgroup(topconversationBeans.get(position)
                .isIsgroup());
        conversationBean.setMsg(topconversationBeans.get(position).getMsg());
        // conversationBean.setMsgfrom(topconversationBeans.get(position)
        // .getMsgfrom());
        conversationBean
                .setMsgto(topconversationBeans.get(position).getMsgto());
        conversationBean.setMsglasttime(topconversationBeans.get(position)
                .getMsglasttime());
        try {
            MyApplication.dbUtils.delete(topconversationBeans.get(position));
            MyApplication.dbUtils.saveOrUpdate(conversationBean);
            topconversationBeans = MyApplication.dbUtils
                    .findAll(TopConversationBean.class);
            MyApplication.getInstance();
            conversationBeans = MyApplication.dbUtils.findAll(Selector.from(
                    ConversationBean.class).orderBy("msglasttime", true));
            topConversationAdapter.setList(topconversationBeans);
            adapter.setList(conversationBeans);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    protected void delConversation(int position, int i) {
        try {
            if (i == 1) {
                ConversationBean c = conversationBeans.get(position);
                MyApplication.dbUtils.delete(MsgBean.class,
                        WhereBuilder.b("mgsTo", "=", c.getMsgfrom()));
                MyApplication.dbUtils.delete(MsgBean.class,
                        WhereBuilder.b("mgsTo", "=", c.getMsgto()));
                MyApplication.dbUtils.delete(MsgBean.class,
                        WhereBuilder.b("msgfrom", "=", c.getMsgfrom()));
                MyApplication.dbUtils.delete(MsgBean.class,
                        WhereBuilder.b("msgfrom", "=", c.getMsgto()));
                MyApplication.dbUtils.delete(c);
                conversationBeans = MyApplication.dbUtils.findAll(Selector
                        .from(ConversationBean.class).orderBy("msglasttime",
                                true));
                adapter.setList(conversationBeans);

            } else {
                TopConversationBean t = topconversationBeans.get(position);
                MyApplication.dbUtils.delete(MsgBean.class,
                        WhereBuilder.b("mgsTo", "=", t.getMsgfrom()));
                MyApplication.dbUtils.delete(MsgBean.class,
                        WhereBuilder.b("mgsTo", "=", t.getMsgto()));
                MyApplication.dbUtils.delete(MsgBean.class,
                        WhereBuilder.b("msgfrom", "=", t.getMsgfrom()));
                MyApplication.dbUtils.delete(MsgBean.class,
                        WhereBuilder.b("msgfrom", "=", t.getMsgto()));
                MyApplication.dbUtils.delete(t);
                topconversationBeans = MyApplication.dbUtils
                        .findAll(TopConversationBean.class);
                MyApplication.getInstance().setTopConversationBeans(
                        topconversationBeans);
                topConversationAdapter.setList(topconversationBeans);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    protected void topConversation(int position) {
        // TODO Auto-generated method stub

        topConversationBean = Tool.toTopconversation(
                conversationBeans.get(position), topConversationBean);

        try {
            MyApplication.dbUtils.delete(conversationBeans.get(position));
            MyApplication.dbUtils.saveOrUpdate(topConversationBean);
            topconversationBeans = MyApplication.dbUtils
                    .findAll(TopConversationBean.class);
            MyApplication.getInstance();
            conversationBeans = MyApplication.dbUtils.findAll(Selector.from(
                    ConversationBean.class).orderBy("msglasttime", true));
            topConversationAdapter.setList(topconversationBeans);
            adapter.setList(conversationBeans);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcast);
        getActivity().unregisterReceiver(msgreceiver);
    }

    class AddFriendBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {


            switch (intent.getAction()) {
                case "action_con_unread":
                    loadData();
                    break;
                case GlobalConstant.ACTION_READ_ADDFRIEND:
                    unread.setVisibility(View.GONE);
                    addFriendRequsrt = false;
                    break;
                case "com.abcs.huaqiaobang.shequ.refresh":
                    loadData();
                    break;
            }

        }
    }

    private void groupNotice() {
        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case "com.im.group.notice":
                        ECGroupNoticeMessage notice = intent
                                .getParcelableExtra("groupnotice");
                        groupid = notice.getGroupId();
                        name = notice.getGroupName();

                        ECGroupNoticeMessage.ECGroupMessageType type = notice
                                .getType();
                        if (type == ECGroupNoticeMessage.ECGroupMessageType.PROPOSE) {
                            Log.i("info", "ECGroupMessageType.PROPOSE");
                            // 群组收到有人申请加入群组
                            ECProposerMsg proposerMsg = (ECProposerMsg) notice;
                            showDailog(proposerMsg);
                        } else if (type == ECGroupNoticeMessage.ECGroupMessageType.INVITE) {
                            Log.i("info", "ECGroupMessageType.INVITE");
                            // 群组管理员邀请用户加入群组 -
                            ECInviterMsg inviterMsg = (ECInviterMsg) notice;
                            // 处理群组管理员邀请加入群组通知
                            switch (inviterMsg.getConfirm()) {
                                case 0:
                                    break;
                                case 1:
                                    GroupBean groupBean = new GroupBean();
                                    groupBean.setGroupName(inviterMsg.getGroupName());
                                    groupBean.setGroupId(inviterMsg.getGroupId());
                                    groupBean.setGroupOwner(inviterMsg.getNickName());
                                    groupBean.setGroupType(inviterMsg.isDiscuss() ? "0"
                                            : "1");
                                    groupBean.setGroupPermission("0");
                                    groupBean.setGroupAvatar("");
                                    groupBean.setGroupDeclared("declared");
                                    groupBean.setMemberInGroup("");
                                    groupBean.setDateCreate(inviterMsg.getDateCreated()
                                            + "");
                                    Intent intent2 = new Intent("action.group.update");
                                    intent2.putExtra("group", groupBean);
                                    getActivity().sendBroadcast(intent2);
                                    saveNotice("您被邀请进入" + inviterMsg.getGroupName());
                                    GroupMemberBean bean = new GroupMemberBean();
                                    bean.setGroupid(inviterMsg.getGroupId());
                                    bean.setMembers("");
                                    try {
                                        MyApplication.dbUtils.saveOrUpdate(groupBean);
                                        MyApplication.dbUtils.saveOrUpdate(bean);
                                    } catch (DbException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                        } else if (type == ECGroupNoticeMessage.ECGroupMessageType.REMOVE_MEMBER) {
                            Log.i("info", "ECGroupMessageType.REMOVE_MEMBER");
                            // 群组管理员删除成员
                            ECRemoveMemberMsg removeMemberMsg = (ECRemoveMemberMsg) notice;
                            // 处理群组移除成员通知
                            if ("$Smith账号".equals(removeMemberMsg.getMember())) {
                                // 如果是自己则将从本地群组关联关系中移除
                                // 通知UI处理刷新
                            }

                        } else if (type == ECGroupNoticeMessage.ECGroupMessageType.QUIT) {
                            Log.i("info", "ECGroupMessageType.QUIT");
                            // 群组成员主动退出群组
                            ECQuitGroupMsg quitGroupMsg = (ECQuitGroupMsg) notice;
                            // 处理某人退出群组通知
                            handlerGroupMsg(GROUP_QUIT, quitGroupMsg.getGroupId(),
                                    quitGroupMsg.getMember());
                        } else if (type == ECGroupNoticeMessage.ECGroupMessageType.DISMISS) {
                            Log.i("info", "ECGroupMessageType.DISMISS");
                            ECDismissGroupMsg dismissGroupMsg = (ECDismissGroupMsg) notice;
                            // 处理群组被解散通知
                            // 将群组从本地缓存中删除并通知UI刷新
                            // 删除群组（解散群组）
                            onGroupDismiss(dismissGroupMsg);
                        } else if (type == ECGroupNoticeMessage.ECGroupMessageType.JOIN) {
                            ECJoinGroupMsg joinGroupMsg = (ECJoinGroupMsg) notice;
                            Log.i("info", joinGroupMsg.getMember() + "加入群组");
                            handlerGroupMsg(GROUP_JOIN, joinGroupMsg.getGroupId(),
                                    joinGroupMsg.getMember());
                        } else if (type == ECGroupNoticeMessage.ECGroupMessageType.REPLY_INVITE) {
                            // 用户通过或拒绝群组管理员邀请加入群组的邀请
                            Log.i("info", "ECGroupMessageType.REPLY_INVITE");
                            ECReplyInviteGroupMsg replyInviteGroupMsg = (ECReplyInviteGroupMsg) notice;
                            switch (replyInviteGroupMsg.getConfirm()) {
                                case 2:
                                    handlerGroupMsg(GROUP_JOIN, groupid,
                                            replyInviteGroupMsg.getMember());
                                    break;
                                case 1:
                                    break;
                            }
                        } else if (type == ECGroupNoticeMessage.ECGroupMessageType.REPLY_JOIN) {
                            Log.i("info", "ECGroupMessageType.REPLY_JOIN");
                        }
                        break;
                    case GlobalConstant.ACTION_ADDFRIEND_REQUEST:
                        unread.setVisibility(View.VISIBLE);
                        addFriendRequsrt = true;
                        break;
                }
            }
        };
        IntentFilter filter = new IntentFilter();// 构造过滤器对象
        filter.addAction("com.im.group.notice");
        filter.addAction(GlobalConstant.ACTION_ADDFRIEND_REQUEST);
        getActivity().registerReceiver(receiver, filter);
    }

    private void onGroupDismiss(ECDismissGroupMsg groupmsg) {
        if (!groupmsg.getAdmin().equals(MyApplication.getInstance().getUser().getVoipAccout()))
            saveNotice("群组已被解散");
        GroupBean bean = null;
        try {
            bean = MyApplication.dbUtils.findById(GroupBean.class, groupmsg.getGroupId());
            if (bean != null) {
                MyApplication.dbUtils.delete(bean);
            }
            MyApplication.dbUtils.deleteById(GroupMemberBean.class, groupmsg.getGroupId());
        } catch (DbException e) {
            e.printStackTrace();
        } finally {
            Intent intent = new Intent();
            intent.setAction("com.abcs.mybc.action.group");
            intent.putExtra("bc", groupmsg.getGroupId());
            intent.putExtra("group", bean);
            getActivity().sendBroadcast(intent);
        }

    }

    private void handlerGroupMsg(final int type, String groupId,
                                 final String member) {
        com.abcs.huaqiaobang.util.HttpRequest.sendGet(TLUrl.URL_GET_VOIP + "User/findvoipuser",
                "voipAccount=" + member, new HttpRevMsg() {
                    private Message message;

                    @Override
                    public void revMsg(String msg) {
                        Log.i("xbb群组成员保存", msg);
                        if (msg.length() <= 0) {
                            return;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.getInt("status") == 1) {
                                JSONObject object = jsonObject
                                        .getJSONObject("msg");
                                User user = new User();
                                user.setAvatar(object.getString("avatar"));
                                user.setNickname(object.getString("nickname"));
                                user.setUid(object.getInt("uid"));
                                user.setVoipAccout(member);
                                switch (type) {
                                    case GROUP_JOIN:
                                        message = myhandler.obtainMessage(1, user);
                                        break;
                                    case GROUP_QUIT:
                                        message = myhandler.obtainMessage(2, user);
                                        break;
                                }
                                myhandler.sendMessage(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    protected void showDailog(final ECProposerMsg proposerMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // 先得到构造器
        builder.setTitle("群组消息！"); // 设置标题
        builder.setMessage("是否同意" + proposerMsg.getSender() + "加入"
                + proposerMsg.getGroupName() + "申请理由："
                + proposerMsg.getDeclared()); // 设置内容

        builder.setPositiveButton("同意", new DialogInterface.OnClickListener() { // 设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dealWith(proposerMsg, 0);
                dialog.dismiss(); // 关闭dialog
            }
        });
        builder.setNegativeButton("不同意", new DialogInterface.OnClickListener() { // 设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dealWith(proposerMsg, 1);
                dialog.dismiss();

            }
        });
        // 参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    protected void dealWith(ECProposerMsg proposerMsg, int i) {
        com.abcs.huaqiaobang.util.HttpRequest.sendGet(
                TLUrl.URL_GET_VOIP + "member/AskJoin",
                "uid=" + MyApplication.getInstance().getUid() + "&groupId="
                        + proposerMsg.getGroupId() + "&asker="
                        + proposerMsg.getSender() + "&confirm=" + i,
                new HttpRevMsg() {

                    @Override
                    public void revMsg(String msg) {
                        Log.i("xbb管理员处理申请消息", msg);
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.getInt("status") == 1) {
                                myhandler.sendEmptyMessage(0);
                            }
                        } catch (JSONException e) {
                            Log.i("xbb管理员处理申请消息", e.toString());
                            e.printStackTrace();
                        } finally {

                        }
                    }
                });

    }

    private void saveNotice(String notice) {
        ConversationBean conversationBean = new ConversationBean();
        conversationBean.setIsgroup(true);
        // conversationBean.setMsgfrom(MyApplication.getInstance()
        // .getUserBean().getVoipAccount());
        User u = new User();
        u.setNickname(name);
        u.setVoipAccout(groupid);
        u.setAvatar("");
        conversationBean.setMsgto(groupid);
        conversationBean.setMsgfrom(u.toString());
        conversationBean.setMsglasttime(System.currentTimeMillis());
        conversationBean.setConversationpeople(MyApplication.getInstance()
                .getUserBean().getVoipAccount()
                + groupid);
        conversationBean.setMsg(notice);
        MsgBean msgBean = new MsgBean();
        msgBean.setMsgfrom(groupid);
        msgBean.setType("notice");
        msgBean.setMsgtime(System.currentTimeMillis());
        msgBean.setMsg(notice);
        try {
            MyApplication.dbUtils.save(msgBean);
        } catch (DbException e) {
            Log.i("xbbbb", e.toString());
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.setAction("com.robin.mybc.action4");
        intent.putExtra("conversation", conversationBean);
        getActivity().sendBroadcast(intent);
    }
}
