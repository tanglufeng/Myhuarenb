package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.local.huohang.view.HuoHangCheckOrderActivity;
import com.abcs.haiwaigou.adapter.viewholder.CheckOrderViewHolder;
import com.abcs.haiwaigou.model.CheckOrder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/1/13.
 */
public class HuoHangCheckOrderAdapter extends RecyclerView.Adapter<CheckOrderViewHolder> {
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


    public HuoHangCheckOrderAdapter(Activity activity) {
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
        holder.liner_yun_heji.setVisibility(View.GONE);
        String store_name = null;
        double good_price=0;
        double good_freight=0;


        for (int i = 0; i < checkOrders.get(position).getStore_list().size(); i++) {
            store_name=checkOrders.get(position).getStore_list().get(i).getStore_name();
            good_price+=checkOrders.get(position).getStore_list().get(i).getGoods_price()*Integer.valueOf(checkOrders.get(position).getStore_list().get(i).getGoods_num());
            good_freight+=checkOrders.get(position).getStore_list().get(i).getGoods_freight();

            View itemView = activity.getLayoutInflater().inflate(R.layout.huohang_item_order, null);

            ImageView img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
            TextView tv_en_title=(TextView) itemView.findViewById(R.id.tv_en_title);
            TextView tv_title=(TextView) itemView.findViewById(R.id.tv_title);
            TextView tv_store=(TextView) itemView.findViewById(R.id.tv_store);
            TextView tv_price=(TextView) itemView.findViewById(R.id.tv_price);
            TextView tv_sui=(TextView) itemView.findViewById(R.id.tv_sui);
            TextView t_num=(TextView) itemView.findViewById(R.id.t_num);



            final int finalI = i;

            img_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(checkOrders.get(position).getStore_list().get(finalI).getStore_id())&&checkOrders.get(position).getStore_list().get(finalI).getStore_id().equals("1")){

                        Intent intent = new Intent(activity, GoodsDetailActivity.class);
                        intent.putExtra("sid", checkOrders.get(position).getStore_list().get(finalI).getGoods_id());
                        intent.putExtra("pic", checkOrders.get(position).getStore_list().get(finalI).getGoods_image_url());
                        activity.startActivity(intent);
                    }
                }
            });

           /* liner_hdong.removeAllViews();
            liner_tag.removeAllViews();

            lin_tianjai.setVisibility(View.VISIBLE);*/
            if(!TextUtils.isEmpty(checkOrders.get(position).getStore_list().get(i).getGoods_image_url())){
//                http://www.huaqiaobang.com/data/upload/shop/store/goods/11/11_05375327290881075.jpg
                MyApplication.imageLoader.displayImage(checkOrders.get(position).getStore_list().get(i).getGoods_image_url(),img_logo,MyApplication.options);
            }
            tv_title.setText(checkOrders.get(position).getStore_list().get(i).getGoods_name());
            tv_en_title.setText(checkOrders.get(position).getStore_list().get(i).goods_en_name+"");
            tv_store.setText("货号:"+checkOrders.get(position).getStore_list().get(i).goods_serial);
            tv_price.setText("€ "+NumberUtils.formatPriceNo(checkOrders.get(position).getStore_list().get(i).getGoods_price()));
            t_num.setText("X"+checkOrders.get(position).getStore_list().get(i).getGoods_num());

            tv_sui.setText(""+checkOrders.get(position).getStore_list().get(i).tax_rate+"");

     /*       if(data.tagArr!=null){

                if(data.tagArr.size()>0){
                    liner_tag.setVisibility(View.VISIBLE);
                    for (int i = 0; i <data.tagArr.size() ; i++) {

                        TagArr bean_hd=data.tagArr.get(i);
                        View view_tab=View.inflate(getContext(),R.layout.item_textview_t,null);

                        ViewGroup parent = (ViewGroup) view_tab.getParent();
                        if (parent != null) {
                            parent.removeAllViews();
                        }

                        TextView tv=(TextView) view_tab.findViewById(R.id.tv_tab);

                        if(bean_hd!=null){
                            if(!TextUtils.isEmpty(bean_hd.tagName)){
                                tv.setText(bean_hd.tagName);
                            }
                        }

                        liner_tag.addView(view_tab);
                    }
                }else {
                    liner_tag.setVisibility(View.GONE);
                }
            }*/


            holder.linear_goods.addView(itemView);
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
                Log.i("zds", "afterTextChanged: s.length()"+s.length());
                HuoHangCheckOrderActivity.native_message=s.toString();
            }
        });

        Log.i("zjz","goods_price="+good_price);
        Log.i("zjz","goods_freight="+good_freight);
        Log.i("zjz", "store_total=" + good_freight + good_price);
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
