package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.activity.ApplyRefundActivity;
import com.abcs.haiwaigou.activity.CartActivity2;
import com.abcs.haiwaigou.activity.CommentActivity;
import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.activity.MyDeliverActivity;
import com.abcs.haiwaigou.activity.OrderDetailActivity;
import com.abcs.haiwaigou.activity.PayWayActivity;
import com.abcs.haiwaigou.adapter.viewholder.OrderRecyclerViewHolder;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.model.Orders;
import com.abcs.haiwaigou.model.PayWay;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.FuyouPayActivity;
import com.abcs.huaqiaobang.activity.NoticeDialogActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 */
public class HuoHangOrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerViewHolder> {
    public Handler handler = new Handler();

    Context context;
    private SortedList<Orders> mSortedList;

    Activity activity;
    private ArrayList<PayWay> payList;

    String zfb = "alipay";
    String wx = "wxpay";

    public ArrayList<PayWay> getPayList() {
        return payList;
    }

    private ArrayList<Orders> orderses;

    public ArrayList<Orders> getOrderses() {
        return orderses;
    }

    public HuoHangOrderRecyclerAdapter(Activity activity) {
        this.activity = activity;
        this.orderses = new ArrayList<>();
        mSortedList = new SortedList<>(Orders.class, new SortedList.Callback<Orders>() {

            /**
             * 返回一个负整数（第一个参数小于第二个）、零（相等）或正整数（第一个参数大于第二个）
             */
            @Override
            public int compare(Orders o1, Orders o2) {

                if (o1.getAdd_time()>o2.getAdd_time()) {
                    return -1;
                } else if (o1.getAdd_time()< o2.getAdd_time()) {
                    return 1;
                }

                return 0;
            }

            @Override
            public boolean areContentsTheSame(Orders oldItem, Orders newItem) {
                return oldItem.getAdd_time().equals(newItem.getAdd_time());
            }

            @Override
            public boolean areItemsTheSame(Orders item1, Orders item2) {
                return item1.getId() == item2.getId();
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }
        });
    }


    public SortedList<Orders> getList() {
        return mSortedList;
    }

    public void addItems(ArrayList<Orders> list) {
        mSortedList.beginBatchedUpdates();

        for (Orders itemModel : list) {
            mSortedList.add(itemModel);
        }

        mSortedList.endBatchedUpdates();
    }

    public void deleteItems(ArrayList<Orders> items) {
        mSortedList.beginBatchedUpdates();
        for (Orders item : items) {
            mSortedList.remove(item);
        }
        mSortedList.endBatchedUpdates();
    }

    @Override
    public OrderRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_order_group, parent, false);
        OrderRecyclerViewHolder hwgFragmentViewHolder = new OrderRecyclerViewHolder(view);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }


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


    @Override
    public void onBindViewHolder(OrderRecyclerViewHolder holder, final int position) {
        final ArrayList<String >goodsIdList=new ArrayList<String >();
        final ArrayList<String >goodsNumList=new ArrayList<String >();
        //图片加载
        final Orders item = mSortedList.get(position);

        holder.t_order_time.setText(Util.formatzjz.format(item.getAdd_time() * 1000) + "");

        holder.linear_store.removeAllViews();
        holder.linear_store.invalidate();

//        Log.i("zjz", "paycount=" + item.getPay_amount());
        if (item.getPay_amount().equals("")) {
            holder.linear_pay.setVisibility(View.GONE);
        } else {
            holder.linear_pay.setVisibility(View.VISIBLE);
        }
        double pay_amount=0;
        if(!item.getPay_amount().equals("")){
            pay_amount= Double.valueOf(item.getPay_amount());
        }
        holder.t_total_wx.setText(NumberUtils.formatPrice(pay_amount));
        holder.t_total_zfb.setText(NumberUtils.formatPrice(pay_amount));
        holder.linear_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String url = "http://www.huaqiaobang.com/mobile/index.php?act=member_payment&op=pay" +
//                        "&key=" + MyApplication.getInstance().getMykey() +
//                        "&pay_sn=" + item.getPay_sn() +
//                        "&payment_code=" + wx;
//                Intent intent = new Intent(activity, FuyouPayActivity.class);
//                intent.putExtra("id", "1");
//                intent.putExtra("url", url);
//                activity.startActivity(intent);
                Intent intent=new Intent(activity, PayWayActivity.class);
                intent.putExtra("pay_sn",item.getPay_sn());
                intent.putExtra("yungou",false);
                if(item.isUseVouvher()){
                    intent.putExtra("isFromOrder", false);
                }else if(item.getCard_state().equals("1")){
                    intent.putExtra("isFromOrder", false);
                }else {
                    intent.putExtra("isFromOrder", true);
                }
                intent.putExtra("total_money",Double.valueOf(item.getPay_amount()));
                activity.startActivity(intent);
            }
        });
        holder.linear_zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = TLUrl.getInstance().URL_hwg_base+"/mobile/index.php?act=member_payment&op=pay" +
                        "&key=" + MyApplication.getInstance().getMykey() +
                        "&pay_sn=" + item.getPay_sn() +
                        "&payment_code=" + zfb;
                Intent intent = new Intent(activity, FuyouPayActivity.class);
                intent.putExtra("id", "1");
                intent.putExtra("url", url);
                activity.startActivity(intent);
            }
        });
        for (int j = 0; j < item.getOrder_list().size(); j++) {
            View view = activity.getLayoutInflater().inflate(R.layout.hwg_item_order_orderlsit, null);
            TextView t_name = (TextView) view.findViewById(R.id.t_name);
//            TextView t_pay = (TextView) view.findViewById(R.id.t_pay);
            TextView t_total = (TextView) view.findViewById(R.id.t_total);
            final ImageView img_delete= (ImageView) view.findViewById(R.id.img_delete);
            final TextView t_cancel = (TextView) view.findViewById(R.id.t_cancel);
            final TextView t_state = (TextView) view.findViewById(R.id.t_state);
            TextView t_order_sn = (TextView) view.findViewById(R.id.t_order_sn);
            TextView t_add_time = (TextView) view.findViewById(R.id.t_add_time);
            final TextView t_deliver = (TextView) view.findViewById(R.id.t_deliver);
            final TextView t_refund = (TextView) view.findViewById(R.id.t_refund);
            final TextView t_receive = (TextView) view.findViewById(R.id.t_receive);
            final TextView t_comment = (TextView) view.findViewById(R.id.t_comment);
            final TextView t_iscomment = (TextView) view.findViewById(R.id.t_iscomment);
            final TextView t_detail = (TextView) view.findViewById(R.id.t_detail);
            final TextView t_buy_again = (TextView) view.findViewById(R.id.t_buy_again);
            final TextView t_state_of_refund = (TextView) view.findViewById(R.id.t_state_of_refund);

            LinearLayout linear_goods = (LinearLayout) view.findViewById(R.id.linear_goods);
            LinearLayout linear_state = (LinearLayout) view.findViewById(R.id.linear_state);

            linear_goods.removeAllViews();
            linear_goods.invalidate();
//            if (item.getPay_amount().equals("")) {
//                t_pay.setVisibility(View.GONE);
//            } else {
//                linear_state.setVisibility(View.VISIBLE);
//                t_pay.setVisibility(View.VISIBLE);
//            }
            final int finalJ = j;
            String orgin=null;

            isCancel = item.getOrder_list().get(j).isIf_cancel();
            isRefundCancel = item.getOrder_list().get(j).isIf_refund_cancel();
            isDeliver = item.getOrder_list().get(j).isIf_deliver();
            isReceive = item.getOrder_list().get(j).isIf_receive();
            isLock = item.getOrder_list().get(j).isIf_lock();
            isComplain =item.getOrder_list().get(j).isIf_complain();
            isDelete = item.getOrder_list().get(j).isIf_delete();
            isEvaluation = item.getOrder_list().get(j).isIf_evaluation();
            isComment = item.getOrder_list().get(j).getEvaluation_state().equals("1");

            evaluation_state=item.getOrder_list().get(j).getEvaluation_state();
            order_state=item.getOrder_list().get(j).getOrder_state();
            refund_state=item.getOrder_list().get(j).getRefund_state();
            lock_state=item.getOrder_list().get(j).getLock_state();

            goodsIdList.clear();
            goodsNumList.clear();
            for (int k = 0; k < item.getOrder_list().get(j).getExtend_order_goods().size(); k++) {
                goodsIdList.add(item.getOrder_list().get(j).getExtend_order_goods().get(k).getGoods_id());
                goodsNumList.add(item.getOrder_list().get(j).getExtend_order_goods().get(k).getGoods_num());
                View view1 = activity.getLayoutInflater().inflate(R.layout.hwg_item_order, null);
                TextView t_goods_name = (TextView) view1.findViewById(R.id.t_goods_name);
                TextView t_goods_price = (TextView) view1.findViewById(R.id.t_goods_price);
                TextView t_num = (TextView) view1.findViewById(R.id.t_num);
                TextView t_refund_return = (TextView) view1.findViewById(R.id.t_refund_return);
                final ImageView img_goods = (ImageView) view1.findViewById(R.id.img_goods);
                t_goods_name.setText(item.getOrder_list().get(j).getExtend_order_goods().get(k).getGoods_name());
                t_goods_price.setText("¥" + item.getOrder_list().get(j).getExtend_order_goods().get(k).getGoods_price());
                t_num.setText("X" + item.getOrder_list().get(j).getExtend_order_goods().get(k).getGoods_num());
                LoadPicture loadPicture = new LoadPicture();
                loadPicture.initPicture(img_goods, item.getOrder_list().get(j).getExtend_order_goods().get(k).getGoods_image_url());

                orgin=item.getOrder_list().get(j).getExtend_order_goods().get(k).getOrgin();  // 发货地


                if(item.getOrder_from().equals("3")){
                    //云购订单
                    t_refund_return.setVisibility(View.INVISIBLE);
                } else if (item.getOrder_list().get(j).getExtend_order_goods().get(k).getRefund().equals("1")) {
                    if(item.getOrder_list().get(j).getFinnshed_time() != 0){
                        long temp = 7 * 24 * 60 * 60 * 1000;
//                        Log.i("zjz", "finish_time=" + item.getOrder_list().get(j).getFinnshed_time() * 1000);
//                        Log.i("zjz", "current_time=" + (System.currentTimeMillis() - temp));
                        if (item.getOrder_list().get(j).getFinnshed_time() * 1000 > System.currentTimeMillis() - temp) {
                            t_refund_return.setVisibility(View.VISIBLE);
                        } else {
                            t_refund_return.setVisibility(View.INVISIBLE);
                        }
                    }
//                    t_refund_return.setVisibility(View.VISIBLE);
                }  else {
                    t_refund_return.setVisibility(View.INVISIBLE);
                }

                final int finalK = k;
                t_refund_return.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, ApplyRefundActivity.class);
                        intent.putExtra("isOrder", false);
                        intent.putExtra("title", "退货/退款");
                        intent.putExtra("order_id", item.getOrder_list().get(finalJ).getOrder_id());
                        intent.putExtra("goods_id", item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getRec_id());
                        intent.putExtra("goods_img", item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getGoods_image_url());
                        intent.putExtra("goods_name", item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getGoods_name());
                        intent.putExtra("goods_price", item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getGoods_price());
                        intent.putExtra("goods_num", item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getGoods_num());
                        activity.startActivity(intent);
                    }
                });
                t_goods_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, GoodsDetailActivity.class);
                        intent.putExtra("sid",item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getGoods_id());
                        intent.putExtra("pic", item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getGoods_image_url());
                        activity.startActivity(intent);
                    }
                });
                img_goods.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, GoodsDetailActivity.class);
                        intent.putExtra("sid",item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getGoods_id());
                        intent.putExtra("pic", item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getGoods_image_url());
                        activity.startActivity(intent);
                    }
                });

                /////////////////////
                t_deliver.setOnClickListener(new View.OnClickListener() {   //  查看物流
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, MyDeliverActivity.class);
//                        intent.putExtra("order_id", item.getOrder_list().get(finalJ).getOrder_id());
                        intent.putExtra("order_id", item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getOrder_id());
                        intent.putExtra("o_rgin", item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getOrgin());
                        activity.startActivity(intent);

                    }
                });

                ////////////////////
                t_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommentActivity.commentList.clear();
                        for(int i=0;i<item.getOrder_list().get(finalJ).getExtend_order_goods().size();i++){
                            Goods goods=new Goods();
                            goods.setGoods_id(item.getOrder_list().get(finalJ).getExtend_order_goods().get(i).getGoods_id());
                            goods.setPicarr(item.getOrder_list().get(finalJ).getExtend_order_goods().get(i).getGoods_image_url());
                            goods.setTitle(item.getOrder_list().get(finalJ).getExtend_order_goods().get(i).getGoods_name());
                            goods.setMoney(Double.valueOf(item.getOrder_list().get(finalJ).getExtend_order_goods().get(i).getGoods_price()));
                            goods.setGoods_num(Integer.valueOf(item.getOrder_list().get(finalJ).getExtend_order_goods().get(i).getGoods_num()));
                            CommentActivity.commentList.add(goods);
                        }
                        Intent intent = new Intent(activity, CommentActivity.class);
                        intent.putExtra("order_id", item.getOrder_list().get(finalJ).getOrder_id());
//                        intent.putExtra("goods_id", item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getGoods_id());
//                        intent.putExtra("goods_img", item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getGoods_image_url());
//                        intent.putExtra("goods_name", item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getGoods_name());
//                        intent.putExtra("goods_price", item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getGoods_price());
//                        intent.putExtra("goods_num", item.getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getGoods_num());
                        activity.startActivity(intent);
                    }
                });
                linear_goods.addView(view1);
            }
            t_state.setText(item.getOrder_list().get(j).getState_desc());
            //(!isCancel && !isRefundCancel && !isComplain && !isReceive && !isLock && !isDeliver && !isEvaluation && isDelete) 已取消
            //(isCancel && !isRefundCancel && !isComplain && !isReceive && !isLock && !isDeliver && !isEvaluation && !isDelete) 代付款
            //(!isCancel && !isRefundCancel && isComplain && !isReceive && !isLock && isDeliver && isEvaluation && isDelete) 已完成，待评价
            //(!isCancel && !isRefundCancel && isComplain && !isReceive && !isLock && isDeliver && !isEvaluation && isDelete)  已完成，已评价
            //(!isCancel && !isRefundCancel && isComplain && !isReceive && isLock && isDeliver && !isEvaluation && !isDelete)  待收货，已退款，不可收货
            //(!isCancel && !isRefundCancel && isComplain && isReceive && !isLock && isDeliver && !isEvaluation && !isDelete)  待收货，可收货
            //(!isCancel && isRefundCancel && isComplain && !isReceive && !isLock && !isDeliver && !isEvaluation && !isDelete)  代发货，可退款
            //(!isCancel && !isRefundCancel && isComplain && !isReceive && isLock && !isDeliver && !isEvaluation && !isDelete)  代发货，已退款


//            if (!isCancel && !isRefundCancel && !isComplain && !isReceive && !isLock && !isDeliver && !isEvaluation && isDelete) {
            if (evaluation_state.equals("0") && order_state.equals("0")&&refund_state.equals("0")&&lock_state.equals("0")) {
                //已取消
                linear_state.setVisibility(View.VISIBLE);
                t_state.setTextColor(Color.parseColor("#ff454545"));
                t_state.setVisibility(View.VISIBLE);
                t_cancel.setVisibility(View.GONE);
                t_deliver.setVisibility(View.GONE);
                t_receive.setVisibility(View.GONE);
                t_comment.setVisibility(View.GONE);
                t_iscomment.setVisibility(View.GONE);
                t_refund.setVisibility(View.GONE);
                t_state_of_refund.setVisibility(View.GONE);
                img_delete.setVisibility(View.VISIBLE);
//            }else if (isCancel && !isRefundCancel && !isComplain && !isReceive && !isLock && !isDeliver && !isEvaluation && !isDelete) {
            }else if (evaluation_state.equals("0") && order_state.equals("10")&&refund_state.equals("0")&&lock_state.equals("0")) {
                //待付款
                linear_state.setVisibility(View.VISIBLE);
                t_state.setTextColor(Color.parseColor("#0e06b1"));
                t_state.setVisibility(View.VISIBLE);
                t_cancel.setVisibility(View.VISIBLE);
                t_deliver.setVisibility(View.GONE);
                t_receive.setVisibility(View.GONE);
                t_comment.setVisibility(View.GONE);
                t_iscomment.setVisibility(View.GONE);
                t_refund.setVisibility(View.GONE);
                t_state_of_refund.setVisibility(View.GONE);
                img_delete.setVisibility(View.GONE);
//            }else if (!isCancel && !isRefundCancel && isComplain && !isReceive && !isLock && isDeliver && isEvaluation && isDelete) {
            }else if (evaluation_state.equals("0") && order_state.equals("40")&&refund_state.equals("0")&&lock_state.equals("0")) {
                //交易完成，待评价
                t_buy_again.setVisibility(View.VISIBLE);
                linear_state.setVisibility(View.VISIBLE);
                t_state.setTextColor(Color.parseColor("#ff454545"));
                t_state.setVisibility(View.VISIBLE);
                t_cancel.setVisibility(View.GONE);
                t_deliver.setVisibility(View.VISIBLE);
                t_receive.setVisibility(View.GONE);
                t_comment.setVisibility(View.VISIBLE);
                t_iscomment.setVisibility(View.GONE);
                t_refund.setVisibility(View.GONE);
                t_state_of_refund.setVisibility(View.GONE);
                img_delete.setVisibility(View.VISIBLE);
//            }else if (!isCancel && !isRefundCancel && isComplain && !isReceive && !isLock && isDeliver && !isEvaluation && isDelete) {
            }else if (evaluation_state.equals("1") && order_state.equals("40")&&refund_state.equals("0")&&lock_state.equals("0")) {
                //交易完成，已评价
                t_buy_again.setVisibility(View.VISIBLE);
                linear_state.setVisibility(View.VISIBLE);
                t_state.setTextColor(Color.parseColor("#ff454545"));
                t_state.setVisibility(View.VISIBLE);
                t_cancel.setVisibility(View.GONE);
                t_deliver.setVisibility(View.VISIBLE);
                t_receive.setVisibility(View.GONE);
                t_comment.setVisibility(View.GONE);
                t_iscomment.setVisibility(View.VISIBLE);
                t_refund.setVisibility(View.GONE);
                t_state_of_refund.setVisibility(View.GONE);
                img_delete.setVisibility(View.VISIBLE);
//            }else if (!isCancel && !isRefundCancel && isComplain && !isReceive && isLock && isDeliver && !isEvaluation && !isDelete) {
            }else if (evaluation_state.equals("0") && order_state.equals("30")&&refund_state.equals("0")&&lock_state.equals("1")) {
                //待收货，已退款,不可收货
                linear_state.setVisibility(View.VISIBLE);
                t_state.setTextColor(Color.parseColor("#eb5041"));
                t_state.setVisibility(View.VISIBLE);
                t_cancel.setVisibility(View.GONE);
                t_deliver.setVisibility(View.VISIBLE);
                t_receive.setVisibility(View.GONE);
                t_comment.setVisibility(View.GONE);
                t_iscomment.setVisibility(View.GONE);
                t_refund.setVisibility(View.GONE);
                t_state_of_refund.setVisibility(View.VISIBLE);
                img_delete.setVisibility(View.GONE);
//            }else if (!isCancel && !isRefundCancel && isComplain && isReceive && !isLock && isDeliver && !isEvaluation && !isDelete) {
            }else if (evaluation_state.equals("0") && order_state.equals("30")&&refund_state.equals("0")&&lock_state.equals("0")) {
                //待收货情况，可收货
                linear_state.setVisibility(View.VISIBLE);
                t_state.setTextColor(Color.parseColor("#eb5041"));
                t_state.setVisibility(View.VISIBLE);
                t_cancel.setVisibility(View.GONE);
                t_deliver.setVisibility(View.VISIBLE);
                t_receive.setVisibility(View.VISIBLE);
                t_comment.setVisibility(View.GONE);
                t_iscomment.setVisibility(View.GONE);
                t_refund.setVisibility(View.GONE);
                t_state_of_refund.setVisibility(View.GONE);
                img_delete.setVisibility(View.GONE);
//            }else if (!isCancel && isRefundCancel && isComplain && !isReceive && !isLock && !isDeliver && !isEvaluation && !isDelete) {
            }else if (evaluation_state.equals("0") && order_state.equals("20")&&refund_state.equals("0")&&lock_state.equals("0")) {
                //待发货，可退款
                linear_state.setVisibility(View.VISIBLE);
                t_state.setTextColor(Color.parseColor("#eb5041"));
                t_state.setVisibility(View.VISIBLE);
                t_cancel.setVisibility(View.GONE);
                t_deliver.setVisibility(View.GONE);
                t_receive.setVisibility(View.GONE);
                t_comment.setVisibility(View.GONE);
                t_iscomment.setVisibility(View.GONE);
                if(item.getOrder_from().equals("3")){
                    t_refund.setVisibility(View.GONE);
                }else {
                    t_refund.setVisibility(View.VISIBLE);
                }
                t_state_of_refund.setVisibility(View.GONE);
                img_delete.setVisibility(View.GONE);
//            }else if (!isCancel && !isRefundCancel && isComplain && !isReceive && isLock && !isDeliver && !isEvaluation && !isDelete) {
            }else if (evaluation_state.equals("0") && order_state.equals("20")&&refund_state.equals("0")&&lock_state.equals("1")) {
                //待发货，已退款
                linear_state.setVisibility(View.VISIBLE);
                t_state.setTextColor(Color.parseColor("#eb5041"));
                t_state.setVisibility(View.VISIBLE);
                t_cancel.setVisibility(View.GONE);
                t_deliver.setVisibility(View.GONE);
                t_receive.setVisibility(View.GONE);
                t_comment.setVisibility(View.GONE);
                t_iscomment.setVisibility(View.GONE);
                t_refund.setVisibility(View.GONE);
                t_state_of_refund.setVisibility(View.VISIBLE);
                img_delete.setVisibility(View.GONE);
            }else if(evaluation_state.equals("0") && order_state.equals("0")&&refund_state.equals("2")&&lock_state.equals("0")){
                //订单全部退款
                linear_state.setVisibility(View.VISIBLE);
                t_state.setTextColor(Color.parseColor("#ff454545"));
                t_state.setVisibility(View.VISIBLE);
                t_cancel.setVisibility(View.GONE);
                t_deliver.setVisibility(View.GONE);
                t_receive.setVisibility(View.GONE);
                t_comment.setVisibility(View.GONE);
                t_iscomment.setVisibility(View.GONE);
                t_refund.setVisibility(View.GONE);
                t_state_of_refund.setVisibility(View.GONE);
                img_delete.setVisibility(View.VISIBLE);
            }

            t_buy_again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String param=null;
                    StringBuffer buffer=new StringBuffer();
                    for (int i=0;i<goodsIdList.size();i++){
                        buffer.append(goodsIdList.get(i)+"|"+goodsNumList.get(i));
                        if(i!=goodsIdList.size()-1){
                            buffer.append(",");
                        }
                    }
                    param=buffer.toString();
                    Log.i("zjz","buy_all_param="+param);
                    HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_goods_buy_all_and_again, "&key="+MyApplication.getInstance().getMykey()+"&goodsinfo="+param, new HttpRevMsg() {
                        @Override
                        public void revMsg(final String msg) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("zjz","buy_all_msg="+msg);
                                    try {
                                        JSONObject object = new JSONObject(msg);
                                        if (object.getInt("code") == 200) {
                                            if(object.optString("datas").equals("1")){
                                                MyUpdateUI.sendUpdateCarNum(activity);
                                                Intent intent=new Intent(activity, CartActivity2.class);
                                                intent.putExtra("store_id","");
                                                activity.startActivity(intent);
                                            }else if(object.getJSONObject("datas").has("error")){
                                                Toast.makeText(activity,"该商品已下架或库存为零！",Toast.LENGTH_SHORT).show();
                                            }else {
                                                JSONObject jsonObject=object.getJSONObject("datas");
                                                Iterator keys=jsonObject.keys();
                                                StringBuffer stringBuffer=new StringBuffer();
                                                while (keys.hasNext()){
                                                    String value= (String) keys.next();
                                                    JSONObject goodsObject=jsonObject.getJSONObject(value);
                                                    JSONObject goods=goodsObject.optJSONObject("kucun");
                                                    if(goods!=null){
                                                        if(goods.optString("goods_name").length()>20){
                                                            stringBuffer.append("商品["+goods.optString("goods_name").substring(0,20)+"] ");
                                                        }else {
                                                            stringBuffer.append("商品["+goods.optString("goods_name")+"] ");
                                                        }
                                                    }else if(goodsObject.has("xiajia")){
                                                        if(goodsObject.optString("xiajia").length()>20){
                                                            stringBuffer.append("商品["+goodsObject.optString("xiajia").substring(0,20)+"] ");
                                                        }else {
                                                            stringBuffer.append("商品["+goodsObject.optString("xiajia")+"] ");
                                                        }
                                                    }

                                                }
                                                String text=stringBuffer+"库存为0或已经下架！";
                                                activity.startActivity(new Intent(activity, NoticeDialogActivity.class).putExtra("msg", text));
//                                                Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
                                                MyUpdateUI.sendUpdateCarNum(activity);
                                                Intent intent=new Intent(activity,CartActivity2.class);
                                                intent.putExtra("store_id","");
                                                activity.startActivity(intent);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        Log.i("zjz", e.toString());
                                        Log.i("zjz", msg);
                                        e.printStackTrace();

                                    }finally {
                                        ProgressDlgUtil.stopProgressDlg();
                                    }
                                }
                            });

                        }
                    });
                }
            });
            t_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(activity, OrderDetailActivity.class);
                    intent.putExtra("order_id",item.getOrder_list().get(finalJ).getOrder_id());
                    activity.startActivity(intent);
                }
            });
            img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new PromptDialog(activity, "是否删除该订单？", new Complete() {
                        @Override
                        public void complete() {
                            ProgressDlgUtil.showProgressDlg("Loading...", activity);
                            HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_order_del+ "&order_id=" + item.getOrder_list().get(finalJ).getOrder_id() ,"&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
                                @Override
                                public void revMsg(final String msg) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                JSONObject object = new JSONObject(msg);
                                                Log.i("zjz", "msg_del=" + msg);
                                                if (object.getInt("code") == 200) {
                                                    if (object.optString("datas").contains("成功")) {
                                                        Toast.makeText(activity, "删除成功！", Toast.LENGTH_SHORT).show();
                                                        MyUpdateUI.sendUpdateCollection(activity, MyUpdateUI.ORDER);
                                                        MyUpdateUI.sendUpdateCollection(activity,MyUpdateUI.MYORDERNUM);
                                                    } else {
                                                        Toast.makeText(activity, "失败！", Toast.LENGTH_SHORT).show();
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
            });

            if(item.getOrder_list().get(finalJ).getStore_id().equals("11")){  // 本地商品不支持退款
                t_refund.setText("取消订货");
            }else{
                t_refund.setText("取消订单");
            }

            t_refund.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.getOrder_list().get(finalJ).getStore_id().equals("11")){  // 本地商品不支持退款
//                        Toast.makeText(activity,"该商品不支持退款",Toast.LENGTH_LONG).show();
                        checkSoLution(item.getOrder_list().get(finalJ).getOrder_id());
                    }else {
                        Intent intent = new Intent(activity, ApplyRefundActivity.class);
                        intent.putExtra("isOrder", true);
                        intent.putExtra("title", "订单退款");
                        intent.putExtra("order_id",item.getOrder_list().get(finalJ).getOrder_id());
                        intent.putExtra("order_money", item.getOrder_list().get(finalJ).getOrder_amount());
                        activity.startActivity(intent);
                    }
                }
            });


            t_receive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new PromptDialog(activity, "是否确认收货？", new Complete() {
                        @Override
                        public void complete() {
                            ProgressDlgUtil.showProgressDlg("Loading...", activity);
                            HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_order_receive, "&order_id=" + item.getOrder_list().get(finalJ).getOrder_id() + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
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
                                                        Toast.makeText(activity, "成功收货！", Toast.LENGTH_SHORT).show();
                                                        MyUpdateUI.sendUpdateCollection(activity, MyUpdateUI.ORDER);
                                                        MyUpdateUI.sendUpdateCollection(activity,MyUpdateUI.MYORDERNUM);
                                                    } else {
                                                        Toast.makeText(activity, "失败！", Toast.LENGTH_SHORT).show();
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
            });
            t_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new PromptDialog(activity, "取消后无法恢复，确定取消该订单？", new Complete() {
                        @Override
                        public void complete() {
                            ProgressDlgUtil.showProgressDlg("Loading...", activity);
                            HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_order_cancle, "&order_id=" + item.getOrder_list().get(finalJ).getOrder_id() + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
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
                                                        Toast.makeText(activity, "成功取消！", Toast.LENGTH_SHORT).show();
                                                        MyUpdateUI.sendUpdateCollection(activity, MyUpdateUI.ORDER);
                                                        MyUpdateUI.sendUpdateCollection(activity,MyUpdateUI.MYORDERNUM);
                                                    } else {
                                                        Toast.makeText(activity, "失败！", Toast.LENGTH_SHORT).show();
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
            });

            /********原来查看物流 的地方************/

//            t_deliver.setOnClickListener(new View.OnClickListener() {   //  查看物流
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(activity, MyDeliverActivity.class);
//                    intent.putExtra("order_id", item.getOrder_list().get(finalJ).getOrder_id());
//                    intent.putExtra("order_id", item.getOrder_list().get(finalJ).getExtend_order_goods().);
//                    activity.startActivity(intent);
//
//                }
//            });
            t_total.setText("¥" + item.getOrder_list().get(j).getOrder_amount() + "( 含运费：" + NumberUtils.formatPrice(Double.valueOf(item.getOrder_list().get(j).getOrder_amount()) - Double.valueOf(item.getOrder_list().get(j).getGoods_amount())) + " )");
            t_order_sn.setText(item.getOrder_list().get(j).getOrder_sn());
            t_add_time.setText(Util.formatzjz.format(item.getAdd_time() * 1000) + "");
            t_name.setText(item.getOrder_list().get(j).getStore_name());
            holder.linear_store.addView(view);
        }
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }

    private void checkSoLution(final String order_id){

        new PromptDialog(activity, "是否取消该订单？", new Complete() {
            @Override
            public void complete() {
                ProgressDlgUtil.showProgressDlg("Loading...", activity);
                HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_refund+"&order_id=" + order_id ,"&key=" + MyApplication.getInstance().getMykey()+"&buyer_message=12&mobile_phone=1234567890", new HttpRevMsg() {
                    @Override
                    public void revMsg(final String msg) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject object = new JSONObject(msg);
                                    Log.i("zjz", "msg_del=" + msg);
                                    if (object.getInt("code") == 200) {
                                        if (object.optString("datas").contains("成功")) {
                                            Toast.makeText(activity, "已取消该订单！", Toast.LENGTH_SHORT).show();
                                            MyUpdateUI.sendUpdateCollection(activity, MyUpdateUI.ORDER);
                                            MyUpdateUI.sendUpdateCollection(activity,MyUpdateUI.MYORDERNUM);
                                        } else {
                                            Toast.makeText(activity, "取消该订单失败！", Toast.LENGTH_SHORT).show();
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
}
