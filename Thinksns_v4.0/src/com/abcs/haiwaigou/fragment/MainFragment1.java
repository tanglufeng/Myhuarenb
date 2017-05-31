package com.abcs.haiwaigou.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.AllGoodsActivity;
import com.abcs.haiwaigou.activity.CompanyConnectActivity;
import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.activity.HWGGoodsFenLeiActivity;
import com.abcs.haiwaigou.activity.HotActivity;
import com.abcs.haiwaigou.activity.HotSpecialActivity;
import com.abcs.haiwaigou.activity.LinkActivity;
import com.abcs.haiwaigou.activity.RechargeActivity;
import com.abcs.haiwaigou.activity.RedBagActivity;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.haiwaigou.view.CircleIndicator;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.haiwaigou.view.zjzbanner.LMBanners;
import com.abcs.haiwaigou.view.zjzbanner.adapter.LBaseAdapter;
import com.abcs.haiwaigou.view.zjzbanner.transformer.TransitionEffect;
import com.abcs.haiwaigou.yyg.activity.YYGActivity;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.adapter.CommonAdapter;
import com.abcs.huaqiaobang.adapter.GalleryAdapter;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.ShowMessageDialog;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.presenter.LoadInterface;
import com.abcs.huaqiaobang.presenter.MainGoods;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.HqbViewHolder;
import com.abcs.huaqiaobang.view.MainScrollView;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zjz on 2016/6/8 0008.
 * 海外购的首页fragment
 */
public class MainFragment1 extends BaseFragment implements LoadInterface, View.OnClickListener {

    public static final String FLAG_BANNER = "1";//广告banner
    public static final String FLAG_UPDATEWEEK = "2";//每周上新
    public static final String FLAG_RECOMMEND = "3";//热门推荐
    public static final String FLAG_BRAND = "4";//品牌推荐
    public static final String FLAG_HOTTODAY = "5";//今日最热
    public static final String FLAG_SALETODAY = "6";//今日特卖
    public static final String FLAG_ACTIVITY = "8";//活动
    public static final String TYPE_KEYWORD = "1";//商品关键字
    public static final String TYPE_SPECIAL = "2";//商品专题
    public static final String TYPE_LINK = "3";//链接
    public static final String TYPE_OTHERS = "4";//其他
    public static final String TYPE_GOODS = "5";//商品id

    Activity activity;
    @InjectView(R.id.viewPager_baner)
    ViewPager viewPagerBaner;
    @InjectView(R.id.circle_indicator)
    CircleIndicator circleIndicator;
    @InjectView(R.id.img_card)
    ImageView imgCard;
    @InjectView(R.id.t_card)
    TextView tCard;
    @InjectView(R.id.relative_card)
    RelativeLayout relativeCard;
    @InjectView(R.id.img_redbag)
    ImageView imgRedbag;
    @InjectView(R.id.t_redbag)
    TextView tRedbag;
    @InjectView(R.id.t_have_send)
    TextView tHaveSend;
    @InjectView(R.id.relative_redbag)
    RelativeLayout relativeRedbag;
    @InjectView(R.id.img_one)
    ImageView imgOne;
    @InjectView(R.id.t_one)
    TextView tOne;
    @InjectView(R.id.relative_one)
    RelativeLayout relativeOne;
    @InjectView(R.id.img_qiye)
    ImageView imgQiye;
    @InjectView(R.id.t_qiye)
    TextView tQiye;
    @InjectView(R.id.relaitve_qiye)
    RelativeLayout relaitveQiye;
    @InjectView(R.id.linear_weekupdate)
    LinearLayout linearWeekupdate;
    @InjectView(R.id.linear_recommend)
    LinearLayout linearRecommend;
    @InjectView(R.id.grid_brand)
    GridView gridBrand;
    @InjectView(R.id.hotrecyclerView)
    RecyclerView hotrecyclerView;
    @InjectView(R.id.linear_special)
    LinearLayout linearSpecial;
    @InjectView(R.id.scrollView)
    MainScrollView scrollView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.card_weekupdate)
    CardView cardWeekupdate;
    @InjectView(R.id.card_recommend)
    CardView cardRecommend;
    @InjectView(R.id.card_today_hot)
    CardView cardTodayHot;
    @InjectView(R.id.card_special)
    CardView cardSpecial;
    @InjectView(R.id.linear_act)
    LinearLayout linearAct;
    @InjectView(R.id.img_top)
    ImageView imgTop;
    @InjectView(R.id.banners)
    LMBanners banners;
    @InjectView(R.id.t_have_bind)
    TextView tHaveBind;
    private View view;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;

    private List<ImageView> list = new ArrayList<ImageView>();
    boolean isDestory;
    boolean isRefresh = false;
    private ArrayList<Goods> bannerList = new ArrayList<>();
    private ArrayList<Goods> weekList = new ArrayList<>();
    private ArrayList<Goods> recommendList = new ArrayList<>();
    private ArrayList<Goods> brandList = new ArrayList<>();
    private ArrayList<Goods> todayhotList = new ArrayList<>();
    private ArrayList<Goods> todaysaleList = new ArrayList<>();
    private ArrayList<Goods> activityList = new ArrayList<>();
    private List<String> bannerString = new ArrayList<>();
    private String plate_id;
    private String objectName;
    private Handler handler = new Handler();
    private List<Goods> mGoods;
    private MainGoods mainGoods;
    private GalleryAdapter galleryAdapter;
    private ACache aCache;

    int picWith;
    int picHeight;
    int picHeight2;

    MyBroadCastReceiver myBroadCastReceiver;

    public static MainFragment1 newInstance(String plateId, String objectName) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("plate_id", plateId);
        bundle.putSerializable("objectName", objectName);
        MainFragment1 fragment = new MainFragment1();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        activity = getActivity();
        aCache = ACache.get(activity);
        if (view == null) {
            view = activity.getLayoutInflater().inflate(
                    R.layout.hwg_fragment_main1, null);
            ButterKnife.inject(this, view);

            Bundle bundle = getArguments();
            if (bundle != null) {
                plate_id = bundle.getString("plate_id");
                objectName = bundle.getString("objectName");
            }
            picWith = Util.WIDTH;
            picHeight = picWith * 33 / 72;
            picHeight2 = picWith * 39 / 72;
            isPrepared = true;
            lazyLoad();
        }
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null) {
            p.removeView(view);
        }
        myBroadCastReceiver = new MyBroadCastReceiver(activity, updateUI);
        myBroadCastReceiver.register();
        ButterKnife.inject(this, view);

        scrollView.setOnScroll(new MainScrollView.OnScroll() {
            @Override
            public void onScrollListener(int x, int y, int oldx, int oldy) {

//                LinearLayout linearRoot = (LinearLayout) scrollView.getChildAt(0);
//                LinearLayout linear_act = (LinearLayout) linearRoot.getChildAt(3);
//                linear_act.getChildCount();
//                CardView card_weekUpdate = (CardView) linearRoot.getChildAt(4);
//                LinearLayout linearWeekRoot = (LinearLayout) card_weekUpdate.getChildAt(0);
//                LinearLayout linear_week = (LinearLayout) linearWeekRoot.getChildAt(1);
//                linear_week.getChildCount();
//                CardView card_recommend = (CardView) linearRoot.getChildAt(5);
//                LinearLayout linearRecommendRoot = (LinearLayout) card_recommend.getChildAt(0);
//                LinearLayout linear_recommend = (LinearLayout) linearRecommendRoot.getChildAt(1);
//                linear_recommend.getChildCount();
//                CardView card_special = (CardView) linearRoot.getChildAt(8);
//                LinearLayout linearSpecial = (LinearLayout) card_special.getChildAt(0);
//                LinearLayout linear_special = (LinearLayout) linearSpecial.getChildAt(1);
//                linear_special.getChildCount();
//                Log.i("zjz", "main1_act=" + linear_act.getChildCount());
//                Log.i("zjz", "main1_week=" + linear_week.getChildCount());
//                Log.i("zjz", "main1_recommend=" + linear_recommend.getChildCount());
//                Log.i("zjz", "main1_special=" + linear_special.getChildCount());
//                Log.i("zjz", "main1_scroll_child_count=" + (linear_act.getChildCount() + linear_week.getChildCount() + linear_recommend.getChildCount() + linear_special.getChildCount()));
                if (oldy - y  < 0||y==0) {
                    //上滑
                    imgTop.setVisibility(View.INVISIBLE);
                } else {
                    imgTop.setVisibility(View.VISIBLE);
                }
            }
        });

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MyUpdateUI.sendUpdateCarNum(activity);
                isRefresh = true;
                mainGoods.loadData(isRefresh);
                initView(isRefresh);
                aCache.remove(objectName);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeRefreshLayout.setRefreshing(false);
//                        isRefresh = false;
//                    }
//                }, 2000);
            }
        });

        return view;
//        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
//        ViewGroup parent = (ViewGroup)mFragmentView.getParent();
//        if(parent != null) {
//            parent.removeView(mFragmentView);
//        }
//        return mFragmentView;
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
            if (intent.getStringExtra("type").equals(MyUpdateUI.CHANGEUSER) || intent.getStringExtra("type").equals(MyUpdateUI.BINDCOMPANY)) {
                Log.i("zjz", "MainFragment1更换用户");
                if (MyApplication.getInstance().getMykey() != null) {
                    initBindCompany();
                } else {
                    tHaveBind.setText("");
                }
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initBindCompany() {
        if (MyApplication.getInstance().getMykey() == null) {
            return;
        }
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_find_company_connect, "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            Log.i("zjz", "find_company_msg=" + msg);
                            if (object.getInt("code") == 200) {
                                JSONObject errObj = object.optJSONObject("datas");
                                if (errObj == null) {
                                    JSONArray array = object.optJSONArray("datas");
                                    if (array != null && array.length() != 0) {
                                        JSONObject jsonObject = array.getJSONObject(0);
                                        jsonObject.optString("id");
                                        if (tHaveBind != null)
                                            tHaveBind.setText(jsonObject.optString("enterprise_name"));
                                        jsonObject.optString("invitation_code");
                                        jsonObject.optString("member_id");
                                        jsonObject.optString("member_name");
                                    }
                                }
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

    private void initRedBagNum() {
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_head, "act=circle_index&op=count_red_envelope", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            Log.i("zjz", "send_redbag_msg=" + msg);
                            String s;
                            NumberFormat nf = NumberFormat.getNumberInstance();
                            nf.setMaximumFractionDigits(2);
                            s = nf.format(object.optJSONObject("datas").optDouble("count"));
                            if (tHaveSend != null)
                                tHaveSend.setText(s);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();

                        } finally {
                            if (swipeRefreshLayout != null)
                                swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });
            }
        });
    }

    private void initView(boolean isRefresh) {
        initRedBagNum();
        initBindCompany();
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hotrecyclerView.setLayoutManager(layoutManager);
        mGoods = new ArrayList<>();
        mainGoods = new MainGoods(this);
        mainGoods.loadData(isRefresh);

        JSONObject mainObject = aCache.getAsJSONObject(objectName);
        if (mainObject != null && !isRefresh) {
            try {
                Log.i("zjz", "有本地main");
                initMainView(mainObject);
                if (swipeRefreshLayout != null&& NetworkUtils.isNetAvailable(activity)) {
                    swipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(true);
                            initNewData();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            initNewData();
        }

    }

    private void initNewData() {
        Log.i("zjz", "刷新main1" + objectName);
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_type + "&plate_id=" + plate_id, null, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i("zjz", "main1_msg=" + msg);
                            JSONObject object = new JSONObject(msg);
                            aCache.remove(objectName);
                            if (aCache.getAsJSONObject(objectName) == null) {
                                aCache.put(objectName, object, 5*24 * 60 * 60);
                            }
                            initMainView(object);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();

                        } finally {
                            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing())
                                swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });

            }
        });
    }

    private void initMainView(JSONObject object) throws JSONException {
        if (object.optInt("code") == 200) {
            JSONObject data = object.optJSONObject("datas");
            JSONArray array = data.optJSONArray("many_list");
            bannerList.clear();
            weekList.clear();
            recommendList.clear();
            brandList.clear();
            todaysaleList.clear();
            todayhotList.clear();
            activityList.clear();
            bannerString.clear();
            for (int i = 0; i < array.length(); i++) {
                JSONObject mObject = array.getJSONObject(i);
                Goods goods = new Goods();
                goods.setType(mObject.optString("type"));
                goods.setPicarr(TLUrl.getInstance().URL_hwg_pic_head + mObject.optString("image"));
                if (!mObject.optString("many_image").equals("")) {
                    goods.setGoods_url(TLUrl.getInstance().URL_hwg_pic_head + mObject.optString("many_image"));
                } else {
                    goods.setGoods_url(TLUrl.getInstance().URL_hwg_pic_head + mObject.optString("image"));
                }
                goods.setManyId(mObject.optString("many_id"));
                goods.setOtype(mObject.optString("other"));
                goods.setKeywords(mObject.optString("depict"));
                goods.setSubhead(mObject.optString("desc_ribe"));
                goods.setTitle(mObject.optString("title"));
                goods.setContent(mObject.optString("advert_goods"));

                if (mObject.optString("flag").equals(FLAG_BANNER)) {
                    bannerList.add(goods);
                    bannerString.add(TLUrl.getInstance().URL_hwg_pic_head + mObject.optString("image"));
                } else if (mObject.optString("flag").equals(FLAG_UPDATEWEEK)) {
                    weekList.add(goods);
                } else if (mObject.optString("flag").equals(FLAG_RECOMMEND)) {
                    recommendList.add(goods);
                } else if (mObject.optString("flag").equals(FLAG_BRAND)) {
                    brandList.add(goods);
                } else if (mObject.optString("flag").equals(FLAG_HOTTODAY)) {
                    todayhotList.add(goods);
                } else if (mObject.optString("flag").equals(FLAG_SALETODAY)) {
                    todaysaleList.add(goods);
                } else if (mObject.optString("flag").equals(FLAG_ACTIVITY)) {
                    activityList.add(goods);
                }
            }
            mHasLoadedOnce = true;
//            initBannerViewPager(viewPagerBaner, circleIndicator);
            initBanners();
            initActivity();
            initRecommend();
            initTodaySale();
            initBrand();
            initWeekUpdate();
        }
    }

    private void initBanners() {
        //设置Banners高度
        banners.setLayoutParams(new LinearLayout.LayoutParams(picWith, picHeight));
        //本地用法
        banners.setAdapter(new UrlImgAdapter(activity), bannerString);
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
        if (bannerString.size() == 1) {

            banners.hideIndicatorLayout();//隐藏原点
        } else {

            banners.showIndicatorLayout();//显示原点
        }
        banners.setIndicatorPosition(LMBanners.IndicaTorPosition.BOTTOM_MID);//设置原点显示位置

    }

    class UrlImgAdapter implements LBaseAdapter<String> {
        private Context mContext;

        public UrlImgAdapter(Context context) {
            mContext = context;
        }

        @Override
        public View getView(final LMBanners lBanners, final Context context, final int position, String data) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.banner_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.id_image);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageLoader.getInstance().displayImage(data, imageView, Options.getHDOptions());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (bannerList.get(position).getType().equals(TYPE_SPECIAL)) {
                        intent = new Intent(activity, HotSpecialActivity.class);
                        intent.putExtra("words", "");
                        intent.putExtra("text_position", 5);
                        intent.putExtra("picture", bannerList.get(position).getGoods_url());
                        intent.putExtra("title", bannerList.get(position).getTitle());
                        intent.putExtra("isMain", false);
                        intent.putExtra("special_id", bannerList.get(position).getKeywords());
                    } else if (bannerList.get(position).getType().equals(TYPE_KEYWORD)) {
                        intent = new Intent(activity, HotActivity.class);
                        intent.putExtra("words", "");
                        intent.putExtra("keyword", bannerList.get(position).getKeywords());
                        intent.putExtra("title", bannerList.get(position).getTitle());
                        intent.putExtra("text_position", 5);
                        intent.putExtra("isMain", false);
                        intent.putExtra("picture", bannerList.get(position).getGoods_url());
                    } else if (bannerList.get(position).getType().equals(TYPE_LINK)) {
                        intent = new Intent(activity, LinkActivity.class);
                        intent.putExtra("words", bannerList.get(position).getSubhead());
                        intent.putExtra("keyword", bannerList.get(position).getKeywords());
                        intent.putExtra("title", bannerList.get(position).getTitle());
                        intent.putExtra("picture", bannerList.get(position).getGoods_url());
                        String[] strings = bannerList.get(position).getContent().split(",");
                        intent.putExtra("goods_id", strings[1]);
                        intent.putExtra("goods_img", strings[0]);
                    } else if (bannerList.get(position).getType().equals(TYPE_GOODS)) {
                        intent = new Intent(activity, GoodsDetailActivity.class);
                        intent.putExtra("sid", bannerList.get(position).getKeywords());
                        intent.putExtra("pic", bannerList.get(position).getGoods_url());
                    }
                    activity.startActivity(intent);
                }
            });
            return view;
        }

    }

    private void initActivity() {
        linearAct.removeAllViews();
        Log.i("zjz", "activity_size=" + activityList.size());
        if (activityList.size() == 0) {
            linearAct.setVisibility(View.GONE);
            return;
        } else {
            linearAct.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < activityList.size(); i++) {
            View view = activity.getLayoutInflater().inflate(R.layout.hwg_weekupdate_view_item, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(picWith, picHeight);
            if (i != activityList.size() - 1) {
                layoutParams.setMargins(0, 0, 0, Util.dip2px(activity, 10));

            }
            view.setLayoutParams(layoutParams);

            ImageView img_item = (ImageView) view.findViewById(R.id.img_item);
            ImageLoader.getInstance().displayImage(activityList.get(i).getPicarr(), img_item, Options.getHDOptions());
//            LoadPicture loadPicture = new LoadPicture();
//            loadPicture.initPicture(img_item, adImgs.get(i).getPicarr());
            final int finalI = i;

            img_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    switch (activityList.get(finalI).getType()){
                        case TYPE_SPECIAL:

                            Log.e("zdsww",activityList.get(finalI).getOtype()+"");

                            if(activityList.get(finalI).getOtype().equals("1")){
                                intent = new Intent(activity, HWGGoodsFenLeiActivity.class);
                                intent.putExtra("special_id", activityList.get(finalI).getManyId());
                            }else {
                                intent = new Intent(activity, HotSpecialActivity.class);
                                intent.putExtra("special_id", activityList.get(finalI).getKeywords());
                            }

                            intent.putExtra("words", activityList.get(finalI).getSubhead());
                            intent.putExtra("text_position", finalI);
                            intent.putExtra("isWeek", true);
                            intent.putExtra("picture", activityList.get(finalI).getGoods_url());
                            intent.putExtra("title", activityList.get(finalI).getTitle());
                            activity.startActivity(intent);
                            break;
                        case TYPE_KEYWORD:
                            intent = new Intent(activity, HotActivity.class);
                            intent.putExtra("words", activityList.get(finalI).getSubhead());
                            intent.putExtra("keyword", activityList.get(finalI).getKeywords());
                            intent.putExtra("isWeek", true);
                            intent.putExtra("text_position", finalI);
                            intent.putExtra("title", activityList.get(finalI).getTitle());
                            intent.putExtra("picture", activityList.get(finalI).getGoods_url());
                            activity.startActivity(intent);
                            break;
                        case TYPE_GOODS:
                            intent = new Intent(activity, GoodsDetailActivity.class);
                            intent.putExtra("sid", activityList.get(finalI).getKeywords());
                            intent.putExtra("pic", activityList.get(finalI).getGoods_url());
                            activity.startActivity(intent);
                            break;
                        case TYPE_LINK:
                            intent = new Intent(activity, LinkActivity.class);
                            intent.putExtra("words", activityList.get(finalI).getSubhead());
                            intent.putExtra("keyword", activityList.get(finalI).getKeywords());
                            intent.putExtra("title", activityList.get(finalI).getTitle());
                            intent.putExtra("picture", activityList.get(finalI).getGoods_url());
                            activity.startActivity(intent);
                            break;
                        case TYPE_OTHERS:
                            if(MyApplication.getInstance().self!=null){
                                initVouncher();
                            }
                            break;
                        default:
                            showToast(activity,"敬请期待");
                            break;
                    }
                }
            });
            linearAct.addView(view);
        }
    }

    private void initVouncher() {
        ProgressDlgUtil.showProgressDlg("Loading...",activity);
        HttpRequest.sendPost(TLUrl.getInstance().URL_first_get_vouncher+MyApplication.getInstance().getMykey(),null , new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject mainObj=new JSONObject(msg);
                            if(mainObj.optString("code").equals("200")){
                                JSONArray array=mainObj.optJSONArray("datas");
                                if(array!=null){
                                    JSONObject object=array.getJSONObject(0);
                                    String title=object.optString("voucher_title");
                                    String desc=object.optString("voucher_desc");
                                    String limit=object.optString("voucher_limit");
                                    String prcie=object.optString("voucher_price");
                                    String endTime=Util.format1.format(object.optLong("voucher_end_date")*1000);
                                    String string="["+title+"]，"+"满"+limit+"减"+prcie+"，有效期至"+endTime;
                                    new ShowMessageDialog(view,activity,Util.WIDTH * 4 / 5,string,"优惠券领取成功");
                                }else {
                                    JSONObject error=mainObj.optJSONObject("datas");
                                    showToast(activity,error.optString("error"));
                                }
                            }else {
                                showToast(activity,"领取失败！");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });
            }
        });
    }

    private void initWeekUpdate() {
        linearWeekupdate.removeAllViews();
        if (weekList.size() == 0) {
            cardWeekupdate.setVisibility(View.GONE);
            return;
        } else {
            cardWeekupdate.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < weekList.size(); i++) {
            View view = activity.getLayoutInflater().inflate(R.layout.hwg_weekupdate_view_item, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(picWith, picHeight);
            if (i != weekList.size() - 1) {
                layoutParams.setMargins(0, 0, 0, Util.dip2px(activity, 10));
            }
            view.setLayoutParams(layoutParams);
            ImageView img_item = (ImageView) view.findViewById(R.id.img_item);
            ImageLoader.getInstance().displayImage(weekList.get(i).getPicarr(), img_item, Options.getHDOptions());
//            LoadPicture loadPicture = new LoadPicture();
//            loadPicture.initPicture(img_item, adImgs.get(i).getPicarr());
            final int finalI = i;
            img_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (weekList.get(finalI).getType().equals(TYPE_SPECIAL)) {

                        Log.e("zds_bbb",weekList.get(finalI).getSubhead()+"");
                        Log.e("zds_baio",weekList.get(finalI).getTitle()+"");

                        intent = new Intent(activity, HotSpecialActivity.class);
                        intent.putExtra("words", weekList.get(finalI).getSubhead());
                        intent.putExtra("text_position", finalI);
                        intent.putExtra("isWeek", true);
                        intent.putExtra("picture", weekList.get(finalI).getGoods_url());
                        intent.putExtra("title", weekList.get(finalI).getTitle());
                        intent.putExtra("special_id", weekList.get(finalI).getKeywords());
                    } else if (weekList.get(finalI).getType().equals(TYPE_KEYWORD)) {
                        intent = new Intent(activity, HotActivity.class);
                        intent.putExtra("words", weekList.get(finalI).getSubhead());
                        intent.putExtra("keyword", weekList.get(finalI).getKeywords());
                        intent.putExtra("isWeek", true);
                        intent.putExtra("text_position", finalI);
                        intent.putExtra("title", weekList.get(finalI).getTitle());
                        intent.putExtra("picture", weekList.get(finalI).getGoods_url());
                    } else if (weekList.get(finalI).getType().equals(TYPE_GOODS)) {
                        intent = new Intent(activity, GoodsDetailActivity.class);
                        intent.putExtra("sid", weekList.get(finalI).getKeywords());
                        intent.putExtra("pic", weekList.get(finalI).getGoods_url());
                    }

                    activity.startActivity(intent);
                }
            });
            linearWeekupdate.addView(view);
        }
    }

    private void initBrand() {
        gridBrand.setAdapter(new CommonAdapter<Goods>(activity, brandList, R.layout.hwg_item_brand) {
            @Override
            public void convert(HqbViewHolder helper, Goods item, int position) {
                helper.setImageByUrl(R.id.img_brand, item.getPicarr(),0);
            }
        });
        gridBrand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, AllGoodsActivity.class);
                intent.putExtra("search", true);
                intent.putExtra("search_keyword", brandList.get(position).getKeywords());
                activity.startActivity(intent);
            }
        });
    }

    private void initTodaySale() {
        linearSpecial.removeAllViews();
        if (todaysaleList.size() == 0) {
            cardSpecial.setVisibility(View.GONE);
            return;
        } else {
            cardSpecial.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < todaysaleList.size(); i++) {
            View view = activity.getLayoutInflater().inflate(R.layout.hwg_special_view_item, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(picWith, picHeight2);
            if (i != todaysaleList.size() - 1) {
                layoutParams.setMargins(0, 0, 0, Util.dip2px(activity, 10));
            }
            view.setLayoutParams(layoutParams);
            ImageView img_item = (ImageView) view.findViewById(R.id.img_item);
            ImageLoader.getInstance().displayImage(todaysaleList.get(i).getPicarr(), img_item, Options.getHDOptions());
//            LoadPicture loadPicture = new LoadPicture();
//            loadPicture.initPicture(img_item, adImgs.get(i).getPicarr());
            final int finalI = i;
            img_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (todaysaleList.get(finalI).getType().equals(TYPE_SPECIAL)) {
                        intent = new Intent(activity, HotSpecialActivity.class);
                        intent.putExtra("words", todaysaleList.get(finalI).getSubhead());
                        intent.putExtra("text_position", finalI);
                        intent.putExtra("isSale", true);
                        intent.putExtra("picture", todaysaleList.get(finalI).getGoods_url());
                        intent.putExtra("title", todaysaleList.get(finalI).getTitle());
                        intent.putExtra("special_id", todaysaleList.get(finalI).getKeywords());
                    } else if (todaysaleList.get(finalI).getType().equals(TYPE_KEYWORD)) {
                        intent = new Intent(activity, HotActivity.class);
                        intent.putExtra("words", todaysaleList.get(finalI).getSubhead());
                        intent.putExtra("keyword", todaysaleList.get(finalI).getKeywords());
                        intent.putExtra("isSale", true);
                        intent.putExtra("text_position", finalI);
                        intent.putExtra("title", todaysaleList.get(finalI).getTitle());
                        intent.putExtra("picture", todaysaleList.get(finalI).getGoods_url());
                    } else if (todaysaleList.get(finalI).getType().equals(TYPE_GOODS)) {
                        intent = new Intent(activity, GoodsDetailActivity.class);
                        intent.putExtra("sid", todaysaleList.get(finalI).getKeywords());
                        intent.putExtra("pic", todaysaleList.get(finalI).getGoods_url());
                    }

                    activity.startActivity(intent);
                }
            });
            linearSpecial.addView(view);
        }
    }

    private void initRecommend() {
        linearRecommend.removeAllViews();
        if (recommendList.size() == 0) {
            cardRecommend.setVisibility(View.GONE);
            return;
        } else {
            cardRecommend.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < recommendList.size(); i++) {
            View view = activity.getLayoutInflater().inflate(R.layout.hwg_weekupdate_view_item, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(picWith, picHeight);
            if (i != recommendList.size() - 1) {

                layoutParams.setMargins(0, 0, 0, Util.dip2px(activity, 10));
            }
            view.setLayoutParams(layoutParams);
            ImageView img_item = (ImageView) view.findViewById(R.id.img_item);
            ImageLoader.getInstance().displayImage(recommendList.get(i).getPicarr(), img_item, Options.getHDOptions());
//            LoadPicture loadPicture = new LoadPicture();
//            loadPicture.initPicture(img_item, adImgs.get(i).getPicarr());
            final int finalI = i;
            img_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (recommendList.get(finalI).getType().equals(TYPE_SPECIAL)) {
                        intent = new Intent(activity, HotSpecialActivity.class);
                        intent.putExtra("words", recommendList.get(finalI).getSubhead());
                        intent.putExtra("text_position", finalI);
                        intent.putExtra("picture", recommendList.get(finalI).getGoods_url());
                        intent.putExtra("title", recommendList.get(finalI).getTitle());
                        intent.putExtra("special_id", recommendList.get(finalI).getKeywords());
                    } else if (recommendList.get(finalI).getType().equals(TYPE_KEYWORD)) {
                        intent = new Intent(activity, HotActivity.class);
                        intent.putExtra("words", recommendList.get(finalI).getSubhead());
                        intent.putExtra("keyword", recommendList.get(finalI).getKeywords());
                        intent.putExtra("title", recommendList.get(finalI).getTitle());
                        intent.putExtra("text_position", finalI);
                        intent.putExtra("picture", recommendList.get(finalI).getGoods_url());
                    } else if (recommendList.get(finalI).getType().equals(TYPE_GOODS)) {
                        intent = new Intent(activity, GoodsDetailActivity.class);
                        intent.putExtra("sid", recommendList.get(finalI).getKeywords());
                        intent.putExtra("pic", recommendList.get(finalI).getGoods_url());
                    }

                    activity.startActivity(intent);
                }
            });
            linearRecommend.addView(view);
        }
    }


    private void initBannerViewPager(ViewPager viewpager, CircleIndicator circleIndicator) {
        list.clear();
        for (int i = 0; i < bannerList.size(); i++) {
            ImageView view = new ImageView(getContext());
//            view.setScaleType(ImageView.ScaleType.FIT_XY);
//            Util.setImage(goodsImgs.get(i).getPicarr(), view, handler);
//            LoadPicture loadPicture = new LoadPicture();
//            loadPicture.initPicture(view, goodsImgs.get(i).getPicarr());
//            loadPicture.initPicture(view, true, goodsImgs.get(i).getPicarr(), i);
            ImageLoader.getInstance().displayImage(bannerList.get(i).getPicarr(), view, Options.getHDOptions());
            list.add(view);
            Log.i("zjz", "list_size=" + list.size());
            final int m = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = null;
                    if (bannerList.get(m).getType().equals(TYPE_SPECIAL)) {
                        intent = new Intent(activity, HotSpecialActivity.class);
                        intent.putExtra("words", "");
                        intent.putExtra("text_position", 5);
                        intent.putExtra("picture", bannerList.get(m).getGoods_url());
                        intent.putExtra("title", bannerList.get(m).getTitle());
                        intent.putExtra("isMain", false);
                        intent.putExtra("special_id", bannerList.get(m).getKeywords());
                    } else if (bannerList.get(m).getType().equals(TYPE_KEYWORD)) {
                        intent = new Intent(activity, HotActivity.class);
                        intent.putExtra("words", "");
                        intent.putExtra("keyword", bannerList.get(m).getKeywords());
                        intent.putExtra("title", bannerList.get(m).getTitle());
                        intent.putExtra("text_position", 5);
                        intent.putExtra("isMain", false);
                        intent.putExtra("picture", bannerList.get(m).getGoods_url());
                    } else if (bannerList.get(m).getType().equals(TYPE_LINK)) {
                        intent = new Intent(activity, LinkActivity.class);
                        intent.putExtra("words", bannerList.get(m).getSubhead());
                        intent.putExtra("keyword", bannerList.get(m).getKeywords());
                        intent.putExtra("title", bannerList.get(m).getTitle());
                        intent.putExtra("picture", bannerList.get(m).getGoods_url());
                        String[] strings = bannerList.get(m).getContent().split(",");
                        intent.putExtra("goods_id", strings[1]);
                        intent.putExtra("goods_img", strings[0]);
                    }
                    activity.startActivity(intent);
                }
            });
        }
        pagerAdapter.notifyDataSetChanged();
        viewpager.setAdapter(pagerAdapter);
//        currentposition = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % list.size();
        viewpager.setCurrentItem(0);
        circleIndicator.setViewPager(viewpager, list.size());
        viewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        swipeRefreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        swipeRefreshLayout.setEnabled(true);
                        break;

                }
                return false;
            }
        });
        if (!isRefresh) {
            handler.post(runnable);
        }
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
            if (!isDestory) {
                if (viewPagerBaner.getCurrentItem() >= list.size() - 1) {
                    viewPagerBaner.setCurrentItem(0);
                } else {
                    viewPagerBaner.setCurrentItem(viewPagerBaner.getCurrentItem() + 1);
                }

                handler.postDelayed(runnable, 5000);
            }

        }
    };


    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
//        initRecycler();
        initView(false);
        initListener();
    }

    private void initListener() {
        relativeCard.setOnClickListener(this);
        relativeRedbag.setOnClickListener(this);
        relativeOne.setOnClickListener(this);
        imgTop.setOnClickListener(this);
        relaitveQiye.setOnClickListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        isDestory = true;
        if (myBroadCastReceiver != null)
            myBroadCastReceiver.unRegister();
        if (banners != null)
            banners.clearImageTimerTask();
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
    public void loadData(List mData) {
        if (isRefresh) {
            mGoods.clear();
            swipeRefreshLayout.setRefreshing(false);
            isRefresh = false;
        }
        if (mData.size() == 0) {
            cardTodayHot.setVisibility(View.GONE);
            return;
        }
        mGoods.addAll(mData);
        if (galleryAdapter != null) {
            galleryAdapter.notifyDataSetChanged();

        } else {

            if(mGoods!=null&&mGoods.size()>0){
                galleryAdapter = new GalleryAdapter(activity, mGoods);
                hotrecyclerView.setAdapter(galleryAdapter);
                galleryAdapter.setOnItemClickListner(new GalleryAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(RecyclerView.ViewHolder holder, int position) {
                        Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                        intent.putExtra("sid", mGoods.get(position).getGoods_id());
                        intent.putExtra("pic", mGoods.get(position).getGoods_url());
                        startActivity(intent);
                    }
                });
            }

        }
    }

    @Override
    public void loadNewsData(List mData) {

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.relative_card:
                intent = new Intent(getActivity(), RechargeActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_redbag:
                intent = new Intent(getActivity(), RedBagActivity.class);
                intent.putExtra("red_code", "");
                startActivity(intent);
                break;
            case R.id.relative_one:
                if (!Util.isYYGLogin) {
                    if (MyApplication.getInstance().self != null)
                        loginForYYG();
                }
                intent = new Intent(getActivity(), YYGActivity.class);
                startActivity(intent);
                break;
            case R.id.img_top:
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
//                        int[] location = new int[2];
//                        titleTwo.getLocationOnScreen(location);
//                        int offset = location[1] - mRootScrollView.getMeasuredHeight();
//                        if (offset < 0) {
//                            offset = 0;
//                        }
                        scrollView.smoothScrollTo(0, 0);
                    }
                });
                break;
            case R.id.relaitve_qiye:
                if (MyApplication.getInstance().getMykey() == null) {
                    intent = new Intent(activity, WXEntryActivity.class);
                } else {
                    intent = new Intent(activity, CompanyConnectActivity.class);
                }
                startActivity(intent);
                break;
        }
    }

    private void loginForYYG() {
//        ModelUser snsUser=Thinksns.getMy();
//        Log.i("zjz","snsUser_id="+snsUser.getUid());
        HttpRequest.sendPost(TLUrl.getInstance().URL_yyg_login, "nickname=" + MyApplication.getInstance().self.getNickName() + "&userId="
                + MyApplication.getInstance().self.getId() + "&avator=" + MyApplication.getInstance().self.getAvatarUrl() + "&userName=" + URLEncoder.encode(MyApplication.getInstance().self.getUserName())
                + "&alias="+MyApplication.getInstance().self.getId() , new HttpRevMsg() {
            @Override
            public void revMsg(String msg) {
                if (msg == null) {
                    return ;
                }
                Log.i("zjz", "login_for_yyg=" + msg);
                try {
                    JSONObject json = new JSONObject(msg);
                    if (json.optInt("status") == 1) {
                        Util.isYYGLogin = true;
                        Log.i("zjz", "mainfragment1_YYG");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
    }
}
