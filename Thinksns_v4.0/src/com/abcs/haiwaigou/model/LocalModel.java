package com.abcs.haiwaigou.model;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.abcs.haiwaigou.local.beans.ActivitysBean;
import com.abcs.haiwaigou.local.beans.Community;
import com.abcs.haiwaigou.local.beans.LNews;
import com.abcs.haiwaigou.local.beans.LocalBean;
import com.abcs.haiwaigou.local.beans.LocalTeDian;
import com.abcs.haiwaigou.local.beans.Menu;
import com.abcs.haiwaigou.local.beans.Msg;
import com.abcs.haiwaigou.local.beans.New;
import com.abcs.haiwaigou.local.beans.NewsBean;
import com.abcs.haiwaigou.local.evenbus.CityChange;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.hqbtravel.entity.HuiYuanTeDian;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.google.gson.Gson;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class LocalModel {
    private String countryName;
    private String cityId;
    private Handler handler=new Handler();
    private ArrayList<LocalBean> localList=new ArrayList<LocalBean>();;
    private ArrayList<Menu> menuList= new ArrayList<Menu>();
    private ArrayList<LocalTeDian> localTeDianList= new ArrayList<LocalTeDian>();
    private static  ArrayList<Msg> msgList=new ArrayList<Msg>();
    private ArrayList<Community> communityList=new ArrayList<Community>();
    private ArrayList<LNews> newsList=new ArrayList<LNews>();
    private ArrayList<PartnersBean> partners=new ArrayList<PartnersBean>();
    private ArrayList<BannersBean> banners=new ArrayList<BannersBean>();
    private ArrayList<GGAds> ads=new ArrayList<GGAds>();
    private ACache aCache;
    private String countryNum="0";
    private String cityNum="0";
    private Activity activity;
    public static String subject;
    public static String lastTime;
    public static String tian_qi_img;
    public static String tian_qi_txt;
    public static String tian_qi_wendu;
    public static String lastId;
    public static int countryId;

    public static String sy_num;
    public static String hq_num;
    public static String hb;
    public static String hl;

    public ArrayList<GGAds> getAds() {
        return ads;
    }

    public ArrayList<BannersBean> getBanners() {
        return banners;
    }

    public ArrayList<PartnersBean> getPartners() {
        return partners;
    }

    private static ArrayList<ActivitysBean> activy  = new ArrayList<ActivitysBean>();
    private static ArrayList<New> newList   = new ArrayList<New>();

    public static ArrayList<ActivitysBean> getActivityList() {
        return activy;
    }

    public static ArrayList<New> getNewList(){
        return newList;
    }

    private static ArrayList<NewsBean> news   = new ArrayList<NewsBean>();
    public static ArrayList<NewsBean> getNews(){
        return news;
    }


    public ArrayList<Menu> getMenuList(){
        return menuList;
    }

    public static ArrayList<Msg> getMsgList(){
        return msgList;
    }

    public ArrayList<Community>getCommunityList(){
        return communityList;
    }

    public ArrayList<LNews>getNewsList(){
        return newsList;
    }
    public String getCountryNum(){
        return countryNum;
    }

    public String getCityNum(){
        return cityNum;
    }

    public LocalModel(Activity activity,LoadStateInterface loadStateInterface) {
        this.loadStateInterface=loadStateInterface;
        this.activity=activity;
        aCache=ACache.get(activity);
    }

    public LocalModel(Activity activity,String cityId,LoadStateInterface loadStateInterface) {
        this.loadStateInterface=loadStateInterface;
        this.activity=activity;
        this.cityId = cityId;
        aCache=ACache.get(activity);
    }


    public void initDatas(final String cityId) {

        if(aCache.getAsJSONObject(TLUrl.getInstance().LOCALFRAGMENT)!=null){
            aCache.remove(TLUrl.getInstance().LOCALFRAGMENT);
        }

        EventBus.getDefault().post(new CityChange(cityId));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                HttpRequest.sendGet(TLUrl.getInstance().URL_local_main_fragment,"cityId=" + cityId+"&version=2", new HttpRevMsg() {
                    @Override
                    public void revMsg(final String msg) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject mainObj = new JSONObject(msg);
                                    Log.i("zjz", "local_msg=" + msg);
                                    if(aCache.getAsJSONObject(TLUrl.getInstance().LOCALFRAGMENT)==null){
                                        aCache.put(TLUrl.getInstance().LOCALFRAGMENT,mainObj, 7 * 24 * 60 * 60);
                                    }

                                    parseDatas(mainObj,cityId);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        },500);
    }

    List<HuiYuanTeDian.VipTdListBean> vipTdList=new ArrayList<>();

    public List<HuiYuanTeDian.VipTdListBean> getVipTdList() {
        return vipTdList;
    }

    private void initTeDian(String cityId){

        Log.i("zds", "initTeDian: cityId"+cityId);

//        http://www.huaqiaobang.com/mobile/index.php?act=vip_td&op=find_vip_td&city_id=55&type=2
        HttpRequest.sendGet(TLUrl.getInstance().getInstance().URL_travel_tedian, "act=vip_td&op=find_vip_td&city_id=" + cityId + "&type=2", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.i("zds", "tedian=" + msg);

                        if (msg == null) {
                            return;
                        }

                        final HuiYuanTeDian tedian=new Gson().fromJson(msg, HuiYuanTeDian.class);

                        if(tedian!=null){

                            if(tedian.state==1){

                                if(tedian.vipTdList!=null&&tedian.vipTdList.size()>0){
                                    vipTdList.clear();
                                    vipTdList.addAll(vipTdList);
                                }
                            }
                        }
              /*          JSONObject mainObj = null;
                        try {
                            mainObj = new JSONObject(msg);
                            if(mainObj.optInt("state")==1){
                                JSONArray vip_td_list = mainObj.optJSONArray("vip_td_list");
                                if (vip_td_list != null) {
                                    initTeDianList(vip_td_list);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }
                });
            }
        });

    }
    public void parseDatas(JSONObject mainObj,String cityId) throws JSONException {
        if (mainObj.optString("status").equals("1")) {
            JSONObject msgObj = mainObj.optJSONObject("msg");

            JSONObject bannerTitle = msgObj.optJSONObject("bannerTitle");

            if(bannerTitle!=null){
                sy_num=bannerTitle.optString("sy_num");
                hq_num=bannerTitle.optString("hq_num");
                hb=bannerTitle.optString("hb");
                hl=bannerTitle.optString("hl");
            }
            tian_qi_img = msgObj.optString("img");
            tian_qi_txt = msgObj.optString("txt");
            tian_qi_wendu = msgObj.optString("temp");

            countryId = msgObj.optInt("countryId");
            lastTime = msgObj.optString("lastTime");
            lastId = msgObj.optString("lastId");
            subject = msgObj.optString("subject");

            JSONObject numObj = msgObj.optJSONObject("onLineNum");
            if(numObj!=null){
                countryNum=numObj.optString("country");
                cityNum=numObj.optString("city");
            }
            JSONArray menuArray = msgObj.optJSONArray("menu");
            if (menuArray != null) {
                initMenuList(menuArray,cityId);
            }
            JSONArray partnersArray = msgObj.optJSONArray("partners");
            if (partnersArray != null) {
                initPateneList(partnersArray);
            }

            JSONArray bannersArray = msgObj.optJSONArray("banners");
            if (bannersArray != null) {
                initBannersList(bannersArray);
            }
            /*新上的广告*/
            JSONArray ads = msgObj.optJSONArray("ads");
            if (ads != null) {
                initGGNewsList(ads);
            }

            JSONArray msgArray = msgObj.optJSONArray("newestInfo");
            if (msgArray != null) {
                initMsgList(msgArray);
            }

            JSONArray newArray = msgObj.optJSONArray("news");
            if (newArray !=null){
                initNewList(newArray);
            }
            JSONArray activitys = msgObj.optJSONArray("activitys");
            if (activitys !=null){
                initActivity(activitys);
            }


            JSONArray communityArray = msgObj.optJSONArray("minDistanceOrg");
            if (communityArray != null) {
                initCommunityList(communityArray);
            }
            JSONArray newsListArray=msgObj.optJSONArray("newsList");
            if(newsListArray!=null){
                initNewsList(newsListArray);
            }
            loadStateInterface.loadSuccess();
        } else {
            loadStateInterface.loadFailed();
        }
    }

    private void initNewsList(JSONArray newsListArray) throws JSONException {
        newsList.clear();
        for(int i=0;i<newsListArray.length();i++){
            JSONObject newsObj=newsListArray.getJSONObject(i);
            LNews lNews=new LNews();
            lNews.setId(newsObj.optString("id"));
            lNews.setTitle(newsObj.optString("title"));
            lNews.setAuditTime(newsObj.optLong("auditTime"));
            lNews.setContent(newsObj.optString("content"));
            lNews.setImage(newsObj.optString("image"));
            lNews.setFrom(newsObj.optString("from"));
            newsList.add(lNews);
        }
    }

    public void initCommunityList(JSONArray communityArray) throws JSONException {
        communityList.clear();
        for (int i=0;i<communityArray.length();i++){
            JSONObject comObj=communityArray.getJSONObject(i);
            Community community=new Community();
            community.setId(comObj.optString("id"));
            community.setIcon(comObj.optString("icon"));
            community.setOrgExplain(comObj.optString("orgExplain"));
            community.setOrgMemberNum(comObj.optInt("orgMemberNum"));
            community.setOrgName(comObj.optString("orgName"));
            communityList.add(community);
        }
    }

    /**
     * 本地头条
     * */
    public void initMsgList(JSONArray msgArray) throws JSONException {
        msgList.clear();
        for (int i=0;i<msgArray.length();i++){
            JSONObject msgObj=msgArray.getJSONObject(i);
            Msg msg=new Msg();
            msg.setId(msgObj.optString("id"));
            msg.setCityName(msgObj.optString("cityName"));
            msg.setCountry(msgObj.optString("country"));
            msg.setTitle(msgObj.optString("title"));
            msg.setTypeName(msgObj.optString("typeName"));
            msg.setCreateTime(msgObj.optLong("createTime"));
            msg.setImgUrl(msgObj.optString("icon"));
            msg.setListTypeId(msgObj.optString("listTypeId"));
            msgList.add(msg);
        }
    }

    /**
     * 热点资讯
     * */
    private void initNewList(JSONArray newArray) throws JSONException {
        newList.clear();
        for (int i=0;i<newArray.length();i++){
            JSONObject newObj = newArray.getJSONObject(i);
            New news = new New();
            news.setId(newObj.optString("id"));
            news.setTime(newObj.optString("time"));
            news.setTitle(newObj.optString("title"));
            news.setPurl(newObj.optString("purl"));
            newList.add(news);
        }
    }
    /**
     * 活动信息
     * */
    private void initActivity(JSONArray activitys) throws JSONException {
        activy.clear();
        for (int i=0;i<activitys.length();i++){
            JSONObject newObj = activitys.getJSONObject(i);
            ActivitysBean bean = new ActivitysBean();
            bean.jbf=newObj.optString("jbf");
            bean.id=newObj.optInt("id");
            bean.title=newObj.optString("title");
            bean.tag=newObj.optString("tag");
            bean.img=newObj.optString("img");
            bean.activity_time=newObj.optString("activity_time");
            bean.is_click=newObj.optInt("is_click");
            bean.date=newObj.optLong("date");
            bean.ads=newObj.optString("ads");
            bean.url=newObj.optString("url");
            activy.add(bean);
        }
    }


    public void initMenuList(JSONArray menuArray,String cityId) throws JSONException {
        menuList.clear();
        for (int i = 0; i < menuArray.length(); i++) {
            JSONObject menuObj = menuArray.getJSONObject(i);
            Menu menu = new Menu();
            menu.setImgUrl(menuObj.optString("imgUrl"));
            menu.setMenuId(menuObj.optString("menuId"));
            menu.setMenuName(menuObj.optString("menuName"));
            menu.setMenuParentId(menuObj.optInt("menuParentId"));
            menu.setIs_show(menuObj.optInt("is_show"));
            menu.setIs_click(menuObj.optInt("is_click"));
            menuList.add(menu);
        }
    }
    public void initTeDianList(JSONArray teDianArray) throws JSONException {
        localTeDianList.clear();
        for (int i = 0; i < teDianArray.length(); i++) {
            JSONObject menuObj = teDianArray.getJSONObject(i);
            LocalTeDian tedian = new LocalTeDian();
            tedian.city_id=menuObj.optString("city_id");
            tedian.goods_id=menuObj.optString("goods_id");
            tedian.id=menuObj.optString("id");
            tedian.img=menuObj.optString("img");
            tedian.intro=menuObj.optString("intro");
            tedian.jump_id=menuObj.optString("jump_id");
            tedian.sort=menuObj.optString("sort");
            tedian.state=menuObj.optString("state");
            tedian.td_cn_name=menuObj.optString("td_cn_name");
            tedian.td_desc=menuObj.optString("td_desc");
            tedian.td_desc_img=menuObj.optString("td_desc_img");
            tedian.td_en_name=menuObj.optString("td_en_name");
            tedian.td_id=menuObj.optString("td_id");
            tedian.type=menuObj.optString("type");
            tedian.td_name=menuObj.optString("td_name");

            localTeDianList.add(tedian);
        }
    }
    public void initPateneList(JSONArray partnersArray) throws JSONException {
        partners.clear();
        for (int i = 0; i < partnersArray.length(); i++) {
            JSONObject menuObj = partnersArray.getJSONObject(i);
            PartnersBean pate = new PartnersBean();
            pate.id=menuObj.optInt("id");
            pate.cityName=menuObj.optString("city_name");
            pate.phone=menuObj.optString("phone");
            pate.fzMan=menuObj.optString("fz_man");
            pate.address=menuObj.optString("address");
            pate.headImg=menuObj.optString("head_img");
            pate.qrCode=menuObj.optString("qr_code");
            pate.intro=menuObj.optString("intro");
            pate.mobile=menuObj.optString("mobile");
            pate.weChat=menuObj.optString("we_chat");
            pate.isSq=menuObj.optInt("is_sq");
            pate.isRz=menuObj.optInt("is_rz");
            partners.add(pate);
        }
    }
    public void initBannersList(JSONArray bannersArray) throws JSONException {
        banners.clear();
        for (int i = 0; i < bannersArray.length(); i++) {
            JSONObject menuObj = bannersArray.getJSONObject(i);
            BannersBean pate = new BannersBean();
            pate.id=menuObj.optInt("id");
            pate.cityId=menuObj.optInt("city_id");
            pate.id=menuObj.optInt("id");
            pate.newsUrl=menuObj.optString("news_url");
            pate.createTime=menuObj.optLong("create_time");
            pate.img=menuObj.optString("img");
            pate.orderId=menuObj.optInt("order_id");
            pate.type=menuObj.optInt("type");
            banners.add(pate);
        }
    }
    public void initGGNewsList(JSONArray bannersArray) throws JSONException {
        ads.clear();
        for (int i = 0; i < bannersArray.length(); i++) {
            JSONObject menuObj = bannersArray.getJSONObject(i);
            GGAds pate = new GGAds();
            pate.img=menuObj.optString("img");
            pate.url=menuObj.optString("url");
            pate.is_jump=menuObj.optInt("is_jump");
            ads.add(pate);
        }
    }



    LoadStateInterface loadStateInterface;
    public interface LoadStateInterface{
        void loadSuccess();
        void loadFailed();
    }

}