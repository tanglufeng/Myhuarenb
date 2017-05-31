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

import com.abcs.haiwaigou.local.adapter.MessageAdapter;
import com.abcs.haiwaigou.local.model.HireAndFindModel;
import com.abcs.haiwaigou.local.viewholder.MessageViewHolder;
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
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MessageMoreActivity extends BaseActivity implements RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh, MessageViewHolder.RootOnClick, HireAndFindModel.LoadStateInterFace {

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
    MessageAdapter messageAdapter;
    String cityId;
    private View view;
    public static HashMap<String, MessageMoreActivity> msgMoreList = new HashMap<String, MessageMoreActivity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.local_activity_news_more, null);
        }
        setContentView(view);
        ButterKnife.inject(this);
        tTitleName.setText("更多消息");
        cityId = getIntent().getStringExtra("cityId");
        hireAndFindModel = new HireAndFindModel(this, this);
        initRecycler();
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        msgMoreList.put(MyString.MSGMORE, this);
    }

    private void initRecycler() {
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        recyclerView.addOnScrollListener(mOnScrollListener);
        messageAdapter = new MessageAdapter(this, this);
        initAllDates();
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(messageAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(this, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
    }

    public void initAllDates() {

        if (isLoadMore) {
            hireAndFindModel.initMoreMsg(cityId, currentPage, true);
        } else {
            messageAdapter.getList().clear();
            hireAndFindModel.initMoreMsg(cityId, currentPage, false);
        }

    }

    public void initMoreDates() {
        isLoadMore = true;
        currentPage += 1;
        Log.i("zjz", "current=" + currentPage);
        initAllDates();
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
        Intent intent = null;
//        if (hireAndFindModel.getMsgList().get(position).getListTypeId().equals(MyString.LOCAL_MENU2)) {
//            intent = new Intent(this, RestaurantDetailActivity.class);
//            intent.putExtra("conId", hireAndFindModel.getMsgList().get(position).getId());
//            startActivity(intent);
//        } else {
        intent = new Intent(this, HireAndFindDetailActivity.class);
        intent.putExtra("isMsg", true);
        intent.putExtra("isform_fen", false);
        intent.putParcelableArrayListExtra("datas", hireAndFindModel.getMsgList());
        intent.putExtra("conId", hireAndFindModel.getMsgList().get(position).getId());
        startActivity(intent);
//        }
    }

    @Override
    public void LoadSuccess(String msg) {
        ProgressDlgUtil.stopProgressDlg();
        if (swipeRefreshLayout != null)
            if (swipeRefreshLayout.isRefreshing())
                recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
        messageAdapter.addItems(hireAndFindModel.getMsgList());
        messageAdapter.notifyDataSetChanged();
        if (initMoreInterfacer != null)
            initMoreInterfacer.notifyDataSetChanged();
    }

    @Override
    public void LoadFailed(String msg) {
        ProgressDlgUtil.stopProgressDlg();
        if (swipeRefreshLayout != null)
            if (swipeRefreshLayout.isRefreshing())
                recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
        if (initMoreInterfacer != null)
            initMoreInterfacer.notifyDataSetChanged();
    }

    @Override
    public void isMore(boolean state) {
        isMore = state;
        HireAndFindDetailActivity.isMore = isMore;
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
                RecyclerViewStateUtils.setFooterViewState(MessageMoreActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(MessageMoreActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }

    };


    private class PreviewHandler extends Handler {

        private WeakReference<MessageMoreActivity> ref;

        PreviewHandler(MessageMoreActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final MessageMoreActivity activity = ref.get();
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
            RecyclerViewStateUtils.setFooterViewState(MessageMoreActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                if (NetworkUtils.isNetAvailable(MessageMoreActivity.this)) {
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

    public void initInterfacer(InitMoreInterfacer initMoreInterfacer) {
        this.initMoreInterfacer = initMoreInterfacer;
    }

    InitMoreInterfacer initMoreInterfacer;

    public interface InitMoreInterfacer {
        void notifyDataSetChanged();
    }
}
