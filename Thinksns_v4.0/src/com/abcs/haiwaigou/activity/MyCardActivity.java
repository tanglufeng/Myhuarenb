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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.view.RiseNumberTextView;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyCardActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.relative_title)
    RelativeLayout relativeTitle;
    @InjectView(R.id.t_yucun)
    RiseNumberTextView tYucun;
    @InjectView(R.id.t_point)
    RiseNumberTextView tPoint;
    @InjectView(R.id.t_chongzhi)
    RiseNumberTextView tChongzhi;
    @InjectView(R.id.linear_bottom)
    LinearLayout linearBottom;
    //    @InjectView(R.id.img_recharge)
//    ImageView imgRecharge;
    @InjectView(R.id.relative_recharge)
    RelativeLayout relativeRecharge;
    //    @InjectView(R.id.img_cash)
//    ImageView imgCash;
    @InjectView(R.id.relative_cash)
    RelativeLayout relativeCash;
    //    @InjectView(R.id.img_coupon)
//    ImageView imgCoupon;
    @InjectView(R.id.relative_coupon)
    RelativeLayout relativeCoupon;
    //    @InjectView(R.id.img_exchange)
//    ImageView imgExchange;
    @InjectView(R.id.relative_exchange)
    RelativeLayout relativeExchange;
    @InjectView(R.id.img_bank)
    ImageView imgBank;
    @InjectView(R.id.relative_bank)
    RelativeLayout relativeBank;
    @InjectView(R.id.relative_yucun)
    RelativeLayout relativeYucun;
    @InjectView(R.id.relative_point)
    RelativeLayout relativePoint;
    @InjectView(R.id.relative_chongzhi)
    RelativeLayout relativeChongzhi;
    @InjectView(R.id.t_vouncher_num)
    TextView tVouncherNum;
    @InjectView(R.id.t_zong)
    RiseNumberTextView tZong;
    @InjectView(R.id.relative_zong)
    RelativeLayout relativeZong;


    private Handler handler = new Handler();
    MyBroadCastReceiver myBroadCastReceiver;
    private Double yucun = 0.0;
    private String myvoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_my_card);
        ButterKnife.inject(this);
        myBroadCastReceiver = new MyBroadCastReceiver(this, updateUI);
        myBroadCastReceiver.register();
        setOnListener();
        initUserData();

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

    MyBroadCastReceiver.UpdateUI updateUI = new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

        }

        @Override
        public void updateCarNum(Intent intent) {

        }

        @Override
        public void updateCollection(Intent intent) {
            if (intent.getStringExtra("type").equals(MyUpdateUI.RECHARGE)) {
                Log.i("zjz", "更新信息");
                initUserData();
            }
            if (intent.getStringExtra("type").equals(MyUpdateUI.COMMITCASH)) {
                initUserData();
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initUserData() {

        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_user, "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
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
                                JSONObject member = object1.optJSONObject("member_info");
                                yucun = member.optDouble("predepoit", 0.0);
                                if (tYucun != null)
                                    tYucun.withNumber(member.optDouble("predepoit", 0.0)).start();
                                if (tPoint != null)
                                    tPoint.withNumber(member.optDouble("point", 0.0)).start();
                                if (tChongzhi != null)
                                    tChongzhi.withNumber(member.optDouble("available_rc_balance", 0.0)).start();
                                if(tZong!=null)
                                    tZong.withNumber(member.optDouble("available_rc_balance", 0.0)+member.optDouble("predepoit", 0.0)).start();
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

        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_my_vourcher_count, "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.optInt("code") == 200) {
                                JSONObject datas = object.getJSONObject("datas");
                                myvoucher = datas.optString("count");
                                if (tVouncherNum != null)
                                    if (!myvoucher.equals("0")) {
                                        tVouncherNum.setVisibility(View.VISIBLE);
                                        tVouncherNum.setText(myvoucher);
                                    } else {
                                        tVouncherNum.setVisibility(View.GONE);
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

    private void setOnListener() {
        relativeBack.setOnClickListener(this);
        relativeBank.setOnClickListener(this);
        relativeCash.setOnClickListener(this);
        relativeCoupon.setOnClickListener(this);
        relativeExchange.setOnClickListener(this);
        relativeRecharge.setOnClickListener(this);
        relativeChongzhi.setOnClickListener(this);
        relativePoint.setOnClickListener(this);
        relativeYucun.setOnClickListener(this);
    }

    private PopupWindow popupWindow = null;

    @Override
    public void onClick(View v) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Intent intent;
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.relative_recharge:
//                intent = new Intent(this, RechargeActivity.class);
//                startActivity(intent);
                intent = new Intent(this, RechargeActivity.class);
                startActivityForResult(intent, 1);
//                View viewContent = LayoutInflater.from(this).inflate(R.layout.hwg_pop_chongzhi, null);
////                ViewGroup.LayoutParams layoutParams=new LinearLayout.LayoutParams(Util.WIDTH*2/3, ViewGroup.LayoutParams.WRAP_CONTENT);
////                viewContent.setLayoutParams(layoutParams);
//                popupWindow = new PopupWindow(viewContent, Util.WIDTH * 3 / 4, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//                final EditText et_code = (EditText) viewContent.findViewById(R.id.et_code);
//                et_code.setFocusable(true);
//                imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
//                TextView t_cancel = (TextView) viewContent.findViewById(R.id.t_cancel);
//                TextView t_exchange = (TextView) viewContent.findViewById(R.id.t_exchange);
//                t_cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        imm.hideSoftInputFromInputMethod(et_code.getWindowToken(), 0);
//                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
//                                hideSoftInputFromWindow(et_code.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                        popupWindow.dismiss();
//                    }
//                });
//                t_exchange.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ProgressDlgUtil.showProgressDlg("Loading...", MyCardActivity.this);
//                        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_chongzhi, "&rc_sn=" + et_code.getText().toString() + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
//                            @Override
//                            public void revMsg(final String msg) {
//                                handler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        try {
//                                            JSONObject object = new JSONObject(msg);
//                                            if (object.getInt("code") == 200) {
//                                                Log.i("zjz", "msg=" + msg);
//                                                Toast.makeText(MyCardActivity.this, object.optString("datas"), Toast.LENGTH_SHORT).show();
//                                                if (object.optString("datas").contains("成功")) {
//                                                    initUserData();
//                                                    popupWindow.dismiss();
//                                                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
//                                                            hideSoftInputFromWindow(et_code.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                                                }
//                                                ProgressDlgUtil.stopProgressDlg();
//                                            } else {
//                                                ProgressDlgUtil.stopProgressDlg();
//                                                Log.i("zjz", "goodsDetail:解析失败");
//                                            }
//                                        } catch (JSONException e) {
//                                            // TODO Auto-generated catch block
//                                            Log.i("zjz", e.toString());
//                                            Log.i("zjz", msg);
//                                            e.printStackTrace();
//                                            ProgressDlgUtil.stopProgressDlg();
//                                        }
//                                    }
//                                });
//
//                            }
//                        });
//
//                    }
//                });
//                WindowManager.LayoutParams params = getWindow().getAttributes();
//                params.alpha = 0.8f;
//                getWindow().setAttributes(params);
//                popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
////                popupWindow.setTouchable(true);
////                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
////                    @Override
////                    public boolean onTouch(View v, MotionEvent event) {
////
////                        if(v.getId()!=R.id.pop_linear) {
////                            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
////                                    hideSoftInputFromWindow(et_code.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
////                        }
////                        return false;
////                    }
////                });
////                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
////
////                    @Override
////                    public void onDismiss() {
//////                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
//////                                hideSoftInputFromWindow(et_code.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
////                        WindowManager.LayoutParams params = getWindow()
////                                .getAttributes();
////                        params.alpha = 1f;
////                        getWindow().setAttributes(params);
////                    }
////                });
//                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ffffff")));
//                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
//                popupWindow.setOutsideTouchable(false);
//                final EditText et = new EditText(this);
//                et.setBackground(null);


//                AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请输入充值卡号")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                ProgressDlgUtil.showProgressDlg("Loading...", MyCardActivity.this);
//                                HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_chongzhi, "&rc_sn=" + et.getText().toString() + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
//                                    @Override
//                                    public void revMsg(final String msg) {
//                                        handler.post(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                try {
//                                                    JSONObject object = new JSONObject(msg);
//                                                    if (object.getInt("code") == 200) {
//                                                        Log.i("zjz", "msg=" + msg);
//                                                        Toast.makeText(MyCardActivity.this, object.optString("datas"), Toast.LENGTH_SHORT).show();
//                                                        initUserData();
//                                                        ProgressDlgUtil.stopProgressDlg();
//                                                    } else {
//                                                        ProgressDlgUtil.stopProgressDlg();
//                                                        Log.i("zjz", "goodsDetail:解析失败");
//                                                    }
//                                                } catch (JSONException e) {
//                                                    // TODO Auto-generated catch block
//                                                    Log.i("zjz", e.toString());
//                                                    Log.i("zjz", msg);
//                                                    e.printStackTrace();
//                                                    ProgressDlgUtil.stopProgressDlg();
//                                                }
//                                            }
//                                        });
//
//                                    }
//                                });
//                                imm.hideSoftInputFromInputMethod(et.getWindowToken(), 0);
//                            }
//                        })
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                imm.hideSoftInputFromInputMethod(et.getWindowToken(), 0);
//                                dialog.dismiss();
//                            }
//                        })
//                        .setView(et)
//                        .create();
//                dialog.show();
                break;
            case R.id.relative_cash:
                if (yucun == 0.0) {
                    showToast("您的预存款为0，无法进行提现申请！");
                    return;
                }
                intent = new Intent(this, BindPhoneActivity.class);
                intent.putExtra("title", "提现申请");
                intent.putExtra("type", BindPhoneActivity.APPLYCASH);
                startActivity(intent);
//                intent = new Intent(this, ApplyCashActivity.class);
//                intent.putExtra("cash", yucun);
//                startActivity(intent);

                break;
            case R.id.relative_coupon:
                if (MyApplication.getInstance().getMykey() == null) {
                    intent = new Intent(this, WXEntryActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, MyRechargeActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.relative_exchange:
                if (MyApplication.getInstance().getMykey() == null) {
                    intent = new Intent(this, WXEntryActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, ExchangePointActivity.class);
                    intent.putExtra("position", 0);
                    startActivity(intent);
                }
                break;
            case R.id.relative_bank:
                break;

            case R.id.relative_point:
                if (MyApplication.getInstance().getMykey() == null) {
                    intent = new Intent(this, WXEntryActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, ExchangePointActivity.class);
                    intent.putExtra("position", 2);
                    startActivity(intent);
                }
                break;
            case R.id.relative_chongzhi:
                if (MyApplication.getInstance().getMykey() == null) {
                    intent = new Intent(this, WXEntryActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, ChongzhiDetailActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.relative_yucun:
                if (MyApplication.getInstance().getMykey() == null) {
                    intent = new Intent(this, WXEntryActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, PreDepositActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1 && data != null) {
            initUserData();
        }
    }

    @Override
    protected void onDestroy() {
        myBroadCastReceiver.unRegister();
        super.onDestroy();
    }
}
