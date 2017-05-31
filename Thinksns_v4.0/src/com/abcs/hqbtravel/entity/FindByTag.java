package com.abcs.hqbtravel.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/10/28.
 */
public class FindByTag {


    @SerializedName("body")
    public BodyEntity body;
    @SerializedName("result")
    public int result;
    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("version")
    public String version;
    @SerializedName("transactionid")
    public String transactionid;

    public static class BodyEntity {
        @SerializedName("next")
        public int next;
        @SerializedName("pageCount")
        public int pageCount;
        @SerializedName("list")
        public List<ListEntity> list;

        public static class ListEntity {
            @SerializedName("english_name")
            public String english_name;
            @SerializedName("id")
            public String id;
            @SerializedName("detail_id")
            public String detailId;
            @SerializedName("city_id")
            public String cityId;
            @SerializedName("chinese_name")
            public String chineseName;
            @SerializedName("grade")
            public double grade;
            @SerializedName("photo")
            public String photo;
            @SerializedName("been_number")
            public String beenNumber;
            @SerializedName("rank")
            public String rank;
            @SerializedName("cate_name")
            public String cateName;
            @SerializedName("category_id")
            public String categoryId;
            @SerializedName("create_time")
            public String createTime;
            @SerializedName("oss_url")
            public String ossUrl;
            @SerializedName("lng")
            public String lng;
            @SerializedName("lat")
            public String lat;
            @SerializedName("type")   // 1 必玩  2  必吃  3 必买
            public int type;
            @SerializedName("commentCount")
            public int commentCount;
        }
    }
}
