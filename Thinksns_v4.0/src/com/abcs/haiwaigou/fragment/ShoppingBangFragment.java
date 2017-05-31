package com.abcs.haiwaigou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.abcs.haiwaigou.activity.GoodsDetailActivity2;
import com.abcs.haiwaigou.activity.StoreActivity;
import com.abcs.haiwaigou.adapter.AllGoodsRecyclerAapter;
import com.abcs.haiwaigou.adapter.viewholder.AllGoodsRecyclerViewHolder;
import com.abcs.haiwaigou.fragment.customtool.FullyLinearLayoutManager;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.sociax.android.R;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/3/3.
 */
public class ShoppingBangFragment extends Fragment implements AllGoodsRecyclerViewHolder.ItemOnClick {

    StoreActivity activity;
    ArrayList<Goods> goodses;
    @InjectView(R.id.goodsListView)
    RecyclerView goodsListView;
    @InjectView(R.id.layout_null)
    RelativeLayout layoutNull;
    private View view;
    int currentPage = 1;
    String store_id;
    RequestQueue mRequestQueue;
    boolean isLoadMore;

    public ShoppingBangFragment() {
    }

//    public ShoppingBangFragment(String store_id) {
//        this.store_id = store_id;
//    }

    public static ShoppingBangFragment getInstance(String store_id) {

        ShoppingBangFragment f = new ShoppingBangFragment();
        Bundle bundle = new Bundle();
//        bundle.putSerializable("data", goodses);
        bundle.putSerializable("id", store_id);
        f.setArguments(bundle);
        return f;
    }

    AllGoodsRecyclerAapter allGoodsRecyclerAapter;
    FullyLinearLayoutManager fullyLinearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle arguments = getArguments();
//        goodses = (ArrayList<Goods>) arguments.getSerializable("data");
        store_id=arguments.getString("id");

        activity = (StoreActivity) getActivity();
        if (view == null)
            view = inflater.inflate(R.layout.hwg_shopingbang_fragment, null);
        ButterKnife.inject(this, view);
        mRequestQueue = Volley.newRequestQueue(activity);

        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {

        fullyLinearLayoutManager = new FullyLinearLayoutManager(activity);
        allGoodsRecyclerAapter = new AllGoodsRecyclerAapter(activity,this);
        goodsListView.setLayoutManager(fullyLinearLayoutManager);
        goodsListView.setAdapter(allGoodsRecyclerAapter);
//        goodsListView.setFocusable(false);
        initAllDates();
    }

    private void initAllDates() {
        final ArrayList<Goods> dataList = new ArrayList<>();
//        ProgressDlgUtil.showProgressDlg("", this);
        Log.i("zjz", "url_currentPage=" + currentPage);
        Log.i("zjz", "store_id=" + store_id);

        String param = null;
        param = TLUrl.getInstance().URL_hwg_store_goods + "&key=4&page=10&curpage=" + currentPage + "&store_id=" + store_id;
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, param, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "storegoods:连接成功");
//                        type = NOMAL;
//                        totalPage = response.getInt("page_total");
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("goods_list");
                        if (isLoadMore) {
                            int currentSize = allGoodsRecyclerAapter.getItemCount();
                            Log.i("zjz", "currentsize=" + currentSize);
                            //模拟组装10个数据

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                Goods g = new Goods();
                                g.setId(currentSize + i);
                                g.setGoods_id(object1.optString("goods_id"));
                                g.setTitle(object1.optString("goods_name"));
                                g.setMoney(object1.optDouble("goods_price"));
                                g.setPicarr(object1.optString("goods_image_url"));
                                g.setGoods_salenum(object1.optString("goods_salenum"));
                                dataList.add(g);
                            }
//                            addItems(dataList);
                        } else {
                            allGoodsRecyclerAapter.getList().clear();
                            dataList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                Goods g = new Goods();
                                g.setId(i);
                                g.setGoods_id(object1.optString("goods_id"));
                                g.setTitle(object1.optString("goods_name"));
                                g.setMoney(object1.optDouble("goods_price"));
                                g.setPicarr(object1.optString("goods_image_url"));
                                g.setGoods_salenum(object1.optString("goods_salenum"));
                                dataList.add(g);
                            }
//                            mCurrentCounter = dataList.size();
                            allGoodsRecyclerAapter.addItems(dataList);
                            allGoodsRecyclerAapter.notifyDataSetChanged();
                        }
                        Log.i("zjz","size="+allGoodsRecyclerAapter.getList().size());
                        if (allGoodsRecyclerAapter.getList().size() == 0) {
                            layoutNull.setVisibility(View.VISIBLE);
                        } else {
                            layoutNull.setVisibility(View.GONE);
                        }
//                        allGoodsRecyclerAdapter.notifyDataSetChanged();
//                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
//                        ProgressDlgUtil.stopProgressDlg();
                    } else {
//                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                        Log.i("zjz", "goodsActivity解析失败");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();
//                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
            }
        });
        mRequestQueue.add(jr);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    @Override
    public void onItemRootViewClick(int position) {
        Intent intent=new Intent(activity, GoodsDetailActivity2.class);
        intent.putExtra("sid", allGoodsRecyclerAapter.getList().get(position).getGoods_id());
        intent.putExtra("pic", allGoodsRecyclerAapter.getList().get(position).getPicarr());
        activity.startActivity(intent);
    }
}
