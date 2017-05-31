package com.abcs.haiwaigou.local.beans;

import com.google.gson.annotations.SerializedName;

/**
 * 项目名称：com.abcs.haiwaigou.local.beans
 * 作者：zds
 * 时间：2017/5/9 17:51
 */

public class HotCityEntry {

    /**
     * city_id : 41
     * catename_en : 维也纳
     * country_name : 维也纳
     * cate_name : 维也纳
     * oss_url : http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img1562.jpg
     * national_flag : http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb_abc_img/%E5%A5%A5%E5%9C%B0%E5%88%A9%403x1494315408803.png
     */

    @SerializedName("city_id")
    public int cityId;
    @SerializedName("cate_name")
    public String cateName;
    @SerializedName("catename_en")
    public String catename_en;
    @SerializedName("country_name")
    public String country_name;
    @SerializedName("oss_url")
    public String ossUrl;
    @SerializedName("national_flag")
    public String nationalFlag;
}
