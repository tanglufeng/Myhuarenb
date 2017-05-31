package com.abcs.haiwaigou.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.abcs.sociax.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GoodsGuaranteeActivity extends AppCompatActivity {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_goods_guarantee);
        ButterKnife.inject(this);
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
