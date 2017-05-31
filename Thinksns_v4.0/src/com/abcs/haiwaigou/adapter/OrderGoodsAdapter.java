package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.haiwaigou.model.Orders;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/2/29.
 */
public class OrderGoodsAdapter extends BaseAdapter {

    private ArrayList<Orders.OrderListEntity.ExtendOrderGoodsEntity> goodsList;

    Activity activity;
    LayoutInflater inflater = null;
    ZrcListView listView;
    //    MyListView listView;
    public Handler handler = new Handler();


    public OrderGoodsAdapter(Activity activity,
                             ArrayList<Orders.OrderListEntity.ExtendOrderGoodsEntity> goodsList, ZrcListView listView) {
        // TODO Auto-generated constructor stub

        this.activity = activity;
        this.goodsList = goodsList;
        this.listView = listView;

        inflater = LayoutInflater.from(activity);
    }


    ImageView imageView;

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder mHolder = null;
        final Orders.OrderListEntity.ExtendOrderGoodsEntity goods = getItem(position);
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(activity);
            convertView = mInflater.inflate(R.layout.hwg_item_order, null);

            mHolder = new ViewHolder();
            mHolder.t_goods_name= (TextView) convertView.findViewById(R.id.t_goods_name);
            mHolder.t_goods_price= (TextView) convertView.findViewById(R.id.t_goods_price);
            mHolder.t_num= (TextView) convertView.findViewById(R.id.t_num);
            mHolder.img_goods= (ImageView) convertView.findViewById(R.id.img_goods);
            convertView.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) convertView.getTag();

        }


        mHolder.t_goods_name.setText(goods.getGoods_name());
        mHolder.t_goods_price.setText("￥"+goods.getGoods_price());
        mHolder.t_num.setText(goods.getGoods_num()+"");
        LoadPicture loadPicture=new LoadPicture();
        loadPicture.initPicture(mHolder.img_goods,goods.getGoods_image_url());
//        mHolder.group_zListView.setAdapter();
//        mHolder.pay_zListView.setAdapter();
        return convertView;

    }


    private static class ViewHolder {
        TextView t_goods_name;
        TextView t_goods_price;
        TextView t_num;
        ImageView img_goods;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return goodsList == null ? 0 : goodsList.size();
    }


    @Override
    public Orders.OrderListEntity.ExtendOrderGoodsEntity getItem(int position) {
        if (goodsList != null && goodsList.size() != 0) {
            if (position >= goodsList.size()) {
                return goodsList.get(0);
            }
            return goodsList.get(position);
        }
        return null;
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}
