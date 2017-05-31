package com.abcs.haiwaigou.fragment;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.adapter.RechargeAdapter;
import com.abcs.haiwaigou.fragment.viewholder.RechargeViewHolder;
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
import com.abcs.huaqiaobang.activity.NoticeDialogActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
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
 * Created by zjz on 2016/4/26.
 */
public class RechargeFragment extends Fragment implements RechargeViewHolder.ItemOnClick, RecyclerViewAndSwipeRefreshLayout.SwipeRefreshLayoutRefresh {
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
    Activity activity;
    private View view;
    private RequestQueue mRequestQueue;
    int totalPage;
    int currentPage = 1;
    boolean isLoadMore = false;
    boolean first = false;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    RecyclerViewAndSwipeRefreshLayout recyclerViewAndSwipeRefreshLayout;
    RechargeAdapter rechargeAdapter;

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
        rechargeAdapter = new RechargeAdapter(activity, this);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(rechargeAdapter);
        recyclerViewAndSwipeRefreshLayout = new RecyclerViewAndSwipeRefreshLayout(activity, view, mHeaderAndFooterRecyclerViewAdapter, this, MyString.TYPE0);
    }

    private void initAllDates() {
        final ArrayList<Voucher> dataList = new ArrayList<>();
        if(!first){
            ProgressDlgUtil.showProgressDlg("Loading...", activity);
        }
//        type('order_sn','refund_sn','goods_name')
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_vouncher_list + "&key=" + MyApplication.getInstance().getMykey() + "&curpage=" + currentPage, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "good_voucher:连接成功");
                        Log.i("zjz", "response=" + response);
                        totalPage = response.getInt("page_total");
                        if (response.getInt("page_total") != 0) {
                            JSONObject object = response.getJSONObject("datas");
                            JSONArray array = object.getJSONArray("voucherlist");
                            if (isLoadMore) {
                                int currentSize = rechargeAdapter.getItemCount();

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject voucherObject = array.getJSONObject(i);
                                    Voucher voucher = new Voucher();
                                    voucher.setId(currentSize + i);
                                    voucher.setVoucher_t_id(voucherObject.optString("voucher_t_id"));
                                    voucher.setVoucher_t_title(voucherObject.optString("voucher_t_title"));
                                    voucher.setVoucher_t_end_date(voucherObject.optLong("voucher_t_end_date"));
                                    voucher.setVoucher_t_limit(voucherObject.optInt("voucher_t_limit"));
                                    voucher.setVoucher_t_price(voucherObject.optString("voucher_t_price"));
                                    voucher.setVoucher_t_total(voucherObject.optString("voucher_t_total"));
                                    voucher.setVoucher_t_customimg(voucherObject.optString("voucher_t_customimg"));
                                    voucher.setVoucher_t_points(voucherObject.optString("voucher_t_points"));

                                    dataList.add(voucher);
                                }
                                addItems(dataList);

                            } else {
                                rechargeAdapter.getList().clear();
                                dataList.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject voucherObject = array.getJSONObject(i);
                                    Voucher voucher = new Voucher();
                                    voucher.setId(i);
                                    voucher.setVoucher_t_id(voucherObject.optString("voucher_t_id"));
                                    voucher.setVoucher_t_title(voucherObject.optString("voucher_t_title"));
                                    voucher.setVoucher_t_end_date(voucherObject.optLong("voucher_t_end_date"));
                                    voucher.setVoucher_t_limit(voucherObject.optInt("voucher_t_limit"));
                                    voucher.setVoucher_t_price(voucherObject.optString("voucher_t_price"));
                                    voucher.setVoucher_t_total(voucherObject.optString("voucher_t_total"));
                                    voucher.setVoucher_t_customimg(voucherObject.optString("voucher_t_customimg"));
                                    voucher.setVoucher_t_points(voucherObject.optString("voucher_t_points"));

                                    dataList.add(voucher);
                                }
                                mCurrentCounter = dataList.size();
                                rechargeAdapter.addItems(dataList);
                                rechargeAdapter.notifyDataSetChanged();
                            }
                        }
                        if(layoutNull!=null)
                            layoutNull.setVisibility(rechargeAdapter.getList().size() == 0?View.VISIBLE:View.GONE);
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                    } else {
                        Log.i("zjz", "goodsActivity解析失败");
                        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
                    }

                    ProgressDlgUtil.stopProgressDlg();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();
                    recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(false);
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


    private MyHandler handler = new MyHandler();
    private int mCurrentCounter = 0;

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Voucher> list) {

        rechargeAdapter.addItems(list);
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

    @Override
    public void swipeRefreshLayoutOnRefresh() {
        first=true;
        recyclerViewAndSwipeRefreshLayout.getSwipeRefreshLayout().setRefreshing(true);
        isLoadMore = false;
        currentPage = 1;
        initAllDates();
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
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    @Override
    public void ItemRootClick(final int position) {
        final TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15, 10, 10, 15);
        tv.setLayoutParams(layoutParams);
        ProgressDlgUtil.showProgressDlg("Loading...", activity);
//        type('order_sn','refund_sn','goods_name')
        final String[] string = new String[1];
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_vouncher_detail + "&vid=" + rechargeAdapter.getList().get(position).getVoucher_t_id() + "&key=" + MyApplication.getInstance().getMykey(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "voucher_detail:连接成功");
                        Log.i("zjz", "response=" + response);
                        JSONObject object = response.getJSONObject("datas");
                        if (object.optBoolean("result")) {
                            JSONObject temp = object.optJSONObject("template_info");
                            tv.setText("您正在使用" + temp.optString("voucher_t_points") + "积分 兑换 1 张\n"
                                    + temp.optString("voucher_t_storename") + temp.optString("voucher_t_price") + "元店铺代金券"
                                    + "(满" + temp.optString("voucher_t_limit") + "减" + temp.optString("voucher_t_price") + ")");
                            string[0] = "您正在使用" + temp.optString("voucher_t_points") + "积分 兑换 1 张\n"
                                    + temp.optString("voucher_t_storename") + temp.optString("voucher_t_price") + "元店铺代金券"
                                    + "(满" + temp.optString("voucher_t_limit") + "减" + temp.optString("voucher_t_price") + ")";
                            initDialog(string[0], rechargeAdapter.getList().get(position).getVoucher_t_id());
                        } else {
                            tv.setText(object.optString("message"));
                            string[0] = object.optString("message");
                            startActivity(new Intent(getActivity(), NoticeDialogActivity.class).putExtra("msg", string[0] ));
                        }

                    } else {
                        Log.i("zjz", "goodsActivity解析失败");
                    }

                    ProgressDlgUtil.stopProgressDlg();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();
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

    private void initDialog(String string, final String vid) {

        new PromptDialog(activity, string, new Complete() {
            @Override
            public void complete() {
                ProgressDlgUtil.showProgressDlg("Loading...", getActivity());
                HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_vouncher_save, "&vid=" + vid + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
                            @Override
                            public void revMsg(final String msg) {
                                handler.post(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     try {
                                                         JSONObject object = new JSONObject(msg);
                                                         Log.i("zjz", "object=" + msg);
                                                         if(object.optString("datas").contains("成功")){
                                                             MyUpdateUI.sendUpdateCollection(activity,MyUpdateUI.RECHARGE);
                                                             Toast.makeText(activity,"兑换成功！",Toast.LENGTH_SHORT).show();
                                                         }else {
                                                             Toast.makeText(activity,object.optString("datas"),Toast.LENGTH_SHORT).show();
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
        }).show();

    }

}
