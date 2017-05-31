package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.abcs.haiwaigou.activity.CollectionActivity;
import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.adapter.viewholder.CollectionViewHolder;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zjz on 2016/2/29.
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionViewHolder>{

    private ArrayList<Goods> goodsList;
    Activity activity;
    LayoutInflater inflater = null;
    public Handler handler = new Handler();
    Context context;
    private SortedList<Goods> mSortedList;

    CollectionViewHolder.ItemRootOnclick itemOnClick;
    private HashMap<String, Boolean> inCartMap = new HashMap<String, Boolean>();// 用于存放选中的项
    public CollectionAdapter(Activity activity, CollectionViewHolder.ItemRootOnclick itemOnClick) {
        this.itemOnClick = itemOnClick;
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
    public CollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_collection, parent, false);
        CollectionViewHolder hwgFragmentViewHolder = new CollectionViewHolder(view, itemOnClick);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    //    String[] strings;
    @Override
    public void onBindViewHolder(CollectionViewHolder holder, final int position) {
        //图片加载
        final Goods item = mSortedList.get(position);

        holder.t_goods_name.setText(item.getTitle());
        holder.t_goods_money.setText(item.getMoney() + "");
        holder.t_goods_sales.setText(item.getGoods_salenum());

        LoadPicture loadPicture = new LoadPicture();
        final String pic=item.getPicarr();

        if(!TextUtils.isEmpty(item.getStore_id())){

            if(item.getStore_id().equals("11")){
                // 本地的商品
                holder.btn_check.setVisibility(View.INVISIBLE);
//                http://www.huaqiaobang.com/data/upload/shop/store/goods/11/11_05375327290881075.jpg
                loadPicture.initPicture(holder.img_goods_icon, TLUrl.getInstance().URL_hwg_base+"/data/upload/shop/store/goods/11/"+item.getPicarr());
            }else {
                holder.btn_check.setVisibility(View.VISIBLE);
                loadPicture.initPicture(holder.img_goods_icon, TLUrl.getInstance().URL_hwg_comment_goods +pic.charAt(0) + "/" + item.getPicarr());
            }
        }

        holder.btn_check.setOnCheckedChangeListener(null);
        Boolean isChecked = inCartMap.get(item.getFav_id());
        if (isChecked != null && isChecked) {
            holder.btn_check.setChecked(true);
        } else {
            holder.btn_check.setChecked(false);
        }
        holder.btn_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    inCartMap.put(item.getFav_id(), isChecked);
                    // 如果所有项都被选中，则点亮全选按钮
                    if (inCartMap.size() == mSortedList.size()) {
                        CollectionActivity.btnCheckAll.setOnCheckedChangeListener(null);
                        CollectionActivity.btnCheckAll.setChecked(true);
                        CollectionActivity.setOnCheckChangeListener();
                        CollectionActivity.tvDelete.setText("清空");
                    } else {
                        CollectionActivity.tvDelete.setText("删除");
                    }
                } else {
                    // 如果之前是全选状态，则取消全选状态
                    if (inCartMap.size() == mSortedList.size()) {
                        CollectionActivity.btnCheckAll.setOnCheckedChangeListener(null);
                        CollectionActivity.btnCheckAll.setChecked(false);
                        CollectionActivity.setOnCheckChangeListener();
                        CollectionActivity.tvDelete.setText("删除");
                    }
                    inCartMap.remove(item.getFav_id());
                }
                notifyCheckedChanged();
            }
        });


        holder.t_goods_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, GoodsDetailActivity.class);
                intent.putExtra("sid", item.getGoods_id());
                intent.putExtra("pic", TLUrl.getInstance().URL_hwg_comment_goods + pic.charAt(0)+ "/" + item.getPicarr());
                activity.startActivity(intent);
            }
        });
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PromptDialog(activity, "确认删除该收藏？", new Complete() {
                    @Override
                    public void complete() {
                        ProgressDlgUtil.showProgressDlg("Loading...", activity);
                        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_favorite_del, "fav_id=" + item.getFav_id() + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
                            @Override
                            public void revMsg(final String msg) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject object = new JSONObject(msg);
                                            if (object.getInt("code") == 200) {
                                                Log.i("zjz", "msg=" + msg);
                                                ProgressDlgUtil.stopProgressDlg();
                                                MyUpdateUI.sendUpdateCollection(activity, MyUpdateUI.COLLECTTION);
                                            } else {
                                                ProgressDlgUtil.stopProgressDlg();
                                                Log.i("zjz", "goodsDetail:解析失败");
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
                }).show();

            }
        });

    }

    private void notifyCheckedChanged() {
        CollectionActivity.favId.clear();
        for (int i = 0; i < mSortedList.size(); i++) {
            Boolean isChecked = inCartMap.get(mSortedList.get(i).getFav_id());
            if (isChecked != null && isChecked) {
                Goods goods = mSortedList.get(i);
                CollectionActivity.favId.add(goods.getFav_id());
            }
        }
    }
    public void checkAll() {
        CollectionActivity.favId.clear();
        for (int i = 0; i < mSortedList.size(); i++) {
            inCartMap.put(mSortedList.get(i).getFav_id(), true);
            CollectionActivity.favId.add(mSortedList.get(i).getFav_id());
        }
        notifyDataSetChanged();
    }

    public void cancelAll(){
        inCartMap.clear();
        CollectionActivity.favId.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }

}
