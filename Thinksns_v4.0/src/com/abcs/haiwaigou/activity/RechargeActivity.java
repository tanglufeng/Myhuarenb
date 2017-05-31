package com.abcs.haiwaigou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.mining.app.zxing.MipcaActivityCapture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.et_code)
    EditText etCode;
    @InjectView(R.id.t_chongzhi)
    TextView tChongzhi;
    @InjectView(R.id.relative_input)
    RelativeLayout relativeInput;
    @InjectView(R.id.t_sn)
    TextView tSn;
    @InjectView(R.id.t_money)
    TextView tMoney;
    @InjectView(R.id.t_time)
    TextView tTime;
    @InjectView(R.id.t_type)
    TextView tType;
    @InjectView(R.id.t_success_again)
    TextView tSuccessAgain;
    @InjectView(R.id.relative_success)
    RelativeLayout relativeSuccess;
    @InjectView(R.id.t_fail_text)
    TextView tFailText;
    @InjectView(R.id.t_fail_again)
    TextView tFailAgain;
    @InjectView(R.id.relative_failed)
    RelativeLayout relativeFailed;
    @InjectView(R.id.relative_saomiao)
    RelativeLayout relativeSaomiao;


    private Handler handler = new Handler();
    boolean isSuccess=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwgactivity_recharge);
        ButterKnife.inject(this);
        setOnListener();
    }

    private void setOnListener() {
        relativeBack.setOnClickListener(this);
        tFailAgain.setOnClickListener(this);
        tSuccessAgain.setOnClickListener(this);
        tChongzhi.setOnClickListener(this);
        relativeSaomiao.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ){
            if(isSuccess){
                Intent intent1=new Intent();
                intent1.putExtra("datas", "充值成功！");
                setResult(1, intent1);
            }
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()) {
            case R.id.relative_back:
                if(isSuccess){
                    Intent intent1=new Intent();
                    intent1.putExtra("datas", "充值成功！");
                    setResult(1, intent1);
                }
                finish();
                imm.hideSoftInputFromWindow(etCode.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.t_chongzhi:
                if (MyApplication.getInstance().getMykey() == null) {
                    Intent intent = new Intent(this, WXEntryActivity.class);
                    startActivity(intent);
                } else if (etCode.getText().toString().trim().length() == 0) {
                    showToast("请输入充值卡密码！");
                } else {
                    imm.hideSoftInputFromWindow(etCode.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    commit();
                }
                break;
            case R.id.t_success_again:
                relativeInput.setVisibility(View.VISIBLE);
                relativeSuccess.setVisibility(View.GONE);
                etCode.setText("");
                break;
            case R.id.t_fail_again:
                relativeInput.setVisibility(View.VISIBLE);
                relativeFailed.setVisibility(View.GONE);
                etCode.setText("");
                break;
            case R.id.relative_saomiao:

                Intent intent = new Intent();
                intent.setClass(this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("isRecharge",true);
                startActivityForResult(intent, 1);
                relativeInput.setVisibility(View.VISIBLE);
                relativeFailed.setVisibility(View.GONE);
                relativeSuccess.setVisibility(View.GONE);
                etCode.setText("");
//                startActivityForResult(new Intent(this, MipcaActivityCapture.class), 1);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    if (bundle.getString("result").contains("hqb")){
                        etCode.setText(bundle.getString("result").replaceAll("hqb", ""));
                        etCode.setSelection(bundle.getString("result").replaceAll("hqb", "").length());
                    }
                    //显示
//                    mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
        }
    }

    private void changeFailView(String str) {
        if (relativeInput != null)
            relativeInput.setVisibility(View.GONE);
        if (relativeFailed != null)
            relativeFailed.setVisibility(View.VISIBLE);
        if (tFailText != null)
            tFailText.setText(str);
    }

    private void changeSuccessView(JSONObject jsonObject) {
        if (relativeInput != null)
            relativeInput.setVisibility(View.GONE);
        if (relativeSuccess != null)
            relativeSuccess.setVisibility(View.VISIBLE);
        if (jsonObject != null) {
            String str = jsonObject.optString("sn");
            long time = jsonObject.optLong("tscreated") + (3 * 365 * 24 * 60 * 60);
            if (tSn != null)
                tSn.setText(str.substring(0, 3) + "-" + str.substring(3, 6) + "-" + str.substring(6, str.length()));


            if(jsonObject.optInt("rec_type")>0){   // 大于0表示会员卡充值
                if (tMoney != null)
                    tMoney.setText(jsonObject.optString("rec_name"));
            }else { // 0为充值金额
                if (tMoney != null)
                    tMoney.setText("面值 "+jsonObject.optString("denomination")+" 元");
            }

            if (tType != null)
                tType.setText(jsonObject.optString("batchflag") + "购物卡");
            if (tTime != null)
                tTime.setText(Util.format1.format(time * 1000));
        }
    }

    private void commit() {
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_chongzhi, "&rc_sn=" + etCode.getText().toString() + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "msg=" + msg);
//                                Toast.makeText(RechargeActivity.this, object.optString("datas"), Toast.LENGTH_SHORT).show();
                                JSONObject dataObj = object.optJSONObject("datas");
                                if (dataObj != null) {
                                    if (dataObj.has("error")) {
                                        changeFailView(dataObj.optString("error"));
                                    }
                                } else {
                                    JSONArray array = object.optJSONArray("datas");
                                    if (array != null) {
                                        isSuccess=true;
                                        JSONObject jsonObject = array.getJSONObject(0);
                                        changeSuccessView(jsonObject);
                                    }
                                }
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
}
