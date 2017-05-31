package com.abcs.haiwaigou.yyg.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.sociax.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class YYGMessageActivity extends BaseActivity {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_yyg_activity_yygmessage);
        ButterKnife.inject(this);
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
