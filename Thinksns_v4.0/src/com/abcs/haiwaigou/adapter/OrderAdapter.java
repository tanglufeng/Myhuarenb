package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.activity.GoodsDetailActivity2;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.Orders;
import com.abcs.haiwaigou.model.PayWay;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.activity.FuyouPayActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/2/29.
 */
public class OrderAdapter extends BaseAdapter {

    private ArrayList<PayWay> payList;
    private ArrayList<Orders> orderses;


    Activity activity;
    LayoutInflater inflater = null;
    ZrcListView listView;
    //    MyListView listView;
    public Handler handler = new Handler();
    OrderListAdapter orderListAdapter;
    OrderPayListAdapter orderPayListAdapter;

    private RequestQueue mRequestQueue;

    //    ArrayList<String>isCancel=new ArrayList<String>();
    public OrderAdapter(Activity activity, ArrayList<Orders> orderses, ArrayList<PayWay> payList, ZrcListView listView) {
        // TODO Auto-generated constructor stub

        this.activity = activity;
        this.listView = listView;
        this.payList = payList;
        this.orderses = orderses;
        mRequestQueue = Volley.newRequestQueue(activity);
        inflater = LayoutInflater.from(activity);
    }


    ImageView imageView;

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder mHolder = null;
        final Orders goods = getItem(position);
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(activity);
            convertView = mInflater.inflate(R.layout.hwg_item_order_group, null);

            mHolder = new ViewHolder();
            mHolder.t_order_time = (TextView) convertView.findViewById(R.id.t_order_time);
//            mHolder.group_zListView = (ZrcListView) convertView.findViewById(R.id.group_zListView);
//            mHolder.pay_zListView = (ZrcListView) convertView.findViewById(R.id.pay_zListView);
            mHolder.linear_store = (LinearLayout) convertView.findViewById(R.id.linear_store);
            mHolder.linear_pay = (LinearLayout) convertView.findViewById(R.id.linear_pay);
            mHolder.linear_order = (LinearLayout) convertView.findViewById(R.id.linear_order);

            convertView.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) convertView.getTag();

        }

        mHolder.t_order_time.setText(Util.formatzjz.format(goods.getAdd_time() * 1000) + "");

        mHolder.linear_store.removeAllViews();
        mHolder.linear_store.invalidate();
        mHolder.linear_pay.removeAllViews();
        mHolder.linear_pay.invalidate();

        Log.i("zjz","paycount="+goods.getPay_amount());
        if(goods.getPay_amount().equals("")){
            Log.i("zjz","不显示");
            mHolder.linear_pay.setVisibility(View.GONE);
        }else {
            Log.i("zjz","显示");
            mHolder.linear_pay.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < payList.size(); i++) {
            View view2 = activity.getLayoutInflater().inflate(R.layout.hwg_item_order_pay, null);
            TextView t_total2 = (TextView) view2.findViewById(R.id.t_total);
            TextView t_pay = (TextView) view2.findViewById(R.id.t_pay);
            LinearLayout linear_zhifu = (LinearLayout) view2.findViewById(R.id.linear_zhifu);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Util.WIDTH * 2 / 3, Util.HEIGHT / 14);
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.setMargins(0, 10, 0, 10);
            linear_zhifu.setLayoutParams(layoutParams);
            t_total2.setText("￥" + goods.getPay_amount());
            t_pay.setText(payList.get(i).getPayment_name());
            final int finalI = i;
            linear_zhifu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String url = TLUrl.getInstance().URL_hwg_base+"/mobile/index.php?act=member_payment&op=pay" +
                            "&key=" + MyApplication.getInstance().getMykey() +
                            "&pay_sn=" + goods.getPay_sn() +
                            "&payment_code=" + payList.get(finalI).getPayment_code();
                    Intent intent = new Intent(activity, FuyouPayActivity.class);
                    intent.putExtra("id", "1");
                    intent.putExtra("url", url);
                    activity.startActivity(intent);
                }
            });
            mHolder.linear_pay.addView(view2);

        }
        for (int j = 0; j < orderses.get(position).getOrder_list().size(); j++) {
            View view = activity.getLayoutInflater().inflate(R.layout.hwg_item_order_orderlsit, null);
            TextView t_name = (TextView) view.findViewById(R.id.t_name);
            TextView t_total = (TextView) view.findViewById(R.id.t_total);
            TextView t_cancel = (TextView) view.findViewById(R.id.t_cancel);
            TextView t_state = (TextView) view.findViewById(R.id.t_state);
            TextView t_order_sn = (TextView) view.findViewById(R.id.t_order_sn);
            TextView t_deliver = (TextView) view.findViewById(R.id.t_deliver);
            TextView t_receive = (TextView) view.findViewById(R.id.t_receive);
//            TextView t_order_time = (TextView) view.findViewById(R.id.t_order_time);
//            t_order_time.setText(goods.getAdd_time());
            LinearLayout linear_goods = (LinearLayout) view.findViewById(R.id.linear_goods);
//            LinearLayout linear_pay = (LinearLayout) view.findViewById(R.id.linear_pay);

            linear_goods.removeAllViews();
            linear_goods.invalidate();
            final int finalJ = j;
            for (int k = 0; k < orderses.get(position).getOrder_list().get(j).getExtend_order_goods().size(); k++) {
                View view1 = activity.getLayoutInflater().inflate(R.layout.hwg_item_order, null);
                TextView t_goods_name = (TextView) view1.findViewById(R.id.t_goods_name);
                TextView t_goods_price = (TextView) view1.findViewById(R.id.t_goods_price);
                TextView t_num = (TextView) view1.findViewById(R.id.t_num);
                ImageView img_goods = (ImageView) view1.findViewById(R.id.img_goods);
                t_goods_name.setText(orderses.get(position).getOrder_list().get(j).getExtend_order_goods().get(k).getGoods_name());
                t_goods_price.setText("￥" + orderses.get(position).getOrder_list().get(j).getExtend_order_goods().get(k).getGoods_price());
                t_num.setText(orderses.get(position).getOrder_list().get(j).getExtend_order_goods().get(k).getGoods_num() + "");
                LoadPicture loadPicture = new LoadPicture();
                loadPicture.initPicture(img_goods, orderses.get(position).getOrder_list().get(j).getExtend_order_goods().get(k).getGoods_image_url());
                final int finalK = k;
                img_goods.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(activity,GoodsDetailActivity2.class);
                        intent.putExtra("sid", orderses.get(position).getOrder_list().get(finalJ).getExtend_order_goods().get(finalK).getGoods_id());
                        activity.startActivity(intent);
                    }
                });
                linear_goods.addView(view1);
            }
//            linear_pay.removeAllViews();

            boolean isCancel;
            boolean isDeliver;
            boolean isReceive;
            isCancel = orderses.get(position).getOrder_list().get(j).isIf_cancel();
            isDeliver = orderses.get(position).getOrder_list().get(j).isIf_deliver();
            isReceive = orderses.get(position).getOrder_list().get(j).isIf_receive();
            Log.i("zjz", "isCancel=" + isCancel);
            if (!isCancel && !isDeliver & !isReceive) {
                t_state.setTextColor(Color.parseColor("#ff454545"));
                t_cancel.setVisibility(View.GONE);
//                linear_pay.setVisibility(View.GONE);
                mHolder.linear_pay.setVisibility(View.GONE);

                t_deliver.setVisibility(View.GONE);
                t_receive.setVisibility(View.GONE);
            } else if (isCancel && !isDeliver && !isReceive) {
                t_state.setTextColor(Color.parseColor("#eb5041"));
                t_cancel.setVisibility(View.VISIBLE);
//                mHolder.linear_pay.setVisibility(View.VISIBLE);
                t_deliver.setVisibility(View.GONE);
                t_receive.setVisibility(View.GONE);

            } else if (!isCancel && isDeliver && !isReceive) {
                t_receive.setVisibility(View.GONE);
                t_deliver.setVisibility(View.VISIBLE);
                t_cancel.setVisibility(View.GONE);
//                mHolder.linear_pay.setVisibility(View.GONE);
                t_state.setTextColor(Color.parseColor("#eb5041"));
            } else if (isReceive && isDeliver && !isCancel) {
                t_receive.setVisibility(View.VISIBLE);
                t_deliver.setVisibility(View.VISIBLE);
                t_cancel.setVisibility(View.GONE);
//                mHolder.linear_pay.setVisibility(View.GONE);
                t_state.setTextColor(Color.parseColor("#eb5041"));
            }else {
                t_state.setTextColor(Color.parseColor("#ff454545"));
                t_cancel.setVisibility(View.GONE);
//                mHolder.linear_pay.setVisibility(View.GONE);

                t_deliver.setVisibility(View.GONE);
                t_receive.setVisibility(View.GONE);
            }

            t_state.setText(orderses.get(position).getOrder_list().get(j).getState_desc());

            t_receive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgressDlgUtil.showProgressDlg("", activity);
                    HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_order_receive, "&order_id=" + orderses.get(position).getOrder_list().get(finalJ).getOrder_id() + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
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
            });
            t_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgressDlgUtil.showProgressDlg("", activity);
                    HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_order_cancle, "&order_id=" + orderses.get(position).getOrder_list().get(finalJ).getOrder_id() + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
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
            });
            t_receive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            t_total.setText("￥" + orderses.get(position).getOrder_list().get(j).getGoods_amount());
            t_order_sn.setText(orderses.get(position).getOrder_list().get(j).getOrder_sn());
            t_name.setText(orderses.get(position).getOrder_list().get(j).getStore_name());
            mHolder.linear_store.addView(view);
        }

//        mHolder.linear_pay.removeAllViews();
//        for (int i = 0; i < payList.size(); i++) {
//            View view2 = activity.getLayoutInflater().inflate(R.layout.hwg_item_order_pay, null);
//            TextView t_total2 = (TextView) view2.findViewById(R.id.t_total);
//            TextView t_pay = (TextView) view2.findViewById(R.id.t_pay);
//            t_total2.setText("￥"+goods.getPay_amount());
//            t_pay.setText(payList.get(i).getPayment_name());
//            mHolder.linear_pay.addView(view2);
//        }


//        orderListAdapter=new OrderListAdapter(activity, (ArrayList<Orders.OrderListEntity>) orderses.get(position).getOrder_list(),mHolder.group_zListView);
//        orderPayListAdapter=new OrderPayListAdapter(activity,payList,goods.getPay_amount()+"",mHolder.pay_zListView);
//        mHolder.group_zListView.setAdapter(orderListAdapter);
//        setListViewHeight(mHolder.group_zListView);
//        mHolder.pay_zListView.setAdapter(orderPayListAdapter);
//        setListViewHeight(mHolder.pay_zListView);
//        orderListAdapter.notifyDataSetChanged();
//        orderPayListAdapter.notifyDataSetChanged();
        return convertView;

    }


    private static class ViewHolder {
        TextView t_order_time;
        ZrcListView group_zListView;
        ZrcListView pay_zListView;

        LinearLayout linear_store;
        LinearLayout linear_pay;
        LinearLayout linear_order;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return orderses == null ? 0 : orderses.size();
    }


    @Override
    public Orders getItem(int position) {
        if (orderses != null && orderses.size() != 0) {
            if (position >= orderses.size()) {
                return orderses.get(0);
            }
            return orderses.get(position);
        }
        return null;
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setListViewHeight(ZrcListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
