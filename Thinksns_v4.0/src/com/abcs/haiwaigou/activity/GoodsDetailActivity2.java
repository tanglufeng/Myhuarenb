package com.abcs.haiwaigou.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.InitCarNum;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.utils.MyViewAnimUtil;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.haiwaigou.utils.mCountDownTimer;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GoodsDetailActivity2 extends BaseFragmentActivity implements View.OnClickListener, IWeiboHandler.Response {
    @InjectView(R.id.tljr_txt_country_title)
    TextView tljrTxtCountryTitle;
    @InjectView(R.id.tljr_hqss_news_titlebelow)
    TextView tljrHqssNewsTitlebelow;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.tljr_hwg_more)
    ImageView tljrHwgMore;
    @InjectView(R.id.tljr_grp_country_title)
    RelativeLayout tljrGrpCountryTitle;
    @InjectView(R.id.img_goods)
    ImageView imgGoods;
    @InjectView(R.id.tljr_viewpager)
    ViewPager tljrViewpager;
    @InjectView(R.id.linear_viewpager)
    LinearLayout linearViewpager;
    @InjectView(R.id.tljr_viewGroup)
    LinearLayout tljrViewGroup;
    @InjectView(R.id.img_bottom)
    ImageView imgBottom;
    @InjectView(R.id.img_top)
    ImageView imgTop;
    @InjectView(R.id.relative_container)
    RelativeLayout relativeContainer;
    @InjectView(R.id.t_goods_money)
    TextView tGoodsMoney;
    @InjectView(R.id.t_goods_oldmoney)
    TextView tGoodsOldmoney;
    @InjectView(R.id.t_goods_name)
    TextView tGoodsName;
    @InjectView(R.id.t_jingle)
    TextView tJingle;
    @InjectView(R.id.relative_goods_detail)
    LinearLayout relativeGoodsDetail;
    @InjectView(R.id.t_send_to)
    TextView tSendTo;
    @InjectView(R.id.t_address)
    TextView tAddress;
    @InjectView(R.id.relative_send_to)
    RelativeLayout relativeSendTo;
    @InjectView(R.id.image11)
    ImageView image11;
    @InjectView(R.id.image112)
    ImageView image112;
    @InjectView(R.id.image111)
    ImageView image111;
    @InjectView(R.id.image2111)
    ImageView image2111;
    @InjectView(R.id.img_store)
    ImageView imgStore;
    @InjectView(R.id.relative_icon)
    RelativeLayout relativeIcon;
    @InjectView(R.id.jiantou)
    RelativeLayout jiantou;
    @InjectView(R.id.t_store_name)
    TextView tStoreName;
    @InjectView(R.id.t_average_score)
    TextView tAverageScore;
    @InjectView(R.id.t_description)
    TextView tDescription;
    @InjectView(R.id.t_descriptionPoint)
    TextView tDescriptionPoint;
    @InjectView(R.id.linear_pingjia)
    LinearLayout linearPingjia;
    @InjectView(R.id.t_service)
    TextView tService;
    @InjectView(R.id.t_servicePoint)
    TextView tServicePoint;
    @InjectView(R.id.linear_taidu)
    LinearLayout linearTaidu;
    @InjectView(R.id.t_deliver)
    TextView tDeliver;
    @InjectView(R.id.t_deliverPoint)
    TextView tDeliverPoint;
    @InjectView(R.id.linear_sudu)
    LinearLayout linearSudu;
    @InjectView(R.id.img_top2)
    ImageView imgTop2;
    @InjectView(R.id.img_bottom2)
    ImageView imgBottom2;
    @InjectView(R.id.relative_shop)
    RelativeLayout relativeShop;
    @InjectView(R.id.image141)
    ImageView image141;
    @InjectView(R.id.image1512)
    ImageView image1512;
    @InjectView(R.id.image1411)
    ImageView image1411;
    @InjectView(R.id.image21311)
    ImageView image21311;
    @InjectView(R.id.img_kefu)
    ImageView imgKefu;
    @InjectView(R.id.rl_kefu)
    RelativeLayout rlKefu;
    @InjectView(R.id.image151)
    ImageView image151;
    @InjectView(R.id.image1142)
    ImageView image1142;
    @InjectView(R.id.image1113)
    ImageView image1113;
    @InjectView(R.id.image21113)
    ImageView image21113;
    @InjectView(R.id.rl_store)
    RelativeLayout rlStore;
    @InjectView(R.id.scroll)
    ScrollView scroll;
    @InjectView(R.id.img_dianpu)
    ImageView imgDianpu;
    @InjectView(R.id.rl_dianpu)
    RelativeLayout rlDianpu;
    @InjectView(R.id.img_fenxiang)
    ImageView imgFenxiang;
    @InjectView(R.id.rl_fenxiang)
    RelativeLayout rlFenxiang;
    @InjectView(R.id.shoucang)
    ImageView shoucang;
    @InjectView(R.id.rl_shoucang)
    RelativeLayout rlShoucang;
    @InjectView(R.id.textView)
    TextView textView;
    @InjectView(R.id.rl_shopcar)
    RelativeLayout rlShopcar;
    @InjectView(R.id.t_addshopcar)
    TextView tAddshopcar;
    @InjectView(R.id.relative_bottom)
    RelativeLayout relativeBottom;
    @InjectView(R.id.t_storage)
    TextView tStorage;
    @InjectView(R.id.t_select)
    TextView tSelect;
    @InjectView(R.id.btn_cart_reduce)
    Button btnCartReduce;
    @InjectView(R.id.btn_cart_num_edit)
    EditText btnCartNumEdit;
    @InjectView(R.id.btn_cart_add)
    Button btnCartAdd;
    @InjectView(R.id.relative_biaoqian)
    RelativeLayout relativeBiaoqian;
    @InjectView(R.id.t_goods_promote_money)
    TextView tGoodsPromoteMoney;
    @InjectView(R.id.linear_qiang)
    LinearLayout linearQiang;
    @InjectView(R.id.linear_count_down)
    LinearLayout linearCountDown;
    @InjectView(R.id.countdown_day)
    TextView countdownDay;
    @InjectView(R.id.countdown_hour)
    TextView countdownHour;
    @InjectView(R.id.countdown_minute)
    TextView countdownMinute;
    @InjectView(R.id.countdown_second)
    TextView countdownSecond;
    @InjectView(R.id.goods_baozhang)
    ImageView goodsBaozhang;
    @InjectView(R.id.t_freight)
    TextView tFreight;
    //banner
    private boolean isRefresh = false;
    private ViewPager viewpager = null;
    private List<ImageView> list = null;
    private List<ImageView> mList = null;
    private ImageView[] img = null;
    public static String[] picUrl = {"http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/img_dashujuguanggao.png",
            "http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/img_jichaguanggaotu.png", "http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/img_licaiguanggaotu.png",
            "http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/img_licaiguanggaotu.png", "http://tuling.oss-cn-hangzhou.aliyuncs.com/banner/img_licaiguanggaotu.png"};
    private int pageChangeDelay = 0;

    private ArrayList<Goods> goodsImgs = new ArrayList<Goods>();
    public Handler handler = new Handler();
    private String sid;
    String pic;
    String photo_url;
    String detail_url;

    private int num = 1;
    //top
    private ArrayList<Goods> goodsList = new ArrayList<Goods>();

    MyBroadCastReceiver myBroadCastReceiver;
    public static ImageView shopcar;
    public static TextView car_num;
    MyViewAnimUtil myViewAnimUtil;
    FrameLayout animation_viewGroup;
    private RequestQueue mRequestQueue;
    String store_id;
    String address;
    long end_time;
    boolean isQiang;
    int storage;
    double promote_money;
    String type;
    int limit;
    private TextView[] times;


    public ShareWeiXinPlatform shareWeiXinPlatform;
    private ArrayList<String> uList = new ArrayList<String>();
    private View view;
    Goods g = new Goods();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareWeiXinPlatform = new ShareWeiXinPlatform(this);
        ShareQQPlatform.getInstance().registerShare(this);
        ShareWeiboPlatform.getInstanse().regiesetShare(this, savedInstanceState, this);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_activity_goods_detail, null);
        }
        setContentView(view);
        ButterKnife.inject(this);
        sid = (String) getIntent().getSerializableExtra("sid");
        pic = (String) getIntent().getSerializableExtra("pic");
        isQiang = getIntent().getBooleanExtra("qiang", false);
        myBroadCastReceiver = new MyBroadCastReceiver(this, updateUI);
        myBroadCastReceiver.register();
        mRequestQueue = Volley.newRequestQueue(this);
        initView();
        initAnim();

        setOnListener();
        initTitlePage();
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

    MyBroadCastReceiver.UpdateUI updateUI = new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

        }

        @Override
        public void updateCarNum(Intent intent) {
            initView();
        }


        @Override
        public void updateCollection(Intent intent) {

        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initView() {
        if (isQiang) {
            end_time = (long) getIntent().getSerializableExtra("end_time");
            promote_money = (double) getIntent().getSerializableExtra("promote_money");
            long currentTime = System.currentTimeMillis() / 1000;
            relativeBiaoqian.setVisibility(View.VISIBLE);
            linearCountDown.setVisibility(View.VISIBLE);
            linearQiang.setVisibility(View.VISIBLE);
            times = new TextView[4];
            times[0] = countdownDay;
            times[1] = countdownHour;
            times[2] = countdownMinute;
            times[3] = countdownSecond;
            mCountDownTimer countDownTimer = new mCountDownTimer((end_time - currentTime) * 1000, 1000, times);
            countDownTimer.start();
        } else {
            relativeBiaoqian.setVisibility(View.GONE);
            linearCountDown.setVisibility(View.GONE);
            linearQiang.setVisibility(View.GONE);
        }

        shopcar = (ImageView) findViewById(R.id.shopcar);
        imgGoods.setVisibility(View.INVISIBLE);
        btnCartReduce.setEnabled(false);
        LoadPicture loadPicture = new LoadPicture();
        loadPicture.initPicture(imgGoods, pic);
        car_num = (TextView) findViewById(R.id.car_num);
        if (MyApplication.getInstance().self != null) {
            new InitCarNum(car_num, this,"");
        }
    }

    String[] goods_images;

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
                                relativeGoodsDetail.setVisibility(View.VISIBLE);
                                JSONObject object1 = object.getJSONObject("datas");
                                JSONObject object2 = object1.getJSONObject("goods_info");
                                JSONObject storeObject = object1.getJSONObject("store_info");
                                initStore(storeObject);

                                g.setTitle(object2.optString("goods_name"));
                                g.setSubhead(object2.optString("goods_jingle"));
                                g.setMoney(object2.optDouble("goods_price"));
                                g.setPromote_money(object2.optDouble("goods_promotion_price"));
                                g.setStore_goods_total(object2.optString("goods_storage"));
                                g.setGoods_url(object2.optString("goods_url"));
                                if (object2.optDouble("promotion_price", 0) == 0) {
                                    relativeBiaoqian.setVisibility(View.GONE);
                                    linearQiang.setVisibility(View.GONE);
                                } else {
                                    relativeBiaoqian.setVisibility(View.VISIBLE);
                                    linearQiang.setVisibility(View.VISIBLE);
                                    tGoodsPromoteMoney.setText(NumberUtils.formatPrice(object2.optDouble("promotion_price", 0)));
                                    tGoodsMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                }
                                storage = object2.optInt("goods_storage");
                                type = object2.optString("title", "");
                                limit = object2.optInt("upper_limit", 0);
                                Log.i("zjz", "monery=" + g.getMoney());
                                g.setDismoney(object2.optDouble("goods_marketprice"));
                                Log.i("zjz", "dismonery=" + g.getDismoney());
                                //商品详情html连接
                                tGoodsName.setText(g.getTitle());
                                tGoodsMoney.setText(NumberUtils.formatPrice(g.getMoney()));
//                                if (isQiang) {
//                                    tGoodsPromoteMoney.setText(NumberUtils.formatPrice(promote_money));
//                                    tGoodsMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//                                }
                                tGoodsOldmoney.setText(NumberUtils.formatPrice(g.getDismoney()));
                                tGoodsOldmoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                tStorage.setText(g.getStore_goods_total());
                                tFreight.setText(object2.optDouble("goods_freight")==0.00?"卖家承担运费":NumberUtils.formatPrice(object2.optDouble("goods_freight")));
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
                                                showToast("数量不能为空");
                                                btnCartReduce.setEnabled(false);
                                                btnCartAdd.setEnabled(true);
                                            }

                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {

                                        }
                                    });
                                }
                                if (g.getSubhead().equals("")) {
                                    tJingle.setText("暂无商品描述");
                                } else {
                                    tJingle.setText(g.getSubhead());
                                }
                                photo_url = object1.optString("content");
                                detail_url = object1.optString("shoppar");
                                goods_images = object1.optString("goods_image").split(",");
                                Log.i("zjz", "goods=" + goods_images.length);
                                initTitleDate();
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

    private void initStore(JSONObject object) {
        store_id = object.optString("store_id");
        mRequestQueue.add(new JsonObjectRequest(TLUrl.getInstance().URL_hwg_store_detail + "&store_id=" + store_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject data = jsonObject.getJSONObject("datas");
                    JSONObject store_pf = data.getJSONObject("store_pf");
                    JSONObject store_info = data.getJSONObject("store_info");
                    initStoreData(store_pf, store_info);
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

    private void initStoreData(JSONObject store_pf, JSONObject store_info) throws JSONException {
        JSONObject store_desccredit = store_pf.optJSONObject("store_desccredit");
        JSONObject store_servicecredit = store_pf.getJSONObject("store_servicecredit");
        JSONObject store_deliverycredit = store_pf.getJSONObject("store_deliverycredit");
        if (!store_desccredit.equals("")) {
            tDescription.setText(store_desccredit.optString("text"));
            if (store_desccredit.optString("credit", "0").contains(".")) {
                tDescriptionPoint.setText(store_desccredit.optString("credit", "0"));
            } else {
                tDescriptionPoint.setText(store_desccredit.optString("credit", "0") + ".0");
            }
        }
        if (!"".equals(store_servicecredit)) {
            tService.setText(store_servicecredit.optString("text"));
            if (store_servicecredit.optString("credit", "0").contains(".")) {
                tServicePoint.setText(store_servicecredit.optString("credit", "0"));
            } else {
                tServicePoint.setText(store_servicecredit.optString("credit", "0") + ".0");
            }
        }
        if (!"".equals(store_deliverycredit)) {
            tDeliver.setText(store_deliverycredit.optString("text"));
            if (store_deliverycredit.optString("credit", "0").contains(".")) {

                tDeliverPoint.setText(store_deliverycredit.optString("credit", "0"));
            } else {
                tDeliverPoint.setText(store_deliverycredit.optString("credit", "0") + ".0");
            }
        }
        tStoreName.setText(store_info.optString("store_name"));
        LoadPicture loadPicture = new LoadPicture();
        loadPicture.initPicture(imgStore, TLUrl.getInstance().URL_hwg_store_head + store_info.optString("store_avatar"));
        if (store_info.optString("store_credit_average").contains(".")) {
            tAverageScore.setText(store_info.optString("store_credit_average"));
        } else {
            tAverageScore.setText(store_info.optString("store_credit_average") + ".0 分");
        }
    }

    private void setOnListener() {
        relativeBack.setOnClickListener(this);
        relativeGoodsDetail.setOnClickListener(this);
        tAddshopcar.setOnClickListener(this);
        rlDianpu.setOnClickListener(this);
        rlShopcar.setOnClickListener(this);
        rlFenxiang.setOnClickListener(this);
        rlShoucang.setOnClickListener(this);
        rlStore.setOnClickListener(this);
        rlKefu.setOnClickListener(this);
        relativeSendTo.setOnClickListener(this);
        btnCartAdd.setOnClickListener(this);
        btnCartReduce.setOnClickListener(this);
        goodsBaozhang.setOnClickListener(this);

    }

    private void initTitlePage() {
        // TODO Auto-generated method stub
        viewpager = (ViewPager) findViewById(R.id.tljr_viewpager);
        Util.setSize(viewpager, Util.IMAGEWIDTH, (int) (Util.IMAGEWIDTH / 2.1));
//        initTitleDate();
//        initTop();
        initTopDates();
        if (!isRefresh) {
//            handler.post(runnable);
        }
    }


    private void initTitleDate() {

        if (!type.equals("")) {

        }
        list = new ArrayList<ImageView>();
//        TypedArray array = getResources().obtainTypedArray(R.array.banner_array2);
        for (int i = 0; i < goods_images.length; i++) {
            uList.add(goods_images[i]);
            ImageView view = new ImageView(this);
//            view.setBackgroundResource(array.getResourceId(i, R.drawable.img_morentupian));
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            view.setLayoutParams(params);
            view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            LoadPicture loadPicture = new LoadPicture();
            loadPicture.initPicture(view, goods_images[i]);
//            StartActivity.imageLoader.displayImage(goods_images[i], view);
//            Util.setImage(goodsImgs.get(i).getPicarr(), view, handler);
            list.add(view);
            final int m = i;
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent();
                    intent.putExtra("image", goods_images[m]);
                    intent.putExtra("ulist", uList);
                    intent.setClass(GoodsDetailActivity2.this, ShowWebImageActivity.class);
                    GoodsDetailActivity2.this.startActivity(intent);
                }
            });
        }

        img = new ImageView[list.size()];
        LinearLayout layout = (LinearLayout) findViewById(R.id.tljr_viewGroup);
        if (list.size() == 1) {
            layout.setVisibility(View.GONE);
        }
        layout.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            img[i] = new ImageView(this);
            if (0 == i) {
                img[i].setBackgroundResource(R.drawable.img_yuandian1);
            } else {
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
        if (viewpager.getAdapter() == null) {
            viewpager.setAdapter(adapter);
            viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrollStateChanged(int state) {
                }

                @Override
                public void onPageScrolled(int page, float positionOffset,
                                           int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int page) {
                    check(page);
                }
            });
        }
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

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.relative_goods_detail:
                intent = new Intent(this, GoodsDetailCommentActivity.class);
                intent.putExtra("sid", sid);
                startActivity(intent);
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
                intent.putExtra("store_id",store_id);
                startActivity(intent);
                break;
            case R.id.rl_dianpu:
                intent = new Intent(this, AllStoreActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_shoucang:

                addToCollection();
                break;
            case R.id.rl_fenxiang:
//                showToast("分享成功...");
                showPopupView();
                break;
            case R.id.rl_store:
                intent = new Intent(this, StoreActivity.class);
                intent.putExtra("store_id", store_id);
                startActivity(intent);
                break;
            case R.id.relative_send_to:
                if (MyApplication.getInstance().getMykey() == null) {
                    intent = new Intent(this, WXEntryActivity.class);
                    startActivity(intent);

                } else {
                    intent = new Intent(this, AddressActivity.class);
                    startActivityForResult(intent, 1);
                }
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
            case R.id.goods_baozhang:
                startActivity(new Intent(this, SecurityActivity.class));
                break;

        }
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
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
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

                shareWeiXinPlatform.setUrl(g.getGoods_url());
                shareWeiXinPlatform.setTitle(g.getTitle().length() > 22 ? g
                        .getTitle().substring(0, 22) + "..." : g.getTitle());

//                String ct = Util.getTextFromHtml(g.getContent());

                shareWeiXinPlatform.setContent(g.getTitle().length() > 26 ? g.getTitle().substring(0,
                        26) + "..." : g.getTitle());
                shareWeiXinPlatform.wechatShare(0);

                break;
            case 3:
                shareWeiXinPlatform.setUrl(g.getGoods_url());
                shareWeiXinPlatform.setTitle(g.getTitle());
                shareWeiXinPlatform.wechatShare(1);
                break;
            case 0:
                ShareQQPlatform.getInstance().share(this, g.getGoods_url(), g.getTitle(), pic, null, ShareContent.appName);
                break;
            case 2:
                ShareWeiboPlatform.getInstanse().share(this,
                        g.getGoods_url(), g.getTitle(), g.getTitle());
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == 1 && data != null) {
            address = (String) data.getSerializableExtra("address");
            tAddress.setText(address);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        if (tStorage.getText().toString().equals("0")) {
            showToast("商品库存为0，无法购买！");
            return;
        } else if (btnCartNumEdit.getText().toString().equals("")) {
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
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_add_to_cart, "key=" + MyApplication.getInstance().getMykey() + "&goods_id=" + sid + "&quantity=" + num, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                //更新购物车数量
                                MyUpdateUI.sendUpdateCarNum(GoodsDetailActivity2.this);
//                                new InitCarNum(car_num, GoodsDetailActivity2.this);
                                btnCartNumEdit.setText("1");
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
        ButterKnife.reset(this);
        super.onDestroy();
    }

    private HolderClickListener mHolderClickListener;

    public void SetOnSetHolderClickListener(HolderClickListener holderClickListener) {
        this.mHolderClickListener = holderClickListener;
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

    public interface HolderClickListener {
        public void onHolderClick(Drawable drawable, int[] start_location);
    }

}
