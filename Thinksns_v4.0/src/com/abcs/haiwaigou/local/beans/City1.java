package com.abcs.haiwaigou.local.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class City1 implements Serializable {
    /**
     * body : {"data":[{"area_id":"10","area_cn_name":"亚洲","citys":[{"cate_name":"曼谷","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img947.jpg","city_id":55},{"cate_name":"清迈","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img948.jpg","city_id":54},{"cate_name":"普吉岛","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img949.jpg","city_id":8845},{"cate_name":"芭堤雅","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img950.jpg","city_id":8804}]}]}
     * result : 1
     * timestamp : 1479811365254
     * version : 1.0
     * transactionid : null
     */

    @SerializedName("body")
    public BodyBean body;
    @SerializedName("result")
    public int result;
    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("version")
    public String version;
    @SerializedName("transactionid")
    public Object transactionid;

    public static class BodyBean {
        @SerializedName("data")
        public List<DataBean> data;

        public static class DataBean {
            /**
             * area_id : 10
             * area_cn_name : 亚洲
             * citys : [{"cate_name":"曼谷","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img947.jpg","city_id":55},{"cate_name":"清迈","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img948.jpg","city_id":54},{"cate_name":"普吉岛","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img949.jpg","city_id":8845},{"cate_name":"芭堤雅","oss_url":"http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img950.jpg","city_id":8804}]
             */

            @SerializedName("area_id")
            public String areaId;
            @SerializedName("area_cn_name")
            public String areaCnName;
            @SerializedName("citys")
            public List<CitysBean> citys;

            public static class CitysBean {
                /**
                 * cate_name : 曼谷
                 * oss_url : http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb/qyer_city_img947.jpg
                 * city_id : 55
                 */

                @SerializedName("cate_name")
                public String cateName;
                @SerializedName("oss_url")
                public String ossUrl;
                @SerializedName("city_id")
                public int cityId;
            }
        }
    }


}
