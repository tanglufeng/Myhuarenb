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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.adapter.CollectionAdapter;
import com.abcs.haiwaigou.adapter.viewholder.CollectionViewHolder;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.utils.RecyclerViewAndSwipeRefreshLayout;
import com.abcs.haiwaigou.view.recyclerview.EndlessRecyclerOnScrollListener;
import com.abcs.haiwaigou.view.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.abcs.haiwaigou.view.recyclerview.LoadingFooter;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.haiwaigou.view.recyclerview.RecyclerViewStateUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
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
import java.util.Iterator;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CollectionActivity extends BaseActivity implements View.OnClickListener, RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh, CollectionViewHolder.ItemRootOnclick {

    @InjectView(R.id.tljr_txt_news_title)
    TextView tljrTxtNewsTitle;
    @InjectView(R.id.tljr_hqss_news_titlebelow)
    TextView tljrHqssNewsTitlebelow;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.tljr_grp_goods_title)
    RelativeLayout tljrGrpGoodsTitle;
    @InjectView(R.id.img_null)
    ImageView imgNull;
    @InjectView(R.id.tv_null)
    TextView tvNull;
    @InjectView(R.id.tv_null2)
    TextView tvNull2;
    @InjectView(R.id.layout_null)
    RelativeLayout layoutNull;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.relative_network)
    RelativeLayout relativeNetwork;
    @InjectView(R.id.t_edit)
    TextView tEdit;
    @InjectView(R.id.relative_edit)
    RelativeLayout relativeEdit;

    @InjectView(R.id.tv_add)
    TextView tvAdd;
    @InjectView(R.id.btn_add)
    RelativeLayout btnAdd;

    @InjectView(R.id.btn_delete)
    RelativeLayout btnDelete;
    @InjectView(R.id.linear_bottom)
    RelativeLayout linearBottom;

    public static CheckBox btnCheckAll;
    public static TextView tvDelete;


    private static CollectionAdapter collectionAdapter;
    public Handler handler = new Handler();
    ArrayList<Goods> goodsList = new ArrayList<Goods>();
    private RequestQueue mRequestQueue;
    MyBroadCastReceiver myBroadCastReceiver;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    int totalPage;
    int currentPage = 1;
    boolean isLoadMore = false;
    boolean first = false;
    private View view;

    public static ArrayList<String> favId = new ArrayList<String>();
    boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_activity_collection, null);
        }
        setContentView(view);
        ButterKnife.inject(this);
        mRequestQueue = Volley.newRequestQueue(this);
        myBroadCastReceiver = new MyBroadCastReceiver(this, updateUI);
        myBroadCastReceiver.register();
        initView();

        initRecycler();
        setOnListener();
    }


    private static CompoundButton.OnCheckedChangeListener checkAllListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            btnCheckAll.setChecked(isChecked);
            if (isChecked) {
                collectionAdapter.checkAll();
                tvDelete.setText("清空");
            } else {
                collectionAdapter.cancelAll();
                tvDelete.setText("删除");
            }
        }

    };

    public static void setOnCheckChangeListener() {
        btnCheckAll.setOnCheckedChangeListener(checkAllListener);
    }

    private void initRecycler() {
        recyclerView.addOnScrollListener(mOnScrollListener);
        if (!NetworkUtils.isNetAvailable(this)) {
            if (relativeNetwork != null) {
                relativeNetwork.setVisibility(View.VISIBLE);
            }
        } else {
            initAllDates();
        }
        collectionAdapter = new CollectionAdapter(this, this);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(collectionAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(this, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
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
            if (intent.getStringExtra("type").equals(MyUpdateUI.COLLECTTION)) {
                initRecycler();
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initAllDates() {
        if (!first) {
            ProgressDlgUtil.showProgressDlg("Loading...", this);
        }
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_favorite_list + "&key=" + MyApplication.getInstance().getMykey() + "&curpage=" + currentPage, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "collection_msg=" + response);

                        totalPage = response.getInt("page_total");
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("favorites_list");
                        if (isLoadMore) {
                            int currentSize = collectionAdapter.getItemCount();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Goods g = new Goods();
                                g.setId(currentSize + i);
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                JSONObject goodsObject = object1.getJSONObject("goods");
                                g.setGoods_id(goodsObject.optString("goods_id"));
                                g.setStore_id(goodsObject.optString("store_id"));
                                g.setTitle(goodsObject.optString("goods_name"));
                                g.setMoney(goodsObject.optDouble("goods_price"));
                                g.setPicarr(goodsObject.optString("goods_image"));
                                g.setGoods_salenum(goodsObject.optString("goods_salenum"));
                                g.setFav_id(object1.optString("fav_id"));
                                g.setTime(object1.optLong("fav_time"));
                                goodsList.add(g);
                            }
                            addItems(goodsList);
                            if (btnCheckAll != null){
                                btnCheckAll.setOnCheckedChangeListener(null);
                                btnCheckAll.setChecked(false);
                                btnCheckAll.setOnCheckedChangeListener(checkAllListener);
                            }
                        } else {
                            collectionAdapter.getList().clear();
                            goodsList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Goods g = new Goods();
                                g.setId(i);
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                JSONObject goodsObject = object1.getJSONObject("goods");
                                g.setGoods_id(goodsObject.optString("goods_id"));
                                g.setStore_id(goodsObject.optString("store_id"));
                                g.setTitle(goodsObject.optString("goods_name"));
                                g.setMoney(goodsObject.optDouble("goods_price"));
                                g.setPicarr(goodsObject.optString("goods_image"));
                                g.setGoods_salenum(goodsObject.optString("goods_salenum"));
                                g.setFav_id(object1.optString("fav_id"));
                                g.setTime(object1.optLong("fav_time"));
                                goodsList.add(g);
                            }
                            mCurrentCounter = goodsList.size();
                            collectionAdapter.addItems(goodsList);
                            collectionAdapter.notifyDataSetChanged();
                            collectionAdapter.cancelAll();
                            if (btnCheckAll != null)
                                btnCheckAll.setChecked(false);
                        }
                        if (collectionAdapter.getList().size() == 0) {
                            if (layoutNull != null)
                                layoutNull.setVisibility(View.VISIBLE);
                            if (relativeEdit != null)
                                relativeEdit.setVisibility(View.GONE);
                            if (linearBottom != null)
                                linearBottom.setVisibility(View.GONE);
                        } else {
                            if (layoutNull != null)
                                layoutNull.setVisibility(View.GONE);
                            if (relativeEdit != null)
                                relativeEdit.setVisibility(View.VISIBLE);
                            if (linearBottom != null)
                                linearBottom.setVisibility(View.VISIBLE);
                        }
                        collectionAdapter.notifyDataSetChanged();
                    } else {
                        Log.i("zjz", "goodsActivity解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();
                } finally {
                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                    ProgressDlgUtil.stopProgressDlg();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast("请求失败");
                ProgressDlgUtil.stopProgressDlg();
                recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
            }
        });
        mRequestQueue.add(jr);
    }

    private void setOnListener() {
        relativeNetwork.setOnClickListener(this);
        findViewById(R.id.tljr_img_news_back).setOnClickListener(this);
        relativeEdit.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnCheckAll.setOnCheckedChangeListener(checkAllListener);
    }

    private void initView() {
        btnCheckAll = (CheckBox) view.findViewById(R.id.btn_check_all);
        tvDelete = (TextView) view.findViewById(R.id.tv_delete);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tljr_img_news_back:
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
            case R.id.relative_edit:
                edit();
                break;
            case R.id.btn_add:
                add2cart();
                break;
            case R.id.btn_delete:
                delete();
                break;
        }
    }

    private void add2cart() {
        if (MyApplication.getInstance().getMykey() == null) {
            showToast("请登录");
            Intent intent = new Intent(this, WXEntryActivity.class);
            startActivity(intent);
            return;
        }else if (favId.size() == 0) {
            showToast("您还没有选中任何一项");
            return;
        }
        String param = null;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < favId.size(); i++) {
            buffer.append(favId.get(i) + "|1");
            if (i != favId.size() - 1) {
                buffer.append(",");
            }
        }
        Log.i("zjz", "add2cart=" + buffer.toString());
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        param = buffer.toString();
        final String finalParam = param;
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_goods_buy_all_and_again, "&key=" + MyApplication.getInstance().getMykey() + "&goodsinfo=" +param, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zjz", "buy_all_msg=" + msg);
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                if (object.optString("datas").equals("1")) {
                                    showToast("添加到购物车成功！");
                                    MyUpdateUI.sendUpdateCarNum(CollectionActivity.this);
                                    Intent intent = new Intent(CollectionActivity.this, CartActivity2.class);
                                    intent.putExtra("store_id","");
                                    intent.putExtra("isCheck", true);
                                    intent.putExtra("goods", finalParam);
                                    startActivity(intent);
                                } else if (object.getJSONObject("datas").has("error")) {
                                    showToast("该商品已经下架或库存为0！");
                                } else {
                                    JSONObject jsonObject = object.getJSONObject("datas");
                                    Iterator keys = jsonObject.keys();
                                    StringBuffer stringBuffer = new StringBuffer();
                                    while (keys.hasNext()){
                                        String value= (String) keys.next();
                                        JSONObject goodsObject=jsonObject.getJSONObject(value);
                                        JSONObject goods=goodsObject.optJSONObject("kucun");
                                        if(goods!=null){
                                            if(goods.optString("goods_name").length()>20){
                                                stringBuffer.append("商品["+goods.optString("goods_name").substring(0,20)+"] ");
                                            }else {
                                                stringBuffer.append("商品["+goods.optString("goods_name")+"] ");
                                            }
                                        }else if(goodsObject.has("xiajia")){
                                            if(goodsObject.optString("xiajia").length()>20){
                                                stringBuffer.append("商品["+goodsObject.optString("xiajia").substring(0,20)+"] ");
                                            }else {
                                                stringBuffer.append("商品["+goodsObject.optString("xiajia")+"] ");
                                            }
                                        }

                                    }
                                    String text = stringBuffer + "库存为0或已经下架！";
                                    showToast(text);
                                    MyUpdateUI.sendUpdateCarNum(CollectionActivity.this);
                                    Intent intent = new Intent(CollectionActivity.this, CartActivity2.class);
                                    intent.putExtra("isCheck", true);
                                    intent.putExtra("store_id","");
                                    intent.putExtra("goods", finalParam);
                                    startActivity(intent);
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();

                        } finally {
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });
    }

    private void delete() {
        if (favId.size() == 0) {
            showToast("您还没有选中任何一项");
            return;
        }
        final StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < favId.size(); i++) {
            stringBuffer.append(favId.get(i));
            if (i != favId.size() - 1) {
                stringBuffer.append(",");
            }
        }
        Log.i("zjz", "delete=" + stringBuffer.toString());
        new PromptDialog(this, "确认删除这些收藏？", new Complete() {
            @Override
            public void complete() {
                ProgressDlgUtil.showProgressDlg("Loading...", CollectionActivity.this);
                HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_favorite_del, "fav_id=" + stringBuffer.toString() + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
                    @Override
                    public void revMsg(final String msg) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject object = new JSONObject(msg);
                                    if (object.getInt("code") == 200) {
                                        Log.i("zjz", "msg=" + msg);
                                        ProgressDlgUtil.stopProgressDlg();
                                        MyUpdateUI.sendUpdateCollection(CollectionActivity.this, MyUpdateUI.COLLECTTION);
                                    } else {
                                        ProgressDlgUtil.stopProgressDlg();
                                        Log.i("zjz", "goodsDetail:解析失败");
                                    }
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    Log.i("zjz", e.toString());
                                    Log.i("zjz", msg);
                                    e.printStackTrace();
                                    ProgressDlgUtil.stopProgressDlg();
                                }
                            }
                        });
                    }
                });
            }
        }).show();


    }

    /**
     * 编辑
     */
    private void edit() {
        isEdit = !isEdit;
        if (isEdit) {
            tEdit.setText("完成");
            btnAdd.setVisibility(View.GONE);
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            tEdit.setText("编辑");
            btnAdd.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        myBroadCastReceiver.unRegister();
        ButterKnife.reset(this);
        super.onDestroy();
    }

    @Override
    public void swipeRefreshLayoutOnRefresh() {
        first = true;
        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(true);
        isLoadMore = false;
        currentPage = 1;
        initAllDates();
    }

    @Override
    public void ItemRootClick(int position) {
        Intent intent = new Intent(this, GoodsDetailActivity.class);
        intent.putExtra("sid", collectionAdapter.getList().get(position).getGoods_id());
        intent.putExtra("pic", TLUrl.getInstance().URL_hwg_comment_goods + collectionAdapter.getList().get(position).getPicarr().charAt(0) + "/" + collectionAdapter.getList().get(position).getPicarr());
        startActivity(intent);
    }


    private PreviewHandler mHandler = new PreviewHandler(this);
    private int mCurrentCounter = 0;

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Goods> list) {

        collectionAdapter.addItems(list);
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
                RecyclerViewStateUtils.setFooterViewState(CollectionActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(CollectionActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }

    };


    private class PreviewHandler extends Handler {

        private WeakReference<CollectionActivity> ref;

        PreviewHandler(CollectionActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final CollectionActivity activity = ref.get();
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
            RecyclerViewStateUtils.setFooterViewState(CollectionActivity.this, recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                if (NetworkUtils.isNetAvailable(CollectionActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

}
