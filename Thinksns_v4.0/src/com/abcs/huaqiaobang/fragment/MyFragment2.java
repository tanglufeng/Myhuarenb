package com.abcs.huaqiaobang.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.activity.CartActivity2;
import com.abcs.haiwaigou.activity.CollectionActivity;
import com.abcs.haiwaigou.activity.CommentAndShareActivity;
import com.abcs.haiwaigou.activity.MyCardActivity;
import com.abcs.haiwaigou.activity.OrderActivity;
import com.abcs.haiwaigou.activity.ReturnAndRefundActivity;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.activity.DingQiActivity;
import com.abcs.huaqiaobang.activity.HeaderViewActivity;
import com.abcs.huaqiaobang.activity.MyProductActivity;
import com.abcs.huaqiaobang.activity.NoticeDialogActivity;
import com.abcs.huaqiaobang.activity.RecordActivity;
import com.abcs.huaqiaobang.activity.SettinsActivity;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.model.User;
import com.abcs.huaqiaobang.presenter.UserDataInterface;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.MyCustomer;
import com.abcs.huaqiaobang.wxapi.RegisterActivity;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhou on 2016/4/26.
 */
public class MyFragment2 extends Fragment implements UserDataInterface {


    @InjectView(R.id.rl_back)
    RelativeLayout rlBack;
    @InjectView(R.id.img_settings)
    ImageView imgSettings;
    @InjectView(R.id.imageView)
    ImageView imageView;
    @InjectView(R.id.tv_VipLever)
    TextView tvVipLever;
    @InjectView(R.id.relative_head)
    RelativeLayout relativeHead;
    @InjectView(R.id.my_tv_name)
    TextView myTvName;
    @InjectView(R.id.my_userId)
    TextView myUserId;
    @InjectView(R.id.t_huati_num)
    TextView tHuatiNum;
    @InjectView(R.id.t_huati)
    TextView tHuati;
    @InjectView(R.id.t_activity_num)
    TextView tActivityNum;
    @InjectView(R.id.t_activity)
    TextView tActivity;
    @InjectView(R.id.t_club_num)
    TextView tClubNum;
    @InjectView(R.id.t_club)
    TextView tClub;
    @InjectView(R.id.t_friends_num)
    TextView tFriendsNum;
    @InjectView(R.id.t_friends)
    TextView tFriends;
    @InjectView(R.id.img_order)
    ImageView imgOrder;
    @InjectView(R.id.img_order_more)
    ImageView imgOrderMore;
    @InjectView(R.id.rl_allOrder)
    RelativeLayout rlAllOrder;
    @InjectView(R.id.rl_order_noPay)
    RelativeLayout rlOrderNoPay;
    @InjectView(R.id.img_diliver)
    ImageView imgDiliver;
    @InjectView(R.id.rl_order_delivery)
    RelativeLayout rlOrderDelivery;
    @InjectView(R.id.img_receive)
    ImageView imgReceive;
    @InjectView(R.id.rl_order_goods)
    RelativeLayout rlOrderGoods;
    @InjectView(R.id.rl_order_evaluation)
    RelativeLayout rlOrderEvaluation;
    @InjectView(R.id.img_pay)
    ImageView imgPay;
    @InjectView(R.id.rl_order_tuihuo)
    RelativeLayout rlOrderTuihuo;
    @InjectView(R.id.img_licai)
    ImageView imgLicai;
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
    @InjectView(R.id.img_card)
    ImageView imgCard;
    @InjectView(R.id.rl_myCard)
    RelativeLayout rlMyCard;
    @InjectView(R.id.img_collection)
    ImageView imgCollection;
    @InjectView(R.id.rl_myCollection)
    RelativeLayout rlMyCollection;
    @InjectView(R.id.rl_settings)
    RelativeLayout rlSettings;

    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, null);


        ButterKnife.inject(this, view);
        //设置图片旋转
        imgSettings.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate_settings));


        initUserView();

        return view;
    }

    private void initUserView() {

        User self = MyApplication.getInstance().self;
        if (self != null) {

            ImageLoader.getInstance().displayImage(self.getAvatarUrl(), imageView, Options.getCircleListOptions());

            myTvName.setText(self.getNickName().equals(self.getId()) ? self.getUserName() : self.getNickName());
            myUserId.setText("ID：" + self.getId());
//            getUserBanks();
            getUserCash();
        } else {
            SpannableString spanstr = new SpannableString("登录/注册");
//        spanstr.setSpan(new ForegroundColorSpan(Color.BLUE), 0, spanstr.length(),
//
//                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spanstr.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.WHITE);       //设置文件颜色
                    ds.setUnderlineText(false);      //设置下划线
                }

                @Override
                public void onClick(View widget) {

                    startActivity(new Intent(getContext(), WXEntryActivity.class));
                }
            }, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanstr.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.WHITE);       //设置文件颜色
                    ds.setUnderlineText(false);      //设置下划线
                }

                @Override
                public void onClick(View widget) {

                    startActivity(new Intent(getContext(), RegisterActivity.class));
                }
            }, 3, spanstr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            myTvName.setHighlightColor(Color.TRANSPARENT);
            myTvName.setText(spanstr);
            myTvName.setMovementMethod(LinkMovementMethod.getInstance());
            tZichanNum.setText("0.00");
            tShouyiNum.setText("0.00");
            tYesterdayNum.setText("0.00");
            myUserId.setText("");
            imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.img_hwg_my_nologin));
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
                                tZichanNum.setText(Util.df
                                        .format(MyApplication.getInstance().self
                                                .getTotalAssets() / 100));
                                tYesterdayNum.setText(Util.df.format(MyApplication
                                        .getInstance().self
                                        .getEarningsYesterday() / 100));
                                tShouyiNum.setText(Util.df.format(MyApplication
                                        .getInstance().self.getAllEarnings() / 100));
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
//                            getUserBanks();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.rl_order_noPay, R.id.rl_order_goods, R.id.rl_order_delivery
            , R.id.rl_order_evaluation, R.id.rl_order_tuihuo, R.id.rl_allOrder,
            R.id.rl_customer, R.id.rl_record, R.id.rl_product, R.id.imageView,
            R.id.rl_myCollection, R.id.rl_myshaidan, R.id.rl_myCart, R.id.rl_more_licai
            , R.id.rl_myCard
    })
    public void onClick(View view) {
        Intent intent;
        if (MyApplication.getInstance().self == null) {
            Toast.makeText(getContext(), "请登录", Toast.LENGTH_SHORT).show();
            intent = new Intent(getActivity(), WXEntryActivity.class);
            startActivity(intent);
            return;
        }
        switch (view.getId()) {
            //待支付
            case R.id.rl_order_noPay:
                intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("position", 1);
                startActivity(intent);
                break;
            //待收货
            case R.id.rl_order_goods:
                intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("position", 3);
                startActivity(intent);
                break;
            //待发货
            case R.id.rl_order_delivery:
                intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("position", 2);
                startActivity(intent);
                break;
            //待评价
            case R.id.rl_order_evaluation:
                intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("position", 5);
                startActivity(intent);
                break;
            //退货
            case R.id.rl_order_tuihuo:
                intent = new Intent(getActivity(), ReturnAndRefundActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_allOrder:
                intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("position", 0);
                startActivity(intent);
                break;

            case R.id.rl_product:

                startActivity(new Intent(getContext(), DingQiActivity.class));
                break;
            case R.id.rl_more_licai:

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

            //修改头像
            case R.id.imageView:

                intent = new Intent(getActivity(), HeaderViewActivity.class);
                startActivityForResult(intent, 1);

                break;

            case R.id.rl_myCollection:

                intent = new Intent(getActivity(), CollectionActivity.class);
                startActivity(intent);

                break;
            case R.id.rl_myshaidan:

                intent = new Intent(getActivity(), CommentAndShareActivity.class);
                startActivity(intent);

                break;
            case R.id.rl_myCart:

                intent = new Intent(getActivity(), CartActivity2.class);
                intent.putExtra("store_id","");
                startActivity(intent);

                break;
            case R.id.rl_myCard:

                intent = new Intent(getActivity(), MyCardActivity.class);
                startActivity(intent);

                break;


        }

    }

    @Override
    public void loginSuccess() {
        initUserView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //修改头像回调
        if (resultCode == 1 && requestCode == 1) {

            ImageLoader.getInstance().displayImage(MyApplication.getInstance().self.getAvatarUrl()
                    , imageView, Options.getCircleListOptions());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.rl_settings, R.id.rl_settings2})
    public void onSettings(View view) {

        switch (view.getId()) {
            case R.id.rl_settings:

                startActivity(new Intent(getContext(), SettinsActivity.class));
                break;
            case R.id.rl_settings2:

                startActivity(new Intent(getContext(), SettinsActivity.class));
                break;


        }

    }
}
