package com.abcs.haiwaigou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.abcs.haiwaigou.adapter.GoodsSortDetailAdapter;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
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

public class GoodsSortDetailActivity extends BaseActivity  {

    private RequestQueue mRequestQueue;
    public Handler handler = new Handler();
    String gc_id;
    public ZrcListView listView;
    ArrayList<Goods> goodsList = new ArrayList<Goods>();
    GoodsSortDetailAdapter goodsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_goods_sort_detail);
        mRequestQueue= Volley.newRequestQueue(this);
        gc_id= (String) getIntent().getSerializableExtra("gc_id");
        listView = (ZrcListView) findViewById(R.id.zrclistview);
        findViewById(R.id.tljr_img_news_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initDatas();

    }

    private void initDatas() {

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_goodssort+"&gc_id="+gc_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "group:连接成功");
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("class_list");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            Goods g = new Goods();
                            g.setGc_id(object1.optString("gc_id"));
                            g.setTitle(object1.optString("gc_name"));
                            goodsList.add(g);
                        }
                        goodsAdapter = new GoodsSortDetailAdapter(GoodsSortDetailActivity.this, goodsList, listView);
                        listView.setAdapter(goodsAdapter);
                        goodsAdapter.notifyDataSetChanged();
                    } else {
                        Log.i("zjz", "goodsActivity解析失败");
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
    }

}
