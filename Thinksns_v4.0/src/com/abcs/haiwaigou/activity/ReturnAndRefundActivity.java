package com.abcs.haiwaigou.activity;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.ViewPagerAdapter;
import com.abcs.haiwaigou.fragment.RefundFragment;
import com.abcs.haiwaigou.fragment.ReturnFragment;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.tljr.news.viewpager.DepthPageTransformer;
import com.abcs.huaqiaobang.util.Util;
import com.astuetz.PagerSlidingTabStrip;

public class ReturnAndRefundActivity extends BaseFragmentActivity implements View.OnClickListener {

    ReturnFragment returnFragment;
    RefundFragment refundFragment;
    Fragment curentfragment;
    ViewPagerAdapter viewPagerAdapter;
    private PagerSlidingTabStrip tabs;
    ViewPager pager;
    int currentType;
    public Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_return_and_refund);
        findViewById(R.id.relative_back).setOnClickListener(this);
        initFragment();
        initViewPager();
    }

    private void initViewPager() {
        pager = (ViewPager) findViewById(R.id.hqss_pager);
        //第三方Tab
        tabs = (PagerSlidingTabStrip) findViewById(R.id.hqss_tabs);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//        viewPagerAdapter.getDatas().add(goodsFragment);
        viewPagerAdapter.getDatas().add(refundFragment);
        viewPagerAdapter.getDatas().add(returnFragment);
        viewPagerAdapter.getTitle().set(0, "退款申请");
        viewPagerAdapter.getTitle().set(1, "退货申请");
        pager.setAdapter(viewPagerAdapter);
        pager.setPageTransformer(true, new DepthPageTransformer());
        tabs.setViewPager(pager);
        tabs.setTextSize(Util.dip2px(this, 16));
        setSelectTextColor(0);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                setSelectTextColor(position);
                curentfragment = viewPagerAdapter.getItem(position);
                currentType = position + 1;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int position) {

                System.out.println("Change Posiont:" + position);

                // TODO Auto-generated method stub

            }
        });
        curentfragment = viewPagerAdapter.getItem(0);
        currentType = 1;
    }

    private void setSelectTextColor(int position) {
        Log.i("zjz", "position=" + position);
        for (int i = 0; i < 2; i++) {
            View view = tabs.getChildAt(0);
//            if (view instanceof LinearLayout) {
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView !=null) {
                if (position == i) {
                    tabTextView.setTextColor(getResources().getColor(R.color.tljr_statusbarcolor));
                } else {
                    tabTextView.setTextColor(getResources().getColor(R.color.hwg_text2));
                }
            }
//            }
        }
    }
    private void initFragment() {
        refundFragment=new RefundFragment();
        returnFragment=new ReturnFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relative_back:
                finish();
                break;
        }
    }
}
