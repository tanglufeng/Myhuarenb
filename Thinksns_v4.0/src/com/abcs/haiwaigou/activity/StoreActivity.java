package com.abcs.haiwaigou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.ViewPagerAdapter;
import com.abcs.haiwaigou.adapter.viewholder.AllGoodsRecyclerViewHolder;
import com.abcs.haiwaigou.fragment.LocalBangFragment;
import com.abcs.haiwaigou.fragment.ShoppingBangFragment;
import com.abcs.haiwaigou.fragment.TravelBangFragment;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.view.CircleIndicator;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.tljr.news.viewpager.DepthPageTransformer;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.CircleImageView;
import com.abcs.huaqiaobang.view.FlowLayout;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.astuetz.PagerSlidingTabStrip;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StoreActivity extends BaseFragmentActivity implements View.OnClickListener, AllGoodsRecyclerViewHolder.ItemOnClick {


    @InjectView(R.id.banner_viewpager)
    ViewPager bannerViewpager;
    @InjectView(R.id.indicator)
    CircleIndicator indicator;
    //    @InjectView(R.id.rl_banner)
//    RelativeLayout rlBanner;
    @InjectView(R.id.img_storeHeader)
    CircleImageView imgStoreHeader;
    @InjectView(R.id.img_sexIcon)
    ImageView imgSexIcon;
    @InjectView(R.id.tv_personalName)
    TextView tvPersonalName;
    @InjectView(R.id.img_renzhengIcon)
    ImageView imgRenzhengIcon;
    @InjectView(R.id.txt_vAuthentication)
    TextView txtVAuthentication;
    @InjectView(R.id.img_VIcon)
    ImageView imgVIcon;
    @InjectView(R.id.tv_onlineTime)
    TextView tvOnlineTime;
    @InjectView(R.id.tv_storeLocation)
    TextView tvStoreLocation;
    @InjectView(R.id.tv_storeHometowns)
    TextView tvStoreHometowns;
    @InjectView(R.id.tv_describePoint)
    TextView tvDescribePoint;
    @InjectView(R.id.tv_servicePoint)
    TextView tvServicePoint;
    @InjectView(R.id.tv_logisticsPoint)
    TextView tvLogisticsPoint;
    @InjectView(R.id.personal_shuoming)
    TextView personalShuoming;
    @InjectView(R.id.tv_personalDesc)
    TextView tvPersonalDesc;
    @InjectView(R.id.store_details)
    LinearLayout storeDetails;
    @InjectView(R.id.hqb_tabs)
    PagerSlidingTabStrip hqbTabs;
    @InjectView(R.id.linear_tab)
    LinearLayout linearTab;
    @InjectView(R.id.bangViewpager)
    ViewPager bangViewpager;
    //    @InjectView(R.id.img_fenxiang)
//    ImageView imgFenxiang;
//    @InjectView(R.id.img_shoucang)
//    ImageView imgShoucang;
//    @InjectView(R.id.img_liuyan)
//    ImageView imgLiuyan;
//    @InjectView(R.id.img_siliao)
//    ImageView imgSiliao;
//    @InjectView(R.id.bottom_bar)
//    LinearLayout bottomBar;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.store_name)
    TextView storeName;
    @InjectView(R.id.title_bar)
    RelativeLayout titleBar;
    //    @InjectView(R.id.scroll)
//    ScrollView scroll;
    @InjectView(R.id.relative_more)
    RelativeLayout relativeMore;
    @InjectView(R.id.flowlayout)
    FlowLayout flowlayout;

    private RequestQueue requestQueue;
    private ArrayList<Goods> goodses;
    ViewPagerAdapter viewPagerAdapter;
    ShoppingBangFragment shoppingBangFragment;
    TravelBangFragment travelBangFragment;
    LocalBangFragment localBangFragment;
    Fragment curentfragment;
    int currentType;
    String store_id;
    String storename;
    ArrayList<Goods> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_store);
        ButterKnife.inject(this);
        requestQueue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        if (intent != null) {
            loadData(intent.getStringExtra("store_id"));
//            loadGoodsData(intent.getStringExtra("store_id"));
            store_id = intent.getStringExtra("store_id");
        }
        initAllDates();
        initFragment();

        addFlow();

        setOnListener();
    }

    private void addFlow() {


        TextView textView = new TextView(this);
        TextView textView1 = new TextView(this);
        TextView textView2 = new TextView(this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(4, 4, 4, 4);

        textView.setBackground(getResources().getDrawable(R.drawable.flag_01));
        textView.setText("阳光");

        textView.setTextColor(getResources().getColor(R.color.white));
        textView1.setTextColor(getResources().getColor(R.color.white));
        textView2.setTextColor(getResources().getColor(R.color.white));
        textView.setLayoutParams(layoutParams);
        textView1.setLayoutParams(layoutParams);
        textView2.setLayoutParams(layoutParams);


        textView1.setBackground(getResources().getDrawable(R.drawable.textview_style));
        textView1.setText("天真浪漫善良");
        textView2.setBackground(getResources().getDrawable(R.drawable.textview_style2));
        textView2.setText("玉树临风风流倜傥");
        flowlayout.addView(textView);
        flowlayout.addView(textView1);
        flowlayout.addView(textView2);
        flowlayout.requestLayout();


    }

    private void initAllDates() {

//        ProgressDlgUtil.showProgressDlg("", this);
        Log.i("zjz", "store_id=" + store_id);
        String param = null;
        param = TLUrl.getInstance().URL_hwg_store_goods + "&key=4&page=10&curpage=" + 1 + "&store_id=" + store_id;
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, param, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "storegoods:连接成功");
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("goods_list");
                        dataList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            Goods g = new Goods();
                            g.setId(i);
                            g.setGoods_id(object1.optString("goods_id"));
                            g.setTitle(object1.optString("goods_name"));
                            g.setMoney(object1.optDouble("goods_price"));
                            g.setPicarr(object1.optString("goods_image_url"));
                            g.setGoods_salenum(object1.optString("goods_salenum"));
                            dataList.add(g);
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
        requestQueue.add(jr);
    }

    LinearLayout.LayoutParams layoutParams;

    private void initViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.getTitle().add(0, "购物·BANG");
        viewPagerAdapter.getTitle().add(1, "旅游·BANG");
        viewPagerAdapter.getTitle().add(2, "当地·BANG");
        viewPagerAdapter.getDatas().add(shoppingBangFragment);
        viewPagerAdapter.getDatas().add(travelBangFragment);
        viewPagerAdapter.getDatas().add(localBangFragment);

        Log.i("zjz", "size=" + dataList.size());
        if (dataList.size() == 0) {
            relativeMore.setVisibility(View.GONE);
            layoutParams = new LinearLayout.LayoutParams(Util.WIDTH, Util.HEIGHT / 2);
        } else if (dataList.size() % 2 == 0) {
            relativeMore.setVisibility(View.VISIBLE);
            layoutParams = new LinearLayout.LayoutParams(Util.WIDTH, 116 * dataList.size());
        } else {
            relativeMore.setVisibility(View.VISIBLE);
            layoutParams = new LinearLayout.LayoutParams(Util.WIDTH, 116 * 2 * dataList.size());
        }
        bangViewpager.setLayoutParams(layoutParams);
        bangViewpager.setAdapter(viewPagerAdapter);
        bangViewpager.setPageTransformer(true, new DepthPageTransformer());
        hqbTabs.setViewPager(bangViewpager);
        hqbTabs.setTextSize(30);
        hqbTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub

                curentfragment = viewPagerAdapter.getItem(position);
                currentType = position + 1;
                if (currentType == 1) {
                    relativeMore.setVisibility(View.VISIBLE);
                } else {
                    relativeMore.setVisibility(View.GONE);
                }
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
        shoppingBangFragment = ShoppingBangFragment.getInstance(store_id);
        travelBangFragment = new TravelBangFragment();
        localBangFragment = new LocalBangFragment();
    }

    private void loadGoodsData(String store_id) {

        goodses = new ArrayList<Goods>();
        requestQueue.add(new JsonObjectRequest(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php?act=store&op=goods_list&key=4&page=10&curpage=1&store_id" + store_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject data = jsonObject.getJSONObject("datas");
                    JSONArray datas = data.getJSONArray("goods_list");
                    for (int i = 0; i < datas.length(); i++) {
                        Goods goods = new Goods();
                        JSONObject goodsObject = datas.getJSONObject(i);
                        goods.setGoods_id(goodsObject.optString("goods_id"));
                        goods.setTitle(goodsObject.optString("goods_name"));
                        goods.setMoney(goodsObject.optDouble("goods_price", 0.0));
                        goods.setDismoney(goodsObject.optDouble("goods_marketprice", 0.0));
                        goods.setGoods_salenum(goodsObject.optString("goods_salenum"));
                        goods.setTitle2(goodsObject.optString("evaluation_good_star"));
                        goods.setPicarr(goodsObject.optString("goods_image_url"));
                        goodses.add(goods);
                    }

                    bangViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                        @Override
                        public int getCount() {
                            return 3;
                        }

                        @Override
                        public Fragment getItem(int position) {
                            return null;
//                            return ShoppingBangFragment.getInstance(goodses);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }));


    }

    private void loadData(String store_id) {


        requestQueue.add(new JsonObjectRequest(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php?act=store&op=store_detail&store_id=" + store_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject data = jsonObject.getJSONObject("datas");
                    JSONObject store_pf = data.getJSONObject("store_pf");
                    JSONObject store_info = data.getJSONObject("store_info");
                    initData(store_pf, store_info);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }));

//        requestQueue.start();
    }

    private void initData(JSONObject store_pf, JSONObject store_info) throws JSONException {

        JSONObject store_desccredit = store_pf.optJSONObject("store_desccredit");
        JSONObject store_servicecredit = store_pf.getJSONObject("store_servicecredit");
        JSONObject store_deliverycredit = store_pf.getJSONObject("store_deliverycredit");
        if (!store_desccredit.equals("")) {
            tvDescribePoint.setText(store_desccredit.optString("credit", "0.0"));
        }
        if (!"".equals(store_servicecredit)) {
            tvServicePoint.setText(store_servicecredit.optString("credit", "0.0"));
        }
        if (!"".equals(store_deliverycredit)) {
            tvLogisticsPoint.setText(store_deliverycredit.optString("credit", "0.0"));
        }


        ImageLoader.getInstance().displayImage(TLUrl.getInstance().URL_hwg_base+"/data/upload/shop/store/" + store_info.optString("store_avatar"), imgStoreHeader, Options.getListOptions());
        tvPersonalName.setText(store_info.optString("member_name"));
        storeName.setText(store_info.optString("store_name"));
        storename = store_info.optString("store_name");
//        tvStoreLocation.setText(store_info.optString("area_info"));


        String store_slide = store_info.optString("store_slide");
        final String[] split = store_slide.split(",");


        bannerViewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return split.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(StoreActivity.this);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                ImageLoader.getInstance().displayImage(TLUrl.getInstance().URL_hwg_base+"/data/upload/shop/store/slide/" + split[position], imageView, Options.getListOptions());

                container.addView(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
//                super.destroyItem(container, position, object);
            }
        });
        bannerViewpager.setCurrentItem(0);
        indicator.setViewPager(bannerViewpager);

    }

    private void setOnListener() {
        relativeBack.setOnClickListener(this);
        relativeMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.relative_more:
                intent = new Intent(this, AllGoodsActivity.class);
                intent.putExtra("store", true);
                intent.putExtra("store_id", store_id);
                intent.putExtra("store_name", storename);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemRootViewClick(int position) {

    }
}
