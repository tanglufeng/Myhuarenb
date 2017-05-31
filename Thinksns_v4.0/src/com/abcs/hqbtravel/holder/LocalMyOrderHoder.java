package com.abcs.hqbtravel.holder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.activity.CartActivity2;
import com.abcs.haiwaigou.activity.CommentActivity;
import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.activity.MyDeliverActivity;
import com.abcs.haiwaigou.activity.OrderDetailActivity;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.DeliverActivityEntry;
import com.abcs.haiwaigou.model.ExtendOrderGoodsEntry;
import com.abcs.haiwaigou.model.HuoHMyOrder;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.NoticeDialogActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Administrator on 2016/9/8.
 */
public class LocalMyOrderHoder extends BaseViewHolder<HuoHMyOrder.DatasEntry> {

    boolean open=true;
    TextView t_name ;
    TextView t_total ;
     ImageView img_delete;
     TextView t_cancel ;
     TextView t_state ;
    TextView t_order_sn ;
    TextView t_add_time ;
     TextView t_deliver ;
     TextView t_refund ;
     TextView t_receive ;
     TextView t_comment ;
     TextView t_iscomment ;
     TextView t_detail ;
     TextView t_buy_again ;
     TextView t_state_of_refund ;

    LinearLayout linear_goods ;
    LinearLayout lin_delete ;
    LinearLayout linear_state ;




    public Handler handler=new Handler();
    Activity activity=(Activity) getContext();
     ArrayList<String > goodsIdList=new ArrayList<String >();
    ArrayList<String >goodsNumList=new ArrayList<String >();
    


    public LocalMyOrderHoder(ViewGroup parent) {
        super(parent, R.layout.local_item_order_orderlsit);

         t_name = (TextView) itemView.findViewById(R.id.t_name);
         t_total = (TextView) itemView.findViewById(R.id.t_total);
          img_delete= (ImageView) itemView.findViewById(R.id.img_delete);
          t_cancel = (TextView) itemView.findViewById(R.id.t_cancel);
          t_state = (TextView) itemView.findViewById(R.id.t_state);
         t_order_sn = (TextView) itemView.findViewById(R.id.t_order_sn);
         t_add_time = (TextView) itemView.findViewById(R.id.t_add_time);
          t_deliver = (TextView) itemView.findViewById(R.id.t_deliver);
          t_refund = (TextView) itemView.findViewById(R.id.t_refund);
          t_receive = (TextView) itemView.findViewById(R.id.t_receive);
          t_comment = (TextView) itemView.findViewById(R.id.t_comment);
          t_iscomment = (TextView) itemView.findViewById(R.id.t_iscomment);
          t_detail = (TextView) itemView.findViewById(R.id.t_detail);
          t_buy_again = (TextView) itemView.findViewById(R.id.t_buy_again);
          t_state_of_refund = (TextView) itemView.findViewById(R.id.t_state_of_refund);

         linear_goods = (LinearLayout) itemView.findViewById(R.id.linear_goods);
        lin_delete = (LinearLayout) itemView.findViewById(R.id.lin_delete);
         linear_state = (LinearLayout) itemView.findViewById(R.id.linear_state);


    }


    @Override
    public void setData(final HuoHMyOrder.DatasEntry data) {

        if(data!=null){

            goodsIdList.clear();
            goodsNumList.clear();
            for (int j = 0; j < data.extendOrderGoods.size(); j++) {
                linear_goods.setVisibility(View.GONE);
                linear_goods.removeAllViews();
                linear_goods.invalidate();

                final ExtendOrderGoodsEntry evaluation_state=data.extendOrderGoods.get(j);

                goodsIdList.add(evaluation_state.goodsId);
                goodsNumList.add(evaluation_state.goodsNum);

                View itemView = activity.getLayoutInflater().inflate(R.layout.huohang_item_order2, null);

                View lin_top=(View) itemView.findViewById(R.id.lin_top);
                View lin_bottom=(View) itemView.findViewById(R.id.lin_bottom);

                ImageView img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
                TextView tv_en_title=(TextView) itemView.findViewById(R.id.tv_en_title);
                TextView tv_title=(TextView) itemView.findViewById(R.id.tv_title);
                TextView tv_store=(TextView) itemView.findViewById(R.id.tv_store);
                TextView tv_price=(TextView) itemView.findViewById(R.id.tv_price);
                TextView tv_sui=(TextView) itemView.findViewById(R.id.tv_sui);
                TextView t_num=(TextView) itemView.findViewById(R.id.t_num);

                TextView tvShuilvNe2=(TextView) itemView.findViewById(R.id.tv_shuilv_ne2);
                TextView tvShuilvM2=(TextView) itemView.findViewById(R.id.tv_shuilv_m2);
                TextView tvShuilvBr2=(TextView) itemView.findViewById(R.id.tv_shuilv_br2);
                TextView tvTotalNetto=(TextView) itemView.findViewById(R.id.tv_total_Netto);
                TextView tvTotalMwst=(TextView) itemView.findViewById(R.id.tv_total_Mwst);
                TextView tvTotalBru=(TextView) itemView.findViewById(R.id.tv_total_Bru);
                LinearLayout linerHdong=(LinearLayout) itemView.findViewById(R.id.liner_hdong2);

                //税率信息 tax_arr
                if(data.taxArr!=null){
                        tvShuilvNe2.setText(NumberUtils.formatPriceNo(data.taxArr.nettoA)+"\n"+NumberUtils.formatPriceNo(data.taxArr.nettoB)+"");
                        tvShuilvM2.setText(NumberUtils.formatPriceNo(data.taxArr.mwstA)+"\n"+NumberUtils.formatPriceNo(data.taxArr.mwstB)+"");
                        tvShuilvBr2.setText(NumberUtils.formatPriceNo(data.taxArr.bruttoA)+"\n"+NumberUtils.formatPriceNo(data.taxArr.bruttoB)+"");

                        tvTotalNetto.setText(NumberUtils.formatPriceNo((data.taxArr.nettoA)+(data.taxArr.nettoB))+"");
                        tvTotalMwst.setText(NumberUtils.formatPriceNo((data.taxArr.mwstA+data.taxArr.mwstB))+"");
                        tvTotalBru.setText(NumberUtils.formatPriceNo((data.taxArr.bruttoA+data.taxArr.bruttoB))+"");
                }

                //活动信息 activity_arr
                if(data.deliverActivity!=null){

                    if(data.deliverActivity.size()>0){
                        lin_top.setVisibility(View.VISIBLE);
                        lin_bottom.setVisibility(View.VISIBLE);
                        linerHdong.setVisibility(View.VISIBLE);

                        linerHdong.removeAllViews();
                        linerHdong.setVisibility(View.VISIBLE);
                        for (int i = 0; i <data.deliverActivity.size(); i++) {

                            DeliverActivityEntry item=data.deliverActivity.get(i);
                            View view=View.inflate(activity,R.layout.item_hd_text2_big,null);

                            ViewGroup parent = (ViewGroup) view.getParent();
                            if (parent != null) {
                                parent.removeAllViews();
                            }

                            ImageView img=(ImageView) view.findViewById(R.id.img);
                            TextView tv=(TextView) view.findViewById(R.id.tv);

                            if(item!=null){
                                if(!TextUtils.isEmpty(item.img)){
                                    MyApplication.imageLoader.displayImage(item.img,img,MyApplication.options);
                                }

                                if(!TextUtils.isEmpty(item.activityName)){
                                    tv.setText(item.activityName);
                                }
                            }

                            linerHdong.addView(view);
                        }
                    }else {
                        linerHdong.setVisibility(View.GONE);
                    }
                }

                img_logo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, GoodsDetailActivity.class);
                        intent.putExtra("sid", evaluation_state.goodsId);
                        intent.putExtra("pic", evaluation_state.goodsImage);
                        activity.startActivity(intent);
                    }
                });


                if(!TextUtils.isEmpty(evaluation_state.goodsImage)){
//                http://www.huaqiaobang.com/data/upload/shop/store/goods/11/11_05375327290881075.jpg
                    MyApplication.imageLoader.displayImage("http://www.huaqiaobang.com/data/upload/shop/store/goods/11/"+evaluation_state.goodsImage,img_logo,MyApplication.options);
                }
                tv_title.setText(evaluation_state.goodsName);
//            tv_en_title.setText(checkOrders.get(position).getStore_list().get(i).goods_en_name+"");
                tv_store.setText("货号:"+evaluation_state.goodsSerial);
                tv_price.setText("€ "+NumberUtils.formatPriceNo(evaluation_state.goodsPrice));
                t_num.setText("X"+evaluation_state.goodsNum);

                tv_sui.setText(""+evaluation_state.taxRate+"");

                String orgin=evaluation_state.gOrgin;  // 发货地

                /////////////////////
                t_deliver.setOnClickListener(new View.OnClickListener() {   //  查看物流
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, MyDeliverActivity.class);
                        intent.putExtra("order_id", data.orderId);
                        intent.putExtra("o_rgin", evaluation_state.gOrgin);
                        activity.startActivity(intent);

                    }
                });

                ////////////////////
                t_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(activity, CommentActivity.class);
                        intent.putExtra("order_id", data.orderId);
                        activity.startActivity(intent);
                    }
                });
                linear_goods.addView(itemView);

//                t_state.setText(item.getOrder_list().get(j).getState_desc());

                if (data.orderState.equals("0")) {  // zds
                    //已取消
                    linear_state.setVisibility(View.VISIBLE);
                    t_state.setTextColor(Color.parseColor("#ff454545"));
                    t_state.setVisibility(View.VISIBLE);
                    t_state.setText("已取消");
                    t_cancel.setVisibility(View.GONE);
                    t_deliver.setVisibility(View.GONE);
                    t_receive.setVisibility(View.GONE);
                    t_comment.setVisibility(View.GONE);
                    t_iscomment.setVisibility(View.GONE);
                    t_refund.setVisibility(View.GONE);
                    t_state_of_refund.setVisibility(View.GONE);
                    img_delete.setVisibility(View.VISIBLE);
                }else if (data.orderState.equals("10")) { //zds
                    //待付款
                    linear_state.setVisibility(View.VISIBLE);
                    t_state.setTextColor(Color.parseColor("#0e06b1"));
                    t_state.setVisibility(View.VISIBLE);
                    t_state.setText("待付款");
                    t_cancel.setVisibility(View.VISIBLE);
                    t_deliver.setVisibility(View.GONE);
                    t_receive.setVisibility(View.GONE);
                    t_comment.setVisibility(View.GONE);
                    t_iscomment.setVisibility(View.GONE);
                    t_refund.setVisibility(View.GONE);
                    t_state_of_refund.setVisibility(View.GONE);
                    img_delete.setVisibility(View.VISIBLE);
                }else if (data.orderState.equals("20")) { //zds
                    //待发货
                    linear_state.setVisibility(View.VISIBLE);
                    t_state.setTextColor(Color.parseColor("#eb5041"));
                    t_state.setVisibility(View.VISIBLE);
                    t_state.setText("待发货");
                    t_cancel.setVisibility(View.VISIBLE);
                    t_deliver.setVisibility(View.GONE);
                    t_receive.setVisibility(View.GONE);
                    t_comment.setVisibility(View.GONE);
                    t_iscomment.setVisibility(View.GONE);
                    t_refund.setVisibility(View.GONE);
                    t_state_of_refund.setVisibility(View.GONE);
                    img_delete.setVisibility(View.VISIBLE);
                }else if (data.orderState.equals("30")) { //zds
                    //待收货
                    linear_state.setVisibility(View.VISIBLE);
                    t_state.setTextColor(Color.parseColor("#eb5041"));
                    t_state.setVisibility(View.VISIBLE);
                    t_state.setText("待收货");
                    t_cancel.setVisibility(View.GONE);
                    t_deliver.setVisibility(View.VISIBLE);
                    t_receive.setVisibility(View.VISIBLE);
                    t_comment.setVisibility(View.GONE);
                    t_iscomment.setVisibility(View.GONE);
                    t_refund.setVisibility(View.GONE);
                    t_state_of_refund.setVisibility(View.GONE);
                    img_delete.setVisibility(View.VISIBLE);
                }else if (data.orderState.equals("40")) { // zds
                    //交易完成
                    t_buy_again.setVisibility(View.VISIBLE);
                    linear_state.setVisibility(View.VISIBLE);
                    t_state.setTextColor(Color.parseColor("#ff454545"));
                    t_state.setVisibility(View.VISIBLE);
                    t_state.setText("已完成");
                    t_cancel.setVisibility(View.GONE);
                    t_deliver.setVisibility(View.VISIBLE);
                    t_receive.setVisibility(View.GONE);
                    t_comment.setVisibility(View.GONE);
                    t_iscomment.setVisibility(View.VISIBLE);
                    t_refund.setVisibility(View.GONE);
                    t_state_of_refund.setVisibility(View.GONE);
                    img_delete.setVisibility(View.VISIBLE);
                }else if (data.orderState.equals("40")) { // zds
                    //待评价
                    t_buy_again.setVisibility(View.VISIBLE);
                    linear_state.setVisibility(View.VISIBLE);
                    t_state.setTextColor(Color.parseColor("#ff454545"));
                    t_state.setVisibility(View.VISIBLE);
                    t_state.setText("待评价");
                    t_cancel.setVisibility(View.GONE);
                    t_deliver.setVisibility(View.VISIBLE);
                    t_receive.setVisibility(View.GONE);
                    t_comment.setVisibility(View.VISIBLE);
                    t_iscomment.setVisibility(View.GONE);
                    t_refund.setVisibility(View.GONE);
                    t_state_of_refund.setVisibility(View.GONE);
                    img_delete.setVisibility(View.VISIBLE);
                }

                t_buy_again.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String param=null;
                        StringBuffer buffer=new StringBuffer();
                        for (int i=0;i<goodsIdList.size();i++){
                            buffer.append(goodsIdList.get(i)+"|"+goodsNumList.get(i));
                            if(i!=goodsIdList.size()-1){
                                buffer.append(",");
                            }
                        }
                        param=buffer.toString();
                        Log.i("zjz","buy_all_param="+param);
                        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_goods_buy_all_and_again, "&key="+MyApplication.getInstance().getMykey()+"&goodsinfo="+param, new HttpRevMsg() {
                            @Override
                            public void revMsg(final String msg) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i("zjz","buy_all_msg="+msg);
                                        try {
                                            JSONObject object = new JSONObject(msg);
                                            if (object.getInt("code") == 200) {
                                                if(object.optString("datas").equals("1")){
                                                    MyUpdateUI.sendUpdateCarNum(activity);
                                                    Intent intent=new Intent(activity, CartActivity2.class);
                                                    intent.putExtra("store_id","");
                                                    activity.startActivity(intent);
                                                }else if(object.getJSONObject("datas").has("error")){
                                                    Toast.makeText(activity,"该商品已下架或库存为零！",Toast.LENGTH_SHORT).show();
                                                }else {
                                                    JSONObject jsonObject=object.getJSONObject("datas");
                                                    Iterator keys=jsonObject.keys();
                                                    StringBuffer stringBuffer=new StringBuffer();
                                                    while (keys.hasNext()){
                                                        String value= (String) keys.next();
                                                        JSONObject goodsObject=jsonObject.getJSONObject(value);
                                                        JSONObject goods=goodsObject.optJSONObject("kucun");
                                                        if(goods!=null){
                                                            if(goods.optString("goods_name").length()>20){
                                                                stringBuffer.append("商品["+goods.optString("goods_name").substring(0,20)+"] ");
                                                            }else {
                                                                stringBuffer.append("商品["+goods.optString("goods_name")+"] ");
                                                            }
                                                        }else if(goodsObject.has("xiajia")){
                                                            if(goodsObject.optString("xiajia").length()>20){
                                                                stringBuffer.append("商品["+goodsObject.optString("xiajia").substring(0,20)+"] ");
                                                            }else {
                                                                stringBuffer.append("商品["+goodsObject.optString("xiajia")+"] ");
                                                            }
                                                        }

                                                    }
                                                    String text=stringBuffer+"库存为0或已经下架！";
                                                    activity.startActivity(new Intent(activity, NoticeDialogActivity.class).putExtra("msg", text));
//                                                Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
                                                    MyUpdateUI.sendUpdateCarNum(activity);
                                                    Intent intent=new Intent(activity,CartActivity2.class);
                                                    intent.putExtra("store_id","");
                                                    activity.startActivity(intent);
                                                }
                                            }
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            Log.i("zjz", e.toString());
                                            Log.i("zjz", msg);
                                            e.printStackTrace();

                                        }finally {
                                            ProgressDlgUtil.stopProgressDlg();
                                        }
                                    }
                                });

                            }
                        });
                    }
                });

                t_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(activity, OrderDetailActivity.class);
                        intent.putExtra("order_id",data.orderId);
                        activity.startActivity(intent);
                    }
                });
                lin_delete.setOnClickListener(new View.OnClickListener() {  // 展开和折叠
                    @Override
                    public void onClick(View v) {

                        if(open){
                            img_delete.setImageResource(R.drawable.iv_local_dingdan_close);
                            linear_goods.setVisibility(View.VISIBLE);
                            notifyAll();
                            open=false;
                        }else {
                            img_delete.setImageResource(R.drawable.iv_local_dingdan_open);
                            linear_goods.setVisibility(View.GONE);
                            notifyAll();
                            open=true;
                        }
                    }
                });

                if(data.storeId.equals("11")){  // 本地商品不支持退款
                    t_cancel.setText("取消订货");
                }

                t_receive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new PromptDialog(activity, "是否确认收货？", new Complete() {
                            @Override
                            public void complete() {
                                ProgressDlgUtil.showProgressDlg("Loading...", activity);
                                HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_order_receive, "&order_id=" + data.orderId + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
                                    @Override
                                    public void revMsg(final String msg) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    JSONObject object = new JSONObject(msg);
                                                    Log.i("zjz", "msg=" + msg);
                                                    if (object.getInt("code") == 200) {
                                                        if (object.optString("datas").equals("1")) {
                                                            Toast.makeText(activity, "成功收货！", Toast.LENGTH_SHORT).show();
                                                            MyUpdateUI.sendUpdateCollection(activity, MyUpdateUI.ORDER);
                                                            MyUpdateUI.sendUpdateCollection(activity,MyUpdateUI.MYORDERNUM);
                                                        } else {
                                                            Toast.makeText(activity, "失败！", Toast.LENGTH_SHORT).show();
                                                        }

                                                        ProgressDlgUtil.stopProgressDlg();
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
                t_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new PromptDialog(activity, "取消后无法恢复，确定取消该订单？", new Complete() {
                            @Override
                            public void complete() {
                                ProgressDlgUtil.showProgressDlg("Loading...", activity);
                                HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_order_cancle, "&order_id=" + data.orderId + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
                                    @Override
                                    public void revMsg(final String msg) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    JSONObject object = new JSONObject(msg);
                                                    Log.i("zjz", "msg=" + msg);
                                                    if (object.getInt("code") == 200) {
                                                        if (object.optString("datas").equals("1")) {
                                                            Toast.makeText(activity, "成功取消！", Toast.LENGTH_SHORT).show();
                                                            MyUpdateUI.sendUpdateCollection(activity, MyUpdateUI.ORDER);
                                                            MyUpdateUI.sendUpdateCollection(activity,MyUpdateUI.MYORDERNUM);
                                                        } else {
                                                            Toast.makeText(activity, "失败！", Toast.LENGTH_SHORT).show();
                                                        }

                                                        ProgressDlgUtil.stopProgressDlg();
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

                t_total.setText("€" + data.orderAmount + "( 含运费：" + NumberUtils.formatPrice(Double.valueOf(data.orderAmount) - Double.valueOf(data.goodsAmount)) + " )");
                t_order_sn.setText(data.orderSn);
                t_add_time.setText(Util.formatzjz.format(data.addTime * 1000) + "");
//                t_name.setText(item.getOrder_list().get(j).getStore_name());
            }
        }
    }

    private void checkSoLution(final String order_id){

        new PromptDialog(activity, "取消后无法恢复，确定取消改订单", new Complete() {
            @Override
            public void complete() {
                ProgressDlgUtil.showProgressDlg("Loading...", activity);
                HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_refund+"&order_id=" + order_id ,"&key=" + MyApplication.getInstance().getMykey()+"&buyer_message=12&mobile_phone=1234567890", new HttpRevMsg() {
                    @Override
                    public void revMsg(final String msg) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject object = new JSONObject(msg);
                                    Log.i("zjz", "msg_del=" + msg);
                                    if (object.getInt("code") == 200) {
                                        if (object.optString("datas").contains("成功")) {
                                            Toast.makeText(activity, "已取消该订单！", Toast.LENGTH_SHORT).show();
                                            MyUpdateUI.sendUpdateCollection(activity, MyUpdateUI.ORDER);
                                            MyUpdateUI.sendUpdateCollection(activity,MyUpdateUI.MYORDERNUM);
                                        } else {
                                            Toast.makeText(activity, "取消该订单失败！", Toast.LENGTH_SHORT).show();
                                        }

                                        ProgressDlgUtil.stopProgressDlg();
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

}
