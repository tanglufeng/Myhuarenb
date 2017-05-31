package com.abcs.haiwaigou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.ChongzhiDetailAdapter;
import com.abcs.haiwaigou.model.Recharge;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.utils.RecyclerViewAndSwipeRefreshLayout;
import com.abcs.haiwaigou.view.recyclerview.EndlessRecyclerOnScrollListener;
import com.abcs.haiwaigou.view.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.abcs.haiwaigou.view.recyclerview.LoadingFooter;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.haiwaigou.view.recyclerview.RecyclerViewStateUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ChongzhiDetailActivity extends BaseActivity implements View.OnClickListener, RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
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

    boolean first = false;
    boolean isLoadMore = false;
    private static final int REQUEST_COUNT = 20;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    ChongzhiDetailAdapter chongzhiDetailAdapter;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private Handler handler = new Handler();
    private View view;
    int totalPage;
    int currentPage = 1;
    private int mCurrentCounter = 0;
    private RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_activity_chongzhi_detail, null);
        }
        setContentView(view);
        ButterKnife.inject(this);
        relativeBack.setOnClickListener(this);
        mRequestQueue= Volley.newRequestQueue(this);
        initRecycler();
    }

    private void initRecycler() {
        recyclerView.addOnScrollListener(mOnScrollListener);
        initAllDates();
        chongzhiDetailAdapter = new ChongzhiDetailAdapter(this);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(chongzhiDetailAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(this, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
    }

    private void initAllDates() {
        final ArrayList<Recharge> dataList = new ArrayList<>();
        if (!first) {
            ProgressDlgUtil.showProgressDlg("Loading...", this);
        }
        Log.i("zjz", "current=" + currentPage);

        JsonObjectRequest jr=new JsonObjectRequest(Request.Method.POST, TLUrl.getInstance().URL_hwg_recharge_list+"&key=" + MyApplication.getInstance().getMykey() + "&curpage=" + currentPage, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "recharge_response=" + response);
                        totalPage = response.getInt("page_total");
                        if (response.getInt("page_total") != 0) {
                            JSONArray array = response.getJSONArray("datas");
                            if (isLoadMore) {
                                int currentSize = chongzhiDetailAdapter.getItemCount();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject rechargeObject = array.getJSONObject(i);
                                    Recharge recharge=new Recharge();
                                    recharge.setId(currentSize + i);
                                    recharge.setRecharge_id(rechargeObject.optString("id"));
                                    recharge.setMember_id(rechargeObject.optString("member_id"));
                                    recharge.setMember_name(rechargeObject.optString("member_name"));
                                    recharge.setType(rechargeObject.optString("type"));
                                    recharge.setAdd_time(rechargeObject.optLong("add_time") * 1000);
                                    recharge.setAvailable_amount(rechargeObject.optDouble("available_amount"));
                                    recharge.setFreeze_amount(rechargeObject.optDouble("freeze_amount"));
                                    recharge.setDescription(rechargeObject.optString("description"));

                                    dataList.add(recharge);
                                }
                                addItems(dataList);

                            } else {
                                chongzhiDetailAdapter.getList().clear();
                                dataList.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject rechargeObject = array.getJSONObject(i);
                                    Recharge recharge=new Recharge();
                                    recharge.setId( i);
                                    recharge.setRecharge_id(rechargeObject.optString("id"));
                                    recharge.setMember_id(rechargeObject.optString("member_id"));
                                    recharge.setMember_name(rechargeObject.optString("member_name"));
                                    recharge.setType(rechargeObject.optString("type"));
                                    recharge.setAdd_time(rechargeObject.optLong("add_time") * 1000);
                                    recharge.setAvailable_amount(rechargeObject.optDouble("available_amount"));
                                    recharge.setFreeze_amount(rechargeObject.optDouble("freeze_amount"));
                                    recharge.setDescription(rechargeObject.optString("description"));

                                    dataList.add(recharge);
                                }
                                mCurrentCounter = dataList.size();
                                chongzhiDetailAdapter.addItems(dataList);
                                chongzhiDetailAdapter.notifyDataSetChanged();
                            }
                        }
                        if (layoutNull != null)
                            layoutNull.setVisibility(chongzhiDetailAdapter.getList().size() == 0 ? View.VISIBLE : View.GONE);
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    ProgressDlgUtil.stopProgressDlg();
                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("zjz","chongzhidetail_failed");
            }
        });
        mRequestQueue.add(jr);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
        }
    }

    @Override
    public void swipeRefreshLayoutOnRefresh() {
        first = true;
        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(true);
        isLoadMore = false;
        currentPage = 1;
        initAllDates();
    }

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Recharge> list) {

        chongzhiDetailAdapter.addItems(list);
        mCurrentCounter += list.size();
    }

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
                RecyclerViewStateUtils.setFooterViewState(ChongzhiDetailActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(ChongzhiDetailActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }
    };

    private class PreviewHandler extends Handler {

        private WeakReference<ChongzhiDetailActivity> ref;

        PreviewHandler(ChongzhiDetailActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ChongzhiDetailActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            switch (msg.what) {
                case -1:
                    isLoadMore = true;
                    Log.i("zjz", "totalPage=" + totalPage);
                    if (currentPage < totalPage && isLoadMore) {
                        currentPage += 1;

                        RecyclerViewStateUtils.setFooterViewState(recyclerView, LoadingFooter.State.Normal);
                        initAllDates();
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
            RecyclerViewStateUtils.setFooterViewState(ChongzhiDetailActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                if (NetworkUtils.isNetAvailable(ChongzhiDetailActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }
}
