package com.abcs.haiwaigou.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.abcs.haiwaigou.activity.KefuActivity;
import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.haiwaigou.local.activity.CountryCityActivity;
import com.abcs.haiwaigou.local.activity.LoacalKeFuActivity;
import com.abcs.haiwaigou.local.activity.LocalZiXunDetialsActivity;
import com.abcs.haiwaigou.local.activity.MessageMoreActivity;
import com.abcs.haiwaigou.local.activity.MyPublishActivity;
import com.abcs.haiwaigou.local.activity.NewMoreActivity;
import com.abcs.haiwaigou.local.activity.NewsMoreActivity;
import com.abcs.haiwaigou.local.activity.PublishActivity;
import com.abcs.haiwaigou.local.activity.WXNewsMoreActivity;
import com.abcs.haiwaigou.local.beans.ActivitysBean;
import com.abcs.haiwaigou.local.beans.MsgViewFlip;
import com.abcs.haiwaigou.local.beans.New;
import com.abcs.haiwaigou.local.beans.News;
import com.abcs.haiwaigou.local.fragment.LocalMenuFragment;
import com.abcs.haiwaigou.local.fragment.LocalTeDianFragment;
import com.abcs.haiwaigou.local.huohang.view.NoticeActivity;
import com.abcs.haiwaigou.model.BannersBean;
import com.abcs.haiwaigou.model.GGAds;
import com.abcs.haiwaigou.model.LocalModel;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.view.MyGridView;
import com.abcs.haiwaigou.view.zjzbanner.LMBanners;
import com.abcs.haiwaigou.view.zjzbanner.adapter.LBaseAdapter;
import com.abcs.haiwaigou.view.zjzbanner.transformer.TransitionEffect;
import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.adapter.CeSuAdapter;
import com.abcs.hqbtravel.adapter.TravelZhenXuanAdapter;
import com.abcs.hqbtravel.entity.ZhenXuan;
import com.abcs.hqbtravel.view.activity.SeachYouJiByTagActivity;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.GuanggaoActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.NoDoubleClickListener;
import com.abcs.huaqiaobang.util.ServerUtils;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.CircleImageView;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.huaqiaobang.ytbt.common.utils.ToastUtil;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.ActivityHome;
import com.abcs.sociax.t4.android.bean.CeSu;
import com.abcs.sociax.t4.android.fragment.FragmentSociax;
import com.abcs.sociax.t4.android.weiba.ActivityWeibaCommon;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.common.Logger;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.sociax.thinksnsbase.activity.widget.EmptyLayout;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.abcs.haiwaigou.model.LocalModel.getActivityList;
import static com.abcs.haiwaigou.model.LocalModel.getMsgList;
import static com.abcs.haiwaigou.model.LocalModel.getNewList;
import static com.abcs.haiwaigou.model.LocalModel.hb;
import static com.abcs.haiwaigou.model.LocalModel.hl;
import static com.abcs.haiwaigou.model.LocalModel.hq_num;
import static com.abcs.haiwaigou.model.LocalModel.sy_num;
import static com.abcs.haiwaigou.model.LocalModel.tian_qi_img;
import static com.abcs.haiwaigou.model.LocalModel.tian_qi_txt;
import static com.abcs.haiwaigou.model.LocalModel.tian_qi_wendu;
import static com.abcs.sociax.t4.android.video.ToastUtils.showToast;

/**
 * Created by zjz on 2016/8/27.
 */
public class LocalFragment2 extends FragmentSociax implements View.OnClickListener, LocalModel.LoadStateInterface {
    @InjectView(R.id.iv_gg_new1)
    ImageView ivGgNew1;
    @InjectView(R.id.iv_gg_new2)
    ImageView ivGgNew2;
    @InjectView(R.id.iv_gg_new3)
    ImageView ivGgNew3;
    @InjectView(R.id.iv_gg_new4)
    ImageView ivGgNew4;
    @InjectView(R.id.ll_guanggao3)
    LinearLayout llGuanggao3;
    @InjectView(R.id.iv_gg_new5)
    ImageView ivGgNew5;
    @InjectView(R.id.iv_gg_new6)
    ImageView ivGgNew6;
    @InjectView(R.id.iv_gg_new7)
    ImageView ivGgNew7;
    @InjectView(R.id.iv_gg_new8)
    ImageView ivGgNew8;
    @InjectView(R.id.ll_guanggao4)
    LinearLayout llGuanggao4;
    @InjectView(R.id.iv_gg_new9)
    ImageView ivGgNew9;
    @InjectView(R.id.iv_gg_new10)
    ImageView ivGgNew10;
    @InjectView(R.id.iv_gg_new11)
    ImageView ivGgNew11;
    @InjectView(R.id.iv_gg_new12)
    ImageView ivGgNew12;
    @InjectView(R.id.ll_guanggao5)
    LinearLayout llGuanggao5;

    ArrayList<ImageView> iv_gg_news=new ArrayList<>();


    @Override
    public int getLayoutId() {
        return R.layout.hqb_local_fragment22;
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

    @InjectView(R.id.grid_brand)
    MyGridView gridBrand;
    @InjectView(R.id.ll_new)
    LinearLayout llNew;
    @InjectView(R.id.ll_new_more)
    LinearLayout llNewMore;
    @InjectView(R.id.t_title)
    TextView tTitle;
    @InjectView(R.id.linner_toutiao)
    LinearLayout linner_toutiao;
    @InjectView(R.id.relative_title)
    LinearLayout relativeTitle;
    @InjectView(R.id.view_pager)
    ViewPager viewPager;
    @InjectView(R.id.view_pager_g)
    ViewPager viewPager_g;
    @InjectView(R.id.line1)
    View line1;
    @InjectView(R.id.linear_message_more)
    LinearLayout linearMessageMore;
    @InjectView(R.id.linear_message)
    LinearLayout linearMessage;
    @InjectView(R.id.linear_news_more)
    LinearLayout linearNewsMore;
    @InjectView(R.id.linear_news)
    LinearLayout linearNews;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.t_select)
    TextView tSelect;
    @InjectView(R.id.relative_no_city)
    RelativeLayout relativeNoCity;
    @InjectView(R.id.linear_wxnews_more)
    LinearLayout linearWxnewsMore;
    @InjectView(R.id.linear_wxnews)
    LinearLayout linearWxnews;
    @InjectView(R.id.banners)
    LMBanners banners;
    @InjectView(R.id.iv_dain1)
    ImageView iv_dain1;
    @InjectView(R.id.iv_dain2)
    ImageView iv_dain2;
    @InjectView(R.id.iv_dain1_g)
    ImageView iv_dain1_g;
    @InjectView(R.id.iv_dain2_g)
    ImageView iv_dain2_g;
    @InjectView(R.id.iv_gg1)
    ImageView ivGg1;
    @InjectView(R.id.iv_gg3)
    ImageView ivGg3;
    @InjectView(R.id.iv_gg4)
    ImageView ivGg4;
    @InjectView(R.id.iv_gg5)
    ImageView ivGg5;
    @InjectView(R.id.iv_gg6)
    ImageView ivGg6;
    @InjectView(R.id.iv_gg7)
    ImageView ivGg7;
    @InjectView(R.id.iv_gg8)
    ImageView ivGg8;
    @InjectView(R.id.scrollbanner)
    ViewFlipper scrollbanner;
    /*    @InjectView(R.id.local_close)
        ImageView localClose;*/
    @InjectView(R.id.imge_ttou)
    ImageView imge_ttou;
    /*  @InjectView(R.id.local_tv_city_name)
      TextView localTvCityName;*/
  /*  @InjectView(R.id.local_civ_head)
    CircleImageView localCivHead;*/
    /*@InjectView(R.id.local_iv_shiming)
    ImageView localIvShiming;*/
    /*@InjectView(R.id.local_tv_shiming)
    TextView localTvShiming;*/
   /* @InjectView(R.id.local_iv_guanfang)
    ImageView localIvGuanfang;*/
 /*   @InjectView(R.id.local_tv_guanfang)
    TextView localTvGuanfang;*/
   /* @InjectView(R.id.local_tv_name)
    TextView localTvName;*/
    @InjectView(R.id.local_rl_ad)
    RelativeLayout localRlAd;
    @InjectView(R.id.rl)
    RelativeLayout rl;
    /* @InjectView(R.id.city_partner)
     TextView cityPartner;*/
    @InjectView(R.id.mainScrollView)
    ScrollView mainScrollView;
    @InjectView(R.id.ll_guanggao1)
    LinearLayout llGuanggao1;
    @InjectView(R.id.ll_guanggao2)
    LinearLayout llGuanggao2;
    @InjectView(R.id.linear_change)
    LinearLayout linearChange;
    @InjectView(R.id.liner_ser)
    LinearLayout linerSer;
    @InjectView(R.id.tv_huaren_num)
    TextView tvHuarenNum;
    @InjectView(R.id.tv_shangye)
    TextView tvShangye;
    @InjectView(R.id.tv_huilv)
    TextView tvHuilv;
    @InjectView(R.id.tv_city_name)
    TextView tvCityName;
    @InjectView(R.id.tv_jiaotou)
    TextView tv_jiaotou;
    @InjectView(R.id.tv_location)
    TextView tv_location;
    @InjectView(R.id.tv_tianqi)
    TextView tvTianqi;
    @InjectView(R.id.tv_tianqi_des)
    TextView tvTianqi_des;
    @InjectView(R.id.iv_yun)
    ImageView ivYun;
    @InjectView(R.id.listview_zhenxuan)
    ListView listview_zhenxuan;
    @InjectView(R.id.cesu)
    ImageView cesu;
    @InjectView(R.id.gotomap)
    ImageView gotomap;
    @InjectView(R.id.lin_se_ke)
    LinearLayout linSeKe;
    @InjectView(R.id.lin_tian_yun)
    LinearLayout linTianYun;
    @InjectView(R.id.emptry_layout)
    EmptyLayout emptryLayout;
    @InjectView(R.id.liner_youxuan)
    LinearLayout liner_youxuan;
    @InjectView(R.id.liner_banner)
    LinearLayout liner_banner;
    @InjectView(R.id.relative_search_j)
    RelativeLayout relative_search_j;
    @InjectView(R.id.iv_qian_huiren_logo)
    ImageView iv_qian_huiren_logo;  // 仟慧人logo  可点击
    @InjectView(R.id.setting)
    ImageView setting;
    @InjectView(R.id.line)
    View line;
    // 右上角消息
    @InjectView(R.id.tv_xiaoxi_num)
    TextView tv_xiaoxi_num;
    @InjectView(R.id.relat_xx)
    RelativeLayout relat_xx;
    @InjectView(R.id.iv_weini_youxuan)
    ImageView iv_weini_youxuan;

    @InjectView(R.id.liner_kefu_logo)
    LinearLayout liner_kefu_logo;
    @InjectView(R.id.liner_weixin_logo)
    LinearLayout liner_weixin_logo;

    private String menuId;
    private List<ImageView> images = new ArrayList<>();
    private View view;
    private ActivityHome activity;
    LocalModel localModel;
    private ACache aCache;
    private ArrayList<News> newsList = new ArrayList<News>();
    public static LocalFragment2 localFragment;
    private String lastTime;
    private String lastId;
    private String cityName;
    private String cityId;
    private Handler handler = new Handler();
    CFViewPagerAdapter viewPagerAdapter;
//    CFViewPagerAdapter viewPagerGAdapter;
//    ChangCityDialog chanDialog;

    public static LocalFragment2 newInstance() {
        if (localFragment == null) {
            localFragment = new LocalFragment2();
        }
        return localFragment;
    }

    /**
     * 热点资讯
     */
    private void initNewList() {
        if (getNewList().size() == 0) {
            llNew.setVisibility(View.GONE);
        } else {
            llNew.setVisibility(View.VISIBLE);
            if (getNewList().size() != 0 && llNew.getChildCount() > 1) {
                llNew.removeViews(1, llNew.getChildCount() - 1);
                llNew.invalidate();
            }

            for (int i = 0; i < getNewList().size(); i++) {
                if (i < 6) {
                    llNew.addView(getNewView(i, getNewList().get(i)), i + 1);
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.hqb_local_fragment22, container, false);
        activity = (ActivityHome) getActivity();
        ButterKnife.inject(this, view);
        aCache = ACache.get(activity);
       /* ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeAllViewsInLayout();
        ButterKnife.inject(this, view);*/
        initMyViews();
        initMyListen();
        initMyData();
        return view;
    }

    private void initMyListen() {
        linearMessageMore.setOnClickListener(this);
        linearNewsMore.setOnClickListener(this);
        relat_xx.setOnClickListener(this);
        linearWxnewsMore.setOnClickListener(this);
        llNewMore.setOnClickListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (localModel != null) {

                    if (!ServerUtils.isConnect(activity)) {
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                showToast("请检查您的网络");
                            }
                        });
                        return;
                    }
                    localModel.initDatas(cityId);
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });


        mainScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        Log.i("down:", event.getY() + "");
                        //隐藏
                        localRlAd.setVisibility(View.GONE);
                        rl.setVisibility(View.GONE);
                        break;
                    case MotionEvent.ACTION_UP:
                        //显示
                        localRlAd.setVisibility(View.VISIBLE);
                        rl.setVisibility(View.VISIBLE);
                        break;
                }
                return false;
            }
        });


        tSelect.setOnClickListener(this);

        //广告栏点击事件
        localRlAd.setOnClickListener(this);
//        localClose.setOnClickListener(this);
    }

    private static final String TAG = "LocalFragment2";

    private void initMyViews() {

        Log.i(TAG, "initMyViews: ");
        images.add(ivGg1);
        images.add(ivGg3);
        images.add(ivGg4);
        images.add(ivGg5);
        images.add(ivGg6);
        images.add(ivGg7);
        images.add(ivGg8);
        images.add(ivGgNew9);
        images.add(ivGgNew11);
        images.add(ivGgNew10);
        images.add(ivGgNew12);
        /////////////////////////
        iv_gg_news.clear();
        iv_gg_news.add(ivGgNew1);
        iv_gg_news.add(ivGgNew3);
        iv_gg_news.add(ivGgNew2);
        iv_gg_news.add(ivGgNew4);
        iv_gg_news.add(ivGgNew5);
        iv_gg_news.add(ivGgNew7);
        iv_gg_news.add(ivGgNew6);
        iv_gg_news.add(ivGgNew8);

//        chanDialog = new ChangCityDialog(activity);
        localModel = new LocalModel(activity, this);
        viewPagerAdapter = new CFViewPagerAdapter(getChildFragmentManager());
//        viewPagerGAdapter = new CFViewPagerAdapter(getChildFragmentManager());
    }


    /****
     * 获取设置的列表
     */
    private void settingPop() {

        View itemView = View.inflate(activity, R.layout.popup_quanqiu_setting, null);
        LinearLayout line_messge = (LinearLayout) itemView.findViewById(R.id.line_messge);
        LinearLayout line_cs = (LinearLayout) itemView.findViewById(R.id.line_cs);
        LinearLayout line_fb = (LinearLayout) itemView.findViewById(R.id.line_fb);

        final PopupWindow pop = new PopupWindow(activity);
        pop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        pop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        pop.setContentView(itemView);

        pop.setAnimationStyle(R.style.popWindowAnimation);//设置弹出和消失的动画
        //触摸点击事件
        pop.setTouchable(true);
        //聚集
        pop.setFocusable(true);
        //设置允许在外点击消失
        pop.setOutsideTouchable(true);
        //点击返回键popupwindown消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        //背景变暗
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.5f;
        activity.getWindow().setAttributes(params);
        pop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        //监听如果popupWindown消失之后背景变亮
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow()
                        .getAttributes();
                params.alpha = 1f;
                activity.getWindow().setAttributes(params);
            }
        });
        pop.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        pop.showAsDropDown(setting);

        line_cs.setOnClickListener(new View.OnClickListener() {  // 测速
            @Override
            public void onClick(View v) {
                initCeSuPop();
                pop.dismiss();
            }
        });
        line_fb.setOnClickListener(new View.OnClickListener() {  // 我的发布
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(cityId)) {
                    ToastUtil.showMessage("换个城市试试！");
                    return;
                }
                Intent intent = null;
                if (localModel != null && MyApplication.getInstance().self != null) {
//                     intent = new Intent(activity, PublishActivity.class);
                    intent = new Intent(activity, MyPublishActivity.class);
                    intent.putExtra("menuId", menuId);
                    intent.putExtra("cityId", cityId);
                    intent.putParcelableArrayListExtra("menuList", localModel.getMenuList());
                    startActivity(intent);
                } else {
                    intent = new Intent(activity, WXEntryActivity.class);
                    startActivity(intent);
                }
                pop.dismiss();
            }
        });
        line_messge.setOnClickListener(new View.OnClickListener() { //  我的消息
            @Override
            public void onClick(View v) {
                Intent intent;
                if (TextUtils.isEmpty(MyApplication.getInstance().getMykey())) {
                    intent = new Intent(activity, WXEntryActivity.class);
                    intent.putExtra("isthome",true);
                } else {
                    intent = new Intent(activity, NoticeActivity.class);
                }
                startActivity(intent);
                pop.dismiss();
            }
        });

    }

    private void isHasLocation(String lat, String lng) {

//        http://120.24.19.29:7075/find/getCityBylnglat?lng=114.231234&lat=22.123456

        Log.i("zds", "isHasLocation: lat" + lat);
        Log.i("zds", "isHasLocation: lng" + lng);

        HttpRequest.sendGet(Contonst.HOST + "/find/getCityBylnglat", "lat=" + lat + "&lng=" + lng, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds", "run: " + msg);
                        if (TextUtils.isEmpty(msg)) {
                            loadLocal();
                            return;
                        } else {
                            try {
                                JSONObject mainObject = new JSONObject(msg);

                                if (mainObject.optInt("result") == 1) {

                                    JSONObject objest = mainObject.optJSONObject("body");
                                    if (objest != null) {

                                        if (objest.optBoolean("isChina")) {  // 中国

                                            ChangeNetUrl(TLUrl.URL_CHIAN);
                                            MyApplication.savePosition(0);

                                            listview_zhenxuan.setVisibility(View.VISIBLE);
                                            swipeRefreshLayout.setVisibility(View.GONE);
                                            getZhenXuan();
                                            tvCityName.setText("国内");
                                            tTitle.setText("华人邦臻选");
                                            tv_location.setVisibility(View.GONE);
                                            linSeKe.setVisibility(View.VISIBLE);
                                            linTianYun.setVisibility(View.GONE);
                                            tv_jiaotou.setBackgroundResource(R.drawable.img_changcity_downs);
                                            tvCityName.setTextColor(activity.getResources().getColor(R.color.gray));
                                            tTitle.setTextColor(activity.getResources().getColor(R.color.white));
                                            relativeTitle.setBackgroundResource(R.color.bg_edit_color);
                                            //隐藏
                                            localRlAd.setVisibility(View.GONE);
                                            rl.setVisibility(View.GONE);

                                        } else {

                                            if (objest.has("area")) {  // 判断各大洲 切换服务器

                                                if (objest.optString("area").equals("亚洲")) {
                                                    ChangeNetUrl(TLUrl.URL_DONGNANYA);
                                                    MyApplication.savePosition(1);
                                                } else if (objest.optString("area").equals("欧洲")) {
                                                    ChangeNetUrl(TLUrl.URL_OUZHOU);
                                                    MyApplication.savePosition(2);
                                                } else if (objest.optString("area").equals("大洋洲")) {
                                                    ChangeNetUrl(TLUrl.URL_DONGNANYA);
                                                    MyApplication.savePosition(1);
                                                } else {
                                                    ChangeNetUrl(TLUrl.URL_OUZHOU);
                                                    MyApplication.savePosition(2);
                                                }
                                            }


                                            loadLocal();
                                            listview_zhenxuan.setVisibility(View.GONE);
                                            swipeRefreshLayout.setVisibility(View.VISIBLE);

                                            tvCityName.setTextColor(activity.getResources().getColor(R.color.local_text_left));
                                            tTitle.setTextColor(activity.getResources().getColor(R.color.white));

                                            tv_location.setVisibility(View.VISIBLE);
                                            linSeKe.setVisibility(View.VISIBLE);
                                            linTianYun.setVisibility(View.GONE);
                                            tv_jiaotou.setBackgroundResource(R.drawable.iv_bendi_jian_down);
                                            relativeTitle.setBackgroundResource(R.color.white);
                                            //隐藏
                                            localRlAd.setVisibility(View.VISIBLE);
                                            rl.setVisibility(View.VISIBLE);

                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                loadLocal();
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }


    ArrayList<MsgViewFlip> msgViewFlips = new ArrayList<>();

    private void initMsgList(String cityId) {
        if (getMsgList().size() == 0) {
            linner_toutiao.setVisibility(View.GONE);
            line1.setVisibility(View.GONE);
        } else {
            linner_toutiao.setVisibility(View.VISIBLE);
            line1.setVisibility(View.VISIBLE);

            msgViewFlips.clear();
            if (getMsgList().size() % 2 == 0) {  // 集合偶数
                for (int i = 0; i < getMsgList().size() / 2; i++) {
                    MsgViewFlip ben = new MsgViewFlip();
                    ben.title2 = getMsgList().get(i).getTitle();
                    Logger.logI("zds====title2", getMsgList().get(i).getTitle() + "");
                    ben.title = getMsgList().get(i + 1).getTitle();
                    Logger.logI("zds====title", getMsgList().get(i + 1).getTitle() + "");
                    msgViewFlips.add(ben);
                }
            } else { // 集合奇数
                for (int i = 0; i < (getMsgList().size() - 1) / 2; i++) {
                    MsgViewFlip ben = new MsgViewFlip();
                    ben.title2 = getMsgList().get(i).getTitle();
                    Logger.logI("zds====title2", getMsgList().get(i).getTitle() + "");
                    ben.title = getMsgList().get(i + 1).getTitle();
                    Logger.logI("zds====title", getMsgList().get(i + 1).getTitle() + "");
                    msgViewFlips.add(ben);
                }

                MsgViewFlip ben = new MsgViewFlip();
                ben.title2 = getMsgList().get(getMsgList().size() - 1).getTitle();
                msgViewFlips.add(ben);
            }


            if (msgViewFlips.size() != 0) {
                initViewFliper(msgViewFlips, cityId);
                line.setVisibility(View.VISIBLE);
            } else {
                line.setVisibility(View.GONE);
            }
//            initViewFliper(getMsgList());
        }
    }

    private void initViewFliper(ArrayList<MsgViewFlip> msgList, final String cityId) {

        scrollbanner.removeAllViews();
        for (int i = 0; i < msgList.size(); i++) {

            final MsgViewFlip msg = msgList.get(i);
            View view = inflater.inflate(R.layout.local_item_new_viewflip, null);
            TextView t_dain = (TextView) view.findViewById(R.id.t_dain);
            TextView t_dain2 = (TextView) view.findViewById(R.id.t_dain2);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            TextView tv_title2 = (TextView) view.findViewById(R.id.tv_title2);

            if (!TextUtils.isEmpty(msg.title)) {
                tv_title.setText(msg.title);
                t_dain.setVisibility(View.VISIBLE);
            } else {
                t_dain.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(msg.title2)) {
                tv_title2.setText(msg.title2);
                t_dain2.setVisibility(View.VISIBLE);
            } else {
                t_dain2.setVisibility(View.GONE);
            }

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                   /* Intent intent = null;
                    intent = new Intent(activity, HireAndFindDetailActivity.class);
                    intent.putExtra("isMsg", true);
                    intent.putParcelableArrayListExtra("datas", getMsgList());
                    intent.putExtra("conId", msg.getId());
                    activity.startActivity(intent);*/

                    Log.i("zds", "onClick: cityId" + cityId);
                    Intent intent = new Intent(activity, MessageMoreActivity.class);
                    intent.putExtra("cityId", cityId);
                    startActivity(intent);

                }
            });


            scrollbanner.addView(view);
        }

        // 设置动画开始滚动
        scrollbanner.setInAnimation(activity, R.anim.vp_bottom_in_activity);
        scrollbanner.setOutAnimation(activity, R.anim.vp_bottom_out_activity);
        scrollbanner.setFlipInterval(3000);
        scrollbanner.startFlipping();
    }

    private boolean idFirst = true;

    private void initMenuList(final String cityId) {

        if (idFirst) {
            initViewPager(cityId);
            idFirst = false;
        }
    }


    private final List<CeSu> ceSuList = new ArrayList<>();

    private void initWanSu() {
        ceSuList.clear();
//        http://www.huaqiaobang.com/mobile/index.php?act=test_cy&op=find_server

        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base + "/mobile/index.php", "act=test_cy&op=find_server", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.i("cesu", msg + "");
                        if (!TextUtils.isEmpty(msg)) {

                            try {
                                JSONArray jsonArray = new JSONArray(msg);
                                Log.i("cesujsonArray", jsonArray.length() + "");
                                if (jsonArray != null && jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        final JSONObject object1 = jsonArray.getJSONObject(i);
                                        final CeSu ceSu = new CeSu();
                                        Log.i("cesuname", object1.optString("server_name") + "");
                                        Log.i("cesuurl", object1.optString("server_url") + "");

                                        ceSu.setId(object1.optString("id"));
                                        ceSu.setServerName(object1.optString("server_name"));
                                        ceSu.setServerUrl(object1.optString("server_url"));

                                        ceSuList.add(ceSu);
                                    }
                                    MyApplication.setCeSuList(ceSuList);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
            }
        });
    }

    boolean isFirst = true;

    public void initMyData() {
        if (ServerUtils.isConnect(activity)) {

            relativeNoCity.setVisibility(View.GONE);

            //初始百度lbs
            initBaiduLbs();
            if (!TextUtils.isEmpty(MyApplication.my_current_lat) && !TextUtils.isEmpty(MyApplication.my_current_lng)) {  // 定位成功
                isHasLocation(MyApplication.my_current_lat, MyApplication.my_current_lng);
            } else {
                ChangeNetUrl(TLUrl.URL_OUZHOU);
                MyApplication.savePosition(2); //默认选欧洲
                loadLocal();
            }
            initWanSu();
        } else {
            relativeNoCity.setVisibility(View.VISIBLE);
            ToastUtil.showMessage("请检查网络！");
        }
    }

    private void isChina() {
        listview_zhenxuan.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
        getZhenXuan();
        tvCityName.setText("国内");
        tTitle.setText("华人邦臻选");
        tv_location.setVisibility(View.GONE);
        linSeKe.setVisibility(View.VISIBLE);
        linTianYun.setVisibility(View.GONE);
        tv_jiaotou.setBackgroundResource(R.drawable.img_changcity_downs);
        tvCityName.setTextColor(activity.getResources().getColor(R.color.gray));
        tTitle.setTextColor(activity.getResources().getColor(R.color.white));
        relativeTitle.setBackgroundResource(R.color.bg_edit_color);
        //隐藏
        localRlAd.setVisibility(View.GONE);
        rl.setVisibility(View.GONE);
    }

    private void UpteloadData(String cate_name, String city_id) {
        Log.i("cityName:  ", cate_name);
        Log.i("cityId:  ", city_id + "");

        listview_zhenxuan.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);

        tvCityName.setTextColor(activity.getResources().getColor(R.color.local_text_left));
        tTitle.setTextColor(activity.getResources().getColor(R.color.white));

        tv_location.setVisibility(View.VISIBLE);
        linSeKe.setVisibility(View.VISIBLE);
        linTianYun.setVisibility(View.GONE);
        tv_jiaotou.setBackgroundResource(R.drawable.iv_bendi_jian_down);
        relativeTitle.setBackgroundResource(R.color.white);

        //隐藏
        localRlAd.setVisibility(View.VISIBLE);
        rl.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(Util.preference.getString(MyString.LOCAL_COUNTRY_NAME, ""))) {
            tvCityName.setText(Util.preference.getString(MyString.LOCAL_COUNTRY_NAME, ""));
        } else {
            tvCityName.setText(cate_name);
        }
        tTitle.setText(cate_name);
        cityId = city_id + "";

        ProgressDlgUtil.showProgressDlg("", activity);
        localModel.initDatas(cityId);
    }

    private void loadLocal() {

        cityName = Util.preference.getString(MyString.LOCAL_CITY_NAME, "");
        cityId = Util.preference.getString(MyString.LOCAL_CITY_ID, "");


        if (!TextUtils.isEmpty(cityId)) {

            tTitle.setText(cityName);
            if (!TextUtils.isEmpty(Util.preference.getString(MyString.LOCAL_COUNTRY_NAME, ""))) {
                tvCityName.setText(Util.preference.getString(MyString.LOCAL_COUNTRY_NAME, ""));
            } else {
                tvCityName.setText(cityName);
            }
            if (aCache.getAsJSONObject(TLUrl.getInstance().LOCALFRAGMENT) != null) {
                try {
                    Log.i("zjz", "localObject=" + aCache.getAsJSONObject(TLUrl.getInstance().LOCALFRAGMENT));
                    localModel.parseDatas(aCache.getAsJSONObject(TLUrl.getInstance().LOCALFRAGMENT), cityId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                initMenuList(cityId);
                initBanners();
                initTianQi();
                initHuiLv();
//                initAD();
                initMsgList(cityId);
                initNewList();
                getLLHDMsg();


            } else {
                if (isFirst) {
                    ProgressDlgUtil.showProgressDlg("", activity);
                    isFirst = false;
                }
                localModel.initDatas(cityId);
            }

            relativeNoCity.setVisibility(View.GONE);
        } else {
            cityId = "41";
            relativeNoCity.setVisibility(View.GONE);
            if (isFirst) {
                ProgressDlgUtil.showProgressDlg("", activity);
                isFirst = false;
            }
            localModel.initDatas(cityId); // 用户第一次进入默认维也纳 cityId=41;
           /* Intent intent = new Intent(activity, CountryCityActivity.class);
            startActivityForResult(intent, 1);*/
           /* chanDialog.show();
            chanDialog.setGetCity(new ChangCityDialog.GetCity() {
                @Override
                public void setCity(City2.BodyBean.DataBean.CountrysBean.CitysBean citysBean) {
                    UpteloadData(citysBean.cate_name,citysBean.city_id+"");
                }
            });*/

      /*      chanDialog.show();
            chanDialog.setisChina(new ChangCityDialog.isChina() {
                @Override
                public void getChina(boolean yes) {
                    Log.i("zds", "getChina: " + yes);
                    if (yes) {
                        getZhenXuan();
                        tvCityName.setText("国内");
                        tTitle.setText("华人邦臻选");
                        tv_location.setVisibility(View.GONE);
                        linSeKe.setVisibility(View.VISIBLE);
                        linTianYun.setVisibility(View.GONE);
                        tv_jiaotou.setBackgroundResource(R.drawable.img_changcity_downs);
                        tvCityName.setTextColor(activity.getResources().getColor(R.color.gray));
                        tTitle.setTextColor(activity.getResources().getColor(R.color.white));
                        listview_zhenxuan.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                        relativeTitle.setBackgroundResource(R.color.bg_edit_color);
                        //隐藏
                        localRlAd.setVisibility(View.GONE);
                        rl.setVisibility(View.GONE);
                    }
                }
            });

            chanDialog.setGetCity(new ChangCityDialog.GetCity() {
                @Override
                public void setCity(City2.BodyBean.DataBean.CountrysBean.CitysBean citysBean) {


                    Log.i("cityName:  ", citysBean.cate_name);
                    Log.i("cityId:  ", citysBean.city_id + "");

                    listview_zhenxuan.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);

                    tvCityName.setTextColor(activity.getResources().getColor(R.color.local_text_left));
                    tTitle.setTextColor(activity.getResources().getColor(R.color.white));

                    tv_location.setVisibility(View.VISIBLE);
                    linSeKe.setVisibility(View.VISIBLE);
                    linTianYun.setVisibility(View.GONE);
                    tv_jiaotou.setBackgroundResource(R.drawable.iv_bendi_jian_down);
                    relativeTitle.setBackgroundResource(R.color.white);
                    //隐藏
                    localRlAd.setVisibility(View.VISIBLE);
                    rl.setVisibility(View.VISIBLE);

                    if (TextUtils.isEmpty(Util.preference.getString(MyString.LOCAL_COUNTRY_NAME, ""))) {
                        tvCityName.setText(Util.preference.getString(MyString.LOCAL_COUNTRY_NAME, ""));
                    } else {
                        tvCityName.setText(citysBean.cate_name);
                    }

                    tTitle.setText(citysBean.cate_name);
                    cityId = citysBean.city_id + "";
                    ProgressDlgUtil.showProgressDlg("", activity);
                    localModel.initDatas(cityId);
                }
            });*/
        }
    }

    private void initLunBo() {
       /* bannerString.clear();
        for (int i = 0; i < lunbo.size(); i++) {
            bannerString.add(lunbo.get(i).img);
            Log.i("zds", "initLunBo: lunbo.get(i).img"+lunbo.get(i).img);
        }*/

        if (banners != null) {
            //设置Banners高度
            banners.setLayoutParams(new RelativeLayout.LayoutParams(MyApplication.getWidth(), 300));
//            banners.setLayoutParams(new RelativeLayout.LayoutParams(MyApplication.getWidth(), MyApplication.getWidth()*240/750));
            //本地用法
            banners.setAdapter(new UrlImgAdapter(activity), lunbo);
            //网络图片
//        mLBanners.setAdapter(new UrlImgAdapter(MainActivity.this), networkImages);
            //参数设置
            banners.setAutoPlay(true);//自动播放
            banners.setVertical(false);//是否可以垂直
            banners.setScrollDurtion(500);//两页切换时间
            banners.setCanLoop(true);//循环播放
            banners.setSelectIndicatorRes(R.drawable.img_hwg_indicator_select);//选中的原点
            banners.setUnSelectUnIndicatorRes(R.drawable.img_hwg_indicator_unselect);//未选中的原点
//        mLBanners.setHoriZontalTransitionEffect(TransitionEffect.Default);//选中喜欢的样式
//        banners.setHoriZontalCustomTransformer(new ParallaxTransformer(R.id.id_image));//自定义样式
            banners.setHoriZontalTransitionEffect(TransitionEffect.Alpha);//Alpha
            banners.setDurtion(5000);//切换时间
            if (lunbo.size() == 1) {

                banners.hideIndicatorLayout();//隐藏原点
            } else {

                banners.showIndicatorLayout();//显示原点
            }
            banners.setIndicatorPosition(LMBanners.IndicaTorPosition.BOTTOM_MID);//设置原点显示位置
        }


    }

    @OnClick({R.id.linear_change, R.id.liner_ser, R.id.gotomap, R.id.cesu, R.id.relative_search_j, R.id.lin_se_ke, R.id.t_select, R.id.relative_no_city})
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.t_select: // 重新加载网络
            case R.id.relative_no_city: // 重新加载网络
                initMyData();
                break;
            case R.id.gotomap: // 客服
                startActivity(new Intent(activity, KefuActivity.class));
                break;
            case R.id.lin_se_ke: // 弹窗
                settingPop();
                break;
            case R.id.relative_search_j: // 搜索 吃喝玩乐
                Intent d = new Intent(activity, SeachYouJiByTagActivity.class);
                d.putExtra("cityId", cityId);
                d.putExtra("type", "ismeishi");
                startActivity(d);
                break;
            case R.id.cesu: // 测速
                break;
            case R.id.ll_new_more:
                intent = new Intent(activity, NewMoreActivity.class);
                intent.putExtra("subject", LocalModel.subject);
                intent.putExtra("countryId", LocalModel.countryId + "");
                startActivity(intent);
                break;
            case R.id.linear_message_more:

                ToastUtil.showMessage("敬请期待！");

              /*  Intent rft=new Intent (activity,TestActivity.class);
                startActivity(rft);*/

               /* intent = new Intent(activity, MessageMoreActivity.class);
                intent.putExtra("cityId", cityId);
                startActivity(intent);*/
                break;
            case R.id.linear_news_more:

                intent = new Intent(activity, NewsMoreActivity.class);
                intent.putExtra("cityId", cityId);
                startActivity(intent);

                break;
            case R.id.relat_xx:  //  消息列表
                Intent rt = new Intent(activity, NoticeActivity.class);
                startActivity(rt);

                break;
            case R.id.linear_change:
//                intent = new Intent(activity, CountryCityActivity4.class); // 原来的
                intent = new Intent(activity, CountryCityActivity.class);
                startActivityForResult(intent, 1);

/*                if (chanDialog != null) {
                    chanDialog.show();
                    chanDialog.setisChina(new ChangCityDialog.isChina() {
                        @Override
                        public void getChina(boolean yes) {
                            Log.i("zds", "getChina: " + yes);
                            if (yes) {
                                getZhenXuan();
                                tvCityName.setText("国内");
                                tTitle.setText("华人邦臻选");
                                tv_location.setVisibility(View.GONE);
                                linSeKe.setVisibility(View.VISIBLE);
                                linTianYun.setVisibility(View.GONE);
                                tv_jiaotou.setBackgroundResource(R.drawable.img_changcity_downs);
                                tvCityName.setTextColor(activity.getResources().getColor(R.color.gray));
                                tTitle.setTextColor(activity.getResources().getColor(R.color.white));
                                listview_zhenxuan.setVisibility(View.VISIBLE);
                                swipeRefreshLayout.setVisibility(View.GONE);
                                relativeTitle.setBackgroundResource(R.color.bg_edit_color);
                                //隐藏
                                localRlAd.setVisibility(View.GONE);
                                rl.setVisibility(View.GONE);
                            }
                        }
                    });

                    chanDialog.setGetCity(new ChangCityDialog.GetCity() {
                        @Override
                        public void setCity(City2.BodyBean.DataBean.CountrysBean.CitysBean citysBean) {


                            Log.i("cityName:  ", citysBean.cate_name);
                            Log.i("cityId:  ", citysBean.city_id + "");

                            listview_zhenxuan.setVisibility(View.GONE);
                            swipeRefreshLayout.setVisibility(View.VISIBLE);

                            tvCityName.setTextColor(activity.getResources().getColor(R.color.local_text_left));
                            tTitle.setTextColor(activity.getResources().getColor(R.color.white));

                            tv_location.setVisibility(View.VISIBLE);
                            linSeKe.setVisibility(View.VISIBLE);
                            linTianYun.setVisibility(View.GONE);
                            tv_jiaotou.setBackgroundResource(R.drawable.iv_bendi_jian_down);
                            relativeTitle.setBackgroundResource(R.color.white);

                            //隐藏
                            localRlAd.setVisibility(View.VISIBLE);
                            rl.setVisibility(View.VISIBLE);

                            if (TextUtils.isEmpty(Util.preference.getString(MyString.LOCAL_COUNTRY_NAME, ""))) {
                                tvCityName.setText(Util.preference.getString(MyString.LOCAL_COUNTRY_NAME, ""));
                            } else {
                                tvCityName.setText(citysBean.cate_name);
                            }
                            tTitle.setText(citysBean.cate_name);
                            cityId = citysBean.city_id + "";
                            ProgressDlgUtil.showProgressDlg("", activity);
                            localModel.initDatas(cityId);
                        }
                    });
                }*/

                break;
            case R.id.relative_fabu:
                if (localModel != null && MyApplication.getInstance().self != null) {
                    intent = new Intent(activity, PublishActivity.class);
                    intent.putExtra("menuId", menuId);
                    intent.putExtra("cityId", cityId);
                    intent.putParcelableArrayListExtra("menuList", localModel.getMenuList());
                    startActivity(intent);
                } else {
                    intent = new Intent(activity, WXEntryActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.linear_wxnews_more:
                intent = new Intent(activity, WXNewsMoreActivity.class);
                intent.putExtra("cityId", cityId);
//                intent.putExtra("cityId", countryId);
                startActivity(intent);
                break;
            //关闭广告详情页面
            case R.id.local_detail_close:
                //关闭popupWindow
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                //显示广告
                localRlAd.setVisibility(View.VISIBLE);
                rl.setVisibility(View.VISIBLE);
                break;

            //点击广告栏
            case R.id.local_rl_ad:
                //隐藏
                localRlAd.setVisibility(View.GONE);
//                rl.setVisibility(View.GONE);
                /*//弹出广告的信息窗口
                showGuanggaoWindow();*/

                startActivity(new Intent(activity, LoacalKeFuActivity.class));
                break;
            //关闭广告栏
            case R.id.local_close:
                //隐藏
//                localRlAd.setVisibility(View.GONE);
//                rl.setVisibility(View.GONE);
                break;
            case R.id.liner_ser:
                break;
        }
    }


    public String getCurrentW(String uRLss, long start) {

//        InputStream is=null;
        try {
            URL url = new URL(uRLss);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");// 设置请求的方式
            urlConnection.setReadTimeout(5000);// 设置超时的时间
            urlConnection.setConnectTimeout(5000);// 设置链接超时的时间
            urlConnection.setDoInput(true);
            urlConnection.connect();
//            // 设置请求的头
//            urlConnection
//                    .setRequestProperty("User-Agent",
//                            "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
            // 获取响应的状态码 404 200 505 302

            Log.i("zds", "start1=" + start);

            if (urlConnection.getResponseCode() == 200) {
//                // 获取响应的输入流对象
//                is = urlConnection.getInputStream();
//                // 释放资源
//                is.close();
                Log.i("zds", "start2=" + start);
                Log.i("zds", "end=" + System.currentTimeMillis());

                long to = System.currentTimeMillis() - start;

                Log.i("zds", "to=" + to);

                return to + "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initCeSuPop() {

        final View root_view = View.inflate(activity, R.layout.item_travel_cesu, null);
        final TextView tv_compelete = (TextView) root_view.findViewById(R.id.tv_compelete);
        final ListView listview_cesu = (ListView) root_view.findViewById(R.id.listview_cesu);
        final RelativeLayout close = (RelativeLayout) root_view.findViewById(R.id.tyt);

        final List<CeSu> data_cesu = MyApplication.getCeSuList();

        if (data_cesu != null && data_cesu.size() > 0) {
            final List<CeSu> data_ce = new ArrayList<>();
            data_ce.clear();
            final CeSuAdapter adapter = new CeSuAdapter(activity);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < data_cesu.size(); i++) {
                        CeSu da = new CeSu();
                        da.setServerName(data_cesu.get(i).getServerName());
                        da.setServerUrl(data_cesu.get(i).getServerUrl());
                        String to_time = getCurrentW(data_cesu.get(i).getServerUrl(), System.currentTimeMillis());
                        Log.i("zds", "to_time==" + to_time);
                        if(!TextUtils.isEmpty(to_time)){
                            da.setCurrWanSu(to_time + "  ms");
                        }else {
                            da.setCurrWanSu("未知");
                        }
                        data_ce.add(da);
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_compelete.setText("测速完成，你可以...");
                            adapter.addDatas(data_ce);
                            listview_cesu.setAdapter(adapter);


                            final PopupWindow popupWindow = new PopupWindow(root_view, Util.WIDTH * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                            WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                            params.alpha = 0.5f;
                            activity.getWindow().setAttributes(params);
                            popupWindow.setTouchable(true);
                            popupWindow.setOutsideTouchable(true);
                            popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    return false;
                                }
                            });
                            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                                @Override
                                public void onDismiss() {
                                    WindowManager.LayoutParams params = activity.getWindow()
                                            .getAttributes();
                                    params.alpha = 1f;
                                    activity.getWindow().setAttributes(params);
                                }
                            });
                            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ffffff")));

                            Log.i("zds", "run: position" + MyApplication.getPosition());

                            adapter.setSelectedPosition(MyApplication.getPosition());
                            adapter.notifyDataSetChanged();
                            popupWindow.showAtLocation(root_view, Gravity.CENTER, 0, 0);

                            listview_cesu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Log.i("zds", "onItemClick: cesu_postion========" + i);
                                    CeSu dabe = (CeSu) adapterView.getItemAtPosition(i);
                                    ChangeNetUrl(dabe.getServerUrl());
                                    MyApplication.savePosition(i);
                                    popupWindow.dismiss();

                                }
                            });

                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    popupWindow.dismiss();
                                }
                            });


                        }
                    });
                }
            }).start();


        }
    }

    /****
     * 改变服务器
     **/
    private void ChangeNetUrl(String url_base) {
        String basU_hua = url_base.substring(url_base.lastIndexOf("/") + 1, url_base.length());

        Log.i("zdstra", "basU_base==" + url_base);
        Log.i("zdstra", "basU_hua==" + basU_hua);
        TLUrl.URL_BASE = url_base;
        TLUrl.URL_huayouhui = basU_hua;
        TLUrl.getInstance().isChange = true;

        Log.i("zdstra", "basU_base2==" + TLUrl.getInstance().getUrl());
        Log.i("zdstra", "basU_hua2==" + TLUrl.getInstance().getHuaUrl());
        MyApplication.saveCurrentHost(url_base);
    }

    private void getZhenXuan() {
        //        http://120.24.221.46:7076/city/queryNoCityImg?type=2
        ProgressDlgUtil.showProgressDlg("", activity);
        HttpRequest.sendGet("http://120.24.221.46:7076/city/queryNoCityImg", "type=2", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDlgUtil.stopProgressDlg();
                        Log.i("zds_zhenxuan", msg + "");

                        if (TextUtils.isEmpty(msg)) {
                            return;
                        } else {
                            ZhenXuan bean = new Gson().fromJson(msg, ZhenXuan.class);
                            if (bean != null && bean.result == 1) {
                                if (bean.body != null) {
                                    if (bean.body.size() > 0) {
                                        TravelZhenXuanAdapter mXuanAdapter = new TravelZhenXuanAdapter(activity, bean.body);
                                        listview_zhenxuan.setAdapter(mXuanAdapter);
                                    } else {
                                        showToast("还没有数据哦！");
                                    }
                                }
                            }
                        }
                    }
                });
            }
        });

        listview_zhenxuan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ZhenXuan.BodyEntry bodyEntry = (ZhenXuan.BodyEntry) parent.getItemAtPosition(position);
                if (bodyEntry != null && !TextUtils.isEmpty(bodyEntry.htmlUrl)) {
                    Intent intent = new Intent(activity, GuanggaoActivity.class);
                    intent.putExtra("url_local", bodyEntry.htmlUrl);
                    startActivity(intent);
                }
            }
        });
    }

    class UrlImgAdapter implements LBaseAdapter<BannersBean> {
        private Context mContext;

        public UrlImgAdapter(Context context) {
            mContext = context;
        }

        @Override
        public View getView(final LMBanners lBanners, final Context context, final int position, final BannersBean data) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.banner_item_no_scaletype, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.id_image);

//            750 240   1080 290

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(MyApplication.getWidth(), 300);
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(MyApplication.getWidth(), MyApplication.getWidth()*240/750);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            MyApplication.imageLoader.displayImage(data.img, imageView, MyApplication.getListOptions());
            Log.i(TAG, "getView: position============="+position);
            if(position!=0){
                tTitle.setVisibility(View.GONE);
                ivYun.setVisibility(View.GONE);
                tvTianqi.setVisibility(View.GONE);
                tvTianqi_des.setVisibility(View.GONE);
            }else {
                tTitle.setVisibility(View.VISIBLE);
                ivYun.setVisibility(View.VISIBLE);
                tvTianqi.setVisibility(View.VISIBLE);
                tvTianqi_des.setVisibility(View.VISIBLE);
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(data.newsUrl)){
//                        Intent intentVp = new Intent(activity, ActivityWeiba.class);
//                        Intent intentVp = new Intent(activity, GuanggaoActivity.class);
//                        intentVp.putExtra("url", data.newsUrl);
                        if(data.is_junp==1){
                            Intent intent;
                            if (TextUtils.isEmpty(MyApplication.getInstance().getMykey())) {
                                intent = new Intent(activity, WXEntryActivity.class);
                                intent.putExtra("isthome",true);
                            } else {
                                intent = new Intent(activity, ActivityWeibaCommon.class);
                                intent.putExtra("name", "全部微吧");
                                intent.putExtra("type", ActivityWeibaCommon.FRAGMENT_WEIBA_ALL);
                            }
                            activity.startActivity(intent);
                        }
                    }
                }
            });
            return view;
        }

    }


    private void initViewPager(String cityId) {

        viewPagerAdapter.getDatas().add(LocalMenuFragment.newInstance(cityId));
        viewPagerAdapter.getTitle().add(0, "1");
        viewPagerAdapter.getDatas().add(LocalTeDianFragment.newInstance(cityId));
        viewPagerAdapter.getTitle().add(1, "2");

        //滑动的viewpager
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);

        iv_dain1.setVisibility(View.VISIBLE);
        iv_dain2.setVisibility(View.VISIBLE);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("zds", "postion===" + position);
                if (position == 0) {
                    iv_dain1.setImageResource(R.drawable.img_hwg_indicator_select);
                    iv_dain2.setImageResource(R.drawable.img_hwg_indicator_unselect);
                } else if (position == 1) {
                    iv_dain1.setImageResource(R.drawable.img_hwg_indicator_unselect);
                    iv_dain2.setImageResource(R.drawable.img_hwg_indicator_select);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
/*    private void initGViewPager(ArrayList<BannersBean> gg1,ArrayList<BannersBean> gg2) {

        viewPagerGAdapter.getDatas().add(LocalGuangGaoFragment.newInstance(gg1));
        viewPagerGAdapter.getTitle().add(0, "1");
        viewPagerGAdapter.getDatas().add(LocalGuangGaoFragment2.newInstance(gg2));
        viewPagerGAdapter.getTitle().add(1, "2");

        //滑动的viewpager
        viewPager_g.setAdapter(viewPagerGAdapter);
        viewPager_g.setOffscreenPageLimit(1);

        iv_dain1_g.setVisibility(View.VISIBLE);
        iv_dain2_g.setVisibility(View.VISIBLE);

        viewPager_g.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("zds", "postion===" + position);
                if (position == 0) {
                    iv_dain1_g.setImageResource(R.drawable.img_hwg_indicator_select);
                    iv_dain2_g.setImageResource(R.drawable.img_hwg_indicator_unselect);
                } else if (position == 1) {
                    iv_dain1_g.setImageResource(R.drawable.img_hwg_indicator_unselect);
                    iv_dain2_g.setImageResource(R.drawable.img_hwg_indicator_select);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }*/

/*
    private void initAD() {
        if (localModel.getPartners() != null) {

            if (localModel.getPartners().size() > 0) {
                localRlAd.setVisibility(View.VISIBLE);
                rl.setVisibility(View.VISIBLE);

                for (int i = 0; i < localModel.getPartners().size(); i++) {

                    PartnersBean partnersBean = localModel.getPartners().get(i);

                    //城市名
                    localTvCityName.setText(partnersBean.cityName);
                    //头像
                    MyApplication.imageLoader.displayImage(partnersBean.headImg, localCivHead, MyApplication.getAvatorOptions());
                    //是否实名认证   0否  1是
                    if (partnersBean.isRz == 1) {
                        //认证标志
                        localIvShiming.setVisibility(View.VISIBLE);
                        localTvShiming.setText("实名认证");
                    } else {
                        localIvShiming.setVisibility(View.INVISIBLE);
                        localTvShiming.setVisibility(View.INVISIBLE);
                    }
                    //是否授权认证   0否  1是
                    if (partnersBean.isSq == 1) {
                        //认证标志
                        localIvGuanfang.setVisibility(View.VISIBLE);
                        localTvGuanfang.setText("官方授权");
                    } else {
                        //否则隐藏
                        localIvGuanfang.setVisibility(View.INVISIBLE);
                        localTvGuanfang.setVisibility(View.INVISIBLE);
                    }

                    cityPartner.setText("国家合伙人");
                    localTvName.setText("负责人：" + partnersBean.fzMan);
                }

            } else {
                localRlAd.setVisibility(View.GONE);
                rl.setVisibility(View.GONE);
            }
        }
    }
*/

    List<BannersBean> lunbo = new ArrayList<>();
    List<BannersBean> guanggao = new ArrayList<>();
    List<BannersBean> qian_huiren_logo = new ArrayList<>();

    private void initTianQi() {
        if (!TextUtils.isEmpty(tian_qi_img)) {
            MyApplication.imageLoader.displayImage(tian_qi_img, ivYun, MyApplication.getAvatorOptions());
        }

        if (!TextUtils.isEmpty(tian_qi_wendu)) {
            tvTianqi.setText(tian_qi_wendu);
        }

        if (!TextUtils.isEmpty(tian_qi_txt)) {
            tvTianqi_des.setText(tian_qi_txt);
        }
    }

    private void initHuiLv() {

        if (!TextUtils.isEmpty(hb)) {
            tvHuilv.setText("1" + hb + "=" + hl + "CNY");
        }
        if (!TextUtils.isEmpty(hq_num)) {
            tvHuarenNum.setText(hq_num + "");
        }
        if (!TextUtils.isEmpty(sy_num)) {
            tvShangye.setText(sy_num + "");
        }
    }

    private void initBanners() {

        /*新上的广告*/
        if(localModel.getAds().size()>0){
            for (int i = 0; i < localModel.getAds().size(); i++) {
                if (i < 8) {
                   /* LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MyApplication.getWidth()/2, (MyApplication.getWidth()/2)*288/505);
                    layoutParams.setMargins(5,5,5,5);
                    iv_gg_news.get(i).setLayoutParams(layoutParams);
                    iv_gg_news.get(i).setBackgroundResource(R.color.hwg_bg);
                    iv_gg_news.get(i).setScaleType(ImageView.ScaleType.FIT_XY);*/
                    iv_gg_news.get(i).setVisibility(View.VISIBLE);
                    final GGAds bannersBean = localModel.getAds().get(i);

                    MyApplication.imageLoader.displayImage(bannersBean.img, iv_gg_news.get(i), MyApplication.getListOptions());
                    iv_gg_news.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!TextUtils.isEmpty(bannersBean.url)){
                                if(bannersBean.is_jump==1){  // 跳微吧
                                    Intent intent;
                                    if (TextUtils.isEmpty(MyApplication.getInstance().getMykey())) {
                                        intent = new Intent(activity, WXEntryActivity.class);
                                        intent.putExtra("isthome",true);
                                    } else {
                                        intent = new Intent(activity, ActivityWeibaCommon.class);
                                        intent.putExtra("name", "全部微吧");
                                        intent.putExtra("type", ActivityWeibaCommon.FRAGMENT_WEIBA_ALL);
                                    }
                                    activity.startActivity(intent);
                                }else {
                                    Intent intent = new Intent(activity, GuanggaoActivity.class);
                                    intent.putExtra("url_local", bannersBean.url);
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                }
            }
        }else {
            for (int i = 0; i < iv_gg_news.size(); i++) {
                iv_gg_news.get(i).setVisibility(View.GONE);
            }
        }

        if (localModel.getBanners().size() > 0) {

            lunbo.clear();
            guanggao.clear();
            qian_huiren_logo.clear();
            for (int i = 0; i < localModel.getBanners().size(); i++) {
                BannersBean bannersBean = localModel.getBanners().get(i);
                if (bannersBean.type == 2) {
                    guanggao.add(bannersBean);
                } else if (bannersBean.type == 1) {
                    lunbo.add(bannersBean);
                } else if (bannersBean.type == 3) {  // 仟慧人 logo
                    qian_huiren_logo.add(bannersBean);
                }
            }
        }

        Log.i("zds", "initBanners: guanggao" + guanggao.size());
        Log.i("zds", "initBanners:lunbo " + lunbo.size());
        Log.i("zds", "initBanners:qian_huiren_logo " + qian_huiren_logo.size());

//        MyApplication.guanggao.clear();
//        MyApplication.guanggao2.clear();

        if (qian_huiren_logo.size() > 0) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Util.WIDTH, Util.WIDTH * 260 / 1080);
            iv_qian_huiren_logo.setLayoutParams(layoutParams);

//            iv_qian_huiren_logo.setVisibility(View.VISIBLE);
            MyApplication.imageLoader.displayImage(qian_huiren_logo.get(0).img, iv_qian_huiren_logo, MyApplication.getListOptions());
            iv_qian_huiren_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, GuanggaoActivity.class);
                    intent.putExtra("url", qian_huiren_logo.get(0).newsUrl);
                    startActivity(intent);
                }
            });
        } else {
            iv_qian_huiren_logo.setVisibility(View.GONE);
        }

        if (guanggao.size() > 0) {
//            liner_youxuan.setVisibility(View.VISIBLE);
          /*  for (int i = 0; i < guanggao.size(); i++) {
                if(i<4){
                    MyApplication.guanggao.add(guanggao.get(i));
                }else {
                    MyApplication.guanggao2.add(guanggao.get(i));
                }
            }

            if (idFirst_g) {
                initGViewPager(MyApplication.guanggao,MyApplication.guanggao2);
                idFirst_g = false;
            }*/
                     /*原来的*/
//            llGuanggao1.setVisibility(View.VISIBLE);
            for (int i = 0; i < guanggao.size(); i++) {
                iv_weini_youxuan.setVisibility(View.VISIBLE);
                if (i < 8) {
                   /* LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MyApplication.getWidth()/2, (MyApplication.getWidth()/2)*288/505);
                    layoutParams.setMargins(5,5,5,5);
                    images.get(i + 3).setLayoutParams(layoutParams);
                    images.get(i + 3).setBackgroundResource(R.color.hwg_bg);
                    images.get(i + 3).setScaleType(ImageView.ScaleType.FIT_XY);*/
                    images.get(i + 3).setVisibility(View.VISIBLE);
                    final BannersBean bannersBean = guanggao.get(i);
                    MyApplication.imageLoader.displayImage(bannersBean.img, images.get(i + 3), MyApplication.getListOptions());
                    images.get(i + 3).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, GuanggaoActivity.class);
                            intent.putExtra("url", bannersBean.newsUrl);
                            startActivity(intent);
                        }
                    });
                }
            }

//            EventBus.getDefault().post(new Pic());

        } else {
//            liner_youxuan.setVisibility(View.GONE);
//            EventBus.getDefault().post(new Pic());
            iv_weini_youxuan.setVisibility(View.GONE);
            llGuanggao1.setVisibility(View.GONE);
            for (int i = 0; i < images.size(); i++) {
                images.get(i).setVisibility(View.GONE);
            }
        }

        if (lunbo.size() > 0) {
            liner_banner.setVisibility(View.VISIBLE);
            initLunBo();
        } else {
            liner_banner.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (banners != null)
            banners.stopImageTimerTask();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (banners != null)
            banners.startImageTimerTask();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (banners != null) {
            banners.clearImageTimerTask();
        }
        ButterKnife.reset(this);
    }


    private void initPetenr(String msg_s) {

        View itemView = View.inflate(activity, R.layout.local_guanggao_detail_item2, null);
        final TextView localDetailTvContain = (TextView) itemView.findViewById(R.id.local_item_detail_tv_contain);  //广告的内容
        final TextView localDetailTvName = (TextView) itemView.findViewById(R.id.local_detail_tv_name);          //标题
        final TextView tv_weixin_hao = (TextView) itemView.findViewById(R.id.tv_weixin_hao);          //微信号
        final ImageView localDetailIvWeiXin = (ImageView) itemView.findViewById(R.id.local_item_iv_weixin);  //微信二维码
        final ImageView localDetailClose = (ImageView) itemView.findViewById(R.id.local_detail_close);
        final CircleImageView local_detail_civ_head = (CircleImageView) itemView.findViewById(R.id.local_detail_civ_head);

        try {
            JSONObject jsonObject = new JSONObject(msg_s);
            if (jsonObject != null && jsonObject.optInt("status") == 1) {
                JSONObject msg = jsonObject.optJSONObject("msg");

                localDetailTvContain.setText(msg.optString("intro"));
                localDetailTvName.setText(msg.optString("fz_man"));
                tv_weixin_hao.setText(msg.optString("phone"));

                if (!TextUtils.isEmpty(msg.optString("head_img"))) {
                    MyApplication.imageLoader.displayImage(msg.optString("head_img"), local_detail_civ_head, MyApplication.getListOptions());
                }

                if (!TextUtils.isEmpty(msg.optString("qr_code"))) {
                    MyApplication.imageLoader.displayImage(msg.optString("qr_code"), localDetailIvWeiXin, MyApplication.getListOptions());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        localDetailClose.setOnClickListener(this);
        popupWindow = new PopupWindow(itemView, Util.WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT);

        //触摸点击事件
        popupWindow.setTouchable(true);
        //聚集
        popupWindow.setFocusable(true);
        //设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        //点击返回键popupwindown消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //背景变暗
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.5f;
        activity.getWindow().setAttributes(params);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        //监听如果popupWindown消失之后背景变亮
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow()
                        .getAttributes();
                params.alpha = 1f;
                activity.getWindow().setAttributes(params);

                //显示广告
                localRlAd.setVisibility(View.VISIBLE);
                rl.setVisibility(View.VISIBLE);
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ffffff")));
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }

    /**
     * 点击底下的广告,弹出popupwindow
     *
     * @param parent
     */
    private PopupWindow popupWindow;

    private void showGuanggaoWindow() {
//        https://japi.tuling.me/hrq/partner/getPartner

        if (!TextUtils.isEmpty(aCache.getAsString("patenr"))) {
            initPetenr(aCache.getAsString("patenr"));
        } else {

            HttpRequest.sendGet("https://japi.tuling.me/hrq/partner/getPartner", "", new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("getPartner", msg + "");

                            if (TextUtils.isEmpty(msg)) {
                                ToastUtil.showMessage("网络出错！");
                                return;
                            } else {
                                initPetenr(msg);
                                if (!TextUtils.isEmpty(aCache.getAsString("patenr"))) {
                                    aCache.remove("patenr");
                                    aCache.put("patenr", msg);
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    /**
     * item  本地新闻
     */
    private View getNewView(int position, final New aNew) {
        View view = inflater.inflate(R.layout.local_item_new, null);
        ImageView img_new = (ImageView) view.findViewById(R.id.img_new);
        TextView tv_new = (TextView) view.findViewById(R.id.tv_new);

        if (!TextUtils.isEmpty(aNew.getTitle())) {
            tv_new.setText(aNew.getTitle());
        }

        if (!TextUtils.isEmpty(aNew.getPurl())) {
            MyApplication.imageLoader.displayImage(aNew.getPurl(), img_new, MyApplication.getListOptions());
        } else {
            MyApplication.imageLoader.displayImage("", img_new, MyApplication.getListOptions());
        }
/*
        View view = inflater.inflate(R.layout.local_item_new2, null);
        TextView t_dain = (TextView) view.findViewById(R.id.t_dain);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);

        if (position == 0) {
            t_dain.setBackground(getResources().getDrawable(R.drawable.img_hwg_indicator_select));
        } else {
            t_dain.setBackground(getResources().getDrawable(R.drawable.img_hwg_indicator_unselect));
        }

        if (!TextUtils.isEmpty(aNew.getTitle())) {
            tv_title.setText(aNew.getTitle());
        }
*/

        view.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                super.onNoDoubleClick(v);
                Intent intent = new Intent(activity, LocalZiXunDetialsActivity.class);
                intent.putExtra("id", aNew.getId());
                intent.putExtra("time", aNew.getTime());
                intent.putExtra("title", aNew.getTitle());
                activity.startActivity(intent);
            }
        });
        return view;
    }

    /**
     * 活动信息的item
     */
    private View getActivityView(int position, final ActivitysBean activy) {
        View view = inflater.inflate(R.layout.local_item_activiy, null);
        LinearLayout line_tag = (LinearLayout) view.findViewById(R.id.line_tag);
        ImageView img_icon = (ImageView) view.findViewById(R.id.img_icon);
        TextView t_title = (TextView) view.findViewById(R.id.t_title);
        TextView t_jubanfang = (TextView) view.findViewById(R.id.t_jubanfang);
        TextView t_address = (TextView) view.findViewById(R.id.t_address);
        TextView t_time = (TextView) view.findViewById(R.id.t_time);

        line_tag.removeAllViews();
        if (!TextUtils.isEmpty(activy.tag)) {
            String[] tags = activy.tag.split(",");
            if (tags != null) {

                if (tags.length > 0) {
                    line_tag.setVisibility(View.VISIBLE);
                    for (int i = 0; i < tags.length; i++) {

                        View items = View.inflate(getContext(), R.layout.item_text_tag, null);

                        ViewGroup parent = (ViewGroup) items.getParent();
                        if (parent != null) {
                            parent.removeAllViews();
                        }

                        TextView t_tips = (TextView) items.findViewById(R.id.t_tips);

                        if (!TextUtils.isEmpty(tags[i])) {
                            t_tips.setText(tags[i] + "");
                        }

                        line_tag.addView(items);
                    }
                } else {
                    line_tag.setVisibility(View.GONE);
                }
            }
        }

        if (!TextUtils.isEmpty(activy.img)) {
            ImageLoader.getInstance().displayImage(activy.img, img_icon, MyApplication.getListOptions());
        }

        if (!TextUtils.isEmpty(activy.title)) {
            t_title.setText(activy.title);
        }
       /* if (!TextUtils.isEmpty(activy.jbf)) {
            t_jubanfang.setText("举办方：" + activy.jbf);
        }*/

       /* if (activy.date < 2 * 1000000000) {
            t_time.setText(Util.format1.format(activy.date * 1000));
        } else {
            t_time.setText(Util.format1.format(activy.date));
        }*/

        if (!TextUtils.isEmpty(activy.activity_time)) {
            t_time.setText(activy.activity_time);
            t_jubanfang.setVisibility(View.VISIBLE);
        } else {
            t_time.setText("");
            t_jubanfang.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(activy.ads)) {
            t_address.setText(activy.ads);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activy.is_click == 1) {  // 可点击
                    Intent intent = new Intent(activity, GuanggaoActivity.class);
                    intent.putExtra("url_local", activy.url);
                    intent.putExtra("title_local", activy.title);
                    startActivity(intent);
                } else {
                    ToastUtil.showMessage("敬请期待！");
                }
            }
        });
        return view;
    }

    /**
     * 活动的item
     */
    private void getLLHDMsg() {

        if (getActivityList().size() == 0) {
            linearMessage.setVisibility(View.GONE);
        } else {
            linearMessage.setVisibility(View.VISIBLE);
            if (getActivityList().size() != 0 && linearMessage.getChildCount() > 1) {
                linearMessage.removeViews(1, linearMessage.getChildCount() - 1);
                linearMessage.invalidate();
            }

            for (int i = 0; i < getActivityList().size(); i++) {
                if (i < 6) {
                    linearMessage.addView(getActivityView(i, getActivityList().get(i)), i + 1);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1 && data != null) {
            String cityName = data.getStringExtra("cityName");
            String cityId = data.getStringExtra("cityId");
            boolean isChina = data.getBooleanExtra("isChina", false);
            if (isChina) {
                initChina();
            } else {
                UpteloadData(cityName, cityId);
            }
        }
    }

    private void initChina() {
        getZhenXuan();
        tvCityName.setText("国内");
        tTitle.setText("华人邦臻选");
        tv_location.setVisibility(View.GONE);
        linSeKe.setVisibility(View.VISIBLE);
        linTianYun.setVisibility(View.GONE);
        tv_jiaotou.setBackgroundResource(R.drawable.img_changcity_downs);
        tvCityName.setTextColor(activity.getResources().getColor(R.color.gray));
        tTitle.setTextColor(activity.getResources().getColor(R.color.white));
        listview_zhenxuan.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
        relativeTitle.setBackgroundResource(R.color.bg_edit_color);
        //隐藏
        localRlAd.setVisibility(View.GONE);
        rl.setVisibility(View.GONE);
    }

    @Override
    public void loadSuccess() {
        ProgressDlgUtil.stopProgressDlg();
        localRlAd.setVisibility(View.VISIBLE);
        rl.setVisibility(View.VISIBLE);
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        initMenuList(cityId);
        initBanners();
        initTianQi();
        initHuiLv();
//        initAD();
        initMsgList(cityId);
        initNewList();
        getLLHDMsg();
    }

    public BDLocationListener myListener = new MyLocationListener();

    private void initBaiduLbs() {

        mLocationClient = new LocationClient(activity);
        mLocationClient.registerLocationListener(myListener);    //注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();

    }

    private String current_lng;
    private String current_lat;
    double latitude;//获取经度
    double longitude;//获取纬度

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果

                latitude = location.getLatitude();//获取经度
                longitude = location.getLongitude();//获取纬度
                current_lat = String.valueOf(latitude);
                current_lng = String.valueOf(longitude);

                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                latitude = location.getLatitude();//获取经度
                longitude = location.getLongitude();//获取纬度
                current_lat = String.valueOf(latitude);
                current_lng = String.valueOf(longitude);

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                latitude = location.getLatitude();//获取经度
                longitude = location.getLongitude();//获取纬度
                current_lat = String.valueOf(latitude);
                current_lng = String.valueOf(longitude);

                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }

//
//            /////////////////////////////////////
//            //Receive Location
//            StringBuffer sb = new StringBuffer(256);
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//                double latitude = location.getLatitude();//获取经度
//                double longitude = location.getLongitude();//获取纬度
//                current_lat=String .valueOf(latitude);
//                current_lng=String .valueOf(longitude);
//
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//                double latitude = location.getLatitude();//获取经度
//                double longitude = location.getLongitude();//获取纬度
//                current_lat=String .valueOf(latitude);
//                current_lng=String .valueOf(longitude);
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                double latitude = location.getLatitude();//获取经度
//                double longitude = location.getLongitude();//获取纬度
//                current_lat=String .valueOf(latitude);
//                current_lng=String .valueOf(longitude);
//            } else if (location.getLocType() == BDLocation.TypeServerError) {
//                sb.append("\ndescribe : ");
//                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                sb.append("\ndescribe : ");
//                sb.append("网络不同导致定位失败，请检查网络是否通畅");
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                sb.append("\ndescribe : ");
//                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//            }
//            sb.append("\nlocationdescribe : ");
//            sb.append(location.getLocationDescribe());// 位置语义化信息
//            List<Poi> list = location.getPoiList();// POI数据
//            if (list != null) {
//                sb.append("\npoilist size = : ");
//                sb.append(list.size());
//                for (Poi p : list) {
//                    sb.append("\npoi= : ");
//                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
//                }
//            }

                Log.i("BaiduLocationApiDem", sb.toString());
                Log.i("uuuu_lat", latitude + "");
                Log.i("uuuu_lng", longitude + "");

                MyApplication.my_current_lat = current_lat;
                MyApplication.my_current_lng = current_lng;
                mLocationClient.stop();
            }
        }
    }

    public LocationClient mLocationClient;

    @Override
    public void loadFailed() {
        Log.i("zds", "loadFailed: ");
        ProgressDlgUtil.stopProgressDlg();
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
