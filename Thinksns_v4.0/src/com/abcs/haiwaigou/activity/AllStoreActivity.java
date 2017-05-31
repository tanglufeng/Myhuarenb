package com.abcs.haiwaigou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.abcs.haiwaigou.adapter.AllStoreAdapter;
import com.abcs.haiwaigou.model.Store;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
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

public class AllStoreActivity extends BaseActivity {
    private RequestQueue mRequestQueue;
    public ZrcListView listView;
    public Handler handler = new Handler();
    ArrayList<Store> storeList = new ArrayList<Store>();
    AllStoreAdapter goodsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_all_store);

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


//        listView.setOnRefreshStartListener(new ZrcListView.OnStartListener() {
//            @Override
//            public void onStart() {
//            }
//        });
//
//        listView.setOnLoadMoreStartListener(new ZrcListView.OnStartListener() {
//            @Override
//            public void onStart() {
//
//            }
//        });
        initAllDates();
    }


    private void initAllDates() {
        ProgressDlgUtil.showProgressDlg("Loading...",this);
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_store, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 200) {
                        Log.i("zjz", "store:连接成功");
                        storeList.clear();
                        JSONObject jsonObject = response.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject.getJSONArray("class_list");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            Store s = new Store();
                            s.setId(object1.optString("sc_id"));
                            s.setName(object1.optString("sc_name"));
                            storeList.add(s);
                        }
                        Store store=new Store();
                        store.setName("显示所有商铺");
                        store.setId("0");
                        storeList.add(0, store);
                        goodsAdapter = new AllStoreAdapter(AllStoreActivity.this, storeList, listView);
                        listView.setAdapter(goodsAdapter);
                        goodsAdapter.notifyDataSetChanged();
                        ProgressDlgUtil.stopProgressDlg();
                    } else {
                        Log.i("zjz", "goodsActivity解析失败");
                        ProgressDlgUtil.stopProgressDlg();
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
