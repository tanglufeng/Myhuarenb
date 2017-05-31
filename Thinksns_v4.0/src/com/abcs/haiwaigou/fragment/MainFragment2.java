package com.abcs.haiwaigou.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.activity.HotActivity;
import com.abcs.haiwaigou.activity.HotSpecialActivity;
import com.abcs.haiwaigou.activity.LinkActivity;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.utils.SecondUtils;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.haiwaigou.view.CircleIndicator;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.haiwaigou.view.zjzbanner.LMBanners;
import com.abcs.haiwaigou.view.zjzbanner.adapter.LBaseAdapter;
import com.abcs.haiwaigou.view.zjzbanner.transformer.TransitionEffect;
import com.abcs.huaqiaobang.adapter.CommonAdapter;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.HqbViewHolder;
import com.abcs.huaqiaobang.view.MainScrollView;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zjz on 2016/6/8 0008.
 * 海外购的其他fragment
 */
public class MainFragment2 extends BaseFragment {

    public static final String FLAG_BANNER = "1";//广告banner
    public static final String FLAG_UPDATEWEEK = "2";//每周上新
    public static final String FLAG_RECOMMEND = "3";//热门推荐
    public static final String FLAG_BRAND = "4";//品牌推荐
    public static final String FLAG_HOTTODAY = "5";//今日最热
    public static final String FLAG_SALETODAY = "6";//今日特卖
    public static final String FLAG_SECOND = "7";//二级分类
    public static final String TYPE_KEYWORD = "1";//商品关键字
    public static final String TYPE_SPECIAL = "2";//商品专题
    public static final String TYPE_LINK = "3";//链接
    public static final String TYPE_OTHERS = "4";//其他
    public static final String TYPE_GOODS = "5";//商品id
    @InjectView(R.id.img_top)
    ImageView imgTop;
    @InjectView(R.id.banners)
    LMBanners banners;
    private String[] strs = new String[]{TLUrl.getInstance().HWGBANNER,};
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
    @InjectView(R.id.linear_recommend)
    LinearLayout linearRecommend;
    @InjectView(R.id.grid_brand)
    GridView gridBrand;
    @InjectView(R.id.linear_special)
    LinearLayout linearSpecial;
    @InjectView(R.id.scrollView)
    MainScrollView scrollView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
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
    private ArrayList<Goods> secondList = new ArrayList<Goods>();//二级分类
    private List<String> bannerString = new ArrayList<>();
    private String plate_id;
    private String objectName;
    private Handler handler = new Handler();
    private ACache aCache;


    int picWith;
    int picHeight;
    int picHeight2;

    public static MainFragment2 newInstance(String plateId, String objectName) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("plate_id", plateId);
        bundle.putSerializable("objectName", objectName);
        MainFragment2 fragment = new MainFragment2();
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
                    R.layout.hwg_fragment_main2_2, null);
            ButterKnife.inject(this, view);
            Bundle bundle = getArguments();
            if (bundle != null) {
                plate_id = bundle.getString("plate_id");
                objectName = bundle.getString("objectName");

            }
            picWith=Util.WIDTH;
            picHeight=picWith*33/72;
            picHeight2=picWith*39/72;
            isPrepared = true;
            lazyLoad();
        }
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null) {
            p.removeView(view);
        }
        ButterKnife.inject(this, view);

        imgTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0, 0);
                    }
                });
            }
        });

        scrollView.setOnScroll(new MainScrollView.OnScroll() {
            @Override
            public void onScrollListener(int x, int y, int oldx, int oldy) {
                if (oldy - y  < 0||y==0) {
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
                aCache.remove(objectName);
                initView(isRefresh);
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


    private void initView(boolean isRefresh) {

        JSONObject mainObject = aCache.getAsJSONObject(objectName);
        if (mainObject != null && !isRefresh) {
            try {
                Log.i("zjz", "有本地main2" + objectName);
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
        Log.i("zjz", "刷新main2" + objectName);
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_type + "&plate_id=" + plate_id, null, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i("zjz", "main2_msg=" + msg);
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
            secondList.clear();
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
                goods.setKeywords(mObject.optString("depict"));
                goods.setSubhead(mObject.optString("desc_ribe"));
                goods.setTitle(mObject.optString("title"));
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
                } else if (mObject.optString("flag").equals(FLAG_SECOND)) {//二级分类
                    secondList.add(goods);

                }
            }
            mHasLoadedOnce = true;
//            initBannerViewPager(viewPagerBaner, circleIndicator);
//                                initRecommend();
            initBanners();
            initTodaySale();
            initBrand();
            //加载二级分类的图片按钮
            initSecondList();

//                                initWeekUpdate();
        }
    }

    private void initBanners() {
        //设置Banners高度
        banners.setLayoutParams(new LinearLayout.LayoutParams(picWith,picHeight));
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
                        intent.putExtra("text_position", 5);
                        intent.putExtra("isMain", false);
                        intent.putExtra("title", bannerList.get(position).getTitle());
                        intent.putExtra("picture", bannerList.get(position).getGoods_url());
                    } else if (bannerList.get(position).getType().equals(TYPE_LINK)) {
                        intent = new Intent(activity, LinkActivity.class);
                        intent.putExtra("words", bannerList.get(position).getSubhead());
                        intent.putExtra("keyword", bannerList.get(position).getKeywords());
                        intent.putExtra("title", bannerList.get(position).getTitle());
                        intent.putExtra("picture", bannerList.get(position).getGoods_url());
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

    private void initSecond() {
        if (secondList == null || secondList.size() == 0) {
            return;
        }
        final LinearLayout classificationContianer = (LinearLayout) view.findViewById(R.id.fragment_main2_classification);
        classificationContianer.removeAllViews();
        classificationContianer.setVisibility(View.VISIBLE);
        final int itemHight = (int) (SecondUtils.getScreenHight(activity) / 6.0f);
        //设置容器的高度
        classificationContianer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHight));
        for (int i = 0; i < secondList.size(); i++) {
            View secondView = activity.getLayoutInflater().inflate(R.layout.hwg_special_view_item2, null);
            secondView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHight, 1.0f));
            final ImageView imageView = (ImageView) secondView.findViewById(R.id.img_item);
        }
    }

    /**
     * 加载二级分类的按钮图片
     */
    private void initSecondList() {
        //二级分类的按钮图片的容器
        if (secondList == null || secondList.size() == 0) {
            return;
        }
        final LinearLayout classificationContianer = (LinearLayout) view.findViewById(R.id.fragment_main2_classification);
        classificationContianer.removeAllViews();
        classificationContianer.setVisibility(View.VISIBLE);
        final int listSize = secondList.size();
        //   final int   itemWith= (SecondUtils.getScreenWith(activity))/secondList.size();
        final int itemHight = (int) (SecondUtils.getScreenHight(activity) / 5.0f);
        //设置容器的高度
        classificationContianer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHight));
        for (int i = 0; i < listSize; i++) {
            final int finalI = i;
            View secondView = activity.getLayoutInflater().inflate(R.layout.hwg_special_view_item2, null);
            secondView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHight, 1.0f));
            final ImageView imageView = (ImageView) secondView.findViewById(R.id.img_item);
            // imageView.setLayoutParams(params);
            if (i == 0) {
                // int  itemFirstWith=  (SecondUtils.getScreenWith(activity)-30)/secondList.size();
                LinearLayout.LayoutParams firstParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHight);
                firstParams.setMargins(0, 0, 0, 0);
                imageView.setLayoutParams(firstParams);


            } else {
                LinearLayout.LayoutParams otherParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHight);
                otherParams.setMargins(2, 0, 0, 0);
                imageView.setLayoutParams(otherParams);
                // imageView.setPadding(0,15,0,10);

            }
            ImageLoader.getInstance().displayImage(secondList.get(i).getPicarr(), imageView, Options.getHDOptions());

            if (!secondList.get(finalI).getKeywords().equals(""))
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (secondList.get(finalI).getType().equals(TYPE_KEYWORD)) {

                            Intent intent = null;
                            intent = new Intent(activity, HotActivity.class);
                            intent.putExtra("words", secondList.get(finalI).getSubhead());
                            intent.putExtra("keyword", secondList.get(finalI).getKeywords());
                            intent.putExtra("text_position", finalI);
                            intent.putExtra("isSale", true);
                            intent.putExtra("title", secondList.get(finalI).getTitle());
                            intent.putExtra("picture", secondList.get(finalI).getGoods_url());
                            intent.putExtra("isSecond", true);
                            activity.startActivity(intent);
                        }

                    }
                });
            //添加到容器
            classificationContianer.addView(secondView);

//            if (plate_id.equals("62") && i == listSize - 1) {
//                //如果是个护护肤，还有一个敬请期待的按钮，该按钮点击没有效果
//                View secondView2 = activity.getLayoutInflater().inflate(R.layout.hwg_special_view_item2, null);
//                secondView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHight, 1.0f));
//                //   final ImageView   imageView=(ImageView)secondView.findViewById(R.id.img_item);
//                ImageView hufuImageView = (ImageView) secondView2.findViewById(R.id.img_item);
//                LinearLayout.LayoutParams otherParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHight);
//                otherParams.setMargins(5, 10, 0, 10);
//                hufuImageView.setLayoutParams(otherParams);
//                //   hufuImageView.setPadding(0, 15, 0, 10);
//                hufuImageView.setImageResource(R.drawable.qidai);
//                classificationContianer.addView(secondView2);
//
//            }


        }

    }

//    private void initWeekUpdate() {
//        linearWeekupdate.removeAllViews();
//        for (int i = 0; i < weekList.size(); i++) {
//            View view = activity.getLayoutInflater().inflate(R.layout.hwg_hot_view_item, null);
//            ImageView img_item = (ImageView) view.findViewById(R.id.img_item);
//            ImageLoader.getInstance().displayImage(weekList.get(i).getPicarr(), img_item, Options.getHDOptions());
////            LoadPicture loadPicture = new LoadPicture();
////            loadPicture.initPicture(img_item, adImgs.get(i).getPicarr());
//            final int finalI = i;
//            img_item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = null;
//                    if (weekList.get(finalI).getTitle_style().equals(TYPE_SPECIAL)) {
//                        intent = new Intent(activity, HotSpecialActivity.class);
//                        intent.putExtra("words", weekList.get(finalI).getSubhead());
//                        intent.putExtra("text_position", finalI);
//                        intent.putExtra("picture", weekList.get(finalI).getPicarr());
//                        intent.putExtra("special_id", weekList.get(finalI).getKeywords());
//                    } else if (weekList.get(finalI).getTitle_style().equals(TYPE_KEYWORD)) {
//                        intent = new Intent(activity, HotActivity.class);
//                        intent.putExtra("words", weekList.get(finalI).getSubhead());
//                        intent.putExtra("keyword", weekList.get(finalI).getKeywords());
//                        intent.putExtra("text_position", finalI);
//                        intent.putExtra("picture", weekList.get(finalI).getPicarr());
//                    }
//
//                    activity.startActivity(intent);
//                }
//            });
//            linearWeekupdate.addView(view);
//        }
//    }

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
                        intent.putExtra("title", todaysaleList.get(finalI).getTitle());
                        intent.putExtra("picture", todaysaleList.get(finalI).getGoods_url());
                        intent.putExtra("special_id", todaysaleList.get(finalI).getKeywords());
                    } else if (todaysaleList.get(finalI).getType().equals(TYPE_KEYWORD)) {
                        intent = new Intent(activity, HotActivity.class);
                        intent.putExtra("words", todaysaleList.get(finalI).getSubhead());
                        intent.putExtra("keyword", todaysaleList.get(finalI).getKeywords());
                        intent.putExtra("text_position", finalI);
                        intent.putExtra("isSale", true);
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
        for (int i = 0; i < recommendList.size(); i++) {
            View view = activity.getLayoutInflater().inflate(R.layout.hwg_weekupdate_view_item, null);
            ImageView img_item = (ImageView) view.findViewById(R.id.img_item);
            if (i != recommendList.size() - 1) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 0, 10);
                view.setLayoutParams(layoutParams);
            }
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
                        intent.putExtra("picture", recommendList.get(finalI).getPicarr());
                        intent.putExtra("special_id", recommendList.get(finalI).getKeywords());
                    } else if (recommendList.get(finalI).getType().equals(TYPE_KEYWORD)) {
                        intent = new Intent(activity, HotActivity.class);
                        intent.putExtra("words", recommendList.get(finalI).getSubhead());
                        intent.putExtra("keyword", recommendList.get(finalI).getKeywords());
                        intent.putExtra("text_position", finalI);
                        intent.putExtra("picture", recommendList.get(finalI).getPicarr());
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
            final int m = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = null;
                    if (bannerList.get(m).getType().equals(TYPE_SPECIAL)) {
                        intent = new Intent(activity, HotSpecialActivity.class);
                        intent.putExtra("words", "");
                        intent.putExtra("text_position", 5);
                        intent.putExtra("picture", bannerList.get(m).getPicarr());
                        intent.putExtra("title", bannerList.get(m).getTitle());
                        intent.putExtra("isMain", false);
                        intent.putExtra("special_id", bannerList.get(m).getKeywords());
                    } else if (bannerList.get(m).getType().equals(TYPE_KEYWORD)) {
                        intent = new Intent(activity, HotActivity.class);
                        intent.putExtra("words", "");
                        intent.putExtra("keyword", bannerList.get(m).getKeywords());
                        intent.putExtra("text_position", 5);
                        intent.putExtra("isMain", false);
                        intent.putExtra("title", bannerList.get(m).getTitle());
                        intent.putExtra("picture", bannerList.get(m).getPicarr());
                    } else if (bannerList.get(m).getType().equals(TYPE_LINK)) {
                        intent = new Intent(activity, LinkActivity.class);
                        intent.putExtra("words", bannerList.get(m).getSubhead());
                        intent.putExtra("keyword", bannerList.get(m).getKeywords());
                        intent.putExtra("title", bannerList.get(m).getTitle());
                        intent.putExtra("picture", bannerList.get(m).getPicarr());
                    }
                    activity.startActivity(intent);
                }
            });
        }
        viewpager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
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
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        isDestory = true;
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

}
