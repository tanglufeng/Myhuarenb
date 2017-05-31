package com.abcs.haiwaigou.yyg.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.haiwaigou.yyg.fragment.YYGMyBuyRecordFragment;
import com.abcs.haiwaigou.yyg.fragment.YYGMyLotteryRecordFragment;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.RiseNumberTextView;
import com.abcs.sociax.android.R;
import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyYYGActivity extends BaseFragmentActivity {

    @InjectView(R.id.seperate)
    View seperate;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.yyg_tabs)
    PagerSlidingTabStrip yygTabs;
    @InjectView(R.id.seperate_line)
    View seperateLine;
    @InjectView(R.id.yyg_pager)
    ViewPager yygPager;
    CFViewPagerAdapter viewPagerAdapter;
    YYGMyBuyRecordFragment yygMyBuyRecordFragment;
    YYGMyLotteryRecordFragment yygMyLotteryRecordFragment;
    Fragment currentFragment;
    @InjectView(R.id.t_balance)
    RiseNumberTextView tBalance;

    private double yygBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_yyg_activity_my_yyg);
        ButterKnife.inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            seperate.setVisibility(View.VISIBLE);
        }
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initViewPager();
        initMyBalance();
    }


    private void initMyBalance() {
        HttpRequest.sendGet(TLUrl.getInstance().URL_yyg_my_balance, "userId=" + MyApplication.getInstance().self.getId(),
                new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {
                        Log.i("zjz", "yyg_my_code_msg=" + msg);
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (msg.length() == 0) {
//                                    showToast("请求失败,请重试");
                                    return;
                                }
                                try {
                                    JSONObject result = new JSONObject(msg);
                                    if (result.optInt("status") == 1) {
                                        JSONObject jsonObject = result.optJSONObject("msg");
                                        if (jsonObject != null) {
                                            yygBalance = jsonObject.optDouble("userMoney");
                                            Log.i("zjz", "yygBalance=" + yygBalance);
                                            if (yygBalance != 0) {
                                                if (tBalance != null)
                                                    tBalance.withNumber(yygBalance).start();
                                            }

                                        }
                                    } else {
                                        showToast(result.optString("msg"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                });
    }


    private void initViewPager() {
        //第三方Tab
        viewPagerAdapter = new CFViewPagerAdapter(getSupportFragmentManager());

        yygMyLotteryRecordFragment = new YYGMyLotteryRecordFragment();
        yygMyBuyRecordFragment = new YYGMyBuyRecordFragment();
        viewPagerAdapter.getDatas().add(yygMyBuyRecordFragment);
        viewPagerAdapter.getDatas().add(yygMyLotteryRecordFragment);
        viewPagerAdapter.getTitle().add(0, "抢单记录");
        viewPagerAdapter.getTitle().add(1, "幸运记录");
        //滑动的viewpager
        yygPager.setAdapter(viewPagerAdapter);
        yygPager.setOffscreenPageLimit(1);
        yygPager.setCurrentItem(0);
//        pager.setPageTransformer(true, new DepthPageTransformer());
        yygTabs.setViewPager(yygPager);
        yygTabs.setIndicatorHeight(Util.dip2px(this, 4));
        yygTabs.setTextSize(Util.dip2px(this, 16));
        setSelectTextColor(0);
        yygTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                setSelectTextColor(position);
                currentFragment = viewPagerAdapter.getItem(position);
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
    }


    private void setSelectTextColor(int position) {
        for (int i = 0; i < 2; i++) {
            View view = yygTabs.getChildAt(0);
//            if (view instanceof LinearLayout) {
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView != null) {
//                SpannableString msp = new SpannableString(tabTextView.getText());
//                msp.setSpan(new RelativeSizeSpan(0.2f),0,msp.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                tabTextView.setText(msp);
                if (position == i) {
                    tabTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    tabTextView.setTextColor(getResources().getColor(R.color.hwg_text2));
                }
            }
//            }
        }
    }
}
