package com.abcs.huaqiaobang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.pay.AliPay;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HuiYuanPayActivity2 extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.tljr_per_btn_lfanhui)
    RelativeLayout rlBack;
    @InjectView(R.id.relative_yl)
    RelativeLayout relativeYl;
    @InjectView(R.id.relative_wx)
    RelativeLayout relativeWx;
    @InjectView(R.id.relative_zfb)
    RelativeLayout relativeZfb;
    @InjectView(R.id.tv_huiyuan_name)
    TextView tv_huiyuan_name;
    @InjectView(R.id.tv_price)
    TextView tv_price;

    boolean isPay = false;

    private String title,huoid;
    private double price;
    private String levelid;
    private boolean isfrompop=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hui_yuan_pay2);
        ButterKnife.inject(this);
        AliPay.getInstance().init(this);
        MyApplication.isFlashVip=false;
        mRequestQueue = Volley.newRequestQueue(this);

        iwxapi = WXAPIFactory.createWXAPI(this, "wxe134ccc9e61300a0");
        iwxapi.registerApp("wxe134ccc9e61300a0");

        setOnLick();

        price=Double.valueOf(getIntent().getStringExtra("money"));
        isfrompop=getIntent().getBooleanExtra("isfrompop",false);
        title=getIntent().getStringExtra("title");
        huoid=getIntent().getStringExtra("huoid");
        levelid=getIntent().getStringExtra("levelid");

        if(!TextUtils.isEmpty(title)){
            tv_huiyuan_name.setText("充值"+title);
        }
        if(price>0){
            tv_price.setText("￥"+price+" /年");
            money=(int)price;
        }

        LogUtil.e("zds",levelid+"");
        LogUtil.e("zds",money+"");

    }


    private void setOnLick(){
        rlBack.setOnClickListener(this);
        relativeWx.setOnClickListener(this);
        relativeZfb.setOnClickListener(this);
        relativeYl.setOnClickListener(this);
    }

    private int money=0;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tljr_per_btn_lfanhui:   // 返回
                finish();
                break;
            case R.id.relative_wx:
                isWX = !isWX;
                type = isWX ? WX : null;
                isZFB = false;
                isYH = false;
                break;
            case R.id.relative_zfb:
                isZFB = !isZFB;
                type = isZFB ? ZFB : null;
                isYH = false;
                isWX = false;
                break;
            case R.id.relative_yl:
                isYH = !isYH;
                type = isYH ? YH : null;
                isZFB = false;
                isWX = false;
                break;

        }
        isPay=false;
        pay();
    }

    String type;
    boolean isYH;
    boolean isWX;
    boolean isZFB;
    IWXAPI iwxapi;
    private RequestQueue mRequestQueue;


    public static final String YH = "yhk";
    public static final String WX = "wx";
    public static final String ZFB = "zfb";
    String pay_sn;



    private void pay() {

        if (type == null) {
            showToast("请选择支付方式！");
        } else if (type.equals(WX)) {
            if(isfrompop){
                weixinPop();
            }else {
                weixinPay();
            }
        } else if (type.equals(YH)) {
            if(isfrompop){
                fuyouPayPop();
            }else {
                fuyouPay();
            }
        } else if (type.equals(ZFB)) {

            if(isfrompop){
                String url = TLUrl.getInstance().URL_vip_zhifubao +
                        "&key=" + MyApplication.getInstance().getMykey() +
                        "&money=" + money +
                        "&payment_code=alipay"+"&type="+huoid+"";
                Log.i("zds_zhifubao", "pay_url=" + url);
                Intent intent = new Intent(this, FuyouPayActivity.class);
                intent.putExtra("id", "1");
                intent.putExtra("url", url);
                startActivity(intent);
                MyApplication.isFlashVip=true;
            }else {
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
            }


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    EventBus.getDefault().post("finish_activity");
                }
            },2000);
        }

    }

private Handler handler=new Handler();


    private void weixinPop() {
        Log.i("zds", "weixin_url=" + TLUrl.getInstance().URL_hwg_fuyou_pay + "&key=" + MyApplication.getInstance().getMykey() +"&payment_code=weixin" + "&uid=" + MyApplication.getInstance().self.getId()+ "&money=" + money+ "&type="+huoid+"");
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_fuyou_pay + "&key=" + MyApplication.getInstance().getMykey() +  "&payment_code=weixin" + "&uid=" + MyApplication.getInstance().self.getId()+ "&money=" + money+ "&type="+huoid+"", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("zds", "weixin_msg=" + response);
                    if (response.getInt("status") == 1) {
                        isPay=true;
                        Log.i("zds", "成功跳转到微信支付");
                        JSONObject json = response.getJSONObject("msg");
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
                    } else {
                        Log.i("zds", "解析失败");
                        showToast("微信支付请求失败！请重试");
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zds", e.toString());
                    e.printStackTrace();
                } finally {
                    ProgressDlgUtil.stopProgressDlg();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mRequestQueue.add(jr);
    }


    private void fuyouPayPop() {
        ProgressDlgUtil.showProgressDlg("", this);
        Log.i("zds", "fuyou_url=" + TLUrl.getInstance().URL_hwg_fuyou_pay + "&key=" + MyApplication.getInstance().getMykey() +"&payment_code=fuyou" + "&uid=" + MyApplication.getInstance().self.getId()+ "&money=" + money+ "&type="+huoid+"");

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_fuyou_pay + "&key=" + MyApplication.getInstance().getMykey() + "&payment_code=fuyou" + "&uid=" + MyApplication.getInstance().self.getId()+ "&money=" + money+ "&type="+huoid+"", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("zds", "fuyou_msg=" + response);
                    if (response.getInt("status") == 1) {
                        Log.i("zds", "成功跳转到富友支付");
                        String url = response.optString("msg");
                        Log.i("zds", "pay_url=" + url);
                        Intent intent = new Intent(HuiYuanPayActivity2.this, FuyouPayActivity.class);
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

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zds", e.toString());
                    e.printStackTrace();
                } finally {
                    ProgressDlgUtil.stopProgressDlg();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mRequestQueue.add(jr);

    }
    
    private void fuyouPay() {

        ProgressDlgUtil.showProgressDlg("", this);
//        uid
//        money
//        key
//        levelid   -1   充值   //   其他的会员升级


        Log.i("zds", "yinlian_url=" + TLUrl.getInstance().URL_vip_yinlian + "&key=" + MyApplication.getInstance().getMykey() + "&money=" + money + "&levelid="+levelid+"&uid=" + MyApplication.getInstance().self.getId());

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
                                    Intent intent = new Intent(HuiYuanPayActivity2.this, FuyouPayActivity.class);
                                    intent.putExtra("id", "1");
                                    intent.putExtra("url", url);
                                    startActivity(intent);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                            EventBus.getDefault().post("finish_activity");
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
        Log.i("zds", "weixin_url=" + TLUrl.getInstance().URL_vip_yinlian + "&key=" + MyApplication.getInstance().getMykey() + "&money=" + money + "&levelid="+levelid + "&uid=" + MyApplication.getInstance().self.getId()+ "&weixin=1" );

        HttpRequest.sendGet(TLUrl.getInstance().URL_vip_yinlian, "key=" + MyApplication.getInstance().getMykey() + "&money=" + money + "&levelid="+levelid + "&uid=" + MyApplication.getInstance().self.getId()+ "&weixin=1", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{

//                            {
//                                "buyId":"120315314700001238",
//                                    "msg":{
//                                "appid":"wxe134ccc9e61300a0",
//                                        "noncestr":"4b17d3264fd9070a5be706c853ccd720",
//                                        "package":"Sign=WXPay",
//                                        "partnerid":"1343173601",
//                                        "prepayid":"wx20161203153147f4b19a5fa80095884230",
//                                        "sign":"E6E53E6A30E780431BD2CFC306D92F7E",
//                                        "timestamp":1480750307
//                            },
//                                "status":1
//                            }

                            Log.i("zds", "fuyou_msg=" + msg);
                            ProgressDlgUtil.stopProgressDlg();
                            if(!TextUtils.isEmpty(msg)){
                                JSONObject respone=new JSONObject(msg);
                                if (respone.getInt("status") == 1) {
                                    JSONObject json=respone.getJSONObject("msg");
                                    if(json!=null){
                                        isPay=true;
                                        MyApplication.isFlashVip=true;
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
                                                EventBus.getDefault().post("finish_activity");
                                            }
                                        },2000);
                                    }
                                } else {
                                    Log.i("zds", "解析失败");
                                    showToast("微信支付请求失败！请重试");
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
}
