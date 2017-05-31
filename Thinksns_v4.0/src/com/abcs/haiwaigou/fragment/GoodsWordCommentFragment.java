package com.abcs.haiwaigou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.tljr.news.viewpager.DepthPageTransformer;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zjz on 2016/5/18.
 */
public class GoodsWordCommentFragment extends Fragment {
    GoodsDetailActivity activity;
    View rootView;
    @InjectView(R.id.comment_tabs)
    PagerSlidingTabStrip commentTabs;
    @InjectView(R.id.comment_pager)
    ViewPager commentPager;

    CFViewPagerAdapter viewPagerAdapter;
    DetailFragment detailFragment;
    CommentFragment commentFragment;
    Fragment currentgoodsFragment;
    int currentType;
    String goods_id;
    private MyBroadCastReceiver myBroadCastReceiver;
    private RequestQueue mRequestQueue;
    private String all_comment="0";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (GoodsDetailActivity) getActivity();
        rootView = inflater.inflate(R.layout.hwg_fragment_goods_wc, null);
        ButterKnife.inject(this, rootView);
        myBroadCastReceiver=new MyBroadCastReceiver(activity,updateUI);
        myBroadCastReceiver.register();
        mRequestQueue = Volley.newRequestQueue(activity);
        goods_id = (String) getArguments().getSerializable("goods_id");
        initFragment();
        initViewPager();
//        initAllDates();
        return rootView;
    }
    MyBroadCastReceiver.UpdateUI updateUI=new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

        }

        @Override
        public void updateCarNum(Intent intent) {

        }

        @Override
        public void updateCollection(Intent intent) {
            if (intent.getStringExtra("type").equals(MyUpdateUI.TURN2COMMENT)){
                commentPager.setCurrentItem(1);
            }else  if (intent.getStringExtra("type").equals(MyUpdateUI.TURN2DETAIL)){
                commentPager.setCurrentItem(0);
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };


    private void initAllDates() {
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, TLUrl.getInstance().URL_hwg_goods_comment2 + "&goods_id=" + goods_id + "&type=1" + "&curpage=" + 1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "response=" + response);
                        if (response.getInt("page_total") != 0) {
                            JSONObject object = response.getJSONObject("datas");
                            JSONObject jsonObject = object.getJSONObject("goods_evaluate_info");
                            all_comment = jsonObject.optString("all");
                        }
                        initViewPager();
//                        initView();
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
//        pager = (ViewPager) findViewById(R.id.comment_pager);
//        //第三方Tab
//        tabs = (PagerSlidingTabStrip) findViewById(R.id.comment_tabs);
        viewPagerAdapter = new CFViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.getDatas().add(detailFragment);
//        viewPagerAdapter.getDatas().add(commentFragment);
//        viewPagerAdapter.getDatas().add(GoodsCommentFragment.newInstance(goods_id));
        viewPagerAdapter.getTitle().add(0, "图文详情");
//        viewPagerAdapter.getTitle().add(1, "商品评论");
//        viewPagerAdapter.getDatas().add(g5);
//        layoutParams = new LinearLayout.LayoutParams(Util.WIDTH, Util.HEIGHT);
//        commentPager.setLayoutParams(layoutParams);

        commentPager.setAdapter(viewPagerAdapter);
        commentPager.setPageTransformer(true, new DepthPageTransformer());
        commentPager.setOffscreenPageLimit(2);
        commentTabs.setViewPager(commentPager);
        commentTabs.setIndicatorHeight(Util.dip2px(activity, 4));
        commentTabs.setTextSize(Util.dip2px(activity,16));
        setSelectTextColor(0);
        commentTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                setSelectTextColor(position);
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
            View view = commentTabs.getChildAt(0);
//            if (view instanceof LinearLayout) {
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView !=null) {

                if (position == i) {
                    tabTextView.setTextColor(activity.getResources().getColor(R.color.tljr_statusbarcolor));
                } else {
                    tabTextView.setTextColor(activity.getResources().getColor(R.color.hwg_text2));
                }
            }
//            }
        }
    }

    public void initFragment() {
        Bundle bundle=new Bundle();
        bundle.putSerializable("goods_id",goods_id);
        detailFragment=new DetailFragment();
        detailFragment.setArguments(bundle);
        commentFragment=new CommentFragment();
        commentFragment.setArguments(bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        myBroadCastReceiver.unRegister();
    }
}
