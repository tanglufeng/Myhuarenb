package com.abcs.haiwaigou.fragment;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.abcs.haiwaigou.activity.GoodsDetailCommentActivity;
import com.abcs.haiwaigou.adapter.GoodsAdapter;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.tljr.zrclistview.SimpleFooter;
import com.abcs.huaqiaobang.tljr.zrclistview.SimpleHeader;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjz on 2016/1/13.
 */
public class GoodsFragment extends Fragment {

    GoodsDetailCommentActivity activity;
    View rootView;
    RelativeLayout view;
    ZrcListView listView;
    GoodsAdapter goodsAdapter;
    ArrayList<Goods> goodsList = new ArrayList<Goods>();

    LinearLayout linear_photo;
    private WebView webView;
    //banner
    private boolean isRefresh = false;
    private ViewPager viewpager = null;
    private List<ImageView> list = null;
    private List<ImageView> mList = null;
    private ImageView[] img = null;
    public static String[] picUrl = {"http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/img_dashujuguanggao.png",
            "http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/img_jichaguanggaotu.png", "http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/img_licaiguanggaotu.png",
            "http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/img_licaiguanggaotu.png"};
    private int pageChangeDelay = 0;

    private Button[] btns;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (GoodsDetailCommentActivity) getActivity();
        view = (RelativeLayout) activity.getLayoutInflater().inflate(
                R.layout.hwg_fragment_goods, null);
        linear_photo = (LinearLayout) view.findViewById(R.id.linear_photo);
//        webView= (WebView) view.findViewById(R.id.web_view);
//        WebSettings settings=webView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setAllowFileAccess(true);
//        settings.setBuiltInZoomControls(true);

//        webView.loadUrl(activity.getPhoto_url());
        initDates();
    }

    private void initDates() {
        //获取手机宽
        final int width = (Util.dip2px(activity, 16));
        TypedArray array = activity.getResources().obtainTypedArray(R.array.banner_zhanshi);
        for (int i = 0; i < picUrl.length; i++) {
            //代码动态创建组件，而不是直接在xml里面布局

            ImageView view = new ImageView(activity);
            //            view.setClickable(true);
//            view.setOnClickListener(this);
            //	if (i < array.length())
            view.setBackgroundResource(array.getResourceId(i,
                    R.drawable.img_morentupian));

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(0, 0, 0, 15);
            view.setLayoutParams(layoutParams);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
//             StartActivity.imageLoader.displayImage(picUrl[i], view);
//            Util.setImage(picUrl[i], view, handler);
            linear_photo.addView(view);


        }
    }


    public void InitListView() {

        // // 设置默认偏移量，主要用于实现透明标题栏功能。（可选）
        // float density = getResources().getDisplayMetrics().density;
        // listView.setFirstTopOffset((int) (50 * density));
        listView = (ZrcListView) view.findViewById(R.id.tljr_zListView);
        listView.setFooterDividersEnabled(false);
        // listView.setSelector(R.drawable.tljr_listview_selector);
        // 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
        SimpleHeader header = new SimpleHeader(activity);
        header.setTextColor(0xffeb5041);
        header.setCircleColor(0xffeb5041);
        listView.setHeadable(header);
        // 设置加载更多的样式（可选）
        SimpleFooter footer = new SimpleFooter(activity);
        footer.setCircleColor(0xffeb5041);
        listView.setFootable(footer);

        goodsList.clear();
        for (int i = 0; i < 5; i++) {
            Goods goods = new Goods();
            goods.setTitle("Estee Lauder雅诗兰黛红石榴 鲜亮幻彩泡沫洁面乳 125ml");
            goodsList.add(goods);
        }
        goodsAdapter = new GoodsAdapter(activity, goodsList, listView);
        listView.setAdapter(goodsAdapter);
        goodsAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeAllViewsInLayout();
        return view;
    }

}
