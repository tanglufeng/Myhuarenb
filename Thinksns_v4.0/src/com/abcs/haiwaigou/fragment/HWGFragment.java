package com.abcs.haiwaigou.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.AllGoodsActivity;
import com.abcs.haiwaigou.activity.CartActivity2;
import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.activity.GoodsSearchActivity2;
import com.abcs.haiwaigou.activity.HotActivity;
import com.abcs.haiwaigou.activity.HotSpecialActivity;
import com.abcs.haiwaigou.activity.RechargeActivity;
import com.abcs.haiwaigou.activity.RedBagActivity;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.fragment.adapter.HWGFragmentAdapter;
import com.abcs.haiwaigou.fragment.adapter.HWGUpdateAdapter;
import com.abcs.haiwaigou.fragment.customtool.FullyGridLayoutManager;
import com.abcs.haiwaigou.fragment.viewholder.HWGFragmentViewHolder;
import com.abcs.haiwaigou.fragment.viewholder.HWGUpdateViewHolder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.utils.InitCarNum;
import com.abcs.haiwaigou.utils.SpacesItemDecoration;
import com.abcs.haiwaigou.utils.mCountDownTimer;
import com.abcs.haiwaigou.view.CircleIndicator;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.main.MainActivity;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.model.StatusBarCompat;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.view.MainScrollView;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.mining.app.zxing.MipcaActivityCapture;
import com.abcs.sociax.android.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/4/9.
 */
public class HWGFragment extends Fragment implements View.OnClickListener, HWGFragmentViewHolder.ItemOnClick, HWGUpdateViewHolder.ItemOnClick {


    @InjectView(R.id.header_layout)
    LinearLayout headerLayout;
    //    @InjectView(R.id.img_bangong)
//    ImageView imgBangong;
//    @InjectView(R.id.food_name)
//    TextView foodName;
//    @InjectView(R.id.food_desc)
//    TextView foodDesc;
//    @InjectView(R.id.food_price)
//    TextView foodPrice;
    @InjectView(R.id.tv_shangxin)
    TextView tvShangxin;
    @InjectView(R.id.tv_time)
    TextView tvTime;
    //    @InjectView(R.id.img_celloction)
//    ImageView imgCelloction;
//    @InjectView(R.id.foodcelloction_name)
//    TextView foodcelloctionName;
//    @InjectView(R.id.foodcelloction_desc)
//    TextView foodcelloctionDesc;
//    @InjectView(R.id.num)
//    TextView num;
    @InjectView(R.id.scrollView)
//    XScrollView scrollView;
            MainScrollView scrollView;
    @InjectView(R.id.tv_searchHint)
    TextView tvSearchHint;
    @InjectView(R.id.relative_search)
    RelativeLayout relativeSearch;
    @InjectView(R.id.hwgtitle_bar)
    LinearLayout hwgtitleBar;
    @InjectView(R.id.viewPager_baner)
    ViewPager viewPagerBanner;
    @InjectView(R.id.circle_indicator)
    CircleIndicator circleIndicator;
    @InjectView(R.id.linear_hot)
    LinearLayout linearHot;
    @InjectView(R.id.relative_saomiao)
    RelativeLayout relativeSaomiao;
    @InjectView(R.id.car_num)
    TextView carNum;
    @InjectView(R.id.relative_cart)
    RelativeLayout relativeCart;
    @InjectView(R.id.week_recyclerView)
    RecyclerView weekRecyclerView;
    @InjectView(R.id.update_recyclerView)
    RecyclerView updateRecyclerView;
    @InjectView(R.id.img_shyp)
    ImageView imgShyp;
    @InjectView(R.id.img_spyl)
    ImageView imgSpyl;
    @InjectView(R.id.img_ghhz)
    ImageView imgGhhz;
    @InjectView(R.id.img_yybj)
    ImageView imgYybj;
    @InjectView(R.id.img_jrkz)
    ImageView imgJrkz;
    @InjectView(R.id.relative_week_more)
    RelativeLayout relativeWeekMore;
    @InjectView(R.id.relative_update_more)
    RelativeLayout relativeUpdateMore;
    @InjectView(R.id.t_hour)
    TextView tHour;
    @InjectView(R.id.t_minute)
    TextView tMinute;
    @InjectView(R.id.t_second)
    TextView tSecond;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.t_shyp_num)
    TextView tShypNum;
    @InjectView(R.id.t_spyl_num)
    TextView tSpylNum;
    @InjectView(R.id.t_ghhz_num)
    TextView tGhhzNum;
    @InjectView(R.id.t_yybj_num)
    TextView tYybjNum;
    @InjectView(R.id.img_card)
    ImageView imgCard;
    @InjectView(R.id.img_redbag)
    ImageView imgRedbag;
    @InjectView(R.id.img_one)
    ImageView imgOne;
    @InjectView(R.id.img_qiye)
    ImageView imgQiye;
    @InjectView(R.id.relative_card)
    RelativeLayout relativeCard;
    @InjectView(R.id.relative_redbag)
    RelativeLayout relativeRedbag;
    @InjectView(R.id.relative_one)
    RelativeLayout relativeOne;
    @InjectView(R.id.relaitve_qiye)
    RelativeLayout relaitveQiye;
    @InjectView(R.id.t_have_send)
    TextView tHaveSend;
    private View view;
    private MainActivity activity;
    private ArrayList<Goods> gcId = new ArrayList<Goods>();
    private ArrayList<Goods> goodsImgs = new ArrayList<Goods>();
    private List<ImageView> list = new ArrayList<ImageView>();
    private ArrayList<Goods> adImgs = new ArrayList<Goods>();
    private RequestQueue mRequestQueue;
    String shyp_gcid;
    String spyl_gcid;
    String ghhz_gcid;
    String yybj_gcid;
    String jrkz_gcid;

    boolean isDestory;
    boolean isRefresh = false;
    private Handler handler = new Handler();
    public static TextView car_num;
    MyBroadCastReceiver myBroadCastReceiver;
    private TextView[] times;
    boolean isFirst = true;

    public ACache aCache;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_hwq, null);
        activity = (MainActivity) getActivity();
        mRequestQueue = Volley.newRequestQueue(getContext());
        ButterKnife.inject(this, view);
        myBroadCastReceiver = new MyBroadCastReceiver(activity, updateUI);
        myBroadCastReceiver.register();
        aCache = ACache.get(activity);
        initView();
        initBanner(isRefresh);
        initGoodsGCid();
        initHot(isRefresh);
        setOnListener();
        car_num = (TextView) view.findViewById(R.id.car_num);
        initInCartNum();
        initWeek(isRefresh);
        initRedBagNum();
//        initUpdate(isRefresh);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////            window = getWindow();
////            // Translucent status bar
////            window.setFlags(
////                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
////                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) hwgtitleBar.getLayoutParams();
            params.setMargins(0, ((MainActivity) getActivity()).getStatusBarHeight(), 0, 0);
            hwgtitleBar.setLayoutParams(params);
        }
        return view;
    }

    private void initRedBagNum() {
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_head, "act=circle_index&op=count_red_envelope", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            Log.i("zjz","send_redbag_msg="+msg);
                            String s;
                            NumberFormat nf = NumberFormat.getNumberInstance();
                            nf.setMaximumFractionDigits(2);
                            s = nf.format(object.optJSONObject("datas").optDouble("count"));
                            if(tHaveSend!=null)
                                tHaveSend.setText(s);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();

                        } finally {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });

            }
        });
    }


    MyBroadCastReceiver.UpdateUI updateUI = new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

        }

        @Override
        public void updateCarNum(Intent intent) {
            initInCartNum();
        }

        @Override
        public void updateCollection(Intent intent) {

        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initInCartNum() {
        if (MyApplication.getInstance().self != null) {
            new InitCarNum(car_num, activity,"");
            Log.i("zjz", "用户存在");
        } else {
            car_num.setVisibility(View.GONE);
            Log.i("zjz", "用户不存在");
        }
    }

    private void initHot(boolean isRefresh) {
        JSONObject hotObject = aCache.getAsJSONObject(TLUrl.getInstance().HWGHOT);
        if (hotObject != null && !isRefresh) {
            Log.i("zjz", "有本地hot");
            try {
                initHotObject(hotObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_home, null, new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (aCache.getAsJSONObject(TLUrl.getInstance().HWGHOT) == null) {
                                    aCache.put(TLUrl.getInstance().HWGHOT, object, 24 * 60 * 60);
                                }
                                initHotObject(object);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                Log.i("zjz", e.toString());
                                Log.i("zjz", msg);
                                e.printStackTrace();

                            } finally {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });

                }
            });
        }

    }

    private void initHotObject(JSONObject object) throws JSONException {
        if (object.getInt("code") == 200) {
            adImgs.clear();
            Log.i("zjz", "hot:连接成功");

            JSONArray array = object.getJSONArray("datas");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object3 = array.getJSONObject(i);
                Iterator key = object3.keys();
                if (key.hasNext() && key.next().equals("home1")) {
                    JSONObject image = object3.optJSONObject("home1");
                    if (image.optString("title").equals("")) {
                        Goods g = new Goods();
                        g.setKeywords(image.optString("data"));
                        g.setPicarr(image.optString("image"));
                        g.setGoods_id(i + "");
                        g.setTitle_style(image.optString("type"));
                        g.setSubhead(image.optString("depict"));
                        adImgs.add(g);
                    }
                    Log.i("zjz", "pic=" + image.optString("image"));
                }
            }
            initHotData();
            Log.i("zjz", "zjzsize=" + adImgs.size());
        } else {
            Log.i("zjz", "banner:解析失败");
        }
    }

    private void initHotData() {
        linearHot.removeAllViews();
        for (int i = 0; i < adImgs.size(); i++) {
            View view = activity.getLayoutInflater().inflate(R.layout.hwg_hot_view_item, null);
            ImageView img_item = (ImageView) view.findViewById(R.id.img_item);
            ImageLoader.getInstance().displayImage(adImgs.get(i).getPicarr(), img_item, Options.getHDOptions());
//            LoadPicture loadPicture = new LoadPicture();
//            loadPicture.initPicture(img_item, adImgs.get(i).getPicarr());
            final int finalI = i;
            img_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (adImgs.get(finalI).getTitle_style().equals("special")) {
                        intent = new Intent(activity, HotSpecialActivity.class);
                        intent.putExtra("words",adImgs.get(finalI).getSubhead());
                        intent.putExtra("text_position", finalI);
                        intent.putExtra("picture", adImgs.get(finalI).getPicarr());
                        intent.putExtra("special_id", adImgs.get(finalI).getKeywords());
                    } else if (adImgs.get(finalI).getTitle_style().equals("keyword")) {
                        intent = new Intent(activity, HotActivity.class);
                        intent.putExtra("words",adImgs.get(finalI).getSubhead());
                        intent.putExtra("keyword", adImgs.get(finalI).getKeywords());
                        intent.putExtra("text_position", finalI);
                        intent.putExtra("picture", adImgs.get(finalI).getPicarr());
                    }

                    activity.startActivity(intent);
                }
            });
            linearHot.addView(view);
        }
    }


    private void initBanner(final boolean isRefresh) {
        JSONObject bannerObject = aCache.getAsJSONObject(TLUrl.getInstance().HWGBANNER);
        if (bannerObject != null && !isRefresh) {
            try {
                Log.i("zjz", "有本地banner");
                initBannerObject(bannerObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_home, null, new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (aCache.getAsJSONObject(TLUrl.getInstance().HWGBANNER) == null) {
                                    aCache.put(TLUrl.getInstance().HWGBANNER, object, 24 * 60 * 60);
                                }
                                swipeRefreshLayout.setRefreshing(false);
                                initBannerObject(object);
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
    }

    private void initBannerObject(JSONObject object) throws JSONException {
        if (object.getInt("code") == 200) {
            goodsImgs.clear();
            Log.i("zjz", "banner:连接成功");
            JSONArray array = object.getJSONArray("datas");
            JSONObject object1 = array.getJSONObject(0);
            JSONObject object2 = object1.getJSONObject("adv_list");
            JSONArray jsonArray = object2.getJSONArray("item");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object3 = jsonArray.getJSONObject(i);
                Goods g = new Goods();
                g.setPicarr(object3.optString("image"));
                g.setGoods_id(i + "");
                g.setTitle_style(object3.optString("type"));
                g.setKeywords(object3.optString("data"));
                goodsImgs.add(g);
            }
            initBannerViewPager(viewPagerBanner, circleIndicator);
            if (!isRefresh) {
                handler.post(runnable);
            }
        } else {
            Log.i("zjz", "banner:解析失败");
        }
    }

    /**
     * 初始化banner
     */
    private void initBannerViewPager(ViewPager viewpager, CircleIndicator circleIndicator) {
        list.clear();
        for (int i = 0; i < goodsImgs.size(); i++) {
            ImageView view = new ImageView(getContext());
            view.setScaleType(ImageView.ScaleType.FIT_XY);
//            Util.setImage(goodsImgs.get(i).getPicarr(), view, handler);
//            LoadPicture loadPicture = new LoadPicture();
//            loadPicture.initPicture(view, goodsImgs.get(i).getPicarr());
//            loadPicture.initPicture(view, true, goodsImgs.get(i).getPicarr(), i);
            ImageLoader.getInstance().displayImage(goodsImgs.get(i).getPicarr(), view, Options.getHDOptions());
            list.add(view);
            final int m = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = null;
                    if (goodsImgs.get(m).getTitle_style().equals("special")) {
                        intent = new Intent(activity, HotSpecialActivity.class);
                        intent.putExtra("words","");
                        intent.putExtra("text_position", 5);
                        intent.putExtra("picture", goodsImgs.get(m).getPicarr());
                        intent.putExtra("isMain", true);
                        intent.putExtra("special_id", goodsImgs.get(m).getKeywords());
                    } else if (goodsImgs.get(m).getTitle_style().equals("keyword")) {
                        intent = new Intent(activity, HotActivity.class);
                        intent.putExtra("words","");
                        intent.putExtra("keyword", goodsImgs.get(m).getKeywords());
                        intent.putExtra("text_position", 5);
                        intent.putExtra("isMain", true);
                        intent.putExtra("picture", goodsImgs.get(m).getPicarr());
                    }
                    activity.startActivity(intent);
                }
            });
        }
        viewpager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
//        currentposition = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % list.size();
        viewpager.setCurrentItem(0);
        circleIndicator.setViewPager(viewpager, list.size());
        viewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        swipeRefreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        swipeRefreshLayout.setEnabled(true);
                        break;

                }
                return false;
            }
        });
    }

    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isDestory) {
                if (viewPagerBanner.getCurrentItem() >= list.size() - 1) {
                    viewPagerBanner.setCurrentItem(0);
                } else {
                    viewPagerBanner.setCurrentItem(viewPagerBanner.getCurrentItem() + 1);
                }

                handler.postDelayed(runnable, 5000);
            }

        }
    };

    private void initGoodsGCid() {
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_goodssort, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        gcId.clear();
                        Log.i("zjz", "goodssort:连接成功");
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("class_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            Goods g = new Goods();
                            g.setGc_id(object1.optString("gc_id"));
                            g.setTitle(object1.optString("gc_name"));
                            g.setSubhead(object1.getString("text"));
                            g.setStore_goods_total(object1.optString("count"));
                            gcId.add(g);
                        }
                        for (int j = 0; j < gcId.size(); j++) {
                            switch (j) {
                                case 0:
                                    if (tShypNum != null)
                                        tShypNum.setText("宝贝:" + gcId.get(j).getStore_goods_total());
                                    shyp_gcid = gcId.get(j).getGc_id();
                                    break;
                                case 1:
                                    if (tSpylNum != null)
                                        tSpylNum.setText("宝贝:" + gcId.get(j).getStore_goods_total());
                                    spyl_gcid = gcId.get(j).getGc_id();
                                    break;
                                case 2:
                                    if (tGhhzNum != null)
                                        tGhhzNum.setText("宝贝:" + gcId.get(j).getStore_goods_total());
                                    ghhz_gcid = gcId.get(j).getGc_id();
                                    break;
                                case 3:
                                    if (tYybjNum != null)
                                        tYybjNum.setText("宝贝:" + gcId.get(j).getStore_goods_total());
                                    yybj_gcid = gcId.get(j).getGc_id();
                                    break;
                                case 4:
                                    jrkz_gcid = gcId.get(j).getGc_id();
                                    break;
                            }
                        }

                        initOnListener();

                    } else {
                        Log.i("zjz", "goodsActivity解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();

                } finally {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        mRequestQueue.add(jr);
    }

    private void initOnListener() {
        imgShyp.setOnClickListener(this);
        imgSpyl.setOnClickListener(this);
        imgGhhz.setOnClickListener(this);
        imgYybj.setOnClickListener(this);
        imgJrkz.setOnClickListener(this);
    }

    private void setOnListener() {
//        imgBangong.setOnClickListener(this);
        relativeSaomiao.setOnClickListener(this);
        relativeSearch.setOnClickListener(this);
        relativeCart.setOnClickListener(this);
        relativeUpdateMore.setOnClickListener(this);
        relativeWeekMore.setOnClickListener(this);
        imgCard.setOnClickListener(this);
        imgOne.setOnClickListener(this);
        imgQiye.setOnClickListener(this);
        imgRedbag.setOnClickListener(this);
        relaitveQiye.setOnClickListener(this);
        relativeCard.setOnClickListener(this);
        relativeRedbag.setOnClickListener(this);
        relativeOne.setOnClickListener(this);
    }

    private void initView() {


        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                isRefresh = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onLoad();
                    }
                }, 2000);
            }
        });


//        scrollView.initWithContext(activity);
//        scrollView.setPullRefreshEnable(false);
//        scrollView.setPullLoadEnable(false);
//        scrollView.setAutoLoadEnable(false);
//        scrollView.setRefreshTime(Util.getNowTime());
//        scrollView.setIXScrollViewListener(new XScrollView.IXScrollViewListener() {
//
//            @Override
//            public void onRefresh() {
//                isRefresh = true;
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        onLoad();
//                    }
//                }, 2000);
//                Log.i("zjz", "下拉刷新！！");
//            }
//
//            @Override
//            public void onLoadMore() {
//
//            }
//        });
        scrollView.setOnScroll(new MainScrollView.OnScroll() {
            @Override
            public void onScrollListener(int x, int y, int oldx, int oldy) {
                changeTitleColor(y);
            }
        });
//        scrollView.setOnScrollViewListener(new XScrollView.OnScrollViewListener() {
//            @Override
//            public void onScrollListener(int x, int y, int oldx, int oldy) {
//                changeTitleColor(y);
//            }
//        });
    }

    private void onLoad() {
        isFirst = false;
        aCache.remove(TLUrl.getInstance().HWGBANNER);
        aCache.remove(TLUrl.getInstance().HWGHOT);
        aCache.remove(TLUrl.getInstance().HWGWEEK);
        aCache.remove(TLUrl.getInstance().HWGUPDATE);

        if (NetworkUtils.isNetAvailable(getContext())) {
            Log.i("zjz", "网络连接正常");
            initBanner(isRefresh);
            initGoodsGCid();
            initHot(isRefresh);
            setOnListener();
            initInCartNum();
            initWeek(isRefresh);
            initRedBagNum();
//            initUpdate(isRefresh);
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
//        scrollView.stopRefresh();
//        scrollView.stopLoadMore();
    }

    private void changeTitleColor(int y) {
        if (y >= 0 && y <= 255) {
            if (y == 0) {
//                tvSearchHint.setTextColor(Color.parseColor("#eb5041"));
                tvSearchHint.setTextColor(Color.parseColor("#e50042"));
                hwgtitleBar.setBackgroundColor(Color.argb(70, 0, 0, 0));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    StatusBarCompat.compat(activity, Color.argb(90, 0, 0, 0), true);
            } else {
//                hwgtitleBar.setBackgroundColor(Color.argb(y, 235, 80, 65));
                hwgtitleBar.setBackgroundColor(Color.argb(y, 229, 0, 66));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
//                    StatusBarCompat.compat(activity, Color.argb(y, 235, 80, 65), true);
                    StatusBarCompat.compat(activity, Color.argb(y, 229, 0, 66), true);
            }
        } else if (y >= 255) {
            hwgtitleBar.setBackgroundColor(Color.argb(255, 229, 0, 66));
//            hwgtitleBar.setBackgroundColor(Color.argb(255, 235, 80, 65));
//            setSystembarColor(Color.argb(255, 235, 80, 65));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
//                StatusBarCompat.compat(activity, Color.argb(255, 235, 80, 65), true);
                StatusBarCompat.compat(activity, Color.argb(255, 229, 0, 66), true);
            tvSearchHint.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    HWGFragmentAdapter hwgFragmentAdapter;
    FullyGridLayoutManager fullyGridLayoutManager;

    HWGUpdateAdapter hwgUpdateAdapter;

    private void initUpdate(boolean isRefresh) {

        times = new TextView[3];
        times[0] = tHour;
        times[1] = tMinute;
        times[2] = tSecond;
        if (isFirst) {
            mCountDownTimer countDownTimer = new mCountDownTimer(24 * 60 * 60 * 1000, 1000, times);
            countDownTimer.start();
        }
        hwgUpdateAdapter = new HWGUpdateAdapter(this, true);
        fullyGridLayoutManager = new FullyGridLayoutManager(getContext(), 2);

        updateRecyclerView.setFocusable(false);
        updateRecyclerView.setLayoutManager(fullyGridLayoutManager);
        updateRecyclerView.setAdapter(hwgUpdateAdapter);
        //添加分割线
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_size2);
        if (!isRefresh)
            updateRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        initUpdateDatas(isRefresh);
    }

    private void initUpdateDatas(boolean isRefresh) {
        hwgUpdateAdapter.getDatas().clear();
        JSONObject updateObject = aCache.getAsJSONObject(TLUrl.getInstance().HWGUPDATE);
        if (updateObject != null && !isRefresh) {
            Log.i("zjz", "有本地update");
            try {
                initUpdateObject(updateObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_update + "1", null, new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (aCache.getAsJSONObject(TLUrl.getInstance().HWGUPDATE) == null) {
                                    aCache.put(TLUrl.getInstance().HWGUPDATE, object, 24 * 60 * 60);
                                }
                                initUpdateObject(object);
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

    }

    private void initUpdateObject(JSONObject object) throws JSONException {
        if (object.getInt("code") == 200) {
            Log.i("zjz", "goodsupdate:连接成功");
            JSONObject jsonObject = object.getJSONObject("datas");
            JSONArray jsonArray = jsonObject.getJSONArray("goods_list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object1 = jsonArray.getJSONObject(i);
                Goods g = new Goods();
                g.setGc_id(object1.optString("gc_id"));
                g.setGoods_id(object1.optString("goods_id"));
                g.setTitle(object1.optString("goods_name"));
                g.setMoney(object1.optDouble("goods_price"));
                g.setPicarr(object1.optString("goods_image_url"));
                hwgUpdateAdapter.getDatas().add(g);
            }
            hwgUpdateAdapter.notifyDataSetChanged();
            relativeUpdateMore.setVisibility(View.VISIBLE);
        } else {
            Log.i("zjz", "recycler:解析失败");
        }
    }

    private void initWeek(boolean isRefresh) {

        hwgFragmentAdapter = new HWGFragmentAdapter(activity,this, true);
        fullyGridLayoutManager = new FullyGridLayoutManager(getContext(), 2);

        weekRecyclerView.setFocusable(false);
        weekRecyclerView.setLayoutManager(fullyGridLayoutManager);
        weekRecyclerView.setAdapter(hwgFragmentAdapter);
        //添加分割线
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_size2);
        if (!isRefresh)
            weekRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        initDatas(isRefresh);
    }

    private void initDatas(boolean isRefresh) {
        hwgFragmentAdapter.getDatas().clear();
        JSONObject weekObject = aCache.getAsJSONObject(TLUrl.getInstance().HWGWEEK);
        if (weekObject != null && !isRefresh) {
            Log.i("zjz", "有本地week");
            try {
                initWeekObject(weekObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_week + "1", null, new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (aCache.getAsJSONObject(TLUrl.getInstance().HWGWEEK) == null) {
                                    aCache.put(TLUrl.getInstance().HWGWEEK, object, 24 * 60 * 60);
                                }
                                initWeekObject(object);
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

    }

    private void initWeekObject(JSONObject object) throws JSONException {
        if (object.getInt("code") == 200) {
            Log.i("zjz", "goodsweek:连接成功");
            JSONObject jsonObject = object.getJSONObject("datas");
            JSONArray jsonArray = jsonObject.getJSONArray("goods_list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object1 = jsonArray.getJSONObject(i);
                Goods g = new Goods();
                g.setGc_id(object1.optString("gc_id"));
                g.setTitle(object1.optString("goods_name"));
                g.setGoods_id(object1.optString("goods_id"));
                g.setMoney(object1.optDouble("goods_price"));
                g.setPicarr(object1.optString("goods_image_url"));
                hwgFragmentAdapter.getDatas().add(g);
            }
            hwgFragmentAdapter.notifyDataSetChanged();
            relativeWeekMore.setVisibility(View.VISIBLE);
        } else {
            Log.i("zjz", "recycler:解析失败");
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.relative_search:
//                intent = new Intent(getActivity(), AllGoodsActivity.class);
//                intent.putExtra("search", true);
//                getActivity().startActivity(intent);
                intent = new Intent(getActivity(), GoodsSearchActivity2.class);
                startActivity(intent);
                break;
            case R.id.relative_cart:
                if (MyApplication.getInstance().self == null) {
                    intent = new Intent(activity, WXEntryActivity.class);
                    activity.startActivity(intent);
                    return;
                }
                intent = new Intent(activity, CartActivity2.class);
                intent.putExtra("store_id","");
                activity.startActivity(intent);
                break;
            case R.id.img_shyp:
                intent = new Intent(getActivity(), AllGoodsActivity.class);
                intent.putExtra("gc_id", shyp_gcid);
                intent.putExtra("title", gcId.get(0).getTitle());
                getActivity().startActivity(intent);
                break;
            case R.id.img_spyl:
                intent = new Intent(getActivity(), AllGoodsActivity.class);
                intent.putExtra("gc_id", spyl_gcid);
                intent.putExtra("title", gcId.get(1).getTitle());
                getActivity().startActivity(intent);
                break;
            case R.id.img_ghhz:
                intent = new Intent(getActivity(), AllGoodsActivity.class);
                intent.putExtra("gc_id", ghhz_gcid);
                intent.putExtra("title", gcId.get(2).getTitle());
                getActivity().startActivity(intent);
                break;
            case R.id.img_yybj:
                intent = new Intent(getActivity(), AllGoodsActivity.class);
                intent.putExtra("gc_id", yybj_gcid);
                intent.putExtra("title", gcId.get(3).getTitle());
                getActivity().startActivity(intent);
                break;
            case R.id.img_jrkz:
                intent = new Intent(getActivity(), AllGoodsActivity.class);
                intent.putExtra("gc_id", jrkz_gcid);
                intent.putExtra("title", gcId.get(4).getTitle());
                getActivity().startActivity(intent);
                break;
            case R.id.relative_week_more:
                intent = new Intent(getActivity(), AllGoodsActivity.class);
                intent.putExtra("isWeek", true);
                intent.putExtra("title", "每周上新");
                getActivity().startActivity(intent);
                break;
            case R.id.relative_update_more:
                intent = new Intent(getActivity(), AllGoodsActivity.class);
                intent.putExtra("isUpdate", true);
                intent.putExtra("title", "预售上新");
                getActivity().startActivity(intent);
                break;
            case R.id.relative_saomiao:
//                intent = new Intent(getActivity(), RechargeActivity.class);
//                startActivity(intent);
                startActivity(new Intent(getActivity(), MipcaActivityCapture.class));
                break;
            case R.id.img_card:
                intent = new Intent(getActivity(), RechargeActivity.class);
                startActivity(intent);
                break;
            case R.id.img_redbag:
                intent = new Intent(getActivity(), RedBagActivity.class);
                startActivity(intent);
                break;
            case R.id.img_one:
                break;
            case R.id.img_qiye:
                break;
            case R.id.relative_card:
                intent = new Intent(getActivity(), RechargeActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_redbag:
                intent = new Intent(getActivity(), RedBagActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_one:
                break;
            case R.id.relaitve_qiye:
                break;
        }
    }


    @Override
    public void onDestroyView() {

        super.onDestroyView();
        myBroadCastReceiver.unRegister();
        ButterKnife.reset(this);
        isDestory = true;
    }

    @Override
    public void onItemRootViewClick(int position) {
        Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
        intent.putExtra("sid", hwgFragmentAdapter.getDatas().get(position).getGoods_id());
        Log.i("zjz", "sid=" + hwgFragmentAdapter.getDatas().get(position).getGoods_id());
        intent.putExtra("pic", hwgFragmentAdapter.getDatas().get(position).getPicarr());
        startActivity(intent);
    }

    @Override
    public void onItemUpdateViewClick(int position) {
        Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
        intent.putExtra("sid", hwgUpdateAdapter.getDatas().get(position).getGoods_id());
        Log.i("zjz", "sid=" + hwgUpdateAdapter.getDatas().get(position).getGoods_id());
        intent.putExtra("pic", hwgUpdateAdapter.getDatas().get(position).getPicarr());
        startActivity(intent);
    }
}
