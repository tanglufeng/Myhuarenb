package com.abcs.haiwaigou.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.relative_cancel)
    RelativeLayout relativeCancel;
    @InjectView(R.id.relative_title)
    RelativeLayout relativeTitle;
    @InjectView(R.id.t_order_state)
    TextView tOrderState;
    @InjectView(R.id.t_truename)
    TextView tTruename;
    @InjectView(R.id.t_phone)
    TextView tPhone;
    @InjectView(R.id.t_address)
    TextView tAddress;
    @InjectView(R.id.t_freaght)
    TextView tFreaght;
    @InjectView(R.id.t_vouncher)
    TextView tVouncher;
    @InjectView(R.id.relative_vouncher)
    RelativeLayout relativeVouncher;
    @InjectView(R.id.t_chongzhi)
    TextView tChongzhi;
    @InjectView(R.id.relative_chongzhi)
    RelativeLayout relativeChongzhi;
    @InjectView(R.id.t_total_money)
    TextView tTotalMoney;
    @InjectView(R.id.img_xiaoxi)
    ImageView imgXiaoxi;
    @InjectView(R.id.relative_connect_kefu)
    RelativeLayout relativeConnectKefu;
    @InjectView(R.id.t_order_code)
    TextView tOrderCode;
    @InjectView(R.id.t_add_time)
    TextView tAddTime;
    @InjectView(R.id.img_gou1)
    ImageView imgGou1;
    @InjectView(R.id.img_gou2)
    ImageView imgGou2;
    @InjectView(R.id.img_gou3)
    ImageView imgGou3;
    @InjectView(R.id.btn_pay)
    Button btnPay;
    @InjectView(R.id.tv_total_money)
    TextView tvTotalMoney;
    @InjectView(R.id.t_text)
    TextView tText;
    @InjectView(R.id.t_refunding)
    TextView tRefunding;
    @InjectView(R.id.t_commented)
    TextView tCommented;
    @InjectView(R.id.btn_comment)
    Button btnComment;
    @InjectView(R.id.btn_receive)
    Button btnReceive;
    @InjectView(R.id.linear_goods)
    LinearLayout linearGoods;
    @InjectView(R.id.t_invoice)
    TextView tInvoice;
    @InjectView(R.id.linear_invoice)
    LinearLayout linearInvoice;
    @InjectView(R.id.t_message)
    TextView tMessage;
    @InjectView(R.id.linear_message)
    LinearLayout linearMessage;
    @InjectView(R.id.t_deliver)
    TextView tDeliver;
    @InjectView(R.id.linear_deliver)
    LinearLayout linearDeliver;
    @InjectView(R.id.t_deliver_code)
    TextView tDeliverCode;
    @InjectView(R.id.linear_deliver_code)
    LinearLayout linearDeliverCode;
    @InjectView(R.id.btn_cancel)
    Button btnCancel;
    @InjectView(R.id.relative_bottom)
    RelativeLayout relativeBottom;
    @InjectView(R.id.btn_refund)
    Button btnRefund;
    @InjectView(R.id.t_refund_state)
    TextView tRefundState;
    @InjectView(R.id.t_yucun)
    TextView tYucun;
    @InjectView(R.id.relative_yucun)
    RelativeLayout relativeYucun;
    @InjectView(R.id.t_yyg)
    TextView tYyg;
    @InjectView(R.id.relative_yyg)
    RelativeLayout relativeYyg;
    private Handler handler = new Handler();
    String order_id;

    boolean isCancel;
    boolean isRefundCancel;
    boolean isDeliver;
    boolean isReceive;
    boolean isComplain;
    boolean isDelete;
    boolean isComment;
    boolean isLock;
    boolean isEvaluation;

    String evaluation_state;
    String order_state;
    String refund_state;
    String lock_state;

    String pay_sn;
    String order_amount;
    String return_amount;
    String refundtext;
    String order_from;
    private ArrayList<Goods> goodsList = new ArrayList<Goods>();
    private RequestQueue mRequestQueue;
    MyBroadCastReceiver myBroadCastReceiver;
    boolean isUseVouncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.inject(this);

//        new LoadDexTask().execute();
        myBroadCastReceiver = new MyBroadCastReceiver(this, updateUI);
        myBroadCastReceiver.register();
        mRequestQueue = Volley.newRequestQueue(this);
        order_id = getIntent().getStringExtra("order_id");
        Log.i("zjz", "order_id=" + order_id);
        initDatas();
        setOnListener();
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
            if (intent.getStringExtra("type").equals(MyUpdateUI.ORDER)) {
                initDatas();
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initDatas() {
        ProgressDlgUtil.showProgressDlg("Loading...", this);

        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_head, "act=member_order&op=show_order" + "&order_id=" + order_id + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            Log.i("zjz", "order_detail_msg=" + jsonObject);
                            if (jsonObject.getInt("code") == 200) {
                                JSONObject datas = jsonObject.getJSONObject("datas");
                                JSONObject orderInfoObject = datas.getJSONObject("order_info");
                                isCancel = orderInfoObject.optBoolean("if_cancel");
                                isRefundCancel = orderInfoObject.optBoolean("if_refund_cancel");
                                isDeliver = orderInfoObject.optBoolean("if_deliver");
                                isReceive = orderInfoObject.optBoolean("if_receive");
                                isComplain = orderInfoObject.optBoolean("if_complain");
                                isDelete = orderInfoObject.optBoolean("if_delete");
                                isLock = orderInfoObject.optBoolean("if_lock");
                                isEvaluation = orderInfoObject.optBoolean("if_evaluation");
                                order_from=orderInfoObject.optString("order_from");
                                return_amount=orderInfoObject.optString("order_amount");
                                if (orderInfoObject.has("close_info") && tRefundState != null) {
                                    JSONObject closeInfo = orderInfoObject.optJSONObject("close_info");
                                    refundtext = "用户于" +
                                            Util.format.format(closeInfo.optLong("log_time") * 1000) +
                                            closeInfo.optString("log_msg");
                                    tRefundState.setVisibility(View.VISIBLE);
                                    tRefundState.setText(refundtext);
                                }

                                evaluation_state = orderInfoObject.optString("evaluation_state");
                                order_state = orderInfoObject.optString("order_state");
                                refund_state = orderInfoObject.optString("refund_state");
                                lock_state = orderInfoObject.optString("lock_state");

                                if (tOrderCode != null)
                                    tOrderCode.setText(orderInfoObject.optString("order_sn"));
                                if (tAddTime != null)
                                    tAddTime.setText(Util.formatzjz.format(orderInfoObject.optLong("add_time") * 1000) + "");
                                if (tvTotalMoney != null)
                                    tvTotalMoney.setText(NumberUtils.formatPrice(orderInfoObject.optDouble("goods_amount") + orderInfoObject.optDouble("shipping_fee") - orderInfoObject.optDouble("rcb_amount") - orderInfoObject.optDouble("pd_amount") - orderInfoObject.optDouble("yyg_amount")));
                                if (tTotalMoney != null)
                                    tTotalMoney.setText(NumberUtils.formatPrice(orderInfoObject.optDouble("goods_amount") + orderInfoObject.optDouble("shipping_fee") - orderInfoObject.optDouble("rcb_amount") - orderInfoObject.optDouble("pd_amount") - orderInfoObject.optDouble("yyg_amount")));
                                order_amount = String.valueOf((orderInfoObject.optDouble("goods_amount") + orderInfoObject.optDouble("shipping_fee"))- orderInfoObject.optDouble("rcb_amount") - orderInfoObject.optDouble("pd_amount") - orderInfoObject.optDouble("yyg_amount"));
                                pay_sn = orderInfoObject.optString("pay_sn");
                                if (tFreaght != null)
                                    tFreaght.setText(NumberUtils.formatPrice(orderInfoObject.optDouble("shipping_fee")));
                                if (!orderInfoObject.optString("shipping_code").equals("")) {
                                    if (linearDeliver != null)
                                        linearDeliver.setVisibility(View.VISIBLE);
                                    if (linearDeliverCode != null)
                                        linearDeliverCode.setVisibility(View.VISIBLE);
                                    if (tDeliverCode != null)
                                        tDeliverCode.setText("物流单号：" + orderInfoObject.optString("shipping_code")+"(点击查看)");
                                    JSONObject expressObject = orderInfoObject.optJSONObject("express_info");
                                    if (expressObject != null) {
                                        if (tDeliver != null)
                                            tDeliver.setText("物流公司：" + expressObject.optString("e_name"));
                                    }
                                } else {
                                    if (linearDeliver != null)
                                        linearDeliver.setVisibility(View.GONE);
                                    if (linearDeliverCode != null)
                                        linearDeliverCode.setVisibility(View.GONE);
                                }
                                if (relativeChongzhi != null && tChongzhi != null) {
                                    if (!orderInfoObject.optString("rcb_amount").equals("0.00")) {
                                        relativeChongzhi.setVisibility(View.VISIBLE);
                                        tChongzhi.setText("-"+NumberUtils.formatPrice(orderInfoObject.optDouble("rcb_amount")));
                                    } else {
                                        relativeChongzhi.setVisibility(View.GONE);
                                    }
                                }
                                if(relativeYucun!=null&&tYucun!=null){
                                    if (!orderInfoObject.optString("pd_amount").equals("0.00")) {
                                        relativeYucun.setVisibility(View.VISIBLE);
                                        tYucun.setText("-"+NumberUtils.formatPrice(orderInfoObject.optDouble("pd_amount")));
                                    } else {
                                        relativeYucun.setVisibility(View.GONE);
                                    }
                                }
                                if(relativeYyg!=null&&tYyg!=null){
                                    if (!orderInfoObject.optString("yyg_amount").equals("0.00")) {
                                        relativeYyg.setVisibility(View.VISIBLE);
                                        tYyg.setText("-"+NumberUtils.formatPrice(orderInfoObject.optDouble("yyg_amount")));
                                    } else {
                                        relativeYyg.setVisibility(View.GONE);
                                    }
                                }
                                JSONObject orderCommentObject = orderInfoObject.getJSONObject("extend_order_common");
                                if (relativeVouncher != null && tVouncher != null) {
                                    if (!orderCommentObject.optString("voucher_price").equals("null")) {
                                        isUseVouncher=true;
                                        relativeVouncher.setVisibility(View.VISIBLE);
                                        tVouncher.setText("- " + NumberUtils.formatPrice(orderCommentObject.optDouble("voucher_price")));
                                    } else {
                                        isUseVouncher=false;
                                        relativeVouncher.setVisibility(View.GONE);
                                    }
                                }
                                if (tTruename != null)
                                    tTruename.setText("收件人：" + orderCommentObject.optString("reciver_name"));

                                if (linearMessage != null && tMessage != null) {
                                    if (!orderCommentObject.optString("order_message").equals("null")) {
                                        linearMessage.setVisibility(View.VISIBLE);
                                        tMessage.setText("买家留言：" + orderCommentObject.optString("order_message"));
                                    } else {
                                        linearMessage.setVisibility(View.GONE);
                                    }
                                }

                                JSONObject invoiceObject = orderCommentObject.optJSONObject("invoice_info");
                                if (linearInvoice != null && tInvoice != null) {
                                    if (invoiceObject != null) {
                                        linearInvoice.setVisibility(View.VISIBLE);
                                        tInvoice.setText(invoiceObject.optString("类型") + " " + invoiceObject.optString("抬头") + " " + invoiceObject.optString("内容"));
                                    } else {
                                        linearInvoice.setVisibility(View.GONE);
                                    }
                                }

                                JSONObject receiveObject = orderCommentObject.getJSONObject("reciver_info");
                                if (tPhone != null) {
                                    String phone = receiveObject.optString("phone");
                                    String mob_phone = receiveObject.optString("mob_phone");
                                    String tel_phone = receiveObject.optString("tel_phone");

                                    if(phone.length()>0){
                                        if (phone.length() > 7) {
                                            tPhone.setText(phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length()));
                                        } else {
                                            tPhone.setText(phone);
                                        }
                                    }else  if(mob_phone.length()>0){
                                        if (mob_phone.length() > 7) {
                                            tPhone.setText(mob_phone.substring(0, 3) + "****" + mob_phone.substring(mob_phone.length() - 4, mob_phone.length()));
                                        } else {
                                            tPhone.setText(mob_phone);
                                        }
                                    }else  if(tel_phone.length()>0){
                                        if (tel_phone.length() > 7) {
                                            tPhone.setText(tel_phone.substring(0, 3) + "****" + tel_phone.substring(tel_phone.length() - 4, tel_phone.length()));
                                        } else {
                                            tPhone.setText(tel_phone);
                                        }
                                    }
                                }

                                if (tAddress != null) {
                                    String address = receiveObject.optString("address");
//                                    tAddress.setText("收货地址：" + address.replaceAll("\t", " "));
                                    tAddress.setText("收货地址：" + Util.ReplaceSpecialSymbols(address));
                                }

                                goodsList.clear();
                                CommentActivity.commentList.clear();
                                JSONArray goodsArray = orderInfoObject.getJSONArray("goods_list");
                                for (int i = 0; i < goodsArray.length(); i++) {
                                    JSONObject goodsObjece = goodsArray.getJSONObject(i);
                                    Goods goods = new Goods();
                                    goods.setGoods_id(goodsObjece.optString("goods_id"));
                                    goods.setTitle(goodsObjece.optString("goods_name"));
                                    goods.setMoney(goodsObjece.optDouble("goods_price"));
                                    goods.setPicarr(goodsObjece.optString("image_240_url"));
                                    goods.setGoods_num(goodsObjece.optInt("goods_num"));
                                    goods.setGoods_url(goodsObjece.optString("goods_url"));
                                    CommentActivity.commentList.add(goods);
                                    goodsList.add(goods);
                                }
                                initView();
                                initGoodsDatas();

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

    private void initView() {
        if (btnCancel != null && tOrderState != null & btnReceive != null && btnComment != null && btnPay != null && tvTotalMoney != null && tText != null && relativeCancel != null && tCommented != null && tRefunding != null && relativeBottom != null && btnRefund != null)
//            if (!isCancel && !isRefundCancel && !isComplain && !isReceive && !isLock && !isDeliver && !isEvaluation) {
            if (evaluation_state.equals("0") && order_state.equals("0") && refund_state.equals("0") && lock_state.equals("0")) {
                //已取消
                btnCancel.setVisibility(View.VISIBLE);
                tOrderState.setText("买家已取消订单");
                btnReceive.setVisibility(View.GONE);
                btnComment.setVisibility(View.GONE);
                btnPay.setVisibility(View.GONE);
                tvTotalMoney.setVisibility(View.GONE);
                tText.setVisibility(View.GONE);
                relativeCancel.setVisibility(View.GONE);
                tCommented.setVisibility(View.GONE);
                tRefunding.setVisibility(View.GONE);
                btnRefund.setVisibility(View.GONE);
//            } else if (isCancel && !isRefundCancel && !isComplain && !isReceive && !isLock && !isDeliver && !isEvaluation) {
            } else if (evaluation_state.equals("0") && order_state.equals("10") && refund_state.equals("0") && lock_state.equals("0")) {
                //待付款
                btnCancel.setVisibility(View.GONE);
                tOrderState.setText("等待买家付款");
                btnReceive.setVisibility(View.GONE);
                btnComment.setVisibility(View.GONE);
                btnPay.setVisibility(View.VISIBLE);
                tvTotalMoney.setVisibility(View.VISIBLE);
                tText.setVisibility(View.VISIBLE);
                relativeCancel.setVisibility(View.VISIBLE);
                tCommented.setVisibility(View.GONE);
                tRefunding.setVisibility(View.GONE);
                btnRefund.setVisibility(View.GONE);
//            } else if (!isCancel && !isRefundCancel && isComplain && !isReceive && !isLock && isDeliver && isEvaluation) {
            } else if (evaluation_state.equals("0") && order_state.equals("40") && refund_state.equals("0") && lock_state.equals("0")) {
                //交易完成，待评价
                btnCancel.setVisibility(View.GONE);
                tOrderState.setText("买家已确认收货");
                btnReceive.setVisibility(View.GONE);
                btnComment.setVisibility(View.VISIBLE);
                btnPay.setVisibility(View.GONE);
                tvTotalMoney.setVisibility(View.GONE);
                tText.setVisibility(View.GONE);
                relativeCancel.setVisibility(View.GONE);
                tCommented.setVisibility(View.GONE);
                tRefunding.setVisibility(View.GONE);
                btnRefund.setVisibility(View.GONE);
//            } else if (!isCancel && !isRefundCancel && isComplain && !isReceive && !isLock && isDeliver && !isEvaluation) {
            } else if (evaluation_state.equals("1") && order_state.equals("40") && refund_state.equals("0") && lock_state.equals("0")) {
                //交易完成，已评价
                btnCancel.setVisibility(View.GONE);
                tOrderState.setText("买家已确认收货");
                btnReceive.setVisibility(View.GONE);
                btnComment.setVisibility(View.GONE);
                btnPay.setVisibility(View.GONE);
                tvTotalMoney.setVisibility(View.GONE);
                tText.setVisibility(View.GONE);
                relativeCancel.setVisibility(View.GONE);
                tCommented.setVisibility(View.VISIBLE);
                tRefunding.setVisibility(View.GONE);
                btnRefund.setVisibility(View.GONE);
//            } else if (!isCancel && !isRefundCancel && isComplain && !isReceive && isLock && isDeliver && !isEvaluation) {
            } else if (evaluation_state.equals("0") && order_state.equals("30") && refund_state.equals("0") && lock_state.equals("1")) {
                //待收货，已退款,不可收货
                btnCancel.setVisibility(View.GONE);
                tOrderState.setText("买家已申请退货退款");
                btnReceive.setVisibility(View.GONE);
                btnComment.setVisibility(View.GONE);
                btnPay.setVisibility(View.GONE);
                tvTotalMoney.setVisibility(View.GONE);
                tText.setVisibility(View.GONE);
                relativeCancel.setVisibility(View.GONE);
                tCommented.setVisibility(View.GONE);
                tRefunding.setVisibility(View.VISIBLE);
                btnRefund.setVisibility(View.GONE);
//            } else if (!isCancel && !isRefundCancel && isComplain && isReceive && !isLock && isDeliver && !isEvaluation) {
            } else if (evaluation_state.equals("0") && order_state.equals("30") && refund_state.equals("0") && lock_state.equals("0")) {
                //待收货，可收货
                btnCancel.setVisibility(View.GONE);
                tOrderState.setText("卖家已发货");
                btnReceive.setVisibility(View.VISIBLE);
                btnComment.setVisibility(View.GONE);
                btnPay.setVisibility(View.GONE);
                tvTotalMoney.setVisibility(View.GONE);
                tText.setVisibility(View.GONE);
                relativeCancel.setVisibility(View.GONE);
                tCommented.setVisibility(View.GONE);
                tRefunding.setVisibility(View.GONE);
                btnRefund.setVisibility(View.GONE);
//            } else if (!isCancel && isRefundCancel && isComplain && !isReceive && !isLock && !isDeliver && !isEvaluation) {
            } else if (evaluation_state.equals("0") && order_state.equals("20") && refund_state.equals("0") && lock_state.equals("0")) {
                //待发货，可退款
                btnCancel.setVisibility(View.GONE);
                tOrderState.setText("等待卖家发货");
                btnReceive.setVisibility(View.GONE);
                btnComment.setVisibility(View.GONE);
                btnPay.setVisibility(View.GONE);
                tvTotalMoney.setVisibility(View.GONE);
                tText.setVisibility(View.GONE);
                relativeCancel.setVisibility(View.GONE);
                tCommented.setVisibility(View.GONE);
                tRefunding.setVisibility(View.GONE);
                if(order_from.equals("3")){
                    btnRefund.setVisibility(View.GONE);
                }else{
                    btnRefund.setVisibility(View.VISIBLE);
                }
//            } else if (!isCancel && !isRefundCancel && isComplain && !isReceive && isLock && !isDeliver && !isEvaluation) {
            } else if (evaluation_state.equals("0") && order_state.equals("20") && refund_state.equals("0") && lock_state.equals("1")) {
                //待发货，已退款
                btnCancel.setVisibility(View.GONE);
                tOrderState.setText("买家已申请退货退款");
                btnReceive.setVisibility(View.GONE);
                btnComment.setVisibility(View.GONE);
                btnPay.setVisibility(View.GONE);
                tvTotalMoney.setVisibility(View.GONE);
                tText.setVisibility(View.GONE);
                relativeCancel.setVisibility(View.GONE);
                tCommented.setVisibility(View.GONE);
                tRefunding.setVisibility(View.VISIBLE);
                btnRefund.setVisibility(View.GONE);
            } else if (evaluation_state.equals("0") && order_state.equals("0") && refund_state.equals("2") && lock_state.equals("0")) {
                //全部退款
                btnCancel.setVisibility(View.VISIBLE);
                tOrderState.setText("买家已取消订单");
                btnReceive.setVisibility(View.GONE);
                btnComment.setVisibility(View.GONE);
                btnPay.setVisibility(View.GONE);
                tvTotalMoney.setVisibility(View.GONE);
                tText.setVisibility(View.GONE);
                relativeCancel.setVisibility(View.GONE);
                tCommented.setVisibility(View.GONE);
                tRefunding.setVisibility(View.GONE);
                btnRefund.setVisibility(View.GONE);
//            } else if (isCancel && !isRefundCancel && !isComplain && !isReceive && !isLock && !isDeliver && !isEvaluation) {
            }
    }

    private void initGoodsDatas() {
        linearGoods.removeAllViews();
        for (int i = 0; i < goodsList.size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.hwg_item_order_detail_goods, null);
            ImageView img_goods_icon = (ImageView) view.findViewById(R.id.img_goods_icon);
            TextView t_goods_name = (TextView) view.findViewById(R.id.t_goods_name);
            TextView t_goods_num = (TextView) view.findViewById(R.id.t_goods_num);
            TextView t_goods_price = (TextView) view.findViewById(R.id.t_goods_price);
            LoadPicture loadPicture = new LoadPicture();
            loadPicture.initPicture(img_goods_icon, goodsList.get(i).getPicarr());
            t_goods_name.setText(goodsList.get(i).getTitle());
            t_goods_price.setText(NumberUtils.formatPrice(goodsList.get(i).getMoney()));
            t_goods_num.setText("X" + goodsList.get(i).getGoods_num());
            final int finalI = i;
            img_goods_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OrderDetailActivity.this, GoodsDetailActivity.class);
                    intent.putExtra("sid", goodsList.get(finalI).getGoods_id());
                    intent.putExtra("pic", goodsList.get(finalI).getPicarr());
                    startActivity(intent);
                }
            });
            linearGoods.addView(view);
        }
    }

    private void setOnListener() {
        relativeBack.setOnClickListener(this);
        relativeCancel.setOnClickListener(this);
        relativeConnectKefu.setOnClickListener(this);
        btnPay.setOnClickListener(this);
        btnComment.setOnClickListener(this);
        btnReceive.setOnClickListener(this);
        btnRefund.setOnClickListener(this);
        tDeliverCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.relative_cancel:
                cancelOrder();
                break;
            case R.id.btn_pay:
                payOrder();
                break;
            case R.id.btn_comment:
                commentOrder();
                break;
            case R.id.relative_connect_kefu:
                startActivity(new Intent(this, KefuActivity.class));
                break;
            case R.id.btn_receive:
                receiveOrder();
                break;
            case R.id.btn_refund:
                refund();
                break;
            case R.id.t_deliver_code:
                Intent intent = new Intent(OrderDetailActivity.this, MyDeliverActivity.class);
                intent.putExtra("order_id",order_id);
                startActivity(intent);
                break;
        }
    }


    private void refund() {
        Intent intent = new Intent(this, ApplyRefundActivity.class);
        intent.putExtra("isOrder", true);
        intent.putExtra("title", "订单退款");
        intent.putExtra("order_id", order_id);
        intent.putExtra("order_money", return_amount);
        startActivity(intent);
    }

    private void receiveOrder() {
        new PromptDialog(this, "是否确认收货？", new Complete() {
            @Override
            public void complete() {
                ProgressDlgUtil.showProgressDlg("Loading...", OrderDetailActivity.this);
                HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_order_receive, "&order_id=" + order_id + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
                    @Override
                    public void revMsg(final String msg) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject object = new JSONObject(msg);
                                    Log.i("zjz", "msg=" + msg);
                                    if (object.getInt("code") == 200) {
                                        if (object.optString("datas").equals("1")) {
                                            Toast.makeText(OrderDetailActivity.this, "成功收货！", Toast.LENGTH_SHORT).show();
                                            MyUpdateUI.sendUpdateCollection(OrderDetailActivity.this, MyUpdateUI.ORDER);
                                            finish();
                                        } else {
                                            Toast.makeText(OrderDetailActivity.this, "失败！", Toast.LENGTH_SHORT).show();
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
        }).show();
    }

    private void commentOrder() {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("order_id", order_id);
        startActivity(intent);
//        Intent intent = new Intent(this, CommentActivity.class);
//        intent.putExtra("order_id", item.getOrder_list().get(finalJ).getOrder_id());
//        intent.putExtra("goods_id", item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getGoods_id());
//        intent.putExtra("goods_img", item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getGoods_image_url());
//        intent.putExtra("goods_name", item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getGoods_name());
//        intent.putExtra("goods_price", item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getGoods_price());
//        intent.putExtra("goods_num", item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getGoods_num());
//        startActivity(intent);
    }

    private void payOrder() {
        Intent intent = new Intent(this, PayWayActivity.class);
        intent.putExtra("pay_sn", pay_sn);
        intent.putExtra("yungou", false);
        if(isUseVouncher){
            intent.putExtra("isFromOrder",false);
        }else {
            intent.putExtra("isFromOrder",true);
        }
        intent.putExtra("total_money", Double.valueOf(order_amount));
        startActivity(intent);
        finish();
    }

    private void cancelOrder() {
        new PromptDialog(this, "取消后无法恢复，确定取消该订单？", new Complete() {
            @Override
            public void complete() {
                ProgressDlgUtil.showProgressDlg("Loading...", OrderDetailActivity.this);
                HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_order_cancle, "&order_id=" + order_id + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
                    @Override
                    public void revMsg(final String msg) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject object = new JSONObject(msg);
                                    Log.i("zjz", "msg=" + msg);
                                    if (object.getInt("code") == 200) {
                                        if (object.optString("datas").equals("1")) {
                                            Toast.makeText(OrderDetailActivity.this, "成功取消！", Toast.LENGTH_SHORT).show();
                                            MyUpdateUI.sendUpdateCollection(OrderDetailActivity.this, MyUpdateUI.ORDER);
                                            finish();
                                        } else {
                                            Toast.makeText(OrderDetailActivity.this, "失败！", Toast.LENGTH_SHORT).show();
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
        }).show();
    }

    class LoadDexTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                MultiDex.install(getApplication());
                Log.d("loadDex", "install finish");
                ((MyApplication) getApplication()).installFinish(getApplication());
            } catch (Exception e) {
                Log.e("loadDex", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.d("loadDex", "get install finish");
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myBroadCastReceiver.unRegister();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //cannot backpress
    }
}
