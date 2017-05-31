package com.abcs.haiwaigou.local.huohang.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.activity.BindPhoneActivity;
import com.abcs.haiwaigou.activity.CompanyEditActivity;
import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.activity.InvoiceAddActivity;
import com.abcs.haiwaigou.activity.OrderActivity2;
import com.abcs.haiwaigou.adapter.HuoHangCheckOrderAdapter;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager;
import com.abcs.haiwaigou.model.CheckOrder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.model.Invoice;
import com.abcs.haiwaigou.model.SDMangSong;
import com.abcs.haiwaigou.model.Voucher;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.haiwaigou.view.TabLayout.OnInitSelectedPosition;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.NoticeDialogActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.dialog.ShowMessageDialog;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.nineoldandroids.animation.ObjectAnimator;
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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class HuoHangCheckOrderActivity extends BaseActivity implements View.OnClickListener {


    JSONArray jsonArray = new JSONArray();
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.rl_choose)
    RelativeLayout rlChoose;
    @InjectView(R.id.t_name)
    TextView tName;
    @InjectView(R.id.t_phone)
    TextView tPhone;
    @InjectView(R.id.t_address)
    TextView tAddress;
    //    @InjectView(R.id.tljr_zListView)
//    ZrcListView tljrZListView;
    @InjectView(R.id.linear_address)
    RelativeLayout linearAddress;
    @InjectView(R.id.img_edit)
    ImageView imgEdit;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.tv_pay)
    Button tvPay;
    @InjectView(R.id.tv_huodaofukuan)
    TextView tv_huodaofukuan;
    @InjectView(R.id.cb_isuse)
    CheckBox cbIsuse;
    @InjectView(R.id.t_remain)
    TextView tRemain;
    @InjectView(R.id.linear_chongzhi)
    RelativeLayout linearChongzhi;  //  充值卡支付
    @InjectView(R.id.ed_pwd)
    EditText edPwd;
    @InjectView(R.id.t_use)
    TextView tUse;
    @InjectView(R.id.linear_pay_pdw)
    LinearLayout linearPayPdw;  // 支付密码
    @InjectView(R.id.linear_no_paypwd)
    LinearLayout linearNoPaypwd;  //  密码提示
    @InjectView(R.id.t_invoice)
    TextView tInvoice;
    @InjectView(R.id.img_invoice)
    ImageView imgInvoice;
    @InjectView(R.id.relative_invoice)
    RelativeLayout relativeInvoice;
    @InjectView(R.id.zlist_invoice)
    ZrcListView zlistInvoice;
    @InjectView(R.id.img_vouncher)
    ImageView imgVouncher;
    @InjectView(R.id.relative_vouncher)
    RelativeLayout relativeVouncher;
    @InjectView(R.id.spinner_vouncher)
    Spinner spinnerVouncher;
    @InjectView(R.id.linear_cz)
    LinearLayout linearCZ;
    @InjectView(R.id.t_freight)
    TextView tFreight;  // 运费
    @InjectView(R.id.t_store_total)
    TextView tStoreTotal;  // 合计
    @InjectView(R.id.t_store_name)
    TextView tStoreName;
    @InjectView(R.id.t_mansong)
    TextView tMansong;
    @InjectView(R.id.t_mansong_desc)
    TextView tMansongDesc;
    @InjectView(R.id.t_id_card)
    TextView tIdCard;
    @InjectView(R.id.cb_isuse1)
    CheckBox cbIsuse1;
    @InjectView(R.id.t_remain1)
    TextView tRemain1;
    @InjectView(R.id.linear_yck)
    LinearLayout linearYCK; //  预存款支付
    @InjectView(R.id.t_no2)
    TextView tNo2; // 提示
    @InjectView(R.id.linear_mansong)
    LinearLayout linearMansong;
    @InjectView(R.id.t_manjian)
    TextView tManjian;
    @InjectView(R.id.t_manjian_desc)
    TextView tManjianDesc;
    @InjectView(R.id.linear_manjian)
    LinearLayout linearManjian;
    @InjectView(R.id.t_set_paypwd)
    TextView tSetPaypwd;
    @InjectView(R.id.cb_isuse_company)
    CheckBox cbIsuseCompany;
    @InjectView(R.id.cb_is_xiaofeiquan)
    CheckBox cb_is_xiaofeiquan;
    @InjectView(R.id.t_discount)
    TextView tDiscount;
    @InjectView(R.id.t_bind_name)
    TextView tBindName;
    @InjectView(R.id.t_edit)
    TextView tEdit;
    @InjectView(R.id.relative_company)
    RelativeLayout relativeCompany;  // 公司
    @InjectView(R.id.cb_isuseyyg)
    CheckBox cbIsuseyyg;
    @InjectView(R.id.t_remain_yyg)
    TextView tRemainYyg;
    @InjectView(R.id.t_remain_xiaofeiquan)
    TextView t_remain_xiaofeiquan;
    @InjectView(R.id.linear_yyg)
    LinearLayout linearYyg; //  云购支付
    @InjectView(R.id.linear_xiaofeiquan)
    LinearLayout linear_xiaofeiquan;  //  消费券支付
    @InjectView(R.id.relative_help)
    RelativeLayout relativeHelp;
    @InjectView(R.id.linear_rules)
    LinearLayout linearRules;
    String cart_id;
    public static ArrayList<String> stores = new ArrayList<String>();
    public ArrayList<Goods> goodsList = new ArrayList<Goods>();
    HuoHangCheckOrderAdapter checkOrderAdapter;
    FullyLinearLayoutManager fullyLinearLayoutManager;
    public static String params[];
    public Handler handler = new Handler();
    String address_id;
    @InjectView(R.id.t_vouncher_num)
    TextView tVouncherNum;
    @InjectView(R.id.tv_shuilv_ne2)
    TextView tvShuilvNe2;
    @InjectView(R.id.tv_shuilv_m2)
    TextView tvShuilvM2;
    @InjectView(R.id.tv_shuilv_br2)
    TextView tvShuilvBr2;
    @InjectView(R.id.tv_total_Netto)
    TextView tvTotalNetto;
    @InjectView(R.id.tv_total_Mwst)
    TextView tvTotalMwst;
    @InjectView(R.id.tv_total_Bru)
    TextView tvTotalBru;
    @InjectView(R.id.liner_hdong2)
    LinearLayout linerHdong;
    @InjectView(R.id.liner_shui_root)
    LinearLayout liner_shui_root;
    @InjectView(R.id.lin_top)
    View lin_top;
    @InjectView(R.id.lin_bottom)
    View lin_bottom;
    @InjectView(R.id.tv_shifu_price)  // 实付金额
    TextView tvShifuPrice;
    @InjectView(R.id.tv_shifu_price_shuiqian)
    TextView tvShifuPriceShuiqian;

    @InjectView(R.id.liner_mang_pop)
    RelativeLayout liner_mang_pop;
    @InjectView(R.id.tv_mang_price)
    TextView tv_mang_price;
    @InjectView(R.id.tv_song)
    TextView tv_song;
    @InjectView(R.id.iv_wenhao)
    ImageView iv_wenhao;

    @InjectView(R.id.tv_have_fapiao)  // 需要发票
    LinearLayout tv_have_fapiao;
    @InjectView(R.id.iv_have_piao)// 需要发票
    ImageView iv_have_piao;
    @InjectView(R.id.tv_no_fapiao)// 不需要发票  默认
    LinearLayout tv_no_fapiao;
    @InjectView(R.id.iv_no_piao)// 不需要发票  默认
    ImageView iv_no_piao;
    @InjectView(R.id.relat_null)// 空布局 蒙版
    RelativeLayout relat_null;


    private int durationRotate = 200;// 旋转动画时间
    private int durationAlpha = 200;// 透明度动画时间
    ArrayList<Invoice> provinceList = new ArrayList<Invoice>();
    boolean isChoose;
    boolean isRefresh;
    ListAdapter listAdapter;

    private String store;
    double good_price = 0;
    double good_freight = 0;
    double mansong_discount = 0;
    double total = 0;
    double totalTemp = 0;
    double voucherTemp = 0;
    double useTemp = 0;
    String vat_hash;
    String freight_hash;
    String offpay_hash;
    String offpay_hash_batch;
    String invoice_id = "undefined";
    String vourch = "";

    String rule_id;
    String address;
    String address2;
    String name;
    String phone;
    String tel_phone;
    String idCard;
    String area_id;
    String city_id;
    String discount;
    String discountTemp;
    double discountNum;
    boolean isSelectRcb = false;//是否选中使用充值卡
    boolean isSelectPd = false; //是否选中使用预存款
    boolean isSelectYYG = false; //是否选中使用云购余额
    boolean isSelectXFQuan = false; //是否选中使用消费券
    boolean isUseCZ = false;//充值卡选择
    boolean isUseYCK = false;  //预存款选择
    boolean isUseYYG = false;  //云购余额选择
    boolean isUseXFQ = false;  //云购余额选择
    boolean isUse = false; //是否已使用充值卡或预存款(最后结算参数判断)
    double rcNum = 0.0; //预存款余额
    double pdNum = 0.0;//充值卡余额
    double yygNum = 0.0;//云购余额
    double xfQuanNum = 0.0;//云购余额
    String pwd;
    int rcb_pay = 0;//充值卡支付（0使用 1未使用）
    int pd_pay = 0;//预存款支付（0使用 1未使用）
    int yyg_pay = 0;//云购余额支付（0使用 1未使用）
    int vip_pay = 0;//消费券支付（0未使用 1使用）
    String paypwd;
    boolean isVouncher;//是否有代金券
    boolean isChongzhi;//是否有充值卡
    boolean isYucun;//是否有预存款
    boolean isXFQuan;//是否有有消费券
    boolean isYYG;//是否有云购余额
    boolean isCompany;//企业优惠选择
    boolean isUseVou;//是否使用代金券

    boolean isBindEmail;//是否绑定邮箱
    boolean isBindPhone;//是否绑定手机
    boolean isBindSuccess;//是否绑定成功

    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (rootView == null) {
            rootView = getLayoutInflater().inflate(R.layout.huohang_activity_check_order, null);
        }
        setContentView(rootView);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        cart_id = intent.getStringExtra("cart_id");
        params = new String[stores.size()];

        try {
            Typeface type= Typeface.createFromAsset(getAssets(),"font/ltheijian.TTF");
            if(type!=null){
                tvShifuPrice.setTypeface(type) ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        initListView();
        initUser();
        initRecyclerView();
//        initView();
        setOnListener();
//        initInvoice();
        initInvoiceList();

//        initGridTags();
    }

/*    private void initGridTags(){
        
        
        if (item.datas.tagList != null && item.datas.tagList.size() > 0) {
            //属性标签
            TagAdapter mMobileTagAdapter = new TagAdapter<>(HuoHangCheckOrderActivity.this);
            gridTags.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
            gridTags.setAdapter(mMobileTagAdapter);

            gridTags.setOnTagSelectListener(new OnTagSelectListener() {
                @Override
                public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                    if (selectedList != null && selectedList.size() > 0) {


                        StringBuilder sb = new StringBuilder();

                        for (int i : selectedList) {
                            sb.append(parent.getAdapter().getItem(i));
                            sb.append(":");
                        }
                        Log.d(TAG, "onItemSelect: "+sb.toString());
                    }
                }
            });

            mMobileTagAdapter.onlyAddAll(item.datas.tagList);

        }
    }*/
    private void initUser() {
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_member, "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "msg=" + msg);
                                JSONObject data = object.getJSONObject("datas");
                                if (data.optString("member_email_bind").equals("1")) {
                                    isBindEmail = true;
                                } else if (!data.optString("member_email").equals("null")) {
                                    isBindSuccess = true;
                                } else {
                                    isBindSuccess = false;
                                    isBindEmail = false;
                                }
                                if (data.optString("member_mobile_bind").equals("1")) {
//                                    phone = data.optString("member_mobile");
                                    isBindPhone = true;
                                } else {
                                    isBindPhone = false;
                                }
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

    private void initOffPayHash() {
        tName.setText(name.trim());
        if (phone.length() > 0) {
            tPhone.setText(phone);
        } else if (tel_phone.length() > 0) {
            tPhone.setText(tel_phone);
        }

        if (!TextUtils.isEmpty(address) && !TextUtils.isEmpty(address2)) {
            tAddress.setText("收货地址：" + address + " " + address2);
        } else if (!TextUtils.isEmpty(address)) {
            tAddress.setText("收货地址：" + address);
        } else if (!TextUtils.isEmpty(address2)) {
            tAddress.setText("收货地址：" + address2);
        }

        tIdCard.setText(idCard);
        rlChoose.setVisibility(GONE);
        linearAddress.setVisibility(VISIBLE);
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        Log.i("zjz", "change_address=" + "&key=" + MyApplication.getInstance().getMykey() + "&area_id=" + area_id + "&city_id=" + city_id + "&freight_hash=" + freight_hash);
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_address_change, "&key=" + MyApplication.getInstance().getMykey() + "&area_id=" + area_id + "&city_id=" + city_id + "&freight_hash=" + freight_hash, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                JSONObject object1 = object.getJSONObject("datas");
                                if (object1.has("error")) {
                                    showToast("订单中存在无法购买的商品，请重新下单！");
                                } else {
                                    offpay_hash = object1.optString("offpay_hash");
                                    offpay_hash_batch = object1.optString("offpay_hash_batch");
                                }

                                Log.i("zjz", "msg_offpayhash=" + msg);
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

    private void initRecyclerView() {
        checkOrderAdapter = new HuoHangCheckOrderAdapter(this);
        fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
        recyclerview.setFocusable(false);
//        recyclerView.setLayoutManager(fullyLinearLayoutManager);
        recyclerview.setLayoutManager(fullyLinearLayoutManager);
        recyclerview.setAdapter(checkOrderAdapter);
//        initAllDates();
//        initDatas();
        initCheckDatas();
    }

    private void setOnListener() {
        linearYCK.setOnClickListener(this);
        linearCZ.setOnClickListener(this);
        rlChoose.setOnClickListener(this);
        relativeBack.setOnClickListener(this);
        tv_have_fapiao.setOnClickListener(this);
        tv_no_fapiao.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
        tvPay.setOnClickListener(this);
        tv_huodaofukuan.setOnClickListener(this);
        tUse.setOnClickListener(this);
        relativeInvoice.setOnClickListener(this);
        cbIsuse.setOnClickListener(this);
        cbIsuse1.setOnClickListener(this);
        linearAddress.setOnClickListener(this);
        tSetPaypwd.setOnClickListener(this);
        relativeCompany.setOnClickListener(this);
        cbIsuseCompany.setOnClickListener(this);
        tEdit.setOnClickListener(this);
        linearYyg.setOnClickListener(this);
        cbIsuseyyg.setOnClickListener(this);
        cb_is_xiaofeiquan.setOnClickListener(this);
        relativeHelp.setOnClickListener(this);
        linear_xiaofeiquan.setOnClickListener(this);
    }

    ArrayList<CheckOrder> orderses = new ArrayList<CheckOrder>();
    ArrayList<Voucher> vouchers = new ArrayList<Voucher>();
    ArrayList<String> rules = new ArrayList<String>();

    private String store_id;
    private String vipMoney;

    private void initCheckDatas() {
        typeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        Log.d("zds", "initCheckDatas: " + TLUrl.getInstance().URL_hwg_buy_step1 + "&key=" + MyApplication.getInstance().getMykey() + cart_id + "&ifcart=1" + "&userId=" + MyApplication.getInstance().self.getId());
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_buy_step1, "key=" + MyApplication.getInstance().getMykey() + cart_id + "&ifcart=1" + "&userId=" + MyApplication.getInstance().self.getId(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                checkOrderAdapter.getCheckOrders().clear();
                                Log.i("zjz", "msg_buy_step1=" + msg);
                                JSONObject object1 = object.getJSONObject("datas");
                                if (object1.optBoolean("state")) {
                                    JSONObject data = object1.getJSONObject("data");

                                    if (linearNoPaypwd != null)
                                        linearNoPaypwd.setVisibility(data.optBoolean("member_paypwd") ? GONE : GONE);
//                                        linearNoPaypwd.setVisibility(data.optBoolean("member_paypwd") ? View.GONE : View.VISIBLE);
                                    if (linear_xiaofeiquan != null)

                                        linear_xiaofeiquan.setVisibility(GONE);
//                                        linear_xiaofeiquan.setVisibility(Double.valueOf(data.optString("vip_money"))>0 ? View.VISIBLE : View.GONE);
                                    if (data.optString("no_rcb").equals("0") && data.has("available_rc_balance") && linearCZ != null && tRemain != null) {
                                        linearCZ.setVisibility(VISIBLE);
                                        isChongzhi = true;
                                        rcNum = data.optDouble("available_rc_balance");
                                        tRemain.setText(NumberUtils.formatPrice(rcNum));
                                    } else if (linearCZ != null) {
                                        isChongzhi = false;
                                        linearCZ.setVisibility(GONE);
                                    }

                                    linearYCK.setVisibility(GONE);

                                    if (data.has("available_predeposit") && linearYCK != null && tRemain1 != null) {

//                                        linearYCK.setVisibility(View.VISIBLE);
                                        pdNum = data.optDouble("available_predeposit");
                                        tRemain1.setText(NumberUtils.formatPrice(pdNum));
                                        isYucun = true;
                                    } else if (linearYCK != null) {
                                        isYucun = false;
                                        linearYCK.setVisibility(GONE);
                                    }

                                    linearYyg.setVisibility(GONE);

                                    if (data.has("userMoney") && linearYyg != null && tRemainYyg != null) {

//                                        linearYyg.setVisibility(View.VISIBLE);
                                        isYYG = true;
                                        yygNum = data.optDouble("userMoney");
                                        tRemainYyg.setText(NumberUtils.formatPrice(yygNum));
                                    } else if (linearYyg != null) {
                                        isYYG = false;
                                        linearYyg.setVisibility(GONE);
                                    }
                                    /*************使用消费券******************/

                                    if (data.has("vip_money") && linear_xiaofeiquan != null && t_remain_xiaofeiquan != null) {
                                        if (Double.valueOf(data.optString("vip_money")) > 0) {
                                            vipMoney = data.optString("vip_money");
//                                            linear_xiaofeiquan.setVisibility(View.VISIBLE);
                                            isXFQuan = true;
                                            xfQuanNum = Double.valueOf(data.optString("vip_money"));
                                            t_remain_xiaofeiquan.setText(NumberUtils.formatPrice(xfQuanNum));
                                        }
                                    } else if (linear_xiaofeiquan != null) {
                                        isXFQuan = false;
                                        linear_xiaofeiquan.setVisibility(GONE);
                                    }

                                    if (tNo2 != null)
                                        if (data.has("available_predeposit") && data.has("available_rc_balance") && data.has("userMoney") && data.has("vip_money") && Double.valueOf(data.optString("vip_money")) > 0
                                                || data.has("available_predeposit") && data.has("available_rc_balance") && data.has("userMoney")
                                                || data.has("available_predeposit") && data.has("available_rc_balance") && data.has("vip_money") && Double.valueOf(data.optString("vip_money")) > 0
                                                || data.has("available_predeposit") && data.has("userMoney") && data.has("vip_money") && Double.valueOf(data.optString("vip_money")) > 0
                                                || data.has("available_rc_balance") && data.has("userMoney") && data.has("vip_money") && Double.valueOf(data.optString("vip_money")) > 0
                                                || data.has("available_predeposit") && data.has("available_rc_balance")
                                                || data.has("available_predeposit") && data.has("userMoney")
                                                || data.has("available_predeposit") && data.has("vip_money") && Double.valueOf(data.optString("vip_money")) > 0
                                                || data.has("available_rc_balance") && data.has("userMoney")
                                                || data.has("available_rc_balance") && data.has("vip_money") && Double.valueOf(data.optString("vip_money")) > 0
                                                || data.has("userMoney") && data.has("vip_money") && Double.valueOf(data.optString("vip_money")) > 0)

                                        {
                                            tNo2.setVisibility(GONE);
//                                            tNo2.setVisibility(View.VISIBLE);
                                        } else {
                                            tNo2.setVisibility(GONE);
                                        }

                                    linearChongzhi.setVisibility(GONE);
//                                    linearChongzhi.setVisibility(data.has("available_rc_balance") ? View.VISIBLE : View.GONE);
                                    vat_hash = data.optString("vat_hash");
                                    freight_hash = data.optString("freight_list");
                                    JSONObject storeObject = data.getJSONObject("store_cart_list");
                                    JSONObject storeGoodsObject = data.getJSONObject("store_goods_total");
                                    CheckOrder checkOrder = new CheckOrder();
                                    for (int s = 0; s < stores.size(); s++) {
                                        Log.i("zjz", "store=" + stores.get(s));
                                        store = stores.get(s);
                                        checkOrder.setStore_id(stores.get(s));
                                        good_price = storeGoodsObject.optDouble(stores.get(s));
//                                        JSONObject object2 = storeObject.getJSONObject(stores.get(s));
                                        JSONArray jsonArray = storeObject.getJSONArray(stores.get(s));
                                        ArrayList<CheckOrder.StoreIdBean> storeIdBeans = new ArrayList<CheckOrder.StoreIdBean>();
                                        rules.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject good = jsonArray.getJSONObject(i);
                                            if (!rules.contains(good.optString("rule_id"))) {
                                                rules.add(good.optString("rule_id"));
                                            }
                                            rule_id = good.optString("rule_id");
                                            if (!good.optBoolean("state")) {
                                                Toast.makeText(HuoHangCheckOrderActivity.this, "所选商品中[" + good.optString("goods_name").substring(0, 12) + "]已经下架，无法购买，请重新下单！", Toast.LENGTH_LONG).show();
                                                tvPay.setEnabled(false);
                                                return;
                                            }
                                            if (!good.optBoolean("storage_state")) {
                                                Toast.makeText(HuoHangCheckOrderActivity.this, "所选商品中[" + good.optString("goods_name").substring(0, 12) + "]库存不足，无法购买，请重新下单！", Toast.LENGTH_LONG).show();
                                                tvPay.setEnabled(false);
                                                return;
                                            }
                                            if (good.has("bl_goods_list")) {
                                                JSONArray blgoodArray = good.getJSONArray("bl_goods_list");
                                                for (int j = 0; j < blgoodArray.length(); j++) {
                                                    JSONObject blgoods = blgoodArray.getJSONObject(j);
                                                    CheckOrder.StoreIdBean storeIdBean = new CheckOrder.StoreIdBean();
                                                    storeIdBean.setGoods_id(blgoods.optString("goods_id"));
                                                    storeIdBean.setGoods_image_url(TLUrl.getInstance().URL_hwg_comment_goods + blgoods.optString("store_id") + "/" + blgoods.optString("goods_image"));
                                                    storeIdBean.setGoods_price(blgoods.optDouble("bl_goods_price"));
                                                    storeIdBean.setGoods_name(blgoods.optString("goods_name"));
                                                    storeIdBean.tax_rate = blgoods.optString("tax_rate");
                                                    storeIdBean.goods_serial = blgoods.optString("goods_serial");
                                                    storeIdBean.goods_en_name = blgoods.optString("goods_en_name");
                                                    storeIdBean.setGoods_num(good.optString("goods_num"));
                                                    storeIdBean.setStore_name(good.optString("store_name"));

                                                    storeIdBean.setGoods_freight(good.optDouble("goods_freight", 0));
                                                    storeIdBean.setGoods_total(good.optDouble("goods_total"));
                                                    storeIdBeans.add(storeIdBean);
                                                }
                                            } else {
                                                CheckOrder.StoreIdBean storeIdBean = new CheckOrder.StoreIdBean();
                                                storeIdBean.setGoods_id(good.optString("goods_id"));
                                                store_id = good.optString("store_id");
                                                storeIdBean.setGoods_image_url(good.optString("goods_image_url"));
                                                storeIdBean.setGoods_price(good.optDouble("goods_price"));
                                                storeIdBean.setGoods_name(good.optString("goods_name"));
                                                storeIdBean.tax_rate = good.optString("tax_rate");
                                                storeIdBean.goods_serial = good.optString("goods_serial");
                                                storeIdBean.goods_en_name = good.optString("goods_en_name");
                                                storeIdBean.setGoods_num(good.optString("goods_num"));
                                                storeIdBean.setStore_name(good.optString("store_name"));

                                                storeIdBean.setGoods_freight(good.optDouble("goods_freight", 0));
                                                storeIdBean.setGoods_total(good.optDouble("goods_total"));
                                                storeIdBeans.add(storeIdBean);
                                            }

//                                            good_price += good.optDouble("goods_price") * Integer.valueOf(good.optString("goods_num"));
                                            if (tStoreName != null)
                                                tStoreName.setText(good.optString("store_name"));
                                            good_freight = good.optDouble("goods_freight", 0);
                                            if (tFreight != null)
                                                tFreight.setText(NumberUtils.formatPrice(good_freight));
                                        }
                                        JSONObject cancelObject = data.optJSONObject("cancel_calc_sid_list");
                                        if (cancelObject != null) {
                                            good_freight = 0;
                                            if (tFreight != null)
                                                tFreight.setText(NumberUtils.formatPrice(good_freight));
                                            JSONObject calObject = cancelObject.optJSONObject(stores.get(s));
                                            if (linearManjian != null)
                                                linearManjian.setVisibility(VISIBLE);
                                            if (tManjian != null)
                                                tManjian.setVisibility(VISIBLE);
                                            if (tManjianDesc != null) {
                                                tManjianDesc.setVisibility(VISIBLE);
                                                tManjianDesc.setText(calObject.optString("desc"));
                                            }
                                        }
                                        //满送规则
                                        ArrayList<CheckOrder.ManSongBean> manSongBeans = new ArrayList<CheckOrder.ManSongBean>();
                                        JSONObject mansongObject = data.optJSONObject("store_mansong_rule_list");
                                        if (mansongObject != null) {
                                            JSONObject preObject = data.optJSONObject("store_premiums_list");

                                            for (int k = 0; k < stores.size(); k++) {
                                                JSONArray mansongArray = mansongObject.optJSONArray(stores.get(k));
                                                if (mansongArray != null && linearRules != null) {
                                                    linearRules.removeAllViews();
                                                    linearRules.invalidate();
                                                    for (int m = 0; m < mansongArray.length(); m++) {
                                                        View view = getLayoutInflater().inflate(R.layout.hwg_item_checkorder_rules, null);
                                                        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dip2px(HuoHangCheckOrderActivity.this, 25));
                                                        view.setLayoutParams(params);
                                                        TextView t_mansong = (TextView) view.findViewById(R.id.t_mansong);
                                                        TextView t_mansong_desc = (TextView) view.findViewById(R.id.t_mansong_desc);
                                                        ImageView img_goods = (ImageView) view.findViewById(R.id.img_goods);
                                                        final JSONObject mObj = mansongArray.getJSONObject(m);
                                                        if (mObj.optDouble("discount") != 0) {
                                                            //满减
                                                            for (int n = 0; n < rules.size(); n++) {
                                                                if (rules.get(n).equals(mObj.optString("rule_ids"))) {
                                                                    if (mObj.optDouble("price") < 50) {
                                                                        t_mansong.setText("店铺活动:满即送");
                                                                    } else {
                                                                        t_mansong.setText("店铺活动:满即减");
                                                                        mansong_discount = Double.valueOf(mObj.optString("discount", "0"));
                                                                    }
                                                                    t_mansong_desc.setText(mObj.optString("rule_name"));
                                                                    t_mansong_desc.setTextColor(getResources().getColor(R.color.blue));
                                                                    img_goods.setVisibility(GONE);

                                                                    linearRules.addView(view);
                                                                }
                                                            }
//                                                            if (rule_id.equals(mObj.optString("rule_id"))) {
//                                                                if (linearMansong != null)
//                                                                    linearMansong.setVisibility(View.VISIBLE);
//                                                                if (tMansong != null) {
//                                                                    tMansong.setVisibility(View.VISIBLE);
//                                                                    tMansong.setText("店铺活动:满即减");
//                                                                }
//                                                                if (tMansongDesc != null) {
//                                                                    tMansongDesc.setVisibility(View.VISIBLE);
//                                                                    tMansongDesc.setText(mObj.optString("rule_name"));
//                                                                    tMansongDesc.setTextColor(getResources().getColor(R.color.blue));
//                                                                    mansong_discount = Double.valueOf(mObj.optString("discount", "0"));
//                                                                }
//                                                            }
                                                        } else {
                                                            //满送

                                                            for (int n = 0; n < rules.size(); n++) {
                                                                if (rules.get(n).equals("0")) {
                                                                    t_mansong.setText("店铺活动:满即送");
                                                                    t_mansong_desc.setText(mObj.optString("rule_name"));
                                                                    t_mansong_desc.setTextColor(getResources().getColor(R.color.blue));
                                                                    ImageLoader.getInstance().displayImage(mObj.optString("goods_image_url").replaceAll("_240.jpg", "_60.jpg"), img_goods, Options.getListOptions());
                                                                    img_goods.setVisibility(VISIBLE);
                                                                    img_goods.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            Intent intent = new Intent(HuoHangCheckOrderActivity.this, GoodsDetailActivity.class);
                                                                            intent.putExtra("sid", mObj.optString("goods_id"));
                                                                            intent.putExtra("pic", mObj.optString("goods_image_url"));
                                                                            startActivity(intent);
                                                                        }
                                                                    });
                                                                    linearRules.addView(view);
                                                                }
                                                            }
//                                                            if (rule_id.equals("0")) {
//                                                                if (linearMansong != null)
//                                                                    linearMansong.setVisibility(View.VISIBLE);
//                                                                if (tMansong != null) {
//                                                                    tMansong.setVisibility(View.VISIBLE);
//                                                                    tMansong.setText("店铺活动:满即送");
//                                                                }
//                                                                if (tMansongDesc != null) {
//                                                                    tMansongDesc.setVisibility(View.VISIBLE);
//                                                                    tMansongDesc.setText(mObj.optString("rule_name"));
//                                                                    tMansongDesc.setTextColor(getResources().getColor(R.color.blue));
//                                                                    tMansongDesc.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
//                                                                    tMansongDesc.getPaint().setAntiAlias(true);//抗锯齿
//                                                                    tMansongDesc.setOnClickListener(new View.OnClickListener() {
//                                                                        @Override
//                                                                        public void onClick(View v) {
//                                                                            Intent intent = new Intent(CheckOrderActivity.this, GoodsDetailActivity.class);
//                                                                            intent.putExtra("sid", mObj.optString("goods_id"));
//                                                                            intent.putExtra("pic", mObj.optString("goods_image_url"));
//                                                                            startActivity(intent);
//                                                                        }
//                                                                    });
//                                                                }
//                                                            }
                                                        }
                                                    }
                                                } else {
                                                    final JSONObject manObject = mansongObject.optJSONObject(stores.get(k));
                                                    if (manObject != null) {
                                                        CheckOrder.ManSongBean manSongBean = new CheckOrder.ManSongBean();
                                                        manSongBean.setDesc(manObject.optString("rule_name"));
                                                        manSongBean.setRule_id(manObject.optString("rule_id"));
                                                        manSongBean.setPrice(manObject.optDouble("price"));
                                                        manSongBean.setDiscount(manObject.optDouble("discount"));
                                                        if (linearMansong != null)
                                                            linearMansong.setVisibility(VISIBLE);
                                                        if (tMansong != null)
                                                            tMansong.setVisibility(VISIBLE);
                                                        if (tMansongDesc != null) {
                                                            tMansongDesc.setVisibility(VISIBLE);
                                                            if (preObject != null) {
                                                                tMansongDesc.setText(manObject.optString("mansong_name"));
                                                                tMansongDesc.setTextColor(getResources().getColor(R.color.blue));
                                                                tMansongDesc.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                                                                tMansongDesc.getPaint().setAntiAlias(true);//抗锯齿
                                                                tMansongDesc.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        Intent intent = new Intent(HuoHangCheckOrderActivity.this, GoodsDetailActivity.class);
                                                                        intent.putExtra("sid", manObject.optString("goods_id"));
                                                                        intent.putExtra("pic", manObject.optString("goods_image_url"));
                                                                        startActivity(intent);
                                                                    }
                                                                });
                                                            } else {
                                                                tMansongDesc.setText(manObject.optString("desc"));
                                                            }
                                                        }
                                                        mansong_discount = Double.valueOf(manObject.optString("discount", "0"));
                                                        Log.i("zjz", "mansong_discount=" + mansong_discount);
                                                        manSongBeans.add(manSongBean);
                                                        checkOrder.setMansong_list(manSongBeans);
                                                    }
                                                }
                                            }
                                        }
                                        checkOrder.setStore_list(storeIdBeans);
                                        checkOrderAdapter.getCheckOrders().add(checkOrder);
                                    }


//                                    JSONObject mansongObject = data.optJSONObject("store_mansong_rule_list");
//                                    if (mansongObject != null) {
//                                        for (int k = 0; k < stores.size(); k++) {
//                                            JSONObject manObject = mansongObject.optJSONObject(stores.get(k));
//                                            ArrayList<CheckOrder.ManSongBean> manSongBeans = new ArrayList<CheckOrder.ManSongBean>();
//                                            CheckOrder.ManSongBean manSongBean = new CheckOrder.ManSongBean();
//                                            manSongBean.setDesc(manObject.optString("desc"));
//                                            manSongBean.setRule_id(manObject.optString("rule_id"));
//                                            manSongBean.setPrice(manObject.optDouble("price"));
//                                            manSongBean.setDiscount(manObject.optDouble("discount"));
//                                            mansong_discount = Double.valueOf(manObject.optString("discount","0"));
//                                            Log.i("zjz","mansong_discount="+mansong_discount);
//                                            manSongBeans.add(manSongBean);
//                                            checkOrder.setMansong_list(manSongBeans);
//                                        }
//                                    }
//                                    checkOrderAdapter.getCheckOrders().add(checkOrder);

                                    liner_shui_root.setVisibility(VISIBLE);

                                    /*首单满送*/
                                    if(data.has("native_rule_money")){
                                        final int  native_rule_money = data.optInt("native_rule_money");
                                        if(native_rule_money>0){
                                            liner_mang_pop.setVisibility(VISIBLE);
                                            tv_mang_price.setText("满"+native_rule_money);
                                        }else {
                                            liner_mang_pop.setVisibility(GONE);
                                        }
                                    }

                                    if(data.has("native_rule_message")){
                                        final String  native_rule_message = data.optString("native_rule_message");
                                        if(!TextUtils.isEmpty(native_rule_message)){
                                            tv_mang_price.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    tiShiPop(native_rule_message);
                                                }
                                            });
                                            iv_wenhao.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    tiShiPop(native_rule_message);
                                                }
                                            });
                                        }
                                    }

                                    if(data.has("native_rule_list")){
                                        final JSONArray  native_rule_list = data.optJSONArray("native_rule_list");

                                        if(native_rule_list.length()>0){
                                            final ArrayList<SDMangSong> list_mang=new ArrayList<SDMangSong>();
                                            list_mang.clear();
                                            liner_mang_pop.setOnClickListener(new View.OnClickListener() {  // 弹窗 首单满送
                                                @Override
                                                public void onClick(View v) {
                                                    View itemView = View.inflate(HuoHangCheckOrderActivity.this, R.layout.huoh_danmang_song, null);
                                                    TextView tv_cancle = (TextView) itemView.findViewById(R.id.tv_cancle);
                                                    LinearLayout liner_content = (LinearLayout) itemView.findViewById(R.id.liner_content);

                                                    final PopupWindow popupWindow_store = new PopupWindow();
                                                    popupWindow_store.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                                                    popupWindow_store.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                                                    popupWindow_store.setContentView(itemView);

                                                    popupWindow_store.setAnimationStyle(R.style.popUpwindow_anim);//设置弹出和消失的动画
                                                    //触摸点击事件
                                                    popupWindow_store.setTouchable(true);
                                                    //聚集
                                                    popupWindow_store.setFocusable(true);
                                                    //设置允许在外点击消失
                                                    popupWindow_store.setOutsideTouchable(false);
                                                    //点击返回键popupwindown消失
                                                    popupWindow_store.setBackgroundDrawable(new BitmapDrawable());
                                                    //背景变暗
                                                    WindowManager.LayoutParams params = getWindow().getAttributes();
                                                    params.alpha = 1f;
                                                    getWindow().setAttributes(params);
                                                    popupWindow_store.setTouchInterceptor(new View.OnTouchListener() {
                                                        @Override
                                                        public boolean onTouch(View v, MotionEvent event) {
                                                            return false;
                                                        }
                                                    });
                                                    //监听如果popupWindown消失之后背景变亮
                                                    popupWindow_store.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                                        @Override
                                                        public void onDismiss() {
                                                            WindowManager.LayoutParams params = getWindow()
                                                                    .getAttributes();
                                                            params.alpha = 1f;
                                                            getWindow().setAttributes(params);
                                                        }
                                                    });
                                                    popupWindow_store.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                                                    if(popupWindow_store!=null){
//                                                        popupWindow_store.showAsDropDown(liner_mang_pop);
                                                        popupWindow_store.showAtLocation(v, Gravity.BOTTOM, 0,0);//显示位置  第三个参数设置以起始点的右下角为原点，向左、上各偏移的像素
                                                    }


                                                    tv_cancle.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            rg_id=0;
                                                            tv_song.setText("(任选其一)");
                                                            tv_song.setTextColor(getResources().getColor(R.color.huoh_song));
                                                            popupWindow_store.dismiss();
                                                        }
                                                    });

                                                    liner_content.removeAllViews();
                                                    liner_content.invalidate();

                                                    for (int i = 0; i < native_rule_list.length(); i++) {
                                                        final JSONObject item = native_rule_list.optJSONObject(i);
                                                        SDMangSong mangSong=new SDMangSong();
                                                        mangSong.rg_id=item.optInt("rg_id");
                                                        mangSong.goods_id=item.optInt("goods_id");
                                                        mangSong.goods_num=item.optInt("goods_num");
                                                        mangSong.title=item.optString("title");
                                                        mangSong.money=item.optDouble("money");
                                                        list_mang.add(mangSong);

                                                        View view = View.inflate(HuoHangCheckOrderActivity.this,R.layout.text, null);
                                                        TextView tv = (TextView) view.findViewById(R.id.tv);
                                                        tv.setText(item.optString("title"));
                                                        liner_content.addView(view);

                                                        view.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
//                                                                ToastUtil.showMessage(item.optInt("rg_id")+"");
                                                                 isCheck=false;
                                                                rg_id=item.optInt("rg_id");
                                                                tv_song.setText(item.optString("title"));
                                                                tv_song.setTextColor(getResources().getColor(R.color.gray));
                                                                popupWindow_store.dismiss();
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    }

                                    //税率信息 tax_arr
                                    JSONObject tax_arr_Object = data.optJSONObject("tax_arr");
                                    if (tax_arr_Object != null) {
                                        tvShuilvNe2.setText(NumberUtils.formatPriceNo(tax_arr_Object.optDouble("netto_a")) + "\n" + NumberUtils.formatPriceNo(tax_arr_Object.optDouble("netto_b")) + "");
                                        tvShuilvM2.setText(NumberUtils.formatPriceNo(tax_arr_Object.optDouble("mwst_a")) + "\n" + NumberUtils.formatPriceNo(tax_arr_Object.optDouble("mwst_b")) + "");
                                        tvShuilvBr2.setText(NumberUtils.formatPriceNo(tax_arr_Object.optDouble("brutto_a")) + "\n" + NumberUtils.formatPriceNo(tax_arr_Object.optDouble("brutto_b")) + "");

                                        tvTotalNetto.setText(NumberUtils.formatPriceNo((tax_arr_Object.optDouble("netto_a") + tax_arr_Object.optDouble("netto_b"))) + "");
                                        tvTotalMwst.setText(NumberUtils.formatPriceNo((tax_arr_Object.optDouble("mwst_a") + tax_arr_Object.optDouble("mwst_b"))) + "");
                                        tvTotalBru.setText(NumberUtils.formatPriceNo((tax_arr_Object.optDouble("brutto_a") + tax_arr_Object.optDouble("brutto_b"))) + "");
                                    }

                                    //活动信息 activity_arr
                                    JSONArray activity_arr_Object = data.optJSONArray("activity_arr");
                                    if (activity_arr_Object != null) {

                                        linerHdong.removeAllViews();

                                        if (activity_arr_Object.length() > 0) {
                                            lin_top.setVisibility(VISIBLE);
                                            lin_bottom.setVisibility(VISIBLE);
                                            linerHdong.setVisibility(VISIBLE);
                                            for (int i = 0; i < activity_arr_Object.length(); i++) {

                                                JSONObject item = activity_arr_Object.optJSONObject(i);
                                                View view = View.inflate(HuoHangCheckOrderActivity.this, R.layout.item_hd_text2_big, null);

                                                ViewGroup parent = (ViewGroup) view.getParent();
                                                if (parent != null) {
                                                    parent.removeAllViews();
                                                }

                                                ImageView img = (ImageView) view.findViewById(R.id.img);
                                                TextView tv = (TextView) view.findViewById(R.id.tv);

                                                if (item != null) {
                                                    if (!TextUtils.isEmpty(item.optString("img"))) {
                                                        MyApplication.imageLoader.displayImage(item.optString("img"), img, MyApplication.options);
                                                    }

                                                    if (!TextUtils.isEmpty(item.optString("activity_name"))) {
                                                        tv.setText(item.optString("activity_name"));
                                                    }
                                                }

                                                linerHdong.addView(view);
                                            }
                                        } else {
                                            linerHdong.setVisibility(GONE);
                                        }
                                    }

                                    //地址信息
                                    JSONObject addressObject = data.optJSONObject("address_info");
                                    if (addressObject != null) {
                                        address = addressObject.optString("area_info");
                                        address2 = addressObject.optString("address");
                                        name = addressObject.optString("true_name");
                                        phone = addressObject.optString("mob_phone");  //手机号码
                                        tel_phone = addressObject.optString("tel_phone");  //座机号码
                                        idCard = addressObject.optString("id_card");
                                        area_id = addressObject.optString("area_id");
                                        city_id = addressObject.optString("city_id");
                                        address_id = addressObject.optString("address_id");
                                        Log.i("zjz", "area_id=" + area_id);
                                        Log.i("zjz", "city_id=" + city_id);
                                        Log.i("zjz", "freight_hash=" + freight_hash);
                                        Log.i("zjz", "address_id=" + address_id);
                                        if (area_id.equals("0") || city_id.equals("0")) {
                                            showToast("选择的地址出现未知错误，请修改改地址或重新选择地址");
                                            ProgressDlgUtil.stopProgressDlg();
                                            return;
                                        }
                                        initOffPayHash();
                                    }
                                    vouchers.clear();

                                    //优惠券
                                    JSONObject voucherObject = data.optJSONObject("store_voucher_list");
                                    if (voucherObject != null) {
                                        Voucher v1 = new Voucher();
                                        v1.setVoucher_t_title("请选择优惠券");
                                        vouchers.add(v1);
                                        typeAdapter.add(v1.getVoucher_t_title());
                                        for (int j = 0; j < stores.size(); j++) {
                                            JSONObject vObject = voucherObject.optJSONObject(stores.get(j));
                                            if (vObject != null) {
                                                Iterator<String> datas = vObject.keys();
                                                while (datas.hasNext()) {
                                                    Voucher voucher = new Voucher();
                                                    JSONObject vo = vObject.getJSONObject(datas.next());
                                                    voucher.setVoucher_t_id(vo.optString("voucher_t_id"));
                                                    voucher.setVoucher_t_store_id(vo.optString("voucher_store_id"));
                                                    voucher.setVoucher_t_price(vo.optString("voucher_price"));
                                                    voucher.setVoucher_t_title(vo.optString("voucher_title"));
                                                    voucher.setVoucher_t_desc(vo.optString("desc"));
                                                    vouchers.add(voucher);
                                                    typeAdapter.add(voucher.getVoucher_t_desc());
                                                }
                                            }
                                            initVouncher();
                                        }
                                    }
//                                total = good_price - mansong_discount;
                                    totalTemp = good_price - mansong_discount;
                                    total = totalTemp + good_freight;
//                                totalTemp = good_price - mansong_discount;
                                    Log.i("zjz", "total=" + total);
                                    //查找绑定企业
                                    initCompany();
                                }

                                tvShifuPrice.setText("€" + NumberUtils.formatPriceNo(total));

                                if (tStoreTotal != null)
//                                    tStoreTotal.setText(NumberUtils.formatPrice(total));
                                    Log.i("zjz", "list=" + checkOrderAdapter.getCheckOrders().size());
                                checkOrderAdapter.notifyDataSetChanged();

                                relat_null.setVisibility(View.GONE);

                            } else {
                                Log.i("zjz", "goodsDetail:解析失败");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
                        } finally {
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });
    }

    private int rg_id=0;
    private boolean isCheck=true;

    private void initCompany() {
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_find_company_connect, "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            Log.i("zjz", "find_company_msg=" + msg);
                            if (object.getInt("code") == 200) {
                                JSONObject errObj = object.optJSONObject("datas");
                                if (errObj == null) {
                                    JSONArray array = object.optJSONArray("datas");
                                    if (array != null && array.length() != 0) {
                                        if (relativeCompany != null)
                                            relativeCompany.setVisibility(GONE);
//                                            relativeCompany.setVisibility(View.VISIBLE);
                                        JSONObject jsonObject = array.getJSONObject(0);
                                        jsonObject.optString("id");
                                        if (tBindName != null)
                                            tBindName.setText(jsonObject.optString("enterprise_name"));
                                        if (tDiscount != null)
                                            tDiscount.setText(jsonObject.optString("discount"));
                                        jsonObject.optString("invitation_code");
                                        jsonObject.optString("member_id");
                                        jsonObject.optString("member_name");
                                        discountTemp = jsonObject.optString("discount");
                                        discountNum = jsonObject.getDouble("discount");
                                        isCompany = false;
                                        companySelect();
                                    }
                                }
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

    private void tiShiPop(String msg){
        View root_view=View.inflate(this,R.layout.pop_notices_msg2,null);
        TextView img_close=(TextView) root_view.findViewById(R.id.imge_close);
        com.abcs.haiwaigou.view.AlignTextView tv_msg=(com.abcs.haiwaigou.view.AlignTextView) root_view.findViewById(R.id.tv_msg);

        tv_msg.setText(msg);
        tv_msg.setTextSize(14.0f);

        final PopupWindow popupWindow= new PopupWindow();
        popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(root_view);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params =getWindow()
                        .getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
        popupWindow.setAnimationStyle(R.style.popWindowAnimation);//设置弹出和消失的动画
        //触摸点击事件
        popupWindow.setTouchable(true);
        //聚集
        popupWindow.setFocusable(true);
        //设置允许在外点击消失
        popupWindow.setOutsideTouchable(false);
        //点击返回键popupwindown消失

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        popupWindow.showAtLocation(root_view, Gravity.CENTER, 0, 0);    //消息弹窗
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

//    native_invoice    发票状态   1需要   2不需要

    private int native_invoice=2;
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.tv_have_fapiao:
                native_invoice=1;
                iv_have_piao.setImageResource(R.drawable.iv_check);
                iv_no_piao.setImageResource(R.drawable.iv_no_check);
                break;
            case R.id.tv_no_fapiao:
                native_invoice=2;
                iv_have_piao.setImageResource(R.drawable.iv_no_check);
                iv_no_piao.setImageResource(R.drawable.iv_check);
                break;
            case R.id.rl_choose:
                if (MyApplication.getInstance().getMykey() == null) {
                    intent = new Intent(this, WXEntryActivity.class);
                    startActivity(intent);

                } else {
                    intent = new Intent(this, HuoHangAddressActivity.class);
                    intent.putExtra("isBuy", true);
                    intent.putExtra("store_id", store_id);
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.img_edit:
                intent = new Intent(this, HuoHangAddressActivity.class);
                intent.putExtra("isBuy", true);
                intent.putExtra("store_id", store_id);
                startActivityForResult(intent, 1);
                break;
            case R.id.linear_address:
                Log.d("zds", "onClick: store_id" + store_id);

                intent = new Intent(this, HuoHangAddressActivity.class);
                intent.putExtra("isBuy", true);
                intent.putExtra("store_id", store_id);
                startActivityForResult(intent, 1);
                break;
            case R.id.tv_huodaofukuan:
//            case R.id.tv_pay:
//                confirmOrder();
                confirmMyOrder();  // 提交订单
                Log.i("zjz", "total=" + total);
                break;
            case R.id.t_use:
                confirmPwd();
                break;

            case R.id.relative_invoice:
                isChoose = !isChoose;
                if (isChoose) {
                    ObjectAnimator.ofFloat(imgInvoice, "rotation", 0, 180).setDuration(durationRotate).start();
                    zlistInvoice.setVisibility(VISIBLE);
                    ObjectAnimator.ofFloat(zlistInvoice, "alpha", 0, 1).setDuration(durationAlpha).start();
                } else {
                    ObjectAnimator.ofFloat(imgInvoice, "rotation", 180, 360).setDuration(durationRotate).start();

                    zlistInvoice.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            zlistInvoice.setVisibility(GONE);
                        }
                    }, durationAlpha);
                }
                break;
            case R.id.linear_cz:
                chongzhiSelect();
                break;
            case R.id.cb_isuse:
                chongzhiSelect();
                break;
            case R.id.linear_yck:
                yucunSelect();
                break;
            case R.id.cb_isuse1:
                yucunSelect();
                break;
            case R.id.t_set_paypwd:
                if (!isBindEmail || isBindPhone) {
                    startActivity(new Intent(HuoHangCheckOrderActivity.this, NoticeDialogActivity.class).putExtra("msg",
                            "您还未绑定手机或邮箱，请先到个人界面->更多中进行手机或邮箱的绑定！"));
                } else {
                    intent = new Intent(this, BindPhoneActivity.class);
                    intent.putExtra("isFirst", true);
                    intent.putExtra("title", "设置支付密码");
                    intent.putExtra("type", BindPhoneActivity.BINDPAYPWD);
                    startActivity(intent);
                }
                break;

            case R.id.relative_company:
                companySelect();
                break;
            case R.id.cb_isuse_company:
                companySelect();
                break;
            case R.id.t_edit:
                intent = new Intent(this, CompanyEditActivity.class);
                startActivityForResult(intent, 3);
                break;

            case R.id.cb_isuseyyg:
                yygSelect();
                break;
            case R.id.linear_yyg:
                yygSelect();
                break;
            case R.id.cb_is_xiaofeiquan:
                xfqSelect();
                break;
            case R.id.linear_xiaofeiquan:
                xfqSelect();
                break;
            case R.id.relative_help:
                new ShowMessageDialog(rootView, this, Util.WIDTH * 3 / 4, "因可能存在系统缓存、页面更新导致价格变动异常等不确定性情况出现，商品售价以本结算页商品价格为准；如有疑问，请您立即联系客服咨询。", "温馨提示");
                break;
        }
    }

    private void companySelect() {
        isCompany = !isCompany;
        if (isCompany) {
            cbIsuseCompany.setChecked(true);
            discount = discountTemp;
            if (isUse) {
                total = totalTemp * discountNum / 10 + good_freight;
                if (isSelectYYG && isSelectPd && isSelectRcb) {
                    //使用云购，预存，充值
                    total = total - yygNum - rcNum - pdNum;
                } else if (isSelectYYG && isSelectRcb) {
                    //使用云购，充值
                    total = total - yygNum - rcNum;
                } else if (isSelectYYG && isSelectPd) {
                    //使用云购，预存
                    total = total - yygNum - pdNum;
                } else if (isSelectPd && isSelectRcb) {
                    //使用充值，预存
                    total = total - rcNum - pdNum;
                } else if (isSelectRcb) {
                    //使用充值
                    total = total - rcNum;
                } else if (isSelectPd) {
                    //使用预存
                    total = total - pdNum;
                } else if (isSelectYYG) {
                    //使用云购
                    total = total - yygNum;
                } else if (isSelectXFQuan) {
                    //使用消费券
                    total = total - xfQuanNum;
                }
                if (total <= 0) {
                    total = 0.0;
                }
                tStoreTotal.setText(NumberUtils.formatPrice(total));
            } else if (isUseVou) {
                total = totalTemp * discountNum / 10 + good_freight - voucherTemp;
                tStoreTotal.setText(NumberUtils.formatPrice(total));
            } else {
                total = totalTemp * discountNum / 10 + good_freight;
                tStoreTotal.setText(NumberUtils.formatPrice(total));
            }
        } else {

            cbIsuseCompany.setChecked(false);
            discount = "";
            if (isUse) {
                total = totalTemp + good_freight;
                if (isSelectYYG && isSelectPd && isSelectRcb) {
                    //使用云购，预存，充值
                    total = total - yygNum - rcNum - pdNum;
                } else if (isSelectYYG && isSelectRcb) {
                    //使用云购，充值
                    total = total - yygNum - rcNum;
                } else if (isSelectYYG && isSelectPd) {
                    //使用云购，预存
                    total = total - yygNum - pdNum;
                } else if (isSelectPd && isSelectRcb) {
                    //使用充值，预存
                    total = total - rcNum - pdNum;
                } else if (isSelectRcb) {
                    //使用充值
                    total = total - rcNum;
                } else if (isSelectPd) {
                    //使用预存
                    total = total - pdNum;
                } else if (isSelectYYG) {
                    //使用云购
                    total = total - yygNum;
                } else if (isSelectXFQuan) {
                    //使用云购
                    total = total - xfQuanNum;
                }

                if (total <= 0) {
                    total = 0;
                }
                tStoreTotal.setText(NumberUtils.formatPrice(total));
            } else if (isUseVou) {
                total = totalTemp + good_freight - voucherTemp;
                tStoreTotal.setText(NumberUtils.formatPrice(total));
            } else {
                total = totalTemp + good_freight;
                tStoreTotal.setText(NumberUtils.formatPrice(total));
            }
        }

    }

    private void yygSelect() {
        isUse = false;
        yyg_pay = 0;
        pd_pay = 0;
        rcb_pay = 0;
        isUseYYG = !isUseYYG;
        //隐藏代金券
        if (isVouncher) {
            if (isUseCZ || isUseYCK || isUseYYG) {
                relativeVouncher.setVisibility(GONE);
            } else {
                relativeVouncher.setVisibility(VISIBLE);
            }
        }

        if (isUseYYG) {
            cbIsuseyyg.setChecked(true);
            isSelectYYG = true;
        } else {
            if (isCompany) {
                total = totalTemp * discountNum / 10 + good_freight;
            } else {
                total = totalTemp + good_freight;
            }
            tStoreTotal.setText(NumberUtils.formatPrice(total));
            isSelectYYG = false;
            cbIsuseyyg.setChecked(false);
        }
        if (isUseCZ || isUseYCK || isUseYYG || isUseXFQ) {
            linearPayPdw.setVisibility(GONE);
//            linearPayPdw.setVisibility(View.VISIBLE);
        } else {
            linearPayPdw.setVisibility(GONE);
        }

    }

    private void xfqSelect() {
        isUse = false;
        yyg_pay = 0;
        pd_pay = 0;
        rcb_pay = 0;
//        vip_pay = 1;
        isUseXFQ = !isUseXFQ;
        //隐藏代金券
        if (isVouncher) {
            if (isUseCZ || isUseYCK || isUseXFQ) {
                relativeVouncher.setVisibility(GONE);
            } else {
                relativeVouncher.setVisibility(VISIBLE);
            }
        }

        if (isUseXFQ) {
            cb_is_xiaofeiquan.setChecked(true);
            isSelectXFQuan = true;
        } else {
            if (isCompany) {
                total = totalTemp * discountNum / 10 + good_freight;
            } else {
                total = totalTemp + good_freight;
            }
            tStoreTotal.setText(NumberUtils.formatPrice(total));
            isSelectXFQuan = false;
            cb_is_xiaofeiquan.setChecked(false);
        }
        if (isUseCZ || isUseYCK || isUseYYG || isUseXFQ) {
            linearPayPdw.setVisibility(GONE);
//            linearPayPdw.setVisibility(View.VISIBLE);
        } else {
            linearPayPdw.setVisibility(GONE);
        }

    }

    private void yucunSelect() {
        isUse = false;
        yyg_pay = 0;
        pd_pay = 0;
        rcb_pay = 0;
        isUseYCK = !isUseYCK;
        if (isVouncher) {
            if (isUseCZ || isUseYCK || isUseYYG) {
                relativeVouncher.setVisibility(GONE);
            } else {
                relativeVouncher.setVisibility(VISIBLE);
            }
        }
        if (isUseYCK) {
            cbIsuse1.setChecked(true);
            isSelectPd = true;
        } else {
            if (isCompany) {
                total = totalTemp * discountNum / 10 + good_freight;
            } else {
                total = totalTemp + good_freight;
            }
            tStoreTotal.setText(NumberUtils.formatPrice(total));
            isSelectPd = false;
            cbIsuse1.setChecked(false);
        }
        if (isUseCZ || isUseYCK || isUseYYG || isUseXFQ) {
            linearPayPdw.setVisibility(GONE);
//            linearPayPdw.setVisibility(View.VISIBLE);
        } else {
            linearPayPdw.setVisibility(GONE);
        }
    }

    private void chongzhiSelect() {
        isUse = false;
        yyg_pay = 0;
        pd_pay = 0;
        rcb_pay = 0;
        isUseCZ = !isUseCZ;
        if (isVouncher) {
            if (isUseCZ || isUseYCK || isUseYYG) {
                relativeVouncher.setVisibility(GONE);
            } else {
                relativeVouncher.setVisibility(VISIBLE);
            }
        }

        if (isUseCZ) {
            cbIsuse.setChecked(true);
            isSelectRcb = true;
        } else {

            if (isCompany) {
                total = totalTemp * discountNum / 10 + good_freight;
            } else {
                total = totalTemp + good_freight;
            }
            tStoreTotal.setText(NumberUtils.formatPrice(total));
            isSelectRcb = false;
            cbIsuse.setChecked(false);

        }

        if (isUseCZ || isUseYCK || isUseYYG || isUseXFQ) {
            linearPayPdw.setVisibility(GONE);
//            linearPayPdw.setVisibility(View.VISIBLE);
        } else {
            linearPayPdw.setVisibility(GONE);
        }
    }


    class ListAdapter extends BaseAdapter {
        private ArrayList<Invoice> invoiceList;
        Activity activity;
        LayoutInflater inflater = null;
        ZrcListView listView;
        //    MyListView listView;
        public Handler handler = new Handler();


        public ListAdapter(Activity activity, ArrayList<Invoice> invoiceList,
                           ZrcListView listView) {
            // TODO Auto-generated constructor stub
            this.listView = listView;
            this.activity = activity;
            this.invoiceList = invoiceList;
            inflater = LayoutInflater.from(activity);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            // TODO Auto-generated method stub
            ProvinceViewHolder mHolder = null;
            final Invoice invoice = getItem(position);
            if (convertView == null) {
                LayoutInflater mInflater = LayoutInflater.from(activity);
                convertView = mInflater.inflate(R.layout.hwg_item_invoice, null);
                mHolder = new ProvinceViewHolder();
                mHolder.t_title = (TextView) convertView.findViewById(R.id.t_title);
                mHolder.t_content = (TextView) convertView.findViewById(R.id.t_content);
                mHolder.linear_root = (RelativeLayout) convertView.findViewById(R.id.linear_root);
                mHolder.relative_delete = (RelativeLayout) convertView.findViewById(R.id.relative_delete);

                convertView.setTag(mHolder);

            } else {
                mHolder = (ProvinceViewHolder) convertView.getTag();

            }
            mHolder.t_title.setText(invoice.getInv_title());
            mHolder.t_content.setText(invoice.getInv_content());
            mHolder.linear_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isChoose = false;
                    ObjectAnimator.ofFloat(imgInvoice, "rotation", 180, 360).setDuration(durationRotate).start();
                    zlistInvoice.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            zlistInvoice.setVisibility(GONE);
                        }
                    }, durationAlpha);
                    tInvoice.setText(invoice.getInv_title() + " " + invoice.getInv_content());
                    invoice_id = invoice.getInv_id();
                    Log.i("zjz", "invoice_id=" + invoice_id);
                }
            });
            mHolder.relative_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new PromptDialog(HuoHangCheckOrderActivity.this, "确定删除" + "[" + invoice.getInv_title() + " " + invoice.getInv_content() + "]" + "该条发票么？", new Complete() {
                        @Override
                        public void complete() {
                            ProgressDlgUtil.showProgressDlg("Loading...", HuoHangCheckOrderActivity.this);
                            HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_invoice_del, "&key=" + MyApplication.getInstance().getMykey() + "&inv_id=" + invoice.getInv_id(), new HttpRevMsg() {
                                @Override
                                public void revMsg(final String msg) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                JSONObject object = new JSONObject(msg);
                                                if (object.getInt("code") == 200) {
                                                    Log.i("zjz", "msg_invoice_del=" + msg);
                                                    if (object.optString("datas").equals("1")) {
                                                        isRefresh = true;
                                                        initInvoiceList();
                                                        showToast("删除成功！");
                                                    }
                                                } else {
                                                    Log.i("zjz", "msg_invoice_del:解析失败");
                                                }
                                                ProgressDlgUtil.stopProgressDlg();
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
                    }).show();
                }
            });
            return convertView;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return invoiceList == null ? 0 : invoiceList.size();
        }

        @Override
        public Invoice getItem(int position) {
            if (invoiceList != null && invoiceList.size() != 0) {
                if (position >= invoiceList.size()) {
                    return invoiceList.get(0);
                }
                return invoiceList.get(position);
            }
            return null;
        }


        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }


    }

    class ProvinceViewHolder {
        TextView t_title;
        TextView t_content;
        RelativeLayout linear_root;
        RelativeLayout relative_delete;
    }


    private void confirmPwd() {
        if (edPwd.getText().toString().equals("")) {
            showToast("请输入支付密码！");
            return;
        }
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_confirm_pwd, "&key=" + MyApplication.getInstance().getMykey() + "&password=" + edPwd.getText().toString(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "msg_paypwd=" + msg);
                                if (object.optInt("datas") == 1) {
                                    showToast("使用成功");
                                    if (isSelectRcb) {
                                        rcb_pay = 1;
                                    }
                                    if (isSelectPd) {
                                        pd_pay = 1;
                                    }
                                    if (isSelectYYG) {
                                        yyg_pay = 1;
                                    }
                                    if (isSelectXFQuan) {
                                        vip_pay = 1;
                                    }
                                    isUse = true;
                                    pwd = edPwd.getText().toString();
                                    linearPayPdw.setVisibility(GONE);
                                    if (isCompany) {
                                        total = totalTemp * discountNum / 10 + good_freight;
                                    } else {
                                        total = totalTemp + good_freight;
                                    }

                                    if (isSelectYYG && isSelectPd && isSelectRcb) {
                                        //使用云购，预存，充值
                                        total = total - yygNum - rcNum - pdNum;
                                    } else if (isSelectYYG && isSelectRcb) {
                                        //使用云购，充值
                                        total = total - yygNum - rcNum;
                                    } else if (isSelectYYG && isSelectPd) {
                                        //使用云购，预存
                                        total = total - yygNum - pdNum;
                                    } else if (isSelectPd && isSelectRcb) {
                                        //使用充值，预存
                                        total = total - rcNum - pdNum;
                                    } else if (isSelectRcb) {
                                        //使用充值
                                        total = total - rcNum;
                                    } else if (isSelectPd) {
                                        //使用预存
                                        total = total - pdNum;
                                    } else if (isSelectYYG) {
                                        //使用云购
                                        total = total - yygNum;
                                    } else if (isSelectXFQuan) {
                                        //使用云购
                                        total = total - xfQuanNum;
                                    }


                                    if (total <= 0)
                                        total = 0.00;
                                    tStoreTotal.setText(NumberUtils.formatPrice(total));

                                } else {
                                    showToast("密码错误！");
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


    public static String native_message;  // 买家留言；
    JSONObject jsonObject = new JSONObject();

    private void confirmMyOrder() {
        tvPay.setEnabled(false);
        String tempmessage = null;
        String pay_message = "";
        StringBuffer message = new StringBuffer("");
        for (int i = 0; i < stores.size(); i++) {
            message.append(params[i]);
        }
        pay_message = message.toString();
//        if(tempmessage.contains("null")){
//            pay_message=tempmessage.replaceAll("null","");
//        }
        if (address_id == null) {
            showToast("请选择收货地址");
            tvPay.setEnabled(true);
            return;
        }

        Log.i("zjz", "isCheck----------=" + isCheck);
        Log.i("zjz", "rg_id----------=" + rg_id);

        if (isCheck&&liner_mang_pop.getVisibility()==View.VISIBLE) {
            showToast("亲，你还没有选择一个满送的商品！");
            return;
        }
        Log.i("zjz", "pay_message=" + pay_message);

        //rcb_pay 使用充值卡  pd_pay 预存款
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        Log.i("zjz", "rcb_pay=" + rcb_pay);
        Log.i("zjz", "pd_pay=" + pd_pay);
        Log.i("zjz", "yyg_pay=" + yyg_pay);
        Log.i("zjz", "vip_pay=" + vip_pay);
        String temp = null;

        if(rg_id>0){
            if (isUse) {
                temp = "&key=" + MyApplication.getInstance().getMykey() + "&ifcart=1" + "&cart_id=" + cart_id + "&payment_type=offline" + "&address_id="
                        + address_id + "&vat_hash=" + vat_hash + "&offpay_hash=" + offpay_hash + "&offpay_hash_batch=" + offpay_hash_batch
                        + "&pay_name=offline" + "&invoice_id=" + invoice_id + "&voucher=" + jsonObject + "&rcb_pay=" + rcb_pay + "&password=" + pwd + "&pd_pay=" + pd_pay + pay_message +
                        "&login_mode=1" + "&discount=" + discount + "&yyg_pay=" + yyg_pay + "&vip_pay=" + vip_pay + "&userId=" + MyApplication.getInstance().self.getId()+"&rg_id="+rg_id+"&native_invoice="+native_invoice+"&native_message="+native_message;

            } else {
                temp = "&key=" + MyApplication.getInstance().getMykey() + "&ifcart=1" + "&cart_id=" + cart_id + "&payment_type=offline" + "&address_id="
                        + address_id + "&vat_hash=" + vat_hash + "&offpay_hash=" + offpay_hash + "&offpay_hash_batch=" + offpay_hash_batch
                        + "&pay_name=offline" + "&invoice_id=" + invoice_id + "&voucher=" + jsonObject + "&rcb_pay=" + rcb_pay + "&pd_pay=" + pd_pay + pay_message + "&login_mode=1" +
                        "&discount=" + discount + "&yyg_pay=" + yyg_pay + "&vip_pay=" + vip_pay + "&userId=" + MyApplication.getInstance().self.getId()+"&rg_id="+rg_id+"&native_invoice="+native_invoice+"&native_message="+native_message;
            }
        }else {
            if (isUse) {
                temp = "&key=" + MyApplication.getInstance().getMykey() + "&ifcart=1" + "&cart_id=" + cart_id + "&payment_type=offline" + "&address_id="
                        + address_id + "&vat_hash=" + vat_hash + "&offpay_hash=" + offpay_hash + "&offpay_hash_batch=" + offpay_hash_batch
                        + "&pay_name=offline" + "&invoice_id=" + invoice_id + "&voucher=" + jsonObject + "&rcb_pay=" + rcb_pay + "&password=" + pwd + "&pd_pay=" + pd_pay + pay_message +
                        "&login_mode=1" + "&discount=" + discount + "&yyg_pay=" + yyg_pay + "&vip_pay=" + vip_pay + "&userId=" + MyApplication.getInstance().self.getId()+"&native_invoice="+native_invoice+"&native_message="+native_message;

            } else {
                temp = "&key=" + MyApplication.getInstance().getMykey() + "&ifcart=1" + "&cart_id=" + cart_id + "&payment_type=offline" + "&address_id="
                        + address_id + "&vat_hash=" + vat_hash + "&offpay_hash=" + offpay_hash + "&offpay_hash_batch=" + offpay_hash_batch
                        + "&pay_name=offline" + "&invoice_id=" + invoice_id + "&voucher=" + jsonObject + "&rcb_pay=" + rcb_pay + "&pd_pay=" + pd_pay + pay_message + "&login_mode=1" +
                        "&discount=" + discount + "&yyg_pay=" + yyg_pay + "&vip_pay=" + vip_pay + "&userId=" + MyApplication.getInstance().self.getId()+"&native_invoice="+native_invoice+"&native_message="+native_message;
            }
        }

        Log.i("zjz", "temp=" + temp);
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_buy_step2, temp, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            Log.i("zjz", "msg_confirm_order=" + msg);
                            if (object.getInt("code") == 200) {
                                JSONObject object1 = object.optJSONObject("datas");
                                if (object1 != null) {
                                    JSONObject payinfo = object1.getJSONObject("pay_info");
                                    if (payinfo.has("pay_sn") && payinfo != null) {

                                        MyUpdateUI.sendUpdateCarNum(HuoHangCheckOrderActivity.this);
                                        showToast("提交成功！" + object1.optString("order_remind"));
                                        finish();

                                    }
                                } else if (object.optString("datas").contains("成功")) {
//                                    MyUpdateUI.sendUpdateCollection(HuoHangCheckOrderActivity.this, MyUpdateUI.CART);
                                    MyUpdateUI.sendUpdateCarNum(HuoHangCheckOrderActivity.this);
                                    showToast("提交成功！");
                                    Intent intent = new Intent(HuoHangCheckOrderActivity.this, HuoHangOrderActivity.class);
                                    intent.putExtra("position", 0);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    showToast(object.optString("datas"));
                                    if (tvPay != null)
                                        tvPay.setEnabled(true);
                                }
                                ProgressDlgUtil.stopProgressDlg();
                            } else {
                                ProgressDlgUtil.stopProgressDlg();
                                if (tvPay != null)
                                    tvPay.setEnabled(true);
                                Log.i("zjz", "confirm:解析失败");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
                            ProgressDlgUtil.stopProgressDlg();
                            if (tvPay != null)
                                tvPay.setEnabled(true);
                        }
                    }
                });

            }
        });
    }


    private void confirmOrder() {
        if (address_id == null) {
            showToast("请选择收货地址");
            return;
        }
        //rcb_pay 使用充值卡  pd_pay 预存款
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        Log.i("zjz", "rcb_pay=" + rcb_pay);
        String temp = null;
        if (rcb_pay == 1 && isSelectRcb) {
            temp = "&key=" + MyApplication.getInstance().getMykey() + "&ifcart=1" + "&cart_id=" + cart_id + "&address_id="
                    + address_id + "&vat_hash=" + vat_hash + "&offpay_hash=" + offpay_hash + "&offpay_hash_batch=" + offpay_hash_batch
                    + "&pay_name=online" + "&invoice_id=undefined" + "&voucher=" + jsonObject + "&rcb_pay=" + rcb_pay + "&password=" + pwd + "&pd_pay=" + pd_pay;
        } else {
            temp = "&key=" + MyApplication.getInstance().getMykey() + "&ifcart=1" + "&cart_id=" + cart_id + "&address_id="
                    + address_id + "&vat_hash=" + vat_hash + "&offpay_hash=" + offpay_hash + "&offpay_hash_batch=" + offpay_hash_batch
                    + "&pay_name=online" + "&invoice_id=undefined" + "&voucher=" + jsonObject + "&rcb_pay=" + rcb_pay + "&pd_pay=" + pd_pay;
        }

        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_buystep2, temp, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "msg=" + msg);
                                JSONObject object1 = object.getJSONObject("datas");
                                if (object1.has("pay_sn") && object1 != null) {
                                    MyUpdateUI.sendUpdateCollection(HuoHangCheckOrderActivity.this, MyUpdateUI.CART);
                                    showToast("订单提交成功");
                                    Intent intent = new Intent(HuoHangCheckOrderActivity.this, OrderActivity2.class);
                                    startActivity(intent);
                                    finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == 1 && data != null) {
            address = (String) data.getSerializableExtra("address");
            name = (String) data.getSerializableExtra("name");
            phone = (String) data.getSerializableExtra("phone");
            tel_phone = (String) data.getSerializableExtra("tel_phone");//座机号码
            address_id = (String) data.getSerializableExtra("address_id");
            Log.i("zjz", "address_id=" + address_id);
            address2 = null;
            initOffPayHash();
        }
        if (requestCode == 2 && resultCode == 2 && data != null) {
            if (data.getBooleanExtra("result", false)) {
                isRefresh = true;
                initInvoiceList();
            }
        }
        if (requestCode == 3 && resultCode == 3 && data != null) {
            initCompany();
//            tBindName.setText((String) data.getSerializableExtra("name"));
//            tDiscount.setText((String) data.getSerializableExtra("code"));
//            discountTemp = (String) data.getSerializableExtra("discount");
//            discountNum= (double) data.getSerializableExtra("discount");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setListViewHeight(ZrcListView listView) {
        android.widget.ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    ArrayAdapter<String> typeAdapter;

    private void initVouncher() {
        relativeVouncher.setVisibility(vouchers.size() != 1 ? VISIBLE : GONE);
        tVouncherNum.setText(vouchers.size() - 1 + "");
        if (vouchers.size() != 1) {
            isVouncher = true;
        } else {
            isVouncher = false;
        }
        typeAdapter.setDropDownViewResource(R.layout.hwg_spinner_dropdown_item);
        spinnerVouncher.setAdapter(typeAdapter);
        spinnerVouncher.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isChongzhi || isYucun || isYYG) {
                    if (position == 0) {
//                        linearChongzhi.setVisibility(View.VISIBLE);
                    } else {
                        linearChongzhi.setVisibility(GONE);
                    }
                }
                if (position == 0) {
                    isUseVou = false;
                    vourch = "";
                    if (isCompany) {
                        total = totalTemp * discountNum / 10 + good_freight;
                    } else {
                        total = totalTemp + good_freight;
                    }
                    tStoreTotal.setText(NumberUtils.formatPrice(total));
                } else {
                    isUseVou = true;
                    vourch = vouchers.get(position).getVoucher_t_id() + "|" + vouchers.get(position).getVoucher_t_store_id() + "|" + vouchers.get(position).getVoucher_t_price();
                    try {
                        jsonObject.put("1", vourch);
                    } catch (JSONException e) {
                    }
                    Log.e("tttttt", jsonObject + "");

//
// vourch = vouchers.get(position).getVoucher_t_id() + "|" + vouchers.get(position).getVoucher_t_store_id() + "|" + vouchers.get(position).getVoucher_t_price();
                    if (isCompany) {
                        total = totalTemp * discountNum / 10 + good_freight - Double.valueOf(vouchers.get(position).getVoucher_t_price());
                    } else {
                        total = totalTemp + good_freight - Double.valueOf(vouchers.get(position).getVoucher_t_price());
                    }
                    voucherTemp = Double.valueOf(vouchers.get(position).getVoucher_t_price());
                    tStoreTotal.setText(NumberUtils.formatPrice(total));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initInvoiceList() {
        provinceList.clear();
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_invoice_list, "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "msg_invoice_list=" + msg);
                                JSONObject object1 = object.getJSONObject("datas");
                                JSONArray invoiceArray = object1.getJSONArray("invoice_list");
                                for (int i = 0; i < invoiceArray.length(); i++) {
                                    JSONObject invoice = invoiceArray.getJSONObject(i);
                                    Invoice in = new Invoice();
                                    in.setInv_id(invoice.optString("inv_id"));
                                    in.setInv_title(invoice.optString("inv_title"));
                                    in.setInv_content(invoice.optString("inv_content"));
                                    provinceList.add(in);
                                }
                                listAdapter = new ListAdapter(HuoHangCheckOrderActivity.this, provinceList, zlistInvoice);
                                if (zlistInvoice != null)
                                    zlistInvoice.setAdapter(listAdapter);

                                View view = getLayoutInflater().inflate(R.layout.hwg_invoice_footview, null);
//                                ZrcListView.LayoutParams layoutParams=new ZrcListView.LayoutParams(Util.WIDTH*2/3, ViewGroup.LayoutParams.WRAP_CONTENT);
//                                view.setLayoutParams(layoutParams);
                                Button btn_add = (Button) view.findViewById(R.id.btn_add);
                                Button btn_no = (Button) view.findViewById(R.id.btn_no);
                                btn_add.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(HuoHangCheckOrderActivity.this, InvoiceAddActivity.class);
                                        HuoHangCheckOrderActivity.this.startActivityForResult(intent, 2);
                                    }
                                });
                                btn_no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        isChoose = false;
                                        ObjectAnimator.ofFloat(imgInvoice, "rotation", 180, 360).setDuration(durationRotate).start();
                                        zlistInvoice.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                zlistInvoice.setVisibility(GONE);
                                            }
                                        }, durationAlpha);
                                        invoice_id = "undefined";
                                        Log.i("zjz", "invoice_id=" + invoice_id);
                                        tInvoice.setText("不需要发票");

                                    }
                                });
                                if (!isRefresh && zlistInvoice != null) {
                                    zlistInvoice.addFooterView(view);
                                    setListViewHeight(zlistInvoice);
                                }

                                listAdapter.notifyDataSetChanged();
                            } else {
                                Log.i("zjz", "goodsDetail:解析失败");
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
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }

    public class MyTagAdapter extends BaseAdapter implements OnInitSelectedPosition {

    private final Context mContext;
    private final List<SDMangSong> mDataList;

    public MyTagAdapter(Context context) {
        this.mContext = context;
        mDataList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public SDMangSong getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_text_mangsong, null);

        TextView textView = (TextView) view.findViewById(R.id.tv);
        SDMangSong t = (SDMangSong) mDataList.get(position);

        if (t != null) {
            if (!TextUtils.isEmpty(t.title)) {
                textView.setText(t.title);
            }
        }

        return view;
    }

    public void onlyAddAll(List<SDMangSong> datas) {
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    public void clearAndAddAll(List<SDMangSong> datas) {
        mDataList.clear();
        onlyAddAll(datas);
    }

    @Override
    public boolean isSelectedPosition(int position) {
        if (position % 2 == 0) {
            return true;
        }
        return false;
    }
}
}
