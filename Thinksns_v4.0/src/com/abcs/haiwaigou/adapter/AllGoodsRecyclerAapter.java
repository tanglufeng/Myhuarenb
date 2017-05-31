package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abcs.haiwaigou.adapter.viewholder.AllGoodsRecyclerViewHolder;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/2/26.
 */
public class AllGoodsRecyclerAapter extends RecyclerView.Adapter<AllGoodsRecyclerViewHolder> {

    ArrayList<Goods> goods;
    Context context;
    Activity activity;
    private SortedList<Goods> mSortedList;
    public ArrayList<Goods> getDatas() {
        return goods;
    }

    AllGoodsRecyclerViewHolder.ItemOnClick itemOnClick;

    public Handler handler=new Handler();
    public AllGoodsRecyclerAapter(Activity activity,AllGoodsRecyclerViewHolder.ItemOnClick itemOnClick) {
        this.activity=activity;
        this.itemOnClick = itemOnClick;
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

    @Override
    public AllGoodsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_hot_goods, parent, false);
        AllGoodsRecyclerViewHolder hwgFragmentViewHolder = new AllGoodsRecyclerViewHolder(view, itemOnClick);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    @Override
    public void onBindViewHolder(AllGoodsRecyclerViewHolder holder, final int position) {
        //图片加载
        final Goods item = mSortedList.get(position);

//        ViewHolder viewHolder = (ViewHolder) holder;
//        LoadPicture loadPicture = new LoadPicture();
//        loadPicture.initPicture(viewHolder.img_goods_icon, item.getPicarr());
//        viewHolder.t_goods_money.setText(NumberUtils.formatPrice(item.getMoney()));
//        viewHolder.t_goods_name.setText(item.getTitle());
//        viewHolder.t_goods_money.setText(item.getMoney() + "");
//        viewHolder.t_goods_count.setText(item.getGoods_salenum());

        LoadPicture loadPicture = new LoadPicture();
        loadPicture.initPicture(holder.img_goods_icon, item.getPicarr());
        holder.t_goods_money.setText(NumberUtils.formatPrice(item.getMoney()));
        holder.t_goods_name.setText(item.getTitle());
        holder.t_goods_money.setText(item.getMoney() + "");
        holder.t_goods_count.setText(item.getGoods_salenum());
        if(item.getXiangou()==1){
            holder.t_desc.setVisibility(View.VISIBLE);
        }else {
            holder.t_desc.setVisibility(View.GONE);
        }
        holder.img_btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getInstance().self == null) {
                    Intent intent = new Intent(activity, WXEntryActivity.class);
                    activity.startActivity(intent);
                    return;
                }
                if (MyApplication.getInstance().getMykey() == null) {
                    Intent intent = new Intent(activity, WXEntryActivity.class);
                    activity.startActivity(intent);
                    return;
                }
                int[] start_location = new int[2];

                ProgressDlgUtil.showProgressDlg("Loading...", activity);
                HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_add_to_cart, "key=" + MyApplication.getInstance().getMykey() + "&goods_id=" + item.getGoods_id() + "&quantity=1", new HttpRevMsg() {
                    @Override
                    public void revMsg(final String msg) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject object = new JSONObject(msg);
                                    if (object.getInt("code") == 200) {
                                        //更新购物车数量
                                        MyUpdateUI.sendUpdateCarNum(activity);
                                        ProgressDlgUtil.stopProgressDlg();
                                        Toast.makeText(activity,"添加成功！",Toast.LENGTH_SHORT).show();
                                        Log.i("zjz", "add:添加成功");
                                    } else {
                                        ProgressDlgUtil.stopProgressDlg();
                                        Log.i("zjz", "add:解析失败");
                                    }
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    Log.i("zjz", e.toString());
                                    Log.i("zjz", msg);
                                    e.printStackTrace();
                                    ProgressDlgUtil.stopProgressDlg();
                                }
                            }
                        });

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }
}
