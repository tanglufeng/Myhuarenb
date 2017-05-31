package com.abcs.huaqiaobang.ytbt.im;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.ytbt.adapter.ConversationAdapter;
import com.abcs.huaqiaobang.ytbt.adapter.TopConversationAdapter;
import com.abcs.huaqiaobang.ytbt.bean.ConversationBean;
import com.abcs.huaqiaobang.ytbt.bean.GroupBean;
import com.abcs.huaqiaobang.ytbt.bean.MsgBean;
import com.abcs.huaqiaobang.ytbt.bean.TopConversationBean;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.call.CallActivity;
import com.abcs.huaqiaobang.ytbt.chats.ChattingActivity;
import com.abcs.huaqiaobang.ytbt.chats.group.CreateGroupActivity;
import com.abcs.huaqiaobang.ytbt.chats.group.SearchActivity;
import com.abcs.huaqiaobang.ytbt.im.sdkhelper.SDKCoreHelper;
import com.abcs.huaqiaobang.ytbt.util.GlobalConstant;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.abcs.huaqiaobang.ytbt.voicemeeting.CreateMeetingActivity;
import com.abcs.huaqiaobang.ytbt.voicemeeting.InviteContactActivity;
import com.abcs.huaqiaobang.ytbt.voicemeeting.MeetingListActivity;
import com.abcs.mining.app.zxing.MipcaActivityCapture;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConversationFragment extends Fragment implements
        OnItemClickListener, OnClickListener {
    Button friend;
    Button group;
    ImageView search, unread;
    Button meetting;
    boolean addFriendRequsrt = false;
    private PopupWindow popupWindow;
    public static final int REQUEST_CONTACTS = 999;
    public static final int REQUEST_CODE_CREATE = 0x003;
    public static final int CREATE_GROUP = 200;
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
    @SuppressLint("HandlerLeak")
    private Handler myhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {

                topConversationAdapter.notifyDataSetChanged();

            }
            if (msg.what == 2) {

                // Toast.makeText(SettingActivity.this, "上传头像成功！",
                // Toast.LENGTH_SHORT).show();
            }
        }
    };
    private TopConversationBean topConversationBean;

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
        getActivity().registerReceiver(broadcast, new IntentFilter(GlobalConstant.ACTION_READ_ADDFRIEND));
        initView();
        
        return view;
    }

   

    private void initView() {
		((ScrollView)view.findViewById(R.id.sc)).smoothScrollTo(0,20);
        search = (ImageView) view.findViewById(R.id.search);
        unread = (ImageView) view.findViewById(R.id.unread);
        search.setOnClickListener(this);
        view.findViewById(R.id.back).setOnClickListener(this);
        view.findViewById(R.id.more).setOnClickListener(this);
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
        toplistview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                Intent intent = new Intent(getActivity(),
                        ChattingActivity.class);
                Bundle bundle = new Bundle();

                try {
                    if (topconversationBeans.get(position).isIsgroup()) {
                        GroupBean group;
                        group = MyApplication.dbUtils.findById(GroupBean.class,
                                topconversationBeans.get(position).getMsgto());
                        User user = new User();
                        user.setVoipAccout(group.getGroupId());
                        user.setNickname(group.getGroupName());
                        bundle.putSerializable("friend", user);
                        isgroup = true;
                        intent.putExtras(bundle);
                        intent.putExtra("isgroup", isgroup);
                        intent.putExtra("group", group);
                        startActivityForResult(intent, 200);
                    } else {
                        User user = MyApplication.dbUtils.findById(User.class,
                                topconversationBeans.get(position).getMsgto());
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
        toplistview.setOnItemLongClickListener(new OnItemLongClickListener() {

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
        listview.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                CharSequence[] items = {"置顶聊天", "删除聊天"};
                showDialog(position,
                        conversationBeans.get(position).getMsgto(), items);
                return true;
            }
        });
        listview.setOnItemClickListener(ConversationFragment.this);
        loadData();
        receiver = new BroadcastReceiver() {
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

                        }
                    }
                    if (topflag) {
                        // MyApplication.getInstance();
                        MyApplication.dbUtils.saveOrUpdate(Tool
                                .toTopconversation(conversationBean,
                                        topConversationBean));
                        topflag = false;
                    } else {
                        // MyApplication.getInstance();
                        MyApplication.dbUtils.saveOrUpdate(conversationBean);
                    }
                   
                    // MyApplication.getInstance();
					topconversationBeans = MyApplication.dbUtils
                            .findAll(TopConversationBean.class);
                    topConversationAdapter.setList(topconversationBeans);
                    // MyApplication.getInstance();

					conversationBeans = MyApplication.dbUtils.findAll(Selector
                            .from(ConversationBean.class).orderBy(
                                    "msglasttime", true));
                    adapter.setList(conversationBeans);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        };
        IntentFilter filter = new IntentFilter();// 构造过滤器对象
        filter.addAction(SDKCoreHelper.action);
        getActivity().registerReceiver(receiver, filter);
        updatereceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    MyApplication.getInstance();
                    topconversationBeans = MyApplication.dbUtils
                            .findAll(TopConversationBean.class);
                    topConversationAdapter.setList(topconversationBeans);
                    MyApplication.getInstance();
                    conversationBeans = MyApplication.dbUtils.findAll(Selector
                            .from(ConversationBean.class).orderBy(
                                    "msglasttime", true));
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

    private void loadData() {
        try {
            MyApplication.dbUtils.createTableIfNotExist(ConversationBean.class);
			MyApplication.dbUtils
					.createTableIfNotExist(TopConversationBean.class);
            topconversationBeans.clear();
            topconversationBeans.addAll(MyApplication.dbUtils
                    .findAll(TopConversationBean.class));
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
            conversationBeans.clear();
            conversationBeans.addAll(list);
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
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(receiver);
        getActivity().unregisterReceiver(updatereceiver);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        Intent intent = new Intent(getActivity(), ChattingActivity.class);
        Bundle bundle = new Bundle();
        ConversationBean c = conversationBeans.get(position);
        try {
            if (c.isIsgroup()) {
//                GroupBean group;
                MyApplication.getInstance();
				GroupBean group = MyApplication.dbUtils.findById(GroupBean.class,
                        c.getMsgto());
                User user = new User();
				if (group == null) {
					user.setVoipAccout(c.getMsgto());
					user.setNickname(c.getMsgto());
				} else {
					user.setVoipAccout(group.getGroupId());
					user.setNickname(group.getGroupName());
					intent.putExtra("group", group);
				}
				bundle.putSerializable("friend", user);
				isgroup = true;
				intent.putExtras(bundle);
				intent.putExtra("isgroup", isgroup);
				startActivityForResult(intent, 200);
			} else {
				MyApplication.getInstance();
				User user = MyApplication.dbUtils.findById(User.class,
						c.getMsgto());
				if (user == null) {
					user = new User();
					try {
						JSONObject obj = new JSONObject(c.getMsgfrom());
						user.setAvatar(obj.optString("avatar"));
						user.setNickname(obj.getString("nickname"));
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
                Intent intent = new Intent(getActivity(), FriendsActivity.class);
                intent.putExtra("addFriendRequsrt", addFriendRequsrt);
                startActivity(intent);
                break;
            case R.id.relativegroup:
                Intent intent1 = new Intent(getActivity(), GroupActivity.class);
                startActivity(intent1);
                break;
            case R.id.relativemeetting:
                Intent intent2 = new Intent(getActivity(),
                        MeetingListActivity.class);
                startActivity(intent2);
                break;
            case R.id.more:
                showPopupWindow(v);
                break;
            case R.id.back:
                // exit();
//			startActivity(new Intent(getActivity(), SettingActivity.class));
                getActivity().finish();
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
        popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, true);
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
        popupWindow.setTouchInterceptor(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getActivity().getWindow()
                        .getAttributes();
                params.alpha = 1f;
                getActivity().getWindow().setAttributes(params);
            }
        });
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
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
    }

    class AddFriendBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            unread.setVisibility(View.GONE);
            addFriendRequsrt = false;
        }
    }
}
