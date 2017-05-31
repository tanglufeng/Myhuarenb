package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.activity.RedBagDetailActivity;
import com.abcs.haiwaigou.adapter.viewholder.CollectionViewHolder;
import com.abcs.haiwaigou.adapter.viewholder.MyRedBagViewHolder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.util.Util;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/2/29.
 */
public class MyRedBagAdapter extends RecyclerView.Adapter<MyRedBagViewHolder> {

    private ArrayList<Goods> goodsList;
    Activity activity;
    LayoutInflater inflater = null;
    public Handler handler = new Handler();
    Context context;
    private SortedList<Goods> mSortedList;

    CollectionViewHolder.ItemRootOnclick itemOnClick;

    public MyRedBagAdapter(Activity activity) {
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
    public MyRedBagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_my_redbag, parent, false);
        MyRedBagViewHolder hwgFragmentViewHolder = new MyRedBagViewHolder(view);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    //    String[] strings;
    @Override
    public void onBindViewHolder(MyRedBagViewHolder holder, final int position) {
        final Goods item = mSortedList.get(position);
        holder.t_time.setText(Util.format.format(item.getTime() * 1000));
        if (item.getState_desc().equals("1")){
            holder.t_price.setText(item.getMoney()+"元优惠券");
        }else if(item.getState_desc().equals("2")){
            holder.t_price.setText(item.getMoney()+"元");
        }
        holder.relative_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RedBagDetailActivity.class);
                intent.putExtra("keyword", item.getKeywords());
                activity.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mSortedList.size();
    }

}
