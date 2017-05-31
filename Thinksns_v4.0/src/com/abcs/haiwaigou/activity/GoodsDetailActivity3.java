package com.abcs.haiwaigou.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.CommentFragment;
import com.abcs.haiwaigou.fragment.DetailFragment;
import com.abcs.haiwaigou.fragment.adapter.CFViewPagerAdapter;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.utils.MyViewAnimUtil;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.haiwaigou.view.CircleIndicator;
import com.abcs.haiwaigou.view.SlidingMenu;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.tljr.news.ShowWebImageActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.huaqiaobang.wxapi.official.share.ShareQQPlatform;
import com.abcs.huaqiaobang.wxapi.official.share.ShareWeiXinPlatform;
import com.abcs.huaqiaobang.wxapi.official.share.ShareWeiboPlatform;
import com.abcs.huaqiaobang.wxapi.official.share.util.ShareContent;
import com.astuetz.PagerSlidingTabStrip;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GoodsDetailActivity3 extends BaseFragmentActivity implements View.OnClickListener, IWeiboHandler.Response {


    public ShareWeiXinPlatform shareWeiXinPlatform;
    MyBroadCastReceiver myBroadCastReceiver;
    @InjectView(R.id.img_goods)
    ImageView imgGoods;
    @InjectView(R.id.tljr_viewpager)
    ViewPager tljrViewpager;
    @InjectView(R.id.linear_viewpager)
    LinearLayout linearViewpager;
    @InjectView(R.id.indicator)
    CircleIndicator indicator;
    @InjectView(R.id.img_bottom)
    ImageView imgBottom;
    @InjectView(R.id.relative_container)
    RelativeLayout relativeContainer;
    @InjectView(R.id.t_goods_name)
    TextView tGoodsName;
    @InjectView(R.id.t_goods_jingle)
    TextView tGoodsJingle;
    @InjectView(R.id.t_goods_money)
    TextView tGoodsMoney;
    @InjectView(R.id.t_goods_old_money)
    TextView tGoodsOldMoney;
    @InjectView(R.id.t_deliver_add)
    TextView tDeliverAdd;
    @InjectView(R.id.linear_goods_detail)
    LinearLayout linearGoodsDetail;
    @InjectView(R.id.img_bottom2)
    ImageView imgBottom2;
    @InjectView(R.id.t_freight)
    TextView tFreight;
    @InjectView(R.id.relative_freight)
    RelativeLayout relativeFreight;
    @InjectView(R.id.t_storage)
    TextView tStorage;
    @InjectView(R.id.relative_storage)
    RelativeLayout relativeStorage;
    @InjectView(R.id.t_msales)
    TextView tMsales;
    @InjectView(R.id.relative_msales)
    RelativeLayout relativeMsales;
    @InjectView(R.id.t_select)
    TextView tSelect;
    @InjectView(R.id.btn_cart_reduce)
    Button btnCartReduce;
    @InjectView(R.id.btn_cart_num_edit)
    EditText btnCartNumEdit;
    @InjectView(R.id.btn_cart_add)
    Button btnCartAdd;
    @InjectView(R.id.linearLayout1)
    LinearLayout linearLayout1;
    @InjectView(R.id.relative_select)
    RelativeLayout relativeSelect;
    @InjectView(R.id.img_top3)
    ImageView imgTop3;
    @InjectView(R.id.linear_baozhang)
    LinearLayout linearBaozhang;
    @InjectView(R.id.img_more)
    ImageView imgMore;
    @InjectView(R.id.comment_recyclerView)
    RecyclerView commentRecyclerView;
    @InjectView(R.id.t_message)
    TextView tMessage;
    @InjectView(R.id.linear_null)
    LinearLayout linearNull;
    @InjectView(R.id.relative_more)
    LinearLayout relativeMore;
    @InjectView(R.id.linear_goods)
    LinearLayout linearGoods;
    @InjectView(R.id.img_nodata)
    ImageView imgNodata;
    @InjectView(R.id.t_text)
    TextView tText;
    @InjectView(R.id.relative_null)
    RelativeLayout relativeNull;
    @InjectView(R.id.comment_tabs)
    PagerSlidingTabStrip commentTabs;
    @InjectView(R.id.linear_tab)
    LinearLayout linearTab;
    @InjectView(R.id.seperate_line)
    View seperateLine;
    @InjectView(R.id.comment_pager)
    ViewPager commentPager;
    @InjectView(R.id.expanded_menu)
    SlidingMenu expandedMenu;
    private View view;
    MyViewAnimUtil myViewAnimUtil;
    public Handler handler = new Handler();
    private String sid;
    String pic;
    String photo_url;
    String detail_url;
    FrameLayout animation_viewGroup;
    public static ImageView shopcar;
    public static TextView car_num;
    public int num = 1;
    public String buynum;
    public int limit;
    public int storage;
    String type;
    public String goodstorage;
    public String goods_title;
    public String goods_url;
    private List<ImageView> list = null;
    private ArrayList<String> uList = new ArrayList<String>();
    String[] goods_images;
    Goods g = new Goods();
    private boolean isRefresh = false;
    private boolean isDestory;


    CFViewPagerAdapter viewPagerAdapter;
    DetailFragment detailFragment;
    CommentFragment commentFragment;
    Fragment currentgoodsFragment;
    int currentType;

    public String getDetail_url() {
        return detail_url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_activity_goods_detail2, null);
        }
        setContentView(view);
        ButterKnife.inject(this);
        shareWeiXinPlatform = new ShareWeiXinPlatform(this);
        ShareQQPlatform.getInstance().registerShare(this);
        ShareWeiboPlatform.getInstanse().regiesetShare(this, savedInstanceState, this);
//        sid = (int) getIntent().getSerializableExtra("sid");
//        pic = (String) getIntent().getSerializableExtra("pic");
        myBroadCastReceiver = new MyBroadCastReceiver(this, updateUI);
        myBroadCastReceiver.register();
        sid = (String) getIntent().getSerializableExtra("sid");
        detail_url = TLUrl.getInstance().URL_hwg_base+"/mobile/index.php?act=goods&op=goods_body&goods_id=" + sid;
        pic = (String) getIntent().getSerializableExtra("pic");
        car_num = (TextView) findViewById(R.id.car_num);
        if (MyApplication.getInstance().self != null) {
            initInCartNum();
        }
//        if (!NetworkUtils.isNetAvailable(this)) {
//            if (relativeNetwork != null)
//                relativeNetwork.setVisibility(View.VISIBLE);
//        }
//        tAddshopcar.setEnabled(false);
        initView();
        initAnim();
        initTopDates();

        initFragment();
        initViewPager();

        setOnListener();
    }


    LinearLayout.LayoutParams layoutParams;

    private void initViewPager() {
//        pager = (ViewPager) findViewById(R.id.comment_pager);
//        //第三方Tab
//        tabs = (PagerSlidingTabStrip) findViewById(R.id.comment_tabs);
        viewPagerAdapter = new CFViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.getDatas().add(detailFragment);
        viewPagerAdapter.getDatas().add(commentFragment);
        viewPagerAdapter.getTitle().add(0, "商品详情");
        viewPagerAdapter.getTitle().add(1, "商品评论");
//        viewPagerAdapter.getDatas().add(g5);
        layoutParams = new LinearLayout.LayoutParams(Util.WIDTH, Util.HEIGHT);
        commentPager.setLayoutParams(layoutParams);
        commentPager.setAdapter(viewPagerAdapter);
        commentPager.setOffscreenPageLimit(2);
//        pager.setPageTransformer(true, new DepthPageTransformer());
        commentTabs.setViewPager(commentPager);
        commentTabs.setIndicatorHeight(Util.dip2px(this, 4));
        commentTabs.setTextSize(Util.dip2px(this, 16));
        setSelectTextColor(0);
        commentTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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
        for (int i = 0; i < 2; i++) {
            View view = commentTabs.getChildAt(0);
//            if (view instanceof LinearLayout) {
            View viewText = ((LinearLayout) view).getChildAt(i);
            TextView tabTextView = (TextView) viewText;
            if (tabTextView != null) {

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
        Bundle bundle = new Bundle();
        bundle.putSerializable("goods_id", sid);
        detailFragment = new DetailFragment();
        commentFragment = new CommentFragment();
        commentFragment.setArguments(bundle);
    }


    private void setOnListener() {

//        relativeNetwork.setOnClickListener(this);
//        relativeBack.setOnClickListener(this);
//        relativeShare.setOnClickListener(this);
//        rlDianpu.setOnClickListener(this);
//        rlShopcar.setOnClickListener(this);
//        rlShoucang.setOnClickListener(this);
//        tAddshopcar.setOnClickListener(this);
        linearBaozhang.setOnClickListener(this);
    }

    private void initView() {

        shopcar = (ImageView) view.findViewById(R.id.shopcar);
        imgGoods.setVisibility(View.INVISIBLE);
        btnCartReduce.setEnabled(false);
        LoadPicture loadPicture = new LoadPicture();
        loadPicture.initPicture(imgGoods, pic);
//        car_num = (TextView) view.findViewById(R.id.car_num);
        if (MyApplication.getInstance().self != null) {
//            new InitCarNum(car_num, this,"");
        }

    }

    private void initAnim() {

        //初始化动画工具
        myViewAnimUtil = new MyViewAnimUtil(this, animation_viewGroup);
        SetOnSetHolderClickListener(new HolderClickListener() {
            @Override
            public void onHolderClick(Drawable drawable, int[] start_location) {
                myViewAnimUtil.doAnim(drawable, start_location, shopcar);
            }
        });
    }

    /**
     * 内存过低时及时处理动画产生的未处理冗余
     */
    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        myViewAnimUtil.isClean = true;
        try {
            animation_viewGroup.removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        myViewAnimUtil.isClean = false;
        super.onLowMemory();
    }

    private void initInCartNum() {
        if (MyApplication.getInstance().getMykey() != null) {
//            new InitCarNum(car_num, this,"");
        }
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

            if (intent.getSerializableExtra("type").equals(MyUpdateUI.GOODSDETAIL)) {
                Log.i("zjz", "商品详情");
//                hwgGoodsDetailTitle.setText("商品详情");
            }
            if (intent.getSerializableExtra("type").equals(MyUpdateUI.TUWENDETAIL)) {
                Log.i("zjz", "图文详情");
//                hwgGoodsDetailTitle.setText("图文详情");
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };


    private void initTopDates() {
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_gdetail + "&goods_id=" + sid, null, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "goodsDetail:连接成功");
                                JSONObject object1 = object.getJSONObject("datas");
                                if (object1.has("error")) {
                                    if (relativeNull != null && linearGoods != null) {
                                        relativeNull.setVisibility(View.VISIBLE);
                                        linearGoods.setVisibility(View.INVISIBLE);
                                    }

                                } else {
//                                    relativeNull.setVisibility(View.INVISIBLE);
//                                    linearGoods.setVisibility(View.VISIBLE);
                                    JSONObject object2 = object1.getJSONObject("goods_info");
                                    JSONObject storeObject = object1.getJSONObject("store_info");

                                    g.setTitle(object2.optString("goods_name"));
                                    goods_title = object2.optString("goods_name");
                                    g.setSubhead(object2.optString("goods_jingle"));
                                    g.setMoney(object2.optDouble("goods_price"));
                                    g.setPromote_money(object2.optDouble("goods_promotion_price"));
                                    g.setStore_goods_total(object2.optString("goods_storage"));
                                    g.setGoods_url(object2.optString("goods_url"));
                                    goods_url = object2.optString("goods_url");
                                    if (object2.optDouble("promotion_price", 0) == 0) {
//                                    relativeBiaoqian.setVisibility(View.GONE);
//                                    linearQiang.setVisibility(View.GONE);
//                                    tGoodsMoney.setText(NumberUtils.formatPrice(g.getMoney()));
                                        if (tGoodsMoney != null) {
                                            tGoodsMoney.setText((object2.optDouble("goods_price", 0)) + "");
                                        }
                                    } else {
//                                    relativeBiaoqian.setVisibility(View.VISIBLE);
//                                    linearQiang.setVisibility(View.VISIBLE);
//                                    tGoodsPromoteMoney.setText(NumberUtils.formatPrice(object2.optDouble("promotion_price", 0)));
                                        if (tGoodsMoney != null)
                                            tGoodsMoney.setText(object2.optDouble("promotion_price", 0) + "");
//                                    tGoodsMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                    }
                                    if (tGoodsJingle != null) {
                                        if (g.getSubhead().length() != 0) {
                                            tGoodsJingle.setText(g.getSubhead());
                                        } else {
                                            tGoodsJingle.setVisibility(View.GONE);
                                        }
                                    }
                                    storage = object2.optInt("goods_storage");
                                    type = object2.optString("title", "");
                                    limit = object2.optInt("upper_limit", 0);
                                    Log.i("zjz", "monery=" + g.getMoney());
                                    g.setDismoney(object2.optDouble("goods_marketprice"));
                                    Log.i("zjz", "dismonery=" + g.getDismoney());
                                    //商品详情html连接
                                    if (tGoodsName != null)
                                        tGoodsName.setText(g.getTitle());
                                    if (tGoodsOldMoney != null) {
                                        tGoodsOldMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                        tGoodsOldMoney.setText(NumberUtils.formatPrice(g.getDismoney()));
                                    }
                                    if (tMsales != null) {
                                        tMsales.setText(object2.optString("goods_salenum"));
                                    }
                                    if (tStorage != null)
                                        tStorage.setText(g.getStore_goods_total());
                                    goodstorage = g.getStore_goods_total();
                                    buynum = "1";
                                    if (tFreight != null)
                                        tFreight.setText(object2.optDouble("goods_freight") == 0.00 ? "包邮(港澳台，西藏新疆等偏远地区除外)" : NumberUtils.formatPrice(object2.optDouble("goods_freight")));
                                    if (btnCartNumEdit != null && btnCartAdd != null && btnCartReduce != null) {
                                        if (g.getStore_goods_total().equals("0")) {
                                            btnCartNumEdit.setFocusable(false);
                                            btnCartNumEdit.setText(0 + "");
                                            btnCartAdd.setEnabled(false);
                                            btnCartReduce.setEnabled(false);
                                        } else {
                                            btnCartNumEdit.setText("1");
                                            btnCartNumEdit.setSelection(btnCartNumEdit.getText().length());
                                            btnCartNumEdit.addTextChangedListener(new TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                    try {
                                                        int temp = Integer.parseInt(btnCartNumEdit.getText().toString());
                                                        num = temp;
                                                        buynum = btnCartNumEdit.getText().toString();
                                                        btnCartNumEdit.setSelection(btnCartNumEdit.getText().length());
                                                        if (temp > storage) {
                                                            btnCartNumEdit.setText(storage + "");
                                                            btnCartAdd.setEnabled(false);
                                                            btnCartReduce.setEnabled(true);
                                                        } else if (btnCartNumEdit.getText().toString().equals("")) {
                                                            btnCartNumEdit.setText("1");
                                                            btnCartReduce.setEnabled(false);
                                                            btnCartAdd.setEnabled(true);
                                                        } else if (temp < 1) {
                                                            btnCartNumEdit.setText("1");
                                                            btnCartReduce.setEnabled(false);
                                                            btnCartAdd.setEnabled(true);
                                                        } else if (temp == storage) {
                                                            btnCartAdd.setEnabled(false);
                                                            btnCartReduce.setEnabled(true);
                                                        } else if (temp == 1) {
                                                            btnCartReduce.setEnabled(false);
                                                            btnCartAdd.setEnabled(true);
                                                        } else {
                                                            btnCartReduce.setEnabled(true);
                                                            btnCartAdd.setEnabled(true);
                                                        }
                                                    } catch (Exception e) {
                                                        Toast.makeText(GoodsDetailActivity3.this, "数量不能为空", Toast.LENGTH_SHORT).show();
                                                        buynum = "";
                                                        btnCartReduce.setEnabled(false);
                                                        btnCartAdd.setEnabled(true);
                                                    }

                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {

                                                }
                                            });
                                        }
                                    }

//                                photo_url = object1.optString("content");
//                                detail_url = object1.optString("shoppar");
                                    if (linearGoodsDetail != null)
                                        linearGoodsDetail.setVisibility(View.VISIBLE);
                                    goods_images = object1.optString("goods_image").split(",");
                                    Log.i("zjz", "goods=" + goods_images.length);
                                    initTitleDate();

                                }
//                                tAddshopcar.setEnabled(true);
                                ProgressDlgUtil.stopProgressDlg();
                            } else {
                                ProgressDlgUtil.stopProgressDlg();
                                Log.i("zjz", "goodsDetail:解析失败");
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


    private void initTitleDate() {
        list = new ArrayList<ImageView>();
//        TypedArray array = getResources().obtainTypedArray(R.array.banner_array2);
        for (int i = 0; i < goods_images.length; i++) {
            uList.add(goods_images[i].replaceAll("_360", "_1280"));
            Log.i("zjz", "goods_img" + i + "=" + goods_images[i].replaceAll("_360", "_1280"));
            ImageView view = new ImageView(this);
//            view.setBackgroundResource(array.getResourceId(i, R.drawable.img_morentupian));
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            view.setLayoutParams(params);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            view.setLayoutParams(layoutParams);
            LoadPicture loadPicture = new LoadPicture();
            loadPicture.initPicture(view, goods_images[i].replaceAll("_360", "_1280"));
//            StartActivity.imageLoader.displayImage(goods_images[i], view);
//            Util.setImage(goodsImgs.get(i).getPicarr(), view, handler);
            list.add(view);
            final int m = i;
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent();
                    intent.putExtra("image", goods_images[m].replaceAll("_360", "_1280"));
                    intent.putExtra("ulist", uList);
                    intent.setClass(GoodsDetailActivity3.this, ShowWebImageActivity.class);
                    startActivity(intent);
                }
            });
        }

//        img = new ImageView[list.size()];
//        LinearLayout layout = (LinearLayout) activity.findViewById(R.id.tljr_viewGroup);
//        if (layout != null) {
//            if (list.size() == 1) {
//                layout.setVisibility(View.GONE);
//            }
//            layout.removeAllViews();
//            for (int i = 0; i < list.size(); i++) {
//                img[i] = new ImageView(activity);
//                if (0 == i) {
//                    img[i].setBackgroundResource(R.drawable.img_yuandian1);
//                } else {
//                    img[i].setBackgroundResource(R.drawable.img_yuandian2);
//                }
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.leftMargin = 10;
//                params.width = 10;
//                params.height = 10;
//                layout.addView(img[i], params);
//            }
//        }

        adapter.notifyDataSetChanged();
        if (tljrViewpager != null) {
            if (tljrViewpager.getAdapter() == null) {
                tljrViewpager.setAdapter(adapter);
                tljrViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }

                    @Override
                    public void onPageScrolled(int page, float positionOffset,
                                               int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int page) {
//                        check(page);
                    }
                });
            }
        }

        indicator.setViewPager(tljrViewpager);


        Log.i("zjz", "list_size=" + list.size());
        if (!isRefresh) {
            if (list.size() != 1) {
                handler.post(runnable);
            }
        }
    }


//    private void check(int page) {
//        pageChangeDelay = 0;
//        for (int i = 0; i < list.size(); i++) {
//            if (page == i) {
//                img[i].setBackgroundResource(R.drawable.img_yuandian1);
//            } else {
//                img[i].setBackgroundResource(R.drawable.img_yuandian2);
//            }
//        }
//    }

    PagerAdapter adapter = new PagerAdapter() {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    };

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (list.size() != 1 && !isDestory) {
                if (tljrViewpager.getCurrentItem() >= list.size() - 1) {
                    tljrViewpager.setCurrentItem(0);
                } else {
                    tljrViewpager.setCurrentItem(tljrViewpager.getCurrentItem() + 1);
                }
                handler.postDelayed(runnable, 5000);
            }

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        myBroadCastReceiver.unRegister();
        isDestory = true;
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.relative_share:
                showPopupView();
                break;
            case R.id.t_addshopcar:
                addToCart();

                break;
            case R.id.rl_shopcar:
                if (MyApplication.getInstance().self == null) {
                    intent = new Intent(this, WXEntryActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(this, CartActivity2.class);
                intent.putExtra("store_id","");
                startActivity(intent);
                break;
            case R.id.rl_dianpu:
                startActivity(new Intent(this, KefuActivity.class));
                break;
            case R.id.rl_shoucang:
                addToCollection();
                break;
            case R.id.relative_network:
                intent = new Intent("/");
                ComponentName cm = new ComponentName("com.android.settings",
                        "com.android.settings.Settings");
                intent.setComponent(cm);
                intent.setAction("android.intent.action.VIEW");
                startActivity(intent);
                break;

            case R.id.linear_baozhang:
                startActivity(new Intent(this, GoodsGuaranteeActivity.class));
                break;
            case R.id.btn_cart_add:
                num++;
                if (num == storage) {
                    btnCartAdd.setEnabled(false);
                }
                btnCartReduce.setEnabled(true);
                btnCartNumEdit.setText(num + "");
                break;
            case R.id.btn_cart_reduce:
                num--;
                if (num == 1) {
                    btnCartReduce.setEnabled(false);
                }
                btnCartAdd.setEnabled(true);
                btnCartNumEdit.setText(num + "");
                break;
        }
    }


    private void addToCart() {
        if (MyApplication.getInstance().self == null) {
            Intent intent = new Intent(this, WXEntryActivity.class);
            startActivity(intent);
            return;
        }
        if (MyApplication.getInstance().getMykey() == null) {
            Intent intent = new Intent(this, WXEntryActivity.class);
            startActivity(intent);
            return;
        }
        if (goodstorage.equals("0")) {
            showToast("商品库存为0，无法购买！");
            return;
        } else if (buynum.equals("")) {
            showToast("请选择购买商品数量！");
            return;
        }
        if (limit != 0 && num > limit) {
            showToast("该商品为抢购商品，每人限购" + limit + "件！");
            return;
        }
        int[] start_location = new int[2];
        imgGoods.getLocationInWindow(start_location);//获取点击商品图片的位置

        Drawable drawable = imgGoods.getDrawable();//复制一个新的商品图标
        mHolderClickListener.onHolderClick(drawable, start_location);

        ProgressDlgUtil.showProgressDlg("Loading...", this);
        Log.i("zjz", "num=" + num);
        Log.i("zjz", "add2cart_key=" + MyApplication.getInstance().getMykey());

        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_add_to_cart, "key=" + MyApplication.getInstance().getMykey() + "&goods_id=" + sid + "&quantity=" + num, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "addcart=" + msg);
                                //更新购物车数量
                                if (object.optString("datas").equals("1")) {
                                    MyUpdateUI.sendUpdateCarNum(GoodsDetailActivity3.this);
                                    showToast("添加成功！");
                                }
//                                new InitCarNum(car_num, GoodsDetailActivity2.this);
//                                btnCartNumEdit.setText("1");
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
                        } finally {
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });
    }

    private void addToCollection() {
        if (MyApplication.getInstance().getMykey() == null) {
            Intent intent = new Intent(this, WXEntryActivity.class);
            startActivity(intent);
            return;
        }
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_favorite_add, "goods_id=" + sid + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                if (object.optString("datas").equals("1")) {
                                    showToast("收藏成功");
                                } else {
                                    showToast("您已经收藏了该商品");
                                }
                                ProgressDlgUtil.stopProgressDlg();
                            } else {
                                ProgressDlgUtil.stopProgressDlg();
                                Log.i("zjz", "goodsDetail:解析失败");
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


    private PopupWindow popupWindow;

    @SuppressLint("InflateParams")
    @SuppressWarnings("deprecation")
    private void showPopupView() {
        if (popupWindow == null) {
            // 一个自定义的布局，作为显示的内容
            RelativeLayout contentView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.tljr_dialog_share_news, null);

            ((Button) contentView.findViewById(R.id.btn_cancle)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (popupWindow != null)
                        popupWindow.dismiss();
                }
            });

            LinearLayout ly1 = (LinearLayout) contentView.findViewById(R.id.ly1);

            for (int i = 0; i < ly1.getChildCount(); i++) {
                final int m = i;
                ly1.getChildAt(i).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        shareNewsUrl(m);
                        popupWindow.dismiss();
                    }
                });
            }
            popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            popupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setAnimationStyle(R.style.AnimationPreview);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setAlpha(1f);
                }
            });
        }

//        setAlpha(0.8f);
        int[] location = new int[2];
        View v = view.findViewById(R.id.relative_bottom);
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0],
                location[1] - popupWindow.getContentView().getMeasuredHeight());
    }

    private void setAlpha(float f) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = f;
        lp.dimAmount = f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    // type 0:微信 1朋友圈 2QQ 3新浪微博
    //       0 qq  1 微信  2 新浪微博 3朋友圈
    private void shareNewsUrl(int type) {

        switch (type) {
            case 1:

                shareWeiXinPlatform.setUrl(goods_url);
                shareWeiXinPlatform.setTitle(goods_title.length() > 22 ? goods_title.substring(0, 22) + "..." : goods_title);

//                String ct = Util.getTextFromHtml(g.getContent());

                shareWeiXinPlatform.setContent(goods_title.length() > 26 ? goods_title.substring(0, 26) + "..." : goods_title);
                shareWeiXinPlatform.wechatShare(0);

                break;
            case 3:
                shareWeiXinPlatform.setUrl(goods_url);
                shareWeiXinPlatform.setTitle(goods_title);
                shareWeiXinPlatform.wechatShare(1);
                break;
            case 0:
                ShareQQPlatform.getInstance().share(this, goods_url, goods_title, pic, null, ShareContent.appName);
                break;
            case 2:
                ShareWeiboPlatform.getInstanse().share(this, goods_url, goods_title, goods_title);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                showToast("分享成功");
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                showToast("取消分享");
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                showToast("分享失败，Error Message: " + baseResponse.errMsg);
                break;
        }
    }


    private HolderClickListener mHolderClickListener;

    public void SetOnSetHolderClickListener(HolderClickListener holderClickListener) {
        this.mHolderClickListener = holderClickListener;
    }


    public interface HolderClickListener {
        public void onHolderClick(Drawable drawable, int[] start_location);
    }
}
