package com.abcs.haiwaigou.yyg.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.fragment.viewholder.HWGFragmentViewHolder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.yyg.adapter.viewholder.YYGBuyRecordViewHolder;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/2/26.
 */
public class YYGBuyRecordAdapter extends RecyclerView.Adapter<YYGBuyRecordViewHolder> {

    ArrayList<Goods> goods;
    Context context;
    Activity activity;
    private SortedList<Goods> mSortedList;
    public ArrayList<Goods> getDatas() {
        return goods;
    }

    HWGFragmentViewHolder.ItemOnClick itemOnClick;
    public Handler handler=new Handler();

    public YYGBuyRecordAdapter(final Activity activity) {
        this.activity=activity;
        this.goods = new ArrayList<>();
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


    public  SortedList<Goods> getList(){
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

    private static final int TYPE_HEADER = 0, TYPE_ITEM = 1, TYPE_FOOT = 2;
    private View headView;
    private View footView;
    private int headViewSize = 0;
    private int footViewSize = 0;
    private ChangeGridLayoutManagerSpance changeGridLayoutManager;
    private boolean isAddFoot=false;
    private boolean isAddHead=false;


    public interface ChangeGridLayoutManagerSpance{
        public void change(int size, boolean isAddHead, boolean isAddFoot);
    }
    //提供接口给 让LayoutManager根据添加尾部 头部与否来做判断 显示头部与底部的SpanSize要在添加头部和尾部之后
    public void setChangeGridLayoutManager(ChangeGridLayoutManagerSpance changeGridLayoutManager){
        this.changeGridLayoutManager=changeGridLayoutManager;
        changeGridLayoutManager.change(getItemCount()-1,isAddHead,isAddFoot);
    }

    public void addHeadView(View view) {
        headView = view;
        headViewSize = 1;
        isAddHead=true;
    }

    public void addFootView(View view) {
        footView = view;
        footViewSize = 1;
        isAddFoot=true;
    }

    @Override
    public int getItemViewType(int position) {
        int type = TYPE_ITEM;

        if (headViewSize==1 && position == 0) {
            type = TYPE_HEADER;
        } else if (footViewSize==1 && position == getItemCount()-1) {
            //最后一个位置
            type = TYPE_FOOT;
        }
        return type;
    }


    @Override
    public YYGBuyRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        YYGBuyRecordViewHolder hwgFragmentViewHolder =null;
        switch (viewType) {
            case TYPE_HEADER:
                view = headView;
                hwgFragmentViewHolder=new YYGBuyRecordViewHolder(view);
                break;

            case TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_yyg_item_buy_record, parent, false);
                hwgFragmentViewHolder = new YYGBuyRecordViewHolder(view);
                break;

            case TYPE_FOOT:
                view =footView;
                hwgFragmentViewHolder=new YYGBuyRecordViewHolder(view);
                break;
        }
        return hwgFragmentViewHolder;
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_hot_goods, parent, false);
//        HotGoodsRecyclerViewHolder hwgFragmentViewHolder = new HotGoodsRecyclerViewHolder(view, itemOnClick);
//        this.context = parent.getContext();
//        return hwgFragmentViewHolder;
    }

    @Override
    public void onBindViewHolder(final YYGBuyRecordViewHolder holder,  int position) {
        switch (getItemViewType(position)){
            case TYPE_HEADER:
                break;
            case TYPE_ITEM:
                final Goods item = mSortedList.get(position);
                holder.t_name.setText(item.getTitle());
                holder.t_cishu.setText(item.getGoods_num()+"");
                ImageLoader.getInstance().displayImage(item.getPicarr(), holder.img_user, Options.getUserHeadOptions());
                holder.t_ip.setText("IP:"+item.getQ_uid());
                holder.t_time.setText(Util.format.format(item.getTime()));
                break;
            case TYPE_FOOT:
                break;
        }


    }

    @Override
    public int getItemCount() {
        return mSortedList.size()+footViewSize+headViewSize;
    }

}
