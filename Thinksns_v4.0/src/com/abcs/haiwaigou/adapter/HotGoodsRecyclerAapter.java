package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.abcs.haiwaigou.activity.HotActivity;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.viewholder.HWGFragmentViewHolder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.utils.MyViewAnimUtil;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.ShengJiHuiYuanActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/2/26.
 */
public class HotGoodsRecyclerAapter extends RecyclerView.Adapter<HWGFragmentViewHolder> {

    ArrayList<Goods> goods;
    Context context;
    Activity activity;
    private SortedList<Goods> mSortedList;
    public ArrayList<Goods> getDatas() {
        return goods;
    }

    HWGFragmentViewHolder.ItemOnClick itemOnClick;
    MyViewAnimUtil myViewAnimUtil;
    public Handler handler=new Handler();
    FrameLayout animation_viewGroup;

    public HotGoodsRecyclerAapter(final Activity activity, HWGFragmentViewHolder.ItemOnClick itemOnClick) {
        myViewAnimUtil = new MyViewAnimUtil(activity, animation_viewGroup);
        SetOnSetHolderClickListener(new HolderClickListener() {
            @Override
            public void onHolderClick(Drawable drawable, int[] start_location) {
                myViewAnimUtil.doAnim(drawable, start_location, HotActivity.img_cart);
            }
        });
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
    public HWGFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        HWGFragmentViewHolder hwgFragmentViewHolder =null;
        switch (viewType) {
            case TYPE_HEADER:
                view = headView;
                hwgFragmentViewHolder=new HWGFragmentViewHolder(view);
                break;

            case TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_country_goods, parent, false);
                hwgFragmentViewHolder = new HWGFragmentViewHolder(view, itemOnClick);
                break;

            case TYPE_FOOT:
                view =footView;
                hwgFragmentViewHolder=new HWGFragmentViewHolder(view);
                break;
        }
        return hwgFragmentViewHolder;
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_hot_goods, parent, false);
//        HotGoodsRecyclerViewHolder hwgFragmentViewHolder = new HotGoodsRecyclerViewHolder(view, itemOnClick);
//        this.context = parent.getContext();
//        return hwgFragmentViewHolder;
    }

private void addGoods(String goodId){
    ProgressDlgUtil.showProgressDlg("Loading...", activity);
    Log.i("zjz", "add2cart_key=" + MyApplication.getInstance().getMykey());

    HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_add_to_cart, "key=" + MyApplication.getInstance().getMykey() + "&goods_id=" +goodId + "&quantity=1", new HttpRevMsg() {
        @Override
        public void revMsg(final String msg) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject object = new JSONObject(msg);
                        if (object.getInt("code") == 200) {
                            Log.i("zjz", "hot_add_cart=" + msg);
                            //更新购物车数量
                            if (object.optString("datas").equals("1")) {
                                MyUpdateUI.sendUpdateCarNum(activity);
                                Toast.makeText(activity,"添加成功！",Toast.LENGTH_SHORT).show();
                            } else if (object.optString("datas").contains("error")) {
                                Toast.makeText(activity,object.getJSONObject("datas").optString("error"),Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        Log.i("zjz", e.toString());
                        Log.i("zjz", msg);
                        e.printStackTrace();
                        ProgressDlgUtil.stopProgressDlg();
                    } finally {
                        ProgressDlgUtil.stopProgressDlg();
                    }
                }
            });

        }
    });
}

//    private boolean isMai=false;
    private void confirmData(final String goodId){


        Log.i("zds_goodId",goodId+"");
//        goods_id=101146&key=939f6c2c1ad7199187be733cc714955a
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_comfirm_isSale, "key=" + MyApplication.getInstance().getMykey() + "&goods_id=" + goodId , new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(!TextUtils.isEmpty(msg)){

//                                {"state":0}
                                JSONObject object = new JSONObject(msg);   //更新购物车数量   state  为1表示可以购买，为0表示不允许购买
                                if (object.optString("state").equals("1")) {
//                                    isMai=true;
                                    addGoods(goodId);
                                }else if(object.optString("state").equals("0")){
//                                    isMai=false;
                                    activity.startActivity(new Intent(activity, ShengJiHuiYuanActivity.class));
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
                            ProgressDlgUtil.stopProgressDlg();
                        } finally {
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });
//        return isMai;
    }

    @Override
    public void onBindViewHolder(final HWGFragmentViewHolder holder,  int position) {
        switch (getItemViewType(position)){
            case TYPE_HEADER:
                break;
            case TYPE_ITEM:
                final Goods item = mSortedList.get(position-1);
                LoadPicture loadPicture = new LoadPicture();
                loadPicture.initPicture(holder.img_goods_icon, item.getPicarr());
                if(item.getXiangou()==1){
                    holder.img_xianshi.setVisibility(View.VISIBLE);
                }else {
                    holder.img_xianshi.setVisibility(View.GONE);
                }

                holder.t_goods_money.setText(NumberUtils.formatPrice(item.getMoney()));

                        if(item.getGoods_markeprice()>0||item.getPromote_money()>0){
                            holder.t_y_goods_money.setVisibility(View.VISIBLE);
                            if(item.getPromote_money()==item.getMoney()){  /*****促销价和原来相等*******/
                                holder.t_y_goods_money.setText(NumberUtils.formatPrice(item.getGoods_markeprice()));
                                //添加删除线
                                holder.t_y_goods_money.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            }else {
                                holder.t_y_goods_money.setText(NumberUtils.formatPrice(item.getPromote_money()));
                                //添加删除线
                                holder.t_y_goods_money.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            }
                        }else {
                            holder.t_y_goods_money.setVisibility(View.GONE);
                        }

                holder.t_goods_name.setText(item.getTitle());
                holder.img_buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        int[] start_location = new int[2];
//                        holder.img_goods_icon.getLocationInWindow(start_location);//获取点击商品图片的位置
//
//                        Drawable drawable =  holder.img_goods_icon.getDrawable();//复制一个新的商品图标
//                        mHolderClickListener.onHolderClick(drawable, start_location);
                        if (MyApplication.getInstance().self == null) {
                            Intent intent = new Intent(activity, WXEntryActivity.class);
                            activity.startActivity(intent);
                            return;
                        }

                        confirmData(item.getGoods_id());  /*******验证商品是否可以买*******/
                    }
                });
                break;
            case TYPE_FOOT:
                break;
        }


    }

    @Override
    public int getItemCount() {
        return mSortedList.size()+footViewSize+headViewSize;
    }


    private HolderClickListener mHolderClickListener;

    public void SetOnSetHolderClickListener(HolderClickListener holderClickListener) {
        this.mHolderClickListener = holderClickListener;
    }


    public interface HolderClickListener {
        public void onHolderClick(Drawable drawable, int[] start_location);
    }

}
