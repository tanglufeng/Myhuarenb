package com.abcs.haiwaigou.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.abcs.haiwaigou.adapter.GoodsNewsAdapter;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager;
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
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.fragment.MainFragment2;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GoodsNewsActivity extends BaseActivity implements View.OnClickListener, RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.relative_title)
    RelativeLayout relativeTitle;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    GoodsNewsAdapter goodsNewsAdapter;
    FullyLinearLayoutManager fullyLinearLayoutManager;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    @InjectView(R.id.relative_null)
    RelativeLayout relativeNull;
    @InjectView(R.id.relative_clean)
    RelativeLayout relativeClean;
    @InjectView(R.id.relative_tishi)
    RelativeLayout relativeTishi;
    private View view;
    private Handler handler = new Handler();
    private ArrayList<Goods> goodList = new ArrayList<>();
    int currentPage = 1;
    boolean isLoadMore = false;
    boolean first = false;
    boolean isMore = true;

    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;

    MyBroadCastReceiver myBroadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_activity_goods_news, null);
        }
        setContentView(view);
        myBroadCastReceiver = new MyBroadCastReceiver(this, updateUI);
        myBroadCastReceiver.register();
        ButterKnife.inject(this);
        initRecyclerView();
        relativeClean.setOnClickListener(this);
        relativeBack.setOnClickListener(this);
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
            if (intent.getStringExtra("type").equals(MyUpdateUI.NEWS)) {
                initRecyclerView();
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initRecyclerView() {
        recyclerView.addOnScrollListener(mOnScrollListener);
        goodsNewsAdapter = new GoodsNewsAdapter(this);
        fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
        recyclerView.setFocusable(false);
        initAllDates();

        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(goodsNewsAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(this, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE1);
        //添加分割线
//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_size4);
//        weekRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

    }

    private void initAllDates() {
        if (!first) {
            ProgressDlgUtil.showProgressDlg("Loading...", this);
        }
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_goods_message, "member_name=" + MyApplication.getInstance().self.getUserName() + "&page=" + currentPage + "&size=10",
                new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {
                        Log.i("zjz", "message_msg=" + msg);
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                ProgressDlgUtil.stopProgressDlg();
                                if (msg.length() == 0) {
                                    showToast("请求失败,请检查网络重试");
                                    return;
                                }
//                                goodsNewsAdapter.getDatas().clear();
                                JSONObject result = JSONObject.parseObject(msg);
                                if (result.getInteger("status") == 1) {
                                    JSONArray jsonArray = result.getJSONArray("msg");
                                    if (jsonArray != null) {
                                        if (isLoadMore) {
                                            int currentSize = goodsNewsAdapter.getItemCount();
                                            for (int i = 0; i < jsonArray.size(); i++) {
                                                Goods goods = new Goods();
                                                goods.setId(currentSize + i);
                                                JSONObject object = jsonArray.getJSONObject(i);
                                                String data = object.getString("data");
                                                JSONObject idObject = object.getJSONObject("_id");
                                                goods.setCid(idObject.getString("$oid"));
                                                Log.i("zjz", "data=" + data);
                                                JSONObject temp = JSONObject.parseObject(data);
                                                goods.setTime(object.getLong("time"));
                                                goods.setHint(object.getString("type"));
                                                if (object.getString("type").equals("0")) {
                                                    JSONArray goodsArray = JSONArray.parseArray(temp.getString("order_goods"));
                                                    Log.i("zjz", "goodsArray=" + goodsArray);
                                                    JSONObject goodsObject = goodsArray.getJSONObject(0);
                                                    goods.setGoods_id(goodsObject.getString("goods_id"));
                                                    goods.setTitle(goodsObject.getString("goods_name"));
                                                    goods.setPay_amount(goodsObject.getString("goods_pay_price"));
                                                    goods.setOrder_id(goodsObject.getString("order_id"));
                                                    goods.setGoods_num(goodsObject.getInteger("goods_num"));
                                                    goods.setOrder_sn(temp.getString("order_sn"));
                                                    goods.setPicarr(TLUrl.getInstance().URL_hwg_comment_goods + goodsObject.getString("store_id") + "/" + goodsObject.getString("goods_image"));

                                                } else if (object.getString("type").equals("1")) {
                                                    JSONArray deliverArray = JSONArray.parseArray(temp.getString("e_name"));
                                                    if (deliverArray.size() != 0) {
                                                        JSONObject deliverObject = deliverArray.getJSONObject(0);
                                                        goods.setExpress_name(deliverObject.getString("e_name"));
                                                    }
                                                    goods.setDeliver_code(temp.getString("shipping_code"));
                                                    goods.setOrder_sn(temp.getString("order_sn"));
                                                    JSONArray goodsArray = JSONArray.parseArray(temp.getString("order_goods"));
                                                    Log.i("zjz", "goodsArray=" + goodsArray);
                                                    JSONObject goodsObject = goodsArray.getJSONObject(0);
                                                    goods.setGoods_id(goodsObject.getString("goods_id"));
                                                    goods.setTitle(goodsObject.getString("goods_name"));
                                                    goods.setPay_amount(goodsObject.getString("goods_pay_price"));
                                                    goods.setOrder_id(goodsObject.getString("order_id"));
                                                    goods.setGoods_num(goodsObject.getInteger("goods_num"));
                                                    goods.setPicarr(TLUrl.getInstance().URL_hwg_comment_goods + goodsObject.getString("store_id") + "/" + goodsObject.getString("goods_image"));

                                                } else if (object.getString("type").equals("2")) {
                                                    JSONArray goodsArray = JSONArray.parseArray(temp.getString("order_goods"));
                                                    JSONObject orderObject = JSONObject.parseObject(temp.getString("order_info"));
                                                    goods.setOrder_sn(orderObject.getString("order_sn"));
                                                    Log.i("zjz", "goodsArray=" + goodsArray);
                                                    JSONObject goodsObject = goodsArray.getJSONObject(0);
                                                    goods.setGoods_id(goodsObject.getString("goods_id"));
                                                    goods.setTitle(goodsObject.getString("goods_name"));
                                                    goods.setPay_amount(goodsObject.getString("goods_pay_price"));
                                                    goods.setOrder_id(goodsObject.getString("order_id"));
                                                    goods.setGoods_num(goodsObject.getInteger("goods_num"));
                                                    goods.setPicarr(TLUrl.getInstance().URL_hwg_comment_goods + goodsObject.getString("store_id") + "/" + goodsObject.getString("goods_image"));
                                                } else if (object.getString("type").equals("11")) {
                                                    goods.setTitle(temp.getString("stringinfo"));
                                                }else if(object.getString("type").equals("3")){
                                                    goods.setTitle(temp.getString("stringinfo"));
                                                }
                                                goodList.add(goods);
                                                if (goodList.size() == 10) {
                                                    isMore = true;
                                                } else {
                                                    isMore = false;
                                                }
                                                addItems(goodList);
//                                                goodsNewsAdapter.getDatas().add(goods);
                                            }
                                        } else {
                                            goodsNewsAdapter.getList().clear();
                                            goodList.clear();
                                            for (int i = 0; i < jsonArray.size(); i++) {
                                                Goods goods = new Goods();
                                                goods.setId(i);
                                                JSONObject object = jsonArray.getJSONObject(i);
                                                String data = object.getString("data");
                                                JSONObject idObject = object.getJSONObject("_id");
                                                goods.setCid(idObject.getString("$oid"));
                                                Log.i("zjz", "data=" + data);
                                                JSONObject temp = JSONObject.parseObject(data);
                                                goods.setTime(object.getLong("time"));
                                                goods.setHint(object.getString("type"));
                                                if (object.getString("type").equals("0")) {
                                                    JSONArray goodsArray = JSONArray.parseArray(temp.getString("order_goods"));
                                                    Log.i("zjz", "goodsArray=" + goodsArray);
                                                    JSONObject goodsObject = goodsArray.getJSONObject(0);
                                                    goods.setGoods_id(goodsObject.getString("goods_id"));
                                                    goods.setTitle(goodsObject.getString("goods_name"));
                                                    goods.setPay_amount(goodsObject.getString("goods_pay_price"));
                                                    goods.setOrder_id(goodsObject.getString("order_id"));
                                                    goods.setGoods_num(goodsObject.getInteger("goods_num"));
                                                    goods.setOrder_sn(temp.getString("order_sn"));
                                                    goods.setPicarr(TLUrl.getInstance().URL_hwg_comment_goods + goodsObject.getString("store_id") + "/" + goodsObject.getString("goods_image"));

                                                } else if (object.getString("type").equals("1")) {
                                                    JSONArray deliverArray = JSONArray.parseArray(temp.getString("e_name"));
                                                    if (deliverArray.size() != 0) {
                                                        JSONObject deliverObject = deliverArray.getJSONObject(0);
                                                        goods.setExpress_name(deliverObject.getString("e_name"));
                                                    }
                                                    goods.setDeliver_code(temp.getString("shipping_code"));
                                                    goods.setOrder_sn(temp.getString("order_sn"));
                                                    JSONArray goodsArray = JSONArray.parseArray(temp.getString("order_goods"));
                                                    Log.i("zjz", "goodsArray=" + goodsArray);
                                                    JSONObject goodsObject = goodsArray.getJSONObject(0);
                                                    goods.setGoods_id(goodsObject.getString("goods_id"));
                                                    goods.setTitle(goodsObject.getString("goods_name"));
                                                    goods.setPay_amount(goodsObject.getString("goods_pay_price"));
                                                    goods.setOrder_id(goodsObject.getString("order_id"));
                                                    goods.setGoods_num(goodsObject.getInteger("goods_num"));
                                                    goods.setPicarr(TLUrl.getInstance().URL_hwg_comment_goods + goodsObject.getString("store_id") + "/" + goodsObject.getString("goods_image"));

                                                } else if (object.getString("type").equals("2")) {
                                                    JSONArray goodsArray = JSONArray.parseArray(temp.getString("order_goods"));
                                                    Log.i("zjz", "goodsArray=" + goodsArray);
                                                    JSONObject orderObject = JSONObject.parseObject(temp.getString("order_info"));
                                                    goods.setOrder_sn(orderObject.getString("order_sn"));
                                                    JSONObject goodsObject = goodsArray.getJSONObject(0);
                                                    goods.setGoods_id(goodsObject.getString("goods_id"));
                                                    goods.setTitle(goodsObject.getString("goods_name"));
                                                    goods.setPay_amount(goodsObject.getString("goods_pay_price"));
                                                    goods.setOrder_id(goodsObject.getString("order_id"));
                                                    goods.setGoods_num(goodsObject.getInteger("goods_num"));
                                                    goods.setPicarr(TLUrl.getInstance().URL_hwg_comment_goods + goodsObject.getString("store_id") + "/" + goodsObject.getString("goods_image"));
                                                } else if (object.getString("type").equals("11")) {
                                                    goods.setTitle(temp.getString("stringinfo"));
                                                }else if(object.getString("type").equals("3")){
                                                    goods.setTitle(temp.getString("stringinfo"));
                                                }
                                                goodList.add(goods);
                                                if (goodList.size() == 10) {
                                                    isMore = true;
                                                } else {
                                                    isMore = false;
                                                }
                                                goodsNewsAdapter.addItems(goodList);
                                                goodsNewsAdapter.notifyDataSetChanged();
//                                                goodsNewsAdapter.getDatas().add(goods);
                                            }
                                        }
                                    } else {
                                        isMore = false;
                                    }

                                    Log.i("zjz", "GoodsNews_isMore=" + isMore);
                                    if (relativeNull != null&&relativeTishi!=null&&relativeClean!=null)
                                        if (goodsNewsAdapter.getList().size() == 0) {
                                            relativeNull.setVisibility(View.VISIBLE);
                                            relativeTishi.setVisibility(View.GONE);
                                            relativeClean.setVisibility(View.GONE);
                                        }else {
                                            relativeTishi.setVisibility(View.VISIBLE);
                                            relativeClean.setVisibility(View.VISIBLE);
                                        }

                                    SharedPreferences.Editor editor = Util.preference.edit();
                                    editor.putString("goods_news", null);
                                    editor.commit();

                                    goodsNewsAdapter.notifyDataSetChanged();
                                }
                                MainFragment2.t_news.setVisibility(View.GONE);
                                ProgressDlgUtil.stopProgressDlg();
                                recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                            }
                        });
                    }
                });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.relative_clean:
                cleanAll();
                break;
        }
    }

    private void cleanAll() {
        new PromptDialog(this, "您确定清空所有消息?(此操作无法恢复)", new Complete() {
            @Override
            public void complete() {
                ProgressDlgUtil.showProgressDlg("Loading...", GoodsNewsActivity.this);
                Log.i("zjz", "url=" + TLUrl.getInstance().URL_hwg_goods_msg_del_one + "?" + "member_name=" + MyApplication.getInstance().self.getUserName());
                HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_goods_msg_del_all, "member_name=" + MyApplication.getInstance().self.getUserName(),
                        new HttpRevMsg() {
                            @Override
                            public void revMsg(final String msg) {
                                handler.post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Log.i("zjz", "del_msg=" + msg);
                                                    org.json.JSONObject object = new org.json.JSONObject(msg);
                                                    if (object.optInt("status") == 1) {
                                                        if (object.optString("msg").contains("成功")) {
                                                            Toast.makeText(GoodsNewsActivity.this, "清空所有信息成功！", Toast.LENGTH_SHORT).show();
                                                            initRecyclerView();
                                                        }
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
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
        }).show();
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

        goodsNewsAdapter.addItems(list);
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

            if (isMore) {
                // loading more
                RecyclerViewStateUtils.setFooterViewState(GoodsNewsActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(GoodsNewsActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }

    };


    private class PreviewHandler extends Handler {

        private WeakReference<GoodsNewsActivity> ref;

        PreviewHandler(GoodsNewsActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final GoodsNewsActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            switch (msg.what) {
                case -1:
                    isLoadMore = true;
                    if (isMore && isLoadMore) {
                        currentPage += 1;
                        first = false;
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
            RecyclerViewStateUtils.setFooterViewState(GoodsNewsActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                if (NetworkUtils.isNetAvailable(GoodsNewsActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myBroadCastReceiver.unRegister();
    }
}
