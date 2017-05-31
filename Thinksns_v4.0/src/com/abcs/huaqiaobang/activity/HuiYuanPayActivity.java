package com.abcs.huaqiaobang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.pay.AliPay;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HuiYuanPayActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.tljr_per_btn_lfanhui)
    RelativeLayout rlBack;
    @InjectView(R.id.relative_yl)
    RelativeLayout relativeYl;
    @InjectView(R.id.yh_check)
    CheckBox yhCheck;
    @InjectView(R.id.relative_wx)
    RelativeLayout relativeWx;
    @InjectView(R.id.wx_check)
    CheckBox wxCheck;
    @InjectView(R.id.relative_zfb)
    RelativeLayout relativeZfb;

    @InjectView(R.id.zfb_check)
    CheckBox zfbCheck;
    @InjectView(R.id.tv_pay)
    Button tvPay;
    @InjectView(R.id.tv_pay_bai)
    TextView tv_pay_bai;
    @InjectView(R.id.tv_pay_wubai)
    TextView tv_pay_wubai;
    @InjectView(R.id.tv_pay_qian)
    TextView tv_pay_qian;
    @InjectView(R.id.tv_pay_wuqian)
    TextView tv_pay_wuqian;
    @InjectView(R.id.edit_price)
    EditText edit_price;

    boolean isPay = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hui_yuan_pay);
        ButterKnife.inject(this);
        AliPay.getInstance().init(this);
        MyApplication.isFlashVip=false;

        //AppID：wxe134ccc9e61300a0
        iwxapi = WXAPIFactory.createWXAPI(this, "wxe134ccc9e61300a0");
        iwxapi.registerApp("wxe134ccc9e61300a0");
        setOnLick();

        edit_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length()>0){
                    tv_pay_bai.setTextColor(getResources().getColor(R.color.textcloor));
                    tv_pay_bai.setBackground(getResources().getDrawable(R.drawable.bg_textt));
                    tv_pay_wubai.setTextColor(getResources().getColor(R.color.textcloor));
                    tv_pay_wubai.setBackground(getResources().getDrawable(R.drawable.bg_textt));
                    tv_pay_qian.setTextColor(getResources().getColor(R.color.textcloor));
                    tv_pay_qian.setBackground(getResources().getDrawable(R.drawable.bg_textt));
                    tv_pay_wuqian.setTextColor(getResources().getColor(R.color.textcloor));
                    tv_pay_wuqian.setBackground(getResources().getDrawable(R.drawable.bg_textt));

                    tvPay.setBackground(getResources().getDrawable(R.drawable.img_paynow_select));

                }else {
                    tvPay.setBackground(getResources().getDrawable(R.drawable.img_paynow));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void setOnLick(){
        rlBack.setOnClickListener(this);
        tvPay.setOnClickListener(this);
        relativeWx.setOnClickListener(this);
        relativeZfb.setOnClickListener(this);
        relativeYl.setOnClickListener(this);

        tv_pay_bai.setOnClickListener(this);
        tv_pay_wubai.setOnClickListener(this);
        tv_pay_qian.setOnClickListener(this);
        tv_pay_wuqian.setOnClickListener(this);

    }

    private boolean idCheck=false;
    private int position=0;
    private int money=0;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tljr_per_btn_lfanhui:   // 返回
                finish();
                break;
            case R.id.tv_pay_bai:   // 100
                idCheck=true;
                position=100;
                money=100;
                break;
            case R.id.tv_pay_wubai:   // 500
                idCheck=true;
                position=500;
                money=500;
                break;
            case R.id.tv_pay_qian:   // 1000
                idCheck=true;
                position=1000;
                money=1000;
                break;
            case R.id.tv_pay_wuqian:   // 5000
                idCheck=true;
                position=5000;
                money=5000;
                break;
            case R.id.relative_wx:
                isWX = !isWX;
                type = isWX ? WX : null;
                isZFB = false;
                isYH = false;
                wxCheck.setChecked(isWX);
                yhCheck.setChecked(isYH);
                zfbCheck.setChecked(isZFB);
                break;
            case R.id.relative_zfb:
                isZFB = !isZFB;
                type = isZFB ? ZFB : null;
                isYH = false;
                isWX = false;
                wxCheck.setChecked(isWX);
                yhCheck.setChecked(isYH);
                zfbCheck.setChecked(isZFB);
                break;
            case R.id.relative_yl:
                isYH = !isYH;
                type = isYH ? YH : null;
                isZFB = false;
                isWX = false;
                wxCheck.setChecked(isWX);
                yhCheck.setChecked(isYH);
                zfbCheck.setChecked(isZFB);
                break;

            case R.id.tv_pay:  // 立即充值
                isPay=false;
                pay();
                break;
        }
        changeText(position);
    }

    private void changeText(int position){
        switch (position){
            case 100:
                tv_pay_bai.setTextColor(getResources().getColor(R.color.red02));
                tv_pay_bai.setBackground(getResources().getDrawable(R.drawable.bg_textt_select));
                tv_pay_wubai.setTextColor(getResources().getColor(R.color.textcloor));
                tv_pay_wubai.setBackground(getResources().getDrawable(R.drawable.bg_textt));
                tv_pay_qian.setTextColor(getResources().getColor(R.color.textcloor));
                tv_pay_qian.setBackground(getResources().getDrawable(R.drawable.bg_textt));
                tv_pay_wuqian.setTextColor(getResources().getColor(R.color.textcloor));
                tv_pay_wuqian.setBackground(getResources().getDrawable(R.drawable.bg_textt));
                break;
            case 500:
                tv_pay_bai.setTextColor(getResources().getColor(R.color.textcloor));
                tv_pay_bai.setBackground(getResources().getDrawable(R.drawable.bg_textt));
                tv_pay_wubai.setTextColor(getResources().getColor(R.color.red02));
                tv_pay_wubai.setBackground(getResources().getDrawable(R.drawable.bg_textt_select));
                tv_pay_qian.setTextColor(getResources().getColor(R.color.textcloor));
                tv_pay_qian.setBackground(getResources().getDrawable(R.drawable.bg_textt));
                tv_pay_wuqian.setTextColor(getResources().getColor(R.color.textcloor));
                tv_pay_wuqian.setBackground(getResources().getDrawable(R.drawable.bg_textt));
                break;
            case 1000:
                tv_pay_bai.setTextColor(getResources().getColor(R.color.textcloor));
                tv_pay_bai.setBackground(getResources().getDrawable(R.drawable.bg_textt));
                tv_pay_wubai.setTextColor(getResources().getColor(R.color.textcloor));
                tv_pay_wubai.setBackground(getResources().getDrawable(R.drawable.bg_textt));
                tv_pay_qian.setTextColor(getResources().getColor(R.color.red02));
                tv_pay_qian.setBackground(getResources().getDrawable(R.drawable.bg_textt_select));
                tv_pay_wuqian.setTextColor(getResources().getColor(R.color.textcloor));
                tv_pay_wuqian.setBackground(getResources().getDrawable(R.drawable.bg_textt));
                break;
            case 5000:
                tv_pay_bai.setTextColor(getResources().getColor(R.color.textcloor));
                tv_pay_bai.setBackground(getResources().getDrawable(R.drawable.bg_textt));
                tv_pay_wubai.setTextColor(getResources().getColor(R.color.textcloor));
                tv_pay_wubai.setBackground(getResources().getDrawable(R.drawable.bg_textt));
                tv_pay_qian.setTextColor(getResources().getColor(R.color.textcloor));
                tv_pay_qian.setBackground(getResources().getDrawable(R.drawable.bg_textt));
                tv_pay_wuqian.setTextColor(getResources().getColor(R.color.red02));
                tv_pay_wuqian.setBackground(getResources().getDrawable(R.drawable.bg_textt_select));
                break;
            default:  //  自定义金额
                tv_pay_bai.setTextColor(getResources().getColor(R.color.textcloor));
                tv_pay_bai.setBackground(getResources().getDrawable(R.drawable.bg_textt));
                tv_pay_wubai.setTextColor(getResources().getColor(R.color.textcloor));
                tv_pay_wubai.setBackground(getResources().getDrawable(R.drawable.bg_textt));
                tv_pay_qian.setTextColor(getResources().getColor(R.color.textcloor));
                tv_pay_qian.setBackground(getResources().getDrawable(R.drawable.bg_textt));
                tv_pay_wuqian.setTextColor(getResources().getColor(R.color.textcloor));
                tv_pay_wuqian.setBackground(getResources().getDrawable(R.drawable.bg_textt));

                idCheck=true;
                break;
        }

        if(idCheck){
            tvPay.setBackground(getResources().getDrawable(R.drawable.img_paynow_select));
        }else {
            tvPay.setBackground(getResources().getDrawable(R.drawable.img_paynow));
        }
    }

    String type;
    boolean isYH;
    boolean isWX;
    boolean isZFB;
    IWXAPI iwxapi;


    public static final String YH = "yhk";
    public static final String WX = "wx";
    public static final String ZFB = "zfb";
    String pay_sn;



    private void pay() {

        if(!TextUtils.isEmpty(edit_price.getText().toString().trim())){
            money=Integer.valueOf(edit_price.getText().toString().trim());
            position=money;
        }

        if(money>0){
            if (type == null) {
                showToast("请选择支付方式！");
                tvPay.setEnabled(true);
            } else if (type.equals(WX)) {
//            String url = "http://www.huaqiaobang.com/mobile/index.php?act=member_payment&op=pay" +
//                    "&key=" + MyApplication.getInstance().getMykey() +
//                    "&pay_sn=" + pay_sn +
//                    "&payment_code=wxpay";
//            Intent intent = new Intent(this, FuyouPayActivity.class);
//            intent.putExtra("id", "1");
//            intent.putExtra("url", url);
//            startActivity(intent);
//            finish();
                weixinPay();
            } else if (type.equals(YH)) {
                fuyouPay();
            } else if (type.equals(ZFB)) {

                String url = TLUrl.getInstance().URL_vip_zhifubao +
                        "&key=" + MyApplication.getInstance().getMykey() +
                        "&money=" + money +
                        "&payment_code=alipay";
                Log.i("zds_zhifubao", "pay_url=" + url);
                Intent intent = new Intent(this, FuyouPayActivity.class);
                intent.putExtra("id", "1");
                intent.putExtra("url", url);
                startActivity(intent);
                MyApplication.isFlashVip=true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },2000);
            }

        }else {
            showToast("金额不能为零！");
        }
    }

private Handler handler=new Handler();

    private void fuyouPay() {

        ProgressDlgUtil.showProgressDlg("", this);
//        uid
//        money
//        key
//        levelid   -1   充值   //   其他的会员升级


        Log.i("zds", "yinlian_url=" + TLUrl.getInstance().URL_vip_yinlian + "&key=" + MyApplication.getInstance().getMykey() + "&money=" + money + "&levelid=-1" + "&uid=" + MyApplication.getInstance().self.getId());

        HttpRequest.sendGet(TLUrl.getInstance().URL_vip_yinlian, "key=" + MyApplication.getInstance().getMykey() + "&money=" + money + "&levelid=-1" + "&uid=" + MyApplication.getInstance().self.getId(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Log.i("zds", "fuyou_msg=" + msg);
                            ProgressDlgUtil.stopProgressDlg();
                            if(!TextUtils.isEmpty(msg)){
                                JSONObject response=new JSONObject(msg);
                                if (response.getInt("status") == 1) {
                                    MyApplication.isFlashVip=true;
                                    Log.i("zds", "成功跳转到富友支付");
                                    String url = response.optString("msg");
                                    Log.i("zds", "pay_url=" + url);
                                    Intent intent = new Intent(HuiYuanPayActivity.this, FuyouPayActivity.class);
                                    intent.putExtra("id", "1");
                                    intent.putExtra("url", url);
                                    startActivity(intent);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    },2000);

                                } else {
                                    Log.i("zds", "解析失败");
                                }
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    private void weixinPay() {


        ProgressDlgUtil.showProgressDlg("", this);
//        uid
//        money
//        key
//        weixin  随便
//        levelid   -1   充值   //   其他的会员升级


        Log.i("zds", "weixin_url=" + TLUrl.getInstance().URL_vip_yinlian + "&key=" + MyApplication.getInstance().getMykey() + "&money=" + money + "&levelid=-1" + "&uid=" + MyApplication.getInstance().self.getId()+ "&weixin=1" );

        HttpRequest.sendGet(TLUrl.getInstance().URL_vip_yinlian, "key=" + MyApplication.getInstance().getMykey() + "&money=" + money + "&levelid=-1" + "&uid=" + MyApplication.getInstance().self.getId()+ "&weixin=1", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            if(!TextUtils.isEmpty(msg)){
                                JSONObject respone=new JSONObject(msg);
                                if (respone.getInt("status") == 1) {
                                    JSONObject json=respone.getJSONObject("msg");
                                    if(json!=null){
                                        MyApplication.isFlashVip=true;
                                        isPay=true;
                                        Log.i("zds", "成功跳转到微信支付");
                                        PayReq req = new PayReq();
                                        req.appId = json.getString("appid");
                                        req.partnerId = json.getString("partnerid");
                                        req.prepayId = json.getString("prepayid");
                                        req.nonceStr = json.getString("noncestr");
                                        req.timeStamp = json.getString("timestamp");
                                        req.packageValue = json.getString("package");
                                        req.sign = json.getString("sign");
                                        req.extData = "app data"; // optional
                                        Log.i("zds", "req=" + req);
                                        iwxapi.sendReq(req);
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        },2000);
                                    }
                                } else {
                                    Log.i("zds", "解析失败");
                                    showToast("微信支付请求失败！请重试");
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zds", e.toString());
                            e.printStackTrace();
                        } finally {
                            if(tvPay!=null&&!isPay)
                                tvPay.setEnabled(true);
                            ProgressDlgUtil.stopProgressDlg();
                        }

//
                    }
                });
            }
        });


//        //////////////////////////////
//        Log.i("zds", "weixin_url=" + TLUrl.getInstance().URL_vip_yinlian + "&key=" + MyApplication.getInstance().getMykey() + "&pay_sn=" + pay_sn + "&payment_code=weixin" + "&uid=" + MyApplication.getInstance().self.getId());
//        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_fuyou_pay + "&key=" + MyApplication.getInstance().getMykey() + "&pay_sn=" + pay_sn + "&payment_code=weixin" + "&uid=" + MyApplication.getInstance().self.getId(), null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    Log.i("zds", "weixin_msg=" + response);
//                    if (response.getInt("status") == 1) {
//                        isPay=true;
//                        Log.i("zds", "成功跳转到微信支付");
//                        JSONObject json = response.getJSONObject("msg");
//                        PayReq req = new PayReq();
//                        req.appId = json.getString("appid");
//                        req.partnerId = json.getString("partnerid");
//                        req.prepayId = json.getString("prepayid");
//                        req.nonceStr = json.getString("noncestr");
//                        req.timeStamp = json.getString("timestamp");
//                        req.packageValue = json.getString("package");
//                        req.sign = json.getString("sign");
//                        req.extData = "app data"; // optional
//                        Log.i("zds", "req=" + req);
//                        iwxapi.sendReq(req);
//                        finish();
//                    } else {
//                        Log.i("zds", "解析失败");
//                        showToast("微信支付请求失败！请重试");
//                    }
//
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    Log.i("zds", e.toString());
//                    e.printStackTrace();
//                } finally {
//                    if(tvPay!=null&&!isPay)
//                        tvPay.setEnabled(true);
//                    ProgressDlgUtil.stopProgressDlg();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        });
//        mRequestQueue.add(jr);
    }



}
