package com.abcs.haiwaigou.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.fragment.adapter.CommentGoodFragmentAdapter;
import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager;
import com.abcs.haiwaigou.fragment.viewholder.CommentItemFragmentViewHolder;
import com.abcs.haiwaigou.model.Comment;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.utils.RecyclerViewAndSwipeRefreshLayout;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.haiwaigou.view.recyclerview.EndlessRecyclerOnScrollListener;
import com.abcs.haiwaigou.view.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.abcs.haiwaigou.view.recyclerview.LoadingFooter;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.haiwaigou.view.recyclerview.RecyclerViewStateUtils;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
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

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/23 0023.
 */
public class CommetAllFragment extends BaseFragment implements RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh,CommentItemFragmentViewHolder.ItemOnClick{
    private static final String FRAGMENT_INDEX = "fragment_index";
    private final int FIRST_FRAGMENT = 0;
    private final int SECOND_FRAGMENT = 1;
    private final int THIRD_FRAGMENT = 2;
    public static final String ALL="all";
    public static final String GOOD="1";
    public static final String MIDDLE="2";
    public static final String BAD="3";
    public static final String IMAGE="4";
    private View view;
    Activity activity;
    private RequestQueue mRequestQueue;
    String type;
    String goods_id;
    ArrayList<Goods> goodsList = new ArrayList<Goods>();
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView imgNull;
    TextView tvNull;
    RelativeLayout layoutNull;
    int totalPage;
    int currentPage = 1;
    boolean isLoadMore = false;
    boolean first = false;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    //    CommentItemFragmentAdapter commentItemFragmentAdapter;
    CommentGoodFragmentAdapter commentGoodFragmentAdapter;

    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;

    public static CommetAllFragment newInstance(String str,String goodsId) {
        Bundle bundle = new Bundle();
        bundle.putString("type", str);
        bundle.putString("goods_id",goodsId);
        CommetAllFragment fragment = new CommetAllFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        activity=getActivity();
        mRequestQueue = Volley.newRequestQueue(activity);
        if(view==null){
            view =  activity.getLayoutInflater().inflate(
                    R.layout.hwg_fragment_comment_item, null);
            Bundle bundle = getArguments();
            if (bundle != null) {
                goods_id=bundle.getString("goods_id");
                type=bundle.getString("type");
            }
            isPrepared = true;
            lazyLoad();
            initView();
        }
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);
//            p.removeAllViewsInLayout();
//        goods_id= (String) getArguments().getSerializable("goods_id");
//        initView();
//        initRecycler();

        return view;
//        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
//        ViewGroup parent = (ViewGroup)mFragmentView.getParent();
//        if(parent != null) {
//            parent.removeView(mFragmentView);
//        }
//        return mFragmentView;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }

        initRecycler();

    }


    private void initView() {
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(activity));
        layoutNull= (RelativeLayout) view.findViewById(R.id.layout_null);
    }

    public void initRecycler() {
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutNull= (RelativeLayout) view.findViewById(R.id.layout_null);
        recyclerView.addOnScrollListener(mOnScrollListener);
        initAllDates();
        commentGoodFragmentAdapter = new CommentGoodFragmentAdapter(activity,this);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(commentGoodFragmentAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(activity, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
    }

    private void initAllDates() {
        Log.i("zjz", "goods_id=" + goods_id);
        Log.i("zjz", "type=" + type);
        if(!first){
            ProgressDlgUtil.showProgressDlg("Loading...", activity);
        }
        final ArrayList<Comment> dataList = new ArrayList<>();
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, TLUrl.getInstance().URL_hwg_goods_comment2 + "&goods_id=" + goods_id+ "&type="+type+"&curpage="+currentPage, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "good_comment:连接成功");
                        totalPage = response.getInt("page_total");
                        Log.i("zjz", "totalpage=" + totalPage);
                        Log.i("zjz", "response=" + response);
                        if(response.getInt("page_total")!=0){
                            JSONObject object = response.getJSONObject("datas");
                            JSONArray groupArray = object.getJSONArray("goodsevallist");
                            if (isLoadMore) {
                                int currentSize = commentGoodFragmentAdapter.getItemCount();
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
                                commentGoodFragmentAdapter.getList().clear();
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
                                commentGoodFragmentAdapter.addItems(dataList);
                                commentGoodFragmentAdapter.notifyDataSetChanged();
                            }
                        }
                        if(layoutNull!=null)
                            layoutNull.setVisibility(commentGoodFragmentAdapter.getList().size()==0?View.VISIBLE:View.GONE);
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                        mHasLoadedOnce=true;
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
                }finally {
                    ProgressDlgUtil.stopProgressDlg();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mRequestQueue.add(jr);
    }

    private MyHandler handler=new MyHandler();
    private int mCurrentCounter = 0;

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Comment> list) {

        commentGoodFragmentAdapter.addItems(list);
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
    public void onItemRootViewClick(int position) {

    }

    @Override
    public void onImgClick(int position) {

    }

    @Override
    public void swipeRefreshLayoutOnRefresh() {
        first=true;
        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(true);
        isLoadMore = false;
        currentPage = 1;
        initAllDates();
    }


    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
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

}
