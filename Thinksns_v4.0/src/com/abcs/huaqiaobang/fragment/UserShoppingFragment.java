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

import com.abcs.haiwaigou.activity.CartActivity2;
import com.abcs.haiwaigou.activity.CollectionActivity;
import com.abcs.haiwaigou.activity.CommentAndShareActivity;
import com.abcs.haiwaigou.activity.KefuActivity;
import com.abcs.haiwaigou.activity.MyCardActivity;
import com.abcs.haiwaigou.activity.OrderActivity;
import com.abcs.haiwaigou.activity.ReturnAndRefundActivity;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.yyg.activity.MyYYGActivity;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.presenter.UserDataInterface;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/5/24 0024.
 * 我的fragment的购物的页面
 */
public class UserShoppingFragment extends Fragment implements View.OnClickListener, UserDataInterface {

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
    @InjectView(R.id.img_cart)
    ImageView imgCart;
    @InjectView(R.id.rl_myCart)
    RelativeLayout rlMyCart;
    @InjectView(R.id.img_saidan)
    ImageView imgSaidan;
    @InjectView(R.id.rl_myshaidan)
    RelativeLayout rlMyshaidan;
    @InjectView(R.id.img_card)
    ImageView imgCard;
    @InjectView(R.id.rl_myCard)
    RelativeLayout rlMyCard;
    @InjectView(R.id.img_collection)
    ImageView imgCollection;
    @InjectView(R.id.rl_myCollection)
    RelativeLayout rlMyCollection;
    @InjectView(R.id.t_unpay_num)
    TextView tUnpayNum;
    @InjectView(R.id.t_receive_num)
    TextView tReceiveNum;
    @InjectView(R.id.t_deliver_num)
    TextView tDeliverNum;
    @InjectView(R.id.t_comment_num)
    TextView tCommentNum;
    @InjectView(R.id.t_return_num)
    TextView tReturnNum;
    @InjectView(R.id.t_vouncher_num)
    TextView tVouncherNum;
    @InjectView(R.id.rl_myKefu)
    RelativeLayout rlMyKefu;
    @InjectView(R.id.rl_myYYG)
    RelativeLayout rlMyYYG;
    private View view;
    Activity activity;

    private String cancel;
    private String unpay;
    private String receriver;
    private String deliver;
    private String uncomment;
    private String commented;
    private String all;
    private String tuihuo;
    private String myvoucher;
    private Handler handler = new Handler();
    private MyBroadCastReceiver myBroadCastReceiver;

    public static UserShoppingFragment newInstance() {
        UserShoppingFragment userShoppingFragment = new UserShoppingFragment();
        return userShoppingFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        LogUtil.i("haotest","onCreateView()");
        EventBus.getDefault().register(this);
        if (view == null) {
            view = activity.getLayoutInflater().inflate(
                    R.layout.hwg_fragment_user_shop, null);
        }
        ButterKnife.inject(this, view);
        myBroadCastReceiver = new MyBroadCastReceiver(getActivity(), updateUI);
        myBroadCastReceiver.register();
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);
        setOnListener();
        if (MyApplication.getInstance().getMykey() != null)
            initNum();
        return view;
    }

    //观察者监听返回发送的监听事件
    @Subscribe
    public void onEvent(Integer event) {
        switch (event) {
            case 829:
                break;
            default:
                break;
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
            if (intent.getStringExtra("type").equals(MyUpdateUI.MYORDERNUM)) {
                if (MyApplication.getInstance().getMykey() != null) {
                    initNum();
                    Log.i("zjz", "update_order_num");
                }
            } else if (intent.getStringExtra("type").equals(MyUpdateUI.CHANGEUSER)) {
                Log.i("zjz", "UserShopping更换用户");
                if (MyApplication.getInstance().getMykey() != null) {
                    initNum();
                } else {
                    tReturnNum.setVisibility(View.GONE);
                    tCommentNum.setVisibility(View.GONE);
                    tReceiveNum.setVisibility(View.GONE);
                    tDeliverNum.setVisibility(View.GONE);
                    tUnpayNum.setVisibility(View.GONE);
                    tVouncherNum.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initNum() {
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_my_order_num, "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            Log.i("zjz", "order_num_msg=" + msg);
                            if (object.optInt("code") == 200) {
                                JSONObject datas = object.getJSONObject("datas");
                                cancel = datas.optString("yiquxiao");
                                unpay = datas.optString("daifukuan");
                                uncomment = datas.optString("daipingjia");
                                commented = datas.optString("yipingjia");
                                receriver = datas.optString("daishouhuo");
                                deliver = datas.optString("daifahuo");
                                all = datas.optString("quanbu");
                                tuihuo = datas.optString("tuihuokuan");
                                initOrderView();
                            }
                        } catch (JSONException e) {
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
                            Log.i("zjz", "voucher_num_msg=" + msg);
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

    private void initOrderView() {
        if (tUnpayNum != null)
            if (unpay.equals("0")) {
                tUnpayNum.setVisibility(View.GONE);
            } else {
                tUnpayNum.setVisibility(View.VISIBLE);
                tUnpayNum.setText(unpay);
            }
        if (tDeliverNum != null)
            if (deliver.equals("0")) {
                tDeliverNum.setVisibility(View.GONE);
            } else {
                tDeliverNum.setVisibility(View.VISIBLE);
                tDeliverNum.setText(deliver);
            }
        if (tReceiveNum != null)
            if (receriver.equals("0")) {
                tReceiveNum.setVisibility(View.GONE);
            } else {
                tReceiveNum.setVisibility(View.VISIBLE);
                tReceiveNum.setText(receriver);
            }
        if (tCommentNum != null)
            if (uncomment.equals("0")) {
                tCommentNum.setVisibility(View.GONE);
            } else {
                tCommentNum.setVisibility(View.VISIBLE);
                tCommentNum.setText(uncomment);
            }
        if (tReturnNum != null)
            if (tuihuo.equals("0")) {
                tReturnNum.setVisibility(View.GONE);
            } else {
                tReturnNum.setVisibility(View.GONE);
                tReturnNum.setText(tuihuo);
            }
    }

    private void setOnListener() {
        rlAllOrder.setOnClickListener(this);
        rlMyCard.setOnClickListener(this);
        rlMyCart.setOnClickListener(this);
        rlMyCollection.setOnClickListener(this);
        rlMyshaidan.setOnClickListener(this);
        rlOrderDelivery.setOnClickListener(this);
        rlOrderEvaluation.setOnClickListener(this);
        rlOrderGoods.setOnClickListener(this);
        rlOrderNoPay.setOnClickListener(this);
        rlOrderTuihuo.setOnClickListener(this);
        rlMyKefu.setOnClickListener(this);
        rlMyYYG.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myBroadCastReceiver.unRegister();
        LogUtil.i("haotest","onDestroyView()");
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.rl_order_noPay:
                if (MyApplication.getInstance().self == null) {
                    Toast.makeText(getContext(), "请登录", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("position", 1);
                startActivity(intent);
                break;
            //待收货
            case R.id.rl_order_goods:
                if (MyApplication.getInstance().self == null) {
                    Toast.makeText(getContext(), "请登录", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("position", 3);
                startActivity(intent);
                break;
            //待发货
            case R.id.rl_order_delivery:
                if (MyApplication.getInstance().self == null) {
                    Toast.makeText(getContext(), "请登录", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("position", 2);
                startActivity(intent);
                break;
            //待评价
            case R.id.rl_order_evaluation:
                if (MyApplication.getInstance().self == null) {
                    Toast.makeText(getContext(), "请登录", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("position", 5);
                startActivity(intent);
                break;
            //退货
            case R.id.rl_order_tuihuo:
                if (MyApplication.getInstance().self == null) {
                    Toast.makeText(getContext(), "请登录", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(getActivity(), ReturnAndRefundActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_allOrder://点击我的订单-全部订单，调到我的订单页面
                if (MyApplication.getInstance().self == null) {
                    Toast.makeText(getContext(), "请登录", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("position", 0);
                startActivity(intent);
                break;
            case R.id.rl_myCollection:
                if (MyApplication.getInstance().self == null) {
                    Toast.makeText(getContext(), "请登录", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(getActivity(), CollectionActivity.class);
                startActivity(intent);

                break;
            case R.id.rl_myshaidan:
                if (MyApplication.getInstance().self == null) {
                    Toast.makeText(getContext(), "请登录", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(getActivity(), CommentAndShareActivity.class);
                startActivity(intent);

                break;
            case R.id.rl_myCart:
                if (MyApplication.getInstance().self == null) {
                    Toast.makeText(getContext(), "请登录", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(getActivity(), CartActivity2.class);
                intent.putExtra("store_id","");
                startActivity(intent);

                break;
            case R.id.rl_myCard:
                if (MyApplication.getInstance().self == null) {
                    Toast.makeText(getContext(), "请登录", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(getActivity(), MyCardActivity.class);
                startActivity(intent);

                break;
            case R.id.rl_myKefu:
                startActivity(new Intent(getActivity(), KefuActivity.class));
                break;
            case R.id.rl_myYYG:
                if (MyApplication.getInstance().self == null) {
                    Toast.makeText(getContext(), "请登录", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getActivity(), WXEntryActivity.class);
                    startActivity(intent);
                    return;
                }
                intent=new Intent(getActivity(), MyYYGActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void loginSuccess() {
        Log.i("zjz", "user_shopping_change");
        if (MyApplication.getInstance().getMykey() != null) {
            initNum();
        } else {
            tReturnNum.setVisibility(View.GONE);
            tCommentNum.setVisibility(View.GONE);
            tReceiveNum.setVisibility(View.GONE);
            tDeliverNum.setVisibility(View.GONE);
            tUnpayNum.setVisibility(View.GONE);
            tVouncherNum.setVisibility(View.GONE);
        }
    }
}
