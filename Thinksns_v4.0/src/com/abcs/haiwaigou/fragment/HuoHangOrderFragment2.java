package com.abcs.haiwaigou.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.activity.CartActivity2;
import com.abcs.haiwaigou.activity.CommentActivity;
import com.abcs.haiwaigou.activity.GoodsDetailActivity;
import com.abcs.haiwaigou.activity.HuoHangOrderDetailActivity;
import com.abcs.haiwaigou.activity.MyDeliverActivity;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.DeliverActivityEntry;
import com.abcs.haiwaigou.model.ExtendOrderGoodsEntry;
import com.abcs.haiwaigou.model.HuoHMyOrder;
import com.abcs.haiwaigou.utils.NumberUtils;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.NoticeDialogActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;

/**
 */
public class HuoHangOrderFragment2 extends BaseFragment{

    Activity activity;
    ListView listview;
    ImageView imgNull;
    SwipeRefreshLayout refresh;
    TextView tvNull;
    RelativeLayout layoutNull;


    private View view;
    int totalPage;
    int currentPage = 1;
    boolean isLoadMore = false;
    boolean first = true;
    String goods_id;
    MyBroadCastReceiver myBroadCastReceiver;

    private String type;
    public static HuoHangOrderFragment2 newInstance(String type){
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", type);
        HuoHangOrderFragment2 orderFragment=new HuoHangOrderFragment2();
        orderFragment.setArguments(bundle);
        return orderFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        view =  activity.getLayoutInflater().inflate(
                R.layout.local_fragment_comment_item, null);
        myBroadCastReceiver = new MyBroadCastReceiver(activity, updateUI);
        myBroadCastReceiver.register();
        initView();
    }
    MyBroadCastReceiver.UpdateUI updateUI = new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

        }

        @Override
        public void updateCarNum(Intent intent) {

        }

        @Override
        public void updateCollection(Intent intent) {
            if (intent.getStringExtra("type").equals(MyUpdateUI.ORDER)) {
                initAllDates();
                Log.i("zds", "更新订单");
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };
    private void initView() {
        refresh= (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        listview= (ListView) view.findViewById(R.id.listview);
        layoutNull= (RelativeLayout) view.findViewById(R.id.layout_null);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.setRefreshing(false);
                    }
                },300);

                initAllDates();

            }
        });
    }

    Handler handler=new Handler();
    private void initAllDates() {
        if(first){
            ProgressDlgUtil.showProgressDlg("Loading...", activity);
        }

//        http://newapi.tuling.me/huaqiaobang/mobile/index.php?act=member_order&op=native_order_list&key=7661f71726932e44f85f677671ba7ebb
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base+"/mobile/index.php","act=member_order&op=native_order_list&key=" + MyApplication.getInstance().getMykey() + "&order_state="+type, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                if(first){
                    ProgressDlgUtil.stopProgressDlg();
                    first=false;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zds", "msg=" + msg);

                        if(TextUtils.isEmpty(msg)){
                            Toast.makeText(activity,"暂无数据！", Toast.LENGTH_LONG).show();
                            return;
                        }else {
                            HuoHMyOrder response= new Gson().fromJson(msg,HuoHMyOrder.class);
                            if(response.state==1){
                                if(response.datas!=null){
                                    if(response.datas.size()>0){
                                        listview.setAdapter(new MyAdapter(activity,response.datas));
                                    }else {
                                        Toast.makeText(activity,"暂无数据！", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    }
                });
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeAllViewsInLayout();
        ButterKnife.inject(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString("type");
        }

        initAllDates();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onDestroy() {
        myBroadCastReceiver.unRegister();
        super.onDestroy();
    }

    @Override
    protected void lazyLoad() {

    }

    public class MyAdapter extends BaseAdapter {

        boolean open = true;
        Activity activity = (Activity) getContext();
        ArrayList<String> goodsIdList = new ArrayList<String>();
        ArrayList<String> goodsNumList = new ArrayList<String>();

        private Context mContext;
        List<HuoHMyOrder.DatasEntry> list;

        public MyAdapter(Context mContext, List<HuoHMyOrder.DatasEntry> datas) {
            this.mContext = mContext;
            this.list = datas;
        }

        public int getCount() {
            return this.list.size();
        }

        public Object getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.local_item_order_orderlsit, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final HuoHMyOrder.DatasEntry data = (HuoHMyOrder.DatasEntry)getItem(position);

            if(data!=null){

                //税率信息 tax_arr
                if(data.taxArr!=null){
                    holder.tvShuilvNe2.setText(NumberUtils.formatPriceNo(data.taxArr.nettoA)+"\n"+NumberUtils.formatPriceNo(data.taxArr.nettoB)+"");
                    holder.tvShuilvM2.setText(NumberUtils.formatPriceNo(data.taxArr.mwstA)+"\n"+NumberUtils.formatPriceNo(data.taxArr.mwstB)+"");
                    holder.tvShuilvBr2.setText(NumberUtils.formatPriceNo(data.taxArr.bruttoA)+"\n"+NumberUtils.formatPriceNo(data.taxArr.bruttoB)+"");

                    holder.tvTotalNetto.setText(NumberUtils.formatPriceNo((data.taxArr.nettoA)+(data.taxArr.nettoB))+"");
                    holder.tvTotalMwst.setText(NumberUtils.formatPriceNo((data.taxArr.mwstA+data.taxArr.mwstB))+"");
                    holder.tvTotalBru.setText(NumberUtils.formatPriceNo((data.taxArr.bruttoA+data.taxArr.bruttoB))+"");
                }

                //活动信息 activity_arr
                if(data.deliverActivity!=null){

                    if(data.deliverActivity.size()>0){
                        holder.lin_top.setVisibility(View.VISIBLE);
                        holder.lin_bottom.setVisibility(View.VISIBLE);
                        holder.linerHdong.removeAllViews();
                        holder.linerHdong.setVisibility(View.VISIBLE);
                        for (int i = 0; i <data.deliverActivity.size(); i++) {

                            DeliverActivityEntry item=data.deliverActivity.get(i);
                            View view=View.inflate(activity,R.layout.item_hd_text2_big,null);

                            ViewGroup parent2 = (ViewGroup) view.getParent();
                            if (parent2 != null) {
                                parent2.removeAllViews();
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

                            holder.linerHdong.addView(view);
                        }
                    }else {
                        holder.linerHdong.setVisibility(View.GONE);
                    }
                }

                goodsIdList.clear();
                goodsNumList.clear();

                holder.linear_goods.removeAllViews();
                holder.linear_goods.invalidate();

                for (int j = 0; j < data.extendOrderGoods.size(); j++) {

                    final ExtendOrderGoodsEntry evaluation_state=data.extendOrderGoods.get(j);

                    goodsIdList.add(evaluation_state.goodsId);
                    goodsNumList.add(evaluation_state.goodsNum);

                    View itemView = activity.getLayoutInflater().inflate(R.layout.huohang_item_order2, null);
                    ImageView img_logo=(ImageView) itemView.findViewById(R.id.img_logo);
                    TextView tv_en_title=(TextView) itemView.findViewById(R.id.tv_en_title);
                    TextView tv_title=(TextView) itemView.findViewById(R.id.tv_title);
                    TextView tv_store=(TextView) itemView.findViewById(R.id.tv_store);
                    TextView tv_price=(TextView) itemView.findViewById(R.id.tv_price);
                    TextView tv_sui=(TextView) itemView.findViewById(R.id.tv_sui);
                    TextView t_num=(TextView) itemView.findViewById(R.id.t_num);

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
                    tv_en_title.setText(evaluation_state.goodsEnName);
                    tv_store.setText("货号:"+evaluation_state.goodsSerial);
                    tv_price.setText("€ "+NumberUtils.formatPriceNo(evaluation_state.goodsPrice));
                    t_num.setText("X"+evaluation_state.goodsNum);

                    tv_sui.setText(""+evaluation_state.taxRate+"");

                    String orgin=evaluation_state.gOrgin;  // 发货地

                    /////////////////////
                    holder.t_deliver.setOnClickListener(new View.OnClickListener() {   //  查看物流
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, MyDeliverActivity.class);
                            intent.putExtra("order_id", data.orderId);
                            intent.putExtra("o_rgin", evaluation_state.gOrgin);
                            activity.startActivity(intent);

                        }
                    });

                    ////////////////////
                    holder.t_comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(activity, CommentActivity.class);
                            intent.putExtra("order_id", data.orderId);
                            activity.startActivity(intent);
                        }
                    });

//                t_state.setText(item.getOrder_list().get(j).getState_desc());

                    if (data.orderState.equals("0")) {  // zds
                        //已取消
                        holder.linear_state.setVisibility(View.VISIBLE);
                        holder.t_state.setTextColor(Color.parseColor("#ff454545"));
                        holder.t_state.setVisibility(View.VISIBLE);
                        holder.t_state.setText("已取消");
                        holder.t_cancel.setVisibility(View.GONE);
                        holder.t_deliver.setVisibility(View.GONE);
                        holder.t_receive.setVisibility(View.GONE);
                        holder.t_comment.setVisibility(View.GONE);
                        holder.t_iscomment.setVisibility(View.GONE);
                        holder.t_refund.setVisibility(View.GONE);
                        holder.t_state_of_refund.setVisibility(View.GONE);
                        holder.img_delete.setVisibility(View.VISIBLE);
                    }else if (data.orderState.equals("10")) { //zds
                        //待付款
                        holder.linear_state.setVisibility(View.VISIBLE);
                        holder.t_state.setTextColor(Color.parseColor("#0e06b1"));
                        holder.t_state.setVisibility(View.VISIBLE);
                        holder.t_state.setText("待付款");
                        holder.t_cancel.setVisibility(View.VISIBLE);
                        holder.t_deliver.setVisibility(View.GONE);
                        holder.t_receive.setVisibility(View.GONE);
                        holder.t_comment.setVisibility(View.GONE);
                        holder.t_iscomment.setVisibility(View.GONE);
                        holder.t_refund.setVisibility(View.GONE);
                        holder.t_state_of_refund.setVisibility(View.GONE);
                        holder.img_delete.setVisibility(View.VISIBLE);
                    }else if (data.orderState.equals("20")) { //zds
                        //待发货
                        holder.linear_state.setVisibility(View.VISIBLE);
                        holder.t_state.setTextColor(Color.parseColor("#eb5041"));
                        holder.t_state.setVisibility(View.VISIBLE);
                        holder.t_state.setText("待发货");
                        holder.t_cancel.setVisibility(View.VISIBLE);
                        holder.t_deliver.setVisibility(View.GONE);
                        holder.t_receive.setVisibility(View.GONE);
                        holder.t_comment.setVisibility(View.GONE);
                        holder.t_iscomment.setVisibility(View.GONE);
                        holder.t_refund.setVisibility(View.GONE);
                        holder.t_state_of_refund.setVisibility(View.GONE);
                        holder.img_delete.setVisibility(View.VISIBLE);
                    }else if (data.orderState.equals("30")) { //zds
                        //待收货
                        holder.linear_state.setVisibility(View.VISIBLE);
                        holder.t_state.setTextColor(Color.parseColor("#eb5041"));
                        holder.t_state.setVisibility(View.VISIBLE);
                        holder.t_state.setText("待收货");
                        holder.t_cancel.setVisibility(View.GONE);
                        holder.t_deliver.setVisibility(View.VISIBLE);
                        holder.t_receive.setVisibility(View.VISIBLE);
                        holder.t_comment.setVisibility(View.GONE);
                        holder.t_iscomment.setVisibility(View.GONE);
                        holder.t_refund.setVisibility(View.GONE);
                        holder.t_state_of_refund.setVisibility(View.GONE);
                        holder.img_delete.setVisibility(View.VISIBLE);
                    }else if (data.orderState.equals("40")) { // zds
                        //交易完成
                        holder.t_buy_again.setVisibility(View.VISIBLE);
                        holder.linear_state.setVisibility(View.VISIBLE);
                        holder.t_state.setTextColor(Color.parseColor("#ff454545"));
                        holder.t_state.setVisibility(View.VISIBLE);
                        holder.t_state.setText("已完成");
                        holder.t_cancel.setVisibility(View.GONE);
                        holder.t_deliver.setVisibility(View.VISIBLE);
                        holder.t_receive.setVisibility(View.GONE);
                        holder.t_comment.setVisibility(View.GONE);
                        holder.t_iscomment.setVisibility(View.VISIBLE);
                        holder.t_refund.setVisibility(View.GONE);
                        holder.t_state_of_refund.setVisibility(View.GONE);
                        holder.img_delete.setVisibility(View.VISIBLE);
                    }else if (data.orderState.equals("40")) { // zds
                        //待评价
                        holder.t_buy_again.setVisibility(View.VISIBLE);
                        holder.linear_state.setVisibility(View.VISIBLE);
                        holder.t_state.setTextColor(Color.parseColor("#ff454545"));
                        holder.t_state.setVisibility(View.VISIBLE);
                        holder.t_state.setText("待评价");
                        holder.t_cancel.setVisibility(View.GONE);
                        holder.t_deliver.setVisibility(View.VISIBLE);
                        holder.t_receive.setVisibility(View.GONE);
                        holder.t_comment.setVisibility(View.VISIBLE);
                        holder.t_iscomment.setVisibility(View.GONE);
                        holder.t_refund.setVisibility(View.GONE);
                        holder.t_state_of_refund.setVisibility(View.GONE);
                        holder.img_delete.setVisibility(View.VISIBLE);
                    }

                    holder.t_buy_again.setOnClickListener(new View.OnClickListener() {
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

                    holder.t_detail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(activity, HuoHangOrderDetailActivity.class);
                            intent.putExtra("order_id",data.orderId);
                            intent.putExtra("storeId",data.storeId);
                            activity.startActivity(intent);
                        }
                    });

                    holder.lin_delete.setOnClickListener(new View.OnClickListener() {  // 展开和折叠
                        @Override
                        public void onClick(View v) {

                            if(open){
                                holder.img_delete.setImageResource(R.drawable.iv_local_dingdan_close);
                                holder.linear_goods.setVisibility(View.VISIBLE);
                                holder.liner_shui_root.setVisibility(View.VISIBLE);
                                notifyDataSetChanged();
                                open=false;
                            }else {
                                holder.img_delete.setImageResource(R.drawable.iv_local_dingdan_open);
                                holder.linear_goods.setVisibility(View.GONE);
                                holder.liner_shui_root.setVisibility(View.GONE);
                                notifyDataSetChanged();
                                open=true;
                            }
                        }
                    });

                    if(data.storeId.equals("11")){  // 本地商品不支持退款
                        holder.t_cancel.setText("取消订货");
                    }

                    holder.t_receive.setOnClickListener(new View.OnClickListener() {
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
                    holder.t_cancel.setOnClickListener(new View.OnClickListener() {
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

                    Log.i("zds", "getView: data.order_message"+data.order_message);

                   // 买家留言
                    if(!TextUtils.isEmpty(data.order_message)&&!data.order_message.equals("null")){
                        holder.tv_liuyan_msg.setText(data.order_message);
                        holder.liner_meg.setVisibility(View.VISIBLE);
                    }else {
                        holder.liner_meg.setVisibility(View.GONE);
                    }

                    //  是否需要发票
                    if(!TextUtils.isEmpty(data.native_invoice)&&data.native_invoice.equals("1")){
                        holder.t_fapiao.setText("需要发票");
                    }else {
                        holder.t_fapiao.setText("不需要发票");
                    }

                    holder.t_total.setText("€" + data.atPrice + "( 含运费：" + NumberUtils.formatPriceOuYuan(Double.valueOf(data.orderAmount) - Double.valueOf(data.goodsAmount))+"，");
                    holder.t_order_sn.setText(data.orderSn);
                    holder.t_add_time.setText(Util.formatzjz.format(data.addTime * 1000) + "");
//                t_name.setText(item.getOrder_list().get(j).getStore_name());

                    holder.linear_goods.addView(itemView);

                }
            }

            return convertView;
        }


        public class ViewHolder {

            TextView t_name;

            TextView t_total,t_fapiao,tv_liuyan_msg;
            ImageView img_delete;
            TextView t_cancel;
            TextView t_state;
            TextView t_order_sn;
            TextView t_add_time;
            TextView t_deliver;
            TextView t_refund;
            TextView t_receive;
            TextView t_comment;
            TextView t_iscomment;
            TextView t_detail;
            TextView t_buy_again;
            TextView t_state_of_refund;

            LinearLayout linear_goods,liner_meg;
            LinearLayout lin_delete;
            LinearLayout linear_state;

            TextView tvShuilvNe2;
            TextView tvShuilvM2;
            TextView tvShuilvBr2;
            TextView tvTotalNetto;
            TextView tvTotalMwst;
            TextView tvTotalBru;
            LinearLayout linerHdong,liner_shui_root;
            View lin_top;
            View lin_bottom;



            public ViewHolder(View itemView) {

                t_name = (TextView) itemView.findViewById(R.id.t_name);
                t_total = (TextView) itemView.findViewById(R.id.t_total);
                tv_liuyan_msg = (TextView) itemView.findViewById(R.id.tv_liuyan_msg);
                t_fapiao = (TextView) itemView.findViewById(R.id.t_fapiao);
                img_delete = (ImageView) itemView.findViewById(R.id.img_delete);
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

                liner_meg = (LinearLayout) itemView.findViewById(R.id.liner_meg);
                linear_goods = (LinearLayout) itemView.findViewById(R.id.linear_goods);
                lin_delete = (LinearLayout) itemView.findViewById(R.id.lin_delete);
                linear_state = (LinearLayout) itemView.findViewById(R.id.linear_state);

                 tvShuilvNe2=(TextView) itemView.findViewById(R.id.tv_shuilv_ne2);
                 tvShuilvM2=(TextView) itemView.findViewById(R.id.tv_shuilv_m2);
                 tvShuilvBr2=(TextView) itemView.findViewById(R.id.tv_shuilv_br2);
                 tvTotalNetto=(TextView) itemView.findViewById(R.id.tv_total_Netto);
                 tvTotalMwst=(TextView) itemView.findViewById(R.id.tv_total_Mwst);
                 tvTotalBru=(TextView) itemView.findViewById(R.id.tv_total_Bru);
                 linerHdong=(LinearLayout) itemView.findViewById(R.id.liner_hdong2);
                liner_shui_root=(LinearLayout) itemView.findViewById(R.id.liner_shui_root);
                 lin_top=(View) itemView.findViewById(R.id.lin_top);
                 lin_bottom=(View) itemView.findViewById(R.id.lin_bottom);


            }
        }
    }
}
