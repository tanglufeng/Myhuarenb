package com.abcs.haiwaigou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.OrderAdapter;
import com.abcs.haiwaigou.adapter.OrderRecyclerAdapter;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.model.Orders;
import com.abcs.haiwaigou.model.PayWay;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.utils.RecyclerViewAndSwipeRefreshLayout;
import com.abcs.haiwaigou.view.recyclerview.EndlessRecyclerOnScrollListener;
import com.abcs.haiwaigou.view.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.abcs.haiwaigou.view.recyclerview.LoadingFooter;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.haiwaigou.view.recyclerview.RecyclerViewStateUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OrderActivity2 extends BaseActivity implements View.OnClickListener, RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh {

    @InjectView(R.id.tljr_txt_news_title)
    TextView tljrTxtNewsTitle;
    @InjectView(R.id.tljr_hqss_news_titlebelow)
    TextView tljrHqssNewsTitlebelow;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.tljr_grp_goods_title)
    RelativeLayout tljrGrpGoodsTitle;
    //    @InjectView(R.id.tljr_zListView)
//    ZrcListView tljrZListView;
//    @InjectView(R.id.allGoods_content_fragment)
//    FrameLayout allGoodsContentFragment;
    @InjectView(R.id.img_null)
    ImageView imgNull;
    @InjectView(R.id.tv_null)
    TextView tvNull;
    @InjectView(R.id.tv_null2)
    TextView tvNull2;
    @InjectView(R.id.layout_null)
    RelativeLayout layoutNull;
    public Handler handler = new Handler();
    OrderAdapter orderAdapter;
    ArrayList<PayWay> payList = new ArrayList<PayWay>();
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    //    @InjectView(R.id.swipeRefreshLayout)
//    SwipeRefreshLayout swipeRefreshLayout;
    private RequestQueue mRequestQueue;

    ArrayList<Orders> orderses = new ArrayList<Orders>();
    MyBroadCastReceiver myBroadCastReceiver;
    private View view;
    int totalPage;
    int currentPage = 1;
    boolean isLoadMore = false;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    OrderRecyclerAdapter orderRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_activity_order, null);
        }
        setContentView(view);
        ButterKnife.inject(this);
        myBroadCastReceiver = new MyBroadCastReceiver(this, updateUI);
        myBroadCastReceiver.register();
        mRequestQueue = Volley.newRequestQueue(this);
        initRecyclerView();
//        initListView();
        setOnListener();
    }

    private void initRecyclerView() {
        recyclerView.addOnScrollListener(mOnScrollListener);
        initAllDates2();
        orderRecyclerAdapter = new OrderRecyclerAdapter(this);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(orderRecyclerAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(this, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);

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
                initRecyclerView();
                Log.i("zjz", "更新订单");
                /********退款成功，更新数据***********/
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void setOnListener() {
        findViewById(R.id.tljr_img_news_back).setOnClickListener(this);
    }

    private void initAllDates2() {
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        final ArrayList<Orders> dataList = new ArrayList<>();
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, TLUrl.getInstance().URL_hwg_order2 + "&key=" + MyApplication.getInstance().getMykey() + "&order_sn=&query_start_date=&query_end_date=&state_type=&recycle=" + "&curpage=" + currentPage, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "order:连接成功");
                        totalPage = response.getInt("page_total");
                        Log.i("zjz", "totalpage=" + totalPage);
                        Log.i("zjz", "response=" + response);
//                        payList.clear();
//                        orderRecyclerAdapter.getPayList().clear();
                        JSONObject object = response.getJSONObject("datas");
                        JSONObject groupObject = object.optJSONObject("order_group_list");
                        if(groupObject!=null){
                            if (isLoadMore) {
                                int currentSize = orderRecyclerAdapter.getItemCount();
                                Iterator keys = groupObject.keys();
                                String key;
                                JSONObject value;
                                int id=0;
                                while (keys.hasNext()) {
                                    key = (String) keys.next();
                                    value = (JSONObject) groupObject.get(key);
                                    Log.i("zjz", "value=" + value);

                                    Orders orders = new Orders();
                                    Log.i("zjz","id="+(currentSize+id));
                                    orders.setId(currentSize + id);
                                    id++;
                                    orders.setPay_amount(value.optString("pay_amount",""));
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
                                Log.i("zjz","keys="+keys.toString());
                                String key;
                                JSONObject value;
                                int id=0;
                                while (keys.hasNext()) {

                                    key = (String) keys.next();
                                    value = (JSONObject) groupObject.get(key);
                                    Log.i("zjz", "value=" + value);

                                    Orders orders = new Orders();
                                    Log.i("zjz","id="+id);
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

                            Log.i("zjz", "orders=" + orderses.size());
                            if (orderRecyclerAdapter.getList().size() == 0) {
                                layoutNull.setVisibility(View.VISIBLE);
                            } else {
                                layoutNull.setVisibility(View.GONE);
                            }

                            recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                        }else {
                            layoutNull.setVisibility(View.VISIBLE);
                        }


                    } else {
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                        Log.i("zjz", "goodsActivity解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                    e.printStackTrace();
                }finally {
                    ProgressDlgUtil.stopProgressDlg();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mRequestQueue.add(jr);

    }

    private void initAllDates() {
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        final ArrayList<Orders> dataList = new ArrayList<>();
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, TLUrl.getInstance().URL_hwg_order + "&curpage=" + currentPage + "&getpayment=true" + "&key=" + MyApplication.getInstance().getMykey(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "order:连接成功");
                        totalPage = response.getInt("page_total");
                        Log.i("zjz", "totalpage=" + totalPage);
                        Log.i("zjz", "response=" + response);
                        payList.clear();
                        orderRecyclerAdapter.getPayList().clear();
                        JSONObject object = response.getJSONObject("datas");
                        JSONArray groupArray = object.getJSONArray("order_group_list");
                        if (isLoadMore) {
                            int currentSize = orderRecyclerAdapter.getItemCount();
                            for (int i = 0; i < groupArray.length(); i++) {
                                Orders orders = new Orders();
                                orders.setId(currentSize + i);
                                JSONObject object1 = groupArray.getJSONObject(i);
                                orders.setPay_amount(object1.optString("pay_amount"));
                                orders.setAdd_time(object1.optLong("add_time"));
                                orders.setPay_sn(object1.optString("pay_sn"));
                                ArrayList<Orders.OrderListEntity> orderListEntities = new ArrayList<Orders.OrderListEntity>();
                                JSONArray orderArray = object1.getJSONArray("order_list");
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
                                    orderListEntity.setState_desc(orderObject.optString("state_desc"));
                                    orderListEntity.setIf_cancel(orderObject.optBoolean("if_cancel"));
                                    orderListEntity.setIf_receive(orderObject.optBoolean("if_receive"));
                                    orderListEntity.setIf_lock(orderObject.optBoolean("if_lock"));
                                    orderListEntity.setIf_deliver(orderObject.optBoolean("if_deliver"));
                                    orderListEntity.setGoods_amount(orderObject.optString("goods_amount"));

                                    ArrayList<Orders.OrderListEntity.ExtendOrderGoodsEntity> extendOrderGoodsEntities = new ArrayList<Orders.OrderListEntity.ExtendOrderGoodsEntity>();
                                    JSONArray goodsArray = orderObject.getJSONArray("extend_order_goods");
                                    for (int l = 0; l < goodsArray.length(); l++) {
                                        JSONObject goodsObject = goodsArray.getJSONObject(l);
                                        Goods goods = new Goods();
                                        Orders.OrderListEntity.ExtendOrderGoodsEntity extendOrderGoodsEntity = new Orders.OrderListEntity.ExtendOrderGoodsEntity();
                                        extendOrderGoodsEntity.setOrder_id(goodsObject.optString("order_id"));
                                        extendOrderGoodsEntity.setGoods_id(goodsObject.optString("goods_id"));
                                        extendOrderGoodsEntity.setGoods_name(goodsObject.optString("goods_name"));
                                        extendOrderGoodsEntity.setGoods_price(goodsObject.optString("goods_price"));
                                        extendOrderGoodsEntity.setGoods_num(goodsObject.optString("goods_num"));
                                        extendOrderGoodsEntity.setGoods_image_url(goodsObject.optString("goods_image_url"));
                                        extendOrderGoodsEntity.setGc_id(goodsObject.optString("gc_id"));
                                        extendOrderGoodsEntity.setRec_id(goodsObject.optString("rec_id"));
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
                            for (int i = 0; i < groupArray.length(); i++) {
                                Orders orders = new Orders();
                                orders.setId(i);
                                JSONObject object1 = groupArray.getJSONObject(i);
                                orders.setPay_amount(object1.optString("pay_amount"));
                                orders.setAdd_time(object1.optLong("add_time"));
                                orders.setPay_sn(object1.optString("pay_sn"));
                                ArrayList<Orders.OrderListEntity> orderListEntities = new ArrayList<Orders.OrderListEntity>();
                                JSONArray orderArray = object1.getJSONArray("order_list");
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
                                    orderListEntity.setState_desc(orderObject.optString("state_desc"));
                                    orderListEntity.setIf_cancel(orderObject.optBoolean("if_cancel"));
                                    orderListEntity.setIf_receive(orderObject.optBoolean("if_receive"));
                                    orderListEntity.setIf_lock(orderObject.optBoolean("if_lock"));
                                    orderListEntity.setIf_deliver(orderObject.optBoolean("if_deliver"));
                                    orderListEntity.setGoods_amount(orderObject.optString("goods_amount"));

                                    ArrayList<Orders.OrderListEntity.ExtendOrderGoodsEntity> extendOrderGoodsEntities = new ArrayList<Orders.OrderListEntity.ExtendOrderGoodsEntity>();
                                    JSONArray goodsArray = orderObject.getJSONArray("extend_order_goods");
                                    for (int l = 0; l < goodsArray.length(); l++) {
                                        JSONObject goodsObject = goodsArray.getJSONObject(l);
                                        Goods goods = new Goods();
                                        Orders.OrderListEntity.ExtendOrderGoodsEntity extendOrderGoodsEntity = new Orders.OrderListEntity.ExtendOrderGoodsEntity();
                                        extendOrderGoodsEntity.setOrder_id(goodsObject.optString("order_id"));
                                        extendOrderGoodsEntity.setGoods_id(goodsObject.optString("goods_id"));
                                        extendOrderGoodsEntity.setGoods_name(goodsObject.optString("goods_name"));
                                        extendOrderGoodsEntity.setGoods_price(goodsObject.optString("goods_price"));
                                        extendOrderGoodsEntity.setGoods_num(goodsObject.optString("goods_num"));
                                        extendOrderGoodsEntity.setGoods_image_url(goodsObject.optString("goods_image_url"));
                                        extendOrderGoodsEntity.setGc_id(goodsObject.optString("gc_id"));
                                        extendOrderGoodsEntity.setRec_id(goodsObject.optString("rec_id"));
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

                        Log.i("zjz", "orders=" + orderses.size());
                        JSONArray payArray = object.getJSONArray("payment_list");
                        for (int w = 0; w < payArray.length(); w++) {
                            JSONObject payObject = payArray.getJSONObject(w);
                            PayWay payWay = new PayWay();
                            payWay.setPayment_code(payObject.optString("payment_code"));
                            payWay.setPayment_name(payObject.optString("payment_name"));
                            payList.add(payWay);
                            orderRecyclerAdapter.getPayList().add(payWay);
                        }
                        if (orderRecyclerAdapter.getList().size() == 0) {
                            layoutNull.setVisibility(View.VISIBLE);
                        } else {
                            layoutNull.setVisibility(View.GONE);
                        }
                        Log.i("zjz", "payList=" + payList.size());

                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
//                        orderRecyclerAdapter = new OrderRecyclerAdapter(OrderActivity2.this, orderses, payList);
//                        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(orderRecyclerAdapter);
//                        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(OrderActivity2.this, view, mHeaderAndFooterRecyclerViewAdapter, OrderActivity2.this, false, OrderActivity2.this);
//                        orderAdapter = new OrderAdapter(OrderActivity2.this, orderses, payList, tljrZListView);
//                        tljrZListView.setAdapter(orderAdapter);
//                        orderAdapter.notifyDataSetChanged();
                        ProgressDlgUtil.stopProgressDlg();

                    } else {
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                        Log.i("zjz", "goodsActivity解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mRequestQueue.add(jr);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tljr_img_news_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        myBroadCastReceiver.unRegister();
        super.onDestroy();
    }


    private PreviewHandler mHandler = new PreviewHandler(this);
    private int mCurrentCounter = 0;

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Orders> list) {

        orderRecyclerAdapter.addItems(list);
        mCurrentCounter += list.size();
    }

    private static final int REQUEST_COUNT = 10;
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
                RecyclerViewStateUtils.setFooterViewState(OrderActivity2.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(OrderActivity2.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }

    };

    @Override
    public void swipeRefreshLayoutOnRefresh() {
        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(true);
        isLoadMore = false;
        currentPage = 1;
        initAllDates2();
    }

    private class PreviewHandler extends Handler {

        private WeakReference<OrderActivity2> ref;

        PreviewHandler(OrderActivity2 activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final OrderActivity2 activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            switch (msg.what) {
                case -1:
                    isLoadMore = true;
                    Log.i("zjz", "totalPage=" + totalPage);
                    if (currentPage < totalPage && isLoadMore) {
                        currentPage += 1;
                        Log.i("zjz", "current=" + currentPage);
                        RecyclerViewStateUtils.setFooterViewState(activity.recyclerView, LoadingFooter.State.Normal);
                        initAllDates2();
                    }
                    break;
                case -2:
                    activity.notifyDataSetChanged();
                    break;
                case -3:
                    RecyclerViewStateUtils.setFooterViewState(activity, activity.recyclerView, REQUEST_COUNT, LoadingFooter.State.NetWorkError, activity.mFooterClick);
                    break;
            }
        }
    }

    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(OrderActivity2.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                if (NetworkUtils.isNetAvailable(OrderActivity2.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }


}
