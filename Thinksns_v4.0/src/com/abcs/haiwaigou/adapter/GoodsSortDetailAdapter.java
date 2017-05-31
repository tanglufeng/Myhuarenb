package com.abcs.haiwaigou.adapter;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.abcs.haiwaigou.model.Goods;
import com.abcs.sociax.android.R;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView;
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
 * Created by zjz on 2016/2/22.
 */
public class GoodsSortDetailAdapter extends BaseAdapter{
    GoodsChildAdpater goodsAdapter;
    private RequestQueue mRequestQueue;
    private ArrayList<Goods> goodsList;
    private ArrayList<Goods> goodsArray=new ArrayList<Goods>();
    Activity activity;
    LayoutInflater inflater = null;
    ZrcListView listView;
    //    MyListView listView;
    public Handler handler = new Handler();



    public GoodsSortDetailAdapter(Activity activity, ArrayList<Goods> goodsList,
                            ZrcListView listView) {
        // TODO Auto-generated constructor stub

        this.activity = activity;
        this.goodsList = goodsList;
        this.listView = listView;

        mRequestQueue= Volley.newRequestQueue(activity);
        inflater = LayoutInflater.from(activity);
    }


    ImageView imageView;

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder mHolder = null;
        final Goods goods = getItem(position);
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(activity);
            convertView = mInflater.inflate(R.layout.hwg_item_expandlist_group, null);

            mHolder = new ViewHolder();

            mHolder.t_txt = (TextView) convertView.findViewById(R.id.t_txt);
            mHolder.expendlist= (ZrcListView) convertView.findViewById(R.id.expendlist);
            mHolder.linear_root= (LinearLayout) convertView.findViewById(R.id.linear_root);
            convertView.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) convertView.getTag();

        }

		/*
         * 左上角日期
		 */
        final boolean[] isClick = {false};
        mHolder.t_txt.setText(goods.getTitle());
        final ViewHolder finalMHolder = mHolder;
        mHolder.linear_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isClick[0] =!isClick[0];
                Log.i("zjz","isClick="+isClick[0]);
                if(isClick[0]){
                    finalMHolder.expendlist.setVisibility(View.VISIBLE);
                    Log.i("zjz","gcid="+goodsList.get(position).getGc_id());
                    JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_goodssort+"&gc_id="+goodsList.get(position).getGc_id(), null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getInt("code") == 200) {
                                    goodsArray.clear();
                                    Log.i("zjz", "child:连接成功");
                                    JSONObject jsonObject = response.getJSONObject("datas");
                                    JSONArray jsonArray = jsonObject.getJSONArray("class_list");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object1 = jsonArray.getJSONObject(i);
                                        Goods g = new Goods();
                                        g.setGc_id(object1.optString("gc_id"));
                                        g.setTitle(object1.optString("gc_name"));
                                        goodsArray.add(g);
                                    }
                                    Goods c=new Goods();
                                    c.setGc_id(goodsList.get(position).getGc_id());
                                    c.setTitle("全部商品");
                                    goodsArray.add(0,c);
                                    Log.i("zjz","goodsArray="+goodsArray.size());
                                    goodsAdapter = new GoodsChildAdpater(activity, goodsArray, finalMHolder.expendlist);
                                    finalMHolder.expendlist.setAdapter(goodsAdapter);
                                    setListViewHeight(finalMHolder.expendlist);
                                    goodsAdapter.notifyDataSetChanged();
                                } else {
                                    Log.i("zjz", "child:失败");
                                }

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                Log.i("zjz", e.toString());
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("zjz", error.getMessage());
                        }
                    });
                    mRequestQueue.add(jr);
                }else {
                    finalMHolder.expendlist.setVisibility(View.GONE);
                }
            }
        });

        return convertView;

    }


    private static class ViewHolder {
        TextView t_txt;
        ZrcListView expendlist;
        LinearLayout linear_root;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return goodsList == null ? 0 : goodsList.size();
    }


    @Override
    public Goods getItem(int position) {
        if (goodsList != null && goodsList.size() != 0) {
            if (position >= goodsList.size()) {
                return goodsList.get(0);
            }
            return goodsList.get(position);
        }
        return null;
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void setListViewHeight(ZrcListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
