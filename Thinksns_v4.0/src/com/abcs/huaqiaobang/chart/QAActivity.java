package com.abcs.huaqiaobang.chart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;

/**
 * Created by Administrator on 2016/3/7.
 */
public class QAActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_qalayout);
        Intent intent = getIntent();
        TextView title = (TextView) findViewById(R.id.tljr_img_futures_join_title);
        String title1 = intent.getStringExtra("title");
        title.setText(title1);
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearlayout);
        if (title1.equals("问答")) {
            FutureAskView futureAskView = new FutureAskView(this);
            layout.addView(futureAskView.getView());
        } else if (title1.equals("大家聊")) {
            WeSeeView weSeeView = new WeSeeView(this);
            layout.addView(weSeeView.getView());

        } else if (title1.equals("客服")) {
            RealTimeView view = new RealTimeView(this, new Handler());
            layout.addView(view.getView());
        }
        findViewById(R.id.tljr_img_futures_join_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
