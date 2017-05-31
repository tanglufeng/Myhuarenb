package com.abcs.haiwaigou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ApplyCashActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.t_money)
    TextView tMoney;
    @InjectView(R.id.ed_money)
    EditText edMoney;
    @InjectView(R.id.spinner_bank)
    Spinner spinnerBank;
    @InjectView(R.id.ed_account)
    EditText edAccount;
    @InjectView(R.id.ed_name)
    EditText edName;
    @InjectView(R.id.ed_password)
    EditText edPassword;
    @InjectView(R.id.t_commit)
    TextView tCommit;
    ArrayAdapter<CharSequence> bankAdapter = null;
    double cash;
    String money = "";
    String account = "";
    @InjectView(R.id.ed_phone)
    EditText edPhone;
    @InjectView(R.id.ed_other)
    EditText edOther;
    @InjectView(R.id.linear_other)
    LinearLayout linearOther;

    private Handler handler = new Handler();
    boolean isOther=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_apply_cash);
        ButterKnife.inject(this);
        cash = getIntent().getDoubleExtra("cash", 0);
        if (tMoney != null)
            tMoney.setText(NumberUtils.formatPrice(cash) + "");
        setOnListener();
        initBankSpinner();
        initTextChange();
    }


    private void initTextChange() {
        edMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    double temp = Double.valueOf(edMoney.getText().toString());
                    money = edMoney.getText().toString();
                    edMoney.setSelection(edMoney.getText().length());
                    if (temp > cash) {
                        edMoney.setText(cash + "");
                        edMoney.setSelection(edMoney.getText().length());
                    }
                } catch (Exception e) {
                    showToast("提现金额不能为空");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initBankSpinner() {
        bankAdapter = ArrayAdapter.createFromResource(this, R.array.bank_array, R.layout.spinner_item);
        bankAdapter.setDropDownViewResource(R.layout.hwg_spinner_dropdown_item);
        spinnerBank.setAdapter(bankAdapter);
        spinnerBank.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    account = "";
                    linearOther.setVisibility(View.GONE);
                    isOther = false;
                } else if (spinnerBank.getSelectedItem().toString().equals("其他")) {
                    linearOther.setVisibility(View.VISIBLE);
                    isOther = true;
                } else {
                    isOther = false;
                    account = spinnerBank.getSelectedItem().toString();
                    linearOther.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setOnListener() {
        tCommit.setOnClickListener(this);
        relativeBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm;
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()) {
            case R.id.t_commit:
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if(isOther){
                    account=edOther.getText().toString();
                }
                if (MyApplication.getInstance().getMykey() == null) {
                    showToast("请登录!");
                    Intent intent = new Intent(this, WXEntryActivity.class);
                    startActivity(intent);
                } else if (money.length() == 0) {
                    showToast("请输入提现金额！");
                } else if (account.length() == 0) {
                    showToast("请选择或输入收款账户名称！");
                } else if (edAccount.getText().toString().length() == 0) {
                    showToast("请输入收款账号！");
                } else if (edName.getText().toString().length() == 0) {
                    showToast("请输入持卡人姓名！");
                } else if (edPhone.getText().toString().length() == 0) {
                    showToast("请输入联系电话！");
                } else if (edPassword.getText().toString().length() == 0) {
                    showToast("请输入华人邦支付密码！");
                } else {
                    commit();
                }
                break;
            case R.id.relative_back:
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                finish();
                break;
        }
    }

    private void commit() {
        //pdc_amount 提现金额  pdc_bank_name 收款银行  pdc_bank_no 收款账号  pdc_bank_user 开户人姓名  password 支付密码 pdc_bank_phone
        Log.i("zjz", "commit_cash_url=" + TLUrl.getInstance().URL_hwg_apply_cash + "&pdc_amount=" + money + "&pdc_bank_name=" +
                account + "&pdc_bank_no=" + edAccount.getText().toString() + "&pdc_bank_user=" + edName.getText().toString()
                + "&password=" + edPassword.getText().toString() + "&pdc_bank_phone=" + edPhone.getText().toString() + "&key=" + MyApplication.getInstance().getMykey());
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_apply_cash, "&pdc_amount=" + money + "&pdc_bank_name=" +
                account + "&pdc_bank_no=" + edAccount.getText().toString() + "&pdc_bank_user=" + edName.getText().toString()
                + "&password=" + edPassword.getText().toString() + "&pdc_bank_phone=" + edPhone.getText().toString() + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i("zjz", "commit_cash_msg=" + msg);
                            JSONObject object = new JSONObject(msg);
                            if (object.optInt("code") == 200) {
                                if (object.optString("datas").contains("成功")) {
                                    showToast("提交成功！");
                                    MyUpdateUI.sendUpdateCollection(ApplyCashActivity.this, MyUpdateUI.COMMITCASH);
                                    finish();
                                } else if (object.optJSONObject("datas").has("error")) {
                                    showToast(object.optJSONObject("datas").optString("error"));
                                } else {
                                    showToast("提交失败！");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
    }

    private MyHandler myHandler = new MyHandler(new WeakReference<ApplyCashActivity>(ApplyCashActivity.this));

    private class MyHandler extends Handler {
        WeakReference<ApplyCashActivity> weakReference;

        public MyHandler(WeakReference<ApplyCashActivity> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            ApplyCashActivity temp = weakReference.get();
            switch (msg.what) {
                case 1:

                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
