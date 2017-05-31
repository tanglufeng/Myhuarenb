package com.abcs.haiwaigou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ChongzhiActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.et_code)
    EditText etCode;
    @InjectView(R.id.t_cancel)
    TextView tCancel;
    @InjectView(R.id.t_exchange)
    TextView tExchange;
    @InjectView(R.id.pop_linear)
    LinearLayout popLinear;

    private Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_chongzhi);
        ButterKnife.inject(this);
        tExchange.setOnClickListener(this);
        tCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(getCurrentFocus(), InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()){
            case R.id.t_cancel:
//                imm.hideSoftInputFromInputMethod(etCode.getWindowToken(), 0);
//                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
//                        hideSoftInputFromWindow(etCode.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
               finish();
                break;
            case R.id.t_exchange:
                ProgressDlgUtil.showProgressDlg("Loading...", ChongzhiActivity.this);
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
                                        JSONObject dataObj = object.optJSONObject("datas");
                                        if (dataObj != null) {
                                            if (dataObj.has("error")) {
                                                Toast.makeText(ChongzhiActivity.this, dataObj.optString("error"), Toast.LENGTH_SHORT).show();
//                                                changeFailView(dataObj.optString("error"));
                                            }
                                        } else {
                                            JSONArray array = object.optJSONArray("datas");
                                            if (array != null) {
                                                Intent intent=new Intent();
                                                intent.putExtra("datas","充值成功！");
                                                setResult(1, intent);
                                                finish();
                                            }
                                        }
//                                        Toast.makeText(ChongzhiActivity.this, object.optString("datas"), Toast.LENGTH_SHORT).show();
//                                        if (object.optString("datas").contains("成功")) {
//                                            Intent intent=new Intent();
//                                            intent.putExtra("datas",object.optString("datas"));
//                                            setResult(1, intent);
//                                            finish();
////                                            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
////                                                    hideSoftInputFromWindow(etCode.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                                        }
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

                break;
        }
    }

    @Override
    protected void onDestroy() {
        Log.i("zjz", "关闭啦！！");
        super.onDestroy();
    }
}
