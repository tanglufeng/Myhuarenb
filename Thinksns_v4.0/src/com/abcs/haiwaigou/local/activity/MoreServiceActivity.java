package com.abcs.haiwaigou.local.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.HireFind;
import com.abcs.haiwaigou.local.fragment.adapter.HireJobAdapter;
import com.abcs.haiwaigou.local.fragment.viewholder.HireJobViewHolder;
import com.abcs.haiwaigou.local.model.HireAndFindModel;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.utils.RecyclerViewAndSwipeRefreshLayout;
import com.abcs.haiwaigou.view.recyclerview.EndlessRecyclerOnScrollListener;
import com.abcs.haiwaigou.view.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.abcs.haiwaigou.view.recyclerview.LoadingFooter;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.haiwaigou.view.recyclerview.RecyclerViewStateUtils;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.sociax.android.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MoreServiceActivity extends BaseActivity implements RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh, HireJobViewHolder.RootOnClick, HireAndFindModel.LoadStateInterFace {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.t_title_name)
    TextView tTitleName;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;



    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    boolean isLoadMore = false;
    boolean isMore = true;
    int currentPage = 1;
    HireAndFindModel hireAndFindModel;
    HireJobAdapter hireJobAdapter;

    String menuId;
    String cityId;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(view==null){
            view=getLayoutInflater().inflate(R.layout.local_activity_more_service,null);
        }
        setContentView(view);
        ButterKnife.inject(this);
        tTitleName.setText("更多");
        menuId=getIntent().getStringExtra("menuId");
        cityId=getIntent().getStringExtra("cityId");
        hireAndFindModel=new HireAndFindModel(this,this);
        initRecycler();
    }

    private void initRecycler() {
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        recyclerView.addOnScrollListener(mOnScrollListener);
//        hireJobAdapter = new HireJobAdapter(this, this);
        hireJobAdapter = new HireJobAdapter(this, this,menuId);
        initAllDates();
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(hireJobAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(this, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
    }

    private void initAllDates() {

        if (isLoadMore) {
            hireAndFindModel.initHireAndFindDatas(cityId, menuId, currentPage, true);
        } else {
            hireJobAdapter.getList().clear();
            hireAndFindModel.initHireAndFindDatas(cityId, menuId, currentPage, false);
        }

    }


    @Override
    public void swipeRefreshLayoutOnRefresh() {
        currentPage = 1;
        isLoadMore = false;
        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(true);
        isMore = true;
        initAllDates();
    }
    @Override
    public void itemRootOnClick(int position) {
        Intent intent=new Intent(this, HireAndFindDetailActivity.class);
        intent.putExtra("conId",hireJobAdapter.getList().get(position).getId());
        intent.putExtra("isform_fen", true);
        startActivity(intent);
    }

    @Override
    public void LoadSuccess(String msg) {
        ProgressDlgUtil.stopProgressDlg();
        if (swipeRefreshLayout.isRefreshing())
            recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
        hireJobAdapter.addItems(hireAndFindModel.getHireFindList());
        hireJobAdapter.notifyDataSetChanged();
    }

    @Override
    public void LoadFailed(String msg) {
        ProgressDlgUtil.stopProgressDlg();
        if (swipeRefreshLayout.isRefreshing())
            recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
    }

    @Override
    public void isMore(boolean state) {
        isMore = state;
        Log.i("zjz", "isMore=" + isMore);
    }

    private PreviewHandler mHandler = new PreviewHandler(this);
    private int mCurrentCounter = 0;

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<HireFind> list) {

        hireJobAdapter.addItems(list);
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

            if (isMore) {
                // loading more
                RecyclerViewStateUtils.setFooterViewState(MoreServiceActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(MoreServiceActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }

    };


    private class PreviewHandler extends Handler {

        private WeakReference<MoreServiceActivity> ref;

        PreviewHandler(MoreServiceActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final MoreServiceActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            switch (msg.what) {
                case -1:
                    isLoadMore = true;
                    if (isMore && isLoadMore) {
                        currentPage += 1;
                        Log.i("zjz", "current=" + currentPage);
                        RecyclerViewStateUtils.setFooterViewState(activity.recyclerView, LoadingFooter.State.Normal);
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
            RecyclerViewStateUtils.setFooterViewState(MoreServiceActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                if (NetworkUtils.isNetAvailable(MoreServiceActivity.this)) {
                    Log.i("zjz", "有网络");
                    mHandler.sendEmptyMessage(-1);
//                    handler.sendEmptyMessage(-1);

                } else {
                    mHandler.sendEmptyMessage(-3);
//                    handler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
