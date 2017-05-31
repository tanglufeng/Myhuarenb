package com.abcs.haiwaigou.local.fragment.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.local.beans.HireFind;
import com.abcs.haiwaigou.local.fragment.viewholder.HireJobViewHolder;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zjz on 2016/9/6.
 */
public class HireJobAdapter extends RecyclerView.Adapter<HireJobViewHolder> {
    Activity activity;
    HireJobViewHolder.RootOnClick rootOnClick;
    public ArrayList<HireFind> hireFinds;
    private SortedList<HireFind> mSortedList;
    String oLd_menuId;
    public Handler handler = new Handler();

    private HashMap<String, Boolean> inPublishMap = new HashMap<String, Boolean>();   // 用于存放选中的项
    public HireJobAdapter(Activity activity, HireJobViewHolder.RootOnClick rootOnClick,String oLd_menuId) {
        this.hireFinds=new ArrayList<HireFind>();
        this.oLd_menuId = oLd_menuId;
        this.activity = activity;
        this.rootOnClick = rootOnClick;
        mSortedList = new SortedList<>(HireFind.class, new SortedList.Callback<HireFind>() {

            /**
             * 返回一个负整数（第一个参数小于第二个）、零（相等）或正整数（第一个参数大于第二个）
             */
            @Override
            public int compare(HireFind o1, HireFind o2) {

                if (o1.getIds()<o2.getIds()) {
                    return -1;
                } else if (o1.getIds()> o2.getIds()) {
                    return 1;
                }

                return 0;
            }

            @Override
            public boolean areContentsTheSame(HireFind oldItem, HireFind newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areItemsTheSame(HireFind item1, HireFind item2) {
                return item1.getId() .equals( item2.getId());
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


    public SortedList<HireFind> getList() {
        return mSortedList;
    }

    public void addItems(ArrayList<HireFind> list) {
        mSortedList.beginBatchedUpdates();

        for (HireFind itemModel : list) {
            mSortedList.add(itemModel);
        }

        mSortedList.endBatchedUpdates();
    }

    public void deleteItems(ArrayList<HireFind> items) {
        mSortedList.beginBatchedUpdates();
        for (HireFind item : items) {
            mSortedList.remove(item);
        }
        mSortedList.endBatchedUpdates();
    }

    @Override
    public HireJobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_item_hire_find_job, parent, false);
        HireJobViewHolder currentViewHolder = new HireJobViewHolder(view,rootOnClick);
        return currentViewHolder;
    }

    @Override
    public void onBindViewHolder(HireJobViewHolder holder, int position) {
        final HireFind hireFind=mSortedList.get(position);
        holder.t_title.setText(hireFind.getTitle());
        if(hireFind.getPubTime()==0){
            if(hireFind.getCreateTime()==0){
                holder.t_time.setVisibility(View.GONE);
            }else {
                if(hireFind.getCreateTime()<2*1000000000){
                    holder.t_time.setText(Util.formatzjz3.format(hireFind.getCreateTime() * 1000));
                }else {
                    holder.t_time.setText(Util.formatzjz3.format(hireFind.getCreateTime() ));
                }
            }
        }else {
            holder.t_time.setVisibility(View.VISIBLE);
            if(hireFind.getPubTime()<2*1000000000){
                holder.t_time.setText(Util.formatzjz3.format(hireFind.getPubTime() * 1000));
            }else {
                holder.t_time.setText(Util.formatzjz3.format(hireFind.getPubTime() ));
            }
        }
        if(hireFind.getIcon().equals("")){
            if(!TextUtils.isEmpty(oLd_menuId)){
                if(oLd_menuId.equals(MyString.LOCAL_MENU1)){
                    holder.img_icon.setImageResource(R.drawable.img_local_gongzuo);
                }else if(oLd_menuId.equals(MyString.LOCAL_MENU1)){
                    holder.img_icon.setImageResource(R.drawable.img_local_gongzuo);
                }else if(oLd_menuId.equals(MyString.LOCAL_MENU3)){
                    holder.img_icon.setImageResource(R.drawable.img_local_fangwu);
                }else if(oLd_menuId.equals(MyString.LOCAL_MENU4)){
                    holder.img_icon.setImageResource(R.drawable.img_local_genduo);
                }else if(oLd_menuId.equals(MyString.LOCAL_MENU2)){
                    holder.img_icon.setImageResource(R.drawable.img_local_shengyi);
                }else {
                    holder.img_icon.setImageResource(R.drawable.img_local_huangye);
                }
            }

//            holder.img_icon.setImageResource(R.drawable.img_local_default);
            if(Util.containsString(hireFind.getTitle().substring(0, 1))){
                holder.t_text.setText(hireFind.getTitle().substring(1, 2));
            }else {
                holder.t_text.setText(hireFind.getTitle().substring(0, 1));
            }

            holder.t_text.setVisibility(View.VISIBLE);

        }else {
            holder.t_text.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(hireFind.getIcon(), holder.img_icon, Options.getListOptions());
        }
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }

}
