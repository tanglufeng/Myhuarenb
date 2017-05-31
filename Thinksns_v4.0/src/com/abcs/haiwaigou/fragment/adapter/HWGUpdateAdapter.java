package com.abcs.haiwaigou.fragment.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.fragment.viewholder.HWGUpdateViewHolder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.sociax.android.R;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/1/12.
 */
public class HWGUpdateAdapter extends RecyclerView.Adapter<HWGUpdateViewHolder> {

//    ArrayList<Brand> brands;
//    Context context;
//
//    public ArrayList<Brand> getDatas() {
//        return brands;
//    }
//
//    HWGFragmentViewHolder.ItemOnClick itemOnClick;
//
//    public HWGFragmentAdapter(HWGFragmentViewHolder.ItemOnClick itemOnClick) {
//        this.itemOnClick = itemOnClick;
//        this.brands = new ArrayList<>();
//    }
//
//    @Override
//    public HWGFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_pinpai, parent, false);
//        HWGFragmentViewHolder hwgFragmentViewHolder = new HWGFragmentViewHolder(view, itemOnClick);
//        this.context = parent.getContext();
//        return hwgFragmentViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(HWGFragmentViewHolder holder, final int position) {
//        //图片加载
//        LoadPicture loadPicture=new LoadPicture();
//        loadPicture.initPicture(holder.img_pinpai,brands.get(position).getBimg());
////        holder.img_pinpai.setImageResource(R.drawable.banner02);
//        holder.t_saled.setText("150");
//        holder.t_remain.setText("50");
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return brands.size();
//    }





    ArrayList<Goods>goods;
    Context context;
    boolean isLocal;

    public ArrayList<Goods> getDatas() {
        return goods;
    }

    HWGUpdateViewHolder.ItemOnClick itemOnClick;

    public HWGUpdateAdapter(HWGUpdateViewHolder.ItemOnClick itemOnClick, boolean isLocal) {
        this.itemOnClick = itemOnClick;
        this.isLocal=isLocal;
        this.goods = new ArrayList<>();
    }

    @Override
    public HWGUpdateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_country_goods, parent, false);
        HWGUpdateViewHolder hwgFragmentViewHolder = new HWGUpdateViewHolder(view, itemOnClick);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    @Override
    public void onBindViewHolder(HWGUpdateViewHolder holder, final int position) {
        //图片加载
        LoadPicture loadPicture=new LoadPicture();
        loadPicture.initPicture(holder.img_goods_icon, goods.get(position).getPicarr());
//        loadPicture.initPicture(holder.img_goods_icon,isLocal, goods.get(position).getPicarr(), Integer.parseInt(goods.get(position).getGoods_id()));
        holder.t_goods_money.setText(NumberUtils.formatPrice(goods.get(position).getMoney()));
        holder.t_goods_oldmoney.setVisibility(View.GONE);
//        holder.t_goods_oldmoney.setText(NumberUtils.formatPrice(goods.get(position).getDismoney()));
        holder.t_goods_name.setText(goods.get(position).getTitle());
        holder.t_goods_oldmoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

    }

    @Override
    public int getItemCount() {
        return goods.size();
    }
}
