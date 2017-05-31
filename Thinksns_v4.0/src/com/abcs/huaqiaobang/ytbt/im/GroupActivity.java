package com.abcs.huaqiaobang.ytbt.im;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.ytbt.adapter.GroupAdapter;
import com.abcs.huaqiaobang.ytbt.bean.GroupBean;
import com.abcs.huaqiaobang.ytbt.bean.User;
import com.abcs.huaqiaobang.ytbt.chats.ChattingActivity;
import com.abcs.huaqiaobang.ytbt.chats.group.CreateGroupActivity;
import com.abcs.huaqiaobang.ytbt.chats.group.SearchActivity;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends BaseActivity implements OnClickListener {

    private ListView groudlistview;
    public static List<GroupBean> groups = new ArrayList<>();
    private GroupAdapter adapter;
    private Boolean isgroup = true;
    private MyBroadcastReceiver receiver;
    private TextView title;
    private boolean isqz;

    private void reloadData() {
        try {
            groups.clear();
            groups.addAll(MyApplication.dbUtils.findAll(GroupBean.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
        setAdapterData();
    }

    private void setAdapterData() {
        if (adapter == null) {
            adapter = new GroupAdapter(GroupActivity.this, groups, isqz);
            groudlistview.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);
        try {
            MyApplication.dbUtils.createTableIfNotExist(GroupBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        isqz = getIntent().getBooleanExtra("isQuanzi", false);
        ImageView more = (ImageView) findViewById(R.id.more);
        groudlistview = (ListView) findViewById(R.id.groudlistview);
        groudlistview.setEmptyView(findViewById(R.id.empty_tip_recommend_bind_tv));
        more.setOnClickListener(this);
        findViewById(R.id.more).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        // groupBC();
        // reloadData();
        title = (TextView) findViewById(R.id.ownerid);
        if (!isqz) groudlistview.setOnItemClickListener(listener);
        receiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.group.update");
        intentFilter.addAction("com.abcs.mybc.action.group");
        registerReceiver(receiver, intentFilter);
        if (isqz) {
            title.setText("圈子");
            more.setVisibility(View.GONE);
            getAllQZ();
        } else {
            title.setText("群组列表");
            refreshGroup();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    // @Override
    // public void onDestroyView() {
    // super.onDestroyView();
    // GroupActivity.this.unregisterReceiver(receiver);
    // }

    OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int posion,
                                long arg3) {
            Intent intent = new Intent(GroupActivity.this,
                    ChattingActivity.class);
            User user = new User();
            user.setNickname(groups.get(posion).getGroupName());
            user.setVoipAccout(groups.get(posion).getGroupId());
            user.setAvatar(groups.get(posion).getGroupAvatar());
            intent.putExtra("isgroup", isgroup);
            intent.putExtra("group", groups.get(posion));
            Bundle bundle = new Bundle();
            bundle.putSerializable("friend", user);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    private void getAllQZ() {
        Tool.showProgressDialog(GroupActivity.this, "正在加载...", true);
        HttpUtils httpUtils = new HttpUtils(30000);
        httpUtils.configCurrentHttpCacheExpiry(1000);
        String url = TLUrl.URL_GET_VOIP + "group/recommendgroup";
        httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Tool.removeProgressDialog();
                Tool.showInfo(GroupActivity.this, "网络异常");
//				reloadData();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                try {
                    JSONObject jsonObject = new JSONObject(arg0.result);
                    if (jsonObject.getInt("status") == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("msg");
                        if (jsonArray.length() == 0) {
                            Tool.showInfo(GroupActivity.this, "没有任何圈子");
                            return;
                        }
                        groups.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            GroupBean groupBean = new GroupBean();
                            groupBean.setGroupName(object.getString("name"));
                            groupBean.setGroupId(object.getString("groupid"));
                            groupBean.setGroupOwner(object.getString("uid"));
                            groupBean.setGroupType(object.getString("type"));
                            groupBean.setGroupPermission(object
                                    .getString("permission"));
                            groupBean.setGroupAvatar(object.getString("avatar"));
                            groupBean.setGroupDeclared("declared");
                            groupBean.setMemberInGroup(object.getString("count"));
                            groups.add(groupBean);
                            //MyApplication.dbUtils.saveOrUpdateAll(groups);
                            setAdapterData();
                        }
                    } else
                        Tool.showInfo(GroupActivity.this, "网络异常");
                } catch (Exception e) {
                    e.printStackTrace();
                    Tool.showInfo(GroupActivity.this, "网络异常");
                } finally {
                    Tool.removeProgressDialog();
                }

            }
        });
    }

    private void refreshGroup() {
        Tool.showProgressDialog(GroupActivity.this, "正在加载...", true);
        HttpUtils httpUtils = new HttpUtils(30000);
        httpUtils.configCurrentHttpCacheExpiry(1000);
        String url = TLUrl.URL_GET_VOIP + "member/QueryGroup?uid="
                + MyApplication.getInstance().getUid();
        httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Tool.removeProgressDialog();
                Tool.showInfo(GroupActivity.this, "网络异常");
                reloadData();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                try {
                    JSONObject jsonObject = new JSONObject(arg0.result);
                    if (jsonObject.getInt("status") == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("msg");
                        if (jsonArray.length() == 0) {
                            Tool.showInfo(GroupActivity.this, "你还没有加入任何群组");
                            return;
                        }
                        groups.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            GroupBean groupBean = new GroupBean();
                            groupBean.setGroupName(object.getString("name"));
                            groupBean.setGroupId(object.getString("groupid"));
                            groupBean.setGroupOwner(object.getString("uid"));
                            // groupBean.setDateCreate(object.getString("date"));
                            groupBean.setGroupType(object.getString("type"));
                            groupBean.setGroupPermission(object
                                    .getString("permission"));
                            groupBean.setGroupAvatar(object.getString("avatar"));
                            groupBean.setGroupDeclared("declared");
                            groupBean.setMemberInGroup(object.getString("user"));
                            // groupBean.setDateCreate(object.getString("date"));
                            groups.add(groupBean);
                            MyApplication.dbUtils.saveOrUpdateAll(groups);
                            setAdapterData();
                        }
                    } else
                        reloadData();
                } catch (Exception e) {
                    e.printStackTrace();
                    reloadData();
                } finally {
                    Tool.removeProgressDialog();
                }

            }
        });
    }

    // HttpRequest.sendGet(TLUrl.getInstance().URL_GET_VOIP + "member/QueryGroup", "uid="
    // + MyApplication.getInstance().getUid(), new HttpRevMsg() {
    // @Override
    // public void revMsg(String msg) {
    // Log.i("xbb我的群组", msg);
    // try {
    // JSONObject jsonObject = new JSONObject(msg);
    // if (jsonObject.getInt("status") == 1) {
    // JSONArray jsonArray = jsonObject.getJSONArray("msg");
    // if (jsonArray.length() == 0) {
    // myhandler.sendEmptyMessage(0);
    // return;
    // }
    // groups.clear();
    // for (int i = 0; i < jsonArray.length(); i++) {
    // JSONObject object = jsonArray.getJSONObject(i);
    // GroupBean groupBean = new GroupBean();
    // groupBean.setGroupName(object.getString("name"));
    // groupBean.setGroupId(object.getString("groupid"));
    // groupBean.setGroupOwner(object.getString("uid"));
    // // groupBean.setDateCreate(object.getString("date"));
    // groupBean.setGroupType(object.getString("type"));
    // groupBean.setGroupPermission(object
    // .getString("permission"));
    // groupBean.setGroupAvatar(object.getString("avatar"));
    // groupBean.setGroupDeclared("declared");
    // groupBean.setMemberInGroup(object.getString("user"));
    // // groupBean.setDateCreate(object.getString("date"));
    // groups.add(groupBean);
    // }
    // myhandler.sendEmptyMessage(1);
    // }else reloadData();
    // } catch (JSONException e) {
    // Log.i("xbb加入群组", e.toString());
    // myhandler.sendEmptyMessage(2);
    // e.printStackTrace();
    // } finally {
    // // Tool.removeProgressDialog();
    // }
    // }
    // });

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more:
//			showPopupWindow(v);
                Intent intent = new Intent(GroupActivity.this,
                        CreateGroupActivity.class);
                startActivityForResult(intent, 999);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showPopupWindow(View view) {
        View contentView = LayoutInflater.from(GroupActivity.this).inflate(
                R.layout.pop_window, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        Button searchgroup = (Button) contentView
                .findViewById(R.id.searchgroup);
        searchgroup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this,
                        SearchActivity.class);
                intent.putExtra("flag", "searchgroup");
                startActivityForResult(intent, 666);
                popupWindow.dismiss();
            }
        });
        Button creategroup = (Button) contentView
                .findViewById(R.id.creategroup);
        creategroup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this,
                        CreateGroupActivity.class);
                startActivityForResult(intent, 999);
                popupWindow.dismiss();
            }
        });

        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(view);
    }

//	private void showDialog(String title, String msg, boolean needNegativeButton) {
//		Builder dialog = new Builder(GroupActivity.this).setTitle(title)
//				.setMessage(msg).setCancelable(false)
//				.setPositiveButton("确定", new DialogInterface.OnClickListener() { // 设置确定按钮
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//							}
//						});
//		if (needNegativeButton) {
//			dialog.setNegativeButton("取消", null);
//		}
//		dialog.create().setCanceledOnTouchOutside(false);
//		dialog.create().show();
//	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 666 && resultCode == 666) {
            refreshGroup();
        } else if (requestCode == 999 && resultCode == 999) {
            GroupBean bean = (GroupBean) data.getSerializableExtra("group");
            if (bean != null) {
                groups.add(bean);
                setAdapterData();
            }
        }
    }

    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            GroupBean bean = (GroupBean) intent.getSerializableExtra("group");
            switch (intent.getAction()) {
                case "com.abcs.mybc.action.group":
                    if (bean != null) {
                        groups.remove(bean);
                        setAdapterData();
                        finish();
                    }
                    break;
                case "action.group.update":
                    if (bean != null) {
                        groups.add(bean);
                        setAdapterData();
                    }
                    break;
            }

        }
    }
}
