package com.abcs.haiwaigou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.abcs.haiwaigou.adapter.GoodsAdapter;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.tljr.zrclistview.SimpleFooter;
import com.abcs.huaqiaobang.tljr.zrclistview.SimpleHeader;
import com.abcs.huaqiaobang.tljr.zrclistview.ZrcListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GoodsActivity extends BaseFragmentActivity implements View.OnClickListener{
    public ZrcListView listView;
    private RelativeLayout view;
    GoodsAdapter goodsAdapter;
    ArrayList<Goods> goodsList=new ArrayList<Goods>();
    GoodsActivity activity;
    private int brandid;
    public Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.hwg_activity_goods);
        brandid= (int) getIntent().getSerializableExtra("brandid");
        findViewById(R.id.tljr_img_news_back).setOnClickListener(this);
        InitListView();

    }

    private void InitListView() {
        initAllDates();
        listView = (ZrcListView)findViewById(R.id.tljr_zListView);

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
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_pinpai_goods, "brandid=" + brandid + "&pages=1&pagelist=10", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("status") == 1) {
                                goodsList.clear();
                                Log.i("zjz", "goodsActivity:连接成功");
                                JSONArray jsonArray = object.getJSONArray("msg");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    Goods g = new Goods();

                                    g.setTitle(object1.optString("title"));
                                    g.setSubhead(object1.optString("subhead"));
                                    g.setMoney(object1.optDouble("money"));
                                    g.setPicarr(object1.optString("picarr"));
                                    g.setStore(object1.optInt("store"));
                                    g.setSy_store(object1.optInt("sy_store"));
                                    g.setSid(object1.optInt("sid"));
                                    goodsList.add(g);
                                }
//                                gridAdapter=new GridAdapter(activity,dataList);
//                                gridView.setAdapter(gridAdapter);
//                                gridAdapter.notifyDataSetChanged();
                                goodsAdapter = new GoodsAdapter(GoodsActivity.this, goodsList, listView);
                                listView.setAdapter(goodsAdapter);
                                goodsAdapter.notifyDataSetChanged();
                            } else {
                                Log.i("zjz", "goodsActivity解析失败");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();

                        }
                    }
                });

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tljr_img_news_back:
                finish();
                break;
        }
    }
}
