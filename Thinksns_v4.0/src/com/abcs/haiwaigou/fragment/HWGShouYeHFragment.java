package com.abcs.haiwaigou.fragment;

import android.content.Intent;
import android.os.Build;
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

import com.abcs.haiwaigou.activity.CartActivity2;
import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.ServerUtils;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.ActivityHome;
import com.abcs.sociax.t4.android.fragment.FragmentSociax;
import com.astuetz.PagerSlidingTabStrip;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.abcs.sociax.t4.android.video.ToastUtils.showToast;

/**
 * Created by zjz on 2016/9/13.
 */
public class HWGShouYeHFragment extends FragmentSociax implements View.OnClickListener {

    Fragment currentFragment;
    @InjectView(R.id.main_tabs)
    PagerSlidingTabStrip mainTabs;
    @InjectView(R.id.main_pager)
    ViewPager mainPager;
    @InjectView(R.id.car_num)
    TextView car_num;
    @InjectView(R.id.relative_cart)
    RelativeLayout relative_cart;
    private View view;
    private ActivityHome activity;
    CFViewPagerAdapter viewPagerAdapter;
    private Handler handler = new Handler();
    private ArrayList<String> nameList = new ArrayList<>();
//    private ArrayList<String> idList = new ArrayList<>();
    private String[] strs;
    private ACache aCache;
    private boolean isSpecialDialogFirst = true;
    private boolean loadSuccess = false;

    private String title[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (ActivityHome) getActivity();
        view = activity.getLayoutInflater().inflate(
                R.layout.hwg_main_fragment3, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////            window = getWindow();
//            view.findViewById(R.id.statusbar).setVisibility(View.VISIBLE);
        }
        ButterKnife.inject(this, view);

        title=getResources().getStringArray(R.array.hwgtitle);

        if (!ServerUtils.isConnect(activity)) {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    showToast("请检查您的网络");
                }
            });
            return;
        }else {
            initMyView();
        }

    }

    @Override
    public void initListener() {

        relative_cart.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.hwg_main_fragment3;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initIntentData() {

    }

    @Override
    public void initData() {

    }


    private boolean isReLoad = false;

    private void initMyView() {

        for (int i = 0; i < title.length; i++) {
            String name = title[i];
            nameList.add(name);
        }
        initViewPager();
    }
    HWGFenLeiFragment hwgFenLeiFragment ;  // 分类
    HWGLuxuryFragment hwgLuxuryFragment ;  // 轻奢
    HWGDuDaoFragment2 hwgDudaoFragment;   // 独到
    HWGWanLeFragment hwgWanLeFragment;     // 玩乐

    private void initViewPager() {
        strs = new String[nameList.size()];
        //第三方Tab
        viewPagerAdapter = new CFViewPagerAdapter(getChildFragmentManager());
        for (int i = 0; i < nameList.size(); i++) {
            strs[i] = TLUrl.getInstance().HWGMAIN + i;
            Log.i("zjz", "str[" + i + "]=" + strs[i]);
            if (i == 0) {// 首页
                viewPagerAdapter.getDatas().add(MainFragment1.newInstance("58", strs[0]));
//                viewPagerAdapter.getDatas().add(MainFragment1.newInstance(idList.get(0), strs[0]));
                viewPagerAdapter.getTitle().add(i, nameList.get(i));
            } else if(i == 1){// 分类
                if(hwgFenLeiFragment==null){
                    hwgFenLeiFragment = new HWGFenLeiFragment();
                }else {
                }
                viewPagerAdapter.getDatas().add(hwgFenLeiFragment);
                viewPagerAdapter.getTitle().add(i, nameList.get(i));
            }else if(i == 2){// 轻奢
                if(hwgLuxuryFragment==null){
                    hwgLuxuryFragment = new HWGLuxuryFragment();
                }else {
                }

                viewPagerAdapter.getDatas().add(hwgLuxuryFragment);
                viewPagerAdapter.getTitle().add(i, nameList.get(i));
            }else if(i == 3){// 独到
                if(hwgDudaoFragment==null){
                    hwgDudaoFragment = new HWGDuDaoFragment2();
                }else {
                }
                viewPagerAdapter.getDatas().add(hwgDudaoFragment);
                viewPagerAdapter.getTitle().add(i, nameList.get(i));
            }else if(i == 4){// 玩乐
                if(hwgWanLeFragment==null){
                    hwgWanLeFragment = new HWGWanLeFragment();
                }else {
                }
                viewPagerAdapter.getDatas().add(hwgWanLeFragment);
                viewPagerAdapter.getTitle().add(i, nameList.get(i));
            }


            Log.i("zjz", "nameList.get(" + i + ")=" + nameList.get(i));
        }
        //滑动的viewpager
        mainPager.setAdapter(viewPagerAdapter);
        mainPager.setOffscreenPageLimit(1);
        mainPager.setCurrentItem(0);
//        pager.setPageTransformer(true, new DepthPageTransformer());
        mainTabs.setViewPager(mainPager);
        mainTabs.setIndicatorHeight(Util.dip2px(activity, 4));
        mainTabs.setTextSize(Util.dip2px(activity, 16));
        setSelectTextColor(0);
        setTextType();
        mainTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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

    private void setTextType() {
        for (int i = 0; i < nameList.size(); i++) {
            View view = mainTabs.getChildAt(0);
//            if (view instanceof LinearLayout) {
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView != null) {
//                SpannableString msp = new SpannableString(tabTextView.getText());
//                msp.setSpan(new RelativeSizeSpan(0.2f),0,msp.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                tabTextView.setText(msp);
//                Typeface fontFace = Typeface.createFromAsset(activity.getAssets(),
//                        "font/fangzhenglantinghei.TTF");
//                tabTextView.setTypeface(fontFace);
                Util.setFZLTHFont(tabTextView);
            }
//            }
        }
    }

    private void setSelectTextColor(int position) {
        for (int i = 0; i < nameList.size(); i++) {
            View view = mainTabs.getChildAt(0);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup p = (ViewGroup) view.getParent();

        if (p != null)
            p.removeAllViewsInLayout();
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.t_refresh:
                loadSuccess = false;
                aCache.remove(TLUrl.getInstance().HWGMAIN);
                ProgressDlgUtil.showProgressDlg("Loading...", activity);
                initMyView();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDlgUtil.stopProgressDlg();
                    }
                }, 2000);
                break;
            case R.id.relative_cart:
                if (MyApplication.getInstance().self == null) {
                    intent = new Intent(activity, WXEntryActivity.class);
                    activity.startActivity(intent);
                    return;
                }
                intent = new Intent(activity, CartActivity2.class);
                intent.putExtra("store_id","");
                activity.startActivity(intent);
                break;
        }
    }
}