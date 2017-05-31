package com.abcs.haiwaigou.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.view.CustWebView;
import com.abcs.sociax.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zjz on 2016/4/13.
 */
public class GoodsTuwenFragment extends Fragment {
    GoodsDetailActivity activity;
    View rootView;
    @InjectView(R.id.webview)
    CustWebView webview;

    boolean hasInited=false;
    String goods_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity= (GoodsDetailActivity) getActivity();
        rootView = inflater.inflate(R.layout.hwg_fragment_goods_tuwen, null);
        ButterKnife.inject(this, rootView);
        WebSettings settings=webview.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        //设置可放大缩小
        settings.setBuiltInZoomControls(true);
        //设置加载自适应屏幕大小
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        return rootView;
    }

    public void initView() {
        if (null != webview && !hasInited) {
            hasInited = true;
            webview.loadUrl(activity.getDetail_url());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


}
