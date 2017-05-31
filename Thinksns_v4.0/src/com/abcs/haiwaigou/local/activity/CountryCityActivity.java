package com.abcs.haiwaigou.local.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.haiwaigou.local.beans.City2;
import com.abcs.haiwaigou.local.fragment.CountryListFragment2;
import com.abcs.haiwaigou.local.fragment.CountryListReMenFragment;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class CountryCityActivity extends BaseFragmentActivity {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.t_title_name)
    TextView tTitleName;

    @InjectView(R.id.main_pager)
    ViewPager mainPager;
    @InjectView(R.id.area_tabs)
    PagerSlidingTabStrip areaTabs;

    private ArrayList<String> tags = new ArrayList<String>();
    Fragment currentFragment;
    int position = 0;
    int currentType;
    CFViewPagerAdapter viewPagerAdapter;
    ACache aCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_activity_country_city);
        ButterKnife.inject(this);
        tTitleName.setText("选择城市");
        aCache = ACache.get(this);
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initArea();
    }

    private void initArea() {
        String msg = aCache.getAsString("new_localCity");
        Log.i("zds", "local_msg=" + msg);
        if (!TextUtils.isEmpty(msg)) {
            initAreaList(msg);
        } else {
            ProgressDlgUtil.showProgressDlg("loading...", this);
//            https://japi.tuling.me/hrq/City/getCity?version=v1.0
            HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_area3_location, "version=v1.0", new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    ProgressDlgUtil.stopProgressDlg();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(msg)) {
                                initAreaList(msg);
                                aCache.remove("new_localCity");
                                aCache.put("new_localCity",msg);
                            }
                        }
                    });
                }
            });
        }
    }

    private void initAreaList(String msg)  {
        tags.clear();
        City2 city = new Gson().fromJson(msg, City2.class);
        if (city.result == 1&&city.body!= null) {
            if (city.body.hotCity!= null&&city.body.hotCity.size()>0) {  // 热门城市
                tags.add("热门");
            }
            if (city.body.data!= null&&city.body.data.size()>0) {  // 洲和国家、城市
                for (int i = 0; i < city.body.data.size(); i++) {
                    tags.add(city.body.data.get(i).area_cn_name);
                }
            }
        }

        initViewPager(msg);
    }

    private void initViewPager(String msg) {
        viewPagerAdapter = new CFViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < tags.size(); i++) {
            viewPagerAdapter.getTitle().add(i, tags.get(i));
            if (i==0) {  // 热门
                viewPagerAdapter.getDatas().add(CountryListReMenFragment.newInstance(msg));
            } else {
                viewPagerAdapter.getDatas().add(CountryListFragment2.newInstance(msg,(i-1)));
            }
        }
        //第三方Tab
        if (mainPager != null && areaTabs != null) {
            mainPager.setAdapter(viewPagerAdapter);
            mainPager.setOffscreenPageLimit(1);
            mainPager.setCurrentItem(position);
            areaTabs.setViewPager(mainPager);
            areaTabs.setIndicatorHeight(Util.dip2px(this, 4));
            areaTabs.setTextSize(Util.dip2px(this, 16));
            setSelectTextColor(position);
            setTextType();
            areaTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    // TODO Auto-generated method stub
                    setSelectTextColor(position);
                    currentFragment = viewPagerAdapter.getItem(position);
                    currentType = position + 1;
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                @Override
                public void onPageScrollStateChanged(int position) {
                }
            });
            currentFragment = viewPagerAdapter.getItem(0);
            currentType = 1;
        }

    }

    private void setTextType() {
        for (int i = 0; i < tags.size(); i++) {
            View view = areaTabs.getChildAt(0);
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView != null) {
                Util.setFZLTHFont(tabTextView);
            }
        }
    }

    private void setSelectTextColor(int position) {
        for (int i = 0; i < tags.size(); i++) {
            View view = areaTabs.getChildAt(0);
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
