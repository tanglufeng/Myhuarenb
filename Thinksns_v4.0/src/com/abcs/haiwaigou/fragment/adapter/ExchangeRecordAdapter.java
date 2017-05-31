package com.abcs.haiwaigou.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcs.haiwaigou.fragment.viewholder.ExchangeRecordViewHolder;
import com.abcs.haiwaigou.model.Points;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
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
 * Created by zjz on 2016/4/26.
 */
public class ExchangeRecordAdapter extends RecyclerView.Adapter<ExchangeRecordViewHolder>{
    Context context;
    private SortedList<Points> mSortedList;
    private Activity activity;
    private RequestQueue mRequestQueue;
    public ExchangeRecordAdapter(Activity activity) {
        this.activity = activity;
        mRequestQueue = Volley.newRequestQueue(activity);
        mSortedList = new SortedList<>(Points.class, new SortedList.Callback<Points>() {

            /**
             * 返回一个负整数（第一个参数小于第二个）、零（相等）或正整数（第一个参数大于第二个）
             */
            @Override
            public int compare(Points o1, Points o2) {

                if (o1.getId() < o2.getId()) {
                    return -1;
                } else if (o1.getId() > o2.getId()) {
                    return 1;
                }

                return 0;
            }

            @Override
            public boolean areContentsTheSame(Points oldItem, Points newItem) {
                return oldItem.getPl_id().equals(newItem.getPl_id());
            }

            @Override
            public boolean areItemsTheSame(Points item1, Points item2) {
                return item1.getId() == item2.getId();
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }
        });
    }

    public SortedList<Points> getList() {
        return mSortedList;
    }

    public void addItems(ArrayList<Points> list) {
        mSortedList.beginBatchedUpdates();

        for (Points itemModel : list) {
            mSortedList.add(itemModel);
        }

        mSortedList.endBatchedUpdates();
    }

    public void deleteItems(ArrayList<Points> items) {
        mSortedList.beginBatchedUpdates();
        for (Points item : items) {
            mSortedList.remove(item);
        }
        mSortedList.endBatchedUpdates();
    }

    @Override
    public ExchangeRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hwg_item_fragment_record, parent, false);
        ExchangeRecordViewHolder hwgFragmentViewHolder = new ExchangeRecordViewHolder(view);
        this.context = parent.getContext();
        return hwgFragmentViewHolder;
    }

    @Override
    public void onBindViewHolder(final ExchangeRecordViewHolder holder, int position) {
        final Points item=mSortedList.get(position);
        String string;
        if (item.getPl_desc().contains("代金券")) {
            string =item.getPl_desc().replaceAll("代金券", "").replaceAll("消耗积分", "");
        } else {
            string =item.getPl_desc();
        }
        holder.t_points.setText(item.getPl_points());
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_my_vouncher + "&key=" + MyApplication.getInstance().getMykey() + "&voucher_code=" + string, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "points_detail:连接成功");
                        Log.i("zjz", "response=" + response);
                        JSONArray array = response.getJSONArray("datas");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject pointObject = array.getJSONObject(i);
                                holder.t_desc.setText("满" + pointObject.optString("voucher_limit") + "减" + pointObject.optString("voucher_price") + "代金券");
                                holder.t_add_time.setText(Util.format1.format(pointObject.optLong("voucher_start_date")*1000)+"");

                                holder.t_price.setText("¥"+pointObject.optString("voucher_price"));
                                Points points = new Points();
                                points.setId(i);
                                points.setPl_addtime(pointObject.optLong("voucher_start_date"));
                                points.setPl_desc("满" + pointObject.optString("voucher_limit") + "减" + pointObject.optString("voucher_price") + "代金券");
                                points.setPl_adminname(pointObject.optString("voucher_price"));
                            }

                    } else {
                        Log.i("zjz", "goodsActivity解析失败");
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", e.toString());
                    e.printStackTrace();
                } finally {
                    ProgressDlgUtil.stopProgressDlg();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDlgUtil.stopProgressDlg();
            }
        });
        mRequestQueue.add(jr);
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }
}
