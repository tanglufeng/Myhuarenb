package com.abcs.haiwaigou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.fragment.adapter.HWGFragmentAdapter;
import com.abcs.haiwaigou.fragment.customtool.FullyGridLayoutManager;
import com.abcs.haiwaigou.fragment.viewholder.HWGFragmentViewHolder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.SpacesItemDecoration;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zjz on 2016/1/16.
 */
public class DetailFragment extends BaseFragment implements HWGFragmentViewHolder.ItemOnClick {
    GoodsDetailActivity activity;
    RelativeLayout view;

    private WebView webView;
    private ScrollView scrollView;
    private RecyclerView recyclerView;
    private ImageView imageView;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    private Handler handler = new Handler();
    private String goods_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (GoodsDetailActivity) getActivity();
        view = (RelativeLayout) activity.getLayoutInflater().inflate(
                R.layout.hwg_fragment_detail, null);
        goods_id = (String) getArguments().getSerializable("goods_id");
        webView = (WebView) view.findViewById(R.id.web_view);
        scrollView = (ScrollView) view.findViewById(R.id.news_scroll);
        recyclerView = (RecyclerView) view.findViewById(R.id.recommend_recyclerView);
        imageView= (ImageView) view.findViewById(R.id.img_commend);
        WebSettings settings = webView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        //设置可放大缩小
        settings.setBuiltInZoomControls(true);
        //设置加载自适应屏幕大小
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup p = (ViewGroup) view.getParent();
        isPrepared = true;
        lazyLoad();
        if (p != null)
            p.removeAllViewsInLayout();

        return view;
    }

    @Override
    protected void lazyLoad() {
        if (isVisible) {
            GoodsDetailActivity.setIsVisible(GoodsDetailActivity.DETAIL);

        }
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }

        if (NetworkUtils.isNetAvailable(activity)) {
            initRecommend();
        }


    }

    private void initRecommend() {
        Log.i("zjz","detail_lianjie");
        webView.loadUrl(activity.getDetail_url());
        hwgFragmentAdapter = new HWGFragmentAdapter(activity,this, true);
        fullyGridLayoutManager = new FullyGridLayoutManager(getContext(), 2);

        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(fullyGridLayoutManager);
        recyclerView.setAdapter(hwgFragmentAdapter);
        //添加分割线
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_size2);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        initDatas();
    }


    HWGFragmentAdapter hwgFragmentAdapter;
    FullyGridLayoutManager fullyGridLayoutManager;

    private void initDatas() {
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_gdetail + "&goods_id=" + goods_id, null, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "goods_commend:连接成功");
                                JSONObject object1 = object.getJSONObject("datas");
                                hwgFragmentAdapter.getDatas().clear();
                                if (object1.has("error")) {
                                    if(imageView!=null)
                                        imageView.setVisibility(hwgFragmentAdapter.getDatas().size()==0?View.GONE:View.VISIBLE);
                                } else {
                                    JSONObject object2 = object1.getJSONObject("goods_info");
                                    JSONObject storeObject = object1.getJSONObject("store_info");
                                    JSONObject suitObject = object1.getJSONObject("suitinfo");
                                    JSONArray commendArray = object1.getJSONArray("goods_commend_list");
                                    if (commendArray != null) {
                                        for (int i = 0; i < commendArray.length(); i++) {
                                            JSONObject goodsObject = commendArray.getJSONObject(i);
                                            Goods goods = new Goods();
                                            goods.setTitle(goodsObject.optString("goods_name"));
                                            goods.setGoods_id(goodsObject.optString("goods_id"));
                                            goods.setMoney(goodsObject.optDouble("goods_price"));
                                            goods.setPicarr(goodsObject.optString("goods_image_url"));
                                            /////////////////////////

                                            goods.setGoods_promotion_price(goodsObject.optString("goods_promotion_price"));
                                            goods.setGoods_promotion_type(goodsObject.optString("goods_promotion_type"));

                                            hwgFragmentAdapter.getDatas().add(goods);
                                        }
                                        hwgFragmentAdapter.notifyDataSetChanged();
                                    }
                                    if(imageView!=null)
                                        imageView.setVisibility(hwgFragmentAdapter.getDatas().size()==0?View.GONE:View.VISIBLE);
                                    mHasLoadedOnce = true;
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onItemRootViewClick(int position) {
        Intent intent = new Intent(activity, GoodsDetailActivity.class);
        intent.putExtra("sid", hwgFragmentAdapter.getDatas().get(position).getGoods_id());
        intent.putExtra("pic", hwgFragmentAdapter.getDatas().get(position).getPicarr());
        activity.startActivity(intent);
    }
}
