package com.abcs.haiwaigou.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.GoodsMessageFragment;
import com.abcs.haiwaigou.fragment.GoodsTuwenFragment;
import com.abcs.haiwaigou.fragment.GoodsWordCommentFragment;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.InitCarNum;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.utils.MyViewAnimUtil;
import com.abcs.haiwaigou.view.DragLayout;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.ShengJiHuiYuanActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.huaqiaobang.wxapi.official.share.ShareQQPlatform;
import com.abcs.huaqiaobang.wxapi.official.share.ShareWeiXinPlatform;
import com.abcs.huaqiaobang.wxapi.official.share.ShareWeiboPlatform;
import com.abcs.huaqiaobang.wxapi.official.share.util.ShareContent;
import com.abcs.sociax.android.R;
import com.nineoldandroids.animation.ObjectAnimator;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GoodsDetailActivity extends BaseFragmentActivity implements View.OnClickListener, IWeiboHandler.Response {
    public static final String DETAIL = "goods_detail";
    public static final String COMMENT = "goods_comment";
    public static final String GOODCOMMENT = "goodcomment";
    public static final String MIDDLECOMMENT = "middlecomment";
    public static final String BADCOMMENT = "badcomment";
    public static final String ALLCOMMENT = "allcomment";
    public static final String IMGCOMMENT = "imgcomment";
    public static final String NORMAL = "normal";

    @InjectView(R.id.draglayout)
    DragLayout draglayout;
    @InjectView(R.id.hwg_goods_detail_title)
    TextView hwgGoodsDetailTitle;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.relative_share)
    RelativeLayout relativeShare;
    @InjectView(R.id.rl_dianpu)
    RelativeLayout rlDianpu;
    @InjectView(R.id.rl_shopcar)
    RelativeLayout rlShopcar;
    @InjectView(R.id.rl_shoucang)
    RelativeLayout rlShoucang;
    //    @InjectView(R.id.t_addshopcar)
    public static TextView tAddshopcar;
    @InjectView(R.id.img_goods)
    ImageView imgGoods;
    @InjectView(R.id.hwg_goods_message_title)
    RelativeLayout hwgGoodsMessageTitle;
    @InjectView(R.id.relative_network)
    RelativeLayout relativeNetwork;
    @InjectView(R.id.first)
    FrameLayout first;
    @InjectView(R.id.second)
    FrameLayout second;
    @InjectView(R.id.statusbar)
    View statusbar;
    @InjectView(R.id.img_back)
    ImageView imgBack;
    @InjectView(R.id.img_collect)
    ImageView imgCollect;
    @InjectView(R.id.img_share)
    ImageView imgShare;
    @InjectView(R.id.view_top)
    View viewTop;
    //banner
    private boolean isRefresh = false;
    private ViewPager viewpager = null;
    private List<ImageView> list = null;
    private List<ImageView> mList = null;
    private ImageView[] img = null;
    private int pageChangeDelay = 0;

    private ArrayList<Goods> goodsImgs = new ArrayList<Goods>();
    public Handler handler = new Handler();
    private String sid,store_id;
    String pic;
    String photo_url;
    String detail_url;

    public String getDetail_url() {
        return detail_url;
    }

    //    private int num = 1;
    //top
    private ArrayList<Goods> goodsList = new ArrayList<Goods>();
    private TextView t_goods_money;
    private TextView t_goods_oldmoney;
    private TextView t_goods_name;

    public static TextView car_num;
    MyBroadCastReceiver myBroadCastReceiver;
    ImageView img_goods;

    public static ImageView shopcar;

    MyViewAnimUtil myViewAnimUtil;
    FrameLayout animation_viewGroup;


    public ShareWeiXinPlatform shareWeiXinPlatform;

    private GoodsMessageFragment goodsMessageFragment;
    private GoodsTuwenFragment goodsTuwenFragment;
    private GoodsWordCommentFragment goodsWordCommentFragment;

    public static int num;
    public static String buynum;
    public static int limit;
    public static String storage;
    public static String goods_title;
    public static String goods_url;
    private View view;
    public static final String TUWEN = "2";
    public static final String GOODS = "1";
    String type = GOODS;
    private boolean isYun=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_activity_goods_message, null);
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
        store_id=getIntent().getStringExtra("store_id");
        isYun=getIntent().getBooleanExtra("isYun",false);
        sid = (String) getIntent().getSerializableExtra("sid");
        detail_url = TLUrl.getInstance().URL_hwg_base+"/mobile/index.php?act=goods&op=goods_body&goods_id=" + sid;
        pic = (String) getIntent().getSerializableExtra("pic");
        car_num = (TextView) findViewById(R.id.car_num);
        if (MyApplication.getInstance().self != null) {
            initInCartNum();
        }
        if (!NetworkUtils.isNetAvailable(this)) {
            if (relativeNetwork != null)
                relativeNetwork.setVisibility(View.VISIBLE);
        }
        tAddshopcar = (TextView) view.findViewById(R.id.t_addshopcar);
        tAddshopcar.setEnabled(false);
        if (isYun)
            tAddshopcar.setVisibility(View.GONE);
        initView();
        initAnim();
        setOnListener();

        if (MyApplication.getInstance().getMykey() != null) {
            findIsCollection();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////            window = getWindow();
////            // Translucent status bar
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            statusbar.setVisibility(View.VISIBLE);
        }

    }


    public static String getIsVisible() {
        return isVisible;
    }

    public static void setIsVisible(String isVisible) {
        GoodsDetailActivity.isVisible = isVisible;
    }

    public static String isVisible;

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
            new InitCarNum(car_num, this,store_id);
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
                ObjectAnimator.ofFloat(hwgGoodsDetailTitle, "alpha", 1, 0, 1).setDuration(1000).start();
                ObjectAnimator.ofFloat(imgBack, "alpha", 1, 0, 1).setDuration(1000).start();
                ObjectAnimator.ofFloat(imgCollect, "alpha", 1, 0, 1).setDuration(1000).start();
                ObjectAnimator.ofFloat(imgShare, "alpha", 1, 0, 1).setDuration(1000).start();
//                ObjectAnimator.ofFloat(hwgGoodsDetailTitle, "rotationX",0, -360).setDuration(1000).start();
//                hwgGoodsDetailTitle.setText("商品详情");
                myHandler.postDelayed(weakRun, 500);
                type = TUWEN;
            }
            if (intent.getSerializableExtra("type").equals(MyUpdateUI.TUWENDETAIL)) {
                Log.i("zjz", "图文详情");
                ObjectAnimator.ofFloat(hwgGoodsDetailTitle, "alpha", 1, 0, 1).setDuration(1000).start();
                ObjectAnimator.ofFloat(imgBack, "alpha", 1, 0, 1).setDuration(1000).start();
                ObjectAnimator.ofFloat(imgCollect, "alpha", 1, 0, 1).setDuration(1000).start();
                ObjectAnimator.ofFloat(imgShare, "alpha", 1, 0, 1).setDuration(1000).start();
//                ObjectAnimator.ofFloat(hwgGoodsDetailTitle, "rotationX",0, 360).setDuration(1000).start();
//                hwgGoodsDetailTitle.setText("图文详情");
                myHandler.postDelayed(weakRun, 500);
                type = GOODS;
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };
    MyHandler myHandler = new MyHandler(new WeakReference<GoodsDetailActivity>(GoodsDetailActivity.this));

    static class MyHandler extends Handler {
        WeakReference<GoodsDetailActivity> weakReference;

        public MyHandler(WeakReference<GoodsDetailActivity> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            GoodsDetailActivity temp = weakReference.get();
            switch (msg.what) {
                case 1:
                    temp.hwgGoodsDetailTitle.setText("");
                    temp.imgBack.setImageResource(R.drawable.img_hwg_detail_back_dark);
                    temp.imgShare.setImageResource(R.drawable.img_hwg_detail_share_dark);
                    if(isCollect==1){
                        temp.imgCollect.setImageResource(R.drawable.img_hwg_detail_collect_y_nos);
                    }else {
                        temp.imgCollect.setImageResource(R.drawable.img_hwg_detail_collect_dark);
                    }
                    temp.viewTop.setVisibility(View.GONE);
                    break;
                case 2:
                    temp.hwgGoodsDetailTitle.setText("商品详情");
                    temp.imgBack.setImageResource(R.drawable.img_hwg_detail_back_white);
                    temp.imgShare.setImageResource(R.drawable.img_hwg_detail_share_white);
                    temp.imgCollect.setImageResource(R.drawable.img_hwg_detail_collect_white);
                    temp.viewTop.setVisibility(View.VISIBLE);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    Runnable weakRun = new Runnable() {
        @Override
        public void run() {
            Message message = myHandler.obtainMessage();
            if (type.equals(GOODS)) {
                message.what = 2;
            } else if (type.equals(TUWEN)) {
                message.what = 1;
            }

            message.sendToTarget();
        }
    };

    private FragmentManager manager;
    private FragmentTransaction transaction;

    private void initView() {
        LoadPicture loadPicture = new LoadPicture();
        loadPicture.initPicture(imgGoods, pic);
        shopcar = (ImageView) findViewById(R.id.shopcar);
        Bundle bundle = new Bundle();
        bundle.putSerializable("goods_id", sid);
        bundle.putSerializable("goods_pic", pic);
        goodsMessageFragment = new GoodsMessageFragment();
        goodsMessageFragment.setArguments(bundle);
        goodsTuwenFragment = new GoodsTuwenFragment();
        goodsWordCommentFragment = new GoodsWordCommentFragment();
        goodsWordCommentFragment.setArguments(bundle);
//        manager = getSupportFragmentManager();
//        transaction = manager.beginTransaction();
//        transaction.add(R.id.first, goodsMessageFragment);
//        transaction.add(R.id.second, goodsWordCommentFragment);
//        transaction.commitAllowingStateLoss();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.first, goodsMessageFragment).add(R.id.second, goodsWordCommentFragment)
                .commit();
        DragLayout.ShowNextPageNotifier nextIntf = new DragLayout.ShowNextPageNotifier() {
            @Override
            public void onDragNext() {
                goodsTuwenFragment.initView();
//                goodsWordCommentFragment.initFragment();
            }
        };
//        draglayout = (DragLayout) findViewById(R.id.draglayout);
        draglayout.setNextPageListener(nextIntf);
    }

    public void turn2DetailComment() {
        draglayout.turn2Next(-150, -150);
        MyUpdateUI.sendUpdateCollection(this, MyUpdateUI.TURN2COMMENT);
    }

    public void turn2Detail() {
        draglayout.turn2Next(-150, -150);
        MyUpdateUI.sendUpdateCollection(this, MyUpdateUI.TURN2DETAIL);
    }

    private void setOnListener() {

        relativeNetwork.setOnClickListener(this);
        relativeBack.setOnClickListener(this);
        relativeShare.setOnClickListener(this);
        rlDianpu.setOnClickListener(this);
        rlShopcar.setOnClickListener(this);
        rlShoucang.setOnClickListener(this);
        tAddshopcar.setOnClickListener(this);
    }


    private void check(int page) {
        pageChangeDelay = 0;
        for (int i = 0; i < list.size(); i++) {
            if (page == i) {
                img[i].setBackgroundResource(R.drawable.img_yuandian1);
            } else {
                img[i].setBackgroundResource(R.drawable.img_yuandian2);
            }
        }
    }

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
            if (viewpager.getCurrentItem() >= list.size() - 1) {
                viewpager.setCurrentItem(0);
            } else {
                viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
            }
            handler.postDelayed(runnable, 5000);
        }
    };

     boolean isMai;

    private boolean confirmData(){


        Log.i("zds_sid_xiangqing",sid+"");
//        goods_id=101146&key=939f6c2c1ad7199187be733cc714955a
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_comfirm_isSale, "key=" + MyApplication.getInstance().getMykey() + "&goods_id=" + sid , new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(!TextUtils.isEmpty(msg)){
                                JSONObject object = new JSONObject(msg);   //state  为1表示可以购买，为0表示不允许购买
                                if (object.optString("state").equals("1")) {
                                    addToCart();
//                                    isMai=true;
                                }else if(object.optString("state").equals("0")) {
//                                    isMai=false;
                                    startActivity(new Intent(GoodsDetailActivity.this, ShengJiHuiYuanActivity.class));
                                }
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
        return isMai;
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
//            case R.id.relative_goods_detail:
//                intent = new Intent(this, GoodsCommentActivity.class);
//                intent.putExtra("sid", sid);
//                intent.putExtra("photoUrl", photo_url);
//                intent.putExtra("detailUrl", detail_url);
//                startActivity(intent);
//                break;
            case R.id.t_addshopcar:   //加入购物车

                if (MyApplication.getInstance().getMykey() == null) {
                    Intent rt = new Intent(this, WXEntryActivity.class);
                    rt.putExtra("isthome",true);
                    startActivity(rt);
                    return;
                }else {
                    confirmData();/***********验证是否可以购买*********/
                }
                break;
            case R.id.rl_shopcar:
                if (MyApplication.getInstance().self == null) {
                    intent = new Intent(this, WXEntryActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(this, CartActivity2.class);
                intent.putExtra("store_id",store_id);
                startActivity(intent);
                break;
            case R.id.rl_dianpu:
//                intent = new Intent(this, StoreActivity.class);
//                startActivity(intent);
                startActivity(new Intent(this, KefuActivity.class));
//                new PromptDialog(this, "客服电话：0755-32838341\n客服QQ：3032174381", new Complete() {
//                    @Override
//                    public void complete() {
//
//                    }
//                });
                break;
            case R.id.rl_shoucang:
                addToCollection();
                break;
//            case R.id.rl_fenxiang:
//                showToast("分享成功...");
//                break;
            case R.id.rl_store:
                intent = new Intent(this, StoreActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_network:
                intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
//                intent = new Intent("/");
//                ComponentName cm = new ComponentName("com.android.settings",
//                        "com.android.settings.Settings");
//                intent.setComponent(cm);
//                intent.setAction("android.intent.action.VIEW");
//                startActivity(intent);
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
            intent.putExtra("isthome",true);
            startActivity(intent);
            return;
        }
        if (storage.equals("0")) {
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
                                    MyUpdateUI.sendUpdateCarNum(GoodsDetailActivity.this);
                                    showToast("添加成功！");
                                } else if (object.optString("datas").contains("error")) {
                                    showToast(object.getJSONObject("datas").optString("error"));
                                }
//                                new InitCarNum(car_num, GoodsDetailActivity2.this);
//                                btnCartNumEdit.setText("1");
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

private static   int isCollect=0;
    private  boolean freshCollect=false;

    @Override
    protected void onResume() {
        super.onResume();
        if(freshCollect){
            findIsCollection();
            freshCollect=false;
        }
    }

    private void addToCollection() {
        if (MyApplication.getInstance().getMykey() == null) {
             freshCollect=true;
            Intent intent = new Intent(this, WXEntryActivity.class);
            intent.putExtra("isthome",true);
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
                                     isCollect=1;
                                    imgCollect.setImageResource(R.drawable.img_hwg_detail_collect_y_nos);
                                    showToast("收藏成功");
                                } else if(object.optString("datas").equals("2"))  {
                                    imgCollect.setImageResource(R.drawable.img_hwg_detail_collect_dark);
                                    showToast("取消收藏成功");
                                }else {
                                    showToast("服务器出了点小错误！");
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
    private void findIsCollection() {

//        http://www.huaqiaobang.com/mobile/index.php?act=goods&op=find_goods_favorites&goods_id=101146&key=939f6c2c1ad7199187be733cc714955a
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_favorite_find_iscollect, "act=goods&op=find_goods_favorites&goods_id=" + sid + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("state") == 1) {
                                imgCollect.setImageResource(R.drawable.img_hwg_detail_collect_y_nos);
                            } else {
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

        setAlpha(0.8f);
        int[] location = new int[2];
        View v = view.findViewById(R.id.relative_bottom);
        v.getLocationOnScreen(location);
//        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1] - popupWindow.getContentView().getMeasuredHeight());
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]);
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

                Log.i("zds","share_url=="+goods_url);

                shareWeiXinPlatform.setUrl(goods_url);
                shareWeiXinPlatform.setTitle(goods_title.length() > 22 ? goods_title.substring(0, 22) + "..." : goods_title);

//                String ct = Util.getTextFromHtml(g.getContent());
                shareWeiXinPlatform.setContent(goods_title.length() > 26 ? goods_title.substring(0, 26) + "..." : goods_title);
                shareWeiXinPlatform.wechatShare(0);   //好友

                break;
            case 3:
                shareWeiXinPlatform.setUrl(goods_url);
                shareWeiXinPlatform.setTitle(goods_title);
                shareWeiXinPlatform.wechatShare(1);  //朋友圈
                break;
            case 0:
//                http://www.huaqiaobang.com/data/upload/shop/store/goods/1/
                ShareQQPlatform.getInstance().share(this, goods_url, goods_title, "http://www.huaqiaobang.com/data/upload/shop/store/goods/1/"+pic, null, ShareContent.appName);
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

    @Override
    protected void onDestroy() {
        myBroadCastReceiver.unRegister();
        super.onDestroy();
    }

    private HolderClickListener mHolderClickListener;

    public void SetOnSetHolderClickListener(HolderClickListener holderClickListener) {
        this.mHolderClickListener = holderClickListener;
    }


    public interface HolderClickListener {
        public void onHolderClick(Drawable drawable, int[] start_location);
    }

}
