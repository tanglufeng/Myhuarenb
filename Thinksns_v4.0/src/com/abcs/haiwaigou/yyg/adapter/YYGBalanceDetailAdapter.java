package com.abcs.haiwaigou.yyg.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.haiwaigou.yyg.adapter.viewholder.YYGBalacneDetailViewHolder;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/3 0003.
 */
public class YYGBalanceDetailAdapter extends RecyclerView.Adapter<YYGBalacneDetailViewHolder>{

    Context context;
    private SortedList<Goods> mSortedList;
    private Activity activity;

    public YYGBalanceDetailAdapter(Activity activity) {
        this.activity = activity;
        mSortedList = new SortedList<>(Goods.class, new SortedList.Callback<Goods>() {

            /**
             * 返回一个负整数（第一个参数小于第二个）、零（相等）或正整数（第一个参数大于第二个）
             */
            @Override
            public int compare(Goods o1, Goods o2) {

                if (o1.getId() < o2.getId()) {
                    return -1;
                } else if (o1.getId() > o2.getId()) {
                    return 1;
                }

                return 0;
            }

            @Override
            public boolean areContentsTheSame(Goods oldItem, Goods newItem) {
                return oldItem.getGoods_id().equals(newItem.getGoods_id());
            }

            @Override
            public boolean areItemsTheSame(Goods item1, Goods item2) {
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

    public SortedList<Goods> getList() {
        return mSortedList;
    }

    public void addItems(ArrayList<Goods> list) {
        mSortedList.beginBatchedUpdates();

        for (Goods itemModel : list) {
            mSortedList.add(itemModel);
        }

        mSortedList.endBatchedUpdates();
    }

    public void deleteItems(ArrayList<Goods> items) {
        mSortedList.beginBatchedUpdates();
        for (Goods item : items) {
            mSortedList.remove(item);
        }
        mSortedList.endBatchedUpdates();
    }

    @Override
    public YYGBalacneDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_yyg_item_balance_list, parent, false);
        YYGBalacneDetailViewHolder hwgFragmentViewHolder = new YYGBalacneDetailViewHolder(view);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    @Override
    public void onBindViewHolder(YYGBalacneDetailViewHolder holder, int position) {
        Goods item=mSortedList.get(position);
        holder.t_add_time.setText(Util.format.format(item.getTime()));

        if(item.getDismoney()<0){
            holder.t_expend.setText(""+item.getDismoney());
            holder.t_income.setText("0.00");
            if(item.getType().equals("0")){
                //支付宝
                if(item.getPromote_money()!=0){
                    //使用了余额
                    holder.t_desc.setText("第("+item.getQishu()+")期 "+item.getTitle()+"，支付宝支付"+
                            NumberUtils.formatPrice(item.getMoney() - item.getPromote_money())+"，余额支付"+ NumberUtils.formatPrice(item.getPromote_money()));
                }else {
                    //未使用余额
                    holder.t_desc.setText("第("+item.getQishu()+")期 "+item.getTitle()+"，支付宝支付"+
                            NumberUtils.formatPrice(item.getMoney()));
                }
            }else if(item.getType().equals("1")){
                //微信
                if(item.getPromote_money()!=0){
                    //使用了余额
                    holder.t_desc.setText("第("+item.getQishu()+")期 "+item.getTitle()+"，微信支付"+
                            NumberUtils.formatPrice(item.getMoney() - item.getPromote_money())+"，余额支付"+ NumberUtils.formatPrice(item.getPromote_money()));
                }else {
                    //未使用余额
                    holder.t_desc.setText("第("+item.getQishu()+")期 "+item.getTitle()+"，微信支付"+
                            NumberUtils.formatPrice(item.getMoney()));
                }
            }else if (item.getType().equals("2")){
                holder.t_desc.setText("第("+item.getQishu()+")期 "+item.getTitle()+"，余额支付"+
                        NumberUtils.formatPrice(item.getMoney()));
            } else if(!item.getOrder_id().equals("-1")){
                holder.t_desc.setText("订单"+item.getOrder_id()+"，余额支付"+
                        NumberUtils.formatPrice(item.getDismoney()));
            }

        }else {
            holder.t_expend.setText("0.00");
            holder.t_income.setText("+"+item.getDismoney());
            if(!item.getOrder_id().equals("-1")){
                holder.t_desc.setText("订单"+item.getOrder_id()+"退款，余额存入"+
                        NumberUtils.formatPrice(item.getDismoney()));
            }else {
                holder.t_desc.setText("第("+item.getQishu()+")期 "+item.getTitle()+"中奖，余额存入"+
                        NumberUtils.formatPrice(item.getDismoney()));
            }


        }
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }
}
