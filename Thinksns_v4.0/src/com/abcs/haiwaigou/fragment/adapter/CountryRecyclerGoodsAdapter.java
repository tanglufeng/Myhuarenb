package com.abcs.haiwaigou.fragment.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.fragment.viewholder.CountryGoodsViewHolder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.sociax.android.R;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/1/15.
 */
public class CountryRecyclerGoodsAdapter extends RecyclerView.Adapter<CountryGoodsViewHolder> {

    ArrayList<Goods>goods;
    Context context;

    public ArrayList<Goods> getDatas() {
        return goods;
    }

    CountryGoodsViewHolder.ItemOnClick itemOnClick;

    public CountryRecyclerGoodsAdapter(CountryGoodsViewHolder.ItemOnClick itemOnClick) {
        this.itemOnClick = itemOnClick;
        this.goods=new ArrayList<>();
    }

    @Override
    public CountryGoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_country_goods,parent,false);
        CountryGoodsViewHolder countryGoodsViewHolder=new CountryGoodsViewHolder(view,itemOnClick);
        this.context=parent.getContext();
        return countryGoodsViewHolder;
    }

    @Override
    public void onBindViewHolder(CountryGoodsViewHolder holder, int position) {
        //图片加载
        LoadPicture loadPicture=new LoadPicture();
        loadPicture.initPicture(holder.img_goods_icon, goods.get(position).getPicarr());
//        holder.img_goods_icon.setImageResource(R.drawable.img_chanpin1);
        holder.t_goods_money.setText(NumberUtils.formatPrice(goods.get(position).getMoney()));
        holder.t_goods_oldmoney.setText(NumberUtils.formatPrice(goods.get(position).getDismoney()));
        holder.t_goods_name.setText(goods.get(position).getTitle());
        holder.t_goods_oldmoney.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }
}
