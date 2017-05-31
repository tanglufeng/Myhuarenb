package com.abcs.haiwaigou.yyg.fragment;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager;
import com.abcs.haiwaigou.fragment.viewholder.CommentItemFragmentViewHolder;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.utils.RecyclerViewAndSwipeRefreshLayout;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.haiwaigou.view.recyclerview.EndlessRecyclerOnScrollListener;
import com.abcs.haiwaigou.view.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.abcs.haiwaigou.view.recyclerview.LoadingFooter;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.haiwaigou.view.recyclerview.RecyclerViewStateUtils;
import com.abcs.haiwaigou.yyg.activity.YYGActivity;
import com.abcs.haiwaigou.yyg.adapter.YYGMyBuyRecordAdapter;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.presenter.MyPrsenter;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by zjz on 2016/1/16.
 */
public class YYGMyBuyRecordFragment extends BaseFragment implements RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh, CommentItemFragmentViewHolder.ItemOnClick {

    YYGActivity activity;
    View rootView;
    ArrayList<Goods> goodsList = new ArrayList<Goods>();
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView imgNull;
    TextView tvNull;
    RelativeLayout layoutNull;
    TextView tRefresh;

    private View view;
    int totalPage;
    int currentPage = 1;
    boolean isLoadMore = false;
    boolean first = false;
    boolean isMore = true;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    YYGMyBuyRecordAdapter yygMyBuyRecordAdapter;
    private RequestQueue mRequestQueue;

    private Handler handler = new Handler();
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    boolean isShow;

    private MyBroadCastReceiver myBroadCastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (YYGActivity) getActivity();
        view = activity.getLayoutInflater().inflate(
                R.layout.hwg_fragment_comment_item, null);
        mRequestQueue = Volley.newRequestQueue(activity);
        myBroadCastReceiver = new MyBroadCastReceiver(activity, updateUI);
        myBroadCastReceiver.register();
        initView();
    }

    MyBroadCastReceiver.UpdateUI updateUI = new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

        }

        @Override
        public void updateCarNum(Intent intent) {

        }

        @Override
        public void updateCollection(Intent intent) {
            if (intent.getStringExtra("type").equals(MyUpdateUI.YYGBUY)) {
                first = true;
                Log.i("zjz", "YYG支付成功");
                isLoadMore = false;
                currentPage = 1;
                initRecycler();
            }
            if (intent.getStringExtra("type").equals(MyUpdateUI.YYGLOTTERY)) {
                Log.i("zjz", "更新揭晓");
                first = true;
                isLoadMore = false;
                currentPage = 1;
                initRecycler();
            }
            if (intent.getStringExtra("type").equals(MyUpdateUI.YYGSETADDRESS) || intent.getStringExtra("type").equals(MyUpdateUI.YYGREFUND)) {
                first = true;
                isLoadMore = false;
                currentPage = 1;
                initRecycler();
            }

        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(activity));
        layoutNull = (RelativeLayout) view.findViewById(R.id.layout_null);
        tRefresh = (TextView) view.findViewById(R.id.t_refresh);
        tRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(true);
                initRecycler();
                if (activity.myYYGFragment != null)
                    new MyPrsenter(activity.myYYGFragment).loginSuccess();
            }
        });
    }

    public void initRecycler() {

        recyclerView.addOnScrollListener(mOnScrollListener);
        initAllDates();
        yygMyBuyRecordAdapter = new YYGMyBuyRecordAdapter(activity);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(yygMyBuyRecordAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(activity, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE_MCOMMENT);
    }

    private void initAllDates() {
        if (!first) {
            ProgressDlgUtil.showProgressDlg("Loading...", activity);
        }
        Log.i("zjz", "currentPage=" + currentPage);
        HttpRequest.sendGet(TLUrl.getInstance().URL_yyg_goods_my_buy_record, "userId=" + MyApplication.getInstance().self.getId() + "&page=" + currentPage + "&pageSize=10",
                new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {
                        Log.i("zjz", "yyg_my_buy_record_msg=" + msg);
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (msg.length() == 0) {
                                    if (tRefresh != null && !first) {
                                        tRefresh.setVisibility(View.VISIBLE);
                                        Toast.makeText(activity, "请求失败,请重试", Toast.LENGTH_SHORT).show();
                                    }
                                    ProgressDlgUtil.stopProgressDlg();
                                    if (recyclerViewAndSwipeRefreshLayout != null && recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().isRefreshing())
                                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                                    return;
                                }
                                if (tRefresh != null)
                                    tRefresh.setVisibility(View.GONE);
                                try {
                                    JSONObject result = new JSONObject(msg);
                                    if (result.optInt("status") == 1) {
                                        JSONArray jsonArray = result.optJSONArray("msg");
                                        if (jsonArray != null) {
                                            if (isLoadMore) {
                                                int currentSize = yygMyBuyRecordAdapter.getItemCount();
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    if (jsonArray.length() == 10) {
                                                        isMore = true;
                                                    } else {
                                                        isMore = false;
                                                    }
                                                    Goods goods = new Goods();
                                                    goods.setId(currentSize + i);
                                                    JSONObject object = jsonArray.getJSONObject(i);
                                                    JSONObject actObj = object.optJSONObject("activity");
                                                    if (actObj != null) {
                                                        JSONObject goodsObj = actObj.optJSONObject("goods");
                                                        JSONObject userObj = actObj.optJSONObject("user");
                                                        if (userObj != null) {
                                                            goods.setCid(userObj.optString("userId"));
                                                        }
                                                        goods.setTitle(goodsObj.optString("name"));
                                                        goods.setTotal_money(goodsObj.optString("price"));
                                                        goods.setPicarr(goodsObj.optString("picture"));
                                                        goods.setState_desc(goodsObj.optString("isEntity"));
                                                        goods.setGoods_state(actObj.optString("isDeliver"));
                                                        if (actObj.optInt("unitPrice") < 1) {
                                                            goods.setZongrenshu(actObj.optInt("tatalPrice"));
                                                            goods.setShenyurenshu(actObj.optInt("surplusValue"));
                                                        } else {
                                                            goods.setShenyurenshu(actObj.optInt("surplusValue") / actObj.optInt("unitPrice"));
                                                            goods.setZongrenshu(actObj.optInt("tatalPrice") / actObj.optInt("unitPrice"));
                                                        }
                                                        goods.setCanyurenshu(goods.getZongrenshu() - goods.getShenyurenshu());
                                                        goods.setYunjiage(actObj.optDouble("unitPrice"));
                                                        goods.setQ_uid(object.optString("activityId"));
                                                        goods.setLayoutType(actObj.optInt("substate"));
                                                        goods.setGoods_salenum(object.optString("buyNum"));
                                                        goods.setQ_end_time(actObj.optLong("accomplishTime"));
                                                        goods.setQ_user_code(actObj.optString("luckCode"));
                                                        goods.setQishu(actObj.optInt("periodsNum"));
                                                    }
                                                    goods.setGoods_id(object.optString("goodsId"));


                                                    goodsList.add(goods);

                                                    addItems(goodsList);
                                                }
                                            } else {
                                                yygMyBuyRecordAdapter.getList().clear();
                                                goodsList.clear();
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    Goods goods = new Goods();
                                                    goods.setId(i);
                                                    JSONObject object = jsonArray.getJSONObject(i);
                                                    JSONObject actObj = object.optJSONObject("activity");
                                                    if (actObj != null) {
                                                        JSONObject goodsObj = actObj.optJSONObject("goods");
                                                        JSONObject userObj = actObj.optJSONObject("user");
                                                        if (userObj != null) {
                                                            goods.setCid(userObj.optString("userId"));
                                                        }
                                                        goods.setTitle(goodsObj.optString("name"));
                                                        goods.setTotal_money(goodsObj.optString("price"));
                                                        goods.setPicarr(goodsObj.optString("picture"));
                                                        goods.setState_desc(goodsObj.optString("isEntity"));
                                                        goods.setGoods_state(actObj.optString("isDeliver"));
                                                        if (actObj.optInt("unitPrice") < 1) {
                                                            goods.setZongrenshu(actObj.optInt("tatalPrice"));
                                                            goods.setShenyurenshu(actObj.optInt("surplusValue"));
                                                        } else {
                                                            goods.setShenyurenshu(actObj.optInt("surplusValue") / actObj.optInt("unitPrice"));
                                                            goods.setZongrenshu(actObj.optInt("tatalPrice") / actObj.optInt("unitPrice"));
                                                        }
                                                        goods.setCanyurenshu(goods.getZongrenshu() - goods.getShenyurenshu());
                                                        goods.setYunjiage(actObj.optDouble("unitPrice"));
                                                        goods.setQ_uid(object.optString("activityId"));
                                                        goods.setLayoutType(actObj.optInt("substate"));
                                                        goods.setGoods_salenum(object.optString("buyNum"));
                                                        goods.setQ_end_time(actObj.optLong("accomplishTime"));
                                                        goods.setQ_user_code(actObj.optString("luckCode"));
                                                        goods.setQishu(actObj.optInt("periodsNum"));
                                                    }
                                                    goods.setGoods_id(object.optString("goodsId"));
                                                    goodsList.add(goods);
                                                    if (goodsList.size() == 10) {
                                                        isMore = true;
                                                    } else {
                                                        isMore = false;
                                                    }
                                                    yygMyBuyRecordAdapter.addItems(goodsList);
                                                    yygMyBuyRecordAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        } else {
                                            isMore = false;
                                        }
                                        if (layoutNull != null)
                                            layoutNull.setVisibility(yygMyBuyRecordAdapter.getList().size() == 0 ? View.VISIBLE : View.GONE);
                                        mHasLoadedOnce = true;
                                        Log.i("zjz", "yyg_mybuy_isMore=" + isMore);
//                                        yygMyBuyRecordAdapter.notifyDataSetChanged();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } finally {

                                    ProgressDlgUtil.stopProgressDlg();
                                    if (recyclerViewAndSwipeRefreshLayout != null && recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().isRefreshing())
                                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                                }

                            }
                        });
                    }
                });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeAllViewsInLayout();
        ButterKnife.inject(this, view);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        myBroadCastReceiver.unRegister();
    }

    @Override
    public void swipeRefreshLayoutOnRefresh() {
        first = true;
        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(true);
        isLoadMore = false;
        currentPage = 1;
        initAllDates();
        if (activity.myYYGFragment != null)
            new MyPrsenter(activity.myYYGFragment).loginSuccess();
    }

    @Override
    public void onItemRootViewClick(int position) {

    }

    @Override
    public void onImgClick(int position) {

    }

    private MyHandler mHandler = new MyHandler();
    private int mCurrentCounter = 0;

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Goods> list) {

        yygMyBuyRecordAdapter.addItems(list);
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
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }

        initRecycler();


    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
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
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }
}
