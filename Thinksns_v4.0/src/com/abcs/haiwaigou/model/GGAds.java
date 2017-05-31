package com.abcs.haiwaigou.model;

import com.google.gson.annotations.SerializedName;

/**
 * 项目名称：com.abcs.haiwaigou.model
 * 作者：zds
 * 时间：2017/5/19 15:56
 */

public class GGAds {

    /**
     * img : https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png
     * url : https://www.baidu.com/
     * is_jump : 0
     */

    @SerializedName("img")
    public String img;
    @SerializedName("url")
    public String url;
    @SerializedName("is_jump")
    public int is_jump;

    public GGAds() {
    }

    public GGAds(String img, String url) {
        this.img = img;
        this.url = url;
    }
}
