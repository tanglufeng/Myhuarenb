package com.abcs.huaqiaobang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.activity.DingQiActivity;
import com.abcs.huaqiaobang.activity.MyProductActivity;
import com.abcs.huaqiaobang.activity.NoticeDialogActivity;
import com.abcs.huaqiaobang.activity.RecordActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.RiseNumberTextView;
import com.abcs.huaqiaobang.wxapi.MyCustomer;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/1/21.
 */
public class MyFinanceFragment extends Fragment implements View.OnClickListener {

    @InjectView(R.id.my_vCumulativeProfit)
    RiseNumberTextView myVCumulativeProfit;
    @InjectView(R.id.my_vAllProfit)
    RiseNumberTextView myVAllProfit;
    @InjectView(R.id.my_vProfit)
    RiseNumberTextView myVProfit;
    @InjectView(R.id.yitoujinqian)
    TextView yitoujinqian;
    @InjectView(R.id.rl_yue)
    RelativeLayout rlYue;
    @InjectView(R.id.jiaoyijilu)
    TextView jiaoyijilu;
    @InjectView(R.id.touzi)
    RelativeLayout touzi;
    @InjectView(R.id.modifypaypwd)
    RelativeLayout modifypaypwd;
    @InjectView(R.id.mycustomer)
    RelativeLayout mycustomer;
    @InjectView(R.id.im2)
    ImageView im2;
    @InjectView(R.id.im_1)
    ImageView im1;
    @InjectView(R.id.dingqi)
    RelativeLayout dingqi;
    private View view;

    private boolean isShow = true;
    private Handler handler = new Handler();
    private int total;
    private long initTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_my_finance_item, null);
        }

        ButterKnife.inject(this, view);

        init();


        return view;
    }

    private void init() {

        rlYue.setOnClickListener(this);
        touzi.setOnClickListener(this);
        mycustomer.setOnClickListener(this);
        dingqi.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {

            if (isShow) {
                isShow = false;
                myVAllProfit.withNumber(
                        Double.parseDouble(myVAllProfit.getText().toString()))
                        .start();
                myVProfit.withNumber(Double.parseDouble(myVProfit.getText().toString()))
                        .start();
                myVCumulativeProfit.withNumber(Double.parseDouble(myVCumulativeProfit.getText().toString()))
                        .start();
            }

            if (MyApplication.getInstance().self != null && MyApplication.getInstance().self
                    .getTotalAssets() == 0) {
                initUser();
            } else if (MyApplication.getInstance().self != null) {
                long temptime = System.currentTimeMillis();
                if (!Util.getDateOnlyDat(temptime).equals(Util.getDateOnlyDat(initTime))) {
                    initUser();
                }
            }
        }

    }

    public void initUser() {

        initTime = System.currentTimeMillis();
        getUserCash();
        getUserBanks();
//        getBankList();
//        checkUserisbindidenity();
        getUserChanges();
    }

    public void getUserCash() {
        LogUtil.e(
                "getUserCash",
                "method=getUserInfo&uid="
                        + MyApplication.getInstance().self.getId() + "&token="
                        + Util.token);
        HttpRequest.sendPost(TLUrl.getInstance().URL_userServlet, "method=getUserInfo&uid="
                + MyApplication.getInstance().self.getId() + "&token="
                + Util.token, new HttpRevMsg() {

            @Override
            public void revMsg(String msg) {
                LogUtil.e("getUserCash", msg);
                if (msg.equals("") && MyApplication.getInstance().self != null) {
                    getUserCash();
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(msg);
                    if (jsonObject.getInt("status") == 1) {
                        JSONObject object = jsonObject.getJSONObject("msg");
                        MyApplication.getInstance().self
                                .setAllEarnings((float) object
                                        .getDouble("allEarnings"));
                        MyApplication.getInstance().self
                                .setEarningsYesterday((float) object
                                        .getDouble("earningsYesterday"));
                        MyApplication.getInstance().self.setInvestCount(object
                                .getInt("investCount"));
                        MyApplication.getInstance().self
                                .setTotalAssets((float) object
                                        .getDouble("totalAssets"));
                        MyApplication.getInstance().self
                                .setTotalInvest((float) object
                                        .getDouble("totalInvest"));
                        // {"msg":{"allEarnings":86,"createDate":1445774262045,"earningsYesterday":29,"investCount":4,"totalAssets":121686,"totalInvest":121600,"uid":"10017"},"status":1}
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                myVAllProfit.setText(Util.df
                                        .format(MyApplication.getInstance().self
                                                .getTotalAssets() / 100));
                                myVProfit.setText(Util.df.format(MyApplication
                                        .getInstance().self
                                        .getEarningsYesterday() / 100));
                                myVCumulativeProfit.setText(Util.df.format(MyApplication
                                        .getInstance().self.getAllEarnings() / 100));
                                yitoujinqian.setText(Util.df.format(MyApplication.getInstance().self
                                        .getTotalInvest() / 100) + "元");
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getUserBanks() {
//        LogUtil.e("getUserBanks", "method=requestBanks&identityid="
//                + MyApplication.getInstance().self.getId() + "&token="
//                + Util.token);
        HttpRequest.sendPost(TLUrl.getInstance().URL_bindBank,
                "method=requestBanks&identityid="
                        + MyApplication.getInstance().self.getId() + "&token="
                        + Util.token, new HttpRevMsg() {

                    @Override
                    public void revMsg(String msg) {
                        LogUtil.e("getUserBanks", msg);
                        if (msg.equals("")
                                && MyApplication.getInstance().self != null) {
                            getUserBanks();
                            return;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.getInt("status") == 1) {
                                MyApplication.getInstance().self
                                        .setBanks(jsonObject
                                                .getJSONArray("msg"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private void getUserChanges() {
//        LogUtil.e("getUserChanges", "method=getTradingCount&uid="
//                + MyApplication.getInstance().self.getId() + "&token="
//                + Util.token);
        HttpRequest.sendPost(TLUrl.getInstance().URL_productServlet,
                "method=getTradingCount&uid="
                        + MyApplication.getInstance().self.getId() + "&token="
                        + Util.token, new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {
                        LogUtil.e("getUserChanges", msg);
                        if (msg.equals("")
                                && MyApplication.getInstance().self != null) {
                            getUserChanges();
                            return;
                        }
                        ProgressDlgUtil.stopProgressDlg();
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(msg);
                                    if (jsonObject.getInt("status") == 1) {
                                        total = jsonObject.getInt("msg");
                                        jiaoyijilu.setText(jsonObject
                                                .getInt("msg") + "次交易");
                                    }
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();

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
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {


            //理财资产
            case R.id.rl_yue:
                intent = new Intent(getActivity(), MyProductActivity.class);
                startActivity(intent);
                break;
            //交易记录
            case R.id.touzi:
                intent = new Intent(getActivity(), RecordActivity.class);
                startActivity(intent);
                break;
            //vip
            case R.id.mycustomer:
                if (MyApplication.getInstance().self != null) {
                    intent = new Intent(getActivity(), MyCustomer.class);
                    String param = "method=getoffline&uid=" + MyApplication.getInstance().self.getId() + "&token=" + Util.token;
                    HttpRequest.sendGet(TLUrl.getInstance().URL_customer, param, new HttpRevMsg() {
                        @Override
                        public void revMsg(final String msg) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    com.alibaba.fastjson.JSONObject custom = com.alibaba.fastjson.JSONObject.parseObject(msg);

                                    if (custom.size() != 0 && custom.getInteger("status") == 1) {

                                        startActivity(new Intent(getActivity(), MyCustomer.class).putExtra("msg", msg));

                                    } else {
                                        startActivity(new Intent(getActivity(), NoticeDialogActivity.class).putExtra("msg", "您还没有该权限"));
                                    }
                                }
                            });

                        }
                    });
                }
                break;

            case R.id.dingqi:
                startActivity(new Intent(getContext(), DingQiActivity.class));
                break;

        }

    }


    public void refreshcash() {

        jiaoyijilu.setText(++total + "次交易");
    }
}
