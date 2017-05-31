package com.abcs.haiwaigou.local.adapter;

import android.app.Activity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.local.beans.Msg;
import com.abcs.haiwaigou.local.viewholder.MessageViewHolder;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/9/6.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    Activity activity;
    MessageViewHolder.RootOnClick rootOnClick;
    public ArrayList<Msg> newsList;
    private SortedList<Msg> mSortedList;

    public MessageAdapter(Activity activity, MessageViewHolder.RootOnClick rootOnClick) {
        this.newsList = new ArrayList<Msg>();
        this.activity = activity;
        this.rootOnClick = rootOnClick;
        mSortedList = new SortedList<>(Msg.class, new SortedList.Callback<Msg>() {

            /**
             * 返回一个负整数（第一个参数小于第二个）、零（相等）或正整数（第一个参数大于第二个）
             */
            @Override
            public int compare(Msg o1, Msg o2) {

                if (o1.getIds() < o2.getIds()) {
                    return -1;
                } else if (o1.getIds() > o2.getIds()) {
                    return 1;
                }

                return 0;
            }

            @Override
            public boolean areContentsTheSame(Msg oldItem, Msg newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areItemsTheSame(Msg item1, Msg item2) {
                return item1.getId().equals(item2.getId());
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

    public SortedList<Msg> getList() {
        return mSortedList;
    }

    public void addItems(ArrayList<Msg> list) {
        mSortedList.beginBatchedUpdates();

        for (Msg itemModel : list) {
            mSortedList.add(itemModel);
        }

        mSortedList.endBatchedUpdates();
    }

    public void deleteItems(ArrayList<Msg> items) {
        mSortedList.beginBatchedUpdates();
        for (Msg item : items) {
            mSortedList.remove(item);
        }
        mSortedList.endBatchedUpdates();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_item_msg_more, parent, false);
        MessageViewHolder currentViewHolder = new MessageViewHolder(view, rootOnClick);
        return currentViewHolder;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Msg msg = mSortedList.get(position);
        if (!TextUtils.isEmpty(msg.getImgUrl())) {
            ImageLoader.getInstance().displayImage(msg.getImgUrl(), holder.img_icon, Options.getCornerListOptions());
            holder.t_text.setVisibility(View.GONE);
        } else {

            if(!TextUtils.isEmpty(msg.getTypeName())){
                if(msg.getTypeName().contains("招工")){
                    holder.img_icon.setImageResource(R.drawable.img_local_gongzuo);
                }else if(msg.getTypeName().contains("找工作")){
                    holder.img_icon.setImageResource(R.drawable.img_local_gongzuo);
                }else if(msg.getTypeName().contains("出租")){
                    holder.img_icon.setImageResource(R.drawable.img_local_fangwu);
                }else if(msg.getTypeName().contains("其他")){
                    holder.img_icon.setImageResource(R.drawable.img_local_genduo);
                }else if(msg.getTypeName().contains("生意")){
                    holder.img_icon.setImageResource(R.drawable.img_local_shengyi);
                }else {
                    holder.img_icon.setImageResource(R.drawable.img_local_huangye);
                }
            }


//            holder.img_icon.setImageResource(R.drawable.img_local_default);
            if(!TextUtils.isEmpty(msg.getTitle())){
                holder.t_text.setText(msg.getTitle().substring(0, 1));
                holder.t_text.setVisibility(View.VISIBLE);
            }
        }

//        img_icon.setImageBitmap(CreateBitmap.create(msg.getTypeName().substring(0, 1),activity));
        holder.t_title.setText(msg.getTitle());
        holder.t_address.setText(msg.getCountry() + " " + msg.getCityName());

        if (msg.getCreateTime() < 2 * 1000000000) {
            holder.t_time.setText(Util.format1.format(msg.getCreateTime()*1000));
        } else {
            holder.t_time.setText(Util.format1.format(msg.getCreateTime()));
        }
        holder.t_tips.setVisibility(TextUtils.isEmpty(msg.getTypeName()) ? View.GONE : View.VISIBLE);
        holder.t_tips.setText(msg.getTypeName());
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }
}
