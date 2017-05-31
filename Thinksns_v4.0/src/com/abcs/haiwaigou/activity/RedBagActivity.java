package com.abcs.haiwaigou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RedBagActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.et_code)
    EditText etCode;
    @InjectView(R.id.t_ling)
    TextView tLing;
    @InjectView(R.id.relative_title)
    RelativeLayout relativeTitle;
    @InjectView(R.id.t_text)
    TextView tText;
    @InjectView(R.id.relative_my)
    RelativeLayout relativeMy;

    private Handler handler = new Handler();
    private RequestQueue mRequestQueue;
    private String red_code="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_red_bag);
        ButterKnife.inject(this);
        red_code=getIntent().getStringExtra("red_code");
        if(!red_code.equals("")){
            etCode.setText(red_code);
        }
        mRequestQueue = Volley.newRequestQueue(this);
        tLing.setOnClickListener(this);
        relativeMy.setOnClickListener(this);
        relativeBack.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////            window = getWindow();
////            // Translucent status bar
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relativeTitle.getLayoutParams();
            params.setMargins(0, getStatusBarHeight(), 0, 0);
            relativeTitle.setLayoutParams(params);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()) {
            case R.id.t_ling:
                if (MyApplication.getInstance().getMykey() == null) {
                    intent = new Intent(this, WXEntryActivity.class);
                    startActivity(intent);
                } else if (etCode.getText().toString().length() == 0) {
                    showToast("请输入红包口令！");
                } else {
                    receiveAct();
//                    intent = new Intent(this, RedBagDetailActivity.class);
//                    intent.putExtra("keyword", etCode.getText().toString().trim());
//                    startActivity(intent);
                }
                break;
            case R.id.relative_back:
                finish();
                imm.hideSoftInputFromWindow(etCode.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.relative_my:
                if (MyApplication.getInstance().getMykey() == null) {
                    intent = new Intent(this, WXEntryActivity.class);
                }else {
                    intent=new Intent(this,MyRedBagActivity.class);
                }
                startActivity(intent);
                break;
        }
    }

    private void receiveAct() {
//        ?act=word_receive&op=index&key=4f58f79c4afafe308d08300308ae0d7b&value=
        ProgressDlgUtil.showProgressDlg("Loading...", this);
//        http://www.huaqiaobang.com/mobile/index.php?act=word_receive&op=index
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_redbag_lingqu + "&key=" + MyApplication.getInstance().getMykey(), "&value=" + etCode.getText().toString().trim(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(msg);

                            Intent intent = null;
                            Log.i("zjz", "redbag_msg=" + msg);
                            if (jsonObject.optInt("code") == 200) {
                                JSONArray array = jsonObject.optJSONArray("datas");
                                JSONObject dataObject = jsonObject.optJSONObject("datas");
                                if (array != null) {
                                    JSONObject object = array.getJSONObject(0);
                                    //领取充值卡成功
                                    intent = new Intent(RedBagActivity.this, RedBagComfirActivity.class);
                                    intent.putExtra("type", RedBagComfirActivity.RECHARGE);
                                    intent.putExtra("money", object.optString("denomination"));
                                    intent.putExtra("keyword", etCode.getText().toString());
                                    startActivity(intent);
                                } else if (dataObject != null) {
                                    if (dataObject.has("voucher_code")) {
                                        //领取代金券成功
                                        JSONObject vounchObj = jsonObject.optJSONObject("datas");
                                        intent = new Intent(RedBagActivity.this, RedBagComfirActivity.class);
                                        intent.putExtra("type", RedBagComfirActivity.VOUNCHER);
                                        intent.putExtra("keyword", etCode.getText().toString());
                                        intent.putExtra("name", vounchObj.optString("voucher_title"));
                                        intent.putExtra("price", vounchObj.optString("voucher_price"));
                                        intent.putExtra("limit", "购物满" + vounchObj.optString("voucher_limit") + "元可用");
                                        intent.putExtra("time", "有效期至" + Util.format1.format(vounchObj.optLong("voucher_end_date") * 1000));
                                        startActivity(intent);
                                    }

                                } else {
                                    showToast(jsonObject.getString("datas"));
                                    if (jsonObject.getString("datas").contains("已领取")||jsonObject.getString("datas").contains("领完毕")) {
                                        intent = new Intent(RedBagActivity.this, RedBagDetailActivity.class);
                                        intent.putExtra("keyword", etCode.getText().toString().trim());
                                        startActivity(intent);
                                    }
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
//        Log.i("zjz","url="+TLUrl.getInstance().URL_hwg_head+ "?act=word_receive&op=index" + "&key=" + MyApplication.getInstance().getMykey() + "&value=" + etCode.getText().toString());
//        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, TLUrl.getInstance().URL_hwg_head+ "?act=word_receive&op=index" + "&key=" + MyApplication.getInstance().getMykey() + "&value=" + etCode.getText().toString(), null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    if(response.optInt("code")==200){
//                        JSONArray array = response.optJSONArray("datas");
//                        JSONObject dataObject=response.optJSONObject("datas");
//                        Intent intent=null;
//                        Log.i("zjz","redbag_msg="+response);
//                        if (array != null) {
//                            JSONObject object = array.getJSONObject(0);
//                            //领取充值卡成功
//                            intent=new Intent(RedBagActivity.this,RedBagComfirActivity.class);
//                            intent.putExtra("type",RedBagComfirActivity.RECHARGE);
//                            intent.putExtra("money",object.optString("denomination"));
//                            intent.putExtra("keyword",etCode.getText().toString());
//                            startActivity(intent);
//                        }else if(dataObject!=null){
//                            if(dataObject.has("voucher_code")){
//                                //领取代金券成功
//                                JSONObject vounchObj=response.optJSONObject("datas");
//                                intent=new Intent(RedBagActivity.this,RedBagComfirActivity.class);
//                                intent.putExtra("type", RedBagComfirActivity.VOUNCHER);
//                                intent.putExtra("keyword", etCode.getText().toString());
//                                intent.putExtra("name", vounchObj.optString("voucher_title"));
//                                intent.putExtra("price", "yen"+vounchObj.optString("voucher_price"));
//                                intent.putExtra("limit", "购物满" +vounchObj.optString("voucher_limit")+"元可用");
//                                intent.putExtra("time", "有效期至"+ Util.format1.format(vounchObj.optLong("voucher_end_date") * 1000));
//                                startActivity(intent);
//                            }
//
//                        }else {
//                            showToast(response.getString("datas"));
//                        }
//                    }
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    Log.i("zjz", e.toString());
//                    e.printStackTrace();
//                }finally {
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
