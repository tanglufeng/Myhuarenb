package com.abcs.haiwaigou.local.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.haiwaigou.local.fragment.PublishFragment;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.astuetz.PagerSlidingTabStrip;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyPublishActivity extends BaseFragmentActivity {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.t_title_name)
    TextView tTitleName;
    @InjectView(R.id.relative_top)
    RelativeLayout relativeTop;
    @InjectView(R.id.publish_tabs)
    PagerSlidingTabStrip publishTabs;
    @InjectView(R.id.linear_tab)
    LinearLayout linearTab;
    @InjectView(R.id.seperate_line)
    View seperateLine;
    @InjectView(R.id.main_pager)
    ViewPager mainPager;
    @InjectView(R.id.linear_root)
    LinearLayout linearRoot;


    private View view;

    Fragment currentFragment;
    int position;
    int currentType;
    CFViewPagerAdapter viewPagerAdapter;
    private static final String PUBLISH = "1";
    private static final String SAVE = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.local_activity_my_publish1, null);
        }
        setContentView(view);
        ButterKnife.inject(this);
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tTitleName.setText("我的发布");

        menuId = getIntent().getStringExtra("menuId");


        initViewPager();

    }


    private String menuId;

    private void initViewPager() {
        viewPagerAdapter = new CFViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.getDatas().add(PublishFragment.newInstance(PUBLISH, menuId));
        viewPagerAdapter.getDatas().add(PublishFragment.newInstance(SAVE, menuId));
        viewPagerAdapter.getTitle().add(0, "已发布");
        viewPagerAdapter.getTitle().add(1, "已保存");
        //第三方Tab
        mainPager.setAdapter(viewPagerAdapter);
        mainPager.setOffscreenPageLimit(1);
        mainPager.setCurrentItem(position);
//        pager.setPageTransformer(true, new DepthPageTransformer());
        publishTabs.setViewPager(mainPager);
        publishTabs.setIndicatorHeight(Util.dip2px(this, 4));
        publishTabs.setTextSize(Util.dip2px(this, 16));
        setSelectTextColor(position);
        setTextType();
        publishTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                setSelectTextColor(position);
                currentFragment = viewPagerAdapter.getItem(position);
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
        currentFragment = viewPagerAdapter.getItem(0);
        currentType = 1;
    }

    private void setTextType() {
        for (int i = 0; i < 2; i++) {
            View view = publishTabs.getChildAt(0);
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView != null) {
                Util.setFZLTHFont(tabTextView);
            }
        }
    }

    private void setSelectTextColor(int position) {
        for (int i = 0; i < 2; i++) {
            View view = publishTabs.getChildAt(0);
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView != null) {
                if (position == i) {
                    tabTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    tabTextView.setTextColor(getResources().getColor(R.color.hwg_text2));
                }
            }
        }
    }

}
