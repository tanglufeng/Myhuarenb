package com.abcs.haiwaigou.local.beans;

import com.google.gson.annotations.SerializedName;

/**
 * 项目名称：com.abcs.haiwaigou.local.beans
 * 作者：zds
 * 时间：2017/3/21 12:01
 */

public class ActivitysBean {
    /**
     * jbf : 维也纳华人乒乓球协会
     * id : 1
     * title : 维也纳华人乒乓球擂王争霸赛
     * tag : 运动,娱乐
     * img : http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb_abc_img/%E6%9C%AA%E6%A0%87%E9%A2%98-121489716004608.png
     * activity_time : ""
     * is_click : 0
     * date : 1489680000
     * ads : 古斯韦克
     * url :
     */

    @SerializedName("jbf")
    public String jbf;
    @SerializedName("id")
    public int id;
    @SerializedName("is_click")
    public int is_click;
    @SerializedName("title")
    public String title;
    @SerializedName("tag")
    public String tag;
    @SerializedName("img")
    public String img;
    @SerializedName("activity_time")
    public String activity_time;
    @SerializedName("date")
    public long date;
    @SerializedName("ads")
    public String ads;
    @SerializedName("url")
    public String url;
}
