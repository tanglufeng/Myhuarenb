package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.model.Orders;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/2/29.
 */
public class OrderListAdapter extends BaseAdapter {

    private ArrayList<Orders.OrderListEntity> orderList;
    private ArrayList<Goods> goodsList;
    Activity activity;
    LayoutInflater inflater = null;
    ZrcListView listView;
    //    MyListView listView;
    public Handler handler = new Handler();

    boolean isCancel;
    OrderGoodsAdapter orderGoodsAdapter;

    public OrderListAdapter(Activity activity,  ArrayList<Orders.OrderListEntity> orderList,
                           ZrcListView listView) {
        // TODO Auto-generated constructor stub

        this.activity = activity;
        this.listView = listView;
        this.orderList=orderList;
        this.goodsList=goodsList;
        inflater = LayoutInflater.from(activity);
    }


    ImageView imageView;

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder mHolder = null;
        final Orders.OrderListEntity goods = getItem(position);
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(activity);
            convertView = mInflater.inflate(R.layout.hwg_item_order_orderlsit, null);

            mHolder = new ViewHolder();
            mHolder.t_name= (TextView) convertView.findViewById(R.id.t_name);
            mHolder.t_total= (TextView) convertView.findViewById(R.id.t_total);
            mHolder.t_cancel= (TextView) convertView.findViewById(R.id.t_cancel);
            mHolder.t_state= (TextView) convertView.findViewById(R.id.t_state);
            mHolder.t_order_sn= (TextView) convertView.findViewById(R.id.t_order_sn);
            mHolder.goods_zListView= (ZrcListView) convertView.findViewById(R.id.goods_zListView);
            convertView.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) convertView.getTag();

        }


        isCancel=goods.isIf_cancel();
        mHolder.t_state.setText(goods.getState_desc());
        if(isCancel){
            mHolder.t_state.setTextColor(Color.parseColor("#ff454545"));
            mHolder.t_cancel.setVisibility(View.GONE);
        }else {
            mHolder.t_state.setTextColor(Color.parseColor("#eb5041"));
            mHolder.t_cancel.setVisibility(View.VISIBLE);
        }
        mHolder.t_order_sn.setText(goods.getOrder_sn());
        mHolder.t_name.setText(goods.getStore_name());
        orderGoodsAdapter=new OrderGoodsAdapter(activity, (ArrayList<Orders.OrderListEntity.ExtendOrderGoodsEntity>) orderList.get(position).getExtend_order_goods(),mHolder.goods_zListView);
        mHolder.goods_zListView.setAdapter(orderGoodsAdapter);
        setListViewHeight(mHolder.goods_zListView);
        orderGoodsAdapter.notifyDataSetChanged();
        return convertView;

    }


    private static class ViewHolder {
        TextView t_name;
        TextView t_total;
        TextView t_cancel;
        TextView t_state;
        TextView t_order_sn;

        ZrcListView goods_zListView;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return orderList == null ? 0 : orderList.size();
    }


    @Override
    public Orders.OrderListEntity getItem(int position) {
        if (orderList != null && orderList.size() != 0) {
            if (position >= orderList.size()) {
                return orderList.get(0);
            }
            return orderList.get(position);
        }
        return null;
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
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
