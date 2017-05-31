package com.abcs.huaqiaobang.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class User {
    // {"msg":{"allEarnings":0,"createDate":1445597662001,"earningsYesterday":0,"investCount":0,"totalAssets":0,"totalInvest":0,"uid":"10001"},"status":1}
    // 总收益 创建时间 昨日收益 投资产品个数 总资产 总投入
    protected String id;
    protected String bindId;
    protected long score;// 总共
    protected String userName = "qh";
    protected String userPwd;
    protected String nickName;//
    protected String avatarUrl;// 头像
    protected int level;
    protected double levelNeed;
    protected double levelNeedTotal;
    protected int integral;
    protected double levelTotal;
    protected String levelUnit;
    protected String sex;
    protected int diamond;
    protected String mood;
    protected String area;// 区域
    protected String from;// 来源
    protected String email;
    protected String phone;
    protected boolean isInvailPhone;
    protected JSONObject last;
    protected boolean isInvail;
    protected float allEarnings;// 总收益
    protected float earningsYesterday;// 昨日收益
    protected int investCount;// 投资产品个数
    protected float totalAssets;// 总资产
    protected float totalInvest;// 总投入
    protected int isbindidenity = -1;// 是否身份验证 -1未获取。0未验证 1验证
    protected JSONArray banks;// 绑定的银行卡
    protected int invest;// 单个产品投入

    // 云之讯
    protected String userid;
    protected String clientNumber;
    protected String SSID;
    private boolean isadmin = false;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String sSID) {
        SSID = sSID;
    }

    public double getLevelNeedTotal() {
        return levelNeedTotal;
    }


    public void setLevelNeedTotal(double levelNeedTotal) {
        this.levelNeedTotal = levelNeedTotal;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public double getLevelNeed() {
        return levelNeed;
    }

    public void setLevelNeed(double levelNeed) {
        this.levelNeed = levelNeed;
    }

    public double getLevelTotal() {
        return levelTotal;
    }

    public void setLevelTotal(double levelTotal) {
        this.levelTotal = levelTotal;
    }

    public String getLevelUnit() {
        return levelUnit;
    }

    public void setLevelUnit(String levelUnit) {
        this.levelUnit = levelUnit;
    }

    public boolean isIsadmin() {
        return isadmin;
    }

    public void setIsadmin(boolean isadmin) {
        this.isadmin = isadmin;
    }

    public int getIsbindidenity() {
        return isbindidenity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isInvail() {
        return isInvail;
    }

    public boolean isInvailPhone() {
        return isInvailPhone;
    }

    public void setIsInvailPhone(boolean isInvailPhone) {
        this.isInvailPhone = isInvailPhone;
    }

    public void setInvail(boolean isInvail) {
        this.isInvail = isInvail;
    }

    protected Map<String, Object> map = new HashMap<String, Object>();// 更多的属性

    public User(String id, long score, String userName, String userPwd,
                String nickName, String avatarUrl, int level, String sex,
                int diamond, String mood) {
        this.id = id;
        this.score = score;
        this.userName = userName;
        this.userPwd = userPwd;
        this.nickName = nickName;
        this.avatarUrl = avatarUrl;
        this.level = level;
        this.sex = sex;
        this.diamond = diamond;
        this.mood = mood;
    }

    public User() {
    }

    public String getId() {
        if (id == null) {
            return "";
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public JSONObject getLast() {
        return last;
    }

    public void setLast(JSONObject last) {
        this.last = last;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public int isIsbindidenity() {
        return isbindidenity;
    }

    public void setIsbindidenity(int isbindidenity) {
        this.isbindidenity = isbindidenity;
    }

    public JSONArray getBanks() {
        return banks;
    }

    public void setBanks(JSONArray banks) {
        this.banks = banks;
    }

    public float getAllEarnings() {
        return allEarnings;
    }

    public void setAllEarnings(float allEarnings) {
        this.allEarnings = allEarnings;
    }

    public float getEarningsYesterday() {
        return earningsYesterday;
    }

    public void setEarningsYesterday(float earningsYesterday) {
        this.earningsYesterday = earningsYesterday;
    }

    public int getInvestCount() {
        return investCount;
    }

    public void setInvestCount(int investCount) {
        this.investCount = investCount;
    }

    public float getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(float totalAssets) {
        this.totalAssets = totalAssets;
    }

    public float getTotalInvest() {
        return totalInvest;
    }

    public void setTotalInvest(float totalInvest) {
        this.totalInvest = totalInvest;
    }

    public int getInvest() {
        return invest;
    }

    public void setInvest(int invest) {
        this.invest = invest;
    }

}
