package com.abcs.haiwaigou.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.ExchangeRecordFragment;
import com.abcs.haiwaigou.fragment.PointDetailFragment;
import com.abcs.haiwaigou.fragment.RechargeFragment;
import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.RiseNumberTextView;
import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ExchangePointActivity extends BaseFragmentActivity implements View.OnClickListener {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.relative_title)
    RelativeLayout relativeTitle;
    @InjectView(R.id.relative_help)
    RelativeLayout relativeHelp;
    @InjectView(R.id.img_head)
    ImageView imgHead;
    @InjectView(R.id.t_jifen)
    TextView tJifen;
    @InjectView(R.id.t_point)
    RiseNumberTextView tPoint;
    @InjectView(R.id.t_detail)
    TextView tDetail;
    @InjectView(R.id.relative_user)
    RelativeLayout relativeUser;
    @InjectView(R.id.exchange_tabs)
    PagerSlidingTabStrip exchangeTabs;
    @InjectView(R.id.linear_tab)
    LinearLayout linearTab;
    @InjectView(R.id.seperate_line)
    View seperateLine;
    @InjectView(R.id.exchange_pager)
    ViewPager exchangePager;
    @InjectView(R.id.linear_root)
    LinearLayout linearRoot;

    RechargeFragment rechargeFragment;
    PointDetailFragment pointDetailFragment;
    ExchangeRecordFragment exchangeRecordFragment;
    Fragment currentFragment;
    CFViewPagerAdapter viewPagerAdapter;
    int currentType;
    MyBroadCastReceiver myBroadCastReceiver;
    private View view;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_activity_exchange_point, null);
        }
        setContentView(view);
        ButterKnife.inject(this);
        position=getIntent().getIntExtra("position",0);
        myBroadCastReceiver = new MyBroadCastReceiver(this, updateUI);
        myBroadCastReceiver.register();
        setOnListener();
        initFragment();
        initViewPager();
        initUserHead();
        initUserData();
//        initPopWindow();
    }

    PopupWindow popupWindow = null;

    private void initPopWindow(View view) {

        View popview = LayoutInflater.from(this).inflate(R.layout.hwg_pop_point_detail, null);
//        ViewGroup.LayoutParams layoutParams=new LinearLayout.LayoutParams(Util.WIDTH*2/3, ViewGroup.LayoutParams.WRAP_CONTENT);
//        popview.setLayoutParams(layoutParams);
        popupWindow = new PopupWindow(popview, Util.WIDTH * 2 / 3, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow()
                        .getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ffffff")));
        Button btnOk = (Button) popview.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//        popupWindow.setContentView(popview);
//        popupWindow.setFocusable(true);
//        popupWindow.setOutsideTouchable(true);

    }

    private void initUserHead() {
        LoadPicture loadPicture = new LoadPicture();
        loadPicture.initPicture(imgHead, MyApplication.getInstance().self.getAvatarUrl());
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
            if (intent.getStringExtra("type").equals(MyUpdateUI.RECHARGE)) {
                Log.i("zjz", "更新积分");
                initUserData();
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initUserData() {

        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_user, "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "msg=" + msg);
                                JSONObject object1 = object.getJSONObject("datas");
                                JSONObject member = object1.optJSONObject("member_info");
                                if (tPoint != null)
                                    tPoint.withNumber(member.optDouble("point", 0.0)).start();

                            } else {
                                Log.i("zjz", "goodsDetail:解析失败");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

    }

    private void setOnListener() {
        relativeBack.setOnClickListener(this);
        tDetail.setOnClickListener(this);
        relativeHelp.setOnClickListener(this);
    }

    private void initFragment() {
        pointDetailFragment = new PointDetailFragment();
        exchangeRecordFragment = new ExchangeRecordFragment();
        rechargeFragment = new RechargeFragment();
    }

    private void initViewPager() {
        //第三方Tab
        viewPagerAdapter = new CFViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.getDatas().add(rechargeFragment);
        viewPagerAdapter.getDatas().add(exchangeRecordFragment);
        viewPagerAdapter.getDatas().add(pointDetailFragment);
        viewPagerAdapter.getTitle().add(0, "优惠券兑换");
        viewPagerAdapter.getTitle().add(1, "兑换记录");
        viewPagerAdapter.getTitle().add(2, "积分明细");
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        exchangePager.setPageMargin(pageMargin);
        exchangePager.setAdapter(viewPagerAdapter);
        exchangePager.setOffscreenPageLimit(1);
        exchangePager.setCurrentItem(position);
//        pager.setPageTransformer(true, new DepthPageTransformer());
        exchangeTabs.setViewPager(exchangePager);
        exchangeTabs.setIndicatorHeight(Util.dip2px(this, 4));
        exchangeTabs.setTextSize(Util.dip2px(this,16));
        setSelectTextColor(position);
        exchangeTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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

    private void setSelectTextColor(int position) {
        for (int i = 0; i < 3; i++) {
            View view = exchangeTabs.getChildAt(0);
//            if (view instanceof LinearLayout) {
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView != null) {
                if (position == i) {
                    tabTextView.setTextColor(getResources().getColor(R.color.tljr_statusbarcolor));
                } else {
                    tabTextView.setTextColor(getResources().getColor(R.color.hwg_text2));
                }
            }
//            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.t_detail:
                final TextView et = new TextView(this);
                et.setText("\t成功注册会员：增加20积分；会员每天登录：增加5积分；评价完成订单：增加10积分。\n" +
                        "\t购物并付款成功后将获得订单总价3%（最高限额不超过800）积分。\n" +
                        "\t如订单发生退款、退货等问题时，积分将不予退还。");
                et.setTextSize(16);
                AlertDialog dialog = new AlertDialog.Builder(this).setTitle("积分说明")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setView(et)
                        .create();
                dialog.show();
                break;
            case R.id.relative_help:
                initPopWindow(v);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        myBroadCastReceiver.unRegister();
        super.onDestroy();
    }
}
