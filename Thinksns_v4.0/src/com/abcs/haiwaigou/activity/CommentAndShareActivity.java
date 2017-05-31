package com.abcs.haiwaigou.activity;

import android.content.ComponentName;
import android.content.Intent;
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

import com.abcs.haiwaigou.adapter.CommentRecyclerAdapter;
import com.abcs.haiwaigou.adapter.viewholder.CommentRecyclerViewHolder;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.Comment;
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
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CommentAndShareActivity extends BaseActivity implements View.OnClickListener, RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh, CommentRecyclerViewHolder.ItemOnClick {

    @InjectView(R.id.tljr_txt_news_title)
    TextView tljrTxtNewsTitle;
    @InjectView(R.id.tljr_hqss_news_titlebelow)
    TextView tljrHqssNewsTitlebelow;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.tljr_grp_goods_title)
    RelativeLayout tljrGrpGoodsTitle;
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


    MyBroadCastReceiver myBroadCastReceiver;
    @InjectView(R.id.relative_network)
    RelativeLayout relativeNetwork;
    private View view;
    int totalPage;
    int currentPage = 1;
    boolean isLoadMore = false;
    boolean first = false;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    CommentRecyclerAdapter commentAdapter;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_activity_comment_and_share, null);
        }
        setContentView(view);
        myBroadCastReceiver = new MyBroadCastReceiver(this, updateUI);
        myBroadCastReceiver.register();
        mRequestQueue = Volley.newRequestQueue(this);
        ButterKnife.inject(this);

        initRecyclerView();
        setOnListener();
    }

    private void initRecyclerView() {
        recyclerView.addOnScrollListener(mOnScrollListener);
        if (!NetworkUtils.isNetAvailable(this)) {
            if (relativeNetwork != null) {
                relativeNetwork.setVisibility(View.VISIBLE);
            }
        }else {
            initAllDates();
        }
        commentAdapter = new CommentRecyclerAdapter(this, this);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(commentAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(this, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
    }

    private void initAllDates() {
        if (!first) {
            ProgressDlgUtil.showProgressDlg("Loading...", this);
        }
        final ArrayList<Comment> dataList = new ArrayList<>();
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_comment_list + "&curpage=" + currentPage + "&key=" + MyApplication.getInstance().getMykey(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "comment:连接成功");
                        totalPage = response.getInt("page_total");
                        Log.i("zjz", "totalpage=" + totalPage);
                        Log.i("zjz", "response=" + response);
                        JSONObject object = response.getJSONObject("datas");
                        JSONArray groupArray = object.getJSONArray("goodsevallist");
                        if (isLoadMore) {
                            int currentSize = commentAdapter.getItemCount();
                            for (int i = 0; i < groupArray.length(); i++) {
                                Comment orders = new Comment();
                                orders.setId(currentSize + i);
                                JSONObject object1 = groupArray.getJSONObject(i);
                                orders.setGeval_id(object1.optString("geval_id"));
                                orders.setGeval_orderid(object1.optString("geval_orderid"));
                                orders.setGeval_orderno(object1.optString("geval_orderno"));
                                orders.setGeval_ordergoodsid(object1.optString("geval_ordergoodsid"));
                                orders.setGeval_goodsid(object1.optString("geval_goodsid"));
                                orders.setGeval_goodsname(object1.optString("geval_goodsname"));
                                orders.setGeval_goodsprice(object1.optDouble("geval_goodsprice"));
                                orders.setGeval_goodsimage(object1.optString("geval_goodsimage"));
                                orders.setGeval_scores(object1.optInt("geval_scores"));
                                orders.setGeval_content(object1.optString("geval_content"));
                                orders.setGeval_isanonymous(object1.optString("geval_isanonymous").equals("0") ? false : true);
                                orders.setGeval_addtime(object1.optLong("geval_addtime"));
                                orders.setGeval_storeid(object1.optString("geval_storeid"));
                                orders.setGeval_storename(object1.optString("geval_storename"));
                                orders.setGeval_frommemberid(object1.optString("geval_frommemberid"));
                                orders.setGeval_frommembername(object1.optString("geval_frommembername"));
                                orders.setGeval_image(object1.optString("geval_image"));
                                orders.setGeval_explain(object1.optString("geval_explain"));

                                dataList.add(orders);
                            }
                            addItems(dataList);

                        } else {
                            dataList.clear();
                            for (int i = 0; i < groupArray.length(); i++) {
                                Comment orders = new Comment();
                                orders.setId(i);
                                JSONObject object1 = groupArray.getJSONObject(i);
                                orders.setGeval_id(object1.optString("geval_id"));
                                orders.setGeval_orderid(object1.optString("geval_orderid"));
                                orders.setGeval_orderno(object1.optString("geval_orderno"));
                                orders.setGeval_ordergoodsid(object1.optString("geval_ordergoodsid"));
                                orders.setGeval_goodsid(object1.optString("geval_goodsid"));
                                orders.setGeval_goodsname(object1.optString("geval_goodsname"));
                                orders.setGeval_goodsprice(object1.optDouble("geval_goodsprice"));
                                orders.setGeval_scores(object1.optInt("geval_scores"));
                                orders.setGeval_content(object1.optString("geval_content"));
                                orders.setGeval_goodsimage(object1.optString("geval_goodsimage"));
                                orders.setGeval_isanonymous(object1.optString("geval_isanonymous").equals("0") ? false : true);
                                orders.setGeval_addtime(object1.optLong("geval_addtime"));
                                orders.setGeval_storeid(object1.optString("geval_storeid"));
                                orders.setGeval_storename(object1.optString("geval_storename"));
                                orders.setGeval_frommemberid(object1.optString("geval_frommemberid"));
                                orders.setGeval_frommembername(object1.optString("geval_frommembername"));
                                orders.setGeval_image(object1.optString("geval_image"));
                                orders.setGeval_explain(object1.optString("geval_explain"));
                                dataList.add(orders);
                            }
                            mCurrentCounter = dataList.size();
                            commentAdapter.addItems(dataList);
                            commentAdapter.notifyDataSetChanged();
                        }

                        if (commentAdapter.getList().size() == 0) {
                            layoutNull.setVisibility(View.VISIBLE);
                        } else {
                            layoutNull.setVisibility(View.GONE);
                        }
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                        ProgressDlgUtil.stopProgressDlg();

                    } else {
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                        Log.i("zjz", "goodsActivity解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
            }
        });
        mRequestQueue.add(jr);
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
            if (intent.getStringExtra("type").equals(MyUpdateUI.COMMENT)) {
//                initListView();
                isLoadMore = false;
                currentPage = 1;
                initRecyclerView();
                Log.i("zjz", "更新评论");
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };


    private void setOnListener() {
        relativeBack.setOnClickListener(this);
        relativeNetwork.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_back:
                finish();
                break;
            case R.id.relative_network:
                Intent intent = new Intent("/");
                ComponentName cm = new ComponentName("com.android.settings",
                        "com.android.settings.Settings");
                intent.setComponent(cm);
                intent.setAction("android.intent.action.VIEW");
                startActivity(intent);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        myBroadCastReceiver.unRegister();
        ButterKnife.reset(this);
        super.onDestroy();
    }


    private PreviewHandler mHandler = new PreviewHandler(this);
    private int mCurrentCounter = 0;

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Comment> list) {

        commentAdapter.addItems(list);
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

            if (currentPage < totalPage) {
                // loading more
                RecyclerViewStateUtils.setFooterViewState(CommentAndShareActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(CommentAndShareActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
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

    @Override
    public void onItemRootViewClick(int position) {
        Intent intent = new Intent(this, GoodsDetailActivity.class);
        intent.putExtra("sid", commentAdapter.getList().get(position).getGeval_goodsid());
        intent.putExtra("pic", TLUrl.getInstance().URL_hwg_comment_goods + commentAdapter.getList().get(position).getGeval_storeid() + "/" + commentAdapter.getList().get(position).getGeval_goodsimage());
        startActivity(intent);
    }

    @Override
    public void onBtnShare(int position) {
        Intent intent = new Intent(this, SharePicActivity.class);
        intent.putExtra("geval_id", commentAdapter.getList().get(position).getGeval_id());
        intent.putExtra("geval_goodsname", commentAdapter.getList().get(position).getGeval_goodsname());
        intent.putExtra("geval_goodsimg", TLUrl.getInstance().URL_hwg_comment_goods + commentAdapter.getList().get(position).getGeval_storeid() + "/" + commentAdapter.getList().get(position).getGeval_goodsimage());
        intent.putExtra("geval_scores", commentAdapter.getList().get(position).getGeval_scores());
        intent.putExtra("geval_comment", commentAdapter.getList().get(position).getGeval_content());
        startActivity(intent);
//        showToast("跳转到晒单界面");
    }

    private class PreviewHandler extends Handler {

        private WeakReference<CommentAndShareActivity> ref;

        PreviewHandler(CommentAndShareActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final CommentAndShareActivity activity = ref.get();
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
            RecyclerViewStateUtils.setFooterViewState(CommentAndShareActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                if (NetworkUtils.isNetAvailable(CommentAndShareActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

}
