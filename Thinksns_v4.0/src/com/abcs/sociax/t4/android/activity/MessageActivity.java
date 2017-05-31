package com.abcs.sociax.t4.android.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.ThinksnsAbscractActivity;
import com.abcs.sociax.t4.android.fragment.FragmentMessage;
import com.abcs.sociax.t4.model.ModelNotification;

/**
 * Created by Administrator on 2016/8/9.
 */
public class MessageActivity extends ThinksnsAbscractActivity {

    //新消息实体类
    private ModelNotification mdNotification;

    private FragmentMessage fg_message;
    FragmentManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreateNoTitle(savedInstanceState);
        fm = getSupportFragmentManager();

        fg_message = new FragmentMessage();
      //  fg_message = FragmentMessage.newInstance(mdNotification);
        fm.beginTransaction().add(R.id.ll_container, fg_message).commit();


    }

    @Override
    public String getTitleCenter() {
        return "消息";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();


        finish();
    }
}