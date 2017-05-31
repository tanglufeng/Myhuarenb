package com.abcs.huaqiaobang.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.sociax.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/26.
 */

public class GuanggaoActivity extends Activity {

    @InjectView(R.id.local_guangao_wv)
    WebView localGuangaoWv;
    @InjectView(R.id.tv_title)
    TextView tv_title;

    Handler mHandler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_guanggao_activity);
        ButterKnife.inject(this);

        String title_local=getIntent().getStringExtra("title_local");

        if(!TextUtils.isEmpty(title_local)){
            tv_title.setText(title_local);
        }else {
            tv_title.setText("详情");
        }
        //支持javascript
        localGuangaoWv.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        localGuangaoWv.getSettings().setSupportZoom(false);
        localGuangaoWv.getSettings().setDefaultTextEncodingName("UTF-8");//设置编码格式
        // 设置出现缩放工具
        localGuangaoWv.getSettings().setBuiltInZoomControls(false);
        //扩大比例的缩放
        localGuangaoWv.getSettings().setUseWideViewPort(false);
        //自适应屏幕
        localGuangaoWv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        localGuangaoWv.getSettings().setLoadWithOverviewMode(true);
        localGuangaoWv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { // 屏蔽超链接
                if (url != null)
                    view.loadUrl(url);
                return true;
            }
        });

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            localGuangaoWv.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        ProgressDlgUtil.showProgressDlg("loading",this);

        String inntent_Url=getIntent().getStringExtra("url");
        if(!TextUtils.isEmpty(inntent_Url)){
            Log.i("zds", "onCreate: URL========"+inntent_Url);
            if(inntent_Url.startsWith("www")){
                localGuangaoWv.loadUrl("http://"+inntent_Url);
            }else {
                localGuangaoWv.loadUrl(inntent_Url);
            }
        }


        if(getIntent().getStringExtra("url_local")!=null){
            Log.i("zds", "onCreate: URL========"+getIntent().getStringExtra("url_local"));
            localGuangaoWv.loadUrl(getIntent().getStringExtra("url_local"));
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressDlgUtil.stopProgressDlg();
            }
        }, 1000);
    }

    @OnClick(R.id.relative_back)
    public void onClick() {
        finish();
    }
}
