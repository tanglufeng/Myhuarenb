package com.abcs.haiwaigou.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LinkActivity extends BaseActivity {

    @InjectView(R.id.seperate)
    View seperate;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.t_title)
    TextView tTitle;
    @InjectView(R.id.relative_title)
    RelativeLayout relativeTitle;
    @InjectView(R.id.webview)
    WebView webview;
    @InjectView(R.id.img_goods)
    ImageView imgGoods;

    String goods_id;
    String goods_img;
    @InjectView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_link);
        ButterKnife.inject(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            seperate.setVisibility(View.VISIBLE);
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relative_title.getLayoutParams();
//            params.setMargins(0, getStatusBarHeight(), 0, 0);
//            relative_title.setLayoutParams(params);
//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) relativeSpinner.getLayoutParams();
//            layoutParams.setMargins(0, getStatusBarHeight(), 0, 0);
//            relativeSpinner.setLayoutParams(layoutParams);
        }
        if (!getIntent().getStringExtra("title").equals(""))
            tTitle.setText(getIntent().getStringExtra("title"));
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        if (!getIntent().getStringExtra("keyword").equals(""))
//            ImageLoader.getInstance().displayImage(getIntent().getStringExtra("keyword"), imgIcon, Options.getHDOptions());
        if (!getIntent().getStringExtra("goods_id").equals(""))
            goods_id = getIntent().getStringExtra("goods_id");
        if (!getIntent().getStringExtra("goods_id").equals(""))
            goods_img = getIntent().getStringExtra("goods_img");

        WebSettings settings = webview.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        //设置可放大缩小
//        settings.setBuiltInZoomControls(true);
        //设置加载自适应屏幕大小
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                    ImageLoader.getInstance().displayImage(goods_img, imgGoods, Options.getHDOptions());
                    imgGoods.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(LinkActivity.this, GoodsDetailActivity.class);
                            intent.putExtra("sid", goods_id);
                            intent.putExtra("pic", goods_img);
                            startActivity(intent);
                        }
                    });
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });
        if (!getIntent().getStringExtra("keyword").equals("") && NetworkUtils.isNetAvailable(this))
            webview.loadUrl(getIntent().getStringExtra("keyword"));
    }
}
