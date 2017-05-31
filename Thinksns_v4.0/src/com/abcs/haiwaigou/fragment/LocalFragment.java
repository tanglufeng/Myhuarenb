package com.abcs.haiwaigou.fragment;

import android.content.ClipboardManager;
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
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.abcs.haiwaigou.activity.KefuActivity;
import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.haiwaigou.local.huohang.view.HuoHangEditAddressActivity;
import com.abcs.haiwaigou.local.activity.LocalZiXunDetialsActivity;
import com.abcs.haiwaigou.local.activity.MessageMoreActivity;
import com.abcs.haiwaigou.local.activity.NewMoreActivity;
import com.abcs.haiwaigou.local.activity.NewsMoreActivity;
import com.abcs.haiwaigou.local.activity.PublishActivity;
import com.abcs.haiwaigou.local.activity.WXNewsMoreActivity;
import com.abcs.haiwaigou.local.beans.ActivitysBean;
import com.abcs.haiwaigou.local.beans.City2;
import com.abcs.haiwaigou.local.beans.MsgViewFlip;
import com.abcs.haiwaigou.local.beans.New;
import com.abcs.haiwaigou.local.beans.News;
import com.abcs.haiwaigou.local.fragment.LocalMenuFragment;
import com.abcs.haiwaigou.local.fragment.LocalTeDianFragment;
import com.abcs.haiwaigou.local.view.ChangCityDialog;
import com.abcs.haiwaigou.model.BannersBean;
import com.abcs.haiwaigou.model.LocalModel;
import com.abcs.haiwaigou.model.PartnersBean;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.view.MyGridView;
import com.abcs.haiwaigou.view.zjzbanner.LMBanners;
import com.abcs.haiwaigou.view.zjzbanner.adapter.LBaseAdapter;
import com.abcs.haiwaigou.view.zjzbanner.transformer.TransitionEffect;
import com.abcs.haiwaigou.view.zjzbanner.utils.ScreenUtils;
import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.adapter.CeSuAdapter;
import com.abcs.hqbtravel.adapter.TravelZhenXuanAdapter;
import com.abcs.hqbtravel.entity.ZhenXuan;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.GuanggaoActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.ServerUtils;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.CircleImageView;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.ActivityHome;
import com.abcs.sociax.t4.android.bean.CeSu;
import com.abcs.sociax.t4.android.fragment.FragmentSociax;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
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
public class LocalFragment extends FragmentSociax implements View.OnClickListener, LocalModel.LoadStateInterface {


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
    RelativeLayout relativeTitle;
    @InjectView(R.id.view_pager)
    ViewPager viewPager;
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
    @InjectView(R.id.local_close)
    ImageView localClose;
    @InjectView(R.id.imge_ttou)
    ImageView imge_ttou;
    @InjectView(R.id.local_tv_city_name)
    TextView localTvCityName;
    @InjectView(R.id.local_civ_head)
    CircleImageView localCivHead;
    @InjectView(R.id.local_iv_shiming)
    ImageView localIvShiming;
    @InjectView(R.id.local_tv_shiming)
    TextView localTvShiming;
    @InjectView(R.id.local_iv_guanfang)
    ImageView localIvGuanfang;
    @InjectView(R.id.local_tv_guanfang)
    TextView localTvGuanfang;
    @InjectView(R.id.local_tv_name)
    TextView localTvName;
    @InjectView(R.id.local_rl_ad)
    RelativeLayout localRlAd;
    @InjectView(R.id.rl)
    RelativeLayout rl;
    @InjectView(R.id.city_partner)
    TextView cityPartner;
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
    @InjectView(R.id.liner_mian)
    RelativeLayout linerMian;
    @InjectView(R.id.emptry_layout)
    EmptyLayout emptryLayout;

    private String menuId;
    private List<ImageView> images = new ArrayList<>();
    private View view;
    private ActivityHome activity;
    LocalModel localModel;
    private ACache aCache;
    private ArrayList<News> newsList = new ArrayList<News>();
    public static LocalFragment localFragment;
    private String lastTime;
    private String lastId;
    private String cityName;
    private String cityId;
    private Handler handler = new Handler();
    CFViewPagerAdapter viewPagerAdapter;
    ChangCityDialog chanDialog;

    public static LocalFragment newInstance() {
        if (localFragment == null) {
            localFragment = new LocalFragment();
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (ActivityHome) getActivity();
        view = activity.getLayoutInflater().inflate(R.layout.hqb_local_fragment2, null);
        ButterKnife.inject(this, view);
        aCache = ACache.get(activity);

        linerMian.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeAllViewsInLayout();
        ButterKnife.inject(this, view);
        return view;

    }

    @Override
    public int getLayoutId() {
        return R.layout.hqb_local_fragment2;
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
                                            tTitle.setTextColor(activity.getResources().getColor(R.color.grayy));
                                            relativeTitle.setBackgroundResource(R.color.bg_edit_color);
                                            //隐藏
                                            localRlAd.setVisibility(View.GONE);
                                            rl.setVisibility(View.GONE);

                                        } else {
                                            loadLocal();
                                            listview_zhenxuan.setVisibility(View.GONE);
                                            swipeRefreshLayout.setVisibility(View.VISIBLE);

                                            tvCityName.setTextColor(activity.getResources().getColor(R.color.red_o));
                                            tTitle.setTextColor(activity.getResources().getColor(R.color.red_o));

                                            tv_location.setVisibility(View.VISIBLE);
                                            linSeKe.setVisibility(View.GONE);
                                            linTianYun.setVisibility(View.VISIBLE);
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

    @Override
    public void initView() {
        images.add(ivGg1);
        images.add(ivGg3);
        images.add(ivGg4);
        images.add(ivGg5);
        images.add(ivGg6);
        images.add(ivGg7);
        images.add(ivGg8);

        viewPagerAdapter = new CFViewPagerAdapter(getChildFragmentManager());

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
            for (int i = 0; i < getMsgList().size(); i++) {
                MsgViewFlip ben = new MsgViewFlip();
                if (i % 2 == 0) { // 偶数
                    ben.title2 = getMsgList().get(i).getTitle();
                } else {
                    ben.title = getMsgList().get(i).getTitle();
                }
                msgViewFlips.add(ben);
            }

            if (msgViewFlips.size() != 0) {
                initViewFliper(msgViewFlips, cityId);
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
            }
            if (!TextUtils.isEmpty(msg.title2)) {
                tv_title2.setText(msg.title2);
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

    private void checkIsInto() {
//        http://www.huaqiaobang.com/mobile/index.php?act=native&op=verify_member_native&key=939f6c2c1ad7199187be733cc714955a
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base + "/mobile/index.php", "act=native&op=verify_member_native&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (TextUtils.isEmpty(msg)) {
                            return;
                        }
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.optInt("state") == 1) {  // 去首页
//                                JSONObject bean = object.optJSONObject("datas");
//                                Intent intent = new Intent(activity, BenDiPeiSongActivity2.class);
//                                intent.putExtra("district_id", bean.optString("district_id"));
//                                intent.putExtra("store_name", bean.optString("store_name"));
//                                intent.putExtra("district_name", bean.optString("district_name"));
//                                activity.startActivity(intent);
                            } else if (object.optInt("state") == -1) {  // 绑定店铺
                                Intent intent = new Intent(activity, HuoHangEditAddressActivity.class);
                                intent.putExtra("isAdd", true);
                                activity.startActivity(intent);
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
    public void initIntentData() {
    }

    @Override
    public void initListener() {
        linearMessageMore.setOnClickListener(this);
        linearNewsMore.setOnClickListener(this);
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
                    ProgressDlgUtil.showProgressDlg("", activity);
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

                        if (localModel.getPartners() != null) {
                            if (localModel.getPartners().size() > 0) {
                                localRlAd.setVisibility(View.VISIBLE);
                                rl.setVisibility(View.VISIBLE);
                            } else {
                                localRlAd.setVisibility(View.GONE);
                                rl.setVisibility(View.GONE);
                            }
                        }
                        break;
                }
                return false;
            }
        });


        tSelect.setOnClickListener(this);

        //广告栏点击事件
        localRlAd.setOnClickListener(this);
        localClose.setOnClickListener(this);

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

    @Override
    public void initData() {

        //初始百度lbs
        initBaiduLbs();

        if (chanDialog == null) {
            chanDialog = new ChangCityDialog(activity);
        }

        if (localModel == null) {
            localModel = new LocalModel(activity, this);
        }

        if (!TextUtils.isEmpty(MyApplication.my_current_lat) && !TextUtils.isEmpty(MyApplication.my_current_lng)) {  // 定位成功
            isHasLocation(MyApplication.my_current_lat, MyApplication.my_current_lng);
        }else {
            loadLocal();
        }
        initWanSu();

    }

    private void loadLocal(){

        cityName = Util.preference.getString(MyString.LOCAL_CITY_NAME, "");
        cityId = Util.preference.getString(MyString.LOCAL_CITY_ID, "");


        if (!TextUtils.isEmpty(cityId)) {

            tTitle.setText(cityName);
            if (TextUtils.isEmpty(Util.preference.getString(MyString.LOCAL_COUNTRY_NAME, ""))) {
                tvCityName.setText(Util.preference.getString(MyString.LOCAL_COUNTRY_NAME, ""));
            } else {
                tvCityName.setText(cityName);
            }
            if (aCache.getAsJSONObject(TLUrl.getInstance().LOCALFRAGMENT) != null) {
                emptryLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                linerMian.setVisibility(View.VISIBLE);
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
                initAD();
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
            relativeNoCity.setVisibility(View.GONE);
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
                        tTitle.setTextColor(activity.getResources().getColor(R.color.grayy));
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

                    tvCityName.setTextColor(activity.getResources().getColor(R.color.red_o));
                    tTitle.setTextColor(activity.getResources().getColor(R.color.red_o));

                    tv_location.setVisibility(View.VISIBLE);
                    linSeKe.setVisibility(View.GONE);
                    linTianYun.setVisibility(View.VISIBLE);
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
            banners.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ScreenUtils.dip2px(activity, 399)));
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

    @OnClick({R.id.linear_change, R.id.liner_ser, R.id.gotomap, R.id.cesu})
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.gotomap: // 客服
                startActivity(new Intent(activity, KefuActivity.class));
                break;
            case R.id.cesu: // 测速
                initCeSuPop();
                break;
            case R.id.ll_new_more:
                intent = new Intent(activity, NewMoreActivity.class);
                intent.putExtra("subject", LocalModel.subject);
                intent.putExtra("countryId", LocalModel.countryId + "");
                startActivity(intent);
                break;
            case R.id.linear_message_more:
              /*  Intent rt=new Intent (activity,TestActivity.class);
                startActivity(rt);*/

               /* intent = new Intent(activity, MessageMoreActivity.class);
                intent.putExtra("cityId", cityId);
                startActivity(intent);*/
                break;
            case R.id.linear_news_more:

                intent = new Intent(activity, NewsMoreActivity.class);
                intent.putExtra("cityId", cityId);
                startActivity(intent);

                break;
            case R.id.linear_change:
                /*intent = new Intent(activity, CountryCityActivity4.class);
                startActivityForResult(intent, 1);*/

                if (chanDialog != null) {
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
                                tTitle.setTextColor(activity.getResources().getColor(R.color.grayy));
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

                            tvCityName.setTextColor(activity.getResources().getColor(R.color.red_o));
                            tTitle.setTextColor(activity.getResources().getColor(R.color.red_o));

                            tv_location.setVisibility(View.VISIBLE);
                            linSeKe.setVisibility(View.GONE);
                            linTianYun.setVisibility(View.VISIBLE);
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
                }

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
            case R.id.t_select:
/*//               intent = new Intent(activity, ForeignCityPickerActivity.class);
                intent = new Intent(activity, CountryCityActivity4.class);
                startActivityForResult(intent, 1);*/
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
                rl.setVisibility(View.GONE);
                //弹出广告的信息窗口
                showGuanggaoWindow();
                break;
            //关闭广告栏
            case R.id.local_close:
                //隐藏
                localRlAd.setVisibility(View.GONE);
                rl.setVisibility(View.GONE);
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
                        da.setCurrWanSu(to_time + "  ms");
                        data_ce.add(da);
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_compelete.setText("测速完成，你可以...");
                            adapter.addDatas(data_ce);
                            listview_cesu.setAdapter(adapter);

                            adapter.setSelectedPosition(0);

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
                            popupWindow.showAtLocation(root_view, Gravity.CENTER, 0, 0);

                            listview_cesu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    CeSu dabe = (CeSu) adapterView.getItemAtPosition(i);


                                    adapter.setSelectedPosition(i);
                                    adapter.notifyDataSetChanged();
                                    String basU_hua = dabe.getServerUrl().substring(dabe.getServerUrl().lastIndexOf("/") + 1, dabe.getServerUrl().length());

                                    Log.i("zdstra", "basU_base==" + dabe.getServerUrl());
                                    Log.i("zdstra", "basU_hua==" + basU_hua);

                                    TLUrl.URL_BASE = dabe.getServerUrl();
                                    TLUrl.URL_huayouhui = basU_hua;
                                    TLUrl.getInstance().isChange = true;

                                    Log.i("zdstra", "basU_base2==" + TLUrl.getInstance().getUrl());
                                    Log.i("zdstra", "basU_hua2==" + TLUrl.getInstance().getHuaUrl());
                                    MyApplication.saveCurrentHost(dabe.getServerUrl());
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

    private void getZhenXuan() {
        //        http://120.24.221.46:7076/city/queryNoCityImg?type=2
        emptryLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        HttpRequest.sendGet("http://120.24.221.46:7076/city/queryNoCityImg", "type=2", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds_zhenxuan", msg + "");

                        if (TextUtils.isEmpty(msg)) {
                            emptryLayout.setErrorType(EmptyLayout.NODATA);
                            return;
                        } else {
                            emptryLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                            linerMian.setVisibility(View.VISIBLE);
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
            View view = LayoutInflater.from(mContext).inflate(R.layout.banner_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.id_image);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            MyApplication.imageLoader.displayImage(data.img, imageView, MyApplication.getListOptions());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.cityId == 41 && position == 0) {
                        if (MyApplication.getInstance().getMykey() == null) {
                            Intent ty = new Intent(activity, WXEntryActivity.class);
                            ty.putExtra("isthome", true);
                            startActivity(ty);
                        } else {
                            checkIsInto();
                        }
                    } else {
                        Intent intentVp = new Intent(activity, GuanggaoActivity.class);
                        intentVp.putExtra("url", data.newsUrl);
                        activity.startActivity(intentVp);
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

    List<BannersBean> lunbo = new ArrayList<>();
    List<BannersBean> guanggao = new ArrayList<>();

    private void initTianQi() {
        if (!TextUtils.isEmpty(tian_qi_img)) {
            MyApplication.imageLoader.displayImage(tian_qi_img, ivYun, MyApplication.getAvatorOptions());
        }

        if (!TextUtils.isEmpty(tian_qi_wendu)) {
            if (!TextUtils.isEmpty(tian_qi_txt)) {

                tvTianqi.setText(tian_qi_wendu + "\n" + tian_qi_txt);
            } else {
                tvTianqi.setText(tian_qi_wendu);
            }
        }

    }

    private void initHuiLv() {

        if (!TextUtils.isEmpty(hb)) {
            tvHuilv.setText("1" + hb + "=" + hl + "ATS");
        }
        if (!TextUtils.isEmpty(hq_num)) {
            tvHuarenNum.setText(hq_num + "华侨在此生活");
        }
        if (!TextUtils.isEmpty(sy_num)) {
            tvShangye.setText(sy_num + "个商业服务");
        }
    }

    private void initBanners() {

        if (localModel.getBanners().size() > 0) {

            lunbo.clear();
            guanggao.clear();
            for (int i = 0; i < localModel.getBanners().size(); i++) {
                BannersBean bannersBean = localModel.getBanners().get(i);
                if (bannersBean.type == 2) {
                    guanggao.add(bannersBean);
                } else if (bannersBean.type == 1) {
                    lunbo.add(bannersBean);
                }
            }
        } else {

        }

        Log.i("zds", "initBanners: guanggao" + guanggao.size());
        Log.i("zds", "initBanners:lunbo " + lunbo.size());

        if (guanggao.size() > 0) {
            llGuanggao1.setVisibility(View.VISIBLE);
            for (int i = 0; i < guanggao.size(); i++) {
                if (i < 7) {
                    images.get(i).setVisibility(View.VISIBLE);
                    final BannersBean bannersBean = guanggao.get(i);
                    MyApplication.imageLoader.displayImage(bannersBean.img, images.get(i), MyApplication.getAvatorOptions());
                    images.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, GuanggaoActivity.class);
                            intent.putExtra("url", bannersBean.newsUrl);
                            startActivity(intent);
                        }
                    });
                }
            }
        } else {
            llGuanggao1.setVisibility(View.GONE);
            for (int i = 0; i < images.size(); i++) {
                images.get(i).setVisibility(View.GONE);
            }
        }

        if (lunbo.size() > 0) {
            Log.i("zds", "initBanners:lunbo img " + lunbo.get(0).img);
            banners.setVisibility(View.VISIBLE);
            initLunBo();
        } else {
            banners.setVisibility(View.GONE);
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

    /**
     * 点击底下的广告,弹出popupwindow
     *
     * @param parent
     */
    private PopupWindow popupWindow;

    private void showGuanggaoWindow() {
        View itemView = View.inflate(activity, R.layout.local_guanggao_detail_item, null);
        CircleImageView localDetailCivHead = (CircleImageView) itemView.findViewById(R.id.local_detail_civ_head); //合伙人的头像
        TextView localDetailTvCityName = (TextView) itemView.findViewById(R.id.local_detail_tv_city_name);  //城市名
        ImageView localDetailIvShiMing = (ImageView) itemView.findViewById(R.id.local_detail_iv_shiming);  //实名认证的图标
        TextView localDetailTvShiMing = (TextView) itemView.findViewById(R.id.local_detail_tv_shiming);  //实名认证
        ImageView localDetailIvGuanFang = (ImageView) itemView.findViewById(R.id.local_detail_iv_guanfang); //官方认证的图标
        TextView localDetailTvGuanFang = (TextView) itemView.findViewById(R.id.local_detail_tv_guanfang); //官方认证
        TextView localDetailTvCityPartner = (TextView) itemView.findViewById(R.id.local_detail_tv_city_partner);  //国家合伙人/国家合伙人
        TextView localDetailTvContain = (TextView) itemView.findViewById(R.id.local_item_detail_tv_contain);  //广告的内容
        TextView localDetailTvAddress = (TextView) itemView.findViewById(R.id.local_item_detail_tv_address);  //地址
        TextView localDetailTvName = (TextView) itemView.findViewById(R.id.local_detail_tv_name);          //姓名 刘会长
        ImageView localDetailIvWeiXin = (ImageView) itemView.findViewById(R.id.local_item_iv_weixin);  //微信二维码

        ImageView localDetailPhoneTb = (ImageView) itemView.findViewById(R.id.local_detail_phone_tb);   //电话的图标
        TextView localDetailPhone = (TextView) itemView.findViewById(R.id.local_detail_phone);      //电话号
        TextView localDetailMobile = (TextView) itemView.findViewById(R.id.local_detail_mobile);      //电话号

        ImageView localDetailWeixinTb = (ImageView) itemView.findViewById(R.id.local_detail_weixin_tb);  //微信的图标
        final TextView localDetailWeixin = (TextView) itemView.findViewById(R.id.local_detail_weixin);  //微信号


        ImageView localDetailClose = (ImageView) itemView.findViewById(R.id.local_detail_close);
        localDetailClose.setOnClickListener(this);

        if (localModel.getPartners().size() > 0) {

            for (int i = 0; i < localModel.getPartners().size(); i++) {
                PartnersBean partnersBean = localModel.getPartners().get(i);
                //城市名
                localDetailTvCityName.setText(partnersBean.cityName);
                localDetailTvCityPartner.setText("国家合伙人");
                //头像
                MyApplication.imageLoader.displayImage(partnersBean.headImg, localDetailCivHead, MyApplication.getAvatorOptions());
                //微信二维码
                MyApplication.imageLoader.displayImage(partnersBean.qrCode, localDetailIvWeiXin, MyApplication.getAvatorOptions());

                //是否实名认证   0否  1是
                if (partnersBean.isSq == 1) {
                    //认证标志
                    localDetailIvShiMing.setVisibility(View.VISIBLE);
                    localDetailTvShiMing.setText("实名认证");
                } else {
                    localDetailIvShiMing.setVisibility(View.INVISIBLE);
                    localDetailTvShiMing.setVisibility(View.INVISIBLE);
                }
                //是否授权认证   0否  1是
                if (partnersBean.isSq == 1) {
                    //认证标志
                    localDetailIvGuanFang.setVisibility(View.VISIBLE);
                    localDetailTvGuanFang.setText("官方授权");
                } else {
                    //否则隐藏
                    localDetailIvGuanFang.setVisibility(View.INVISIBLE);
                    localDetailTvGuanFang.setVisibility(View.INVISIBLE);
                }
                //  内容
                if (partnersBean.intro != null) {

//                    localTvAdContent.setText(partnersBean.intro);
//                    localDetailTvCityPartner.setText("国家合伙人");
                    localDetailTvContain.setText(partnersBean.intro);
                }
                //  地址
                if (!TextUtils.isEmpty(partnersBean.address)) {
                    localDetailTvAddress.setText("地址：" + partnersBean.address);
                }
                //负责人
                localDetailTvName.setText("负责人：" + partnersBean.fzMan);
                // 电话
                if (partnersBean.phone != null) {
                    localDetailPhoneTb.setVisibility(View.VISIBLE);
                    localDetailPhone.setText(partnersBean.phone);
                } else {
                    localDetailPhoneTb.setVisibility(View.INVISIBLE);
                    localDetailPhone.setVisibility(View.INVISIBLE);
                }
                //手机号
                if (partnersBean.mobile != null) {
                    localDetailPhoneTb.setVisibility(View.VISIBLE);
                    localDetailMobile.setText(partnersBean.mobile);
                } else {
                    localDetailPhoneTb.setVisibility(View.INVISIBLE);
                    localDetailMobile.setVisibility(View.INVISIBLE);
                }
                //微信
                if (partnersBean.weChat != null) {
                    localDetailWeixinTb.setVisibility(View.VISIBLE);
                    localDetailWeixin.setText(partnersBean.weChat);
                    localDetailWeixin.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                            // 将文本内容放到系统剪贴板里。
                            cm.setText(localDetailWeixin.getText());
                            Toast.makeText(getContext(), "微信已经复制到剪切板了", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                } else {
                    localDetailWeixinTb.setVisibility(View.INVISIBLE);
                    localDetailWeixin.setVisibility(View.INVISIBLE);
                }
            }
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
        } else {

        }
    }

    /**
     * 热点资讯的item  本地新闻
     */
    private View getNewView(int position, final New aNew) {
        View view = inflater.inflate(R.layout.local_item_new2, null);
        TextView t_dain = (TextView) view.findViewById(R.id.t_dain);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);

        if (position == 0) {
            t_dain.setBackgroundColor(getResources().getColor(R.color.red));
        } else {
            t_dain.setBackgroundColor(getResources().getColor(R.color.gray));
        }

        if (!TextUtils.isEmpty(aNew.getTitle())) {
            tv_title.setText(aNew.getTitle());
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        if (!TextUtils.isEmpty(activy.jbf)) {
            t_jubanfang.setText("举办方：" + activy.jbf);
        }

        if (!TextUtils.isEmpty(activy.ads)) {
            t_address.setText(activy.ads);
        }

        if (activy.date < 2 * 1000000000) {
            t_time.setText(Util.format1.format(activy.date * 1000));
        } else {
            t_time.setText(Util.format1.format(activy.date));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    public void loadSuccess() {
        ProgressDlgUtil.stopProgressDlg();
        emptryLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        linerMian.setVisibility(View.VISIBLE);
        localRlAd.setVisibility(View.VISIBLE);
        rl.setVisibility(View.VISIBLE);
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        initMenuList(cityId);
        initBanners();
        initTianQi();
        initHuiLv();
        initAD();
        initMsgList(cityId);
        initNewList();
        getLLHDMsg();
    }

    public BDLocationListener myListener = new MyLocationListener();
    private void initBaiduLbs() {

        mLocationClient = new LocationClient(activity);
        mLocationClient.registerLocationListener( myListener );    //注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
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
    double latitude  ;//获取经度
    double longitude  ;//获取纬度

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

                MyApplication.my_current_lat=current_lat;
                MyApplication.my_current_lng=current_lng;
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
