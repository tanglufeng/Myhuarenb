package com.abcs.haiwaigou.utils;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.abcs.haiwaigou.model.Goods;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/1/23.
 */
public class InitCarNum {
    public TextView textView;
    Activity activity;
    public Handler handler = new Handler();

 /*   public InitCarNum(TextView textView, Activity activity) {
        this.textView = textView;
        this.activity=activity;
        initDate(textView);
    }*/
    public InitCarNum(TextView textView, Activity activity,String store_id) {
        this.textView = textView;
        this.activity=activity;
        initDate(textView,store_id);
    }

    private String store_id,paramas;
    public void initDate(final TextView textView,String store_id) {
//        ProgressDlgUtil.showProgressDlg("",activity);
        final ArrayList<Goods> carList = new ArrayList<Goods>();

        if(!TextUtils.isEmpty(store_id)){
            Log.i("zjz", "cart_store=" + TLUrl.getInstance().URL_hwg_cartlist+"key=" + MyApplication.getInstance().getMykey()+"&store_id="+store_id);
            paramas="key=" + MyApplication.getInstance().getMykey()+"&store_id="+store_id;
        }else {
            Log.i("zjz", "cart_store=" + TLUrl.getInstance().URL_hwg_cartlist+"key=" + MyApplication.getInstance().getMykey());
            paramas="key=" + MyApplication.getInstance().getMykey();
        }
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_cartlist, paramas, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object != null && object.has("code")) {
                                int code = object.getInt("code");

                                carList.clear();
                                JSONObject jsonObject = object.getJSONObject("datas");
                                JSONArray jsonArray = jsonObject.getJSONArray("cart_list");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    Goods g = new Goods();
//                                    g.setId(object1.optInt("id"));
                                    g.setGoods_num(object1.optInt("goods_num"));
                                    g.setTitle(object1.optString("goods_name"));
                                    g.setMoney(object1.optDouble("goods_price"));
                                    g.setPicarr(object1.optString("goods_image_url"));
                                    g.setCart_id(object1.optString("cart_id"));
                                    g.setStore_id(object1.optString("store_id"));
                                    g.setGoods_id(object1.optString("goods_id"));
//                                    g.setDismoney(object1.optDouble("dismoney"));
//                                    g.setSid(object1.optInt("sid"));
                                    carList.add(g);
                                }
                                if (carList.size() == 0) {
                                    textView.setVisibility(View.GONE);
                                } else {
                                    textView.setVisibility(View.VISIBLE);
                                    textView.setText(carList.size() + "");
                                }

                            } else {
                                Log.i("zjz", "cart解析失败");
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
}
