package com.abcs.haiwaigou.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zjz on 2016/1/16.
 */
public class CommentFragment extends Fragment {
    GoodsDetailActivity activity;
    View rootView;
    RelativeLayout view;


    Fragment currentgoodsFragment;
    CommentItemFragment allFragment;
    CommentGoodFragment goodFragment;
    CommentMiddleFragment middleFragment;
    CommentBadFragment badFragment;
    int currentType;
    String goods_id;
    String all_comment="0";
    String good_comment="0";
    String middle_comment="0";
    String bad_comment="0";

    ViewPager pager;
    CFViewPagerAdapter viewPagerAdapter;
    private PagerSlidingTabStrip tabs;
    private RequestQueue mRequestQueue;
    private RelativeLayout relative_null;
    private LinearLayout linear_root;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity= (GoodsDetailActivity) getActivity();
        view = (RelativeLayout) activity.getLayoutInflater().inflate(
                R.layout.hwg_fragment_comment, null);
        goods_id= (String) getArguments().getSerializable("goods_id");
        mRequestQueue = Volley.newRequestQueue(activity);
        initFragment();

        relative_null= (RelativeLayout) view.findViewById(R.id.layout_null);
        linear_root= (LinearLayout) view.findViewById(R.id.linear_root);
//        if(MyApplication.getInstance().getMykey()==null){
//            linear_root.setVisibility(View.GONE);
//            relative_null.setVisibility(View.VISIBLE);
//        }else {
            linear_root.setVisibility(View.VISIBLE);
            relative_null.setVisibility(View.GONE);
            initAllDates();
//        }

    }

    private void initAllDates() {
        Log.i("zjz", "goods_id=" + goods_id);
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, TLUrl.getInstance().URL_hwg_goods_comment + "&goods_id=" + goods_id + "&type=1" + "&curpage=" + 1 + "&key=" + MyApplication.getInstance().getMykey(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        LogUtil.e("zjz", "good_comment:连接成功");
                        LogUtil.e("zjz", "response=" + response);
                        if (response.getInt("page_total") != 0) {
                            JSONObject object = response.getJSONObject("datas");
                            JSONObject jsonObject = object.getJSONObject("goods_evaluate_info");
                            all_comment=jsonObject.optString("all");
                            good_comment=jsonObject.optString("good");
                            middle_comment=jsonObject.optString("normal");
                            bad_comment=jsonObject.optString("bad");
                        }
                        initViewPager();

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


    LinearLayout.LayoutParams layoutParams;
    private void initViewPager() {
        LogUtil.e("zjz", "all_comment=" + all_comment);
        LogUtil.e("zjz", "good_comment=" + good_comment);
        LogUtil.e("zjz", "middle_comment=" + middle_comment);
        LogUtil.e("zjz", "bad_comment=" + bad_comment);
        pager = (ViewPager) view.findViewById(R.id.comment_pager);
        //第三方Tab
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.comment_tabs);
        viewPagerAdapter = new CFViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.getDatas().add(allFragment);
        viewPagerAdapter.getDatas().add(goodFragment);
        viewPagerAdapter.getDatas().add(middleFragment);
        viewPagerAdapter.getDatas().add(badFragment);
        viewPagerAdapter.getTitle().add(0, "所有"+"("+all_comment+")");
        viewPagerAdapter.getTitle().add(1, "好评"+"("+good_comment+")");
        viewPagerAdapter.getTitle().add(2, "中评"+"("+middle_comment+")");
        viewPagerAdapter.getTitle().add(3, "差评" + "(" +bad_comment+")");
//        viewPagerAdapter.getDatas().add(g5);
//        layoutParams=new LinearLayout.LayoutParams(Util.WIDTH,Util.HEIGHT);
//        pager.setLayoutParams(layoutParams);
        pager.setAdapter(viewPagerAdapter);
        pager.setOffscreenPageLimit(3);
//        pager.setPageTransformer(true, new DepthPageTransformer());
        tabs.setViewPager(pager);
        tabs.setIndicatorHeight(Util.dip2px(activity, 4));
        tabs.setTextSize(Util.dip2px(activity, 16));
//        tabs.setTextSize(25);
        setSelectTextColor(0);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub

                currentgoodsFragment =  viewPagerAdapter.getItem(position);
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
        currentgoodsFragment =  viewPagerAdapter.getItem(0);
        currentType = 1;
    }

    private void setSelectTextColor(int position) {
        for (int i = 0; i < 4; i++) {
            View view = tabs.getChildAt(0);
//            if (view instanceof LinearLayout) {
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView !=null) {

                if (position == i) {
                    tabTextView.setTextColor(Color.parseColor("#f22828"));
                } else {
                    tabTextView.setTextColor(Color.parseColor("#646464"));
                }
            }
//            }
        }
    }

    private void initFragment() {
        Bundle bundle=new Bundle();
        bundle.putSerializable("goods_id",goods_id);
        bundle.putSerializable("isShow",false);
        allFragment=new CommentItemFragment();
        allFragment.setArguments(bundle);
        goodFragment=new CommentGoodFragment();
        goodFragment.setArguments(bundle);
        middleFragment=new CommentMiddleFragment();
        middleFragment.setArguments(bundle);
        badFragment=new CommentBadFragment();
        badFragment.setArguments(bundle);
//        g5=new CommentItemFragment();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
