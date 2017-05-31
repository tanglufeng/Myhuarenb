package com.abcs.haiwaigou.yyg.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.RecyclerViewAndSwipeRefreshLayout;
import com.abcs.haiwaigou.utils.SpacesItemDecoration;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.haiwaigou.view.recyclerview.EndlessRecyclerOnScrollListener;
import com.abcs.haiwaigou.view.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.abcs.haiwaigou.view.recyclerview.LoadingFooter;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.haiwaigou.view.recyclerview.RecyclerViewStateUtils;
import com.abcs.haiwaigou.yyg.adapter.YYGLotteryFragmentAdapter;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zjz on 2016/7/4 0004.
 */
public class YYGLotteryFragment extends BaseFragment implements RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh {
    Activity activity;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
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

    int totalPage;
    int currentPage = 1;
    boolean isLoadMore = false;
    boolean isMore = true;
    boolean first = false;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    YYGLotteryFragmentAdapter lotteryFragmentAdapter;
    private ArrayList<Goods> goodsList = new ArrayList<Goods>();
    private MyBroadCastReceiver myBroadCastReceiver;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        activity = getActivity();

        if (view == null) {
            view = activity.getLayoutInflater().inflate(
                    R.layout.hwg_yyg_fragment_goods, null);
            ButterKnife.inject(this, view);
//            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            myBroadCastReceiver = new MyBroadCastReceiver(activity, updateUI);
            myBroadCastReceiver.register();
            isPrepared = true;
            lazyLoad();
            initRecycler();
            tRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(true);
                    initRecycler();
                }
            });
        }
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);

        ButterKnife.inject(this, view);
        return view;
//        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
//        ViewGroup parent = (ViewGroup)mFragmentView.getParent();
//        if(parent != null) {
//            parent.removeView(mFragmentView);
//        }
//        return mFragmentView;
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
            if (intent.getStringExtra("type").equals(MyUpdateUI.YYGLOTTERY)) {
                Log.i("zjz", "更新揭晓");
                first=true;
                currentPage=1;
                isLoadMore=false;
                initAllDates();
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initRecycler() {
        if (recyclerView != null) {
            recyclerView.addOnScrollListener(mOnScrollListener);
            lotteryFragmentAdapter = new YYGLotteryFragmentAdapter(activity);
            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_size1);
            recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
            initAllDates();
//        fullyGridLayoutManager = new FullyGridLayoutManager(getContext(), 2);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);

            mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(lotteryFragmentAdapter);
            recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(activity, view, mHeaderAndFooterRecyclerViewAdapter, this, gridLayoutManager, false);
        }


    }

    private void initAllDates() {
        if (!first) {
            ProgressDlgUtil.showProgressDlg("Loading...", activity);
        }
        Log.i("zjz", "currentPage=" + currentPage);

        String param=null;
        if(MyApplication.getInstance().self==null){
            param="status=1" + "&page=" + currentPage + "&pageSize=10";
        }else {
            param="status=1" + "&page=" + currentPage + "&pageSize=10"+"&userId="+ MyApplication.getInstance().self.getId();
        }
        HttpRequest.sendGet(TLUrl.getInstance().URL_yyg_goods_list,param,
                new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {
                        Log.i("zjz", "yyg_lottery_msg=" + msg);
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (msg.length() == 0) {
                                    if (tRefresh != null && !first) {
                                        tRefresh.setVisibility(View.VISIBLE);
                                        Toast.makeText(activity, "请求失败,请重试", Toast.LENGTH_SHORT).show();
                                    }
                                    ProgressDlgUtil.stopProgressDlg();
                                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                                    return;
                                }
                                if (tRefresh != null)
                                    tRefresh.setVisibility(View.GONE);
                                try {
                                    JSONObject result = new JSONObject(msg);
                                    if (result.optInt("status") == 1) {
                                        JSONArray jsonArray = result.getJSONArray("msg");
                                        if (jsonArray != null) {
                                            if (isLoadMore) {
                                                if (jsonArray.length() == 10) {
                                                    isMore = true;
                                                } else {
                                                    isMore = false;
                                                }
                                                int currentSize = lotteryFragmentAdapter.getItemCount();
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    Goods goods = new Goods();
                                                    goods.setId(currentSize + i);
                                                    JSONObject object = jsonArray.getJSONObject(i);
                                                    JSONObject goodsObj = object.optJSONObject("goods");
                                                    JSONObject userObj = object.optJSONObject("user");
                                                    goods.setGoods_id(object.optString("goodsId"));
                                                    goods.setTitle(goodsObj.optString("name"));
                                                    goods.setTotal_money(goodsObj.optString("price"));
                                                    goods.setPicarr(goodsObj.optString("picture"));
                                                    goods.setZongrenshu(object.optInt("tatalPrice") / object.optInt("unitPrice"));
                                                    goods.setShenyurenshu(object.optInt("surplusValue") / object.optInt("unitPrice"));
                                                    goods.setCanyurenshu(goods.getZongrenshu() - goods.getShenyurenshu());
                                                    goods.setYunjiage(object.optDouble("unitPrice"));
                                                    goods.setQ_uid(object.optString("uuid"));
                                                    goods.setState_desc(object.optString("substate"));
                                                    goods.setQ_user(userObj.optString("nickname"));
                                                    goods.setQ_end_time(object.optLong("accomplishTime") + (3 * 60 * 1000));
                                                    goods.setQ_content(object.optString("luckCode"));
                                                    goods.setIf_lock(object.optBoolean("is_buy"));
                                                    goods.setQishu(object.optInt("periodsNum"));
                                                    goods.setGoods_salenum(object.optString("buyNum"));
                                                    goods.setQ_counttime(object.optString("surplusTime"));
                                                    goods.setQ_showtime(object.optLong("surplusTime") + System.currentTimeMillis());
                                                    goods.setQ_user_code(object.optString("isPrizeWinner"));
                                                    goodsList.add(goods);

                                                    addItems(goodsList);
                                                    lotteryFragmentAdapter.notifyDataSetChanged();
                                                }
                                            } else {
                                                lotteryFragmentAdapter.getList().clear();
                                                goodsList.clear();
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    Goods goods = new Goods();
                                                    goods.setId(i);
                                                    JSONObject object = jsonArray.getJSONObject(i);
                                                    JSONObject goodsObj = object.optJSONObject("goods");
                                                    JSONObject userObj = object.optJSONObject("user");
                                                    if(userObj!=null)
                                                        goods.setQ_user(userObj.optString("nickname"));
                                                    goods.setGoods_id(object.optString("goodsId"));
                                                    goods.setTitle(goodsObj.optString("name"));
                                                    goods.setTotal_money(goodsObj.optString("price"));
                                                    goods.setPicarr(goodsObj.optString("picture"));
                                                    goods.setZongrenshu(object.optInt("tatalPrice"));
                                                    goods.setShenyurenshu(object.optInt("surplusValue"));
                                                    goods.setCanyurenshu(object.optInt("tatalPrice") - object.optInt("surplusValue"));
                                                    goods.setYunjiage(object.optDouble("unitPrice"));
                                                    goods.setQ_uid(object.optString("uuid"));
                                                    goods.setState_desc(object.optString("substate"));

                                                    goods.setQ_end_time(object.optLong("accomplishTime") + (3 * 60 * 1000));
                                                    goods.setQ_content(object.optString("luckCode"));
                                                    goods.setIf_lock(object.optBoolean("is_buy"));
                                                    goods.setQishu(object.optInt("periodsNum"));
                                                    goods.setGoods_salenum(object.optString("buyNum"));
                                                    goods.setQ_counttime(object.optString("surplusTime"));
                                                    goods.setQ_showtime(object.optLong("surplusTime") + System.currentTimeMillis());
                                                    goods.setQ_user_code(object.optString("isPrizeWinner"));
                                                    goodsList.add(goods);
                                                    if (goodsList.size() == 10) {
                                                        isMore = true;
                                                    } else {
                                                        isMore = false;
                                                    }
                                                    lotteryFragmentAdapter.addItems(goodsList);
                                                    lotteryFragmentAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        } else {
                                            isMore = false;
                                        }
                                        mHasLoadedOnce = true;
                                        Log.i("zjz", "yyg_lottery_isMore=" + isMore);
//                                        lotteryFragmentAdapter.notifyDataSetChanged();
                                    }
                                } catch (JSONException e) {
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
    protected void lazyLoad() {
        Log.i("zjz", "isprepare=" + isPrepared + "isvisible=" + isVisible + "hasonce=" + mHasLoadedOnce);
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            Log.i("zjz", "return掉了");
            return;
        }

        initRecycler();
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
    }

    private MyHandler handler = new MyHandler();
    private int mCurrentCounter = 0;

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Goods> list) {

        lotteryFragmentAdapter.addItems(list);
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
//                RecyclerViewStateUtils.setFooterViewState(getActivity(), recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }

    };


    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    isLoadMore = true;
                    Log.i("zjz", "totalPage=" + totalPage);
                    if (isMore&& isLoadMore) {
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
}
