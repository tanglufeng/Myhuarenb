package com.abcs.haiwaigou.yyg.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.yyg.activity.LotteryDetailActivity;
import com.abcs.haiwaigou.yyg.adapter.viewholder.YYGLotteryFragmentViewHolder;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zjz on 2016/7/2 0002.
 */
public class YYGLotteryFragmentAdapter extends RecyclerView.Adapter<YYGLotteryFragmentViewHolder> {
    private ArrayList<Goods> goodsList;
    Activity activity;
    public Handler handler = new Handler();
    Context context;
    private SortedList<Goods> mSortedList;


    public YYGLotteryFragmentAdapter(Activity activity) {
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
    public YYGLotteryFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_yyg_item_publish, parent, false);
        YYGLotteryFragmentViewHolder hwgFragmentViewHolder = new YYGLotteryFragmentViewHolder(view);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    //    String[] strings;
    @Override
    public void onBindViewHolder(YYGLotteryFragmentViewHolder holder, final int position) {
        if (holder != null) {
            final Goods item = mSortedList.get(position);
            holder.t_name.setText("第(" + item.getQishu() + ")期 " + item.getTitle());
            ImageLoader.getInstance().displayImage(item.getPicarr(), holder.img_icon, Options.getHDOptions());
            holder.t_total_money.setText("¥" + item.getTotal_money());

            if (item.getQ_user_code().equals("1")) {
                holder.img_isget.setVisibility(View.GONE);
                holder.img_isluck.setVisibility(View.VISIBLE);
                holder.img_isbuy.setVisibility(View.GONE);
            } else {
                holder.img_isluck.setVisibility(View.GONE);
                holder.img_isget.setVisibility(View.GONE);
                holder.img_isbuy.setVisibility(item.isIf_lock() ? View.VISIBLE : View.GONE);
            }
//        holder.img_isget.setVisibility(item.getQ_user_code().equals("1")?View.VISIBLE:View.GONE);
            if (item.getState_desc().equals("0") && item.getQ_counttime().equals("-1")) {
                //已揭晓
                holder.relative_done.setVisibility(View.GONE);
                holder.linear_undo.setVisibility(View.VISIBLE);
                holder.t_code.setText(item.getQ_content());
                holder.t_winner.setText(item.getQ_user());
                holder.t_joins.setText(item.getGoods_salenum() + "人次");
                holder.t_time.setText(Util.format10.format(item.getQ_end_time()));
                holder.relative_root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, LotteryDetailActivity.class);
                        intent.putExtra("act_id", item.getQ_uid());
                        intent.putExtra("isLottery", false);
                        intent.putExtra("isBuy", item.isIf_lock());
                        activity.startActivity(intent);
                    }
                });
            } else {
//        else if (item.getState_desc().equals("1")) {
                //揭晓中

                holder.relative_done.setVisibility(View.VISIBLE);
                holder.linear_undo.setVisibility(View.GONE);
//            if (System.currentTimeMillis() < item.getQ_end_time())
//                holder.t_count_time.setTime(item.getQ_end_time()-System.currentTimeMillis());
                holder.t_count_time.setTime(item.getQ_showtime()-System.currentTimeMillis());

                holder.relative_root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, LotteryDetailActivity.class);
                        intent.putExtra("act_id", item.getQ_uid());
                        intent.putExtra("isLottery", true);
                        activity.startActivity(intent);
                    }
                });
//                holder.t_count_time.setTime(20*1000);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }
}
