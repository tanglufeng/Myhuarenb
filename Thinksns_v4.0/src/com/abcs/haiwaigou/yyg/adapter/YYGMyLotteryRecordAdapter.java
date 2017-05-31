package com.abcs.haiwaigou.yyg.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.yyg.activity.LotteryDetailActivity;
import com.abcs.haiwaigou.yyg.adapter.viewholder.YYGMyLotteryRecordViewHolder;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/7/2 0002.
 */
public class YYGMyLotteryRecordAdapter extends RecyclerView.Adapter<YYGMyLotteryRecordViewHolder>{
    private ArrayList<Goods> goodsList;
    Activity activity;
    public Handler handler = new Handler();
    Context context;
    private SortedList<Goods> mSortedList;


    public YYGMyLotteryRecordAdapter(Activity activity) {
        this.activity=activity;
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
                return oldItem.getTitle().equals(newItem.getTitle());
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
    public YYGMyLotteryRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_yyg_item_my_lottery_record, parent, false);
        YYGMyLotteryRecordViewHolder hwgFragmentViewHolder = new YYGMyLotteryRecordViewHolder(view);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    //    String[] strings;
    @Override
    public void onBindViewHolder(YYGMyLotteryRecordViewHolder holder, final int position) {
        final Goods item = mSortedList.get(position);
        ImageLoader.getInstance().displayImage(item.getPicarr(), holder.img_icon, Options.getHDOptions());
        //isDeliver : 是否已做处理 [isDeliver = 0 (没有做处理)--isDeliver = 1 （已做发货处理）--isDeliver = 2（用户已收到货）--isDeliver =-1（已做云购币兑换处理）9待发货 ]
        if(item.getState_desc().equals("1")){
            holder.linear_state.setVisibility(View.VISIBLE);
            if(item.getGoods_state().equals("0")){
                holder.t_state.setText("未操作");
                holder.img_state.setVisibility(View.GONE);
            }else if(item.getGoods_state().equals("9")){
                holder.t_state.setText("待发货");
                holder.img_state.setVisibility(View.VISIBLE);
            }else if(item.getGoods_state().equals("-1")){
                holder.t_state.setText("已折现");
                holder.img_state.setVisibility(View.VISIBLE);
            }else if (item.getGoods_state().equals("2")){
                holder.t_state.setText("已签收");
                holder.img_state.setVisibility(View.VISIBLE);
            }else if(item.getGoods_state().equals("1")){
                holder.t_state.setText("待签收");
                holder.img_state.setVisibility(View.VISIBLE);
            }
        }else {
            holder.linear_state.setVisibility(View.GONE);
            holder.img_state.setVisibility(View.GONE);
        }
        holder.t_title.setText("第("+item.getQishu()+")期 "+item.getTitle());
        holder.t_money.setText("¥" + item.getTotal_money());
        holder.t_all_need.setText("总需" + item.getZongrenshu() + "人次");
        holder.t_time.setText(Util.format.format(item.getQ_end_time()));
        holder.t_luckyNum.setText(item.getQ_user_code());
        holder.t_canyu.setText(item.getGoods_salenum()+"人次");
        holder.linear_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, LotteryDetailActivity.class);
                intent.putExtra("act_id", item.getQ_uid());
                intent.putExtra("isLottery", false);
                intent.putExtra("isBuy", true);
                activity.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mSortedList.size();
    }
}
