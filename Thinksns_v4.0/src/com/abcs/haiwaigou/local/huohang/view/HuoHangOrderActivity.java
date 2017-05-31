package com.abcs.haiwaigou.local.huohang.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.HuoHangOrderFragment2;
import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.astuetz.PagerSlidingTabStrip;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 本地配送 我的订单页面*/
public class HuoHangOrderActivity extends BaseFragmentActivity implements View.OnClickListener {

    public Handler handler = new Handler();
    @InjectView(R.id.tljr_txt_order_title)
    TextView tljrTxtOrderTitle;
    @InjectView(R.id.tljr_hqss_news_titlebelow)
    TextView tljrHqssNewsTitlebelow;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.tljr_grp_all_title)
    RelativeLayout tljrGrpAllTitle;
    @InjectView(R.id.comment_tabs)
    PagerSlidingTabStrip commentTabs;
    @InjectView(R.id.linear_tab)
    LinearLayout linearTab;
    @InjectView(R.id.comment_pager)
    ViewPager commentPager;
    @InjectView(R.id.seperate_line)
    View seperateLine;
    @InjectView(R.id.linear_root)
    LinearLayout linearRoot;
    @InjectView(R.id.img_null)
    ImageView imgNull;
    @InjectView(R.id.tv_null)
    TextView tvNull;
    @InjectView(R.id.layout_null)
    RelativeLayout layoutNull;
    Fragment currentgoodsFragment;
//    OrderAllFragment orderAllFragment;
//    OrderCommentFragment orderCommentFragment;
//    OrderCompleteFragment orderCompleteFragment;
//    OrderCancelFragment orderCancelFragment;
//    OrderDeliverFragment orderDeliverFragment;
//    OrderReceiveFragment orderReceiveFragment;
//    OrderPayFragment orderPayFragment;
    private RequestQueue mRequestQueue;
    CFViewPagerAdapter viewPagerAdapter;
    MyBroadCastReceiver myBroadCastReceiver;
    private View view;
    int currentType;
    int position=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.local_activity_order_list, null);
        }
//        position=getIntent().getIntExtra("position",0);
        setContentView(view);
        ButterKnife.inject(this);
        myBroadCastReceiver = new MyBroadCastReceiver(this, updateUI);
        myBroadCastReceiver.register();
        mRequestQueue = Volley.newRequestQueue(this);
        setOnListener();
        initViewPager();
    }
    private void initViewPager() {
        //第三方Tab
        viewPagerAdapter = new CFViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.getDatas().add(HuoHangOrderFragment2.newInstance(""));//全部
        viewPagerAdapter.getDatas().add(HuoHangOrderFragment2.newInstance("10"));//待支付
        viewPagerAdapter.getDatas().add(HuoHangOrderFragment2.newInstance("20"));//代发货
        viewPagerAdapter.getDatas().add(HuoHangOrderFragment2.newInstance("30"));//待收货
        viewPagerAdapter.getDatas().add(HuoHangOrderFragment2.newInstance("40"));//已完成
        viewPagerAdapter.getDatas().add(HuoHangOrderFragment2.newInstance("40"));//待评价
        viewPagerAdapter.getDatas().add(HuoHangOrderFragment2.newInstance("0"));//已取消


        viewPagerAdapter.getTitle().add(0, "全部");
        viewPagerAdapter.getTitle().add(1, "待付款");
        viewPagerAdapter.getTitle().add(2, "待发货");
        viewPagerAdapter.getTitle().add(3, "待收货");
        viewPagerAdapter.getTitle().add(4, "已完成");
        viewPagerAdapter.getTitle().add(5, "待评价");
        viewPagerAdapter.getTitle().add(6, "已取消");
//        viewPagerAdapter.getDatas().add(g5);
        commentPager.setAdapter(viewPagerAdapter);
        commentPager.setOffscreenPageLimit(1);
        commentPager.setCurrentItem(position);
//        pager.setPageTransformer(true, new DepthPageTransformer());
        commentTabs.setViewPager(commentPager);
        commentTabs.setIndicatorHeight(Util.dip2px(this, 4));
        commentTabs.setTextSize(Util.dip2px(this,16));
        setSelectTextColor(position);
        setTextType();
        commentTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                setSelectTextColor(position);
                currentgoodsFragment =  viewPagerAdapter.getItem(position);
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
        currentgoodsFragment =  viewPagerAdapter.getItem(0);
        currentType = 1;
    }

    private void setTextType() {
        for (int i = 0; i < 7; i++) {
            View view = commentTabs.getChildAt(0);
//            if (view instanceof LinearLayout) {
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView != null) {
//                SpannableString msp = new SpannableString(tabTextView.getText());
//                msp.setSpan(new RelativeSizeSpan(0.2f),0,msp.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                tabTextView.setText(msp);
//                Typeface fontFace = Typeface.createFromAsset(getAssets(),
//                        "font/fangzhenglantinghei.TTF");
//                tabTextView.setTypeface(fontFace);
                Util.setFZLTHFont(tabTextView);
            }
//            }
        }
    }

    private void setSelectTextColor(int position) {
        for (int i = 0; i < 7; i++) {
            View view = commentTabs.getChildAt(0);
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


    MyBroadCastReceiver.UpdateUI updateUI = new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

        }

        @Override
        public void updateCarNum(Intent intent) {

        }

        @Override
        public void updateCollection(Intent intent) {
            if (intent.getStringExtra("type").equals(MyUpdateUI.ORDER)) {
                Log.i("zjz", "更新订单");
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void setOnListener() {
       relativeBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        myBroadCastReceiver.unRegister();
        super.onDestroy();
    }
}
