package com.abcs.haiwaigou.yyg.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.RecyclerViewAndSwipeRefreshLayout;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.haiwaigou.view.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.abcs.haiwaigou.yyg.activity.YYGBalanceDetailActivity;
import com.abcs.haiwaigou.yyg.adapter.YYGGoodsFragmentAdapter;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.presenter.UserDataInterface;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.RiseNumberTextView;
import com.abcs.sociax.android.R;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyYYGFragment extends BaseFragment implements UserDataInterface, View.OnClickListener {
    Activity activity;
    @InjectView(R.id.yyg_tabs)
    PagerSlidingTabStrip yygTabs;
    @InjectView(R.id.yyg_pager)
    ViewPager yygPager;

    CFViewPagerAdapter viewPagerAdapter;
    YYGMyBuyRecordFragment yygMyBuyRecordFragment;
    YYGMyLotteryRecordFragment yygMyLotteryRecordFragment;
    Fragment currentFragment;

    @InjectView(R.id.t_balance)
    RiseNumberTextView tBalance;
    @InjectView(R.id.t_outcome)
    RiseNumberTextView tOutcome;
    @InjectView(R.id.t_income)
    RiseNumberTextView tIncome;
    @InjectView(R.id.relative_out)
    RelativeLayout relativeOut;
    @InjectView(R.id.relative_yue)
    RelativeLayout relativeYue;
    @InjectView(R.id.relative_in)
    RelativeLayout relativeIn;


    private View view;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    int totalPage;
    int currentPage = 1;
    boolean isLoadMore = false;
    boolean isMore = true;
    boolean first = false;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    YYGGoodsFragmentAdapter goodsFragmentAdapter;
    private ArrayList<Goods> goodsList = new ArrayList<Goods>();
    private RequestQueue mRequestQueue;

    private MyBroadCastReceiver myBroadCastReceiver;
    private Handler handler = new Handler();
    private double yygBalance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        activity = getActivity();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(
                    R.layout.hwg_yyg_my_record_fragment, null);
            ButterKnife.inject(this, view);
            mRequestQueue = Volley.newRequestQueue(activity);
            myBroadCastReceiver = new MyBroadCastReceiver(activity, updateUI);
            myBroadCastReceiver.register();
            isPrepared = true;
            lazyLoad();
            initMyBalance();
            initMyInOut();
            initViewPager();
            setOnListener();
        }
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);


        ButterKnife.inject(this, view);
        return view;
//        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
//        ViewGroup parent = (ViewGroup)mFragmentView.getParent();
//        if(parent != null) {
//            parent.removeView(mFragmentView);
//        }
//        return mFragmentView;
    }

    private void setOnListener() {
        relativeIn.setOnClickListener(this);
        relativeOut.setOnClickListener(this);
        relativeYue.setOnClickListener(this);
    }


    MyBroadCastReceiver.UpdateUI updateUI = new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

        }

        @Override
        public void updateCarNum(Intent intent) {

        }

        @Override
        public void updateCollection(Intent intent) {
            if (intent.getStringExtra("type").equals(MyUpdateUI.YYGBUY) || intent.getStringExtra("type").equals(MyUpdateUI.YYGREFUND)) {
                initMyBalance();
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };


    @Override
    protected void lazyLoad() {
        Log.i("zjz", "isprepare=" + isPrepared + "isvisible=" + isVisible + "hasonce=" + mHasLoadedOnce);
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            Log.i("zjz", "return掉了");
            return;
        }
        initViewPager();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        myBroadCastReceiver.unRegister();
    }

    private void initMyInOut() {
        HttpRequest.sendGet(TLUrl.getInstance().URL_yyg_find_my_outcome, "userId=" + MyApplication.getInstance().self.getId(),
                new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {
                        Log.i("zjz", "yyg_my_inout_msg=" + msg);
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
                                            if (tOutcome != null)
                                                if (jsonObject.has("totalExpenditure")&&jsonObject.optDouble("totalExpenditure") != 0) {
                                                    tOutcome.withNumber(jsonObject.optDouble("totalExpenditure")).start();
                                                } else {
                                                    tOutcome.withNumber(0.0).start();
                                                }
                                            if (tIncome != null)
                                                if (jsonObject.has("totalIncome") && jsonObject.optDouble("totalIncome") != 0) {
                                                    tIncome.withNumber(jsonObject.optDouble("totalIncome")).start();
                                                }else {
                                                    tIncome.withNumber(0.0).start();
                                                }

                                        }
                                    } else {
//                                        showToast(result.optString("msg"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                });
    }


    private void initMyBalance() {
        HttpRequest.sendGet(TLUrl.getInstance().URL_yyg_my_balance, "userId=" + MyApplication.getInstance().self.getId(),
                new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {
                        Log.i("zjz", "yyg_my_balance_msg=" + msg);
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
                                        Toast.makeText(activity,result.optString("msg"),Toast.LENGTH_SHORT).show();
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
        viewPagerAdapter = new CFViewPagerAdapter(getChildFragmentManager());

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
        yygTabs.setIndicatorHeight(Util.dip2px(activity, 4));

        yygTabs.setTextSize(Util.dip2px(activity, 16));
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

    @Override
    public void loginSuccess() {
        initMyBalance();
        initMyInOut();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.relative_in:
                intent = new Intent(activity, YYGBalanceDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_out:
                intent = new Intent(activity, YYGBalanceDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_yue:
                intent = new Intent(activity, YYGBalanceDetailActivity.class);
                startActivity(intent);
                break;
        }
    }
}
