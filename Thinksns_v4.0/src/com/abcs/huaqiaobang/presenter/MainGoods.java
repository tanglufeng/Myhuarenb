package com.abcs.huaqiaobang.presenter;

import android.content.Context;

import com.abcs.haiwaigou.model.Goods;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.tljr.data.Constent;
import com.abcs.huaqiaobang.tljr.news.bean.News;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhou on 2016/4/15.
 */
public class MainGoods {

//    private static final long CACHE_TIME = 1000;
    private static final long CACHE_TIME = 12 * 60 * 60 * 1000;
    LoadInterface mainGoodsInterface;

    public static Context context;

    static {

        context = MyApplication.getInstance();
        requestQueue = Volley.newRequestQueue(MyApplication.getInstance());
    }

    public MainGoods(LoadInterface mainGoodsInterface) {
        this.mainGoodsInterface = mainGoodsInterface;
    }

    private static RequestQueue requestQueue;

    public static boolean isCacheDataFailure(String cachefile) {
        boolean failure = false;
        File data = context.getFileStreamPath(cachefile);
        if (data.exists()
                && (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
            failure = true;
        else if (!data.exists())
            failure = true;
        return failure;
    }

    public void loadData(boolean refresh) {

        if (!isCacheDataFailure("goods.txt")&&!refresh) {
            try {

                FileInputStream fileInputStream = context.openFileInput("goods.txt");

                BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
                String str = null;
                StringBuffer stringBuffer = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                    stringBuffer.append("\n");
                }
                String json = stringBuffer.toString();

                getData(new JSONObject(json));
                br.close();
                fileInputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, TLUrl.getInstance().URL_hwg_good_remai, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        try {
                            FileOutputStream fileOutputStream = context.openFileOutput("goods.txt", Context.MODE_PRIVATE);
                            BufferedWriter rw = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
                            rw.write(response.toString());
                            rw.close();
                            fileOutputStream.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        getData(response);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

                    , new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }

            );
            requestQueue.add(jr);
        }

        if (!isCacheDataFailure("news.txt")&&!refresh) {

            try {

                FileInputStream fileInputStream = context.openFileInput("news.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
                String str = null;
                StringBuffer stringBuffer = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                    stringBuffer.append("\n");
                }
                String json = stringBuffer.toString();

                getNews(new JSONObject(json));
                br.close();
                fileInputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {

            String id = Constent.preference.getString("news_p_id", "0");
            if (MyApplication.getInstance().self != null) {
                id = MyApplication.getInstance().self.getId();
                Constent.preference.edit().putString("news_p_id", id).commit();

            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    TLUrl.getInstance().URL_new + "/api/main/hn?platform=2&uid=" + id + "&digest=35",
                    null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    // TODO Auto-generated method stub

                    //   Log.i("tga", "reCmd====" + cmdInfo.toString());
                    try {
                        try {
                            FileOutputStream fileOutputStream = context.openFileOutput("news.txt", Context.MODE_PRIVATE);
                            BufferedWriter rw = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
                            rw.write(response.toString());
                            rw.close();
                            fileOutputStream.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        getNews(response);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });


            requestQueue.add(jsonObjectRequest);
        }

    }

    private void getNews(JSONObject response) throws JSONException {
        List<News> news = new ArrayList<News>();
        JSONObject obj = response.getJSONObject("joData");
        JSONArray array = obj.getJSONArray("data");

        for (int i = 0; i < array.length(); i++) {
            News news_temp = new News();

            String title = array.getJSONObject(i)
                    .optString("title");
            news_temp.setTitle(title);
            String url = array.getJSONObject(i)
                    .optString("purl");
            news_temp.setpUrl(url);
            news_temp.setDigest(array.getJSONObject(i).optString("digest"));
            news_temp.setTime(array.getJSONObject(i)
                    .optLong("time"));
            String source = array.getJSONObject(i)
                    .optString("source");
            news_temp.setSource(source);
            String surl = array.getJSONObject(i)
                    .optString("surl");
            news_temp.setSurl(surl);
            news_temp.setId(array.getJSONObject(i)
                    .optString("id"));
            news_temp.setContent(array.getJSONObject(i)
                    .optString("content"));
            news_temp.setSpecial(array.getJSONObject(i).optString("species"));

            news_temp.setHaveCai(array.getJSONObject(i)
                    .optBoolean("hasOppose", false));
            news_temp.setHaveZan(array.getJSONObject(i)
                    .optBoolean("hasPraise", false));
            news_temp.setHaveSee(array.getJSONObject(i)
                    .optBoolean("read", false));

            news.add(news_temp);
        }
        mainGoodsInterface.loadNewsData(news);
    }

    private void getData(JSONObject response) throws JSONException {
        ArrayList<Goods> mGoods = new ArrayList<>();
        if (response.getInt("code") == 200) {
            JSONObject datasObj = response.getJSONObject("datas");
            Iterator<String> datas = response.getJSONObject("datas").keys();
            while (datas.hasNext()) {
                String next = datas.next();
                JSONObject recommend = datasObj.getJSONObject(next).getJSONObject("recommend");
                if ("热卖商品疯狂抢购".indexOf(recommend.getString("name")) > -1) {
                    JSONObject goods_list = datasObj.getJSONObject(next).getJSONObject("goods_list");
                    Iterator<String> keys = goods_list.keys();
                    while (keys.hasNext()) {
                        Goods goods = new Goods();
                        JSONObject goods_listObj = goods_list.getJSONObject(keys.next());
                        goods.setGoods_id(goods_listObj.optString("goods_id"));
                        goods.setMoney(goods_listObj.optDouble("market_price",0));
                        goods.setTitle(goods_listObj.optString("goods_name"));
                        goods.setPromote_money(goods_listObj.optDouble("goods_price",0));
                        goods.setGoods_url(TLUrl.getInstance().URL_hwg_remai + goods_listObj.optString("goods_pic"));
                        mGoods.add(goods);
                    }
                }
            }
            mainGoodsInterface.loadData(mGoods);
        }
    }


}
