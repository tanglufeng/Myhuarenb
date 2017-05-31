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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.adapter.ExchangeRecordAdapter;
import com.abcs.haiwaigou.model.Points;
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
import butterknife.InjectView;

/**
 * Created by zjz on 2016/4/26.
 */
public class ExchangeRecordFragment extends Fragment implements RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh {
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.img_null)
    ImageView imgNull;
    @InjectView(R.id.tv_null)
    TextView tvNull;
    @InjectView(R.id.layout_null)
    RelativeLayout layoutNull;
    private Activity activity;
    private View view;
    private RequestQueue mRequestQueue;
    int totalPage;
    int currentPage = 1;
    boolean isLoadMore = false;
    boolean first = false;

    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    ExchangeRecordAdapter exchangeRecordAdapter;
    MyBroadCastReceiver myBroadCastReceiver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        view = activity.getLayoutInflater().inflate(R.layout.hwg_fragment_comment_item, null);
        mRequestQueue = Volley.newRequestQueue(activity);
        myBroadCastReceiver=new MyBroadCastReceiver(activity,updateUI);
        myBroadCastReceiver.register();
        ButterKnife.inject(this, view);
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
            if (intent.getStringExtra("type").equals(MyUpdateUI.RECHARGE)) {
                initAllDates();
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initRecycler() {
        recyclerView.addOnScrollListener(mOnScrollListener);
        initAllDates();
        exchangeRecordAdapter = new ExchangeRecordAdapter(activity);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(exchangeRecordAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(activity, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
    }

    private void initAllDates() {
        if (!first) {
            ProgressDlgUtil.showProgressDlg("Loading...", activity);
        }
        final ArrayList<Points> dataList = new ArrayList<>();
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_point_detail + "&stage=app&stime=&etime=&description=" + "&key=" + MyApplication.getInstance().getMykey() + "&curpage=" + currentPage, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "points:连接成功");
                        Log.i("zjz", "response=" + response);
                        totalPage = response.getInt("page_total");
                        if (response.getInt("page_total") != 0) {
                            JSONObject object = response.getJSONObject("datas");
                            JSONArray array = object.getJSONArray("list_log");
                            if (isLoadMore) {
                                int currentSize = exchangeRecordAdapter.getItemCount();

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject pointObject = array.getJSONObject(i);
                                    Points points = new Points();
                                    points.setId(currentSize + i);
                                    points.setPl_id(pointObject.optString("pl_id"));
                                    points.setPl_memberid(pointObject.optString("pl_memberid"));
                                    points.setPl_membername(pointObject.optString("pl_membername"));
                                    points.setPl_adminid(pointObject.optString("pl_adminid"));
                                    points.setPl_adminname(pointObject.optString("pl_adminname"));
                                    points.setPl_addtime(pointObject.optLong("pl_addtime"));
                                    points.setPl_desc(pointObject.optString("pl_desc"));
                                    points.setPl_points(pointObject.optString("pl_points"));
                                    points.setPl_stage(pointObject.optString("pl_stage"));
                                    dataList.add(points);
                                }
                                addItems(dataList);

                            } else {
                                exchangeRecordAdapter.getList().clear();
                                dataList.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject pointObject = array.getJSONObject(i);
                                    Points points = new Points();
                                    points.setId(i);
                                    points.setPl_id(pointObject.optString("pl_id"));
                                    points.setPl_memberid(pointObject.optString("pl_memberid"));
                                    points.setPl_membername(pointObject.optString("pl_membername"));
                                    points.setPl_adminid(pointObject.optString("pl_adminid"));
                                    points.setPl_adminname(pointObject.optString("pl_adminname"));
                                    points.setPl_addtime(pointObject.optLong("pl_addtime"));
                                    points.setPl_desc(pointObject.optString("pl_desc"));
                                    points.setPl_points(pointObject.optString("pl_points"));
                                    points.setPl_stage(pointObject.optString("pl_stage"));
                                    dataList.add(points);
                                }
                                mCurrentCounter = dataList.size();
                                exchangeRecordAdapter.addItems(dataList);
                                exchangeRecordAdapter.notifyDataSetChanged();
                            }
                        }
                        if(layoutNull!=null)
                            layoutNull.setVisibility(exchangeRecordAdapter.getList().size() == 0?View.VISIBLE:View.GONE);
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                    } else {
                        Log.i("zjz", "goodsActivity解析失败");
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();
                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                } finally {
                    ProgressDlgUtil.stopProgressDlg();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                ProgressDlgUtil.stopProgressDlg();
            }
        });
        mRequestQueue.add(jr);
    }


    private MyHandler handler = new MyHandler();
    private int mCurrentCounter = 0;

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Points> list) {

        exchangeRecordAdapter.addItems(list);
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

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
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

    @Override
    public void onDestroyView() {
        myBroadCastReceiver.unRegister();
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    @Override
    public void swipeRefreshLayoutOnRefresh() {
        first = true;
        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(true);
        isLoadMore = false;
        currentPage = 1;
        initAllDates();
    }
}
