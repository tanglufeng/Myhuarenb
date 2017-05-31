package com.abcs.huaqiaobang.activity;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.Product;
import com.abcs.huaqiaobang.model.User;
import com.abcs.huaqiaobang.util.BankUtil;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author xbw
 * @version 创建时间：2015年10月23日 上午11:17:51
 */
public class TurnOutActivity extends BaseActivity {
    @InjectView(R.id.turnout_name)
    EditText turnoutName;
    @InjectView(R.id.rl_turnout_name)
    RelativeLayout rlTurnoutName;
    @InjectView(R.id.rl_turnout_bank)
    RelativeLayout rlTurnoutBank;
    @InjectView(R.id.turnout_city)
    TextView turnoutCity;
    @InjectView(R.id.rl_turnout_city)
    RelativeLayout rlTurnoutCity;
    @InjectView(R.id.turnout_bank)
    TextView turnoutBank;
    @InjectView(R.id.rl_turnout_use_phone)
    RelativeLayout rlTurnoutUsePhone;
    @InjectView(R.id.txt_city)
    TextView txtCity;
    @InjectView(R.id.turnout_use_phone)
    EditText turnoutUsePhone;

    private Handler handler = new Handler();
    private User self;
    private TextView money, cardName, tip;
    private EditText et;
    private TextView cardName1;
    private View check;
    private int index = 0;
    private String name, product_id;
    private Product product;
    private String payway;
    private String outway;
    private String[] array;
    private String bankString;
    private CityAcitivity.PopBean temppopBean, bankPopBean;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            temppopBean = data.getParcelableExtra("city");
            turnoutCity.setText(temppopBean.getName());
        }
        if (resultCode == 2) {
            bankPopBean = data.getParcelableExtra("bank");
            turnoutBank.setText(bankPopBean.getName());

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.occft_activity_turnout);
        ButterKnife.inject(this);
        product = (Product) Util.dataRead(getIntent().getStringExtra("info"));
        product_id = getIntent().getStringExtra("id");

        name = product.getName();
        Util.setImage(product.getIconUrl(),
                findViewById(R.id.activity_turnout_icon), handler);
        Util.setImage(product.getIconUrl(),
                findViewById(R.id.activity_turnout_icon1), handler);
        self = MyApplication.getInstance().self;
        ((TextView) findViewById(R.id.activity_turnout_most)).setText("本次最多可转出"
                + Util.df.format(self.getInvest() / 100) + "元");
        money = (TextView) findViewById(R.id.activity_turnout_money);
        cardName = (TextView) findViewById(R.id.activity_turnout_cardname);
        cardName1 = (TextView) findViewById(R.id.activity_turnout_txt_cardName);
        et = (EditText) findViewById(R.id.activity_turnout_edit);
        tip = (TextView) findViewById(R.id.activity_turnout_txt_tip);
        check = findViewById(R.id.activity_turnout_check);
//        check.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                try {
//                    showCheck();
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        });


        findViewById(R.id.turnout_btn_back).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        findViewById(R.id.activity_turnout_more).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        try {
                            Intent intent = new Intent(TurnOutActivity.this,
                                    WebActivity.class);
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
        findViewById(R.id.activity_turnout_all).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        et.setText((float) self.getInvest() / 100 + "");
                        tip.setVisibility(View.GONE);
                        money.setText(Util.df.format((float) self.getInvest() / 100));
                    }
                });
        et.setText((float) self.getInvest() / 100 + "");
        money.setText((float) self.getInvest() / 100 + "");
        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0) {
                    tip.setVisibility(Float.valueOf(s.toString()) > self
                            .getInvest() / 100 ? View.VISIBLE : View.GONE);
                } else {
                    tip.setVisibility(View.GONE);
                }
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
        payway = product.getPayway();
        outway = product.getOutway();
        findViewById(R.id.activity_turnout_btn).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            TurnOut();
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
//        Log.i("TurnOutActivity", Util.hintNumber("13633047577",3,4));
//        String[] split = "62282121511525125152-zhouzhi".split("-");
//        Log.i("TurnOutActivity", Util.hintNumber(split[0],4,4));


//        turnoutBank.

        array = getResources().getStringArray(R.array.bank_list);

        rlTurnoutBank.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TurnOutActivity.this, BankListActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        rlTurnoutCity.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                BankWindow bankWindow = new BankWindow(TurnOutActivity.this);
//                bankWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
//                bankWindow.setOnclickItem(new BankWindow.OnclickItem() {
//                    @Override
//                    public void onClickItem(BankWindow.PopBean popBean) {
//                        temppopBean = popBean;
//                        turnoutCity.setText(popBean.getName());
//                    }
//                });

                Intent intent = new Intent(TurnOutActivity.this, CityAcitivity.class);
//                startActivity(intent);
                startActivityForResult(intent, 1);
            }
        });
        if ("".equals(payway) && "".equals(outway)) {
            showCheckResult();
        } else {
            rlTurnoutName.setVisibility(View.VISIBLE);
            rlTurnoutUsePhone.setVisibility(View.VISIBLE);
            if (payway.equals("支付宝")) {
                cardName.setText(payway + "\n(" + Util.hintNumber(outway, 3, 4) + ")");
                Log.i("TurnOutActivity", Util.hintNumber(outway, 3, 4));
                cardName1.setText(payway + "(" + Util.hintNumber(outway, 3, 4) + ")");

            }
            if (payway.equals("富友")) {
                String[] split = outway.split("-");


                String[] array = getResources().getStringArray(R.array.bank_list);
//                String[] bank = new String[array.length];
                String nameOfBank = BankUtil.getNameOfBank(split[0]);
                if (!"".equals(nameOfBank)) {
                    for (String b : array) {
//                        if(b.indexOf(nameOfBank))
                        String[] bankNum = b.split("-");
                        if (nameOfBank.indexOf(bankNum[1]) > -1) {
                            bankPopBean = new CityAcitivity.PopBean();
                            bankPopBean.setId(bankNum[0]);
                            bankPopBean.setName(bankNum[1]);
                            turnoutBank.setText(bankNum[1]);
                            cardName.setText(bankNum[1] + "\n(" + Util.hintNumber(split[0], 4, 4) + ")");
                            cardName1.setText(bankNum[1] + "(" + Util.hintNumber(split[0], 4, 4) + ")");
                            break;
                        } else {
                            cardName.setText("储蓄卡" + "(" + Util.hintNumber(split[0], 4, 4) + ")");
                            cardName1.setText("储蓄卡" + "(" + Util.hintNumber(split[0], 4, 4) + ")");
                        }
                    }
                } else {
                    cardName.setText("储蓄卡" + "(" + Util.hintNumber(split[0], 4, 4) + ")");
                    cardName1.setText("储蓄卡" + "(" + Util.hintNumber(split[0], 4, 4) + ")");
                }


                turnoutName.setText(split[1]);
                turnoutName.setFocusable(false);
                rlTurnoutBank.setVisibility(View.VISIBLE);
                rlTurnoutCity.setVisibility(View.VISIBLE);
                Log.i("TurnOutActivity", Util.hintNumber(outway, 3, 4));
//                cardName1.setText("储蓄卡" + "(" + Util.hintNumber(split[0], 4, 4) + ")");

            }
        }
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
    private void TurnOut() throws JSONException {
        String s = et.getText().toString().trim();
        float a = 0;
        if (s.length() == 0) {
            showToast("请输入正确转出金额");
        } else {
            a = Float.valueOf(et.getText().toString().trim());
        }
        System.err.println("a:" + a);
        if (a == 0) {
            showToast("请输入正确转出金额");
            return;
        }
        if (!"".equals(payway)) {
            Log.e("LOG_DEBUG", turnoutBank.getText().toString() + "bank");
            if (payway.equals("富友")) {
                if ("".equals(turnoutBank.getText().toString())) {
                    showToast("请选择开户银行");
                    return;
                }
                if ("".equals(turnoutCity.getText().toString())) {
                    showToast("请选择开户城市");
                    return;
                }
            }
            if (!Util.isValidMobile(turnoutUsePhone.getText().toString().trim())) {
                showToast("请输入正确手机号");
                return;
            }
        }
        final float amont = a;

        new PromptDialog(this, "是否确定转出？", new Complete() {
            @Override
            public void complete() {
                turnOutMoney(amont);
            }
        }).show();
//        final String id = self.getBanks().getJSONObject(index)
//                .getString("bindid");
//        final String cardlast = self.getBanks().getJSONObject(index)
//                .getString("card_last");
//        final String cardtop = self.getBanks().getJSONObject(index)
//                .getString("card_top");
//        new EnterPwdDialog(this, "转入"
//                + self.getBanks().getJSONObject(index).getString("card_name"),
//                "从" + name + "转出", Util.df.format(a) + "元", new HttpRevMsg() {
//
//            @Override
//            public void revMsg(String msg) {

    }

    private void turnOutMoney(float amont) {
        ProgressDlgUtil.showProgressDlg("",
                TurnOutActivity.this);
        LogUtil.e("TurnOut", "method=outProduct&uid="
                + MyApplication.getInstance().self.getId()
                + "&productid=" + product_id + "&money="
                + amont + "&paypwd=123456" + "&bindid=123456"
                + "&cardlast=123456" + "&cardtop=123456"
                + "&token=" + Util.token);
        String parma = null;
        if ("".equals(payway) || "".equals(outway)) {
            parma = "method=outProduct&uid="
                    + MyApplication.getInstance().self
                    .getId() + "&productid="
                    + product_id + "&money=" + amont
                    + "&paypwd=123456" + "&bindid=123456"
                    + "&cardlast=123456" + "&cardtop=123456"
                    + "&token=" + Util.token;
        } else if ("富友".equals(payway)) {

            parma = "method=outNew&uid="
                    + MyApplication.getInstance().self
                    .getId() + "&productid="
                    + product_id + "&money=" + amont
                    + "&paypwd=123456" + "&bankname=" + bankPopBean.getName()
                    + "&bankno=" + bankPopBean.getId() + "&city=" + temppopBean.getName() + "&name=" + turnoutName.getText().toString() + "&cityno=" + temppopBean.getId()
                    + "&token=" + Util.token + "&mobile=" + turnoutUsePhone.getText().toString().trim();
        } else if ("支付宝".equals(payway)) {
            parma = "method=outNew&uid="
                    + MyApplication.getInstance().self
                    .getId() + "&productid="
                    + product_id + "&money=" + amont
                    + "&paypwd=123456" + "&name=" + turnoutName.getText().toString()
                    + "&token=" + Util.token + "&mobile=" + turnoutUsePhone.getText().toString().trim();
        }
        HttpRequest.sendPost(TLUrl.getInstance().URL_productServlet, parma,
                new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {
                        LogUtil.e("TurnOut", msg);
                        ProgressDlgUtil.stopProgressDlg();
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(
                                            msg);
                                    if (jsonObject
                                            .getInt("status") == 1) {
                                        Intent intent = new Intent();
                                        intent.putExtra("msg", jsonObject
                                                .getString("msg"));
                                        setResult(3, intent);
                                        finish();
                                        if (MyApplication
                                                .getInstance()
                                                .getMainActivity() != null) {
                                            MyApplication
                                                    .getInstance()
                                                    .getMainActivity().mHandler
                                                    .sendEmptyMessage(900);
                                        }
                                    }
//                                    showToast(jsonObject
//                                            .getString("msg"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
    }
//        }).show();
//
//    }
}
