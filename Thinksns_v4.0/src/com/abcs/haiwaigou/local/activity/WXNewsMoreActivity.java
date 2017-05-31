package com.abcs.haiwaigou.local.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.local.adapter.NewsAdapter;
import com.abcs.haiwaigou.local.adapter.WxNewsAdapter;
import com.abcs.haiwaigou.local.interfacer.LoadStateInterface;
import com.abcs.haiwaigou.local.model.HireAndFindModel;
import com.abcs.haiwaigou.local.model.NewsModel;
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
import com.abcs.huaqiaobang.tljr.news.HuanQiuShiShi;
import com.abcs.huaqiaobang.tljr.news.NewsActivity;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.sociax.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WXNewsMoreActivity extends BaseActivity implements RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh, NewsViewHolder.RootOnClick, LoadStateInterface {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.t_title_name)
    TextView tTitleName;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.local_activity_wxnews_more)
    LinearLayout localActivityWxnewsMore;

    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    boolean isLoadMore = false;
    boolean isMore = true;
    int currentPage = 1;
    NewsModel newsModel;
    WxNewsAdapter wxNewsAdapter;
    String cityId;
    private View view;
    public static HashMap<String, Activity> wxMoreHashMap = new HashMap<String, Activity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.local_activity_wxnews_more, null);
        }
        setContentView(view);
        ButterKnife.inject(this);
        tTitleName.setText("更多新闻");
        cityId = getIntent().getStringExtra("cityId");
        newsModel = new NewsModel(this, this);
        initRecycler();
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        wxMoreHashMap.put(MyString.WXNEWSMORE, this);
    }

    private void initRecycler() {
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        recyclerView.addOnScrollListener(mOnScrollListener);
        wxNewsAdapter = new WxNewsAdapter(this, this);
        initAllDates();
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(wxNewsAdapter);
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
            newsModel.initDatas(cityId, true);
        } else {
            wxNewsAdapter.getList().clear();
            newsModel.initDatas(cityId, false);
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
        Intent intent = new Intent(this, WXNewsDetailActivity.class);
        intent.putExtra("special", wxNewsAdapter.getList().get(position).getSpecies());
        intent.putExtra("id", wxNewsAdapter.getList().get(position).getId());
        intent.putExtra("time", wxNewsAdapter.getList().get(position).getTime());
        intent.putParcelableArrayListExtra("datas", newsModel.getNewsList());
        startActivity(intent);
    }

    @Override
    public void LoadSuccess(String msg) {
        ProgressDlgUtil.stopProgressDlg();
        if (swipeRefreshLayout.isRefreshing())
            recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
        wxNewsAdapter.addItems(newsModel.getNewsList());
        wxNewsAdapter.notifyDataSetChanged();
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
    public void LoadEmpty(String empMsg) {

    }

    @Override
    public void isLoadMore(boolean state) {
        isMore = state;
        WXNewsDetailActivity.isMore=state;
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
                RecyclerViewStateUtils.setFooterViewState(WXNewsMoreActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(WXNewsMoreActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }

    };


    private class PreviewHandler extends Handler {

        private WeakReference<WXNewsMoreActivity> ref;

        PreviewHandler(WXNewsMoreActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final WXNewsMoreActivity activity = ref.get();
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
            RecyclerViewStateUtils.setFooterViewState(WXNewsMoreActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                if (NetworkUtils.isNetAvailable(WXNewsMoreActivity.this)) {
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
        wxMoreHashMap.clear();
    }

    public void initInterfacer(InitMoreInterfacer initMoreInterfacer) {
        this.initMoreInterfacer = initMoreInterfacer;
    }

    InitMoreInterfacer initMoreInterfacer;

    public interface InitMoreInterfacer {
        void notifyDataSetChanged();
    }
}
