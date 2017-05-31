package com.abcs.haiwaigou.activity;

import android.app.Activity;
import android.os.Bundle;

import com.abcs.sociax.android.R;

import butterknife.ButterKnife;

public class TestActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_test);
        ButterKnife.inject(this);
    }
}
