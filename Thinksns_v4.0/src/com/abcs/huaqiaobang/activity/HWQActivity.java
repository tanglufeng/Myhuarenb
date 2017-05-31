package com.abcs.huaqiaobang.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.view.XScrollView;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.StatusBarCompat;
import com.abcs.huaqiaobang.util.Util;

public class HWQActivity extends BaseActivity {

    private XScrollView scrollView;
    private Activity activity;
    private LinearLayout hwgtitlebar;
    private Window window;
    private TextView tvSearchHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hwq);

        activity = this;
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            window = getWindow();
//            // Translucent status bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) hwgtitlebar.getLayoutParams();
            params.setMargins(0, getStatusBarHeight(), 0, 0);
            hwgtitlebar.setLayoutParams(params);
            setTranslucentStatus(true);
        }
        StatusBarCompat.compat(this,true);


    }

    private void initView() {


        hwgtitlebar = (LinearLayout) findViewById(R.id.hwgtitle_bar);
        tvSearchHint = (TextView) findViewById(R.id.tv_searchHint);
        scrollView = (XScrollView) findViewById(R.id.scrollView);
        scrollView.initWithContext(activity);
        scrollView.setPullRefreshEnable(true);
        scrollView.setPullLoadEnable(false);
        scrollView.setAutoLoadEnable(false);
        scrollView.setRefreshTime(Util.getNowTime());

        scrollView.setIXScrollViewListener(new XScrollView.IXScrollViewListener() {

            @Override
            public void onRefresh() {
                Log.i("zjz", "下拉刷新！！");
            }

            @Override
            public void onLoadMore() {

            }
        });
        scrollView.setOnScrollViewListener(new XScrollView.OnScrollViewListener() {
            @Override
            public void onScrollListener(int x, int y, int oldx, int oldy) {
                changeTitleColor(y);
            }
        });
    }

    private void changeTitleColor(int y) {
        if (y >= 0 && y <= 255) {
            hwgtitlebar.setBackgroundColor(Color.argb(y, 235, 80, 65));

            StatusBarCompat.compat(this, Color.argb(y, 235, 80, 65),true);
            if (y == 0)
                tvSearchHint.setTextColor(Color.parseColor("#eb5041"));
        } else if (y >= 255) {
            hwgtitlebar.setBackgroundColor(Color.argb(255, 235, 80, 65));
//            setSystembarColor(Color.argb(255, 235, 80, 65));
            StatusBarCompat.compat(this, Color.argb(255, 235, 80, 65),true);
            tvSearchHint.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StatusBarCompat.statusBarView = null;
    }
}
