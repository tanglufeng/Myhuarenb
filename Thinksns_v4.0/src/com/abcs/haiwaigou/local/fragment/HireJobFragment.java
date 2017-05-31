package com.abcs.haiwaigou.local.fragment;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.local.activity.HireAndFindActivity2;
import com.abcs.haiwaigou.local.activity.HireAndFindDetailActivity;
import com.abcs.haiwaigou.local.beans.HireFind;
import com.abcs.haiwaigou.local.fragment.adapter.HireJobAdapter;
import com.abcs.haiwaigou.local.fragment.viewholder.HireJobViewHolder;
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
 * Created by zjz on 2016/9/5.
 */
public class HireJobFragment extends BaseFragment implements RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh, HireJobViewHolder.RootOnClick, HireAndFindModel.LoadStateInterFace {

    HireAndFindActivity2 activity;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.t_refresh)
    TextView tRefresh;
    @InjectView(R.id.layout_null)
    RelativeLayout layoutNull;
    @InjectView(R.id.t_send)
    TextView tSend;
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
    HireJobAdapter hireJobAdapter;
    int currentPage = 1;
    public HireAndFindModel hireAndFindModel;
    //    String countryId;
    String menuId,oLd_menuId;
    String cityId;
    String typeName;


    public static HireJobFragment newInstance(String cityId, String menuId, String typeName,String oLd_menuId) {
        Bundle bundle = new Bundle();
//        bundle.putSerializable("countryId", countryId);
        bundle.putSerializable("cityId", cityId);
        bundle.putSerializable("menuId", menuId);
        bundle.putSerializable("oLd_menuId", oLd_menuId);
        bundle.putSerializable("typeName", typeName);
        HireJobFragment hireJobFragment = new HireJobFragment();
        hireJobFragment.setArguments(bundle);
        return hireJobFragment;
    }
//
//    public static HireJobFragment newInstance(String cityId, String menuId, String typeName) {
//        Bundle bundle = new Bundle();
////        bundle.putSerializable("countryId", countryId);
//        bundle.putSerializable("cityId", cityId);
//        bundle.putSerializable("menuId", menuId);
//        bundle.putSerializable("typeName", typeName);
//        HireJobFragment hireJobFragment = new HireJobFragment();
//        hireJobFragment.setArguments(bundle);
//        return hireJobFragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HireAndFindActivity2) getActivity();
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
            oLd_menuId = bundle.getString("oLd_menuId");
            typeName = bundle.getString("typeName");
        }
        isPrepared = true;
        lazyLoad();
        HireAndFindActivity2.fragmentHashMap.put(typeName, this);
        tRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRecycler();
            }
        });
        tSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.go2Publish();
            }
        });
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
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
        hireJobAdapter = new HireJobAdapter(activity, this,oLd_menuId);
//        hireJobAdapter = new HireJobAdapter(activity, this);  //旧的
        initAllDates();
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(hireJobAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(activity, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
    }


    public void initMoreDates() {
        if (hireAndFindModel == null) {
            hireAndFindModel = new HireAndFindModel(this);
        }
        isLoadMore = true;
        currentPage += 1;
        Log.i("zjz", "current=" + currentPage);
        initAllDates();
    }

    private void initAllDates() {

        if (isLoadMore) {
            hireAndFindModel.initHireAndFindDatas(cityId, menuId, currentPage, true);
        } else {
            hireJobAdapter.getList().clear();
            hireAndFindModel.initHireAndFindDatas(cityId, menuId, currentPage, false);
        }

    }


    private MyHandler handler = new MyHandler();
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

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        },200);
    }

    @Override
    public void itemRootOnClick(int position) {
        Intent intent = null;
//        if (hireJobAdapter.getList().get(position).getListTypeId().equals(MyString.LOCAL_MENU2)) {
//            intent = new Intent(activity, RestaurantDetailActivity.class);
//            intent.putExtra("conId", hireJobAdapter.getList().get(position).getId());
//            activity.startActivity(intent);
//        } else {
        intent = new Intent(activity, HireAndFindDetailActivity.class);
        intent.putParcelableArrayListExtra("datas", hireAndFindModel.getHireFindList());
        intent.putExtra("conId", hireJobAdapter.getList().get(position).getId());
        intent.putExtra("typeName", typeName);
        intent.putExtra("isform_fen", false);
        activity.startActivity(intent);
//        }

    }


    @Override
    public void LoadSuccess(String msg) {
        mHasLoadedOnce = true;
        ProgressDlgUtil.stopProgressDlg();
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing())
            recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
        hireJobAdapter.addItems(hireAndFindModel.getHireFindList());
        hireJobAdapter.notifyDataSetChanged();
        if (hireAndFindModel.getHireFindList().size() == 0) {
            layoutNull.setVisibility(View.VISIBLE);
            if(tSend!=null)
                tSend.setVisibility(View.VISIBLE);
        } else {
            layoutNull.setVisibility(View.GONE);
        }
        if (initMoreInterfacer != null)
            initMoreInterfacer.notifyDataSetChanged();
    }

    @Override
    public void LoadFailed(String msg) {
        if (msg.equals(MyString.NODATA)) {
            mHasLoadedOnce = true;
            if (layoutNull != null) {
                if (hireAndFindModel.getHireFindList().size() == 0) {
                    layoutNull.setVisibility(View.VISIBLE);
                    if(tSend!=null)
                        tSend.setVisibility(View.VISIBLE);
                } else {
                    layoutNull.setVisibility(View.GONE);
                }
            }
        }
        if (msg.equals(MyString.LOADFAILED)) {
            tRefresh.setVisibility(View.VISIBLE);
        } else {
            tRefresh.setVisibility(View.GONE);
        }
        ProgressDlgUtil.stopProgressDlg();
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing())
            recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
        if (initMoreInterfacer != null)
            initMoreInterfacer.notifyDataSetChanged();
    }

    @Override
    public void isMore(boolean state) {
        isMore = state;
        HireAndFindDetailActivity.isMore = state;
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

    public void initInterfacer(InitMoreInterfacer initMoreInterfacer) {
        this.initMoreInterfacer = initMoreInterfacer;
    }

    InitMoreInterfacer initMoreInterfacer;

    public interface InitMoreInterfacer {
        void notifyDataSetChanged();
    }
}
