package com.abcs.huaqiaobang.fragment;

import android.content.Context;
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
import android.support.v7.widget.LinearLayoutManager;
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

import com.abcs.haiwaigou.activity.ExchangePointActivity;
import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.activity.GoodsNewsActivity;
import com.abcs.haiwaigou.activity.GoodsSearchActivity2;
import com.abcs.haiwaigou.activity.RechargeActivity;
import com.abcs.haiwaigou.activity.RedBagActivity;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.view.CircleIndicator;
import com.abcs.haiwaigou.view.zjzbanner.LMBanners;
import com.abcs.haiwaigou.view.zjzbanner.adapter.LBaseAdapter;
import com.abcs.haiwaigou.view.zjzbanner.transformer.TransitionEffect;
import com.abcs.haiwaigou.view.zjzbanner.utils.ScreenUtils;
import com.abcs.haiwaigou.yyg.activity.YYGActivity;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.ExchangeRateActivity;
import com.abcs.huaqiaobang.activity.SpecialActivity;
import com.abcs.huaqiaobang.adapter.GalleryAdapter;
import com.abcs.huaqiaobang.main.MainActivity;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.model.Rate;
import com.abcs.huaqiaobang.model.StatusBarCompat;
import com.abcs.huaqiaobang.presenter.LoadInterface;
import com.abcs.huaqiaobang.presenter.MainGoods;
import com.abcs.huaqiaobang.tljr.news.HuanQiuShiShi;
import com.abcs.huaqiaobang.tljr.news.NewsActivity;
import com.abcs.huaqiaobang.tljr.news.SortNewsActivity;
import com.abcs.huaqiaobang.tljr.news.bean.News;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.AutoScrollTextView;
import com.abcs.huaqiaobang.view.MainScrollView;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StreamCorruptedException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/4/12.
 */
public class MainFragment2 extends Fragment implements LoadInterface {

    @InjectView(R.id.tv_huilu1)
    TextView tvHuilu1;
    @InjectView(R.id.tv_price1)
    TextView tvPrice1;
    @InjectView(R.id.tv_huilu2)
    TextView tvHuilu2;
    @InjectView(R.id.tv_price2)
    TextView tvPrice2;
    @InjectView(R.id.tv_rmb2)
    TextView tvRmb2;
    @InjectView(R.id.tv_huilu3)
    TextView tvHuilu3;

    @InjectView(R.id.tv_price3)
    TextView tvPrice3;
    @InjectView(R.id.tv_huilu4)
    TextView tvHuilu4;
    @InjectView(R.id.tv_price4)
    TextView tvPrice4;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.autoScrollTextView)
    AutoScrollTextView autoScrollTextView;
    @InjectView(R.id.hot_indicator)
    CircleIndicator hotIndicator;
    @InjectView(R.id.relative_card)
    RelativeLayout relativeCard;
    @InjectView(R.id.relative_redbag)
    RelativeLayout relativeRedbag;
    @InjectView(R.id.relative_one)
    RelativeLayout relativeOne;
    @InjectView(R.id.relaitve_qiye)
    RelativeLayout relaitveQiye;
    @InjectView(R.id.banners)
    LMBanners banners;


    private View view;
    @InjectView(R.id.bannerViewPager)
    ViewPager bannerViewPager;
    @InjectView(R.id.hotBannerViewPager)
    ViewPager hotBannerViewPager;
    @InjectView(R.id.indicator)
    CircleIndicator indicator;
    @InjectView(R.id.titlebar)
    RelativeLayout titlebar;
    @InjectView(R.id.scrollView)
    MainScrollView scrollView;
    @InjectView(R.id.hotrecyclerView)
    RecyclerView hotrecyclerView;
    //    @InjectView(R.id.img_hotGood)
//    ImageView imgHotGood;
    @InjectView(R.id.news_layout)
    LinearLayout newsLayout;

    private Map<TextView, TextView> mapHuiLv = new HashMap<>();
    private List<String> bannerList = new ArrayList<>();
    private List<ImageView> bannerView = new ArrayList<>();
    private List<String> bannerString = new ArrayList<>();
    private List<ImageView> hotBanner = new ArrayList<>();
    private Handler mHandler = new Handler();

    private MainActivity activity;
    private boolean refresh = false;


    private List<Goods> mGoods;
    public static List<News> mNews;

    LayoutInflater mInflater;
    private GalleryAdapter galleryAdapter;
    private RequestQueue mRequestQueue;
    private ArrayList<Rate> rates;
    private MainGoods mainGoods;
    private ArrayList<String> autoList = new ArrayList<String>();
    private int index = 0;
    private Goods hotSpecial;
    private boolean isDestory = false;
    public static TextView t_news;
    private MyBroadCastReceiver myBroadCastReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main2, null);
        ButterKnife.inject(this, view);
        activity = (MainActivity) getActivity();

        myBroadCastReceiver = new MyBroadCastReceiver(activity, updateUI);
        myBroadCastReceiver.register();
        mRequestQueue = Volley.newRequestQueue(activity);
        mInflater = inflater;
        mapHuiLv.put(tvHuilu1, tvPrice1);
        mapHuiLv.put(tvHuilu2, tvPrice2);
        mapHuiLv.put(tvHuilu3, tvPrice3);
        mapHuiLv.put(tvHuilu4, tvPrice4);
     //   new AutoLogin(activity);
        rates = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////            window = getWindow();
////            // Translucent status bar
////            window.setFlags(
////                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
////                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) titlebar.getLayoutParams();
            params.setMargins(0, ((MainActivity) getActivity()).getStatusBarHeight(), 0, 0);
            titlebar.setLayoutParams(params);
        }

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh = true;

                hotBanner.clear();
                rates.clear();
                getUiData();
                mainGoods.loadData(refresh);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        refresh = false;
                    }
                }, 3000);
            }
        });

        autoScrollTextView.setComplete(new Complete() {

            @Override
            public void complete() {
                index++;
                if (index > autoList.size() - 1)
                    index = 0;
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        autoScrollTextView.setText(autoList.get(index));
                        autoScrollTextView.init(activity.getWindowManager());
                        autoScrollTextView.startScroll();
                    }
                }, 3000);
            }
        });

        getUiData();

//        initHotBanner();
        scrollView.setOnScroll(new MainScrollView.OnScroll() {
            @Override
            public void onScrollListener(int x, int y, int oldx, int oldy) {
                changeBarColor(y);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hotrecyclerView.setLayoutManager(layoutManager);


        mGoods = new ArrayList<>();
        mNews = new ArrayList<>();
        //加载数据
        mainGoods = new MainGoods(this);
        mainGoods.loadData(refresh);
        t_news = (TextView) view.findViewById(R.id.t_news);
        if (Util.preference.getString("goods_news", "").length() != 0) {
            t_news.setVisibility(View.VISIBLE);
        }
        return view;
    }

    MyBroadCastReceiver.UpdateUI updateUI = new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

        }

        @Override
        public void updateCarNum(Intent intent) {

        }

        @Override
        public void updateCollection(Intent intent) {
            if (intent.getStringExtra("type").equals(MyUpdateUI.GOODSNEWS)) {
//                initListView();
                t_news.setVisibility(View.VISIBLE);
                Log.i("zjz", "有新消息");
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initHotBanner() {
        Log.i("zjz", "hotBanner_size=" + hotBanner.size());
        hotBannerViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return hotBanner.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                container.addView(hotBanner.get(position));
                return hotBanner.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(hotBanner.get(position));
            }

        });
        hotIndicator.setViewPager(hotBannerViewPager);
        hotBannerViewPager.setOnTouchListener(new View.OnTouchListener() {
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


    private void changeBarColor(int y) {
        if (y >= 0 && y <= 255) {
//            titlebar.setBackgroundColor(Color.argb(y, 235, 80, 65));
            titlebar.setBackgroundColor(Color.argb(y, 229, 0, 66));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                StatusBarCompat.compat(getActivity(), Color.argb(y, 229, 0, 66), true);
        } else if (y > 255) {
//            titlebar.setBackgroundColor(Color.argb(255, 235, 80, 65));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
//                StatusBarCompat.compat(getActivity(), Color.parseColor("#f22828"), true);
                StatusBarCompat.compat(getActivity(), Color.parseColor("#e50042"), true);
        }
    }

    private void getUiData() {
//        ProgressDlgUtil.showProgressDlg("", activity);
        LogUtil.e("getindexinfo", "method=getindexinfo");

        if (!mainGoods.isCacheDataFailure("bannerui.txt") && !refresh) {
            try {

                FileInputStream fileInputStream = activity.openFileInput("bannerui.txt");

                BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
                String str = null;
                StringBuffer stringBuffer = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                    stringBuffer.append("\n");
                }
                String json = stringBuffer.toString();

                initUi(new JSONObject(json));
                br.close();
                fileInputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {

            HttpRequest.sendPost(TLUrl.getInstance().URL_indexPage, "method=getindexinfo",
                    new HttpRevMsg() {

                        @Override
                        public void revMsg(String msg) {
                            LogUtil.e("getindexinfo", msg);
                            if (msg.equals("")) {
//                            getUiData();
                                return;
                            }
//                        ProgressDlgUtil.stopProgressDlg();
                            try {
                                JSONObject jsonObject = new JSONObject(msg);
                                if (jsonObject.getInt("status") == 1) {


                                    try {
                                        FileOutputStream fileOutputStream = activity.openFileOutput("bannerui.txt", Context.MODE_PRIVATE);
                                        BufferedWriter rw = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
                                        rw.write(jsonObject.getJSONObject("msg").toString());
                                        rw.close();
                                        fileOutputStream.close();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    initUi(jsonObject.getJSONObject("msg"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });

        }
        final ImageView imageViewhot1 = new ImageView(getContext());
        final ImageView imageViewhot2 = new ImageView(getContext());

        if (!"".equals(Util.preference.getString("hotspecial", "")) && !refresh) {

            hotSpecial = new Goods();
            try {
                JSONObject image = new JSONObject(Util.preference.getString("hotspecial", ""));
                hotSpecial.setKeywords(image.optString("data"));
                hotSpecial.setPicarr(image.optString("image"));
                //                ImageLoader.getInstance().displayImage(hotSpecial.getPicarr(), imgHotGood, Options.getListOptions());


//                ImageView imageView = new ImageView(getContext());

                imageViewhot1.setScaleType(ImageView.ScaleType.FIT_XY);
                Log.i("zjz", "hot_url=" + hotSpecial.getPicarr());
//                Util.setImage(hotSpecial.getPicarr(), imageViewhot1, mHandler);
                ImageLoader.getInstance().displayImage(hotSpecial.getPicarr(), imageViewhot1, Options.getHDOptions());
//                LoadPicture loadPicture=new LoadPicture();
//                loadPicture.initPicture(imageViewhot1, hotSpecial.getPicarr());
                imageViewhot1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent hotIntent = new Intent(activity, SpecialActivity.class);
                        hotIntent.putExtra("special_id", hotSpecial.getKeywords());
                        hotIntent.putExtra("picture", hotSpecial.getPicarr());
                        startActivity(hotIntent);
                    }
                });
                hotBanner.add(imageViewhot1);

//                ImageView imageView1 = new ImageView(getContext());

                imageViewhot2.setScaleType(ImageView.ScaleType.FIT_XY);
                imageViewhot2.setImageResource(R.drawable.img_quanbanner);
                imageViewhot2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        if (MyApplication.getInstance().getMykey() == null) {
                            intent = new Intent(activity, WXEntryActivity.class);
                            startActivity(intent);
                        } else {
                            intent = new Intent(activity, ExchangePointActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                hotBanner.add(imageViewhot2);
                initHotBanner();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {

            HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_home, null, new HttpRevMsg() {
                @Override
                public void revMsg(final String msg) {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject(msg);
                                if (object.getInt("code") == 200) {
                                    Log.i("zjz", "banner:连接成功");

                                    JSONArray array = object.getJSONArray("datas");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object3 = array.getJSONObject(i);
                                        Iterator key = object3.keys();
                                        if (key.hasNext() && key.next().equals("home1")) {
                                            JSONObject image = object3.optJSONObject("home1");
                                            if (image.optString("title").contains("首页")) {

                                                Util.preference.edit().putString("hotspecial", image.toString()).commit();
                                                hotSpecial = new Goods();
                                                hotSpecial.setKeywords(image.optString("data"));
                                                hotSpecial.setPicarr(image.optString("image"));
                                                Log.i("zjz", "hot_url=" + hotSpecial.getPicarr());
                                                hotSpecial.setGoods_id(i + "");
//                                                ImageLoader.getInstance().displayImage(hotSpecial.getPicarr(), imgHotGood, Options.getListOptions());

//                                                ImageView imageView = new ImageView(getContext());
                                                imageViewhot1.setScaleType(ImageView.ScaleType.FIT_XY);
                                                ImageLoader.getInstance().displayImage(hotSpecial.getPicarr(), imageViewhot1, Options.getHDOptions());
//                                                LoadPicture loadPicture=new LoadPicture();
//                                                loadPicture.initPicture(imageViewhot1, hotSpecial.getPicarr());
//                                                Util.setImage(hotSpecial.getPicarr(), imageViewhot1, mHandler);
                                                imageViewhot1.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent hotIntent = new Intent(activity, SpecialActivity.class);
                                                        hotIntent.putExtra("special_id", hotSpecial.getKeywords());
                                                        hotIntent.putExtra("picture", hotSpecial.getPicarr());
                                                        startActivity(hotIntent);
                                                    }
                                                });
                                                hotBanner.add(imageViewhot1);
//                                                ImageView imageView1 = new ImageView(getContext());
                                                imageViewhot2.setScaleType(ImageView.ScaleType.FIT_XY);
                                                imageViewhot2.setImageResource(R.drawable.img_quanbanner);
                                                imageViewhot2.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent;
                                                        if (MyApplication.getInstance().getMykey() == null) {
                                                            intent = new Intent(activity, WXEntryActivity.class);
                                                            startActivity(intent);
                                                        } else {
                                                            intent = new Intent(activity, ExchangePointActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                });
                                                hotBanner.add(imageViewhot2);
                                                initHotBanner();
                                            }
                                            Log.i("zjz", "pic=" + image.optString("image"));
                                        }
                                    }
                                } else {
                                    Log.i("zjz", "banner:解析失败");
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


    }

    private void initUi(final JSONObject jsonObject) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                try {
                    JSONArray array = jsonObject.getJSONArray("exchangerage");
                    int count = 0;
                    Set<Map.Entry<TextView, TextView>> entries = mapHuiLv.entrySet();
                    Iterator<Map.Entry<TextView, TextView>> iterator = entries.iterator();
                    for (int i = 0; i < array.length(); i++) {
                        Rate rate = new Rate();
                        String currencyName = array.getJSONObject(i).optString("currencyName");
                        String key = array.getJSONObject(i).optString("key");
                        double exchangeRate = array.getJSONObject(i).optDouble("exchangeRate");
                        rate.setName(currencyName);
                        rate.setRate(exchangeRate);
                        rate.setShorthandName(key);
                        rate.setCountryUrl(array.getJSONObject(i).optString("icon"));
                        rates.add(rate);
                        if (iterator.hasNext()) {
                            Map.Entry<TextView, TextView> next = iterator.next();
                            next.getKey().setText(currencyName);
                            next.getValue().setText(exchangeRate + "");
                        }
                    }
//                    hongbao_view.findViewById(R.id.main_grp_award).setVisibility(
//                            jsonObject.getBoolean("showRed") ? View.VISIBLE
//
                    com.alibaba.fastjson.JSONArray jsonArray = com.alibaba.fastjson.JSONArray
                            .parseArray(jsonObject.getString("broadcasts"));
                    for (int i = 0; i < jsonArray.size(); i++) {
                        autoList.add(jsonArray.getString(i));
                        LogUtil.e("getindexinfo", jsonArray.getString(i));
                    }
                    if (autoList.size() > 0) {
                        autoScrollTextView.setText(autoList.get(index));
                        autoScrollTextView.init(activity.getWindowManager());
                        autoScrollTextView.startScroll();
                    }

                    com.alibaba.fastjson.JSONArray urls = com.alibaba.fastjson.JSONArray
                            .parseArray(jsonObject.getString("urls"));
                    Log.i("zjz","url="+urls);
                    if (urls.size() > 0) {
                        bannerView.clear();
                        bannerString.clear();
                        for (int i = 0; i < urls.size(); i++) {
                            ImageView view = new ImageView(getContext());
                            view.setScaleType(ImageView.ScaleType.FIT_XY);
//            Util.setImage(goodsImgs.get(i).getPicarr(), view, handler);
                            String string = urls.getString(i);
                            Util.setImage(urls.getString(i), view, mHandler);
//                            ImageLoader.getInstance().displayImage(urls.getString(i),view,Options.getHDOptions());
                            bannerString.add(urls.getString(i));
                            bannerView.add(view);
                        }

//                        initBannerViewPager();
                        initBanners();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initBanners() {
        Log.i("zjz", "bannerString.size=" + bannerString.size());
        //设置Banners高度
        banners.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ScreenUtils.dip2px(activity, 230)));
        //本地用法
        banners.setAdapter(new UrlImgAdapter(activity), bannerString);
        //网络图片
//        mLBanners.setAdapter(new UrlImgAdapter(MainActivity.this), networkImages);
        //参数设置
        banners.setAutoPlay(true);//自动播放
        banners.setVertical(false);//是否可以垂直
        banners.setScrollDurtion(500);//两页切换时间
        banners.setCanLoop(true);//循环播放
        banners.setSelectIndicatorRes(R.drawable.img_hwg_indicator_select);//选中的原点
        banners.setUnSelectUnIndicatorRes(R.drawable.img_hwg_indicator_unselect);//未选中的原点
//        mLBanners.setHoriZontalTransitionEffect(TransitionEffect.Default);//选中喜欢的样式
//        banners.setHoriZontalCustomTransformer(new ParallaxTransformer(R.id.id_image));//自定义样式
        banners.setHoriZontalTransitionEffect(TransitionEffect.Alpha);//Alpha
        banners.setDurtion(5000);//切换时间
        if (bannerString.size() == 1) {

            banners.hideIndicatorLayout();//隐藏原点
        } else {
            banners.showIndicatorLayout();//显示原点
        }
        banners.setIndicatorPosition(LMBanners.IndicaTorPosition.BOTTOM_MID);//设置原点显示位置

    }

    class UrlImgAdapter implements LBaseAdapter<String> {
        private Context mContext;

        public UrlImgAdapter(Context context) {
            mContext = context;
        }

        @Override
        public View getView(final LMBanners lBanners, final Context context, int position, String data) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.banner_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.id_image);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageLoader.getInstance().displayImage(data, imageView, Options.getHDOptions());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        MainActivity.this.startActivity(new Intent(MainActivity.this,SeconedAc.class));
                }
            });
            return view;
        }

    }

    private void initBannerViewPager() {
        Log.i("zjz", "banner_size=" + bannerView.size());
        pagerAdapter.notifyDataSetChanged();
        bannerViewPager.setAdapter(pagerAdapter);
        indicator.setViewPager(bannerViewPager);
        bannerViewPager.setOnTouchListener(new View.OnTouchListener() {
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
        if (!refresh)
            mHandler.post(runnable);

    }

    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return bannerView.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            container.addView(bannerView.get(position));
            return bannerView.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(bannerView.get(position));
        }

    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isDestory) {
                if (bannerViewPager.getCurrentItem() >= bannerView.size() - 1) {
                    bannerViewPager.setCurrentItem(0);
                } else {
                    bannerViewPager.setCurrentItem(bannerViewPager.getCurrentItem() + 1);
                }

                mHandler.postDelayed(runnable, 5000);
            }

        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        myBroadCastReceiver.unRegister();
        banners.clearImageTimerTask();
        isDestory = true;
    }


    @Override
    public void onPause() {
        super.onPause();
        banners.stopImageTimerTask();
    }

    @Override
    public void onResume() {
        super.onResume();
        banners.startImageTimerTask();
    }

    public View getNewsView(final int position) {

        View view = mInflater.inflate(R.layout.mainfragment_news_item, null);
        ImageView imgNews = (ImageView) view.findViewById(R.id.img_news);
        TextView tvNewsTitle = (TextView) view.findViewById(R.id.tv_newsTitle);
        TextView tvNewsSource = (TextView) view.findViewById(R.id.tv_newsSource);
        TextView tvUpDateTime = (TextView) view.findViewById(R.id.tv_upDateTime);

        ImageLoader.getInstance().displayImage(mNews.get(position).getpUrl(), imgNews, Options.getListOptions());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        tvNewsSource.setText(mNews.get(position).getSource());
        tvNewsTitle.setText(mNews.get(position).getTitle());
        tvUpDateTime.setText(Util.getDateOnlyDay(mNews.get(position).getTime()));

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (!mNews.get(position).isHaveSee() && !HuanQiuShiShi.id.contains(mNews.get(position).getId())) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("time", mNews.get(position).getTime());
                        obj.put("id", mNews.get(position).getId());
                        obj.put("species", mNews.get(position).getSpecial());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (HuanQiuShiShi.readId == null) {
                        HuanQiuShiShi.readId = new JSONArray();
                    }
                    HuanQiuShiShi.readId.put(obj);
                    HuanQiuShiShi.id.add(mNews.get(position).getId());
                    HuanQiuShiShi.updataUserIsRead();
                    LogUtil.i("read", "isReadNewsId :" + HuanQiuShiShi.readId.toString());
                }

                Intent intent = new Intent(activity, NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);

                bundle.putString("SYNews", "");


//                bundle.putSerializable("news1", MainFragment.this.news);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void loadData(List mData) {

        if (refresh) {
            mGoods.clear();
            swipeRefreshLayout.setRefreshing(false);
            refresh = false;
        }
        mGoods.addAll(mData);
        if (galleryAdapter != null) {
            galleryAdapter.notifyDataSetChanged();

        } else {
            galleryAdapter = new GalleryAdapter(activity, mGoods);
            hotrecyclerView.setAdapter(galleryAdapter);
            galleryAdapter.setOnItemClickListner(new GalleryAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                    intent.putExtra("sid", mGoods.get(position).getGoods_id());
                    intent.putExtra("pic", mGoods.get(position).getGoods_url());
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    public void loadNewsData(List mData) {


        if (newsLayout.getChildCount() > 2) {
            newsLayout.removeViews(1, mNews.size());
            mNews.clear();
        }
        mNews.addAll(mData);

        for (int i = 0; i < mNews.size(); i++) {
            newsLayout.addView(getNewsView(i), i + 1);
        }

    }

    @OnClick({R.id.more_goods, R.id.more_news, R.id.huilvLayout, R.id.relative_search, R.id.relative_message, R.id.relative_card, R.id.relative_redbag, R.id.relative_one})
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.more_goods:
                activity.trun2Fragment(1);
                break;
            case R.id.more_news:

                Intent it = new Intent(activity, SortNewsActivity.class);
//                it.putExtra("sp", "hq_d");
//                it.putExtra("defaultPicture", "default");
//                activity.startActivity(it);
                activity.trun2Fragment(2);
                break;

//            case R.id.img_hotGood:
//
//                Intent hotIntent = new Intent(activity, SpecialActivity.class);
//                hotIntent.putExtra("special_id", hotSpecial.getKeywords());
//                hotIntent.putExtra("picture", hotSpecial.getPicarr());
//                startActivity(hotIntent);
//
//                break;

            case R.id.huilvLayout:
                intent = new Intent(activity, ExchangeRateActivity.class);
                intent.putParcelableArrayListExtra("rates", rates);
                startActivity(intent);
                break;
            case R.id.img_msg:
//                intent = new Intent(activity, BangActivity.class);
//                intent.putParcelableArrayListExtra("rates", rates);
//                startActivity(intent);

                break;
            case R.id.relative_message:
                go2Message();
                break;
            case R.id.relative_search:
                intent = new Intent(activity, GoodsSearchActivity2.class);
                startActivity(intent);
                break;
            case R.id.relative_card:
                intent = new Intent(getActivity(), RechargeActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_redbag:
                intent = new Intent(getActivity(), RedBagActivity.class);
                intent.putExtra("red_code", "");
                startActivity(intent);
                break;
            case R.id.relative_one:
                if (!Util.isYYGLogin) {
                    if (MyApplication.getInstance().self != null)
                        loginForYYG();
                }
                intent = new Intent(getActivity(), YYGActivity.class);
                startActivity(intent);
                break;
        }
    }


    private void loginForYYG() {
//        ModelUser snsUser= Thinksns.getMy();
//        Log.i("zjz","snsUser_id="+snsUser.getUid());
        HttpRequest.sendPost(TLUrl.getInstance().URL_yyg_login, "nickname=" + MyApplication.getInstance().self.getNickName() + "&userId="
                + MyApplication.getInstance().self.getId() + "&avator=" + MyApplication.getInstance().self.getAvatarUrl() + "&userName=" + URLEncoder.encode(MyApplication.getInstance().self.getUserName())
                +"&alias="+MyApplication.getInstance().self.getId() , new HttpRevMsg() {
            @Override
            public void revMsg(String msg) {
                if (msg == null) {
                    return;
                }
                Log.i("zjz", "login_for_yyg=" + msg);
                try {
                    JSONObject json = new JSONObject(msg);
                    if (json.optInt("status") == 1) {
                        Util.isYYGLogin = true;
                        Log.i("zjz", "mainfragment1_YYG");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
    }

    private void go2Message() {
        Intent intent;
        if (MyApplication.getInstance().self == null) {

            if (MyApplication.getInstance().getMykey() == null) {
                intent = new Intent(activity, WXEntryActivity.class);
                startActivity(intent);
            } else {
                intent = new Intent(activity, ExchangePointActivity.class);
                intent.putExtra("position", 0);
                startActivity(intent);
            }
            return;
        } else {
            intent = new Intent(activity, GoodsNewsActivity.class);
            startActivity(intent);
        }
//        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_goods_message ,"member_name="+MyApplication.getInstance().self.getUserName(), new HttpRevMsg() {
//            @Override
//            public void revMsg(final String msg) {
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            JSONObject object = new JSONObject(msg);
//                            Log.i("zjz","message_msg="+msg);
//
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            Log.i("zjz", e.toString());
//                            Log.i("zjz", msg);
//                            e.printStackTrace();
//                            ProgressDlgUtil.stopProgressDlg();
//                        } finally {
//                            ProgressDlgUtil.stopProgressDlg();
//                        }
//                    }
//                });
//
//            }
//        });
    }


}
