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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.yyg.activity.LotteryDetailActivity;
import com.abcs.haiwaigou.yyg.activity.YYGGoodsDetailActivity;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/7/2 0002.
 */
public class YYGMyBuyRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int NOMAL=0;
    public static final int JIEXIAOING=1;
    public static final int JIEXIAOED=2;
    private ArrayList<Goods> goodsList;
    Activity activity;
    public Handler handler = new Handler();
    Context context;
    private SortedList<Goods> mSortedList;


    public YYGMyBuyRecordAdapter(Activity activity) {
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
    public int getItemViewType(int position) {
        int type=NOMAL;
        if(mSortedList.get(position).getLayoutType()==-1){
            type=NOMAL;
        }else if(mSortedList.get(position).getLayoutType()==0){
            type=JIEXIAOED;
        }else if(mSortedList.get(position).getLayoutType()==1){
            type=JIEXIAOING;
        }
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        switch (viewType){
//            case NOMAL:
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_yyg_item_my_buy_record, parent, false);
//                YYGMyBuyRecordViewHolder hwgFragmentViewHolder = new YYGMyBuyRecordViewHolder(view);
//                this.context = parent.getContext();
//                return hwgFragmentViewHolder;
//                break;
//        }
        if(viewType==NOMAL){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_yyg_item_my_buy_record, parent, false);
            YYGMyBuyRecordViewHolder yygMyBuyRecordViewHolder = new YYGMyBuyRecordViewHolder(view);
            return yygMyBuyRecordViewHolder;
        }else if(viewType==JIEXIAOED){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_yyg_item_my_lottery_record, parent, false);
            YYGMyLotteryRecordViewHolder yygMyLotteryRecordViewHolder = new YYGMyLotteryRecordViewHolder(view);
            return yygMyLotteryRecordViewHolder;
        }else if(viewType==JIEXIAOING){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_yyg_item_my_lottery_record, parent, false);
            YYGMyLotteryRecordViewHolder hwgFragmentViewHolder = new YYGMyLotteryRecordViewHolder(view);
            return hwgFragmentViewHolder;
        }

        return null;
    }

    //    String[] strings;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Goods item = mSortedList.get(position);
        if(holder instanceof YYGMyBuyRecordViewHolder){
            YYGMyBuyRecordViewHolder yygMyBuyRecordViewHolder= (YYGMyBuyRecordViewHolder) holder;
            ImageLoader.getInstance().displayImage(item.getPicarr(),yygMyBuyRecordViewHolder.img_icon, Options.getHDOptions());
            yygMyBuyRecordViewHolder.t_title.setText("第("+item.getQishu()+")期 "+item.getTitle());
            int pro = (int) (Float.valueOf(item.getCanyurenshu())
                    / Float.valueOf(item.getZongrenshu()) * 100);
            yygMyBuyRecordViewHolder.goods_prograss.setProgress(pro);
            yygMyBuyRecordViewHolder.t_money.setText("¥" + item.getTotal_money());
            yygMyBuyRecordViewHolder.t_all_need.setText("总需" + item.getZongrenshu() + "人次");
            yygMyBuyRecordViewHolder.t_canyu.setText(item.getCanyurenshu()+"参与");
            yygMyBuyRecordViewHolder.t_remain.setText("剩余"+item.getShenyurenshu());
            yygMyBuyRecordViewHolder.linear_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, YYGGoodsDetailActivity.class);
                    intent.putExtra("act_id", item.getQ_uid());
                    intent.putExtra("img_icon", item.getPicarr());
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("all_need", item.getZongrenshu());
                    intent.putExtra("remain_need", item.getShenyurenshu());
                    intent.putExtra("price_one", item.getYunjiage());
                    intent.putExtra("total_money", item.getTotal_money());
                    intent.putExtra("isBuy", true);
                    activity.startActivity(intent);
                }
            });
        }else if(holder instanceof YYGMyLotteryRecordViewHolder){
            YYGMyLotteryRecordViewHolder yygMyLotteryRecordViewHolder = (YYGMyLotteryRecordViewHolder) holder;
            ImageLoader.getInstance().displayImage(item.getPicarr(), yygMyLotteryRecordViewHolder.img_icon, Options.getHDOptions());
            yygMyLotteryRecordViewHolder.t_title.setText("第("+item.getQishu()+")期 "+item.getTitle());
            yygMyLotteryRecordViewHolder.t_money.setText("¥" + item.getTotal_money());
            yygMyLotteryRecordViewHolder.t_all_need.setText("总需" + item.getZongrenshu() + "人次");
            if(item.getQ_end_time().equals("")){
                yygMyLotteryRecordViewHolder.t_time.setText("????");
            }else {
                yygMyLotteryRecordViewHolder.t_time.setText(Util.format.format(item.getQ_end_time()));
            }
            if(item.getQ_user_code().equals("")){
                yygMyLotteryRecordViewHolder.t_luckyNum.setText("????");
            }else {
                yygMyLotteryRecordViewHolder.t_luckyNum.setText(item.getQ_user_code());
            }
            if(item.getGoods_salenum().equals("")){
                yygMyLotteryRecordViewHolder.t_canyu.setText("????");
            }else {
                yygMyLotteryRecordViewHolder.t_canyu.setText(item.getGoods_salenum()+"人次");
            }

            yygMyLotteryRecordViewHolder.linear_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, LotteryDetailActivity.class);
                    intent.putExtra("act_id", item.getQ_uid());
                    intent.putExtra("isLottery", false);
                    intent.putExtra("isBuy", true);
                    activity.startActivity(intent);
                }
            });

            if(item.getCid().equals(MyApplication.getInstance().self.getId())){
                yygMyLotteryRecordViewHolder.img_isluck.setVisibility(View.VISIBLE);
            }else {
                yygMyLotteryRecordViewHolder.img_isluck.setVisibility(View.GONE);
            }
            if(item.getState_desc().equals("1")&&item.getCid().equals(MyApplication.getInstance().self.getId())){
                yygMyLotteryRecordViewHolder.linear_state.setVisibility(View.VISIBLE);
                if(item.getGoods_state().equals("0")){
                    yygMyLotteryRecordViewHolder.t_state.setText("未操作");
                    yygMyLotteryRecordViewHolder.img_state.setVisibility(View.GONE);
                }else if(item.getGoods_state().equals("9")){
                    yygMyLotteryRecordViewHolder.t_state.setText("待发货");
                    yygMyLotteryRecordViewHolder.img_state.setVisibility(View.VISIBLE);
                }else if(item.getGoods_state().equals("-1")){
                    yygMyLotteryRecordViewHolder.t_state.setText("已折现");
                    yygMyLotteryRecordViewHolder.img_state.setVisibility(View.VISIBLE);
                }else if (item.getGoods_state().equals("2")){
                    yygMyLotteryRecordViewHolder.t_state.setText("已签收");
                    yygMyLotteryRecordViewHolder.img_state.setVisibility(View.VISIBLE);
                }else if(item.getGoods_state().equals("1")){
                    yygMyLotteryRecordViewHolder.t_state.setText("待签收");
                    yygMyLotteryRecordViewHolder.img_state.setVisibility(View.VISIBLE);
                }
            }else {
                yygMyLotteryRecordViewHolder.linear_state.setVisibility(View.GONE);
                yygMyLotteryRecordViewHolder.img_state.setVisibility(View.GONE);
            }
        }


    }


    @Override
    public int getItemCount() {
        return mSortedList.size();
    }

    public static  class YYGMyBuyRecordViewHolder extends RecyclerView.ViewHolder{

        public ImageView img_icon;
        public TextView t_title;
        public TextView t_all_need;
        public TextView t_remain;
        public TextView t_canyu;
        public TextView t_money;
        public ProgressBar goods_prograss;
        public LinearLayout linear_root;
        public YYGMyBuyRecordViewHolder(View itemView) {
            super(itemView);
            img_icon= (ImageView) itemView.findViewById(R.id.img_icon);
            t_title= (TextView) itemView.findViewById(R.id.t_title);
            t_all_need= (TextView) itemView.findViewById(R.id.t_all_need);
            t_remain= (TextView) itemView.findViewById(R.id.t_remain);
            t_canyu= (TextView) itemView.findViewById(R.id.t_canyu);
            t_money= (TextView) itemView.findViewById(R.id.t_money);
            goods_prograss= (ProgressBar) itemView.findViewById(R.id.goods_prograss);
            linear_root= (LinearLayout) itemView.findViewById(R.id.linear_root);
        }
    }

    public static class YYGMyLotteryRecordViewHolder extends RecyclerView.ViewHolder{

        public ImageView img_icon;
        public TextView t_title;
        public TextView t_all_need;
        public TextView t_luckyNum;
        public TextView t_time;
        public TextView t_canyu;
        public TextView t_money;
        public TextView t_state;
        public LinearLayout linear_root;
        public LinearLayout linear_state;
        public ImageView img_state;
        public ImageView img_isluck;
        public YYGMyLotteryRecordViewHolder(View itemView) {
            super(itemView);
            img_icon= (ImageView) itemView.findViewById(R.id.img_icon);
            img_state= (ImageView) itemView.findViewById(R.id.img_state);
            img_isluck= (ImageView) itemView.findViewById(R.id.img_isluck);
            t_title= (TextView) itemView.findViewById(R.id.t_title);
            t_all_need= (TextView) itemView.findViewById(R.id.t_all_need);
            t_luckyNum= (TextView) itemView.findViewById(R.id.t_luckyNum);
            t_time= (TextView) itemView.findViewById(R.id.t_time);
            t_canyu= (TextView) itemView.findViewById(R.id.t_canyu);
            t_money= (TextView) itemView.findViewById(R.id.t_money);
            t_state= (TextView) itemView.findViewById(R.id.t_state);
            linear_root= (LinearLayout) itemView.findViewById(R.id.linear_root);
            linear_state= (LinearLayout) itemView.findViewById(R.id.linear_state);
        }
    }

}
