package com.abcs.haiwaigou.local.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名称：com.abcs.haiwaigou.local.beans
 * 作者：zds
 * 时间：2017/5/9 17:51
 */

public class DataEntry {
    /**
     * area_id : 10
     * area_cn_name : 亚洲
     * citys : [{"country_cn_name":"日本","is_click":0,"cate_name":"东京","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img503.jpg","city_id":63},{"country_cn_name":"日本","is_click":0,"cate_name":"京都","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img504.jpg","city_id":65},{"country_cn_name":"日本","is_click":0,"cate_name":"大阪","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img505.jpg","city_id":8603},{"country_cn_name":"泰国","is_click":0,"cate_name":"曼谷","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img947.jpg","city_id":55},{"country_cn_name":"泰国","is_click":0,"cate_name":"清迈","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img948.jpg","city_id":54},{"country_cn_name":"泰国","is_click":0,"cate_name":"普吉","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img949.jpg","city_id":8845},{"country_cn_name":"泰国","is_click":0,"cate_name":"芭堤雅","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img950.jpg","city_id":8804}]
     */

    @SerializedName("area_id")
    public int areaId;
    @SerializedName("area_cn_name")
    public String areaCnName;
    @SerializedName("citys")
    public List<CitysEntry> citys;

    public static class CitysEntry {
        /**
         * country_cn_name : 日本
         * is_click : 0
         * cate_name : 东京
         * oss_url : http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img503.jpg
         * city_id : 63
         */

        @SerializedName("country_cn_name")
        public String countryCnName;
        @SerializedName("is_click")
        public int isClick;
        @SerializedName("cate_name")
        public String cateName;
        @SerializedName("oss_url")
        public String ossUrl;
        @SerializedName("city_id")
        public int cityId;
    }
}
