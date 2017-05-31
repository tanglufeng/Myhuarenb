package com.abcs.huaqiaobang.presenter;

import com.abcs.haiwaigou.model.Goods;
import com.abcs.huaqiaobang.MyApplication;
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
 * Created by zhou on 2016/4/19.
 */
public class Special {


    LoadDataInterFace loadDataInterFace;

    public Special(LoadDataInterFace loadDataInterFace) {
        this.loadDataInterFace = loadDataInterFace;
    }

    private static RequestQueue requestQueue;

    static {
        requestQueue = Volley.newRequestQueue(MyApplication.getInstance());
    }

    public void loadData(String id) {


//        StringRequest jr = new StringRequest(Request.Method.POST, TLUrl.getInstance().URL_hwg_order2, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                if (response.startsWith("ï»¿")) {
//
//                    response.replace("ï»¿", "");
//                }
//                if (response.startsWith("\ufeff")) {
//                    response = response.substring(1);
//
//                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(response);
//                    jsonObject.containsKey("");
//                }
////                try {
////                    JSONObject object = new JSONObject(response);
//// } catch (JSONException e) {
////                    e.printStackTrace();
////                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                String s = error.toString();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("key", MyApplication.getInstance().getMykey());
//                map.put("order_sn", "");
//                map.put("query_start_date", "");
//                map.put("query_end_date", "");
//                map.put("state_type", "");
//                map.put("recycle", "");
//                map.put("curpage", "1");
//                return map;
//            }
//        };
//        requestQueue.add(jr);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_special + "&special_id=" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                try {
                    JSONArray datas = jsonObject.getJSONArray("datas");

                    ArrayList<Goods> goodses = new ArrayList<>();
                    for (int i = 1; i < datas.length(); i++) {

                        JSONObject jsonObject1 = datas.getJSONObject(i).getJSONObject("goods");

                        JSONArray item = jsonObject1.getJSONArray("item");
                        for (int j = 0; j < item.length(); j++) {
                            Goods goods = new Goods();
                            JSONObject goodsObj = item.getJSONObject(j);
                            goods.setGoods_id(goodsObj.optString("goods_id"));
                            goods.setTitle(goodsObj.optString("goods_name"));
                            goods.setMoney(goodsObj.optDouble("goods_promotion_price"));
                            goods.setPicarr(goodsObj.optString("goods_image"));
                            goodses.add(goods);
                        }
                    }
                    loadDataInterFace.loadSuccess(goodses);
                } catch (JSONException e) {
                    e.printStackTrace();
                    loadDataInterFace.loadFailed(e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loadDataInterFace.loadFailed(volleyError.toString());
            }
        });


        requestQueue.add(request);

    }
}
