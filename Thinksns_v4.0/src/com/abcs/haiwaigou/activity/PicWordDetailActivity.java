package com.abcs.haiwaigou.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PicWordDetailActivity extends BaseActivity implements View.OnClickListener{

    @InjectView(R.id.tljr_txt_country_title)
    TextView tljrTxtCountryTitle;
    @InjectView(R.id.tljr_hqss_news_titlebelow)
    TextView tljrHqssNewsTitlebelow;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.tljr_grp_all_title)
    RelativeLayout tljrGrpAllTitle;
    @InjectView(R.id.web_detail)
    WebView webDetail;

    String url;
    String sid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_pic_word_detail);
        ButterKnife.inject(this);
        sid= (String) getIntent().getSerializableExtra("sid");
        url="http://www.huaqiaobang.com/mobile/index.php?act=goods&op=goods_body&goods_id="+sid;
        initWebView();
        setOnListener();

    }

    private void setOnListener() {
        relativeBack.setOnClickListener(this);
    }

    private void initWebView() {
        WebSettings settings=webDetail.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        //设置可放大缩小
        settings.setBuiltInZoomControls(true);
        //设置加载自适应屏幕大小
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webDetail.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relative_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }
}
