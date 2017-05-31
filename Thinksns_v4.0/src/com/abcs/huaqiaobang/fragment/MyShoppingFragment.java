package com.abcs.huaqiaobang.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.activity.AddressActivity;
import com.abcs.haiwaigou.activity.BindPhoneActivity;
import com.abcs.haiwaigou.activity.CartActivity2;
import com.abcs.haiwaigou.activity.CollectionActivity;
import com.abcs.haiwaigou.activity.CommentAndShareActivity;
import com.abcs.haiwaigou.activity.OrderActivity;
import com.abcs.haiwaigou.activity.ReturnAndRefundActivity;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.view.RiseNumberTextView;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/1/21.
 */
public class MyShoppingFragment extends Fragment implements View.OnClickListener {

    @InjectView(R.id.my_rl_cart)
    RelativeLayout myRlCart;
    @InjectView(R.id.my_rl_order)
    RelativeLayout myRlOrder;
    @InjectView(R.id.my_rl_collection)
    RelativeLayout myRlCollection;
    @InjectView(R.id.my_rl_deliver)
    RelativeLayout myRlDeliver;
    @InjectView(R.id.my_rl_comment)
    RelativeLayout myRlComment;
    @InjectView(R.id.my_rl_Recharge)
    RelativeLayout myRlRecharge;
    @InjectView(R.id.my_vIntegral)
    RiseNumberTextView myVIntegral;
    @InjectView(R.id.my_vDeposit)
    RiseNumberTextView myVDeposit;
    @InjectView(R.id.my_vRecharge)
    RiseNumberTextView myVRecharge;
    @InjectView(R.id.my_rl_modifyPaypwd)
    RelativeLayout myRlModifyPaypwd;
    @InjectView(R.id.t_modify_paypwd)
    TextView tModifyPaypwd;
    @InjectView(R.id.my_rl_refund)
    RelativeLayout myRlRefund;
    private View view;

    private Handler handler = new Handler();
    String paypwd;
    boolean isPayPwd = false;
    boolean isBindEmail;
    boolean isBindPhone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_my_shopping_item, null);
        }
        ButterKnife.inject(this, view);

//        findView();
        initUser();
        myRlCart.setOnClickListener(this);
        myRlOrder.setOnClickListener(this);
        myRlCollection.setOnClickListener(this);
        myRlDeliver.setOnClickListener(this);
        myRlRecharge.setOnClickListener(this);

        myRlComment.setOnClickListener(this);
        myRlRefund.setOnClickListener(this);
        getActivity().registerReceiver(receiver, new IntentFilter("com.abcs.huaqiaobang.changeuser"));

        initUserData();

        myRlModifyPaypwd.setOnClickListener(this);
        return view;
    }

//    private void findView() {
//        @InjectView(R.id.my_vIntegral)
//        RiseNumberTextView myVIntegral;
//        @InjectView(R.id.my_vDeposit)
//        RiseNumberTextView myVDeposit;
//        @InjectView()
//        RiseNumberTextView myVRecharge;
//        @InjectView(R.id.t_modify_paypwd)
//        TextView tModifyPaypwd;
//        myRlCart= (RelativeLayout) view.findViewById(R.id.my_rl_cart);
//        myRlOrder= (RelativeLayout) view.findViewById(R.id.my_rl_order);
//        myRlCollection= (RelativeLayout) view.findViewById(R.id.my_rl_collection);
//        myRlDeliver= (RelativeLayout) view.findViewById(R.id.my_rl_deliver);
//        myRlComment= (RelativeLayout) view.findViewById(R.id.my_rl_comment);
//        myRlRecharge= (RelativeLayout) view.findViewById(R.id.my_rl_Recharge);
//        myRlModifyPaypwd= (RelativeLayout) view.findViewById(R.id.my_rl_modifyPaypwd);
//        tModifyPaypwd= (TextView) view.findViewById(R.id.t_modify_paypwd);
//        myVIntegral= (RiseNumberTextView) view.findViewById(R.id.my_vIntegral);
//        myVIntegral= (RiseNumberTextView) view.findViewById(R.id.my_vRecharge);
//        myVIntegral= (RiseNumberTextView) view.findViewById(R.id.my_vIntegral);
//
//
//
//
//    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initUser();
            initUserData();
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
                                myVDeposit.withNumber(member.optDouble("predepoit", 0.0)).start();
                                myVIntegral.withNumber(member.optDouble("point", 0.0)).start();
                                myVRecharge.withNumber(member.optDouble("available_rc_balance", 0.0)).start();

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
                                paypwd = data.optString("member_paypwd");
                                Log.i("zjz", "pwd=" + paypwd);
                                tModifyPaypwd.setText(paypwd.equals("null") ? "设置支付密码" : "修改支付密码");
                                Log.i("zjz", "isTrue=" + (paypwd.equals("null")));
                                if (paypwd.equals("null")) {
                                    isPayPwd = false;
                                } else {
                                    isPayPwd = true;
                                }
                                if (data.optString("member_email_bind").equals("1")) {
//                                    email = data.optString("member_email");
                                    isBindEmail = true;
                                } else {
                                    isBindEmail = false;
                                }
                                if (data.optString("member_mobile_bind").equals("1")) {
//                                    phone = data.optString("member_mobile");
                                    isBindPhone = true;
                                } else {
                                    isBindPhone = false;
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
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        ButterKnife.reset(this);
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onClick(final View v) {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        Intent intent;

        switch (v.getId()) {
            case R.id.my_rl_cart:
                intent = new Intent(getActivity(), CartActivity2.class);
                intent.putExtra("store_id","");
                startActivity(intent);
                break;
            case R.id.my_rl_order:
                if (MyApplication.getInstance().getMykey() == null) {
                    intent = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), OrderActivity.class);
                    startActivity(intent);
                }


                break;
            case R.id.my_rl_collection:
                if (MyApplication.getInstance().getMykey() == null) {
                    intent = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), CollectionActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.my_rl_deliver:

                if (MyApplication.getInstance().getMykey() == null) {
                    intent = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), AddressActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.my_rl_comment:

                if (MyApplication.getInstance().getMykey() == null) {
                    intent = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), CommentAndShareActivity.class);
                    startActivity(intent);
                }

                break;

            case R.id.my_rl_Recharge:
                final EditText et = new EditText(getContext());
                et.setBackground(null);
                AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("请输入充值卡号")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ProgressDlgUtil.showProgressDlg("Loading...", getActivity());
                                HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_chongzhi, "&rc_sn=" + et.getText().toString() + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
                                    @Override
                                    public void revMsg(final String msg) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    JSONObject object = new JSONObject(msg);
                                                    if (object.getInt("code") == 200) {
                                                        Log.i("zjz", "msg=" + msg);
                                                        Toast.makeText(getActivity(), object.optString("datas"), Toast.LENGTH_SHORT).show();
                                                        if (object.optString("datas").contains("成功")) {
                                                            initUserData();
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
                                imm.hideSoftInputFromInputMethod(et.getWindowToken(), 0);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                imm.hideSoftInputFromInputMethod(et.getWindowToken(), 0);
                                dialog.dismiss();
                            }
                        })
                        .setView(et)
                        .create();
                dialog.show();

                break;

            case R.id.my_rl_refund:

                if (MyApplication.getInstance().getMykey() == null) {
                    intent = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), ReturnAndRefundActivity.class);
                    startActivity(intent);
                }

                break;

            case R.id.my_rl_modifyPaypwd:
                if (isBindEmail || isBindPhone) {
                    intent = new Intent(getActivity(), BindPhoneActivity.class);
                    if (isPayPwd) {
                        intent.putExtra("isFirst", false);
                        intent.putExtra("title", "修改支付密码");
                    } else {
                        intent.putExtra("isFirst", true);
                        intent.putExtra("title", "设置支付密码");
                    }
                    intent.putExtra("type", BindPhoneActivity.BINDPAYPWD);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "请先去个人模块绑定手机号或邮箱！", Toast.LENGTH_SHORT).show();
                }

//                final EditText etpwd = new EditText(getContext());
//                final EditText etconfirmpwd = new EditText(getContext());
//                LinearLayout linearLayout = new LinearLayout(getContext());
//                View viewline = new View(getContext());
//                viewline.setBackgroundColor(Color.parseColor("#ffcdcdcd"));
//                viewline.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
//                etpwd.setBackground(null);
//                etconfirmpwd.setBackground(null);
//                etpwd.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                etconfirmpwd.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                etpwd.setHint("请输入支付密码");
//                etconfirmpwd.setHint("请确认支付密码");
//                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                linearLayout.setOrientation(LinearLayout.VERTICAL);
//                linearLayout.addView(etpwd);
//                linearLayout.addView(viewline);
//                linearLayout.addView(etconfirmpwd);
//                AlertDialog dialogpwd = new AlertDialog.Builder(getContext()).setTitle("设置支付密码")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (etpwd.getText().toString().equals("")) {
//                                    Toast.makeText(getActivity(), "支付密码不能为空！", Toast.LENGTH_SHORT).show();
//                                    imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
//                                    return;
//                                } else if (etconfirmpwd.getText().toString().equals("")) {
//                                    Toast.makeText(getActivity(), "确认支付密码不能为空！", Toast.LENGTH_SHORT).show();
//                                    imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
//                                    return;
//                                } else if (!etpwd.getText().toString().equals(etconfirmpwd.getText().toString())) {
//                                    Toast.makeText(getActivity(), "两次密码不一样！请重新输入！", Toast.LENGTH_SHORT).show();
//                                    imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
//                                    return;
//                                }
//                                ProgressDlgUtil.showProgressDlg("Loading...", getActivity());
//                                HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_paypwd, "&password=" + etpwd.getText().toString() + "&confirm_password=" + etconfirmpwd.getText().toString() + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
//                                    @Override
//                                    public void revMsg(final String msg) {
//                                        handler.post(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                try {
//                                                    JSONObject object = new JSONObject(msg);
//                                                    if (object.getInt("code") == 200) {
//                                                        Log.i("zjz", "msg=" + msg);
//                                                        Toast.makeText(getActivity(), object.optString("datas"), Toast.LENGTH_SHORT).show();
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
//                                imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
//                            }
//                        })
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
//                                dialog.dismiss();
//                            }
//                        }).setView(linearLayout)
//                        .create();
//                dialogpwd.show();

                break;
        }

    }

    private void hideSoftInput() {
        InputMethodManager im = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                        .getApplicationWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
