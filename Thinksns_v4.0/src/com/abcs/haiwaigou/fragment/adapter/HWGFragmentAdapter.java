package com.abcs.haiwaigou.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.viewholder.HWGFragmentViewHolder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/1/12.
 */
public class HWGFragmentAdapter extends RecyclerView.Adapter<HWGFragmentViewHolder> {

//    ArrayList<Brand> brands;
//    Context context;
//
//    public ArrayList<Brand> getDatas() {
//        return brands;
//    }
//
//    HWGFragmentViewHolder.ItemOnClick itemOnClick;
//
//    public HWGFragmentAdapter(HWGFragmentViewHolder.ItemOnClick itemOnClick) {
//        this.itemOnClick = itemOnClick;
//        this.brands = new ArrayList<>();
//    }
//
//    @Override
//    public HWGFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_pinpai, parent, false);
//        HWGFragmentViewHolder hwgFragmentViewHolder = new HWGFragmentViewHolder(view, itemOnClick);
//        this.context = parent.getContext();
//        return hwgFragmentViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(HWGFragmentViewHolder holder, final int position) {
//        //图片加载
//        LoadPicture loadPicture=new LoadPicture();
//        loadPicture.initPicture(holder.img_pinpai,brands.get(position).getBimg());
////        holder.img_pinpai.setImageResource(R.drawable.banner02);
//        holder.t_saled.setText("150");
//        holder.t_remain.setText("50");
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return brands.size();
//    }





    ArrayList<Goods>goods;
    Context context;
    boolean isLocal;
    private Handler handler=new Handler();
    public ArrayList<Goods> getDatas() {
        return goods;
    }

    HWGFragmentViewHolder.ItemOnClick itemOnClick;
    Activity activity;
    public HWGFragmentAdapter(Activity activity,HWGFragmentViewHolder.ItemOnClick itemOnClick,boolean isLocal) {
        this.activity=activity;
        this.itemOnClick = itemOnClick;
        this.isLocal=isLocal;
        this.goods = new ArrayList<>();
    }

    @Override
    public HWGFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_country_goods, parent, false);
        HWGFragmentViewHolder hwgFragmentViewHolder = new HWGFragmentViewHolder(view, itemOnClick);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    @Override
    public void onBindViewHolder(HWGFragmentViewHolder holder, final int position) {
        //图片加载
        LoadPicture loadPicture=new LoadPicture();
        loadPicture.initPicture(holder.img_goods_icon, goods.get(position).getPicarr());
//        loadPicture.initPicture(holder.img_goods_icon,isLocal, goods.get(position).getPicarr(), Integer.parseInt(goods.get(position).getGoods_id()));

        holder.t_goods_oldmoney.setVisibility(View.GONE);
//        holder.t_goods_oldmoney.setText(NumberUtils.formatPrice(goods.get(position).getDismoney()));
//        holder.t_goods_name.setText(goods.get(position).getTitle());
//
        if(!TextUtils.isEmpty(goods.get(position).getGoods_promotion_type())) {   // goods_promotion_type  促销类型  0为无促销  2为促销
            if (goods.get(position).getGoods_promotion_type().equals("0")) {
                holder.t_goods_money.setText(NumberUtils.formatPrice(goods.get(position).getMoney()));

            } else {
                if (!TextUtils.isEmpty(goods.get(position).getGoods_promotion_price())) {
                    holder.t_goods_money.setText(goods.get(position).getGoods_promotion_price());
                }
            }

        }

        holder.t_goods_name.setText(Util.toDBC(goods.get(position).getTitle()));
        holder.t_goods_oldmoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.img_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getInstance().self == null) {
                    Intent intent = new Intent(activity, WXEntryActivity.class);
                    activity.startActivity(intent);
                    return;
                }
                ProgressDlgUtil.showProgressDlg("Loading...", activity);
                Log.i("zjz", "add2cart_key=" + MyApplication.getInstance().getMykey());

                HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_add_to_cart, "key=" + MyApplication.getInstance().getMykey() + "&goods_id=" + goods.get(position).getGoods_id() + "&quantity=1", new HttpRevMsg() {
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
                                            Toast.makeText(activity, "添加成功！", Toast.LENGTH_SHORT).show();
                                        } else if (object.optString("datas").contains("error")) {
                                            Toast.makeText(activity, object.getJSONObject("datas").optString("error"), Toast.LENGTH_SHORT).show();
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
        });
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }
}
