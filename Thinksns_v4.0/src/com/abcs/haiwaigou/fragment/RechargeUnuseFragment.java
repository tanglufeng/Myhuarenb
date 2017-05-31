package com.abcs.haiwaigou.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.fragment.adapter.MyRechargeAdapter;
import com.abcs.haiwaigou.model.Voucher;
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
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zjz on 2016/4/27.
 */
public class RechargeUnuseFragment extends Fragment implements RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh {

    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.img_null)
    ImageView imgNull;
    @InjectView(R.id.tv_null)
    TextView tvNull;
    @InjectView(R.id.layout_null)
    RelativeLayout layoutNull;
    private Activity activity;
    private View view;
    private RequestQueue mRequestQueue;
    int totalPage;
    int currentPage = 1;
    boolean isLoadMore = false;
    boolean first = false;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    MyRechargeAdapter myRechargeAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        view = activity.getLayoutInflater().inflate(R.layout.hwg_fragment_comment_item, null);
        mRequestQueue = Volley.newRequestQueue(activity);
        ButterKnife.inject(this, view);
        initRecycler();
    }

    private void initRecycler() {
        recyclerView.addOnScrollListener(mOnScrollListener);
        initAllDates();
        myRechargeAdapter = new MyRechargeAdapter(activity);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(myRechargeAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(activity, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
    }

    private void initAllDates() {
        final ArrayList<Voucher> dataList = new ArrayList<>();
        if (!first) {
            ProgressDlgUtil.showProgressDlg("Loading...", activity);
        }
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_my_voucher, "&key=" + MyApplication.getInstance().getMykey() + "&voucher_state=1&curpage=" + currentPage,
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
                                                Log.i("zjz", "good_voucher:连接成功");
                                                Log.i("zjz", "response=" + response);
                                                totalPage = response.getInt("page_total");
                                                if (response.getInt("page_total") != 0) {
                                                    JSONObject object = response.getJSONObject("datas");
                                                    JSONArray array = object.getJSONArray("voucher_list");
                                                    if (isLoadMore) {
                                                        int currentSize = myRechargeAdapter.getItemCount();

                                                        for (int i = 0; i < array.length(); i++) {
                                                            JSONObject voucherObject = array.getJSONObject(i);
                                                            Voucher voucher = new Voucher();
                                                            voucher.setId(currentSize + i);
                                                            voucher.setVoucher_t_id(voucherObject.optString("voucher_id"));
                                                            voucher.setVoucher_code(voucherObject.optString("voucher_code"));
                                                            voucher.setVoucher_t_title(voucherObject.optString("voucher_title"));
                                                            voucher.setVoucher_t_desc(voucherObject.optString("voucher_desc"));
                                                            voucher.setVoucher_t_start_date(voucherObject.optLong("voucher_start_date"));
                                                            voucher.setVoucher_t_end_date(voucherObject.optLong("voucher_end_date"));
                                                            voucher.setVoucher_t_limit(voucherObject.optInt("voucher_limit"));
                                                            voucher.setVoucher_t_price(voucherObject.optString("voucher_price"));
                                                            voucher.setVoucher_t_state(voucherObject.getString("voucher_state"));
                                                            voucher.setVoucher_state_text(voucherObject.getString("voucher_state_text"));

                                                            dataList.add(voucher);
                                                        }
                                                        addItems(dataList);

                                                    } else {
                                                        myRechargeAdapter.getList().clear();
                                                        dataList.clear();
                                                        for (int i = 0; i < array.length(); i++) {
                                                            JSONObject voucherObject = array.getJSONObject(i);
                                                            Voucher voucher = new Voucher();
                                                            voucher.setId(i);
                                                            voucher.setVoucher_t_id(voucherObject.optString("voucher_id"));
                                                            voucher.setVoucher_code(voucherObject.optString("voucher_code"));
                                                            voucher.setVoucher_t_title(voucherObject.optString("voucher_title"));
                                                            voucher.setVoucher_t_desc(voucherObject.optString("voucher_desc"));
                                                            voucher.setVoucher_t_start_date(voucherObject.optLong("voucher_start_date"));
                                                            voucher.setVoucher_t_end_date(voucherObject.optLong("voucher_end_date"));
                                                            voucher.setVoucher_t_limit(voucherObject.optInt("voucher_limit"));
                                                            voucher.setVoucher_t_price(voucherObject.optString("voucher_price"));
                                                            voucher.setVoucher_t_state(voucherObject.getString("voucher_state"));
                                                            voucher.setVoucher_state_text(voucherObject.getString("voucher_state_text"));

                                                            dataList.add(voucher);
                                                        }
                                                        mCurrentCounter = dataList.size();
                                                        myRechargeAdapter.addItems(dataList);
                                                        myRechargeAdapter.notifyDataSetChanged();
                                                    }
                                                }
                                                if(layoutNull!=null)
                                                    layoutNull.setVisibility(myRechargeAdapter.getList().size()==0?View.VISIBLE:View.GONE);
                                                recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                                            } else {
                                                Log.i("zjz", "goodsActivity解析失败");
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
                                        }
                                    }
                                }
                        );

                    }
                }

        );
    }

    private MyHandler handler = new MyHandler();
    private int mCurrentCounter = 0;

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Voucher> list) {

        myRechargeAdapter.addItems(list);
        mCurrentCounter += list.size();
    }

    private static final int REQUEST_COUNT = 10;
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
                RecyclerViewStateUtils.setFooterViewState(activity, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(activity, recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }

    };

    @Override
    public void swipeRefreshLayoutOnRefresh() {
        first = true;
        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(true);
        isLoadMore = false;
        currentPage = 1;
        initAllDates();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
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
            RecyclerViewStateUtils.setFooterViewState(activity, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                if (NetworkUtils.isNetAvailable(activity)) {
//                    mHandler.sendEmptyMessage(-1);
                    handler.sendEmptyMessage(-1);
                } else {
                    handler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeAllViewsInLayout();
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
