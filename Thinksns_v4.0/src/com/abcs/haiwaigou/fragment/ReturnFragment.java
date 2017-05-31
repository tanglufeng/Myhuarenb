package com.abcs.haiwaigou.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.adapter.ReturnFragmentAdapter;
import com.abcs.haiwaigou.fragment.viewholder.ReturnAndRefundFragmentViewHolder;
import com.abcs.haiwaigou.model.Refund;
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

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by zjz on 2016/3/31.
 */
public class ReturnFragment extends Fragment implements RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh,ReturnAndRefundFragmentViewHolder.ItemOnClick {
    Activity activity;
    View view;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout layoutNull;
    private RequestQueue mRequestQueue;
    ReturnFragmentAdapter returnFragmentAdapter;

    int totalPage;
    int currentPage = 1;
    boolean isLoadMore = false;
    boolean first = false;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    MyBroadCastReceiver myBroadCastReceiver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity= getActivity();
        view = (RelativeLayout) activity.getLayoutInflater().inflate(
                R.layout.hwg_fragment_refund, null);
        mRequestQueue = Volley.newRequestQueue(activity);
        myBroadCastReceiver=new MyBroadCastReceiver(activity,updateUI);
        myBroadCastReceiver.register();
        initView();
        initRecycler();
    }

    MyBroadCastReceiver.UpdateUI updateUI=new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

        }

        @Override
        public void updateCarNum(Intent intent) {

        }

        @Override
        public void updateCollection(Intent intent) {
            if (intent.getStringExtra("type").equals(MyUpdateUI.RETURN)) {
                isLoadMore = false;
                currentPage = 1;
                initRecycler();
                Log.i("zjz", "更新退款");
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initView() {
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutNull= (RelativeLayout) view.findViewById(R.id.layout_null);
    }

    public void initRecycler() {
        recyclerView.addOnScrollListener(mOnScrollListener);
        initAllDates();
        returnFragmentAdapter = new ReturnFragmentAdapter(activity,this);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(returnFragmentAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(activity, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
    }

    private void initAllDates() {
        if(!first){
            ProgressDlgUtil.showProgressDlg("Loading...",activity);
        }
        final ArrayList<Refund> dataList = new ArrayList<>();
//        type('order_sn','refund_sn','goods_name')
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_return_list + "&type=" + "&key=" + MyApplication.getInstance().getMykey()+"&add_time_from="+"&add_time_to="+"&curpage="+currentPage, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "good_refund:连接成功");
                        Log.i("zjz", "response=" + response);
                        totalPage=response.getInt("page_total");
                        if(response.getInt("page_total")!=0){
                            JSONObject object = response.getJSONObject("datas");
                            JSONArray array = object.getJSONArray("refund_list");
                            if (isLoadMore) {
                                int currentSize = returnFragmentAdapter.getItemCount();

                                for(int i=0;i<array.length();i++){
                                    JSONObject refundObject=array.getJSONObject(i);
                                    Refund refund=new Refund();
                                    refund.setId(currentSize + i);
                                    refund.setRefund_id(refundObject.optString("refund_id"));
                                    refund.setOrder_id(refundObject.optString("order_id"));
                                    refund.setStore_id(refundObject.optString("store_id"));
                                    refund.setOrder_sn(refundObject.optString("order_sn"));
                                    refund.setRefund_sn(refundObject.optString("refund_sn"));
                                    refund.setStore_name(refundObject.optString("store_name"));
                                    refund.setGoods_name(refundObject.optString("goods_name"));
                                    refund.setSeller_state(refundObject.optString("seller_state"));
                                    refund.setGoods_num(refundObject.optString("goods_num"));
                                    refund.setRefund_amount(refundObject.optString("refund_amount"));
                                    refund.setGoods_image(refundObject.optString("goods_image"));
                                    refund.setGoods_state(refundObject.optString("goods_state"));
                                    refund.setAdd_time(refundObject.optLong("add_time"));
                                    refund.setRefund_state(refundObject.optString("refund_state"));
                                    refund.setSeller_state(refundObject.optString("seller_state"));
                                    dataList.add(refund);
                                }
                                addItems(dataList);

                            } else {
                                dataList.clear();
                                for(int i=0;i<array.length();i++){
                                    JSONObject refundObject=array.getJSONObject(i);
                                    Refund refund=new Refund();
                                    refund.setId(i);
                                    refund.setRefund_id(refundObject.optString("refund_id"));
                                    refund.setOrder_id(refundObject.optString("order_id"));
                                    refund.setStore_id(refundObject.optString("store_id"));
                                    refund.setOrder_sn(refundObject.optString("order_sn"));
                                    refund.setRefund_sn(refundObject.optString("refund_sn"));
                                    refund.setStore_name(refundObject.optString("store_name"));
                                    refund.setGoods_name(refundObject.optString("goods_name"));
                                    refund.setSeller_state(refundObject.optString("seller_state"));
                                    refund.setGoods_num(refundObject.optString("goods_num"));
                                    refund.setRefund_amount(refundObject.optString("refund_amount"));
                                    refund.setGoods_image(refundObject.optString("goods_image"));
                                    refund.setGoods_state(refundObject.optString("goods_state"));
                                    refund.setAdd_time(refundObject.optLong("add_time"));
                                    refund.setRefund_state(refundObject.optString("refund_state"));
                                    refund.setSeller_state(refundObject.optString("seller_state"));
                                    dataList.add(refund);
                                }
                                mCurrentCounter = dataList.size();
                                returnFragmentAdapter.addItems(dataList);
                                returnFragmentAdapter.notifyDataSetChanged();
                            }
                        }
                        if(layoutNull!=null)
                            layoutNull.setVisibility(returnFragmentAdapter.getList().size()==0?View.VISIBLE:View.GONE);
                        ProgressDlgUtil.stopProgressDlg();
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                    } else {
                        Log.i("zjz", "goodsActivity解析失败");
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                        ProgressDlgUtil.stopProgressDlg();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();
                    ProgressDlgUtil.stopProgressDlg();
                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mRequestQueue.add(jr);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeAllViewsInLayout();
        ButterKnife.inject(this, view);
        return view;
    }


    private MyHandler handler=new MyHandler();
    private int mCurrentCounter = 0;

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Refund> list) {

        returnFragmentAdapter.addItems(list);
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
                RecyclerViewStateUtils.setFooterViewState(activity, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(activity, recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }

    };

    @Override
    public void swipeRefreshLayoutOnRefresh() {
        first=true;
        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(true);
        isLoadMore = false;
        currentPage = 1;
        initAllDates();
    }

    @Override
    public void onClickDetail(int position) {

    }

    @Override
    public void onImgClick(int position) {

    }

    @Override
    public void onStoreClick(int position) {

    }


    private class MyHandler extends Handler {
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
            RecyclerViewStateUtils.setFooterViewState(activity, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                if (NetworkUtils.isNetAvailable(activity)) {
//                    mHandler.sendEmptyMessage(-1);
                    handler.sendEmptyMessage(-1);
                } else {
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
