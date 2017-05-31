package com.abcs.haiwaigou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.ViewPagerAdapter;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.CommentFragment;
import com.abcs.haiwaigou.fragment.DetailFragment;
import com.abcs.haiwaigou.fragment.GoodsFragment;
import com.abcs.haiwaigou.utils.InitCarNum;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.huaqiaobang.tljr.news.viewpager.DepthPageTransformer;
import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONException;
import org.json.JSONObject;

public class GoodsDetailCommentActivity extends BaseFragmentActivity implements View.OnClickListener {

    public static TextView car_num;
    GoodsFragment goodsFragment;
    Fragment curentfragment;
    DetailFragment detailFragment;
    CommentFragment commentFragment;
    ViewPagerAdapter viewPagerAdapter;
    private PagerSlidingTabStrip tabs;
    ViewPager pager;
    int currentType;
    public Handler handler = new Handler();
    int num = 1;
    String sid;

    String photo_url;
    String detail_url;

    public String getPhoto_url() {
        return photo_url;
    }

    public String getDetail_url() {
        return detail_url;
    }


    MyBroadCastReceiver myBroadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_goods_detail_comment);
        sid = (String) getIntent().getSerializableExtra("sid");
//        photo_url= (String) getIntent().getSerializableExtra("photoUrl");
        detail_url = TLUrl.getInstance().URL_hwg_base+"/mobile/index.php?act=goods&op=goods_body&goods_id=" + sid;
        myBroadCastReceiver = new MyBroadCastReceiver(this, updateUI);
        myBroadCastReceiver.register();
        findViewById(R.id.tljr_img_news_back).setOnClickListener(this);

//        initView();
        initFragment();
        initViewPager();
        setViewPagerTitle();
    }

    MyBroadCastReceiver.UpdateUI updateUI = new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

        }

        @Override
        public void updateCarNum(Intent intent) {
//            initView();
        }


        @Override
        public void updateCollection(Intent intent) {

        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initView() {
        car_num = (TextView) findViewById(R.id.car_num);
        if (MyApplication.getInstance().self != null) {
            new InitCarNum(car_num, this,"");
        }
    }

    private void setViewPagerTitle() {
        viewPagerAdapter.getTitle().set(0, "图文详情");
        viewPagerAdapter.getTitle().set(1, "商品评论");
    }

    private void initViewPager() {
        pager = (ViewPager) findViewById(R.id.hqss_pager);
        //第三方Tab
        tabs = (PagerSlidingTabStrip) findViewById(R.id.hqss_tabs);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//        viewPagerAdapter.getDatas().add(goodsFragment);
        viewPagerAdapter.getDatas().add(detailFragment);
        viewPagerAdapter.getDatas().add(commentFragment);
        pager.setAdapter(viewPagerAdapter);
        pager.setPageTransformer(true, new DepthPageTransformer());
        tabs.setViewPager(pager);
//        tabs.setIndicatorHeight(Util.dip2px(this, 4));
        tabs.setTextSize(30);
//        tabs.setOnScrollStateChangedListener(new MyHorizontalScrollView.ScrollViewListener() {
//
//            @Override
//            public void onScrollChanged(MyHorizontalScrollView.ScrollType scrollType) {
//                // TODO Auto-generated method stub
//                if (scrollType == MyHorizontalScrollView.ScrollType.IDLE) {
//                    scrollX = tabs.getScrollX();
//                    Log.i("tabscroll", " 停止滑动标志");
//                    Log.i("mysize", " scrollX :  " + scrollX);
//                    if (scrollX < 100) {
//                        tabs.smoothScrollTo(0, 0);
//                        tempX = scrollTo;
//                        return;
//                    }
//                    for (int i = 0; i < leftList.size() - 1; i++) {
//
//                        if (leftList.get(i) < scrollX
//                                && scrollX < leftList.get(i + 1)) {
//                            if (i == 0) {
//                                scrollTo = scrollX - tempX <= 0 ? 0 : leftList
//                                        .get(1);
//                                break;
//                            }
//                            scrollTo = scrollX - tempX < 0 ? leftList.get(i) - 10
//                                    : leftList.get(i + 1) + 10;
//                            break;
//                        }
//                    }
//                    tabs.smoothScrollTo(scrollTo, 0);
//                    tempX = scrollTo;
//                }
//
//            }
//        });

        // indicator.setCurrentItem(0);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub

                curentfragment = viewPagerAdapter.getItem(position);
                currentType = position + 1;
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
        curentfragment = viewPagerAdapter.getItem(0);
        currentType = 1;
    }

    private void initFragment() {

//        goodsFragment = new GoodsFragment();
        detailFragment = new DetailFragment();
        commentFragment = new CommentFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("goods_id",sid);
        commentFragment.setArguments(bundle);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tljr_img_news_back:
                finish();
                break;
            case R.id.t_addshopcar:
                addToCart();
                break;
            case R.id.rl_shopcar:
                if (MyApplication.getInstance().self == null) {
                    Intent intent2 = new Intent(this, WXEntryActivity.class);
                    startActivity(intent2);
                    return;
                }
                Intent intent1 = new Intent(this, CartActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_dianpu:
                showToast("正在装修中...");
                break;
            case R.id.rl_shoucang:
                showToast("收藏成功...");
                break;
            case R.id.rl_fenxiang:
                showToast("分享成功...");
                break;
        }
    }

    private void addToCart() {
        if (MyApplication.getInstance().self == null) {
            Intent intent = new Intent(this, WXEntryActivity.class);
            startActivity(intent);
            return;
        }
        ProgressDlgUtil.showProgressDlg("", this);
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_add_cart, "uid=" + MyApplication.getInstance().self.getId() + "&sid=" + sid + "&num=" + num, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("status") == 1) {
                                //更新购物车数量
                                MyUpdateUI.sendUpdateCarNum(GoodsDetailCommentActivity.this);
                                new InitCarNum(car_num, GoodsDetailCommentActivity.this,"");
                                ProgressDlgUtil.stopProgressDlg();
                                showToast("添加成功！");
                                Log.i("zjz", "add:添加成功");
                            } else {
                                ProgressDlgUtil.stopProgressDlg();
                                Log.i("zjz", "add:解析失败");
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
    protected void onDestroy() {
        myBroadCastReceiver.unRegister();
        super.onDestroy();
    }
}
