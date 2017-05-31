package com.abcs.haiwaigou.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.OrderRecyclerAdapter;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.model.Orders;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.utils.RecyclerViewAndSwipeRefreshLayout;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.haiwaigou.view.recyclerview.EndlessRecyclerOnScrollListener;
import com.abcs.haiwaigou.view.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.abcs.haiwaigou.view.recyclerview.LoadingFooter;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.haiwaigou.view.recyclerview.RecyclerViewStateUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.ButterKnife;

/**
 * Created by zjz on 2016/1/16.
 */
public class OrderPayFragment extends BaseFragment implements RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh{

//    GoodsDetailCommentActivity commentactivity;
    Activity activity;
    View rootView;
    ArrayList<Goods> goodsList = new ArrayList<Goods>();
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView imgNull;
    TextView tvNull;
    RelativeLayout layoutNull;


    private View view;
    int totalPage;
    int currentPage = 1;
    boolean isLoadMore = false;
    boolean first = false;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    private RequestQueue mRequestQueue;
    String type;
    String goods_id;
    OrderRecyclerAdapter orderRecyclerAdapter;
    ArrayList<Orders> orderses = new ArrayList<Orders>();
    MyBroadCastReceiver myBroadCastReceiver;
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
//        commentactivity= (GoodsDetailCommentActivity) getActivity();
        view =  activity.getLayoutInflater().inflate(
                R.layout.hwg_fragment_comment_item, null);
        mRequestQueue = Volley.newRequestQueue(activity);
        myBroadCastReceiver = new MyBroadCastReceiver(activity, updateUI);
        myBroadCastReceiver.register();
        initView();
//        initRecycler();
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
//                initListView();
                isLoadMore = false;
                currentPage = 1;
                initRecycler();
                Log.i("zjz", "更新订单");
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initView() {
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(activity));
        layoutNull= (RelativeLayout) view.findViewById(R.id.layout_null);
    }

    public void initRecycler() {
        recyclerView.addOnScrollListener(mOnScrollListener);
        initAllDates();
        orderRecyclerAdapter = new OrderRecyclerAdapter(activity);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(orderRecyclerAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(activity, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
    }

    private void initAllDates() {
        if(!first){
            ProgressDlgUtil.showProgressDlg("Loading...", activity);
        }
        final ArrayList<Orders> dataList = new ArrayList<>();

        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_order2 + "&key=" + MyApplication.getInstance().getMykey() + "&order_sn=&query_start_date=&query_end_date=&state_type=state_new&recycle=" + "&curpage=" + currentPage, null, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject response = new JSONObject(msg);
                            Log.i("zjz", "msg=" + msg);
                            if (response.getInt("code") == 200) {
                                Log.i("zjz", "待支付:连接成功");
                                totalPage = response.getInt("page_total");
                                Log.i("zjz", "totalpage=" + totalPage);
                                Log.i("zjz", "response=" + response);
//                        payList.clear();
//                        orderRecyclerAdapter.getPayList().clear();
                                JSONObject object = response.getJSONObject("datas");
                                JSONObject groupObject = object.optJSONObject("order_group_list");
                                if (groupObject != null) {
                                    if (isLoadMore) {
                                        int currentSize = orderRecyclerAdapter.getItemCount();
                                        Iterator keys = groupObject.keys();
                                        String key;
                                        JSONObject value;
                                        int id = 0;
                                        while (keys.hasNext()) {
                                            key = (String) keys.next();
                                            value = (JSONObject) groupObject.get(key);
                                            Log.i("zjz", "value=" + value);

                                            Orders orders = new Orders();
                                            Log.i("zjz", "id=" + (currentSize + id));
                                            orders.setId(currentSize + id);
                                            id++;
                                            orders.setPay_amount(value.optString("pay_amount", ""));
                                            orders.setAdd_time(value.optLong("add_time"));
                                            JSONObject payinfo = value.getJSONObject("pay_info");
                                            orders.setPay_sn(payinfo.optString("pay_sn"));

                                            ArrayList<Orders.OrderListEntity> orderListEntities = new ArrayList<Orders.OrderListEntity>();
                                            JSONArray orderArray = value.getJSONArray("order_list");
                                            for (int j = 0; j < orderArray.length(); j++) {
                                                JSONObject orderObject = orderArray.getJSONObject(j);
                                                Orders.OrderListEntity orderListEntity = new Orders.OrderListEntity();
                                                orderListEntity.setPay_sn(orderObject.optString("pay_sn"));
                                                orderListEntity.setOrder_id(orderObject.optString("order_id"));
                                                orderListEntity.setOrder_sn(orderObject.optString("order_sn"));
                                                orderListEntity.setStore_id(orderObject.optString("store_id"));
                                                orderListEntity.setStore_name(orderObject.optString("store_name"));
                                                orderListEntity.setBuyer_id(orderObject.optString("buyer_id"));
                                                orderListEntity.setBuyer_name(orderObject.optString("buyer_name"));
                                                orderListEntity.setBuyer_email(orderObject.optString("buyer_email"));
                                                orderListEntity.setFinnshed_time(orderObject.getLong("finnshed_time"));
                                                orderListEntity.setEvaluation_state(orderObject.optString("evaluation_state"));
                                                orderListEntity.setOrder_state(orderObject.optString("order_state"));
                                                orderListEntity.setRefund_state(orderObject.optString("refund_state"));
                                                orderListEntity.setLock_state(orderObject.optString("lock_state"));
                                                orderListEntity.setState_desc(orderObject.optString("state_desc"));
                                                orderListEntity.setIf_cancel(orderObject.optBoolean("if_cancel"));
                                                orderListEntity.setIf_receive(orderObject.optBoolean("if_receive"));
                                                orderListEntity.setIf_lock(orderObject.optBoolean("if_lock"));
                                                orderListEntity.setIf_deliver(orderObject.optBoolean("if_deliver"));
                                                orderListEntity.setIf_refund_cancel(orderObject.optBoolean("if_refund_cancel"));
                                                orderListEntity.setIf_complain(orderObject.optBoolean("if_complain"));
                                                orderListEntity.setIf_evaluation(orderObject.optBoolean("if_evaluation"));
                                                orderListEntity.setIf_delete(orderObject.optBoolean("if_delete"));
                                                orderListEntity.setIf_drop(orderObject.optBoolean("if_drop"));
                                                orderListEntity.setIf_restore(orderObject.optBoolean("if_restore"));
                                                orderListEntity.setGoods_amount(orderObject.optString("goods_amount"));
                                                orderListEntity.setOrder_amount(orderObject.optString("order_amount"));

                                                ArrayList<Orders.OrderListEntity.ExtendOrderGoodsEntity> extendOrderGoodsEntities = new ArrayList<Orders.OrderListEntity.ExtendOrderGoodsEntity>();
                                                JSONArray goodsArray = orderObject.getJSONArray("goods_list");
                                                for (int l = 0; l < goodsArray.length(); l++) {
                                                    JSONObject goodsObject = goodsArray.getJSONObject(l);
                                                    Goods goods = new Goods();
                                                    Orders.OrderListEntity.ExtendOrderGoodsEntity extendOrderGoodsEntity = new Orders.OrderListEntity.ExtendOrderGoodsEntity();
                                                    extendOrderGoodsEntity.setOrder_id(goodsObject.optString("order_id"));
                                                    extendOrderGoodsEntity.setGoods_id(goodsObject.optString("goods_id"));
                                                    extendOrderGoodsEntity.setGoods_name(goodsObject.optString("goods_name"));
                                                    extendOrderGoodsEntity.setGoods_price(goodsObject.optString("goods_price"));
                                                    extendOrderGoodsEntity.setGoods_num(goodsObject.optString("goods_num"));
                                                    extendOrderGoodsEntity.setGoods_image_url(goodsObject.optString("image_240_url"));
                                                    extendOrderGoodsEntity.setGc_id(goodsObject.optString("gc_id"));
                                                    extendOrderGoodsEntity.setRec_id(goodsObject.optString("rec_id"));
                                                    extendOrderGoodsEntity.setRefund(goodsObject.optString("refund"));

                                                    extendOrderGoodsEntities.add(extendOrderGoodsEntity);
                                                }
                                                orderListEntity.setExtend_order_goods(extendOrderGoodsEntities);
                                                orderListEntities.add(orderListEntity);
                                            }
                                            orders.setOrder_list(orderListEntities);
                                            orderRecyclerAdapter.getOrderses().add(orders);
                                            dataList.add(orders);
                                        }

                                        addItems(dataList);

                                    } else {
                                        orderses.clear();
                                        orderRecyclerAdapter.getList().clear();
                                        orderRecyclerAdapter.getOrderses().clear();
                                        dataList.clear();
                                        Iterator keys = groupObject.keys();
                                        Log.i("zjz", "keys=" + keys.toString());
                                        String key;
                                        JSONObject value;
                                        int id = 0;
                                        while (keys.hasNext()) {

                                            key = (String) keys.next();
                                            value = (JSONObject) groupObject.get(key);
                                            Log.i("zjz", "value=" + value);

                                            Orders orders = new Orders();
                                            Log.i("zjz", "id=" + id);
                                            orders.setId(id);
                                            id++;
                                            orders.setPay_amount(value.optString("pay_amount"));
                                            orders.setAdd_time(value.optLong("add_time"));
                                            JSONObject payinfo = value.getJSONObject("pay_info");
                                            orders.setPay_sn(payinfo.optString("pay_sn"));

                                            ArrayList<Orders.OrderListEntity> orderListEntities = new ArrayList<Orders.OrderListEntity>();
                                            JSONArray orderArray = value.getJSONArray("order_list");
                                            for (int j = 0; j < orderArray.length(); j++) {
                                                JSONObject orderObject = orderArray.getJSONObject(j);
                                                Orders.OrderListEntity orderListEntity = new Orders.OrderListEntity();
                                                orderListEntity.setPay_sn(orderObject.optString("pay_sn"));
                                                orderListEntity.setOrder_id(orderObject.optString("order_id"));
                                                orderListEntity.setOrder_sn(orderObject.optString("order_sn"));
                                                orderListEntity.setStore_id(orderObject.optString("store_id"));
                                                orderListEntity.setStore_name(orderObject.optString("store_name"));
                                                orderListEntity.setBuyer_id(orderObject.optString("buyer_id"));
                                                orderListEntity.setBuyer_name(orderObject.optString("buyer_name"));
                                                orderListEntity.setBuyer_email(orderObject.optString("buyer_email"));
                                                orderListEntity.setFinnshed_time(orderObject.getLong("finnshed_time"));
                                                orderListEntity.setEvaluation_state(orderObject.optString("evaluation_state"));
                                                orderListEntity.setOrder_state(orderObject.optString("order_state"));
                                                orderListEntity.setRefund_state(orderObject.optString("refund_state"));
                                                orderListEntity.setLock_state(orderObject.optString("lock_state"));
                                                orderListEntity.setState_desc(orderObject.optString("state_desc"));
                                                orderListEntity.setIf_cancel(orderObject.optBoolean("if_cancel"));
                                                orderListEntity.setIf_receive(orderObject.optBoolean("if_receive"));
                                                orderListEntity.setIf_lock(orderObject.optBoolean("if_lock"));
                                                orderListEntity.setIf_deliver(orderObject.optBoolean("if_deliver"));
                                                orderListEntity.setIf_refund_cancel(orderObject.optBoolean("if_refund_cancel"));
                                                orderListEntity.setIf_complain(orderObject.optBoolean("if_complain"));
                                                orderListEntity.setIf_evaluation(orderObject.optBoolean("if_evaluation"));
                                                orderListEntity.setIf_delete(orderObject.optBoolean("if_delete"));
                                                orderListEntity.setIf_drop(orderObject.optBoolean("if_drop"));
                                                orderListEntity.setIf_restore(orderObject.optBoolean("if_restore"));
                                                orderListEntity.setGoods_amount(orderObject.optString("goods_amount"));
                                                orderListEntity.setOrder_amount(orderObject.optString("order_amount"));
                                                ArrayList<Orders.OrderListEntity.ExtendOrderGoodsEntity> extendOrderGoodsEntities = new ArrayList<Orders.OrderListEntity.ExtendOrderGoodsEntity>();
                                                JSONArray goodsArray = orderObject.getJSONArray("goods_list");
                                                for (int l = 0; l < goodsArray.length(); l++) {
                                                    JSONObject goodsObject = goodsArray.getJSONObject(l);
                                                    Goods goods = new Goods();
                                                    Orders.OrderListEntity.ExtendOrderGoodsEntity extendOrderGoodsEntity = new Orders.OrderListEntity.ExtendOrderGoodsEntity();
                                                    extendOrderGoodsEntity.setOrder_id(goodsObject.optString("order_id"));
                                                    extendOrderGoodsEntity.setGoods_id(goodsObject.optString("goods_id"));
                                                    extendOrderGoodsEntity.setGoods_name(goodsObject.optString("goods_name"));
                                                    extendOrderGoodsEntity.setGoods_price(goodsObject.optString("goods_price"));
                                                    extendOrderGoodsEntity.setGoods_num(goodsObject.optString("goods_num"));
                                                    extendOrderGoodsEntity.setGoods_image_url(goodsObject.optString("image_240_url"));
                                                    extendOrderGoodsEntity.setGc_id(goodsObject.optString("gc_id"));
                                                    extendOrderGoodsEntity.setRec_id(goodsObject.optString("rec_id"));
                                                    extendOrderGoodsEntity.setRefund(goodsObject.optString("refund"));

                                                    extendOrderGoodsEntities.add(extendOrderGoodsEntity);
                                                }
                                                orderListEntity.setExtend_order_goods(extendOrderGoodsEntities);
                                                orderListEntities.add(orderListEntity);
                                            }
                                            orders.setOrder_list(orderListEntities);
                                            orderRecyclerAdapter.getOrderses().add(orders);
                                            dataList.add(orders);
                                        }
                                        mCurrentCounter = dataList.size();
                                        orderRecyclerAdapter.addItems(dataList);
                                        orderRecyclerAdapter.notifyDataSetChanged();
                                    }
                                }
                                mHasLoadedOnce=true;
                                if (layoutNull != null)
                                    layoutNull.setVisibility(orderRecyclerAdapter.getList().size() == 0 ? View.VISIBLE : View.GONE);

                            } else {
                                Log.i("zjz", "goodsDetail:解析失败");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
                        } finally {
                            recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });

//        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, TLUrl.getInstance().URL_hwg_order2 + "&key=" + MyApplication.getInstance().getMykey() + "&order_sn=&query_start_date=&query_end_date=&state_type=state_new&recycle=" + "&curpage=" + currentPage, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    if (response.getInt("code") == 200) {
//                        Log.i("zjz", "待支付:连接成功");
//                        totalPage = response.getInt("page_total");
//                        Log.i("zjz", "totalpage=" + totalPage);
//                        Log.i("zjz", "response=" + response);
////                        payList.clear();
////                        orderRecyclerAdapter.getPayList().clear();
//                        JSONObject object = response.getJSONObject("datas");
//                        JSONObject groupObject = object.optJSONObject("order_group_list");
//                        if(groupObject!=null){
//                            if (isLoadMore) {
//                                int currentSize = orderRecyclerAdapter.getItemCount();
//                                Iterator keys = groupObject.keys();
//                                String key;
//                                JSONObject value;
//                                int id=0;
//                                while (keys.hasNext()) {
//                                    key = (String) keys.next();
//                                    value = (JSONObject) groupObject.get(key);
//                                    Log.i("zjz", "value=" + value);
//
//                                    Orders orders = new Orders();
//                                    Log.i("zjz","id="+(currentSize+id));
//                                    orders.setId(currentSize + id);
//                                    id++;
//                                    orders.setPay_amount(value.optString("pay_amount",""));
//                                    orders.setAdd_time(value.optLong("add_time"));
//                                    JSONObject payinfo = value.getJSONObject("pay_info");
//                                    orders.setPay_sn(payinfo.optString("pay_sn"));
//
//                                    ArrayList<Orders.OrderListEntity> orderListEntities = new ArrayList<Orders.OrderListEntity>();
//                                    JSONArray orderArray = value.getJSONArray("order_list");
//                                    for (int j = 0; j < orderArray.length(); j++) {
//                                        JSONObject orderObject = orderArray.getJSONObject(j);
//                                        Orders.OrderListEntity orderListEntity = new Orders.OrderListEntity();
//                                        orderListEntity.setPay_sn(orderObject.optString("pay_sn"));
//                                        orderListEntity.setOrder_id(orderObject.optString("order_id"));
//                                        orderListEntity.setOrder_sn(orderObject.optString("order_sn"));
//                                        orderListEntity.setStore_id(orderObject.optString("store_id"));
//                                        orderListEntity.setStore_name(orderObject.optString("store_name"));
//                                        orderListEntity.setBuyer_id(orderObject.optString("buyer_id"));
//                                        orderListEntity.setBuyer_name(orderObject.optString("buyer_name"));
//                                        orderListEntity.setBuyer_email(orderObject.optString("buyer_email"));
//                                        orderListEntity.setFinnshed_time(orderObject.getLong("finnshed_time"));
//                                        orderListEntity.setEvaluation_state(orderObject.optString("evaluation_state"));
//                                        orderListEntity.setOrder_state(orderObject.optString("order_state"));
//                                        orderListEntity.setRefund_state(orderObject.optString("refund_state"));
//                                        orderListEntity.setLock_state(orderObject.optString("lock_state"));
//                                        orderListEntity.setState_desc(orderObject.optString("state_desc"));
//                                        orderListEntity.setIf_cancel(orderObject.optBoolean("if_cancel"));
//                                        orderListEntity.setIf_receive(orderObject.optBoolean("if_receive"));
//                                        orderListEntity.setIf_lock(orderObject.optBoolean("if_lock"));
//                                        orderListEntity.setIf_deliver(orderObject.optBoolean("if_deliver"));
//                                        orderListEntity.setIf_refund_cancel(orderObject.optBoolean("if_refund_cancel"));
//                                        orderListEntity.setIf_complain(orderObject.optBoolean("if_complain"));
//                                        orderListEntity.setIf_evaluation(orderObject.optBoolean("if_evaluation"));
//                                        orderListEntity.setIf_delete(orderObject.optBoolean("if_delete"));
//                                        orderListEntity.setIf_drop(orderObject.optBoolean("if_drop"));
//                                        orderListEntity.setIf_restore(orderObject.optBoolean("if_restore"));
//                                        orderListEntity.setGoods_amount(orderObject.optString("goods_amount"));
//                                        orderListEntity.setOrder_amount(orderObject.optString("order_amount"));
//
//                                        ArrayList<Orders.OrderListEntity.ExtendOrderGoodsEntity> extendOrderGoodsEntities = new ArrayList<Orders.OrderListEntity.ExtendOrderGoodsEntity>();
//                                        JSONArray goodsArray = orderObject.getJSONArray("goods_list");
//                                        for (int l = 0; l < goodsArray.length(); l++) {
//                                            JSONObject goodsObject = goodsArray.getJSONObject(l);
//                                            Goods goods = new Goods();
//                                            Orders.OrderListEntity.ExtendOrderGoodsEntity extendOrderGoodsEntity = new Orders.OrderListEntity.ExtendOrderGoodsEntity();
//                                            extendOrderGoodsEntity.setOrder_id(goodsObject.optString("order_id"));
//                                            extendOrderGoodsEntity.setGoods_id(goodsObject.optString("goods_id"));
//                                            extendOrderGoodsEntity.setGoods_name(goodsObject.optString("goods_name"));
//                                            extendOrderGoodsEntity.setGoods_price(goodsObject.optString("goods_price"));
//                                            extendOrderGoodsEntity.setGoods_num(goodsObject.optString("goods_num"));
//                                            extendOrderGoodsEntity.setGoods_image_url(goodsObject.optString("image_240_url"));
//                                            extendOrderGoodsEntity.setGc_id(goodsObject.optString("gc_id"));
//                                            extendOrderGoodsEntity.setRec_id(goodsObject.optString("rec_id"));
//                                            extendOrderGoodsEntity.setRefund(goodsObject.optString("refund"));
//
//                                            extendOrderGoodsEntities.add(extendOrderGoodsEntity);
//                                        }
//                                        orderListEntity.setExtend_order_goods(extendOrderGoodsEntities);
//                                        orderListEntities.add(orderListEntity);
//                                    }
//                                    orders.setOrder_list(orderListEntities);
//                                    orderRecyclerAdapter.getOrderses().add(orders);
//                                    dataList.add(orders);
//                                }
//
//                                addItems(dataList);
//
//                            } else {
//                                orderses.clear();
//                                orderRecyclerAdapter.getList().clear();
//                                orderRecyclerAdapter.getOrderses().clear();
//                                dataList.clear();
//                                Iterator keys = groupObject.keys();
//                                Log.i("zjz","keys="+keys.toString());
//                                String key;
//                                JSONObject value;
//                                int id=0;
//                                while (keys.hasNext()) {
//
//                                    key = (String) keys.next();
//                                    value = (JSONObject) groupObject.get(key);
//                                    Log.i("zjz", "value=" + value);
//
//                                    Orders orders = new Orders();
//                                    Log.i("zjz","id="+id);
//                                    orders.setId(id);
//                                    id++;
//                                    orders.setPay_amount(value.optString("pay_amount"));
//                                    orders.setAdd_time(value.optLong("add_time"));
//                                    JSONObject payinfo = value.getJSONObject("pay_info");
//                                    orders.setPay_sn(payinfo.optString("pay_sn"));
//
//                                    ArrayList<Orders.OrderListEntity> orderListEntities = new ArrayList<Orders.OrderListEntity>();
//                                    JSONArray orderArray = value.getJSONArray("order_list");
//                                    for (int j = 0; j < orderArray.length(); j++) {
//                                        JSONObject orderObject = orderArray.getJSONObject(j);
//                                        Orders.OrderListEntity orderListEntity = new Orders.OrderListEntity();
//                                        orderListEntity.setPay_sn(orderObject.optString("pay_sn"));
//                                        orderListEntity.setOrder_id(orderObject.optString("order_id"));
//                                        orderListEntity.setOrder_sn(orderObject.optString("order_sn"));
//                                        orderListEntity.setStore_id(orderObject.optString("store_id"));
//                                        orderListEntity.setStore_name(orderObject.optString("store_name"));
//                                        orderListEntity.setBuyer_id(orderObject.optString("buyer_id"));
//                                        orderListEntity.setBuyer_name(orderObject.optString("buyer_name"));
//                                        orderListEntity.setBuyer_email(orderObject.optString("buyer_email"));
//                                        orderListEntity.setFinnshed_time(orderObject.getLong("finnshed_time"));
//                                        orderListEntity.setEvaluation_state(orderObject.optString("evaluation_state"));
//                                        orderListEntity.setOrder_state(orderObject.optString("order_state"));
//                                        orderListEntity.setRefund_state(orderObject.optString("refund_state"));
//                                        orderListEntity.setLock_state(orderObject.optString("lock_state"));
//                                        orderListEntity.setState_desc(orderObject.optString("state_desc"));
//                                        orderListEntity.setIf_cancel(orderObject.optBoolean("if_cancel"));
//                                        orderListEntity.setIf_receive(orderObject.optBoolean("if_receive"));
//                                        orderListEntity.setIf_lock(orderObject.optBoolean("if_lock"));
//                                        orderListEntity.setIf_deliver(orderObject.optBoolean("if_deliver"));
//                                        orderListEntity.setIf_refund_cancel(orderObject.optBoolean("if_refund_cancel"));
//                                        orderListEntity.setIf_complain(orderObject.optBoolean("if_complain"));
//                                        orderListEntity.setIf_evaluation(orderObject.optBoolean("if_evaluation"));
//                                        orderListEntity.setIf_delete(orderObject.optBoolean("if_delete"));
//                                        orderListEntity.setIf_drop(orderObject.optBoolean("if_drop"));
//                                        orderListEntity.setIf_restore(orderObject.optBoolean("if_restore"));
//                                        orderListEntity.setGoods_amount(orderObject.optString("goods_amount"));
//                                        orderListEntity.setOrder_amount(orderObject.optString("order_amount"));
//                                        ArrayList<Orders.OrderListEntity.ExtendOrderGoodsEntity> extendOrderGoodsEntities = new ArrayList<Orders.OrderListEntity.ExtendOrderGoodsEntity>();
//                                        JSONArray goodsArray = orderObject.getJSONArray("goods_list");
//                                        for (int l = 0; l < goodsArray.length(); l++) {
//                                            JSONObject goodsObject = goodsArray.getJSONObject(l);
//                                            Goods goods = new Goods();
//                                            Orders.OrderListEntity.ExtendOrderGoodsEntity extendOrderGoodsEntity = new Orders.OrderListEntity.ExtendOrderGoodsEntity();
//                                            extendOrderGoodsEntity.setOrder_id(goodsObject.optString("order_id"));
//                                            extendOrderGoodsEntity.setGoods_id(goodsObject.optString("goods_id"));
//                                            extendOrderGoodsEntity.setGoods_name(goodsObject.optString("goods_name"));
//                                            extendOrderGoodsEntity.setGoods_price(goodsObject.optString("goods_price"));
//                                            extendOrderGoodsEntity.setGoods_num(goodsObject.optString("goods_num"));
//                                            extendOrderGoodsEntity.setGoods_image_url(goodsObject.optString("image_240_url"));
//                                            extendOrderGoodsEntity.setGc_id(goodsObject.optString("gc_id"));
//                                            extendOrderGoodsEntity.setRec_id(goodsObject.optString("rec_id"));
//                                            extendOrderGoodsEntity.setRefund(goodsObject.optString("refund"));
//
//                                            extendOrderGoodsEntities.add(extendOrderGoodsEntity);
//                                        }
//                                        orderListEntity.setExtend_order_goods(extendOrderGoodsEntities);
//                                        orderListEntities.add(orderListEntity);
//                                    }
//                                    orders.setOrder_list(orderListEntities);
//                                    orderRecyclerAdapter.getOrderses().add(orders);
//                                    dataList.add(orders);
//                                }
//                                mCurrentCounter = dataList.size();
//                                orderRecyclerAdapter.addItems(dataList);
//                                orderRecyclerAdapter.notifyDataSetChanged();
//                            }
//                        }
//                        if(layoutNull!=null)
//                            layoutNull.setVisibility(orderRecyclerAdapter.getList().size()==0?View.VISIBLE:View.GONE);
//                    }
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    Log.i("zjz", e.toString());
//                    e.printStackTrace();
//                }finally {
//                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
//                    ProgressDlgUtil.stopProgressDlg();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        });
//        mRequestQueue.add(jr);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeAllViewsInLayout();
        ButterKnife.inject(this, view);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void swipeRefreshLayoutOnRefresh() {
        first=true;
        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(true);
        isLoadMore = false;
        currentPage = 1;
        initAllDates();
    }

    private MyHandler handler=new MyHandler();
    private int mCurrentCounter = 0;

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Orders> list) {

        orderRecyclerAdapter.addItems(list);
        mCurrentCounter += list.size();
    }

    private static final int REQUEST_COUNT = 15;
    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(recyclerView);
            if (state == LoadingFooter.State.Loading) {
                Log.d("@Cundong", "the state is Loading, just wait..");
                return;
            }

            if (currentPage < totalPage) {
                // loading more
                RecyclerViewStateUtils.setFooterViewState(getActivity(), recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(getActivity(), recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }

    };

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }

        initRecycler();
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    isLoadMore = true;
                    Log.i("zjz", "totalPage=" + totalPage);
                    if (currentPage < totalPage && isLoadMore) {
                        currentPage += 1;
                        Log.i("zjz", "current=" + currentPage);
                        RecyclerViewStateUtils.setFooterViewState(recyclerView, LoadingFooter.State.Normal);
                        initAllDates();
                    }
                    break;
                case -2:
                    notifyDataSetChanged();
                    break;
                case -3:
                    RecyclerViewStateUtils.setFooterViewState(activity, recyclerView, REQUEST_COUNT, LoadingFooter.State.NetWorkError, mFooterClick);
                    break;
            }
        }
    }


    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(getActivity(), recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
            requestData();
        }
    };

    /**
     * 模拟请求网络
     */
    private void requestData() {

        new Thread() {

            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //模拟一下网络请求失败的情况
                if (NetworkUtils.isNetAvailable(getContext())) {
                    Log.i("zjz","有网络");
//                    mHandler.sendEmptyMessage(-1);
                    handler.sendEmptyMessage(-1);

                } else {
//                    mHandler.sendEmptyMessage(-3);
                    handler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }
    @Override
    public void onDestroy() {
        myBroadCastReceiver.unRegister();
        super.onDestroy();
    }
}
