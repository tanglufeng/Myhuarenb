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
import android.widget.Toast;

import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.yyg.activity.YYGBuyActivity;
import com.abcs.haiwaigou.yyg.activity.YYGGoodsDetailActivity;
import com.abcs.haiwaigou.yyg.adapter.viewholder.YYGGoodsFragmentViewHolder;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/7/2 0002.
 */
public class YYGGoodsFragmentAdapter extends RecyclerView.Adapter<YYGGoodsFragmentViewHolder>{
    private ArrayList<Goods> goodsList;
    Activity activity;
    public Handler handler = new Handler();
    Context context;
    private SortedList<Goods> mSortedList;


    public YYGGoodsFragmentAdapter(Activity activity) {
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
    public YYGGoodsFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_yyg_item_goods, parent, false);
        YYGGoodsFragmentViewHolder hwgFragmentViewHolder = new YYGGoodsFragmentViewHolder(view);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    //    String[] strings;
    @Override
    public void onBindViewHolder(YYGGoodsFragmentViewHolder holder, final int position) {
        final Goods item = mSortedList.get(position);
        ImageLoader.getInstance().displayImage(item.getPicarr(),holder.img_icon, Options.getHDOptions());
        holder.t_name.setText("第(" + item.getQishu() + ")期 " + item.getTitle());
        int pro = (int) (Float.valueOf(item.getCanyurenshu())
                / Float.valueOf(item.getZongrenshu()) * 100);
        holder.goods_prograss.setProgress(pro);
        holder.t_total_money.setText("¥" + item.getTotal_money());
        holder.t_all_need.setText("总需" + item.getZongrenshu() + "人次");
        holder.t_remain.setText(item.getShenyurenshu() + "");
        holder.img_isbuy.setVisibility(item.isIf_lock() ? View.VISIBLE : View.GONE);
        holder.linear_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, YYGGoodsDetailActivity.class);
                intent.putExtra("act_id",item.getQ_uid());
                intent.putExtra("img_icon",item.getPicarr());
                intent.putExtra("title","第(" + item.getQishu() + ")期 " + item.getTitle());
                intent.putExtra("all_need",item.getZongrenshu());
                intent.putExtra("remain_need",item.getShenyurenshu());
                intent.putExtra("price_one",item.getYunjiage());
                intent.putExtra("total_money",item.getTotal_money());
                intent.putExtra("isBuy",item.isIf_lock());
                activity.startActivity(intent);
            }
        });
        holder.img_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.getInstance().self==null){
                    activity.startActivity(new Intent(activity, WXEntryActivity.class));
                    return;
                }else if(item.getShenyurenshu()==0){
                    Toast.makeText(activity,"剩余人数为0，无法参与！",Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent=new Intent(activity,YYGBuyActivity.class);
                intent.putExtra("act_id",item.getQ_uid());
                intent.putExtra("img_icon",item.getPicarr());
                intent.putExtra("title","第(" + item.getQishu() + ")期 " + item.getTitle());
                intent.putExtra("all_need",item.getZongrenshu());
                intent.putExtra("remain_need",item.getShenyurenshu());
                intent.putExtra("price_one",item.getYunjiage());
                activity.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mSortedList.size();
    }
}
