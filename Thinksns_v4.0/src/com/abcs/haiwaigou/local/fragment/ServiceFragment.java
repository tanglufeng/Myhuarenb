package com.abcs.haiwaigou.local.fragment;

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
import android.widget.TextView;

import com.abcs.haiwaigou.local.activity.HireAndFindDetailActivity;
import com.abcs.haiwaigou.local.beans.HireFind;
import com.abcs.haiwaigou.local.fragment.adapter.ServiceAdapter;
import com.abcs.haiwaigou.local.fragment.viewholder.ServiceViewHolder;
import com.abcs.haiwaigou.local.model.HireAndFindModel;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.utils.RecyclerViewAndSwipeRefreshLayout;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.haiwaigou.view.recyclerview.EndlessRecyclerOnScrollListener;
import com.abcs.haiwaigou.view.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.abcs.haiwaigou.view.recyclerview.LoadingFooter;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.haiwaigou.view.recyclerview.RecyclerViewStateUtils;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.sociax.android.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zjz on 2016/9/7.
 */
public class ServiceFragment extends BaseFragment implements RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh, ServiceViewHolder.RootOnClick, HireAndFindModel.LoadStateInterFace {

    Activity activity;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.t_refresh)
    TextView tRefresh;
    private View view;


    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    boolean isLoadMore = false;
    boolean isMore = true;
    ServiceAdapter serviceAdapter;
    int currentPage = 1;
    HireAndFindModel hireAndFindModel;
//    String countryId;
    String menuId;
    String cityId;
    String type;

    public static ServiceFragment newInstance( String cityId, String menuId) {
        Bundle bundle = new Bundle();
//        bundle.putSerializable("countryId", countryId);
        bundle.putSerializable("cityId", cityId);
        bundle.putSerializable("menuId", menuId);
        ServiceFragment serviceFragment = new ServiceFragment();
        serviceFragment.setArguments(bundle);
        return serviceFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.hwg_yyg_fragment_goods, null);
        }
        if (hireAndFindModel == null) {
            hireAndFindModel = new HireAndFindModel(activity, this);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);
        ButterKnife.inject(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
//            countryId = bundle.getString("countryId");
            cityId = bundle.getString("cityId");
            menuId = bundle.getString("menuId");
        }
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }

        initRecycler();
    }

    private void initRecycler() {
        ProgressDlgUtil.showProgressDlg("Loading...", activity);
        recyclerView.addOnScrollListener(mOnScrollListener);
        serviceAdapter = new ServiceAdapter(activity, this);
        initAllDates();
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(serviceAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(activity, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
    }

    private void initAllDates() {

        if (isLoadMore) {
            hireAndFindModel.initHireAndFindDatas( cityId, menuId, currentPage, true);
        } else {
            serviceAdapter.getList().clear();
            hireAndFindModel.initHireAndFindDatas( cityId, menuId, currentPage, false);
        }

    }


    private MyHandler handler = new MyHandler();
    private int mCurrentCounter = 0;

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<HireFind> list) {

        serviceAdapter.addItems(list);
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
                RecyclerViewStateUtils.setFooterViewState(getActivity(), recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(getActivity(), recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }

    };

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
        Intent intent = new Intent(activity, HireAndFindDetailActivity.class);
        intent.putExtra("conId", serviceAdapter.getList().get(position).getId());
        intent.putExtra("isform_fen", true);
        activity.startActivity(intent);
    }

    @Override
    public void LoadSuccess(String msg) {
        mHasLoadedOnce = true;
        ProgressDlgUtil.stopProgressDlg();
        if (swipeRefreshLayout.isRefreshing())
            recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
        serviceAdapter.addItems(hireAndFindModel.getHireFindList());
        serviceAdapter.notifyDataSetChanged();
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


    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    isLoadMore = true;
                    if (isMore && isLoadMore) {
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
                    Log.i("zjz", "有网络");
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}