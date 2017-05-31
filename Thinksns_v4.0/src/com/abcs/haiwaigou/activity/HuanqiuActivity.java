package com.abcs.haiwaigou.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.tljr.news.HuanQiuShiShi;
import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/11/3.
 */

public class HuanqiuActivity extends BaseFragmentActivity{
    private HuanQiuShiShi huanQiuShiShi;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        huanQiuShiShi = new HuanQiuShiShi();
        fm = getSupportFragmentManager();

        //  fg_message = FragmentMessage.newInstance(mdNotification);
        fm.beginTransaction().add(R.id.ll_container, huanQiuShiShi).commit();
    }

}
