package com.abcs.huaqiaobang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HelpAskActivity extends BaseActivity implements View.OnClickListener {


    @InjectView(R.id.my_rl_Access)
    RelativeLayout myRlAccess;
    @InjectView(R.id.my_rl_jiaoyi)
    RelativeLayout myRlJiaoyi;
    @InjectView(R.id.my_rl_network)
    RelativeLayout myRlNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_ask);
        ButterKnife.inject(this);
        myRlAccess.setOnClickListener(this);
        myRlJiaoyi.setOnClickListener(this);
        myRlNetwork.setOnClickListener(this);
        findViewById(R.id.tljr_per_btn_lfanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, AboutWeActivity.class);

        switch (v.getId()) {
            case R.id.my_rl_Access:
                intent.putExtra("url", "file:///android_asset/accountproblem.html");
                intent.putExtra("title", "账户问题");


                break;
            case R.id.my_rl_jiaoyi:
                intent.putExtra("url", "file:///android_asset/networkproblem.html");
                intent.putExtra("title", "交易问题");

                break;
            case R.id.my_rl_network:
                intent.putExtra("url", "file:///android_asset/networkproblem.html");
                intent.putExtra("title", "网络");
                break;

        }
        startActivity(intent);
    }
}
