package com.abcs.haiwaigou.local.model;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.abcs.haiwaigou.local.beans.HireFind;
import com.abcs.haiwaigou.local.beans.LNews;
import com.abcs.haiwaigou.local.beans.Msg;
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
 * Created by zjz on 2016/9/6.
 */
public class HireAndFindModel {
    private Activity activity;
    private Handler handler=new Handler();
    private ACache aCache;
    private ArrayList<HireFind>hireFindList=new ArrayList<HireFind>();
    private ArrayList<LNews>newsList=new ArrayList<LNews>();
    private ArrayList<Msg>msgList=new ArrayList<Msg>();

    public HireAndFindModel( LoadStateInterFace loadStateInterFace) {
        this.loadStateInterFace=loadStateInterFace;
    }

    public HireAndFindModel(Activity activity, LoadStateInterFace loadStateInterFace) {
        this.loadStateInterFace=loadStateInterFace;
        this.aCache =ACache.get(activity);
    }

    public ArrayList<HireFind>getHireFindList(){
        return hireFindList;
    }

    public ArrayList<LNews>getNewsList(){
        return newsList;
    }

    public ArrayList<Msg>getMsgList(){
        return msgList;
    }

    public void initMyPublish(String userId,int page,String type, final boolean isLoadMore){
        Log.i("zjz","hire_url="+TLUrl.getInstance().URL_local_get_mypublish+"?"+"&page="+page+"&pageSize=10"+"&userId="+userId+"&isIssue="+type);
        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_mypublish,  "page=" + page + "&pageSize=10" + "&userId="+userId+"&isIssue="+type, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i("zjz", "hire_msg=" + msg);
                            JSONObject mainObj = new JSONObject(msg);
                            initHireAndFindList(mainObj, isLoadMore);
                        } catch (JSONException e) {
                            loadStateInterFace.LoadFailed("请求失败！请重试！");
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /**
     * 删除我的发布
     * 接口:  https://japi.tuling.me/hrq/conListDetail/delMyPublish?id=60499
     * */
    public void initDelMyPublish(String id){
        HttpRequest.sendGet(TLUrl.getInstance().URL_local_delete_mypublish, "id=" + id, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i("xuke", "del_msg=" + msg);
                            JSONObject deleteMsg = new JSONObject(msg);
                            String status = deleteMsg.getString("status"); //1
                            String delMsg = deleteMsg.getString("msg"); //操作成功
                            Log.i("xuke","deleteMyPublish: status:"+status+",msg:"+delMsg);
                        } catch (JSONException e) {
                            loadStateInterFace.LoadFailed("请求失败！请重试！");
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void initHireAndFindDatas(String cityId,String menuId,int page, final boolean isLoadMore) {
//       https://japi.tuling.me/hrq/conListDetail/getConListByConType.json?menuId=1020&cityId=41&version=v2.0&page=1&pageSize=20
        Log.i("zjz","hire_url="+TLUrl.getInstance().URL_local_get_menuList+"?"+"&cityId="+cityId+"&page="+page+"&pageSize=10"+"&menuId="+menuId);
        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_menuList, "cityId=" + cityId + "&page=" + page + "&pageSize=10" + "&menuId=" + menuId, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i("zjz", "hire_msg=" + msg);
                            JSONObject mainObj = new JSONObject(msg);
                            initHireAndFindList(mainObj, isLoadMore);
                        } catch (JSONException e) {
                            loadStateInterFace.LoadFailed("请求失败！请重试！");
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void initHireAndFindList(JSONObject mainObj,boolean isLoadMore) throws JSONException {
        if(mainObj.optString("status").equals("1")){
            JSONArray msgArray=mainObj.optJSONArray("msg");
            if(msgArray!=null&&msgArray.length()!=0){
                if(msgArray.length()>0){
                    loadStateInterFace.isMore(true);
                }else {
                    loadStateInterFace.isMore(false);
                }
                if(!isLoadMore){
                    hireFindList.clear();
                    for (int i=0;i<msgArray.length();i++){
                        JSONObject itemObj=msgArray.getJSONObject(i);
                        HireFind hireFind=new HireFind();
                        hireFind.setIds(i);
                        hireFind.setId(itemObj.optString("id"));
                        hireFind.setCreateTime(itemObj.optLong("createTime"));
                        hireFind.setPubTime(itemObj.optLong("pubTime", 0));
                        hireFind.setTitle(itemObj.optString("title"));
                        hireFind.setCountryEnName(itemObj.optString("countryEnName"));
                        hireFind.setCityId(itemObj.optString("cityId"));
                        hireFind.setContactMan(itemObj.optString("contactMan"));
                        hireFind.setListTypeId(itemObj.optString("listTypeId"));
                        hireFind.setIcon(itemObj.optString("imgUrl"));
                        hireFind.setViews(itemObj.optString("views"));
                        hireFind.setIsIssue(itemObj.optString("isIssue"));
                        hireFind.setParentId(itemObj.optString("parentId"));
                        hireFindList.add(hireFind);
                    }
                }else {
                    int currentCount=hireFindList.size();
                    for (int i=0;i<msgArray.length();i++){
                        JSONObject itemObj=msgArray.getJSONObject(i);
                        HireFind hireFind=new HireFind();
                        hireFind.setIds(currentCount+i);
                        hireFind.setId(itemObj.optString("id"));
                        hireFind.setCreateTime(itemObj.optLong("createTime"));
                        hireFind.setPubTime(itemObj.optLong("pubTime", 0));
                        hireFind.setTitle(itemObj.optString("title"));
                        hireFind.setCountryEnName(itemObj.optString("countryEnName"));
                        hireFind.setCityId(itemObj.optString("cityId"));
                        hireFind.setContactMan(itemObj.optString("contactMan"));
                        hireFind.setListTypeId(itemObj.optString("listTypeId"));
                        hireFind.setIcon(itemObj.optString("imgUrl"));
                        hireFind.setViews(itemObj.optString("views"));
                        hireFind.setIsIssue(itemObj.optString("isIssue"));
                        hireFind.setParentId(itemObj.optString("parentId"));
                        hireFindList.add(hireFind);
                    }
                }

                loadStateInterFace.LoadSuccess("");
            }else {
                loadStateInterFace.isMore(false);
                loadStateInterFace.LoadFailed(MyString.NODATA);
            }
        }else {
            loadStateInterFace.LoadFailed(MyString.LOADFAILED);
        }
    }


    public void initMoreNews(String cityId, int page, final boolean isLoadMore){
        Log.i("zjz","news_url="+TLUrl.getInstance().URL_local_get_more_news+"?"+"&page="+page+"&pageSize=15"+"&cityId="+cityId);
        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_more_news,  "page=" + page + "&pageSize=15" + "&cityId="+cityId, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i("zjz", "news_msg=" + msg);
                            JSONObject mainObj = new JSONObject(msg);
                            initMoreNewsList(mainObj, isLoadMore);
                        } catch (JSONException e) {
                            loadStateInterFace.LoadFailed("请求失败！请重试！");
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void initMoreNewsList(JSONObject mainObj, boolean isLoadMore) throws JSONException {
        if(mainObj.optString("status").equals("1")){
            JSONArray msgArray=mainObj.optJSONArray("msg");
            if(msgArray!=null&&msgArray.length()!=0){
                if(msgArray.length()==15){
                    loadStateInterFace.isMore(true);
                }else {
                    loadStateInterFace.isMore(false);
                }
                if(!isLoadMore){
                    newsList.clear();
                    for (int i=0;i<msgArray.length();i++){
                        JSONObject itemObj=msgArray.getJSONObject(i);
                        LNews lNews=new LNews();
                        lNews.setIds(i);
                        lNews.setId(itemObj.optString("id"));
                        lNews.setTitle(itemObj.optString("title"));
                        lNews.setAuditTime(itemObj.optLong("auditTime"));
                        lNews.setContent(itemObj.optString("content"));
                        lNews.setImage(itemObj.optString("image"));
                        lNews.setFrom(itemObj.optString("from"));
                        newsList.add(lNews);
                    }
                }else {
                    int currentCount=newsList.size();
                    for (int i=0;i<msgArray.length();i++){
                        JSONObject itemObj=msgArray.getJSONObject(i);
                        LNews lNews=new LNews();
                        lNews.setIds(currentCount+i);
                        lNews.setId(itemObj.optString("id"));
                        lNews.setTitle(itemObj.optString("title"));
                        lNews.setAuditTime(itemObj.optLong("auditTime"));
                        lNews.setContent(itemObj.optString("content"));
                        lNews.setImage(itemObj.optString("image"));
                        lNews.setFrom(itemObj.optString("from"));
                        newsList.add(lNews);
                    }
                }

                loadStateInterFace.LoadSuccess("");
            }else {
                loadStateInterFace.isMore(false);
                loadStateInterFace.LoadFailed(MyString.NODATA);
            }
        }else {
            loadStateInterFace.LoadFailed(MyString.LOADFAILED);
        }
    }

    public void initMoreMsg(String cityId, int page, final boolean isLoadMore){
        Log.i("zjz","msg_url="+TLUrl.getInstance().URL_local_get_more_msg+"?"+"&page="+page+"&pageSize=15"+"&cityId="+cityId);
        HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_more_msg,  "page=" + page + "&pageSize=15" + "&cityId="+cityId, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i("zjz", "msg_msg=" + msg);
                            JSONObject mainObj = new JSONObject(msg);
                            initMoreMsgList(mainObj, isLoadMore);
                        } catch (JSONException e) {
                            loadStateInterFace.LoadFailed("请求失败！请重试！");
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    private void initMoreMsgList(JSONObject mainObj, boolean isLoadMore) throws JSONException {
        if(mainObj.optString("status").equals("1")){
            JSONArray msgArray=mainObj.optJSONArray("msg");
            if(msgArray!=null&&msgArray.length()!=0){
                if(msgArray.length()>0){
                    loadStateInterFace.isMore(true);
                }else {
                    loadStateInterFace.isMore(false);
                }
                if(!isLoadMore){
                    msgList.clear();
                    for (int i=0;i<msgArray.length();i++){
                        JSONObject itemObj=msgArray.getJSONObject(i);
                        Msg msg=new Msg();
                        msg.setIds(i);
                        msg.setId(itemObj.optString("id"));
                        msg.setCityName(itemObj.optString("cityName"));
                        msg.setCountry(itemObj.optString("country"));
                        msg.setTitle(itemObj.optString("title"));
                        msg.setTypeName(itemObj.optString("typeName"));
                        msg.setCreateTime(itemObj.optLong("createTime"));
                        msg.setImgUrl(itemObj.optString("icon"));
                        msg.setListTypeId(itemObj.optString("listTypeId"));
                        msgList.add(msg);
                    }
                }else {
                    int currentCount=msgList.size();
                    for (int i=0;i<msgArray.length();i++){
                        JSONObject itemObj=msgArray.getJSONObject(i);
                        Msg msg=new Msg();
                        msg.setIds(currentCount+i);
                        msg.setId(itemObj.optString("id"));
                        msg.setCityName(itemObj.optString("cityName"));
                        msg.setCountry(itemObj.optString("country"));
                        msg.setTitle(itemObj.optString("title"));
                        msg.setTypeName(itemObj.optString("typeName"));
                        msg.setCreateTime(itemObj.optLong("createTime"));
                        msg.setImgUrl(itemObj.optString("icon"));
                        msg.setListTypeId(itemObj.optString("listTypeId"));
                        msgList.add(msg);
                    }
                }

                loadStateInterFace.LoadSuccess("");
            }else {
                loadStateInterFace.isMore(false);
                loadStateInterFace.LoadFailed(MyString.NODATA);
            }
        }else {
            loadStateInterFace.LoadFailed(MyString.LOADFAILED);
        }
    }


    LoadStateInterFace loadStateInterFace;
    public interface LoadStateInterFace{
        void LoadSuccess(String msg);
        void LoadFailed(String msg);
        void isMore(boolean state);
    }

}
