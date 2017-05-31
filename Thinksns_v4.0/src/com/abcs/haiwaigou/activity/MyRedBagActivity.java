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

import com.abcs.haiwaigou.adapter.MyRedBagAdapter;
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
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.model.User;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyRedBagActivity extends BaseActivity implements RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.img_user)
    ImageView imgUser;
    @InjectView(R.id.t_num)
    TextView tNum;
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
    private PreviewHandler mHandler = new PreviewHandler(this);
    private View view;
    int totalPage;
    int currentPage = 1;
    private int mCurrentCounter = 0;

    private Handler handler = new Handler();

    MyRedBagAdapter myRedBagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_activity_my_red_bag, null);
        }
        setContentView(view);
        ButterKnife.inject(this);

        User self = MyApplication.getInstance().self;
        if (self != null) {
            ImageLoader.getInstance().displayImage(self.getAvatarUrl(), imgUser, Options.getCircleListOptions());
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
        myRedBagAdapter = new MyRedBagAdapter(this);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(myRedBagAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(this, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
    }


    private void initAllDates() {
        final ArrayList<Goods> dataList = new ArrayList<>();
        if (!first) {
            ProgressDlgUtil.showProgressDlg("Loading...", this);
        }
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_my_redbag + "&key=" + MyApplication.getInstance().getMykey(), "&curpage=" + currentPage, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject response = new JSONObject(msg);
                            if (response.getInt("code") == 200) {
                                Log.i("zjz", "my_redbag_msg=" + response);

                                JSONObject object1 = response.optJSONObject("datas");
                                if(object1.has("error")){
                                    if (layoutNull != null && swipeRefreshLayout != null) {
                                        layoutNull.setVisibility( View.VISIBLE);
                                        swipeRefreshLayout.setVisibility(View.GONE );
                                    }
                                    return;
                                }
                                if (response.optInt("page_total") != 0) {
                                    totalPage = response.optInt("page_total");
                                    if (tNum != null)
                                        tNum.setText(object1.optString("count"));
                                    object1.optString("sum_voucher_template");//领的优惠券的总额
                                    object1.optString("sum_rechargecard");//领的充值卡的总额
                                    JSONArray array = object1.optJSONArray("red_envelope_info");
                                    if (isLoadMore) {
                                        int currentSize = myRedBagAdapter.getItemCount();
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject rechargeObject = array.getJSONObject(i);
                                            Goods goods = new Goods();
                                            goods.setId(currentSize + i);
                                            goods.setBuyer_id(rechargeObject.optString("member_id"));
                                            goods.setBuyer_name(rechargeObject.optString("member_name"));
                                            goods.setTime(rechargeObject.optLong("receive_date"));
                                            goods.setState_desc(rechargeObject.optString("status"));
                                            goods.setMoney(rechargeObject.optDouble("relation_desc"));
                                            goods.setKeywords(rechargeObject.optString("relation_id"));
                                            dataList.add(goods);
                                        }
                                        addItems(dataList);

                                    } else {
                                        myRedBagAdapter.getList().clear();
                                        dataList.clear();
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject rechargeObject = array.getJSONObject(i);
                                            Goods goods = new Goods();
                                            goods.setId(i);
                                            goods.setBuyer_id(rechargeObject.optString("member_id"));
                                            goods.setBuyer_name(rechargeObject.optString("member_name"));
                                            goods.setTime(rechargeObject.optLong("receive_date"));
                                            goods.setState_desc(rechargeObject.optString("status"));
                                            goods.setMoney(rechargeObject.optDouble("relation_desc"));
                                            goods.setKeywords(rechargeObject.optString("relation_id"));
                                            dataList.add(goods);
                                        }
                                        mCurrentCounter = dataList.size();
                                        myRedBagAdapter.addItems(dataList);
                                        myRedBagAdapter.notifyDataSetChanged();
                                    }
                                    if (layoutNull != null && swipeRefreshLayout != null) {
                                        layoutNull.setVisibility(myRedBagAdapter.getList().size() == 0 ? View.VISIBLE : View.GONE);
                                        swipeRefreshLayout.setVisibility(myRedBagAdapter.getList().size() == 0 ? View.GONE : View.VISIBLE);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            Log.i("zjz", e.toString());
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

        myRedBagAdapter.addItems(list);
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
                RecyclerViewStateUtils.setFooterViewState(MyRedBagActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(MyRedBagActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }
    };

    private class PreviewHandler extends Handler {

        private WeakReference<MyRedBagActivity> ref;

        PreviewHandler(MyRedBagActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final MyRedBagActivity activity = ref.get();
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
            RecyclerViewStateUtils.setFooterViewState(MyRedBagActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                if (NetworkUtils.isNetAvailable(MyRedBagActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

}
