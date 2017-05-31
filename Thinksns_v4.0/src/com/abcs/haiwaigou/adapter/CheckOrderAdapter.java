package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.CheckOrderActivity;
import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.adapter.viewholder.CheckOrderViewHolder;
import com.abcs.haiwaigou.model.CheckOrder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.sociax.android.R;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/1/13.
 */
public class CheckOrderAdapter extends RecyclerView.Adapter<CheckOrderViewHolder> {
    ArrayList<Goods> goods;
    Context context;
    Activity activity;
    ArrayList<CheckOrder> checkOrders;

    public ArrayList<CheckOrder> getCheckOrders() {
        return checkOrders;
    }

    public ArrayList<Goods> getDatas() {
        return goods;
    }


    public CheckOrderAdapter(Activity activity) {
        this.activity=activity;
        this.goods = new ArrayList<>();
        this.checkOrders = new ArrayList<>();
    }

//    public String params[]=new String[checkOrders.size()];
    @Override
    public CheckOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_check_order, parent, false);
        CheckOrderViewHolder checkOrderViewHolder = new CheckOrderViewHolder(view);
        this.context = parent.getContext();
        return checkOrderViewHolder;
    }



    @Override
    public void onBindViewHolder(CheckOrderViewHolder holder, final int position) {
//        //图片加载
//        LoadPicture loadPicture = new LoadPicture();
//        loadPicture.initPicture(holder.img_goods, goods.get(position).getPicarr());
//        holder.t_goods_name.setText(goods.get(position).getTitle());
//        holder.t_goods_price.setText(NumberUtils.formatPrice(goods.get(position).getMoney()));
//        holder.t_num.setText(goods.get(position).getGoods_num()+"");
        holder.linear_goods.removeAllViews();
        holder.linear_goods.invalidate();
        String store_name = null;
        double good_price=0;
        double good_freight=0;


        for (int i = 0; i < checkOrders.get(position).getStore_list().size(); i++) {
            store_name=checkOrders.get(position).getStore_list().get(i).getStore_name();
            good_price+=checkOrders.get(position).getStore_list().get(i).getGoods_price()*Integer.valueOf(checkOrders.get(position).getStore_list().get(i).getGoods_num());
            good_freight+=checkOrders.get(position).getStore_list().get(i).getGoods_freight();
            View view = activity.getLayoutInflater().inflate(R.layout.hwg_item_order, null);
            TextView t_goods_name = (TextView) view.findViewById(R.id.t_goods_name);
            TextView t_goods_price = (TextView) view.findViewById(R.id.t_goods_price);
            TextView t_num = (TextView) view.findViewById(R.id.t_num);
            ImageView img_goods = (ImageView) view.findViewById(R.id.img_goods);
            final int finalI = i;
            img_goods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, GoodsDetailActivity.class);
                    intent.putExtra("sid", checkOrders.get(position).getStore_list().get(finalI).getGoods_id());
                    intent.putExtra("pic", checkOrders.get(position).getStore_list().get(finalI).getGoods_image_url());
                    activity.startActivity(intent);
                }
            });
            t_goods_name.setText(checkOrders.get(position).getStore_list().get(i).getGoods_name());
            t_goods_price.setText(NumberUtils.formatPrice(checkOrders.get(position).getStore_list().get(i).getGoods_price()));
            t_num.setText("X"+checkOrders.get(position).getStore_list().get(i).getGoods_num());
            LoadPicture loadPicture = new LoadPicture();
            loadPicture.initPicture(img_goods,checkOrders.get(position).getStore_list().get(i).getGoods_image_url());
            holder.linear_goods.addView(view);
        }
//        if (checkOrders.get(position).getMansong_list().size()!=0){
//            for (int i=0;i<checkOrders.get(position).getMansong_list().size();i++){
//                holder.t_mansong.setVisibility(View.VISIBLE);
//                holder.t_mansong_desc.setVisibility(View.VISIBLE);
//                holder.t_mansong_desc.setText(checkOrders.get(position).getMansong_list().get(i).getDesc());
//            }
//        }
        holder.ed_words.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CheckOrderActivity.params[position] = "&pay_message[" + checkOrders.get(position).getStore_id() + "]=" + s.toString();
//                params[position]=
            }
        });

        Log.i("zjz","goods_price="+good_price);
        Log.i("zjz","goods_freight="+good_freight);
        Log.i("zjz", "store_total=" + good_freight + good_price);

        holder.liner_yun_heji.setVisibility(View.GONE);

        holder.t_freight.setText(NumberUtils.formatPrice(good_freight));
//        holder.t_goods_total.setText(NumberUtils.formatPrice(good_price));
        holder.t_store_total.setText(NumberUtils.formatPrice(good_price+good_freight));
        holder.t_store_name.setText(store_name);

    }

    @Override
    public int getItemCount() {
        return checkOrders.size();
    }

}
