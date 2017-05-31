package com.abcs.haiwaigou.local.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.local.adapter.NewsAdapter;
import com.abcs.haiwaigou.local.beans.HireFind;
import com.abcs.haiwaigou.local.fragment.adapter.HireJobAdapter;
import com.abcs.haiwaigou.local.model.HireAndFindModel;
import com.abcs.haiwaigou.local.viewholder.NewsViewHolder;
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
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewsMoreActivity extends BaseActivity implements RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh, NewsViewHolder.RootOnClick, HireAndFindModel.LoadStateInterFace {

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
    NewsAdapter newsAdapter;
    String cityId;
    private View view;
    public static HashMap<String, Activity> newsMoreHashMap = new HashMap<String, Activity>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(view==null){
            view=getLayoutInflater().inflate(R.layout.local_activity_news_more,null);
        }
        setContentView(view);
        ButterKnife.inject(this);
        tTitleName.setText("更多新闻");
        cityId=getIntent().getStringExtra("cityId");
        hireAndFindModel=new HireAndFindModel(this,this);
        initRecycler();
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        newsMoreHashMap.put(MyString.NEWSMORE,this);
    }

    private void initRecycler() {
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        recyclerView.addOnScrollListener(mOnScrollListener);
        newsAdapter = new NewsAdapter(this, this);
        initAllDates();
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(newsAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(this, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
    }
    public void initMoreDates() {
        isLoadMore = true;
        currentPage += 1;
        Log.i("zjz", "current=" + currentPage);
        initAllDates();

    }
    private void initAllDates() {

        if (isLoadMore) {
            hireAndFindModel.initMoreNews(cityId, currentPage, true);
        } else {
            newsAdapter.getList().clear();
            hireAndFindModel.initMoreNews(cityId, currentPage, false);
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
        Intent intent=new Intent(this, NewsDetailActivity.class);
        intent.putExtra("content",newsAdapter.getList().get(position).getContent());
        intent.putExtra("datas",hireAndFindModel.getNewsList());
        startActivity(intent);
    }

    @Override
    public void LoadSuccess(String msg) {
        ProgressDlgUtil.stopProgressDlg();
        if (swipeRefreshLayout.isRefreshing())
            recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
        newsAdapter.addItems(hireAndFindModel.getNewsList());
        newsAdapter.notifyDataSetChanged();
        if(initMoreInterfacer!=null)
            initMoreInterfacer.notifyDataSetChanged();
    }

    @Override
    public void LoadFailed(String msg) {
        ProgressDlgUtil.stopProgressDlg();
        if (swipeRefreshLayout.isRefreshing())
            recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
        if(initMoreInterfacer!=null)
            initMoreInterfacer.notifyDataSetChanged();
    }

    @Override
    public void isMore(boolean state) {
        isMore = state;
        NewsDetailActivity.isMore=state;
        Log.i("zjz", "isMore=" + isMore);
    }

    private PreviewHandler mHandler = new PreviewHandler(this);
    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
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
                RecyclerViewStateUtils.setFooterViewState(NewsMoreActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(NewsMoreActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }

    };


    private class PreviewHandler extends Handler {

        private WeakReference<NewsMoreActivity> ref;

        PreviewHandler(NewsMoreActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final NewsMoreActivity activity = ref.get();
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
            RecyclerViewStateUtils.setFooterViewState(NewsMoreActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                if (NetworkUtils.isNetAvailable(NewsMoreActivity.this)) {
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
        newsMoreHashMap.clear();
    }

    public void initInterfacer(InitMoreInterfacer initMoreInterfacer) {
        this.initMoreInterfacer = initMoreInterfacer;
    }

    InitMoreInterfacer initMoreInterfacer;

    public interface InitMoreInterfacer {
        void notifyDataSetChanged();
    }
}
