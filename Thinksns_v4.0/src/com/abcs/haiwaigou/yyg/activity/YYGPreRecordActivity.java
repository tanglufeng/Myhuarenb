package com.abcs.haiwaigou.yyg.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.utils.RecyclerViewAndSwipeRefreshLayout;
import com.abcs.haiwaigou.view.recyclerview.EndlessRecyclerOnScrollListener;
import com.abcs.haiwaigou.view.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.abcs.haiwaigou.view.recyclerview.LoadingFooter;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.haiwaigou.view.recyclerview.RecyclerViewStateUtils;
import com.abcs.haiwaigou.yyg.adapter.YYGPreRecordAdapter;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class YYGPreRecordActivity extends BaseActivity implements RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh{

    @InjectView(R.id.seperate)
    View seperate;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.layout_null)
    RelativeLayout layoutNull;

    int totalPage;
    int currentPage = 1;
    boolean isLoadMore = false;
    boolean first = false;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    private Handler handler=new Handler();
    YYGPreRecordAdapter yygPreRecordAdapter;
    View view;
    ArrayList<Goods> goodsList = new ArrayList<Goods>();
    boolean isMore=true;
    String act_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(view==null){
            view=getLayoutInflater().inflate(R.layout.hwg_yyg_activity_yygpre_record,null);
        }
        setContentView(view);
        ButterKnife.inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            seperate.setVisibility(View.VISIBLE);
        }
        act_id=getIntent().getStringExtra("act_id");
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
        yygPreRecordAdapter = new YYGPreRecordAdapter(this);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(yygPreRecordAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(this, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
    }

    private void initAllDates() {
        if (!first) {
            ProgressDlgUtil.showProgressDlg("Loading...", this);
        }
        Log.i("zjz", "currentPage=" + currentPage);
        HttpRequest.sendGet(TLUrl.getInstance().URL_yyg_goods_prerecord, "activityId=" + act_id + "&page=" + currentPage + "&pageSize=10",
                new HttpRevMsg() {
                    @Override
                    public void revMsg(final String msg) {
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                Log.i("zjz", "yyg_my_prerecord_record_msg=" + msg);
                                try {
                                    JSONObject result = new JSONObject(msg);
                                    if (result.optInt("status") == 1) {
                                        JSONArray jsonArray = result.optJSONArray("msg");
                                        if (jsonArray != null) {
                                            if (isLoadMore) {
                                                int currentSize = yygPreRecordAdapter.getItemCount();
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    Goods goods = new Goods();
                                                    goods.setId(currentSize + i);
                                                    JSONObject object = jsonArray.getJSONObject(i);
                                                    goods.setQ_end_time(object.optLong("accomplishTime"));
                                                    goods.setQ_user_code(object.optString("luckCode"));
                                                    JSONObject userObj=object.optJSONObject("user");
                                                    if(userObj!=null){
                                                        goods.setQ_user(userObj.optString("nickname"));
                                                    }
                                                    JSONObject goodsObj = object.optJSONObject("goods");
                                                    if(goodsObj!=null){
                                                        goods.setTitle(goodsObj.optString("name"));
                                                        goods.setTotal_money(goodsObj.optString("price"));
                                                        goods.setPicarr(goodsObj.optString("picture"));
                                                    }
                                                    goods.setQ_uid(object.optString("uuid"));
                                                    goods.setGoods_id(object.optString("goodsId"));
                                                    goods.setGoods_salenum(object.optString("buyNum"));
                                                    goods.setQishu(object.optInt("periodsNum"));

                                                    goodsList.add(goods);
                                                    if (goodsList.size() == 10) {
                                                        isMore = true;
                                                    } else {
                                                        isMore = false;
                                                    }
                                                    addItems(goodsList);
                                                }
                                            } else {
                                                yygPreRecordAdapter.getList().clear();
                                                goodsList.clear();
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    Goods goods = new Goods();
                                                    goods.setId(i);
                                                    JSONObject object = jsonArray.getJSONObject(i);
                                                    goods.setQ_end_time(object.optLong("accomplishTime"));
                                                    goods.setQ_user_code(object.optString("luckCode"));
                                                    JSONObject userObj=object.optJSONObject("user");
                                                    if(userObj!=null){
                                                        goods.setQ_user(userObj.optString("nickname"));
                                                    }
                                                    JSONObject goodsObj = object.optJSONObject("goods");
                                                    if(goodsObj!=null){
                                                        goods.setTitle(goodsObj.optString("name"));
                                                        goods.setTotal_money(goodsObj.optString("price"));
                                                        goods.setPicarr(goodsObj.optString("picture"));
                                                    }
                                                    goods.setQ_uid(object.optString("uuid"));
                                                    goods.setGoods_id(object.optString("goodsId"));
                                                    goods.setGoods_salenum(object.optString("buyNum"));
                                                    goods.setQishu(object.optInt("periodsNum"));
                                                    goodsList.add(goods);
                                                    if (goodsList.size() == 10) {
                                                        isMore = true;
                                                    } else {
                                                        isMore = false;
                                                    }
                                                    yygPreRecordAdapter.addItems(goodsList);
                                                    yygPreRecordAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        } else {
                                            isMore = false;
                                        }
                                        if (layoutNull != null)
                                            layoutNull.setVisibility(yygPreRecordAdapter.getList().size() == 0 ? View.VISIBLE : View.GONE);

                                        Log.i("zjz", "yyg_prerecord_isMore=" + isMore);
//                                        yygMyBuyRecordAdapter.notifyDataSetChanged();
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
    public void swipeRefreshLayoutOnRefresh() {
        first = true;
        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(true);
        isLoadMore = false;
        currentPage = 1;
        initAllDates();
    }


    private PreviewHandler mHandler = new PreviewHandler(this);
    private int mCurrentCounter = 0;

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Goods> list) {

        yygPreRecordAdapter.addItems(list);
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
                RecyclerViewStateUtils.setFooterViewState(YYGPreRecordActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(YYGPreRecordActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }

    };



    private class PreviewHandler extends Handler {

        private WeakReference<YYGPreRecordActivity> ref;

        PreviewHandler(YYGPreRecordActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final YYGPreRecordActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            switch (msg.what) {
                case -1:
                    isLoadMore = true;
                    Log.i("zjz", "totalPage=" + totalPage);
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
            RecyclerViewStateUtils.setFooterViewState(YYGPreRecordActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                if (NetworkUtils.isNetAvailable(YYGPreRecordActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

}
