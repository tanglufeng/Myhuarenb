package com.abcs.haiwaigou.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.fragment.CommentBadFragment;
import com.abcs.haiwaigou.fragment.CommentGoodFragment;
import com.abcs.haiwaigou.fragment.CommentItemFragment;
import com.abcs.haiwaigou.fragment.CommentMiddleFragment;
import com.abcs.haiwaigou.fragment.GoodsCommentFragment;
import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
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

public class GoodsCommentActivity extends BaseFragmentActivity implements View.OnClickListener {
    Fragment currentgoodsFragment;
    CommentItemFragment allFragment;
    CommentGoodFragment goodFragment;
    CommentMiddleFragment middleFragment;
    CommentBadFragment badFragment;
    int currentType;
    String goods_id;
    String all_comment = "0";
    String good_comment = "0";
    String middle_comment = "0";
    String bad_comment = "0";

    ViewPager pager;
    CFViewPagerAdapter viewPagerAdapter;
    private PagerSlidingTabStrip tabs;
    private RequestQueue mRequestQueue;
    private RelativeLayout relative_null;
    private RelativeLayout relative_back;
    private LinearLayout linear_root;
    String sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_goods_comment);
        goods_id = (String) getIntent().getSerializableExtra("sid");
        mRequestQueue = Volley.newRequestQueue(this);
        initFragment();

        relative_back = (RelativeLayout) findViewById(R.id.relative_back);
        relative_back.setOnClickListener(this);
//            initAllDates();
        initViewPager();
    }

    private void initAllDates() {
        Log.i("zjz", "goods_id=" + goods_id);
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, TLUrl.getInstance().URL_hwg_goods_comment2 + "&goods_id=" + goods_id + "&type=1" + "&curpage=" + 1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "comment_title:连接成功");
                        Log.i("zjz", "response=" + response);
                        if (response.getInt("page_total") != 0) {
                            JSONObject object = response.getJSONObject("datas");
                            JSONObject jsonObject = object.getJSONObject("goods_evaluate_info");
                            all_comment = jsonObject.optString("all");
                            good_comment = jsonObject.optString("good");
                            middle_comment = jsonObject.optString("normal");
                            bad_comment = jsonObject.optString("bad");
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

    private void initViewPager() {
        Log.i("zjz", "all_comment=" + all_comment);
        Log.i("zjz", "good_comment=" + good_comment);
        Log.i("zjz", "middle_comment=" + middle_comment);
        Log.i("zjz", "bad_comment=" + bad_comment);
        pager = (ViewPager) findViewById(R.id.comment_pager);
        //第三方Tab
        tabs = (PagerSlidingTabStrip) findViewById(R.id.comment_tabs);
        viewPagerAdapter = new CFViewPagerAdapter(getSupportFragmentManager());
//        viewPagerAdapter.getDatas().add(CommetAllFragment.newInstance(0, goods_id));
//        viewPagerAdapter.getDatas().add(CommetAllFragment.newInstance(0,goods_id));
//        viewPagerAdapter.getDatas().add(CommetAllFragment.newInstance(0,goods_id));
//        viewPagerAdapter.getDatas().add(CommetAllFragment.newInstance(0,goods_id));

//        viewPagerAdapter.getDatas().add(allFragment);
//        viewPagerAdapter.getDatas().add(goodFragment);
//        viewPagerAdapter.getDatas().add(middleFragment);
//        viewPagerAdapter.getDatas().add(badFragment);

        viewPagerAdapter.getDatas().add(GoodsCommentFragment.newInstance(goods_id));

        viewPagerAdapter.getTitle().add(0, "所有" + "(" + all_comment + ")");
//        viewPagerAdapter.getTitle().add(1, "好评"+"("+good_comment+")");
//        viewPagerAdapter.getTitle().add(2, "中评"+"("+middle_comment+")");
//        viewPagerAdapter.getTitle().add(3, "差评" + "(" +bad_comment+")");
//        viewPagerAdapter.getDatas().add(g5);
        pager.setAdapter(viewPagerAdapter);
        pager.setOffscreenPageLimit(1);
//        pager.setPageTransformer(true, new DepthPageTransformer());
        tabs.setViewPager(pager);
        tabs.setIndicatorHeight(Util.dip2px(this, 4));
        tabs.setTextSize(Util.dip2px(this, 16));
        setSelectTextColor(0);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                setSelectTextColor(position);
                currentgoodsFragment = viewPagerAdapter.getItem(position);
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
        currentgoodsFragment = viewPagerAdapter.getItem(0);
        currentType = 1;
    }

    private void setSelectTextColor(int position) {
        for (int i = 0; i < 4; i++) {
            View view = tabs.getChildAt(0);
//            if (view instanceof LinearLayout) {
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView != null) {

                if (position == i) {
                    tabTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    tabTextView.setTextColor(getResources().getColor(R.color.hwg_text2));
                }
            }
//            }
        }
    }

    private void initFragment() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("goods_id", goods_id);
        bundle.putSerializable("isShow", false);
        allFragment = new CommentItemFragment();
        allFragment.setArguments(bundle);
        goodFragment = new CommentGoodFragment();
        goodFragment.setArguments(bundle);
        middleFragment = new CommentMiddleFragment();
        middleFragment.setArguments(bundle);
        badFragment = new CommentBadFragment();
        badFragment.setArguments(bundle);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
        }
    }
}
