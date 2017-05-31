package com.abcs.huaqiaobang.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.EnterSecurityDialog;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.Product;
import com.abcs.huaqiaobang.model.User;
import com.abcs.huaqiaobang.pay.AliPay;
import com.abcs.huaqiaobang.pay.PayActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

/**
 * @author xbw
 * @version 创建时间：2015年10月23日 上午11:17:51
 */
public class TurnInActivity extends BaseActivity {
    private Handler handler = new Handler();
    private User self;
    private TextView money, cardName;
    private EditText et;
    private TextView cardName1;
    private View check;
    public static int index = 0;
    public static int type = 2;
    private String name;
    private Product product;
    private String orderId;
    private int amont;
    private String resultCode;

    private Activity activity;
    String name_temp = null;
    String name_desc = null;

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        // index=1;
        // showCheckResult();

//        Log.i("tga", "TurnInActivity" + self.getBanks().toString());
        if (MyApplication.getInstance().cardname != null) {

            name_temp = MyApplication.getInstance().cardname;
            name_desc = MyApplication.getInstance().carddesc;
        }
//        } else {
//            try {
//                name_temp = self.getBanks().getJSONObject(index)
//                        .getString("card_name");
//                name_desc = self.getBanks().getJSONObject(index)
//                        .getString("card_last");
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            ;
//        }
        else {
            name_temp = "富友支付";
//
            name_desc = "富友安全支付";
        }
        cardName1.setText(name_temp + "(" + name_desc + ")");
        cardName.setText(name_temp + "\n(" + name_desc + ")");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.occft_activity_turnin);
        activity = this;
        ((TextView) findViewById(R.id.activity_turnout_getgain))
                .setText("预计收益到账时间"
                        + Util.getDateOnlyDay(System.currentTimeMillis() + 86400000l));
        product = (Product) Util.dataRead(getIntent().getStringExtra("info"));


        name = product.getName();
        try {
            JSONObject jsonObject = new JSONObject(product.getItems());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("zds", "onCreate: product.getIconUrl()"+product.getIconUrl());

        MyApplication.imageLoader.displayImage(product.getIconUrl(),(ImageView) findViewById(R.id.activity_turnout_icon),MyApplication.getListOptions());
        MyApplication.imageLoader.displayImage(product.getIconUrl(),(ImageView) findViewById(R.id.activity_turnout_icon1),MyApplication.getListOptions());

       /* Util.setImage(product.getIconUrl(),
                findViewById(R.id.activity_turnout_icon), handler);
        Util.setImage(product.getIconUrl(),
                findViewById(R.id.activity_turnout_icon1), handler);*/
        self = MyApplication.getInstance().self;
        money = (TextView) findViewById(R.id.activity_turnout_money);
        cardName = (TextView) findViewById(R.id.activity_turnout_cardname);
        cardName1 = (TextView) findViewById(R.id.activity_turnout_txt_cardName);
        et = (EditText) findViewById(R.id.activity_turnout_edit);

        AliPay.getInstance().init(this);

        check = findViewById(R.id.activity_turnout_check);
        check.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // try {
                // showCheck();
                // } catch (JSONException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }

                Intent intent = new Intent(TurnInActivity.this,
                        PayActivity.class);

                intent.putExtra("id", self.getId());
                intent.putExtra("money", money.getText());

                TurnInActivity.this.startActivity(intent);

            }
        });
        findViewById(R.id.turnout_btn_back).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        findViewById(R.id.activity_turnin_more).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TurnInActivity.this,
                                WebActivity.class);
                        try {
                            JSONArray arr = new JSONArray(product.getItems());

                            String url = arr.getJSONObject(arr.length() - 1).getString("更多详情");
                            intent.putExtra("name", "详情");
                            intent.putExtra("url", url);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        findViewById(R.id.activity_turnin_txt_protocol).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TurnInActivity.this,
                                WebActivity.class);
                        intent.putExtra("name", "服务协议");
                        intent.putExtra("url",
                                "http://oss.aliyuncs.com/tuling/protocol.html");
                        startActivity(intent);
                    }
                });
        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                money.setText(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        findViewById(R.id.activity_turnout_btn).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {

                            TurnIn();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
//        if (self.getEmail() == null || self.getPhone() == null) {
//            showToast("请先绑定邮箱或手机!");
//            finish();
//            return;
//        }
//        if (self.isIsbindidenity() == 0) {
//            showToast("请先实名认证");
//            startActivity(new Intent(this, AccountStep1Activity.class));
//            finish();
//            return;
//        }
//        if (self.getBanks() == null || self.getBanks().length() == 0) {
//            showToast("请先绑定银行卡");
//            startActivity(new Intent(this, AddCardActivity.class));
//            finish();
//            return;
//        }
//        showCheckResult();
    }

    private void showCheckResult() {
        try {
            cardName.setText(self.getBanks().getJSONObject(index)
                    .getString("card_name")
                    + "\n("
                    + self.getBanks().getJSONObject(index)
                    .getString("card_last") + ")");
            cardName1.setText(self.getBanks().getJSONObject(index)
                    .getString("card_name")
                    + "("
                    + self.getBanks().getJSONObject(index)
                    .getString("card_last") + ")");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void showCheck() throws JSONException {
        // 初始化要输入的组的数据
        final String[] arrayItems = new String[self.getBanks().length()];
        for (int i = 0; i < arrayItems.length; i++) {
            arrayItems[i] = self.getBanks().getJSONObject(i)
                    .getString("card_name")
                    + "("
                    + self.getBanks().getJSONObject(i).getString("card_last")
                    + ")";
        }
        new AlertDialog.Builder(this)
                .setTitle("请选择收款账户")
                .setSingleChoiceItems(arrayItems, 1,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                index = which;
                            }
                        })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showCheckResult();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }

    /**
     * 转出
     *
     * @throws JSONException
     */
    private void TurnIn() throws JSONException {
        String s = et.getText().toString().trim();
        int a = 0;
        if (s.length() == 0) {
            a = 0;
        } else {

            BigInteger bigInteger = new BigInteger(et.getText().toString().trim());
            BigInteger b2 = BigInteger.valueOf(Integer.MAX_VALUE);
            if (bigInteger.compareTo(b2) == 1) {
                showToast("请输入正确转入金额");
            } else {
                a = Integer.parseInt(et.getText().toString().trim());
            }
        }
        System.err.println("a:" + a);
        if (a == 0) {
            showToast("请输入正确转入金额");
            return;
        }

        if (product == null) {
            showToast("产品获取异常，请返回重试");
            return;
        }
        Log.i("tga", product.getBuyMoney() + "");
        if (a < product.getBuyMoney() / 100) {
            showToast("最少转入金额为" + Util.formatMoney(product.getBuyMoney() / 100));
            return;
        }
        amont = a;
//        final String id = self.getBanks().getJSONObject(index)
//                .getString("bindid");
//        final String cardlast = self.getBanks().getJSONObject(index)
//                .getString("card_last");
//        final String cardtop = self.getBanks().getJSONObject(index)
//                .getString("card_top");

        // if("支付宝".equals(MyApplication.cardname)){
        // alipay();
        // return;
        // }

//        String cardname = self.getBanks().getJSONObject(index)
//                .getString("card_name");
//        if (MyApplication.cardname != null) {
//            cardname = MyApplication.cardname;
//        }
//        new EnterPwdDialog(this, "转入" + name, "从" + cardname + "转出",
//                Util.df.format(a) + "元", new HttpRevMsg() {
//
//            @Override
//            public void revMsg(String msg) {
        ProgressDlgUtil
                .showProgressDlg("", TurnInActivity.this);
        LogUtil.e("TurnIn", TLUrl.getInstance().URL_productServlet+
                "method=investProduct&uid="
                        + MyApplication.getInstance().self
                        .getId() + "&money=" + amont
                        + "&paypwd=123456" + "&bindid=123456"
                        + "&cardlast=123456" + "&cardtop=123456"
                        + "&productid="
                        + product.getId() + "&token="
                        + Util.token + "&type=" + type);
        HttpRequest.sendPost(
                TLUrl.getInstance().URL_productServlet,
                "method=investProduct&uid="
                        + MyApplication.getInstance().self
                        .getId() + "&money=" + amont
                        + "&paypwd=123456" + "&bindid=123456"
                        + "&cardlast=123456" + "&cardtop=123456"
                        + "&productid="
                        + product.getId() + "&token="
                        + Util.token + "&type=" + type,
                new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {
                        ProgressDlgUtil
                                .stopProgressDlg();


                        switch (type) {
                            case 0:

//                                        turnInyibao(msg);

                                break;
                            case 1:
                                turnInResult(msg);
                                break;
                            case 2:
                                turnInfromfuyou(msg);
                                break;

                        }
//                        MyApplication.getInstance().getMainActivity().my.refreshcash();
                    }

                });
//    }
//}).show();

    }


    private void turnInfromfuyou(final String msg) {
//        ProgressDlgUtil.showProgressDlg("正在加载", this);


        Log.i("turnInfromfuyou", msg);
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject msgobject = new JSONObject(msg);
                    if (!msgobject.isNull("status")) {
                        if (msgobject.getInt("status") == 1) {
                            String msg1 = msgobject.getString("msg");
                            Intent intent = new Intent(TurnInActivity.this, FuyouPayActivity.class);
                            intent.putExtra("url", msg1);
                            intent.putExtra("id", msgobject.getString("buyId"));
                            startActivityForResult(intent, 1);
//                            ProgressDlgUtil.stopProgressDlg();
                        } else {
                            showToast(msgobject.getString("msg"));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


//        String mac = EncryptUtils.md5Encrypt("" + "|"
//                + orderNo.getText().toString() + "|" + mchnt_key).toLowerCase();
//
//        Bundle bundle = new Bundle();
//        bundle.putString(FyPay.KEY_ORDER_NO, orderNo.getText().toString());
//        bundle.putString(FyPay.KEY_MOBILE_NO, "");
//        bundle.putString(FyPay.KEY_MAC, mac);

//        int i = FyPay.pay(TurnInActivity.this, bundle, new FyPayCallBack() {

//            @Override
//            public void onPayComplete(String rspCode, String rspDesc, Bundle extraData) {
//                /*
//                * rspCode:支付结果代码
//                * rspDesc:支付结果描述
//                * extraData:支付相关数据
//                * */
//                Log.e("TAG", "rspCode===" + rspCode);
//                Log.e("TAG", "rspDesc===" + rspDesc);
//                Log.e("TAG", extraData.get(FyPay.KEY_ACTUAL_MONEY) + "");
//            }
//        });


    }


    private void turnInyibao(final String msg) {
        // TODO Auto-generated method stub
        LogUtil.e("TurnInyibao", msg);
        //
        // {"msg":{"orderid":"1445587164867","phone":"15172318348","smsconfirm":0},"status":1}
        handler.post(new Runnable() {

            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(msg);
                    if (jsonObject.getInt("status") == 1) {
                        JSONObject object = jsonObject.getJSONObject("msg");
                        orderId = object.getString("orderid");
                        String phone = object.getString("phone");
                        final String s = phone.substring(0, 4)
                                + "****"
                                + phone.substring(phone.length() - 4,
                                phone.length());
                        if (object.getInt("smsconfirm") == 1) {
                            ProgressDlgUtil.stopProgressDlg();
                            new EnterSecurityDialog(TurnInActivity.this, s,
                                    orderId, new HttpRevMsg() {

                                @Override
                                public void revMsg(String msg) {
                                    checkSecurity(msg, orderId, s);
                                }
                            }).show();
                        } else {
                            // showToast("提交成功,几秒后完成订单");
                            checkSuccess(orderId);
                        }
                    } else {
                        showToast(jsonObject.getString("msg"));
                        ProgressDlgUtil.stopProgressDlg();
                    }
                } catch (JSONException e) {
                    ProgressDlgUtil.stopProgressDlg();
                    e.printStackTrace();
                }
            }
        });
    }

    private void turnInResult(final String msg) {
        LogUtil.e("TurnIn", msg);
        // {{"msg":{"date":1449025360362,"id":"1449025360362","money":100,"name":"定期理财7天转入","productId":"1448851222021","status":"wait","tradingType":"enter","uid":"10015","userVipId":"","vip":false},"status":1}}
        ProgressDlgUtil.showProgressDlg("正在加载", this);


        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(msg);
                    if (jsonObject.getInt("status") == 1) {

                        AliPay aliPay = AliPay.getInstance();
                        aliPay.pay(jsonObject.getString("msg"));
                        ProgressDlgUtil.stopProgressDlg();
                        final String id = jsonObject.getString("buyId");
                        aliPay.setPaySuccessListener(new AliPay.PaySuccessListener() {
                            @Override
                            public void paySuccess(String resultStatus) {
                                Log.i("alipay", "id==" + id);
                                MyApplication.turnIn_money = Float.parseFloat(et.getText()
                                        .toString());
                                ProgressDlgUtil.showProgressDlg("请销等", TurnInActivity.this);
                                checkSuccess(id);
                            }
                        });


//				TurnInActivity.this.finish();

                    } else {
                        showToast(jsonObject.getString("msg"));
                    }
                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

        });


        // handler.post(new Runnable() {
        //
        // @Override
        // public void run() {
        // try {
        // JSONObject jsonObject = new JSONObject(msg);
        // if (jsonObject.getInt("status") == 1) {
        // JSONObject object = jsonObject.getJSONObject("msg");
        // String id = object.getString("id");
        // if (id.length() > 0) {
        //
        // ProgressDlgUtil.stopProgressDlg();
        // alipay(id);
        // // startActivity(new Intent(TurnInActivity.this,
        // // PayActivity.class).putExtra("id", id)
        // // .putExtra("money", amont + ""));
        // //
        // // // startActivityForResult(
        // // // new Intent(TurnInActivity.this,
        // // // PayActivity.class).putExtra("id",
        // // // id).putExtra("money", amont + ""),
        // // // 1);
        // } else {
        // showToast("获取商品id失败!");
        // }
        // } else {
        // showToast(jsonObject.getString("msg"));
        // ProgressDlgUtil.stopProgressDlg();
        // }
        // } catch (JSONException e) {
        // ProgressDlgUtil.stopProgressDlg();
        // e.printStackTrace();
        // }
        // }
        // });

    }

    boolean isCheck = false;

    private void checkSuccess(final String id) {
        if (isCheck) {
            ProgressDlgUtil.stopProgressDlg();
            return;
        }
//        ProgressDlgUtil.showProgressDlg("", TurnInActivity.this);
        LogUtil.e("checkSuccesssend", "method=checkPayStatus&orderid=" + id
                + "&uid=" + MyApplication.getInstance().self.getId()
                + "&token=" + Util.token);
        HttpRequest.sendPost(
                TLUrl.getInstance().URL_productServlet,
                "method=checkPayStatus&orderid=" + id + "&uid="
                        + MyApplication.getInstance().self.getId() + "&token="
                        + Util.token, new HttpRevMsg() {

                    @Override
                    public void revMsg(String msg) {
                        LogUtil.e("checkSuccess", msg);
                        ProgressDlgUtil.stopProgressDlg();
                        try {
                            final JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.getInt("status") == 0) {
                                handler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        checkSuccess(id);
                                    }
                                }, 5000);
                            } else {
                                isCheck = true;
                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        try {
                                            String msg = "";
                                            if (jsonObject.getInt("status") == 1) {
                                                msg = "转入成功";
                                                Intent intent = new Intent(
                                                        TurnInActivity.this,
                                                        TurnInSuccessActivity.class);
                                                intent.putExtra("icon",
                                                        product.getIconUrl());
//                                                } else {
//                                                    cardname = self.getBanks()
//                                                            .getJSONObject(
//                                                                    index)
//                                                            .getString(
//                                                                    "card_name")
//                                                            + "\n("
//                                                            + self.getBanks()
//                                                            .getJSONObject(
//                                                                    index)
//                                                            .getString(
//                                                                    "card_last")
//                                                            + ")";
//                                                }
                                                intent.putExtra(
                                                        "cardname", name_temp
                                                );
                                                intent.putExtra("name",
                                                        product.getName());
                                                intent.putExtra("money", amont
                                                        + "");
                                                JSONObject object = jsonObject
                                                        .getJSONObject("msg");
                                                intent.putExtra(
                                                        "arrive",
                                                        object.optLong("arrive"));
                                                intent.putExtra("start",
                                                        object.optLong("start"));
                                                startActivity(intent);
                                                finish();
                                                if (MyApplication.getInstance()
                                                        .getMainActivity() != null) {
                                                    MyApplication.getInstance()
                                                            .getMainActivity().mHandler
                                                            .sendEmptyMessage(900);
                                                }
                                            } else {
                                                msg = jsonObject
                                                        .getString("msg");
                                            }
                                            showToast(msg);
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }

                                    }
                                });

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void checkSecurity(String sms, final String id, final String s) {
        LogUtil.e("checkSecurity",
                "method=validatePay&code=" + sms + "&orderid=" + id + "&uid="
                        + MyApplication.getInstance().self.getId() + "&token="
                        + Util.token);
        HttpRequest.sendPost(TLUrl.getInstance().URL_productServlet,
                "method=validatePay&code=" + sms + "&orderid=" + id + "&uid="
                        + MyApplication.getInstance().self.getId() + "&token="
                        + Util.token, new HttpRevMsg() {

                    @Override
                    public void revMsg(String msg) {
                        LogUtil.e("checkSecurity", msg);
                        try {
                            final JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.getInt("status") == 1) {
                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        showToast("发送成功,等待验证!");
                                        checkSuccess(id);
                                    }
                                });
                            } else {
                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        try {
                                            showToast(jsonObject
                                                    .getString("msg"));
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        new EnterSecurityDialog(
                                                TurnInActivity.this, s,
                                                orderId, new HttpRevMsg() {

                                            @Override
                                            public void revMsg(
                                                    String msg) {
                                                checkSecurity(msg,
                                                        orderId, s);
                                            }
                                        }).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void alipay(String orderid) {
        HttpRequest.sendPost(TLUrl.getInstance().URL_alipay, "id=" + orderid,
                new HttpRevMsg() {

                    @Override
                    public void revMsg(String msg) {
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.getInt("status") == 1) {
                                AliPay.getInstance().pay(
                                        jsonObject.getString("msg"));

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        Log.i("TurnInFUYOU", resultCode + "");
        if (resultCode == 1) {
            String id = data.getStringExtra("id");
            ProgressDlgUtil.showProgressDlg("请稍等", TurnInActivity.this);
            checkSuccess(id);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
