package com.abcs.haiwaigou.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.activity.GoodsCommentActivity;
import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.activity.GoodsGuaranteeActivity;
import com.abcs.haiwaigou.activity.PhotoPreviewActivity;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.adapter.CommentItemFragmentAdapter;
import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager2;
import com.abcs.haiwaigou.fragment.viewholder.CommentItemFragmentViewHolder;
import com.abcs.haiwaigou.model.Comment;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.view.CircleIndicator;
import com.abcs.haiwaigou.view.CustScrollView;
import com.abcs.haiwaigou.view.zjzbanner.LMBanners;
import com.abcs.haiwaigou.view.zjzbanner.adapter.LBaseAdapter;
import com.abcs.haiwaigou.view.zjzbanner.transformer.TransitionEffect;
import com.abcs.haiwaigou.view.zjzbanner.utils.ScreenUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.ShengJiHuiYuanActivity;
import com.abcs.huaqiaobang.adapter.GalleryAdapter;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.ShowMessageDialog;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by zjz on 2016/4/13.
 */
public class GoodsMessageFragment extends Fragment implements View.OnClickListener, CommentItemFragmentViewHolder.ItemOnClick {

    GoodsDetailActivity activity;
    @InjectView(R.id.img_goods)
    ImageView imgGoods;
    @InjectView(R.id.tljr_viewpager)
    ViewPager tljrViewpager;
    @InjectView(R.id.linear_viewpager)
//    LinearLayout linearViewpager;
//    @InjectView(R.id.tljr_viewGroup)
            LinearLayout tljrViewGroup;
    @InjectView(R.id.relative_container)
    RelativeLayout relativeContainer;
    @InjectView(R.id.t_goods_name)
    TextView tGoodsName;
    @InjectView(R.id.t_goods_money)
    TextView tGoodsMoney;

    @InjectView(R.id.tv_jk_price)
    TextView tv_jk_price;
    @InjectView(R.id.tv_jk_price_fang)
    TextView tv_jk_price_fang;   // 金卡返利
    @InjectView(R.id.tv_bjk_price)
    TextView tv_bjk_price;
    @InjectView(R.id.tv_bjk_price_fang)
    TextView tv_bjk_price_fang;  // 白金卡返利
    @InjectView(R.id.tv_hk_price)
    TextView tv_hk_price;
    @InjectView(R.id.tv_hk_price_fang)
    TextView tv_hk_price_fang;  // 黑卡返利

    @InjectView(R.id.t_goods_old_money)
    TextView tGoodsOldMoney;
    @InjectView(R.id.liner_fahuodi)
    LinearLayout liner_fahuodi;
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
    @InjectView(R.id.img_more)
    ImageView imgMore;

    @InjectView(R.id.linear_baozhang)
    LinearLayout linearBaozhang;

    @InjectView(R.id.liner_jk)
    LinearLayout liner_jk;
    @InjectView(R.id.liner_bjk)
    LinearLayout liner_bjk;
    @InjectView(R.id.liner_hk)
    LinearLayout liner_hk;

    @InjectView(R.id.linear_goods_detail)
    LinearLayout linearGoodsDetail;
    @InjectView(R.id.t_message)
    TextView tMessage;
    @InjectView(R.id.tv_daohuo)
    TextView tv_daohuo;
    @InjectView(R.id.comment_recyclerView)
    RecyclerView commentRecyclerView;
    @InjectView(R.id.relative_more)
    RelativeLayout relativeMore;
    @InjectView(R.id.linear_goods)
    LinearLayout linearGoods;
    @InjectView(R.id.relative_null)
    RelativeLayout relativeNull;
    @InjectView(R.id.t_text)
    TextView tText;
    @InjectView(R.id.indicator)
    CircleIndicator indicator;
    @InjectView(R.id.t_msales)
    TextView tMsales;
    @InjectView(R.id.t_goods_jingle)
    TextView tGoodsJingle;
    @InjectView(R.id.t_deliver_add)
    TextView tDeliverAdd;
    @InjectView(R.id.suit_recyclerView)
    RecyclerView suitRecyclerView;
    @InjectView(R.id.linear_suit)
    LinearLayout linearSuit;
    @InjectView(R.id.t_suit_title)
    TextView tSuitTitle;
    @InjectView(R.id.t_buy_suit)
    TextView tBuySuit;
    @InjectView(R.id.t_yuanjia)
    TextView tYuanjia;
    @InjectView(R.id.t_taozhuangjia)
    TextView tTaozhuangjia;
    @InjectView(R.id.t_desc)
    TextView tDesc;
    @InjectView(R.id.t_mansong_name)
    TextView tMansongName;
    @InjectView(R.id.t_mansong_time)
    TextView tMansongTime;
    @InjectView(R.id.linear_mansong_rules)
    LinearLayout linearMansongRules;
    @InjectView(R.id.linear_mansong)
    LinearLayout linearMansong;
    @InjectView(R.id.scrollView)
    CustScrollView scrollView;
    @InjectView(R.id.img_tuwen)
    ImageView imgTuwen;
    @InjectView(R.id.t_comment)
    TextView tComment;
    @InjectView(R.id.img_to_zungui_huiyuan)
    TextView img_to_zungui_huiyuan;
    @InjectView(R.id.linear_comment)
    LinearLayout linearComment;
    @InjectView(R.id.banners)
    LMBanners banners;
    @InjectView(R.id.linear_gift)
    LinearLayout linearGift;
    @InjectView(R.id.linear_gift_list)
    LinearLayout linearGiftList;
    private int num = 1;
    private boolean isRefresh = false;
    public Handler handler = new Handler();
    Goods g = new Goods();
    String[] goods_images;
    //    private ImageView[] img = null;
    private List<ImageView> list = null;
    private ArrayList<String> uList = new ArrayList<String>();
    private int pageChangeDelay = 0;
    private boolean isDestory;
    private RequestQueue mRequestQueue;
    private ArrayList<Comment> dataList = new ArrayList<>();
    private ArrayList<String> bannerString = new ArrayList<>();

    String goods_id;
    String goods_pic;
    int storage;
    double promote_money;
    String type;
    int limit;
    String suitTitleName;
    String suitId;

    private GalleryAdapter galleryAdapter;
    View rootView;
    String rule_id;
    String levelId;
    private String store_id;  // 店铺的Id

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (GoodsDetailActivity) getActivity();
        if (rootView == null) {

            rootView = inflater.inflate(R.layout.hwg_fragment_goods_detail, null);
        }

        ButterKnife.inject(this, rootView);
        mRequestQueue = Volley.newRequestQueue(activity);
        goods_id = (String) getArguments().getSerializable("goods_id");
        goods_pic = (String) getArguments().getSerializable("goods_pic");

        levelId=MyApplication.getInstance().getMyLevelId();


        Util.setFZLTHFont(tGoodsJingle);
//        Typeface fontFace = Typeface.createFromAsset(activity.getAssets(), "font/fangzhenglantinghei.TTF");
//        tGoodsJingle.setTypeface(fontFace);
//        if (MyApplication.getInstance().getMykey() == null) {
//            linearNull.setVisibility(View.VISIBLE);
//            tMessage.setText("温馨提示：请登录后查看评论~");
//        }
//        initTitlePage();
        initTopDates();
        setOnListener();
        initRecycler();

        if(!TextUtils.isEmpty(levelId)&&levelId.equals("4")){
                    img_to_zungui_huiyuan.setBackground(getResources().getDrawable(R.drawable.img_to_zunguihyuan_qyi));
        }else {//我的会员
                    img_to_zungui_huiyuan.setBackground(getResources().getDrawable(R.drawable.img_to_zunguihyuan_sj));
            }

        return rootView;
    }


    private void setOnListener() {
        img_to_zungui_huiyuan.setOnClickListener(this);
        linearBaozhang.setOnClickListener(this);
        btnCartReduce.setOnClickListener(this);
        btnCartAdd.setOnClickListener(this);
        relativeMore.setOnClickListener(this);
        tBuySuit.setOnClickListener(this);
        imgTuwen.setOnClickListener(this);
    }


    private void initTopDates() {
        btnCartReduce.setEnabled(false);
        ProgressDlgUtil.showProgressDlg("Loading...", activity);
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_gdetail + "&goods_id=" + goods_id+"&key=" + MyApplication.getInstance().getMykey(), null, new HttpRevMsg() {
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

                                if(!TextUtils.isEmpty(object1.optString("rank"))){
                                    if(object1.optString("rank").equals("0")){
                                        img_to_zungui_huiyuan.setVisibility(View.VISIBLE);
                                    }else {
                                        img_to_zungui_huiyuan.setVisibility(View.GONE);
                                    }
                                }else {
                                    img_to_zungui_huiyuan.setVisibility(View.GONE);
                                }

                                if (object1.has("error")) {
                                    if (relativeNull != null && linearGoods != null) {
                                        relativeNull.setVisibility(View.VISIBLE);
                                        linearGoods.setVisibility(View.INVISIBLE);
                                    }
                                    GoodsDetailActivity.tAddshopcar.setEnabled(false);
                                } else {
                                    boolean notBuy = false;
//                                    relativeNull.setVisibility(View.INVISIBLE);
//                                    linearGoods.setVisibility(View.VISIBLE);
                                    JSONObject object2 = object1.getJSONObject("goods_info");

                                    store_id=object2.optString("store_id");  // 店铺的Id

                                    if(object2.optString("store_id").equals("1")){
                                        img_to_zungui_huiyuan.setVisibility(View.VISIBLE);
                                    }else {   //本地配送
                                        img_to_zungui_huiyuan.setVisibility(View.GONE);
                                    }

                                    rule_id = object2.optString("rule_id");

                                    JSONObject storeObject = object1.getJSONObject("store_info");
                                    JSONObject suitObject = object1.getJSONObject("suitinfo");
                                    //无法购买，商品下架
                                    if (suitObject != null) {
                                        if (suitObject.has("bunengmai")) {
                                            notBuy = true;
                                            new ShowMessageDialog(rootView, activity, Util.WIDTH * 4 / 5, suitObject.optString("msg"), "温馨提示");
                                        }
                                        JSONObject bundingObject = suitObject.optJSONObject("bundling_array");
                                        if (bundingObject != null && linearSuit != null) {
                                            linearSuit.setVisibility(View.VISIBLE);
                                            Iterator bundingKey = bundingObject.keys();
                                            while (bundingKey.hasNext()) {
                                                String keys = (String) bundingKey.next();
                                                JSONObject bundingObj = bundingObject.getJSONObject(keys);
                                                suitTitleName = bundingObj.optString("name");
                                                suitId = bundingObj.optString("id");
                                                if (tSuitTitle != null)
                                                    tSuitTitle.setText(suitTitleName);
                                                if (tYuanjia != null)
                                                    tYuanjia.setText("原    价：¥" + bundingObj.optString("cost_price"));
                                                if (tTaozhuangjia != null)
                                                    tTaozhuangjia.setText("套装价：¥" + bundingObj.optString("price"));
                                            }
                                            JSONObject goodsArrayObject = suitObject.getJSONObject("b_goods_array");
                                            initSuitGoods(goodsArrayObject);
                                        } else {
                                            if (linearSuit != null)
                                                linearSuit.setVisibility(View.GONE);
                                        }
                                    }

                                    //满送规则
                                    JSONObject mansongObject = object1.optJSONObject("mansong_info");
                                    if (mansongObject.has("mansong_id") && linearMansong != null&&linearMansongRules!=null) {
                                        linearMansong.setVisibility(View.VISIBLE);
//                                        if (tMansongName != null)
//                                            tMansongName.setText(mansongObject.optString("mansong_name"));
                                        if (tMansongTime != null)
                                            tMansongTime.setText("(活动时间：" + Util.format1.format(mansongObject.optLong("start_time") * 1000)
                                                    + "—" + Util.format1.format(mansongObject.optLong("end_time") * 1000) + ")");
                                        JSONArray mansongArray = mansongObject.getJSONArray("rules");
                                        initMansongRule(mansongArray);
                                    }

                                    //礼物赠送
                                    JSONArray giftArray = object1.optJSONArray("gift_array");
                                    if (giftArray.length()!= 0 && linearGift != null&&linearGiftList!=null) {
                                        linearGift.setVisibility(View.VISIBLE);
                                        initGiftList(giftArray,object2);
                                    }

                                    if (tDeliverAdd != null){
                                        if (object2.optString("g_orgin").equals("9")) {
                                            tDeliverAdd.setText("浙江");
                                            tv_daohuo.setText("8-12小时发货");
                                        } else if (object2.optString("g_orgin").equals("11")) {
                                            tDeliverAdd.setText("上海");
                                            tv_daohuo.setText("8-12小时发货");
                                        } else if (object2.optString("g_orgin").equals("35")) {
                                            tDeliverAdd.setText("海外");
                                            tv_daohuo.setText("3-20天到货");
                                        }else if (object2.optString("g_orgin").equals("0")) {
                                            tDeliverAdd.setText("深圳");
                                            tv_daohuo.setText("8-12小时发货");
                                        }else if (object2.optString("g_orgin").equals("175")) {
                                            tDeliverAdd.setText("杭州");
                                            tv_daohuo.setText("8-12小时发货");
                                        } else {
                                            tDeliverAdd.setText("其他");
                                            tv_daohuo.setText("8-12小时发货");
                                        }
                                    }

                                    g.setTitle(object2.optString("goods_name"));
                                    GoodsDetailActivity.goods_title = object2.optString("goods_name");
                                    g.setSubhead(object2.optString("goods_jingle"));
                                    g.setMoney(object2.optDouble("goods_price"));
                                    ////////////////////////
                                    g.setJk_price(object2.optDouble("jk_price"));
                                    g.setBjk_price(object2.optDouble("bjk_price"));
                                    g.setHk_price(object2.optDouble("hk_price"));
                                    /////////////////////
                                    g.setPromote_money(object2.optDouble("goods_promotion_price"));
                                    g.setStore_goods_total(object2.optString("goods_storage"));
                                    g.setGoods_url(object2.optString("goods_url"));
                                    GoodsDetailActivity.goods_url = object2.optString("goods_url");
                                    if (object2.optDouble("promotion_price", 0) == 0) {
//                                    relativeBiaoqian.setVisibility(View.GONE);
//                                    linearQiang.setVisibility(View.GONE);
//                                    tGoodsMoney.setText(NumberUtils.formatPrice(g.getMoney()));

                                        if(object2.optString("store_id").equals("11")){
                                            liner_fahuodi.setVisibility(View.GONE);
                                        }else {
                                            liner_fahuodi.setVisibility(View.VISIBLE);
                                        }

                                        if (tGoodsMoney != null && tDesc != null) {
                                            tDesc.setVisibility(View.GONE);
                                            if(object2.optString("store_id").equals("11")){
                                                tGoodsMoney.setText("€ "+(object2.optDouble("goods_price", 0)) + "");
                                            }else {
                                                tGoodsMoney.setText("￥"+(object2.optDouble("goods_price", 0)) + "");
                                            }

                                        }
                                    } else {
//                                    relativeBiaoqian.setVisibility(View.VISIBLE);
//                                    linearQiang.setVisibility(View.VISIBLE);
//                                    tGoodsPromoteMoney.setText(NumberUtils.formatPrice(object2.optDouble("promotion_price", 0)));
                                        if (tGoodsMoney != null && tDesc != null) {
                                            tDesc.setVisibility(View.VISIBLE);
                                            if(object2.optString("store_id").equals("11")){
                                                tGoodsMoney.setText("€ "+object2.optDouble("promotion_price", 0) + "");
                                            }else {
                                                tGoodsMoney.setText("￥"+object2.optDouble("promotion_price", 0) + "");
                                            }
                                        }
//                                    tGoodsMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                    }

                                    tv_jk_price_fang.setText(object2.optString("jk_rp")+"");
                                    tv_bjk_price_fang.setText(object2.optString("bjk_rp")+"");
                                    tv_hk_price_fang.setText(object2.optString("hk_rp")+"");

                                    if(object2.optInt("goods_promotion_type")==0){   // goods_promotion_type  促销类型  0为无促销  2为促销

                                        if(object2.optDouble("jk_price")>0){
                                            if(object2.optString("store_id").equals("11")){
                                                tv_jk_price.setText("€ "+(object2.optDouble("jk_price", 0)));
                                            }else {
                                                tv_jk_price.setText("￥"+(object2.optDouble("jk_price", 0)));
                                            }

                                        }else {
//                                        liner_jk.setVisibility(View.GONE);
                                            if(object2.optString("store_id").equals("11")){
                                                tv_jk_price.setText("€ "+(object2.optDouble("goods_price", 0)));
                                            }else {

                                                tv_jk_price.setText("￥"+(object2.optDouble("goods_price", 0)));
                                            }
                                        }


                                        if(object2.optDouble("bjk_price")>0){
                                            tv_bjk_price.setText((object2.optDouble("bjk_price", 0)) + "");

                                        }else {
                                            tv_bjk_price.setText((object2.optDouble("goods_price", 0)) + "");
//                                        liner_bjk.setVisibility(View.GONE);
                                        }


                                        if(object2.optDouble("hk_price")>0){
                                            tv_hk_price.setText((object2.optDouble("hk_price", 0)) + "");
                                        }else {
                                            tv_hk_price.setText((object2.optDouble("goods_price", 0)) + "");
//                                        liner_hk.setVisibility(View.GONE);
                                        }

                                    }else{    //  2为促销    goods_promotion_price促销价格
                                        if(object2.optDouble("jk_price")>0){
                                            if(object2.optString("store_id").equals("11")){
                                                tv_jk_price.setText("€ "+(object2.optDouble("jk_price", 0)) + "");
                                            }else {

                                                tv_jk_price.setText("￥"+(object2.optDouble("jk_price", 0)) + "");
                                            }
                                        }else {
//                                        liner_jk.setVisibility(View.GONE);
                                            if(object2.optString("store_id").equals("11")){
                                                tv_jk_price.setText("€ "+(object2.optDouble("goods_promotion_price", 0)) + "");
                                            }else {

                                                tv_jk_price.setText("￥"+(object2.optDouble("goods_promotion_price", 0)) + "");
                                            }
                                        }

                                        if(object2.optDouble("bjk_price")>0){
                                            tv_bjk_price.setText((object2.optDouble("bjk_price", 0)) + "");

                                        }else {
                                            tv_bjk_price.setText((object2.optDouble("goods_promotion_price", 0)) + "");
//                                        liner_bjk.setVisibility(View.GONE);
                                        }

                                        if(object2.optDouble("hk_price")>0){
                                            tv_hk_price.setText((object2.optDouble("hk_price", 0)) + "");
                                        }else {
                                            tv_hk_price.setText((object2.optDouble("goods_promotion_price", 0)) + "");
//                                        liner_hk.setVisibility(View.GONE);
                                        }
                                    }

                                    if (tGoodsJingle != null) {
                                        if (g.getSubhead().length() != 0) {
                                            tGoodsJingle.setText(Util.ReplaceSpecialSymbols(g.getSubhead()));
                                        } else {
                                            tGoodsJingle.setVisibility(View.GONE);
                                        }
                                    }

                                    storage = object2.optInt("goods_storage");
                                    type = object2.optString("title", "");
                                    limit = object2.optInt("upper_limit", 0);
                                    GoodsDetailActivity.limit = limit;
                                    Log.i("zjz", "monery=" + g.getMoney());
                                    g.setDismoney(object2.optDouble("goods_marketprice"));
                                    Log.i("zjz", "dismonery=" + g.getDismoney());
                                    //商品详情html连接
                                    if (tGoodsName != null)
                                        tGoodsName.setText(g.getTitle());
                                    if (tGoodsOldMoney != null) {
                                        tGoodsOldMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                        if(object2.optString("store_id").equals("11")){
                                            tGoodsOldMoney.setText("市场价：€ "+g.getDismoney());
                                        }else {
                                            tGoodsOldMoney.setText("市场价：￥"+g.getDismoney());
//                                            tGoodsOldMoney.setText("市场价："+NumberUtils.formatPrice(g.getDismoney()));
                                        }

                                    }
                                    if (tMsales != null) {
                                        tMsales.setText(object2.optString("goods_salenum"));
                                    }
                                    if (tStorage != null) {
                                        if (g.getStore_goods_total().equals("0"))
                                            tStorage.setTextColor(Color.parseColor("#e50042"));
                                        tStorage.setText(g.getStore_goods_total());
                                    }
                                    GoodsDetailActivity.storage = g.getStore_goods_total();
                                    GoodsDetailActivity.num = num;
                                    GoodsDetailActivity.buynum = "1";
                                    if (tFreight != null)
                                        if(!TextUtils.isEmpty(object2.optString("goods_freight"))){


                                            int start=object2.optString("goods_freight").indexOf("(");
                                            int end=object2.optString("goods_freight").lastIndexOf(")");

//                                            if(start>0&&end>0){
//
//                                            }

                                            log.e("zds","start=="+start+"end=="+end);
                                            log.e("zds","result=="+object2.optString("goods_freight").substring(start,end+1));

                                            SpannableString span=new SpannableString(object2.optString("goods_freight"));

                                            if(!TextUtils.isEmpty(span)){
                                                //给限定字符之间的字符着色
                                                span.setSpan(new ForegroundColorSpan(Color.RED), start, end+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                tFreight.setText(span);
                                            }


                                        }
//                                        tFreight.setText(object2.optDouble("goods_freight") == 0.00 ? "包邮(港澳台，西藏新疆等偏远地区除外)" : NumberUtils.formatPrice(object2.optDouble("goods_freight")));
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
                                                        GoodsDetailActivity.num = num;
                                                        GoodsDetailActivity.buynum = btnCartNumEdit.getText().toString();
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
                                                        Toast.makeText(activity, "数量不能为空", Toast.LENGTH_SHORT).show();
                                                        GoodsDetailActivity.buynum = "";
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
//                                    initTitleDate();
                                    initBanners();
                                    if (notBuy) {

                                        GoodsDetailActivity.tAddshopcar.setEnabled(false);
                                    } else {
                                        GoodsDetailActivity.tAddshopcar.setEnabled(true);
                                    }
                                }
                                if (imgTuwen != null)
                                    imgTuwen.setVisibility(View.VISIBLE);

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

    ArrayList<Goods>giftList=new ArrayList<Goods>();
    private void initGiftList(JSONArray array,JSONObject goodsObj) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            Goods goods = new Goods();
            goods.setTitle(object.optString("gift_goodsname"));
            goods.setGoods_id(object.optString("gift_goodsid"));
            goods.setGoods_num(object.optInt("gift_amount"));
            goods.setGoods_url(TLUrl.getInstance().URL_hwg_comment_goods + goodsObj.optString("store_id")+"/"+object.optString("gift_goodsimage").replaceAll(".jpg","_60.jpg"));
            giftList.add(goods);
        }
        linearGiftList.removeAllViews();
        linearGiftList.invalidate();
        for (int j = 0; j < giftList.size(); j++) {
            View view = activity.getLayoutInflater().inflate(R.layout.hwg_item_goods_gift, null);
            TextView t_gift_name = (TextView) view.findViewById(R.id.t_gift_name);
            TextView t_gift_num = (TextView) view.findViewById(R.id.t_gift_num);
            ImageView img_gift = (ImageView) view.findViewById(R.id.img_gift);
            t_gift_name.setText(giftList.get(j).getTitle());
            t_gift_name.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            t_gift_name.getPaint().setAntiAlias(true);//抗锯齿
            final int finalJ = j;
            t_gift_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                    intent.putExtra("sid", giftList.get(finalJ).getGoods_id());
                    intent.putExtra("pic", giftList.get(finalJ).getGoods_url());
                    startActivity(intent);
                }
            });
            t_gift_num.setText("X"+giftList.get(j).getGoods_num());
            ImageLoader.getInstance().displayImage(giftList.get(j).getGoods_url(), img_gift, Options.getListOptions());
            if (linearGiftList != null)
                linearGiftList.addView(view);
        }
    }

    private void initBanners() {
        bannerString.clear();
        for (int i = 0; i < goods_images.length; i++) {
            bannerString.add(goods_images[i].replaceAll("_360", "_1280"));
        }
        if (banners != null) {
            //设置Banners高度
            banners.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ScreenUtils.dip2px(activity, 300)));
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


    }

    class UrlImgAdapter implements LBaseAdapter<String> {
        private Context mContext;

        public UrlImgAdapter(Context context) {
            mContext = context;
        }

        @Override
        public View getView(final LMBanners lBanners, final Context context, final int position, String data) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.banner_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.id_image);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageLoader.getInstance().displayImage(data, imageView, Options.getHDOptions());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("image", goods_images[position].replaceAll("_360", "_1280"));
                    intent.putExtra("ulist", bannerString);
                    intent.setClass(activity, PhotoPreviewActivity.class);
                    activity.startActivity(intent);
                }
            });
            return view;
        }

    }

    private ArrayList<Goods> mansongList = new ArrayList<>();

    private void initMansongRule(JSONArray array) throws JSONException {

        if(array!=null&&array.length()>0){
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Goods goods = new Goods();
                goods.setTitle2(object.optString("rule_name"));
                goods.setRule_id(object.optString("rule_ids"));
                goods.setMoney(object.optDouble("price", 0));
                goods.setDismoney(object.optDouble("discount", 0));
                goods.setGoods_id(object.optString("goods_id"));
                goods.setGoods_url(object.optString("goods_image_url"));
                mansongList.add(goods);
            }
            linearMansongRules.removeAllViews();
            linearMansongRules.invalidate();
            for (int j = 0; j < mansongList.size(); j++) {
                View view = activity.getLayoutInflater().inflate(R.layout.hwg_item_goods_detail_mansong, null);
                TextView t_money = (TextView) view.findViewById(R.id.t_money);
                TextView t_rules = (TextView) view.findViewById(R.id.t_rule);
                TextView t_unit = (TextView) view.findViewById(R.id.t_unit);
                ImageView img_goods = (ImageView) view.findViewById(R.id.img_goods);
                t_money.setText(mansongList.get(j).getMoney() + "");
                if (mansongList.get(j).getDismoney() != 0) {
                    //满减
                    t_rules.setText("减" + mansongList.get(j).getDismoney() + "元");
                    img_goods.setVisibility(View.GONE);

                    if (rule_id.equals(mansongList.get(j).getRule_id()) && linearMansongRules != null) {
                        if(mansongList.get(j).getMoney()<50){
                            t_rules.setText("送" + String.valueOf(mansongList.get(j).getDismoney()).replaceAll(".0", "") + "件");
                            t_unit.setText("件");
                            t_money.setText(String.valueOf(mansongList.get(j).getMoney()).replaceAll(".0", ""));
                        }
                        linearMansongRules.addView(view);
                        if (tMansongName != null)
                            tMansongName.setText(mansongList.get(j).getTitle2());
                    }

                } else {
                    //满送
                    t_rules.setText("送礼品");
                    img_goods.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(mansongList.get(j).getGoods_url(), img_goods, Options.getListOptions());
                    final int finalJ = j;
                    img_goods.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                            intent.putExtra("sid", mansongList.get(finalJ).getGoods_id());
                            intent.putExtra("pic", mansongList.get(finalJ).getGoods_url());
                            startActivity(intent);
                        }
                    });

                    if (rule_id.equals("0") && linearMansongRules != null) {
                        linearMansongRules.addView(view);
                        if (tMansongName != null)
                            tMansongName.setText(mansongList.get(j).getTitle2());
                    }
                }
//            if (linearMansongRules != null)
//                linearMansongRules.addView(view);
            }
        }else {
            linearMansong.setVisibility(View.GONE);
        }
    }

    private void initSuitGoods(JSONObject jsonObject) throws JSONException {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        suitRecyclerView.setLayoutManager(layoutManager);
        Iterator goodKey = jsonObject.keys();
        final ArrayList<Goods> mGoods = new ArrayList<>();
        while (goodKey.hasNext()) {
            mGoods.clear();
            String string = (String) goodKey.next();
            JSONObject goodObject = jsonObject.getJSONObject(string);
            Iterator goodsKey = goodObject.keys();
            while (goodsKey.hasNext()) {
                String goodsTemp = (String) goodsKey.next();
                JSONObject goodsObject = goodObject.getJSONObject(goodsTemp);
                Goods goods = new Goods();
                goods.setGoods_id(goodsObject.optString("id"));
                goods.setTitle(goodsObject.optString("name"));
                goods.setMoney(goodsObject.optDouble("price", 0));
                goods.setPromote_money(goodsObject.optDouble("shop_price", 0));
                goods.setGoods_url(goodsObject.optString("image"));
                mGoods.add(goods);
            }
        }
        galleryAdapter = new GalleryAdapter(activity, mGoods);
        galleryAdapter.setOnItemClickListner(new GalleryAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                intent.putExtra("sid", mGoods.get(position).getGoods_id());
                intent.putExtra("pic", mGoods.get(position).getGoods_url());
                startActivity(intent);
            }
        });
        suitRecyclerView.setAdapter(galleryAdapter);

    }


    private void initTitleDate() {
        list = new ArrayList<ImageView>();
//        TypedArray array = getResources().obtainTypedArray(R.array.banner_array2);
        for (int i = 0; i < goods_images.length; i++) {
            uList.add(goods_images[i].replaceAll("_360", "_1280"));
            Log.i("zjz", "goods_img" + i + "=" + goods_images[i].replaceAll("_360", "_1280"));
            ImageView view = new ImageView(activity);
//            view.setBackgroundResource(array.getResourceId(i, R.drawable.img_morentupian));
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            view.setLayoutParams(params);
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
//            LoadPicture loadPicture = new LoadPicture();
//            loadPicture.initPicture(view, goods_images[i].replaceAll("_360", "_1280"));
            ImageLoader.getInstance().displayImage(goods_images[i].replaceAll("_360", "_1280"), view, Options.getHDOptions());
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
                    intent.setClass(activity, PhotoPreviewActivity.class);
                    activity.startActivity(intent);
                }
            });
        }


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
                    }
                });
            }
        }

        if (indicator != null && tljrViewpager != null)
            indicator.setViewPager(tljrViewpager, list.size());


        Log.i("zjz", "list_size=" + list.size());
        if (!isRefresh) {
            if (list.size() != 1) {
                handler.post(runnable);
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

    CommentItemFragmentAdapter commentItemFragmentAdapter;
    FullyLinearLayoutManager2 fullyLinearLayoutManager;

    private void initRecycler() {
        commentItemFragmentAdapter = new CommentItemFragmentAdapter(activity, this);
        fullyLinearLayoutManager = new FullyLinearLayoutManager2(getContext());

        commentRecyclerView.setFocusable(false);
        commentRecyclerView.setLayoutManager(fullyLinearLayoutManager);
        commentRecyclerView.setAdapter(commentItemFragmentAdapter);
        initComment();
    }


    private void initComment() {
        Log.i("zjz", "goods_id=" + goods_id);
        LogUtil.e("mwj", "商品评论地址" + TLUrl.getInstance().URL_hwg_goods_comment2 + "&goods_id=" + goods_id + "&type=all" + "&curpage=1");
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_goods_comment2+"&goods_id=" + goods_id + "&type=all" + "&curpage=1",null , new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject response=new JSONObject(msg);
                            if (response.getInt("code") == 200) {
                                Log.i("zjz", "goods_all_comment:连接成功");
                                Log.i("zjz", "response=" + response);
                                if (response.getInt("page_total") != 0) {
                                    JSONObject object = response.getJSONObject("datas");
                                    JSONArray groupArray = object.getJSONArray("goodsevallist");
                                    JSONObject jsonObject = object.getJSONObject("goods_evaluate_info");
                                    if (tComment != null)
                                        tComment.setText("商品评论(" + jsonObject.optString("all") + ")");
                                    dataList.clear();
                                    for (int i = 0; i < groupArray.length(); i++) {
                                        Comment orders = new Comment();
                                        orders.setId(i);
                                        JSONObject object1 = groupArray.getJSONObject(i);
                                        orders.setGeval_id(object1.optString("geval_id"));
                                        orders.setGeval_orderid(object1.optString("geval_orderid"));
                                        orders.setGeval_orderno(object1.optString("geval_orderno"));
                                        orders.setGeval_ordergoodsid(object1.optString("geval_ordergoodsid"));
                                        orders.setGeval_goodsid(object1.optString("geval_goodsid"));
                                        orders.setGeval_goodsname(object1.optString("geval_goodsname"));
                                        orders.setGeval_goodsprice(object1.optDouble("geval_goodsprice"));
                                        orders.setGeval_scores(object1.optInt("geval_scores"));
                                        orders.setGeval_content(object1.optString("geval_content"));
                                        orders.setGeval_goodsimage(object1.optString("geval_goodsimage"));
                                        orders.setGeval_isanonymous(object1.optString("geval_isanonymous").equals("0") ? false : true);
                                        orders.setGeval_addtime(object1.optLong("geval_addtime"));
                                        orders.setGeval_storeid(object1.optString("geval_storeid"));
                                        orders.setGeval_storename(object1.optString("geval_storename"));
                                        orders.setGeval_frommemberid(object1.optString("geval_frommemberid"));
                                        orders.setGeval_frommembername(object1.optString("geval_frommembername"));
                                        orders.setGeval_image(object1.optString("geval_image"));
                                        orders.setGeval_explain(object1.optString("geval_explain"));
                                        if (dataList.size() < 3) {
                                            dataList.add(orders);
                                        }
                                        commentItemFragmentAdapter.addItems(dataList);
                                        commentItemFragmentAdapter.notifyDataSetChanged();
                                    }
                                }
                                if (dataList != null && linearComment != null) {
                                    if (dataList.size() == 0) {
                                        linearComment.setVisibility(View.GONE);
                                    } else {
                                        linearComment.setVisibility(View.VISIBLE);
                                    }
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
//        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, TLUrl.getInstance().URL_hwg_goods_comment2 + "&goods_id=" + goods_id + "&type=all" + "&curpage=1", null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    if (response.getInt("code") == 200) {
//                        Log.i("zjz", "all_comment:连接成功");
//                        Log.i("zjz", "response=" + response);
//                        if (response.getInt("page_total") != 0) {
//                            JSONObject object = response.getJSONObject("datas");
//                            JSONArray groupArray = object.getJSONArray("goodsevallist");
//                            JSONObject jsonObject = object.getJSONObject("goods_evaluate_info");
//                            if (tComment != null)
//                                tComment.setText("商品评论(" + jsonObject.optString("all") + ")");
//                            dataList.clear();
//                            for (int i = 0; i < groupArray.length(); i++) {
//                                Comment orders = new Comment();
//                                orders.setId(i);
//                                JSONObject object1 = groupArray.getJSONObject(i);
//                                orders.setGeval_id(object1.optString("geval_id"));
//                                orders.setGeval_orderid(object1.optString("geval_orderid"));
//                                orders.setGeval_orderno(object1.optString("geval_orderno"));
//                                orders.setGeval_ordergoodsid(object1.optString("geval_ordergoodsid"));
//                                orders.setGeval_goodsid(object1.optString("geval_goodsid"));
//                                orders.setGeval_goodsname(object1.optString("geval_goodsname"));
//                                orders.setGeval_goodsprice(object1.optDouble("geval_goodsprice"));
//                                orders.setGeval_scores(object1.optInt("geval_scores"));
//                                orders.setGeval_content(object1.optString("geval_content"));
//                                orders.setGeval_goodsimage(object1.optString("geval_goodsimage"));
//                                orders.setGeval_isanonymous(object1.optString("geval_isanonymous").equals("0") ? false : true);
//                                orders.setGeval_addtime(object1.optLong("geval_addtime"));
//                                orders.setGeval_storeid(object1.optString("geval_storeid"));
//                                orders.setGeval_storename(object1.optString("geval_storename"));
//                                orders.setGeval_frommemberid(object1.optString("geval_frommemberid"));
//                                orders.setGeval_frommembername(object1.optString("geval_frommembername"));
//                                orders.setGeval_image(object1.optString("geval_image"));
//                                orders.setGeval_explain(object1.optString("geval_explain"));
//                                if (dataList.size() < 3) {
//                                    dataList.add(orders);
//                                }
//                                commentItemFragmentAdapter.addItems(dataList);
//                                commentItemFragmentAdapter.notifyDataSetChanged();
//                            }
//                        }
//                        if (dataList != null && linearComment != null) {
//                            if (dataList.size() == 0) {
//                                linearComment.setVisibility(View.GONE);
//                            } else {
//                                linearComment.setVisibility(View.VISIBLE);
//                            }
//                        }
//
//                    } else {
//                        Log.i("zjz", "goodsActivity解析失败");
//                    }
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    Log.i("zjz", e.toString());
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        });
//        mRequestQueue.add(jr);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (banners != null) {
            banners.clearImageTimerTask();
        }
        isDestory = true;
        ButterKnife.reset(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (banners != null)
            banners.stopImageTimerTask();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (banners != null)
            banners.startImageTimerTask();
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.linear_baozhang:
                startActivity(new Intent(activity, GoodsGuaranteeActivity.class));
                break;
            case R.id.img_to_zungui_huiyuan:

                if (MyApplication.getInstance().getMykey() == null) {
                    Intent rt = new Intent(activity, WXEntryActivity.class);
                    rt.putExtra("isthome",true);
                    startActivity(rt);
                    return;
                }else {
                    if(!TextUtils.isEmpty(levelId)&&levelId.equals("4")){
                        startActivity(new Intent(activity, ShengJiHuiYuanActivity.class));
                    }else {//我的会员
                        startActivity(new Intent(activity, ShengJiHuiYuanActivity.class));
                    }
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
            case R.id.relative_more:
                intent = new Intent(activity, GoodsCommentActivity.class);
                intent.putExtra("sid", goods_id);
                startActivity(intent);
//                activity.turn2DetailComment();
//                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                break;
            case R.id.t_buy_suit:
                buySuitGoods();
                break;
            case R.id.img_tuwen:
                activity.turn2Detail();
                break;
        }
    }


    private void buySuitGoods() {
        if (MyApplication.getInstance().getMykey() == null) {
            Intent intent = new Intent(activity, WXEntryActivity.class);
            startActivity(intent);
            return;
        }
        ProgressDlgUtil.showProgressDlg("Loading...", activity);
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_buy_suitgoods, "act=member_cart&op=suit&bl_id=" + suitId + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i("zjz", "buy_suit_goods=" + msg);
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.optInt("code") == 200) {
                                if (jsonObject.optString("datas").contains("成功")) {
                                    MyUpdateUI.sendUpdateCarNum(activity);
                                    Toast.makeText(activity, "添加成功！", Toast.LENGTH_SHORT).show();
                                } else if (jsonObject.optString("datas").contains("失败")) {
                                    Toast.makeText(activity, "添加失败！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onItemRootViewClick(int position) {

    }

    @Override
    public void onImgClick(int position) {

    }

}
