package com.abcs.haiwaigou.yyg.activity;

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
import android.widget.Toast;

import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.utils.RecyclerViewAndSwipeRefreshLayout;
import com.abcs.haiwaigou.view.recyclerview.EndlessRecyclerOnScrollListener;
import com.abcs.haiwaigou.view.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.abcs.haiwaigou.view.recyclerview.LoadingFooter;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.haiwaigou.view.recyclerview.RecyclerViewStateUtils;
import com.abcs.haiwaigou.yyg.adapter.YYGBalanceDetailAdapter;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class YYGBalanceDetailActivity extends BaseActivity implements View.OnClickListener, RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh {

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
    @InjectView(R.id.t_refresh)
    TextView tRefresh;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    private PreviewHandler mHandler = new PreviewHandler(this);
    YYGBalanceDetailAdapter yygBalanceDetailAdapter;
    private Handler handler = new Handler();
    private View view;
    int totalPage;
    int currentPage = 1;
    private int mCurrentCounter = 0;
    boolean isMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_yyg_activity_yygbalance_list, null);
        }
        setContentView(view);
        ButterKnife.inject(this);
        relativeBack.setOnClickListener(this);
        initRecycler();
        tRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRecycler();
            }
        });
    }

    private void initRecycler() {
        recyclerView.addOnScrollListener(mOnScrollListener);
        initAllDates();
        yygBalanceDetailAdapter = new YYGBalanceDetailAdapter(this);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(yygBalanceDetailAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(this, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
    }


    private void initAllDates() {
        final ArrayList<Goods> dataList = new ArrayList<>();
        if (!first) {
            ProgressDlgUtil.showProgressDlg("Loading...", this);
        }
        HttpRequest.sendGet(TLUrl.getInstance().URL_yyg_find_my_outcome_list, "&page=" + currentPage + "&pageSize=10" + "&userId=" + MyApplication.getInstance().self.getId(),
                new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {
                        Log.i("zjz", "yyg_balanceList_msg=" + msg);
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (msg.length() == 0) {
                                    if (tRefresh != null && !first) {
                                        tRefresh.setVisibility(View.VISIBLE);
                                        Toast.makeText(YYGBalanceDetailActivity.this, "请求失败,请重试", Toast.LENGTH_SHORT).show();
                                    }
                                    ProgressDlgUtil.stopProgressDlg();
                                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                                    return;
                                }

                                if (tRefresh != null)
                                    tRefresh.setVisibility(View.GONE);

                                try {
                                    JSONObject result = new JSONObject(msg);
                                    if (result.optInt("status") == 1) {
                                        JSONArray jsonArray = result.getJSONArray("msg");
                                        if (jsonArray != null) {
                                            if (isLoadMore) {
                                                if (jsonArray.length() == 10) {
                                                    isMore = true;
                                                } else {
                                                    isMore = false;
                                                }
                                                int currentSize = yygBalanceDetailAdapter.getItemCount();
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    Goods goods = new Goods();
                                                    JSONObject object = jsonArray.getJSONObject(i);
                                                    goods.setId(currentSize + i);
                                                    goods.setGoods_id(object.optString("activityId"));
                                                    goods.setQishu(object.optInt("periodsNum"));
                                                    goods.setMoney(object.optDouble("money"));
                                                    goods.setPromote_money(object.optDouble("virtualMoney"));
                                                    goods.setType(object.optString("payType"));
                                                    goods.setDismoney(object.optDouble("incomeAndExpenses"));
                                                    goods.setTitle(object.optString("goodsName"));
                                                    goods.setTime(object.optLong("opTime"));
                                                    goods.setOrder_id(object.optString("hqbOrderId"));
                                                    dataList.add(goods);

                                                    addItems(dataList);
                                                    yygBalanceDetailAdapter.notifyDataSetChanged();
                                                }
                                            } else {
                                                yygBalanceDetailAdapter.getList().clear();
                                                dataList.clear();
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    Goods goods = new Goods();
                                                    JSONObject object = jsonArray.getJSONObject(i);
                                                    goods.setId(i);
                                                    goods.setGoods_id(object.optString("activityId"));
                                                    goods.setQishu(object.optInt("periodsNum"));
                                                    goods.setMoney(object.optDouble("money"));
                                                    goods.setPromote_money(object.optDouble("virtualMoney"));
                                                    goods.setType(object.optString("payType"));
                                                    goods.setDismoney(object.optDouble("incomeAndExpenses"));
                                                    goods.setTitle(object.optString("goodsName"));
                                                    goods.setTime(object.optLong("opTime"));
                                                    goods.setOrder_id(object.optString("hqbOrderId"));

                                                    dataList.add(goods);
                                                    if (dataList.size() == 10) {
                                                        isMore = true;
                                                    } else {
                                                        isMore = false;
                                                    }
                                                    yygBalanceDetailAdapter.addItems(dataList);
                                                    yygBalanceDetailAdapter.notifyDataSetChanged();
                                                }
                                            }
                                            if (layoutNull != null)
                                                layoutNull.setVisibility(dataList.size() == 0 ? View.VISIBLE : View.GONE);
                                        } else {
                                            isMore = false;
                                        }
                                        Log.i("zjz", "yyg_lottery_isMore=" + isMore);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } finally {

                                    ProgressDlgUtil.stopProgressDlg();
                                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                                }

                            }
                        });
                    }
                });
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

    private void addItems(ArrayList<Goods> list) {

        yygBalanceDetailAdapter.addItems(list);
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

            if (isMore) {
                // loading more
                RecyclerViewStateUtils.setFooterViewState(YYGBalanceDetailActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(YYGBalanceDetailActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }
    };

    private class PreviewHandler extends Handler {

        private WeakReference<YYGBalanceDetailActivity> ref;

        PreviewHandler(YYGBalanceDetailActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final YYGBalanceDetailActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            switch (msg.what) {
                case -1:
                    isLoadMore = true;
                    Log.i("zjz", "totalPage=" + totalPage);
                    if (isMore && isLoadMore) {
                        currentPage += 1;
                        Log.i("zjz", "current=" + currentPage);
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
            RecyclerViewStateUtils.setFooterViewState(YYGBalanceDetailActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                if (NetworkUtils.isNetAvailable(YYGBalanceDetailActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }
}
