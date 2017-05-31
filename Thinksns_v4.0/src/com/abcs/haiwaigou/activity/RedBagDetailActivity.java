package com.abcs.haiwaigou.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.RedBagDetailAdapter;
import com.abcs.haiwaigou.model.Goods;
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
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RedBagDetailActivity extends BaseActivity implements RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.relative_title)
    RelativeLayout relativeTitle;
    @InjectView(R.id.t_desc)
    TextView tDesc;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;


    boolean first = false;
    boolean isLoadMore = false;
    private static final int REQUEST_COUNT = 20;
    @InjectView(R.id.t_code)
    TextView tCode;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    RedBagDetailAdapter redBagDetailAdapter;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private View view;
    int totalPage;
    int currentPage = 1;
    private int mCurrentCounter = 0;

    String keyword;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_activity_red_bag_detail, null);
        }
        setContentView(view);
        ButterKnife.inject(this);
        keyword = getIntent().getStringExtra("keyword");
        if(tCode!=null)
            tCode.setText("口令："+keyword);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////            window = getWindow();
////            // Translucent status bar
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relativeTitle.getLayoutParams();
            params.setMargins(0, getStatusBarHeight(), 0, 0);
            relativeTitle.setLayoutParams(params);
        }
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initRecycler();
    }

    private void initRecycler() {
        recyclerView.addOnScrollListener(mOnScrollListener);
        initAllDates();
        redBagDetailAdapter = new RedBagDetailAdapter(this);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(redBagDetailAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(this, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
    }

    private void initAllDates() {
        final ArrayList<Goods> dataList = new ArrayList<>();
        if (!first) {
            ProgressDlgUtil.showProgressDlg("Loading...", this);
        }
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_redbag_detail + "&key=" + MyApplication.getInstance().getMykey(), "&value=" + keyword + "&curpage=" + currentPage, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject response = new JSONObject(msg);
                            if (response.getInt("code") == 200) {
                                Log.i("zjz", "redbag_detail_response=" + response);
                                totalPage = response.optInt("page_total");
                                if (response.optInt("page_total") != 0) {

                                    JSONObject object1 = response.optJSONObject("datas");
                                    if (!object1.has("count")) {
                                        //优惠券列表
                                        Log.i("zjz","优惠券！");
                                        JSONArray array = object1.optJSONArray("red_envelope_info");
                                        if (isLoadMore) {
                                            int currentSize = redBagDetailAdapter.getItemCount();
                                            for (int i = 0; i < array.length(); i++) {
                                                JSONObject rechargeObject = array.getJSONObject(i);
                                                Goods goods = new Goods();
                                                goods.setId(currentSize + i);
                                                goods.setBuyer_id(rechargeObject.optString("member_id"));
                                                goods.setBuyer_name(rechargeObject.optString("member_name"));
                                                goods.setTime(rechargeObject.optLong("receive_date"));
                                                goods.setState_desc(rechargeObject.optString("status"));
                                                goods.setMoney(rechargeObject.optDouble("voucher_t_price"));
                                                dataList.add(goods);
                                            }
                                            addItems(dataList);

                                        } else {
                                            redBagDetailAdapter.getList().clear();
                                            dataList.clear();
                                            for (int i = 0; i < array.length(); i++) {
                                                JSONObject rechargeObject = array.getJSONObject(i);
                                                Goods goods = new Goods();
                                                goods.setId(i);
                                                goods.setBuyer_id(rechargeObject.optString("member_id"));
                                                goods.setBuyer_name(rechargeObject.optString("member_name"));
                                                goods.setTime(rechargeObject.optLong("receive_date"));
                                                goods.setState_desc(rechargeObject.optString("status"));
                                                goods.setMoney(rechargeObject.optDouble("voucher_t_price"));
                                                Log.i("zjz", "test1=" + rechargeObject.optString("voucher_t_giveout"));
                                                Log.i("zjz", "test2=" + rechargeObject.optString("voucher_t_total"));
                                                if (tDesc != null) {
                                                    if (rechargeObject.optString("voucher_t_giveout").equals(rechargeObject.optString("voucher_t_total"))) {
                                                        tDesc.setText("已领取" + rechargeObject.optString("voucher_t_giveout") + "/" + rechargeObject.optString("voucher_t_total") + "，已领完");
                                                    } else {

                                                        tDesc.setText("已领取" + rechargeObject.optString("voucher_t_giveout") + "/" + rechargeObject.optString("voucher_t_total"));
                                                    }
                                                }
                                                dataList.add(goods);
                                            }
                                            mCurrentCounter = dataList.size();
                                            redBagDetailAdapter.addItems(dataList);
                                            redBagDetailAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        Log.i("zjz","充值卡！");
                                        JSONObject object = response.optJSONObject("datas");
                                        if (tDesc != null) {
                                            if (object.optString("count").equals(object.opt("receive"))) {
                                                tDesc.setText("已领取" + object.opt("receive") + "/" + object.optString("count") + "，已领完");
                                            } else {
                                                tDesc.setText("已领取" + object.opt("receive") + "/" + object.optString("count"));
                                            }
                                        }
                                        JSONArray dataArray = object.optJSONArray("red_envelope_info");
                                        //充值卡列表
                                        if (isLoadMore) {
                                            int currentSize = redBagDetailAdapter.getItemCount();
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject rechargeObject = dataArray.getJSONObject(i);
                                                Goods goods = new Goods();
                                                goods.setId(currentSize + i);
                                                goods.setBuyer_id(rechargeObject.optString("member_id"));
                                                goods.setBuyer_name(rechargeObject.optString("member_name"));
                                                goods.setTime(rechargeObject.optLong("receive_date"));
                                                goods.setState_desc(rechargeObject.optString("status"));
                                                goods.setMoney(rechargeObject.optDouble("relation_desc"));
                                                dataList.add(goods);
                                            }
                                            addItems(dataList);
                                        } else {
                                            redBagDetailAdapter.getList().clear();
                                            dataList.clear();
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject rechargeObject = dataArray.getJSONObject(i);
                                                Goods goods = new Goods();
                                                goods.setId(i);
                                                goods.setBuyer_id(rechargeObject.optString("member_id"));
                                                goods.setBuyer_name(rechargeObject.optString("member_name"));
                                                goods.setTime(rechargeObject.optLong("receive_date"));
                                                goods.setState_desc(rechargeObject.optString("status"));
                                                goods.setMoney(rechargeObject.optDouble("relation_desc"));
                                                dataList.add(goods);
                                            }
                                            mCurrentCounter = dataList.size();
                                            redBagDetailAdapter.addItems(dataList);
                                            redBagDetailAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            Log.i("zjz",e.toString());
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

        redBagDetailAdapter.addItems(list);
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
                RecyclerViewStateUtils.setFooterViewState(RedBagDetailActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(RedBagDetailActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }
    };

    private class PreviewHandler extends Handler {

        private WeakReference<RedBagDetailActivity> ref;

        PreviewHandler(RedBagDetailActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final RedBagDetailActivity activity = ref.get();
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
            RecyclerViewStateUtils.setFooterViewState(RedBagDetailActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                if (NetworkUtils.isNetAvailable(RedBagDetailActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }
}
