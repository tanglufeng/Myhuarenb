package com.abcs.haiwaigou.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.activity.HotActivity;
import com.abcs.haiwaigou.activity.HotSpecialActivity;
import com.abcs.haiwaigou.activity.LinkActivity;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.adapter.LuxuryBrandAdapter;
import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager;
import com.abcs.haiwaigou.model.Luxury;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.haiwaigou.view.zjzbanner.LMBanners;
import com.abcs.haiwaigou.view.zjzbanner.adapter.LBaseAdapter;
import com.abcs.haiwaigou.view.zjzbanner.transformer.TransitionEffect;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.MainScrollView;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.ActivityHome;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zjz on 2016/9/13.
 */
public class HWGLuxuryFragment extends BaseFragment implements View.OnClickListener {

    public static final String TYPE_LINK = "1";//链接
    public static final String TYPE_KEYWORD = "2";//关键字
    public static final String TYPE_GOODS = "3";//id
    public static final String TYPE_SPECIAL = "4";//专题
    public static final String TYPE_OTHERS = "5";//其他

    @InjectView(R.id.banners)
    LMBanners banners;
    @InjectView(R.id.linear_second)
    LinearLayout linearSecond;
    @InjectView(R.id.recycler_brand)
    RecyclerView recyclerBrand;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.img_top)
    ImageView imgTop;
    @InjectView(R.id.scrollView)
    MainScrollView scrollView;
    @InjectView(R.id.linear_brand)
    LinearLayout linearBrand;
    private View view;
    ActivityHome activity;

    int picWith;
    int picHeight;
    private Handler handler = new Handler();
    private ACache aCache;
    private List<String> bannerString = new ArrayList<String>();
    boolean isRefresh;
    private ArrayList<Luxury> bannerList = new ArrayList<Luxury>();
    private ArrayList<Luxury> secondList = new ArrayList<Luxury>();
    private ArrayList<Luxury> brandList = new ArrayList<Luxury>();
    private FullyLinearLayoutManager fullyLinearLayoutManager;
    LuxuryBrandAdapter luxuryBrandAdapter;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (ActivityHome) getActivity();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.hwg_luxury_fragment, null);
        }
        ButterKnife.inject(this, view);
        aCache = ACache.get(activity);
        picWith = Util.WIDTH;
        picHeight = picWith * 33 / 72;
        fullyLinearLayoutManager = new FullyLinearLayoutManager(activity);
        imgTop.setOnClickListener(this);
        initDatas(false);
        initScroll();
    }


    private void initScroll() {

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MyUpdateUI.sendUpdateCarNum(activity);
                aCache.remove(TLUrl.getInstance().HWGLUXURY);
                initDatas(true);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }, 2000);
            }
        });
        scrollView.setOnScroll(new MainScrollView.OnScroll() {
            @Override
            public void onScrollListener(int x, int y, int oldx, int oldy) {
                if (oldy - y < 0 || y == 0) {
                    //上滑
                    imgTop.setVisibility(View.INVISIBLE);
                } else {
                    imgTop.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void initDatas(boolean isRefresh) {
        JSONObject mainObj = aCache.getAsJSONObject(TLUrl.getInstance().HWGLUXURY);
        if (mainObj != null && !isRefresh) {
            try {

                initDataList(mainObj);
                if (swipeRefreshLayout != null && NetworkUtils.isNetAvailable(activity))
                    swipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(true);
                            initNewData();
                        }
                    });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            initNewData();
        }

    }

    private void initNewData() {
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_hot_search, "act=luxury&op=find_index", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zjz", "luxury_msg=" + msg);
                        try {
                            JSONObject mainObj = new JSONObject(msg);
                            aCache.remove(TLUrl.getInstance().HWGLUXURY);
                            if (aCache.getAsJSONObject(TLUrl.getInstance().HWGLUXURY) == null) {
                                aCache.put(TLUrl.getInstance().HWGLUXURY, mainObj, 24 * 60 * 60);
                            }

                            initDataList(mainObj);
                        } catch (JSONException e) {
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

    private void initDataList(JSONObject mainObj) throws JSONException {
        mHasLoadedOnce = true;
        bannerString.clear();
        bannerList.clear();
        secondList.clear();
        brandList.clear();
        if (mainObj.optString("code").equals("200")) {
            JSONObject dataObj = mainObj.optJSONObject("datas");
            if (dataObj != null) {
                JSONArray bannerArray = dataObj.optJSONArray("head");
                if (bannerArray != null) {
                    for (int i = 0; i < bannerArray.length(); i++) {
                        JSONObject bannerObj = bannerArray.getJSONObject(i);
                        Luxury luxury = new Luxury();
                        luxury.setImgUrl(TLUrl.getInstance().URL_hwg_pic_head + bannerObj.optString("img"));
                        luxury.setContext(bannerObj.optString("context"));
                        luxury.setFlag(bannerObj.optString("flag"));
                        luxury.setDesc(bannerObj.optString("descs"));
                        luxury.setAdvert(bannerObj.optString("advert"));
                        luxury.setTitle(bannerObj.optString("title"));
                        bannerList.add(luxury);
                        bannerString.add(TLUrl.getInstance().URL_hwg_pic_head + bannerObj.optString("img"));
                    }
                    Log.i("zjz", "luxury_banner=" + bannerString.size());
                    if (bannerString.size() > 0) {
                        initBanners();
                    }
                }

                JSONArray secondArray = dataObj.optJSONArray("class");
                if (secondArray != null) {
                    for (int i = 0; i < secondArray.length(); i++) {
                        JSONObject secondObj = secondArray.getJSONObject(i);
                        Luxury luxury = new Luxury();
                        luxury.setImgUrl(TLUrl.getInstance().URL_hwg_pic_head + secondObj.optString("img"));
                        luxury.setFlag(secondObj.optString("flag"));
                        luxury.setDesc(secondObj.optString("descs"));
                        luxury.setContext(secondObj.optString("context"));
                        luxury.setTitle(secondObj.optString("title"));
                        luxury.setAdvert(secondObj.optString("advert"));
                        secondList.add(luxury);
                    }
                    initSecond();
                }

                JSONArray brandArray = dataObj.optJSONArray("brand");
                if (brandArray != null) {
                    for (int i = 0; i < brandArray.length(); i++) {
                        JSONObject brandObj = brandArray.getJSONObject(i);
                        Luxury luxury = new Luxury();
                        Luxury.ImgBean imgBean = new Luxury.ImgBean();
                        JSONObject imgBeanObj = brandObj.optJSONObject("img");

                        if (imgBeanObj != null) {
                            imgBean.setImg(TLUrl.getInstance().URL_hwg_pic_head + imgBeanObj.optString("img"));
                            imgBean.setFlag(imgBeanObj.optString("flag"));
                            imgBean.setDesc(imgBeanObj.optString("descs"));
                        }
                        luxury.setImgBean(imgBean);
                        JSONArray datasArray = brandObj.optJSONArray("datas");
                        ArrayList<Luxury.DatasBean> datasBeanArrayList = new ArrayList<Luxury.DatasBean>();
                        if (datasArray != null) {
                            datasBeanArrayList.clear();
                            for (int j = 0; j < datasArray.length(); j++) {
                                Luxury.DatasBean datasBean = new Luxury.DatasBean();
                                JSONObject datasBeanObj = datasArray.getJSONObject(j);
                                datasBean.setImg(TLUrl.getInstance().URL_hwg_pic_head + datasBeanObj.optString("img"));
                                datasBean.setSort(datasBeanObj.optString("sort"));
                                datasBean.setFlag(datasBeanObj.optString("flag"));
                                datasBean.setDesc(datasBeanObj.optString("descs"));
                                datasBean.setTitle(datasBeanObj.optString("title"));
                                datasBean.setAdvert(datasBeanObj.optString("advert"));
                                datasBeanArrayList.add(datasBean);
                            }
                            luxury.setDatas(datasBeanArrayList);
                        }
                        brandList.add(luxury);
                    }

                    initBrandList();
//                    initBrandRecycler();
                }
            }
        }
    }

    private void initBrandList() {
        if (brandList == null || brandList.size() == 0) {
            return;
        }
        linearBrand.removeAllViews();
        linearBrand.setVisibility(View.VISIBLE);
        for(int p=0;p<brandList.size();p++){
            final Luxury luxury = brandList.get(p);
            View brand=activity.getLayoutInflater().inflate(R.layout.hwg_item_luxury_brand, null);
            ImageView img_title= (ImageView) brand.findViewById(R.id.img_title);
            ImageView img_big= (ImageView) brand.findViewById(R.id.img_big);
            LinearLayout linear_small= (LinearLayout) brand.findViewById(R.id.linear_small);
            if (!TextUtils.isEmpty(luxury.getImgBean().getImg()))
                ImageLoader.getInstance().displayImage(luxury.getImgBean().getImg(), img_title, Options.getHDOptions());
            if (luxury.getDatasBean() != null && luxury.getDatasBean().size() != 0) {
                linear_small.removeAllViews();
                linear_small.invalidate();
                for (int i = 0; i < luxury.getDatasBean().size(); i++) {
                    if (luxury.getDatasBean().get(i).getSort().equals("1")) {
                        ImageLoader.getInstance().displayImage(luxury.getDatasBean().get(i).getImg(), img_big, Options.getHDOptions());
                        final int finalI1 = i;
                        img_big.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = null;
                                if (luxury.getDatasBean().get(finalI1).getFlag().equals(HWGLuxuryFragment.TYPE_SPECIAL)) {
                                    intent = new Intent(activity, HotSpecialActivity.class);
                                    intent.putExtra("words", "");
                                    intent.putExtra("text_position", 5);
                                    intent.putExtra("picture", luxury.getDatasBean().get(finalI1).getImg());
                                    intent.putExtra("title", luxury.getDatasBean().get(finalI1).getTitle());
                                    intent.putExtra("isMain", false);
                                    intent.putExtra("special_id",luxury.getDatasBean().get(finalI1).getDesc());
                                } else if (luxury.getDatasBean().get(finalI1).getFlag().equals(HWGLuxuryFragment.TYPE_KEYWORD)) {
                                    intent = new Intent(activity, HotActivity.class);
                                    intent.putExtra("words", "");
                                    intent.putExtra("keyword", luxury.getDatasBean().get(finalI1).getDesc());
                                    intent.putExtra("title", luxury.getDatasBean().get(finalI1).getTitle());
                                    intent.putExtra("text_position", 5);
                                    intent.putExtra("isLuxury", true);
                                    intent.putExtra("picture", luxury.getDatasBean().get(finalI1).getImg());
                                } else if (luxury.getDatasBean().get(finalI1).getFlag().equals(HWGLuxuryFragment.TYPE_LINK)) {
                                    intent = new Intent(activity, LinkActivity.class);
                                    intent.putExtra("keyword",luxury.getDatasBean().get(finalI1).getDesc());
                                    intent.putExtra("title", luxury.getDatasBean().get(finalI1).getTitle());
                                    String[] strings = luxury.getDatasBean().get(finalI1).getAdvert().split(",");
                                    intent.putExtra("goods_id", strings[1]);
                                    intent.putExtra("goods_img", strings[0]);
                                } else if (luxury.getDatasBean().get(finalI1).getFlag().equals(HWGLuxuryFragment.TYPE_GOODS)) {
                                    intent = new Intent(activity, GoodsDetailActivity.class);
                                    intent.putExtra("sid", luxury.getDatasBean().get(finalI1).getDesc());
                                    intent.putExtra("pic",luxury.getDatasBean().get(finalI1).getImg());
                                }
                                activity.startActivity(intent);
                            }
                        });
                    } else if (luxury.getDatasBean().get(i).getSort().equals("2")) {
                        View secondView = activity.getLayoutInflater().inflate(R.layout.hwg_item_luxury_img, null);
                        LinearLayout.LayoutParams rootParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Util.dip2px(activity, 120), 1.0f);
                        rootParams.setMargins(1,0,1,0);
                        secondView.setLayoutParams(rootParams);
                        final ImageView imageView = (ImageView) secondView.findViewById(R.id.img_item);
                        LinearLayout.LayoutParams otherParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Util.dip2px(activity, 120));
                        otherParams.setMargins(0, 0, 0, 0);
                        imageView.setLayoutParams(otherParams);
                        // imageView.setPadding(0,15,0,10);
                        ImageLoader.getInstance().displayImage(luxury.getDatasBean().get(i).getImg(), imageView, Options.getHDOptions());
                        final int finalI = i;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(luxury.getDatasBean().get(finalI).getFlag().equals("0")||luxury.getDatasBean().get(finalI).getFlag().equals("null")||luxury.getDatasBean().get(finalI).getFlag().equals("")){
                                    Toast.makeText(activity,"敬请期待", Toast.LENGTH_SHORT).show();
                                }else {
                                    Intent intent = null;
                                    if (luxury.getDatasBean().get(finalI).getFlag().equals(HWGLuxuryFragment.TYPE_SPECIAL)) {
                                        intent = new Intent(activity, HotSpecialActivity.class);
                                        intent.putExtra("words", "");
                                        intent.putExtra("text_position", 5);
                                        intent.putExtra("picture", luxury.getDatasBean().get(finalI).getImg());
                                        intent.putExtra("title", luxury.getDatasBean().get(finalI).getContext());
                                        intent.putExtra("isMain", false);
                                        intent.putExtra("special_id", luxury.getDatasBean().get(finalI).getDesc());
                                    } else if (luxury.getDatasBean().get(finalI).getFlag().equals(HWGLuxuryFragment.TYPE_KEYWORD)) {
                                        intent = new Intent(activity, HotActivity.class);
                                        intent.putExtra("words", "");
                                        intent.putExtra("keyword", luxury.getDatasBean().get(finalI).getDesc());
                                        intent.putExtra("title", luxury.getDatasBean().get(finalI).getContext());
                                        intent.putExtra("text_position", 5);
                                        intent.putExtra("isMain", false);
                                        intent.putExtra("picture", luxury.getDatasBean().get(finalI).getImg());
                                    } else if (luxury.getDatasBean().get(finalI).getFlag().equals(HWGLuxuryFragment.TYPE_GOODS)) {
                                        intent = new Intent(activity, GoodsDetailActivity.class);
                                        intent.putExtra("sid", luxury.getDatasBean().get(finalI).getDesc());
                                        intent.putExtra("pic", luxury.getDatasBean().get(finalI).getImg());
                                    }
                                    activity.startActivity(intent);
                                }


                            }
                        });
                        //添加到容器
                        linear_small.addView(secondView);
                    }
                }
            }
            linearBrand.addView(brand);
        }

    }

    private void initBrandRecycler() {
        luxuryBrandAdapter = new LuxuryBrandAdapter(activity, brandList);
        recyclerBrand.setFocusable(false);
        recyclerBrand.setLayoutManager(fullyLinearLayoutManager);
        recyclerBrand.setAdapter(luxuryBrandAdapter);
        luxuryBrandAdapter.notifyDataSetChanged();
    }

    private void initSecond() {
        //二级分类的按钮图片的容器
        if (secondList == null || secondList.size() == 0) {
            return;
        }
        linearSecond.removeAllViews();
        linearSecond.setVisibility(View.VISIBLE);
        final int listSize = secondList.size();
        final int itemHight = Util.dip2px(activity, 120);
        //设置容器的高度
        linearSecond.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHight));
        for (int i = 0; i < listSize; i++) {
            final int finalI = i;
            View secondView = activity.getLayoutInflater().inflate(R.layout.hwg_special_view_item2, null);
            secondView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHight, 1.0f));
            final ImageView imageView = (ImageView) secondView.findViewById(R.id.img_item);
            if (i == 0) {
                LinearLayout.LayoutParams firstParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHight);
                firstParams.setMargins(0, 0, 0, 0);
                imageView.setLayoutParams(firstParams);
            } else {
                LinearLayout.LayoutParams otherParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHight);
                otherParams.setMargins(2, 0, 0, 0);
                imageView.setLayoutParams(otherParams);
                // imageView.setPadding(0,15,0,10);
            }
            ImageLoader.getInstance().displayImage(secondList.get(i).getImgUrl(), imageView, Options.getHDOptions());

            if (!secondList.get(finalI).getFlag().equals("0"))
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (secondList.get(finalI).getFlag().equals(TYPE_KEYWORD)) {

                            Intent intent = null;
                            intent = new Intent(activity, HotActivity.class);
                            intent.putExtra("words", secondList.get(finalI).getContext());
                            intent.putExtra("keyword", secondList.get(finalI).getDesc());
                            intent.putExtra("text_position", finalI);
                            intent.putExtra("isSale", true);
                            intent.putExtra("title", secondList.get(finalI).getTitle());
                            intent.putExtra("picture", secondList.get(finalI).getFlag());
                            intent.putExtra("isSecond", true);
                            activity.startActivity(intent);
                        }

                    }
                });
            //添加到容器
            linearSecond.addView(secondView);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_top:
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0, 0);
                    }
                });
                break;
        }
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
                    if (bannerList.get(position).getFlag().equals(TYPE_SPECIAL)) {
                        intent = new Intent(activity, HotSpecialActivity.class);
                        intent.putExtra("words", "");
                        intent.putExtra("text_position", 5);
                        intent.putExtra("picture", bannerList.get(position).getImgUrl());
                        intent.putExtra("title", bannerList.get(position).getTitle());
                        intent.putExtra("isMain", false);
                        intent.putExtra("special_id", bannerList.get(position).getDesc());
                    } else if (bannerList.get(position).getFlag().equals(TYPE_KEYWORD)) {
                        intent = new Intent(activity, HotActivity.class);
                        intent.putExtra("words", "");
                        intent.putExtra("keyword", bannerList.get(position).getDesc());
                        intent.putExtra("title", bannerList.get(position).getTitle());
                        intent.putExtra("text_position", 5);
                        intent.putExtra("isMain", false);
                        intent.putExtra("picture", bannerList.get(position).getImgUrl());
                    } else if (bannerList.get(position).getFlag().equals(TYPE_LINK)) {
                        intent = new Intent(activity, LinkActivity.class);
                        intent.putExtra("words", bannerList.get(position).getContext());
                        intent.putExtra("keyword", bannerList.get(position).getDesc());
                        intent.putExtra("title", bannerList.get(position).getTitle());
                        intent.putExtra("picture", bannerList.get(position).getImgUrl());
                        String[] strings = bannerList.get(position).getAdvert().split(",");
                        intent.putExtra("goods_id", strings[1]);
                        intent.putExtra("goods_img", strings[0]);
                    } else if (bannerList.get(position).getFlag().equals(TYPE_GOODS)) {
                        intent = new Intent(activity, GoodsDetailActivity.class);
                        intent.putExtra("sid", bannerList.get(position).getDesc());
                        intent.putExtra("pic", bannerList.get(position).getImgUrl());
                    }
                    activity.startActivity(intent);
                }
            });
            return view;
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeAllViewsInLayout();
        ButterKnife.inject(this, view);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        Log.i("zjz", "isPrepared=" + isPrepared + "isVisible=" + isVisible + "mHasLoadedOnce=" + mHasLoadedOnce);
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            Log.i("zjz", "return掉了");
            return;
        }
        initDatas(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
