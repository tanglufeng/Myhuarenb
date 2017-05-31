package com.abcs.haiwaigou.local.model;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.abcs.haiwaigou.local.beans.News;
import com.abcs.haiwaigou.local.interfacer.LoadStateInterface;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zjz on 2016/8/29.
 */
public class NewsModel {


    Activity activity;
    private String newestId;
    private String newestTime;
    private String lastId;
    private String lastTime;
    LoadStateInterface loadStateInterface;
    private Handler handler = new Handler();
    private ArrayList<News> newsList = new ArrayList<News>();
    private ACache aCache;

    public ArrayList<News> getNewsList() {
        return newsList;
    }


    public NewsModel(Activity activity, LoadStateInterface loadStateInterface) {
        this.loadStateInterface = loadStateInterface;
        this.activity = activity;
        this.aCache = ACache.get(activity);

    }

    public void initDatas(String cityId, boolean isMore) {
        if (isMore) {
            if (lastTime != null && lastId != null)
                initMoreNewsDatas(cityId, "1", lastTime, lastId);
        } else {
            initNewestDatas(cityId, "0");
        }
    }

    public void initNewestDatas(String cityId, String type) {
        Log.i("zjz", "wxnews_url=" + TLUrl.getInstance().URL_local_get_more_wx_news + "?cityId=" + cityId + "&type=" + type);
        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_more_wx_news, "cityId=" + cityId + "&type=" + type, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zjz", "wxnews_msg=" + msg);
                        try {
                            JSONObject mainObj = new JSONObject(msg);
                            initNewsList(mainObj,false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    public void initMoreNewsDatas(String cityId, String type, String time, String id) {
        Log.i("zjz","wxnews_more_url="+TLUrl.getInstance().URL_local_get_more_wx_news+ "?cityId=" + cityId + "&type=" + type + "&time=" + time + "&id=" + id);
        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_more_wx_news, "cityId=" + cityId + "&type=" + type + "&time=" + time + "&id=" + id, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zjz", "wxnews_more_msg=" + msg);
                        try {
                            JSONObject mainObj = new JSONObject(msg);
                            initNewsList(mainObj,true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    private void initNewsList(JSONObject mainObj,boolean isMore) throws JSONException {
        if (mainObj.optString("status").equals("1")) {
            JSONObject joObj = mainObj.optJSONObject("joData");
            if (joObj != null) {
                if(!isMore){
                    newestId = joObj.optString("newestId");
                    newestTime = joObj.optString("newestTime");
                }
                lastId = joObj.optString("lastId");
                lastTime = joObj.optString("lastTime");
                JSONArray newsArray = joObj.optJSONArray("news");
                if (newsArray != null&&newsArray.length()!=0) {
                    if(newsArray.length()!=20){
                        loadStateInterface.isLoadMore(false);
                    }else {
                        loadStateInterface.isLoadMore(true);
                    }
                    if(isMore){
                        int currentCount=newsList.size();
                        for (int i = 0; i < newsArray.length(); i++) {
                            JSONObject newsObj = newsArray.getJSONObject(i);
                            News news = new News();
                            news.setIds(currentCount+i);
                            news.setId(newsObj.optString("id"));
                            news.setTitle(newsObj.optString("title"));
                            news.setPurl(newsObj.optString("purl"));
                            news.setTime(newsObj.optLong("time"));
                            news.setSource(newsObj.optString("source"));
                            news.setSpecies(newsObj.optString("species"));
                            news.setRead(newsObj.optBoolean("read"));
                            newsList.add(news);
                        }
                    }else {
                        newsList.clear();
                        for (int i = 0; i < newsArray.length(); i++) {
                            JSONObject newsObj = newsArray.getJSONObject(i);
                            News news = new News();
                            news.setIds(i);
                            news.setId(newsObj.optString("id"));
                            news.setTitle(newsObj.optString("title"));
                            news.setPurl(newsObj.optString("purl"));
                            news.setTime(newsObj.optLong("time"));
                            news.setSource(newsObj.optString("source"));
                            news.setSpecies(newsObj.optString("species"));
                            news.setRead(newsObj.optBoolean("read"));
                            newsList.add(news);
                        }
                    }
                    loadStateInterface.LoadSuccess("");
                }else {
                    loadStateInterface.LoadFailed(MyString.NODATA);
                    loadStateInterface.isLoadMore(false);
                }
            }else {
                loadStateInterface.LoadFailed(MyString.LOADFAILED);
            }
        }
    }

}
