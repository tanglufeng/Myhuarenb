package com.abcs.haiwaigou.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.AllGoodsActivity;
import com.abcs.haiwaigou.activity.AllStoreActivity;
import com.abcs.haiwaigou.activity.GoodsDetailActivity2;
import com.abcs.haiwaigou.activity.PayWayActivity;
import com.abcs.haiwaigou.activity.StoreActivity;
import com.abcs.haiwaigou.adapter.MyGridAdapter;
import com.abcs.haiwaigou.adapter.YunGouAdapter;
import com.abcs.haiwaigou.db.HaiwaigouDAO;
import com.abcs.haiwaigou.fragment.adapter.HWGFragmentAdapter;
import com.abcs.haiwaigou.fragment.customtool.FullyGridLayoutManager;
import com.abcs.haiwaigou.fragment.viewholder.HWGFragmentViewHolder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.model.Store;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.utils.mCountDownTimer;
import com.abcs.haiwaigou.view.CircleIndicator;
import com.abcs.haiwaigou.view.MyGridView;
import com.abcs.haiwaigou.view.XScrollView;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.adapter.CommonAdapter;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.HqbViewHolder;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.huaqiaobang.ytbt.im.sdkhelper.SDKCoreHelper;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/2/23.
 */
public class HaiwaiGouFragment extends Fragment implements HWGFragmentViewHolder.ItemOnClick, View.OnClickListener {

    private static final String TAG = "LOG_DEBUG";
    @InjectView(R.id.linea_layoutYungou)
    LinearLayout lineaLayoutYungou;
    @InjectView(R.id.iv_shao)
    ImageView ivShao;
    @InjectView(R.id.tv_top_title)
    TextView tvTopTitle;
    @InjectView(R.id.iv_refresh)
    ImageView ivRefresh;
    @InjectView(R.id.viewPager_baner)
    ViewPager viewPagerBaner;
    @InjectView(R.id.store_gridView)
    MyGridView storeGridView;
    @InjectView(R.id.countdown_hour)
    TextView countdownHour;
    @InjectView(R.id.countdown_minute)
    TextView countdownMinute;
    @InjectView(R.id.countdown_second)
    TextView countdownSecond;
    @InjectView(R.id.mScrollView)
    XScrollView mScrollView;
    @InjectView(R.id.circle_indicator)
    CircleIndicator circleIndicator;
    @InjectView(R.id.jiantou)
    ImageView jiantou;
    @InjectView(R.id.jiantou2)
    ImageView jiantou2;
    @InjectView(R.id.header_layout)
    LinearLayout headerLayout;
    @InjectView(R.id.linear_qianggou)
    LinearLayout linearQianggou;
    @InjectView(R.id.linear_gerenhuli)
    LinearLayout linearGerenhuli;
    @InjectView(R.id.img_food1)
    ImageView imgFood1;
    @InjectView(R.id.img_food2)
    ImageView imgFood2;
    @InjectView(R.id.img_food3)
    ImageView imgFood3;
    @InjectView(R.id.img_food4)
    ImageView imgFood4;
    @InjectView(R.id.img_food5)
    ImageView imgFood5;
    @InjectView(R.id.img_food6)
    ImageView imgFood6;
    @InjectView(R.id.linear_foods)
    LinearLayout linearFoods;
    @InjectView(R.id.linear_muying)
    LinearLayout linearMuying;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.linear_cainixihuan)
    LinearLayout linearCainixihuan;
    @InjectView(R.id.img_geren1)
    ImageView imgGeren1;
    @InjectView(R.id.img_geren2)
    ImageView imgGeren2;
    @InjectView(R.id.img_geren3)
    ImageView imgGeren3;
    @InjectView(R.id.img_geren4)
    ImageView imgGeren4;
    @InjectView(R.id.img_geren5)
    ImageView imgGeren5;
    @InjectView(R.id.img_geren6)
    ImageView imgGeren6;
    @InjectView(R.id.img_geren7)
    ImageView imgGeren7;
    @InjectView(R.id.img_geren8)
    ImageView imgGeren8;
    @InjectView(R.id.rl_more_geren)
    RelativeLayout rlMoreGeren;
    @InjectView(R.id.rl_more_foods)
    RelativeLayout rlMoreFoods;
    @InjectView(R.id.rl_more_muying)
    RelativeLayout rlMoreMuying;
    @InjectView(R.id.img_muying1)
    ImageView imgMuying1;
    @InjectView(R.id.img_muying2)
    ImageView imgMuying2;
    @InjectView(R.id.img_muying3)
    ImageView imgMuying3;
    @InjectView(R.id.img_muying4)
    ImageView imgMuying4;
    @InjectView(R.id.img_muying5)
    ImageView imgMuying5;
    @InjectView(R.id.img_muying6)
    ImageView imgMuying6;
    @InjectView(R.id.img_muying7)
    ImageView imgMuying7;
    @InjectView(R.id.img_muying8)
    ImageView imgMuying8;
    @InjectView(R.id.store_tishixiaoxi)
    TextView storeTishixiaoxi;
    //    @InjectView(R.id.yungou_gridView)
//    MyGridView yungouGridView;
    @InjectView(R.id.jiantou3)
    ImageView jiantou3;
    @InjectView(R.id.t_geren)
    TextView tGeren;
    @InjectView(R.id.t_foods)
    TextView tFoods;
    @InjectView(R.id.t_muying)
    TextView tMuying;
    @InjectView(R.id.relative_search)
    RelativeLayout relativeSearch;
    @InjectView(R.id.img_qiang1)
    ImageView imgQiang1;
    @InjectView(R.id.img_qiang2)
    ImageView imgQiang2;
    @InjectView(R.id.img_qiang3)
    ImageView imgQiang3;
    @InjectView(R.id.img_qiang4)
    ImageView imgQiang4;
    @InjectView(R.id.countdown_day)
    TextView countdownDay;
    @InjectView(R.id.viewPager_baner1)
    ViewPager viewPagerBaner1;
    @InjectView(R.id.circle_indicator1)
    CircleIndicator circleIndicator1;
    @InjectView(R.id.viewPager_baner2)
    ViewPager viewPagerBaner2;
    @InjectView(R.id.circle_indicator2)
    CircleIndicator circleIndicator2;
    @InjectView(R.id.viewPager_baner3)
    ViewPager viewPagerBaner3;
    @InjectView(R.id.circle_indicator3)
    CircleIndicator circleIndicator3;
    @InjectView(R.id.img_ad1)
    ImageView imgAd1;
    @InjectView(R.id.img_ad2)
    ImageView imgAd2;
    @InjectView(R.id.img_ad3)
    ImageView imgAd3;
    @InjectView(R.id.img_ad4)
    ImageView imgAd4;
    @InjectView(R.id.viewPager_baner4)
    ViewPager viewPagerBaner4;
    @InjectView(R.id.circle_indicator4)
    CircleIndicator circleIndicator4;
    @InjectView(R.id.btn_reload)
    Button btnReload;
    @InjectView(R.id.linear_no_network)
    LinearLayout linearNoNetwork;


    private View view;
//    private int[] bannerData = new int[]{R.drawable.tu_1, R.drawable.tu_2, R.drawable.tu_3, R.drawable.tu_4};
    private int currentposition;
    boolean isRefresh;
    private ArrayList<Goods> goodsImgs = new ArrayList<Goods>();
    private ArrayList<Goods> adImgs = new ArrayList<Goods>();
    private ArrayList<Goods> qiangImgs = new ArrayList<Goods>();
    private ArrayList<Goods> muyingImgs = new ArrayList<Goods>();
    private ArrayList<Goods> foodsImgs = new ArrayList<Goods>();
    private ArrayList<Goods> gerenImgs = new ArrayList<Goods>();
    private ArrayList<Goods> yungouList = new ArrayList<Goods>();
    private ArrayList<Store> storeList = new ArrayList<Store>();
    private ArrayList<String> storeId = new ArrayList<String>();
    private ArrayList<Goods> gcId = new ArrayList<Goods>();
    private List<ImageView> list = new ArrayList<ImageView>();
    private List<ImageView> list1 = new ArrayList<ImageView>();
    private List<ImageView> list2 = new ArrayList<ImageView>();
    private List<ImageView> list3 = new ArrayList<ImageView>();
    private Handler handler = new Handler();
    private RequestQueue mRequestQueue;
    private MyGridAdapter myGridAdapter;
    private ArrayList<View> girds = new ArrayList<View>();
    LoadPicture loadPicture = new LoadPicture();
    private boolean isFirst = true;
    String geren_gcid;
    String food_gcid;
    String muying_gcid;
    YunGouAdapter yunGouAdapter;
    private SDKCoreHelper sdkCoreHelper = SDKCoreHelper.getInstance();
    private TextView[] times;

    HaiwaigouDAO haiwaigouDAO;
    private boolean isDestory=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_shopping, null);
        }
        ButterKnife.inject(this, view);
        mRequestQueue = Volley.newRequestQueue(getContext());
        haiwaigouDAO = new HaiwaigouDAO(getContext());

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            LinearLayout titlebar = (LinearLayout) view.findViewById(R.id.linear_title);
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) titlebar.getLayoutParams();
//            params.setMargins(0, ((MainActivity) getActivity()).getStatusBarHeight(), 0, 0);
//            titlebar.setLayoutParams(params);
//        }
        initScrollView();

        if (NetworkUtils.isNetAvailable(getContext())) {
            Log.i("zjz", "网络连接正常");
            initBanner();
            initStoreGridView();
            initYunGouDate();
            initBannerAd();
            initLinearQiangGou();
            initGoodsGCid();
            initReMai();
            linearNoNetwork.setVisibility(View.GONE);
        } else {
            linearNoNetwork.setVisibility(View.VISIBLE);
            linearQianggou.setVisibility(View.GONE);
            linearGerenhuli.setVisibility(View.GONE);
            initLocalBanner();
            initLocalBannerAd();
            initLocalFood();
            initLocalMuYing();
            initLocalCainiLike();

        }

//        initCaiNiLike();
        setOnClickListener();


        //注册消息接受广播
        IntentFilter filter = new IntentFilter("com.abcs.huaqiaobang.shoppingxiaoxi");
        getActivity().registerReceiver(xiaoxiReceiver, filter);
//        if (SDKCoreHelper.count != 0) {
//            storeTishixiaoxi.setVisibility(View.VISIBLE);
//            storeTishixiaoxi.setText(SDKCoreHelper.count + "");
//        }
//        ivRefresh.setClickable(SDKCoreHelper.rlLogin ? true : false);


        return view;
    }


    private void initLocalCainiLike() {
        hwgFragmentAdapter = new HWGFragmentAdapter(getActivity(),this, true);
        hwgFragmentAdapter.getDatas().clear();
        fullyGridLayoutManager = new FullyGridLayoutManager(getContext(), 2);
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(fullyGridLayoutManager);
        recyclerView.setAdapter(hwgFragmentAdapter);

        ArrayList<Goods> goodses = new ArrayList<Goods>();
        goodses = haiwaigouDAO.selectByAllRemai();
        Log.i("zjz", "goodses=" + goodses.size());
        for (int i = 0; i < goodses.size(); i++) {
            Goods g = new Goods();
            g.setGoods_id(goodses.get(i).getGoods_id());
            g.setPicarr(TLUrl.getInstance().URL_hwg_remai + goodses.get(i).getPicarr());
            g.setTitle(goodses.get(i).getTitle());
            g.setMoney(goodses.get(i).getMoney());
            hwgFragmentAdapter.getDatas().add(g);
        }
        linearCainixihuan.setVisibility(View.VISIBLE);
        hwgFragmentAdapter.notifyDataSetChanged();
    }

    private void initLocalMuYing() {
        linearMuying.setVisibility(View.VISIBLE);
        muyingImgs = haiwaigouDAO.selectByAllMuYing();
        Log.i("zjz", "muyingImgs=" + muyingImgs.size());
        for (int i = 0; i < muyingImgs.size(); i++) {
            switch (i) {
                case 0:
//                    loadPicture.initPicture(imgMuying1, muyingImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgMuying1, true, muyingImgs.get(i).getPicarr(), Integer.parseInt(muyingImgs.get(i).getGoods_id()));
                    imgMuying1.setOnClickListener(null);
                    break;
                case 1:
//                    loadPicture.initPicture(imgMuying2, muyingImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgMuying2, true, muyingImgs.get(i).getPicarr(), Integer.parseInt(muyingImgs.get(i).getGoods_id()));
                    imgMuying2.setOnClickListener(null);
                    break;
                case 2:
//                    loadPicture.initPicture(imgMuying3, muyingImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgMuying3, true, muyingImgs.get(i).getPicarr(), Integer.parseInt(muyingImgs.get(i).getGoods_id()));
                    imgMuying3.setOnClickListener(null);
                    break;
                case 3:
//                    loadPicture.initPicture(imgMuying4, muyingImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgMuying4, true, muyingImgs.get(i).getPicarr(), Integer.parseInt(muyingImgs.get(i).getGoods_id()));
                    imgMuying4.setOnClickListener(null);
                    break;
                case 4:
//                    loadPicture.initPicture(imgMuying5, muyingImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgMuying5, true, muyingImgs.get(i).getPicarr(), Integer.parseInt(muyingImgs.get(i).getGoods_id()));
                    imgMuying5.setOnClickListener(null);
                    break;
                case 5:
//                    loadPicture.initPicture(imgMuying6, muyingImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgMuying6, true, muyingImgs.get(i).getPicarr(), Integer.parseInt(muyingImgs.get(i).getGoods_id()));
                    imgMuying6.setOnClickListener(null);
                    break;
                case 6:
//                    loadPicture.initPicture(imgMuying7, muyingImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgMuying7, true, muyingImgs.get(i).getPicarr(), Integer.parseInt(muyingImgs.get(i).getGoods_id()));
                    imgMuying7.setOnClickListener(null);
                    break;
                case 7:
//                    loadPicture.initPicture(imgMuying8, muyingImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgMuying8, true, muyingImgs.get(i).getPicarr(), Integer.parseInt(muyingImgs.get(i).getGoods_id()));
                    imgMuying8.setOnClickListener(null);
                    break;
            }
        }
    }

    private void initLocalFood() {
        linearFoods.setVisibility(View.VISIBLE);
        foodsImgs = haiwaigouDAO.selectByAllFood();
        Log.i("zjz", "foodsImgs=" + foodsImgs.size());
        for (int i = 0; i < foodsImgs.size(); i++) {
            switch (i) {
                case 0:
//                    loadPicture.initPicture(imgFood1, foodsImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgFood1, true, foodsImgs.get(i).getPicarr(), Integer.parseInt(foodsImgs.get(i).getGoods_id()));
                    imgFood1.setOnClickListener(null);
                    break;
                case 1:
//                    loadPicture.initPicture(imgFood2, foodsImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgFood2, true, foodsImgs.get(i).getPicarr(), Integer.parseInt(foodsImgs.get(i).getGoods_id()));
                    imgFood2.setOnClickListener(null);
                    break;
                case 2:
//                    loadPicture.initPicture(imgFood3, foodsImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgFood3, true, foodsImgs.get(i).getPicarr(), Integer.parseInt(foodsImgs.get(i).getGoods_id()));
                    imgFood3.setOnClickListener(null);
                    break;
                case 3:
//                    loadPicture.initPicture(imgFood4, foodsImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgFood4, true, foodsImgs.get(i).getPicarr(), Integer.parseInt(foodsImgs.get(i).getGoods_id()));
                    imgFood4.setOnClickListener(null);
                    break;
                case 4:
//                    loadPicture.initPicture(imgFood5, foodsImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgFood5, true, foodsImgs.get(i).getPicarr(), Integer.parseInt(foodsImgs.get(i).getGoods_id()));
                    imgFood5.setOnClickListener(null);
                    break;
                case 5:
//                    loadPicture.initPicture(imgFood6, foodsImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgFood6, true, foodsImgs.get(i).getPicarr(), Integer.parseInt(foodsImgs.get(i).getGoods_id()));
                    imgFood6.setOnClickListener(null);
                    break;
            }
        }
    }

    private void initLocalBannerAd() {
        adImgs = haiwaigouDAO.selectByAllBannerAd();
        Log.i("zjz", "adImgsize=" + adImgs.size());
        for (int i = 0; i < adImgs.size(); i++) {
            switch (i) {
                case 0:
                    LoadPicture loadPicture = new LoadPicture();
                    loadPicture.initPicture(imgAd4, true, adImgs.get(i).getPicarr(), i);
                    imgAd1.setOnClickListener(null);
                    break;
                case 1:
                    LoadPicture loadPicture2 = new LoadPicture();
                    loadPicture2.initPicture(imgAd3, true, adImgs.get(i).getPicarr(), i);
                    imgAd2.setOnClickListener(null);
                    break;
                case 2:
                    LoadPicture loadPicture3 = new LoadPicture();
                    loadPicture3.initPicture(imgAd2, true, adImgs.get(i).getPicarr(), i);
                    imgAd3.setOnClickListener(null);
                    break;
                case 3:
                    LoadPicture loadPicture4 = new LoadPicture();
                    loadPicture4.initPicture(imgAd1, true, adImgs.get(i).getPicarr(), i);
                    imgAd4.setOnClickListener(null);
                    break;
            }

        }
    }

    private void initLocalBanner() {
        goodsImgs = haiwaigouDAO.selectByAllBanner();
        Log.i("zjz", "goodsImgsize=" + goodsImgs.size());
        list.clear();
        for (int i = 0; i < goodsImgs.size(); i++) {
            ImageView view = new ImageView(getContext());
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            LoadPicture loadPicture = new LoadPicture();
            loadPicture.initPicture(view, true, goodsImgs.get(i).getPicarr(), i);
//                loadPicture.initPicture(view, goodsImgs.get(i).getPicarr());
            list.add(view);
            final int m = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                }
            });
        }
        viewPagerBaner.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
        viewPagerBaner.setCurrentItem(0);
        circleIndicator.setViewPager(viewPagerBaner, list.size());
        if (!isRefresh) {
            handler.post(runnable);
        }
    }

    private void initBannerAd() {
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_home, null, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                adImgs.clear();
                                Log.i("zjz", "banner:连接成功");

                                JSONArray array = object.getJSONArray("datas");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object3 = array.getJSONObject(i);
                                    Iterator key = object3.keys();
                                    if (key.hasNext() && key.next().equals("home1") && adImgs.size() < 4) {
                                        JSONObject image = object3.optJSONObject("home1");
                                        Goods g = new Goods();
                                        g.setKeywords(image.optString("data"));
                                        g.setPicarr(image.optString("image"));
                                        g.setGoods_id(i + "");
                                        adImgs.add(g);
                                        Log.i("zjz", "pic=" + image.optString("image"));
                                        if (haiwaigouDAO.selectByAllBannerAd().size() < 4) {
                                            haiwaigouDAO.insertBannerAD(g);
                                            Log.i("zjz", "增加ad" + i);
                                        } else {
                                            haiwaigouDAO.updateBannerAd(g);
                                            Log.i("zjz", "修改ad" + i);
                                        }

                                    }

                                }
                                Log.i("zjz", "zjzsize=" + adImgs.size());
                                initBannerAdDate();
                            } else {
                                Log.i("zjz", "banner:解析失败");
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

    private void initBannerAdDate() {

        for (int i = 0; i < adImgs.size(); i++) {
            switch (i) {
                case 0:
//                    initBannerViewPgaer(viewPagerBaner1, circleIndicator1);
//                    list1.clear();
//                    ImageView view = new ImageView(getContext());
//                    view.setScaleType(ImageView.ScaleType.FIT_XY);
//                    LoadPicture loadPicture = new LoadPicture();
//                    loadPicture.initPicture(view, goodsImgs.get(i).getPicarr());
//                    list1.add(view);
//                    view.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View arg0) {
//
//                        }
//                    });
//
//                    viewPagerBaner1.setAdapter(pagerAdapter1);
//                    pagerAdapter1.notifyDataSetChanged();
//                    viewPagerBaner1.setCurrentItem(0);
//                    circleIndicator1.setViewPager(viewPagerBaner1, list1.size());
                    LoadPicture loadPicture = new LoadPicture();
//                    loadPicture.initPicture(imgAd1, adImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgAd1, true, adImgs.get(i).getPicarr(), i);
                    imgAd1.setOnClickListener(this);
                    break;
                case 1:
//                    initBannerViewPgaer(viewPagerBaner2, circleIndicator2);
//                    list2.clear();
//                    ImageView view2 = new ImageView(getContext());
//                    view2.setScaleType(ImageView.ScaleType.FIT_XY);
//                    LoadPicture loadPicture2 = new LoadPicture();
//                    loadPicture2.initPicture(view2, goodsImgs.get(i).getPicarr());
//                    list2.add(view2);
//                    view2.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View arg0) {
//
//                        }
//                    });
//
//                    viewPagerBaner2.setAdapter(pagerAdapter2);
//                    pagerAdapter2.notifyDataSetChanged();
//                    viewPagerBaner2.setCurrentItem(0);
//                    circleIndicator2.setViewPager(viewPagerBaner2, list2.size());
                    LoadPicture loadPicture2 = new LoadPicture();
//                    loadPicture2.initPicture(imgAd2, adImgs.get(i).getPicarr());
                    loadPicture2.initPicture(imgAd2, true, adImgs.get(i).getPicarr(), i);
                    imgAd2.setOnClickListener(this);
                    break;
                case 2:
//                    initBannerViewPager(viewPagerBaner3, circleIndicator3);
//                    list3.clear();
//                    ImageView view3 = new ImageView(getContext());
//                    view3.setScaleType(ImageView.ScaleType.FIT_XY);
//                    LoadPicture loadPicture3 = new LoadPicture();
//                    loadPicture3.initPicture(view3, goodsImgs.get(i).getPicarr());
//                    list3.add(view3);
//                    view3.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View arg0) {
//
//                        }
//                    });
//
//                    viewPagerBaner3.setAdapter(pagerAdapter3);
//                    pagerAdapter3.notifyDataSetChanged();
//                    viewPagerBaner3.setCurrentItem(0);
//                    circleIndicator3.setViewPager(viewPagerBaner3, list3.size());
                    LoadPicture loadPicture3 = new LoadPicture();
//                    loadPicture3.initPicture(imgAd3, adImgs.get(i).getPicarr());
                    loadPicture3.initPicture(imgAd3, true, adImgs.get(i).getPicarr(), i);
                    imgAd3.setOnClickListener(this);
                    break;
                case 3:
                    LoadPicture loadPicture4 = new LoadPicture();
//                    loadPicture4.initPicture(imgAd4, adImgs.get(i).getPicarr());
                    loadPicture4.initPicture(imgAd4, true, adImgs.get(i).getPicarr(), i);
                    imgAd4.setOnClickListener(this);
                    break;
            }

        }
    }

    private void initReMai() {
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_good_remai, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        gcId.clear();
                        Log.i("zjz", "goodssort:连接成功");
                        JSONObject datas = response.getJSONObject("datas");

                        Iterator keys = datas.keys();
                        String key;
                        JSONObject value;
//                        Map valueMap=new HashMap();
                        while (keys.hasNext()) {
                            key = (String) keys.next();
                            value = (JSONObject) datas.get(key);
                            Log.i("zjz", "value=" + value);
//                            valueMap.put(key, value);
                            JSONObject valueObject = value.getJSONObject("recommend");
                            if (valueObject.optString("name").equals("猜您喜欢")) {
                                initCaiNiLike(value.getJSONObject("goods_list"));
                            }
                            Log.i("zjz", "name=" + valueObject.optString("name"));
                        }


//                        Log.i("zjz","valueMap="+valueMap);
//                        Log.i("zjz","valueMapSize="+valueMap.size());

                    } else {
                        Log.i("zjz", "goodsActivity解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mRequestQueue.add(jr);
    }


    private void initGoodsGCid() {
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_goodssort, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        gcId.clear();
                        Log.i("zjz", "goodssort:连接成功");
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("class_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            Goods g = new Goods();
                            g.setGc_id(object1.optString("gc_id"));
                            g.setTitle(object1.optString("gc_name"));
                            g.setSubhead(object1.getString("text"));
                            gcId.add(g);
                        }
                        for (int j = 0; j < gcId.size(); j++) {
                            switch (j) {
                                case 1:
                                    tFoods.setText(gcId.get(j).getTitle());
                                    initLinearFoods(gcId.get(j).getGc_id());
                                    food_gcid = gcId.get(j).getGc_id();
                                    break;
                                case 2:
                                    tGeren.setText(gcId.get(j).getTitle());
                                    initLinearGeren(gcId.get(j).getGc_id());
                                    geren_gcid = gcId.get(j).getGc_id();
                                    break;
                                case 3:
                                    tMuying.setText(gcId.get(j).getTitle());
                                    initLinearMuYing(gcId.get(j).getGc_id());
                                    muying_gcid = gcId.get(j).getGc_id();
                                    break;
                            }
                        }

                    } else {
                        Log.i("zjz", "goodsActivity解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mRequestQueue.add(jr);
    }

    private void initYunGouDate() {
        HttpRequest.sendGet(TLUrl.getInstance().URL_GOODS_ALL, "key=shopList&pages=1&pagelist=3", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("status") == 1) {
                                yungouList.clear();
                                Log.i("zjz", "allGoodsFragment:连接成功");
                                JSONArray jsonArray = object.getJSONArray("msg");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    Goods g = new Goods();
                                    g.setTitle(object1.getString("title"));
                                    g.setMoney(object1.getDouble("money"));
                                    g.setCanyurenshu(object1.getInt("canyurenshu"));
                                    g.setZongrenshu(object1.getInt("zongrenshu"));
                                    g.setShenyurenshu(g.getZongrenshu() - g.getCanyurenshu());
                                    g.setPicarr(object1.getString("picarr"));
                                    g.setId(object1.getInt("id"));
                                    g.setQishu(object1.getInt("qishu"));
                                    g.setLayoutType(1);
                                    if (yungouList.size() < 4) {
                                        yungouList.add(g);
                                    }
                                }
//                                yunGouAdapter=new YunGouAdapter(getActivity(),yungouList);
//                                yungouGridView.setAdapter(yunGouAdapter);
//
//                                yunGouAdapter.notifyDataSetChanged();
                                initYunGouView();
                            } else {
                                Log.i("zjz", "allGoodsFragment:解析失败");
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

    private void initYunGouView() {
        lineaLayoutYungou.removeAllViews();
        for (int i = 0; i < 3; i++) {
            View yungouView = View.inflate(getActivity(), R.layout.fragment_shopping_yungou_item, null);
            View viewline = new View(getContext());
            viewline.setBackgroundColor(Color.parseColor("#ffcdcdcd"));
            viewline.setLayoutParams(new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT));
            yungouView.setLayoutParams(new LinearLayout.LayoutParams(Util.WIDTH / 3, LinearLayout.LayoutParams.WRAP_CONTENT));
            ImageView imageView = (ImageView) yungouView.findViewById(R.id.img_goods_icon);
            loadPicture.initPicture(imageView, yungouList.get(i).getPicarr());
            TextView tGoodsName = (TextView) yungouView.findViewById(R.id.t_goods_name);
            tGoodsName.setText(yungouList.get(i).getTitle());
            TextView tBuyNum = (TextView) yungouView.findViewById(R.id.t_buy_num);
            tBuyNum.setText(yungouList.get(i).getCanyurenshu() + "");
            TextView tTotalNum = (TextView) yungouView.findViewById(R.id.t_total_num);
            tTotalNum.setText(yungouList.get(i).getZongrenshu() + "");
            ProgressBar progressBar = (ProgressBar) yungouView.findViewById(R.id.processbar);
            int pro = (int) (Float.valueOf(yungouList.get(i).getCanyurenshu())
                    / Float.valueOf(yungouList.get(i).getZongrenshu()) * 100);
            progressBar.setProgress(pro);
            Button button = (Button) yungouView.findViewById(R.id.btn_buy);
            final int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MyApplication.getInstance().self == null) {
                        Intent intent = new Intent(getActivity(), WXEntryActivity.class);
                        startActivity(intent);
                        return;
                    }
                    Log.i("zjz", "url=" + TLUrl.getInstance().URL_GOODS_SHOPCAR + "?" + "uid=" + MyApplication.getInstance().self.getId() + "&id="
                            + yungouList.get(finalI).getId() + "&num=1" + "&qishu=" + yungouList.get(finalI).getQishu());
                    HttpRequest.sendGet(TLUrl.getInstance().URL_GOODS_SHOPCAR, "uid=" + MyApplication.getInstance().self.getId() + "&id="
                            + yungouList.get(finalI).getId() + "&num=1" + "&qishu=" + yungouList.get(finalI).getQishu(), new HttpRevMsg() {
                        @Override
                        public void revMsg(final String msg) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject object = new JSONObject(msg);
                                        if (object.getInt("status") == 1) {
                                            Log.i("zjz", "carFragment添加成功");
                                            Intent intent = new Intent(getActivity(), PayWayActivity.class);
                                            intent.putExtra("yungou", true);
                                            getActivity().startActivity(intent);
                                        } else {
                                            Log.i("zjz", "carFragment添加失败");
                                        }
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        Log.i("zjz", e.toString());
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });

                }
            });

            lineaLayoutYungou.addView(yungouView);
            lineaLayoutYungou.addView(viewline);
        }

    }

    private void setOnClickListener() {
        btnReload.setOnClickListener(this);
        rlMoreFoods.setOnClickListener(this);
        rlMoreGeren.setOnClickListener(this);
        rlMoreMuying.setOnClickListener(this);
        relativeSearch.setOnClickListener(this);
        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getContext(), MainActivity.class));
//                SDKCoreHelper.count = 0;
//                storeTishixiaoxi.setVisibility(View.GONE);
//                MyApplication.getInstance().getMainActivity().mHandler.sendEmptyMessage(com.abcs.huaqiaobang.main.MainActivity.NOTIFYCATION_CHANGE2);
            }
        });
        ivShao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), WXEntryActivity.class));
            }
        });
    }

    HWGFragmentAdapter hwgFragmentAdapter;
    FullyGridLayoutManager fullyGridLayoutManager;

    private void initCaiNiLike(JSONObject object) {


        hwgFragmentAdapter = new HWGFragmentAdapter(getActivity(),this, true);
        fullyGridLayoutManager = new FullyGridLayoutManager(getContext(), 2);
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(fullyGridLayoutManager);
        recyclerView.setAdapter(hwgFragmentAdapter);
        //添加分割线
//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_size2);
//        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        initDatas(object);
    }

    private void initDatas(JSONObject object) {
        hwgFragmentAdapter.getDatas().clear();

        try {

            Iterator keys = object.keys();
            String key;
            JSONObject value;
            while (keys.hasNext()) {
                key = (String) keys.next();

                value = (JSONObject) object.get(key);
                Log.i("zjz", "value=" + value);
                Goods g = new Goods();
                g.setGoods_id(value.optString("goods_id"));
                g.setPicarr(TLUrl.getInstance().URL_hwg_remai + value.optString("goods_pic"));
                g.setTitle(value.optString("goods_name"));
                g.setMoney(value.optDouble("goods_price"));
                hwgFragmentAdapter.getDatas().add(g);
                if (haiwaigouDAO.selectByAllRemai().size() < 4) {
                    haiwaigouDAO.insertRemai(g);
                } else {
                    haiwaigouDAO.updateRemai(g);
                }


            }
            linearCainixihuan.setVisibility(View.VISIBLE);
            hwgFragmentAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.i("zjz", e.toString());
            e.printStackTrace();
        }

    }

    private void initLinearMuYing(String gcid) {
        muyingImgs = new ArrayList<Goods>();
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_home_all_goods + "&key=4&page=8&curpage=1&gc_id=" + gcid, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        muyingImgs.clear();
                        Log.i("zjz", "group:连接成功");
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("goods_list");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            Goods g = new Goods();
                            g.setPicarr(object1.optString("goods_image_url"));
                            g.setGoods_id(object1.optString("goods_id"));
                            muyingImgs.add(g);
                            if (haiwaigouDAO.selectByAllMuYing().size() < 8) {
                                haiwaigouDAO.insertMuYing(g);
                            } else {
                                haiwaigouDAO.updateMuYing(g);
                            }

                        }
                        linearMuying.setVisibility(View.VISIBLE);
                        initMuYingDate();
                    } else {
                        Log.i("zjz", "goodsActivity解析失败");
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mRequestQueue.add(jr);
    }

    private void initMuYingDate() {
        for (int i = 0; i < muyingImgs.size(); i++) {
            switch (i) {
                case 0:
//                    loadPicture.initPicture(imgMuying1, muyingImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgMuying1, true, muyingImgs.get(i).getPicarr(), Integer.parseInt(muyingImgs.get(i).getGoods_id()));
                    imgMuying1.setOnClickListener(this);
                    break;
                case 1:
//                    loadPicture.initPicture(imgMuying2, muyingImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgMuying2, true, muyingImgs.get(i).getPicarr(), Integer.parseInt(muyingImgs.get(i).getGoods_id()));
                    imgMuying2.setOnClickListener(this);
                    break;
                case 2:
//                    loadPicture.initPicture(imgMuying3, muyingImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgMuying3, true, muyingImgs.get(i).getPicarr(), Integer.parseInt(muyingImgs.get(i).getGoods_id()));
                    imgMuying3.setOnClickListener(this);
                    break;
                case 3:
//                    loadPicture.initPicture(imgMuying4, muyingImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgMuying4, true, muyingImgs.get(i).getPicarr(), Integer.parseInt(muyingImgs.get(i).getGoods_id()));
                    imgMuying4.setOnClickListener(this);
                    break;
                case 4:
//                    loadPicture.initPicture(imgMuying5, muyingImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgMuying5, true, muyingImgs.get(i).getPicarr(), Integer.parseInt(muyingImgs.get(i).getGoods_id()));
                    imgMuying5.setOnClickListener(this);
                    break;
                case 5:
//                    loadPicture.initPicture(imgMuying6, muyingImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgMuying6, true, muyingImgs.get(i).getPicarr(), Integer.parseInt(muyingImgs.get(i).getGoods_id()));
                    imgMuying6.setOnClickListener(this);
                    break;
                case 6:
//                    loadPicture.initPicture(imgMuying7, muyingImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgMuying7, true, muyingImgs.get(i).getPicarr(), Integer.parseInt(muyingImgs.get(i).getGoods_id()));
                    imgMuying7.setOnClickListener(this);
                    break;
                case 7:
//                    loadPicture.initPicture(imgMuying8, muyingImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgMuying8, true, muyingImgs.get(i).getPicarr(), Integer.parseInt(muyingImgs.get(i).getGoods_id()));
                    imgMuying8.setOnClickListener(this);
                    break;
            }
        }
    }

    private void initLinearFoods(String gcid) {
        foodsImgs = new ArrayList<Goods>();
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_home_all_goods + "&key=4&page=6&curpage=1&gc_id=" + gcid, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        foodsImgs.clear();
                        Log.i("zjz", "group:连接成功");
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("goods_list");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            Goods g = new Goods();
                            g.setPicarr(object1.optString("goods_image_url"));
                            g.setGoods_id(object1.optString("goods_id"));
                            foodsImgs.add(g);
                            if (haiwaigouDAO.selectByAllFood().size() < 6) {
                                haiwaigouDAO.insertFood(g);
                            } else {
                                haiwaigouDAO.updateFood(g);
                            }

                        }
                        linearFoods.setVisibility(View.VISIBLE);
                        initFoodsDate();
                    } else {
                        Log.i("zjz", "goodsActivity解析失败");
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueue.add(jr);
    }

    private void initFoodsDate() {
        for (int i = 0; i < foodsImgs.size(); i++) {
            switch (i) {
                case 0:
//                    loadPicture.initPicture(imgFood1, foodsImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgFood1, true, foodsImgs.get(i).getPicarr(), Integer.parseInt(foodsImgs.get(i).getGoods_id()));
                    imgFood1.setOnClickListener(this);
                    break;
                case 1:
                    imgFood2.setOnClickListener(this);
//                    loadPicture.initPicture(imgFood2, foodsImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgFood2, true, foodsImgs.get(i).getPicarr(), Integer.parseInt(foodsImgs.get(i).getGoods_id()));
                    break;
                case 2:
                    imgFood3.setOnClickListener(this);
//                    loadPicture.initPicture(imgFood3, foodsImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgFood3, true, foodsImgs.get(i).getPicarr(), Integer.parseInt(foodsImgs.get(i).getGoods_id()));
                    break;
                case 3:
                    imgFood4.setOnClickListener(this);
//                    loadPicture.initPicture(imgFood4, foodsImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgFood4, true, foodsImgs.get(i).getPicarr(), Integer.parseInt(foodsImgs.get(i).getGoods_id()));
                    break;
                case 4:
                    imgFood5.setOnClickListener(this);
//                    loadPicture.initPicture(imgFood5, foodsImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgFood5, true, foodsImgs.get(i).getPicarr(), Integer.parseInt(foodsImgs.get(i).getGoods_id()));
                    break;
                case 5:
                    imgFood6.setOnClickListener(this);
//                    loadPicture.initPicture(imgFood6, foodsImgs.get(i).getPicarr());
                    loadPicture.initPicture(imgFood6, true, foodsImgs.get(i).getPicarr(), Integer.parseInt(foodsImgs.get(i).getGoods_id()));
                    break;
            }
        }
    }

    private void initLinearGeren(final String gcid) {
        HttpRequest.sendGet(TLUrl.getInstance().URL_ISHULI, null, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("msg") == 1) {
                                if (object.optInt("status") == 0) {
                                    gerenImgs = new ArrayList<Goods>();
                                    JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_home_all_goods + "&key=4&page=8&curpage=1&gc_id=" + gcid, null, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                if (response.getInt("code") == 200) {
                                                    gerenImgs.clear();
                                                    Log.i("zjz", "group:连接成功");
                                                    JSONObject jsonObject = response.getJSONObject("datas");
                                                    JSONArray jsonArray = jsonObject.getJSONArray("goods_list");

                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject object1 = jsonArray.getJSONObject(i);
                                                        Goods g = new Goods();
                                                        g.setGoods_id(object1.optString("goods_id"));
                                                        g.setPicarr(object1.optString("goods_image_url"));
                                                        gerenImgs.add(g);
                                                    }
                                                    linearGerenhuli.setVisibility(View.VISIBLE);
                                                    initGeRenDate();
                                                } else {
                                                    Log.i("zjz", "goodsActivity解析失败");
                                                }

                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch block
                                                Log.i("zjz", e.toString());
                                                e.printStackTrace();

                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    });
                                    mRequestQueue.add(jr);
                                } else {
                                    linearGerenhuli.setVisibility(View.GONE);
                                }
                            } else {
                                Log.i("zjz", "解析失败");
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

    private void initGeRenDate() {
        for (int i = 0; i < gerenImgs.size(); i++) {
            switch (i) {
                case 0:
                    loadPicture.initPicture(imgGeren1, gerenImgs.get(i).getPicarr());
                    imgGeren1.setOnClickListener(this);
                    break;
                case 1:
                    loadPicture.initPicture(imgGeren2, gerenImgs.get(i).getPicarr());
                    imgGeren2.setOnClickListener(this);
                    break;
                case 2:
                    loadPicture.initPicture(imgGeren3, gerenImgs.get(i).getPicarr());
                    imgGeren3.setOnClickListener(this);
                    break;
                case 3:
                    loadPicture.initPicture(imgGeren4, gerenImgs.get(i).getPicarr());
                    imgGeren4.setOnClickListener(this);
                    break;
                case 4:
                    loadPicture.initPicture(imgGeren5, gerenImgs.get(i).getPicarr());
                    imgGeren5.setOnClickListener(this);
                    break;
                case 5:
                    loadPicture.initPicture(imgGeren6, gerenImgs.get(i).getPicarr());
                    imgGeren6.setOnClickListener(this);
                    break;
                case 6:
                    loadPicture.initPicture(imgGeren7, gerenImgs.get(i).getPicarr());
                    imgGeren7.setOnClickListener(this);
                    break;
                case 7:
                    loadPicture.initPicture(imgGeren8, gerenImgs.get(i).getPicarr());
                    imgGeren8.setOnClickListener(this);
                    break;
            }
        }
    }

    private void initLinearQiangGou() {
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_good_qianggou, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "qianggou:连接成功");
                        qiangImgs.clear();
                        JSONArray jsonArray = response.getJSONArray("datas");
//                        JSONArray jsonArray = jsonObject.getJSONArray("store_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            Goods g = new Goods();
                            g.setGoods_id(object1.optString("goods_id"));
                            g.setPicarr(TLUrl.getInstance().URL_hwg_qianggou+object1.optString("store_id")+"/"+object1.optString("groupbuy_image"));
//                            if (object1.optString("groupbuy_image").indexOf("6_") == 0) {
//                                g.setPicarr(TLUrl.getInstance().URL_hwg_qianggou + "6/" + object1.optString("groupbuy_image"));
//                            } else if (object1.optString("groupbuy_image").indexOf("4_") == 0) {
//                                g.setPicarr(TLUrl.getInstance().URL_hwg_qianggou + "4/" + object1.optString("groupbuy_image"));
//                            }
                            g.setQ_end_time(object1.optLong("end_time"));
                            g.setTime(object1.optLong("count_down"));
                            g.setPromote_money(object1.optDouble("groupbuy_price"));
                            if (qiangImgs.size() < 4) {
                                qiangImgs.add(g);
                            }
                        }
                        linearQianggou.setVisibility(View.VISIBLE);
                        initQiangGouDate();
                    } else {
                        Log.i("zjz", "storelist:解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueue.add(jr);
    }

    private void initQiangGouDate() {
        times = new TextView[4];
        times[0] = countdownDay;
        times[1] = countdownHour;
        times[2] = countdownMinute;
        times[3] = countdownSecond;
        for (int i = 0; i < qiangImgs.size(); i++) {
            switch (i) {
                case 0:
                    if (isFirst) {
                        mCountDownTimer countDownTimer = new mCountDownTimer(qiangImgs.get(i).getTime() * 1000, 1000, times);
                        countDownTimer.start();
                    }
                    loadPicture.initPicture(imgQiang1, qiangImgs.get(i).getPicarr());
                    imgQiang1.setOnClickListener(this);
                    break;
                case 1:
                    loadPicture.initPicture(imgQiang2, qiangImgs.get(i).getPicarr());
                    imgQiang2.setOnClickListener(this);
                    break;
                case 2:
                    loadPicture.initPicture(imgQiang3, qiangImgs.get(i).getPicarr());
                    imgQiang3.setOnClickListener(this);
                    break;
                case 3:
                    loadPicture.initPicture(imgQiang4, qiangImgs.get(i).getPicarr());
                    imgQiang4.setOnClickListener(this);
                    break;
            }
        }
    }

    private void initScrollView() {
        mScrollView.initWithContext(getContext());
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(false);
        mScrollView.setAutoLoadEnable(false);
        mScrollView.setRefreshTime(Util.getNowTime());

        mScrollView.setIXScrollViewListener(new XScrollView.IXScrollViewListener() {

            @Override
            public void onRefresh() {
                isRefresh = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onLoad();
                    }
                }, 2000);
                Log.i("zjz", "下拉刷新！！");
            }

            @Override
            public void onLoadMore() {

            }
        });
    }

    private void onLoad() {
        isFirst = false;
        if (NetworkUtils.isNetAvailable(getContext())) {
            Log.i("zjz", "网络连接正常");
            initBanner();
            initStoreGridView();
            initYunGouDate();
            initBannerAd();
            initLinearQiangGou();
            initGoodsGCid();
            initReMai();
            linearNoNetwork.setVisibility(View.GONE);
        } else {
            linearNoNetwork.setVisibility(View.VISIBLE);
            initLocalBanner();
            initLocalBannerAd();
            initLocalFood();
            initLocalMuYing();
            initLocalCainiLike();

        }
        mScrollView.stopRefresh();
        mScrollView.stopLoadMore();
    }


    private void initBanner() {

        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_home, null, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                goodsImgs.clear();
                                Log.i("zjz", "banner:连接成功");

                                JSONArray array = object.getJSONArray("datas");
                                JSONObject object1 = array.getJSONObject(0);
                                JSONObject object2 = object1.getJSONObject("adv_list");
                                JSONArray jsonArray = object2.getJSONArray("item");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object3 = jsonArray.getJSONObject(i);
                                    Goods g = new Goods();
                                    g.setPicarr(object3.optString("image"));
                                    g.setGoods_id(i + "");
                                    goodsImgs.add(g);
                                    if (haiwaigouDAO.selectByAllBanner().size() < 3) {
                                        haiwaigouDAO.insertBanner(g);
                                        Log.i("zjz", "增加" + i);
                                    } else {
                                        haiwaigouDAO.updateBanner(g);
                                        Log.i("zjz", "修改" + i);
                                    }

                                }
                                initBannerViewPager(viewPagerBaner, circleIndicator);
                                if (!isRefresh) {
                                    handler.post(runnable);
                                }
                            } else {
                                Log.i("zjz", "banner:解析失败");
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

    /**
     * 初始化banner
     */
    private void initBannerViewPager(ViewPager viewpager, CircleIndicator circleIndicator) {
        list.clear();
        for (int i = 0; i < goodsImgs.size(); i++) {
            ImageView view = new ImageView(getContext());
            view.setScaleType(ImageView.ScaleType.FIT_XY);
//            Util.setImage(goodsImgs.get(i).getPicarr(), view, handler);
            LoadPicture loadPicture = new LoadPicture();
//            loadPicture.initPicture(view, goodsImgs.get(i).getPicarr());
            loadPicture.initPicture(view, true, goodsImgs.get(i).getPicarr(), i);
            list.add(view);
            final int m = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                }
            });
        }
        viewpager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
//        currentposition = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % list.size();
        viewpager.setCurrentItem(0);
        circleIndicator.setViewPager(viewpager, list.size());

    }

    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(!isDestory) {
                if (viewPagerBaner.getCurrentItem() >= list.size() - 1) {
                    viewPagerBaner.setCurrentItem(0);
                } else {
                    viewPagerBaner.setCurrentItem(viewPagerBaner.getCurrentItem() + 1);
                }
//            viewPagerBaner.setCurrentItem(viewPagerBaner.getCurrentItem()+1);
//            currentposition++;

                handler.postDelayed(runnable, 5000);
            }

        }
    };

    /**
     * 初始化店铺主gridview
     */
    private void initStoreGridView() {
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_store_list + "&sc_id=" + 0, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "storelist:连接成功");
                        storeId.clear();
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("store_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            Store s = new Store();
                            s.setId(object1.optString("store_id"));
                            s.setName(object1.optString("store_name"));
                            s.setAddress(object1.optString("store_address"));
                            s.setAreainfo(object1.optString("store_area_info"));
                            if (storeId.size() < 8) {
                                storeId.add(s.getId());
                            }
                        }
                        initAvatarDate();
                    } else {
                        Log.i("zjz", "storelist:解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueue.add(jr);

    }

    private void initAvatarDate() {
        Log.i("zjz", "store_list=" + storeId.size());
//        for (int j=0;j<storeId.size();j++){
//            final int finalJ = j;
//            JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_store_detail + "&store_id="+storeId.get(j), null, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    try {
//                        if (response.getInt("code") == 200) {
//                            Log.i("zjz", "storedetail:连接成功");
//                            JSONObject jsonObject = response.getJSONObject("datas");
//                            JSONObject object = jsonObject.getJSONObject("store_info");
//                            Store s = new Store();
//                            s.setId(object.optString("store_id"));
//                            s.setName(object.optString("store_name"));
//                            s.setAddress(object.optString("store_address"));
//                            s.setAreainfo(object.optString("area_info"));
//                            s.setAvatar(object.optString("store_avatar"));
////                            Log.i("zjz", "url=" + TLUrl.getInstance().URL_hwg_store_head + s.getAvatar());
////                            Log.i("zjz", "NAME=" + s.getName());
//                            storeList.add(0,s);
//                            if(finalJ==storeId.size()-1){
////                                myGridAdapter=new MyGridAdapter(getActivity(),storeList);
////                                storeGridView.setAdapter(myGridAdapter);
////                                myGridAdapter.notifyDataSetChanged();
//                                initAvatar();
//                            }
//                        } else {
//                            Log.i("zjz", "storedetail:解析失败");
//                        }
//                    } catch (JSONException e) {
//                        // TODO Auto-generated catch block
//                        Log.i("zjz", e.toString());
//                        e.printStackTrace();
//
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.i("zjz", error.getMessage());
//                }
//            });
//            mRequestQueue.add(jr);
//        }

        storeGridView.setAdapter(new CommonAdapter<String>(getContext(), storeId, R.layout.fragment_shopping_store_item) {
            @Override
            public void convert(final HqbViewHolder helper, String item, final int position) {
                if (position == 7) {
                    helper.setImageResource(R.id.img_icon, R.drawable.img_sandian);
                    helper.setText(R.id.tv_name, "更多");
                } else {
                    JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_store_detail + "&store_id=" + storeId.get(position), null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getInt("code") == 200) {
                                    Log.i("zjz", "storedetail:连接成功");
                                    JSONObject jsonObject = response.getJSONObject("datas");
                                    JSONObject object = jsonObject.getJSONObject("store_info");
                                    Store s = new Store();
                                    s.setId(object.optString("store_id"));
                                    s.setName(object.optString("store_name"));
                                    s.setAddress(object.optString("store_address"));
                                    s.setAreainfo(object.optString("area_info"));
                                    s.setAvatar(object.optString("store_avatar"));
                                    helper.setImageByUrl(R.id.img_icon, TLUrl.getInstance().URL_hwg_store_head + s.getAvatar(),0);
                                    helper.setText(R.id.tv_name, s.getName());
                                } else {
                                    Log.i("zjz", "storedetail:解析失败");
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                Log.i("zjz", e.toString());
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
                    mRequestQueue.add(jr);
//                    Log.i(TAG, item.toString());
                }

            }
        });
        storeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (position == 7) {
                    intent = new Intent(getActivity(), AllStoreActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), StoreActivity.class);
                    intent.putExtra("store_id", storeId.get(position));
                    getActivity().startActivity(intent);
                }

            }
        });
    }

    private void initAvatar() {
        storeGridView.setAdapter(new CommonAdapter<Store>(getContext(), storeList, R.layout.fragment_shopping_store_item) {
            @Override
            public void convert(final HqbViewHolder helper, Store item, final int position) {
                if (position == 7) {
                    helper.setImageMoreResource(R.id.img_icon, R.drawable.img_storegengduo);
                    helper.setText(R.id.tv_name, "更多");
                } else {
                    helper.setImageByUrl(R.id.img_icon, TLUrl.getInstance().URL_hwg_store_head + storeList.get(position).getAvatar(),0);
                    helper.setText(R.id.tv_name, storeList.get(position).getName());
                }

            }
        });
        storeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 7) {

                } else {

                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        ButterKnife.reset(this);
    }

    @Override
    public void onItemRootViewClick(int position) {
        Intent intent = new Intent(getContext(), GoodsDetailActivity2.class);
        intent.putExtra("sid", hwgFragmentAdapter.getDatas().get(position).getGoods_id());
        Log.i("zjz", "sid=" + hwgFragmentAdapter.getDatas().get(position).getGoods_id());
        intent.putExtra("pic", hwgFragmentAdapter.getDatas().get(position).getPicarr());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.relative_search:
                intent = new Intent(getActivity(), AllGoodsActivity.class);
                intent.putExtra("search", true);
                getActivity().startActivity(intent);
                break;
            case R.id.img_food1:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", foodsImgs.get(0).getGoods_id());
                intent.putExtra("pic", foodsImgs.get(0).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_food2:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", foodsImgs.get(1).getGoods_id());
                intent.putExtra("pic", foodsImgs.get(1).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_food3:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", foodsImgs.get(2).getGoods_id());
                intent.putExtra("pic", foodsImgs.get(2).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_food4:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", foodsImgs.get(3).getGoods_id());
                intent.putExtra("pic", foodsImgs.get(3).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_food5:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", foodsImgs.get(4).getGoods_id());
                intent.putExtra("pic", foodsImgs.get(4).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_food6:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", foodsImgs.get(5).getGoods_id());
                intent.putExtra("pic", foodsImgs.get(5).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_geren1:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", gerenImgs.get(0).getGoods_id());
                intent.putExtra("pic", gerenImgs.get(0).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_geren2:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", gerenImgs.get(1).getGoods_id());
                intent.putExtra("pic", gerenImgs.get(1).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_geren3:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", gerenImgs.get(2).getGoods_id());
                intent.putExtra("pic", gerenImgs.get(2).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_geren4:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", gerenImgs.get(3).getGoods_id());
                intent.putExtra("pic", gerenImgs.get(3).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_geren5:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", gerenImgs.get(4).getGoods_id());
                intent.putExtra("pic", gerenImgs.get(4).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_geren6:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", gerenImgs.get(5).getGoods_id());
                intent.putExtra("pic", gerenImgs.get(5).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_geren7:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", gerenImgs.get(6).getGoods_id());
                intent.putExtra("pic", gerenImgs.get(6).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_geren8:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", gerenImgs.get(7).getGoods_id());
                intent.putExtra("pic", gerenImgs.get(7).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_muying1:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", muyingImgs.get(0).getGoods_id());
                intent.putExtra("pic", muyingImgs.get(0).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_muying2:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", muyingImgs.get(1).getGoods_id());
                intent.putExtra("pic", muyingImgs.get(1).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_muying3:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", muyingImgs.get(2).getGoods_id());
                intent.putExtra("pic", muyingImgs.get(2).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_muying4:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", muyingImgs.get(3).getGoods_id());
                intent.putExtra("pic", muyingImgs.get(3).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_muying5:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", muyingImgs.get(4).getGoods_id());
                intent.putExtra("pic", muyingImgs.get(4).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_muying6:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", muyingImgs.get(5).getGoods_id());
                intent.putExtra("pic", muyingImgs.get(5).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_muying7:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", muyingImgs.get(6).getGoods_id());
                intent.putExtra("pic", muyingImgs.get(6).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.img_muying8:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", muyingImgs.get(7).getGoods_id());
                intent.putExtra("pic", muyingImgs.get(7).getPicarr());
                getActivity().startActivity(intent);
                break;
            case R.id.rl_more_geren:
                intent = new Intent(getActivity(), AllGoodsActivity.class);
                intent.putExtra("gc_id", geren_gcid);
                getActivity().startActivity(intent);
                break;
            case R.id.rl_more_foods:
                intent = new Intent(getActivity(), AllGoodsActivity.class);
                intent.putExtra("gc_id", food_gcid);
                getActivity().startActivity(intent);
                break;
            case R.id.rl_more_muying:
                intent = new Intent(getActivity(), AllGoodsActivity.class);
                intent.putExtra("gc_id", muying_gcid);
                getActivity().startActivity(intent);
                break;
            case R.id.img_qiang1:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", qiangImgs.get(0).getGoods_id());
                intent.putExtra("pic", qiangImgs.get(0).getPicarr());
                intent.putExtra("end_time", qiangImgs.get(0).getQ_end_time());
                intent.putExtra("promote_money", qiangImgs.get(0).getPromote_money());
                intent.putExtra("qiang", true);
                getActivity().startActivity(intent);
                break;
            case R.id.img_qiang2:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", qiangImgs.get(1).getGoods_id());
                intent.putExtra("pic", qiangImgs.get(1).getPicarr());
                intent.putExtra("end_time", qiangImgs.get(1).getQ_end_time());
                intent.putExtra("promote_money", qiangImgs.get(1).getPromote_money());
                intent.putExtra("qiang", true);
                getActivity().startActivity(intent);
                break;
            case R.id.img_qiang3:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", qiangImgs.get(2).getGoods_id());
                intent.putExtra("pic", qiangImgs.get(2).getPicarr());
                intent.putExtra("end_time", qiangImgs.get(2).getQ_end_time());
                intent.putExtra("promote_money", qiangImgs.get(2).getPromote_money());
                intent.putExtra("qiang", true);
                getActivity().startActivity(intent);
                break;
            case R.id.img_qiang4:
                intent = new Intent(getActivity(), GoodsDetailActivity2.class);
                intent.putExtra("sid", qiangImgs.get(3).getGoods_id());
                intent.putExtra("pic", qiangImgs.get(3).getPicarr());
                intent.putExtra("end_time", qiangImgs.get(3).getQ_end_time());
                intent.putExtra("promote_money", qiangImgs.get(3).getPromote_money());
                intent.putExtra("qiang", true);
                getActivity().startActivity(intent);
                break;
            case R.id.img_ad1:
                intent = new Intent(getActivity(), AllGoodsActivity.class);
                intent.putExtra("keyword", adImgs.get(0).getKeywords());
                intent.putExtra("ad", true);
                getActivity().startActivity(intent);
                break;
            case R.id.img_ad2:
                intent = new Intent(getActivity(), AllGoodsActivity.class);
                intent.putExtra("keyword", adImgs.get(1).getKeywords());
                intent.putExtra("ad", true);
                getActivity().startActivity(intent);
                break;
            case R.id.img_ad3:
                intent = new Intent(getActivity(), AllGoodsActivity.class);
                intent.putExtra("keyword", adImgs.get(2).getKeywords());
                intent.putExtra("ad", true);
                getActivity().startActivity(intent);
                break;
            case R.id.img_ad4:
                intent = new Intent(getActivity(), AllGoodsActivity.class);
                intent.putExtra("keyword", adImgs.get(3).getKeywords());
                intent.putExtra("ad", true);
                getActivity().startActivity(intent);
                break;
            case R.id.btn_reload:
                if (NetworkUtils.isNetAvailable(getContext())) {
                    Log.i("zjz", "网络连接正常");
                    initBanner();
                    initStoreGridView();
                    initYunGouDate();
                    initBannerAd();
                    initLinearQiangGou();
                    initGoodsGCid();
                    initReMai();
                    linearNoNetwork.setVisibility(View.GONE);
                } else {
                    linearNoNetwork.setVisibility(View.VISIBLE);
                    initLocalBanner();
                    initLocalBannerAd();
                    initLocalFood();
                    initLocalMuYing();
                    initLocalCainiLike();

                }
                break;
        }
    }


    private BroadcastReceiver xiaoxiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            int count = intent.getIntExtra("count", 0);

            storeTishixiaoxi.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
            if (count > 99) {
                storeTishixiaoxi.setText("99+");
            } else {
                storeTishixiaoxi.setText(count + "");
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestory=true;
        getActivity().unregisterReceiver(xiaoxiReceiver);
    }
}
