package com.abcs.haiwaigou.local.beans;/**
 * Created by Administrator on 2017/1/12.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User: zds
 * Date: 2017-01-12
 * Time: 18:18
 * FIXME
 */
public class HWGWanLe {


    /**
     * status : 1
     * city_list : [{"city_id":"55","cate_name":"曼谷","catename_en":"Bangkok"},{"city_id":"54","cate_name":"清迈","catename_en":"Chiang Mai"},{"city_id":"8845","cate_name":"普吉","catename_en":"Phuket Island"},{"city_id":"8804","cate_name":"芭堤雅","catename_en":"Pattaya"}]
     */

    @SerializedName("status")
    public int status;
    @SerializedName("city_list")
    public List<CityListBean> cityList;

    public static class CityListBean {
        /**
         * city_id : 55
         * cate_name : 曼谷
         * catename_en : Bangkok
         */

        @SerializedName("city_id")
        public String cityId;
        @SerializedName("cate_name")
        public String cateName;
        @SerializedName("catename_en")
        public String catenameEn;
        @SerializedName("goods_count")
        public int goods_count;
    }
}
