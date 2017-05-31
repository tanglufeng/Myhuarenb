package com.abcs.huaqiaobang.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.HeaderViewActivity;
import com.abcs.huaqiaobang.activity.InformationActivity;
import com.abcs.huaqiaobang.activity.YuanHuiCenterActivity;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.model.User;
import com.abcs.huaqiaobang.presenter.UserDataInterface;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.RegisterActivity;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.fragment.FragmentMy;
import com.abcs.sociax.t4.android.fragment.FragmentSociax;
import com.astuetz.PagerSlidingTabStrip;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhou on 2016/4/26.
 * 我的页面fragment
 */
public class MyFragment3 extends FragmentSociax implements UserDataInterface, View.OnClickListener {


    @InjectView(R.id.rl_back)
    RelativeLayout rlBack;
    @InjectView(R.id.img_settings)
    ImageView imgSettings;
    @InjectView(R.id.img_vips)
    ImageView img_vips;
    @InjectView(R.id.rl_settings2)
    RelativeLayout rlSettings2;
    @InjectView(R.id.imageView)
    ImageView imageView;
    @InjectView(R.id.tv_VipLever)
    TextView tvVipLever;
    @InjectView(R.id.relative_head)
    RelativeLayout relativeHead;
    @InjectView(R.id.my_tv_name)
    TextView myTvName;
    @InjectView(R.id.my_userId)
    TextView myUserId;
    @InjectView(R.id.t_huati_num)
    TextView tHuatiNum;
    @InjectView(R.id.t_huati)
    TextView tHuati;
    @InjectView(R.id.t_activity_num)
    TextView tActivityNum;
    @InjectView(R.id.t_activity)
    TextView tActivity;
    @InjectView(R.id.t_club_num)
    TextView tClubNum;
    @InjectView(R.id.t_club)
    TextView tClub;
    @InjectView(R.id.t_friends_num)
    TextView tFriendsNum;
    @InjectView(R.id.t_friends)
    TextView tFriends;
    @InjectView(R.id.user_tabs)
    PagerSlidingTabStrip userTabs;
    @InjectView(R.id.linear_tab)
    LinearLayout linearTab;
    @InjectView(R.id.seperate_line)
    View seperateLine;
    @InjectView(R.id.user_pager)
    ViewPager userPager;
    @InjectView(R.id.linear_root)
    LinearLayout linearRoot;
    @InjectView(R.id.relative_top)
    RelativeLayout relativeTop;
    @InjectView(R.id.relative_msg)
    RelativeLayout relativeMsg;
    private Handler handler = new Handler();

    Fragment currentgoodsFragment;
    UserShoppingFragment userShoppingFragment;
    UserMoreFragment userMoreFragment;
    UserFinanceFragment userFinanceFragment;
    CFViewPagerAdapter viewPagerAdapter;
    int currentType;

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);

          view = getActivity().getLayoutInflater().inflate(R.layout.fragment_myuser2, null);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.inject(this, view);

        //设置图片旋转
        imgSettings.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate_settings));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            final Window window = getActivity().getWindow();
////            // Translucent status bar
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
////
//            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,((MainActivity) getActivity()).getStatusBarHeight());
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeTop.getLayoutParams();
//            layoutParams.setMargins(0, ((MainActivity) getActivity()).getStatusBarHeight(), 0, 0);
//            relativeTop.setLayoutParams(layoutParams);
//            RelativeLayout.LayoutParams layoutParams1=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,215+((MainActivity) getActivity()).getStatusBarHeight());
//            relativeMsg.setLayoutParams(layoutParams1);

//            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
//            params.setMargins(0,  getStatusBarHeight(), 0, 0);
//            appbar.setLayoutParams(params);

        }

        setOnListener();
        initUserView();
        initViewPager();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        ViewGroup p = (ViewGroup) view.getParent();
        ButterKnife.inject(this, view);
        if (p != null)
            p.removeAllViewsInLayout();

        EventBus.getDefault().register(this);
        initvipPic();
        return view;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isFinishActivity(String msg){
        Log.i("zds", "isFinishActivity: "+msg);
        if(!TextUtils.isEmpty(msg)&&msg.equals("finish_activity")){
            if(!TextUtils.isEmpty(MyApplication.getInstance().getMykey())){
                initvipPic();
            }
        }
    }



    private  void initvipPic(){

        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_my_order_num, "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            Log.i("zds", "vippic_msg=" + msg);
                            if (object.optInt("code") == 200) {
                                JSONObject datas = object.getJSONObject("datas");
                                String vip_name = datas.optString("vip_name");

                                MyApplication.saveLeveId(vip_name);

                                if(!TextUtils.isEmpty(vip_name)&&vip_name.equals("1")){
                                    img_vips.setImageResource(R.drawable.img_vip_pu_huiyuans);
                                }else if(!TextUtils.isEmpty(vip_name)&&vip_name.equals("2")){
                                    img_vips.setImageResource(R.drawable.img_vip_jin_huiyuans);
                                }else if(!TextUtils.isEmpty(vip_name)&&vip_name.equals("3")){
                                    img_vips.setImageResource(R.drawable.img_vip_baij_huiyuans);
                                }else if(!TextUtils.isEmpty(vip_name)&&vip_name.equals("4")){
                                    img_vips.setImageResource(R.drawable.img_vip_hei_huiyuans);
                                }else {//我的会员
                                    img_vips.setImageResource(R.drawable.img_vip_huiyuans);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }
    @Override
    public int getLayoutId() {
        return 0;
    }

    private void setOnListener() {
        rlSettings2.setOnClickListener(this);
        imageView.setOnClickListener(this);
        img_vips.setOnClickListener(this);

    }

    FragmentMy fragmentMy;
    private void initViewPager() {
        LogUtil.i("testhao","initViewPager");
        viewPagerAdapter = new CFViewPagerAdapter(getFragmentManager());
        viewPagerAdapter.getDatas().add(UserShoppingFragment.newInstance());
//        fragmentMy=new FragmentMy();
//        viewPagerAdapter.getDatas().add(fragmentMy);

        viewPagerAdapter.getDatas().add(UserFinanceFragment.newInstance());
        viewPagerAdapter.getDatas().add(UserMoreFragment.newInstance());
        viewPagerAdapter.getTitle().add(0, "购物");
//        viewPagerAdapter.getTitle().add(1, "社交");
        viewPagerAdapter.getTitle().add(1, "理财");
        viewPagerAdapter.getTitle().add(2, "更多");
        userPager.setAdapter(viewPagerAdapter);
        userPager.setOffscreenPageLimit(1);
//        userPager.setCurrentItem(position);
        userTabs.setViewPager(userPager);
        userTabs.setIndicatorHeight(Util.dip2px(getActivity(), 4));
        userTabs.setTextSize(Util.dip2px(getActivity(), 16));
        setSelectTextColor(0);
        setTextType();
        userTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                setSelectTextColor(position);
                currentgoodsFragment = viewPagerAdapter.getItem(position);
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
        currentgoodsFragment = viewPagerAdapter.getItem(0);
        currentType = 1;
    }

    private void setTextType() {
        for (int i = 0; i < 4; i++) {
            View view = userTabs.getChildAt(0);
//            if (view instanceof LinearLayout) {
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView != null) {
//                SpannableString msp = new SpannableString(tabTextView.getText());
//                msp.setSpan(new RelativeSizeSpan(0.2f),0,msp.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                tabTextView.setText(msp);
//                Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(),
//                        "font/fangzhenglantinghei.TTF");
//                tabTextView.setTypeface(fontFace);
                Util.setFZLTHFont(tabTextView);
            }
//            }
        }
    }

    private void setSelectTextColor(int position) {
        for (int i = 0; i < 4; i++) {
            View view = userTabs.getChildAt(0);
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

    private void initUserView() {

        User self = MyApplication.getInstance().self;
        if (self != null) {

            ImageLoader.getInstance().displayImage(self.getAvatarUrl(), imageView, Options.getCircleListOptions());

            myTvName.setText(self.getNickName().equals(self.getId()) ? self.getUserName() : self.getNickName());
            if (!Util.isThirdLogin) {
                myTvName.setOnClickListener(this);
            }
            myUserId.setText("ID：" + self.getId());
            getUserBanks();
//            getUserCash();
        } else {
            SpannableString spanstr = new SpannableString("登录/注册");
//        spanstr.setSpan(new ForegroundColorSpan(Color.BLUE), 0, spanstr.length(),
//
//                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spanstr.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.WHITE);       //设置文件颜色
                    ds.setUnderlineText(false);      //设置下划线
                }

                @Override
                public void onClick(View widget) {

                    startActivity(new Intent(getContext(), WXEntryActivity.class));
                }
            }, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanstr.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.WHITE);       //设置文件颜色
                    ds.setUnderlineText(false);      //设置下划线
                }

                @Override
                public void onClick(View widget) {

                    startActivity(new Intent(getContext(), RegisterActivity.class));
                }
            }, 3, spanstr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            myTvName.setHighlightColor(Color.TRANSPARENT);
            myTvName.setText(spanstr+"");
            myTvName.setMovementMethod(LinkMovementMethod.getInstance());
//            tZichanNum.setText("0.00");
//            tShouyiNum.setText("0.00");
//            tYesterdayNum.setText("0.00");
            myUserId.setText("");
            imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.img_hwg_my_nologin));
        }
    }

    public void getUserCash() {
        LogUtil.e(
                "getUserCash",
                "method=getUserInfo&uid="
                        + MyApplication.getInstance().self.getId() + "&token="
                        + Util.token);
        HttpRequest.sendPost(TLUrl.getInstance().URL_userServlet, "method=getUserInfo&uid="
                + MyApplication.getInstance().self.getId() + "&token="
                + Util.token, new HttpRevMsg() {

            @Override
            public void revMsg(String msg) {
                LogUtil.e("getUserCash", msg);
                if (msg.equals("") && MyApplication.getInstance().self != null) {
                    getUserCash();
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(msg);
                    if (jsonObject.getInt("status") == 1) {
                        JSONObject object = jsonObject.getJSONObject("msg");
                        MyApplication.getInstance().self
                                .setAllEarnings((float) object
                                        .getDouble("allEarnings"));
                        MyApplication.getInstance().self
                                .setEarningsYesterday((float) object
                                        .getDouble("earningsYesterday"));
                        MyApplication.getInstance().self.setInvestCount(object
                                .getInt("investCount"));
                        MyApplication.getInstance().self
                                .setTotalAssets((float) object
                                        .getDouble("totalAssets"));
                        MyApplication.getInstance().self
                                .setTotalInvest((float) object
                                        .getDouble("totalInvest"));
                        // {"msg":{"allEarnings":86,"createDate":1445774262045,"earningsYesterday":29,"investCount":4,"totalAssets":121686,"totalInvest":121600,"uid":"10017"},"status":1}
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
//                                tZichanNum.setText(Util.df.format(MyApplication.getInstance().self.getTotalAssets() / 100));
//                                tYesterdayNum.setText(Util.df.format(MyApplication.getInstance().self.getEarningsYesterday() / 100));
//                                tShouyiNum.setText(Util.df.format(MyApplication.getInstance().self.getAllEarnings() / 100));
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getUserBanks() {
//        LogUtil.e("getUserBanks", "method=requestBanks&identityid="
//                + MyApplication.getInstance().self.getId() + "&token="
//                + Util.token);
        HttpRequest.sendPost(TLUrl.getInstance().URL_bindBank,
                "method=requestBanks&identityid="
                        + MyApplication.getInstance().self.getId() + "&token="
                        + Util.token, new HttpRevMsg() {

                    @Override
                    public void revMsg(String msg) {
                        LogUtil.e("getUserBanks", msg);
                        if (msg.equals("")
                                && MyApplication.getInstance().self != null) {
//                            getUserBanks();
                            return;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.getInt("status") == 1) {
                                MyApplication.getInstance().self
                                        .setBanks(jsonObject
                                                .getJSONArray("msg"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initIntentData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

//    @OnClick({R.id.rl_order_noPay, R.id.rl_order_goods, R.id.rl_order_delivery
//            , R.id.rl_order_evaluation, R.id.rl_order_tuihuo, R.id.rl_allOrder,
//            R.id.rl_customer, R.id.rl_record, R.id.rl_product, R.id.imageView,
//            R.id.rl_myCollection, R.id.rl_myshaidan, R.id.rl_myCart, R.id.rl_more_licai
//            , R.id.rl_myCard})
//    public void onClick(View view) {
//        Intent intent;
//        if (MyApplication.getInstance().self == null) {
//            Toast.makeText(getContext(), "请登录", Toast.LENGTH_SHORT).show();
//            intent = new Intent(getActivity(), WXEntryActivity.class);
//            startActivity(intent);
//            return;
//        }
//        switch (view.getId()) {
//            //待支付
//            case R.id.rl_order_noPay:
//                intent = new Intent(getActivity(), OrderActivity.class);
//                intent.putExtra("position", 1);
//                startActivity(intent);
//                break;
//            //待收货
//            case R.id.rl_order_goods:
//                intent = new Intent(getActivity(), OrderActivity.class);
//                intent.putExtra("position", 3);
//                startActivity(intent);
//                break;
//            //待发货
//            case R.id.rl_order_delivery:
//                intent = new Intent(getActivity(), OrderActivity.class);
//                intent.putExtra("position", 2);
//                startActivity(intent);
//                break;
//            //待评价
//            case R.id.rl_order_evaluation:
//                intent = new Intent(getActivity(), OrderActivity.class);
//                intent.putExtra("position", 5);
//                startActivity(intent);
//                break;
//            //退货
//            case R.id.rl_order_tuihuo:
//                intent = new Intent(getActivity(), ReturnAndRefundActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.rl_allOrder:
//                intent = new Intent(getActivity(), OrderActivity.class);
//                intent.putExtra("position", 0);
//                startActivity(intent);
//                break;
//
//            case R.id.rl_product:
//
//                startActivity(new Intent(getContext(), DingQiActivity.class));
//                break;
//            case R.id.rl_more_licai:
//
//                intent = new Intent(getActivity(), MyProductActivity.class);
//                startActivity(intent);
//                break;
//            //交易记录
//            case R.id.rl_record:
//                intent = new Intent(getActivity(), RecordActivity.class);
//                startActivity(intent);
//                break;
//            //客户群
//            case R.id.rl_customer:
//                if (MyApplication.getInstance().self != null) {
//                    intent = new Intent(getActivity(), MyCustomer.class);
//                    String param = "method=getoffline&uid=" + MyApplication.getInstance().self.getId() + "&token=" + Util.token;
//                    HttpRequest.sendGet(TLUrl.getInstance().URL_customer, param, new HttpRevMsg() {
//                        @Override
//                        public void revMsg(final String msg) {
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    com.alibaba.fastjson.JSONObject custom = com.alibaba.fastjson.JSONObject.parseObject(msg);
//
//                                    if (custom.size() != 0 && custom.getInteger("status") == 1) {
//
//                                        startActivity(new Intent(getActivity(), MyCustomer.class).putExtra("msg", msg));
//
//                                    } else {
//                                        startActivity(new Intent(getActivity(), NoticeDialogActivity.class).putExtra("msg", "您还没有该权限"));
//                                    }
//                                }
//                            });
//
//                        }
//                    });
//                }
//                break;
//
//            //修改头像
//            case R.id.imageView:
//
//                intent = new Intent(getActivity(), HeaderViewActivity.class);
//                startActivityForResult(intent, 1);
//
//                break;
//
//            case R.id.rl_myCollection:
//
//                intent = new Intent(getActivity(), CollectionActivity.class);
//                startActivity(intent);
//
//                break;
//            case R.id.rl_myshaidan:
//
//                intent = new Intent(getActivity(), CommentAndShareActivity.class);
//                startActivity(intent);
//
//                break;
//            case R.id.rl_myCart:
//
//                intent = new Intent(getActivity(), CartActivity2.class);
//                startActivity(intent);
//
//                break;
//            case R.id.rl_myCard:
//
//                intent = new Intent(getActivity(), MyCardActivity.class);
//                startActivity(intent);
//
//                break;
//
//
//        }
//
//    }

    @Override
    public void loginSuccess() {
//        UserFinanceFragment.newInstance().loginSuccess();
//        UserMoreFragment.newInstance().loginSuccess()
        initUserView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //修改头像回调
        if (resultCode == 1 && requestCode == 1) {

            ImageLoader.getInstance().displayImage(MyApplication.getInstance().self.getAvatarUrl()
                    , imageView, Options.getCircleListOptions());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.rl_settings2:
                userPager.setCurrentItem(3);
                break;
            case R.id.img_vips:  //  vip 会员
//                img_vips.setVisibility(View.VISIBLE);  // 如果是会员

                    intent = new Intent(getActivity(), YuanHuiCenterActivity.class);
                    startActivity(intent);

                break;
            case R.id.imageView:
                if (Util.isThirdLogin) {
                    return;
                }
                if (MyApplication.getInstance().self == null) {
                    Toast.makeText(getContext(), "请登录", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(getActivity(), HeaderViewActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.my_tv_name:
                if (Util.isThirdLogin) {
                    return;
                }
                intent = new Intent(getActivity(), InformationActivity.class);
                startActivity(intent);
                break;
        }
    }

//    @OnClick({R.id.rl_settings, R.id.rl_settings2})
//    public void onSettings(View view) {
//
//        switch (view.getId()) {
//            case R.id.rl_settings:
//
//                startActivity(new Intent(getContext(), SettinsActivity.class));
//                break;
//            case R.id.rl_settings2:
//
//                startActivity(new Intent(getContext(), SettinsActivity.class));
//                break;
//
//
//        }
//
//    }
}
