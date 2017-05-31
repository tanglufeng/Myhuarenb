package com.abcs.haiwaigou.adapter.viewholder;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.HotItem;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.ShengJiHuiYuanActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;


public class HotItemHoder extends BaseViewHolder<HotItem.DatasBean>{

    public ImageView img_goods_icon;
    public ImageView img_buy;
    public ImageView img_xianshi;
    public TextView t_goods_name;
    public TextView t_goods_money;
    public TextView t_y_goods_money;
    public TextView t_goods_oldmoney;
    public TextView t_desc;
    public LinearLayout linear_root;
    

    public HotItemHoder(ViewGroup parent) {
        super(parent, R.layout.hwg_item_country_goods);
        img_goods_icon= (ImageView) itemView.findViewById(R.id.img_goods_icon);
        img_buy= (ImageView) itemView.findViewById(R.id.img_buy);
        img_xianshi= (ImageView) itemView.findViewById(R.id.img_xianshi);
        t_goods_name= (TextView) itemView.findViewById(R.id.t_goods_name);
        t_desc= (TextView) itemView.findViewById(R.id.t_desc);
        t_goods_money= (TextView) itemView.findViewById(R.id.t_goods_money);
        t_y_goods_money= (TextView) itemView.findViewById(R.id.t_y_goods_money);
        t_goods_oldmoney= (TextView) itemView.findViewById(R.id.t_goods_oldmoney);
        linear_root= (LinearLayout) itemView.findViewById(R.id.linear_root);

    }

    @Override
    public void setData(final HotItem.DatasBean item) {


        t_y_goods_money.setVisibility(View.VISIBLE);

        if(item.goodsPromotionType.equals("0")){  // 未促销
            t_goods_money.setText(NumberUtils.formatPrice(item.goodsPrice));
            t_y_goods_money.setText(NumberUtils.formatPrice(item.goodsMarketprice));
            //添加删除线
            t_y_goods_money.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }else if(item.goodsPromotionType.equals("2")){ // 促销
            t_goods_money.setText(NumberUtils.formatPrice(item.goodsMarketprice));
            t_y_goods_money.setText(NumberUtils.formatPrice(item.goodsPromotionPrice));
            //添加删除线
            t_y_goods_money.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }else {}



     /*   if(item.goodsMarketprice>0||item.goodsPromotionPrice>0){
            t_y_goods_money.setVisibility(View.VISIBLE);
            if(item.goodsPromotionPrice==item.goodsMarketprice){  *//*****促销价和原来相等*******//*
                t_y_goods_money.setText(NumberUtils.formatPrice(item.goodsMarketprice));
                //添加删除线
                t_y_goods_money.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }else {
                t_y_goods_money.setText(NumberUtils.formatPrice(item.goodsPromotionPrice));
                //添加删除线
                t_y_goods_money.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }else {
            t_y_goods_money.setVisibility(View.GONE);
        }*/

        t_goods_name.setText(item.goodsName);
        img_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getInstance().self == null) {
                    Intent intent = new Intent(getContext(), WXEntryActivity.class);
                    getContext().startActivity(intent);
                    return;
                }

                confirmData(item.goodsId);  /*******验证商品是否可以买*******/
            }
        });
        
        MyApplication.imageLoader.displayImage("http://www.huaqiaobang.com/data/upload/shop/store/goods/1/"+item.goodsImage,img_goods_icon, MyApplication.getListOptions());
    }

    Handler handler=new Handler();

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
                                    getContext().startActivity(new Intent(getContext(), ShengJiHuiYuanActivity.class));
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
//                            ProgressDlgUtil.stopProgressDlg();
                        } finally {
//                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });
    }

    private void addGoods(String goodId){
//        ProgressDlgUtil.showProgressDlg("Loading...", getContext());
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
                                    MyUpdateUI.sendUpdateCarNum(getContext());
                                    Toast.makeText(getContext(),"添加成功！",Toast.LENGTH_SHORT).show();
                                } else if (object.optString("datas").contains("error")) {
                                    Toast.makeText(getContext(),object.getJSONObject("datas").optString("error"),Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
//                            ProgressDlgUtil.stopProgressDlg();
                        } finally {
//                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });
    }
}
