package com.abcs.huaqiaobang.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.activity.DingQiActivity;
import com.abcs.huaqiaobang.activity.MyProductActivity;
import com.abcs.huaqiaobang.activity.NoticeDialogActivity;
import com.abcs.huaqiaobang.activity.RecordActivity;
import com.abcs.huaqiaobang.model.User;
import com.abcs.huaqiaobang.presenter.UserDataInterface;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.MyCustomer;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/5/24 0024.
 */
public class UserFinanceFragment extends Fragment implements UserDataInterface, View.OnClickListener {

    @InjectView(R.id.img_licai_more)
    ImageView imgLicaiMore;
    @InjectView(R.id.img_licai)
    ImageView imgLicai;
    @InjectView(R.id.rl_more_licai)
    RelativeLayout rlMoreLicai;
    @InjectView(R.id.t_shouyi_num)
    TextView tShouyiNum;
    @InjectView(R.id.t_shouyi)
    TextView tShouyi;
    @InjectView(R.id.t_zichan_num)
    TextView tZichanNum;
    @InjectView(R.id.t_zichan)
    TextView tZichan;
    @InjectView(R.id.t_yesterday_num)
    TextView tYesterdayNum;
    @InjectView(R.id.t_yesterday)
    TextView tYesterday;
    @InjectView(R.id.img_licai_goods)
    ImageView imgLicaiGoods;
    @InjectView(R.id.t_licai_goods)
    TextView tLicaiGoods;
    @InjectView(R.id.rl_product)
    RelativeLayout rlProduct;
    @InjectView(R.id.img_record)
    ImageView imgRecord;
    @InjectView(R.id.t_record)
    TextView tRecord;
    @InjectView(R.id.rl_record)
    RelativeLayout rlRecord;
    @InjectView(R.id.img_kehu)
    ImageView imgKehu;
    @InjectView(R.id.t_kehu)
    TextView tKehu;
    @InjectView(R.id.rl_customer)
    RelativeLayout rlCustomer;
    private View view;
    Activity activity;
    private Handler handler = new Handler();
    MyBroadCastReceiver myBroadCastReceiver;
    public static UserFinanceFragment newInstance() {
        UserFinanceFragment userFinanceFragment = new UserFinanceFragment();
        return userFinanceFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        activity = getActivity();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.hwg_fragment_user_finance, null);
        }
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);
        ButterKnife.inject(this, view);
        myBroadCastReceiver=new MyBroadCastReceiver(activity,updateUI);
        myBroadCastReceiver.register();
        initUserView();
        setOnListener();
        return view;
    }

    MyBroadCastReceiver.UpdateUI updateUI=new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

        }

        @Override
        public void updateCarNum(Intent intent) {

        }

        @Override
        public void updateCollection(Intent intent) {
            if (intent.getStringExtra("type").equals(MyUpdateUI.CHANGEUSER)) {
                initUserView();
                Log.i("zjz", "UserFinance更新用户");
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };
    private void setOnListener() {
        rlProduct.setOnClickListener(this);
        rlMoreLicai.setOnClickListener(this);
        rlRecord.setOnClickListener(this);
        rlCustomer.setOnClickListener(this);
    }

    private void initUserView() {
        User self = MyApplication.getInstance().self;
        if (self != null) {
            getUserCash();
        } else {
            if(tZichanNum!=null)
            tZichanNum.setText("0.00");
            if(tShouyiNum!=null)
            tShouyiNum.setText("0.00");
            if(tYesterdayNum!=null)
            tYesterdayNum.setText("0.00");
        }
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
//                if (msg.equals("") && MyApplication.getInstance().self != null) {
//                    getUserCash();
//                    return;
//                }
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
                                if (tZichanNum != null)
                                    tZichanNum.setText(Util.df.format(MyApplication.getInstance().self.getTotalAssets() / 100));
                                if (tYesterdayNum != null)
                                    tYesterdayNum.setText(Util.df.format(MyApplication.getInstance().self.getEarningsYesterday() / 100));
                                if (tShouyiNum != null)
                                    tShouyiNum.setText(Util.df.format(MyApplication.getInstance().self.getAllEarnings() / 100));
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        myBroadCastReceiver.unRegister();
    }

    @Override
    public void loginSuccess() {
        Log.i("zjz", "UserFinanceFragment更换用户信息");
        initUserView();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (MyApplication.getInstance().self == null) {
            Toast.makeText(getContext(), "请登录", Toast.LENGTH_SHORT).show();
            intent = new Intent(getActivity(), WXEntryActivity.class);
            startActivity(intent);
            return;
        }
        switch (v.getId()) {
            case R.id.rl_product:
                startActivity(new Intent(getContext(), DingQiActivity.class));
                break;
            case R.id.rl_more_licai:

                Log.i("zds","onclide");
                intent = new Intent(getActivity(), MyProductActivity.class);
                startActivity(intent);
                break;
            //交易记录
            case R.id.rl_record:
                intent = new Intent(getActivity(), RecordActivity.class);
                startActivity(intent);
                break;
            //客户群
            case R.id.rl_customer:
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
        }
    }
}
