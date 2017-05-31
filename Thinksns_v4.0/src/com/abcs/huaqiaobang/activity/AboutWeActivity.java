package com.abcs.huaqiaobang.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AboutWeActivity extends BaseActivity {

    @InjectView(R.id.tljr_per_txt_ltext)
    TextView tljrPerTxtLtext;
    @InjectView(R.id.web_content)
    WebView webContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_we);
        ButterKnife.inject(this);

        if (getIntent() != null) {
            webContent.loadUrl(getIntent().getStringExtra("url"));
            if (getIntent().getBooleanExtra("aboutus", false)) {
                webContent.setBackgroundColor(0);
            } else {
                webContent.setBackgroundColor(getResources().getColor(R.color.white));
            }
            tljrPerTxtLtext.setText(getIntent().getStringExtra("title"));
        }
        findViewById(R.id.tljr_per_btn_lfanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
