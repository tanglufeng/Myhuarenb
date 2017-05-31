package com.abcs.haiwaigou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.CountryGoodsAdapter;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.GoodCartFragment;
import com.abcs.haiwaigou.fragment.adapter.CountryRecyclerGoodsAdapter;
import com.abcs.haiwaigou.fragment.customtool.FullyGridLayoutManager;
import com.abcs.haiwaigou.fragment.viewholder.CountryGoodsViewHolder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.view.XScrollView;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CountryGoodsActivity extends BaseFragmentActivity implements View.OnClickListener, CountryGoodsViewHolder.ItemOnClick {
    public ZrcListView listView;
    private CountryGoodsAdapter goodsAdapter;

    public Handler handler = new Handler();
    private TextView t_country_name;
    private String cid;
    private String country;
    private RecyclerView recyclerView;
    private XScrollView mScrollView;

    private List<ImageView> mohulist = null;
    private ImageView[] img_mohus = null;
    public ArrayList<Goods> goodsList = new ArrayList<Goods>();

    private LinearLayout linear_tuijian_root;
    private TextView t_tuijian_goods_name;
    private TextView t_tuijian_goods_money;
    private TextView t_tuijian_buy;
    private ImageView img_tuijian_goods_icon;
    private ImageView img_mohu;

    //banner
    private boolean isRefresh = false;
    private ViewPager viewpager = null;
    private LinearLayout linear;
    ArrayList<View> mlist = null;
    //    private List<View> mlist = null;
    private List<ImageView> list = null;
    private ImageView[] img = null;
    public static String[] picUrl = {"http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/img_dashujuguanggao.png",
            "http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/img_jichaguanggaotu.png", "http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/img_licaiguanggaotu.png",
            "http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/img_licaiguanggaotu.png", "http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/img_licaiguanggaotu.png"};
    private int pageChangeDelay = 0;

    private Button[] btns;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_country_goods);
        t_country_name = (TextView) findViewById(R.id.tljr_txt_country_title);

        findViewById(R.id.tljr_img_news_back).setOnClickListener(this);
        cid = (String) getIntent().getSerializableExtra("cid");
        country = (String) getIntent().getSerializableExtra("country");
        t_country_name.setText(country + "馆");


        initScrollView();
        initTitlePage();
        initRecyclerView();

    }


    private void initScrollView() {
        mScrollView = (XScrollView) findViewById(R.id.tljr_sy_sc);
        mScrollView.initWithContext(CountryGoodsActivity.this);
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(false);
        mScrollView.setAutoLoadEnable(false);
        mScrollView.setRefreshTime(Util.getNowTime());

        mScrollView.setIXScrollViewListener(new XScrollView.IXScrollViewListener() {

            @Override
            public void onRefresh() {
//                initTitlePage();
//                initRecyclerView();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onLoad();
                    }
                }, 2000);


                Log.i("zjz", "下拉刷新！！");
            }

            @Override
            public void onLoadMore() {
                System.out.println("加载更多");
            }
        });
    }

    private void onLoad() {
        initTitlePage();
        initRecyclerView();
//        Intent intent = new Intent(CountryGoodsActivity.this, InitCountryViewPager.class);
//        CountryGoodsActivity.this.startActivity(intent);
        mScrollView.stopRefresh();
        mScrollView.stopLoadMore();
    }


    private void initTitlePage() {
        // TODO Auto-generated method stub
//        	vpTime = (LinearLayout) view.findViewById(R.id.tljr_fragnment_showye_vptime);
//        	initTime();

        list = new ArrayList<ImageView>();

        viewpager = (ViewPager) findViewById(R.id.tljr_viewpager2);
        viewpager.setOffscreenPageLimit(2);
        viewpager.setPageMargin(30);
        linear = (LinearLayout) findViewById(R.id.linear_viewpager);
        linear.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewpager.dispatchTouchEvent(event);
            }
        });
//        Util.setSize(viewpager, Util.IMAGEWIDTH, (int) (Util.IMAGEWIDTH / 2.1));
        initTitleDate();
//        initPagerViewDates();
//        if (!isRefresh) {
//            handler.post(runnable);
//        }

//        final AsyncTask task = new AsyncTask() {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                Log.i("zjz", "task1");
////                initPagerViewDates();
//            }
//
//            @Override
//            protected Object doInBackground(Object[] params) {
//                Log.i("zjz", "task2");
//                initPagerViewDates();
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Object o) {
//                super.onPostExecute(o);
//                Log.i("zjz", "task3");
//                initView();
//                ProgressDlgUtil.stopProgressDlg();
//            }
//
//        };
//
//        task.execute();

    }

    public void initPagerViewDates() {

        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_country_tuijian, "cid=" + cid, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                try {
                    JSONObject object = new JSONObject(msg);
                    if (object.getInt("status") == 1) {
                        Log.i("zjz", "country_tuijian:连接成功");
                        JSONArray jsonArray = object.getJSONArray("msg");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            final Goods g = new Goods();
                            g.setTitle(object1.optString("title"));
                            g.setMoney(object1.optDouble("money"));
                            g.setPicarr(object1.optString("picarr"));
                            g.setId(object1.optInt("id"));
                            g.setSid(object1.optInt("sid"));
                            goodsList.add(g);
                        }
                        Log.i("zjz", "tijian_list" + goodsList.size());
//                                progressDialog.dismiss();
                        initTitleDate();
                    } else {
                        Log.i("zjz", "country_tuijian:解析失败");
                    }
//                    handler1.sendEmptyMessage(1);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    Log.i("zjz", msg);
                    e.printStackTrace();
                }
            }
        });

    }

    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    initTitleDate();
                    break;
            }
        }
    };

    int size = InitCountryViewPager.dataList.size();

    private void initTitleDate() {
//        Log.i("zjz", "tuijian=" + InitCountryViewPager.dataList.size());
//        View viewTemp1 = new View(this);
//        View viewTemp2 = new View(this);
        Log.i("zjz", "list=" + goodsList.size());
        mohulist = new ArrayList<ImageView>();
//        mlist = new ArrayList<View>();
//        List<ImageView>list=new ArrayList<>();
//        TypedArray array = getResources().obtainTypedArray(R.array.banner_array2);
//
//        for (int i = 0; i < size; i++) {
//            View view = getLayoutInflater().inflate(R.layout.hwg_item_country_tuijian2, null);
//            img_mohu = (ImageView) view.findViewById(R.id.img_mohu);
//            mohulist.add(img_mohu);
//            img_tuijian_goods_icon = (ImageView) view.findViewById(R.id.img_tuijian_goods_icon);
//            LoadPicture loadPicture = new LoadPicture();
//            loadPicture.initPicture(img_tuijian_goods_icon, InitCountryViewPager.dataList.get(i).getPicarr());
////            loadPicture.initPicture(img_tuijian_goods_icon,goodsList.get(i).getPicarr());
//            linear_tuijian_root = (LinearLayout) view.findViewById(R.id.linear_tuijian_root);
//            final int finalI = i;
//            linear_tuijian_root.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(CountryGoodsActivity.this, GoodsDetailActivity.class);
//                    intent.putExtra("sid", InitCountryViewPager.dataList.get(finalI).getSid());
//                    intent.putExtra("pic", InitCountryViewPager.dataList.get(finalI).getPicarr());
//                    startActivity(intent);
//                }
//            });
//            t_tuijian_goods_name = (TextView) view.findViewById(R.id.t_tuijian_goods_name);
//            t_tuijian_goods_name.setText(InitCountryViewPager.dataList.get(i).getTitle());
////            t_tuijian_goods_name.setText(goodsList.get(i).getTitle());
//            t_tuijian_goods_money = (TextView) view.findViewById(R.id.t_tuijian_goods_money);
//            t_tuijian_goods_money.setText(NumberUtils.formatPrice(InitCountryViewPager.dataList.get(i).getMoney()));
////            t_tuijian_goods_money.setText(NumberUtils.formatPrice(goodsList.get(i).getMoney()));
//            t_tuijian_buy = (TextView) view.findViewById(R.id.t_tuijian_buy);
//            t_tuijian_buy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showToast("购买成功！");
//                }
//            });
//            mlist.add(view);
//        }
//
//        Log.i("zjz", "mList=" + mlist.size());
////        img = new ImageView[mlist.size()];
        img = new ImageView[size];
        LinearLayout layout = (LinearLayout) findViewById(R.id.tljr_viewGroup2);
        layout.removeAllViews();
        for (int i = 0; i < size; i++) {
            img[i] = new ImageView(this);
            if (0 == i) {
//                mohulist.get(i).setVisibility(View.GONE);
                img[i].setBackgroundResource(R.drawable.img_yuandian1);
            } else {
//                mohulist.get(i).setVisibility(View.VISIBLE);
                img[i].setBackgroundResource(R.drawable.img_yuandian2);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.width = 10;
            params.height = 10;
            layout.addView(img[i], params);
        }
        adapter.notifyDataSetChanged();
        initAdapter();
    }


    private void initAdapter() {
        if (viewpager.getAdapter() == null) {
//            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
            viewpager.setAdapter(adapter);
            int n = Integer.MAX_VALUE / 2 % size;
            int itemPosition = Integer.MAX_VALUE / 2 - n;
            viewpager.setCurrentItem(itemPosition);
//            viewpager.setCurrentItem(1);

            viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {


                @Override
                public void onPageScrollStateChanged(int state) {
                    switch (state) {
                        case 0:
                            Log.i("zjz", "end");
                            break;
                        case 1:
                            Log.i("zjz", "press");
                            break;
                        case 2:
                            Log.i("zjz", "up");
                            break;
                    }
                }

                @Override
                public void onPageScrolled(int page, float positionOffset,
                                           int positionOffsetPixels) {
//                    Log.i("zjz", "scrolled_page=" + page % mlist.size());
                }

                @Override
                public void onPageSelected(int page) {
                    check(page);

                }
            });
        }
    }

    private GoodsAapater adapter = new GoodsAapater(getSupportFragmentManager());

    int temp = 0;

    private void check(int page) {

        pageChangeDelay = 0;
        temp = page % (size);
        Log.i("zjz", "temp=" + temp);
        for (int i = 0; i < size; i++) {
            if (temp == i) {
                MyUpdateUI.sendHideMohu(this,i);
//                mohulist.get(i).setVisibility(View.GONE);
                img[i].setBackgroundResource(R.drawable.img_yuandian1);
            } else {
//                mohulist.get(i).setVisibility(View.VISIBLE);
                img[i].setBackgroundResource(R.drawable.img_yuandian2);
            }

        }
//        for (int j = 0; j < mohulist.size(); j++) {
//            if (temp == j) {
//                mohulist.get(j).setVisibility(View.GONE);
//            } else {
//                mohulist.get(j).setVisibility(View.VISIBLE);
//            }
//
//        }
    }

//    PagerAdapter adapter = new PagerAdapter() {
//        @Override
//        public int getItemPosition(Object object) {
////            return super.getItemPosition(object);
//            return mlist.indexOf(object);
//        }
//
//        @Override
//        public boolean isViewFromObject(View arg0, Object arg1) {
//            return arg0 == arg1;
//        }
//
//        @Override
//        public int getCount() {
//            return Integer.MAX_VALUE;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
////            container.removeView(list.get(position%list.size()));
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
////            ViewParent parent=mlist.get(position%mlist.size()).getParent();
////            if(parent!=null){
////                viewpager.removeView(mlist.get(position%mlist.size()));
////            }
//
//            try {
//                container.addView(mlist.get(position % mlist.size()));
//            } catch (Exception e) {
//                Log.i("zjz", "chucuo");
//            }
////            container.addView(list.get(position));
//            return mlist.get(position % mlist.size());
//        }
//
//    };

//    class ViewPagerAdapter extends PagerAdapter {
//
//        @Override
//        public int getCount() {
//            return mlist.size();
//        }
//
//        /**
//         * 判断出去的view是否等于进来的view 如果为true直接复用
//         */
//        @Override
//        public boolean isViewFromObject(View arg0, Object arg1) {
//            return arg0 == arg1;
//        }
//
//        /**
//         * 销毁预加载以外的view对象, 会把需要销毁的对象的索引位置传进来就是position
//         */
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView(mlist.get(position));
//        }
//
//        /**
//         * 创建一个view
//         */
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
////            ViewParent parent=mlist.get(position).getParent();
////            if(parent==null){
////                container.addView(mlist.get(position));
////            }
//
//
//            try {
//                container.addView(mlist.get(position));
//            } catch (Exception e) {
//
//            }
//
//            return mlist.get(position);
//        }
//    }

    private class GoodsAapater extends FragmentPagerAdapter {


        public GoodsAapater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            GoodCartFragment faggment = GoodCartFragment.getInstance(InitCountryViewPager.dataList.get(position % size), position % size);
//            mohulist.add(faggment.img_mohu);
            return faggment;
        }


        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (viewpager.getCurrentItem() >= mlist.size() - 1) {
                viewpager.setCurrentItem(0);
            } else {
                viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
            }
            handler.postDelayed(runnable, 5000);
        }
    };


    CountryRecyclerGoodsAdapter countryRecyclerGoodsAdapter;
    FullyGridLayoutManager fullyGridLayoutManager;

    private void initRecyclerView() {
        countryRecyclerGoodsAdapter = new CountryRecyclerGoodsAdapter(this);
        fullyGridLayoutManager = new FullyGridLayoutManager(this, 2);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(fullyGridLayoutManager);

        initAllDates();
    }

    private void initAllDates() {
        countryRecyclerGoodsAdapter.getDatas().clear();
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_countrygoods, "cid=" + cid + "&pages=1&pagelist=30", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("status") == 1) {
                                Log.i("zjz", "country:连接成功");
                                JSONArray jsonArray = object.getJSONArray("msg");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    Goods g = new Goods();
                                    g.setTitle(object1.optString("title"));
                                    g.setMoney(object1.optDouble("money"));
                                    g.setPicarr(object1.optString("picarr"));
                                    g.setDismoney(object1.optDouble("dismoney"));
                                    g.setSid(object1.optInt("sid"));
                                    g.setId(object1.optInt("id"));
                                    countryRecyclerGoodsAdapter.getDatas().add(g);
                                }
                                recyclerView.setAdapter(countryRecyclerGoodsAdapter);

                            } else {
                                Log.i("zjz", "country解析失败");
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

    @Override
    public void onItemRootViewClick(int position) {
        Intent intent = new Intent(this, GoodsDetailActivity.class);
        intent.putExtra("sid", countryRecyclerGoodsAdapter.getDatas().get(position).getSid());
        intent.putExtra("pic", countryRecyclerGoodsAdapter.getDatas().get(position).getPicarr());
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tljr_img_news_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
