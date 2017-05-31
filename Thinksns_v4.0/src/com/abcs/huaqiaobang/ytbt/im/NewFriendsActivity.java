package com.abcs.huaqiaobang.ytbt.im;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.ytbt.adapter.AddFriendRequestAdapter;
import com.abcs.huaqiaobang.ytbt.bean.AddFriendRequestBean;
import com.abcs.huaqiaobang.ytbt.util.GlobalConstant;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

public class NewFriendsActivity extends BaseActivity implements OnClickListener {
    private ListView lv;
    private AddFriendRequestAdapter adapter;
    private MyBroadCastReceiver receiver;
    private List<AddFriendRequestBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends);
        initView();
        sendBroadcast(new Intent(GlobalConstant.ACTION_READ_ADDFRIEND));
        receiver = new MyBroadCastReceiver();
        registerReceiver(receiver, new IntentFilter(
                GlobalConstant.ACTION_ADDFRIEND_REQUEST));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initView() {
        try {
            list.clear();
            List<AddFriendRequestBean> a = MyApplication.dbUtils
                    .findAll(Selector.from(AddFriendRequestBean.class).orderBy(
                            "time", true));
            if (a != null)
                list.addAll(a);
        } catch (DbException e) {
            e.printStackTrace();
        }
        findViewById(R.id.back).setOnClickListener(this);
        lv = (ListView) findViewById(R.id.lv);
        setData();

    }

    private void setData() {
        adapter = new AddFriendRequestAdapter(list, this);
        lv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    class MyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            adapter.setList();
            sendBroadcast(new Intent(GlobalConstant.ACTION_READ_ADDFRIEND));
        }
    }
}
