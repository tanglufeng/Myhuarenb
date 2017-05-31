package com.abcs.haiwaigou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.abcs.haiwaigou.adapter.PreDepositAdapter;
import com.abcs.haiwaigou.model.Prediposit;
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

public class PreDepositActivity extends BaseActivity implements RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh,View.OnClickListener {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.layout_null)
    RelativeLayout layoutNull;


    boolean first = false;
    boolean isLoadMore = false;
    private static final int REQUEST_COUNT = 20;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    PreDepositAdapter preDepositAdapter;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private Handler handler = new Handler();
    private View view;
    int totalPage;
    int currentPage = 1;
    private int mCurrentCounter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(view==null){
            view=getLayoutInflater().inflate(R.layout.hwg_activity_pre_deposit,null);
        }
        setContentView(view);
        ButterKnife.inject(this);
        relativeBack.setOnClickListener(this);
        initRecycler();
    }

    private void initRecycler() {
        recyclerView.addOnScrollListener(mOnScrollListener);
        initAllDates();
        preDepositAdapter = new PreDepositAdapter(this);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(preDepositAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(this, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
    }

    private void initAllDates() {
        final ArrayList<Prediposit> dataList = new ArrayList<>();
        if (!first) {
            ProgressDlgUtil.showProgressDlg("Loading...", this);
        }
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_predeposit_list, "&key=" + MyApplication.getInstance().getMykey() +"&sn_search=&paystate_search="+ "&curpage=" + currentPage,
                new HttpRevMsg() {
                    @Override
                    public void revMsg(final String msg) {
                        handler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject response = new JSONObject(msg);
                                            if (response.getInt("code") == 200) {
                                                Log.i("zjz", "recharge_response=" + response);
                                                totalPage = response.optInt("page_total");
                                                if (response.optInt("page_total") != 0) {
                                                    JSONArray array = response.getJSONArray("datas");
                                                    if (isLoadMore) {
                                                        int currentSize = preDepositAdapter.getItemCount();
                                                        for (int i = 0; i < array.length(); i++) {
                                                            JSONObject preObject = array.getJSONObject(i);
                                                            Prediposit prediposit = new Prediposit();
                                                            prediposit.setId(currentSize + i);
                                                            prediposit.setPdc_id(preObject.optString("pdc_id"));
                                                            prediposit.setPdc_sn(preObject.optString("pdc_sn"));
                                                            prediposit.setPdc_member_id(preObject.optString("pdc_member_id"));
                                                            prediposit.setPdc_member_name(preObject.optString("pdc_member_name"));
                                                            prediposit.setPdc_amount(preObject.optDouble("pdc_amount"));
                                                            prediposit.setPdc_bank_name(preObject.optString("pdc_bank_name"));
                                                            prediposit.setPdc_bank_no(preObject.optString("pdc_bank_no"));
                                                            prediposit.setPdc_bank_user(preObject.optString("pdc_bank_user"));
                                                            prediposit.setPdc_add_time(preObject.optLong("pdc_add_time")*1000);
                                                            prediposit.setPdc_payment_time(preObject.optLong("pdc_payment_time")*1000);
                                                            prediposit.setPdc_payment_state(preObject.optString("pdc_payment_state"));
                                                            prediposit.setPdc_payment_admin(preObject.optString("pdc_payment_admin"));
                                                            prediposit.setPdc_bank_phone(preObject.optString("pdc_bank_phone"));
                                                            dataList.add(prediposit);
                                                        }
                                                        addItems(dataList);

                                                    } else {
                                                        preDepositAdapter.getList().clear();
                                                        dataList.clear();
                                                        for (int i = 0; i < array.length(); i++) {
                                                            JSONObject preObject = array.getJSONObject(i);
                                                            Prediposit prediposit = new Prediposit();
                                                            prediposit.setId( i);
                                                            prediposit.setPdc_id(preObject.optString("pdc_id"));
                                                            prediposit.setPdc_sn(preObject.optString("pdc_sn"));
                                                            prediposit.setPdc_member_id(preObject.optString("pdc_member_id"));
                                                            prediposit.setPdc_member_name(preObject.optString("pdc_member_name"));
                                                            prediposit.setPdc_amount(preObject.optDouble("pdc_amount"));
                                                            prediposit.setPdc_bank_name(preObject.optString("pdc_bank_name"));
                                                            prediposit.setPdc_bank_no(preObject.optString("pdc_bank_no"));
                                                            prediposit.setPdc_bank_user(preObject.optString("pdc_bank_user"));
                                                            prediposit.setPdc_add_time(preObject.optLong("pdc_add_time")*1000);
                                                            prediposit.setPdc_payment_time(preObject.optLong("pdc_payment_time")*1000);
                                                            prediposit.setPdc_payment_state(preObject.optString("pdc_payment_state"));
                                                            prediposit.setPdc_payment_admin(preObject.optString("pdc_payment_admin"));
                                                            prediposit.setPdc_bank_phone(preObject.optString("pdc_bank_phone"));
                                                            dataList.add(prediposit);
                                                        }
                                                        mCurrentCounter = dataList.size();
                                                        preDepositAdapter.addItems(dataList);
                                                        preDepositAdapter.notifyDataSetChanged();
                                                    }
                                                }
                                                if (layoutNull != null)
                                                    layoutNull.setVisibility(preDepositAdapter.getList().size() == 0 ? View.VISIBLE : View.GONE);
                                                recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                                            }
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            Log.i("zjz", e.toString());
                                            Log.i("zjz", msg);
                                            e.printStackTrace();
                                            ProgressDlgUtil.stopProgressDlg();
                                        } finally {
                                            ProgressDlgUtil.stopProgressDlg();
                                            recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                                        }
                                    }
                                }
                        );

                    }
                }

        );
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

    private void addItems(ArrayList<Prediposit> list) {

        preDepositAdapter.addItems(list);
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
                RecyclerViewStateUtils.setFooterViewState(PreDepositActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(PreDepositActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }
    };

    private class PreviewHandler extends Handler {

        private WeakReference<PreDepositActivity> ref;

        PreviewHandler(PreDepositActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final PreDepositActivity activity = ref.get();
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
            RecyclerViewStateUtils.setFooterViewState(PreDepositActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                if (NetworkUtils.isNetAvailable(PreDepositActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }
}
