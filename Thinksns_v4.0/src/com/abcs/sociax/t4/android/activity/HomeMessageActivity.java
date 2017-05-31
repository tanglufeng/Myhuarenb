package com.abcs.sociax.t4.android.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.abcs.sociax.android.R;
import com.abcs.sociax.component.CustomTitle;
import com.abcs.sociax.component.LeftAndRightTitle;
import com.abcs.sociax.t4.android.ThinksnsAbscractActivity;
import com.abcs.sociax.t4.android.fragment.FragmentMessage;
import com.abcs.sociax.t4.model.ModelUser;
import com.thinksns.sociax.thinksnsbase.bean.ListData;
import com.thinksns.sociax.thinksnsbase.bean.SociaxItem;

/**
 * Created by Hao on 2016/11/2.
 */

public class HomeMessageActivity extends ThinksnsAbscractActivity {

    protected ListData<SociaxItem> list;// 数据list
    protected ModelUser user;		// 当前界面所属用户
    protected int uid;				// 当前界面所属uid

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreateNoTitle(savedInstanceState);
        initFragment();
    }

    private void initFragment() {
        fragment = new FragmentMessage();
        fragmentTransaction.add(R.id.ll_content, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public String getTitleCenter() {
        return "xx";
    }

    @Override
    protected CustomTitle setCustomTitle() {
        return new LeftAndRightTitle(R.drawable.img_back, this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_common;
    }

}
