package com.abcs.haiwaigou.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.fragment.RechargeExpiredFragment;
import com.abcs.haiwaigou.fragment.RechargeUnuseFragment;
import com.abcs.haiwaigou.fragment.RechargeUseFragment;
import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.util.Util;
import com.astuetz.PagerSlidingTabStrip;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyRechargeActivity extends BaseFragmentActivity implements View.OnClickListener {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.relative_title)
    RelativeLayout relativeTitle;
    @InjectView(R.id.recharge_tabs)
    PagerSlidingTabStrip rechargeTabs;
    @InjectView(R.id.linear_tab)
    LinearLayout linearTab;
    @InjectView(R.id.recharge_pager)
    ViewPager rechargePager;
    @InjectView(R.id.linear_root)
    LinearLayout linearRoot;


    Fragment currentFragment;
    CFViewPagerAdapter viewPagerAdapter;
    int currentType;
    RechargeUnuseFragment rechargeUnuseFragment;
    RechargeUseFragment rechargeUseFragment;
    RechargeExpiredFragment rechargeExpiredFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_recharge);
        ButterKnife.inject(this);
        setOnListener();
        initFragment();
        initViewPager();
    }


    private void initFragment() {
        rechargeUseFragment=new RechargeUseFragment();
        rechargeUnuseFragment=new RechargeUnuseFragment();
        rechargeExpiredFragment=new RechargeExpiredFragment();
    }
    private void initViewPager() {
        //第三方Tab
        viewPagerAdapter = new CFViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.getDatas().add(rechargeUnuseFragment);
        viewPagerAdapter.getDatas().add(rechargeUseFragment);
        viewPagerAdapter.getDatas().add(rechargeExpiredFragment);
        viewPagerAdapter.getTitle().add(0, "未使用");
        viewPagerAdapter.getTitle().add(1, "已使用");
        viewPagerAdapter.getTitle().add(2, "已过期");
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        rechargePager.setPageMargin(pageMargin);
        rechargePager.setAdapter(viewPagerAdapter);
        rechargePager.setOffscreenPageLimit(1);
        rechargePager.setCurrentItem(0);
//        pager.setPageTransformer(true, new DepthPageTransformer());
        rechargeTabs.setViewPager(rechargePager);
        rechargeTabs.setIndicatorHeight(Util.dip2px(this, 4));
        rechargeTabs.setTextSize(Util.dip2px(this,16));
        setSelectTextColor(0);
        rechargeTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub

                setSelectTextColor(position);
                currentFragment =  viewPagerAdapter.getItem(position);
                currentType = position + 1;
//                currentgoodsFragment.initRecycler();
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
        currentFragment =  viewPagerAdapter.getItem(0);
        currentType = 1;
    }
    private void setSelectTextColor(int position) {
        for (int i = 0; i < 3; i++) {
            View view = rechargeTabs.getChildAt(0);
//            if (view instanceof LinearLayout) {
                View viewText = ((LinearLayout) view).getChildAt(i);
                TextView tabTextView = (TextView) viewText;
                if (tabTextView !=null) {
                    if (position == i) {
                        tabTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    } else {
                        tabTextView.setTextColor(getResources().getColor(R.color.hwg_text2));
                    }
                }
//            }
        }
    }
    private void setOnListener() {
        relativeBack.setOnClickListener(this);
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
