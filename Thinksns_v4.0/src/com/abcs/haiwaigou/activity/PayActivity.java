package com.abcs.haiwaigou.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.abcs.sociax.android.R;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PayActivity extends AppCompatActivity {

    @InjectView(R.id.web_view)
    WebView webView;

    String url= TLUrl.getInstance().URL_hwg_base+"/mobile/index.php?act=member_payment&op=pay&key=2ed0e443a942c2bf90209eb6d05a2a7b&pay_sn=900510081917255013&payment_code=alipay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.inject(this);
//        url= (String) getIntent().getSerializableExtra("url");
        WebSettings settings=webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        //设置可放大缩小
        settings.setBuiltInZoomControls(true);
        //设置加载自适应屏幕大小
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webView.loadUrl(url);
    }
}
