package com.abcs.haiwaigou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.abcs.haiwaigou.adapter.GoodsSortAdapter;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
//import com.abcs.huaqiaobang.util.HttpRequest;
//import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.tljr.zrclistview.SimpleFooter;
import com.abcs.huaqiaobang.tljr.zrclistview.SimpleHeader;
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

public class GoodsSortActivity extends BaseActivity {
    private RequestQueue mRequestQueue;
    public ZrcListView listView;
    public Handler handler = new Handler();
    ArrayList<Goods> goodsList = new ArrayList<Goods>();
    GoodsSortAdapter goodsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_goods_sort);

        mRequestQueue = Volley.newRequestQueue(this);
        findViewById(R.id.tljr_img_news_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        InitListView();
    }

    private void InitListView() {
        listView = (ZrcListView) findViewById(R.id.tljr_zListView);

        // // 设置默认偏移量，主要用于实现透明标题栏功能。（可选）
        // float density = getResources().getDisplayMetrics().density;
        // listView.setFirstTopOffset((int) (50 * density));

        listView.setFooterDividersEnabled(false);
        // listView.setSelector(R.drawable.tljr_listview_selector);
        // 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
        SimpleHeader header = new SimpleHeader(this);
        header.setTextColor(0xffeb5041);
        header.setCircleColor(0xffeb5041);
        listView.setHeadable(header);

        // 设置加载更多的样式（可选）
        SimpleFooter footer = new SimpleFooter(this);
        footer.setCircleColor(0xffeb5041);
        listView.setFootable(footer);

//        for(int i=0;i<5;i++){
//            Goods goods=new Goods();
//            goods.setTitle("Estee Lauder雅诗兰黛红石榴 鲜亮幻彩泡沫洁面乳 125ml");
//            goodsList.add(goods);
//        }

        listView.setOnRefreshStartListener(new ZrcListView.OnStartListener() {
            @Override
            public void onStart() {
            }
        });

        listView.setOnLoadMoreStartListener(new ZrcListView.OnStartListener() {
            @Override
            public void onStart() {

            }
        });
        initAllDates();
    }


    private void initAllDates() {
//        HttpUtils httpUtils=new HttpUtils();
//
//        httpUtils.send(HttpRequest.HttpMethod.GET, TLUrl.getInstance().URL_hwg_goodssort, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess( ResponseInfo<String> responseInfo) {
//                try {
//                    Log.i("zjz","responseInfo="+responseInfo.result);
//                    JSONObject object = new JSONObject(responseInfo.result);
//                    if (object.getInt("code") == 200) {
//                        Log.i("zjz", "goodssort:连接成功");
//                        goodsList.clear();
//                        JSONObject jsonObject = object.getJSONObject("datas");
//                        JSONArray jsonArray = jsonObject.getJSONArray("class_list");
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject object1 = jsonArray.getJSONObject(i);
//                            Goods g = new Goods();
//                            g.setGc_id(object1.optString("gc_id"));
//                            g.setTitle(object1.optString("gc_name"));
//                            g.setSubhead(object1.getString("text"));
//                            goodsList.add(g);
//                        }
//                        goodsAdapter = new GoodsSortAdapter(GoodsSortActivity.this, goodsList, listView);
//                        listView.setAdapter(goodsAdapter);
//                        goodsAdapter.notifyDataSetChanged();
//                    } else {
//                        Log.i("zjz", "goodsActivity解析失败");
//                    }
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    Log.i("zjz", e.toString());
//                    e.printStackTrace();
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//
//            }
//        });


        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_goodssort, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.i("zjz", response.toString());
                try {
//                    JSONObject object = new JSONObject(response.toString());
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "goodssort:连接成功");
                        goodsList.clear();
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("class_list");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            Goods g = new Goods();
                            g.setGc_id(object1.optString("gc_id"));
                            g.setTitle(object1.optString("gc_name"));
                            g.setSubhead(object1.getString("text"));
                            goodsList.add(g);
                        }
                        goodsAdapter = new GoodsSortAdapter(GoodsSortActivity.this, goodsList, listView);
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

//        HttpRequest.sendHttpsGet(TLUrl.getInstance().URL_hwg_goodssort, null, new HttpRevMsg() {
//            @Override
//            public void revMsg(final String msg) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Log.i("zjz", "msg=" + msg);
//                            JSONObject object = new JSONObject(msg);
//                            if (object.getInt("code") == 200) {
//                                Log.i("zjz", "goodssort:连接成功");
//                                goodsList.clear();
//                                JSONObject jsonObject = object.getJSONObject("datas");
//                                JSONArray jsonArray = jsonObject.getJSONArray("class_list");
//
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject object1 = jsonArray.getJSONObject(i);
//                                    Goods g = new Goods();
//                                    g.setGc_id(object1.optString("gc_id"));
//                                    g.setTitle(object1.optString("gc_name"));
//                                    g.setSubhead(object1.getString("text"));
//                                    goodsList.add(g);
//                                }
//                                goodsAdapter = new GoodsSortAdapter(GoodsSortActivity.this, goodsList, listView);
//                                listView.setAdapter(goodsAdapter);
//                                goodsAdapter.notifyDataSetChanged();
//                            } else {
//                                Log.i("zjz", "goodsActivity解析失败");
//                            }
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            Log.i("zjz", e.toString());
//                            Log.i("zjz", msg);
//                            e.printStackTrace();
//
//                        }
//                    }
//                });
//
//            }
//        });
    }
}
