package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.adapter.viewholder.CollectionViewHolder;
import com.abcs.haiwaigou.adapter.viewholder.RedBagDetailViewHolder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/2/29.
 */
public class RedBagDetailAdapter extends RecyclerView.Adapter<RedBagDetailViewHolder> {

    private ArrayList<Goods> goodsList;
    Activity activity;
    LayoutInflater inflater = null;
    public Handler handler = new Handler();
    Context context;
    private SortedList<Goods> mSortedList;

    CollectionViewHolder.ItemRootOnclick itemOnClick;

    public RedBagDetailAdapter(Activity activity) {
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
    public RedBagDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_redbag_detail, parent, false);
        RedBagDetailViewHolder hwgFragmentViewHolder = new RedBagDetailViewHolder(view);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    //    String[] strings;
    @Override
    public void onBindViewHolder(RedBagDetailViewHolder holder, final int position) {
        Goods item = mSortedList.get(position);
        String userhead = TLUrl.getInstance().URL_hwg_base+"/data/upload/shop/avatar/avatar_";
//        loadPicture.initPicture(holder.img_user, userhead+ item.getGeval_frommemberid()+".jpg");
        ImageLoader.getInstance().displayImage(userhead + item.getBuyer_id() + ".jpg", holder.img_user, Options.getUserHeadOptions());
        if (item.getState_desc().equals("1")) {
            holder.t_price.setText(item.getMoney() + "元优惠券");
        } else if (item.getState_desc().equals("2")) {
            holder.t_price.setText(item.getMoney() + "元");
        }
        String name=item.getBuyer_name();
        if (item.getBuyer_name().equals(MyApplication.getInstance().self.getUserName())) {
            if(name.length()>20){
                holder.t_name.setText(name.substring(0, 2)+"***"+name.substring(name.length()-2,name.length()));
            }else {
                holder.t_name.setText(name);
            }
            holder.img_my.setVisibility(View.VISIBLE);
            holder.t_price.setTextColor(Color.parseColor("#eb4456"));
        } else {
            holder.t_name.setText(item.getBuyer_name());
            holder.img_my.setVisibility(View.GONE);
            holder.t_price.setTextColor(activity.getResources().getColor(R.color.hwg_text1));
        }
//        if(name.length()>5){
//            holder.t_name.setText(name.substring(0, 2)+"***"+name.substring(name.length()-1,name.length()));
//        }else {
//            holder.t_name.setText(item.getBuyer_name());
//        }

        holder.t_time.setText(Util.format.format(item.getTime()*1000));
    }


    @Override
    public int getItemCount() {
        return mSortedList.size();
    }

}
