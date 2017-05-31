package com.abcs.haiwaigou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.activity.HotActivity;
import com.abcs.haiwaigou.activity.HotSpecialActivity;
import com.abcs.haiwaigou.activity.LinkActivity;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.adapter.DuDaoAdapter;
import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager;
import com.abcs.haiwaigou.model.Ddao;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zjz on 2016/9/13.
 */
public class HWGDuDaoFragment2 extends BaseFragment implements View.OnClickListener {

    public static final String TYPE_LINK = "1";//链接
    public static final String TYPE_KEYWORD = "2";//关键字
    public static final String TYPE_GOODS = "3";//id
    public static final String TYPE_SPECIAL = "4";//专题
    public static final String TYPE_OTHERS = "5";//其他


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
    boolean isRefresh;

    private ArrayList<Ddao> brandList = new ArrayList<Ddao>();
    private FullyLinearLayoutManager fullyLinearLayoutManager;
    DuDaoAdapter luxuryBrandAdapter;
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
            view = activity.getLayoutInflater().inflate(R.layout.hwg_dudao_fragment, null);
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
                aCache.remove(TLUrl.getInstance().HWGDDAO);
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
        JSONObject mainObj = aCache.getAsJSONObject(TLUrl.getInstance().HWGDDAO);
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
        Log.i("xuke113",TLUrl.getInstance().URL_hwg_hot_search+"?"+"act=luxury&op=find_dudao");
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_hot_search, "act=luxury&op=find_dudao", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("xuke113", "dudao_msg=" + msg);
                        try {
                            JSONObject mainObj = new JSONObject(msg);
                            aCache.remove(TLUrl.getInstance().HWGDDAO);
                            if (aCache.getAsJSONObject(TLUrl.getInstance().HWGDDAO) == null) {
                                aCache.put(TLUrl.getInstance().HWGDDAO, mainObj, 24 * 60 * 60);
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
//        bannerString.clear();
        brandList.clear();
        if (mainObj.optString("code").equals("200")) {
            JSONObject dataObj = mainObj.optJSONObject("datas");
            if (dataObj != null) {
                JSONArray brandArray = dataObj.optJSONArray("brand");
                if (brandArray != null) {
                    for (int i = 0; i < brandArray.length(); i++) {
                        JSONObject brandObj = brandArray.getJSONObject(i);
                        Ddao ddao = new Ddao();
                        if ("null".equals(brandObj.optString("cn_title"))&&"null".equals(brandObj.optString("en_title"))){
                            ddao.setCn_title(brandObj.optString("独道之旅"));
                            ddao.setEn_title(brandObj.optString("TripPrivates"));
                        }else{
                            Log.i("xuke113",brandObj.optString("cn_title")+"--"+brandObj.optString("en_title"));
                            ddao.setCn_title(brandObj.optString("cn_title"));
                            ddao.setEn_title(brandObj.optString("en_title"));
                        }
                        JSONArray datasArray = brandObj.optJSONArray("datas");
                        ArrayList<Ddao.DatasBean> datasBeanArrayList = new ArrayList<>();
                        if (datasArray != null) {
                            datasBeanArrayList.clear();
                            for (int j = 0; j < datasArray.length(); j++) {
                                Ddao.DatasBean datasBean = new Ddao.DatasBean();
                                JSONObject datasBeanObj = datasArray.getJSONObject(j);
                                datasBean.setImg(TLUrl.getInstance().URL_hwg_pic_head + datasBeanObj.optString("img"));
                                datasBean.setSort(datasBeanObj.optString("sort"));
                                datasBean.setFlag(datasBeanObj.optString("flag"));
                                datasBean.setDescs(datasBeanObj.optString("descs"));
                                datasBean.setTitle(datasBeanObj.optString("title"));
                                datasBean.setAdvert(datasBeanObj.optString("advert"));
                                datasBeanArrayList.add(datasBean);
                            }
                            ddao.setDatas(datasBeanArrayList);
                        }
                        brandList.add(ddao);
                    }
                    initBrandList();
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
            final Ddao ddao = brandList.get(p);

            View brand=activity.getLayoutInflater().inflate(R.layout.hwg_item_ddao_brand, null);
            LinearLayout llTitle = (LinearLayout) brand.findViewById(R.id.ll_title);
            TextView cnTitle = (TextView) brand.findViewById(R.id.cn_title);
            TextView enTitle = (TextView) brand.findViewById(R.id.en_title);
            ImageView img_big= (ImageView) brand.findViewById(R.id.img_big);
            LinearLayout linear_small= (LinearLayout) brand.findViewById(R.id.linear_small);
            if (!TextUtils.isEmpty(ddao.getCn_title())){
                llTitle.setVisibility(View.VISIBLE);
                cnTitle.setText(ddao.getCn_title());
            }
            if (!TextUtils.isEmpty(ddao.getEn_title())){
                llTitle.setVisibility(View.VISIBLE);
                enTitle.setText(ddao.getEn_title());
            }

//            if (!TextUtils.isEmpty(ddao.getCn_title())&&!TextUtils.isEmpty(ddao.getEn_title())){
//                cnTitle.setText(ddao.getCn_title());
//                enTitle.setText(ddao.getEn_title());
//            }else{
//                llTitle.setVisibility(View.GONE);
//            }
            if (ddao.getDatas() != null && ddao.getDatas().size() != 0) {
                linear_small.removeAllViews();
                linear_small.invalidate();
                for (int i = 0; i < ddao.getDatas().size(); i++) {
                    if (ddao.getDatas().get(i).getSort().equals("1")) {
                        ImageLoader.getInstance().displayImage(ddao.getDatas().get(i).getImg(), img_big, Options.getHDOptions());
                        final int finalI1 = i;
                        img_big.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = null;
                                if (ddao.getDatas().get(finalI1).getFlag().equals(HWGDuDaoFragment2.TYPE_SPECIAL)) {
                                    intent = new Intent(activity, HotSpecialActivity.class);
                                    intent.putExtra("words", "");
                                    intent.putExtra("text_position", 5);
                                    intent.putExtra("picture", ddao.getDatas().get(finalI1).getImg());
                                    intent.putExtra("title",ddao.getDatas().get(finalI1).getTitle());
                                    intent.putExtra("isMain", false);
                                    intent.putExtra("special_id",ddao.getDatas().get(finalI1).getDescs());
                                } else if (ddao.getDatas().get(finalI1).getFlag().equals(HWGDuDaoFragment2.TYPE_KEYWORD)) {
                                    intent = new Intent(activity, HotActivity.class);
                                    intent.putExtra("words", "");
                                    intent.putExtra("keyword", ddao.getDatas().get(finalI1).getDescs());
                                    intent.putExtra("title", ddao.getDatas().get(finalI1).getTitle());
                                    intent.putExtra("text_position", 5);
                                    intent.putExtra("isLuxury", true);
                                    intent.putExtra("picture", ddao.getDatas().get(finalI1).getImg());
                                } else if (ddao.getDatas().get(finalI1).getFlag().equals(HWGDuDaoFragment2.TYPE_LINK)) {
                                    intent = new Intent(activity, LinkActivity.class);
                                    intent.putExtra("keyword",ddao.getDatas().get(finalI1).getDescs());
                                    intent.putExtra("title", ddao.getDatas().get(finalI1).getTitle());
                                    String[] strings = ddao.getDatas().get(finalI1).getAdvert().split(",");
                                    intent.putExtra("goods_id", strings[1]);
                                    intent.putExtra("goods_img", strings[0]);
                                } else if (ddao.getDatas().get(finalI1).getFlag().equals(HWGDuDaoFragment2.TYPE_GOODS)) {
                                    intent = new Intent(activity, GoodsDetailActivity.class);
                                    intent.putExtra("sid", ddao.getDatas().get(finalI1).getDescs());
                                    intent.putExtra("pic",ddao.getDatas().get(finalI1).getImg());
                                }
                                activity.startActivity(intent);
                            }
                        });
                    } else if (ddao.getDatas().get(i).getSort().equals("2")) {
                        linear_small.setVisibility(View.VISIBLE);
                        View secondView = activity.getLayoutInflater().inflate(R.layout.hwg_item_luxury_img, null);
                        LinearLayout.LayoutParams rootParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Util.dip2px(activity, 120), 1.0f);
                        rootParams.setMargins(1,0,1,0);
                        secondView.setLayoutParams(rootParams);
                        final ImageView imageView = (ImageView) secondView.findViewById(R.id.img_item);
                        LinearLayout.LayoutParams otherParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Util.dip2px(activity, 120));
                        otherParams.setMargins(0, 0, 0, 0);
                        imageView.setLayoutParams(otherParams);
                        ImageLoader.getInstance().displayImage(ddao.getDatas().get(i).getImg(), imageView, Options.getHDOptions());
                        final int finalI = i;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(ddao.getDatas().get(finalI).getFlag().equals("0")||ddao.getDatas().get(finalI).getFlag().equals("null")||ddao.getDatas().get(finalI).getFlag().equals("")){
                                    Toast.makeText(activity,"敬请期待", Toast.LENGTH_SHORT).show();
                                }else {
                                    Intent intent = null;
                                    if (ddao.getDatas().get(finalI).getFlag().equals(HWGDuDaoFragment2.TYPE_SPECIAL)) {
                                        intent = new Intent(activity, HotSpecialActivity.class);
                                        intent.putExtra("words", "");
                                        intent.putExtra("text_position", 5);
                                        intent.putExtra("picture", ddao.getDatas().get(finalI).getImg());
                                        intent.putExtra("title", ddao.getDatas().get(finalI).getContext());
                                        intent.putExtra("isMain", false);
                                        intent.putExtra("special_id", ddao.getDatas().get(finalI).getDescs());
                                    } else if (ddao.getDatas().get(finalI).getFlag().equals(HWGDuDaoFragment2.TYPE_KEYWORD)) {
                                        intent = new Intent(activity, HotActivity.class);
                                        intent.putExtra("words", "");
                                        intent.putExtra("keyword", ddao.getDatas().get(finalI).getDescs());
                                        intent.putExtra("title", ddao.getDatas().get(finalI).getContext());
                                        intent.putExtra("text_position", 5);
                                        intent.putExtra("isMain", false);
                                        intent.putExtra("picture",ddao.getDatas().get(finalI).getImg());
                                    } else if (ddao.getDatas().get(finalI).getFlag().equals(HWGDuDaoFragment2.TYPE_GOODS)) {
                                        intent = new Intent(activity, GoodsDetailActivity.class);
                                        intent.putExtra("sid", ddao.getDatas().get(finalI).getDescs());
                                        intent.putExtra("pic", ddao.getDatas().get(finalI).getImg());
                                    }
                                    activity.startActivity(intent);
                                }
                            }
                        });
                        //添加到容器
                        linear_small.addView(secondView);
                    }else{
                        Log.i("xuke113","------------linear_small gone---------");
                        linear_small.setVisibility(View.GONE);
                    }
                }
            }
            linearBrand.addView(brand);
        }

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

//    class UrlImgAdapter implements LBaseAdapter<String> {
//        private Context mContext;
//
//        public UrlImgAdapter(Context context) {
//            mContext = context;
//        }
//
//        @Override
//        public View getView(final LMBanners lBanners, final Context context, final int position, String data) {
//            View view = LayoutInflater.from(mContext).inflate(R.layout.banner_item, null);
//            ImageView imageView = (ImageView) view.findViewById(R.id.id_image);
//            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            ImageLoader.getInstance().displayImage(data, imageView, Options.getHDOptions());
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = null;
//                    if (bannerList.get(position).getFlag().equals(TYPE_SPECIAL)) {
//                        intent = new Intent(activity, HotSpecialActivity.class);
//                        intent.putExtra("words", "");
//                        intent.putExtra("text_position", 5);
//                        intent.putExtra("picture", bannerList.get(position).getImgUrl());
//                        intent.putExtra("title", bannerList.get(position).getTitle());
//                        intent.putExtra("isMain", false);
//                        intent.putExtra("special_id", bannerList.get(position).getDesc());
//                    } else if (bannerList.get(position).getFlag().equals(TYPE_KEYWORD)) {
//                        intent = new Intent(activity, HotActivity.class);
//                        intent.putExtra("words", "");
//                        intent.putExtra("keyword", bannerList.get(position).getDesc());
//                        intent.putExtra("title", bannerList.get(position).getTitle());
//                        intent.putExtra("text_position", 5);
//                        intent.putExtra("isMain", false);
//                        intent.putExtra("picture", bannerList.get(position).getImgUrl());
//                    } else if (bannerList.get(position).getFlag().equals(TYPE_LINK)) {
//                        intent = new Intent(activity, LinkActivity.class);
//                        intent.putExtra("words", bannerList.get(position).getContext());
//                        intent.putExtra("keyword", bannerList.get(position).getDesc());
//                        intent.putExtra("title", bannerList.get(position).getTitle());
//                        intent.putExtra("picture", bannerList.get(position).getImgUrl());
//                        String[] strings = bannerList.get(position).getAdvert().split(",");
//                        intent.putExtra("goods_id", strings[1]);
//                        intent.putExtra("goods_img", strings[0]);
//                    } else if (bannerList.get(position).getFlag().equals(TYPE_GOODS)) {
//                        intent = new Intent(activity, GoodsDetailActivity.class);
//                        intent.putExtra("sid", bannerList.get(position).getDesc());
//                        intent.putExtra("pic", bannerList.get(position).getImgUrl());
//                    }
//                    activity.startActivity(intent);
//                }
//            });
//            return view;
//        }
//
//    }


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
